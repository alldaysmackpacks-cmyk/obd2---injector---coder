package au.com.fuelcoder

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import au.com.fuelcoder.bt.BtTransport
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var status: TextView
    private var transport: BtTransport? = null
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestBluetoothPermission()
        buildUi()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 36, 28, 24)
        }

        root.addView(TextView(this).apply {
            text = "RANGER INJECTOR CODER"
            textSize = 26f
        })
        root.addView(TextView(this).apply {
            text = "2010 PK 3.0 TDCi • Bosch EDC16C7"
            textSize = 16f
            setPadding(0, 8, 0, 18)
        })

        val devices = Spinner(this)
        root.addView(devices)

        val refresh = Button(this).apply {
            text = "REFRESH BLUETOOTH"
            setOnClickListener { loadPairedDevices(devices) }
        }
        root.addView(refresh)

        val connect = Button(this).apply {
            text = "CONNECT"
            setOnClickListener {
                val item = devices.selectedItem as? DeviceItem
                if (item != null) connectTo(item.device)
            }
        }
        root.addView(connect)

        status = TextView(this).apply {
            text = "Status: Not connected"
            textSize = 16f
            setPadding(0, 14, 0, 14)
        }
        root.addView(status)

        val fields = Array(4) { i ->
            EditText(this).apply {
                hint = "Injector ${i + 1} variation code"
                singleLine = true
            }.also { root.addView(it) }
        }

        val read = Button(this).apply {
            text = "READ"
            setOnClickListener {
                status.text = "READ: Transport connected. Exact injector read service is not yet verified for this ECU."
            }
        }
        root.addView(read)

        val program = Button(this).apply {
            text = "PROGRAM"
            isEnabled = false
            setOnClickListener {
                status.text = "PROGRAM LOCKED: exact ECU injector-write procedure is not verified."
            }
        }
        root.addView(program)

        root.addView(TextView(this).apply {
            text = "PROGRAM is intentionally locked until the exact ECU service/security sequence is verified."
            textSize = 13f
            setPadding(0, 14, 0, 0)
        })

        setContentView(ScrollView(this).apply { addView(root) })
        loadPairedDevices(devices)
    }

    private fun loadPairedDevices(spinner: Spinner) {
        if (!hasBluetoothPermission()) return
        try {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            if (adapter == null) {
                status.text = "Status: Bluetooth unavailable"
                return
            }
            val list = adapter.bondedDevices.sortedBy { it.name ?: it.address }
            val items = list.map { DeviceItem(it, "${it.name ?: "Unknown"} • ${it.address}") }
            spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
            status.text = if (items.isEmpty()) "Status: Pair the MX201 in Android Bluetooth settings first" else "Status: Select the MX201"
        } catch (e: SecurityException) {
            status.text = "Status: Bluetooth permission required"
        }
    }

    private fun connectTo(device: BluetoothDevice) {
        if (!hasBluetoothPermission()) return
        status.text = "Status: Connecting to ${device.name ?: device.address}…"
        executor.execute {
            try {
                transport?.close()
                val bt = BtTransport(device.address)
                bt.open()
                transport = bt
                runOnUiThread { status.text = "Status: Bluetooth connected to OBD adapter" }
            } catch (e: Exception) {
                runOnUiThread { status.text = "Status: Connection failed — ${e.message}" }
            }
        }
    }

    private fun hasBluetoothPermission(): Boolean {
        if (Build.VERSION.SDK_INT < 31) return true
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 10)
            return false
        }
        return true
    }

    override fun onDestroy() {
        transport?.close()
        executor.shutdownNow()
        super.onDestroy()
    }

    data class DeviceItem(val device: BluetoothDevice, val label: String) {
        override fun toString(): String = label
    }
}

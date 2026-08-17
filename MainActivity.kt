package au.com.fuelcoder

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestBluetoothPermission()

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 48, 32, 32)
        }

        layout.addView(TextView(this).apply {
            text = "OBD2 Injector Coder"
            textSize = 28f
        })
        layout.addView(TextView(this).apply {
            text = "Ford Ranger PK 3.0 TDCi\nBosch EDC16C7\n0 281 016 312 / WE7218881A"
            textSize = 18f
            setPadding(0, 20, 0, 20)
        })

        status = TextView(this).apply {
            text = "Connection test not run."
            textSize = 16f
            setPadding(0, 12, 0, 20)
        }
        layout.addView(status)

        val testButton = Button(this).apply {
            text = "TEST OBD CONNECTION"
            setOnClickListener { runConnectionTest() }
        }
        layout.addView(testButton)

        layout.addView(TextView(this).apply {
            text = "READ / TEST ONLY\nInjector writing remains locked until the exact ECU procedure is verified."
            textSize = 15f
            setPadding(0, 24, 0, 0)
        })

        setContentView(ScrollView(this).apply { addView(layout) })
    }

    private fun requestBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 31 &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 10)
        }
    }

    private fun runConnectionTest() {
        try {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            if (adapter == null) {
                status.text = "RESULT: This phone has no Bluetooth adapter."
                return
            }
            if (!adapter.isEnabled) {
                status.text = "RESULT: Bluetooth is OFF. Turn Bluetooth on and test again."
                return
            }
            val devices: Set<BluetoothDevice> = adapter.bondedDevices
            status.text = if (devices.isEmpty()) {
                "RESULT: Bluetooth is available, but no paired OBD adapter was found.\n\nPair your OBD adapter in Android Bluetooth settings, then test again."
            } else {
                "RESULT: Bluetooth OK. ${devices.size} paired device(s) found.\n\nNext step: select the OBD adapter and test ECU communication."
            }
        } catch (e: SecurityException) {
            status.text = "RESULT: Bluetooth permission is required."
        } catch (e: Exception) {
            status.text = "RESULT: Bluetooth test error: ${e.message}"
        }
    }
}

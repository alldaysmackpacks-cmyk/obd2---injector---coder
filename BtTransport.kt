package au.com.fuelcoder.bt

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import au.com.fuelcoder.core.transport.Transport
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

/**
 * Classic-Bluetooth SPP transport to the OBD adapter.
 *
 * The OBDLink MX+ exposes the standard serial port profile; most ELM327
 * clones do too. Requires BLUETOOTH_CONNECT (Android 12+) at runtime.
 */
class BtTransport(private val mac: String) : Transport {

    private var socket: BluetoothSocket? = null
    private var input: InputStream? = null
    private var output: OutputStream? = null

    override fun open() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
            ?: throw IOException("No Bluetooth adapter on this device")
        if (!adapter.isEnabled) throw IOException("Bluetooth is switched off")
        val device = adapter.getRemoteDevice(mac)
        val s = device.createRfcommSocketToServiceRecord(SPP_UUID)
        try {
            s.connect()
        } catch (e: IOException) {
            // fallback: some adapters need the insecure (no-pairing) channel
            try {
                val s2 = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID)
                s2.connect()
                socket = s2
                input = s2.inputStream
                output = s2.outputStream
                return
            } catch (e2: IOException) {
                try {
                    s.close()
                } catch (_: Exception) {
                }
                throw IOException("Could not open RFCOMM to $mac: ${e.message}", e)
            }
        }
        socket = s
        input = s.inputStream
        output = s.outputStream
    }

    override fun writeCommand(command: String) {
        val out = output ?: throw IOException("Not connected")
        out.write((command + "\r").toByteArray(Charsets.US_ASCII))
        out.flush()
    }

    override fun readLine(timeoutMs: Int): String? {
        val inp = input ?: throw IOException("Not connected")
        val deadline = System.currentTimeMillis() + timeoutMs
        val sb = StringBuilder()
        while (System.currentTimeMillis() < deadline) {
            while (inp.available() > 0) {
                val c = inp.read()
                if (c == -1) return null
                when (c) {
                    '\r'.code -> if (sb.isNotEmpty()) return sb.toString()
                    '\n'.code -> {}
                    else -> sb.append(c.toChar())
                }
            }
            if (sb.isNotEmpty()) return sb.toString()
            Thread.sleep(20)
        }
        return null
    }

    override fun close() {
        try {
            socket?.close()
        } catch (_: Exception) {
        }
        socket = null
        input = null
        output = null
    }

    override fun isOpen(): Boolean = socket?.isConnected == true

    companion object {
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}

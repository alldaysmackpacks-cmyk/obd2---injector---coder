package au.com.fuelcoder.core.transport

interface Transport {
    fun open()
    fun writeCommand(command: String)
    fun readLine(timeoutMs: Int): String?
    fun close()
    fun isOpen(): Boolean
}

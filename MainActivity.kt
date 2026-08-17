package au.com.fuelcoder

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 48, 32, 32)
        }

        layout.addView(TextView(this).apply {
            text = "OBD2 Injector Coder"
            textSize = 28f
        })
        layout.addView(TextView(this).apply {
            text = "Android APK build is working. Bluetooth/OBD coding module ready for integration."
            textSize = 16f
            setPadding(0, 24, 0, 0)
        })

        setContentView(layout)
    }
}

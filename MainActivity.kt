package au.com.fuelcoder

import android.graphics.Typeface
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
            setTypeface(null, Typeface.BOLD)
        })

        layout.addView(TextView(this).apply {
            text = "Ford Ranger PK 3.0 TDCi\n\nECU: Bosch EDC16C7\nBosch: 0 281 016 312\nFord: WE7218881A\n\nInjector count: 4"
            textSize = 18f
            setPadding(0, 28, 0, 0)
        })

        layout.addView(TextView(this).apply {
            text = "ECU profile loaded.\n\nREAD / TEST mode: available\nINJECTOR WRITE: LOCKED until the exact EDC16C7 security and injector-coding procedure is verified."
            textSize = 16f
            setPadding(0, 28, 0, 0)
        })

        setContentView(layout)
    }
}

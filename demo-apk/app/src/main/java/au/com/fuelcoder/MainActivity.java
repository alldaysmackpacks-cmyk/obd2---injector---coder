package au.com.fuelcoder;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.widget.*;

public class MainActivity extends Activity {
  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    LinearLayout l = new LinearLayout(this);
    l.setOrientation(LinearLayout.VERTICAL); l.setPadding(32,48,32,32);
    TextView title = new TextView(this); title.setText("OBD2 Injector Coder"); title.setTextSize(28); title.setTypeface(null, Typeface.BOLD); l.addView(title);
    TextView info = new TextView(this); info.setText("\nDEMO MODE\n\n2010 Ford Ranger PK 3.0 TDCi\nBosch EDC16C7\nBosch 0 281 016 312\nFord WE7218881A\n\nInjector count: 4"); info.setTextSize(18); l.addView(info);
    Button test = new Button(this); test.setText("TEST OBD CONNECTION"); l.addView(test);
    TextView status = new TextView(this); status.setText("\nDemo only — no vehicle communication.\nInjector WRITE: LOCKED"); status.setTextSize(16); l.addView(status);
    test.setOnClickListener(v -> status.setText("\nRESULT: Demo connection test passed.\n\nNo ECU was contacted.\nInjector WRITE remains LOCKED."));
    setContentView(l);
  }
}

package hackatum.de.checkcrash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class SystemOverlay {

    private static WindowManager windowManager;
    private static View layout;

    /**
     * Creates an overlay (over the phone app) which can display important information
     *
     * @param c
     * @param line1
     * @param line2
     * @param line3
     */
    public static void createPhoneOverlay(Context c, String line1, String line2, String line3) {
        windowManager = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.CENTER;

        layout = inflater.inflate(R.layout.phone_overlay_layout, null);
        ((TextView) layout.findViewById(R.id.textView)).setText(line1);
        ((TextView) layout.findViewById(R.id.textView2)).setText(line2);
        ((TextView) layout.findViewById(R.id.textView3)).setText(line3);

        windowManager.addView(layout, params);
    }

    /**
     * Shows settings-page to enable overlay
     *
     * @param context
     */
    public static void requestSystemAlertPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, 69);
        }
    }


    public static void destroyPhoneOverlay() {
        windowManager.removeView(layout);
    }
}

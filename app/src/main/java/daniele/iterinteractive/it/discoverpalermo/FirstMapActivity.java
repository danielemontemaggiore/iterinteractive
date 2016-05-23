package daniele.iterinteractive.it.discoverpalermo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class FirstMapActivity extends Activity implements View.OnTouchListener
{
    // TRAGITTO PORTO -> POLITEAMA : DURATA 11 MINUTI - 2 MINUTI DI PREAVVISO = 9 MINUTI
    private FirstMapActivity.MalibuCountDownTimer countDownTimer;

    private final long startTime = 20000; // TODO :  DA CORREGGERE CON 9000000
    private final long interval = 1000;
    private final long notice = 5000;     // TODO : DA CORREGGERE CON 3000000
    private int i=0;
    private TextView time_remaining;

    private PopupWindow pwindo;
    Button btnClose;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_map);

        time_remaining = (TextView) findViewById(R.id.time_remaining);
        countDownTimer = new FirstMapActivity.MalibuCountDownTimer(startTime-notice, interval);
        countDownTimer.start();

        ImageView iv = (ImageView) findViewById (R.id.image);
        if (iv != null) {
            iv.setOnTouchListener (this);
        }
    }

    // CountDownTimer class
    public class MalibuCountDownTimer extends CountDownTimer
    {

        public MalibuCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            i++;
            initiatePopupWindow(i);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            time_remaining.setText("" + TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) +" : " + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)%60));

        }
    }

    private void initiatePopupWindow(int j) {
        try {

            LayoutInflater inflater = (LayoutInflater) FirstMapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_time_finished,(ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 900, 400, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnClose = (Button) layout.findViewById(R.id.btn_close);
            TextView timeFinished = (TextView) layout.findViewById(R.id.select_time);
            switch (j){
                case 1:
                    timeFinished.setText("Hai già raggiunto la destinazione? Affrettati! Hai solo 3 minuti per raggiungere la destinazione..");
                    break;
                case 2:
                    timeFinished.setText("Il tempo di percorrenza a tua disposizione è terminato. Hai già raggiunto il monumento?\n" +
                            "Se si, clicca sull'icona del monumento, altrimenti ricordati che puoi acquistare le ore \n" +
                            "extra a fine giornata.");
                    break;
            }

            final int finalI = j;
            btnClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switch (finalI){
                        case 1:
                            pwindo.dismiss();
                            //toast(Integer.toString(finalI));
                            countDownTimer = new FirstMapActivity.MalibuCountDownTimer(notice, interval);
                            countDownTimer.start();
                            break;
                        case 2:
                            pwindo.dismiss();
                            break;
                    }



                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onTouch (View v, MotionEvent ev)
    {
        boolean handledHere = false;

        final int action = ev.getAction();

        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        int nextImage = -1;			// resource id of the next image to display

        // If we cannot find the imageView, return.
        ImageView imageView = (ImageView) v.findViewById (R.id.image);
        if (imageView == null) return false;

        // When the action is Down, see if we should show the "pressed" image for the default image.
        // We do this when the default image is showing. That condition is detectable by looking at the
        // tag of the view. If it is null or contains the resource number of the default image, display the pressed image.
        Integer tagNum = (Integer) imageView.getTag ();
        int currentResource = (tagNum == null) ? R.drawable.mappa1 : tagNum.intValue ();

        // Now that we know the current resource being displayed we can handle the DOWN and UP events.

        switch (action) {
            case MotionEvent.ACTION_DOWN :
                if (currentResource == R.drawable.mappa1) {
                    nextImage = R.drawable.mappa1;
                    handledHere = true;
       /*
       } else if (currentResource != R.drawable.p2_ship_default) {
         nextImage = R.drawable.p2_ship_default;
         handledHere = true;
       */
                } else handledHere = true;
                break;

            case MotionEvent.ACTION_UP :
                // On the UP, we do the click action.
                // The hidden image (image_areas) has three different hotspots on it.
                // The colors are red, blue, and yellow.
                // Use image_areas to determine which region the user touched.
                int touchColor = getHotspotColor (R.id.image_areas, evX, evY);

                // Compare the touchColor to the expected values. Switch to a different image, depending on what color was touched.
                // Note that we use a Color Tool object to test whether the observed color is close enough to the real color to
                // count as a match. We do this because colors on the screen do not match the map exactly because of scaling and
                // varying pixel density.
                ColorTool ct = new ColorTool ();
                int tolerance = 25;
                nextImage = R.drawable.mappa1;
                if (ct.closeMatch (Color.RED, touchColor, tolerance)) {
                    //nextImage = R.drawable.mappa1;
                    //toast("Hai cliccato sul Teatro Politeama");
                    Intent viewDetailIntent = new Intent(FirstMapActivity.this, Discovery1Activity.class);
                    FirstMapActivity.this.startActivity(viewDetailIntent);
                }
                else
                    toast("Non Hai cliccato sul Teatro Politeama");

                if (currentResource == nextImage) {
                    nextImage = R.drawable.mappa1;
                }
                handledHere = true;
                break;

            default:
                handledHere = false;
        } // end switch

        if (handledHere) {

            if (nextImage > 0) {
                imageView.setImageResource (nextImage);
                imageView.setTag (nextImage);
            }
        }
        return handledHere;
    }




    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        if (img == null) {
            Log.d ("ImageAreasActivity", "Hot spot image not found");
            return 0;
        } else {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            if (hotspots == null) {
                Log.d ("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                img.setDrawingCacheEnabled(false);
                return hotspots.getPixel(x, y);
            }
        }
    }


    public void toast (String msg)
    {
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    }

}

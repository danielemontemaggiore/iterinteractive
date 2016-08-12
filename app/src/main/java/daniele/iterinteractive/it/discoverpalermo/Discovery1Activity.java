package daniele.iterinteractive.it.discoverpalermo;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class Discovery1Activity extends Activity implements View.OnTouchListener
{
    // VISITA POLITEAMA : DURATA 9 MINUTI - 2 MINUTI DI PREAVVISO = 7 MINUTI
    private Discovery1Activity.MalibuCountDownTimer countDownTimer;

    private final long startTime = 100000; // TODO :  DA CORREGGERE CON 7000000
    private final long interval = 1000;
    private final long notice = 5000;     // TODO : DA CORREGGERE CON 2000000
    private TextView time_remaining;
    private int i=0;
    private ImageView image2;
    private PopupWindow pwindo;
    private String msg1, msg2, msg3;
    LinearLayout btnClose;
    Animation animationFadeIn, animationFadeOut;
    private Typeface Windlass;
    @ColorInt
    public static final int LIME        = 0xFF800000;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery1);

        Windlass = Typeface.createFromAsset(getAssets(), "fonts/Windlass.ttf");
        time_remaining = (TextView) findViewById(R.id.time_remaining);
        time_remaining.setTypeface(Windlass);
        countDownTimer = new Discovery1Activity.MalibuCountDownTimer(startTime-notice, interval);
        countDownTimer.start();

        image2 = (ImageView)findViewById(R.id.image2);
        image2.setBackgroundResource(R.drawable.politeama1867);
            animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
            animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        image2.startAnimation(animationFadeIn);


        msg1= getResources().getString(R.string.time_finished1);
        msg2= getResources().getString(R.string.time_finished2);
        msg3= getResources().getString(R.string.time_finished3);



        Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogSlideAnim));
        dialog.setTitle("Title...");

        ImageView iv = (ImageView) findViewById (R.id.image);
        if (iv != null) {
            iv.setOnTouchListener (this);
        }
    }

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
            time_remaining.setTypeface(Windlass);
            time_remaining.setText("" + TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) +" : " + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)%60));
        }
    }

    private void initiatePopupWindow(int j) {
        try {

            LayoutInflater inflater = (LayoutInflater) Discovery1Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_time_finished,(ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 500, 300, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnClose = (LinearLayout) layout.findViewById(R.id.popup_element);
            TextView timeFinished = (TextView) layout.findViewById(R.id.select_time);
            timeFinished.setTypeface(Windlass);
            switch (j){
                case 1:
                    timeFinished.setText(msg1+" 2 "+msg2);
                    break;
                case 2:
                    timeFinished.setText(msg3);
                    break;
            }

            final int finalI = j;
            btnClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switch (finalI){
                        case 1:
                            pwindo.dismiss();
                            countDownTimer = new Discovery1Activity.MalibuCountDownTimer(notice, interval);
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
        int currentResource = (tagNum == null) ? R.drawable.politeama_screen : tagNum.intValue ();

        // Now that we know the current resource being displayed we can handle the DOWN and UP events.

        switch (action) {
            case MotionEvent.ACTION_DOWN :
                if (currentResource == R.drawable.politeama_screen) {
                    nextImage = R.drawable.politeama_screen;
                    handledHere = true;
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
                nextImage = R.drawable.politeama_screen;
                if (ct.closeMatch (Color.LTGRAY, touchColor, tolerance)) {                        // LIGHTGRAY
                    image2.startAnimation(animationFadeOut);
                    image2.setBackgroundResource(R.drawable.politeama1867);
                    image2.startAnimation(animationFadeIn);
                }
                else if (ct.closeMatch (Color.parseColor("#008081"), touchColor, tolerance)) {   // TEAL
                    image2.startAnimation(animationFadeOut);
                    image2.setBackgroundResource(R.drawable.politeama1874);
                    image2.startAnimation(animationFadeIn);
                }
                else if (ct.closeMatch (Color.parseColor("#7B8101"), touchColor, tolerance)) {    // OLIVE
                    image2.startAnimation(animationFadeOut);
                    image2.setBackgroundResource(R.drawable.politeama1877);
                    image2.startAnimation(animationFadeIn);
                }
                else if (ct.closeMatch (Color.parseColor("#800000"), touchColor, tolerance)) {    // MAROON
                    image2.startAnimation(animationFadeOut);
                    image2.setBackgroundResource(R.drawable.politeama1891);
                    image2.startAnimation(animationFadeIn);
                }
                else if (ct.closeMatch (Color.parseColor("#FF7F27"), touchColor, tolerance)) {    // ORANGE
                    image2.startAnimation(animationFadeOut);
                    image2.setBackgroundResource(R.drawable.politeama1910);
                    image2.startAnimation(animationFadeIn);
                }
                else if (ct.closeMatch (Color.BLUE, touchColor, tolerance)) {                     // PAPPAGALLO
                // TODO : deve parlare il pappagallo (deve dare l'aiuto)
                //toast("Il pappagallo parlerà..");
                    MediaPlayer mPlayer = MediaPlayer.create(Discovery1Activity.this, R.raw.coldplay);
                    mPlayer.start();
                }
                else if (ct.closeMatch (Color.parseColor("#712B2B"), touchColor, tolerance)) {    // PROSSIMA TAPPA
                    Intent prossimaTappa = new Intent(Discovery1Activity.this, Tappa2Activity.class);
                    Discovery1Activity.this.startActivity(prossimaTappa);
                }

                if (currentResource == nextImage) {
                    nextImage = R.drawable.politeama_screen;
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

package daniele.iterinteractive.it.discoverpalermo;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class Tappa1Activity extends Activity implements View.OnTouchListener
{
    // TRAGITTO PORTO -> POLITEAMA : DURATA 11 MINUTI - 2 MINUTI DI PREAVVISO = 9 MINUTI
    private Tappa1Activity.MalibuCountDownTimer countDownTimer;

    private final long startTime = 20000;       // TODO :  DA CORREGGERE CON 9000000
    private final long interval = 1000;
    private final long notice = 5000;           // TODO : DA CORREGGERE CON 3000000
    private int i=0;
    private TextView time_remaining;
    private Typeface Windlass;
    MediaPlayer mPlayer;
    private PopupWindow pwindo;
    LinearLayout btnClose;
    SharedPreferences sharedpreferences;
    String language;
    Context context;
    Language languageClass;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        sharedpreferences = getSharedPreferences("Language",
                Context.MODE_PRIVATE);
        language = sharedpreferences.getString("lang", "");
        languageClass= new Language();
        languageClass.setLocale(language, context);
        setContentView(R.layout.activity_tappa1);

        Windlass = Typeface.createFromAsset(getAssets(), "fonts/Windlass.ttf");
        time_remaining = (TextView) findViewById(R.id.time_remaining);
        countDownTimer = new Tappa1Activity.MalibuCountDownTimer(startTime-notice, interval);
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
            time_remaining.setTypeface(Windlass);
            time_remaining.setText("" + TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) +" : " + (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)%60));

        }
    }

    private void initiatePopupWindow(int j) {
        try {

            LayoutInflater inflater = (LayoutInflater) Tappa1Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_time_finished,(ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 500, 300, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnClose = (LinearLayout) layout.findViewById(R.id.popup_element);
            TextView timeFinished = (TextView) layout.findViewById(R.id.select_time);
            timeFinished.setTypeface(Windlass);
            switch (j){
                case 1:
                    mPlayer = MediaPlayer.create(Tappa1Activity.this, R.raw.preavviso_tempo_scaduto);
                    mPlayer.start();
                    timeFinished.setText(getResources().getString(R.string.raggiunto));
                    break;
                case 2:
                    mPlayer = MediaPlayer.create(Tappa1Activity.this, R.raw.tempo_scaduto_small_blast);
                    mPlayer.start();
                    timeFinished.setText(getResources().getString(R.string.terminato));
                    break;
            }

            final int finalI = j;
            btnClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switch (finalI){
                        case 1:
                            pwindo.dismiss();
                            countDownTimer = new Tappa1Activity.MalibuCountDownTimer(notice, interval);
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
                    finish();
                    Intent viewDetailIntent = new Intent(Tappa1Activity.this, Discovery1Activity.class);
                    Tappa1Activity.this.startActivity(viewDetailIntent);
                }
                else if (ct.closeMatch (Color.YELLOW, touchColor, tolerance)) {
                    // L'utente ha cliccato sul Kursaal Biondo
                    openDialog(getResources().getString(R.string.kursaal),getResources().getString(R.string.kursaal_info));
                }
                else if (ct.closeMatch (Color.GREEN, touchColor, tolerance)) {
                    // L'utente ha cliccato sul Kursaal Biondo
                    openDialog(getResources().getString(R.string.valdese),getResources().getString(R.string.valdese_info));
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

    private void openDialog(String titolo, String info){
        final Dialog dialog = new Dialog(Tappa1Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoglayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        LinearLayout btnDismiss = (LinearLayout)dialog.getWindow().findViewById(R.id.dismiss);
        TextView Ttitolo = (TextView)dialog.getWindow().findViewById(R.id.Ttitolo);
        Ttitolo.setText(titolo);
        TextView Tinfo = (TextView)dialog.getWindow().findViewById(R.id.Tinfo);
        Tinfo.setText(info);
        btnDismiss.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }});

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        return;
    }

}

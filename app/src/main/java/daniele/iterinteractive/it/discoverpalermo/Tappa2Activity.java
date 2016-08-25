package daniele.iterinteractive.it.discoverpalermo;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class Tappa2Activity extends Activity implements View.OnTouchListener
{
    // TRAGITTO POLITEAMA -> MASSIMO : DURATA 11 MINUTI - 2 MINUTI DI PREAVVISO = 9 MINUTI
    private Tappa2Activity.MalibuCountDownTimer countDownTimer;

    private final long startTime = 20000;       // TODO :  DA CORREGGERE CON 9000000
    private final long interval = 1000;
    private final long notice = 5000;           // TODO : DA CORREGGERE CON 3000000
    private int i=0;
    private TextView time_remaining;
    private Typeface Windlass;
    MediaPlayer mPlayer;
    private PopupWindow pwindo;
    LinearLayout btnClose;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tappa2);

        Windlass = Typeface.createFromAsset(getAssets(), "fonts/Windlass.ttf");
        time_remaining = (TextView) findViewById(R.id.time_remaining);
        countDownTimer = new Tappa2Activity.MalibuCountDownTimer(startTime-notice, interval);
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

            LayoutInflater inflater = (LayoutInflater) Tappa2Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_time_finished,(ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 500, 300, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnClose = (LinearLayout) layout.findViewById(R.id.popup_element);
            TextView timeFinished = (TextView) layout.findViewById(R.id.select_time);
            timeFinished.setTypeface(Windlass);
            switch (j){
                case 1:
                    mPlayer = MediaPlayer.create(Tappa2Activity.this, R.raw.preavviso_tempo_scaduto);
                    mPlayer.start();
                    timeFinished.setText(getResources().getString(R.string.raggiunto));
                    break;
                case 2:
                    mPlayer = MediaPlayer.create(Tappa2Activity.this, R.raw.tempo_scaduto_small_blast);
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
                            //toast(Integer.toString(finalI));
                            countDownTimer = new Tappa2Activity.MalibuCountDownTimer(notice, interval);
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
        int currentResource = (tagNum == null) ? R.drawable.mappa2 : tagNum.intValue ();

        // Now that we know the current resource being displayed we can handle the DOWN and UP events.

        switch (action) {
            case MotionEvent.ACTION_DOWN :
                if (currentResource == R.drawable.mappa2) {
                    nextImage = R.drawable.mappa2;
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
                nextImage = R.drawable.mappa2;
                if (ct.closeMatch (Color.parseColor("#0070FA"), touchColor, tolerance) || ct.closeMatch (Color.parseColor("#006FF9"), touchColor, tolerance)) {
                    // L'utente ha cliccato sul Teatro Massimo
                    toast("Hai cliccato sul Massimo");
                   // Intent viewDetailIntent = new Intent(Tappa2Activity.this, Discovery2Activity.class);
                //    Tappa2Activity.this.startActivity(viewDetailIntent);
                }
                else if (ct.closeMatch (Color.parseColor("#FA0001"), touchColor, tolerance)) {
                    // L'utente ha cliccato sul Kursaal Biondo
                    openDialog(getResources().getString(R.string.kursaal),getResources().getString(R.string.kursaal_info));
                }
                else if (ct.closeMatch (Color.parseColor("#2300FA"), touchColor, tolerance)) {
                    // L'utente ha cliccato sulla Chiesa Anglicana
                    openDialog(getResources().getString(R.string.chiesa_anglicana),getResources().getString(R.string.chiesa_anglicana_info));
                }
                else if (ct.closeMatch (Color.parseColor("#00CDFA"), touchColor, tolerance)) {
                    // L'utente ha cliccato sulla Parrocchia di SS. Pietro e Paolo Apostoli
                    openDialog(getResources().getString(R.string.parrocchia_pietro_paolo),getResources().getString(R.string.parrocchia_pietro_paolo_info));
                }
                else if (ct.closeMatch (Color.parseColor("#85F587"), touchColor, tolerance) || ct.closeMatch (Color.parseColor("#84D7F5"), touchColor, tolerance)) {
                    // L'utente ha cliccato sui chioschi del Teatro Massimo
                    openDialog(getResources().getString(R.string.chioschi_ribaudo),getResources().getString(R.string.chioschi_ribaudo_info));
                }
                else if (ct.closeMatch (Color.parseColor("#1DFA38"), touchColor, tolerance)) {
                    // L'utente ha cliccato sulla Chiesa di Sant' Ignazio all'Olivella
                    openDialog(getResources().getString(R.string.chiesa_san_ignazio),getResources().getString(R.string.chiesa_san_ignazio_info));
                }
                else if (ct.closeMatch (Color.parseColor("#F98700"), touchColor, tolerance)) {
                    // L'utente ha cliccato sul Museo Salinas
                    openDialog(getResources().getString(R.string.museo_salinas),getResources().getString(R.string.museo_salinas_info));
                }
                else if (ct.closeMatch (Color.parseColor("#881179"), touchColor, tolerance)) {
                    // L'utente ha cliccato sulle Poste
                    openDialog(getResources().getString(R.string.edificio_poste),getResources().getString(R.string.edificio_poste_info));
                }


                if (currentResource == nextImage) {
                    nextImage = R.drawable.mappa2;
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
        final Dialog dialog = new Dialog(Tappa2Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoglayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

}

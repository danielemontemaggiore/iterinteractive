package daniele.iterinteractive.it.discoverpalermo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ChooseModeActivity extends Activity {

    TextView string_choose;
    LinearLayout free_tour_layout, discovery_layout;
    private PopupWindow pwindo;
    Button btnTwoHours, btnFourHours;
    Typeface pirateFonts;
    SharedPreferences prefs;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_mode);
/*
        prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        language = prefs.getString("lang",null);

        // TODO: DA TOGLIERE LA RIGA DI SOTTO!!!
        language="it";
        LanguageHelper.changeLocale(this.getResources(), language);
*/

        Language.setLocaleIt(ChooseModeActivity.this);
        string_choose = (TextView) findViewById(R.id.chooseMode);
        pirateFonts = Typeface.createFromAsset(getAssets(), "fonts/Treamd.ttf");
        string_choose.setTypeface(pirateFonts);
        free_tour_layout = (LinearLayout) findViewById(R.id.free_tour_layout);
        discovery_layout = (LinearLayout) findViewById(R.id.discovery_layout);
        free_tour_layout.setOnClickListener(onClickListener);
        discovery_layout.setOnClickListener(onClickListener);
    }

    private void initiatePopupWindow() {
        try {

            LayoutInflater inflater = (LayoutInflater) ChooseModeActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.activity_choose_hours,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 900, 400, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnTwoHours = (Button) layout.findViewById(R.id.btn_two_hrs);
            btnFourHours = (Button) layout.findViewById(R.id.btn_four_hrs);
            btnTwoHours.setOnClickListener(time_button_click_listener);
            btnFourHours.setOnClickListener(time_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener time_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent viewMapIntent = new Intent(ChooseModeActivity.this, Tappa1Activity.class);
            switch (v.getId()) {
                case R.id.btn_two_hrs:
                    // TODO : Devo aggiungere un "putextra" per passare anche il numero delle ore (2)
                    break;
                case R.id.btn_four_hrs:
                    // TODO : Devo aggiungere un "putextra" per passare anche il numero delle ore (4)
                    break;
            }
            ChooseModeActivity.this.startActivity(viewMapIntent);
        }
    };



    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.free_tour_layout:
                    Intent freeTourIntent = new Intent(ChooseModeActivity.this, ChooseModeActivity.class);
                    ChooseModeActivity.this.startActivity(freeTourIntent);
                    break;
                case R.id.discovery_layout:
                    initiatePopupWindow();
                    break;
            }

        }
    };
}
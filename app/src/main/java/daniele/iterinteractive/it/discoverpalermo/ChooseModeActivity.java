package daniele.iterinteractive.it.discoverpalermo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Locale;

import daniele.iterinteractive.it.discoverpalermo.util.LanguageHelper;

public class ChooseModeActivity extends Activity {

    TextView string_choose, chooseMode;
    LinearLayout free_tour_layout, discovery_layout;
    private PopupWindow pwindo;
    Button btnTwoHours, btnFourHours;
    Typeface Windlass;
    SharedPreferences sharedpreferences;
    String language;
    Context context;
    Language languageClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        sharedpreferences = getSharedPreferences("Language",
                Context.MODE_PRIVATE);
        language = sharedpreferences.getString("lang", "");
        languageClass= new Language();
        languageClass.setLocale(language, context);

        setContentView(R.layout.activity_choose_mode);

        string_choose = (TextView) findViewById(R.id.chooseMode);
        chooseMode = (TextView) findViewById(R.id.chooseMode);

        Windlass = Typeface.createFromAsset(getAssets(), "fonts/Windlass.ttf");
        string_choose.setTypeface(Windlass);
        chooseMode.setTypeface(Windlass);
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
            pwindo = new PopupWindow(layout, 500, 300, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);


            TextView select_time = (TextView) findViewById(R.id.select_time);
            select_time.setTypeface(Windlass);
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
                    //initiatePopupWindow();
                    Intent start2Hrs = new Intent(ChooseModeActivity.this, Tappa1Activity.class);
                    ChooseModeActivity.this.startActivity(start2Hrs);
                    break;
            }

        }
    };

}
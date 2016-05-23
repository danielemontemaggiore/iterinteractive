package daniele.iterinteractive.it.discoverpalermo;

import daniele.iterinteractive.it.discoverpalermo.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    ImageView france_flag,germany_flag,italia_flag,nippon_flag,spain_flag,uk_flag;
    TextView benvenuto,welcome,bienvenue,wilkomme,bienvenida,yokkoso;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editor = getSharedPreferences("preferences", MODE_PRIVATE).edit();


        Typeface pirateFonts = Typeface.createFromAsset(getAssets(), "fonts/Treamd.ttf");
        benvenuto = (TextView) findViewById(R.id.benvenuto);
        benvenuto.setTypeface(pirateFonts);
        welcome = (TextView) findViewById(R.id.welcome);
        welcome.setTypeface(pirateFonts);
        bienvenue = (TextView) findViewById(R.id.bienvenue);
        bienvenue.setTypeface(pirateFonts);
        wilkomme = (TextView) findViewById(R.id.wilkomme);
        wilkomme.setTypeface(pirateFonts);
        bienvenida = (TextView) findViewById(R.id.bienvenida);
        bienvenida.setTypeface(pirateFonts);
        yokkoso = (TextView) findViewById(R.id.yokkoso);
        yokkoso.setTypeface(pirateFonts);
        france_flag = (ImageView) findViewById(R.id.france_flag);
        germany_flag = (ImageView) findViewById(R.id.germany_flag);
        italia_flag = (ImageView) findViewById(R.id.italia_flag);
        nippon_flag = (ImageView) findViewById(R.id.nippon_flag);
        spain_flag = (ImageView) findViewById(R.id.spain_flag);
        uk_flag = (ImageView) findViewById(R.id.uk_flag);

        france_flag.setOnClickListener(onClickListener);
        germany_flag.setOnClickListener(onClickListener);
        italia_flag.setOnClickListener(onClickListener);
        nippon_flag.setOnClickListener(onClickListener);
        spain_flag.setOnClickListener(onClickListener);
        uk_flag.setOnClickListener(onClickListener);

        editor = getSharedPreferences("Language", MODE_PRIVATE).edit();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.france_flag:
                    editor.putString("lang", "fr");
                    editor.commit();
                    break;
                case R.id.germany_flag:
                    editor.putString("lang", "de");
                    editor.commit();
                    break;
                case R.id.italia_flag:
                    editor.putString("lang", "it");
                    editor.commit();
                    break;
                case R.id.nippon_flag:
                    editor.putString("lang", "np");
                    editor.commit();
                    break;
                case R.id.spain_flag:
                    editor.putString("lang", "es");
                    editor.commit();
                    break;
                case R.id.uk_flag:
                    editor.putString("lang", "en");
                    editor.commit();
                    break;
            }
            Intent chooseModeIntent = new Intent(MainActivity.this, ChooseModeActivity.class);
            MainActivity.this.startActivity(chooseModeIntent);
        }
    };
}
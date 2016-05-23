package daniele.iterinteractive.it.discoverpalermo;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by Daniele on 29/03/2016.
 */
public class Language extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setLocaleIt (Context context){
        Locale locale = new Locale("it");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    public static void setLocaleEn (Context context){
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

}

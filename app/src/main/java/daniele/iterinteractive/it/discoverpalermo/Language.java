package daniele.iterinteractive.it.discoverpalermo;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by Sviluppo on 25/08/2016.
 */
public class Language {

    public void setLocale(String lang, Context context) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
}

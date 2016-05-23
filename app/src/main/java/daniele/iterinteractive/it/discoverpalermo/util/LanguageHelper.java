package daniele.iterinteractive.it.discoverpalermo.util;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;


public class LanguageHelper {

    public static void changeLocale(Resources res, String locale) {

        Configuration config;
        config = new Configuration(res.getConfiguration());


        switch (locale) {
            case "it":
                config.locale = Locale.ITALIAN;
                break;
            case "en":
                config.locale = Locale.ENGLISH;
                break;
            case "fr":
                config.locale = Locale.FRENCH;
                break;
            case "ge":
                config.locale = Locale.GERMAN;
                break;
            case "jp":
                config.locale = Locale.JAPANESE;
            default:
                config.locale = Locale.ITALIAN;
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
        // reload files from assets directory
    }
}
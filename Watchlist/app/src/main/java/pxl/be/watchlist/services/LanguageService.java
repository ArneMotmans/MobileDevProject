package pxl.be.watchlist.services;

import java.util.List;

import pxl.be.watchlist.domain.SpokenLanguage;

/**
 * Created by 11501537 on 21/10/2017.
 */

public class LanguageService {
    public static String getLanguages(List<SpokenLanguage> spokenLanguages){
        if (spokenLanguages.size() == 0)
            return null;
        StringBuilder languages = new StringBuilder();
        languages.append(" ");
        for (SpokenLanguage language: spokenLanguages) {
            languages.append(language.getName()+", ");
        }
        return languages.substring(0,languages.length()-2);
    }
}

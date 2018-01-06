package com.dwidasa.admin.custom;

import com.dwidasa.admin.Constants;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.LocalizationSetter;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.Request;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/11/11
 * Time: 11:37 AM
 */
public class CustomLocalizationSetter implements LocalizationSetter {
    private final Request request;

    private final ThreadLocale threadLocale;

    private final Locale defaultLocale;

    private final Set<String> supportedLocaleNames;

    private final List<Locale> supportedLocales;

    private final Map<String, Locale> localeCache = CollectionFactory.newConcurrentMap();

    private final PersistentLocale persistentLocale;

    public CustomLocalizationSetter(ObjectLocator locator,
                                    @Symbol(SymbolConstants.SUPPORTED_LOCALES) String localeNames) {

        this.request = locator.getService(Request.class);
        this.persistentLocale = locator.getService(PersistentLocale.class);
        this.threadLocale = locator.getService(ThreadLocale.class);
        this.supportedLocaleNames = CollectionFactory.newSet();

        String[] names = TapestryInternalUtils.splitAtCommas(localeNames);
        for (String name : names) {
            supportedLocaleNames.add(name.toLowerCase());
        }

        supportedLocales = parseNames(names);
        defaultLocale = supportedLocales.get(0);
    }

    private List<Locale> parseNames(String[] localeNames) {
        List<Locale> list = CollectionFactory.newList();

        for (String name : localeNames) {
            list.add(toLocale(name));
        }

        return Collections.unmodifiableList(list);
    }

    public Locale toLocale(String localeName) {
        Locale result = localeCache.get(localeName);

        if (result == null) {
            result = constructLocale(localeName);
            localeCache.put(localeName, result);
        }

        return result;
    }

    private Locale constructLocale(String name) {
        String[] terms = name.split("_");

        switch (terms.length) {
            case 1:
                return new Locale(terms[0], "");
            case 2:
                return new Locale(terms[0], terms[1]);
            case 3:
                return new Locale(terms[0], terms[1], terms[2]);
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean setLocaleFromLocaleName(String localeName) {
        boolean supported = isSupportedLocaleName(localeName);

        if (supported) {
            Locale locale = findClosestSupportedLocale(toLocale(localeName));

            persistentLocale.set(locale);
            threadLocale.setLocale(locale);
        } else {
            String selectedLanguage = (String) request.getSession(true).getAttribute(Constants.SELECTED_LANGUAGE);
            if (selectedLanguage == null) {
                threadLocale.setLocale(toLocale("in_ID"));
                request.getSession(true).setAttribute(Constants.SELECTED_LANGUAGE, "Indonesia");
            }
        }

        return supported;
    }

    public void setNonPeristentLocaleFromLocaleName(String localeName) {
        Locale requested = toLocale(localeName);
        Locale supported = findClosestSupportedLocale(requested);

        threadLocale.setLocale(supported);
    }

    private Locale findClosestSupportedLocale(Locale desiredLocale) {
        String localeName = desiredLocale.toString();

        while (true) {
            if (isSupportedLocaleName(localeName)) {
                return toLocale(localeName);
            }

            localeName = stripTerm(localeName);
            if (localeName.length() == 0) {
                break;
            }
        }

        return defaultLocale;
    }

    static String stripTerm(String localeName) {
        int scorex = localeName.lastIndexOf('_');

        return scorex < 0 ? "" : localeName.substring(0, scorex);
    }

    public List<Locale> getSupportedLocales() {
        return supportedLocales;
    }

    public boolean isSupportedLocaleName(String localeName) {
        return supportedLocaleNames.contains(localeName.toLowerCase());
    }

    public SelectModel getSupportedLocalesModel() {
        List<OptionModel> options = CollectionFactory.newList();

        for (Locale l : supportedLocales) {
            options.add(new OptionModelImpl(l.getDisplayName(l), l));
        }

        return new SelectModelImpl(null, options);
    }
}

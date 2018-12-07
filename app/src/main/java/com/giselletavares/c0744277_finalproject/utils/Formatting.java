package com.giselletavares.c0744277_finalproject.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatting {

    Locale locale = new Locale("en", "CA");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    SimpleDateFormat dateTimeForIdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat dateShortFormatter = new SimpleDateFormat("dd/MM");
    SimpleDateFormat dateMediumFormatter = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateDescriptiveShortFormatter = new SimpleDateFormat("MMM dd, yy");
    SimpleDateFormat dateLongFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat durationFormatter = new SimpleDateFormat("HH:mm");

    public Formatting() {
    }

    public String getCurrencyFormatter(Double value) {
        return currencyFormatter.format(value);
    }

    public String getDateTimeForIdFormatter(Date value) {
        return dateTimeForIdFormatter.format(value);
    }

    public String getDateShortFormatter(Date value) {
        return dateShortFormatter.format(value);
    }

    public String getDateMediumFormatter(Date value) {
        return dateMediumFormatter.format(value);
    }

    public String getDateDescriptiveShortFormatter(Date value) {
        return dateDescriptiveShortFormatter.format(value);
    }
    public String getDateLongFormatter(Date value) {
        return dateLongFormatter.format(value);
    }

    public String getDurationFormatter(Date value) {
        return durationFormatter.format(value);
    } // the Timer class also


}

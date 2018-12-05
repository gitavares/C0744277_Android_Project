package com.giselletavares.c0744277_finalproject.utils;

import java.sql.Time;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatting {

    Locale locale = new Locale("en", "CA");
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    SimpleDateFormat dateShortFormatter = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateDescriptiveShortFormatter = new SimpleDateFormat("MMM dd, yy");
    SimpleDateFormat dateLongFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat durationFormatter = new SimpleDateFormat("HH:mm");

    public Formatting() {
    }

    public String getCurrencyFormatter(Double value) {
        return currencyFormatter.format(value);
    }

    public String getDateShortFormatter(Date value) {
        return dateShortFormatter.format(value);
    }

    public String getDateDescriptiveShortFormatter(Date value) {
        return dateDescriptiveShortFormatter.format(value);
    }
    public String getDateLongFormatter(Date value) {
        return dateLongFormatter.format(value);
    }

    public String getDurationFormatter(Time value) {
        return durationFormatter.format(value);
    } // the Timer class also


}

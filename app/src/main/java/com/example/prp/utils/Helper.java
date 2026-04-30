package com.example.prp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static String formatDateByMonth(Date date){
        SimpleDateFormat dateFormat= new SimpleDateFormat("MMMM");
        return dateFormat.format(date);
    }
}

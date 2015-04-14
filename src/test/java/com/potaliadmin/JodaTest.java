package com.potaliadmin;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by nihalsharma on 13/04/15.
 */
public class JodaTest {

    @org.junit.Test
    public void testJoda() {
        String time = "13 Apr 2015, 02:15 PM";
        DateTimeFormatter formatter = DateTimeFormat
                .forPattern("dd MMM yyyy, hh:mm a");
        DateTime postedDateTime = formatter.parseDateTime(time);
        DateTime currentDateTime = new DateTime();
        final Period period = new Period(postedDateTime, currentDateTime);
        if (period.getDays() >= 3) {
            time = time.substring(0, time.indexOf(","));
        } else if (period.getHours() >= 24 && period.getDays() < 3) {
            System.out.println("a");
        } else if (period.getHours() < 24 && period.getHours() <= 1) {
            System.out.println("a");
        } else if (period.getHours() < 1) {
            System.out.println("a");
        } else {
            System.out.println("a");
        }
    }
}

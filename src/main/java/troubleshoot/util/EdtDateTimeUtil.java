package troubleshoot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EdtDateTimeUtil {
    /**
     * Get a time stamp. <p>
     * Time stamps can be compared to determine a time delta.
     *
     * @return Time stamp.
     */
    public static long getTimeStamp()
    {
        return System.currentTimeMillis();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get a date string using EDT's standard date/time format. <p>
     *
     * @return Date string.
     */
    public static String getDate()
    {
        return getDateTimeStamp("yyMMdd");
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get a time string using EDT's standard date/time format. <p>
     *
     * @return Time string.
     */
    public static String getTime()
    {
        return getDateTimeStamp("HHmmss.SSS");
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get a time string in EDT's HHMMSS format. <p>
     *
     * @param colons If true will add ':' between HH, MM & SS.
     * @return Time string.
     */
    public static String getTimeHHMMSS(boolean colons)
    {
        if (colons)
        {
            return getDateTimeStamp("HH:mm:ss");
        }
        else
        {
            return getDateTimeStamp("HHmmss");
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get a date/time string using EDT's standard date/time format. <p>
     *
     * @return Date & time string.
     */
    public static String getDateTime()
    {
        return getDateTimeStamp("yyyy.MM.dd HH:mm:ss.SSS");
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get a date/time stamp string. <p>
     *
     * @param dateTimeFormat Format string.
     * @return Time stamp string.
     */
    public static String getDateTimeStamp(String dateTimeFormat)
    {
        Date dateTime = new Date();
        SimpleDateFormat dateTimeSdf = new SimpleDateFormat(dateTimeFormat);
        return dateTimeSdf.format(dateTime);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Break seconds into string of parts: days, hours, minutes and seconds. <p>
     * If any part value is 0 (except for seconds) it will not in the string
     * unless the force flag is true.
     *
     * @param seconds Seconds to break apart.
     * @param force true will force part in string even if part value is 0.
     * @return The string.
     */
    public static String breakSecondsIntoDaysHoursMinsSecs(
            int seconds,
            boolean force)
    {
        int daySecs  = (60 * 60 * 24);
        int hourSecs = (60 * 60);
        int minSecs  = 60;

        int days  = seconds / daySecs;
        int hours = (seconds - (days * daySecs)) / hourSecs;
        int min   = (seconds - (days * daySecs) - (hours * hourSecs)) / minSecs;
        int secs  = seconds - (days * daySecs) - (hours * hourSecs) - (min * minSecs);

        StringBuilder sb = new StringBuilder(100);
        if (days > 0)
        {
            sb.append(days + " day");
            if (days > 1)
            {
                sb.append("s");
            }
            sb.append(", ");
        }
        else if (force)
        {
            sb.append(days + " days, ");
        }

        if (hours > 0)
        {
            sb.append(hours + " hour");
            if (hours > 1)
            {
                sb.append("s");
            }
            sb.append(", ");
        }
        else if (force)
        {
            sb.append(hours + " hours, ");
        }

        if (min > 0)
        {
            sb.append(min + " min");
            if (min > 1)
            {
                sb.append("s");
            }
            sb.append(", ");
        }
        else if (force)
        {
            sb.append(min + " mins, ");

        }

        sb.append(secs + " sec");
        if (secs > 1)
        {
            sb.append("s");
        }

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Break seconds into string of parts: days, hours, minutes and seconds. <p>
     * If any part value is 0 (except for seconds) it will not in the string
     * unless the force flag is true.
     *
     * @param seconds Seconds to break apart.
     * @param force true will force part in string even if part value is 0.
     * @return The string.
     */
    public static String breakSecondsIntoDaysHoursMinsSecs(
            long seconds,
            boolean force)
    {
        long daySecs  = (60 * 60 * 24);
        long hourSecs = (60 * 60);
        long minSecs  = 60;

        long days  = seconds / daySecs;
        long hours = (seconds - (days * daySecs)) / hourSecs;
        long min   = (seconds - (days * daySecs) - (hours * hourSecs)) / minSecs;
        long secs  = seconds - (days * daySecs) - (hours * hourSecs) - (min * minSecs);

        StringBuilder sb = new StringBuilder(100);
        if (days > 0)
        {
            sb.append(days + " day");
            if (days > 1)
            {
                sb.append("s");
            }
            sb.append(", ");
        }
        else if (force)
        {
            sb.append(days + " days, ");
        }

        if (hours > 0)
        {
            sb.append(hours + " hour");
            if (hours > 1)
            {
                sb.append("s");
            }
            sb.append(", ");
        }
        else if (force)
        {
            sb.append(hours + " hours, ");
        }

        if (min > 0)
        {
            sb.append(min + " min");
            if (min > 1)
            {
                sb.append("s");
            }
            sb.append(", ");
        }
        else if (force)
        {
            sb.append(min + " mins, ");

        }

        sb.append(secs + " sec");
        if (secs > 1)
        {
            sb.append("s");
        }

        return sb.toString();
    }
}

package troubleshoot.util;

/*
 * File: EdtUtil.java
 *
 * Desc: Edt utility methods.
 *
 * Gilbarco Inc. 2005
 *
 * History:
 *    2005.01.01.sat.Scott Turner  v01.0.01
 *       - Created.
 */

// ----------------------------------------------------------------------
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.zip.CRC32;

// ----------------------------------------------------------------------
/**
 * Utility methods. <p>
 *
 * @author Scott Turner
 */
public class EdtUtil
{
    // ******************************************************************
    // PUBLIC FIELDS.
    // ******************************************************************

    // ******************************************************************
    // PROTECTED FIELDS.
    // ******************************************************************

    // ******************************************************************
    // PRIVATE FIELDS.
    // ******************************************************************

    // ******************************************************************
    // CONSTRUCTOR.
    // ******************************************************************

    // ******************************************************************
    // FINALIZER.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    public void finalize() throws Throwable
    {
    }

    // ******************************************************************
    // PUBLIC METHODS       (general, getter, setter, interface imp)
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do nothing method. <p>
     */
    public static void nop()
    {
        // Do nothing.
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
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

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Determine if a char is an Ascii-Hex char. <p>
     *
     * @param ch What to test.
     * @return True if Ascii-Hex char, false otherwise.
     */
    public static boolean isHex(int ch)
    {
        String hexLow  = "0123456789abcdef";
        String hexHigh = "0123456789ABCDEF";

        return ((hexLow.indexOf(ch) >= 0) || (hexHigh.indexOf(ch) >= 0));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Determine if all chars in a string are Ascii-Hex. <p>
     *
     * @param str What to test.
     * @return True if all chars are Ascii-Hex, false otherwise.
     */
    public static boolean isHex(String str)
    {
        boolean retval = true;

        // Check all chars.
        for (int index = 0; index < str.length(); index++)
        {
            // Check a char.
            if (! isHex(str.charAt(index)))
            {
                // Failure.
                retval = false;
                break;
            }
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Determine if a char is an Binary char. <p>
     *
     * @param ch What to test.
     * @return True if Binary char (zero or one), false otherwise.
     */
    public static boolean isBinary(int ch)
    {
        String binaryPossibleChars  = "01";

        return (binaryPossibleChars.indexOf(ch) >= 0);
    }
    
    
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Determine if all chars in a string are Binary. <p>
     *
     * @param str What to test.
     * @return True if all chars are Binary (zeros or ones), false otherwise.
     */
    public static boolean isBinary(String str)
    {
        boolean retval = true;

        // Check all chars.
        for (int index = 0; index < str.length(); index++)
        {
            // Check a char.
            if (! isBinary(str.charAt(index)))
            {
                // Failure.
                retval = false;
                break;
            }
        } // for

        return retval;
    }
    
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Deterine if a value is in a range. <p>
     *
     * @param value The value to check.
     * @param low Low range value (inclusive).
     * @param high High range value (inclusive).
     * @return True if in range, false otherwise.
     */
    public static boolean valueInRange(
            byte value,
            byte low,
            byte high)
    {
        return ((value >= low) && (value <= high));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Deterine if a value is in a range. <p>
     *
     * @param value The value to check.
     * @param low Low range value (inclusive).
     * @param high High range value (inclusive).
     * @return True if in range, false otherwise.
     */
    public static boolean valueInRange(
            short value,
            short low,
            short high)
    {
        return ((value >= low) && (value <= high));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Deterine if a value is in a range. <p>
     *
     * @param value The value to check.
     * @param low Low range value (inclusive).
     * @param high High range value (inclusive).
     * @return True if in range, false otherwise.
     */
    public static boolean valueInRange(
            int value,
            int low,
            int high)
    {
        return ((value >= low) && (value <= high));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Deterine if a value is in a range. <p>
     *
     * @param value The value to check.
     * @param low Low range value (inclusive).
     * @param high High range value (inclusive).
     * @return True if in range, false otherwise.
     */
    public static boolean valueInRange(
            long value,
            long low,
            long high)
    {
        return ((value >= low) && (value <= high));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Calculate a crc32 on an input stream. <p>
     *
     * @param is1 The input stream.
     * @return The crc32.
     * @throws IOException If the calculation failed.
     */
    public static long calcCrc32(InputStream is1)
        throws IOException
        {
        int numRead = 0;
        CRC32 crc32 = new CRC32();
        byte[] buffer = new byte[8192];

        // Init.
        crc32.reset();

        // Calc.
        while ((numRead = is1.read(buffer)) != -1)
        {
            crc32.update(buffer,0,numRead);
        } // while

        return crc32.getValue();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get package name (minus the class) from a string. <p>
     *
     * @param str String containing package name.
     * @return The package name.
     */
    public static String getPackageName(String str)
    {
        String packageName = str;
        int index = packageName.lastIndexOf('.');
        if (index > 0)
        {
            packageName = packageName.substring(0,index);
        }
        else
        {
            packageName = "";
        }

        return packageName;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get package name (minus the class) of an object. <p>
     *
     * @param obj The object.
     * @return The package name.
     */
    public static String getPackageName(Object obj)
    {
        return getPackageName(obj.getClass().getName());
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get class name (minus the package) from a string. <p>
     *
     * @param str String containing class name.
     * @return The class name.
     */
    public static String getClassName(String str)
    {
        String className = str;
        int index = className.lastIndexOf('.');
        if (index > 0)
        {
            className = className.substring(index+1);
        }

        return className;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get class name (minus the package) of an object. <p>
     *
     * @param obj The object.
     * @return The class name.
     */
    public static String getClassName(Object obj)
    {
        return getClassName(obj.getClass().getName());
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get method name from a stack trace. <p>
     * Useful to get name of currently executing method.
     * The return value formatted like: className.methodName:lineNumber.
     *
     * @param t Throwable.
     * @param offset Method offset (0 = 1st method in stack trace).
     * @param addClassName true = add class name in return value.
     * @param addLineNumber true = add line number in return value.
     * @return The value.
     */
    public static String getMethodName(
            Throwable t,
            int       offset,
            boolean   addClassName,
            boolean   addLineNumber)
    {
        String retval = "Unable to get method name.";

        // Sanity.
        if (t != null)
        {
            // Get stack trace.
            StackTraceElement[] ste = t.getStackTrace();

            // Check.
            if ((ste.length > 0)
                &&
                (offset >= 0)
                &&
                (offset < ste.length))
            {
                retval = "";

                if (addClassName)
                {
                    retval = retval + ste[offset].getClassName() + ".";
                }

                retval = retval + ste[offset].getMethodName();

                if (addLineNumber)
                {
                    retval = retval + ":" + ste[offset].getLineNumber();
                }
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Sleep current thread. <p>
     *
     * @param ms Sleep duration in ms.
     * @return true if slept total duration, false if not.
     */
    public static boolean sleep(long ms)
    {
        boolean retval = true;
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            retval = false;
        }
        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert an exception stack trace to a multi line string. <p>
     *
     * @param e The exception.
     * @return The string.
     */
    public static String exceptionStackTraceToString(Exception e)
    {
        StringBuilder sb = new StringBuilder(200);

        StackTraceElement[] steArray = e.getStackTrace();
        for (StackTraceElement ste : steArray)
        {
            EdtStringUtil.appendLine(sb,ste.toString());
        }
        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Find dirs/files in the file system. <p>
     *
     * @param path Starting path.
     * @param dirFilter Find dir filter or null.
     *        If dir names contain filter string they are added.
     * @param fileFilter Find dir filter or null.
     *        If dir names contain filter string they are added.
     * @param maxDepth Max search depth (from starting path).
     * @return Dirs/files that were found.
     *         Key = Entry canonical path string,
     *         Value = Entry file object.
     */
    public static Hashtable<String,File> findFsFile(
            String   path,
            String[] dirFilter,
            String[] fileFilter,
            int      maxDepth)
    {
        Hashtable<String,File> entries = new Hashtable<String,File>();

        try
        {
            // Init.
            File file = new File(path);
            String dirCanPath = file.getCanonicalPath();
            int cnt = EdtStringUtil.stringValueCount(
                    dirCanPath,System.getProperty("file.separator").charAt(0));

            // Adjust max depth based on current depth.
            if (maxDepth > 0)
            {
                maxDepth += cnt;
            }
            else
            {
                maxDepth = cnt;
            }

            // Do the find.
            privateFindFsFile(path,
                              dirFilter,
                              fileFilter,
                              maxDepth,
                              entries);

        }
        catch (Exception ex)
        {
        }

        return entries;
    }


    // ******************************************************************
    // PROTECTED METHODS.
    // ******************************************************************

    // ******************************************************************
    // PRIVATE METHODS.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Find dirs/files in the file system. <p>
     *
     * @param path Starting path.
     * @param dirFilter Find dir filter. If dir names contain filter
     *        string they are added.
     * @param fileFilter Find dir filter. If dir names contain filter
     *        string they are added.
     * @param maxDepth Max search depth (from starting path).
     * @entries Dirs/files that were found.
     *          Key = Entry canonical path string,
     *          Value = Entry file object.
     */
    private static void privateFindFsFile(
            String    path,
            String[]  dirFilter,
            String[]  fileFilter,
            int       maxDepth,
            Hashtable<String,File> entries)
    {
        // Process dir.
        File file = new File(path);
        if (file.isDirectory())
        {
            try
            {
                // Init.
                String dirCanPath = file.getCanonicalPath();
                int cnt = EdtStringUtil.stringValueCount(
                        dirCanPath,
                        System.getProperty("file.separator").charAt(0));

                // Max depth.
                if (cnt <= maxDepth)
                {
                    // Process dir.
                    File[] files = file.listFiles();
                    for (int fileIndex=0; fileIndex<files.length; fileIndex++)
                    {
                        String fileCanPath = files[fileIndex].getCanonicalPath();

                        // Process files.
                        if ((files[fileIndex].isFile())
                            &&
                            (fileFilter != null))
                        {
                            // Check filter.
                            boolean addFile = false;
                            for (int filterIndex=0;
                                     filterIndex<fileFilter.length;
                                     filterIndex++)
                            {
                                if (fileFilter[filterIndex].equals("*"))
                                {
                                    addFile = true;
                                    break;
                                }
                                else
                                {
                                    if (fileCanPath.indexOf(fileFilter[filterIndex])>=0)
                                    {
                                        addFile = true;
                                        break;
                                    }
                                }
                            } // for
                            if ((addFile)
                                &&
                                (! entries.containsKey(fileCanPath)))
                            {
                                entries.put(fileCanPath,files[fileIndex]);
                            }
                        }

                        // Process dirs.
                        if (files[fileIndex].isDirectory())
                        {
                            if (dirFilter != null)
                            {
                                // Check filter.
                                boolean addDir = false;
                                for (int filterIndex=0;
                                         filterIndex<dirFilter.length;
                                         filterIndex++)
                                {
                                    if (dirFilter[filterIndex].equals("*"))
                                    {
                                        addDir = true;
                                        break;
                                    }
                                    else
                                    {
                                        if (fileCanPath.indexOf(dirFilter[filterIndex])>=0)
                                        {
                                            addDir = true;
                                            break;
                                        }
                                    }
                                } // for
                                if ((addDir)
                                    &&
                                    (! entries.containsKey(fileCanPath)))
                                {
                                    entries.put(fileCanPath,files[fileIndex]);

                                }
                            }

                            // Recursive find.
                            privateFindFsFile(fileCanPath,
                                              dirFilter,
                                              fileFilter,
                                              maxDepth,
                                              entries);
                        }
                    } // for
                }
            }
            catch (Exception ex) {}
        }
    }

    // ******************************************************************
    // INNER CLASSES.
    // ******************************************************************

    // ******************************************************************
    // NATIVE METHODS.
    // ******************************************************************

    // ******************************************************************
    // MAIN.
    // ******************************************************************
}

// ----------------------------------------------------------------------

/*
 * File: EdtStringUtil.java
 *
 * Desc: Edt string utilities.
 *
 * Gilbarco Inc. 2005
 *
 * History:
 *    2005.10.13.thu.Scott Turner  v01.0.01
 *       - Created.
 */

// ----------------------------------------------------------------------
package troubleshoot.util;

// ----------------------------------------------------------------------
/**
 * Edt string utilities. <p>
 */
public class EdtStringUtil
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

    // Valid hex characters.
    private static final String VALID_HEX_CHARS = "0123456789ABCDEFabcdef";

    // ******************************************************************
    // CONSTRUCTOR.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create. <p>
     */
    public EdtStringUtil() {}

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
     * Return result of toString() on obj if obj != null.
     * If obj is null return "null".
     *
     * The returned string will be <objName> + <obj>.toString() if
     * obj not null, else will be <objName> + "null".
     *
     * @param objName Object name.
     * @param obj Object to do toString() on.
     * @return The value.
     */
    public static String toString(
            String objName,
            Object obj)
    {
        if (obj != null)
        {
            return objName + obj.toString();
        }
        else
        {
            return objName + "null";
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create a string of a certain size filled w/a certain char. <p>
     *
     * @param size Size of created string.
     * @param fillChar String filled with this char.
     * @return The string.
     */
    public static String stringSize(int  size,
                                    char fillChar)
    {
        StringBuilder sb = new StringBuilder(size);
        for (int index=0; index<size; index++)
        {
            sb.append(fillChar);
        } // for
        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create a string of a certain size filled w/space chars. <p>
     *
     * @param size Size of created string.
     * @return The string.
     */
    public static String stringSize(int size)
    {
        return stringSize(size,' ');
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding or removing chars from
     * the right side of the string. <p>
     *
     * @param str String to modify.
     * @param size Size to make string.
     * @param fillChar Chars to add to string if needed.
     * @return The string.
     */
    public static String stringSizeRight(String str,
                                         int    size,
                                         char   fillChar)
    {
        int len = str.length();
        StringBuilder sb =
            new StringBuilder(str.substring(0,java.lang.Math.min(len,size)));

        if (len < size)
        {
            sb.append(stringSize(size-len,fillChar));
        }

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding spaces or removing chars from
     * the right side of the string. <p>
     *
     * @param str String to modify.
     * @param size Size to make string.
     * @return The string.
     */
    public static String stringSizeRight(String str,
                                         int    size)
    {
        return stringSizeRight(str,size,' ');
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding '0's or removing chars from
     * the right side of the string. <p>
     *
     * @param str String to modify.
     * @param size Size to make string.
     * @return The string.
     */
    public static String stringSizeRight0(String str,
                                          int    size)
    {
        return stringSizeRight(str,size,'0');
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding or removing chars from
     * the left side of the string. <p>
     *
     * @param str String to modify.
     * @param size Size to make string.
     * @param fillChar Chars to add to string if needed.
     * @return The string.
     */
    public static String stringSizeLeft(String str,
                                        int    size,
                                        char   fillChar)
    {
        int len = str.length();
        StringBuilder sb = new StringBuilder(str);

        if (len > size)
        {
            sb.delete(0,len-size);
        }
        else if (len < size)
        {
            sb.insert(0,stringSize((size-len),fillChar));
        }

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding spaces or removing chars from
     * the left side of the string. <p>
     *
     * @param str String to modify.
     * @param size Size to make string.
     * @return The string.
     */
    public static String stringSizeLeft(String str,
                                        int    size)
    {
        return stringSizeLeft(str,size,' ');
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding '0's or removing chars from
     * the left side of the string. <p>
     *
     * @param str String to modify.
     * @param size Size to make string.
     * @return The string.
     */
    public static String stringSizeLeft0(String str,
                                        int    size)
    {
        return stringSizeLeft(str,size,'0');
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding a fill char
     * to left size of string. <p>
     * If the string length is >= the size then do nothing.
     *
     * @param str String to modif.
     * @param size Min size of string.
     * @return Modified string.
     */
    public static String stringSizeLeftMin(
            String str,
            int    size,
            char   fillChar)
    {
        String retval = str;

        if (str.length() < size)
        {
            retval = stringSizeLeft(str,size,fillChar);
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding ' ' to left size of string. <p>
     * If the string length is >= the size then do nothing.
     *
     * @param str String to modif.
     * @param size Min size of string.
     * @return Modified string.
     */
    public static String stringSizeLeftMin(
            String str,
            int    size)
    {
        return stringSizeLeftMin(str,size,' ');
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Modify an existing string by adding '0' to left size of string. <p>
     * If the string length is >= the size then do nothing.
     *
     * @param str String to modif.
     * @param size Min size of string.
     * @return Modified string.
     */
    public static String stringSizeLeftMin0(
            String str,
            int    size)
    {
        return stringSizeLeftMin(str,size,'0');
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Count number of times a value is in the data. <p>
     *
     * @param str The data.
     * @param value Value to count.
     * @return Number of times value is in the data.
     */
    public static int stringValueCount(String str,
                                       byte   value)
    {
        int retval = 0;

        // Count.
        for (int index=0; index<str.length(); index++)
        {

            if ((byte)(int)str.charAt(index) == value) {
                retval++;
            }
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Count number of times a value is in the data. <p>
     *
     * @param str The data.
     * @param value Value to count.
     * @return Number of times value is in the data.
     */

    public static int stringValueCount(String str,
                                       char   value) {
        int retval = 0;

        // Count.
        for (int index=0; index<str.length(); index++)
        {
            if (str.charAt(index) == value)
            {
                retval++;
            }
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Dump array of strings into one string. <p>
     * A title is added if provided.
     * A dash line after title line is added if a count is provided.
     *
     * @param title Title string or null.
     * @param dashCount Dash count or 0.
     * @param strings Array of strings.
     * @return Array of strings.
     */
    public static String dumpStrings(String   title,
                                     int      dashCount,
                                     String[] strings)
    {
        StringBuilder sb = new StringBuilder();

        if ((title != null) && (title.length() > 0))
        {
            sb.append(title+"\n");
        }

        if (dashCount > 0)
        {
            sb.append(stringSize(dashCount,'-')+"\n");
        }

        if ((strings != null) && (strings.length > 0))
        {
            for (int index=0; index<strings.length; index++)
            {
                sb.append(strings[index]+"\n");
            } // for
        }

        return sb.toString();
    }


    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Print array of strings to stdout. <p>
     *
     * @param title Title string or null.
     * @param dashCount Dash count or 0.
     * @param strings Array of strings.
     */
    public static void printlnStrings(String   title,
                                      int      dashCount,
                                      String[] strings)
    {
        String str = dumpStrings(title,dashCount,strings);

        if (str.length() > 0)
        {
            System.out.println(str);
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Remove all non numeric chars from a string. <p>
     *
     * @param str The string.
     * @return String w/non numberic chars.
     */
    public static String removeNonNumericChars(String str)
    {
        StringBuilder sb = new StringBuilder();

        // Remove.
        int index = 0;
        while(index < str.length())
        {
            if (Character.isDigit(str.charAt(index)))
            {
                sb.append(str.charAt(index));
            }

            // Loop control.
            index++;
        } // while

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Remove all non alpha numeric chars from a string. <p>
     *
     * @param  str The string.
     * @return String w/o non alpha numeric chars.
     */
    public static String removeNonAlphaNumericChars(String str)
    {
        StringBuilder sb = new StringBuilder();

        // Remove.
        int index = 0;
        while(index < str.length())
        {
            if (Character.isLetterOrDigit(str.charAt(index)))
            {
                sb.append(str.charAt(index));
            }

            // Loop control.
            index++;
        } // while

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Remove non hex chars from a string. <p>
     *
     * @param str The string.
     * @return String w/o non hex chars.
     */
    public static String removeNonHexChars(String str)
    {
        StringBuilder sb = new StringBuilder(str.length());

        // Remove.
        int index = 0;
        while (index < str.length())
        {
            if (VALID_HEX_CHARS.indexOf(str.charAt(index)) != -1)
            {
                sb.append(str.charAt(index));
            }

            // Loop control.
            index++;

        } // while

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert all hex sequences, \xXX, to their ASCII chars. <p>
     * The XX in a \xXX represents a two digit hex-ascii number.
     * Two \ chars convert to a single \ char. If the \xXX sequence
     * is invalid for any reason it is converted to a '?' char.
     *
     * @param str1 The string to convert.
     * @return The converted string.
     */
    public static String doSlash(String str1)
    {
        StringBuilder sb1 = new StringBuilder();
        int index = 0;
        char c1 = 0;

        str1 = doSlashBinary(str1);
        // Process string.
        while (index < str1.length())
        {
            // Get next char.
            c1 = str1.charAt(index);

            // Look for slash sequence.
            if ((str1.startsWith("\\x",index))
                ||
                (str1.startsWith("\\X",index)))
            {
                index += 2;

                // Check length.
                if ((index+1) < str1.length())
                {
                    // Check chars.
//                    if (EdtUtil.isHex(str1.substring(index,index+2)))
                	if (isHex(str1.substring(index,index+2)))
                    {
                        // Convert.
                        c1 = (char)EdtConvert.stringToInt(str1.substring(index,index+2),16);
                    }
                    else
                    {
                        c1 = '?';
                    }
                }
                else
                {
                    c1 = '?';
                }
                index++;
            }
            else if (str1.startsWith("\\",index))
            {
                index ++;
                c1 = '\\';
            }

            // Append.
            sb1.append(c1);

        // Loop control.
        index++;
        } // while

        return sb1.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert all binary sequences, \bXXXXXXXX, to their ASCII chars. <p>
     * The XXXXXXXX in a \xXXXXXXXX represents a eight digit binary-ascii number.
     * it must be 8 len number and ceros or ones (0 or 1)
     * Two \ chars convert to a single \ char. If the \xXXXXXXXX sequence
     * is invalid for any reason it is converted to a '?' char.
     *
     * @param str1 The string to convert.
     * @return The converted string.
     */
    public static String doSlashBinary(String str1)
    {
        StringBuilder sb1 = new StringBuilder();
        int index = 0;
        char c1 = 0;

        // Process string.
        while (index < str1.length())
        {
            // Get next char.
            c1 = str1.charAt(index);

            // Look for slash sequence.
            if ((str1.startsWith("\\b",index))
                ||
                (str1.startsWith("\\B",index)))
            {
                index += 2;

                // Check length.
                if ((index+7) < str1.length())
                {
                    // Check chars.
//                    if (EdtUtil.isBinary(str1.substring(index,index+8)))
                	if (isBinary(str1.substring(index,index+8)))
                    {
                        // Convert.
                        c1 = (char)EdtConvert.stringToInt(str1.substring(index,index+8),2);
                    }
                    else
                    {
                        c1 = '?';
                    }
                }
                else
                {
                    c1 = '?';
                }
                index+= 7;
            }

            // Append.
            sb1.append(c1);

        // Loop control.
        index++;
        } // while

        return sb1.toString();
    }
    
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert all hex sequences, \xXX, to their ASCII chars. <p>
     * The XX in a \xXX represents a two digit hex-ascii number.
     * Two \ chars convert to a single \ char. If the \xXX sequence
     * is invalid for any reason it is converted to a '?' char.
     *
     * @param str1 The string to convert.
     * @param edtBufferSize Created buffer's size.
     * @return The converted string.
     */
    public static EdtBuffer doSlash(
            String str1,
            int    edtBufferSize)
    {
        EdtBuffer eb1 = new EdtBuffer(edtBufferSize);
        int index = 0;
        byte b1 = 0;

        // Process string.

        while (index < str1.length()) {
            // Look for slash sequence.
            if ((str1.startsWith("\\x",index))
                ||
                (str1.startsWith("\\X",index)))
            {
                index += 2;

                // Check length.
                if ((index+1) < str1.length())
                {
                    // Check chars.
//                    if (EdtUtil.isHex(str1.substring(index,index+2)))
                    if (isHex(str1.substring(index,index+2)))	
                    {
                        // Convert.
                        b1 = (byte)EdtConvert.stringToInt(str1.substring(index,index+2),16);
                    }
                    else
                    {
                        b1 = '?';
                    }
                }
                else
                {
                    b1 = '?';
                }
                index++;
            }
            else if (str1.startsWith("\\",index))
            {
                index ++;
                b1 = '\\';
            }

            // Append.
            eb1.append(b1);

        // Loop control.
        index++;
        } // while

        return eb1;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert some or all chars in a string to \xXX sequences. <p>
     * If not converting all then only chars outside of range 0x20..0x7f
     * will be converted.
     *
     * @param str1 The string to convert.
     * @param all Convert all (=true) or some (=false).
     * @return The converted string.
     */
    public static String toSlash(String  str1,
                                 boolean all)
    {
        StringBuilder sb1 = new StringBuilder();
        int index = 0;
        int  c1 = 0;
        char c2 = 0;

        // Process string.
        for (index = 0; index < str1.length(); index++)
        {
            // Next char.
            c1 = str1.charAt(index);
            c2 = str1.charAt(index);

            // Convert.
            if ((all == true)
                ||
                ((c1 < 0x20) || (c1 > 0x7f)))
            {
                sb1.append("\\x" +
                           EdtStringUtil.stringSizeLeft(EdtConvert.intToString(c1,16),2,'0'));
            }
            else
            {
                sb1.append(c2);
            }
        } // for

        return sb1.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert some or all chars in a string to \bXXXXYYYY sequences. <p>
     * If not converting all then only chars outside of range 0x20..0x7f
     * will be converted.
     *
     * @param data The EdtBuffer to convert.
     * @param all Convert all (=true) or some (=false).
     * @return The converted string.
     */
    public static String toSlashBinary(EdtBuffer data,
            boolean all)
    {
    	return toSlashBinary(data.getRealData(), all);
    
    }
    
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert some or all chars in a string to \bXXXXYYYY sequences. <p>
     * If not converting all then only chars outside of range 0x20..0x7f
     * will be converted.
     *
     * @param data The byte array to convert.
     * @param all Convert all (=true) or some (=false).
     * @return The converted string.
     */
    public static String toSlashBinary(byte[]  data,
            boolean all)
    {
    	return toSlashBinary(data, all);
    
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert some or all chars in a string to \bXXXXYYYY sequences. <p>
     * If not converting all then only chars outside of range 0x20..0x7f
     * will be converted.
     *
     * @param str1 The string to convert.
     * @param all Convert all (=true) or some (=false).
     * @return The converted string.
     */
    public static String toSlashBinary(String  str1,
                                 boolean all)
    {
        StringBuilder sb1 = new StringBuilder();
        int index = 0;
        int  c1 = 0;
        char c2 = 0;

        // Process string.
        for (index = 0; index < str1.length(); index++)
        {
            // Next char.
            c1 = str1.charAt(index);
            c2 = str1.charAt(index);

            // Convert.
            if ((all == true)
                ||
                ((c1 < 0x20) || (c1 > 0x7f)))
            {

            	//make it a unsigned integer just in case
        		String s = Integer.toBinaryString(c1 & 0xFF);
            	sb1.append("\\b" +
                           EdtStringUtil.stringSizeLeft(s,8,'0'));
                
            }
            else
            {
                sb1.append(c2);
            }
        } // for

        return sb1.toString();
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert some or all bytes to \xXX sequences. <p>
     * If not converting all then only bytes outside of range 0x20..0x7f
     * will be converted.
     *
     * @param data The data to convert.
     * @param all Convert all (=true) or some (=false).
     * @return The converted string.
     */
    public static String toSlash(
            byte[] data,
            boolean all)
    {
        return toSlash(data,data.length,all);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert some or all bytes to \xXX sequences. <p>
     * If not converting all then only bytes outside of range 0x20..0x7f
     * will be converted.
     *
     * @param data The data to convert.
     * @param all Convert all (=true) or some (=false).
     * @return The converted string.
     */
    public static String toSlash(
            byte data,
            boolean all)
    {
        byte[] b = new byte[1];
        b[0] = data;

        return toSlash(b,all);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert some or all bytes to \xXX sequences. <p>
     * If not converting all then only bytes outside of range 0x20..0x7f
     * will be converted.
     *
     * @param data The data to convert.
     * @param len Length of data to convert.
     * @param all Convert all (=true) or some (=false).
     * @return The converted string.
     */
    public static String toSlash(
            byte[] data,
            int len,
            boolean all)
    {
        StringBuilder sb1 = new StringBuilder();
        int index = 0;
        int c1 = 0;

        // Real length.
        len = Math.min(len,data.length);

        // Process string.
        for (index = 0; index < len; index++)
        {
            // Next char.
            c1 = data[index];

            // Convert.
            if ((all == true)
                ||
                ((c1 < 0x20) || (c1 > 0x7f)))
            {
                sb1.append("\\x" +
                           EdtStringUtil.stringSizeLeft(EdtConvert.intToString((c1&0xff),16),2,'0'));
            }
            else
            {
                sb1.append((char)c1);
            }
        } // for

        return sb1.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert all non printable chars in a string to <chr1> to make the
     * string printable. Non printable chars are < 0x20 or > 0x7f.  <p>
     *
     * @param str1 The string to make printable.
     * @param chr1 Non printable replacement char.
     * @return The converted string.
     */
    public static String printable(String str1,
                                   char   chr1)
    {
        if (str1 != null)
        {
            StringBuilder sb1 = new StringBuilder(str1);

            // Process string.
            char c1 = 0;
            for (int index=0; index<sb1.length(); index++)
            {
                c1 = sb1.charAt(index);
                if ((c1 < 0x20) || (c1 > 0x7f))
                {
                    sb1.setCharAt(index,chr1);
                }
            } // for

            return sb1.toString();
        }
        else
        {
            return "";
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert all non printable chars in a string to '.' to make the
     * string printable. Non printable chars are < 0x20 or > 0x7f.  <p>
     *
     * @param str1 The string to make printable.
     * @return The converted string.
     */
    public static String printable(String str1)
    {
        return(printable(str1,'.'));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append newline. <p>
     *
     * @param sb Dest buffer.
     */
    public static void appendLine(StringBuilder sb)
    {
        sb.append("\n");
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append string. <p>
     * A newline is added after the string is added.
     *
     * @param sb Dest buffer.
     * @param str Appended to dest buffer.
     */
    public static void appendLine(StringBuilder sb,
                                  String        str)
    {
        sb.append(str+"\n");
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append lines putting a filler at beginning of each line. <p>
     * Lines should be terminated with \n.
     *
     * @param sb Dest buffer.
     * @param lines String lines to append.
     * @param fillerSize Filler size, >= 0.
     * @param fillerChar Filler char.
     * @param fillFirstLine True will fill 1st line.
     */
    public static void appendLinesWithFiller(
            StringBuilder sb,
            String        lines,
            int           fillerSize,
            char          fillerChar,
            boolean       fillFirstLine)
    {
        // Fill 1st line if needed.
        if (fillFirstLine)
        {
            sb.append(stringSize(fillerSize,fillerChar));
        }

        // Append and fill lines.
        for (int index=0; index<lines.length(); index++)
        {
            // Get char.
            char c1 = lines.charAt(index);

            // Append.
            sb.append(c1);

            // Process based on char.
            if (c1 == '\n')
            {
                // Filler if more string.
                if (index < lines.length())
                {
                    sb.append(stringSize(fillerSize,fillerChar));
                }
            }
        } // for
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get byte array from a string. <p>
     * The string will be slash processed.
     *
     * This should be used instead of new String(... as doing
     * new String(... with data with bytes > 0x7F does not yield
     * the correct string.
     *
     * @param s1 String.
     * @return Byte array[].
     */
    public static byte[] getByteArrayDoSlash(String s1)
    {
        return getByteArray(doSlash(s1));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get byte array from a string. <p>
     *
     * This should be used instead of the string's getBytes() as the
     * string's getBytes() does not return correct byte values for
     * chars w/values > 0x7f.
     *
     * Better yet, don't use string as a general purpose buffer,
     * use EdtBuffer instead.
     *
     * @param s1 String.
     * @return Byte array.
     */
    public static byte[] getByteArray(String s1)
    {
        byte[] retval = null;

        // Sanity.
        if (s1 != null)
        {
            // New.
            retval = new byte[s1.length()];

            // Correctly copy.
            int len = s1.length();
            for (int index=0; index<len; index++)
            {
                retval[index] = (byte)((int)s1.charAt(index) & 0xff);
            } // for
        }
        else
        {
            retval = new byte[0];
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create a new string from input data (in slash format). <p>
     * This data will be slashed processed.
     *
     * This should be used instead of new String(... as doing
     * new String(... with data with bytes > 0x7F does not yield
     * the correct string.
     *
     * @param data Input data.
     * @return The string.
     */
    public static String newStringDoSlash(String data)
    {
        return newString(getByteArray(doSlash(data)));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create a new string from a byte[]. <p>
     *
     * This should be used instead of new String(... as doing
     * new String(... with data with bytes > 0x7F does not yield
     * the correct string.
     *
     * Better yet, don't use string as a general purpose buffer,
     * use EdtBuffer instead.
     *
     * @param data The byte[].
     * @return The string.
     */
    public static String newString(
            byte[] data)
    {
        String retval = null;

        if (data != null)
        {
            retval = newString(data,0,data.length);
        }
        else
        {
            retval = "";
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create a new string from a byte[]. <p>
     *
     * This should be used instead of new String(byte[]... as doing
     * new String(... when data is a byte[] with bytes > 0x7F does not
     * yield the correct string.
     *
     * Better yet, don't use string as a general purpose buffer,
     * use EdtBuffer instead.
     *
     * @param data The byte[].
     * @param offset Offset into data.
     * @param len Number of data bytes to put in new string.
     * @return The string.
     */
    public static String newString(
            byte[] data,
            int    offset,
            int    len)
    {
        String retval = null;

        // Sanity.
        if (data != null)
        {
            // Chars.
            char[] ca = new char[len];

            // Length.
            len = Math.min(len,data.length);

            // Copy.
            for (int index=0; index<len; index++)
            {
                ca[index] = (char)((int)data[offset+index] & 0xff);
            } // for

            retval = new String(ca);
        }
        else
        {
            retval = "";
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Dump string in string form. <p>
     *
     * @param sb Dump buffer. null will allocate new buffer.
     * @param title Title or null.
     * @param headerSize Header size.
     * @param data String to dump.
     * @return The dump.
     */
    public static StringBuilder dump(
            StringBuilder sb,
            String        title,
            int           headerSize,
            String        data)
    {
        StringBuilder sb1 = new StringBuilder(100);

        // Title.
        if (title != null)
        {
            sb1.append(title);
        }

        // Data.
        if (data != null)
        {
            while (sb1.length() < headerSize)
            {
                sb1.append(' ');
            } // while

            sb.append('=');
            sb.append(data);
        }
        else
        {
            while (sb1.length() < headerSize)
            {
                sb1.append(' ');
            } // while

            sb1.append("= null");
        }

        if (sb == null)
        {
            sb = sb1;
        }
        else
        {
            sb.append(sb1);
        }

        return sb;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Determine if all characters in a string are digits. <p>
     *
     * @param s String to check.
     * @return true or false.
     */
    public static boolean isStringNumeric(String s)
    {
        boolean retval = true;

        int strLen = s.length();
        for (int index=0; index<strLen; index++)
        {
            if (! Character.isDigit(s.charAt(index)))
            {
                retval = false;
                break;
            }
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Determine if all chars in a string are printable. <p>
     * Printable means the char is between (inclusive) 0x20 and 0x7F.
     *
     * @param s String to check.
     * @return true if printable, else not printable.
     */
    public static boolean isStringPrintable(String s)
    {
        return isStringPrintable(s,null);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Determine if all chars in a string are printable. <p>
     * Printable means the char is between (inclusive) 0x20 and 0x7F.
     *
     * @param s String to check.
     * @param extraChars Extra characters that should be considered printable.
     * @return true if printable, else not printable.
     */
    public static boolean isStringPrintable(
            String s,
            String extraChars)
    {
        boolean retval = true;

        if (s != null)
        {
            int sLen = s.length();
            for (int index=0; index<sLen; index++)
            {
                // Check for character in extra chars.
                boolean isExtraChar = false;
                if ((extraChars != null)
                        &&
                        (extraChars.indexOf(s.charAt(index)) != -1))
                {
                    isExtraChar = true;
                }

                // If not extra, do normal check.
                if (!isExtraChar)
                {
                    if ((s.charAt(index) < 0x20)
                            ||
                            (s.charAt(index) > 0x7F))
                    {
                        // Found non printable char, stop looking.
                        retval = false;
                        break;
                    }
                }

            } // for
        }
        else
        {
            retval = false;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Find a string value in an array of strings. <p>
     *
     * @param array Array to check.
     * @param arrayLen How many chars (from left side) to use in check (0=all);
     * @param value Value to find.
     * @param valueLen How many chars (from left side) to use in check (0 = all);
     * @return true if value in array, false otherwise.
     */
    public static boolean arrayContainsValue(
            String[] array,
            int      arrayLen,
            String   value,
            int      valueLen)
    {
        boolean retval = false;

        if ((array != null) && (array.length != 0)
            &&
            (value != null))
        {
            for (String a : array)
            {
                String aCheck = (arrayLen == 0) ? a : a.substring(0,arrayLen);
                String vCheck = (valueLen == 0) ? value : value.substring(0,valueLen);

                if (aCheck.equals(vCheck))
                {
                    retval = true;
                    break;
                }
            } // for
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * String check. <p>
     * Checks: str != null and str.length > 0.
     *
     * @param str String to check.
     * @return true if checks passed, else false.
     */
    public static boolean strchk(String str)
    {
        return ((str != null) && (str.length() > 0));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * String check. <p>
     * Checks: str != null and str.equals(check).
     *
     * @param str String to check.
     * @param check Check value.
     * @return true if checks passed, else false.
     */
    public static boolean strchk(String str, String check)
    {
        return ((str != null) && str.equals(check));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get string (in pascal format: 1st byte = length). <p>
     *
     * @param data Data.
     * @param offset Offset.
     * @return The value.
     */
    public static String getPascalString(
            byte[] data,
            int    offset)
    {
        int length = data[offset++] & 0xFF;

        if (length > 0)
        {
            return EdtStringUtil.newString(data,offset,length);
        }
        else
        {
            return "";
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Insert value between each character in a string. <p>
     *
     * @param str Input string.
     * @param insertValue Value to insert.
     * @param insertValueAfterLastChar If true then will insert
     *        value after last char in string. If false then will not.
     * @return Output string.
     */
    public static String insertBetweenEachChar(
    		String  str,
    		String  insertValue,
    		boolean insertValueAfterLastChar)
    {
    	// Calc length of final string (+x for insert value after last char).
    	int len = str.length() + (str.length() * insertValue.length());

    	StringBuilder sb = new StringBuilder(len);

    	for (int index=0; index<str.length(); index++)
    	{
    		sb.append(str.charAt(index));
    		if (index < str.length()-1)
    		{
        		sb.append(insertValue);
    		}
    	} // for

    	if (insertValueAfterLastChar)
    	{
    		sb.append(insertValue);
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
    public static boolean isHex(char ch)
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
    public static boolean isBinary(char ch)
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
    
    public static boolean isNullOrEmpty(String str) {
    	return str == null || str.equals("");
    }
    // ******************************************************************
    // PROTECTED METHODS.
    // ******************************************************************

	public static String trimLeadingZeros(String source)
	{
		for (int i = 0; i < source.length(); ++i) 
	    { 
	        char c = source.charAt(i); 
	        if (c != '0' && !Character.isSpaceChar(c)) 
	            return source.substring(i); 
	    } 
		return source;
	}

	public static boolean isAlphaNumericString(String text)
	{
		for(int i=0; i < text.length( ); ++i)
		{
			if( !Character.isLetterOrDigit(text.charAt(i)) )
				return false;
		}
		return true;
	}
	

    // ******************************************************************
    // PRIVATE METHODS.
    // ******************************************************************

    // ******************************************************************
    // INNER CLASSES.
    // ******************************************************************

    // ******************************************************************
    // NATIVE METHODS.
    // ******************************************************************

    // ******************************************************************
    // MAIN.
    // ******************************************************************
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}

//----------------------------------------------------------------------

/*
 * File: EdtConvert.java
 *
 * Desc: Edt conversion methods.
 *
 * Gilbarco Inc. 2005
 *
 * History:
 *    2005.04.07.wed.Scott Turner  v01.0.01
 *       - Created.
 */

//  ----------------------------------------------------------------------
package troubleshoot.util;

import java.math.BigInteger;
import java.util.Formatter;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;

//  ----------------------------------------------------------------------
/**
 * Conversion methods. <p>
 *
 * @author Scott Turner
 */
 public class EdtConvert
 {
	 private final static Logger logger = Logger.getLogger(TroubleshootController.class);
	 
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
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block of bytes to convert.
     * @param off Offset into block.
     * @param len Length of bytes to convert.
     * @param skip Add skip space after skip many ascii-hex pairs.
     * @return The converted string.
     */
    public static String bytesToBinaryString(byte[] block,
                                          int    off,
                                          int    len,
                                          int    skip,
                                          String separator,
                                          String prefix)
    {
        StringBuilder buf = new StringBuilder();
        int count = 0;
        //safety check
        if(separator == null)
        {
        	separator = "";	
        }
        if(prefix == null)
        {
          prefix = "";	
        }

        // Sanity.
        if (block != null)
        {
            // Convert.
            for (int i = off; i < off + len; ++i )
            {
                //make it a unsigned integer just in case
        		String s = Integer.toBinaryString(block[i] & 0xFF);
        		//padd missing zeros.
            	buf.append(prefix+EdtStringUtil.stringSizeLeft(s,8,'0'));
                
                // Skip.
                if (skip > 0)
                {
                    count++;
                    if (count >= skip)
                    {
                        count = 0;
                        buf.append(separator);
                    }
                }
            } // for
        }
        else
        {
            buf.append("");
        }

        return  buf.toString();
    }
    
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block of bytes to convert.
     * @param off Offset into block.
     * @param len Length of bytes to convert.
     * @param skip Add skip space after skip many ascii-hex pairs.
     * @param separator for the output.
     * @return The converted string.
     */
    public static String bytesToHexString(byte[] block,
                                          int    off,
                                          int    len,
                                          int    skip,
                                          char   separator)
    {
        String hexits = "0123456789ABCDEF";
        StringBuilder buf = new StringBuilder();
        int count = 0;

        // Sanity.
        if (block != null)
        {
            // Convert.
            for (int i = off; i < off + len; ++i )
            {
                buf.append( hexits.charAt( ( block[i] >>> 4 ) & 0xf ) );
                buf.append( hexits.charAt( block[i] & 0xf ) );

                // Skip.
                if (skip > 0)
                {
                    count++;
                    if (count >= skip)
                    {
                        count = 0;
                        buf.append(separator);
                    }
                }
            } // for
        }
        else
        {
            buf.append("");
        }

        return buf.toString();
    }
    
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block of bytes to convert.
     * @param off Offset into block.
     * @param len Length of bytes to convert.
     * @param skip Add skip space after skip many ascii-hex pairs.
     * @return The converted string.
     */
    public static String bytesToHexString(byte[] block,
                                          int    off,
                                          int    len,
                                          int    skip)
    {
    
    	return bytesToHexString(block, off, len, skip, ' ');
    }    

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block of bytes to convert.
     * @param off Offset into block.
     * @param len Length of bytes to convert.
     * @return The converted string.
     */
    public static String bytesToHexString(byte[] block,
                                          int    off,
                                          int    len)
    {
        return bytesToHexString(block,off,len,0);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block of bytes to convert.
     * @return The converted string.
     */
    public static String bytesToHexString(byte[] block)
    {
       // Sanity
       if (block == null)
       {
          return "";
       }
        return bytesToHexString(block,0,block.length,0);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block of bytes to convert.
     * @param skip Add skip space after skip many ascii-hex pairs.
     * @return The converted string.
     */
    public static String bytesToHexString(byte[] block,
                                          int    skip)
    {
       // Sanity
       if (block == null)
       {
          return "";
       }
        return bytesToHexString(block,0,block.length,skip);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block to convert.
     * @return The converted string.
     */
    public static String bytesToHexString(EdtBuffer block)
    {
       // Sanity
       if (block == null)
       {
          return "";
       }
        return bytesToHexString(block.data,0,block.length,0);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block to convert.
     * @param skip Add skip space after skip many ascii-hex pairs.
     * @return The converted string.
     */
    public static String bytesToHexString(EdtBuffer block,
                                          int       skip)
    {
       // Sanity
       if (block == null)
       {
          return "";
       }
        return bytesToHexString(block.data,0,block.length,skip);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block to convert.
     * @return The converted string.
     */
    public static String bytesToHexString(String block)
    {
       // Sanity
       if (block == null)
       {
          return "";
       }
        return bytesToHexString(
                EdtStringUtil.getByteArray(block),
                0,
                block.length(),
                0);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bytes to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param block Block to convert.
     * @param skip Add skip space after skip many ascii-hex pairs.
     * @return The converted string.
     */
    public static String bytesToHexString(String block,
                                          int    skip)
    {
       // Sanity
       if (block == null)
       {
          return "";
       }
        return bytesToHexString(
                EdtStringUtil.getByteArray(block),
                0,
                block.length(),
                skip);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert a single byte to an Ascii-Hex (base 16) string. <p>
     * The converted string will be uppercase.
     *
     * @param byte1 Byte to convert.
     * @return The converted string.
     */
    public static String byteToHexString(byte byte1)
    {
        byte[] block = new byte[1];
        block[0] = byte1;
        return bytesToHexString(block);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert byte to Ascii string. <p>
     * If the byte is less than 0x20: conversion = byte to Ascii name.
     * If the byte is larger than 0x7f: conversion = byte to Ascii-Hex.
     * Else: conversion = (char)byte1.
     *
     * @param byte1 The byte to convert.
     * @param handleSpace If true then 0x20 converts to space char, else as 0x20.
     * @return The converted string.
     */
    public static String byteToAsciiString(byte byte1,
                                           boolean handleSpace)
    {
        StringBuilder sb = new StringBuilder();

        // Convert.
        if (byte1 < 0x20)
            sb.append(EdtByteUtil.toAsciiString(byte1));
        else if(byte1 == 0x20)
                if (handleSpace)
                {
                    sb.append(" ");
                }
                else
                {
                    sb.append("0x20");
                }
        else if (byte1 == 0x7f)
        {
            sb.append("DEL");
        }
        else if (byte1 > 0x7f)
        {
            sb.append("x"+byteToHexString(byte1));
        }
        else
        {
            sb.append((char)byte1);
        }

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert string in Ascii-Hex (base 16) to byte[]. <p>
     * Spaces are removed before conversion.
     * If the conversion failed the returned byte[] will be
     * set to all zeros.
     *
     * @param block The result of the conversion.
     * @param str The string to convert.
     * @return Number of bytes converted (-1 if conversion failed).
     */
    public static int hexStringToBytes(byte[] block, String str)
    {
        int retval = 0;
        int dIndex = 0;
        int sIndex = 0;

        // Remove all spaces.
        StringBuilder sb = new StringBuilder(str);
        int spaceIndex = sb.indexOf(" ");
        while (spaceIndex >= 0)
        {
            sb.deleteCharAt(spaceIndex);
            spaceIndex = sb.indexOf(" ");
        } // while

        // Pad in case last pair of ascii-hex chars is not a pair.
        // Will be ignored if last pair is a pair.
        int sbLen = sb.length();
        sb.append("0");
        retval = sbLen;

        try
        {
            // Convert.
            while (sIndex < sbLen)
            {
                block[dIndex++] = Integer.valueOf(sb.substring(sIndex,sIndex+2),16).byteValue();
                sIndex += 2;
            } //while
        }
        catch (NumberFormatException ex)
        {
            retval=-1;
            for (int index=0; index<block.length; index++)
            {
                block[index]=(byte)0x00;
            } // for
        }

        return retval;
    }
    
    
    public static byte[] hexStringToByteArray(String s) 
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        
        for (int i = 0; i < len; i += 2) 
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert hex string to byte array. <p>
     *
     * The hex string may be in one of two forms.
     * Form1 is edt slash form.
     * Form2 is ascii-hex chars (spaces are ignored, 0x are removed).
     *
     * If form2 is used then the converted string
     * will be right padded with '0' to make the
     * string length even before it is converted.
     *
     * @param s1 The string to convert.
     * @return Byte array from conversion.
     */
    public static byte[] hexStringToBytes(String s1)
    {
        byte[] retval = null;
        int newLength   = 0;

        if (s1.indexOf("\\") >= 0)
        {
            retval = EdtStringUtil.getByteArrayDoSlash(s1);
        }
        else
        {
            // Remove 0x and 0X sequences.
            if (s1.indexOf("0x") >= 0)
            {
                s1 = s1.replaceAll("0x","");
            }
            if (s1.indexOf("0X") >= 0)
            {
                s1 = s1.replaceAll("0X","");
            }
            // Subtract # of spaces from length.
            newLength =
                s1.length() -
                EdtStringUtil.stringValueCount(
                        s1,' ');

            // Length must be even.
            if (newLength > 0)
            {
                // Make even.
                if ((newLength & 0x01) != 0)
                {
                    newLength++;
                }

                // Divide by 2 to get real size of
                // byte array needed for conversion.
                newLength /= 2;
            }

            retval = new byte[newLength];
            EdtConvert.hexStringToBytes(retval,s1);
        }

        return retval;
    }

	/**
	 * Convert an hex symbol to a single byte.
	 * @param char c  The nibble Ascii-hex symbol to be converted
	 * @return a byte in the range 0..15
	 */
	private static byte hexNibbleToByte(char c)
	{
		if(c >= '0' && c <= '9')
			return (byte) ( c - '0');
		else if(c >= 'A' && c <= 'F')
			return (byte) ( c - 'A' + 10);
		else if(c >= 'a' && c <= 'f')
			return (byte) ( c - 'a' + 10);
		else
			return (byte) 0xFF;
	}

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert string in Ascii-Hex (base 16) to byte[ ]. <p>
     * Each group of two consecutives hex digits must be followed by the specified separator,
     * except for the last group. The conversion will fail if ANY of the separator characters
     * in the string are found different from the one specified in the function parameter.
     * In this case the returned byte[] will be set to null.
     *
     * @param String : sHexData The string to convert.
     * @param char : cSeparator The specified separator character between hex digits couples.
     * @return A new byte array or null if conversion failed due separator mismatches.
     */
	public static byte[] hexStringToBytes(String sHexData, char cSeparator)
	{
		int iLength =  sHexData.length() / 3;
		int iReminder = sHexData.length() % 3;
		if( iReminder == 1) // last hex nibble is missing !
		{
			String sError = "Format ERROR in ASCII signature, length is " + sHexData.length() + " !! Conversion impossible.\n";
			logger.error(sError);
			return null;
		}

		iLength += ( iReminder == 0 ? 0 : 1 );
		byte[] bOut = null;

		for(int i = 0; i < iLength ; i++)
		{
			byte bh, bl;
			char cSepRead;
			String sFormatError = "Format error reading signature file, offset %d, ";

			bh = hexNibbleToByte( sHexData.charAt(3*i) );
			bl = hexNibbleToByte( sHexData.charAt(3*i + 1) );
			if( bh == 0xFF || bl == 0xFF )
			{
				Formatter f = new Formatter();
				f.format(sFormatError + "byte: %c%c . Conversion aborted.\n",
									3*i, sHexData.charAt(3*i), sHexData.charAt(3*i + 1));
				logger.error(f.out().toString());
				f.close( );
				return null;
			}

			if(i == iLength - 1) // it is the last iteration.
				cSepRead = cSeparator; // virtual final separator (actually EOF in file)
			else
				cSepRead = sHexData.charAt(3*i + 2);

			if( cSepRead == cSeparator ) // format coincidence
			{
				if(bOut == null)
					bOut = new byte[iLength];

				bOut[i] =  (byte) (16 * bh + bl) ;
			}
			else
			{
				Formatter f = new Formatter();
				f.format(sFormatError + "separator: %c . Conversion aborted.\n",
									3*i + 2, cSepRead);
				logger.error(f.out().toString());
				f.close( );
				return null;
			}
		}

		return bOut;
	}

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert hex string to byte array. <p>
     *
     * The hex string may be in one of two forms.
     * Form1 is edt slash form.
     * Form2 is ascii-hex chars (spaces are ignored, 0x are removed).
     *
     * If form2 is used then the converted string
     * will be left padded with padChar to make the
     * string length even before it is converted.
     *
     * @param s1 The string to convert.
     * @param padChar Char to pad on left if needed.
     * @return Byte array from conversion.
     */
    public static byte[] hexStringToBytesLeft(
            String s1,
            char   padChar)
    {
        byte[] retval = null;
        int newLength   = 0;

        if (s1.indexOf("\\") >= 0)
        {
            retval = EdtStringUtil.getByteArrayDoSlash(s1);
        }
        else
        {
            // Remove 0x and 0X sequences.
            if (s1.indexOf("0x") >= 0)
            {
                s1 = s1.replaceAll("0x","");
            }
            if (s1.indexOf("0X") >= 0)
            {
                s1 = s1.replaceAll("0X","");
            }

            // Subtract # of spaces from length.
            newLength =
                s1.length() -
                EdtStringUtil.stringValueCount(
                        s1,' ');

            // Length must be even.
            if (newLength > 0)
            {
                // Make even.
                if ((newLength & 0x01) != 0)
                {
                    newLength++;

                    s1 = padChar + s1;
                }

                // Divide by 2 to get real size of
                // byte array needed for conversion.
                newLength /= 2;
            }

            retval = new byte[newLength];
            EdtConvert.hexStringToBytes(retval,s1);
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert ascii chars in a string to a number. <p>
     * The radix is determined by how the string starts.
     *
     *    Radix     String starts with
     *    --------  --------------------
     *    16        "0x", "0X", "\x" or "\X"
     *    10        None of the above.
     *
     * @param str The string to convert.
     * @return The conversion or 0 if conversion failed.
     */
    public static int stringToInt(String str)
    {
        int retval = 0;

        // Sanity.
        if (str != null)
        {
            int radix = 10;

            str = str.trim();

            // Check for radix 16.
            if ((str.startsWith("0x"))
                 ||
                (str.startsWith("0X"))
                ||
                (str.startsWith("\\x"))
                ||
                (str.startsWith("\\X")))
            {
                str = str.substring(2);
                radix = 16;
            }

            retval = stringToInt(str,0,str.length(),radix);
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert ascii chars in a string to a number. <p>
     *
     * @param str The string to convert.
     * @param radix Number's radix.
     * @return The conversion or 0 if conversion failed.
     */
    public static int stringToInt(String str,
                                  int radix)
    {
        int retval = 0;

        if (str != null)
        {
            retval = stringToInt(str,0,str.length(),radix);
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert ascii chars in a string to a number. <p>
     *
     * @param str The string to convert.
     * @param index Index into string.
     * @param size Number of chars to convert, range is: 0..4.
     * @param radix Number's radix.
     * @return The conversion or 0 if conversion failed.
     */
    public static int stringToInt(String str,
                                  int index,
                                  int size,
                                  int radix)
    {
        int retval = 0;

        // Sanity.
        if ((str != null) && (str.length() > 0))
        {
            try
            {
                str =  str.substring(index,index+size);

                BigInteger bi = new BigInteger(str,radix);

                retval = bi.intValue();
            }
            catch (Exception ex)
            {
                retval = 0;
                logger.error(ex);
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert number to a string. <p>
     * The converted string will be uppercase.
     *
     * @param value The number to convert.
     * @param radix Number's converted radix.
     * @return The conversion or "" if conversion failed.
     */
    public static String intToString(int value,
                                     int radix)
    {
        String retval = "";

        try
        {
            // Process based on radix.
            switch (radix)
            {
            case 2 :
                retval = Integer.toBinaryString(value);
                break;

            case 8 :
                retval = Integer.toOctalString(value);
                break;

            case 16 :
                retval = Integer.toHexString(value);
                break;

            default :
                // Check for negative values = will not convert correctly.
                if (value >= 0)
                {
                    retval = Integer.toString(value,radix).toUpperCase();
                }
                else
                {
                    retval = longToString(value &0xFFFFFFFFL,radix);
                }
                break;
            } // switch
        }
        catch (Exception ex)
        {
            retval = "";
        }

        return retval.toUpperCase();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert number to a string. <p>
     *
     * @param value The number to convert.
     * @param radix Converted number's radix.
     * @param size Size string should be, padded left with '0'.
     * @return The conversion or "" if conversion failed.
     */
    public static String intToStringLeft0(int value,
                                          int radix,
                                          int size)
    {
        return EdtStringUtil.stringSizeLeft0(intToString(value,radix),
                                            size);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert number to a string. <p>
     *
     * @param value The number to convert.
     * @param radix Converted number's radix.
     * @param size Size string should be, padded left with '0'.
     * @return The conversion or "" if conversion failed.
     */
    public static String longToStringLeft0(long value,
                                          int radix,
                                          int size)
    {
        return EdtStringUtil.stringSizeLeft0(longToString(value,radix),
                                            size);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert value to string (w/base 16 and base 10 values). <p>
     * The returned string will be: 0x base 16 # (base 10 #).
     *
     * @param value Value to convert to string.
     * @return The string.
     */
    public static String intToStringBase16Base10(int value)
    {
        StringBuilder sb = new StringBuilder(50);

        sb.append("0x");
        sb.append(intToString(value,16));
        sb.append(" (");
        sb.append(intToString(value,10));
        sb.append(")");

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert int to packed bcd byte[]. <p>
     * Two bcd digits are packed into each byte.
     * <br>
     * Conversion rules: <br>
     *                       returned byte[]      <br>
     * value  start bcdLen   length,data[0,1,...] <br>
     * ------------------------------------------ <br>
     * 1      0     1        1      0x01          <br>
     * 1      0     2        1      0x01          <br>
     * 1      0     3        2      0x01 0x00     <br>
     * 12     0     1        1      0x02          <br>
     * 12     0     2        1      0x12          <br>
     * 12     0     3        2      0x12 0x00     <br>
     * 123    0     1        1      0x03          <br>
     * 123    0     2        1      0x23          <br>
     * 123    0     3        2      0x23 0x01     <br>
     * 123    1     2        1      0x23          <br>
     * 123    1     3        2      0x23 0x00     <br>
     *
     * @param value Value to convert. Must be >= 0.
     *        If < 0 then a conversion failure happens.
     * @param start Which bcd digit to start with.
     *        0 = the right most digit, 1 = 1 left of right most.
     * @param bcdLen Number of bcd digits in returned byte[].
     * @return Result of conversion or byte [] = 0x00 if failure.
     *         The length of the returned byte[] = (bcdLen/2)+1
     *         for a good conversion. If the conversion fails
     *         the lenght of the returned byte[] = 1.
     */
    public static byte[] intToBcd(
            int value,
            int start,
            int bcdLen)
    {
        byte[] retval = null;

        // Sanity.
        if ((value >= 0)
            &&
            (start >= 0)
            &&
            (bcdLen > 0))
        {
            // Create byte[].
            retval = EdtByteUtil.bytes((bcdLen+1)/2);

            // Create bcd nibbles.
            int dIndex = 0;
            boolean lowNibble = true;
            while ((bcdLen > 0) && (dIndex >= 0))
            {
                // Get base 10 digit and adjust value.
                int v = (value % 10);
                value /= 10;

                // Consume "start" bytes.
                if (start > 0)
                {
                    start--;
                }
                else
                {
                    // High or low nibble.
                    if (lowNibble)
                    {
                        retval[dIndex] = 0;
                        retval[dIndex] |= (byte)(v & 0x0f);
                    }
                    else
                    {
                        retval[dIndex] |= (byte)(((v & 0x0f) << 4) & 0xf0);
                        dIndex++;
                    }

                    // Toggle high/low nibble indicator.
                    lowNibble = ! lowNibble;

                    // One bcd digit stored.
                    bcdLen--;
                }
            } // while
        }
        else
        {
            retval = EdtByteUtil.bytes(1);
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert long to packed bcd byte[]. <p>
     * Two bcd digits are packed into each byte.
     * <br>
     * Conversion rules: <br>
     *                       returned byte[]      <br>
     * value  start bcdLen   length,data          <br>
     * ------------------------------------------ <br>
     * 1      0     1        1      0x01          <br>
     * 1      0     2        1      0x01          <br>
     * 1      0     3        2      0x01 0x00     <br>
     * 12     0     1        1      0x02          <br>
     * 12     0     2        1      0x12          <br>
     * 12     0     3        2      0x12 0x00     <br>
     * 123    0     1        1      0x03          <br>
     * 123    0     2        1      0x23          <br>
     * 123    0     3        2      0x23 0x01     <br>
     * 123    1     2        1      0x23          <br>
     * 123    1     3        2      0x23 0x00     <br>
     *
     * @param value Value to convert. Must be >= 0.
     *        If < 0 then a conversion failure happens.
     * @param start Which bcd digit to start with.
     *        0 = the right most digit, 1 = 1 left of right most.
     * @param bcdLen Number of bcd digits in returned byte[].
     * @return Result of conversion or byte [] = 0x00 if failure.
     *         The length of the returned byte[] = (bcdLen/2)+1
     *         for a good conversion. If the conversion fails
     *         the lenght of the returned byte[] = 1.
     */
    public static byte[] longToBcd(
            long value,
            int start,
            int bcdLen)
    {
        byte[] retval = null;

        // Sanity.
        if ((value >= 0)
            &&
            (start >= 0)
            &&
            (bcdLen > 0))
        {
            // Create byte[].
            retval = EdtByteUtil.bytes((bcdLen+1)/2);

            // Create bcd nibbles.
            int dIndex = 0;
            boolean lowNibble = true;
            while ((bcdLen > 0) && (dIndex >= 0))
            {
                // Get base 10 digit and adjust value.
                long v = (value % 10);
                value /= 10;

                // Consume "start" bytes.
                if (start > 0)
                {
                    start--;
                }
                else
                {
                    // High or low nibble.
                    if (lowNibble)
                    {
                        retval[dIndex] = 0;
                        retval[dIndex] |= (byte)(v & 0x0f);
                    }
                    else
                    {
                        retval[dIndex] |= (byte)(((v & 0x0f) << 4) & 0xf0);
                        dIndex++;
                    }

                    // Toggle high/low nibble indicator.
                    lowNibble = ! lowNibble;

                    // One bcd digit stored.
                    bcdLen--;
                }
            } // while
        }
        else
        {
            retval = EdtByteUtil.bytes(1);
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert a short into 2 bytes. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data Dest data bytes.
     * @param index Data index.
     * @param value Value to convert (will be anded with 0xFFFF).
     */
    public static void shortToBytes(
            boolean nbo,
            byte[]  data,
            int     index,
            short   value)
    {
        longToBytes(nbo,data,index,data.length,(value & 0xFFFF),2);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert an integer into 4 bytes. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data Dest data bytes.
     * @param index Data index.
     * @param value Value to convert (will be anded with 0xFFFFFFFF).
     */
    public static void intToBytes(
            boolean nbo,
            byte[]  data,
            int     index,
            int     value)
    {
        longToBytes(nbo,data,index,data.length,(value & 0xFFFFFFFF),4);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert a long into 8 bytes. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data Dest data bytes.
     * @param index Data index.
     * @param value Value to convert.
     */
    public static void longToBytes(
            boolean nbo,
            byte[]  data,
            int     index,
            long    value)
    {
        longToBytes(nbo,data,index,data.length,value,8);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert a value into 1..8 bytes. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data Dest data bytes.
     * @param index Data index.
     * @param dataLen Data length.
     * @param value Value to convert.
     * @param convBytes Number of bytes to convert.
     */
    public static void longToBytes(
            boolean nbo,
            byte[]  data,
            int     index,
            int     dataLen,
            long    value,
            int     convBytes)
    {
        // Sanity.
        if ((data != null)
            &&
            (dataLen <= data.length)
            &&
            (index >= 0)
            &&
            ((convBytes >=1) && (convBytes <= 8)))
        {
            // Storage order.
            if (nbo)
            {
                while ((convBytes > 0)
                        &&
                        ((index+convBytes-1) < dataLen))
                {
                    // Convert.
                    data[index+convBytes-1] = (byte)(value & 0xff);

                    // Adjust.
                    value >>= 8;

                    // Loop control.
                    convBytes--;
                } // while
            }
            else
            {
                while ((convBytes > 0)
                       &&
                       (index < dataLen))
                {
                    // Convert.
                    data[index] = (byte)(value & 0xff);

                    // Adjust.
                    value >>= 8;

                    // Loop control.
                       convBytes--;
                    index++;
                } // while
            }
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert 2 bytes into a short. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data Source data bytes.
     * @param index Data index.
     * @return The value (will be anded with 0xFFFF).
     */
    public static short bytesToShort(
            boolean nbo,
            byte[] data,
            int    index)
    {
        return (short)(bytesToLong(nbo,data,index,data.length,2) & 0xFFFF);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert 4 bytes into an integer. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data Source data bytes.
     * @param index Data index.
     * @return The value (will be anded with 0xFFFFFFFF).
     */
    public static int bytesToInt(
            boolean nbo,
            byte[] data,
            int    index)
    {
        return (int)(bytesToLong(nbo,data,index,data.length,4) & 0xFFFFFFFF);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert 8 bytes into an long. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data Source data bytes.
     * @param index Data index.
     * @return The value.
     */
    public static long bytesToLong(
            boolean nbo,
            byte[] data,
            int    index)
    {
        return bytesToLong(nbo,data,index,data.length,8);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert 1..8 bytes into a value. <p>
     * The bytes are in network byte order (MSB..LSB)
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data Source data bytes.
     * @param index Data index.
     * @param dataLen Length of data.
     * @param convBytes Number of bytes to convert.
     * @return The value.
     */
    public static long bytesToLong(
            boolean nbo,
            byte[]  data,
            int     index,
            int     dataLen,
            int     convBytes)
    {
        long retval = 0;

        // Sanity.
        if ((data != null)
            &&
            (dataLen <= data.length)
            &&
            (index >= 0)
            &&
            ((convBytes >=1 ) && (convBytes <= 8)))
        {
            // Storage order.
            if (nbo)
            {
                while ((convBytes > 0)
                       &&
                       (index < dataLen))
                {
                    // Next byte.
                    retval = (retval << 8) | (data[index] & 0xff);

                    // Loop control.
                    index++;
                    convBytes--;
                } // while
            }
            else
            {
                while ((convBytes > 0)
                       &&
                       ((index+convBytes-1) < dataLen))
                {
                    // Next byte.
                    retval = (retval << 8) | (data[index+convBytes-1] & 0xff);

                    // Loop control.
                    convBytes--;
                } // while
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert ascii chars in a string to a number. <p>
     * The radix is determined by how the string starts.
     *
     *    Radix     String starts with
     *    --------  --------------------
     *    16        "0x", "0X", "\x" or "\X"
     *    10        None of the above.
     *
     * @param str The string to convert.
     * @return The conversion or 0 if conversion failed.
     */
    public static long stringToLong(String str)
    {
        int radix = 10;

        // Check for radix 16.
        if ((str.startsWith("0x"))
             ||
            (str.startsWith("0X"))
            ||
            (str.startsWith("\\x"))
            ||
            (str.startsWith("\\X")))
        {
            str = str.substring(2);
            radix = 16;
        }

        return stringToLong(str,0,str.length(),radix);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert ascii chars in a string to a number. <p>
     *
     * @param str The string to convert.
     * @param radix Number's radix.
     * @return The conversion or 0 if conversion failed.
     */
    public static long stringToLong(String str,
                                    int radix)
    {
        return stringToLong(str,0,str.length(),radix);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert ascii chars in a string to a number. <p>
     *
     * @param str The string to convert.
     * @param index Index into string.
     * @param size Number of chars to convert, range is: 0..4.
     * @param radix Number's radix.
     * @return The conversion or 0 if conversion failed.
     */
    public static long stringToLong(String str,
                                  int index,
                                  int size,
                                  int radix)
    {
        str =  str.substring(index,index+size);

        BigInteger bi = new BigInteger(str,radix);

        return bi.longValue();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert number to a string. <p>
     * The converted string will be uppercase. <br>
     * Warning: The string returned from this method will be
     *          incorrect for negative values for radix
     *          that are not powers of 2. (Ex: radix of
     *          2,8,16 work correctly for negative numbers).
     *
     * @param value The number to convert.
     * @param radix Number's converted radix.
     * @return The conversion or "" if conversion failed.
     */
    public static String longToString(
            long value,
            int radix)
    {
        String retval = "";

        try
        {
            // Process based on radix.
            switch (radix)
            {
            case 2 :
                retval = Long.toBinaryString(value);
                break;

            case 8 :
                retval = Long.toOctalString(value);
                break;

            case 16 :
                retval = Long.toHexString(value);
                break;

            default :
                // Check for negative values = will not convert correctly.
                if (value >= 0)
                {
                    retval = Long.toString(value,radix).toUpperCase();
                }
                else
                {
                    retval = "0";

                    logger.error("longToString conversion failure because " +
                            "value is negative and radix not power of 2.");
                }
                break;
            } // switch
        }
        catch (Exception ex)
        {
            retval = "";
        }

        return retval.toUpperCase();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bcd array to an integer. <p>
     *
     * @param bcd Bcd array.
     * @return The value.
     */
    public static int bcdToInt(
            byte[] bcd)
    {
        return bcdToInt(bcd,0,bcd.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bcd array to an integer. <p>
     *
     * @param bcd Bcd array.
     * @param index Index into bcd array to startup.
     * @param len Number of bytes in array to process.
     * @return The value.
     */
    public static int bcdToInt(
            byte[] bcd,
            int    index,
            int    len)
    {
        int retval = 0;

        // Process.
        while (len > 0)
        {
            // Get high / low nibbles.
            int h = ((bcd[index] >> 4) & 0x0F) * 10;
            int l = ((bcd[index]     ) & 0x0F);

            // Update value.
            retval *= 100;
            retval += (h + l);

            // Loop control.
            index++;
            len--;
        } // while

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bcd array to an long. <p>
     *
     * @param bcd Bcd array.
     * @return The value.
     */
    public static long bcdToLong(
            byte[] bcd)
    {
        return bcdToLong(bcd,0,bcd.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bcd array to an long. <p>
     *
     * @param bcd Bcd array.
     * @param index Index into bcd array to startup.
     * @param len Number of bytes in array to process.
     * @return The value.
     */
    public static long bcdToLong(
            byte[] bcd,
            int    index,
            int    len)
    {
        long retval = 0;

        // Process.
        while (len > 0)
        {
            // Get high / low nibbles.
            int h = ((bcd[index] >> 4) & 0x0F) * 10;
            int l = ((bcd[index]     ) & 0x0F);

            // Update value.
            retval *= 100;
            retval += (h + l);

            // Loop control.
            index++;
            len--;
        } // while

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bcd array to string. <p>
     *
     * @param bcd BCD byte array.
     * @param start Offset into bcd array to start.
     * @param len Number of bytes to process in the bcd array.
     * @return Byte array converted to string.
     */
    public static String bcdToString(
            byte[] bcd,
            int start,
            int len)
    {
        byte[] bcdArray = new byte[len];
        System.arraycopy(bcd, start, bcdArray, 0, len);
        return(bcdToString(bcdArray, len));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bcd array to string. <p>
     *
     * @param bcdArray BCD byte array.
     * @param bcdLen Number of bytes to process in the bcd array.
     * @return Byte array converted to string.
     */
    public static String bcdToString(
            byte[] bcdArray,
            int bcdLen)
    {
        StringBuilder sb = new StringBuilder(300);
        byte[] asciiNo = new byte[2*bcdLen];
        int asciiNoIndex = 0;

        for(int i = 0; i < bcdLen; i++)
        {
            // MSB.
            asciiNo[asciiNoIndex] = (byte)(bcdArray[i]);
            asciiNo[asciiNoIndex] >>=4;
            asciiNo[asciiNoIndex] &= 0x0F;
            asciiNo[asciiNoIndex] |= (byte)0x30;
            sb.append((char)(asciiNo[asciiNoIndex] & 0x00FF));

            // LSB.
            asciiNoIndex++;
            asciiNo[asciiNoIndex] = (byte)(bcdArray[i] & 0x0F);
            asciiNo[asciiNoIndex] &= 0x0F;
            asciiNo[asciiNoIndex] |= (byte)(0x30);
            sb.append((char)(asciiNo[asciiNoIndex]&0x00FF));
            asciiNoIndex++;
        } // for

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert bcd string to bcd array. <p>
     *
     * @param value BCD string.
     * @return The value.
     */
    public static byte[] stringToBcd(String value)
    {
        int len = (value.length()+1)/2;
        EdtBuffer buf = new EdtBuffer(value);
        byte[] bcdArray = new byte[len];
        int stringIndex = value.length() -1;

        for(int i = len-1; i >= 0; i--)
        {
            bcdArray[i] = (byte)(buf.data[stringIndex] & 0x0F);
            stringIndex--;
            if(stringIndex >= 0)
            {
                bcdArray[i] |= (byte)((buf.data[stringIndex] & 0x0F) << 4);
                stringIndex--;
            }
        } // for

        return bcdArray;
    }
    
    public static  String stringToHex (String string) 
    {
    	StringBuilder buf = new StringBuilder(200);    //  buffer size 200 Max.
    	for (char ch: string.toCharArray()) 
    	{
    		if (buf.length() > 0)
    		{
    			buf.append(' ');
    		}
    		buf.append(String.format("%02x", (int) ch));
    	}
    	return buf.toString();
    }
    
    
    /**
     * Convert Decimal to hex. <p>
     *
     * @param decimal num in long.
     * @return hex string.
     */
       public static String decToHex(long n)
       {
              
              long r;
              String s=""; //variable for storing the result
              //array storing the digits (as characters) in a hexadecimal number system
              char dig[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
              while(n>0)
              {
                     r=n % 16; //finding remainder by dividing the number by 16
                     s=dig[(int) r]+s; //adding the remainder to the result
                     n=n/16;
              }
              return s;
       }

/**
     * Convert hex string to Ascii. <p>
     *
     * @param hex string.
     * @return Ascii string.
     */
       public static String hexToAscii(String s) {
                int n = s.length();
                StringBuilder sb = new StringBuilder(n / 2);
                for (int i = 0; i < n; i += 2) {
                  char a = s.charAt(i);
                  char b = s.charAt(i + 1);
                  sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
                }
                return sb.toString();
       }
       
	   /**
	        * Convert hex char to int. <p>
	        *
	        * @param hex char.
	        * @return int value.
	        */
	   	public static int hexToInt(char ch) 
	   	{
	   		  if ('a' <= ch && ch <= 'f') { return ch - 'a' + 10; }
	   		  if ('A' <= ch && ch <= 'F') { return ch - 'A' + 10; }
	   		  if ('0' <= ch && ch <= '9') { return ch - '0'; }
	   		  throw new IllegalArgumentException(String.valueOf(ch));
	   	} 

    // ******************************************************************
    // PROTECTED METHODS.
    // ******************************************************************

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

//  ----------------------------------------------------------------------

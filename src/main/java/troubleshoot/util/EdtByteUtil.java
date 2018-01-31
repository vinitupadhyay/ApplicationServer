package troubleshoot.util;

import java.util.Arrays;

public class EdtByteUtil 
{
	 // ******************************************************************
    // PUBLIC FIELDS.
    // ******************************************************************
    // -------------------------------------------------
    // Bits.
    // -------------------------------------------------
    public static final byte BIT7 = (byte)0x80;
    public static final byte BIT6 = (byte)0x40;
    public static final byte BIT5 = (byte)0x20;
    public static final byte BIT4 = (byte)0x10;
    public static final byte BIT3 = (byte)0x08;
    public static final byte BIT2 = (byte)0x04;
    public static final byte BIT1 = (byte)0x02;
    public static final byte BIT0 = (byte)0x01;
    
    // -------------------------------------------------
    // Ascii values.
    // -------------------------------------------------
    public static final byte ASCII_NUL = (byte)0x00;
    public static final byte ASCII_SOH = (byte)0x01;
    public static final byte ASCII_STX = (byte)0x02;
    public static final byte ASCII_ETX = (byte)0x03;
    public static final byte ASCII_EOT = (byte)0x04;
    public static final byte ASCII_ENQ = (byte)0x05;
    public static final byte ASCII_ACK = (byte)0x06;
    public static final byte ASCII_BEL = (byte)0x07;
    public static final byte ASCII_BS  = (byte)0x08;
    public static final byte ASCII_HT  = (byte)0x09;
    public static final byte ASCII_LF  = (byte)0x0A;
    public static final byte ASCII_VT  = (byte)0x0B;
    public static final byte ASCII_FF  = (byte)0x0C;
    public static final byte ASCII_CR  = (byte)0x0D;
    public static final byte ASCII_SO  = (byte)0x0E;
    public static final byte ASCII_SI  = (byte)0x0F;
    public static final byte ASCII_DLE = (byte)0x10;
    public static final byte ASCII_DC1 = (byte)0x11;
    public static final byte ASCII_DC2 = (byte)0x12;
    public static final byte ASCII_DC3 = (byte)0x13;
    public static final byte ASCII_DC4 = (byte)0x14;
    public static final byte ASCII_NAK = (byte)0x15;
    public static final byte ASCII_SYN = (byte)0x16;
    public static final byte ASCII_ETB = (byte)0x17;
    public static final byte ASCII_CAN = (byte)0x18;
    public static final byte ASCII_EM  = (byte)0x19;
    public static final byte ASCII_SUB = (byte)0x1A;
    public static final byte ASCII_ESC = (byte)0x1B;
    public static final byte ASCII_FS  = (byte)0x1C;
    public static final byte ASCII_GS  = (byte)0x1D;
    public static final byte ASCII_RS  = (byte)0x1E;
    public static final byte ASCII_US  = (byte)0x1F;
    public static final byte ASCII_SPC = (byte)0x20;

    /** Byte util operations. <p> */
    public enum Op
    {
        /** No operation. <p> */
        NOP,

        /** Bitwise OR operation. <p> */
        OR,

        /** Bitwise AND operation. <p> */
        AND,

        /** Bitwise xor operation. <p> */
        XOR,

        /** Add w/no carry operation. <p> */
        ADD,

        /** Sub w/o no carry operation. <p> */
        SUB,
    };

    // ******************************************************************
    // PROTECTED FIELDS.
    // ******************************************************************

    // ******************************************************************
    // PRIVATE FIELDS.
    // ******************************************************************

    // ******************************************************************
    // CONSTRUCTOR.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    private EdtByteUtil() {}

    // ******************************************************************
    // PUBLIC METHODS       (general, getter, setter, interface imp)
    // ******************************************************************

    public static String toAsciiString(byte b){
        switch (b)
        {
	        case ASCII_NUL : return "NUL";
	        case ASCII_SOH : return "SOH";
	        case ASCII_STX : return "STX";
	        case ASCII_ETX : return "ETX";
	        case ASCII_EOT : return "EOT";
	        case ASCII_ENQ : return "ENQ";
	        case ASCII_ACK : return "ACK";
	        case ASCII_BEL : return "BEL";
	        case ASCII_BS  : return "BS";
	        case ASCII_HT  : return "HT";
	        case ASCII_LF  : return "LF";
	        case ASCII_VT  : return "VT";
	        case ASCII_FF  : return "FF";
	        case ASCII_CR  : return "CR";
	        case ASCII_SO  : return "SO";
	        case ASCII_SI  : return "SI";
	        case ASCII_DLE : return "DLE";
	        case ASCII_DC1 : return "DC1";
	        case ASCII_DC2 : return "DC2";
	        case ASCII_DC3 : return "DC3";
	        case ASCII_DC4 : return "DC4";
	        case ASCII_NAK : return "NAK";
	        case ASCII_SYN : return "SYN";
	        case ASCII_ETB : return "ETB";
	        case ASCII_CAN : return "CAN";
	        case ASCII_EM  : return "EM";
	        case ASCII_SUB : return "SUB";
	        case ASCII_ESC : return "ESC";
	        case ASCII_FS  : return "FS";
	        case ASCII_GS  : return "GS";
	        case ASCII_RS  : return "RS";
	        case ASCII_US  : return "US";
	        default: return String.valueOf((char)b);
        }
    }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get array inited bytes. <p>
     * Each byte will have a value of 0.
     *
     * @param bytesLen Number of bytes in array.
     * @return The array.
     */
    public static byte[] bytes(int bytesLen)
    {
        // Get.
        byte[] retval = new byte[bytesLen];

        // Init.
        for (int index=0; index<bytesLen; index++)
        {
            retval[index] = 0;
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get array of inited bytes. <p>
     *
     * @param data Source array.
     * @return The array.
     */
    public static byte[] bytes(byte[] data)
    {
        byte[] retval = new byte[data.length];

        // Copy.
        for (int index=0; index<data.length; index++)
        {
            retval[index] = data[index];
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get array of inited bytes. <p>
     *
     * @param data Initial byte value.
     * @param len Length of array.
     * @return The array.
     */
    public static byte[] bytes(
            byte data,
            int  len)
    {
        byte[] retval = new byte[len];

        // Copy.
        for (int index=0; index<len; index++)
        {
            retval[index] = data;
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get array of inited bytes. <p>
     *
     * @param data Source array.
     * @param len Length of bytes to get from source array.
     * @return The array.
     */
    public static byte[] bytes(
            byte[] data,
            int    len)
    {
        // Fix length.
        len = Math.min(len,data.length);

        // Get.
        byte[] retval = new byte[len];

        // Copy.
        for (int index=0; index<len; index++)
        {
            retval[index] = data[index];
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get array of bytes. <p>
     * The returned array length will always be length.
     * If offset + length > array length then those data bytes not copied.
     *
     * @param data Source array.
     * @param offset Offset into array.
     * @param length Length of bytes to get from source array.
     * @return The array.
     */
    public static byte[] bytes(
            byte[] data,
            int    offset,
            int    length)
    {
        byte[] retval = bytes(length);

        // Fix length.
        length = Math.min(length,(data.length-offset));

        // Copy.
        for (int index=0; index<length; index++)
        {
            retval[index] = data[offset++];
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get array of inited bytes. <p>
     * Each byte in the array is first inited to 0 then the
     * passed in data is copied into the array.
     * If data len > array len then those data bytes not copied.
     *
     * @param bytesLen Number of bytes in inited array.
     * @param data Initial values.
     * @param dataLen Length of initial values.
     * @return The array.
     */
    public static byte[] bytes(
            int    bytesLen,
            byte[] data,
            int    dataLen)
    {
        // Get.
        byte[] retval = bytes(bytesLen);

        // Adjust.
        dataLen = (dataLen > bytesLen) ? bytesLen : dataLen;

        // Copy.
        for (int index=0; index<dataLen; index++)
        {
            retval[index] = data[index];
        } // for

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do operation (OP_xxx) on two byte arrays. <p>
     * The following is done: dest[x] = dest[dI++] (operation op) sour[sI++].
     *
     * @param op The operation to do.
     * @param destData Destination data.
     * @param destIndex Destination data start index.
     * @param destLen Destination data length.
     * @param sourData Source data.
     * @param sourIndex Source data start index.
     * @param sourLen Source data length.
     */
    public static void byteOp(
            Op     op,
            byte[] destData,
            int    destIndex,
            int    destLen,
            byte[] sourData,
            int    sourIndex,
            int    sourLen)
    {
        byte db = 0;
        byte sb = 0;

        while (((destLen--) > 0) && ((sourLen--) > 0))
        {
            // Get dest and source bytes.
            db = destData[destIndex];
            sb = sourData[sourIndex++];

            // Do op.
            switch (op)
            {
            case OR  : db |= sb; break;
            case AND : db &= sb; break;
            case XOR : db ^= sb; break;
            case ADD : db += sb; break;
            case SUB : db -= sb; break;
            } // switch

            // Update dest.
            destData[destIndex++] = db;
        } // while
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do operation (OP_xxx) on single byte array. <p>
     * The result is generated by doing the operation on all bytes in
     * the data. The initial result value before any ops are done is 0.
     *
     * @param op The operation to do.
     * @param destData Destination data.
     * @param destIndex Destination data start index.
     * @param destLen Destination data length.
     * @return Result of doing the operation.
     */
    public static byte byteOp(
            Op     op,
            byte[] destData,
            int    destIndex,
            int    destLen)
    {
        byte retval = 0;
        byte sb = 0;

        while ((destLen--) > 0)
        {
            // Get byte.
            sb = destData[destIndex++];

            // Do op.
            switch (op)
            {
            case OR  : retval |= sb; break;
            case AND : retval &= sb; break;
            case XOR : retval ^= sb; break;
            case ADD : retval += sb; break;
            case SUB : retval -= sb; break;
            } // switch
        } // while

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do operation (OP_xxx) on single byte array. <p>
     * The result is generated by doing the operation on all bytes in
     * the data. The initial result value before any ops are done is 0.
     *
     * @param op The operation to do.
     * @param destData Destination data.
     * @param destIndex Destination data start index.
     * @param destLen Destination data length.
     * @return Result of doing the operation.
     */
    public static int byteOpInt(
            Op     op,
            byte[] destData,
            int    destIndex,
            int    destLen)
    {
        int retval = 0;
        int sb = 0;

        while ((destLen--) > 0)
        {
            // Get byte.
            sb = (destData[destIndex++] & 0xff);

            // Do op.
            switch (op)
            {
            case OR  : retval |= sb; break;
            case AND : retval &= sb; break;
            case XOR : retval ^= sb; break;
            case ADD : retval += sb; break;
            case SUB : retval -= sb; break;
            } // switch
        } // while

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Dump (convert) byte[] to readable (ascii-hex) form. <p>
     *
     * @param sb Dump buffer. null will allocate new buffer.
     * @param title Title or null.
     * @param headerSize Header size.
     * @param skip Add skip space after skip many ascii-hex pairs.
     * @param data Byte[] to dump.
     * @return The dump.
     */
    public static StringBuilder dump(
            StringBuilder sb,
            String        title,
            int           headerSize,
            int           skip,
            byte[]        data)
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
            int dataLen = data.length;

            sb1.append(" (");
            sb1.append("len 0x");
            sb1.append(EdtConvert.intToString(dataLen,16));
            sb1.append(", ");
            sb1.append(EdtConvert.intToString(dataLen,10));
            sb1.append(")= ");

            while (sb1.length() < headerSize)
            {
                sb1.append(' ');
            } // while

            sb1.append(EdtConvert.bytesToHexString(data,skip));
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
	 * Compare two byte arrays. <p>
	 *
	 * @param destData Destination data.
	 * @param sourData Source data.
	 * 
	 */
	public static boolean compare( byte[] destData, byte[] sourData)
	{
		return Arrays.equals(destData, sourData);
	}

	/**
	 * Searches for the first occurrence of the pattern into the given array, if any. <p>
	 *
	 * @param array where the pattern is searched for.
	 * @param pattern to search for.
	 * @return the index position of the first pattern byte in the given array, if the whole pattern is found.
	 *         -1 if not found
	 */
	public static int indexOf( byte array[], byte pattern[] )
	{
		if( pattern.length > array.length)
		{
			return -1; // pattern must fit into array !
		}
		else if( pattern.length == array.length )
		{
			return Arrays.equals(array, pattern) ? 0 : -1; // compare exactly
		}

		int iFound = -1;
		for(int i = 0; iFound < 0 && i < array.length; i++)
		{
			// try to find the first pattern byte.
			if( array[i] != pattern[0] )
				continue;

			if( i + pattern.length >= array.length )
				return -1; // There is too few bytes left to find the pattern.

			// try to find the reminder of the pattern bytes.
			for(int j = 1; j < pattern.length; j++)
			{
				if( array[i+j] != pattern[j] )
					break;

				if( j == pattern.length - 1 ) // is last pattern byte ?
					iFound = i;
			}
		}

		return iFound;
	}

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Set all bytes in byte[] to 0. <p>
     *
     * @param destData Destination data.
     */
    public static void zero(byte[] destData)
    {
        if (destData != null)
        {
            for (int index=0; index<destData.length; index++)
            {
                destData[index]=0;
            } // for
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Arithmetic shift right all bits in a buffer. <p>
     *
     * @param data Data to shift.
     */
    public static void arithmeticShiftRight(
            byte[] data)
    {
        boolean setBit = false;
        boolean carryBit = false;
        int     index = 0;

        // Sanity
        if (data != null)
        {
            // Process.
            while (index < data.length)
            {
                if ((data[index] & 0x01) != 0)
                {
                    carryBit = true;
                }
                data[index] = (byte)(((data[index] & 0xff) >> 1) & 0xff);

                if (setBit)
                {
                    data[index] |= (byte)0x80;
                }

                setBit = carryBit;
                carryBit = false;

                // Loop control.
                index++;
            } // while
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Add two data buffers together. <p>
     * The buffers are added from right to left w/carry.
     *
     * @param destData Dest data.
     * @param sourData Source data.
     */
    public static void addLeftWithCarry(
            byte[] destData,
            byte[] sourData)
    {
        // Sanity.
        if ((destData != null) && (sourData != null))
        {
            int destLen = destData.length;
            int sourLen = sourData.length;

            int i1        = 0;
            int i2        = 0;
            int addReg    = 0;
            int carryReg  = 0;

            while ((destLen > 0) && (sourLen > 0))
            {
                // Get bytes.
                i1 = (destData[destLen-1]) & 0xff;
                i2 = (sourData[sourLen-1]) & 0xff;

                // Do integer addition to avoid sign problems.
                addReg    = (i1 + i2 + carryReg);
                carryReg  = (addReg / 0x100);

                // Update dest byte.
                destData[destLen-1] = (byte)(addReg & 0xff);

                // Loop control.
                destLen--;
                sourLen--;
            } // while
        }
    }
    
    public static byte[] concatBytes(byte b1, byte... b2)
    {
       byte[] b = new byte[1 + b2.length];
       b[0] = b1;
       System.arraycopy(b2, 0, b, 1, b2.length);
       return b;
    }
    
    public static byte[] concatBytes(byte[] b1, byte... b2)
    {
       byte[] b = new byte[b1.length + b2.length];
       System.arraycopy(b1, 0, b, 0, b1.length);
       System.arraycopy(b2, 0, b, b1.length, b2.length);
       return b;
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

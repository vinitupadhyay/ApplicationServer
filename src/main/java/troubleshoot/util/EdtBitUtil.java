/*
 * File: EdtBitUtil.java
 *
 * Desc: Edt bit utility class.
 *
 * Gilbarco Inc. 2007
 *
 * History:
 *    2008.02.15.fri.Yogesh Kotkar v01.0.03.
 *       - Added bit togle operation.
 *
 *    2008.01.07.mon.Yogesh Kotkar v01.0.02.
 *       - Added method to rotate left and right.
 *
 *    2007.07.03.tue.Scott Turner  v01.0.01
 *       - Created.
 */

// ----------------------------------------------------------------------
package troubleshoot.util;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;

// ----------------------------------------------------------------------

// ----------------------------------------------------------------------
public class EdtBitUtil
{
	private final static Logger logger = Logger.getLogger(TroubleshootController.class);
    // ******************************************************************
    // PUBLIC FIELDS.
    // ******************************************************************

    /** Bit operations. <p> */
    public enum EdtBitUtilBitOp
    {
        /** Clear bit. <p> */
        CLEAR,

        /** Set bit. <p> */
        SET,

        /** Toggle bit. <p> */
        TOGGLE,
        ;
    }

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
     * Do a bit operation. <p>
     *
     * @param bitOp Bit operation.
     * @param value Value to operate on.
     * @param bit Bit index/id (0 based).
     * @param flag Bit operation done if this is true.
     * @return The result of the bit operation.
     */
    public static int bitOp(
            EdtBitUtilBitOp bitOp,
            int value,
            int bit,
            boolean flag)
    {
        // Operate if needed.
        if (flag)
        {
            value = bitOp(bitOp,value,bit);
        }

        return value;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do a bit operation. <p>
     *
     * @param bitOp Bit operation.
     * @param value Value to operate on.
     * @param bit Bit index/id (0 based).
     * @return The result of the bit operation.
     */
    public static int bitOp(
            EdtBitUtilBitOp bitOp,
            int value,
            int bit)
    {
        // Process based on bit op.
        switch (bitOp)
        {
        case CLEAR :
            value &= (~(1 << bit));
            break;

        case SET :
            value |= (1 << bit);
            break;

        case TOGGLE:
            value ^= (1 << bit);
            break;
        } // switch

        return value;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Determine if a bit is set in a value. <p>
     *
     * @param value Value to test.
     * @param bit Bit index/id (0 based).
     * @return true if bit set, false otherwise.
     */
    public static boolean isBitSet(
            int value,
            int bit)
    {
        return ((value & (1 << bit)) != 0);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Count number of bits that are set. <p>
     *
     * @param value Source data.
     * @return The value.
     */
    public static int bitCount(byte value)
    {
        int retval = 0;

        if ((value & 0x80) != 0) { retval++; }
        if ((value & 0x40) != 0) { retval++; }
        if ((value & 0x20) != 0) { retval++; }
        if ((value & 0x10) != 0) { retval++; }
        if ((value & 0x08) != 0) { retval++; }
        if ((value & 0x04) != 0) { retval++; }
        if ((value & 0x02) != 0) { retval++; }
        if ((value & 0x01) != 0) { retval++; }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Count number of bits that are set. <p>
     *
     * @param value Source data.
     * @return The value.
     */
    public static int bitCount(byte[] value)
    {
        int retval = 0;

        // Sanity.
        if (value != null)
        {
            for (int index=0; index<value.length; index++)
            {
                retval += bitCount(value[index]);
            } // for
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Rotate the value either left or right. <p>
     *
     * @param leftDirn Rotate left if true else right.
     * @param value    Value to be rotated.
     * @param distance The no of bits the value to be rotated.
     * @return The reult of rotate operation.
     */
    public static byte rotate(
            boolean leftDirn,
            byte    value,
            int     distance)
    {
        return (byte)rotate(leftDirn, (value & 0xFF), distance, 8);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Rotate the value either left or right. <p>
     *
     * @param leftDirn Rotate left if true else right.
     * @param value    Value to be rotated.
     * @param distance The no of bits the value to be rotated.
     * @return The reult of rotate operation.
     */
    public static short rotate(
            boolean  leftDirn,
            short    value,
            int      distance)
    {
        return (short)rotate(leftDirn, (value & 0xFFFF), distance, 16);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Rotate the value either left or right. <p>
     *
     * @param leftDirn Rotate left if true else right.
     * @param value    Value to be rotated.
     * @param distance The no of bits the value to be rotated.
     * @return The reult of rotate operation.
     */
    public static int rotate(
            boolean leftDirn,
            int     value,
            int     distance)
    {
        return (int)rotate(leftDirn, (value & 0xFFFFFFFF), distance, 32);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Rotate the value either left or right. <p>
     *
     * @param leftDirn Rotate left if true else right.
     * @param value    Value to be rotated.
     * @param distance The no of bits the value to be rotated.
     * @return The reult of rotate operation.
     */
    public static long rotate(
            boolean leftDirn,
            long    value,
            int     distance)
    {
        return rotate(leftDirn, value, distance, 64);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Rotate the value either left or right. <p>
     *
     * @param leftDirn Rotate left if true else right.
     * @param value    Value to be rotated.
     * @param distance The no of bits the value to be rotated.
     * @param bitSize  The bitsize of the value.
     * @return The reult of rotate operation.
     */
    public static long rotate(
            boolean leftDirn,
            long    value,
            int     distance,
            int     bitSize)
    {
        long retValue = 0;

        if(bitSize  < distance)
        {
            distance = distance - bitSize;
        }

        // Sanity.
        if((bitSize <= 64)
            &&
            (bitSize >= distance))
        {
            if(leftDirn)
            {
                retValue = (value << distance) | (value >> (bitSize -distance));

            }
            else
            {
                retValue = (value >> distance) |(value << (bitSize - distance));
            }
        }
        else
        {
            String warning = "\nEither bitsize of the value to rotate is invalid or" +
                    "\nNo of bits to rotate is equal to bitsize of the value results in no operation.";
            logger.warn(warning);
        }
        return (retValue);
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

// ----------------------------------------------------------------------

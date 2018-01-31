/*
 * File: EdtCrc16_8005.java
 *
 * Desc: Edt crc16 calculator (using 8005 polynomial).
 *
 * Gilbarco Inc. 2008
 *
 * History:
 *    2008.05.31.sat.Scott Turner  v01.0.01
 *       - Created.
 */

//----------------------------------------------------------------------
package troubleshoot.util;

//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Edt crc16 calculator (using 8005 polynomial). <p>
 */
public class EdtCrc16_8005
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

    // Polynomial 8005.
    private static final short CRC_POLYNOMIAL = (short)0x8005;

    // Initial crc value.
    private int _initialValue = 0;

    // Current crc value.
    private int _crc = 0;

    // ******************************************************************
    // CONSTRUCTOR.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create. <p>
     * The crc value will be 0.
     */
    public EdtCrc16_8005()
    {
        reset();
    }

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
     * Convert to string. <p>
     *
     * @return The string.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder(100);

        sb.append(this.getClass().getSimpleName());
        sb.append(":");
        sb.append("\ncrc=" + getValue());

        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Reset crc value to its initial value. <p>
     */
    public void reset()
    {
        _crc = (_initialValue & 0xFFFF);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Update checksum w/a byte. <p>
     *
     * @param b The byte.
     */
    public void update(byte b)
    {
        int i = 0;

        short crc = (short)(_crc ^ (b << 8));
        for (i=0; i <= 7; i++)
        {
            if (crc < 0)
            {
                crc = (short)(crc << 1);
                crc = (short)(crc ^ CRC_POLYNOMIAL);
            }
            else
            {
                crc = (short)(crc << 1);
            }
        }
        _crc = (crc & 0xFFFF);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Update checksum w/byte array. <p>
     *
     * @param b The byte array.
     */
    public void update(byte[] b)
    {
        for (byte bx : b)
        {
            update(bx);
        } // for
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Update checksum w/byte array. <p>
     *
     * @param b Byte array.
     * @param offset Offset into byte array.
     * @param len Number of bytes to use in the byte array.
     */
    public void update(
            byte[] b,
            int    offset,
            int    len)
    {
        for (int index=0; index<len; index++)
        {
            if (offset+index < b.length)
            {
                update(b[offset+index]);
            }
        } // for
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Update checksum w/an EdtBuffer. <p>
     *
     * @param buf The buffer.
     */
    public void update(EdtBuffer buf)
    {
        update(buf,0,buf.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Update checksum w/an EdtBuffer. <p>
     *
     * @param buf The buffer.
     * @param offset Start offset into buffer.
     * @param len Number of bytes to use in the buffer.
     */
    public void update(
            EdtBuffer buf,
            int       offset,
            int       len)
    {
        for (int index=0; index<len; index++)
        {
            if (offset+index < buf.length)
            {
                update(buf.data[offset+index]);
            }
        } // for
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get crc value. <p>
     *
     * @return The current crc value.
     */
    public int getValue()
    {
        return _crc;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get most significant byte (msb) of crc value. <p>
     *
     * @return The msb of the crc value.
     */
    public int getValueMsb()
    {
        return ((_crc >> 8) & 0xFF);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get least significant byte (lsb) of crc value. <p>
     *
     * @return The lsb of the crc value.
     */
    public int getValueLsb()
    {
        return (_crc & 0xFF);
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

//----------------------------------------------------------------------

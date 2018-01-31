/*
 * File: EdtBuffer.java
 *
 * Desc: Edt: A byte buffer.
 *
 * Gilbarco Inc. 2004
 *
 * History:
 *    2005.01.04.tue.Scott Turner  v01.0.01
 *       - Created.
 */

// ----------------------------------------------------------------------
package troubleshoot.util;

// ----------------------------------------------------------------------
import java.util.Arrays;
import java.util.Vector;

// ----------------------------------------------------------------------
/**
 * Create a data buffer.<p>
 * The buffer's data is a byte[].<p>
 * Associated with a buffer are:
 * <ul>
 *    <li>length = Current length of data in buffer.
 *    <li>size   = Max length data can be in buffer.
 *    <li>data   = Buffer's data.
 *</ul>
 * Buffer marking is also supported. Buffer marks allow sections
 * of the buffer's data to be marked. Once marked the marked sections
 * can be read from the buffer one at a time.
 * Each buffer marker can also store an optional Object that can be
 * read as each mark is read. Buffer marking provides a way of storing
 * metadata w/in the data buffer object.
 */
public class EdtBuffer
{
    // ******************************************************************
    // PUBLIC FIELDS.
    // ******************************************************************

    /**
     * Buffer's data. <p>
     */
    public byte[] data = null;

    /**
     * Current length of data in buffer. <p>
     * This value will range from 0 to buffer's size;
     */
    public int length = 0;

    /** To string format types. <p> */
    public enum EdtBufferToStringFormatType
    {
        /**
         * Normal. <p>
         */
        NORMAL,

        /**
         * Ascii hex. <p>
         * A single line formated as ascii hex.
         */
        HEX,

        /**
         * Ascii hex and printable. <p>
         * Line 1 formated as ascii hex. <br>
         * Line 2 formated as printable ascii.
         */
        HAP1,

        /**
         * Ascii hex and printable. <p>
         * Line 1 has buffer's length. <br>
         * Line 2 formated as ascii hex. <br>
         * Line 3 formated as printable ascii.
         */
        HAP2,

        /**
         * Ascii hex and printable. <p>
         * Line 1 has buffer's length. <br>
         * Line 2 formatted as ascii hex. <br>
         * Line 3 formatted as printable ascii (w/space between each char).
         */
        HAP3,
        ;
    };

    // ******************************************************************
    // PROTECTED FIELDS.
    // ******************************************************************

    // ******************************************************************
    // PRIVATE FIELDS.
    // ******************************************************************

    // Max length of data in buffer.
    private int size = 0;

    // --------------------------
    // Marks info: begin.

    // If any changes here, see methods: marksCopy().

    // Marks read index.
    private int marksReadIndex = 0;

    // Buffer marks.
    private Vector<MarkData> marks = null;

    // Marks info: end.
    // --------------------------

    // ******************************************************************
    // CONSTRUCTOR.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create 0 sized buffer. <p>
     * A new byte[] will be created.
     * Size = 0;
     * Length = 0;
     * @deprecated Confusing ctor, if you want to create an empty EdtBuffer use new EdtBuffer(0)
     */
    public EdtBuffer()
    {
        this(0);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create empty buffer. <p>
     * A new byte[] will be created.
     * Size = specified size.
     * Length = 0.
     *
     * @param size Max length of data in buffer.
     */
    public EdtBuffer(int size)
    {
        this.size = size;
        data = new byte[size];
        length = 0;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create buffer from existing data. <p>
     * An new byte[] will be created.
     * Size = size.
     * Length = data's size.
     *
     * @param data Data copied to new byte[].
     */
    public EdtBuffer(byte[] data)
    {
        this(data,data.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create buffer from existing data. <p>
     * An new byte[] will be created.
     * Size = size.
     * Length = Min(data's length, size).
     *
     * @param data Data copied to new byte[].
     * @param size Size of new byte[].
     */
    public EdtBuffer(byte[] data,
                     int    size)
    {
        this(size);

        // Length.
        length = Math.min(data.length,size);

        // Make copy.
        System.arraycopy(data,0,
                         this.data,0,
                         length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create buffer from existing data. <p>
     * A new byte[] will be created.
     * Size = size.
     * Length = Min(data's length, size).
     *
     * @param data Data copied to new byte[].
     * @param offset Offset into source data.
     * @param size Size of new byte[].
     */
    public EdtBuffer(byte[] data,
                     int    offset,
                     int    size)
    {
        this(size);

        // Length.
        length = Math.min(data.length,size);

        // Make copy.
        System.arraycopy(data,offset,
                         this.data,0,
                         length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create buffer from existing data. <p>
     * An new byte[] will be created.
     * Size = size.
     * Length = Min(data's length, size).
     *
     * @param data Data copied to new byte[].
     * @param size Size of new byte[].
     */
    public EdtBuffer(char[] data,
                     int    size)
    {
        this(size);

        // Length.
        length = Math.min(data.length,size);

        // Make copy.
        for (int index=0; index<length; index++) {
            this.data[index] = (byte)data[index];
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create buffer from string's bytes. <p>
     * A new byte[] will be created.
     * Size = string length.
     * Length = string length.
     *
     * @param data Data copied to new byte[].
     */
    public EdtBuffer(String data)
    {
        this(data.length());

        // Length.
        length = data.length();

        // Make copy.
        for (int index=0; index<length; index++)
        {
            this.data[index] = (byte)(int)data.charAt(index);
        } // for
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create buffer from string's bytes. <p>
     * A new byte[] will be created.
     * Size = specified size.
     * Length = Min(string length, size)
     *
     * @param data Data copied to new byte[].
     * @param size Size of new byte[].
     */
    public EdtBuffer(String data,
                     int    size)
    {
        this(size);

        // Length.
        length = Math.min(data.length(),size);

        // Make copy.
        for (int index=0; index<length; index++)
        {
            this.data[index] = (byte)(int)data.charAt(index);
        } // for
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create buffer from an existing buffer's data. <p>
     * A new byte[] will be created.
     * Size = existing buffer's size.
     * Length = existing buffer's length.
     * This duplicates a buffer.
     * This duplicates marks.
     * Objects stored w/marks are shared.
     *
     * @param aBuf Buffer to duplicate (or null).
     */
    public EdtBuffer(final EdtBuffer aBuf)
    {
        this((aBuf != null) ? aBuf.size : 0);

        if (aBuf != null)
        {
            length = aBuf.length;

            // Copy.
            System.arraycopy(aBuf.data,0,
                             data,0,
                             length);

            // Copy marks.
            if (aBuf.marks != null)
            {
                marksCopy(aBuf);
            }
        }
    }

    
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create buffer from an existing buffer's data. <p>
     * A new byte[] will be created.
     * Size = specified size.
     * Length = Min(existing buffer's length, size).
     * This duplicates a buffer.
     * This duplicates marks.
     * Objects stored w/marks are shared.
     *
     * @param aBuf Buffer to duplicate.
     * @param size Size of new byte[].
     */
    public EdtBuffer(final EdtBuffer aBuf,
                     int       size)
    {
        this(size);
        length = Math.min(aBuf.length,size);

        // Make copy.
        System.arraycopy(aBuf.data,0,
                         data,0,
                         length);

        // Copy marks.
        if (aBuf.marks != null)
        {
            marksCopy(aBuf);
        }
    }

    // ******************************************************************
    // FINALIZER.
    // ******************************************************************

    public void finalize() throws Throwable
    {
    }

    // ******************************************************************
    // PUBLIC METHODS       (general, getter, setter, interface imp)
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Reset length of buffer to 0. <p>
     * The buffer's byte[] is untouched.
     * This clears any marks.
     */
    public void clear()
    {
        length = 0;
        marksClear();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Clear buffer then append data to a buffer. <p>
     * The bytes of the string are appended.
     * If no room then nothing is appended.
     * This clears any marks.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean set(String data)
    {
        clear();
        marksClear();
        return append(data);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Clear buffer then append data to a buffer. <p>
     * If no room then nothing is appended.
     * This clears marks.
     *
     * @param d What to append.
     * @return True if appended, else false.
     */
    public boolean set(byte[] d)
    {
        clear();
        marksClear();
        return append(d,d.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Clear buffer then append data to a buffer. <p>
     * If no room then nothing is appended.
     * This clears marks.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean set(final EdtBuffer data)
    {
        clear();
        marksClear();
        return append(data);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append byte to buffer. <p>
     * If no room then nothing is appended.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean append(byte data)
    {
        boolean retval = false;

        // Length check.
        if (length < size)
        {
            retval = true;
            this.data[length++] = data;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append byte[] to a buffer. <p>
     * If no room then nothing is appended.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean append(byte[] data)
    {
        return append(data,data.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append byte to buffer. <p>
     * If no room then nothing is appended.
     *
     * @param hasData true=append data to buffer, false=! append.
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean append(
            boolean hasData,
            byte    data)
    {
        if (hasData)
        {
            return append(data);
        }
        else
        {
            return false;
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append byte to buffer. <p>
     * If no room then nothing is appended.
     * If data is null then nothing is appended.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean append(Byte data)
    {
        if (data != null)
        {
            return append(data.byteValue());
        }
        else
        {
            return false;
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append byte[] to a buffer. <p>
     * If no room then nothing is appended.
     *
     * @param data What to append.
     * @param length Length of what to append.
     * @return True if appended, else false.
     */
    public boolean append(byte[] data,
                          int    length)
    {
        boolean retval = false;

        // Length check.
        if ((this.length + length) <= size)
        {
            for (int index=0; index<length; index++)
            {
                retval = append(data[index]);
            } // for
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append buffer to a buffer. <p>
     * If no room then nothing is appended.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean append(final EdtBuffer data)
    {
        boolean retval = false;

        // Length check.
        if ((length + data.length) <= size)
        {
            for (int index=0; index<data.length; index++)
            {
                retval = append(data.data[index]);
            } //for
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append string to a buffer. <p>
     * The bytes of the string are appended.
     * If no room then nothing is appended.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean append(String data)
    {
        boolean retval = false;

        // Length check.
        if ((length + data.length()) <= size)
        {
            for (int index=0; index<data.length(); index++)
            {
                retval = append((byte)(int)data.charAt(index) & 0xff);
            } //for
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append (data & 0xff) (1 byte) to buffer. <p>
     * If no room then nothing is appended.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean append(int data)
    {
        boolean retval = false;

        // Length check.
        if (length < size)
        {
            retval = true;
            this.data[length++] = (byte)(data & 0xff);
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Add short (2 bytes) to buffer <p>
     * If no room then nothing is appended.
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean appendShort(
            boolean nbo,
            short   data)
    {
        boolean retval = false;

        // Length check.
        if (length+2 <= size)
        {
            retval = true;
            EdtConvert.shortToBytes(nbo,this.data,length,data);
            length+=2;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Add int (4 bytes) to buffer <p>
     * If no room then nothing is appended.
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean appendInt(
            boolean nbo,
            int     data)
    {
        boolean retval = false;

        // Length check.
        if (length+4 <= size)
        {
            retval = true;
            EdtConvert.intToBytes(nbo,this.data,length,data);
            length+=4;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Add long (8 bytes) to buffer <p>
     * If no room then nothing is appended.
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean appendLong(
            boolean nbo,
            long    data)
    {
        boolean retval = false;

        // Length check.
        if (length+8 <= size)
        {
            retval = true;
            EdtConvert.longToBytes(nbo,this.data,length,data);
            length+=8;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append (data & 0xff) (1 byte) to buffer as 2 BCD digits. <p>
     * If no room then nothing is appended.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean appendBcd(int data)
    {
        byte[] bcd = new byte[1];

        bcd = EdtConvert.intToBcd(
                (data & 0xFF),
                0,
                2);

        return append((int)bcd[0] & 0xFF);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append short (2 bytes) to buffer as 4 BCD digits. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to append.
     * @return True if appeded, else false.
     */
    public boolean appendShortBcd(
            boolean nbo,
            short   data)
    {
        boolean retval = false;

        // Length check.
        if (length+2 <= size)
        {
            retval = true;

            byte[] bcd = new byte[2];

            bcd = EdtConvert.intToBcd(
                    (int)(data & 0xFFFF),
                    0,
                    4);

            // Byte order.
            if (nbo)
            {
                append((int)bcd[1] & 0xFF);
                append((int)bcd[0] & 0xFF);
            }
            else
            {
                append((int)bcd[0] & 0xFF);
                append((int)bcd[1] & 0xFF);
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append int (4 bytes) to buffer as 8 BCD digits. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to append.
     * @return True if appeded, else false.
     */
    public boolean appendIntBcd(
            boolean nbo,
            int     data)
    {
        boolean retval = false;

        // Length check.
        if (length+4 <= size)
        {
            retval = true;

            byte[] bcd = new byte[4];

            bcd = EdtConvert.intToBcd(
                    data,
                    0,
                    8);

            // Byte order.
            if (nbo)
            {
                append((int)bcd[3] & 0xFF);
                append((int)bcd[2] & 0xFF);
                append((int)bcd[1] & 0xFF);
                append((int)bcd[0] & 0xFF);
            }
            else
            {
                append((int)bcd[0] & 0xFF);
                append((int)bcd[1] & 0xFF);
                append((int)bcd[2] & 0xFF);
                append((int)bcd[3] & 0xFF);
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append long (8 bytes) to buffer as 16 BCD digits. <p>
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to append.
     * @return True if appeded, else false.
     */
    public boolean appendLongBcd(
            boolean nbo,
            long    data)
    {
        boolean retval = false;

        // Length check.
        if (length+8 <= size)
        {
            retval = true;

            byte[] bcd = new byte[8];

            bcd = EdtConvert.longToBcd(
                    data,
                    0,
                    16);

            // Byte order.
            if (nbo)
            {
                append((int)bcd[7] & 0xFF);
                append((int)bcd[6] & 0xFF);
                append((int)bcd[5] & 0xFF);
                append((int)bcd[4] & 0xFF);
                append((int)bcd[3] & 0xFF);
                append((int)bcd[2] & 0xFF);
                append((int)bcd[1] & 0xFF);
                append((int)bcd[0] & 0xFF);
            }
            else
            {
                append((int)bcd[0] & 0xFF);
                append((int)bcd[1] & 0xFF);
                append((int)bcd[2] & 0xFF);
                append((int)bcd[3] & 0xFF);
                append((int)bcd[4] & 0xFF);
                append((int)bcd[5] & 0xFF);
                append((int)bcd[6] & 0xFF);
                append((int)bcd[7] & 0xFF);
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert hex string to byte[] and append byte[] to buffer. <p>
     * If no room then nothing is appended.
     *
     * The hex string may be in one of two forms.
     * Form1 is edt slash form.
     * Form2 is ascii-hex chars (spaces are ignored).
     *
     * If form2 is used then the converted string
     * will be right padded with '0' to make the
     * string length even before it is converted.
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean appendHexString(String data)
    {
        boolean retval = false;

        byte[] b1 = EdtConvert.hexStringToBytes(data);
        retval = append(b1);

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Append string to a buffer. <p>
     * The string is slashed processed before appending.
     * The bytes of the string are appended.
     * If no room then nothing is appended.
     *
     * @param data What to append.
     * @return True if appended, else false.
     */
    public boolean slashAppend(String data)
    {
        // Slash and append.
        return append(EdtStringUtil.doSlash(data));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Insert byte into position in buffer. <p>
     * All bytes after insert position are moved right 1 position to make
     * room for the insert.
     * If no room then nothing is appended.
     *
     * @param b1 Byte to insert.
     * @param index Index into buffer to insert byte.
     * @return True if inserted, else false.
     */
    public boolean insert(byte b1,
                          int  index)
    {
        boolean retval = false;

        // Sanity.
        if ((length + 1) <= size)
        {
            // Determine number of bytes to move.
            int moveLength = length - index;

            // Move.
            if (moveLength > 0)
            {

                // Dest index.
                int dIndex = length;

                // Move.
                while (moveLength-- > 0)
                {
                    data[dIndex] = data[dIndex-1];
                    dIndex--;
                } // while
            }

            // Inserted.
            retval = true;

            // Insert.
            data[index] = b1;

            // Bump up length.
            length++;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Insert bytes from a buffer into a buffer. <p>
     * All bytes after insert position are moved right to make
     * room for the insert.
     * If no room then nothing is inserted.
     *
     * @param buf Buffer bytes to insert.
     * @param index Index into buffer to insert bytes.
     * @return True if inserted, false otherwise.
     */
    public boolean insert(
            EdtBuffer buf,
            int       index)
    {
        boolean retval = false;

        // Length check.
        if ((length + buf.length) <= size)
        {
            for (int i1=0; i1<buf.length; i1++)
            {
                retval = insert(buf.data[i1],index++);
            } // for
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Insert string into a buffer. <p>
     * All bytes after insert position are moved right to make
     * room for the insert.
     * If no room then nothing is inserted.
     *
     * @param str String to insert.
     * @param index Index into buffer to insert string.
     * @return True if inserted, false otherwise.
     */
    public boolean insert(
            String str,
            int index)
    {
        boolean retval = false;

        if ((length + str.length()) <= size)
        {
            for (int i1=0; i1<str.length(); i1++)
            {
                retval = insert((byte)(str.charAt(i1) & 0xFF),index++);
            } // for
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Insert short (2 bytes) to buffer. <p>
     * If no room then nothing is inserted.
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to insert.
     * @return True if inserted, else false.
     */
    public boolean insertShort(
            boolean nbo,
            short   data,
            int     index)
    {
        boolean retval = false;

        // Length check.
        if (length+2 <= size)
        {
            // Index check.
            if (index < length)
            {
                retval = true;

                // Make room.
                System.arraycopy(
                        this.data,index,
                        this.data,index+2,
                        2);

                // Insert.
                EdtConvert.shortToBytes(nbo,this.data,index,data);

                // Adjust length.
                length+=2;
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Insert int (4 bytes) to buffer. <p>
     * If no room then nothing is inserted.
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to insert.
     * @return True if inserted, else false.
     */
    public boolean insertInt(
            boolean nbo,
            int     data,
            int     index)
    {
        boolean retval = false;

        // Length check.
        if (length+4 <= size)
        {
            // Index check.
            if (index < length)
            {
                retval = true;

                // Make room.
                System.arraycopy(
                        this.data,index,
                        this.data,index+4,
                        4);

                // Insert.
                EdtConvert.intToBytes(nbo,this.data,index,data);

                // Adjust length.
                length+=4;
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Insert long (8 bytes) to buffer. <p>
     * If no room then nothing is inserted.
     *
     * @param nbo true to store in network byte order (MSB..LSB), else
     *            store (LSB..MSB).
     * @param data What to insert.
     * @return True if inserted, else false.
     */
    public boolean insertLong(
            boolean nbo,
            long    data,
            int     index)
    {
        boolean retval = false;

        // Length check.
        if (length+8 <= size)
        {
            // Index check.
            if (index < length)
            {
                retval = true;

                // Make room.
                System.arraycopy(
                        this.data,index,
                        this.data,index+8,
                        8);

                // Insert.
                EdtConvert.longToBytes(nbo,this.data,index,data);

                // Adjust length.
                length+=8;
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Insert string into position in buffer. <p>
     * All bytes after insert position are moved right to make room
     * for the insert.
     * If no room then nothing is inserted.
     *
     * @param str String to insert.
     * @param index Index into buffer to insert string.
     * @return True if inserted, else false.
     */
    public boolean insertString(
            String str,
            int    index)
    {
        boolean retval = false;

        // Length check.
        if ((length + str.length()) <= size)
        {
            int strLen = str.length();
            for (int si=0; si<strLen; si++)
            {
                retval = insert((byte)str.charAt(si),index);
                index++;
            } // for
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Delete one byte from buffer. <p>
     * All bytes after delete position are moved left 1 position.
     *
     * @param index Index of the byte to delete.
     * @return True if deleted, else false.
     */
    public boolean delete(int index)
    {
        boolean retval = false;

        // Sanity.
        if (index < length)
        {
            retval = true;

            // Determine number of move.
            int moveLength = length - index - 1;

            // Move.
            if (moveLength >= 1)
            {
                System.arraycopy(data,index+1,
                                 data,index,
                                 moveLength);

            }

            // Bump down.
            length--;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Delete section from buffer. <p>
     * All bytes after deleted section are moved left.
     *
     * @param index Index of the section to delete.
     * @param length Length of section to delete.
     * @return True if deleted, else false.
     */
    public boolean delete(int index, int length)
    {
        boolean retval = false;

        // Sanity
        if (length > 0)
        {
            while (length-- > 0) {
                retval = delete(index);

            } // while
        }
        else
        {
            retval = true;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert buffer to a string. <p>
     *
     * @return The string.
     */
    public String toString()
    {
        return EdtStringUtil.newString(data,0,length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Return result of toString(HAP2) on obj if obj != null.
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
            String    objName,
            EdtBuffer obj)
    {
        if (obj != null)
        {
            return objName + obj.toString(EdtBufferToStringFormatType.HAP2);
        }
        else
        {
            return objName + "null";
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert buffer to a string. <p>
     *
     * @param type Format type.
     * @return The string.
     */
    public String toString(EdtBufferToStringFormatType type)
    {
        String retval = null;

        // Process based on format type.
        switch (type)
        {
        case HEX :
            retval = toHexString();
            break;

        case HAP1 :
            retval = "data(hex)=" + toHexString() + "\n" +
                     "data(asc)=" + EdtStringUtil.printable(toString());
            break;

        case HAP2 :
            retval =
                "len=0x" + EdtConvert.intToString(length,16) +
                ' ' + '(' + length + ')' + '\n' +
                toString(EdtBufferToStringFormatType.HAP1);
            break;

        case HAP3 :
            StringBuilder sb = new StringBuilder(200);

            sb.append("len=");
            sb.append(EdtConvert.intToStringBase16Base10(length));
            sb.append("\ndata(hex)=");
            sb.append(toHexString());
            sb.append("\ndata(asc)=");

            String printable = EdtStringUtil.printable(toString());
            for (int index=0; index<printable.length(); index++)
            {
                sb.append(printable.charAt(index));
                sb.append(' ');
            } // for

            retval = sb.toString();
            break;

        default :
            retval = toString();
            break;
        } // switch

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert buffer to a string of ASCII-HEX digits. <p>
     *
     * @return The string.
     */
    public String toHexString()
    {
        return EdtConvert.bytesToHexString(this);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do a EdtByteUtil.byteOp on two buffers. <p>
     * The buffer is the dest buffer.
     *
     * @param op The operation.
     * @param sourBuf Source buffer.
     */
    public void byteOp(EdtByteUtil.Op op,
                       EdtBuffer      sourBuf)
    {
        EdtByteUtil.byteOp(op,
                           this.data,0,this.length,
                           sourBuf.data,0,sourBuf.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do a EdtByteUtil.byteOp on a buffer. <p>
     *
     * @param op The operation.
     * @return Operation result.
     */
    public byte byteOp(EdtByteUtil.Op op)
    {
        return EdtByteUtil.byteOp(op,
                                  this.data,0,this.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do a EdtByteUtil.byteOp on a buffer. <p>
     *
     * @param op The operation.
     * @param index Index into buffer.
     * @param len Buffer length.
     * @return Operation result.
     */
    public byte byteOp(EdtByteUtil.Op op,
                       int            index,
                       int            len)
    {
        return EdtByteUtil.byteOp(op,
                                  this.data,index,len);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do a EdtByteUtil.byteOp on a buffer. <p>
     *
     * @param op The operation.
     * @return Operation result.
     */
    public int byteOpInt(EdtByteUtil.Op op)
    {
        return EdtByteUtil.byteOpInt(op,this.data,0,this.length);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Do a EdtByteUtil.byteOp on a buffer. <p>
     *
     * @param op The operation.
     * @param index Index into buffer.
     * @param len Number of bytes to include in the operation.
     * @return Operation result.
     */
    public int byteOpInt(EdtByteUtil.Op op,
                         int            index,
                         int            len)
    {
        return EdtByteUtil.byteOpInt(op,this.data,index,len);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Decrement or increment a byte in the buffer. <p>
     *
     * @param increment True to increment or false to decrement.
     * @param index Byte index. -1 indicates last real byte (length-1).
     * @return The byte's new value or 0 if index out of range.
     */
    public byte decIncByte(boolean increment,
                          int      index)
    {
        byte retval = 0;

        // Sanity.
        if ((length > 0) && (index < size))
        {
            // Set index.
            if (index < 0)
            {
                index = length-1;
            }

            // Change.
            data[index] = increment ? (byte)(data[index]+1)
                                    : (byte)(data[index]-1);

            // Current.
            retval = data[index];
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Compare two buffer data contents. <p>
     * Buffers are equal if: both lengths and data[] conents are same.
     *
     * @param buf1 Buffer to compare to.
     * @return true if buffers compare, false otherwise.
     */
    public boolean compare(EdtBuffer buf1)
    {
        boolean retval = true;

        // Basic checks.
        if ((buf1 != null)
            &&
            (buf1.data != null)
            &&
            (data != null)
            &&
            (length == buf1.length))
        {
            // Data[] checks.
            for (int index=0; index<length; index++)
            {
                if (data[index] != buf1.data[index])
                {
                    retval = false;
                    break;
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
     * Determine if a buffers starts with a string. <p>
     * The buffer's length must be >= string's length and the
     * buffer's data must start with the string.
     *
     * @param str String.
     * @return true if buffer starts with string, else false.
     */
    public boolean startsWith(String str)
    {
    	boolean retval = true;

    	if (((data != null) && (str != null))
    		&&
    		((length > 0) && (str.length() > 0))
    		&&
    		(length >= str.length()))
    	{
    		for (int index=0; index<str.length() && retval; index++)
    		{
    			if (data[index] != (byte)(str.charAt(index) & 0xFF))
    			{
    				retval = false;
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
     * Clear marks. <p>
     */
    public void marksClear()
    {
        marksReadIndex = 0;

        if (marks != null)
        {
            marks.clear();
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get length (number) of marks. <p>
     *
     * @return Number of marks.
     */
    public int marksLength()
    {
        return (marks != null) ? marks.size() : 0;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Write mark w/o an object. <p>
     */
    public void marksWrite()
    {
        marksWrite(null);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Write mark w/an object. <p>
     * The mark should be written before new data added to buffer.
     *
     * @param object Object to associate w/this mark.
     */
    public void marksWrite(Object object)
    {
        // Init.
        marksInit();

        // New.
        MarkData markData = new MarkData();

        // Mark spot in buffer's data where new data will be added.
        markData.index = length;

        // Mark object.
        markData.object = object;

        // Store.
        if (marks != null)
        {
            marks.add(markData);
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Reset to read all marks again. <p>
     * This resets the mark read index.
     */
    public void marksReadReset()
    {
        marksReadIndex = 0;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Read (get) next marked section of buffer. <p>
     * This advances the mark read index.
     *
     * @return The marked part of the buffer or null if no more marks.
     */
    public EdtBuffer marksRead()
    {
        EdtBuffer retval = null;

        // Nothing to read.
        if (marks == null)
        {
            return null;
        }

        // Checks.
        if ((length > 0)                              // Must have something.
            &&
            (marks.size() > 0)                        // Must have something.
            &&
            (marksReadIndex < marks.size()))          // Read not at end.
        {
            // The mark.
            MarkData markData = (MarkData)marks.get(marksReadIndex);

            // Get buffer start & end points.
            int start = 0;
            int end = 0;
            if ((marksReadIndex+1) == marks.size())
            {
                start = markData.index;
                end   = (length - 1);
            }
            else
            {
                MarkData markData2 = (MarkData)marks.get(marksReadIndex+1);
                start = markData.index;
                end   = markData2.index - 1;
            }

            // Make new buffer w/marked data.
            retval = new EdtBuffer(size);
            retval.length = (end-start + 1);
            System.arraycopy(data,start,retval.data,0,retval.length);

            // Update.
            marksReadIndex++;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Read (get) next mark index. <p>
     *
     * @param advance If true the mark read index is advanced.
     * @return The index or -1 if no read index.
     */
    public int marksReadIndex(boolean advance)
    {
        int retval = -1;

        // Checks.
        if ((length > 0)                              // Must have something.
            &&
            (marks != null)                           // Must have marks.
            &&
            (marks.size() > 0)                        // Must have something.
            &&
            (marksReadIndex < marks.size()))          // Read not at end.
        {
            // Get index.
            retval = marks.get(marksReadIndex).index;

            // Advance.
            if (advance)
            {
                marksReadIndex++;
            }
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Read next mark's object. <p>
     * This does not advance the mark read index.
     *
     * @return Objected associated w/this mark or null.
     */
    public Object marksReadObject()
    {
        Object retval = null;

        // Must have something to read.
        if ((marks != null)
            &&
            (marks.size() > 0)
            &&
            (marksReadIndex < marks.size()))
        {
            MarkData markData = (MarkData)marks.get(marksReadIndex);
            retval = (Object)markData.object;
        }

        return retval;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get size of buffer's byte[]. <p>
     *
     * @return The size.
     */
    public int getSize()
    {
        return size;
    }
    
    /**
     * returns an array of used bytes.
     * 
     * @return a data subset from 0 to length 
     */
    public byte[] getRealData(){
    	return Arrays.copyOfRange(this.data, 0, this.length);
    }
    
   /**
    * Create new Sub-EdtBuffer from "startPosition", "length" bytes long
    * 
    * @param startPosition
    *           starting point of sub EdtBuffer
    * @param length
    *           is the length of new sub EdtBuffer
    * @return a new instance of EdtBuffer
    */
   public EdtBuffer newSubEdtBuffer(int startPosition, int length)
   {
      EdtBuffer buffer = null;
      if (length > 0 && startPosition >= 0)
      {
         buffer = new EdtBuffer(this);
         if (length + startPosition <= this.length)
         {
            buffer.delete(length + startPosition, this.length);
         }
         buffer.delete(0, startPosition);
      }
      else
      {
         buffer = new EdtBuffer(0);
      }
      return buffer;
   }

   /**
    * Create new EdtBuffer with left-most "length" bytes
    * 
    * @param length
    *           is the length of new sub EdtBuffer
    * @return a new instance of EdtBuffer
    */
   public EdtBuffer newLeftSubEdtBuffer(int length)
   {
      EdtBuffer buffer = null;
      if (this.length > length)
      {
         buffer = new EdtBuffer(this);
         buffer.delete(0, length);
      }
      else
      {
         buffer = new EdtBuffer(this.size);
      }
      return buffer;
   }

   /**
    * Create new EdtBuffer with right-most "length" bytes
    * 
    * @param length
    *           is the length of new sub EdtBuffer
    * @return a new instance of EdtBuffer
    */
   public EdtBuffer newRightSubEdtBuffer(int length)
   {
      EdtBuffer buffer = null;
      if (this.length > length)
      {
         buffer = new EdtBuffer(this);
         buffer.delete(0, this.length - length);
      }
      else
      {
         buffer = new EdtBuffer(this.size);
      }
      return buffer;
   }

    // ******************************************************************
    // PROTECTED METHODS.
    // ******************************************************************

    // ******************************************************************
    // PRIVATE METHODS.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Marks init. <p>
     */
    private void marksInit()
    {
        if (marks == null)
        {
            marks = new Vector<MarkData>();
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Copy marks. <p>
     *
     * @param aBuf Source buffer.
     */
    private void marksCopy(EdtBuffer aBuf)
    {
        // Must have something to copy.
        if (aBuf.marks != null)
        {
            // Copy info.
            this.marksReadIndex = aBuf.marksReadIndex;

            // New marks.
            this.marks = new Vector<MarkData>();

            // Copy marks.
            for (int index=0; index<aBuf.marksLength(); index++)
            {
                // Old and new.
                MarkData markDataOld  = (MarkData)aBuf.marks.get(index);
                MarkData markDataNew = new MarkData();

                // Copy.
                markDataNew.index  = markDataOld.index;
                markDataNew.object = markDataOld.object;

                // Add.
                this.marks.add(markDataNew);
            } // for
        }
    }

    // ******************************************************************
    // INNER CLASSES.
    // ******************************************************************

    /**
     * Mark data. <p>
     */
    private class MarkData
    {
        // Mark index into buffer's data.
        public int index = 0;

        // Optional mark object.
        public Object object = null;
    }

    // ******************************************************************
    // NATIVE METHODS.
    // ******************************************************************

    // ******************************************************************
    // MAIN.
    // ******************************************************************
}

// ----------------------------------------------------------------------

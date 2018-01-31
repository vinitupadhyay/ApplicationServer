/*
 * File: EdtHexRecord.java
 *
 * Desc: Edt hex record processing.
 *
 * Gilbarco Inc. 2005
 *
 * History:
 *    2005.04.07.thu.Scott Turner  v01.0.01
 *       - Created.
 */

// ----------------------------------------------------------------------
package troubleshoot.util;

// ----------------------------------------------------------------------
// import

// ----------------------------------------------------------------------
/**
 * Hex record processing. <p>
 *
 * @author Scott Turner
 */
public class EdtHexRecord {

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
    public void finalize() throws Throwable {
    }

    // ******************************************************************
    // PUBLIC METHODS       (general, getter, setter, interface imp)
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Parse an intel hex record and return its data field. <p>
     * Record types 0 & 1 are supported.
     *<br>
     *<br>
     * Intel HEX-record Format
     *<br>
     *<br>
     *
     * Intel's Hex-record format allows program or data files to be
     * encoded in a printable (ASCII) format. This allows viewing
     * of the object file with standard tools and easy file transfer
     * from one computer to another, or between a host and target.
     * An individual Hex-record is a single line in a file composed
     * of many Hex-records.
     *
     * Hex-Records are character strings made of several fields which
     * specify the record type, record length, memory address, data,
     * and checksum. Each byte of binary data is encoded as a 2-character
     * hexadecimal number: the first ASCII character representing the
     * high-order 4 bits, and the second the low-order 4 bits of the byte.
     *<br>
     *<br>
     * The 6 fields which comprise a Hex-record are defined as follows:
     * <br><br>
     * <table border=1>
     * <tr>
     * <th>Field</th>          <th>Name</th>          <th>Size</th>		<th>Description</th>
     * </tr>
     * <tr>
     * <td>1</td>      <td>Start Code</td>        <td>1</td>    <td>An ASCII colon, ":"</td>
     * </tr>
     * <tr>
     * <td>2</td>	   <td>Byte Count</td>		  <td>2</td>	<td>The count of the character pairs in the data field</td>
     * </tr>
     * <tr>
     * <td>3</td>	    <td>Address</td>		   <td>4</td>    <td>The 2-byte address at which the data field is to be loaded into memory</td>
     * </tr>
     * <tr>
     * <td>4</td>		<td>Type</td>			   <td>2</td>	<td>00, 01, or 02</td>
     * </tr>
     * <tr>
     * <td>5</td>			<td>Data</td>				<td>0-2n</td>	<td>From 0 to n bytes of executable code, or memory loadable data. n is normally 20 hex (32 decimal) or less</td>
     * </tr>
     * <tr>
     * <td>6</td>			<td>CheckSum</td>			<td>2</td>		<td>The least significant byte of the two's complement sum of values represented by all the pairs of characters in the record except the start code and checksum.</td>
     * </tr>
     * </table>
     *
     * 
     * <br><br>
     * Each record may be terminated with a CR/LF/NULL.
     * Accuracy is ensured by the byte count and checksum fields.
     * <br>
     *<br><strong>
     * Record Types:
     * </strong>
     * <br>
     * <ul>
     *   <li> 00  A data record w/2-byte data address and w/x data bytes.
     *    <li>01  A termination record for a file of Hex-records.
     *        Only one termination record is allowed per file and it
     *        must be the last line of the file. There is no data field.
     *    <li>02  A segment base address record.
     *        This type of record is ignored by Lucid programmers.
     *</ul>
     * @param data Data from the data field of the parsed record.
     * @param info Info from parse attempt. Should be sized [5].
     *                [0] = Record data address.
     * @param line The intel hex record line to parse.
     * @return Parse status:
     * <br>
     * <ul>
     *             <li>> 0 - Line parsed ok & contains data w/length = return value.
     *                   info[0] set.
     *             <li>= 0 - Line parsed ok & contains termination record.
     *             <li>< 0 - Line parse failure.
     *             <li> -1 - Line parse failure: Basic checks failed.
     *             <li> -2 - Line parse failure: Bad record type.
     *             <li>-3 - Line parse failure: Checksum failed.
     * </ul>            
     */
    public static int parseIntelHexRecord(byte[] data,
                                          int[]  info,
                                          String line) {
        int retval = -1;

        // Basic checks.
        if ((line != null)
            &&
            (line.length() >= 11)
            &&
            (line.charAt(0) == ':')) {

            // Init.
            int calcCrc = 0;
            int dataIndex = 0;

            // Get some fields.
            int lineDataCount = EdtConvert.stringToInt(line,1,2,16);
            int lineAddress   = EdtConvert.stringToInt(line,3,4,16);
            int lineType      = EdtConvert.stringToInt(line,7,2,16);
            int lineCrc       = EdtConvert.stringToInt(line,(9 + (lineDataCount * 2)),2,16);

            // Info
            info[0] = lineAddress;

            // Check record type.
            if ((lineType == 0) || (lineType == 1)) {
                // Parse line.
                int byte1 = 0;
                for (int index=1; index<(9+(lineDataCount*2)); index+=2) {
                    // Get byte.
                    byte1 = (byte)EdtConvert.stringToInt(line,index,2,16);

                    // Gather checksum.
                    calcCrc += byte1;

                    // Gather data.
                    if (index >= 9) {
                        data[dataIndex++] = (byte)byte1;
                    }
                } //for

                // Check checksum.
                calcCrc &= 0xff;
                calcCrc = (~calcCrc);
                calcCrc++;
                calcCrc &= 0xff;

                if (calcCrc == lineCrc) {
                    retval = lineDataCount;
                } else {
                    // Bad crc.
                    retval = -3;
                }
            } else {
                // Bad line type.
                retval = -2;
            }
        } else {
            // Basic checks failed.
            retval = -1;
        }

        return retval;
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

package troubleshoot.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CheckedInputStream;

public class CRC32
{
   // ******************************************************************
   // STATIC FIELDS
   // ******************************************************************

   private static final byte[] CRC32_ERROR           = new byte[] { 0x00 };

   // ******************************************************************
   // STATIC METHODS
   // ******************************************************************

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
   // PUBLIC METHODS (general, getter, setter, interface imp)
   // ******************************************************************

   public static byte[] getValue(File file,boolean direct)
   {
	  CheckedInputStream cis = null;
      try
      {
         cis = new CheckedInputStream(new FileInputStream(file), new java.util.zip.CRC32());
         byte[] buf = new byte[128];
         while (cis.read(buf) >= 0);
         if(direct)
            return longToByte((cis.getChecksum().getValue()));
         else
            return longToByteIndirect((cis.getChecksum().getValue()));
      }
      catch (FileNotFoundException e)
      {
         System.err.println("File not found.");
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      finally
      {
    	if(cis != null)
    	{
    		try
    		{
				cis.close();
			}
    		catch (IOException e)
    		{
				e.printStackTrace();
			}
    	}
      }
      return CRC32_ERROR;
   }

   public static byte[] getValue(byte[] data)
   {
      return longToByte(crc32Update(data));
   }

   // ******************************************************************
   // PROTECTED METHODS.
   // ******************************************************************

   // ******************************************************************
   // PRIVATE METHODS.
   // ******************************************************************

   private static long crc32Update(byte[] data)
   {
      java.util.zip.CRC32 crc = new java.util.zip.CRC32();
      crc.update(data);
      return crc.getValue();

   }

   private static byte[] longToByte(long crc)
   {
      byte[] b = new byte[4];
      for (int i = 0; i < 4; i++)
      {
         b[3 - i] = (byte) (crc >>> (i * 8));
      }
      return b;
   }
   
   private static byte[] longToByteIndirect(long crc)
   {
      byte[] b = new byte[4];
      for (int i = 0; i < 4; i++)
      {
         b[i] = (byte) (crc >>> (i * 8));
      }
      return b;
   }
   
   // ******************************************************************
   // INNER CLASSES.
   // ******************************************************************

   // ******************************************************************
   // MAIN.
   // ******************************************************************

}

package troubleshoot.util;
//----------------------------------------------------------------------
import java.awt.Dimension;
import java.util.Properties;

//----------------------------------------------------------------------
/**
* Edt property utilities. <p>
*/
public class EdtPropertyUtil
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

 // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 /**
  * Create. <p>
  */
 public EdtPropertyUtil()
 {
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

 //  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 /**
  * Get a boolean property value from some properties. <p>
  * "true" = true returned.
  * "false" = false returned.
  * Else false returned.
  *
  * @param ps The properties.
  * @param pk Property key.
  * @param pd Default property value.
  * @return Property value.
  */
 public static boolean getBooleanProp(
         Properties ps,
         String     pk,
         String     pd)
 {
     boolean retval = false;

     // Get value.
     String pv = ps.getProperty(pk,pd);

     // Check.
     if (pv != null)
     {
         // Check value.
         retval = (pv.equalsIgnoreCase("true"));
     }

     return retval;
 }

 // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 /**
  * Get a dimension property value from some properties. <p>
  * Value format: xSize,ySize.
  * A size may be '*' to use default size.
  * A size may be +num or -num to increase/decrease default size.
  *
  * @param ps The properties.
  * @param pk Property key.
  * @param defX Default x value.
  * @param defY Default y value.
  * @return Property value.
  */
 public static Dimension getDimensionProp(
         Properties ps,
         String     pk,
         int        defX,
         int        defY)
 {
     // Get value.
     String pv = ps.getProperty(pk);

     // Check.
     if (pv != null)
     {
         // Split.
         String[] parts = pv.split(",");

         // Check.
         if (parts.length >= 2)
         {
             String part = null;

             // X size.
             part = parts[0].trim();
             if ((part.startsWith("+") && (part.length() >= 2)))
             {
                 defX += EdtConvert.stringToInt(part.substring(1));
             }
             else if ((part.startsWith("-") && (part.length() >= 2)))
             {
                 int v = EdtConvert.stringToInt(part.substring(1));

                 defX = ((defX - v) > 0) ? defX - v : defX;
             }
             else
             {
                 int x = EdtConvert.stringToInt(part);
                 defX = (x > 0) ? x : defX;
             }

             // Y size.
             part = parts[1].trim();
             if ((part.startsWith("+") && (part.length() >= 2)))
             {
                 defY += EdtConvert.stringToInt(part.substring(1));
             }
             else if ((part.startsWith("-") && (part.length() >= 2)))
             {
                 int v = EdtConvert.stringToInt(part.substring(1));

                 defY = ((defY - v) > 0) ? defY - v : defY;
             }
                 else
             {
                 int x = EdtConvert.stringToInt(part);
                 defY = (x > 0) ? x : defY;
             }
         }
     }

     return new Dimension(defX,defY);
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

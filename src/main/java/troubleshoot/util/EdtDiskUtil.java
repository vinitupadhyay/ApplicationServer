
package troubleshoot.util;

import java.io.File;
import java.util.Vector;

import javax.swing.filechooser.FileSystemView;

public class EdtDiskUtil
{

	private static final String	REMOVABLE_DISK_DESCRIPTION	= "Removable Disk";
	private static final String	REMOVABLE_DISK_DESCRIPTION_FOR_WINDOW10	= "USB Drive";

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
	public EdtDiskUtil()
	{

	}

	// ******************************************************************
	// PUBLIC METHODS
	// ******************************************************************
	/**
	 * @return String[] the list of all available "removable disk"
	 */
	public static String[] getRemovableDisks()
	{
		try
		{
			FileSystemView fsv = FileSystemView.getFileSystemView( );
			File[] froots = File.listRoots( );
			Vector<String> rootname = new Vector<String>( );
			for (File f : froots)
			{
				if (fsv.getSystemTypeDescription( f ).contains( REMOVABLE_DISK_DESCRIPTION ) || fsv.getSystemTypeDescription( f ).contains( REMOVABLE_DISK_DESCRIPTION_FOR_WINDOW10))
				{
					rootname.add( f.toString( ) );
				}
			}
			String[] srootname = new String[rootname.size( )];
			rootname.toArray( srootname );
			return srootname;
		}
		catch (Exception e)
		{
			return new String[] {};
		}
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

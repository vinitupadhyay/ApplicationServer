
package troubleshoot.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class EdtSortedProperties extends Properties
{


	public EdtSortedProperties(Properties theProperties)
	{
		// copy the list of keys
		Enumeration keysEnum = theProperties.keys( );
		// copy the list of properties to internal properties.
		while (keysEnum.hasMoreElements( ))
		{

			String name = (String) keysEnum.nextElement( );
			setProperty( name, theProperties.getProperty( name ) );
		}

	}

	@SuppressWarnings("unchecked")
	public Enumeration keys()
	{
		Enumeration keysEnum = super.keys( );
		Vector<String> keyList = new Vector<String>( );
		while (keysEnum.hasMoreElements( ))
		{
			keyList.add( (String) keysEnum.nextElement( ) );
		}
		Collections.sort( keyList );
		return keyList.elements( );
	}

}

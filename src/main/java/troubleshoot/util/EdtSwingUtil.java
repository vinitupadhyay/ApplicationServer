
package troubleshoot.util;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * EdtSwingUtil. <p>
 * 
 * @author mgrieco
 * Swing helper methods.
 */
public class EdtSwingUtil
{
	// ******************************************************************
	// CONSTRUCTOR.
	// ******************************************************************

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	/**
	 * Create.
	 * <p>
	 */
	public EdtSwingUtil()
	{

	}

	/**
	 * @param Component, the component that requires to focus the next element.
	 * <p>
	 */
	public static void focusNextElement(Component e)
	{
		Component comp = getNextElement( e );
		if (comp != null)
		{
			comp.requestFocus( );
			if (comp instanceof JTextField)
			{
				((JTextField) comp).setSelectionStart( 0 );
				((JTextField) comp).setSelectionEnd( ((JTextField) comp).getText().length( ));
			}
		}
	}

	/**
	 * @param Component, the component to find its next element.
	 * <p>
	 */
	public static Component getNextElement(Component e)
	{
		JPanel p = (JPanel) e.getParent( );
		Component[] comp = p.getComponents( );
		for (int i = 0; i < comp.length; ++i)
		{
			if (p.getComponent( i ).equals( e ))
			{
				if (i < comp.length - 1)
				{
					return p.getComponent( i + 1 );
				}
				else
				{
					return p.getComponent( 0 );
				}
			}
		}
		return null;
	}

}

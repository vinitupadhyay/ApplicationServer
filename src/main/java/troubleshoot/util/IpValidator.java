
package troubleshoot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpValidator
{

	private Pattern				pattern;
	private Matcher				matcher;

	private static final String	IP_PATTERN	= "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";

	public IpValidator()
	{
		pattern = Pattern.compile( IP_PATTERN, Pattern.CASE_INSENSITIVE );
	}

	public boolean validate(final String hex)
	{

		matcher = pattern.matcher( hex );
		return matcher.matches( );

	}

	public static String[] splitIP(String strIP)
	{
		String[] splits = {};
		IpValidator valid = new IpValidator( );
		if (valid.validate( strIP ))
		{
			splits = strIP.split( "\\." );
		}
		return splits;
	}

	public static String joinIP(String strIP1, String strIP2, String strIP3, String strIP4)
	{
		String strIP = strIP1 + "." + strIP2 + "." + strIP3 + "." + strIP4;
		IpValidator valid = new IpValidator( );
		if (!valid.validate( strIP ))
		{
			strIP = "";
		}
		return strIP;
	}
}

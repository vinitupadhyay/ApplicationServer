/**
 * © 2014 Gilbarco Inc.
 * Confidential and Proprietary
 *
 * @file BadResourceFilenameException.java
 * @author mgrieco
 * @date 26 May 2014
 * @copyright © 2014 Gilbarco Inc. Confidential and Proprietary
 */
package troubleshoot.exceptions;

/**
 * @class BadResourceFilenameException.java "com.gilbarco.globaltools.flexpay.maintenance.gui"
 * @brief This class is the exception extension to be use in different resources uses.
 * @since 1.0.0
 */
public class BadResourceFilenameException extends Exception
{
	/** The generated constant serialVersionUID. */
	private static final long	serialVersionUID	= 7832094534524615528L;

	/** 
	 * Default constructor
	 ***/
	public BadResourceFilenameException()
	{
		super();
	}

	/** 
	 * Default constructor with a given error description
	 * @param errorDescription, error description to identify the problem.
	 * **/
	public BadResourceFilenameException(String errorDescription)
	{
		super(errorDescription);
	}
}
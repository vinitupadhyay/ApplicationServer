/**
 * © 2014 Gilbarco Inc.
 * Confidential and Proprietary
 *
 * @file EdtUtilException.java
 * @author Guillermo Paris
 * @date 29 May 2014
 * @copyright © 2014 Gilbarco Inc. Confidential and Proprietary
 *
 */
package troubleshoot.util;

/**
 * @author Guillermo Paris
 * 
 * @brief This class extends the base class Exception as a mean to transport error messages
 * from this library to upper layers having logging.
 */
public class EdtUtilException extends Exception
{
	// ******************************************************************
	// INNER TYPES 
	// ******************************************************************

	// ******************************************************************
	// STATIC FIELDS 
	// ******************************************************************
	private static final long serialVersionUID = 1L;
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
	// CONSTRUCTORS.
	// ******************************************************************
	/**
	 * @brief Default Constructor
	 */
	public EdtUtilException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	 public EdtUtilException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public EdtUtilException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public EdtUtilException(Throwable cause)
	{
		super(cause);
	}

	// ******************************************************************
	// OVERRIDDEN METHODS  (invoked from polymorphic interface).
	// ******************************************************************

	// ******************************************************************
	// PUBLIC METHODS	  (general, getter, setter, interface imp)
	// ******************************************************************

	// ******************************************************************
	// PROTECTED METHODS.
	// ******************************************************************

	// ******************************************************************
	// PRIVATE METHODS.
	// ******************************************************************

	// ******************************************************************
	// STATIC METHODS 
	// ******************************************************************

	// ******************************************************************
	// MAIN.
	// ******************************************************************

}

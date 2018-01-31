package troubleshoot.exceptions;

public class TroubleshootUtilException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @brief Default Constructor
	 */
	public TroubleshootUtilException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	 public TroubleshootUtilException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public TroubleshootUtilException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public TroubleshootUtilException(Throwable cause)
	{
		super(cause);
	}
}

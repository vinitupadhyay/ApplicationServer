package troubleshoot.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Formatter;

import org.apache.log4j.Logger;

import troubleshoot.model.pojo.ResourceCertificate;
import troubleshoot.states.ResourceCertificateField;

public class VanguardResourceCertificate extends ResourceCertificate
{
	// ******************************************************************
	// STATIC FIELDS
	// ******************************************************************
	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(VanguardROMfsCertificate.class);


	/** The Constants for each vanguard certificate field data*/
	private static final String			VERSION_KEY					= "V";
	private static final String			TYPE_KEY					= "TYPE";
	private static final String			FILE_ID_KEY					= "FILE_ID";
	private static final String			VENDOR_NAME_KEY				= "VENDOR_NAME";
	private static final String			PRODUCT_BOARD_NAME_KEY		= "PRODUCT_BOARD_NAME";
	private static final String			APPLICATION_VERSION_KEY		= "APPLICATION_VERSION";
	private static final String			DATE_AND_TIME_KEY			= "DATE_AND_TIME";
	private static final String			HARDWARE_COMPATIBILITY_KEY	= "HARDWARE_COMPATIBILITY";
	private static final String			X509_CHAIN_KEY				= "X509_CHAIN";
	private static final String			RES_INFO_KEY				= "RESINFO";
	
	// ******************************************************************
	// PROTECTED FIELDS.
	// ******************************************************************
	
	/** The start certif label. */
	private byte[]	startCertifLabel;

	/** The file type field. */
	private byte[]	fileType;

	/** The hardware compatibility file field. */
	protected byte[] hardwareCompatibility = null;


	// ******************************************************************
	// CONSTRUCTORS.
	// ******************************************************************

	/**
	 * Instantiates a new vanguard file certificate.
	 *
	 * @param file the file
	 */
	public VanguardResourceCertificate ( File file )
	{
		super( file );
	}

	/**
	 * Instantiates a new vanguard file certificate.
	 *
	 * @param certificate the certificate
	 */
	public VanguardResourceCertificate ( byte[] certificate )
	{
		super( certificate );
	}
	
	/**
	 * Instantiates a new vanguard file certificate.
	 *
	 * @param resourceRelativePath the relative file Path
	 */
	public VanguardResourceCertificate (String resourceRelativePath )
	{
		super( resourceRelativePath );
	}


	// ******************************************************************
	// PUBLIC METHODS (general, getter, setter, interface impl)
	// ******************************************************************

	/**
	 * Gets the hardware compatibility.
	 *
	 * @return the hardware compatibility file field.
	 */
	public byte[] getHardwareCompatibility()
	{
		return hardwareCompatibility;
	}

	// ******************************************************************
	// OVERRIDDEN METHODS.
	// ******************************************************************

	@Override
	protected void calculateCertificateAndSignaturePosition(FileInputStream in) throws IOException
	{
		int iMSB = 0, iLSB = 0;
		int fileSize = (int) getFile().length();
		iSignatureLength = ROMFS_SIGNATURE_FIXEDSIZE;
		iSignatureOffset = fileSize - iSignatureLength;

		if(in.getChannel().position() > 0)
		{
			in.getChannel().position(0); // file must be scanned from the beginning
		}

		// reads the certificate length
		in.skip(iSignatureOffset - 2); // it positions over the certificate length field
		iMSB = in.read();
		if( iMSB < 0 ) iMSB += 256;
		iLSB = in.read();
		if( iLSB < 0 ) iLSB += 256;

		// file position must be restored to the beginning for proper further scanning
		in.getChannel().position(0);

		iCertificateLength = 256 * iMSB + iLSB;
		iCertificateOffset = iSignatureOffset - iCertificateLength;

		return;
	}
	
	@Override
	protected void calculateCertificateAndSignaturePosition(InputStream in) throws IOException
	{
		int iMSB = 0, iLSB = 0;
		int fileSize = in.available();
		iSignatureLength = ROMFS_SIGNATURE_FIXEDSIZE;
		iSignatureOffset = fileSize - iSignatureLength;
		
		// reads the certificate length
		in.skip(iSignatureOffset - 2); // it positions over the certificate length field
		iMSB = in.read();
		if( iMSB < 0 ) iMSB += 256;
		iLSB = in.read();
		if( iLSB < 0 ) iLSB += 256;

		iCertificateLength = 256 * iMSB + iLSB;
		iCertificateOffset = iSignatureOffset - iCertificateLength;

		in.close();
		return;
	}

	@Override
	protected boolean AreThereNullFields()
	{
		if( fileId == null )
			return true;
		if (fileType == null)
			return true;
		else if( vendor == null )
			return true;
		else if( boardProduct == null )
			return true;
		else if( resInfo == null )
			return true;
		else if( appVersion == null )
			return true;
		else if( dateAndTime == null )
			return true;
		else if( hardwareCompatibility == null )
			return true;
		else
			return false;
	}
	
	@Override
	protected void scanCertificate() 
	{
		// first field not valuable
		nextField( true );
		// initial certificate label
		startCertifLabel = nextField( false );

		// data fields
		while (hasNextField( ))
		{
			byte[] newfield = nextField( false );
			if (newfield == null) // no more certificates.
				break;

			String data = getFieldData( newfield );
			int indexOf = data.indexOf( "=" );
			if (indexOf > 0)
			{
				ResourceCertificateField key = getkey(data.substring( 0, indexOf ));
				newfield = data.substring( indexOf + 1 ).getBytes( );
				switch(key)
				{
					case TYPE_KEY :
						fileType = newfield;
						break;
					case FILE_ID_KEY :
						fileId = newfield;
						break;
					case VENDOR_NAME_KEY :
						vendor = newfield;
						break;
					case PRODUCT_BOARD_NAME_KEY :
						boardProduct = newfield;
						break;
					case APPLICATION_VERSION_KEY :
						appVersion = newfield;
						break;
					case DATE_AND_TIME_KEY :
						dateAndTime = newfield;
						break;
					case HARDWARE_COMPATIBILITY_KEY :
						hardwareCompatibility = newfield;
						break;
					case RES_INFO_KEY :
						resInfo = newfield;
						break;
					//new unused certificates fields
					case VERSION_KEY:
						break;
					case X509_CHAIN_KEY:
						break;
					default :
						logger.debug("Unkown certificate field - "+ data);
						break; //invalid file, abort.
				}
			}
		}
	}
	
	private ResourceCertificateField getkey(String type)
	{
		ResourceCertificateField field = null;
		if(type.equals(TYPE_KEY))
		{
			field = ResourceCertificateField.TYPE_KEY;
		}
		else if(type.equals(RES_INFO_KEY))
		{
			field = ResourceCertificateField.RES_INFO_KEY;
		}
		else if(type.equals(FILE_ID_KEY))
		{
			field = ResourceCertificateField.FILE_ID_KEY;
		}
		else if(type.equals(VENDOR_NAME_KEY))
		{
			field = ResourceCertificateField.VENDOR_NAME_KEY;
		}
		else if(type.equals(PRODUCT_BOARD_NAME_KEY))
		{
			field = ResourceCertificateField.PRODUCT_BOARD_NAME_KEY;
		}
		else if(type.equals(APPLICATION_VERSION_KEY))
		{
			field = ResourceCertificateField.APPLICATION_VERSION_KEY;
		}
		else if(type.equals(DATE_AND_TIME_KEY))
		{
			field = ResourceCertificateField.DATE_AND_TIME_KEY;
		}
		else if(type.equals(HARDWARE_COMPATIBILITY_KEY))
		{
			field = ResourceCertificateField.HARDWARE_COMPATIBILITY_KEY;
		}
		else if(type.equals(VERSION_KEY))
		{
			field = ResourceCertificateField.VERSION_KEY;
		}
		else if(type.equals(X509_CHAIN_KEY))
		{
			field = ResourceCertificateField.X509_CHAIN_KEY;
		}
		else
		{
			field = ResourceCertificateField.OTHER;
		}
		
		return field;
	}

	/**
	 * Checks for next field possibility.
	 *
	 * @return true, if there are data for a next field.
	 */
	private boolean hasNextField()
	{
		return this.offsetInsideCertificate < iCertificateLength;
	}

	/**
	 * Gets the data of a bytes array field.
	 *
	 * @param data the bytes array field
	 * @return the data
	 */
	private String getFieldData(byte[] data) 
	{
		try
		{
			return new String(data ,"UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			logger.error( e.getMessage( ) );
		}
		return "";
	}


	@Override
	protected byte[] nextField( boolean bStart ) 
	{
		if( bStart )
		{
			this.offsetInsideCertificate = 0;
		}
		
		byte[] buf = new byte[iCertificateLength];
		int offsetInsideTemporalBuf = 0;
		boolean bValidField = false;
		
		while( this.offsetInsideCertificate < iCertificateLength )
		{
			byte aByte = this.certificate[this.offsetInsideCertificate];
			this.offsetInsideCertificate++;
			if( aByte != 0 )
			{
				buf[offsetInsideTemporalBuf] = aByte;
				offsetInsideTemporalBuf++;
				bValidField = true;
			}
			else
			{
				break;
			}
		}
		if( bValidField )
		{
			return Arrays.copyOf(buf, offsetInsideTemporalBuf);			
		}
		else
		{
			return null;
		}
	}	
	
	
	/**
	 * Overridable method.
	 * @return boolean saying Ok or failed.
	 */
	@Override
	public boolean checkCertificate() // overridable method
	{
		if (AreThereNullFields( )) // overridable null field evaluation
		{
			return false;
		}

		String sStartCertifLabel, sFileType, sFileID, sVendor, sBoardProduct, sResInfo, sAppVersion, sDateAndTime, sHardwareCompatibility;
		try
		{
			sStartCertifLabel = new String( startCertifLabel, "UTF-8" );
			sFileType = new String( fileType, "UTF-8" );
			sFileID = new String( fileId, "UTF-8" );
			sVendor = new String( vendor, "UTF-8" );
			sBoardProduct = new String( boardProduct, "UTF-8" );
			sResInfo = new String( resInfo, "UTF-8" );
			sAppVersion = new String( appVersion, "UTF-8" );
			sDateAndTime = new String( dateAndTime, "UTF-8" );
			sHardwareCompatibility = new String( hardwareCompatibility, "UTF-8" );
		}
		catch (UnsupportedEncodingException e)
		{
			return false;
		}

		StringBuilder sMsgBuffer = new StringBuilder( );
		Formatter f = new Formatter( sMsgBuffer );
		String sTempl;
		String sMsg;
		if (certificateFile != null)
		{
			sTempl = "from filename [%s] = fileType [%s], fileId [%s], vendor [%s], boardProduct [%s], resInfo [%s], appVersion [%s], dateAndTime [%s].";
			sMsg = f.format( sTempl, this.certificateFile.getName( ), sFileType, sFileID, sVendor, sBoardProduct, sResInfo, sAppVersion, sDateAndTime ).out( ).toString( );
		}
		else
		{
			sTempl = "from SPOT = fileType [%s], fileId [%s], vendor [%s], boardProduct [%s], resInfo [%s], appVersion [%s], dateAndTime [%s].";
			sMsg = f.format( sTempl, sFileType, sFileID, sVendor, sBoardProduct, sResInfo, sAppVersion, sDateAndTime ).out( ).toString( );
		}

		f.close( );
		logger.debug( sMsg );

		if (!sStartCertifLabel.equalsIgnoreCase( "GILBARCO CERTIFICATE" ))
		{
			logger.error( "Invalid Certificate Format - TYPE, ID, etc." );
			return false;
		}

		return true;
	}
}

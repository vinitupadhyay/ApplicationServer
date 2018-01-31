package troubleshoot.model.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Formatter;

import org.apache.log4j.Logger;

public class ResourceCertificate
{
	// ******************************************************************
	// STATIC FIELDS
	// ******************************************************************
	/** The Constant ROMFS_CERTIFICATE_FIXEDSIZE. */
	protected static final int ROMFS_CERTIFICATE_FIXEDSIZE = 398;
	
	/** The Constant ROMFS_SIGNATURE_FIXEDSIZE. */
	protected static final int ROMFS_SIGNATURE_FIXEDSIZE = 256;
	
	/** The Constant logger. */
	private final static Logger logger = Logger.getLogger(ROMfsCertificate.class);

	/*	The file certificate contain information about version, FWR, APPL, HWR, etc. and signature.
		The max length is 512 bytes. The structure is:
		========================================================================
		VANGUARD Certificate layout      size = variable(LEN_MSB,LEN_LSB) + 256
		<0x00> START CERTIFICATE "GILBARCO VANGUARD CERTIFICATE"
		<0x00> FILE_ID={"recovery","ROMfs1","ROMfs2","ROMfs3","ROMfs4","ROMfs5","ROMfs6","ROMfs7","ROMfs8"}
		<0x00> VENDOR_NAME=
		<0x00> PRODUCT_BOARD_NAME=
		<0x00> APPLICATION_NAME=
		<0x00> APPLICATION_VERSION=
		<0x00> DATE_AND_TIME=
		<0x00> HARDWARE_COMPATIBILITY=
		<0x00>
		<0x00>
			LEN_MSB
			LEN_LSB                 len is from [START_CERTIFICATE ...... Signature]
			Signature [256 bytes]
		========================================================================
	*/

	// ******************************************************************
	// PROTECTED FIELDS.
	// ******************************************************************
	/** The certificate length. */
	protected int iCertificateLength = ROMFS_CERTIFICATE_FIXEDSIZE;
	
	/** The certificate offset. */
	protected int iCertificateOffset = 0;
	
	/** The signature length. */
	protected int iSignatureLength = 0; // VANGUARD field
	
	/** The signature offset. */
	protected int iSignatureOffset = 0; // VANGUARD field
	
	/** The file id. */
	protected byte[] fileId;
	
	/** The vendor. */
	protected byte[] vendor;
	
	/** The board product. */
	protected byte[] boardProduct;
	
	/** The app name. */
	protected byte[] resInfo;
	
	/** The app version. */
	protected byte[] appVersion;		
	
	/** The date and time. */
	protected byte[] dateAndTime;
	
	/** The offset inside certificate. */
	protected int offsetInsideCertificate = 0;
	
	/** The certificate. */
	protected byte[] certificate;
	
	/** The certificate file. */
	protected File certificateFile;

	// ******************************************************************
	// PRIVATE FIELDS.
	// ******************************************************************
	/** The file name. */
	private String fileName;
	
	/** The signature. */
	private byte[] signature;
	
	private String resourceRelativePath;
	
	private boolean isInputStream;

	// ******************************************************************
	// CONSTRUCTORS.
	// ******************************************************************

	/**
	 * Instantiates a new certificate.
	 *
	 * @param file the file for the certificate
	 */
	public ResourceCertificate ( File file )
	{
		this.fileName = file.getAbsolutePath( );
		this.certificateFile = file; 
		this.certificate = null;
		this.resourceRelativePath = null;
		isInputStream = false;
	}

	/**
	 * Instantiates a new certificate.
	 *
	 * @param certificate the certificate data
	 */
	public ResourceCertificate ( byte[] certificate )
	{
		this.fileName = null;
		this.certificateFile = null;
		this.certificate = certificate;
		this.resourceRelativePath = null;
		isInputStream = false;
	}
	
	/**
	 * Instantiates a new certificate.
	 *
	 * @param resourceRelativePath the relative file path 
	 */
	public ResourceCertificate (String resourceRelativePath)
	{
		this.fileName = null;
		this.certificateFile = null;
		this.certificate = null;
		this.resourceRelativePath = resourceRelativePath;
		isInputStream = true;
	}


	// ******************************************************************
	// PUBLIC METHODS (general, getter, setter, interface impl)
	// ******************************************************************

	/**
	 * Gets the certificate size.
	 *
	 * @return the ROMfs certificate size
	 */
	public int getCertificateSize()
	{
		return iCertificateLength;
	}

	/**
	 * Gets the signature size.
	 *
	 * @return the ROMfsCertificateSize
	 */
	public int getSignatureSize()
	{
		return iSignatureLength;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * Gets the file.
	 *
	 * @return the inner file object
	 */
	public File getFile()
	{
		return certificateFile;
	}

	/**
	 * Gets the certificate data.
	 *
	 * @return the binary certificate ( byte array of the certificate )
	 */
	public byte[] getCertificateData()
	{
		return certificate;
	}

	/**
	 * Gets the signature data.
	 *
	 * @return the binary signature ( byte array of the signature )
	 */
	public byte[] getSignatureData()
	{
		return signature;
	}

	/**
	 * Gets the file id.
	 *
	 * @return the fileId
	 */
	public byte[] getFileId()
	{
		return fileId;
	}

	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public byte[] getVendor()
	{
		return vendor;
	}

	/**
	 * Gets the board product.
	 *
	 * @return the boardProduct
	 */
	public byte[] getBoardProduct()
	{
		return boardProduct;
	}

	/**
	 * Gets the app name.
	 *
	 * @return the appName
	 */
	public byte[] getResInfo()
	{
		return resInfo;
	}

	/**
	 * Gets the app version.
	 *
	 * @return the appVersion
	 */
	public byte[] getAppVersion()
	{
		return appVersion;
	}
	
	/**
	 * Gets the date and time.
	 *
	 * @return the dateAndTime
	 */
	public byte[] getDateAndTime()
	{
		return dateAndTime;
	}
	
	public InputStream getInputStream()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream(resourceRelativePath);
	}


	/**
	 * Load certificate.
	 *
	 * @param blParseIt the bl parse it
	 * @return true, if successful
	 */
	public boolean loadCertificate(boolean blParseIt)
	{
		if( certificate == null )
		{
			try
			{
				if(isInputStream)
				{
					calculateCertificateAndSignaturePosition(getInputStream());
					
					InputStream inStream = getInputStream();
					inStream.skip( iCertificateOffset );
					certificate = new byte[iCertificateLength];
					inStream.read( certificate );
					if( iSignatureLength > 0) // Vanguard only
					{
						signature = new byte[iSignatureLength];
						inStream.read( signature );
					}
					inStream.close();
				}
				else
				{
					FileInputStream in = new FileInputStream(fileName);
					calculateCertificateAndSignaturePosition(in); // overridable method
	
					in.skip( iCertificateOffset );
					certificate = new byte[iCertificateLength];
					in.read( certificate );
					if( iSignatureLength > 0) // Vanguard only
					{
						signature = new byte[iSignatureLength];
						in.read( signature );
					}
					in.close();
				}
			}
			catch (IOException e)
			{
				logger.error(e.getMessage());
				return false;
			}
		}

		if( certificate.length <= 0 )
		{
			return false;
		}

		if( !blParseIt ) // do not parse the binary certificate
			return true;

		scanCertificate(); // overridable method

		return checkCertificate(); // overridable method
	}

	/**
	 * Validate the certificate data.
	 *
	 * @return true if it is valid, false in other case.
	 */
	public boolean checkCertificate() // overridable method
	{
		if( AreThereNullFields() ) // overridable null field evaluation
		{
			return false;
		}
		
		String sFileID, sVendor, sBoardProduct, sResInfo, sAppVersion, sDateAndTime;
		try
		{
			sFileID = new String(fileId ,"UTF-8");
			sVendor = new String(vendor ,"UTF-8");
			sBoardProduct = new String(boardProduct ,"UTF-8");
			sResInfo = new String(resInfo,"UTF-8");
			sAppVersion = new String(appVersion ,"UTF-8");
			sDateAndTime = new String(dateAndTime ,"UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return false;
		}

		StringBuilder sMsgBuffer = new StringBuilder();
		Formatter f = new Formatter(sMsgBuffer);
		String sTempl;
		String sMsg;
		if( this.certificateFile != null )
		{
			sTempl = "from filename [%s] = fileId [%s], vendor [%s], boardProduct [%s], resInfo [%s], appVersion [%s], dateAndTime [%s].";
			sMsg = f.format( sTempl, this.certificateFile.getName( ), sFileID, sVendor, sBoardProduct, sResInfo, sAppVersion, sDateAndTime ).out().toString();
		}
		else
		{
			sTempl = "from SPOT = fileId [%s], vendor [%s], boardProduct [%s], resInfo [%s], appVersion [%s], dateAndTime [%s].";
			sMsg = f.format( sTempl, sFileID, sVendor, sBoardProduct, sResInfo, sAppVersion, sDateAndTime ).out().toString();
		}
		f.close();
		logger.debug( sMsg );
		return true;
	}


	// ******************************************************************
	// PROTECTED METHODS.
	// ******************************************************************
	/**
	 * Calculate certificate and signature position.
	 *
	 * @param in the input file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void calculateCertificateAndSignaturePosition(FileInputStream in) throws IOException
	{
		int fileSize = (int) certificateFile.length();
		iCertificateOffset = fileSize - iCertificateLength ;
		iCertificateLength = ROMFS_CERTIFICATE_FIXEDSIZE;
		iSignatureOffset = 0;
		iSignatureLength = 0;
		return;
	}
	
	/**
	 * Calculate certificate and signature position.
	 *
	 * @param in the InputStream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void calculateCertificateAndSignaturePosition(InputStream in) throws IOException
	{
		int fileSize = (int) certificateFile.length();
		iCertificateOffset = fileSize - iCertificateLength ;
		iCertificateLength = ROMFS_CERTIFICATE_FIXEDSIZE;
		iSignatureOffset = 0;
		iSignatureLength = 0;
		return;
	}

	/**
	 * Are there null fields for the current certificate.
	 *
	 * @return boolean
	 * @author gparis
	 */
	protected boolean AreThereNullFields() // overridable method
	{
		if( fileId == null )
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
		else
			return false;
	}

	/**
	 * Scan the certificate fields.
	 *
	 * @return boolean
	 * @author gparis
	 */
	protected void scanCertificate() // overridable method
	{
		nextField(true);
		fileId 			 = nextField(false);
		vendor 			 = nextField(false);
		boardProduct	 = nextField(false);
		resInfo			 = nextField(false);
		appVersion		 = nextField(false);
		dateAndTime		 = nextField(false);
	}

	
	/**
	 * Next field.
	 *
	 * @param bStart true if it is the first field.
	 * @return the byte[] for a new field.
	 */
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
}


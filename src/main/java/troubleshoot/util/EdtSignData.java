/*
* © 2012 Gilbarco Inc.
* Confidential and Proprietary
*
*/

package troubleshoot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Formatter;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import troubleshoot.controller.TroubleshootController;

/**
 * Sign data methods.
 * <p>
 * 
 * @author Mariano Volker
 */
public class EdtSignData
{
	private static volatile EdtSignData	instance;
	private final static Logger logger = Logger.getLogger(TroubleshootController.class);
	private static final String			S_PEM_CERTIF_HEADER			= "-----BEGIN CERTIFICATE-----\n";
	private static final String			S_PEM_CERTIF_FOOTER			= "\n-----END CERTIFICATE-----\n";
	private static transient String		senderPrivateKeyFilename	= "private.key";
	private static transient String		receiverCertificateFilename	= "certificate.pem";
	private static transient PrivateKey	privateKey					= null;
	private static transient PublicKey	publicKey					= null;

	private EdtSignData()
	{
	}

	public static EdtSignData getInstance()
	{
		if (instance == null)
		{
			synchronized (EdtSignData.class)
			{
				if (instance == null)
				{
					instance = new EdtSignData( );
				}
			}
		}

		return instance;
	}

	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException( );
	}

	public final void setPrivateKeyFilename(String privKeyFilename)
	{
		synchronized (this)
		{
			senderPrivateKeyFilename = privKeyFilename;
		}
	}

	public final void setCertificateFilename(String certFilename)
	{
		synchronized (this)
		{
			receiverCertificateFilename = certFilename;
		}
	}

	/**
	 * This method is used to obtain the private key from a file.
	 * 
	 * @return none.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws KeyStoreException
	 * @throws java.security.cert.CertificateException
	 * @throws UnrecoverableKeyException
	 */
	public final void loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		synchronized (this)
		{	
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec( getPrivateKeyFromFile( ) );
			KeyFactory factory = KeyFactory.getInstance( "RSA" );
			privateKey = (RSAPrivateKey) factory.generatePrivate( pkcs8EncodedKeySpec );			
		}
	}

	/**
	 * This method is used to obtain the private key from a file that is encoded as pkcs12 and der format.
	 * it is used by sas server clients if use that format
	 * @return none.
	 * @throws UnrecoverableKeyException 
	 * */
	public void loadPrivateKeyPKCS12() throws KeyStoreException, IOException, NoSuchAlgorithmException, java.security.cert.CertificateException, FileNotFoundException, UnrecoverableKeyException 
	{
		synchronized (this)
		{	
			KeyStore ksKeys;
			ksKeys = KeyStore.getInstance( "PKCS12" );
			ksKeys.load( new FileInputStream( senderPrivateKeyFilename ), "".toCharArray( ) );
			privateKey = (PrivateKey) ksKeys.getKey( "1", "".toCharArray( ) );
		}
	}

	/**
	 * This method is used to sign the data passed by the parameter.
	 * 
	 * @return obtain the data signed in hex format string, example 45:56:CA:FE.
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 */
	public final String signDataSHA256withRSA(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException
	{
		synchronized (this)
		{
			if (privateKey == null)
			{
				try
				{
					loadPrivateKey( );
				}
				catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e)
				{
					throw new IOException( "The private key has been not generated - " + e.getMessage( ) );
				}
			}

			Signature signature = Signature.getInstance( "SHA256withRSA" );
			signature.initSign( privateKey );
			signature.update( data );

			byte[] signedData = signature.sign( );
			String strSignedDataHex = EdtConvert.bytesToHexString( signedData, 0, signedData.length, 1, ':' );

			return strSignedDataHex.substring( 0, (strSignedDataHex.length( ) - 1) );
		}
	}

	/**
	 * This method is used to verify if the given signature is correct for the data bytes passed as parameter. The signature is verified according to the public key of this object (if it is null then
	 * it is forced to be loaded from the certificate file before performing the algorithm calculation. If the public key can't be retrieved from file, then a CertificateException is raised.
	 * 
	 * @return boolean : true if the signature is correctly verified, false otherwise.
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public final boolean verifySignedData(byte[] data, String strSignature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
	{
		synchronized (this)
		{
			if (publicKey == null)
			{
				try
				{
					loadPublicKey( );
				}
				catch (CertificateException e)
				{
					throw new SignatureException( "The public key has been not generated." );
				}
			}

			byte[] signBytes = EdtConvert.hexStringToBytes( strSignature, ':' );
			Signature signature = Signature.getInstance( "SHA256withRSA" );
			signature.initVerify( publicKey );
			signature.update( data );
			return signature.verify( signBytes );
		}
	}

	public static PublicKey generatePublicKeyFromPEMCertificateFile(String sFullPathName)
	{
		int iFileLength = 0;
		File file = null;
		FileInputStream fisCertif = null;

		file = new File( sFullPathName );
		iFileLength = (int) file.length( );

		try
		{
			fisCertif = new FileInputStream( file );
		}
		catch (FileNotFoundException e)
		{
			Formatter f = new Formatter( );
			f.format( "%s. Generation aborted.\n", e.getMessage( ) );
			logger.error( f.out( ).toString( ) );
			f.close( );
			return null;
		}

		// Input certificate's PEM file is read
		boolean blContinueOk = true;
		byte abEncodedB64Certificate[] = new byte[iFileLength];
		byte[] abBinaryCertificate = null;

		try
		{
			fisCertif.read( abEncodedB64Certificate, 0, iFileLength );
		}
		catch (IOException e) // exception thrown while reading certificate file
		{
			Formatter f = new Formatter( );
			f.format( "Error reading certificate file: %s . Generation aborted.\n", e.getMessage( ) );
			logger.error( f.out( ).toString( ) );
			f.close( );
			blContinueOk = false;
		}

		// Input certificate's PEM file is closed
		try
		{
			fisCertif.close( );
		}
		catch (IOException e) // exception thrown while closing file
		{
			Formatter f = new Formatter( );
			f.format( "Error closing certificate file: %s. Generation aborted.\n", e.getMessage( ) );
			logger.error( f.out( ).toString( ) );
			f.close( );
			blContinueOk = false;
		}

		if (!blContinueOk)
							return null;

		// Input certificate's data is decoded from Base64 to binary
		try
		{
			String s2, s1, sEncodedData = new String( abEncodedB64Certificate, "UTF-8" );
			s1 = sEncodedData.replace( S_PEM_CERTIF_HEADER, "" );
			s2 = s1.replace( S_PEM_CERTIF_FOOTER, "" );
			abBinaryCertificate = DatatypeConverter.parseBase64Binary( s2 );
		}
		catch (UnsupportedEncodingException e)
		{
			Formatter f = new Formatter( );
			f.format( "Error decoding PEM data: %s\n", e.getMessage( ) );
			logger.error( f.out( ).toString( ) );
			f.close( );
			return null;
		}

		// Certificate object is created from structured binary specification
		try
		{
			X509Certificate certificate = X509Certificate.getInstance( abBinaryCertificate );
			return certificate.getPublicKey( ); // public key read from certificate
		}
		catch (CertificateException e)
		{
			Formatter f = new Formatter( );
			f.format( "Error creating certificate from decoded PEM data: %s\n", e.getMessage( ) );
			logger.error( f.out( ).toString( ) );
			f.close( );
			return null;
		}
	}

	public static PublicKey generatePublicKeyFromPEMCertificateFile_Easy(String sFullPathName)
																								throws FileNotFoundException, IOException, CertificateException
	{
		int iFileLength = 0;
		File file = null;
		FileInputStream fisCertif = null;

		file = new File( sFullPathName );
		iFileLength = (int) file.length( );
		fisCertif = new FileInputStream( file );

		// Input certificate's PEM file is now read
		byte abEncodedB64Certificate[] = new byte[iFileLength];
		byte[] abBinaryCertificate = null;

		fisCertif.read( abEncodedB64Certificate, 0, iFileLength );

		// Input certificate's PEM file is closed
		fisCertif.close( );

		// Input certificate's data is decoded from Base64 to plain text
		String s2, s1, sEncodedData = new String( abEncodedB64Certificate, "UTF-8" );
		s1 = sEncodedData.replace( S_PEM_CERTIF_HEADER, "" );
		s2 = s1.replace( S_PEM_CERTIF_FOOTER, "" );
		abBinaryCertificate = DatatypeConverter.parseBase64Binary( s2 );

		// Certificate object is created from plain text specification
		X509Certificate certificate = X509Certificate.getInstance( abBinaryCertificate );
		return certificate.getPublicKey( ); // public key read from certificate
	}

	/**
	 * Loads the public key from file.
	 * 
	 * @throws CertificateException
	 */
	private void loadPublicKey() throws CertificateException
	{
		String str = "";

		try
		{
			publicKey = generatePublicKeyFromPEMCertificateFile( new java.io.File( receiverCertificateFilename ).getCanonicalPath( ) );
		}
		catch (IOException e)
		{
			str = e.getMessage( );
			publicKey = null;
		}

		if (publicKey == null)
		{
			Formatter f = new Formatter( );
			f.format( "Couldn't get the public key: %s\n", str );
			logger.error( f.out( ).toString( ) );
			f.close( );
			throw new CertificateException( f.out( ).toString( ) );
		}
	}

	/**
	 * This method is used to obtain the private key from a file.
	 * 
	 * @return private key as byte array.
	 * @throws IOException
	 */
	private final byte[] getPrivateKeyFromFile() throws IOException
	{
		File file = new File( new java.io.File( senderPrivateKeyFilename ).getPath( ) );
		FileInputStream fin = new FileInputStream( file );
		byte fileContent[] = new byte[(int) file.length( )];
		fin.read( fileContent );
		fin.close( );
		return fileContent;
	}
}

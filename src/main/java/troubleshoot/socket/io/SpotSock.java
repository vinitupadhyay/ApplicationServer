package troubleshoot.socket.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;

import troubleshoot.model.pojo.IncomingData;
import troubleshoot.model.pojo.SocketAttributes;
import troubleshoot.models.Connection;
import troubleshoot.socket.event.SocketEvent;
import troubleshoot.socket.event.SocketEventService;


public class SpotSock 
{
	private final static Logger logger = Logger.getLogger(SpotSock.class);
	private static final String	TLS_PROTOCOL = "TLSv1.2"; /** TLS Protocol version to use by this service */
	private Socket socket;
	private SocketReadThread reader;
	private SocketAttributes socketAttributes;
	private Vector<IncomingData> readBuffer;
	private boolean isConnected;
	
	public SpotSock(SocketAttributes socketAttributes) 
	{
		this.socketAttributes = socketAttributes;
		readBuffer = new Vector<IncomingData>();
		isConnected = false;
	}
	
	public void setSockAttributes(SocketAttributes attributes)
	{
		socketAttributes = attributes;
	}
	
	/**
	 * This method performs a connection according to the definition parameters.
	 * @return True if successful, false otherwise.
	 * @throws EdtSocketException 
	 */
	private synchronized boolean Connect ()
	{
		if (this.socket != null && this.socket.isConnected())
			return false;

		try 
		{
			this.socket = null;
			this.socket = new Socket(socketAttributes.getIpAddress(),socketAttributes.getPort());
		}
		catch (Exception e) 
		{
			logger.info(e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public synchronized boolean doConnect()
	{
		boolean bSucceeded = false;
		
		if(isConnected)
			return true;
		
		if (this.socket != null && this.socket.isConnected())
			return false;

		try 
		{
			this.socket = null;
			this.socket = new Socket(socketAttributes.getIpAddress(),socketAttributes.getPort());
			if(doStartReader())
			{
				bSucceeded = true;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		isConnected = bSucceeded;
		return bSucceeded;
	}
	
	/**
	 * This method performs a TLS connection according to the definition parameters.
	 * @return True if successful, false otherwise.
	 * @throws EdtSocketException 
	 */
	public synchronized boolean doConnectTLS ()
	{
		boolean bSucceeded = false;
		
		if(isConnected)
			return true;
		
		if (Connect())
		{
			String sTrustKeyFilename = socketAttributes.getTlsTrustKeyFileName();

			try
			{
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				if (sTrustKeyFilename.isEmpty() == false)
				{
					char[] trustStorePassphrase = "G1l*b4rc0".toCharArray();
					InputStream trustStoreIS = new FileInputStream(sTrustKeyFilename);
					KeyStore ksTrust = KeyStore.getInstance("JKS");
					ksTrust.load(trustStoreIS,trustStorePassphrase);
					tmf.init(ksTrust);
				}

				SSLContext sslContext;
				sslContext = SSLContext.getInstance(TLS_PROTOCOL);
				sslContext.init(new KeyManager[0],tmf.getTrustManagers(),new SecureRandom()); // No dual authentication and key management implemented yet.
				SSLSocketFactory tlsSocketFactory = sslContext.getSocketFactory();
				SSLSocket tlsSocket = (SSLSocket) tlsSocketFactory.createSocket(this.socket,this.socket.getInetAddress().getHostAddress(),this.socket.getPort(),true);
				if (tlsSocket != null)
				{
					String[] strSupportedCipherSuites = tlsSocket.getSupportedCipherSuites( );
					String[] strSupportedProtocols = null;
					{
						List<String> strAvailableProtocols = new ArrayList<String>(Arrays.asList(tlsSocket.getSupportedProtocols()));
						List<String> strUnsupportedProtocols = new ArrayList<String>();
						strUnsupportedProtocols.add("SSLv2Hello");
						strAvailableProtocols.removeAll(strUnsupportedProtocols);
						strSupportedProtocols = strAvailableProtocols.toArray(new String[strAvailableProtocols.size()]);
					}
					tlsSocket.setEnabledCipherSuites( strSupportedCipherSuites );
					tlsSocket.setEnabledProtocols( strSupportedProtocols );
					tlsSocket.setUseClientMode( true );
					tlsSocket.setEnableSessionCreation( true );
					tlsSocket.setSoTimeout(30000);
					logger.info("Performing TLS Handshake..." );
					tlsSocket.startHandshake( );
					logger.info("Succeeded TLS Handshake." );
					SSLSession session = tlsSocket.getSession( );
					System.out.println( "EdtClientSocketService - TLS Session Data - protocol:" + session.getProtocol( ) + " ciphersuite:" + session.getCipherSuite());
					logger.info("EdtClientSocketService - TLS Session Data - protocol:" + session.getProtocol( ) + " ciphersuite:" + session.getCipherSuite());
					this.socket = tlsSocket;
					if (doStartReader())
					{
						bSucceeded = true;
					}
					else
					{
						logger.error("TLS Socket reader thread failed to start.");
					}
				}
				else
				{
					logger.error("TLS Socket creation failed.");
				}
			}
			catch (Exception e)
			{
				logger.error(e.getMessage());
				doCloseHard();
			}
			
		}
		isConnected = bSucceeded;
		return bSucceeded;
	}
	
	/**
	 * This method performs a connection close. 
	 */
	public synchronized void close(String message) 
	{
		doCloseSoft();
		doStopReader(message);
	}
	
	/**
	 * This method facilitates a soft disconnection.
	 */
	private synchronized void doCloseSoft ()
	{
		if (this.socket == null)
			return;
		
		if (this.socket.isClosed())
		{
			this.socket = null;
			return;
		}
	}
	
	/**
	 * This method performs a hard disconnection.
	 */
	private synchronized void doCloseHard ()
	{
		if (this.socket == null)
			return;
		
		if (this.socket.isClosed())
		{
			this.socket = null;
			return;
		}
		else
		{
			try
			{
				this.socket.close();
			}
			catch (IOException e)
			{
				logger.error("[SOCKET ERROR] " + e.getMessage( ) + " - Socket close failed while doing a Hard Close.");
			}
			this.socket = null;
			return;
		}
	}
	
	public synchronized boolean sendData(byte[] data) 
	{
		if ( socket == null || this.socket.isClosed() ) 
		{
			logger.info("Socket is closed. Can't send.");
		}
		else 
		{
			try 
			{
				if (!this.socket.isConnected())
				{
					this.doConnectTLS();
				}
				OutputStream oStream = this.socket.getOutputStream();
				oStream.write(data);
				return true;
			}
			catch (IOException e) 
			{
				logger.error("IO_SOCKET_EXCEPTION ["+e.getMessage( )+"]");
				doProcessSocketDisconnect( );
			}
		}
		return false;
	}
	
	/**
	 * This method starts the connection reader thread.
	 * @return True if successful, false otherwise.
	 */
	private synchronized boolean doStartReader ()
	{
		boolean bSucceeded = false;
		if (this.socket != null && this.socket.isConnected())
		{
			this.reader = new SocketReadThread();
			this.reader.start();
			bSucceeded = true;
		}
		return bSucceeded;
	}
	
	/**
	 * This method stops the connection reader thread.
	 * @throws EdtSocketException 
	 */
	private synchronized void doStopReader (String message)
	{
		this.reader.stopReading();
	}
	
	/** Execute a process socket disconnection
	 * note: it is not synchronized to avoid dead locks when it is call from the reader thread
	 * */
	private void doProcessSocketDisconnect()
	{
		if(!this.reader.isReadingStoped( ))
		{
			logger.info("Stop and process SOCKET_DISCONNECT" );
			this.reader.stopReading();
			SocketEventService.getInstance().publish(SocketEvent.Disconnect);
		}
		else
		{
			SocketEventService.getInstance().publish(SocketEvent.Disconnect);
		}
	}
	
	private void receivedMessage(IncomingData incomingData)
	{
		synchronized (readBuffer) 
		{
			readBuffer.addElement(incomingData);
		}
		
	}
	
	public IncomingData getSpotMessage()
	{
		IncomingData data = null;
		synchronized(readBuffer) {
			if (!readBuffer.isEmpty()) {
				data = readBuffer.elementAt(0);
				readBuffer.removeElementAt(0);
			}
		}
		return data;
	}
	
	protected class SocketReadThread extends Thread 
	{
		private static final int DEFAULT_READ_BUFFER_SIZE	= 10000;
		private boolean	stoped;

		public SocketReadThread() 
		{
			super();
			setName("SpotSockReader");
			this.stoped = false;
		}

		public boolean isReadingStoped() 
		{
			return this.stoped;
		}
		
		public void stopReading() 
		{
			this.stoped = true;
		}

		public void resumeReading() 
		{
			this.stoped = false;
		}

		@Override
		public synchronized void run() 
		{
			byte[] lvData = new byte[DEFAULT_READ_BUFFER_SIZE];
			readBuffer.clear();
			
			// Process while open.
			while (!this.stoped && socket!= null && !socket.isClosed())
			{
				try
				{
					// Read.
					int i = socket.getInputStream().read(lvData, 0, lvData.length);
					// Determine what happened.
					if (i > 0)
					{
						byte[] data = Arrays.copyOf(lvData, i);
						receivedMessage(new IncomingData(data));
					}
					else
					{
						logger.error("IO_SOCKET_EXCEPTION [Read error from socket, read size [" + i + "]");
						this.stoped = true;
						SocketEventService.getInstance().publish(SocketEvent.Disconnect);
					}					
				}
				catch (IOException e) 
				{
					if(socket!=null && !socket.isClosed( )) //socket error 
					{
						logger.error("IO_SOCKET_EXCEPTION - SOCKET_DISCONNECT [" + e.getMessage() + "]");
						doProcessSocketDisconnect();
					}
					else
					{
						logger.error("reading socket closed [" + e.getMessage( )+ "]");
					}
					this.stoped = true;
				}
			}
			
			if( socket!= null)
			{	
				try
				{
					if(!socket.isClosed( ))
					{
						socket.close( );
					}
					socket = null;
				}
				catch (IOException e)
				{
					socket = null;
					logger.error(e.getMessage( ));
				}
			}
			
			isConnected = false;
			Connection.getInstance().isLoggedInMaintenaceMode = false;
		}
	}
}

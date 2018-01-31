package spot.messages;

public enum SpotMessageType implements MessageType
{
	PING
	{
		@Override
		public String getName()
		{
			return "Ping";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x03 };
		}
	},
	LOGIN
	{
		@Override
		public String getName()
		{
			return "Login";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x01 };
		}
	},
	LOGOUT
	{
		@Override
		public String getName()
		{
			return "Logout";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x02 };
		}
	},
	RESET
	{
		@Override
		public String getName()
		{
			return "Reset";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x06 };
		}
	},

	/**
	 * Command used for Vanguard Units to access secure menu options.
	 * This message will be handled only if Dual Control is ongoing and if the switch operational is allowed by the token/authentication sent.
	 */
	SERVICE_MENU_SWITCH
	{
		@Override
		public String getName()
		{
			return "ServiceMenuSwitch";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x0D };
		}
	},

	/**
	 * Command used for Vanguard Units to get the serial number and the unit challenge.
	 * This message will be handle only if Dual Control is ongoing.
	 */
	GET_CHALLENGE
	{
		@Override
		public String getName()
		{
			return "GetChallenge";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x0E };
		}
	},
	TEMP_ACTIVATION
	{
		@Override
		public String getName()
		{
			return "TempActivation";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, (byte)0xFF };
		}
	},
	FILE_TRANSFER
	{
		@Override
		public String getName()
		{
			return "FileTransfer";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10 };
		}
	},
	
	/**
	 * @author gparis
	 * @brief SPOT protocol Command 0x01 0x10 0x05 to set the SPOT date & time.
	 */
	SET_DATETIME
	{
		@Override
		public String getName()
		{
			return "SetDateAndTime";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x05, 0x01 };
		}
	},

	/**
	 * @author gparis
	 * @brief SPOT protocol Command 0x01 0x10 0x06 to get the actual SPOT date & time.
	 */
	GET_DATETIME
	{
		@Override
		public String getName()
		{
			return "GetDateAndTime";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x06, 0x01 };
		}
	},

	/**
	 * @author gparis
	 * @brief SPOT protocol Command 0x01 0x10 0x08 to reset one (or all device counter(s).
	 */
	DIAGNOSTIC_COUNTER_RESET
	{
		@Override
		public String getName()
		{
			return "ResetCounters";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x08, 0x01 };
		}
	},
	CREATE_WINDOW_FROM_RES
	{
		@Override
		public String getName()
		{
			return "CreateWindow";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x30, 0x0A };
		}
	},
	CREATE_WINDOW
	{
		@Override
		public String getName()
		{
			return "CreateWindow";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x30, 0x03 };
		}
	},
	SHOW_WINDOW
	{
		@Override
		public String getName()
		{
			return "ShowWindow";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x30, 0x07};
		}
	},
	HIDE_WINDOW
	{
		@Override
		public String getName()
		{
			return "HideWindow";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x30, 0x08};
		}

	},
	DESTROY_WINDOW
	{
		@Override
		public String getName()
		{
			return "HideWindow";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x30, 0x04};
		}

	},
	FILE_BROWSE
	{
		@Override
		public String getName()
		{
			return "FileBrowse";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x00, 0x03};
		}
	},
	FILE_DELETE
	{
		@Override
		public String getName()
		{
			return "FileDelete";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x00, 0x04};
		}
	},
	M3M5_ROMfs_PACKAGE_ACTIVATION
	{
		@Override
		public String getName()
		{
			return "M3M5ROMfsPackageActivation";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x04, 0x03};
		}
	},
	VANGUARD_ROMfs_PACKAGE_ACTIVATION
	{
		@Override
		public String getName()
		{
			return "VanguardROMfsPackageActivation";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x60, 0x04, 0x01};
		}
	},
	M3M5_ROMfs_PACKAGE_INFO
	{
		@Override
		public String getName()
		{
			return "M3M5ROMfsPackageInfo";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x03, 0x03};
		}
	},
	DEBIANS_PACKAGE_INFO
	{
		@Override
		public String getName()
		{
			return "DebiansPackageInfo";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x05, 0x03 };
		}
	},
	CLEAR_DEBIANS_REPOSITORY
	{
		@Override
		public String getName()
		{
			return "ClearDebiansRepository";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x06, 0x03 };
		}
	},
	VANGUARD_ROMfs_PACKAGE_INFO
	{
		@Override
		public String getName()
		{
			return "VanguardROMfsPackageInfo";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x60, 0x03 };
		}
	},
	VANGUARD_ROMfs_PROCESS_PACKAGE
	{
		@Override
		public String getName()
		{
			return "VanguardROMfsProcessPackage";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x60, 0x05, 0x01};
		}
	},
	M3M5_ROMfs_FILE_DOWNLOAD
	{
		@Override
		public String getName()
		{
			return "M3M5ROMfsFileDownload";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x01, 0x03};
		}
	},

	FILE_DOWNLOAD_REQUEST
	{
		@Override
		public String getName()
		{
			return "FileDownloadRequest";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x00, 0x01};
		}
	},
	FILE_BLOCK_DOWNLOAD
	{
		@Override
		public String getName()
		{
			return "FileBlockDownload";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x00, 0x02};
		}
	},

	VANGUARD_MAINTENANCE_MODE_LOGIN
	{
		@Override
		public String getName()
		{
			return "VanguardMaintenanceModeLogin";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x0C };
		}
	},
	SYSTEM_STATUS
	{
		@Override
		public String getName()
		{
			return "SystemStatus";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x01 };
		}
	},
	HARDWARE_INFO
	{
		@Override
		public String getName()
		{
			return "HardwareInfo";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x02 };
		}
	},

	SOFTWARE_INFO
	{
		@Override
		public String getName()
		{
			return "SoftwareInfo";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x03 };
		}
	},
	
	DELETE_PACKAGE
	{
		@Override
		public String getName()
		{
			return "DeletePackage";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x60, 0x06, 0x01 };
		}
	},
	
	DIAGNOSTIC
	{
		@Override
		public String getName()
		{
			return "Diagnostic";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x0F };
		}
	},
	RCSH_CERTIFICATE_INIT_CONNECT
	{
		@Override
		public String getName()
		{
			return "RcshCertificateInitConnect";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
 			{ 0x70, (byte) 0x80, 0x01 };
		}
	},
	RCSH_CERTIFICATE_INSTALLATION_STATUS
	{
		@Override
		public String getName()
		{
			return "RcshCertificateInstallationStatus";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
 			{ 0x70, (byte) 0x81, 0x01 };
		}
	},
	RCSH_LIST_OF_INSTALLED_HIERARCHIES
	{
		@Override
		public String getName()
		{
			return "RcshListOfInstalledHierarchies";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
 			{ 0x70, (byte) 0x82, 0x01 };
		}
	},

	RKL_START
	{
		@Override
		public String getName()
		{
			return "RklStart";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
 			{ 0x41, 0x01, 0x01 };
		}
	},
	
	RKL_Host_WL_Download
	{
		@Override
		public String getName()
		{
			return "RklHostWLDownload";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
 			{ 0x41, 0x03, 0x01 };
		}
	},
	

	RKL_STATUS_REQUEST
	{
		@Override
		public String getName()
		{
			return "RklStatusRequest";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x41, 0x02, 0x01 };
		}
	},
	VANGUARD_EXTENDED_DIAGNOSTIC
	{
		@Override
		public String getName()
		{
			return "VanguardExtendedDiagnostic";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, 0x0A };
		}
	},
	AFP_EXTENDED_DIAGNOSTIC_XML  // this type is to be deprecated when AFP adopts non XML reply.
	{
		@Override
		public String getName()
		{
			return "AFPExtendedDiagnosticXML";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x10, (byte) 0xAA, 0x01 };
		}
	},
	OTI_SET
	{
		@Override
		public String getName()
		{
			return "OTISet";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x50, 0x06 };
		}
	},
	OTI_GET
	{
		@Override
		public String getName()
		{
			return "OTIGet";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x50, 0x07 };
		}
	},
	OTI_DO
	{
		@Override
		public String getName()
		{
			return "OTIDo";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x50, 0x08 };
		}
	},
	ENABLE_CL_READER
	{
		@Override
		public String getName()
		{
			return "EnableCardReader";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x50, 0x01 };
		}
	},
	DISABLE_CL_READER
	{
		@Override
		public String getName()
		{
			return "DisableCardReader";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x50, 0x05 };
		}
	},
	CL_DATA
	{
		@Override
		public String getName()
		{
			return "ContactlessData";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x50, 0x04 };
		}
	},
	ENABLE_READER
	{
		@Override
		public String getName()
		{
			return "EnableReader";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x20, 0x01 };
		}
	},
	DISABLE_READER
	{
		@Override
		public String getName()
		{
			return "DisableReader";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x20, 0x02 };
		}
	},
	CHIP_RESET
	{
		@Override
		public String getName()
		{
			return "ChipReset";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x20, 0x07 };
		}
	},
	UX_LOG_UPLOAD
	{
		@Override
		public String getName()
		{
			return "UxLogUpload";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x20, 0x0a };
		}
	},
	FILE_UPLOAD
	{
		@Override
		public String getName()
		{
			return "FileUpload";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x00, 0x05 };
		}
	},
	FILE_BLOCK_UPLOAD
	{
		@Override
		public String getName()
		{
			return "FileBlockUpload";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x00, 0x06 };
		}
	},
	SMC
	{
		@Override
		public String getName()
		{
			return "SMC";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x30, 0x11 };
		}
	},
	DISPLAY_STATUS
	{
		@Override
		public String getName()
		{
			return "DisplayStatus";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x30, 0x0F };
		}
	},
	INPUT_ENABLE
	{
		@Override
		public String getName()
		{
			return "InputEnable";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x40, 0x01 };
		}
	},
	INPUT_ABORT
	{
		@Override
		public String getName()
		{
			return "InputAbort";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x40, 0x02 };
		}
	},
	KEY_STATUS
	{ 
		@Override
		public String getName()
		{
			return "KeyStatus";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x40, 0x06, 0x01 };
		}
	},
	APPLICATION_KEY_DOWNLOAD
	{ 
		@Override
		public String getName()
		{
			return "ApplicationKeyDownload";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x40, 0x04, 0x01 };
		}
	},
	VANGUARD_CERTIFICATE_DOWNLOAD
	{
		@Override
		public String getName()
		{
			return "CertificateDownload";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x70 /*command*/ , 0x01 /*sub command*/ , 0x01 /*Application id*/};
		}
		
	},
	PRINTER_CREATE_JOB
	{
		@Override
		public String getName()
		{
			return "PrinterCreateJob";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x20, 0x04 };
		}
		
	},
	PRINTER_ENQUEUE_DATA
	{
		@Override
		public String getName()
		{
			return "PrinterEnqueueData";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x20, 0x01 };
		}
		
	},
	PRINTER_ENQUEUE_DATA_XML
	{
		@Override
		public String getName()
		{
			return "PrinterEnqueueDataXML";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x20, 0x02 };
		}
		
	},
	PRINTER_EXECUTE_JOB
	{
		@Override
		public String getName()
		{
			return "PrinterExecuteJob";
		}

		@Override
		public byte[] getCommand()
		{
			return new byte[]
			{ 0x20, 0x05 };
		}
		
	};

	/**
	 * VGD_INTERFACE_APPLICATION constant for Vanguard application response id.
	 */
	private static final byte	VGD_INTERFACE_APPLICATION	= (byte) 0x80;

	
	public static SpotMessageType getByName(String name)
	{
		for (SpotMessageType msg : SpotMessageType.values( ))
			if (msg.getName( ).equals( name )) return msg;

		return null;
	}
	
	/**
	 * Gets the vanguard app response id.
	 *
	 * @return the vanguard app response id byte value.
	 */
	public static byte getVanguardAppResponseId()
	{
		return VGD_INTERFACE_APPLICATION;
	}
}

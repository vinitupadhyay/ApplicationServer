package troubleshoot.model.pojo;

public class FileResource 
{
	/**
	 * @brief Resource File Name field
	 */
	private String	fileName;	

	/**
	 * @brief File unique identification number
	 */
	private byte	fileId;	

	/**
	 * @brief File type id
	 */
	private byte	fileType;

	/**
	 * @brief File Authentication byte: 0 No Auth, 1 PIN Auth, 2 Non-PIN Auth
	 */
	private byte	fileAuthentication;

	/**
	 * @brief Resource File Path field
	 */
	private String	filePath;

	/**
	 * Constructor
	 * @param name the name of the resource file
	 * @param type the type id of the file resource 
	 * @param id the file resource id
	 * @param auth the file resource authentication byte
	 * @param path the file path
	 */
	public FileResource(String name, byte type, byte id, byte auth, String path)
	{
		fileName = name;
		fileId = id;
		fileType = type;
		fileAuthentication = auth;
		filePath = path;
	}
	
	public FileResource()
	{
	}

	/**
	 * Returns the file id value
	 * @return the fileId field value
	 */
	public byte getFileId()
	{
		return fileId;
	}

	/**
	 * Returns the file type value
	 * @return the fileTyoe field value
	 */	
	public byte getFileType()
	{
		return fileType;
	}

	/**
	 * Returns the file authentication byte
	 * @return the fileAuthentication field value
	 */	
	public byte getFileAuthentication()
	{
		return fileAuthentication;
	}

	/**
	 * Returns the file name value
	 * @return the fileName field value
	 */	
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * Returns the file path value
	 * @return the filePath field value
	 */	
	public String getFilePath()
	{
		return filePath;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileId(byte fileId) {
		this.fileId = fileId;
	}

	public void setFileType(byte fileType) {
		this.fileType = fileType;
	}

	public void setFileAuthentication(byte fileAuthentication) {
		this.fileAuthentication = fileAuthentication;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}

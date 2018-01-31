/**
 * Copyright © 2013, 2014 Gilbarco Inc.
 * Confidential and Proprietary.
 *
 * @file FileResourceType.java
 * @author mgrieco
 * @date Wednesday, December 18, 2013
 * @copyright Copyright © 2013, 2014 Gilbarco Inc. Confidential and Proprietary.
 */

package troubleshoot.file.managment.api;

/** @class FileResourceType FileResourceType.java "com.gilbarco.globaltools.flexpay.targetunit.frscmgnt.api"
 * @brief This class provides the structure to manage types of resource.
 **/
public class FileResourceType
{
	private byte	typeId;
	private String	typeName;

	/**
	 * @brief Default constructor
	 * @param the id of the resource type
	 * @param the mane of the resource type
	 * @since 1.0.0
	 */
	public FileResourceType(byte id, String name)
	{
		this.typeId = id;
		this.typeName = name;
	}

	/**
	 * @brief get the type id of a resource type
	 * @return the type id value of a resource type
	 * @since 1.0.0
	 */
	public byte getTypeId()
	{
		return typeId;
	}

	/**
	 * @brief get the type name of a resource type
	 * @return the type name value of a resource type
	 * @since 1.0.0
	 */
	public String getTypeName()
	{
		return typeName;
	}
}

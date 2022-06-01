/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.io.Serializable;

/**
 * FormBeanHelper.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>May 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CharacteristicValueBean implements Serializable
{
  private Long joinTableId;
  private Long characteristicId;
  private Long version;
  private Long domainId;
  private long dateCreated;
  private String createdBy;
  private String characteristicName;
  private String characteristicValue;
  private String characteristicDataType;
  private Long maxSize;
  private String maxValue;
  private String minValue;
  private String dateStart;
  private String dateEnd;
  private String plName;
  private String[] characteristicValues;
  private Boolean isRequired;
  private Boolean isUnique;
  private String nameCmKey;
  private String cmAssetCode;
  private String rosterId;

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public CharacteristicValueBean()
  {
    // empty constructor
  }

  public CharacteristicValueBean( Long userCharacteristicId, Long characteristicId, Long version, String characteristicName, String characteristicValue )
  {
    this.joinTableId = userCharacteristicId;
    this.characteristicId = characteristicId;
    this.version = version;
    this.characteristicName = characteristicName;
    this.characteristicValue = characteristicValue;
  }

  public Long getJoinTableId()
  {
    return joinTableId;
  }

  public void setJoinTableId( Long userCharacteristicId )
  {
    this.joinTableId = userCharacteristicId;
  }

  public Long getCharacteristicId()
  {
    return characteristicId;
  }

  public void setCharacteristicId( Long characteristicId )
  {
    this.characteristicId = characteristicId;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCharacteristicName()
  {
    return characteristicName;
  }

  public void setCharacteristicName( String characteristicName )
  {
    this.characteristicName = characteristicName;
  }

  public String getCharacteristicValue()
  {
    return characteristicValue;
  }

  public void setCharacteristicValue( String characteristicValue )
  {
    this.characteristicValue = characteristicValue;
  }

  public Long getMaxSize()
  {
    return maxSize;
  }

  public void setMaxSize( Long maxSize )
  {
    this.maxSize = maxSize;
  }

  public String getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue( String maxValue )
  {
    this.maxValue = maxValue;
  }

  public String getMinValue()
  {
    return minValue;
  }

  public void setMinValue( String minValue )
  {
    this.minValue = minValue;
  }

  public String getCharacteristicDataType()
  {
    return characteristicDataType;
  }

  public void setCharacteristicDataType( String characteristicType )
  {
    this.characteristicDataType = characteristicType;
  }

  public String getDateEnd()
  {
    return dateEnd;
  }

  public void setDateEnd( String dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  public String getDateStart()
  {
    return dateStart;
  }

  public void setDateStart( String dateStart )
  {
    this.dateStart = dateStart;
  }

  public String getPlName()
  {
    return plName;
  }

  public void setPlName( String plName )
  {
    this.plName = plName;
  }

  public String[] getCharacteristicValues()
  {
    return characteristicValues;
  }

  public void setCharacteristicValues( String[] characteristicValues )
  {
    this.characteristicValues = characteristicValues;
  }

  public Boolean getIsRequired()
  {
    return isRequired;
  }

  public void setIsRequired( Boolean isRequired )
  {
    this.isRequired = isRequired;
  }

  public Long getDomainId()
  {
    return domainId;
  }

  public void setDomainId( Long domainId )
  {
    this.domainId = domainId;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public Boolean getIsUnique()
  {
    return isUnique;
  }

  public void setIsUnique( Boolean isUnique )
  {
    this.isUnique = isUnique;
  }

  public String getRosterId()
  {
    return rosterId;
  }

  public void setRosterId( String rosterId )
  {
    this.rosterId = rosterId;
  }
  
  

}

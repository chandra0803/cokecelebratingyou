/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/characteristic/Characteristic.java,v $
 */

package com.biperf.core.domain.characteristic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Characteristic domain object which represents ALL characteristics within the Beacon application.
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
 * <td>Jason</td>
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class Characteristic extends BaseDomain
{

  private static final long serialVersionUID = 3256726164994536240L;

  private CharacteristicDataType characteristicDataType;
  private DynaPickListType dynaPickListType;
  private CharacteristicVisibility visibility;
  private String charId;

  private boolean active;
  private Boolean isRequired;

  private BigDecimal minValue;
  private BigDecimal maxValue;
  private Long maxSize;
  private String description;
  private String characteristicName;
  private String plName;

  private Date dateStart;
  private Date dateEnd;

  private String nameCmKey;
  private String cmAssetCode;

  private UUID rosterCharacteristicId;

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Characteristic [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{description=" + this.getDescription() + "}, " );
    buf.append( "{characteristicName=" + this.getCharacteristicName() + "}, " );
    buf.append( "{characteristicDataType=" + this.getCharacteristicDataType() + "}, " );
    buf.append( "{minValue=" + this.getMinValue() + "}, " );
    buf.append( "{maxValue=" + this.getMaxValue() + "}, " );
    buf.append( "{maxSize=" + this.getMaxSize() + "}" );
    buf.append( "{datStart=" + this.getDateStart() + "}" );
    buf.append( "{dateEnd=" + this.getDateEnd() + "}" );
    buf.append( "{plName=" + this.getPlName() + "}" );
    buf.append( "{nameCmKey=" + this.getNameCmKey() + "}" );
    buf.append( "{cmAssetCode=" + this.getCmAssetCode() + "}" );
    buf.append( "{isRequired=" + this.getIsRequired() + "}" );
    buf.append( "{active=" + this.isActive() + "}" );
    buf.append( "]" );
    return buf.toString();
  }

  public String getCharId()
  {
    return getId().toString();
  }

  public void setCharId( String charId )
  {
    this.charId = getId().toString();
  }

  /**
   * @return value of characteristicName property
   */
  public String getCharacteristicName()
  {
    if ( characteristicName == null )
    {
      return ContentReaderManager.getText( this.cmAssetCode, this.nameCmKey );
    }
    return characteristicName;
  }

  /**
   * @param characteristicName value for characteristicName property
   */
  public void setCharacteristicName( String characteristicName )
  {
    this.characteristicName = characteristicName;
  }

  /**
   * @return value of characteristicDataType property
   */
  public CharacteristicDataType getCharacteristicDataType()
  {
    return characteristicDataType;
  }

  /**
   * @param characteristicDataType value for characteristicDataType property
   */
  public void setCharacteristicDataType( CharacteristicDataType characteristicDataType )
  {
    this.characteristicDataType = characteristicDataType;
  }

  /**
   * @return value of maxValue property
   */
  public BigDecimal getMaxValue()
  {
    return maxValue;
  }

  /**
   * @param maxValue value for maxValue property
   */
  public void setMaxValue( BigDecimal maxValue )
  {
    this.maxValue = maxValue;
  }

  /**
   * @return value of minValue property
   */
  public BigDecimal getMinValue()
  {
    return minValue;
  }

  /**
   * @param minValue value for minValue property
   */
  public void setMinValue( BigDecimal minValue )
  {
    this.minValue = minValue;
  }

  /**
   * @return value of plName property
   */
  public String getPlName()
  {
    return plName;
  }

  /**
   * @param plName value for plName property
   */
  public void setPlName( String plName )
  {
    this.plName = plName;
  }

  /**
   * @return value of isRequired property
   */
  public Boolean getIsRequired()
  {
    return isRequired;
  }

  /**
   * @param isRequired value for isRequired property
   */
  public void setIsRequired( Boolean isRequired )
  {
    this.isRequired = isRequired;
  }

  /**
   * @return value of maxSize property
   */
  public Long getMaxSize()
  {
    return maxSize;
  }

  /**
   * @param maxSize value for maxSize property
   */
  public void setMaxSize( Long maxSize )
  {
    this.maxSize = maxSize;
  }

  /**
   * @return value of dateEnd property
   */
  public Date getDateEnd()
  {
    return dateEnd;
  }

  /**
   * @return formatted string value of dateEnd property
   */
  public String getDisplayDateEnd()
  {
    return DateUtils.toDisplayString( dateEnd );
  }

  /**
   * @param dateEnd value for dateEnd property
   */
  public void setDateEnd( Date dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  /**
   * @return value of dateStart property
   */
  public Date getDateStart()
  {
    return dateStart;
  }

  /**
   * @return formatted string value for the dateStart
   */
  public String getDisplayDateStart()
  {
    return DateUtils.toDisplayString( dateStart );
  }

  /**
   * @param dateStart value for dateStart property
   */
  public void setDateStart( Date dateStart )
  {
    this.dateStart = dateStart;
  }

  /**
   * @return value of nameCmKey property
   */
  public String getNameCmKey()
  {
    return nameCmKey;
  }

  /**
   * @param nameCmKey value for nameCmKey property
   */
  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

  /**
   * @return value of cmAssetCode property
   */
  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  /**
   * @param cmAssetCode value for cmAssetCode property
   */
  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  /**
   * @return value of description property
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description value for description property
   */
  public void setDescription( String description )
  {
    this.description = description;
  }

  /**
   * @return value of dynaPickListType property
   */
  public DynaPickListType getDynaPickListType()
  {
    return dynaPickListType;
  }

  /**
   * @param dynaPickListType value for dynaPickListType property
   */
  public void setDynaPickListType( DynaPickListType dynaPickListType )
  {
    this.dynaPickListType = dynaPickListType;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  /**
   * Return the CM Asset name prefix for this Characteristic type.
   * 
   * @return String
   */
  public abstract String getCmAssetNamePrefix();

  /**
   * Return the CM Asset Type name for this Characteristic type.
   * 
   * @return String
   */
  public abstract String getCmAssetTypeName();

  /**
   * Return the CM Section name for this Characteristic type.
   * 
   * @return String
   */
  public String getCmSectionName()
  {
    return "characteristic_data";
  }

  private static final String CHARACTERISTIC_ASSET_NAME_SUFFIX = " Characteristic";

  /**
   * Get the CM Asset title suffix for Characterisitics.
   * 
   * @return String
   */
  public String getCmAssetTitleSuffix()
  {
    return CHARACTERISTIC_ASSET_NAME_SUFFIX;
  }

  private static final String CHARACTERISTIC_NAME_KEY = "CHARACTERISTIC_NAME";

  /**
   * Get the CM key for the Characteristic name.
   * 
   * @return String
   */
  public String getCmKeyName()
  {
    return CHARACTERISTIC_NAME_KEY;
  }

  public CharacteristicVisibility getVisibility()
  {
    return visibility;
  }

  public void setVisibility( CharacteristicVisibility visibility )
  {
    this.visibility = visibility;
  }

  public UUID getRosterCharacteristicId()
  {
    return rosterCharacteristicId;
  }

  public void setRosterCharacteristicId( UUID rosterCharacteristicId )
  {
    this.rosterCharacteristicId = rosterCharacteristicId;
  }

}

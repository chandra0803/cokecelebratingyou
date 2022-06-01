/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/characteristic/CharacteristicForm.java,v $
 */

package com.biperf.core.ui.characteristic;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;

/**
 * Characteristic ActionForm transfer object.
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
 * <td>Apr 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CharacteristicForm extends BaseForm
{
  public static final String FORM_NAME = "characteristicForm";

  private String method;
  private String charId;
  private Long characteristicId;
  private Long version;
  private String characteristicType = "";
  private String domainId = "";
  private String description = "";
  private String characteristicName = "";
  private String characteristicDataType = "";
  private String visibility = CharacteristicVisibility.VISIBLE;
  private String minValue = "";
  private String maxValue = "";
  private String maxSize = "";
  private String plName = "";
  private String dateStart = "";
  private String dateEnd = "";
  private String nameCmKey = "";
  private String cmAssetCode = "";
  private boolean required;
  private boolean active = true;
  private Boolean unique;
  private String createdBy = "";
  private long dateCreated;

  private String[] deleteValues;

  private String nodeTypeName;

  private String rosterCharacteristicId;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    Date startDate = null;
    Date endDate = null;
    BigDecimal minVal = null;
    BigDecimal maxVal = null;
    Integer minVal2 = null;
    Integer maxVal2 = null;

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    /**
     * Validate required (dynamic) fields depending on the characteristicDataType.
     */
    // To fix bug 20152
    if ( getCharacteristicName().equals( "" ) )
    {
      actionErrors.add( "characteristicName", new ActionMessage( "admin.characteristic.errors.CHARACTERISTIC_NAME" ) );
    }
    if ( getCharacteristicDataType().equals( "txt" ) )
    {
      if ( getMaxSize().equals( "" ) )
      {
        actionErrors.add( "maxSize", new ActionMessage( "admin.characteristic.errors.SIZE" ) );
      }
      try
      {
        new Long( getMaxSize() );
      }
      catch( NumberFormatException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.NONNUMERIC", "Max Size" ) );
      }
    }
    else if ( getCharacteristicDataType().equals( "single_select" ) || getCharacteristicDataType().equals( "multi_select" ) )
    {
      if ( getPlName().equals( "" ) )
      {
        actionErrors.add( "plName", new ActionMessage( "admin.characteristic.errors.PL_NAME" ) );
      }
    }
    else if ( getCharacteristicDataType().equals( "date" ) )
    {
      try
      {
        if ( getDateStart() != null && getDateStart().length() > 0 )
        {
          startDate = DateUtils.toDateChecked( getDateStart() );
        }
      }
      catch( ParseException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.DATE", "Start Date" ) );
      }
      try
      {
        if ( getDateEnd() != null && getDateEnd().length() > 0 )
        {
          endDate = DateUtils.toDateChecked( getDateEnd() );
        }
      }
      catch( ParseException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.DATE", "End Date" ) );
      }
      try
      {
        if ( startDate != null && endDate != null && startDate.getTime() - endDate.getTime() >= 0 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.DATE_RANGE", "Start Date" ) );
        }
      }
      catch( Exception e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.DATE", "Start/End Date" ) );
      }
    }
    else if ( getCharacteristicDataType().equals( "decimal" ) )
    {
      try
      {
        if ( getMinValue() != null && getMinValue().length() > 0 )
        {
          minVal = new BigDecimal( getMinValue() );
        }
      }
      catch( NumberFormatException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", "Min Value" ) );
      }
      try
      {
        if ( getMaxValue() != null && getMaxValue().length() > 0 )
        {
          maxVal = new BigDecimal( getMaxValue() );
        }
      }
      catch( NumberFormatException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", "Max Value" ) );
      }
      if ( minVal != null && maxVal != null && minVal.doubleValue() - maxVal.doubleValue() >= 0.0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", "Min/Max Value" ) );
      }
    }
    else if ( getCharacteristicDataType().equals( "int" ) )
    {
      try
      {
        if ( getMinValue() != null && getMinValue().length() > 0 )
        {
          minVal2 = new Integer( getMinValue() );
        }

      }
      catch( NumberFormatException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INTEGER", "Min Value" ) );
      }
      try
      {
        if ( getMaxValue() != null && getMaxValue().length() > 0 )
        {
          maxVal2 = new Integer( getMaxValue() );
        }
      }
      catch( NumberFormatException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INTEGER", "Max Value" ) );
      }
      if ( minVal2 != null && maxVal2 != null && minVal2.intValue() - maxVal2.intValue() >= 0 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.INVALID", "Min/Max Value" ) );
      }
    }

    return actionErrors;
  }

  /**
   * @param userCharacteristicType
   */
  public void loadUserCharacteristicType( UserCharacteristicType userCharacteristicType )
  {
    load( userCharacteristicType );
  }

  /**
   * @param nodeTypeCharacteristicType
   */
  public void loadNodeTypeCharacteristicType( NodeTypeCharacteristicType nodeTypeCharacteristicType )
  {
    load( nodeTypeCharacteristicType );

    if ( nodeTypeCharacteristicType.getDomainId() != null )
    {
      this.domainId = String.valueOf( nodeTypeCharacteristicType.getDomainId() );
    }
  }

  /**
   * @param productCharacteristicType
   */
  public void loadProductCharacteristicType( ProductCharacteristicType productCharacteristicType )
  {
    load( productCharacteristicType );

    if ( productCharacteristicType.getIsUnique() != null )
    {
      this.unique = productCharacteristicType.getIsUnique();
    }
  }

  /**
   * Load Characteristic
   * 
   * @param characteristic
   */
  private void load( Characteristic characteristic )
  {
    this.characteristicId = characteristic.getId();
    this.version = characteristic.getVersion();
    this.description = characteristic.getDescription();
    if ( characteristic.getId() != null )
    {
      this.characteristicName = characteristic.getCharacteristicName();
    }
    this.characteristicDataType = characteristic.getCharacteristicDataType().getCode();
    this.minValue = characteristic.getMinValue() != null ? characteristic.getMinValue().toString() : "";
    this.maxValue = characteristic.getMaxValue() != null ? characteristic.getMaxValue().toString() : "";
    this.maxSize = characteristic.getMaxSize() != null ? characteristic.getMaxSize().toString() : "";
    this.plName = characteristic.getPlName();
    this.dateStart = characteristic.getDateStart() != null ? DateUtils.toDisplayString( characteristic.getDateStart() ) : DateUtils.displayDateFormatMask;
    this.dateEnd = characteristic.getDateEnd() != null ? DateUtils.toDisplayString( characteristic.getDateEnd() ) : DateUtils.displayDateFormatMask;
    this.nameCmKey = characteristic.getNameCmKey();
    this.cmAssetCode = characteristic.getCmAssetCode();
    this.required = characteristic.getIsRequired().booleanValue();
    this.active = characteristic.isActive();
    this.createdBy = characteristic.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = characteristic.getAuditCreateInfo().getDateCreated().getTime();
    this.visibility = characteristic.getVisibility().getCode();

    if ( characteristic.getId() != null )
    {
      this.rosterCharacteristicId = characteristic.getRosterCharacteristicId().toString();
    }
  }

  public UserCharacteristicType toDomainObjectUserCharacteristicType()
  {
    UserCharacteristicType userCharacteristicType = new UserCharacteristicType();
    setDomainData( userCharacteristicType );

    return userCharacteristicType;
  }

  public ProductCharacteristicType toDomainObjectProductCharacteristicType()
  {
    ProductCharacteristicType characteristicType = new ProductCharacteristicType();
    setDomainData( characteristicType );
    characteristicType.setIsUnique( this.unique );

    return characteristicType;
  }

  public NodeTypeCharacteristicType toDomainObjectNodeTypeCharacteristicType()
  {
    NodeTypeCharacteristicType nodeTypeCharacteristicType = new NodeTypeCharacteristicType();
    setDomainData( nodeTypeCharacteristicType );

    if ( domainId != null && !domainId.equals( "" ) )
    {
      nodeTypeCharacteristicType.setDomainId( new Long( this.domainId ) );
    }

    return nodeTypeCharacteristicType;
  }

  /**
   * Sets the characteristic in the Domain Object
   */
  private void setDomainData( Characteristic characteristic )
  {
    if ( this.characteristicId == null || this.characteristicId.longValue() == 0 )
    {
      characteristic.setId( null );
    }
    else
    {
      characteristic.setId( this.characteristicId );
    }

    if ( this.version == null )
    {
      characteristic.setVersion( null );
    }
    else
    {
      characteristic.setVersion( this.version );
    }
    characteristic.setDescription( this.description );
    characteristic.setCharacteristicName( this.characteristicName );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( this.characteristicDataType ) );
    characteristic.setVisibility( CharacteristicVisibility.lookup( this.visibility ) );
    if ( !this.minValue.equals( "" ) )
    {
      characteristic.setMinValue( new BigDecimal( this.minValue ) );
    }
    if ( !this.maxValue.equals( "" ) )
    {
      characteristic.setMaxValue( new BigDecimal( this.maxValue ) );
    }
    if ( !this.maxSize.equals( "" ) )
    {
      characteristic.setMaxSize( new Long( this.maxSize ) );
    }
    if ( !this.plName.equals( "" ) )
    {
      characteristic.setPlName( this.plName );
    }
    if ( !this.dateStart.equals( "" ) )
    {
      characteristic.setDateStart( DateUtils.toDate( this.dateStart ) );
    }
    if ( !this.dateEnd.equals( "" ) )
    {
      characteristic.setDateEnd( DateUtils.toDate( this.dateEnd ) );
    }
    characteristic.setNameCmKey( this.nameCmKey );
    characteristic.setCmAssetCode( this.cmAssetCode );
    characteristic.setIsRequired( Boolean.valueOf( this.required ) );
    characteristic.setActive( this.active );

    if ( this.createdBy != null && !this.createdBy.equals( "" ) )
    {
      characteristic.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }
    if ( this.dateCreated != 0 )
    {
      characteristic.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    }

    if ( this.rosterCharacteristicId != null && !StringUtils.isEmpty( this.rosterCharacteristicId ) )
    {
      characteristic.setRosterCharacteristicId( UUID.fromString( this.rosterCharacteristicId ) );
    }
  }

  /**
   * @return value of characteristicType property
   */
  public String getCharacteristicType()
  {
    return characteristicType;
  }

  /**
   * @param characteristicType value for characteristicType property
   */
  public void setCharacteristicType( String characteristicType )
  {
    this.characteristicType = characteristicType;
  }

  /**
   * @return value of characteristicId property
   */
  public Long getCharacteristicId()
  {
    return characteristicId;
  }

  /**
   * @param characteristicId value for characteristicId property
   */
  public void setCharacteristicId( Long characteristicId )
  {
    this.characteristicId = characteristicId;
  }

  /**
   * @return value of characteristicName property
   */
  public String getCharacteristicName()
  {
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
  public String getCharacteristicDataType()
  {
    return characteristicDataType;
  }

  /**
   * @param characteristicDataType value for characteristicDataType property
   */
  public void setCharacteristicDataType( String characteristicDataType )
  {
    this.characteristicDataType = characteristicDataType;
  }

  public String getCharacteristicTypeDesc()
  {
    return CharacteristicDataType.lookup( this.characteristicDataType ).getName();
  }

  /**
   * @return value of deleteValues property
   */
  public String[] getDeleteValues()
  {
    return deleteValues;
  }

  /**
   * @param deleteValues value for deleteValues property
   */
  public void setDeleteValues( String[] deleteValues )
  {
    this.deleteValues = deleteValues;
  }

  /**
   * @return value of maxSize property
   */
  public String getMaxSize()
  {
    return maxSize;
  }

  /**
   * @param maxSize value for maxSize property
   */
  public void setMaxSize( String maxSize )
  {
    this.maxSize = maxSize;
  }

  /**
   * @return value of maxValue property
   */
  public String getMaxValue()
  {
    return maxValue;
  }

  /**
   * @param maxValue value for maxValue property
   */
  public void setMaxValue( String maxValue )
  {
    this.maxValue = maxValue;
  }

  /**
   * @return value of minValue property
   */
  public String getMinValue()
  {
    return minValue;
  }

  /**
   * @param minValue value for minValue property
   */
  public void setMinValue( String minValue )
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
   * @return value of required property
   */
  public boolean getRequired()
  {
    return required;
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
   * @param required value for required property
   */
  public void setRequired( boolean required )
  {
    this.required = required;
  }

  /**
   * @return value of dateEnd property
   */
  public String getDateEnd()
  {
    return dateEnd;
  }

  /**
   * @param dateEnd value for dateEnd property
   */
  public void setDateEnd( String dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  /**
   * @return value of dateStart property
   */
  public String getDateStart()
  {
    return dateStart;
  }

  /**
   * @param dateStart value for dateStart property
   */
  public void setDateStart( String dateStart )
  {
    this.dateStart = dateStart;
  }

  /**
   * @return value of version property
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * @param version value for version property
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @return value of createdBy property
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * @param createdBy value for createdBy property
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  /**
   * @param dateCreated value for dateCreated property
   */
  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * @return value of method property
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method value for method property
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getDomainId()
  {
    return domainId;
  }

  public void setDomainId( String domainId )
  {
    this.domainId = domainId;
  }

  public String getNodeTypeName()
  {
    return nodeTypeName;
  }

  public void setNodeTypeName( String nodeTypeName )
  {
    this.nodeTypeName = nodeTypeName;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getCharId()
  {
    return charId;
  }

  public void setCharId( String charId )
  {
    this.charId = charId;
  }

  /**
   * @return value of unique property
   */
  public Boolean isUnique()
  {
    return unique;
  }

  /**
   * @param unique value for unique property
   */
  public void setUnique( Boolean unique )
  {
    this.unique = unique;
  }

  /**
   * @return value of unique property
   */
  public Boolean getUnique()
  {
    return unique;
  }

  public String getVisibility()
  {
    return visibility;
  }

  public void setVisibility( String visibility )
  {
    this.visibility = visibility;
  }

  public String getRosterCharacteristicId()
  {
    return rosterCharacteristicId;
  }

  public void setRosterCharacteristicId( String rosterCharacteristicId )
  {
    this.rosterCharacteristicId = rosterCharacteristicId;
  }

}

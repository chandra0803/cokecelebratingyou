/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/ClaimElementForm.java,v $
 *
 */

package com.biperf.core.ui.claim;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.CustomerInformationBlock;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.user.AddressFormBean;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.util.StringUtils;

/**
 * ClaimElementForm <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ClaimElementForm extends BaseForm
{
  public static final String ADRESS_DELIMITER = "|";
  private ClaimFormStepElement claimFormStepElement;
  private String value;
  private String[] valueArray;
  private Long claimFormStepElementId;
  private Long claimElementId;
  private Long claimElementVersion;
  private long claimElementDateCreated;
  private String claimElementCreatedBy;
  private List pickList;// todo set this on update and view
  private List pickListItems = new ArrayList();

  private String claimFormAssetCode;
  private Long claimFormId;

  private AddressFormBean mainAddressFormBean = new AddressFormBean();
  private List countryList = null;
  private List stateList = null;

  /**
   * Load this form with the ClaimElement data.
   * 
   * @param claimElement
   */
  public void load( ClaimElement claimElement )
  {
    this.claimElementId = claimElement.getId();
    this.value = claimElement.getValue();
    this.claimFormStepElement = claimElement.getClaimFormStepElement();
    if ( claimFormStepElement.getClaimFormElementType().isDateField() && ( value == null || value.equals( "" ) ) )
    {
      this.value = DateUtils.displayDateFormatMask;
    }
    this.claimFormStepElementId = claimElement.getClaimFormStepElement().getId();

    this.claimElementVersion = claimElement.getVersion();
    if ( claimElement.getAuditCreateInfo().getCreatedBy() != null )
    {
      this.claimElementCreatedBy = claimElement.getAuditCreateInfo().getCreatedBy().toString();
    }
    if ( claimElement.getAuditCreateInfo().getDateCreated() != null )
    {
      this.claimElementDateCreated = claimElement.getAuditCreateInfo().getDateCreated().getTime();
    }

    if ( CustomerInformationBlock.MAIN_ADDRESS_1_CFSE_ID.equals( this.claimFormStepElement.getCustomerInformationBlockId() )
        || CustomerInformationBlock.ADDITIONAL_ADDRESS_2_CFSE_ID.equals( this.claimFormStepElement.getCustomerInformationBlockId() ) )
    {
      this.countryList = getCountryService().getAllActive();
      // get the list of all states
      this.stateList = StateType.getList( null );

      if ( StringUtils.isEmpty( this.mainAddressFormBean.getCountryCode() ) )
      {
        this.mainAddressFormBean.setCountryCode( Country.UNITED_STATES );
      }
      // this.value = "8590 Magnolia Trl|apt.206||edenprairie|mn|55344|";
      if ( !StringUtils.isEmpty( this.value ) )
      {
        String addressValue = this.value;
        this.value = addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) );
        this.mainAddressFormBean.setCountryCode( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
        addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
        this.mainAddressFormBean.setAddr1( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
        addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
        this.mainAddressFormBean.setAddr2( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
        addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
        this.mainAddressFormBean.setAddr3( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
        addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );

        this.mainAddressFormBean.setCity( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
        addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
        this.mainAddressFormBean.setStateTypeCode( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );
        addressValue = addressValue.substring( addressValue.indexOf( ADRESS_DELIMITER ) + 1, addressValue.length() );
        this.mainAddressFormBean.setPostalCode( addressValue.substring( 0, addressValue.indexOf( ADRESS_DELIMITER ) ) );

      }
    }
  }

  /**
   * Create a detached ClaimElement from this form.
   * 
   * @return ClaimElement
   */
  public ClaimElement toDomainObject()
  {
    ClaimElement claimElement = new ClaimElement();
    if ( claimElementId != null && claimElementId.longValue() != 0 )
    {
      claimElement.setId( claimElementId );
      claimElement.setVersion( claimElementVersion );
      claimElement.getAuditCreateInfo().setCreatedBy( Long.valueOf( claimElementCreatedBy ) );
      claimElement.getAuditCreateInfo().setDateCreated( new Timestamp( claimElementDateCreated ) );
    }

    claimElement.setClaimFormStepElement( claimFormStepElement );

    if ( claimFormStepElement.getClaimFormElementType().isMultiSelectField() )
    {
      claimElement.setValue( ArrayUtil.convertStringArrayToDelimited( valueArray, "," ) );
    }
    else if ( claimFormStepElement.getClaimFormElementType().isSelectField() )
    {
      claimElement.setValue( value );
    }
    if ( claimFormStepElement.getClaimFormElementType().isMultiSelectField() || claimFormStepElement.getClaimFormElementType().isSelectField() )
    {
      // convert the comma delimited list of selected pickListItems to a list of strings
      Iterator pickListCodes = ArrayUtil.convertDelimitedStringToList( claimElement.getValue(), "," ).iterator();
      this.pickListItems.clear(); // fix for bug 52348
      while ( pickListCodes.hasNext() )
      {
        String code = (String)pickListCodes.next();
        this.pickListItems.add( DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), code ) );
      }
      claimElement.setPickListItems( this.pickListItems );
    }
    else
    {
      String fullValue = value;
      if ( fullValue == null )
      {
        if ( CustomerInformationBlock.MAIN_ADDRESS_1_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() )
            || CustomerInformationBlock.ADDITIONAL_ADDRESS_2_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() ) )
        {
          fullValue = ( this.getMainAddressFormBean().getCountryCode() == null ? "" : this.getMainAddressFormBean().getCountryName() ) + ADRESS_DELIMITER
              + ( this.getMainAddressFormBean().getAddr1() == null ? "" : this.getMainAddressFormBean().getAddr1() ) + ADRESS_DELIMITER
              + ( this.getMainAddressFormBean().getAddr2() == null ? "" : this.getMainAddressFormBean().getAddr2() ) + ADRESS_DELIMITER
              + ( this.getMainAddressFormBean().getAddr3() == null ? "" : this.getMainAddressFormBean().getAddr3() ) + ADRESS_DELIMITER + this.getMainAddressFormBean().getCity() + ADRESS_DELIMITER
              + this.getMainAddressFormBean().getStateTypeDesc() + ADRESS_DELIMITER + this.getMainAddressFormBean().getPostalCode() + ADRESS_DELIMITER;
        }
      }

      claimElement.setValue( fullValue );
    }

    claimElement.setClaimFormAssetCode( this.getClaimFormAssetCode() );
    return claimElement;
  }

  public ClaimFormStepElement getClaimFormStepElement()
  {
    return claimFormStepElement;
  }

  public void setClaimFormStepElement( ClaimFormStepElement claimFormStepElement )
  {
    this.claimFormStepElement = claimFormStepElement;
  }

  public List getPickListItems()
  {
    return pickListItems;
  }

  public void setPickListItems( List pickListItems )
  {
    this.pickListItems = pickListItems;
  }

  public String getValue()
  {
    String fullValue = value;
    // added country code incase we need that for future
    if ( claimFormStepElement != null && ( CustomerInformationBlock.MAIN_ADDRESS_1_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() )
        || CustomerInformationBlock.ADDITIONAL_ADDRESS_2_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() ) ) )
    {
      fullValue = this.getMainAddressFormBean().getCountryName() + ADRESS_DELIMITER + this.getMainAddressFormBean().getAddr1() + ADRESS_DELIMITER + this.getMainAddressFormBean().getAddr2()
          + ADRESS_DELIMITER + this.getMainAddressFormBean().getAddr3() + ADRESS_DELIMITER + this.getMainAddressFormBean().getCity() + ADRESS_DELIMITER
          + this.getMainAddressFormBean().getStateTypeDesc() + ADRESS_DELIMITER + this.getMainAddressFormBean().getPostalCode() + ADRESS_DELIMITER;
    }
    // end
    return StringEscapeUtils.escapeHtml4( fullValue );
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public void setValueArray( String[] values )
  {
    valueArray = values;
  }

  public String[] getValueArray()
  {
    return valueArray;
  }

  public Long getClaimElementId()
  {
    return claimElementId;
  }

  public void setClaimElementId( Long claimElementId )
  {
    this.claimElementId = claimElementId;
  }

  public Long getClaimElementVersion()
  {
    return claimElementVersion;
  }

  public void setClaimElementVersion( Long claimElementVersion )
  {
    this.claimElementVersion = claimElementVersion;
  }

  public List getPickList()
  {
    return pickList;
  }

  public void setPickList( List pickList )
  {
    this.pickList = pickList;
  }

  public void setClaimFormAssetCode( String claimFormAssetCode )
  {
    this.claimFormAssetCode = claimFormAssetCode;
  }

  public String getClaimFormAssetCode()
  {
    return claimFormAssetCode;
  }

  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public Long getClaimFormId()
  {
    return claimFormId;
  }

  public Long getClaimFormStepElementId()
  {
    return claimFormStepElementId;
  }

  public void setClaimFormStepElementId( Long claimFormStepElementId )
  {
    this.claimFormStepElementId = claimFormStepElementId;
  }

  public String getClaimElementCreatedBy()
  {
    return claimElementCreatedBy;
  }

  public void setClaimElementCreatedBy( String claimElementCreatedBy )
  {
    this.claimElementCreatedBy = claimElementCreatedBy;
  }

  public long getClaimElementDateCreated()
  {
    return claimElementDateCreated;
  }

  public void setClaimElementDateCreated( long claimElementDateCreated )
  {
    this.claimElementDateCreated = claimElementDateCreated;
  }

  public AddressFormBean getMainAddressFormBean()
  {
    return mainAddressFormBean;
  }

  public void setMainAddressFormBean( AddressFormBean mainAddressFormBean )
  {
    this.mainAddressFormBean = mainAddressFormBean;
  }

  /**
   * Returns a reference to the Country service.
   * 
   * @return a reference to the Country service.
   */
  private CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

  public List getCountryList()
  {
    return countryList;
  }

  public void setCountryList( List countryList )
  {
    this.countryList = countryList;
  }

  public List getStateList()
  {
    return stateList;
  }

  public void setStateList( List stateList )
  {
    this.stateList = stateList;
  }
}

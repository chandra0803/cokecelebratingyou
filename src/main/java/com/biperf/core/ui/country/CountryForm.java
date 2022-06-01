/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/country/CountryForm.java,v $
 */

package com.biperf.core.ui.country;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.domain.enums.AddressMethodType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.CountrySupplierValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Supplier ActionForm transfer object.
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
 * <td>June 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CountryForm extends BaseForm
{
  private Long countryId;
  private Long version;
  private Long supplierId;
  private String countryCode;
  private String phoneCountryCode;
  private String countryName;
  private String cmAssetCode;
  private String nameCmKey;
  private String awardbanqAbbrev;
  private String addressMethod;
  private String campaignNbr;
  private String campaignCode;
  private String campaignPassword;
  private String programId;
  private String programPassword;
  private String status = "inactive";
  private String shoppingBannerUrl;
  private Boolean allowSms = Boolean.FALSE;
  private boolean smsCapable = Boolean.FALSE;
  private String supportEmailAddr;
  private String method;
  private String createdBy = "";
  private long dateCreated;
  private String timeZoneId;
  private Double budgetMediaValue;
  private Boolean displayTravelAward = Boolean.FALSE;
  private Boolean displayExperiences = Boolean.FALSE;

  private List countrySuppliersList = new ArrayList();
  private int countrySuppliersListCount;
  private boolean addCountry = false;
  private boolean hasErrors = false;
  private boolean requirePostalCode;
  private Long currencyId;

  /**
   * Load Country Item
   * 
   * @param country
   */
  public void load( Country country )
  {
    // todo jjd add flag to disable editing for non-default locales
    this.countryId = country.getId();
    this.version = country.getVersion();
    // this.supplierId = country.getSupplier().getId();
    this.countryCode = country.getCountryCode();
    this.phoneCountryCode = country.getPhoneCountryCode();
    if ( country.getId() != null )
    {
      this.countryName = ContentReaderManager.getText( country.getCmAssetCode(), country.getNameCmKey() );
    }
    this.cmAssetCode = country.getCmAssetCode();
    this.nameCmKey = country.getNameCmKey();
    this.awardbanqAbbrev = country.getAwardbanqAbbrev();
    this.addressMethod = country.getAddressMethod().getCode();
    this.campaignNbr = country.getCampaignNbr();
    this.campaignCode = country.getCampaignCode();
    this.campaignPassword = country.getCampaignPassword();
    this.programId = country.getProgramId();
    this.programPassword = country.getProgramPassword();
    this.status = country.getStatus().getCode();
    this.shoppingBannerUrl = country.getShoppingBannerUrl();
    this.createdBy = country.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = country.getAuditCreateInfo().getDateCreated().getTime();
    this.allowSms = country.getAllowSms();
    this.smsCapable = country.isSmsCapable();
    this.supportEmailAddr = country.getSupportEmailAddr();
    this.timeZoneId = country.getTimeZoneId().getCode();
    this.displayTravelAward = country.getDisplayTravelAward();
    this.displayExperiences = country.isDisplayExperiences();
    this.requirePostalCode = country.getRequirePostalCode();

    if ( country.getCountrySuppliers() != null && country.getCountrySuppliers().size() > 0 )
    {
      for ( Iterator iter = country.getCountrySuppliers().iterator(); iter.hasNext(); )
      {
        CountrySupplier supplier = (CountrySupplier)iter.next();
        CountrySupplierValueBean valueBean = new CountrySupplierValueBean();
        valueBean.setIsPrimary( supplier.getPrimary() );
        valueBean.setSelectedSupplierId( supplier.getSupplier().getId().toString() );
        valueBean.setSupplierId( supplier.getSupplier().getId().toString() );
        valueBean.setSupplierName( supplier.getSupplier().getSupplierName() );
        this.countrySuppliersList.add( valueBean );
      }
    }

  }

  /**
   * Sets the country in the Domain Object
   * 
   * @return country
   */
  public Country toDomainObject()
  {
    Country country = new Country();
    country.setId( this.countryId );
    country.setVersion( this.version );
    country.setCountryCode( this.countryCode );
    country.setPhoneCountryCode( this.phoneCountryCode );
    country.setCountryName( this.countryName );
    country.setCmAssetCode( this.cmAssetCode );
    country.setNameCmKey( this.nameCmKey );
    country.setAwardbanqAbbrev( this.awardbanqAbbrev );
    country.setAddressMethod( AddressMethodType.lookup( this.addressMethod ) );
    country.setCampaignNbr( this.campaignNbr );
    country.setCampaignCode( this.campaignCode );
    country.setCampaignPassword( this.campaignPassword );
    country.setProgramId( this.programId );
    country.setProgramPassword( this.programPassword );
    country.setStatus( CountryStatusType.lookup( this.status ) );
    country.setShoppingBannerUrl( this.shoppingBannerUrl );
    country.setAllowSms( this.allowSms );
    country.setSupportEmailAddr( this.supportEmailAddr );
    country.setTimeZoneId( TimeZoneId.lookup( this.timeZoneId ) );
    country.setBudgetMediaValue( budgetMediaValue == null ? null : BigDecimal.valueOf( budgetMediaValue ) );
    country.setDisplayTravelAward( this.displayTravelAward );
    country.setDisplayExperiences( this.displayExperiences );
    country.setRequirePostalCode( this.requirePostalCode );
    country.setSmsCapable( this.smsCapable );

    if ( this.createdBy != null && this.createdBy.length() > 0 )
    {
      // We are editing, not creating, a Country object.
      country.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }

    if ( this.dateCreated != 0 )
    {
      // We are editing, not creating, a Country object.
      country.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    }

    Iterator supplierIter = countrySuppliersList.iterator();
    while ( supplierIter.hasNext() )
    {
      CountrySupplierValueBean valueBean = (CountrySupplierValueBean)supplierIter.next();
      if ( valueBean.getSelectedSupplierId() != null && !valueBean.getSelectedSupplierId().equals( "" ) )
      {
        CountrySupplier countrySupplier = new CountrySupplier();
        if ( valueBean.getIsPrimary() != null )
        {
          countrySupplier.setPrimary( Boolean.valueOf( true ) );
        }
        else
        {
          countrySupplier.setPrimary( Boolean.valueOf( false ) );
        }

        Long supplierId = new Long( valueBean.getSelectedSupplierId() );
        Supplier supplier = getSupplierService().getSupplierById( supplierId );
        countrySupplier.setSupplier( supplier );
        country.addCountrySuppliers( countrySupplier );
      }
    }

    return country;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param mapping the mapping used to select this instance.
   * @param request the servlet request we are processing.
   * @return <code>ActionErrors</code> object that encapsulates any validation errors.
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( this.getAwardBanqIsUsed() && this.status.equals( "active" ) )
    {
      if ( this.getCampaignNbr() == null || this.getCampaignNbr().equals( "" ) )
      {

        actionErrors.add( "campaignNbr", new ActionMessage( ServiceErrorMessageKeys.CAMPAIGN_NUMBER_REQUIRED ) );
      }
      if ( this.getCampaignNbr() != null && this.getCampaignNbr().length() > 6 )
      {

        actionErrors
            .add( "campaignNbr",
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_MAXLENGTH, CmsResourceBundle.getCmsBundle().getString( "admin.country.details", "CAMPAIGN" ), "6" ) );
      }

      if ( this.getCampaignPassword() == null || this.getCampaignPassword().equals( "" ) )
      {

        actionErrors.add( "campaignPassword",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.country.details", "CAMPAIGN_PASSWORD" ) ) );
      }
    }

    // cannot allow SMS if the country is not smsCapable
    if ( allowSms && !smsCapable )
    {
      actionErrors.add( "allowSms", new ActionMessage( ServiceErrorMessageKeys.NOT_SMS_CAPABLE ) );
    }

    // Bug fix 39925 starts
    if ( countrySuppliersList != null && countrySuppliersList.size() > 0 )
    {
      boolean hasSupplier = false;
      boolean hasPrimary = false;
      for ( Iterator iter = countrySuppliersList.iterator(); iter.hasNext(); )
      {
        CountrySupplierValueBean bean = (CountrySupplierValueBean)iter.next();
        if ( bean.getSelectedSupplierId() != null && !bean.getSelectedSupplierId().equals( "" ) )
        {
          hasSupplier = true;
        }
        if ( bean.getIsPrimary() != null && bean.getSelectedSupplierId() != null )
        {
          hasPrimary = true;
        }
      }
      if ( !hasPrimary )
      {
        actionErrors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.country.errors.PRIMARY_SUPPLIER" ) );

      }
      if ( !hasSupplier )
      {
        actionErrors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.country.errors.SUPPLIER_OPTION" ) );
      }
    }
    else
    {
      actionErrors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.country.errors.SUPPLIER_REQUIRED" ) );
    }

    if ( actionErrors.size() > 0 )
    {
      hasErrors = true;
    }

    return actionErrors;
  }

  // Bug fix 39925 ends
  /**
   * Returns the client launch date, which is the date on which this client's version of this web
   * application was launched.
   * 
   * @return the client launch date.
   */
  private boolean getAwardBanqIsUsed()
  {
    boolean isAwardBanqUsed = false;

    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );

    String awardbanqMode = systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_MODE ).getStringVal();

    if ( awardbanqMode.equals( AwardBanQServiceFactory.AWARDBANQ ) )
    {
      isAwardBanqUsed = true;
    }

    return isAwardBanqUsed;
  }

  public String getAddressMethod()
  {
    return addressMethod;
  }

  public void setAddressMethod( String addressMethod )
  {
    this.addressMethod = addressMethod;
  }

  public String getAwardbanqAbbrev()
  {
    return awardbanqAbbrev;
  }

  public void setAwardbanqAbbrev( String awardbanqAbbrev )
  {
    this.awardbanqAbbrev = awardbanqAbbrev;
  }

  public String getCampaignNbr()
  {
    return campaignNbr;
  }

  public void setCampaignNbr( String campaignNbr )
  {
    this.campaignNbr = campaignNbr;
  }

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

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
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

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public String getProgramPassword()
  {
    return programPassword;
  }

  public void setProgramPassword( String programPassword )
  {
    this.programPassword = programPassword;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public Long getSupplierId()
  {
    return supplierId;
  }

  public void setSupplierId( Long supplierId )
  {
    this.supplierId = supplierId;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCampaignPassword()
  {
    return campaignPassword;
  }

  public void setCampaignPassword( String campaignPassword )
  {
    this.campaignPassword = campaignPassword;
  }

  public String getShoppingBannerUrl()
  {
    return shoppingBannerUrl;
  }

  public void setShoppingBannerUrl( String shoppingBannerUrl )
  {
    this.shoppingBannerUrl = shoppingBannerUrl;
  }

  public Boolean getAllowSms()
  {
    return allowSms;
  }

  public void setAllowSms( Boolean allowSms )
  {
    this.allowSms = allowSms;
  }

  public boolean isSmsCapable()
  {
    return smsCapable;
  }

  public void setSmsCapable( boolean smsCapable )
  {
    this.smsCapable = smsCapable;
  }

  public String getSupportEmailAddr()
  {
    return supportEmailAddr;
  }

  public void setSupportEmailAddr( String supportEmailAddr )
  {
    this.supportEmailAddr = supportEmailAddr;
  }

  public String getCampaignCode()
  {
    return campaignCode;
  }

  public void setCampaignCode( String campaignCode )
  {
    this.campaignCode = campaignCode;
  }

  public void setPhoneCountryCode( String phoneCountryCode )
  {
    this.phoneCountryCode = phoneCountryCode;
  }

  public String getPhoneCountryCode()
  {
    return phoneCountryCode;
  }

  public String getTimeZoneId()
  {
    return timeZoneId;
  }

  public void setTimeZoneId( String timeZoneId )
  {
    this.timeZoneId = timeZoneId;
  }

  public void setBudgetMediaValue( Double budgetMediaValue )
  {
    this.budgetMediaValue = budgetMediaValue;
  }

  public Double getBudgetMediaValue()
  {
    return budgetMediaValue;
  }

  public void setDisplayTravelAward( Boolean displayTravelAward )
  {
    this.displayTravelAward = displayTravelAward;
  }

  public Boolean getDisplayTravelAward()
  {
    return displayTravelAward;
  }

  public void setCountrySuppliersList( List countrySuppliersList )
  {
    this.countrySuppliersList = countrySuppliersList;
  }

  public Boolean getDisplayExperiences()
  {
    return displayExperiences;
  }

  public void setDisplayExperiences( Boolean displayExperiences )
  {
    this.displayExperiences = displayExperiences;
  }

  public CountrySupplierValueBean getCountrySuppliersList( int index )
  {
    try
    {
      return (CountrySupplierValueBean)countrySuppliersList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // CountrySupliersValueBean. If this is not done, the form wont initialize
    // properly.
    countrySuppliersList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "countrySuppliersListCount" ) );
  }

  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty CountrySupplierValueBean
      CountrySupplierValueBean giverBean = new CountrySupplierValueBean();
      valueList.add( giverBean );
    }

    return valueList;
  }

  public void setCountrySuppliersListCount( int countrySuppliersListCount )
  {
    this.countrySuppliersListCount = countrySuppliersListCount;
  }

  public int getCountrySuppliersListCount()
  {
    return countrySuppliersList.size();
  }

  public List getCountrySuppliersList()
  {
    return countrySuppliersList;
  }

  private SupplierService getSupplierService()
  {
    return (SupplierService)BeanLocator.getBean( SupplierService.BEAN_NAME );
  }

  public void setAddCountry( boolean addCountry )
  {
    this.addCountry = addCountry;
  }

  public boolean isAddCountry()
  {
    return addCountry;
  }

  public void setHasErrors( boolean hasErrors )
  {
    this.hasErrors = hasErrors;
  }

  public boolean isHasErrors()
  {
    return hasErrors;
  }

  public boolean isRequirePostalCode()
  {
    return requirePostalCode;
  }

  public void setRequirePostalCode( boolean requirePostalCode )
  {
    this.requirePostalCode = requirePostalCode;
  }

  public Long getCurrencyId()
  {
    return currencyId;
  }

  public void setCurrencyId( Long currencyId )
  {
    this.currencyId = currencyId;
  }

  public Boolean getSmsCapable()
  {
    return smsCapable;
  }

  public void setSmsCapable( Boolean smsCapable )
  {
    this.smsCapable = smsCapable;
  }

}

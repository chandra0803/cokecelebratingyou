/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/country/Country.java,v $
 */

package com.biperf.core.domain.country;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.AddressMethodType;
import com.biperf.core.domain.enums.CountryStatusType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.supplier.Supplier;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Country domain object which represents a country within the Beacon application.
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
 * <td>June 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Country extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  // ISO country codes
  public static final String FRANCE = "fr";
  public static final String CANADA = "ca";
  public static final String MEXICO = "mx";
  public static final String UNITED_STATES = "us";
  public static final String ABBR_UNITED_STATES = "USA";
  public static final String ABBR_CANADA = "CAN";
  public static final String ABBR_MEXICO = "MEX";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private AddressMethodType addressMethod;
  private CountryStatusType status;
  private Supplier supplier;

  private String countryCode;
  private String countryName;
  private String cmAssetCode;
  private String nameCmKey;
  private String awardbanqAbbrev;
  private String campaignNbr;
  private String campaignPassword;
  private String programId;
  private String programPassword;
  private Date dateStatus;
  private String shoppingBannerUrl;
  private Boolean allowSms;
  private boolean smsCapable;
  private Boolean requirePostalCode;
  private String supportEmailAddr;
  private String campaignCode;
  private String phoneCountryCode;
  private String countryNameandPhoneCodeDisplay;
  private TimeZoneId timeZoneId;
  private Boolean displayTravelAward;
  private BigDecimal budgetMediaValue;

  private String suppliersName;
  private String currencyCode;
  private Boolean displayExperiences;

  private Set countrySuppliers = new LinkedHashSet();

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Boolean getRequirePostalCode()
  {
    return requirePostalCode;
  }

  public void setRequirePostalCode( Boolean requirePostalCode )
  {
    this.requirePostalCode = requirePostalCode;
  }

  public AddressMethodType getAddressMethod()
  {
    return addressMethod;
  }

  public void setAddressMethod( AddressMethodType addressMethod )
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

  public String getCountryName()
  {
    return countryName;
  }

  public String getI18nCountryName()
  {
    return ContentReaderManager.getText( this.cmAssetCode, this.nameCmKey );
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public Date getDateStatus()
  {
    return dateStatus;
  }

  public void setDateStatus( Date dateStatus )
  {
    this.dateStatus = dateStatus;
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

  public CountryStatusType getStatus()
  {
    return status;
  }

  public void setStatus( CountryStatusType status )
  {
    this.status = status;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------
  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Country ) )
    {
      return false;
    }

    final Country country = (Country)object;

    if ( getCountryCode() != null // && getCountryName() != null
        && getAwardbanqAbbrev() != null
            ? !getCountryCode().equals( country.getCountryCode() )
                // && !getCountryName().equals( country.getCountryName() )
                && !getAwardbanqAbbrev().equals( country.getAwardbanqAbbrev() )
            : country.getCountryCode() != null // &&
                                               // country.getCountryName()
                                               // !=
                                               // null
                && country.getAwardbanqAbbrev() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return getCountryCode() != null // && getCountryName() != null
        && getAwardbanqAbbrev() != null
            ? getCountryCode().hashCode() // +
                                          // getCountryName().hashCode()
                + getAwardbanqAbbrev().hashCode()
            : 0;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Country [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{countryCode=" ).append( this.getCountryCode() ).append( "}, " );
    buf.append( "{countryName=" ).append( this.getCountryName() ).append( "}, " );
    buf.append( "{cmAssetCode=" ).append( this.getCmAssetCode() ).append( "}, " );
    buf.append( "{cmAssetKey=" ).append( this.getNameCmKey() ).append( "}, " );
    buf.append( "{awardbanqAbbrev=" ).append( this.getAwardbanqAbbrev() ).append( "}, " );
    buf.append( "{campaignNbr=" ).append( this.getCampaignNbr() ).append( "}" );
    buf.append( "{programId=" ).append( this.getProgramId() ).append( "}" );
    buf.append( "{programPassword=" ).append( this.getProgramPassword() ).append( "}" );
    buf.append( "{addressMethod=" ).append( this.getAddressMethod().getCode() ).append( "}" );
    buf.append( "{status=" ).append( this.getStatus().getCode() ).append( "}" );
    // buf.append( "{supplier=" ).append( this.getSupplier() ).append( "}" );
    buf.append( "{requirePostalCode" ).append( this.getRequirePostalCode() ).append( "}" );
    buf.append( "{supportEmailAddr" ).append( this.getSupportEmailAddr() ).append( "}" );
    buf.append( "{campaignCode" ).append( this.getCampaignCode() ).append( "}" );
    buf.append( "{displayTravelAward" ).append( this.getDisplayTravelAward() ).append( "}" );
    buf.append( "{displayExperiences" ).append( this.isDisplayExperiences() ).append( "}" );
    buf.append( "]" );
    return buf.toString();
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

  public String getSupportEmailAddr()
  {
    return supportEmailAddr;
  }

  public void setSupportEmailAddr( String supportEmailAddr )
  {
    this.supportEmailAddr = supportEmailAddr;
  }

  public void setCampaignCode( String campaignCode )
  {
    this.campaignCode = campaignCode;
  }

  public String getCampaignCode()
  {
    return campaignCode;
  }

  public String getCountryNameandPhoneCodeDisplay()
  {
    countryNameandPhoneCodeDisplay = getI18nCountryName() + " +" + getPhoneCountryCode();
    return countryNameandPhoneCodeDisplay;
  }

  public void setPhoneCountryCode( String phoneCountryCode )
  {
    this.phoneCountryCode = phoneCountryCode;
  }

  public String getPhoneCountryCode()
  {
    return phoneCountryCode;
  }

  public TimeZoneId getTimeZoneId()
  {
    return timeZoneId;
  }

  public void setTimeZoneId( TimeZoneId timeZoneId )
  {
    this.timeZoneId = timeZoneId;
  }

  public void setBudgetMediaValue( BigDecimal budgetMediaValue )
  {
    this.budgetMediaValue = budgetMediaValue;
  }

  public BigDecimal getBudgetMediaValue()
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

  public Set getCountrySuppliers()
  {
    return countrySuppliers;
  }

  public void setCountrySuppliers( Set countrySuppliers )
  {
    this.countrySuppliers = countrySuppliers;
  }

  public void addCountrySuppliers( CountrySupplier countrySupplier )
  {
    countrySupplier.setCountry( this );
    this.countrySuppliers.add( countrySupplier );
  }

  public Supplier getPrimarySupplier()
  {
    Supplier primarySupplier = null;
    if ( countrySuppliers != null && countrySuppliers.size() > 0 )
    {
      Iterator supplierIter = countrySuppliers.iterator();
      while ( supplierIter.hasNext() )
      {
        CountrySupplier savedCountrySupplier = (CountrySupplier)supplierIter.next();
        if ( savedCountrySupplier.isPrimary() )
        {
          primarySupplier = savedCountrySupplier.getSupplier();
        }
      }
    }

    return primarySupplier;
  }

  public void setSuppliersName( String suppliersName )
  {
    this.suppliersName = suppliersName;
  }

  public String getSuppliersName()
  {
    return suppliersName;
  }

  public String getCurrencyCode()
  {
    return currencyCode;
  }

  public void setCurrencyCode( String currencyCode )
  {
    this.currencyCode = currencyCode;
  }
  
  public Boolean isDisplayExperiences()
  {
    return displayExperiences;
  }
  
  public void setDisplayExperiences( Boolean displayExperiences )
  {
    this.displayExperiences = displayExperiences;
  }

  public boolean isSmsCapable()
  {
    return smsCapable;
  }

  public void setSmsCapable( boolean smsCapable )
  {
    this.smsCapable = smsCapable;
  }
}

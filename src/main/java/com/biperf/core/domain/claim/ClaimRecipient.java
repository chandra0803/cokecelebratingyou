/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/ClaimRecipient.java,v $
 */

package com.biperf.core.domain.claim;

import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.utils.BeanLocator;

/**
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
 * <td>wadzinsk</td>
 * <td>Oct 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class ClaimRecipient extends ClaimItem
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private Participant recipient;
  private Node node;
  private PromoMerchCountry promoMerchCountry;
  private PromoMerchProgramLevel promoMerchProgramLevel;
  private Long awardQuantity;
  private BigDecimal cashAwardQuantity;
  private String productId;
  private Long calculatorScore;
  private Date notificationDate;

  private transient Long previousAwardQuantity;
  private transient BigDecimal previousCashAwardQuantity;
  private boolean modalWindowViewed;

  // Client customizations for WIP #42701 starts 
  private String cashCurrencyCode;
  private String cashPaxDivisionNumber;
  private boolean cashPaxClaimed;
  private String displayUSDAwardQuantity;
  private Long customCashAwardQuantity;
 
  public Long getCustomCashAwardQuantity()
  {
    return customCashAwardQuantity;
  }

  public void setCustomCashAwardQuantity( Long customCashAwardQuantity )
  {
    this.customCashAwardQuantity = customCashAwardQuantity;
  }

  public String getCashCurrencyCode()
  {
    return cashCurrencyCode;
  }

  public void setCashCurrencyCode( String cashCurrencyCode )
  {
    this.cashCurrencyCode = cashCurrencyCode;
  }

  public String getCashPaxDivisionNumber()
  {
    return cashPaxDivisionNumber;
  }

  public void setCashPaxDivisionNumber( String cashPaxDivisionNumber )
  {
    this.cashPaxDivisionNumber = cashPaxDivisionNumber;
  }

  public boolean isCashPaxClaimed()
  {
    return cashPaxClaimed;
  }

  public void setCashPaxClaimed( boolean cashPaxClaimed )
  {
    this.cashPaxClaimed = cashPaxClaimed;
  }
  
  public String getDisplayUSDAwardQuantity()
  {
    return displayUSDAwardQuantity;
  }

  public void setDisplayUSDAwardQuantity( String displayUSDAwardQuantity )
  {
    this.displayUSDAwardQuantity = displayUSDAwardQuantity;
  }
  // Client customizations for WIP #42701 ends
  // ---------------------------------------------------------------------------
  // Equals and Hash Code Methods
  // ---------------------------------------------------------------------------
  public Date getNotificationDate()
  {
    return notificationDate;
  }

  public void setNotificationDate( Date notificationDate )
  {
    this.notificationDate = notificationDate;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public Node getNode()
  {
    return node;
  }

  public Participant getRecipient()
  {
    return recipient;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public void setRecipient( Participant recipient )
  {
    this.recipient = recipient;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public PromoMerchProgramLevel getPromoMerchProgramLevel()
  {
    return promoMerchProgramLevel;
  }

  public void setPromoMerchProgramLevel( PromoMerchProgramLevel promoMerchProgramLevel )
  {
    this.promoMerchProgramLevel = promoMerchProgramLevel;
  }

  public String getProductId()
  {
    return productId;
  }

  public void setProductId( String productId )
  {
    this.productId = productId;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ClaimRecipient ) )
    {
      return false;
    }

    ClaimRecipient claimRecipient = (ClaimRecipient)object;

    if ( getSerialId() != null ? !getSerialId().equals( claimRecipient.getSerialId() ) : claimRecipient.getSerialId() != null )
    {
      return false;
    }

    return true;

  }

  public int hashCode()
  {
    int result = 0;

    result += this.getSerialId() != null ? this.getSerialId().hashCode() : 13;

    return result;
  }

  public int getCalculatedAwardQuantity()
  {
    int calculatedAwardQuantity = 0;
    if ( null != awardQuantity )
    {
      calculatedAwardQuantity = awardQuantity.intValue();
      String hostCountryCode = null;
      String foreignCountryCode = null;
      if ( null != getClaim() && null != getClaim().getSubmitterCountry() )
      {
        hostCountryCode = getClaim().getSubmitterCountry().getCountryCode();
      }
      if ( null != getRecipientCountry() )
      {
        foreignCountryCode = getRecipientCountry().getCountryCode();
      }
      if ( null != hostCountryCode && null != foreignCountryCode )
      {
        // TODO: We want to avoid calling the service layer from a domain object.
        // However, having this method is convienient, espically when called from a jsp context.
        Double ratio = getAwardBanQService().getMediaRatio( hostCountryCode, foreignCountryCode );
        if ( ratio != null )
        {
          calculatedAwardQuantity = new Double( Math.ceil( awardQuantity.doubleValue() * ratio.doubleValue() ) ).intValue();
        }
      }
    }
    return calculatedAwardQuantity;
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return factory.getAwardBanQService();
  }

  public String getRecipientDisplayCountryName()
  {
    Country country = getRecipientCountry();
    if ( null != country )
    {
      return country.getI18nCountryName();
    }
    return null;
  }

  public String getRecipientDisplayCountryCode()
  {
    Country country = getRecipientCountry();
    if ( null != country )
    {
      return country.getCountryCode();
    }
    return null;
  }

  public Country getRecipientCountry()
  {
    Country country = null;
    if ( null != recipient )
    {
      UserAddress address = recipient.getPrimaryAddress();
      if ( null != address )
      {
        country = address.getAddress().getCountry();
      }
    }
    return country;
  }

  public String getRecipientDisplayName()
  {
    String displayName;
    if ( recipient == null )
    {
      // For team nomination, recipient is null. Show team name
      displayName = ( (NominationClaim)getClaim() ).getTeamName();
    }
    else
    {
      displayName = recipient.getNameLFMWithComma();
    }

    return displayName;
  }

  public String getFirstName()
  {
    if ( getRecipient() != null )
    {
      return getRecipient().getFirstName();
    }
    return null;
  }

  public String getLastName()
  {
    if ( getRecipient() != null )
    {
      return getRecipient().getLastName();
    }
    return null;
  }

  public Long getCalculatorScore()
  {
    return calculatorScore;
  }

  public void setCalculatorScore( Long calculatorScore )
  {
    this.calculatorScore = calculatorScore;
  }

  public PromoMerchCountry getPromoMerchCountry()
  {
    return promoMerchCountry;
  }

  public void setPromoMerchCountry( PromoMerchCountry promoMerchCountry )
  {
    this.promoMerchCountry = promoMerchCountry;
  }

  public Long getPreviousAwardQuantity()
  {
    return previousAwardQuantity;
  }

  public void setPreviousAwardQuantity( Long previousAwardQuantity )
  {
    this.previousAwardQuantity = previousAwardQuantity;
  }

  public BigDecimal getPreviousCashAwardQuantity()
  {
    return previousCashAwardQuantity;
  }

  public void setPreviousCashAwardQuantity( BigDecimal previousCashAwardQuantity )
  {
    this.previousCashAwardQuantity = previousCashAwardQuantity;
  }

  public BigDecimal getCashAwardQuantity()
  {
    return cashAwardQuantity;
  }

  public void setCashAwardQuantity( BigDecimal cashAwardQuantity )
  {
    this.cashAwardQuantity = cashAwardQuantity;
  }

  public boolean isModalWindowViewed()
  {
    return modalWindowViewed;
  }

  public void setModalWindowViewed( boolean modalWindowViewed )
  {
    this.modalWindowViewed = modalWindowViewed;
  }
 //-------------------------------
// coke customization start
//-------------------------------
private String optOut;
public String getOptOut()
{
  return optOut;
}

public void setOptOut( String optOut )
{
  this.optOut = optOut;
}
}

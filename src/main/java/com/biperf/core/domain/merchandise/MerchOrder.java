
package com.biperf.core.domain.merchandise;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@SuppressWarnings( "serial" )
public class MerchOrder extends BaseDomain
{
  @SuppressWarnings( "unused" )
  private String giftCode;
  private String giftCodeDecrypted;
  @SuppressWarnings( "unused" )
  private String giftCodeKey;
  private String giftCodeKeyDecrypted;

  private Claim claim;
  private Participant participant;
  private PaxGoal paxGoal;

  private boolean redeemed;
  private MerchGiftCodeType merchGiftCodeType;

  private Long levelPosition;
  private int points;
  private String referenceNumber;
  private Date expirationDate;
  private String productId;
  private String productDescription;

  private String programId;

  private String orderNumber;
  private String orderStatus;

  private Date dateLastReminded;
  private Long batchId;
  
  //Client customizations for wip #23129 starts
  private String billingCode;

  public String getBillingCode()
  {
    return billingCode;
  }

  public void setBillingCode( String billingCode )
  {
    this.billingCode = billingCode;
  }
  // Client customizations for wip #23129 ends

  public String getOrderStatus()
  {
    return orderStatus;
  }

  public void setOrderStatus( String orderStatus )
  {
    this.orderStatus = orderStatus;
  }

  private PromoMerchProgramLevel promoMerchProgramLevel;
  private Set activityMerchOrders = new HashSet();

  /* WIP# 25130 Start */
  @JsonManagedReference
  private Set<MerchOrderBillCode> billCodes = new HashSet<MerchOrderBillCode>();
  /* WIP# 25130 End */

  /**
   * Default Constructor
   */
  public MerchOrder()
  {
    super();
  }

  public void addActivities( Set activities )
  {
    Iterator iter = activities.iterator();
    while ( iter.hasNext() )
    {
      Activity activity = (Activity)iter.next();
      this.addActivityMerchOrder( new ActivityMerchOrder( activity, this ) );
    }
  }

  @SuppressWarnings( "unchecked" )
  public void addActivityMerchOrder( ActivityMerchOrder activityMerchOrder )
  {
    activityMerchOrder.setMerchOrder( this );
    this.activityMerchOrders.add( activityMerchOrder );
  }

  @SuppressWarnings( "rawtypes" )
  public Set getActivityMerchOrders()
  {
    return activityMerchOrders;
  }

  @SuppressWarnings( "rawtypes" )
  public void setActivityMerchOrders( Set activityMerchOrders )
  {
    this.activityMerchOrders = activityMerchOrders;
  }

  /**
   * @return value of claim property
   */
  public Claim getClaim()
  {
    return claim;
  }

  /**
   * @param claim value for claim property
   */
  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof MerchOrder ) )
    {
      return false;
    }

    MerchOrder merchOrder = (MerchOrder)object;
    if ( getGiftCode() != null ? !getGiftCode().equals( merchOrder.getGiftCode() ) : merchOrder.getGiftCode() != null )
    {
      return false;
    }

    return true;

  } // end equals

  public int hashCode()
  {
    int result = 0;

    result += getGiftCode() != null ? getGiftCode().hashCode() : 13;

    return result;
  }

  public String getGiftCode()
  {
    return giftCodeDecrypted;
  }

  public void setGiftCode( String giftCode )
  {
    this.giftCode = giftCode;
    this.giftCodeDecrypted = giftCode;
  }

  public String getGiftCodeKey()
  {
    return giftCodeKeyDecrypted;
  }

  public void setGiftCodeKey( String giftCodeKey )
  {
    this.giftCodeKey = giftCodeKey;
    this.giftCodeKeyDecrypted = giftCodeKey;
  }

  public String getFullGiftCode()
  {
    StringBuffer fullGiftCode = new StringBuffer();

    if ( this.giftCodeDecrypted != null && this.giftCodeKeyDecrypted != null )
    {
      fullGiftCode.append( giftCodeDecrypted ).append( giftCodeKeyDecrypted );
      return fullGiftCode.toString();
    }
    return null;
  }

  public void setFullGiftCode( String fullGiftCode )
  {
    if ( null != fullGiftCode && fullGiftCode.length() > 8 )
    {
      String code = fullGiftCode.substring( 0, 8 );
      String key = fullGiftCode.substring( 8, fullGiftCode.length() );
      setGiftCode( code );
      setGiftCodeKey( key );
    }
  }

  public Long getLevelPosition()
  {
    return levelPosition;
  }

  public void setLevelPosition( Long levelPosition )
  {
    this.levelPosition = levelPosition;
  }

  public MerchGiftCodeType getMerchGiftCodeType()
  {
    return merchGiftCodeType;
  }

  public void setMerchGiftCodeType( MerchGiftCodeType merchGiftCodeType )
  {
    this.merchGiftCodeType = merchGiftCodeType;
  }

  public String getProductId()
  {
    return productId;
  }

  public void setProductId( String productId )
  {
    this.productId = productId;
  }

  public boolean isRedeemed()
  {
    return redeemed;
  }

  public void setRedeemed( boolean redeemed )
  {
    this.redeemed = redeemed;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public PromoMerchProgramLevel getPromoMerchProgramLevel()
  {
    return promoMerchProgramLevel;
  }

  public void setPromoMerchProgramLevel( PromoMerchProgramLevel promoMerchProgramLevel )
  {
    this.promoMerchProgramLevel = promoMerchProgramLevel;
  }

  public int getPoints()
  {
    return points;
  }

  public void setPoints( int points )
  {
    this.points = points;
  }

  public String getProductDescription()
  {
    return productDescription;
  }

  public void setProductDescription( String productDescription )
  {
    this.productDescription = productDescription;
  }

  public String getOrderNumber()
  {
    return orderNumber;
  }

  public void setOrderNumber( String orderNumber )
  {
    this.orderNumber = orderNumber;
  }

  public java.util.Date getExpirationDate()
  {
    return expirationDate;
  }

  public void setExpirationDate( java.util.Date expirationDate )
  {
    this.expirationDate = expirationDate;
  }

  public String getReferenceNumber()
  {
    return referenceNumber;
  }

  public void setReferenceNumber( String referenceNumber )
  {
    this.referenceNumber = referenceNumber;
  }

  public Date getDateLastReminded()
  {
    return dateLastReminded;
  }

  public void setDateLastReminded( Date dateLastReminded )
  {
    this.dateLastReminded = dateLastReminded;
  }

  public Long getBatchId()
  {
    return batchId;
  }

  public void setBatchId( Long batchId )
  {
    this.batchId = batchId;
  }

  /* WIP# 25130 Start */
  public Set<MerchOrderBillCode> getBillCodes()
  {
    return billCodes;
  }

  public void setBillCodes( Set<MerchOrderBillCode> billCodes )
  {
    this.billCodes = billCodes;
  }
  /* WIP# 25130 End */

}

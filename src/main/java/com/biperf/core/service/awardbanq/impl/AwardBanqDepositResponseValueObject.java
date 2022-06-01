
package com.biperf.core.service.awardbanq.impl;

import com.biperf.awardbanq.value.participant.BRTCredentials;

public class AwardBanqDepositResponseValueObject implements java.io.Serializable
{
  private static final long serialVersionUID = 1L;

  private BRTCredentials brtCredentials;
  private String campaignId;
  private String sourceSystem;
  private String sourceUserId;
  private String campaignNbr;
  private String omPaxId;
  private int amount;
  private String description;
  private String bnkTrGroup;
  private Long trackingId;
  private String lastMaintOprid;
  private String taxable;
  private String bnkTrCode;
  private String primaryBillingCode;
  private String secondaryBillingCode;
  private String descriptionTwo;
  private String billCodeList;
  private String expirationDate;

  public AwardBanqDepositResponseValueObject()
  {

  }

  public AwardBanqDepositResponseValueObject( AccountDepositVO depositVO, BRTCredentials brtCredentials )
  {
    this.brtCredentials = brtCredentials;
    // this.campaignId = depositVO.getCampaignNbr();
    this.campaignId = depositVO.getCampaignNbr();
    this.sourceSystem = depositVO.getSourceSystem();
    this.sourceUserId = depositVO.getSourceUserId();
    this.campaignNbr = depositVO.getCampaignNbr();
    this.omPaxId = depositVO.getOmPaxId();
    this.amount = depositVO.getAmount();
    this.description = depositVO.getDescription();
    this.bnkTrCode = depositVO.getBnkTrCode();
    this.bnkTrGroup = depositVO.getBnkTrGroup();
    this.trackingId = depositVO.getTrackingId();
    this.lastMaintOprid = depositVO.getLastMaintOprid();
    this.taxable = depositVO.getTaxable();
    this.primaryBillingCode = depositVO.getPrimaryBillingCode();
    this.secondaryBillingCode = depositVO.getSecondaryBillingCode();
    this.descriptionTwo = depositVO.getDescription();
    this.billCodeList = depositVO.getBillCodeList();
    this.expirationDate = depositVO.getExpirationDate();
  }

  public BRTCredentials getBrtCredentials()
  {
    return brtCredentials;
  }

  public void setBrtCredentials( BRTCredentials brtCredentials )
  {
    this.brtCredentials = brtCredentials;
  }

  public String getCampaignId()
  {
    return campaignId;
  }

  public void setCampaignId( String campaignId )
  {
    this.campaignId = campaignId;
  }

  public String getSourceSystem()
  {
    return sourceSystem;
  }

  public void setSourceSystem( String sourceSystem )
  {
    this.sourceSystem = sourceSystem;
  }

  public String getSourceUserId()
  {
    return sourceUserId;
  }

  public void setSourceUserId( String sourceUserId )
  {
    this.sourceUserId = sourceUserId;
  }

  public String getCampaignNbr()
  {
    return campaignNbr;
  }

  public void setCampaignNbr( String campaignNbr )
  {
    this.campaignNbr = campaignNbr;
  }

  public String getOmPaxId()
  {
    return omPaxId;
  }

  public void setOmPaxId( String omPaxId )
  {
    this.omPaxId = omPaxId;
  }

  public int getAmount()
  {
    return amount;
  }

  public void setAmount( int amount )
  {
    this.amount = amount;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getBnkTrGroup()
  {
    return bnkTrGroup;
  }

  public void setBnkTrGroup( String bnkTrGroup )
  {
    this.bnkTrGroup = bnkTrGroup;
  }

  public Long getTrackingId()
  {
    return trackingId;
  }

  public void setTrackingId( Long trackingId )
  {
    this.trackingId = trackingId;
  }

  public String getLastMaintOprid()
  {
    return lastMaintOprid;
  }

  public void setLastMaintOprid( String lastMaintOprid )
  {
    this.lastMaintOprid = lastMaintOprid;
  }

  public String getTaxable()
  {
    return taxable;
  }

  public void setTaxable( String taxable )
  {
    this.taxable = taxable;
  }

  public String getBnkTrCode()
  {
    return bnkTrCode;
  }

  public void setBnkTrCode( String bnkTrCode )
  {
    this.bnkTrCode = bnkTrCode;
  }

  public String getPrimaryBillingCode()
  {
    return primaryBillingCode;
  }

  public void setPrimaryBillingCode( String primaryBillingCode )
  {
    this.primaryBillingCode = primaryBillingCode;
  }

  public String getSecondaryBillingCode()
  {
    return secondaryBillingCode;
  }

  public void setSecondaryBillingCode( String secondaryBillingCode )
  {
    this.secondaryBillingCode = secondaryBillingCode;
  }

  public String getDescriptionTwo()
  {
    return descriptionTwo;
  }

  public void setDescriptionTwo( String descriptionTwo )
  {
    this.descriptionTwo = descriptionTwo;
  }

  public String getBillCodeList()
  {
    return billCodeList;
  }

  public void setBillCodeList( String billCodeList )
  {
    this.billCodeList = billCodeList;
  }

  public String getExpirationDate()
  {
    return expirationDate;
  }

  public void setExpirationDate( String expirationDate )
  {
    this.expirationDate = expirationDate;
  }

}

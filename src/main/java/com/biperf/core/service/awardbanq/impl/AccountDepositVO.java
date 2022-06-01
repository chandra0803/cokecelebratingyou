
package com.biperf.core.service.awardbanq.impl;

import com.biperf.awardbanq.value.participant.BRTCredentials;

/**
 * Value object for the the OM deposit for a participant
 */
// @XmlRootElement
public class AccountDepositVO implements java.io.Serializable
{
  private String sourceSystem = null;
  private String sourceUserId = null;
  private String campaignNbr = null;
  private String omPaxId = null;
  private int amount = 0;
  private String description = null;
  private String bnkTrGroup = null;
  private long trackingId = 0;
  private String lastMaintOprid = null;
  private String taxable = null;
  private String bnkTrCode = null;
  private String primaryBillingCode;
  private String secondaryBillingCode;
  // @XmlElement(name = "brtCredentials")
  private BRTCredentials brtCredentials;
  private String descriptionTwo;
  private String billCodeList; // Pipe delimited containing up to 4000 chars for bill code
  private String expirationDate; // Expiration date for FIFO campaign project

  /**
   * AccountDepositVO default constructor
   */
  public AccountDepositVO()
  {
  }

  /**
   * Returns the amount.
   * 
   * @return int
   */
  public int getAmount()
  {
    return amount;
  }

  /**
   * Returns the bnkTrCode.
   * 
   * @return String
   */
  public String getBnkTrCode()
  {
    return bnkTrCode;
  }

  /**
   * Returns the bnkTrGroup.
   * 
   * @return String
   */
  public String getBnkTrGroup()
  {
    return bnkTrGroup;
  }

  /**
   * Returns the campaignNbr.
   * 
   * @return String
   */
  public String getCampaignNbr()
  {
    return campaignNbr;
  }

  /**
   * Returns the description.
   * 
   * @return String
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the lastMaintOprid.
   * 
   * @return String
   */
  public String getLastMaintOprid()
  {
    return lastMaintOprid;
  }

  /**
   * Returns the omPaxId.
   * 
   * @return String
   */
  public String getOmPaxId()
  {
    return omPaxId;
  }

  /**
   * Returns the sourceSystem.
   * 
   * @return String
   */
  public String getSourceSystem()
  {
    return sourceSystem;
  }

  /**
   * Returns the sourceUserId.
   * 
   * @return String
   */
  public String getSourceUserId()
  {
    return sourceUserId;
  }

  /**
   * Returns the taxable.
   * 
   * @return String
   */
  public String getTaxable()
  {
    return taxable;
  }

  /**
   * Returns the trackingId.
   * 
   * @return long
   */
  public long getTrackingId()
  {
    return trackingId;
  }

  /**
   * @return primary billing code
   */
  public String getPrimaryBillingCode()
  {
    return primaryBillingCode;
  }

  /**
   * @return secondary billing code
   */
  public String getSecondaryBillingCode()
  {
    return secondaryBillingCode;
  }

  /**
   * Sets the amount.
   * 
   * @param amount
   *            The amount to set
   */
  public void setAmount( int amount )
  {
    this.amount = amount;
  }

  /**
   * Sets the bnkTrCode.
   * 
   * @param bnkTrCode
   *            The bnkTrCode to set
   */
  public void setBnkTrCode( String bnkTrCode )
  {
    this.bnkTrCode = bnkTrCode;
  }

  /**
   * Sets the bnkTrGroup.
   * 
   * @param bnkTrGroup
   *            The bnkTrGroup to set
   */
  public void setBnkTrGroup( String bnkTrGroup )
  {
    this.bnkTrGroup = bnkTrGroup;
  }

  /**
   * Sets the campaignNbr.
   * 
   * @param campaignNbr
   *            The campaignNbr to set
   */
  public void setCampaignNbr( String campaignNbr )
  {
    this.campaignNbr = campaignNbr;
  }

  /**
   * Sets the description.
   * 
   * @param description
   *            The description to set
   */
  public void setDescription( String description )
  {
    this.description = description;
  }

  /**
   * Sets the lastMaintOprid.
   * 
   * @param lastMaintOprid
   *            The lastMaintOprid to set
   */
  public void setLastMaintOprid( String lastMaintOprid )
  {
    this.lastMaintOprid = lastMaintOprid;
  }

  /**
   * Sets the omPaxId.
   * 
   * @param omPaxId
   *            The omPaxId to set
   */
  public void setOmPaxId( String omPaxId )
  {
    this.omPaxId = omPaxId;
  }

  /**
   * Sets the sourceSystem.
   * 
   * @param sourceSystem
   *            The sourceSystem to set
   */
  public void setSourceSystem( String sourceSystem )
  {
    this.sourceSystem = sourceSystem;
  }

  /**
   * Sets the sourceUserId.
   * 
   * @param sourceUserId
   *            The sourceUserId to set
   */
  public void setSourceUserId( String sourceUserId )
  {
    this.sourceUserId = sourceUserId;
  }

  /**
   * Sets the taxable.
   * 
   * @param taxable
   *            The taxable to set
   */
  public void setTaxable( String taxable )
  {
    this.taxable = taxable;
  }

  /**
   * Sets the trackingId.
   * 
   * @param trackingId
   *            The trackingId to set
   */
  public void setTrackingId( long trackingId )
  {
    this.trackingId = trackingId;
  }

  /**
   * Sets the primary billing code.
   * 
   * @param primaryBillingCode
   */
  public void setPrimaryBillingCode( String primaryBillingCode )
  {
    this.primaryBillingCode = primaryBillingCode;
  }

  /**
   * Sets the secondary billing code.
   * 
   * @param secondaryBillingCode
   */
  public void setSecondaryBillingCode( String secondaryBillingCode )
  {
    this.secondaryBillingCode = secondaryBillingCode;
  }

  public BRTCredentials getBrtCredentials()
  {
    return brtCredentials;
  }

  public void setBrtCredentials( BRTCredentials brtCredentials )
  {
    this.brtCredentials = brtCredentials;
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

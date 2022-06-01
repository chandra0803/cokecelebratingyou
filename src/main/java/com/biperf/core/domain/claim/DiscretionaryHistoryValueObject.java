
package com.biperf.core.domain.claim;

import java.util.Date;

public class DiscretionaryHistoryValueObject
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String promotionName;

  private String promotionTypeName;

  private Date transactionDate;

  private Long awardQuantity;

  private String awardTypeName;

  private String journalStatus;

  private String journalComments;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  public DiscretionaryHistoryValueObject()
  {
    // default constructor
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getAwardTypeName()
  {
    return awardTypeName;
  }

  public void setAwardTypeName( String awardTypeName )
  {
    this.awardTypeName = awardTypeName;
  }

  public String getJournalStatus()
  {
    return journalStatus;
  }

  public void setJournalStatus( String journalStatus )
  {
    this.journalStatus = journalStatus;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public Date getTransactionDate()
  {
    return transactionDate;
  }

  public void setTransactionDate( Date transactionDate )
  {
    this.transactionDate = transactionDate;
  }

  public String getJournalComments()
  {
    return journalComments;
  }

  public void setJournalComments( String journalComments )
  {
    this.journalComments = journalComments;
  }
}

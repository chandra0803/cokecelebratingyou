
package com.biperf.core.domain.claim;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;

public class GoalquestHistoryValueObject
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

  private boolean isReversal = false;
  private Promotion promotion;
  private Timestamp submissionDate;
  private Participant recipient;
  private String formatSubmissionDate;
  private List merchGiftCodeActivityList;
  private String levelName;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  public GoalquestHistoryValueObject()
  {
    // default constructor
  }

  public GoalquestHistoryValueObject( Activity activity )
  {
    setPromotion( activity.getPromotion() );
    setPromotionName( getPromotion().getName() );
    setSubmissionDate( new Timestamp( activity.getSubmissionDate().getTime() ) );
    setRecipient( activity.getParticipant() );
    setAwardTypeName( PromotionAwardsType.lookup( activity.getPromotion().getAwardType().getCode() ).getName() );
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

  public boolean isReversal()
  {
    return isReversal;
  }

  public void setReversal( boolean isReversal )
  {
    this.isReversal = isReversal;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Timestamp getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Timestamp submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public Participant getRecipient()
  {
    return recipient;
  }

  public void setRecipient( Participant recipient )
  {
    this.recipient = recipient;
  }

  public String getFormatSubmissionDate()
  {
    return formatSubmissionDate;
  }

  public void setFormatSubmissionDate( String formatSubmissionDate )
  {
    this.formatSubmissionDate = formatSubmissionDate;
  }

  public List getMerchGiftCodeActivityList()
  {
    return merchGiftCodeActivityList;
  }

  public void setMerchGiftCodeActivityList( List merchGiftCodeActivityList )
  {
    this.merchGiftCodeActivityList = merchGiftCodeActivityList;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }
}

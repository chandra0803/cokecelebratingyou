
package com.biperf.core.mobileapp.recognition.service;

import java.util.List;

import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.value.RecognitionBean.BehaviorBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class EligibleRecognitionPromotion
{
  private static final int NO_END_DATE = -1;

  private final AbstractRecognitionPromotion promotion;

  private final Long id;
  private Long nodeId;
  private String nodeName;
  private final String name;
  private final int daysRemaining;
  private boolean awardAvailable;
  private int totalSent;
  private int lastSent;
  private BudgetInfo budgetInfo;
  private List<BadgeDetails> badgeDetails;
  private boolean behaviorRequired;
  private List<BehaviorBean> behaviors;
  private boolean hasMultipleOrgBasedBudgets;

  private String awardType;
  private Long awardMin;
  private Long awardMax;
  private Long awardFixed;

  // Make recognition private
  private boolean recognitionPrivateActive;
  private boolean allowYourOwnCard;

  public EligibleRecognitionPromotion( AbstractRecognitionPromotion promotion, Long id, String name, int daysRemaining )
  {
    this.promotion = promotion;
    this.id = id;
    this.name = name;
    this.daysRemaining = daysRemaining;
  }

  public EligibleRecognitionPromotion shallowCopy()
  {
    EligibleRecognitionPromotion clone = new EligibleRecognitionPromotion( promotion, id, name, daysRemaining );

    clone.setNodeId( nodeId );
    clone.setNodeName( nodeName );
    clone.setAwardAvailable( awardAvailable );
    clone.setTotalSent( totalSent );
    clone.setLastSent( lastSent );
    clone.setBudgetInfo( budgetInfo );
    clone.setBadgeDetails( badgeDetails );
    clone.setBehaviorRequired( behaviorRequired );
    clone.setBehaviors( behaviors );
    clone.setAwardType( awardType );
    clone.setAwardMax( awardMax );
    clone.setAwardMin( awardMin );
    clone.setAwardFixed( awardFixed );
    clone.setHasMultipleOrgBasedBudgets( hasMultipleOrgBasedBudgets );
    clone.setRecognitionPrivateActive( recognitionPrivateActive );
    clone.setAllowYourOwnCard( allowYourOwnCard );

    return clone;
  }

  @JsonIgnore
  public AbstractRecognitionPromotion getPromotion()
  {
    return promotion;
  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public int getDaysRemaining()
  {

    // we want to return the lesser (the soonest to expire) of this promotion's
    // end date or the budget's end date.
    // remember that a value of -1 means no date specified

    // if there is no budgetInfo then return my own daysRemaining
    if ( budgetInfo == null )
    {
      return daysRemaining;
    }

    // if both this promotion and the budget have end dates specified....
    if ( daysRemaining != NO_END_DATE && budgetInfo.getDaysRemaining() != NO_END_DATE )
    {
      return Math.min( daysRemaining, budgetInfo.getDaysRemaining() );
    }

    // if neither the promotion nor the budget have end dates specified...
    if ( daysRemaining == NO_END_DATE && budgetInfo.getDaysRemaining() == NO_END_DATE )
    {
      return NO_END_DATE;
    }

    // if this promotion has no end date specified but the budget does
    if ( daysRemaining == NO_END_DATE && budgetInfo.getDaysRemaining() != NO_END_DATE )
    {
      return budgetInfo.getDaysRemaining();
    }

    return daysRemaining;
  }

  public boolean endsBefore( EligibleRecognitionPromotion other )
  {
    if ( this.hasEndDate() && other.hasEndDate() )
    {
      if ( this.getDaysRemaining() < other.getDaysRemaining() )
      {
        return true;
      }
      if ( other.getDaysRemaining() < this.getDaysRemaining() )
      {
        return false;
      }
    }

    if ( this.hasEndDate() && !other.hasEndDate() )
    {
      return true;
    }

    if ( !this.hasEndDate() && other.hasEndDate() )
    {
      return false;
    }

    // default to false
    return false;
  }

  private boolean hasEndDate()
  {
    return daysRemaining != NO_END_DATE;
  }

  /* default */ void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public boolean isAwardAvailable()
  {
    return awardAvailable;
  }

  /* default */ void setAwardAvailable( boolean awardAvailable )
  {
    this.awardAvailable = awardAvailable;
  }

  /* default */ void setBudgetInfo( BudgetInfo budgetInfo )
  {
    if ( budgetInfo != null )
    {
      this.budgetInfo = budgetInfo;
      this.awardAvailable = true;
      nodeId = budgetInfo.getNodeId();
    }
  }

  public BudgetInfo getBudgetInfo()
  {
    return budgetInfo;
  }

  public int getTotalSent()
  {
    return totalSent;
  }

  /* default */ void setTotalSent( int totalSent )
  {
    this.totalSent = totalSent;
  }

  public int getLastSent()
  {
    return lastSent;
  }

  /* default */ void setLastSent( int lastSent )
  {
    this.lastSent = lastSent;
  }

  public String getAwardType()
  {
    return awardType;
  }

  /* default */ void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public Long getAwardMin()
  {
    return awardMin;
  }

  /* default */ void setAwardMin( Long awardMin )
  {
    this.awardMin = awardMin;
  }

  public Long getAwardMax()
  {
    return awardMax;
  }

  /* default */ void setAwardMax( Long awardMax )
  {
    this.awardMax = awardMax;
  }

  public Long getAwardFixed()
  {
    return awardFixed;
  }

  /* default */ void setAwardFixed( Long awardFixed )
  {
    this.awardFixed = awardFixed;
  }

  public List<BadgeDetails> getBadgeDetails()
  {
    return badgeDetails;
  }

  /* default */ void setBadgeDetails( List<BadgeDetails> badgeDetails )
  {
    this.badgeDetails = badgeDetails;
  }

  public List<BehaviorBean> getBehaviors()
  {
    return behaviors;
  }

  /* default */ void setBehaviors( List<BehaviorBean> behaviors )
  {
    this.behaviors = behaviors;
  }

  public boolean getHasMultipleOrgBasedBudgets()
  {
    return hasMultipleOrgBasedBudgets;
  }

  public void setHasMultipleOrgBasedBudgets( boolean multipleOrgBasedBudgets )
  {
    this.hasMultipleOrgBasedBudgets = multipleOrgBasedBudgets;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public boolean isBehaviorRequired()
  {
    return behaviorRequired;
  }

  public void setBehaviorRequired( boolean behaviorRequired )
  {
    this.behaviorRequired = behaviorRequired;
  }

  public boolean isRecognitionPrivateActive()
  {
    return recognitionPrivateActive;
  }

  public void setRecognitionPrivateActive( boolean recognitionPrivateActive )
  {
    this.recognitionPrivateActive = recognitionPrivateActive;
  }

  public boolean isAllowYourOwnCard()
  {
    return allowYourOwnCard;
  }

  public void setAllowYourOwnCard( boolean allowYourOwnCard )
  {
    this.allowYourOwnCard = allowYourOwnCard;
  }
}

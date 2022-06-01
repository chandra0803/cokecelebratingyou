
package com.biperf.core.domain.promotion;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * EngagementPromotion.
 * 
 * @author kandhi
 * @since Apr 8, 2014
 * @version 1.0
 */
@SuppressWarnings( "serial" )
public class EngagementPromotion extends Promotion
{
  public static final int INTERMEDIATE_MULTIPLIER = 3;
  public static final int ADVANCED_MULTIPLIER = 12;

  public static final long RECOGNITION_SENT = 0x0001; // 1
  public static final long RECOGNITION_RECEIVED = 0x0002; // 2
  public static final long UNIQUE_RECOGNITION_SENT = 0x0004; // 4
  public static final long UNIQUE_RECOGNITION_RECEIVED = 0x0008; // 8
  public static final long LOGIN_ACTIVITY = 0x0010; // 16

  private boolean isScoreActive;
  private boolean displayTargetToPax;
  private Double companyGoal;
  private String scorePreference;
  private Long selectedBenchmarks;
  private Date prevProcessDate;

  private Set<EngagementPromotions> engagementPromotions = new LinkedHashSet<EngagementPromotions>();
  private Set<EngagementPromotionRules> engagementPromotionRules = new LinkedHashSet<EngagementPromotionRules>();

  public EngagementPromotion()
  {
    super();
  }

  /**
   * Does a deep copy of the promotion and its children if specified. This is a customized
   * implementation of
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newPromotionName
   * @param newChildPromotionNameHolders
   * @return Object
   * @throws CloneNotSupportedException
   */
  @SuppressWarnings( "rawtypes" )
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    EngagementPromotion clonedPromotion = (EngagementPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    // copy the engagementPromotions
    clonedPromotion.setEngagementPromotions( new LinkedHashSet<EngagementPromotions>() );
    for ( Iterator engagementPromotionsIter = this.engagementPromotions.iterator(); engagementPromotionsIter.hasNext(); )
    {
      EngagementPromotions engagementPromotions = (EngagementPromotions)engagementPromotionsIter.next();
      clonedPromotion.addEngagementPromotions( (EngagementPromotions)engagementPromotions.clone() );
    }

    clonedPromotion.setEngagementPromotionRules( new LinkedHashSet<EngagementPromotionRules>() );
    for ( Iterator engagementPromotionRulesIter = this.engagementPromotionRules.iterator(); engagementPromotionRulesIter.hasNext(); )
    {
      EngagementPromotionRules engagementPromotionRules = (EngagementPromotionRules)engagementPromotionRulesIter.next();
      clonedPromotion.addEngagementPromotionRules( (EngagementPromotionRules)engagementPromotionRules.clone() );
    }

    return clonedPromotion;
  }

  public boolean isScoreActive()
  {
    return isScoreActive;
  }

  public void setScoreActive( boolean isScoreActive )
  {
    this.isScoreActive = isScoreActive;
  }

  public Double getCompanyGoal()
  {
    return companyGoal;
  }

  public void setCompanyGoal( Double companyGoal )
  {
    this.companyGoal = companyGoal;
  }

  public String getScorePreference()
  {
    return scorePreference;
  }

  public void setScorePreference( String scorePreference )
  {
    this.scorePreference = scorePreference;
  }

  public Set<EngagementPromotions> getEngagementPromotions()
  {
    return engagementPromotions;
  }

  public void setEngagementPromotions( Set<EngagementPromotions> engagementPromotions )
  {
    this.engagementPromotions = engagementPromotions;
  }

  public void addEngagementPromotions( EngagementPromotions engagementPromotions )
  {
    engagementPromotions.setPromotion( this );
    this.engagementPromotions.add( engagementPromotions );
  }

  public void addEngagementPromotionRules( EngagementPromotionRules engagementPromotionRules )
  {
    engagementPromotionRules.setPromotion( this );
    this.engagementPromotionRules.add( engagementPromotionRules );
  }

  public Set<EngagementPromotionRules> getEngagementPromotionRules()
  {
    return engagementPromotionRules;
  }

  public void setEngagementPromotionRules( Set<EngagementPromotionRules> engagementPromotionRules )
  {
    this.engagementPromotionRules = engagementPromotionRules;
  }

  public boolean isDisplayTargetToPax()
  {
    return displayTargetToPax;
  }

  public void setDisplayTargetToPax( boolean displayTargetToPax )
  {
    this.displayTargetToPax = displayTargetToPax;
  }

  public Long getSelectedBenchmarks()
  {
    return selectedBenchmarks;
  }

  public Date getPrevProcessDate()
  {
    return prevProcessDate;
  }

  public void setPrevProcessDate( Date prevProcessDate )
  {
    this.prevProcessDate = prevProcessDate;
  }

  public void setSelectedBenchmarks( Long selectedBenchmarks )
  {
    this.selectedBenchmarks = selectedBenchmarks;
  }

  public boolean isRecognitionSent()
  {
    if ( selectedBenchmarks == null )
    {
      return false;
    }
    return ( selectedBenchmarks & RECOGNITION_SENT ) != 0;
  }

  public boolean isRecognitionReceived()
  {
    if ( selectedBenchmarks == null )
    {
      return false;
    }
    return ( selectedBenchmarks & RECOGNITION_RECEIVED ) != 0;
  }

  public boolean isUniqueRecognitionSent()
  {
    if ( selectedBenchmarks == null )
    {
      return false;
    }
    return ( selectedBenchmarks & UNIQUE_RECOGNITION_SENT ) != 0;
  }

  public boolean isUniqueRecognitionReceived()
  {
    if ( selectedBenchmarks == null )
    {
      return false;
    }
    return ( selectedBenchmarks & UNIQUE_RECOGNITION_RECEIVED ) != 0;
  }

  public boolean isLoginActivity()
  {
    if ( selectedBenchmarks == null )
    {
      return false;
    }
    return ( selectedBenchmarks & LOGIN_ACTIVITY ) != 0;
  }

  @Override
  public boolean hasParent()
  {
    return false;
  }

  @Override
  public boolean isClaimFormUsed()
  {
    return false;
  }

}

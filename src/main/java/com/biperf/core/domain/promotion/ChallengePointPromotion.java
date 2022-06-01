/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/ChallengePointPromotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.utils.DateUtils;

/**
 * ChallengePointPromotion.
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
 * <td>May 29, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengePointPromotion extends GoalQuestPromotion
{
  private static final long serialVersionUID = 1L;

  public static final String PRIMARY_AWARD_THRESHOLD_NONE = "none";
  public static final String PRIMARY_AWARD_THRESHOLD_FIXED = "fixed";
  public static final String PRIMARY_AWARD_THRESHOLD_PCT_BASE = "perofbase";
  public static final String PRIMARY_AWARD_INCREMENT_FIXED = "fixed";
  public static final String PRIMARY_AWARD_INCREMENT_PCT_BASE = "perofbase";
  public static final String PRIMARY_AWARD_INCREMENT_PCT_THRS = "perthresh";
  public static final int MAX_LEVELS = 3;
  public static final String SECONDARY_TOTAL_PRODUCTION_PERCENTAGE = "totalPrdPct";
  public static final String SECONDARY_TOTAL_PRODUCTION_QUANTITY = "totalPrdQty";

  private ChallengePointAwardType challengePointAwardType;
  private Boolean managerCanSelect;
  private String selfEnrollProgramId;
  private String awardThresholdType;
  private Integer awardThresholdValue;
  private String awardIncrementType;
  private Integer awardIncrementValue;
  private Integer awardPerIncrement;
  private String totalTeamProductionMeasure;
  private Integer totalTeamProduction;
  private Integer managerAward;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.promotion.GoalQuestPromotion#isClaimFormUsed()
   */
  public boolean isClaimFormUsed()
  {
    return false;
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
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    ChallengePointPromotion clonedPromotion = (ChallengePointPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );
    clonedPromotion.setId( null );

    // copy the cpLevels
    clonedPromotion.setGoalLevels( new LinkedHashSet<AbstractGoalLevel>() );
    for ( Iterator<AbstractGoalLevel> iter = this.getGoalLevels().iterator(); iter.hasNext(); )
    {
      GoalLevel challengePointLevelToCopy = (GoalLevel)iter.next();
      clonedPromotion.addGoalLevel( (GoalLevel)challengePointLevelToCopy.clone() );
    }

    // Clear enroll program code and audience since this must be unique
    clonedPromotion.setEnrollProgramCode( null );
    clonedPromotion.setSecondaryAudienceType( null );
    return clonedPromotion;
  }

  /**
   * Given a goalLevelName, return a GoalLevel or ManagerOverrideGoalLevel object
   * 
   * @param goalLevelName
   * @return AbstractGoalLevel
   */
  public GoalLevel getChallengePointLevelByName( String levelName )
  {
    Set<AbstractGoalLevel> levels = getGoalLevels();
    if ( levels != null && !levels.isEmpty() )
    {
      Iterator<AbstractGoalLevel> iter = levels.iterator();
      while ( iter.hasNext() )
      {
        GoalLevel level = (GoalLevel)iter.next();
        if ( StringUtils.isNotBlank( level.getGoalLevelName() ) && level.getGoalLevelName().equalsIgnoreCase( levelName ) )
        {
          return level;
        }
      }
    }
    return null;
  }

  // ===================================
  // GETTERS & SETTERS
  // ===================================

  public String getGoalCollectionEndDateForDisplay()
  {
    return DateUtils.toDisplayString( getGoalCollectionEndDate() );
  }

  public String getGoalCollectionStartDateForDisplay()
  {
    return DateUtils.toDisplayString( getGoalCollectionStartDate() );
  }

  public boolean isGoalCollectionPeriodStarted()
  {
    Date today = new Date();
    return today.before( DateUtils.toStartDate( getGoalCollectionStartDate() ) );
  }

  public boolean isGoalCollectionPeriodEnded()
  {
    Date today = new Date();
    return today.after( DateUtils.toEndDate( getGoalCollectionEndDate() ) );
  }

  public boolean isAfterFinalProcessDate()
  {
    Date today = new Date();
    return today.after( DateUtils.toStartDate( getFinalProcessDate() ) );
  }

  public boolean isBeforeFinalProcessDate()
  {
    Date today = new Date();
    if ( getFinalProcessDate() != null )
    {
      return today.before( DateUtils.toStartDate( getFinalProcessDate() ) );
    }
    else
    {
      return false;
    }
  }

  public ChallengePointAwardType getChallengePointAwardType()
  {
    return challengePointAwardType;
  }

  public void setChallengePointAwardType( ChallengePointAwardType challengePointAwardType )
  {
    this.challengePointAwardType = challengePointAwardType;
  }

  public Boolean getManagerCanSelect()
  {
    return managerCanSelect;
  }

  public void setManagerCanSelect( Boolean managerCanSelect )
  {
    this.managerCanSelect = managerCanSelect;
  }

  public String getSelfEnrollProgramId()
  {
    return selfEnrollProgramId;
  }

  public void setSelfEnrollProgramId( String selfEnrollProgramId )
  {
    this.selfEnrollProgramId = selfEnrollProgramId;
  }

  public String getAwardThresholdType()
  {
    return awardThresholdType;
  }

  public void setAwardThresholdType( String awardThresholdType )
  {
    this.awardThresholdType = awardThresholdType;
  }

  public Integer getAwardThresholdValue()
  {
    return awardThresholdValue;
  }

  public void setAwardThresholdValue( Integer awardThresholdValue )
  {
    this.awardThresholdValue = awardThresholdValue;
  }

  public String getAwardIncrementType()
  {
    return awardIncrementType;
  }

  public void setAwardIncrementType( String awardIncrementType )
  {
    this.awardIncrementType = awardIncrementType;
  }

  public Integer getAwardIncrementValue()
  {
    return awardIncrementValue;
  }

  public void setAwardIncrementValue( Integer awardIncrementValue )
  {
    this.awardIncrementValue = awardIncrementValue;
  }

  public Integer getAwardPerIncrement()
  {
    return awardPerIncrement;
  }

  public void setAwardPerIncrement( Integer awardPerIncrement )
  {
    this.awardPerIncrement = awardPerIncrement;
  }

  public String getTotalTeamProductionMeasure()
  {
    return totalTeamProductionMeasure;
  }

  public void setTotalTeamProductionMeasure( String totalTeamProductionMeasure )
  {
    this.totalTeamProductionMeasure = totalTeamProductionMeasure;
  }

  public Integer getTotalTeamProduction()
  {
    return totalTeamProduction;
  }

  public void setTotalTeamProduction( Integer totalTeamProduction )
  {
    this.totalTeamProduction = totalTeamProduction;
  }

  public Integer getManagerAward()
  {
    return managerAward;
  }

  public void setManagerAward( Integer managerAward )
  {
    this.managerAward = managerAward;
  }

}

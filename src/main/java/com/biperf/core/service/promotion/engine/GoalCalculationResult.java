/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/GoalCalculationResult.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.user.User;

/**
 * GoalCalculationResult contains the results of a GoalPayoutStrategy
 * 
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
 * <td>meadows</td>
 * <td>Jan 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalCalculationResult implements Serializable
{
  /* User of receiver */
  private User reciever;
  /* A boolean indicating of the goal level was achieved */
  private boolean achieved = false;
  /* The calculated payout amount for this goal level */
  private BigDecimal calculatedPayout;
  /* the GoalLevel for this calculation */
  private AbstractGoalLevel goalLevel;
  /*
   * A Map containing the calculated payout by GoalLevel - only used for some of the manager
   * override types
   */
  private Map payoutsByGoal;
  /* A boolean indicating if the user is a Manager */
  private boolean manager;
  /* Role type for the node */
  private HierarchyRoleType nodeRole;
  /* Owner of the participants node - this is the owner credited with manager override */
  private User nodeOwner;
  /*
   * Contains the total performance to date. If this is a manager it will be filled accordingly: Amt
   * per achiever - # of employees that have achieved. % team achieved - Actual Percent Level Payout
   * - # of achievers % of team earnings - total team earnings
   */
  private BigDecimal totalPerformance;
  /* Contains base objective if set */
  private BigDecimal baseObjective;
  /* For managers this is the node that the award was for */
  private Long nodeId;
  /* A BigDecimal indication goal amount to achieve */
  private BigDecimal amountToAchieve;

  private BigDecimal calculatedIncremental;

  // If partner result, the value of this attribute would be 'true'
  private boolean partner;
  // If partner result, the value of this attribute would specify, PAX who picked the partner
  private User partnerToParticipant;
  // If partner result, the value of this attribute would specify, if PAX who picked the partner has
  // achieved his goal
  private boolean partnersParticipantAchieved;
  /* A boolean indicating that by default leveloneAward is true and levetwoAward is false */
  private boolean leveloneAward = false;
  private int totalAchieved = 0;
  private BigDecimal totalAward = new BigDecimal( 0 );
  private boolean owner;
  private BigDecimal percentAchieved;

  public void incrementCalculatedPayout( BigDecimal incrementAward )
  {
    if ( this.calculatedPayout == null )
    {
      this.calculatedPayout = incrementAward;
    }
    else
    {
      this.calculatedPayout = this.calculatedPayout.add( incrementAward );
    }
  }

  public BigDecimal getPercentageAchieved()
  {
    BigDecimal percentageAchieved = null;
    if ( goalLevel != null )
    {
      GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)goalLevel.getPromotion();
      int decimalPlaces = 0;
      if ( goalQuestPromotion.getAchievementPrecision() != null )
      {
        decimalPlaces = goalQuestPromotion.getAchievementPrecision().getPrecision();
      }
      if ( !isManager() && getTotalPerformance() != null && getAmountToAchieve() != null && getAmountToAchieve().longValue() > 0 )
      {
        // Always round down for percentage calculation
        percentageAchieved = getTotalPerformance().divide( getAmountToAchieve(), decimalPlaces + 2, BigDecimal.ROUND_DOWN ).movePointRight( 2 );
      }
    }
    return percentageAchieved;
  }

  public boolean isAchieved()
  {
    return achieved;
  }

  public void setAchieved( boolean achieved )
  {
    this.achieved = achieved;
  }

  public BigDecimal getCalculatedPayout()
  {
    return calculatedPayout;
  }

  public void setCalculatedPayout( BigDecimal calculatedPayout )
  {
    this.calculatedPayout = calculatedPayout;
  }

  public AbstractGoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( AbstractGoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public Map getPayoutsByGoal()
  {
    return payoutsByGoal;
  }

  public void setPayoutsByGoal( Map payoutsByGoal )
  {
    this.payoutsByGoal = payoutsByGoal;
  }

  public User getReciever()
  {
    return reciever;
  }

  public void setReciever( User reciever )
  {
    this.reciever = reciever;
  }

  public boolean isManager()
  {
    return manager;
  }

  public void setManager( boolean manager )
  {
    this.manager = manager;
  }

  public User getNodeOwner()
  {
    return nodeOwner;
  }

  public void setNodeOwner( User nodeOwner )
  {
    this.nodeOwner = nodeOwner;
  }

  public HierarchyRoleType getNodeRole()
  {
    return nodeRole;
  }

  public void setNodeRole( HierarchyRoleType nodeRole )
  {
    this.nodeRole = nodeRole;
  }

  public BigDecimal getTotalPerformance()
  {
    return totalPerformance;
  }

  public void setTotalPerformance( BigDecimal totalPerformance )
  {
    this.totalPerformance = totalPerformance;
  }

  public BigDecimal getBaseObjective()
  {
    return baseObjective;
  }

  public void setBaseObjective( BigDecimal baseObjective )
  {
    this.baseObjective = baseObjective;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public BigDecimal getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( BigDecimal amountToAchieve )
  {
    this.amountToAchieve = amountToAchieve;
  }

  public BigDecimal getCalculatedIncremental()
  {
    return calculatedIncremental;
  }

  public void setCalculatedIncremental( BigDecimal calculatedIncremental )
  {
    this.calculatedIncremental = calculatedIncremental;
  }

  public User getPartnerToParticipant()
  {
    return partnerToParticipant;
  }

  public void setPartnerToParticipant( User partnerToParticipant )
  {
    this.partnerToParticipant = partnerToParticipant;
  }

  public boolean isPartner()
  {
    return partner;
  }

  public void setPartner( boolean partner )
  {
    this.partner = partner;
  }

  public boolean isPartnersParticipantAchieved()
  {
    return partnersParticipantAchieved;
  }

  public void setPartnersParticipantAchieved( boolean partnersParticipantAchieved )
  {
    this.partnersParticipantAchieved = partnersParticipantAchieved;
  }

  public boolean isLeveloneAward()
  {
    return leveloneAward;
  }

  public void setLeveloneAward( boolean leveloneAward )
  {
    this.leveloneAward = leveloneAward;
  }

  public int getTotalAchieved()
  {
    return totalAchieved;
  }

  public void setTotalAchieved( int totalAchieved )
  {
    this.totalAchieved = totalAchieved;
  }

  public void incrementTotalAchieved()
  {
    totalAchieved++;
  }

  public void incrementTotalAchieved( int value )
  {
    totalAchieved += value;
  }

  public BigDecimal getTotalAward()
  {
    return totalAward;
  }

  public void setTotalAward( BigDecimal totalAward )
  {
    this.totalAward = totalAward;
  }

  public boolean isOwner()
  {
    return owner;
  }

  public void setOwner( boolean owner )
  {
    this.owner = owner;
  }

  public BigDecimal getPercentAchieved()
  {
    return percentAchieved;
  }

  public void setPercentAchieved( BigDecimal percentAchieved )
  {
    this.percentAchieved = percentAchieved;
  }

}

/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/ChallengePointCalculationResult.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.io.Serializable;
import java.math.BigDecimal;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.user.User;

/**
 * ChallengePointCalculationResult contains the results of a ChallengePointPayoutStrategy
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
 * <td>Babu</td>
 * <td>Aug 6, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengePointCalculationResult implements Serializable
{
  private static final long serialVersionUID = 1L;
  /* User of receiver */
  private User reciever;
  /* A boolean indicating of the ChallengePoint level was achieved */
  private boolean achieved = false;
  /* The calculated payout amount for this ChallengePoint level */
  private BigDecimal calculatedAchievement;
  /* the GoalLevel for this calculation */
  private GoalLevel goalLevel;

  /*
   * Contains the total performance to date. If this is a manager it will be filled accordingly: Amt
   * per achiever - # of employees that have achieved. % team achieved - Actual Percent Level Payout
   * - # of achievers % of team earnings - total team earnings
   */
  private BigDecimal totalPerformance;
  /* Contains base objective if set */
  private BigDecimal baseObjective;
  /* A boolean indicating if the user is a Manager */
  private boolean manager;
  /* Role type for the node */
  private HierarchyRoleType nodeRole;
  /* Owner of the participants node - this is the owner credited with manager override */
  private User nodeOwner;
  /* A BigDecimal indication calculated goal amount to achieve */
  private BigDecimal amountToAchieve;

  private BigDecimal calculatedIncremental;

  private boolean partner;

  private User partnerToParticipant;

  private boolean partnersParticipantAchieved;

  public void incrementCalculatedPayout( BigDecimal incrementAward )
  {
    if ( this.calculatedAchievement == null )
    {
      this.calculatedAchievement = incrementAward;
    }
    else
    {
      this.calculatedAchievement = this.calculatedAchievement.add( incrementAward );
    }
  }

  public BigDecimal getPercentageAchieved()
  {
    BigDecimal percentageAchieved = null;
    if ( goalLevel != null )
    {
      ChallengePointPromotion ChallengePointQuestPromotion = (ChallengePointPromotion)goalLevel.getPromotion();
      int decimalPlaces = 0;
      if ( ChallengePointQuestPromotion.getAchievementPrecision() != null )
      {
        decimalPlaces = ChallengePointQuestPromotion.getAchievementPrecision().getPrecision();
      }
      if ( getTotalPerformance() != null && getAmountToAchieve() != null && getAmountToAchieve().longValue() > 0 )
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

  public BigDecimal getCalculatedAchievement()
  {
    return calculatedAchievement;
  }

  public void setCalculatedAchievement( BigDecimal calculatedAchievement )
  {
    this.calculatedAchievement = calculatedAchievement;
  }

  public GoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( GoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public User getReciever()
  {
    return reciever;
  }

  public void setReciever( User reciever )
  {
    this.reciever = reciever;
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

  public boolean isPartner()
  {
    return partner;
  }

  public void setPartner( boolean partner )
  {
    this.partner = partner;
  }

  public User getPartnerToParticipant()
  {
    return partnerToParticipant;
  }

  public void setPartnerToParticipant( User partnerToParticipant )
  {
    this.partnerToParticipant = partnerToParticipant;
  }

  public boolean isPartnersParticipantAchieved()
  {
    return partnersParticipantAchieved;
  }

  public void setPartnersParticipantAchieved( boolean partnersParticipantAchieved )
  {
    this.partnersParticipantAchieved = partnersParticipantAchieved;
  }

}

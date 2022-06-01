
package com.biperf.core.value;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.user.User;

/**
 * ChallengepointAward domain object.
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
 * <td>Prabu Babu</td>
 * <td>Jul 16, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * 
 */
public class ChallengepointPaxAwardValueBean extends BaseDomain implements Cloneable
{
  /**
   * The participant for whom the cp is set for.
   */
  private Participant participant;

  /**
   * The challengepoint promotion for which the cp is set for.
   */
  private ChallengePointPromotion challengePointPromotion;

  /**
   * The Result calculated at the time of issuance.
   */
  private BigDecimal result;

  /*
   * Basic Award earned
   */
  private Long awardEarned;

  /*
   * Award Issued
   */
  private Long awardIssued;

  /*
   * Total Award with Challengepoint earned
   */
  private Long totalAwardIssued;

  // deposited
  private Long interimAward;

  /*
   * Total Award with Challengepoint earned
   */
  private Long totalBasicAwardIssued;
  // this is need for final award calculation email
  private Long emailTotalAwardIssued;

  /*
   * award Type challengepoint/basic
   */
  private String awardType;

  private Long nodeId;

  private boolean achieved;
  /**
   * The performance amount used to calculate challengepoint.
   */
  private BigDecimal baseQuantity;

  private PaxGoal paxGoal;

  private BigDecimal amountToAchieve;

  /* Fields used for extract */
  private Long extractBasicAwardEarned;
  private Long extractBasicAwardPending;
  private Long extractBasicAwardDeposited;
  private Long extractCPAwardDeposited;
  private User goalSelecter;

  // calculatedAchievement is payout amount
  private BigDecimal calculatedAchievement;

  /* A boolean indicating that by default leveloneAward is true and levetwoAward is false */
  private boolean leveloneAward = false;

  private int totalAchieved = 0;
  private BigDecimal totalAward;
  private Long totalSelected = new Long( 0 );
  private boolean partnerTotals;
  private GoalLevel goalLevel;
  private boolean partner;
  private BigDecimal percentAchieved;

  public ChallengepointPaxAwardValueBean()
  {
    super();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof ChallengepointPaxAwardValueBean ) )
    {
      return false;
    }

    ChallengepointPaxAwardValueBean otherChallengepointAward = (ChallengepointPaxAwardValueBean)object;

    if ( otherChallengepointAward.getChallengePointPromotion() != null && !otherChallengepointAward.getChallengePointPromotion().equals( this.getChallengePointPromotion() ) )
    {

      equals = false;
    }

    if ( otherChallengepointAward.getParticipant() != null && !otherChallengepointAward.getParticipant().equals( this.getParticipant() ) )
    {

      equals = false;
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int resultHS;
    resultHS = this.getChallengePointPromotion() != null ? this.getChallengePointPromotion().hashCode() : 0;
    resultHS = 29 * resultHS + ( getParticipant() != null ? getParticipant().hashCode() : 0 );
    resultHS = 29 * resultHS + ( getTotalAwardIssued() != null ? getTotalAwardIssued().hashCode() : 0 );

    return resultHS;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public ChallengePointPromotion getChallengePointPromotion()
  {
    return challengePointPromotion;
  }

  public void setChallengePointPromotion( ChallengePointPromotion challengePointPromotion )
  {
    this.challengePointPromotion = challengePointPromotion;
  }

  public Long getAwardEarned()
  {
    return awardEarned;
  }

  public void setAwardEarned( Long awardEarned )
  {
    this.awardEarned = awardEarned;
  }

  public Long getAwardIssued()
  {
    return awardIssued;
  }

  public void setAwardIssued( Long awardIssued )
  {
    this.awardIssued = awardIssued;
  }

  public BigDecimal getResult()
  {
    return result;
  }

  public void setResult( BigDecimal result )
  {
    this.result = result;
  }

  public Long getTotalAwardIssued()
  {
    return totalAwardIssued;
  }

  public void setTotalAwardIssued( Long totalAwardIssued )
  {
    this.totalAwardIssued = totalAwardIssued;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public boolean isAchieved()
  {
    return achieved;
  }

  public void setAchieved( boolean achieved )
  {
    this.achieved = achieved;
  }

  public BigDecimal getBaseQuantity()
  {
    return baseQuantity;
  }

  public void setBaseQuantity( BigDecimal baseQuantity )
  {
    this.baseQuantity = baseQuantity;
  }

  public PaxGoal getPaxGoal()
  {
    return paxGoal;
  }

  public void setPaxGoal( PaxGoal paxGoal )
  {
    this.paxGoal = paxGoal;
  }

  public BigDecimal getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( BigDecimal calculatedAchievementAmount )
  {
    this.amountToAchieve = calculatedAchievementAmount;
  }

  public Long getExtractBasicAwardDeposited()
  {
    return extractBasicAwardDeposited;
  }

  public void setExtractBasicAwardDeposited( Long extractBasicAwardDeposited )
  {
    this.extractBasicAwardDeposited = extractBasicAwardDeposited;
  }

  public Long getExtractBasicAwardEarned()
  {
    return extractBasicAwardEarned;
  }

  public void setExtractBasicAwardEarned( Long extractBasicAwardEarned )
  {
    this.extractBasicAwardEarned = extractBasicAwardEarned;
  }

  public Long getExtractBasicAwardPending()
  {
    return extractBasicAwardPending;
  }

  public void setExtractBasicAwardPending( Long extractBasicAwardPending )
  {
    this.extractBasicAwardPending = extractBasicAwardPending;
  }

  public Long getExtractCPAwardDeposited()
  {
    return extractCPAwardDeposited;
  }

  public void setExtractCPAwardDeposited( Long extractCPAwardDeposited )
  {
    this.extractCPAwardDeposited = extractCPAwardDeposited;
  }

  public BigDecimal getCalculatedAchievement()
  {
    return calculatedAchievement;
  }

  public void setCalculatedAchievement( BigDecimal calculatedAchievement )
  {
    this.calculatedAchievement = calculatedAchievement;
  }

  public Long getEmailTotalAwardIssued()
  {
    return emailTotalAwardIssued;
  }

  public void setEmailTotalAwardIssued( Long emailTotalAwardIssued )
  {
    this.emailTotalAwardIssued = emailTotalAwardIssued;
  }

  public Long getTotalBasicAwardIssued()
  {
    return totalBasicAwardIssued;
  }

  public void setTotalBasicAwardIssued( Long totalBasicAwardIssued )
  {
    this.totalBasicAwardIssued = totalBasicAwardIssued;
  }

  public Long getInterimAward()
  {
    return interimAward;
  }

  public void setInterimAward( Long interimAward )
  {
    this.interimAward = interimAward;
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

  public BigDecimal getTotalAward()
  {
    return totalAward;
  }

  public void setTotalAward( BigDecimal totalAward )
  {
    this.totalAward = totalAward;
  }

  public Long getTotalSelected()
  {
    return totalSelected;
  }

  public void setTotalSelected( Long totalSelected )
  {
    this.totalSelected = totalSelected;
  }

  public boolean isPartnerTotals()
  {
    return partnerTotals;
  }

  public void setPartnerTotals( boolean partnerTotals )
  {
    this.partnerTotals = partnerTotals;
  }

  public GoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( GoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public boolean isPartner()
  {
    return partner;
  }

  public void setPartner( boolean partner )
  {
    this.partner = partner;
  }

  public User getGoalSelecter()
  {
    return goalSelecter;
  }

  public void setGoalSelecter( User goalSelecter )
  {
    this.goalSelecter = goalSelecter;
  }

  public void incrementTotalAchieved()
  {
    totalAchieved++;
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

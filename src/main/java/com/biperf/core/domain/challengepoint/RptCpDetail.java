
package com.biperf.core.domain.challengepoint;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;

/**
 * RptCpDetail.
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
 * <td>reddy</td>
 * <td>Aug 29, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class RptCpDetail extends BaseDomain implements Cloneable
{
  private static final long serialVersionUID = 1L;
  private Participant participant;
  private ChallengePointPromotion challengePointPromotion;
  private PaxGoal paxGoal;

  private String lastName;
  private String firstName;
  private String middleInit;
  private String jobPosition;
  private String department;

  private Long nodeId;
  private BigDecimal currentValue;
  private BigDecimal baseQuantity;
  private BigDecimal amountToAchieve;
  private BigDecimal percentOfCp;
  private Boolean achieved;
  private BigDecimal calculatedPayout;
  private BigDecimal calculatedThreshold;
  private BigDecimal basicAwardsEarned;
  private BigDecimal basicAwardsDeposited;
  private Boolean thresholdReached;
  private BigDecimal incrementQuantity;
  private Boolean manager;
  private GoalLevel goalLevel;

  public Boolean getManager()
  {
    return manager;
  }

  public void setManager( Boolean manager )
  {
    this.manager = manager;
  }

  public BigDecimal getIncrementQuantity()
  {
    return incrementQuantity;
  }

  public void setIncrementQuantity( BigDecimal incrementQuantity )
  {
    this.incrementQuantity = incrementQuantity;
  }

  public BigDecimal getBasicAwardsDeposited()
  {
    return basicAwardsDeposited;
  }

  public void setBasicAwardsDeposited( BigDecimal basicAwardsDeposited )
  {
    this.basicAwardsDeposited = basicAwardsDeposited;
  }

  public BigDecimal getBasicAwardsEarned()
  {
    return basicAwardsEarned;
  }

  public void setBasicAwardsEarned( BigDecimal basicAwardsEarned )
  {
    this.basicAwardsEarned = basicAwardsEarned;
  }

  public RptCpDetail()
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

    if ( ! ( object instanceof RptCpDetail ) )
    {
      return false;
    }

    RptCpDetail otherRptCpDetial = (RptCpDetail)object;

    if ( otherRptCpDetial.getChallengePointPromotion() != null && !otherRptCpDetial.getChallengePointPromotion().equals( this.getChallengePointPromotion() ) )
    {
      equals = false;
    }

    if ( otherRptCpDetial.getParticipant() != null && !otherRptCpDetial.getParticipant().equals( this.getParticipant() ) )
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
    int result;
    result = this.getChallengePointPromotion() != null ? this.getChallengePointPromotion().hashCode() : 0;
    result = 29 * result + ( getParticipant() != null ? getParticipant().hashCode() : 0 );

    return result;
  }

  public BigDecimal getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( BigDecimal amountToAchieve )
  {
    this.amountToAchieve = amountToAchieve;
  }

  public BigDecimal getBaseQuantity()
  {
    return baseQuantity;
  }

  public void setBaseQuantity( BigDecimal baseQuantity )
  {
    this.baseQuantity = baseQuantity;
  }

  public BigDecimal getCalculatedPayout()
  {
    return calculatedPayout;
  }

  public void setCalculatedPayout( BigDecimal calculatedPayout )
  {
    this.calculatedPayout = calculatedPayout;
  }

  public BigDecimal getCurrentValue()
  {
    return currentValue;
  }

  public void setCurrentValue( BigDecimal currentValue )
  {
    this.currentValue = currentValue;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getMiddleInit()
  {
    return middleInit;
  }

  public void setMiddleInit( String middleInit )
  {
    this.middleInit = middleInit;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Boolean getAchieved()
  {
    return achieved;
  }

  public void setAchieved( Boolean achieved )
  {
    this.achieved = achieved;
  }

  public ChallengePointPromotion getChallengePointPromotion()
  {
    return challengePointPromotion;
  }

  public void setChallengePointPromotion( ChallengePointPromotion challengePointPromotion )
  {
    this.challengePointPromotion = challengePointPromotion;
  }

  public PaxGoal getPaxGoal()
  {
    return paxGoal;
  }

  public void setPaxGoal( PaxGoal paxGoal )
  {
    this.paxGoal = paxGoal;
  }

  public BigDecimal getPercentOfCp()
  {
    return percentOfCp;
  }

  public void setPercentOfCp( BigDecimal percentOfCp )
  {
    this.percentOfCp = percentOfCp;
  }

  public Boolean getThresholdReached()
  {
    return thresholdReached;
  }

  public void setThresholdReached( Boolean thresholdReached )
  {
    this.thresholdReached = thresholdReached;
  }

  public GoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( GoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public BigDecimal getCalculatedThreshold()
  {
    return calculatedThreshold;
  }

  public void setCalculatedThreshold( BigDecimal calculatedThreshold )
  {
    this.calculatedThreshold = calculatedThreshold;
  }

}


package com.biperf.core.domain.goalquest;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;

/**
 * RptGoalDetail domain object.
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
 * <td>Tammy Cheng</td>
 * <td>Dec 28, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RptGoalDetail extends BaseDomain
{
  private Participant participant;
  private GoalQuestPromotion goalQuestPromotion;
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
  private BigDecimal percentOfGoal;
  private Boolean achieved;
  private BigDecimal calculatedPayout;
  private Boolean manager;

  public RptGoalDetail()
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

    if ( ! ( object instanceof RptGoalDetail ) )
    {
      return false;
    }

    RptGoalDetail otherRptGoalDetial = (RptGoalDetail)object;

    if ( otherRptGoalDetial.getGoalQuestPromotion() != null && !otherRptGoalDetial.getGoalQuestPromotion().equals( this.getGoalQuestPromotion() ) )
    {
      equals = false;
    }

    if ( otherRptGoalDetial.getParticipant() != null && !otherRptGoalDetial.getParticipant().equals( this.getParticipant() ) )
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
    result = this.getGoalQuestPromotion() != null ? this.getGoalQuestPromotion().hashCode() : 0;
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

  public GoalQuestPromotion getGoalQuestPromotion()
  {
    return goalQuestPromotion;
  }

  public void setGoalQuestPromotion( GoalQuestPromotion goalQuestPromotion )
  {
    this.goalQuestPromotion = goalQuestPromotion;
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

  public BigDecimal getPercentOfGoal()
  {
    return percentOfGoal;
  }

  public void setPercentOfGoal( BigDecimal percentOfGoal )
  {
    this.percentOfGoal = percentOfGoal;
  }

  public PaxGoal getPaxGoal()
  {
    return paxGoal;
  }

  public void setPaxGoal( PaxGoal paxGoal )
  {
    this.paxGoal = paxGoal;
  }

  public Boolean getAchieved()
  {
    return achieved;
  }

  public void setAchieved( Boolean achieved )
  {
    this.achieved = achieved;
  }

  public Boolean getManager()
  {
    return manager;
  }

  public void setManager( Boolean manager )
  {
    this.manager = manager;
  }

}


package com.biperf.core.domain.goalquest;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;

/**
 * RptGoalPartnerDetail domain object.
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
 * <td>gadapa</td>
 * <td>Apr 10, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RptGoalPartnerDetail extends BaseDomain
{
  private Participant participant;
  private GoalQuestPromotion goalQuestPromotion;
  private Participant partner;

  private BigDecimal currentValue;
  private BigDecimal baseQuantity;
  private BigDecimal amountToAchieve;
  private BigDecimal percentOfGoal;
  private Boolean achieved;
  private BigDecimal calculatedPayout;

  public RptGoalPartnerDetail()
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

    if ( ! ( object instanceof RptGoalPartnerDetail ) )
    {
      return false;
    }

    RptGoalPartnerDetail otherPartnerDetail = (RptGoalPartnerDetail)object;

    if ( otherPartnerDetail.getGoalQuestPromotion() != null && !otherPartnerDetail.getGoalQuestPromotion().equals( this.getGoalQuestPromotion() ) )
    {
      equals = false;
    }

    if ( otherPartnerDetail.getParticipant() != null && !otherPartnerDetail.getParticipant().equals( this.getParticipant() ) )
    {
      equals = false;
    }

    if ( otherPartnerDetail.getPartner() != null && !otherPartnerDetail.getPartner().equals( this.getPartner() ) )
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
    result = 29 * result + ( getPartner() != null ? getPartner().hashCode() : 0 );

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

  public GoalQuestPromotion getGoalQuestPromotion()
  {
    return goalQuestPromotion;
  }

  public void setGoalQuestPromotion( GoalQuestPromotion goalQuestPromotion )
  {
    this.goalQuestPromotion = goalQuestPromotion;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Participant getPartner()
  {
    return partner;
  }

  public void setPartner( Participant partner )
  {
    this.partner = partner;
  }

  public BigDecimal getPercentOfGoal()
  {
    return percentOfGoal;
  }

  public void setPercentOfGoal( BigDecimal percentOfGoal )
  {
    this.percentOfGoal = percentOfGoal;
  }

  public Boolean getAchieved()
  {
    return achieved;
  }

  public void setAchieved( Boolean achieved )
  {
    this.achieved = achieved;
  }

}

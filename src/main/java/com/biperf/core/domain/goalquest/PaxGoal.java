
package com.biperf.core.domain.goalquest;

import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;

/**
 * PaxGoal domain object.
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
public class PaxGoal extends BaseDomain implements Cloneable
{
  private static final long serialVersionUID = 1L;

  /**
   * The participant for whom the goal is set for.
   */
  private Participant participant;

  /**
   * The goal quest promotion for which the goal is set for.
   */
  private GoalQuestPromotion goalQuestPromotion;

  /**
   * The specific goal level the participant is measured by.
   */
  private AbstractGoalLevel goalLevel;

  /**
   * The up-todate performance amount the participant maintains towards acheiving the goalquest goal.
   */
  private BigDecimal currentValue;

  /**
   * The discretionary make-up amount used in conjuction with the currentValue to calculate whether a pax has achieved his/her goal.
   */
  private BigDecimal overrideQuantity;

  /**
   * The performance amount used to calculate goal on a goal structure of % of base goal quest promotion.
   */
  private BigDecimal baseQuantity;

  /**
   * For merch and travel this is the catalog id of the selected award.
   */
  private String catalogId;

  /**
   * For merch and travel this is the product set id of the selected award.
   */
  private String productSetId;

  private String selectedProductName;

  /**
   * Issue Date of Current Amount
   */
  private Date submissionDate;

  public PaxGoal()
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

    if ( ! ( object instanceof PaxGoal ) )
    {
      return false;
    }

    PaxGoal otherPaxGoal = (PaxGoal)object;

    if ( otherPaxGoal.getGoalQuestPromotion() != null && !otherPaxGoal.getGoalQuestPromotion().equals( this.getGoalQuestPromotion() ) )
    {
      equals = false;
    }

    if ( otherPaxGoal.getParticipant() != null && !otherPaxGoal.getParticipant().equals( this.getParticipant() ) )
    {
      equals = false;
    }

    if ( otherPaxGoal.getGoalLevel() != null && !otherPaxGoal.getGoalLevel().equals( this.getGoalLevel() ) )
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
    result = 29 * result + ( getGoalLevel() != null ? getGoalLevel().hashCode() : 0 );

    return result;
  }

  public BigDecimal getCurrentValue()
  {
    return currentValue;
  }

  public void setCurrentValue( BigDecimal currentValue )
  {
    this.currentValue = currentValue;
  }

  public AbstractGoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( AbstractGoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public GoalQuestPromotion getGoalQuestPromotion()
  {
    return goalQuestPromotion;
  }

  public void setGoalQuestPromotion( GoalQuestPromotion goalQuestPromotion )
  {
    this.goalQuestPromotion = goalQuestPromotion;
  }

  public BigDecimal getBaseQuantity()
  {
    return baseQuantity;
  }

  public void setBaseQuantity( BigDecimal baseQuantity )
  {
    this.baseQuantity = baseQuantity;
  }

  public BigDecimal getAchievementAmount()
  {
    return ( (GoalLevel)goalLevel ).getAchievementAmount();
  }

  public BigDecimal getAward()
  {
    return ( (GoalLevel)goalLevel ).getAward();
  }

  public BigDecimal getOverrideQuantity()
  {
    return overrideQuantity;
  }

  public void setOverrideQuantity( BigDecimal overrideQuantity )
  {
    this.overrideQuantity = overrideQuantity;
  }

  public boolean isManagerOverrideGoal()
  {
    if ( getGoalLevel() != null && getGoalLevel().isManagerOverrideGoalLevel() )
    {
      return true;
    }
    return false;
  }

  public String getCatalogId()
  {
    return catalogId;
  }

  public void setCatalogId( String catalogId )
  {
    this.catalogId = catalogId;
  }

  public String getProductSetId()
  {
    return productSetId;
  }

  public void setProductSetId( String productSetId )
  {
    this.productSetId = productSetId;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date issueDate )
  {
    this.submissionDate = issueDate;
  }

  public String getSelectedProductName()
  {
    return selectedProductName;
  }

  public void setSelectedProductName( String selectedProductName )
  {
    this.selectedProductName = selectedProductName;
  }

  public String getAwardLocaleBased()
  {
    if ( ( (GoalLevel)goalLevel ).getAward() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( ( (GoalLevel)goalLevel ).getAward(), 0, UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getBaseQuantityLocaleBased()
  {
    if ( this.getBaseQuantity() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getBaseQuantity(), goalQuestPromotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getCurrentValueLocaleBased()
  {
    if ( this.getCurrentValue() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getCurrentValue(), goalQuestPromotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }
}

/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/calculator/ViewCalculatorForm.java,v $
 */

package com.biperf.core.ui.calculator;

import java.util.List;
import java.util.Set;

import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.enums.CalculatorStatusType;

/**
 * ViewCalculatorForm.
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
 * <td>May 24, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ViewCalculatorForm extends CalculatorForm
{
  private String calculatorCriterionId;
  private String calculatorPayoutId;
  private String newCriterionSequenceNum;
  private List criterion;
  private Set payouts;
  private String[] criterionDeleteIds;
  private String[] payoutDeleteIds;

  /**
   * Load the form
   * 
   * @param calculator
   */
  public void load( Calculator calculator )
  {
    super.load( calculator );
    this.criterion = calculator.getCalculatorCriterion();
    this.payouts = calculator.getCalculatorPayouts();
  }

  public String getCalculatorCriterionId()
  {
    return calculatorCriterionId;
  }

  public void setCalculatorCriterionId( String calculatorCriterionId )
  {
    this.calculatorCriterionId = calculatorCriterionId;
  }

  public String getCalculatorPayoutId()
  {
    return calculatorPayoutId;
  }

  public void setCalculatorPayoutId( String calculatorPayoutId )
  {
    this.calculatorPayoutId = calculatorPayoutId;
  }

  public List getCriterion()
  {
    return criterion;
  }

  public void setCriterion( List criterion )
  {
    this.criterion = criterion;
  }

  public int getCriterionSize()
  {
    int size = 0;
    if ( criterion != null )
    {
      size = criterion.size();
    }
    return size;
  }

  public String[] getCriterionDeleteIds()
  {
    return criterionDeleteIds;
  }

  public void setCriterionDeleteIds( String[] criterionDeleteIds )
  {
    this.criterionDeleteIds = criterionDeleteIds;
  }

  public String getNewCriterionSequenceNum()
  {
    return newCriterionSequenceNum;
  }

  public void setNewCriterionSequenceNum( String newCriterionSequenceNum )
  {
    this.newCriterionSequenceNum = newCriterionSequenceNum;
  }

  public String[] getPayoutDeleteIds()
  {
    return payoutDeleteIds;
  }

  public void setPayoutDeleteIds( String[] payoutDeleteIds )
  {
    this.payoutDeleteIds = payoutDeleteIds;
  }

  public Set getPayouts()
  {
    return payouts;
  }

  public void setPayouts( Set payouts )
  {
    this.payouts = payouts;
  }

  /**
   * Checks if the calculator is at a status that can be edited.
   * 
   * @return boolean - Returns true if the calculator is "Under Construction" or "Completed" phase, and
   *         false otherwise.
   */
  public boolean isEditable()
  {
    // first make sure there is a status type available to check
    if ( getCalculatorStatus() == null )
    {
      return false;
    }

    if ( getCalculatorStatus().equals( CalculatorStatusType.UNDER_CONSTRUCTION ) || getCalculatorStatus().equals( CalculatorStatusType.COMPLETED ) )
    {
      return true;
    }

    return false;
  }

}

/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/calculator/CalculatorCriterionForm.java,v $
 */

package com.biperf.core.ui.calculator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.biperf.core.domain.enums.StatusType;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * CalculatorCriterionForm.
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
public class CalculatorCriterionForm extends BaseForm
{
  private String method;
  private Long calculatorId;
  private String calculatorName;
  private boolean calculatorAwardFixed;
  private Long calculatorCriterionId;
  private String cmAssetName;
  private String criterionText;
  private boolean weightedScore;
  private int weightValue;
  private String criterionStatus;
  private String criterionStatusText;
  private String[] deletedRatings;
  private String newRatingSequenceNum;
  private Long criterionRatingId;
  private List ratings;
  private String calculatorStatus;

  /**
   * Load the form
   * 
   * @param criterion
   */
  public void load( CalculatorCriterion criterion )
  {
    this.calculatorId = criterion.getCalculator().getId();
    this.calculatorName = criterion.getCalculator().getName();
    this.calculatorStatus = criterion.getCalculator().getCalculatorStatusType().getCode();
    this.calculatorCriterionId = criterion.getId();
    this.criterionText = StringUtil.convertLineBreaks( criterion.getCriterionText() );
    this.cmAssetName = criterion.getCmAssetName();
    this.weightedScore = criterion.getCalculator().isWeightedScore();
    this.weightValue = criterion.getWeightValue();
    this.criterionStatus = criterion.getCriterionStatus().getCode();
    this.criterionStatusText = criterion.getCriterionStatus().getName();
    this.ratings = criterion.getCriterionRatings();
  }

  /**
   * Creates a detatched CalculatorCriterion Domain Object that will later be synchronized with a
   * looked up promotion object in the service
   * 
   * @return CalculatorCriteion
   */
  public CalculatorCriterion toDomainObject()
  {
    CalculatorCriterion criterion = new CalculatorCriterion();
    criterion.setId( this.calculatorCriterionId );
    criterion.setCmAssetName( this.cmAssetName );
    criterion.setCriterionStatus( StatusType.lookup( this.criterionStatus ) );
    criterion.setWeightValue( this.weightValue );

    return criterion;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( this.criterionText == null || this.criterionText.equals( "" ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "calculator.criterion.CRITERION" ) ) );
    }
    if ( this.weightedScore )
    {
      if ( this.weightValue > 0 )
      {
        if ( this.weightValue > 100 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.INVALID_WEIGHT" ) );
        }
      }
      else
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "calculator.criterion.WEIGHT" ) ) );
      }
    }

    return actionErrors;
  }

  // getters and setters
  public boolean isCalculatorAwardFixed()
  {
    return calculatorAwardFixed;
  }

  public void setCalculatorAwardFixed( boolean calculatorAwardFixed )
  {
    this.calculatorAwardFixed = calculatorAwardFixed;
  }

  public Long getCalculatorCriterionId()
  {
    return calculatorCriterionId;
  }

  public void setCalculatorCriterionId( Long calculatorCriterionId )
  {
    this.calculatorCriterionId = calculatorCriterionId;
  }

  public Long getCalculatorId()
  {
    return calculatorId;
  }

  public void setCalculatorId( Long calculatorId )
  {
    this.calculatorId = calculatorId;
  }

  public String getCalculatorName()
  {
    return calculatorName;
  }

  public void setCalculatorName( String calculatorName )
  {
    this.calculatorName = calculatorName;
  }

  public String getCmAssetName()
  {
    return cmAssetName;
  }

  public void setCmAssetName( String cmAssetName )
  {
    this.cmAssetName = cmAssetName;
  }

  public Long getCriterionRatingId()
  {
    return criterionRatingId;
  }

  public void setCriterionRatingId( Long criterionRatingId )
  {
    this.criterionRatingId = criterionRatingId;
  }

  public String getCriterionStatus()
  {
    return criterionStatus;
  }

  public void setCriterionStatus( String criterionStatus )
  {
    this.criterionStatus = criterionStatus;
  }

  public String getCriterionStatusText()
  {
    return criterionStatusText;
  }

  public void setCriterionStatusText( String criterionStatusText )
  {
    this.criterionStatusText = criterionStatusText;
  }

  public String getCriterionText()
  {
    return criterionText;
  }

  public void setCriterionText( String criterionText )
  {
    this.criterionText = criterionText;
  }

  public String[] getDeletedRatings()
  {
    return deletedRatings;
  }

  public void setDeletedRatings( String[] deletedRatings )
  {
    this.deletedRatings = deletedRatings;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getNewRatingSequenceNum()
  {
    return newRatingSequenceNum;
  }

  public void setNewRatingSequenceNum( String newRatingSequenceNum )
  {
    this.newRatingSequenceNum = newRatingSequenceNum;
  }

  public List getRatings()
  {
    return ratings;
  }

  public void setRatings( List ratings )
  {
    this.ratings = ratings;
  }

  public int getRatingsSize()
  {
    int size = 0;
    if ( ratings != null )
    {
      size = ratings.size();
    }
    return size;
  }

  public int getWeightValue()
  {
    return weightValue;
  }

  public void setWeightValue( int weightValue )
  {
    this.weightValue = weightValue;
  }

  public boolean isWeightedScore()
  {
    return weightedScore;
  }

  public void setWeightedScore( boolean weightedScore )
  {
    this.weightedScore = weightedScore;
  }

  public String getCalculatorStatus()
  {
    return calculatorStatus;
  }

  public void setCalculatorStatus( String calculatorStatus )
  {
    this.calculatorStatus = calculatorStatus;
  }

  /**
   * Checks if the calculator is at a status that can be edited.
   * 
   * @return boolean - Returns true if the calculator is "Under Construction" or "Completed" phase, and
   *         false otherwise.
   */
  public boolean isEditable()
  {
    if ( getCmAssetName() == null || getCmAssetName().equals( "" ) )
    {
      return true;
    }

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

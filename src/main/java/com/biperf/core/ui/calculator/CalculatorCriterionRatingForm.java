/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/calculator/CalculatorCriterionRatingForm.java,v $
 */

package com.biperf.core.ui.calculator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.calculator.CalculatorCriterionRating;
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
 * <td>attada</td>
 * <td>May 29, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorCriterionRatingForm extends BaseForm
{
  private static final String RATING_LABEL_ASSET_KEY = "calculator.rating.RATING_LABEL";
  private static final String RATING_VALUE_ASSET_KEY = "calculator.rating.VALUE";
  private String method;
  private Long calculatorId;
  private String calculatorName;
  private Long calculatorCriterionId;
  private String criterionText;
  private boolean weightedScore;
  private int weightValue;
  private String criterionStatus;
  private Long criterionRatingId;
  private String cmAssetName = "";
  private String ratingLabel = "";
  private int ratingValue;

  /**
   * Load the form
   * 
   * @param criterionRating
   */
  public void load( CalculatorCriterionRating criterionRating )
  {
    this.calculatorId = criterionRating.getCalculatorCriterion().getCalculator().getId();
    this.calculatorName = criterionRating.getCalculatorCriterion().getCalculator().getName();
    this.calculatorCriterionId = criterionRating.getCalculatorCriterion().getId();
    this.criterionText = StringUtil.convertLineBreaks( criterionRating.getCalculatorCriterion().getCriterionText() );
    this.cmAssetName = criterionRating.getCmAssetName();
    this.weightedScore = criterionRating.getCalculatorCriterion().getCalculator().isWeightedScore();
    this.weightValue = criterionRating.getCalculatorCriterion().getWeightValue();
    this.criterionStatus = criterionRating.getCalculatorCriterion().getCriterionStatus().getName();
    this.ratingLabel = criterionRating.getRatingText();
    this.ratingValue = criterionRating.getRatingValue();
  }

  /**
   * Creates a detatched CalculatorCriterion Domain Object that will later be synchronized with a
   * looked up promotion object in the service
   * 
   * @return CalculatorCriterionRating
   */
  public CalculatorCriterionRating toDomainObject()
  {
    CalculatorCriterionRating criterionRating = new CalculatorCriterionRating();
    criterionRating.setId( this.getCriterionRatingId() );
    criterionRating.setRatingValue( this.getRatingValue() );
    criterionRating.setCmAssetName( this.getCmAssetName() );

    return criterionRating;
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
    if ( this.ratingLabel == null || this.ratingLabel.equals( "" ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( RATING_LABEL_ASSET_KEY ) ) );
    }

    if ( this.ratingValue < 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( RATING_VALUE_ASSET_KEY ) ) );
    }

    return actionErrors;
  }

  // getters and setters

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

  public String getCriterionText()
  {
    return criterionText;
  }

  public void setCriterionText( String criterionText )
  {
    this.criterionText = criterionText;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
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

  public int getRatingValue()
  {
    return ratingValue;
  }

  public void setRatingValue( int ratingValue )
  {
    this.ratingValue = ratingValue;
  }

  public String getRatingLabel()
  {
    return ratingLabel;
  }

  public void setRatingLabel( String ratingLabel )
  {
    this.ratingLabel = ratingLabel;
  }

}

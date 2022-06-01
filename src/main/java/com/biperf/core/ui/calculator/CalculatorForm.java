/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/calculator/CalculatorForm.java,v $
 */

package com.biperf.core.ui.calculator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.StringUtil;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * CalculatorLibraryForm.
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
 * <td>jenniget</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorForm extends BaseActionForm
{
  private static final String COPY_CALCULATOR_NAME_ASSET_KEY = "calculator.addoredit.NEW_CALCULATOR_NAME";
  private static final String CALCULATOR_NAME_ASSET_KEY = "calculator.addoredit.NAME";
  private static final String CALCULATOR_DISPLAY_WEIGHTS_ASSET_KEY = "calculator.addoredit.DISPLAY_WEIGHTS";
  private static final String CALCULATOR_DISPLAY_SCORES_ASSET_KEY = "calculator.addoredit.DISPLAY_SCORE";

  private String method;
  private String calculatorId;
  private Long version;
  private String calculatorName;
  private String copyCalculatorName;
  private String description;
  private boolean weightedScore;
  private boolean displayWeights;
  private boolean displayScore;
  private String calculatorAward;
  private String weightCMAssetName;
  private String scoreCMAssetName;
  private String calculatorStatus;

  private String weightLabel;
  private String scoreLabel;

  /**
   * Load the form
   * 
   * @param calculator
   */

  public void load( Calculator calculator )
  {
    if ( calculator != null )
    {
      this.calculatorId = calculator.getId().toString();
      this.version = calculator.getVersion();
      this.calculatorName = calculator.getName();
      this.calculatorStatus = calculator.getCalculatorStatusType().getCode();
      this.description = calculator.getDescription() == null ? null : convertLineBreaks( calculator.getDescription() );
      this.weightedScore = calculator.isWeightedScore();
      this.displayWeights = calculator.isDisplayWeights();
      this.displayScore = calculator.isDisplayScores();
      this.calculatorAward = calculator.getCalculatorAwardType() == null ? "" : calculator.getCalculatorAwardType().getCode();
      this.weightCMAssetName = calculator.getWeightCMAssetName();
      this.scoreCMAssetName = calculator.getScoreCMAssetName();

      if ( !StringUtil.isEmpty( calculator.getWeightCMAssetName() ) )
      {
        this.weightLabel = CmsResourceBundle.getCmsBundle().getString( calculator.getWeightCMAssetName(), Calculator.CM_CALC_WEIGHT_NAME_KEY );
      }
      if ( !StringUtil.isEmpty( calculator.getScoreCMAssetName() ) )
      {
        this.scoreLabel = CmsResourceBundle.getCmsBundle().getString( calculator.getScoreCMAssetName(), Calculator.CM_CALC_SCORE_NAME_KEY );
      }
    }
  }

  private String convertLineBreaks( String text )
  {
    return StringUtil.convertLineBreaks( text );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Calculator
   */
  public Calculator toDomainObject()
  {
    Calculator calculator = new Calculator();
    if ( this.calculatorId != null && !this.calculatorId.equals( "" ) )
    {
      calculator.setId( new Long( this.calculatorId ) );
    }
    calculator.setVersion( this.version );
    calculator.setName( this.calculatorName );
    calculator.setDescription( this.description );
    calculator.setCalculatorStatusType( CalculatorStatusType.lookup( this.calculatorStatus ) );
    calculator.setWeightedScore( this.weightedScore );
    calculator.setDisplayWeights( this.displayWeights );
    calculator.setDisplayScores( this.displayScore );
    calculator.setCalculatorAwardType( CalculatorAwardType.lookup( this.calculatorAward ) );
    calculator.setWeightCMAssetName( this.weightCMAssetName );
    calculator.setScoreCMAssetName( this.scoreCMAssetName );
    if ( this.calculatorId == null || this.calculatorId.equals( "" ) )
    {
      calculator.setCalculatorStatusType( CalculatorStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    }

    calculator.setScoreLabel( this.scoreLabel );
    calculator.setWeightLabel( this.weightLabel );

    return calculator;
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

    if ( method != null )
    {
      if ( method.equals( "copy" ) )
      {
        if ( StringUtils.isEmpty( copyCalculatorName ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( COPY_CALCULATOR_NAME_ASSET_KEY ) ) );
        }
      }
      else if ( method.equals( "save" ) )
      {
        if ( StringUtils.isEmpty( calculatorName ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( CALCULATOR_NAME_ASSET_KEY ) ) );
        }

        if ( displayWeights && StringUtils.isEmpty( weightLabel ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.addoredit.REQUIRED_WEIGHT_LABEL" ) );
        }
        if ( displayScore && StringUtils.isEmpty( scoreLabel ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.addoredit.REQUIRED_SCORE_LABEL" ) );
        }
        if ( calculatorAward == null )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.REQUIRED_AWARD_TYPE" ) );
        }

      }
    }

    return actionErrors;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getCalculatorAward()
  {
    return calculatorAward;
  }

  public void setCalculatorAward( String calculatorAward )
  {
    this.calculatorAward = calculatorAward;
  }

  public boolean isDisplayScore()
  {
    return displayScore;
  }

  public void setDisplayScore( boolean displayScore )
  {
    this.displayScore = displayScore;
  }

  public boolean isDisplayWeights()
  {
    return displayWeights;
  }

  public void setDisplayWeights( boolean displayWeights )
  {
    this.displayWeights = displayWeights;
  }

  public boolean isWeightedScore()
  {
    return weightedScore;
  }

  public void setWeightedScore( boolean weightedScore )
  {
    this.weightedScore = weightedScore;
  }

  public String getCalculatorId()
  {
    return calculatorId;
  }

  public void setCalculatorId( String calculatorId )
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

  public String getCopyCalculatorName()
  {
    return copyCalculatorName;
  }

  public void setCopyCalculatorName( String copyCalculatorName )
  {
    this.copyCalculatorName = copyCalculatorName;
  }

  public String getScoreCMAssetName()
  {
    return scoreCMAssetName;
  }

  public void setScoreCMAssetName( String scoreCMAssetName )
  {
    this.scoreCMAssetName = scoreCMAssetName;
  }

  public String getWeightCMAssetName()
  {
    return weightCMAssetName;
  }

  public void setWeightCMAssetName( String weightCMAssetName )
  {
    this.weightCMAssetName = weightCMAssetName;
  }

  public String getCalculatorStatus()
  {
    return calculatorStatus;
  }

  public void setCalculatorStatus( String calculatorStatus )
  {
    this.calculatorStatus = calculatorStatus;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getWeightLabel()
  {
    return weightLabel;
  }

  public void setWeightLabel( String weightLabel )
  {
    this.weightLabel = weightLabel;
  }

  public String getScoreLabel()
  {
    return scoreLabel;
  }

  public void setScoreLabel( String scoreLabel )
  {
    this.scoreLabel = scoreLabel;
  }
}

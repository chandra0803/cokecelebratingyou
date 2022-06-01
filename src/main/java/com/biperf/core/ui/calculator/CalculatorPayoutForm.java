/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/calculator/CalculatorPayoutForm.java,v $
 */

package com.biperf.core.ui.calculator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.ui.BaseForm;

/**
 * CalculatorPayoutForm.
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
 * <td>May 25, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorPayoutForm extends BaseForm
{
  private String method;
  private Long calculatorId;
  private String calculatorName;
  private String calculatorAward;
  private Long calculatorPayoutId;
  private String lowScore;
  private String highScore;
  private String lowAward;
  private String highAward;
  private String levelName;

  /**
   * Load the form
   * 
   * @param payout
   */
  public void load( CalculatorPayout payout )
  {
    this.calculatorId = payout.getCalculator().getId();
    this.calculatorName = payout.getCalculator().getName();
    this.calculatorPayoutId = payout.getId();
    this.calculatorAward = payout.getCalculator().getCalculatorAwardType().getCode();
    this.lowScore = String.valueOf( payout.getLowScore() );
    this.highScore = String.valueOf( payout.getHighScore() );
    this.lowAward = String.valueOf( payout.getLowAward() );
    this.highAward = String.valueOf( payout.getHighAward() );
  }

  /**
   * Creates a detatched CalculatorPayout Domain Object that will later be synchronized with a
   * looked up promotion object in the service
   * 
   * @return CalculatorPayout
   */
  public CalculatorPayout toDomainObject()
  {
    CalculatorPayout payout = new CalculatorPayout();
    payout.setId( this.calculatorPayoutId );
    payout.setLowScore( Integer.parseInt( this.lowScore ) );
    payout.setHighScore( Integer.parseInt( this.highScore ) );
    if ( this.lowAward != null && !this.lowAward.equals( "" ) )
    {
      payout.setLowAward( Integer.parseInt( this.lowAward ) );
    }
    if ( this.highAward != null && !this.highAward.equals( "" ) )
    {
      payout.setHighAward( Integer.parseInt( this.highAward ) );
    }

    return payout;
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

    if ( !CalculatorAwardType.lookup( calculatorAward ).isMerchLevelAward() )
    {
      if ( this.lowScore == null || this.lowScore.equals( "" ) || this.highScore == null || this.highScore.equals( "" ) || this.lowAward == null || this.lowAward.equals( "" ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.PAYOUT_NUMERIC_OR_NULL" ) );
      }
      else
      {
        if ( !isNumeric( this.lowScore ) || !isNumeric( this.highScore ) || !isNumeric( this.lowAward ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.PAYOUT_NUMERIC_OR_NULL" ) );
        }
      }

      if ( actionErrors.size() == 0 )
      {
        if ( CalculatorAwardType.lookup( calculatorAward ).isRangeAward() )
        {
          if ( this.highAward == null || this.highAward.equals( "" ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.PAYOUT_NUMERIC_OR_NULL" ) );
          }
          else
          {
            if ( !isNumeric( this.highAward ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.PAYOUT_NUMERIC_OR_NULL" ) );
            }
          }
        }
      }

      if ( this.lowScore != null && !this.lowScore.equals( "" ) && this.highScore != null && !this.highScore.equals( "" ) && isNumeric( this.lowScore ) && isNumeric( this.highScore ) )
      {
        if ( Integer.parseInt( this.lowScore ) > Integer.parseInt( this.highScore ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.INVALID_SCORES" ) );
        }
      }

      if ( CalculatorAwardType.lookup( calculatorAward ).isRangeAward() )
      {
        if ( this.lowAward != null && !this.lowAward.equals( "" ) && this.highAward != null && !this.highAward.equals( "" ) && isNumeric( lowAward ) && isNumeric( highAward ) )
        {
          if ( Integer.parseInt( this.lowAward ) > Integer.parseInt( this.highAward ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "calculator.errors.INVALID_AWARDS" ) );
          }
        }
      }
    }

    return actionErrors;
  }

  private boolean isNumeric( String field )
  {
    try
    {
      Integer.parseInt( field );
      return true;
    }
    catch( NumberFormatException nbe )
    {
      return false;
    }
  }

  // getters and setters
  public String getCalculatorAward()
  {
    return calculatorAward;
  }

  public void setCalculatorAward( String calculatorAward )
  {
    this.calculatorAward = calculatorAward;
  }

  public Long getCalculatorPayoutId()
  {
    return calculatorPayoutId;
  }

  public void setCalculatorPayoutId( Long calculatorPayoutId )
  {
    this.calculatorPayoutId = calculatorPayoutId;
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

  public String getHighAward()
  {
    return highAward;
  }

  public void setHighAward( String highAward )
  {
    this.highAward = highAward;
  }

  public String getHighScore()
  {
    return highScore;
  }

  public void setHighScore( String highScore )
  {
    this.highScore = highScore;
  }

  public String getLowAward()
  {
    return lowAward;
  }

  public void setLowAward( String lowAward )
  {
    this.lowAward = lowAward;
  }

  public String getLowScore()
  {
    return lowScore;
  }

  public void setLowScore( String lowScore )
  {
    this.lowScore = lowScore;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }
}

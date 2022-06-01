/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/sweepstakes/CreateWinnersForm.java,v $
 */

package com.biperf.core.ui.promotion.sweepstakes;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;

/**
 * CreateWinnersForm.
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
 * <td>Nov 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CreateWinnersForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  public static final String FORM_NAME = "createWinnersForm";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String promotionId;
  private String startDate;
  private String endDate;

  // ---------------------------------------------------------------------------
  // Validate Methods
  // ---------------------------------------------------------------------------

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
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

    Promotion promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
    if ( !promotion.isSurveyPromotion() )
    {
      // The start date is required.
      if ( startDate == null || startDate.length() == 0 )
      {
        actionErrors.add( "startDate", new ActionMessage( "system.errors.REQUIRED", "promotion.sweepstakes.create.winners.START_DATE" ) );
      }

      // The start date string must represent a valid date.
      Date localStartDate = null;
      try
      {
        if ( startDate != null )
        {
          localStartDate = DateUtils.toStartDate( startDate );
        }
      }
      catch( ParseException e )
      {
        actionErrors.add( "startDate", new ActionMessage( "system.errors.INVALID", "promotion.sweepstakes.create.winners.START_DATE" ) );
      }

      // The end date is required.
      if ( endDate == null || endDate.length() == 0 )
      {
        actionErrors.add( "endDate", new ActionMessage( "system.errors.REQUIRED", "promotion.sweepstakes.create.winners.END_DATE" ) );
      }

      // The end date string must represent a valid date.
      Date localEndDate = null;
      try
      {
        if ( endDate != null )
        {
          localEndDate = DateUtils.toEndDate( endDate );
        }
      }
      catch( ParseException e )
      {
        actionErrors.add( "endDate", new ActionMessage( "system.errors.INVALID", "promotion.sweepstakes.create.winners.END_DATE" ) );
      }

      // The start date must be earlier than the end date.
      if ( localStartDate != null && localEndDate != null )
      {
        if ( localStartDate.after( localEndDate ) )
        {
          actionErrors.add( "startDate", new ActionMessage( "promotion.sweepstakes.create.winners.START_DATE_BEFORE_END_DATE" ) );
        }
      }
    }

    return actionErrors;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the promotion service.
   * 
   * @return a reference to the promotion service.
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }
}

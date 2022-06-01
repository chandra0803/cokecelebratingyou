/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/sweepstakes/CreateWinnersAction.java,v $
 */

package com.biperf.core.ui.promotion.sweepstakes;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PromotionSweepstakeService;
import com.biperf.core.ui.BaseAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.DateUtils;

/**
 * CreateWinnersAction <p/> <b>Change History:</b><br>
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
public class CreateWinnersAction extends BaseAction
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog( CreateWinnersAction.class );

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    ActionForward actionForward = null;

    if ( isCancelled( request ) )
    {
      actionForward = mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }
    else
    {
      actionForward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
      ActionMessages errors = new ActionMessages();

      if ( isTokenValid( request, true ) )
      {
        FormAdapter formAdapter = new FormAdapter( (CreateWinnersForm)form );

        Long promotionId = formAdapter.getPromotionId();
        Date startDate = formAdapter.getStartDate();
        Date endDate = formAdapter.getEndDate();

        try
        {
          getPromotionSweepstakeService().createWinnersList( promotionId, startDate, endDate );
        }
        catch( UniqueConstraintViolationException e )
        {
          // This shouldn't happen because we aren't saving anything that needs to be unique.
          logger.error( ">>> UniqueConstraintViolationException thrown while creating Sweepstake Winners list." );
        }
        catch( ServiceErrorException se )
        {
          logger.error( ">>> ServiceErrorException thrown while creating Sweepstake Winners list." );
        }
      }
      else
      {
        errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
        saveErrors( request, errors );
        actionForward = mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }

    return actionForward;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the promotion sweepstakes service.
   *
   * @return a reference to the promotion sweepstakes service.
   */
  private PromotionSweepstakeService getPromotionSweepstakeService()
  {
    return (PromotionSweepstakeService)getService( PromotionSweepstakeService.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  private class FormAdapter
  {
    /**
     * the adapted form
     */
    private CreateWinnersForm form;

    /**
     * Constructs a <code>FormAdapter</code> object.
     *
     * @param form  the form to be adapted
     */
    public FormAdapter( CreateWinnersForm form )
    {
      this.form = form;
    }

    public Date getEndDate()
    {
      Date endDate = null;

      Promotion promotion = getPromotion();
      if ( promotion.isSurveyPromotion() )
      {
        endDate = promotion.getSubmissionEndDate();
        if ( endDate == null )
        {
          endDate = new Date();
        }
      }
      else
      {
        endDate = DateUtils.toDate( form.getEndDate() );
      }

      return endDate;
    }

    public Long getPromotionId()
    {
      Long promotionId = null;

      String promotionIdString = form.getPromotionId();
      if ( promotionIdString != null && promotionIdString.length() > 0 )
      {
        promotionId = new Long( promotionIdString );
      }

      return promotionId;
    }

    public Date getStartDate()
    {
      Date startDate = null;

      Promotion promotion = getPromotion();
      if ( promotion.isSurveyPromotion() )
      {
        startDate = promotion.getSubmissionStartDate();
      }
      else
      {
        startDate = DateUtils.toDate( form.getStartDate() );
      }

      return startDate;
    }

    private Promotion getPromotion()
    {
      return getPromotionService().getPromotionById( new Long( form.getPromotionId() ) );
    }

    private PromotionService getPromotionService()
    {
      return (PromotionService)getService( PromotionService.BEAN_NAME );
    }
  }
}

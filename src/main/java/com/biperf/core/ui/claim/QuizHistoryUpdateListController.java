/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/QuizHistoryUpdateListController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.QuizPromotionQueryConstraint;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/**
 * QuizHistoryUpdateListController.
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
 * <td>zahler</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizHistoryUpdateListController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long promotionId = null;
    Date startDate = null;
    Date endDate = null;
    List promotionList = null;
    List quizClaimList = null;

    if ( ! ( request.getParameter( "promotionId" ) == null ) )
    {
      if ( !request.getParameter( "promotionId" ).equals( "" ) )
      {
        promotionId = new Long( request.getParameter( "promotionId" ) );
      }
    }

    QuizPromotionQueryConstraint quizPromotionQueryConstraint = new QuizPromotionQueryConstraint();
    promotionList = getPromotionService().getPromotionList( quizPromotionQueryConstraint );

    if ( request.getParameter( "startDate" ) != null )
    {
      startDate = DateUtils.toDate( request.getParameter( "startDate" ) );
    }
    else
    {
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.add( GregorianCalendar.MONTH, -1 );
      startDate = calendar.getTime();
    }

    if ( request.getParameter( "endDate" ) != null )
    {
      endDate = DateUtils.toDate( request.getParameter( "endDate" ) );
    }
    else
    {
      endDate = DateUtils.getCurrentDate();
    }

    QuizClaimQueryConstraint quizClaimQueryConstraint = new QuizClaimQueryConstraint();
    quizClaimQueryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    quizClaimQueryConstraint.setStartDate( startDate );
    quizClaimQueryConstraint.setEndDate( endDate );
    quizClaimQueryConstraint.setSubmitterId( UserManager.getUser().getUserId() );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );

    quizClaimList = getClaimService().getClaimListWithAssociations( quizClaimQueryConstraint, associationRequestCollection );

    request.setAttribute( "startDate", DateUtils.toDisplayString( startDate ) );
    request.setAttribute( "endDate", DateUtils.toDisplayString( endDate ) );
    request.setAttribute( "promotionList", promotionList );
    request.setAttribute( "quizClaimList", quizClaimList );
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the ClaimService from the beanLocator.
   * 
   * @return ClaimService
   */
  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

}

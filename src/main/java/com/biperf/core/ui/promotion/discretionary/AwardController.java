/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/discretionary/AwardController.java,v $
 */

package com.biperf.core.ui.promotion.discretionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

/**
 * AwardController.
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
 * <td>Sep 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardController extends BaseController
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
    AwardForm awardForm = (AwardForm)request.getAttribute( "discretionaryAwardForm" );
    // BugFix 17932
    // Replacing paxOverview call with specific associations
    // Participant pax = getParticipantService().getParticipantOverviewById( awardForm.getUserId()
    // );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
    Participant pax = getParticipantService().getParticipantByIdWithAssociations( awardForm.getUserId(), associationRequestCollection );

    // Retrieve the list of all live master promos the current pax is eligible for and set on
    // request
    List promotionList = getPromotionService().getClaimablePromotionsForParticipant( pax );
    List parentPromotionList = new ArrayList();

    for ( Iterator iter = promotionList.iterator(); iter.hasNext(); )
    {
      Promotion promotion = (Promotion)iter.next();
      if ( promotion instanceof ProductClaimPromotion )
      {
        ProductClaimPromotion pcp = (ProductClaimPromotion)promotion;
        if ( !pcp.isChild() )
        {
          parentPromotionList.add( promotion );
        }
      }
      else
      {
        parentPromotionList.add( promotion );
      }
    }

    request.setAttribute( "promotionList", parentPromotionList );

    // Retrieve the pax from the DB and set the properly formatted pax name on the request
    User user = getUserService().getUserById( awardForm.getUserId() );
    request.setAttribute( "user", user );
  }

  /**
   * Get the PromotionService.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the ParticipantService.
   * 
   * @return ParticipantService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the ParticipantService.
   * 
   * @return ParticipantService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}

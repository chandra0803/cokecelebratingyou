/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryActivitiesListController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.activity.impl.SalesActivityAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * TransactionHistoryActivitiesListController.
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
 *          Exp $
 */
public class TransactionHistoryActivitiesListController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( TransactionHistoryActivitiesListController.class );

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

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      Long claimId = null;
      Long userId = null;
      try
      {
        String claimIdString = (String)clientStateMap.get( "claimId" );
        claimId = new Long( claimIdString );
      }
      catch( ClassCastException e )
      {
        claimId = (Long)clientStateMap.get( "claimId" );
      }
      try
      {
        String userIdString = (String)clientStateMap.get( "userId" );
        userId = new Long( userIdString );
      }
      catch( ClassCastException e2 )
      {
        userId = (Long)clientStateMap.get( "userId" );
      }

      String livePromotionType = request.getParameter( "livePromotionType" );
      if ( livePromotionType == null || livePromotionType.equals( "" ) )
      {
        livePromotionType = (String)clientStateMap.get( "promotionType" );
      }

      if ( claimId != null && userId != null && livePromotionType != null )
      {
        List activities = new ArrayList();

        Participant participant = getParticipantService().getParticipantById( userId );
        request.setAttribute( "participant", participant );

        if ( livePromotionType.equals( "product_claim" ) )
        {
          List salesActivities = new ArrayList( getActivityService().getSalesActivitiesByClaimIdAndUserId( claimId, userId ) );
          List managerOverrideActivities = new ArrayList( getActivityService().getManagerOverrideActivityByClaimIdAndUserId( claimId, userId ) );
          SalesActivity salesActivity = null;
          ManagerOverrideActivity managerOverrideActivity = null;

          for ( Iterator activitiesIter = salesActivities.iterator(); activitiesIter.hasNext(); )
          {

            salesActivity = (SalesActivity)activitiesIter.next();

            AssociationRequestCollection activityAssociationRequestCollection = new AssociationRequestCollection();
            activityAssociationRequestCollection.add( new SalesActivityAssociationRequest( SalesActivityAssociationRequest.ALL ) );

            SalesActivity hydatedSalesActivity = getActivityService().getSalesActivityByIdWithAssociations( salesActivity.getId(), activityAssociationRequestCollection );

            salesActivity.setProduct( hydatedSalesActivity.getProduct() );

            activities.add( salesActivity );
          }

          for ( Iterator managerOverrideActivitiesIter = managerOverrideActivities.iterator(); managerOverrideActivitiesIter.hasNext(); )
          {

            managerOverrideActivity = (ManagerOverrideActivity)managerOverrideActivitiesIter.next();

            activities.add( managerOverrideActivity );
          }

        }
        else if ( livePromotionType.equals( "recognition" ) )
        {

          activities = new ArrayList( getActivityService().getRecognitionActivitiesByClaimIdAndUserId( claimId, userId ) );

        }
        else if ( livePromotionType.equals( "quiz" ) )
        {

          activities = new ArrayList( getActivityService().getQuizActivityByClaimIdAndUserId( claimId, userId ) );
        }

        request.setAttribute( "activities", activities );
      }
      else
      {
        LOG.error( "claimId or userId or livePromotionType not found in client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
}

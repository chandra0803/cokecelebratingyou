/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/CarryOverActivitiesController.java,v $
 *
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.activity.impl.SalesActivityAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>gaddam</td>
 * <td>Feb 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author gaddam
 *
 */
public class CarryOverActivitiesController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( CarryOverActivitiesController.class );

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
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
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
      Long promotionId = null;
      try
      {
        promotionId = (Long)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        String promoIdStr = (String)clientStateMap.get( "promotionId" );
        if ( promoIdStr != null && promoIdStr.length() > 0 )
        {
          promotionId = new Long( promoIdStr );
        }
      }
      if ( promotionId != null )
      {
        Promotion promotion = getPromotionService().getPromotionById( promotionId );
        request.setAttribute( "promotion", promotion );

        Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
        AssociationRequestCollection salesAssociationRequestCollection = new AssociationRequestCollection();
        salesAssociationRequestCollection.add( new SalesActivityAssociationRequest( SalesActivityAssociationRequest.ALL ) );

        List carryOverActivityList = getActivityService().getActivitiesByCarryOverBalance( promotion.getId(), participant.getId(), salesAssociationRequestCollection );

        request.setAttribute( "carryOverActivityList", carryOverActivityList );
      }
      else
      {
        LOG.error( "PromotionId not found in clientState" );
        request.setAttribute( "carryOverActivityList", new ArrayList() );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the Promotion service.
   * 
   * @return a reference to the Promotion service.
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Activity service.
   * 
   * @return a reference to the Promotion service.
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

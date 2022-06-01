/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.StackRankNodeAssociationRequest;
import com.biperf.core.service.promotion.StackRankNodeService;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PendingStackRankingDetailsController.
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
 * <td>gaddam</td>
 * <td>Mar 8, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PendingStackRankingDetailsController extends BaseController
{
  /**
   * Tiles controller for the Pending Stack Ranking Node Details page Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PendingStackRankingDetailsForm stackRankForm = (PendingStackRankingDetailsForm)request.getAttribute( "pendingStackRankForm" );
    // get the promotion
    if ( stackRankForm.getPromotionId() != null && !"".equals( stackRankForm.getPromotionId() ) )
    {
      String promotionId = stackRankForm.getPromotionId();
      stackRankForm.setPromotionName( getPromotionService().getPromotionById( new Long( promotionId ) ).getName() );
    }

    Long stackRankId = null;
    String nodeType = "";
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
      if ( stackRankForm.getNodeType() == null || stackRankForm.getNodeType().equals( "" ) )
      {
        nodeType = (String)clientStateMap.get( "nodeType" );
        stackRankForm.setNodeType( nodeType );
      }

      try
      {
        String stackRankIdString = (String)clientStateMap.get( "stackRankId" );
        stackRankId = new Long( stackRankIdString );
      }
      catch( ClassCastException cce )
      {
        stackRankId = (Long)clientStateMap.get( "stackRankId" );
      }
      if ( stackRankId != null )
      {
        // get the stackRank
        StackRank stackRank = getStackRankService().getStackRank( stackRankId );

        stackRankForm.setRankingPeriodFrom( DateUtils.toDisplayString( stackRank.getStartDate() ) );
        stackRankForm.setRankingPeriodTo( DateUtils.toDisplayString( stackRank.getEndDate() ) );
        // see whether this stack rank has payout
        request.setAttribute( "hasPayout", Boolean.valueOf( stackRank.isCalculatePayout() ) );

        Long nodeId = null;

        if ( stackRankForm.getNodeId() != null && !"".equals( stackRankForm.getNodeId() ) )
        {
          nodeId = new Long( stackRankForm.getNodeId() );

          StackRankNode stackRankNode = null;

          // create association request collection
          AssociationRequestCollection stackRankAssociationRequestCollection = new AssociationRequestCollection();
          stackRankAssociationRequestCollection.add( new StackRankNodeAssociationRequest( StackRankNodeAssociationRequest.STACK_RANK_PARTICIPANTS_HYDRATION_LEVEL ) );

          // get the stack rank node
          stackRankNode = getStackRankNodeService().getStackRankNode( stackRank.getId(), nodeId, stackRankAssociationRequestCollection );

          if ( stackRankNode != null )
          {
            // now get the list of stack rank participants
            Set participantsSet = stackRankNode.getStackRankParticipants();
            List participantsList = new ArrayList();
            for ( Iterator iter = participantsSet.iterator(); iter.hasNext(); )
            {
              StackRankParticipant srp = (StackRankParticipant)iter.next();
              if ( srp.getStackRankFactor() == 0 )
              {
                iter.remove();
              }
              else
              {
                participantsList.add( srp );
              }
            }
            // Sort the StackRank list.
            Collections.sort( participantsList, new Comparator()
            {
              public int compare( Object object, Object object1 )
              {
                StackRankParticipant stackRankParticipant1 = (StackRankParticipant)object;
                StackRankParticipant stackRankParticipant2 = (StackRankParticipant)object1;

                return stackRankParticipant1.getParticipant().getNameLFMNoComma().compareTo( stackRankParticipant2.getParticipant().getNameLFMNoComma() );
              }
            } );
            request.setAttribute( "participantsList", participantsList );
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    List nodeList = getStackRankService().getNodesByStackRankIdAndNodeTypeId( new Long( stackRankForm.getStackRankId() ), new Long( stackRankForm.getNodeTypeId() ) );
    request.setAttribute( "nodeList", nodeList );

  }

  /**
   * Get the StackRankService from the beanLocator.
   * 
   * @return StackRankService
   */
  private StackRankNodeService getStackRankNodeService()
  {
    return (StackRankNodeService)getService( StackRankNodeService.BEAN_NAME );
  }

  /**
   * Get the NodeTypeService from the beanLocator.
   * 
   * @return NodeTypeService
   */
  private StackRankService getStackRankService()
  {
    return (StackRankService)getService( StackRankService.BEAN_NAME );
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

}

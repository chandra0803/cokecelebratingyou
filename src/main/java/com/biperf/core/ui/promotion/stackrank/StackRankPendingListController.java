/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/stackrank/StackRankPendingListController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.promotion.stackrank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.promotion.hibernate.StackRankQueryConstraint;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.promotion.StackRankAssociationRequest;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.StackRankValueBean;

/**
 * StackRankPendingListController.
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
 * <td>March 09, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankPendingListController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( StackRankPendingListController.class );

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
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
    StackRankPendingListForm stackRankPendingListForm = (StackRankPendingListForm)request.getAttribute( "stackRankPendingListForm" );

    Long promotionId = null;
    List nodeTypeList = new ArrayList();
    StackRank pendingStackRank = null;

    if ( stackRankPendingListForm != null )
    {
      promotionId = stackRankPendingListForm.getPromotionId();
    }

    if ( promotionId == null || promotionId.longValue() == 0 )
    {
      // The first time the page is loaded, the prmotionId will be passed in the request
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
        String promotionIdString = (String)clientStateMap.get( "promotionId" );
        promotionId = new Long( promotionIdString );
        if ( promotionId == null )
        {
          LOG.error( "promotionId not found in client state" );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    if ( promotionId != null )
    {
      // Create a StackRankQueryConstraint and get the list of pending stackRanks
      AssociationRequestCollection stackRankAssociationRequestCollection = new AssociationRequestCollection();
      stackRankAssociationRequestCollection.add( new StackRankAssociationRequest( StackRankAssociationRequest.ALL ) );
      StackRankQueryConstraint queryConstraint = new StackRankQueryConstraint();
      queryConstraint.setPromotionIdsIncluded( new Long[] { promotionId } );
      queryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.WAITING_FOR_STACK_RANK_LISTS_TO_BE_APPROVED ) } );
      List pendingStackRankList = getStackRankService().getStackRankList( queryConstraint, stackRankAssociationRequestCollection );
      // even though this is a list, per business rule there should always be just one
      if ( pendingStackRankList != null && pendingStackRankList.size() > 0 )
      {
        pendingStackRank = (StackRank)pendingStackRankList.get( 0 );
        stackRankPendingListForm.setStackRankId( pendingStackRank.getId() );
        stackRankPendingListForm.setPromotionName( pendingStackRank.getPromotion().getName() );

        Set nodeTypeSet = getStackRankService().getNodeTypesByStackRankId( pendingStackRank.getId() );

        Iterator nodeTypeIter = nodeTypeSet.iterator();
        while ( nodeTypeIter.hasNext() )
        {
          int totalNodesToApprove = 0;
          StackRankValueBean stackRankValueBean = new StackRankValueBean();
          NodeType nodeType = (NodeType)nodeTypeIter.next();
          stackRankValueBean.setNodeTypeName( nodeType.getI18nName() );
          stackRankValueBean.setNodeTypeId( nodeType.getId().toString() );
          // get the list of all the nodes for this node type.
          List nodeTypeNodes = getNodeService().getNodesByNodeType( nodeType );

          stackRankValueBean.setTotalNodes( String.valueOf( nodeTypeNodes.size() ) );

          if ( pendingStackRank.getStackRankNodes() != null && pendingStackRank.getStackRankNodes().size() > 0 )
          {
            Iterator stackRankNodeIter = pendingStackRank.getStackRankNodes().iterator();
            while ( stackRankNodeIter.hasNext() )
            {
              StackRankNode stackRankNode = (StackRankNode)stackRankNodeIter.next();
              if ( stackRankNode.getNode().getNodeType().equals( nodeType ) )
              {
                if ( nodeTypeNodes.contains( stackRankNode.getNode() ) )
                {
                  totalNodesToApprove++;
                }
              }
            }
          }
          stackRankValueBean.setTotalNodesWithRankings( String.valueOf( totalNodesToApprove ) );
          nodeTypeList.add( stackRankValueBean );
        }
      }

      request.setAttribute( "nodeTypeList", nodeTypeList );
      request.setAttribute( "pendingStackRank", pendingStackRank );
      if ( pendingStackRank != null )
      {
        request.setAttribute( "startDate", DateUtils.toDisplayString( pendingStackRank.getStartDate() ) );
        request.setAttribute( "endDate", DateUtils.toDisplayString( pendingStackRank.getEndDate() ) );
        request.setAttribute( "rankingSubmitted", DateUtils.toDisplayTimeString( pendingStackRank.getAuditCreateInfo().getDateCreated() ) );
      }
    }
  }

  /**
   * Gets a StackRankService
   * 
   * @return StackRankService
   */
  private StackRankService getStackRankService()
  {
    return (StackRankService)getService( StackRankService.BEAN_NAME );
  }

  /**
   * Gets a NodeService
   * 
   * @return NodeService
   */
  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }
}

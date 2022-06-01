/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.dao.promotion.hibernate.StackRankParticipantQueryConstraint;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.StackRankAssociationRequest;
import com.biperf.core.service.promotion.StackRankParticipantAssociationRequest;
import com.biperf.core.service.promotion.StackRankParticipantService;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;

/**
 * StackRankHistoryAction.
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
 * <td>Mar 17, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankHistoryAction extends BaseDispatchAction
{
  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    StackRankHistoryForm stackRankHistoryForm = (StackRankHistoryForm)form;

    Long promotionId = stackRankHistoryForm.getPromotionId();
    Long stackRankId = stackRankHistoryForm.getStackRankId();
    Long nodeTypeId = stackRankHistoryForm.getNodeTypeId();
    Long nodeId = stackRankHistoryForm.getNodeId();

    if ( promotionId == null || promotionId.longValue() == 0 )
    {
      // TODO Log this error
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    StackRank stackRank = null;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new StackRankAssociationRequest( StackRankAssociationRequest.NODES ) );

    // if stackRankId is null, get the most recent stack rank.
    if ( stackRankId == null || stackRankId.longValue() == 0 )
    {
      stackRank = getStackRankService().getLatestStackRankByPromotionId( promotionId, StackRankState.STACK_RANK_LISTS_APPROVED, associationRequestCollection );

      if ( stackRank == null )
      {
        throw new BeaconRuntimeException( "No StackRankFound for promotion." );
      }
    }
    else
    {
      stackRank = getStackRankService().getStackRank( stackRankId, associationRequestCollection );
    }

    if ( nodeId == null || nodeId.longValue() == 0 )
    {
      Set stackRankNodes = stackRank.getStackRankNodes();
      if ( stackRankNodes != null && stackRankNodes.size() > 0 )
      {
        StackRankNode stackRankNode = (StackRankNode)stackRankNodes.iterator().next();
        Node node = stackRankNode.getNode();
        nodeId = node.getId();
        nodeTypeId = node.getNodeType().getId();
      }
    }

    request.setAttribute( "stackRankParticipantList", getStackRankPaxList( stackRank.getId(), nodeId ) );

    stackRankHistoryForm.setStackRankId( stackRank.getId() );
    stackRankHistoryForm.setNodeId( nodeId );
    stackRankHistoryForm.setNodeTypeId( nodeTypeId );

    // get the actionForward to display the detail page.
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward onChangeStackRank( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    StackRankHistoryForm stackRankHistoryForm = (StackRankHistoryForm)form;

    Long stackRankId = stackRankHistoryForm.getStackRankId();
    Long nodeTypeId = stackRankHistoryForm.getNodeTypeId();
    Long nodeId = stackRankHistoryForm.getNodeId();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new StackRankAssociationRequest( StackRankAssociationRequest.NODES ) );

    StackRank stackRank = getStackRankService().getStackRank( stackRankId, associationRequestCollection );

    Set stackRankNodes = stackRank.getStackRankNodes();
    if ( stackRankNodes != null && stackRankNodes.size() > 0 )
    {
      StackRankNode stackRankNode = (StackRankNode)stackRankNodes.iterator().next();

      Node node = stackRankNode.getNode();
      nodeId = node.getId();
      nodeTypeId = node.getNodeType().getId();
    }

    stackRankHistoryForm.setNodeId( nodeId );
    stackRankHistoryForm.setNodeTypeId( nodeTypeId );

    request.setAttribute( "stackRankParticipantList", getStackRankPaxList( stackRankId, nodeId ) );

    // get the actionForward to display the detail page.
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward onChangeNodeType( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    StackRankHistoryForm stackRankHistoryForm = (StackRankHistoryForm)form;

    Long stackRankId = stackRankHistoryForm.getStackRankId();
    Long nodeTypeId = stackRankHistoryForm.getNodeTypeId();
    Long nodeId = stackRankHistoryForm.getNodeId();

    List nodeList = getStackRankService().getNodesByStackRankIdAndNodeTypeId( stackRankId, nodeTypeId );

    if ( nodeList != null && nodeList.size() > 0 )
    {
      Node node = (Node)nodeList.iterator().next();
      nodeId = node.getId();
    }

    stackRankHistoryForm.setNodeId( nodeId );

    request.setAttribute( "stackRankParticipantList", getStackRankPaxList( stackRankId, nodeId ) );

    // get the actionForward to display the detail page.
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param stackRankId
   * @param nodeId
   * @return List of StackRankParticipant
   */
  private List getStackRankPaxList( Long stackRankId, Long nodeId )
  {
    List stackRankParticipantList = new ArrayList();
    StackRankParticipantQueryConstraint queryConstraint = new StackRankParticipantQueryConstraint();
    queryConstraint.setNodeIdsIncluded( new Long[] { nodeId } );
    queryConstraint.setStackRankIdsIncluded( new Long[] { stackRankId } );
    queryConstraint.setExcludeParticipantsWithoutStackRank( true );

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new StackRankParticipantAssociationRequest( StackRankParticipantAssociationRequest.PARTICIPANT ) );

    stackRankParticipantList = getStackRankParticipantService().getStackRankParticipantList( queryConstraint, requestCollection );

    // Sort the StackRank list.
    Collections.sort( stackRankParticipantList, new Comparator()
    {
      public int compare( Object object, Object object1 )
      {
        StackRankParticipant stackRankParticipant1 = (StackRankParticipant)object;
        StackRankParticipant stackRankParticipant2 = (StackRankParticipant)object1;
        if ( stackRankParticipant1.getRank() > stackRankParticipant2.getRank() )
        {
          return 1;
        }
        return 0;
      }
    } );

    return stackRankParticipantList;
  }

  /**
   * Returns a reference to the StackRankParticipantService service.
   * 
   * @return a reference to the StackRankParticipantService service.
   */
  private StackRankParticipantService getStackRankParticipantService()
  {
    return (StackRankParticipantService)getService( StackRankParticipantService.BEAN_NAME );
  }

  /**
   * Returns a reference to the StackRankService service.
   * 
   * @return a reference to the StackRankService service.
   */
  private StackRankService getStackRankService()
  {
    return (StackRankService)getService( StackRankService.BEAN_NAME );
  }
}

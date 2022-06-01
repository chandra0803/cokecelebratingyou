/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/stackrank/StackRankNodeForm.java,v $
 */

package com.biperf.core.ui.promotion.stackrank;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.promotion.StackRankNodeService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;

/*
 * StackRankNodeForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 21, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankNodeForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The ID of the stack rank to display.
   */
  private Long stackRankId;

  /**
   * The node name entered by the user. May be invalid.
   */
  private String nameOfNode;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getNameOfNode()
  {
    return nameOfNode;
  }

  public void setNameOfNode( String nameOfNode )
  {
    this.nameOfNode = nameOfNode;
  }

  public Long getStackRankId()
  {
    return stackRankId;
  }

  public void setStackRankId( Long stackRankId )
  {
    this.stackRankId = stackRankId;
  }

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  /**
   * Validates the data in this form.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @return <code>ActionErrors</code> object that encapsulates any validation errors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( actionErrors.size() == 0 )
    {
      // Constraint: A node with the given name must exist.
      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      Node node = getNodeService().getNodeByNameAndHierarchy( nameOfNode, primaryHierarchy );
      if ( node == null )
      {
        actionErrors.add( "nodeName", new ActionMessage( "promotion.stackranknode.errors.NODE_DOES_NOT_EXIST", nameOfNode ) );
      }

      // Constraint: A stack rank node for the given stack rank and node name must exist.
      StackRankNode stackRankNode = getStackRankNodeService().getStackRankNode( stackRankId, node != null ? node.getId() : null );
      if ( stackRankNode == null )
      {
        actionErrors.add( "nodeName", new ActionMessage( "promotion.stackranknode.errors.STACK_RANK_NODE_DOES_NOT_EXIST", nameOfNode ) );
      }
    }

    return actionErrors;
  }

  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the Hierarchy service.
   * 
   * @return a reference to the Hierarchy service.
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)BeanLocator.getBean( HierarchyService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Node service.
   * 
   * @return a reference to the Node service.
   */
  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Stack Rank Node service.
   * 
   * @return a reference to the Stack Rank Node service.
   */
  private StackRankNodeService getStackRankNodeService()
  {
    return (StackRankNodeService)BeanLocator.getBean( StackRankNodeService.BEAN_NAME );
  }
}

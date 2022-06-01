/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/node/NodeParticipantListForm.java,v $
 */

package com.biperf.core.ui.node;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.nodetype.ListValidatorFormWithDelete;

/**
 * NodeParticipantListForm <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class NodeParticipantListForm extends ListValidatorFormWithDelete
{

  private long nodeId = 0;
  private String lastName;
  private String method;

  /**
   * overriden from ListValidatorFormWithDelete to supply the name of the error key needed
   * 
   * @return asset for error message
   */
  protected String getErrorMessageKeyWhenNoneSelected()
  {
    return "hierarchy.errors.MUST_SELECT_DELETED";
  }

  public long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    // do NOT validate when search is performed based on lastName
    if ( method != null && method.equals( "search" ) )
    {
      return null;
    }
    return super.validate( mapping, request );
  }
}

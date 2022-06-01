/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/nodetype/NodeTypeListForm.java,v $
 */

package com.biperf.core.ui.nodetype;

/**
 * NodeTypeListForm.
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
 * <td>tennant</td>
 * <td>May 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeTypeListForm extends ListValidatorFormWithDelete
{
  /**
   * The struts error key for the message to display when no nodetypes are selected for a delete
   * 
   * @return error message key
   */
  protected String getErrorMessageKeyWhenNoneSelected()
  {
    return "hierarchy.errors.MUST_SELECT_DELETED";
  }
}

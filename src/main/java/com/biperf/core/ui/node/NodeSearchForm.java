/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/node/NodeSearchForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.node;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.ui.BaseActionForm;

/**
 * NodeSearchForm.
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
 * <td>sharma</td>
 * <td>May 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeSearchForm extends BaseActionForm
{
  private String method;
  private long hierarchyId;
  private String nameOfNode;
  private String nodeId;
  private long nodeTypeId;
  private String returnActionUrl; // action url to return to after a node selection on node lookup
  private boolean hierarchyListDisabled; // this was named as "Disabled" rather than "Enabled"
  // so the attribute on the JSP tag can be coded as:
  // <html:select disabled="${form.hierarchyListDisabled}" ...>
  // rather than
  // <html:select disabled="${form.hierarchyListEnabled == false}" ...>

  private String nodeSearchType;

  private final Map params = new HashMap(); // parameters that are passed to

  // node search by the caller and can be
  // propagated on the search result links

  public long getHierarchyId()
  {
    return hierarchyId;
  }

  public void setHierarchyId( long hierarchyId )
  {
    this.hierarchyId = hierarchyId;
  }

  public long getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( long nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public String getNameOfNode()
  {
    return nameOfNode;
  }

  public void setNameOfNode( String nameOfNode )
  {
    this.nameOfNode = nameOfNode;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public final Map getParamsMap()
  {
    return params;
  }

  public Object getParams( String key )
  {
    return params.get( key );
  }

  public void setParams( String key, Object value )
  {
    params.put( key, value );
  }

  public boolean isHierarchyListDisabled()
  {
    return hierarchyListDisabled;
  }

  public void setHierarchyListDisabled( boolean hierarchyListDisabled )
  {
    this.hierarchyListDisabled = hierarchyListDisabled;
  }

  public String getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( String nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public void setNodeSearchType( String nodeSearchType )
  {
    this.nodeSearchType = nodeSearchType;
  }

  public String getNodeSearchType()
  {
    return nodeSearchType;
  }
}

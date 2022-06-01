/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/node/NodeForm.java,v $
 */

package com.biperf.core.ui.node;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.CharacteristicValueBean;

/**
 * NodeForm.
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
 * <td>crosenquest</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeForm extends BaseActionForm
{

  private static final long serialVersionUID = 3256725074021987634L;

  public static final String FORM_NAME = "nodeForm";

  /** name */
  private String name = "";

  /** description */
  private String description = "";

  /** method */
  private String method = "";

  private String path = "";

  /** version */
  private String version = "0";

  /** id */
  private String id = "0";

  /** parentNodeId */
  private String parentNodeId = "0";

  /** dateCreated */
  private String dateCreated = "0";

  /** createdBy */
  private String createdBy = "";

  /** deleted */
  private String deleted = "false";

  private String nodeTypeId = "0";

  private String hierarchyId;

  private String paxToNodeId = "0";

  private String childrenToNodeId = "0";

  private List nodeTypeCharacteristicValueList = new ArrayList();

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( !"5001".equals( nodeTypeId ) )
    {
      if ( getNodeTypeCharacteristicValueListCount() > 0 )
      {

        CharacteristicUtils.validateCharacteristicValueList( nodeTypeCharacteristicValueList, actionErrors );
      }
    }
    if ( name != null && name.length() > 0 )
    {
      Hierarchy hierarchy = getHierarchyService().getById( new Long( hierarchyId ) );
      Node duplicateNode = getNodeService().getNodeByNameAndHierarchy( name, hierarchy );
      // If matching node name found, also confirm that we aren't just updating the existing node.
      if ( duplicateNode != null && !duplicateNode.getId().toString().equals( id ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "hierarchy.errors.DUPLICATE_NODE", name ) );
      }
    }

    return actionErrors;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // UserCharacteristicFormBeans. If this is not done, the form wont initialize
    // properly.
    nodeTypeCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "nodeTypeCharacteristicValueListCount" ) );
  } // end reset

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setChildrenToNodeId( String childrenToNodeId )
  {
    this.childrenToNodeId = childrenToNodeId;
  }

  public String getChildrenToNodeId()
  {
    return this.childrenToNodeId;
  }

  public void setPaxToNodeId( String paxToNodeId )
  {
    this.paxToNodeId = paxToNodeId;
  }

  public String getPaxToNodeId()
  {
    return this.paxToNodeId;
  }

  public void setDeleted( String deleted )
  {
    this.deleted = deleted;
  }

  public String getDeleted()
  {
    return deleted;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( String dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getParentNodeId()
  {
    return parentNodeId;
  }

  public void setParentNodeId( String parentNodeId )
  {
    this.parentNodeId = parentNodeId;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion( String version )
  {
    this.version = version;
  }

  /**
   * Load the form with the domain object value;
   * 
   * @param node
   */
  public void load( Node node )
  {
    this.name = node.getName();
    this.hierarchyId = String.valueOf( node.getHierarchy().getId() );
    if ( node.getParentNode() != null )
    {
      this.parentNodeId = String.valueOf( node.getParentNode().getId().longValue() );
    }
    this.nodeTypeId = String.valueOf( node.getNodeType().getId() );
    this.description = node.getDescription();
    if ( node.getAuditCreateInfo().getCreatedBy() != null )
    {
      this.createdBy = node.getAuditCreateInfo().getCreatedBy().toString();
    }
    this.dateCreated = String.valueOf( node.getAuditCreateInfo().getDateCreated().getTime() );
    this.id = String.valueOf( node.getId().longValue() );
    this.version = String.valueOf( node.getVersion().longValue() );
    this.path = node.getPath();
  }

  /**
   * Builds a domain object from the form.
   * 
   * @param node
   * @return Node
   */
  public Node toDomainObject( Node node )
  {

    node.setId( StringUtils.isBlank( id ) || id.equals( "0" ) ? null : new Long( this.id ) );
    node.setVersion( new Long( this.version ) );
    node.setName( this.name );
    node.setDescription( this.description );
    node.setPath( this.path );
    node.setVersion( new Long( this.version ) );
    if ( this.createdBy != null && !this.createdBy.equals( "" ) )
    {
      node.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }
    node.getAuditCreateInfo().setDateCreated( new Timestamp( Long.valueOf( this.dateCreated ).longValue() ) );

    if ( null != this.hierarchyId && !"".equals( this.hierarchyId ) )
    {
      Hierarchy hierarchy = new Hierarchy();
      hierarchy.setId( new Long( this.hierarchyId ) );
      node.setHierarchy( hierarchy );
    }

    if ( null != this.nodeTypeId && !"".equals( this.nodeTypeId ) )
    {
      NodeType nodeType = new NodeType();
      nodeType.setId( new Long( this.nodeTypeId ) );
      node.setNodeType( nodeType );
    }

    Collection nodeCharacteristics = CharacteristicUtils.toListOfNodeCharacteristicDomainObjects( nodeTypeCharacteristicValueList );

    node.getNodeCharacteristics().clear();
    Iterator nodeCharIterator = nodeCharacteristics.iterator();
    while ( nodeCharIterator.hasNext() )
    {
      node.addNodeCharacteristic( (NodeCharacteristic)nodeCharIterator.next() );
    }

    return node;
  }

  public String getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( String nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public void setHierarchyId( String hierarchyId )
  {
    this.hierarchyId = hierarchyId;
  }

  public String getHierarchyId()
  {
    return this.hierarchyId;
  }

  public List getNodeTypeCharacteristicValueList()
  {
    return nodeTypeCharacteristicValueList;
  }

  public void setNodeTypeCharacteristicValueList( List valueList )
  {
    this.nodeTypeCharacteristicValueList = valueList;
  }

  public int getNodeTypeCharacteristicValueListCount()
  {
    if ( nodeTypeCharacteristicValueList != null )
    {
      return nodeTypeCharacteristicValueList.size();
    }
    return 0;
  }

  /**
   * @return value of path property
   */
  public String getPath()
  {
    return path;
  }

  /**
   * @param path value for path property
   */
  public void setPath( String path )
  {
    this.path = path;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of CharacteristicFormBean from the value list
   */
  public CharacteristicValueBean getNodeTypeCharacteristicValueInfo( int index )
  {
    try
    {
      return (CharacteristicValueBean)nodeTypeCharacteristicValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

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

  public Long getParentNodeIdAsLong()
  {
    if ( parentNodeId == null || parentNodeId.equals( "" ) )
    {
      return null;
    }

    return Long.valueOf( parentNodeId );
  }

  public Long getNodeTypeIdAsLong()
  {
    if ( nodeTypeId == null || nodeTypeId.equals( "" ) )
    {
      return null;
    }

    return Long.valueOf( nodeTypeId );
  }

}

/*
 * $Source:
 * /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/hierarchy/HierarchyForm.java,v $
 * (c) 2005 BI, Inc. All rights reserved.
 */

package com.biperf.core.ui.hierarchy;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.HierarchyNodeType;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.ui.BaseActionForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * HierarchyForm.
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
 * <td>kumars</td>
 * <td>Apr 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HierarchyForm extends BaseActionForm
{
  private static final long serialVersionUID = 3256725074021987634L;

  public static final String FORM_NAME = "hierarchyForm";

  private String method;

  private String name;

  private String description;

  private boolean active;

  private String cmAssetCode = "";

  private String nameCmKey = "";

  private long version;

  private long id;

  private String createdBy;

  private String dateCreated;

  private String[] availableNodeTypes;

  private String[] addedNodeTypes;

  private boolean nodeTypeRequired = true;

  private String rosterHierarchyId;

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public long getId()
  {
    return id;
  }

  public void setId( long id )
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

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

  public String[] getAddedNodeTypes()
  {
    return addedNodeTypes;
  }

  public void setAddedNodeTypes( String[] addedNodeTypes )
  {
    this.addedNodeTypes = addedNodeTypes;
  }

  public String[] getAvailableNodeTypes()
  {
    return availableNodeTypes;
  }

  public void setAvailableNodeTypes( String[] availableNodeTypes )
  {
    this.availableNodeTypes = availableNodeTypes;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public String getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( String dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public boolean isNodeTypeRequired()
  {
    return nodeTypeRequired;
  }

  public void setNodeTypeRequired( boolean nodeTypeRequired )
  {
    this.nodeTypeRequired = nodeTypeRequired;
  }

  public String getRosterHierarchyId()
  {
    return rosterHierarchyId;
  }

  public void setRosterHierarchyId( String rosterHierarchyId )
  {
    this.rosterHierarchyId = rosterHierarchyId;
  }

  /**
   * Load the form with the domain object value;
   * 
   * @param hierarchy
   */
  public void load( Hierarchy hierarchy )
  {
    // todo jjd set flag to not allow edit if locale is not the default
    if ( hierarchy.getId() != null )
    {
      this.name = CmsResourceBundle.getCmsBundle().getString( hierarchy.getCmAssetCode(), hierarchy.getNameCmKey() );
      this.rosterHierarchyId = hierarchy.getRosterHierarchyId().toString();
    }
    this.description = hierarchy.getDescription();
    this.id = hierarchy.getId().longValue();
    this.version = hierarchy.getVersion().longValue();
    this.active = hierarchy.isActive();
    this.cmAssetCode = hierarchy.getCmAssetCode();
    this.nameCmKey = hierarchy.getNameCmKey();
    if ( hierarchy.getAuditCreateInfo().getCreatedBy() != null )
    {
      this.createdBy = hierarchy.getAuditCreateInfo().getCreatedBy().toString();
    }
    this.dateCreated = String.valueOf( hierarchy.getAuditCreateInfo().getDateCreated().getTime() );
    this.nodeTypeRequired = hierarchy.isNodeTypeRequired();
  }

  /**
   * Builds a domain object from the form. If there are nodeTypes they will be added to the
   * HierarchyNodeType Set.
   * 
   * @param nodeTypes
   * @return Hierarchy
   */
  public Hierarchy toDomainObject( List nodeTypes, NodeType defaultNodeType )
  {
    Hierarchy hierarchy = new Hierarchy();
    if ( this.id == 0 )
    {
      hierarchy.setId( null );
      hierarchy.setVersion( null );
    }
    else
    {
      hierarchy.setId( new Long( this.id ) );
      hierarchy.setVersion( new Long( this.version ) );
    }
    hierarchy.setName( this.name );
    hierarchy.setDescription( this.description );
    hierarchy.setActive( isActive() );
    hierarchy.setCmAssetCode( this.cmAssetCode );
    hierarchy.setNameCmKey( this.nameCmKey );
    if ( this.createdBy != null && !this.createdBy.equals( "" ) )
    {
      hierarchy.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }
    if ( !StringUtils.isEmpty( dateCreated ) )
    {
      hierarchy.getAuditCreateInfo().setDateCreated( new Timestamp( Long.valueOf( this.dateCreated ).longValue() ) );
    }
    if ( this.rosterHierarchyId != null && !StringUtils.isEmpty( this.rosterHierarchyId ) )
    {
      hierarchy.setRosterHierarchyId( UUID.fromString( this.rosterHierarchyId ) );
    }

    if ( nodeTypes != null && this.nodeTypeRequired )
    {
      Set hierarchyNodeTypes = new LinkedHashSet();
      for ( int i = 0; i < nodeTypes.size(); i++ )
      {
        NodeType nodeType = (NodeType)nodeTypes.get( i );
        HierarchyNodeType hierarchyNodeType = new HierarchyNodeType();

        hierarchyNodeType.setNodeType( nodeType );
        hierarchyNodeType.setHierarchy( hierarchy );
        hierarchyNodeType.setAuditCreateInfo( hierarchy.getAuditCreateInfo() );

        hierarchyNodeTypes.add( hierarchyNodeType );
      }
      hierarchy.setHierarchyNodeTypes( hierarchyNodeTypes );
    }
    else
    {
      Set hierarchyNodeTypes = new LinkedHashSet();

      HierarchyNodeType hierarchyNodeType = new HierarchyNodeType();
      hierarchyNodeType.setNodeType( defaultNodeType );
      hierarchyNodeType.setHierarchy( hierarchy );
      hierarchyNodeType.setAuditCreateInfo( hierarchy.getAuditCreateInfo() );

      hierarchyNodeTypes.add( hierarchyNodeType );

      hierarchy.setHierarchyNodeTypes( hierarchyNodeTypes );
    }
    hierarchy.setNodeTypeRequired( this.nodeTypeRequired );

    return hierarchy;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
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

    /**
     * Validate that the assignedNodeType list has values only if they are trying to save.
     */
    if ( actionMapping.getPath() != null && actionMapping.getPath().equals( "/hierarchyMaintainCreate" ) && nodeTypeRequired )
    {
      if ( request.getSession().getAttribute( "nodeTypes" ) == null || ( (ArrayList)request.getSession().getAttribute( "nodeTypes" ) ).size() == 0 )
      {
        actionErrors.add( "addedNodeTypes", new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "hierarchy.hierarchy", "LABEL_NODE_TYPES" ) ) );
      }
    }
    return actionErrors;
  }

}

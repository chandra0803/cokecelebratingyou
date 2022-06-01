/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/hierarchy/Hierarchy.java,v $
 */

package com.biperf.core.domain.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Hierarchy.
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
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class Hierarchy extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final long serialVersionUID = 3257005449605100852L;

  private String name;

  private String description;

  private boolean active;

  private boolean primary;

  private String cmAssetCode;

  private String nameCmKey;

  private boolean deleted;

  private Set nodes = new LinkedHashSet();

  public Set hierarchyNodeTypes = new LinkedHashSet();

  private boolean nodeTypeRequired;

  private UUID rosterHierarchyId;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getI18nName()
  {
    return ContentReaderManager.getText( this.cmAssetCode, this.nameCmKey );
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public boolean isPrimary()
  {
    return primary;
  }

  public String getPrimaryCMKey()
  {
    return ( "" + this.primary ).toUpperCase();
  }

  public void setPrimary( boolean primary )
  {
    this.primary = primary;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean status )
  {
    this.active = status;
  }

  public String getActiveCMKey()
  {
    return ( "" + this.active ).toUpperCase();
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmNameAsset )
  {
    this.cmAssetCode = cmNameAsset;
  }

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public void setNameCmKey( String cmNameKey )
  {
    this.nameCmKey = cmNameKey;
  }

  public Set getNodes()
  {
    return nodes;
  }

  public List getNodesAsList()
  {
    return Collections.unmodifiableList( new ArrayList( nodes ) );
  }

  public void setNodes( Set nodes )
  {
    this.nodes = nodes;
  }

  public void addNode( Node node )
  {
    this.nodes.add( node );
  }

  /**
   * Returns a set of hierarchy node types.
   * 
   * @return a <code>Set</code> of {@link HierarchyNodeType} objects.
   */
  public Set getHierarchyNodeTypes()
  {
    return hierarchyNodeTypes;
  }

  /**
   * Sets this hierarchy's hierarchy node types.
   * 
   * @param nodeTypes a <code>Set</code> of {@link HierarchyNodeType} objects.
   */
  public void setHierarchyNodeTypes( Set nodeTypes )
  {
    this.hierarchyNodeTypes = nodeTypes;
  }

  public boolean isDeleted()
  {
    return deleted;
  }

  public void setDeleted( boolean deleted )
  {
    this.deleted = deleted;
  }

  public boolean isNodeTypeRequired()
  {
    return nodeTypeRequired;
  }

  public void setNodeTypeRequired( boolean nodeTypeRequired )
  {
    this.nodeTypeRequired = nodeTypeRequired;
  }

  public UUID getRosterHierarchyId()
  {
    return rosterHierarchyId;
  }

  public void setRosterHierarchyId( UUID rosterHierarchyId )
  {
    this.rosterHierarchyId = rosterHierarchyId;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof Hierarchy ) )
    {
      return false;
    }

    final Hierarchy hierarchy = (Hierarchy)o;

    if ( getCmAssetCode() != null ? !getCmAssetCode().equals( hierarchy.getCmAssetCode() ) : hierarchy.getCmAssetCode() != null )
    {
      return false;
    }
    if ( getNameCmKey() != null ? !getNameCmKey().equals( hierarchy.getNameCmKey() ) : hierarchy.getNameCmKey() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getCmAssetCode() != null ? getCmAssetCode().hashCode() : 0;
    result = 29 * result + ( getNameCmKey() != null ? getNameCmKey().hashCode() : 0 );
    return result;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Hierarchy [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{name=" ).append( this.getName() ).append( "}, " );
    buf.append( "{description=" ).append( this.getDescription() ).append( "}, " );
    buf.append( "{active=" ).append( this.isActive() ).append( "}, " );
    buf.append( "{primary=" ).append( this.isPrimary() ).append( "}, " );
    buf.append( "{cmAssetCode=" ).append( this.getCmAssetCode() ).append( "}, " );
    buf.append( "{nameCmKey=" ).append( this.getNameCmKey() ).append( "}, " );
    buf.append( "]" );

    return buf.toString();
  }
}

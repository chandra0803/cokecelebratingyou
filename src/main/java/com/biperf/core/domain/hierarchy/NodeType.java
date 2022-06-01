/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/hierarchy/NodeType.java,v $
 */

package com.biperf.core.domain.hierarchy;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * NodeType.
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
 * <td>Apr 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeType extends BaseDomain
{

  String name = "";
  String cmAssetCode = "";
  String nameCmKey = "";
  boolean deleted = false;

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String name )
  {
    this.cmAssetCode = name;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public boolean isDeleted()
  {
    return deleted;
  }

  public void setDeleted( boolean deleted )
  {
    this.deleted = deleted;
  }

  /**
   * getter for returning the cm value for the cmAssetCode and key
   * 
   * @return String
   */
  public String getI18nName()
  {
    return ContentReaderManager.getText( this.cmAssetCode, this.nameCmKey );
  }

  public String getNodeTypeName()
  {
    return getI18nName();
  }

  /**
   * Overridden from
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
    if ( ! ( o instanceof NodeType ) )
    {
      return false;
    }

    final NodeType nodeType = (NodeType)o;

    if ( getCmAssetCode() != null ? !getCmAssetCode().equals( nodeType.getCmAssetCode() ) : nodeType.getCmAssetCode() != null )
    {
      return false;
    }
    if ( getNameCmKey() != null ? !getNameCmKey().equals( nodeType.getNameCmKey() ) : nodeType.getNameCmKey() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
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

  public boolean isDefaultNodeType()
  {
    return nameCmKey.equals( "DEFAULT_NODE_TYPE" );
  }

}

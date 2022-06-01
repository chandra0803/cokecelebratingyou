/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/nodetype/NodeTypeForm.java,v $
 */

package com.biperf.core.ui.nodetype;

import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * NodeTypeForm <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Apr 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeTypeForm extends BaseForm
{
  private String name = "";
  private String cmAssetCode = "";
  private String nameCmKey = "";
  private String id = null;
  private long version = 0;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
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

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public NodeType toBusinessObject()
  {
    NodeType nodeType = new NodeType();
    nodeType.setCmAssetCode( getCmAssetCode() );
    nodeType.setNameCmKey( getNameCmKey() );
    nodeType.setName( getName() );

    if ( getId() != null )
    {
      nodeType.setId( new Long( getId() ) );
      nodeType.setVersion( new Long( getVersion() ) );
    }
    return nodeType;
  }

  /**
   * @param nodeType
   */
  public void load( NodeType nodeType )
  {
    setId( "" + nodeType.getId() );
    setVersion( nodeType.getVersion().longValue() );
    // todo jjd add flag to disable editing for non-default locales
    setName( ContentReaderManager.getText( nodeType.getCmAssetCode(), nodeType.getNameCmKey() ) );
    setNameCmKey( nodeType.getNameCmKey() );
    setCmAssetCode( nodeType.getCmAssetCode() );
  }

}

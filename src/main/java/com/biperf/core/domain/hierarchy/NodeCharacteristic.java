/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/hierarchy/NodeCharacteristic.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.hierarchy;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.enums.DynaPickListType;

/**
 * NodeCharacteristic.
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
 * <td>May 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeCharacteristic extends BaseDomain
{
  private Node node;
  private NodeTypeCharacteristicType nodeTypeCharacteristicType;
  private String characteristicValue;
  private UUID rosterNodeCharId;

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "NodeCharacteristic [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{node=" + this.getNode() + "}, " );
    buf.append( "{nodeTypeCharacteristicType=" + this.getNodeTypeCharacteristicType() + "}, " );
    buf.append( "{characteristicValue=" + this.getCharacteristicValue() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Does equals on the Business Key ( userId and characteristicId ) Overridden from
   * 
   * @see java.lang.Object#toString()
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof NodeCharacteristic ) )
    {
      return false;
    }

    final NodeCharacteristic nodeChar = (NodeCharacteristic)object;

    if ( getNode() != null ? !getNode().equals( nodeChar.getNode() ) : nodeChar.getNode() != null )
    {
      return false;
    }
    if ( getNodeTypeCharacteristicType() != null ? !getNodeTypeCharacteristicType().equals( nodeChar.getNodeTypeCharacteristicType() ) : nodeChar.getNodeTypeCharacteristicType() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Does hashCode on the Business Key ( userId and characteristicId ) Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    if ( getNode() != null && getNodeTypeCharacteristicType() != null )
    {
      return 29 * getNode().hashCode() + getNodeTypeCharacteristicType().hashCode();
    }
    return 0;
  }

  /**
   * @return value of node property
   */
  public Node getNode()
  {
    return node;
  }

  /**
   * @param node value for node property
   */
  public void setNode( Node node )
  {
    this.node = node;
  }

  /**
   * @return value of nodeTypeCharacteristicType property
   */
  public NodeTypeCharacteristicType getNodeTypeCharacteristicType()
  {
    return nodeTypeCharacteristicType;
  }

  /**
   * @param nodeTypeCharacteristicType value for nodeTypeCharacteristicType property
   */
  public void setNodeTypeCharacteristicType( NodeTypeCharacteristicType nodeTypeCharacteristicType )
  {
    this.nodeTypeCharacteristicType = nodeTypeCharacteristicType;
  }

  /**
   * @return value of characteristicValue property
   */
  public String getCharacteristicValue()
  {
    return characteristicValue;
  }

  /**
   * @param characteristicValue for characteristicValue property
   */
  public void setCharacteristicValue( String characteristicValue )
  {
    this.characteristicValue = characteristicValue;
  }

  /**
   * This is for display pages where the actual name is needed instead of the code stored in the
   * database.
   * 
   * @return list
   */
  public List getCharacteristicDisplayValueList()
  {
    List displayValueList = new ArrayList();
    if ( characteristicValue.indexOf( "," ) != -1 )
    {
      StringTokenizer tokens = new StringTokenizer( characteristicValue, "," );
      int i = 0;
      while ( tokens.hasMoreTokens() )
      {
        displayValueList.add( DynaPickListType.lookup( nodeTypeCharacteristicType.getPlName(), tokens.nextToken() ) );
        i++;
      }
    }
    else
    {
      displayValueList.add( DynaPickListType.lookup( nodeTypeCharacteristicType.getPlName(), characteristicValue ) );
    }

    return displayValueList;
  }

  public UUID getRosterNodeCharId()
  {
    return rosterNodeCharId;
  }

  public void setRosterNodeCharId( UUID rosterNodeCharId )
  {
    this.rosterNodeCharId = rosterNodeCharId;
  }

}

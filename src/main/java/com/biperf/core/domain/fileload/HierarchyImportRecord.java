/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/fileload/HierarchyImportRecord.java,v $
 */

package com.biperf.core.domain.fileload;

/*
 * HierarchyImportRecord <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 19, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class HierarchyImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The hierarchy node's intended name.
   */
  private String name;

  /**
   * The hierarchy node's current name.
   */
  private String oldNodeName;

  /**
   * The hierarchy node's name where participants/users should be moved.
   */
  private String moveToNodeName;

  /**
   * A description of the hierarchy node.
   */
  private String description;

  /**
   * The ID of the hierarchy node's node type.
   */
  private Long nodeTypeId;

  /**
   * The name of the hierarchy node's node type. Used only when displaying this hierarchy import
   * record to the user.
   */
  private String nodeTypeName;

  /**
   * The name of the intended parent node of this hierarchy node.
   */
  private String parentNodeName;

  /**
   * The name of the current parent node of this hierarchy node.
   */
  private String oldParentNodeName;

  /**
   * The IDs of the characteristics for this hierarchy node.
   */
  private Long characteristicId1;
  private Long characteristicId2;
  private Long characteristicId3;
  private Long characteristicId4;
  private Long characteristicId5;
  private Long characteristicId6;
  private Long characteristicId7;
  private Long characteristicId8;
  private Long characteristicId9;
  private Long characteristicId10;

  /**
   * The names of the characteristics for this hierarchy node. Used only when displaying this
   * hierarchy import record to the user.
   */
  private String characteristicName1;
  private String characteristicName2;
  private String characteristicName3;
  private String characteristicName4;
  private String characteristicName5;
  private String characteristicName6;
  private String characteristicName7;
  private String characteristicName8;
  private String characteristicName9;
  private String characteristicName10;

  /**
   * The values of the characteristics for this hierarchy node.
   */
  private String characteristicValue1;
  private String characteristicValue2;
  private String characteristicValue3;
  private String characteristicValue4;
  private String characteristicValue5;
  private String characteristicValue6;
  private String characteristicValue7;
  private String characteristicValue8;
  private String characteristicValue9;
  private String characteristicValue10;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getDescription()
  {
    return description;
  }

  public String getName()
  {
    return name;
  }

  public Long getCharacteristicId1()
  {
    return characteristicId1;
  }

  public Long getCharacteristicId2()
  {
    return characteristicId2;
  }

  public Long getCharacteristicId3()
  {
    return characteristicId3;
  }

  public Long getCharacteristicId4()
  {
    return characteristicId4;
  }

  public Long getCharacteristicId5()
  {
    return characteristicId5;
  }

  public String getCharacteristicName1()
  {
    return characteristicName1;
  }

  public String getCharacteristicName2()
  {
    return characteristicName2;
  }

  public String getCharacteristicName3()
  {
    return characteristicName3;
  }

  public String getCharacteristicName4()
  {
    return characteristicName4;
  }

  public String getCharacteristicName5()
  {
    return characteristicName5;
  }

  public String getCharacteristicValue1()
  {
    return characteristicValue1;
  }

  public String getCharacteristicValue2()
  {
    return characteristicValue2;
  }

  public String getCharacteristicValue3()
  {
    return characteristicValue3;
  }

  public String getCharacteristicValue4()
  {
    return characteristicValue4;
  }

  public String getCharacteristicValue5()
  {
    return characteristicValue5;
  }

  public Long getNodeTypeId()
  {
    return nodeTypeId;
  }

  public String getNodeTypeName()
  {
    return nodeTypeName;
  }

  public String getParentNodeName()
  {
    return parentNodeName;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public void setCharacteristicId1( Long characteristicId1 )
  {
    this.characteristicId1 = characteristicId1;
  }

  public void setCharacteristicId2( Long characteristicId2 )
  {
    this.characteristicId2 = characteristicId2;
  }

  public void setCharacteristicId3( Long characteristicId3 )
  {
    this.characteristicId3 = characteristicId3;
  }

  public void setCharacteristicId4( Long characteristicId4 )
  {
    this.characteristicId4 = characteristicId4;
  }

  public void setCharacteristicId5( Long characteristicId5 )
  {
    this.characteristicId5 = characteristicId5;
  }

  public void setCharacteristicName1( String characteristicName1 )
  {
    this.characteristicName1 = characteristicName1;
  }

  public void setCharacteristicName2( String characteristicName2 )
  {
    this.characteristicName2 = characteristicName2;
  }

  public void setCharacteristicName3( String characteristicName3 )
  {
    this.characteristicName3 = characteristicName3;
  }

  public void setCharacteristicName4( String characteristicName4 )
  {
    this.characteristicName4 = characteristicName4;
  }

  public void setCharacteristicName5( String characteristicName5 )
  {
    this.characteristicName5 = characteristicName5;
  }

  public void setCharacteristicValue1( String characteristicValue1 )
  {
    this.characteristicValue1 = characteristicValue1;
  }

  public void setCharacteristicValue2( String characteristicValue2 )
  {
    this.characteristicValue2 = characteristicValue2;
  }

  public void setCharacteristicValue3( String characteristicValue3 )
  {
    this.characteristicValue3 = characteristicValue3;
  }

  public void setCharacteristicValue4( String characteristicValue4 )
  {
    this.characteristicValue4 = characteristicValue4;
  }

  public void setCharacteristicValue5( String characteristicValue5 )
  {
    this.characteristicValue5 = characteristicValue5;
  }

  public void setNodeTypeId( Long nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public void setNodeTypeName( String nodeTypeName )
  {
    this.nodeTypeName = nodeTypeName;
  }

  public void setParentNodeName( String parentNodeName )
  {
    this.parentNodeName = parentNodeName;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the given object and this object are equal; returns false otherwise.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to compare to this object.
   * @return true if the given object and this object are equal; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof HierarchyImportRecord ) )
    {
      return false;
    }

    HierarchyImportRecord hierarchyImportRecord = (HierarchyImportRecord)object;

    if ( this.getId() != null && this.getId().equals( hierarchyImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Returns a string representation of this object.
   * 
   * @see java.lang.Object#toString()
   * @return a string representation of this object.
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "HierarchyImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public String getMoveToNodeName()
  {
    return moveToNodeName;
  }

  public void setMoveToNodeName( String moveToNodeName )
  {
    this.moveToNodeName = moveToNodeName;
  }

  public String getOldNodeName()
  {
    return oldNodeName;
  }

  public void setOldNodeName( String oldNodeName )
  {
    this.oldNodeName = oldNodeName;
  }

  public String getOldParentNodeName()
  {
    return oldParentNodeName;
  }

  public void setOldParentNodeName( String oldParentNodeName )
  {
    this.oldParentNodeName = oldParentNodeName;
  }

  public Long getCharacteristicId6()
  {
    return characteristicId6;
  }

  public void setCharacteristicId6( Long characteristicId6 )
  {
    this.characteristicId6 = characteristicId6;
  }

  public Long getCharacteristicId7()
  {
    return characteristicId7;
  }

  public void setCharacteristicId7( Long characteristicId7 )
  {
    this.characteristicId7 = characteristicId7;
  }

  public Long getCharacteristicId8()
  {
    return characteristicId8;
  }

  public void setCharacteristicId8( Long characteristicId8 )
  {
    this.characteristicId8 = characteristicId8;
  }

  public Long getCharacteristicId9()
  {
    return characteristicId9;
  }

  public void setCharacteristicId9( Long characteristicId9 )
  {
    this.characteristicId9 = characteristicId9;
  }

  public Long getCharacteristicId10()
  {
    return characteristicId10;
  }

  public void setCharacteristicId10( Long characteristicId10 )
  {
    this.characteristicId10 = characteristicId10;
  }

  public String getCharacteristicName6()
  {
    return characteristicName6;
  }

  public void setCharacteristicName6( String characteristicName6 )
  {
    this.characteristicName6 = characteristicName6;
  }

  public String getCharacteristicName7()
  {
    return characteristicName7;
  }

  public void setCharacteristicName7( String characteristicName7 )
  {
    this.characteristicName7 = characteristicName7;
  }

  public String getCharacteristicName8()
  {
    return characteristicName8;
  }

  public void setCharacteristicName8( String characteristicName8 )
  {
    this.characteristicName8 = characteristicName8;
  }

  public String getCharacteristicName9()
  {
    return characteristicName9;
  }

  public void setCharacteristicName9( String characteristicName9 )
  {
    this.characteristicName9 = characteristicName9;
  }

  public String getCharacteristicName10()
  {
    return characteristicName10;
  }

  public void setCharacteristicName10( String characteristicName10 )
  {
    this.characteristicName10 = characteristicName10;
  }

  public String getCharacteristicValue6()
  {
    return characteristicValue6;
  }

  public void setCharacteristicValue6( String characteristicValue6 )
  {
    this.characteristicValue6 = characteristicValue6;
  }

  public String getCharacteristicValue7()
  {
    return characteristicValue7;
  }

  public void setCharacteristicValue7( String characteristicValue7 )
  {
    this.characteristicValue7 = characteristicValue7;
  }

  public String getCharacteristicValue8()
  {
    return characteristicValue8;
  }

  public void setCharacteristicValue8( String characteristicValue8 )
  {
    this.characteristicValue8 = characteristicValue8;
  }

  public String getCharacteristicValue9()
  {
    return characteristicValue9;
  }

  public void setCharacteristicValue9( String characteristicValue9 )
  {
    this.characteristicValue9 = characteristicValue9;
  }

  public String getCharacteristicValue10()
  {
    return characteristicValue10;
  }

  public void setCharacteristicValue10( String characteristicValue10 )
  {
    this.characteristicValue10 = characteristicValue10;
  }
}

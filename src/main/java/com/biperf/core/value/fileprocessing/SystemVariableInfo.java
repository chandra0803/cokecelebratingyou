
package com.biperf.core.value.fileprocessing;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Stores Operation Info
 * 
 * @author Prabu Babu
 */
public class SystemVariableInfo implements Serializable, Cloneable
{
  private int systemVariableId = 0;

  private String varName = "";

  private String varGroup = "";

  private String varType = "";

  private String description = "";

  private int displayOrder = 0;

  private String valueText = "";

  private int intValue = 0;

  public SystemVariableInfo()
  {
  }

  /**
   * Getter for property systemVariableId.
   * 
   * @return Value of property systemVariableId.
   */
  public int getSystemVariableId()
  {
    return systemVariableId;
  }

  /**
   * Setter for property systemVariableId.
   * 
   * @param systemVariableId
   *            New valueText of property systemVariableId.
   */
  public void setSystemVariableId( int systemVariableId )
  {
    this.systemVariableId = systemVariableId;
  }

  /**
   * Getter for property varName.
   * 
   * @return Value of property varName.
   */
  public java.lang.String getVarName()
  {
    return varName;
  }

  /**
   * Setter for property varName.
   * 
   * @param varName
   *            New valueText of property varName.
   */
  public void setVarName( java.lang.String varName )
  {
    this.varName = varName;
  }

  /**
   * Getter for property operationGroup.
   * 
   * @return Value of property operationGroup.
   */
  public java.lang.String getVarGroup()
  {
    return varGroup;
  }

  /**
   * Setter for property operationGroup.
   * 
   * @param operationGroup
   *            New valueText of property operationGroup.
   */
  public void setVarGroup( java.lang.String varGroup )
  {
    this.varGroup = varGroup;
  }

  /**
   * Getter for property varType.
   * 
   * @return Value of property varType.
   */
  public java.lang.String getVarType()
  {
    return varType;
  }

  /**
   * Setter for property varType.
   * 
   * @param varType
   *            New valueText of property varType.
   */
  public void setVarType( java.lang.String varType )
  {
    this.varType = varType;
  }

  /**
   * Getter for property description.
   * 
   * @return Value of property description.
   */
  public java.lang.String getDescription()
  {
    return description;
  }

  /**
   * Setter for property description.
   * 
   * @param description
   *            New valueText of property description.
   */
  public void setDescription( java.lang.String description )
  {
    this.description = description;
  }

  /**
   * Getter for property displayOrder.
   * 
   * @return Value of property displayOrder.
   */
  public int getDisplayOrder()
  {
    return displayOrder;
  }

  /**
   * Setter for property displayOrder.
   * 
   * @param displayOrder
   *            New valueText of property displayOrder.
   */
  public void setDisplayOrder( int displayOrder )
  {
    this.displayOrder = displayOrder;
  }

  /**
   * Getter for property valueText.
   * 
   * @return Value of property valueText.
   */
  public java.lang.String getValueText()
  {
    return valueText;
  }

  /**
   * Setter for property valueText.
   * 
   * @param valueText
   *            New valueText of property valueText.
   */
  public void setValueText( java.lang.String valueText )
  {
    this.valueText = valueText;
  }

  /**
   * Getter for property intValue.
   * 
   * @return Value of property intValue.
   */
  public int getIntValue()
  {
    return intValue;
  }

  /**
   * Setter for property intValue.
   * 
   * @param intValue
   *            New valueText of property intValue.
   */
  public void setIntValue( int intValue )
  {
    this.intValue = intValue;
  }

  public Object clone() throws CloneNotSupportedException
  {

    SystemVariableInfo t_operationInfo = new SystemVariableInfo();
    t_operationInfo.setDisplayOrder( displayOrder );
    t_operationInfo.setIntValue( intValue );
    t_operationInfo.setDescription( description );
    t_operationInfo.setVarGroup( varGroup );
    t_operationInfo.setSystemVariableId( systemVariableId );
    t_operationInfo.setVarName( varName );
    t_operationInfo.setVarType( varType );
    t_operationInfo.setValueText( valueText );
    return t_operationInfo;
  }

  private void writeObject( java.io.ObjectOutputStream out ) throws IOException
  {
    HashMap<String, Object> t_hsmap = new HashMap<String, Object>();
    t_hsmap.put( "valueText", valueText );
    t_hsmap.put( "varName", varName );
    t_hsmap.put( "description", description );
    t_hsmap.put( "varType", varType );
    t_hsmap.put( "operationGroup", varGroup );
    t_hsmap.put( "displayOrder", new Integer( displayOrder ) );
    t_hsmap.put( "systemVariableId", new Integer( systemVariableId ) );
    out.writeObject( t_hsmap );
  }

  @SuppressWarnings( "unchecked" )
  private void readObject( java.io.ObjectInputStream in ) throws IOException, ClassNotFoundException
  {
    HashMap<String, Object> t_hsmap = (HashMap<String, Object>)in.readObject();
    valueText = (String)t_hsmap.get( "valueText" );
    varName = (String)t_hsmap.get( "varName" );
    description = (String)t_hsmap.get( "description" );
    varType = (String)t_hsmap.get( "varType" );
    varGroup = (String)t_hsmap.get( "operationGroup" );
    displayOrder = ( (Integer)t_hsmap.get( "displayOrder" ) ).intValue();
    systemVariableId = ( (Integer)t_hsmap.get( "systemVariableId" ) ).intValue();
  }
}


package com.biperf.core.value.fileprocessing;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Stores Operation Info
 * 
 * @author Prabu Babu
 */
public class OperationInfo extends SystemVariableInfo implements Serializable, Cloneable
{
  private int intValue = 0;

  private String pattern = "";

  private String operationText = ""; // Abbreviation for operation, used

  // as a key to identify operation
  private String selected = "";

  public OperationInfo()
  {
  }

  /**
   * Getter for property operationId.
   * 
   * @return Value of property operationId.
   */
  public int getOperationId()
  {
    return this.getSystemVariableId();
  }

  /**
   * Setter for property operationId.
   * 
   * @param operationId
   *            New value of property operationId.
   */
  public void setOperationId( int operationId )
  {
    this.setSystemVariableId( operationId );
  }

  /**
   * Getter for property operationName.
   * 
   * @return Value of property operationName.
   */
  public java.lang.String getOperationName()
  {
    return this.getVarName();
  }

  /**
   * Setter for property operationName.
   * 
   * @param operationName
   *            New value of property operationName.
   */
  public void setOperationName( java.lang.String operationName )
  {
    this.setVarName( operationName );
  }

  /**
   * Getter for property operationGroup.
   * 
   * @return Value of property operationGroup.
   */
  public java.lang.String getOperationGroup()
  {
    return this.getVarGroup();
  }

  /**
   * Setter for property operationGroup.
   * 
   * @param operationGroup
   *            New value of property operationGroup.
   */
  public void setOperationGroup( java.lang.String operationGroup )
  {
    this.setVarGroup( operationGroup );
  }

  /**
   * Getter for property operationType.
   * 
   * @return Value of property operationType.
   */
  public java.lang.String getOperationType()
  {
    return this.getVarType();
  }

  /**
   * Setter for property operationType.
   * 
   * @param operationType
   *            New value of property operationType.
   */
  public void setOperationType( java.lang.String operationType )
  {
    this.setVarType( operationType );
  }

  /**
   * Getter for property operationDesc.
   * 
   * @return Value of property operationDesc.
   */
  public java.lang.String getOperationDesc()
  {
    return this.getDescription();
  }

  /**
   * Setter for property operationDesc.
   * 
   * @param operationDesc
   *            New value of property operationDesc.
   */
  public void setOperationDesc( java.lang.String operationDesc )
  {
    this.setDescription( operationDesc );
  }

  /**
   * Getter for property value.
   * 
   * @return Value of property value.
   */
  public java.lang.String getValue()
  {
    return this.getValueText();
  }

  /**
   * Setter for property value.
   * 
   * @param value
   *            New value of property value.
   */
  public void setValue( java.lang.String value )
  {
    this.setValueText( value );
  }

  /**
   * Getter for property pattern.
   * 
   * @return Value of property pattern.
   */
  public java.lang.String getPattern()
  {
    return pattern;
  }

  /**
   * Setter for property pattern.
   * 
   * @param pattern
   *            New value of property pattern.
   */
  public void setPattern( java.lang.String pattern )
  {
    this.pattern = pattern;
  }

  /**
   * Getter for property operationText.
   * 
   * @return Value of property operationText.
   */
  public java.lang.String getOperationText()
  {
    return operationText;
  }

  /**
   * Setter for property operationText.
   * 
   * @param operationText
   *            New value of property operationText.
   */
  public void setOperationText( java.lang.String operationText )
  {
    this.operationText = operationText;
  }

  /**
   * Getter for property selected.
   * 
   * @return Value of property selected.
   */
  public java.lang.String getSelected()
  {
    return selected;
  }

  /**
   * Setter for property selected.
   * 
   * @param selected
   *            New value of property selected.
   */
  public void setSelected( java.lang.String selected )
  {
    this.selected = selected;
  }

  public Object clone() throws CloneNotSupportedException
  {

    OperationInfo t_operationInfo = new OperationInfo();
    t_operationInfo.setDisplayOrder( this.getDisplayOrder() );
    t_operationInfo.setIntValue( intValue );
    t_operationInfo.setOperationDesc( this.getDescription() );
    t_operationInfo.setOperationGroup( this.getOperationGroup() );
    t_operationInfo.setOperationId( this.getOperationId() );
    t_operationInfo.setOperationName( this.getOperationName() );
    t_operationInfo.setOperationText( operationText );
    t_operationInfo.setOperationType( this.getOperationType() );
    t_operationInfo.setPattern( pattern );
    t_operationInfo.setSelected( selected );
    t_operationInfo.setValue( this.getValue() );
    return t_operationInfo;
  }

  private void writeObject( java.io.ObjectOutputStream out ) throws IOException
  {
    HashMap<String, Object> t_hsmap = new HashMap<String, Object>();
    t_hsmap.put( "value", this.getValue() );
    t_hsmap.put( "pattern", pattern );
    t_hsmap.put( "operationName", this.getOperationName() );
    t_hsmap.put( "operationDesc", this.getDescription() );
    t_hsmap.put( "operationText", operationText );
    t_hsmap.put( "operationType", this.getOperationType() );
    t_hsmap.put( "operationGroup", this.getOperationGroup() );
    t_hsmap.put( "displayOrder", new Integer( this.getDisplayOrder() ) );
    t_hsmap.put( "operationId", new Integer( this.getOperationId() ) );
    t_hsmap.put( "selected", selected );
    out.writeObject( t_hsmap );
  }

  @SuppressWarnings( "unchecked" )
  private void readObject( java.io.ObjectInputStream in ) throws IOException, ClassNotFoundException
  {
    selected = "TRUE";
    HashMap<String, Object> t_hsmap = (HashMap<String, Object>)in.readObject();
    this.setValue( (String)t_hsmap.get( "value" ) );
    pattern = (String)t_hsmap.get( "pattern" );
    this.setOperationName( (String)t_hsmap.get( "operationName" ) );
    this.setOperationDesc( (String)t_hsmap.get( "operationDesc" ) );
    operationText = (String)t_hsmap.get( "operationText" );
    this.setOperationType( (String)t_hsmap.get( "operationType" ) );
    this.setOperationGroup( (String)t_hsmap.get( "operationGroup" ) );
    this.setDisplayOrder( ( (Integer)t_hsmap.get( "displayOrder" ) ).intValue() );
    this.setOperationId( ( (Integer)t_hsmap.get( "operationId" ) ).intValue() );
    selected = (String)t_hsmap.get( "selected" );
  }
}

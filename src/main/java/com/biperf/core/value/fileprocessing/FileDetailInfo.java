
package com.biperf.core.value.fileprocessing;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores Project Info
 * 
 * @author Hima Kapalli
 */
public class FileDetailInfo implements Serializable, Comparable<FileDetailInfo>
{
  private int fileDetailId = 0;

  private int fileId = 0;

  private String columnName = "";

  private String columnLabel = "";

  private String columnDesc = "";

  private String columnDataType = "";

  private int columnLength = 0;

  private int columnPrecision = 0;

  private String columnAddlQualifier = "";

  private int inputOrder = 0;

  private int outputOrder = 0;

  private String uniqueString = ""; // InputOrder + outputOrder is used as
                                    // unique identifier

  private String value = "";

  private ArrayList<OperationInfo> fileDetailOperations = new ArrayList<OperationInfo>();

  private ArrayList<String> errorMessage = new ArrayList<String>();

  public FileDetailInfo()
  {
    fileDetailOperations = new ArrayList<OperationInfo>();
  }

  /**
   * Getter for property fileDetailId.
   * 
   * @return Value of property fileDetailId.
   */
  public int getFileDetailId()
  {
    return fileDetailId;
  }

  /**
   * Setter for property fileDetailId.
   * 
   * @param fileDetailId
   *            New value of property fileDetailId.
   */
  public void setFileDetailId( int fileDetailId )
  {
    this.fileDetailId = fileDetailId;
  }

  /**
   * Getter for property fileId.
   * 
   * @return Value of property fileId.
   */
  public int getFileId()
  {
    return fileId;
  }

  /**
   * Setter for property fileId.
   * 
   * @param fileId
   *            New value of property fileId.
   */
  public void setFileId( int fileId )
  {
    this.fileId = fileId;
  }

  /**
   * Getter for property columnName.
   * 
   * @return Value of property columnName.
   */
  public java.lang.String getColumnName()
  {
    return columnName;
  }

  /**
   * Setter for property columnName.
   * 
   * @param columnName
   *            New value of property columnName.
   */
  public void setColumnName( java.lang.String columnName )
  {
    this.columnName = columnName;
  }

  /**
   * Getter for property columnLabel.
   * 
   * @return Value of property columnLabel.
   */
  public java.lang.String getColumnLabel()
  {
    return columnLabel;
  }

  /**
   * Setter for property columnLabel.
   * 
   * @param columnLabel
   *            New value of property columnLabel.
   */
  public void setColumnLabel( java.lang.String columnLabel )
  {
    this.columnLabel = columnLabel;
  }

  /**
   * Getter for property columnDesc.
   * 
   * @return Value of property columnDesc.
   */
  public java.lang.String getColumnDesc()
  {
    return columnDesc;
  }

  /**
   * Setter for property columnDesc.
   * 
   * @param columnDesc
   *            New value of property columnDesc.
   */
  public void setColumnDesc( java.lang.String columnDesc )
  {
    this.columnDesc = columnDesc;
  }

  /**
   * Getter for property columnDataType.
   * 
   * @return Value of property columnDataType.
   */
  public java.lang.String getColumnDataType()
  {
    return columnDataType;
  }

  /**
   * Setter for property columnDataType.
   * 
   * @param columnDataType
   *            New value of property columnDataType.
   */
  public void setColumnDataType( java.lang.String columnDataType )
  {
    this.columnDataType = columnDataType;
  }

  /**
   * Getter for property columnLength.
   * 
   * @return Value of property columnLength.
   */
  public int getColumnLength()
  {
    return columnLength;
  }

  /**
   * Setter for property columnLength.
   * 
   * @param columnLength
   *            New value of property columnLength.
   */
  public void setColumnLength( int columnLength )
  {
    this.columnLength = columnLength;
  }

  /**
   * Getter for property columnPrecision.
   * 
   * @return Value of property columnPrecision.
   */
  public int getColumnPrecision()
  {
    return columnPrecision;
  }

  /**
   * Setter for property columnPrecision.
   * 
   * @param columnPrecision
   *            New value of property columnPrecision.
   */
  public void setColumnPrecision( int columnPrecision )
  {
    this.columnPrecision = columnPrecision;
  }

  /**
   * Getter for property columnAddlQualifier.
   * 
   * @return Value of property columnAddlQualifier.
   */
  public java.lang.String getColumnAddlQualifier()
  {
    return columnAddlQualifier;
  }

  /**
   * Setter for property columnAddlQualifier.
   * 
   * @param columnAddlQualifier
   *            New value of property columnAddlQualifier.
   */
  public void setColumnAddlQualifier( java.lang.String columnAddlQualifier )
  {
    this.columnAddlQualifier = columnAddlQualifier;
  }

  /**
   * Getter for property inputOrder.
   * 
   * @return Value of property inputOrder.
   */
  public int getInputOrder()
  {
    return inputOrder;
  }

  /**
   * Setter for property inputOrder.
   * 
   * @param inputOrder
   *            New value of property inputOrder.
   */
  public void setInputOrder( int inputOrder )
  {
    this.inputOrder = inputOrder;
  }

  /**
   * Getter for property outputOrder.
   * 
   * @return Value of property outputOrder.
   */
  public int getOutputOrder()
  {
    return outputOrder;
  }

  /**
   * Setter for property outputOrder.
   * 
   * @param outputOrder
   *            New value of property outputOrder.
   */
  public void setOutputOrder( int outputOrder )
  {
    this.outputOrder = outputOrder;
  }

  /**
   * Getter for property uniqueString.
   * 
   * @return Value of property uniqueString.
   */
  public java.lang.String getUniqueString()
  {
    return uniqueString;
  }

  /**
   * Setter for property uniqueString.
   * 
   * @param uniqueString
   *            New value of property uniqueString.
   */
  public void setUniqueString( java.lang.String uniqueString )
  {
    this.uniqueString = uniqueString;
  }

  /**
   * Getter for property value.
   * 
   * @return Value of property value.
   */
  public java.lang.String getValue()
  {
    return value;
  }

  /**
   * Setter for property value.
   * 
   * @param value
   *            New value of property value.
   */
  public void setValue( java.lang.String value )
  {
    this.value = value;
  }

  /**
   * Getter for property fileDetailOperations.
   * 
   * @return Value of property fileDetailOperations.
   */
  public java.util.ArrayList<OperationInfo> getFileDetailOperations()
  {
    return fileDetailOperations;
  }

  /**
   * Setter for property fileDetailOperations.
   * 
   * @param fileDetailOperations
   *            New value of property fileDetailOperations.
   */
  public void setFileDetailOperations( java.util.ArrayList<OperationInfo> fileDetailOperations )
  {
    this.fileDetailOperations = fileDetailOperations;
  }

  @SuppressWarnings( "unchecked" )
  private void writeObject( java.io.ObjectOutputStream out ) throws IOException
  {
    HashMap t_hsmap = new HashMap();
    t_hsmap.put( "fileDetailId", new Integer( fileDetailId ) );
    t_hsmap.put( "fileId", new Integer( fileId ) );
    t_hsmap.put( "columnName", columnName );
    t_hsmap.put( "columnLabel", columnLabel );
    t_hsmap.put( "columnDesc", columnDesc );
    t_hsmap.put( "columnDataType", columnDataType );
    t_hsmap.put( "columnLength", new Integer( columnLength ) );
    t_hsmap.put( "columnPrecision", new Integer( columnPrecision ) );
    t_hsmap.put( "columnAddlQualifier", columnAddlQualifier );
    t_hsmap.put( "inputOrder", new Integer( inputOrder ) );
    t_hsmap.put( "outputOrder", new Integer( outputOrder ) );
    t_hsmap.put( "value", value );
    out.writeObject( t_hsmap );
    out.writeObject( fileDetailOperations );
  }

  /**
   * @return
   */
  public ArrayList<String> getErrorMessage()
  {
    return errorMessage;
  }

  /**
   * @param list
   */
  public void setErrorMessage( ArrayList<String> list )
  {
    errorMessage = list;
  }

  /**
   * Setter for property errorMessage.
   * 
   * @param errorMessage
   */
  public void setErrorMessage( String errorMessage )
  {
    this.errorMessage.add( errorMessage );
  }

  public int getErrorMessageSize()
  {
    if ( errorMessage == null )
    {
      return 0;
    }
    return errorMessage.size();
  }

  /**
   * Overridden from @see java.lang.Comparable#compareTo(java.lang.Object)
   * 
   * @param arg0
   * @return
   */
  @Override
  public int compareTo( FileDetailInfo arg0 )
  {
    return this.columnName.compareToIgnoreCase( arg0.columnName );
  }
}


package com.biperf.core.value.fileprocessing;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Stores Operation Result Info
 * 
 * @author Prabu Babu
 */
public class OperationResultInfo implements Serializable
{
  private int projectId = 0;
  private int fileId = 0;
  private String status = "Success";
  private String reason = "";
  private int columnCount = 0;
  private int maxColumnCount = 0;
  private int rowCount = 0;
  private String inputFileName = "";
  private String inputFileFormat = "";
  private String outputFileName = "";
  private String outputFileFormat = "";
  private String clientName = "";
  private String programName = "";
  private ArrayList<String> contactList = new ArrayList<String>();
  private Date dateReceived = java.util.Calendar.getInstance().getTime();
  private String emailTextAttach = "N";
  private String fileProcessing = "";
  private int result = 0;
  private ArrayList<String> errorMessage = new ArrayList<String>();
  private String emailList = ""; // Additional email address to send email to..
  private String badOutputFileName = "";
  private String pid = "";
  private String prefix = "";
  private String qnumber = "";
  private int errorCode = 0;
  private String errorFileCode = "";
  private int rowCountBad = 0;
  private int maxErrorCount = 0;
  private String subjectLine = "";
  private String errorMsg = ""; // Error message for error code.
  private int upLoadFileId = 0;
  private int validOutputRecCount = 0;
  private String webFileLocation; // vishnu change
  private String industryOldGroup;
  private String industryNewGroup;
  private String oldPrefix;
  private String schemaName;
  private String fileDestination;
  private String tolocation;
  private String endLocation;
  private String saveSucessMessage;
  private String environmentName = "";
  private String inputFileURL = "";
  private String outputFileURL = "";

  /**
   * @return Returns the fileDestination.
   */
  public String getFileDestination()
  {
    return fileDestination;
  }

  /**
   * @param fileDestination
   *            The fileDestination to set.
   */
  public void setFileDestination( String fileDestination )
  {
    this.fileDestination = fileDestination;
  }

  /**
   * @return Returns the oldPrefix.
   */
  public String getOldPrefix()
  {
    return oldPrefix;
  }

  /**
   * @param oldPrefix
   *            The oldPrefix to set.
   */
  public void setOldPrefix( String oldPrefix )
  {
    this.oldPrefix = oldPrefix;
  }

  /**
   * @return Returns the schemaName.
   */
  public String getSchemaName()
  {
    return schemaName;
  }

  /**
   * @param schemaName
   *            The schemaName to set.
   */
  public void setSchemaName( String schemaName )
  {
    this.schemaName = schemaName;
  }

  public OperationResultInfo()
  {
  }

  /**
   * Getter for property columnCount.
   * 
   * @return Value of property columnCount.
   */
  public int getColumnCount()
  {
    return columnCount;
  }

  /**
   * Setter for property columnCount.
   * 
   * @param columnCount
   *            New value of property columnCount.
   */
  public void setColumnCount( int columnCount )
  {
    if ( maxColumnCount < columnCount )
    {
      maxColumnCount = columnCount;
    }
    this.columnCount = columnCount;
  }

  /**
   * Getter for property rowCount.
   * 
   * @return Value of property rowCount.
   */
  public int getRowCount()
  {
    return rowCount;
  }

  /**
   * Setter for property rowCount.
   * 
   * @param rowCount
   *            New value of property rowCount.
   */
  public void setRowCount( int rowCount )
  {
    this.rowCount = rowCount;
  }

  /**
   * Getter for property projectId.
   * 
   * @return Value of property projectId.
   */
  public int getProjectId()
  {
    return projectId;
  }

  /**
   * Setter for property projectId.
   * 
   * @param projectId
   *            New value of property projectId.
   */
  public void setProjectId( int projectId )
  {
    this.projectId = projectId;
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
   * Getter for property status.
   * 
   * @return Value of property status.
   */
  public java.lang.String getStatus()
  {
    return status;
  }

  /**
   * Setter for property status.
   * 
   * @param status
   *            New value of property status.
   */
  public void setStatus( java.lang.String status )
  {
    this.status = status;
  }

  /**
   * Getter for property reason.
   * 
   * @return Value of property reason.
   */
  public java.lang.String getReason()
  {
    return reason;
  }

  /**
   * Setter for property reason.
   * 
   * @param reason
   *            New value of property reason.
   */
  public void setReason( java.lang.String reason )
  {
    this.reason = reason;
  }

  /**
   * Getter for property inputFileName.
   * 
   * @return Value of property inputFileName.
   */
  public java.lang.String getInputFileName()
  {
    return inputFileName;
  }

  /**
   * Setter for property inputFileName.
   * 
   * @param inputFileName
   *            New value of property inputFileName.
   */
  public void setInputFileName( java.lang.String inputFileName )
  {
    this.inputFileName = inputFileName;
  }

  /**
   * Getter for property inputFileFormat.
   * 
   * @return Value of property inputFileFormat.
   */
  public java.lang.String getInputFileFormat()
  {
    return inputFileFormat;
  }

  /**
   * Setter for property inputFileFormat.
   * 
   * @param inputFileFormat
   *            New value of property inputFileFormat.
   */
  public void setInputFileFormat( java.lang.String inputFileFormat )
  {
    this.inputFileFormat = inputFileFormat;
  }

  /**
   * Getter for property outputFileName.
   * 
   * @return Value of property outputFileName.
   */
  public java.lang.String getOutputFileName()
  {
    return outputFileName;
  }

  /**
   * Setter for property outputFileName.
   * 
   * @param outputFileName
   *            New value of property outputFileName.
   */
  public void setOutputFileName( java.lang.String outputFileName )
  {
    this.outputFileName = outputFileName;
  }

  /**
   * Getter for property outputFileFormat.
   * 
   * @return Value of property outputFileFormat.
   */
  public java.lang.String getOutputFileFormat()
  {
    return outputFileFormat;
  }

  /**
   * Setter for property outputFileFormat.
   * 
   * @param outputFileFormat
   *            New value of property outputFileFormat.
   */
  public void setOutputFileFormat( java.lang.String outputFileFormat )
  {
    this.outputFileFormat = outputFileFormat;
  }

  /**
   * Getter for property clientName.
   * 
   * @return Value of property clientName.
   */
  public java.lang.String getClientName()
  {
    return clientName;
  }

  /**
   * Setter for property clientName.
   * 
   * @param clientName
   *            New value of property clientName.
   */
  public void setClientName( java.lang.String clientName )
  {
    this.clientName = clientName;
  }

  /**
   * Getter for property programName.
   * 
   * @return Value of property programName.
   */
  public java.lang.String getProgramName()
  {
    return programName;
  }

  /**
   * Setter for property programName.
   * 
   * @param programName
   *            New value of property programName.
   */
  public void setProgramName( java.lang.String programName )
  {
    this.programName = programName;
  }

  /**
   * Getter for property contactList.
   * 
   * @return Value of property contactList.
   */
  public ArrayList<String> getContactList()
  {
    return contactList;
  }

  /**
   * Setter for property contactList.
   * 
   * @param contactList
   *            New value of property contactList.
   */
  public void setContactList( ArrayList<String> contactList )
  {
    this.contactList = contactList;
  }

  /**
   * Getter for property dateReceived.
   * 
   * @return Value of property dateReceived.
   */
  public Date getDateReceived()
  {
    return dateReceived;
  }

  /**
   * Getter for property dateReceived.
   * 
   * @return Value of property dateReceived.
   */
  public String getDateReceivedAsString()
  {
    SimpleDateFormat t_sdf = new SimpleDateFormat( "MM/dd/yyyy hh:mm:ss a z" );
    return t_sdf.format( dateReceived );
  }

  /**
   * Setter for property dateReceived.
   * 
   * @param dateReceived
   *            New value of property dateReceived.
   */
  public void setDateReceived( Date dateReceived )
  {
    this.dateReceived = dateReceived;
  }

  /**
   * Getter for property emailTextAttach.
   * 
   * @return Value of property emailTextAttach.
   */
  public java.lang.String getEmailTextAttach()
  {
    return emailTextAttach;
  }

  /**
   * Setter for property emailTextAttach.
   * 
   * @param emailTextAttach
   *            New value of property emailTextAttach.
   */
  public void setEmailTextAttach( java.lang.String emailTextAttach )
  {
    this.emailTextAttach = emailTextAttach;
  }

  /**
   * Getter for property fileProcessing.
   * 
   * @return Value of property fileProcessing.
   */
  public java.lang.String getFileProcessing()
  {
    return fileProcessing;
  }

  /**
   * Setter for property fileProcessing.
   * 
   * @param fileProcessing
   *            New value of property fileProcessing.
   */
  public void setFileProcessing( java.lang.String fileProcessing )
  {
    this.fileProcessing = fileProcessing;
  }

  /**
   * Getter for property maxColumnCount.
   * 
   * @return Value of property maxColumnCount.
   */
  public int getMaxColumnCount()
  {
    return maxColumnCount;
  }

  /**
   * Setter for property maxColumnCount.
   * 
   * @param maxColumnCount
   *            New value of property maxColumnCount.
   */
  public void setMaxColumnCount( int maxColumnCount )
  {
    this.maxColumnCount = maxColumnCount;
  }

  /**
   * Getter for property errorMessage.
   * 
   * @return errorMessage
   */
  public ArrayList<String> getErrorMessage()
  {
    return errorMessage;
  }

  /**
   * Getter for property result.
   * 
   * @return result
   */
  public int getResult()
  {
    return result;
  }

  /**
   * Setter for property errorMessage.
   * 
   * @param list
   */
  public void setErrorMessage( ArrayList<String> list )
  {
    errorMessage = list;
  }

  /**
   * Setter for property result.
   * 
   * @param i
   */
  public void setResult( int i )
  {
    result = i;
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

  /**
   * @return
   */
  public String getEmailList()
  {
    return emailList;
  }

  /**
   * @param string
   */
  public void setEmailList( String string )
  {
    emailList = string;
  }

  /**
   * @return
   */
  public String getBadOutputFileName()
  {
    return badOutputFileName;
  }

  /**
   * @param string
   */
  public void setBadOutputFileName( String string )
  {
    badOutputFileName = string;
  }

  /**
   * @return
   */
  public int getErrorCode()
  {
    return errorCode;
  }

  /**
   * @return
   */
  public String getPid()
  {
    return pid;
  }

  /**
   * @return
   */
  public String getPrefix()
  {
    return prefix;
  }

  /**
   * @return
   */
  public String getQnumber()
  {
    return qnumber;
  }

  /**
   * @return
   */
  public int getRowCountBad()
  {
    return rowCountBad;
  }

  /**
   * @return
   */
  public int getMaxErrorCount()
  {
    return maxErrorCount;
  }

  /**
   * @param i
   */
  public void setErrorCode( int i )
  {
    errorCode = i;
  }

  /**
   * @param string
   */
  public void setPid( String string )
  {
    pid = string;
  }

  /**
   * @param string
   */
  public void setPrefix( String string )
  {
    prefix = string;
  }

  /**
   * @param string
   */
  public void setQnumber( String string )
  {
    qnumber = string;
  }

  /**
   * @param i
   */
  public void setRowCountBad( int i )
  {
    rowCountBad = i;
  }

  /**
   * @param i
   */
  public void setMaxErrorCount( int i )
  {
    maxErrorCount = i;
  }

  /**
   * @return
   */
  public String getSubjectLine()
  {
    return subjectLine;
  }

  /**
   * @param string
   */
  public void setSubjectLine( String string )
  {
    subjectLine = string;
  }

  /**
   * @return
   */
  public boolean getBadOutputFileNameEmpty()
  {
    return badOutputFileName.trim().length() > 0;
  }

  /**
   * @return
   */
  public boolean getOutputFileNameEmpty()
  {
    return outputFileName.trim().length() > 0;
  }

  /**
   * @return
   */
  public String getErrorMsg()
  {
    return errorMsg;
  }

  /**
   * @param string
   */
  public void setErrorMsg( String string )
  {
    errorMsg = string;
  }

  /**
   * @return Returns the upLoadFileId.
   */
  public int getUpLoadFileId()
  {
    return upLoadFileId;
  }

  /**
   * @param upLoadFileId
   *            The upLoadFileId to set.
   */
  public void setUpLoadFileId( int upLoadFileId )
  {
    this.upLoadFileId = upLoadFileId;
  }

  public void setWebFileLocation( String webFileLocation )
  {
    this.webFileLocation = webFileLocation;
  }

  public String getWebFileLocation()
  {
    return webFileLocation;
  }

  /**
   * @return Returns the errorFileCode.
   */
  public String getErrorFileCode()
  {
    return errorFileCode;
  }

  /**
   * @param errorFileCode
   *            The errorFileCode to set.
   */
  public void setErrorFileCode( String errorFileCode )
  {
    this.errorFileCode = errorFileCode;
  }

  /**
   * @return Returns the industryNewGroup.
   */
  public String getIndustryNewGroup()
  {
    return industryNewGroup;
  }

  /**
   * @param industryNewGroup
   *            The industryNewGroup to set.
   */
  public void setIndustryNewGroup( String industryNewGroup )
  {
    this.industryNewGroup = industryNewGroup;
  }

  /**
   * @return Returns the industryOldGroup.
   */
  public String getIndustryOldGroup()
  {
    return industryOldGroup;
  }

  /**
   * @param industryOldGroup
   *            The industryOldGroup to set.
   */
  public void setIndustryOldGroup( String industryOldGroup )
  {
    this.industryOldGroup = industryOldGroup;
  }

  /**
   * @return Returns the tolocation.
   */
  public String getTolocation()
  {
    return tolocation;
  }

  /**
   * @param tolocation
   *            The tolocation to set.
   */
  public void setTolocation( String tolocation )
  {
    this.tolocation = tolocation;
  }

  /**
   * @return Returns the endLocation.
   */
  public String getEndLocation()
  {
    return endLocation;
  }

  /**
   * @param endLocation
   *            The endLocation to set.
   */
  public void setEndLocation( String endLocation )
  {
    this.endLocation = endLocation;
  }

  /**
   * @return Returns the message.
   */

  /**
   * @return Returns the saveSucessMessage.
   */
  public String getSaveSucessMessage()
  {
    return saveSucessMessage;
  }

  /**
   * @param saveSucessMessage
   *            The saveSucessMessage to set.
   */
  public void setSaveSucessMessage( String saveSucessMessage )
  {
    this.saveSucessMessage = saveSucessMessage;
  }

  /**
   * @return Returns the validOutputRecCount.
   */
  public int getValidOutputRecCount()
  {
    return validOutputRecCount;
  }

  /**
   * @param validOutputRecCount
   *            The validOutputRecCount to set.
   */
  public void setValidOutputRecCount( int validOutputRecCount )
  {
    this.validOutputRecCount = validOutputRecCount;
  }

  public String getEnvironmentName()
  {
    return environmentName;
  }

  public void setEnvironmentName( String environmentName )
  {
    this.environmentName = environmentName;
  }

  public String getInputFileURL()
  {
    return inputFileURL;
  }

  public void setInputFileURL( String inputFileURL )
  {
    this.inputFileURL = inputFileURL;
  }

  public String getOutputFileURL()
  {
    return outputFileURL;
  }

  public void setOutputFileURL( String outputFileURL )
  {
    this.outputFileURL = outputFileURL;
  }

}

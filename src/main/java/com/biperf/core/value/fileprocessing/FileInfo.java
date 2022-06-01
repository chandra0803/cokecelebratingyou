
package com.biperf.core.value.fileprocessing;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Stores Project Info
 * 
 * @author Prabu Babu
 */
public class FileInfo implements Serializable
{
  private int fileId = 0;

  private int projectId = 0;

  private String projectName = "";

  private String fileName = "";

  private String fileProcessing = "";

  private String fileFormat = "";

  private String processingSchedule = "";

  private Date dateStart = Calendar.getInstance().getTime();

  private Date dateEnd = Calendar.getInstance().getTime();

  private String strDateStart = "";

  private String strDateEnd = "";

  private String expectedDay = "";

  private String fileSpecReceived = "";

  private String recCountReq = "";

  private String colCountReq = "";

  private String subjectLine = "";

  private String outFileName = "";

  private String outFileFormat = "";

  private String fromLocation = "";

  private String toLocation = "";

  private ArrayList<FileDetailInfo> fileDetails = new ArrayList<FileDetailInfo>();

  private ArrayList<OperationInfo> fileOperations = new ArrayList<OperationInfo>();

  private String clientName = "";

  private String programName = "";

  private String emailTextAttach = "No";

  private String ownerFlag = "No";

  private String readPer = "No";

  private String modifyPer = "No";

  private String deletePer = "No";

  // vishnu change
  private String fileDestination;

  // These Strings are default to empty because the feature is temporarily on
  // hold
  private String oldPrefix = "";

  private String industryOldGroup = "";

  private String industryNewGroup = "";

  private String schemaName = "";

  // vishnu change
  private int errorCount = 0;

  private String emailList = "";

  private String userExists = "";

  // These three are used for backend process.
  private String pid = "";

  private String prefix = "";

  private String qnumber = "";

  private String fileType = "";

  private String description = "";

  private String action = "";

  // Padmaja
  private int upLoadFileId = 0;

  private String inputFileName = "";

  private int[] rowNumList = null;

  private boolean webDavEnabled = false;

  private String inputURL = "";

  private boolean informaticaEndpoint = false;

  private String testEndpoint = "";

  private String prodEndpoint = "";

  private String subfolderName = "";

  private String emptyFileReq = "";

  public String getEmptyFileReq()
  {
    return emptyFileReq;
  }

  public void setEmptyFileReq( String emptyFileReq )
  {
    this.emptyFileReq = emptyFileReq;
  }

  public FileInfo()
  {
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
   * Getter for property fileName.
   * 
   * @return Value of property fileName.
   */
  public java.lang.String getFileName()
  {
    return fileName;
  }

  /**
   * Setter for property fileName.
   * 
   * @param fileName
   *            New value of property fileName.
   */
  public void setFileName( java.lang.String fileName )
  {
    this.fileName = fileName;
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
   * Getter for property fileFormat.
   * 
   * @return Value of property fileFormat.
   */
  public java.lang.String getFileFormat()
  {
    return fileFormat;
  }

  /**
   * Setter for property fileFormat.
   * 
   * @param fileFormat
   *            New value of property fileFormat.
   */
  public void setFileFormat( java.lang.String fileFormat )
  {
    this.fileFormat = fileFormat;
  }

  /**
   * Getter for property processingSchedule.
   * 
   * @return Value of property processingSchedule.
   */
  public java.lang.String getProcessingSchedule()
  {
    return processingSchedule;
  }

  /**
   * Setter for property processingSchedule.
   * 
   * @param processingSchedule
   *            New value of property processingSchedule.
   */
  public void setProcessingSchedule( java.lang.String processingSchedule )
  {
    this.processingSchedule = processingSchedule;
  }

  /**
   * Getter for property dateStart.
   * 
   * @return Value of property dateStart.
   */
  public Date getDateStart()
  {
    return dateStart;
  }

  /**
   * Setter for property dateStart.
   * 
   * @param dateStart
   *            New value of property dateStart.
   */
  public void setDateStart( Date dateStart )
  {
    this.dateStart = dateStart;
  }

  /**
   * Getter for property dateEnd.
   * 
   * @return Value of property dateEnd.
   */
  public Date getDateEnd()
  {
    return dateEnd;
  }

  /**
   * Setter for property dateEnd.
   * 
   * @param dateEnd
   *            New value of property dateEnd.
   */
  public void setDateEnd( Date dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  /**
   * Getter for property expectedDay.
   * 
   * @return Value of property expectedDay.
   */
  public java.lang.String getExpectedDay()
  {
    return expectedDay;
  }

  /**
   * Setter for property expectedDay.
   * 
   * @param expectedDay
   *            New value of property expectedDay.
   */
  public void setExpectedDay( java.lang.String expectedDay )
  {
    this.expectedDay = expectedDay;
  }

  /**
   * Getter for property fileSpecReceived.
   * 
   * @return Value of property fileSpecReceived.
   */
  public java.lang.String getFileSpecReceived()
  {
    return fileSpecReceived;
  }

  /**
   * Setter for property fileSpecReceived.
   * 
   * @param fileSpecReceived
   *            New value of property fileSpecReceived.
   */
  public void setFileSpecReceived( java.lang.String fileSpecReceived )
  {
    this.fileSpecReceived = fileSpecReceived;
  }

  /**
   * Getter for property recCountReq.
   * 
   * @return Value of property recCountReq.
   */
  public java.lang.String getRecCountReq()
  {
    return recCountReq;
  }

  /**
   * Setter for property recCountReq.
   * 
   * @param recCountReq
   *            New value of property recCountReq.
   */
  public void setRecCountReq( java.lang.String recCountReq )
  {
    this.recCountReq = recCountReq;
  }

  /**
   * Getter for property colCountReq.
   * 
   * @return Value of property colCountReq.
   */
  public java.lang.String getColCountReq()
  {
    return colCountReq;
  }

  /**
   * Setter for property colCountReq.
   * 
   * @param colCountReq
   *            New value of property colCountReq.
   */
  public void setColCountReq( java.lang.String colCountReq )
  {
    this.colCountReq = colCountReq;
  }

  /**
   * Getter for property subjectLine.
   * 
   * @return Value of property subjectLine.
   */
  public java.lang.String getSubjectLine()
  {
    return subjectLine;
  }

  /**
   * Setter for property subjectLine.
   * 
   * @param subjectLine
   *            New value of property subjectLine.
   */
  public void setSubjectLine( java.lang.String subjectLine )
  {
    this.subjectLine = subjectLine;
  }

  /**
   * Getter for property outFileName.
   * 
   * @return Value of property outFileName.
   */
  public java.lang.String getOutFileName()
  {
    return outFileName;
  }

  /**
   * Setter for property outFileName.
   * 
   * @param outFileName
   *            New value of property outFileName.
   */
  public void setOutFileName( java.lang.String outFileName )
  {
    this.outFileName = outFileName;
  }

  /**
   * Getter for property outFileFormat.
   * 
   * @return Value of property outFileFormat.
   */
  public java.lang.String getOutFileFormat()
  {
    return outFileFormat;
  }

  /**
   * Setter for property outFileFormat.
   * 
   * @param outFileFormat
   *            New value of property outFileFormat.
   */
  public void setOutFileFormat( java.lang.String outFileFormat )
  {
    this.outFileFormat = outFileFormat;
  }

  /**
   * Getter for property fromLocation.
   * 
   * @return Value of property fromLocation.
   */
  public java.lang.String getFromLocation()
  {
    return fromLocation;
  }

  /**
   * Setter for property fromLocation.
   * 
   * @param fromLocation
   *            New value of property fromLocation.
   */
  public void setFromLocation( java.lang.String fromLocation )
  {
    this.fromLocation = fromLocation;
  }

  /**
   * Getter for property toLocation.
   * 
   * @return Value of property toLocation.
   */
  public java.lang.String getToLocation()
  {
    return toLocation;
  }

  /**
   * Setter for property toLocation.
   * 
   * @param toLocation
   *            New value of property toLocation.
   */
  public void setToLocation( java.lang.String toLocation )
  {
    this.toLocation = toLocation;
  }

  /**
   * Getter for property fileDetails.
   * 
   * @return Value of property fileDetails.
   */
  public ArrayList<FileDetailInfo> getFileDetails()
  {
    return fileDetails;
  }

  /**
   * Getter for property fileDetails.
   * 
   * @return Value of property fileDetails.
   */
  public FileDetailInfo getFileDetails( int column )
  {
    for ( int i = 0; fileDetails != null && i < fileDetails.size(); i++ )
    {
      if ( ( (FileDetailInfo)fileDetails.get( i ) ).getInputOrder() == column )
      {
        return (FileDetailInfo)fileDetails.get( i );
      }
    }
    return null;
  }

  /**
   * Setter for property fileDetails.
   * 
   * @param fileDetails
   *            New value of property fileDetails.
   */
  public void setFileDetails( ArrayList<FileDetailInfo> fileDetails )
  {
    this.fileDetails = fileDetails;
  }

  /**
   * Getter for property fileOperations.
   * 
   * @return Value of property fileOperations.
   */
  public java.util.ArrayList<OperationInfo> getFileOperations()
  {
    return fileOperations;
  }

  /**
   * Setter for property fileOperations.
   * 
   * @param fileOperations
   *            New value of property fileOperations.
   */
  public void setFileOperations( java.util.ArrayList<OperationInfo> fileOperations )
  {
    this.fileOperations = fileOperations;
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
   * Getter for property strDateStart.
   * 
   * @return Value of property strDateStart.
   */
  public java.lang.String getStrDateStart()
  {
    SimpleDateFormat t_simpleDateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
    return t_simpleDateFormat.format( dateStart );
  }

  /**
   * Setter for property strDateStart.
   * 
   * @param strDateStart
   *            New value of property strDateStart.
   */
  public void setStrDateStart( java.lang.String strDateStart )
  {
    this.strDateStart = strDateStart;
  }

  /**
   * Getter for property strDateEnd.
   * 
   * @return Value of property strDateEnd.
   */
  public java.lang.String getStrDateEnd()
  {
    SimpleDateFormat t_simpleDateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
    return t_simpleDateFormat.format( dateEnd );
  }

  /**
   * Setter for property strDateEnd.
   * 
   * @param strDateEnd
   *            New value of property strDateEnd.
   */
  public void setStrDateEnd( java.lang.String strDateEnd )
  {
    this.strDateEnd = strDateEnd;
  }

  /**
   * Getter for property ownerFlag.
   * 
   * @return Value of property ownerFlag.
   */
  public java.lang.String getOwnerFlag()
  {
    return ownerFlag;
  }

  /**
   * Setter for property ownerFlag.
   * 
   * @param ownerFlag
   *            New value of property ownerFlag.
   */
  public void setOwnerFlag( java.lang.String ownerFlag )
  {
    if ( ownerFlag != null && ownerFlag.equals( "Yes" ) )
    {
      this.ownerFlag = ownerFlag;
    }
    else
    {
      this.ownerFlag = "No";
    }
  }

  /**
   * Getter for property readPer.
   * 
   * @return Value of property readPer.
   */
  public java.lang.String getReadPer()
  {
    return readPer;
  }

  /**
   * Setter for property readPer.
   * 
   * @param readPer
   *            New value of property readPer.
   */
  public void setReadPer( java.lang.String readPer )
  {
    if ( readPer != null && readPer.equals( "Yes" ) )
    {
      this.readPer = readPer;
    }
    else
    {
      this.readPer = "No";
    }
  }

  /**
   * Getter for property modifyPer.
   * 
   * @return Value of property modifyPer.
   */
  public java.lang.String getModifyPer()
  {
    return modifyPer;
  }

  /**
   * Setter for property modifyPer.
   * 
   * @param modifyPer
   *            New value of property modifyPer.
   */
  public void setModifyPer( java.lang.String modifyPer )
  {
    if ( modifyPer != null && modifyPer.equals( "Yes" ) )
    {
      this.modifyPer = modifyPer;
    }
    else
    {
      this.modifyPer = "No";
    }
  }

  /**
   * Getter for property deletePer.
   * 
   * @return Value of property deletePer.
   */
  public java.lang.String getDeletePer()
  {
    return deletePer;
  }

  /**
   * Setter for property deletePer.
   * 
   * @param deletePer
   *            New value of property deletePer.
   */
  public void setDeletePer( java.lang.String deletePer )
  {
    if ( deletePer != null && deletePer.equals( "Yes" ) )
    {
      this.deletePer = deletePer;
    }
    else
    {
      this.deletePer = "No";
    }
  }

  private void writeObject( java.io.ObjectOutputStream out ) throws IOException
  {
    HashMap<String, Object> t_hsmap = new HashMap<String, Object>();
    t_hsmap.put( "fileId", new Integer( fileId ) );
    t_hsmap.put( "projectId", new Integer( projectId ) );
    t_hsmap.put( "fileName", fileName );
    t_hsmap.put( "fileProcessing", fileProcessing );
    t_hsmap.put( "fileFormat", fileFormat );
    t_hsmap.put( "processingSchedule", processingSchedule );
    t_hsmap.put( "dateStart", dateStart );
    t_hsmap.put( "dateEnd", dateEnd );
    t_hsmap.put( "expectedDay", expectedDay );
    t_hsmap.put( "fileSpecReceived", fileSpecReceived );
    t_hsmap.put( "recCountReq", recCountReq );
    t_hsmap.put( "colCountReq", colCountReq );
    t_hsmap.put( "subjectLine", subjectLine );
    t_hsmap.put( "outFileName", outFileName );
    t_hsmap.put( "outFileFormat", outFileFormat );
    t_hsmap.put( "fromLocation", fromLocation );
    t_hsmap.put( "toLocation", toLocation );
    t_hsmap.put( "emailTextAttach", emailTextAttach );
    t_hsmap.put( "clientName", clientName );
    t_hsmap.put( "programName", programName );
    out.writeObject( t_hsmap );
    out.writeObject( fileDetails );
    out.writeObject( fileOperations );
  }

  @SuppressWarnings( "unchecked" )
  private void readObject( java.io.ObjectInputStream in ) throws IOException, ClassNotFoundException
  {
    HashMap<String, Object> t_hsmap = (HashMap<String, Object>)in.readObject();
    fileId = ( (Integer)t_hsmap.get( "fileId" ) ).intValue();
    projectId = ( (Integer)t_hsmap.get( "projectId" ) ).intValue();
    fileName = (String)t_hsmap.get( "fileName" );
    fileProcessing = (String)t_hsmap.get( "fileProcessing" );
    fileFormat = (String)t_hsmap.get( "fileFormat" );
    processingSchedule = (String)t_hsmap.get( "processingSchedule" );
    dateStart = (Date)t_hsmap.get( "dateStart" );
    dateEnd = (Date)t_hsmap.get( "dateEnd" );
    expectedDay = (String)t_hsmap.get( "expectedDay" );
    fileSpecReceived = (String)t_hsmap.get( "fileSpecReceived" );
    recCountReq = (String)t_hsmap.get( "recCountReq" );
    colCountReq = (String)t_hsmap.get( "colCountReq" );
    subjectLine = (String)t_hsmap.get( "subjectLine" );
    outFileName = (String)t_hsmap.get( "outFileName" );
    outFileFormat = (String)t_hsmap.get( "outFileFormat" );
    fromLocation = (String)t_hsmap.get( "fromLocation" );
    toLocation = (String)t_hsmap.get( "toLocation" );
    emailTextAttach = (String)t_hsmap.get( "emailTextAttach" );
    clientName = (String)t_hsmap.get( "clientName" );
    programName = (String)t_hsmap.get( "programName" );
    fileDetails = (ArrayList<FileDetailInfo>)in.readObject();
    fileOperations = (ArrayList<OperationInfo>)in.readObject();
  }

  /**
   * @return
   */
  public String getProjectName()
  {
    return projectName;
  }

  /**
   * @param string
   */
  public void setProjectName( String string )
  {
    projectName = string;
  }

  /**
   * @return
   */
  public String getFileType()
  {
    return fileType;
  }

  /**
   * @param string
   */
  public void setFileType( String string )
  {
    fileType = string;
  }

  /**
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param string
   */
  public void setDescription( String string )
  {
    description = string;
  }

  /*
   * return approx size of columns height assuming 14px per line
   * @author babu
   */
  public int getColumnHeight()
  {
    if ( fileDetails != null )
    {
      return fileDetails.size() * 30 + 100;
    }
    return 100;
  }

  /**
   * @return
   */
  public String getAction()
  {
    return action;
  }

  /**
   * @param string
   */
  public void setAction( String string )
  {
    action = string;
  }

  /**
   * @return
   */
  public int getErrorCount()
  {
    return errorCount;
  }

  /**
   * @param i
   */
  public void setErrorCount( int i )
  {
    errorCount = i;
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

  /**
   * @return Returns the inputFileName.
   */
  public String getInputFileName()
  {
    return inputFileName;
  }

  /**
   * @param inputFileName
   *            The inputFileName to set.
   */
  public void setInputFileName( String inputFileName )
  {
    this.inputFileName = inputFileName;
  }

  /**
   * @return Returns the rowNumList.
   */
  /*
   * public ArrayList getRowNumList() { return rowNumList; }
   *//**
    * @param rowNumList
    *            The rowNumList to set.
    */

  /*
   * public void setRowNumList(ArrayList rowNumList) { this.rowNumList = rowNumList; }
   */
  /**
   * @return Returns the rowNumList.
   */
  public int[] getRowNumList()
  {
    return rowNumList;
  }

  /**
   * @param rowNumList
   *            The rowNumList to set.
   */
  public void setRowNumList( int[] rowNumList )
  {
    this.rowNumList = rowNumList;
  }

  public String getUserExists()
  {
    return userExists;
  }

  /**
   * @param userExists
   *            The userExists to set.
   */
  public void setUserExists( String userExists )
  {
    this.userExists = userExists;
  }

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

  public boolean isWebDavEnabled()
  {
    return webDavEnabled;
  }

  public void setWebDavEnabled( boolean webDavEnabled )
  {
    this.webDavEnabled = webDavEnabled;
  }

  public String getInputURL()
  {
    return inputURL;
  }

  public void setInputURL( String inputURL )
  {
    this.inputURL = inputURL;
  }

  public boolean isInformaticaEndpoint()
  {
    return informaticaEndpoint;
  }

  public void setInformaticaEndpoint( boolean informaticaEndpoint )
  {
    this.informaticaEndpoint = informaticaEndpoint;
  }

  public String getTestEndpoint()
  {
    return testEndpoint;
  }

  public void setTestEndpoint( String testEndpoint )
  {
    this.testEndpoint = testEndpoint;
  }

  public String getProdEndpoint()
  {
    return prodEndpoint;
  }

  public void setProdEndpoint( String prodEndpoint )
  {
    this.prodEndpoint = prodEndpoint;
  }

  public String getSubfolderName()
  {
    return subfolderName;
  }

  public void setSubfolderName( String subfolderName )
  {
    this.subfolderName = subfolderName;
  }

}

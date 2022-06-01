
package com.biperf.core.service.fileprocessing.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.fileprocessing.GlobalFileProcessingService;
import com.biperf.core.service.fileprocessing.GlobalFileTemplateFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.fileprocessing.FileOperation;
import com.biperf.core.utils.fileprocessing.UpLoadFileOperation;
import com.biperf.core.value.fileprocessing.FileInfo;
import com.biperf.core.value.fileprocessing.OperationInfo;
import com.biperf.core.value.fileprocessing.OperationResultInfo;
import com.biperf.util.StringUtils;

public class GlobalFileProcessingServiceImpl extends FileOperation implements GlobalFileProcessingService
{
  private static final Log log = LogFactory.getLog( GlobalFileProcessingServiceImpl.class );

  private SystemVariableService systemVariableService;
  private GlobalFileTemplateFactory globalFileTemplateFactory;

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setGlobalFileTemplateFactory( GlobalFileTemplateFactory globalFileTemplateFactory )
  {
    this.globalFileTemplateFactory = globalFileTemplateFactory;
  }

  // ---------------------------------------------------------------------------
  // Service Methods
  // ---------------------------------------------------------------------------
  public OperationResultInfo process( String type, String fileName, InputStream inputFile, String userId ) throws Exception
  {
    FileInfo fileInfo = globalFileTemplateFactory.getFileDetailInfo( type );

    fileInfo.setWebDavEnabled( true );
    fileInfo.setPrefix( getPrefix() );
    fileInfo.setSubfolderName( getSubFolderName() );
    fileInfo.setInputURL( getFileInputUrl( fileInfo.getPrefix(), fileInfo.getSubfolderName() ) );
    logger.error( "******" + getFileInputUrl( fileInfo.getPrefix(), fileInfo.getSubfolderName() ) + fileName );
    fileInfo.setFileFormat( getFileFormat( fileName ) );
    fileInfo.setInputFileName( fileName );
    fileInfo.setOutFileName( getConvertedFileName( fileInfo.getOutFileName(), '%' ) );
    fileInfo.setOutFileFormat( FileOperation.OUTPUT_FILE_TYPE_PIPE );

    return process( inputFile, fileInfo, userId );
  }

  private OperationResultInfo process( InputStream inputFile, FileInfo fileInfo, String userId ) throws Exception
  {
    try
    {
      OperationResultInfo operationResultInfo = new OperationResultInfo();
      operationResultInfo.setInputFileName( fileInfo.getFileName() );
      operationResultInfo.setInputFileFormat( fileInfo.getFileFormat() );
      operationResultInfo.setInputFileURL( fileInfo.getInputURL() );
      log.error( "fileInfo.getInputURL() ******" + fileInfo.getInputURL() );
      operationResultInfo.setOutputFileFormat( fileInfo.getOutFileFormat() );
      operationResultInfo.setOutputFileName( fileInfo.getOutFileName() );
      operationResultInfo.setProgramName( fileInfo.getProgramName() );
      operationResultInfo.setClientName( fileInfo.getClientName() );
      operationResultInfo.setEmailTextAttach( fileInfo.getEmailTextAttach() );
      operationResultInfo.setFileProcessing( fileInfo.getFileProcessing() );
      operationResultInfo.setProjectId( fileInfo.getProjectId() );
      operationResultInfo.setFileId( fileInfo.getFileId() );
      operationResultInfo.setEmailList( fileInfo.getEmailList() );
      operationResultInfo.setPid( fileInfo.getPid() );
      operationResultInfo.setQnumber( fileInfo.getQnumber() );
      operationResultInfo.setPrefix( fileInfo.getPrefix() );
      operationResultInfo.setSubjectLine( fileInfo.getSubjectLine() );
      // vishnu change
      operationResultInfo.setFileDestination( fileInfo.getFileDestination() );
      log.error( "fileInfo.getF ileDestination()******" + fileInfo.getFileDestination() );
      operationResultInfo.setTolocation( fileInfo.getToLocation() );
      // ToLocation will be prefix|location. separate them.location|prefix
      String toLocation = operationResultInfo.getTolocation().trim();
      int toIndex = toLocation.indexOf( '|' );
      if ( toIndex > 0 )
      {
        operationResultInfo.setOldPrefix( toLocation.substring( toIndex + 1 ).trim() );
        operationResultInfo.setEndLocation( toLocation.substring( 0, toIndex ) );
      }
      else
      {
        operationResultInfo.setEndLocation( operationResultInfo.getTolocation() );
      }
      log.error( "operationResultInfo.getTolocation() ******" + operationResultInfo.getTolocation() );
      // to vishnu change
      // If processing is Manual return
      // Do file level operation
      ArrayList<OperationInfo> operationList = fileInfo.getFileOperations();
      FileOperation fileOperation = new UpLoadFileOperation();
      for ( int i = 0; operationList != null && i < operationList.size(); i++ )
      {
        OperationInfo t_operationInfo = (OperationInfo)operationList.get( i );
        fileOperation.setFileOperationProperty( t_operationInfo );
      }

      fileOperation.perform( inputFile, null, fileInfo.getPrefix(), fileInfo, operationResultInfo, userId );
      operationResultInfo.setUpLoadFileId( fileInfo.getUpLoadFileId() );
      operationResultInfo.setFileProcessing( fileInfo.getFileProcessing() );

      return operationResultInfo;
    }
    catch( Throwable t )
    {
      log.error( "Error during processing Global file", t );
      throw new Exception( t.getMessage() );
    }
  }

  private String getConvertedFileName( String fileName, char seperator )
  {
    if ( StringUtils.isEmpty( fileName ) )
    {
      return fileName;
    }
    int t_beginIndex = fileName.indexOf( seperator );
    int t_endIndex = fileName.indexOf( seperator, t_beginIndex + 1 );
    if ( t_beginIndex < 0 || t_endIndex < 0 || t_beginIndex >= t_endIndex )
    {
      return fileName;
    }
    StringBuffer t_convertedString = new StringBuffer();
    t_convertedString.append( fileName.substring( 0, t_beginIndex ) );
    t_convertedString.append( formatString( fileName.substring( t_beginIndex + 1, t_endIndex ) ) );
    if ( t_endIndex + 1 < fileName.length() )
    {
      t_convertedString.append( fileName.substring( t_endIndex + 1 ) );
    }
    return getConvertedFileName( t_convertedString.toString(), seperator );
  }

  private String formatString( String format )
  {
    return new SimpleDateFormat( format ).format( Calendar.getInstance().getTime() );
  }

  private String getPrefix()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_PREFIX ).getStringVal();
  }

  private String getSubFolderName()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_SUBFOLDER ).getStringVal();
  }

  private String getFileInputUrl( String prefix, String subfolder )
  {
    StringBuilder builder = new StringBuilder();
    builder.append( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.GLOBAL_FILE_PROCESSING_WEBDAV ).getStringVal() );
    builder.append( '/' );
    builder.append( prefix );
    builder.append( '/' );
    builder.append( subfolder );
    return builder.toString();
  }

  private String getFileFormat( String fileName )
  {
    final String[] XSL_EXTN = { "xls", "xlsx" };
    final String CSV_EXTN = "csv";
    final String[] DB_DUMP_EXT = { "dmp" };
    if ( !StringUtils.isEmpty( fileName ) )
    {
      int idx = fileName.lastIndexOf( '.' );
      if ( idx > 0 )
      {
        String extn = fileName.substring( idx + 1 );
        for ( String xslExtn : XSL_EXTN )
        {
          if ( extn.equals( xslExtn ) )
          {
            return FileOperation.INPUT_FILE_TYPE_EXCEL;
          }
          if ( extn.equals( CSV_EXTN ) )
          {
            return FileOperation.INPUT_FILE_TYPE_COMMA;
          }
        }
        for ( String dbExtn : DB_DUMP_EXT )
        {
          if ( extn.equals( dbExtn ) )
          {
            return FileOperation.INPUT_FILE_TYPE_DATABASE_DUMP;
          }
        }
      }
    }
    return FileOperation.INPUT_FILE_TYPE_PIPE;
  }

}

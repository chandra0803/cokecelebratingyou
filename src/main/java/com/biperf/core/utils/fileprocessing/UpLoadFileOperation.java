
package com.biperf.core.utils.fileprocessing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.util.WebDavConfiguration;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.value.fileprocessing.FileInfo;
import com.biperf.core.value.fileprocessing.OperationResultInfo;
import com.biperf.core.value.fileprocessing.WebDavFileWriteInfo;

public class UpLoadFileOperation extends FileOperation
{
  public static final String CLASS_NAME = "UpLoadFileOperation";

  protected static final Log logger = LogFactory.getLog( UpLoadFileOperation.class );

  /**
   * This class returns the seperator for different format type
   */
  public void perform( InputStream inputFile, File outputFolder, String prefix, FileInfo fileInfo, OperationResultInfo operationResultInfo, String userId ) throws IOException, Exception
  {
    String t_outputFileName = operationResultInfo.getOutputFileName();
    // Now empty the output file name in result info so we can track if both
    // bad and good file names are written
    operationResultInfo.setOutputFileName( "" );

    if ( t_outputFileName != null )
    {
      WebDavFileWriteInfo fileWriterInfo = new WebDavFileWriteInfo( fileInfo, outputFolder, t_outputFileName, operationResultInfo );
      String outUrl = getOutputUrl( fileInfo );// fileInfo.getInputURL() + "/valid/" +
                                               // fileInfo.getOutFileName();
      if ( logger.isInfoEnabled() )
      {
        logger.info( "processing to output Url: " + outUrl );
        logger.info( "inputURl: " + fileInfo.getInputURL() );
      }
      WebDavConfiguration outConfiguration = new WebDavConfiguration( outUrl );
      fileWriterInfo.setOutConfiguration( outConfiguration );
      WebDavConfiguration configuration = new WebDavConfiguration( fileInfo.getInputURL() );
      fileWriterInfo.setConfiguration( configuration );
      fileWriterInfo.setFileInfo( fileInfo );
      fileWriterInfo.setUserId( userId );
      fileWriterInfo.setInputFileName( operationResultInfo.getInputFileName() );

      if ( fileInfo == null || fileInfo.getFileFormat() == null )
      {
        fileWriterInfo.close();
        throw new Exception( CLASS_NAME + ".perform - File Information Missing." );
      }
      operationResultInfo.setMaxErrorCount( fileInfo.getErrorCount() );
      if ( fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_EXCEL ) )
      {
        handleExcelFile( inputFile, fileWriterInfo, fileInfo, operationResultInfo );
      }
      else if ( fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_COMMA ) || fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_PIPE ) || fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_TAB ) )
      {
        handleCSVFile( inputFile, fileWriterInfo, fileInfo, operationResultInfo );
      }
      else if ( fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_DATABASE_DUMP ) )
      {
        handleDBDumpFile( inputFile, fileWriterInfo, fileInfo, operationResultInfo );
      }

      fileWriterInfo.close();
    } // endIf output file name is not null
  }

  public void handleDBDumpFile( InputStream inputFile, WebDavFileWriteInfo fileWriterInfo, FileInfo fileInfo, OperationResultInfo operationResultInfo ) throws Exception
  {
    fileInfo.setOutFileName( fileInfo.getInputFileName() );

    final Path destination = Paths.get( WebDavFileWriteInfo.getAppDataDir() + File.separator + fileWriterInfo.outputFileName );
    Files.copy( inputFile, destination );
    fileWriterInfo.setOutputFile( destination.toString() );
  }

  protected String getOutputUrl( FileInfo fileInfo )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( fileInfo.getInputURL() );
    if ( !AwsUtils.isAws() )
    {
      sb.append( "/valid" );
    }
    sb.append( "/" );
    sb.append( fileInfo.getOutFileName() );
    return sb.toString();
  }
}

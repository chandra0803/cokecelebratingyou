/*
 * Created on Dec 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.biperf.core.value.fileprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.fileprocessing.AWSFileLoadTransferService;
import com.biperf.core.service.util.WebDavConfiguration;
import com.biperf.core.service.util.WebDavHelper;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.BeanLocator;

/**
 * WebDavFileWriteInfo.
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
 * <td>babu</td>
 * <td>Dec 8, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class WebDavFileWriteInfo
{
  protected static final Log logger = LogFactory.getLog( WebDavFileWriteInfo.class );

  private static final String SYS_APPDATADIR = "appdatadir";
  private static final String ADC_SUBFOLDER = "fileimport";
  private static final String CLASS_NAME = "WebdavFileWriteInfo";

  private FileInfo fileInfo = null;
  private String outputBadFile = null;
  private String outputFile = null;
  private String inputFileName = "";
  private WebDavConfiguration configuration = null;
  private WebDavConfiguration outConfiguration = null;
  private String userId = null;
  private Date time = new Date();
  private long timeStamp = time.getTime();
  public String outputFileName = "";
  public BufferedWriter bufferedGoodFileWriter = null;
  public BufferedWriter bufferedBadFileWriter = null;
  public OperationResultInfo operationResultInfo = null;
  public File outputFolder = null;

  /**
   * @param outputFolder
   * @param outputFileName
   * @param operationResultInfo
   */
  public WebDavFileWriteInfo( File outputFolder, String outputFileName, OperationResultInfo operationResultInfo )
  {
    this.outputFileName = outputFileName;
    this.outputFolder = outputFolder;
    this.operationResultInfo = operationResultInfo;
  }

  public WebDavFileWriteInfo( FileInfo fileInfo, File outputFolder, String outputFileName, OperationResultInfo operationResultInfo )
  {
    this.outputFileName = outputFileName;
    this.outputFolder = outputFolder;
    this.operationResultInfo = operationResultInfo;
    this.fileInfo = fileInfo;
  }

  public void write( boolean isError, String message ) throws IOException
  {
    BufferedWriter bufferedWriter = null;

    try
    {

      String t_filePath = getAppDataDir();
      if ( isError )
      {
        if ( bufferedBadFileWriter == null )
        {
          outputBadFile = t_filePath + File.separator + outputFileName + ".bad";
          bufferedBadFileWriter = new BufferedWriter( new FileWriter( outputBadFile ) );
          if ( operationResultInfo != null )
          {
            operationResultInfo.setBadOutputFileName( outputFileName + ".bad" );
          }
        }
        bufferedWriter = bufferedBadFileWriter;
      }
      else
      {
        if ( bufferedGoodFileWriter == null )
        {
          outputFile = t_filePath + File.separator + outputFileName;
          bufferedGoodFileWriter = new BufferedWriter( new FileWriter( outputFile ) );
          if ( operationResultInfo != null )
          {
            operationResultInfo.setOutputFileName( outputFileName );
          }
        }
        bufferedWriter = bufferedGoodFileWriter;
      }

      bufferedWriter.write( message );
      bufferedWriter.flush();
    }
    catch( Exception sqe )
    {
      sqe.printStackTrace();
      String t_message = CLASS_NAME + ".write - " + sqe.getMessage();

      throw new IOException( t_message );
    }
  }

  public void create() throws IOException
  {
    BufferedWriter bufferedWriter = null;
    try
    {
      String t_filePath = getAppDataDir();
      outputFile = t_filePath + File.separator + outputFileName;
      File goodFile = new File( outputFile );
      goodFile.createNewFile();
      bufferedGoodFileWriter = new BufferedWriter( new FileWriter( outputFile ) );
      if ( operationResultInfo != null )
      {
        operationResultInfo.setOutputFileName( outputFileName );
      }

      bufferedWriter = bufferedGoodFileWriter;

      // bufferedWriter.write(0);
      // bufferedWriter.flush();
    }
    catch( Exception sqe )
    {
      sqe.printStackTrace();
      String t_message = CLASS_NAME + ".create - " + sqe.getMessage();

      throw new IOException( t_message );
    }
  }

  public void Fileclose() throws IOException
  {

    if ( bufferedGoodFileWriter != null )
    {
      bufferedGoodFileWriter.close();
    }
    if ( bufferedBadFileWriter != null )
    {
      bufferedBadFileWriter.close();
      bufferedBadFileWriter = null;
    }
  }

  public void close() throws IOException
  {
    try
    {
      boolean errorPresent = false;
      if ( bufferedGoodFileWriter != null )
      {
        bufferedGoodFileWriter.close();
        bufferedGoodFileWriter = null;
      }
      if ( bufferedBadFileWriter != null )
      {
        errorPresent = true;
        bufferedBadFileWriter.close();
        bufferedBadFileWriter = null;
      }

      if ( !errorPresent )
      {
        if ( outputFile != null && outputFile.trim().length() > 0 )
        {
          File file = new File( outputFile );
          if ( file.exists() )
          {
            // if running locally - use the standard WebDav, if using AWS, call perl script...
            if ( AwsUtils.isAws() )
            {
              logger.info( "Calling the CGI script for " + outputFile );
              ( (AWSFileLoadTransferService)BeanLocator.getBean( AWSFileLoadTransferService.BEAN_NAME ) ).transfer( outputFile );
            }
            else
            {
              String goodpath = WebDavHelper.put( outConfiguration, "valid/", file, fileInfo.getPrefix() );
            }
            // cleanup regardless..
            boolean success = file.delete();
          }
        }
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  public static String getAppDataDir()
  {
    StringBuilder builder = new StringBuilder();
    builder.append( System.getProperty( SYS_APPDATADIR ) );
    builder.append( File.separator );
    builder.append( ADC_SUBFOLDER );

    // Setup the path, if it does not exist
    String path = builder.toString();
    if ( path != null && !new File( path ).exists() )
    {
      new File( path ).mkdirs();
    }
    return path;
  }

  public void setOutputFile( String outputFile )
  {
    this.outputFile = outputFile;
  }

  public WebDavConfiguration getConfiguration()
  {
    return configuration;
  }

  public void setConfiguration( WebDavConfiguration configuration )
  {
    this.configuration = configuration;
  }

  public FileInfo getFileInfo()
  {
    return fileInfo;
  }

  public void setFileInfo( FileInfo fileInfo )
  {
    this.fileInfo = fileInfo;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getInputFileName()
  {
    return inputFileName;
  }

  public void setInputFileName( String inputFileName )
  {
    this.inputFileName = inputFileName;
  }

  public WebDavConfiguration getOutConfiguration()
  {
    return outConfiguration;
  }

  public void setOutConfiguration( WebDavConfiguration outConfiguration )
  {
    this.outConfiguration = outConfiguration;
  }

}

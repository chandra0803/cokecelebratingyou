
package com.biperf.core.service.fileprocessing.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.fileprocessing.AWSFileLoadTransferService;
import com.biperf.core.service.system.SystemVariableService;

public class AWSFileLoadTransferServiceImpl implements AWSFileLoadTransferService
{
  private static final Log log = LogFactory.getLog( AWSFileLoadTransferServiceImpl.class );

  public SystemVariableService systemVariableService;

  @Override
  public void transfer( String filename )
  {
    String command = buildExecutionCommand();
    String cgiPath = buildCGIFileLocation();
    log.warn( "Executing the following Script: " + command + " " + cgiPath + " " + filename );
    int exitValue = -1;
    Process process = null;
    try
    {
      ProcessBuilder builder = new ProcessBuilder( command, cgiPath, filename );
      process = builder.start();
      exitValue = process.waitFor();
      log.warn( "Successfully transfered AWS file upload [ exit value " + exitValue + " ]" + filename );
    }
    catch( Throwable e )
    {
      e.printStackTrace();
      log.error( "Error processing " + command + " " + cgiPath + " " + filename + ": " + e.getMessage() + " Exit Value: " + exitValue, e );
    }
    finally
    {
      String outputResults = null;
      String outputErrors = null;
      try
      {
        if ( null != process )
        {
          outputResults = buildProcessOutput( process.getInputStream() );
          outputErrors = buildProcessOutput( process.getErrorStream() );
          log.warn( "Script output: " + outputResults );
          log.warn( "Script Error output: " + outputErrors );
          process.destroy();
        }
      }
      catch( Exception t )
      {
        t.printStackTrace();
        log.error( "Error destroing process " + cgiPath + " " + filename + ": " + t.getMessage() + " Exit Value: " + exitValue, t );
      }
    }
  }

  private String buildProcessOutput( InputStream inputStream ) throws IOException
  {
    StringBuilder sb = new StringBuilder();
    if ( null == inputStream )// this will likely should be removed...it's caused by calluing the
                              // buildProcessOutput twice on the same InputStream
    {
      return "OOOPS!";
    }
    BufferedReader br = null;
    try
    {
      br = new BufferedReader( new InputStreamReader( inputStream ) );
      String line = null;
      while ( ( line = br.readLine() ) != null )
      {
        sb.append( line + System.getProperty( "line.separator" ) );
      }
    }
    finally
    {
      br.close();
    }
    return sb.toString();
  }

  /*
   * Returns the absolute path to the executable perl script
   */
  protected String buildCGIFileLocation()
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( SystemVariableService.AWS_FILE_LOAD_TRANSFER_CGI );
    return property.getStringVal();
  }

  /*
   * Returns the command used to execute the script
   */
  protected String buildExecutionCommand()
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( SystemVariableService.AWS_FILE_LOAD_TRANSFER_EXECUTION_CMD );
    return property.getStringVal();
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }
}

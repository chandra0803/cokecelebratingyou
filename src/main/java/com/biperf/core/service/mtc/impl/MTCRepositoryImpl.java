
package com.biperf.core.service.mtc.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.mtc.MTCRepository;
import com.biperf.core.service.ots.impl.OTSRepositoryImpl;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;

@Service( "MTCRepositoryImpl" )
public class MTCRepositoryImpl implements MTCRepository
{
  private static final Log log = LogFactory.getLog( OTSRepositoryImpl.class );

  public static final String UnixFileSeparator = "/";
  public static final String WindowsFileSeparator = "\\";
  public static final String FILE_DELIMITER = ",";

  @Autowired
  private SystemVariableService systemVariableService;

  @Override
  public UploadResponse uploadVideo( URL filePath, String transcoderProfile )
  {
    String callBackUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/mtc/callbackpost.action";

    // Request is sent as a multi-part upload
    MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
    String extractLocation = getExtractLocation();
    FileExtractUtils.createDirIfNeeded( extractLocation );

    String fileName = "start." + ImageUtils.getFileExtension( filePath.toString() );
    File file = new File( extractLocation, fileName );
    try
    {
      FileUtils.copyURLToFile( filePath, file );
    }
    catch( IOException e1 )
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    parameters.add( "media", new FileSystemResource( file ) );

    parameters.add( "options.profile", transcoderProfile );
    parameters.add( "callbackUrl", callBackUrl );

    HttpEntity<MultiValueMap<String, Object>> entity = null;
    try
    {
      entity = new HttpEntity<MultiValueMap<String, Object>>( parameters, MeshServicesUtil.getAuthorizationHeadersWithCompanyIDAndJWTTokenForMultiPart() );

    }
    catch( ServiceErrorException e )
    {
      log.error( e.getMessage() );

    }
    log.error( "Entity" + entity.toString() );
    UploadResponse response = null;
    try
    {
      response = MeshServicesUtil.getRestWebClient().postForObject( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + "/mtc-ingest/v1/upload-video", entity, UploadResponse.class );
    }
    catch( Exception ex )
    {
      log.error( "Exception while uploading video : " + ex.getMessage() );
    }
    log.error( "Response " + response.toString() );
    file.delete();
    return response;

  }

  private String getExtractLocation()
  {
    String extractLocation = null;

    // On Windows this is \ On Unix this is /
    String currentSystemFileSeparator = File.separator;

    // user defined path where the csv file should be saved. i.e. /tmp/ on Unix
    extractLocation = System.getProperty( "appdatadir" );

    // make sure the user defined directory works with the current system
    if ( !StringUtils.isBlank( extractLocation ) )
    {
      // e.g. Developers running on localhosts on Windows
      // but the system variable specifies an Unix file separator
      if ( extractLocation.indexOf( UnixFileSeparator ) >= 0 && currentSystemFileSeparator.equals( WindowsFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '/', '\\' );
      }
      // e.g. QA, PPRD, PROD - CTECH environments running on Unix
      // but the system variable specifies a Windows file separator
      if ( extractLocation.indexOf( WindowsFileSeparator ) >= 0 && currentSystemFileSeparator.equals( UnixFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '\\', '/' );
      }
    }

    return extractLocation;
  }

}

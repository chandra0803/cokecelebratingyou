
package com.biperf.core.service.mtc.impl;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.mtc.MTCRepositoryFactory;
import com.biperf.core.service.mtc.MTCService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;

@Service( "mtcService" )
public class MTCServiceImpl implements MTCService
{

  private static final Log logger = LogFactory.getLog( MTCServiceImpl.class );

  private static final String TRANSCODER_PROFILE = "simple";
  @Autowired
  private MTCRepositoryFactory mtcRepoFactory;

  @Autowired
  private SystemVariableService systemVariableService;

  @Override
  public UploadResponse uploadVideo( URL file ) throws ServiceErrorException, JSONException
  {
    UploadResponse uploadResponse = null;

    try
    {
      uploadResponse = mtcRepoFactory.getRepo().uploadVideo( file, TRANSCODER_PROFILE );
    }
    catch( HttpStatusCodeException httpException )
    {
      logger.error( "Exception while uploading video : " + httpException.getMessage() );
      throw new ServiceErrorException( httpException.getResponseBodyAsString() );
    }

    return uploadResponse;
  }

  public MTCRepositoryFactory getMtcRepoFactory()
  {
    return mtcRepoFactory;
  }

  public void setMtcRepoFactory( MTCRepositoryFactory mtcRepoFactory )
  {
    this.mtcRepoFactory = mtcRepoFactory;
  }

  private String getParentPath()
  {
    return System.getProperty( "appdatadir" ) + '/' + getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() + '/';
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

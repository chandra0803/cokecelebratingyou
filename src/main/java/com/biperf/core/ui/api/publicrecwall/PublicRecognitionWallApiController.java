package com.biperf.core.ui.api.publicrecwall;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.publicrecognitionwall.PublicRecognitionWallService;
import com.biperf.core.service.publicrecognitionwall.dto.PublicRecognitionWall;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.utils.crypto.SHA256Hash;

@Controller
@RequestMapping( "/api/publicrecognitionwall/1" )
public class PublicRecognitionWallApiController extends SpringBaseController
{
  
  public static String API_GATEWAY_HEADER = "API-Gateway-Auth";
  
  private static final Log logger = LogFactory.getLog( PublicRecognitionWallApiController.class );
  
  public @Autowired SystemVariableService systemVariableService;
  public @Autowired PublicRecognitionWallService publicRecognitionWallService;
  
  @RequestMapping( value = "/ping.action", method = RequestMethod.GET )
  public String ping()
  {
    return "PublicRecognitionWall API Ping Response";
  }
  
  @RequestMapping( value = "/publicRecognitionWall.action", method = RequestMethod.GET )
  public @ResponseBody ResponseEntity<PublicRecognitionWall> publicRecognitionsByLocationId( HttpServletRequest httpRequest )
  {
	PublicRecognitionWall publicRecognitionWall=null;
    if ( isAuthenticated( httpRequest ) )
    {
      try
      {
    	  publicRecognitionWall = new PublicRecognitionWall();
        publicRecognitionWall = publicRecognitionWallService.getPublicRecognitionWall();
        return new ResponseEntity<>( publicRecognitionWall, HttpStatus.OK );
      }
      catch( ServiceErrorException e )
      {
        logger.error( e.getMessage(), e );
        return new ResponseEntity<>( publicRecognitionWall, HttpStatus.INTERNAL_SERVER_ERROR );
      }
    }
    else
    {
    	return new ResponseEntity<>( publicRecognitionWall, HttpStatus.UNAUTHORIZED );
    }
  }

  private boolean isAuthenticated( HttpServletRequest httpRequest )
  {
    String kongHeaderValue = httpRequest.getHeader( API_GATEWAY_HEADER );

    String contextName = systemVariableService.getContextName();
    String contextNameHash = new SHA256Hash().encrypt( contextName, false, false );

    return contextNameHash != null && contextNameHash.equals( kongHeaderValue );
  }

}

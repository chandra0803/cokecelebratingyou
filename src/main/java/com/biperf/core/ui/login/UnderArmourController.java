
package com.biperf.core.ui.login;

import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.biperf.core.service.underarmour.UnderArmourService;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.RequestUtil;
import com.biperf.core.utils.UserManager;

@Controller
@RequestMapping( "/ua" )
public class UnderArmourController
{

  @Autowired
  UnderArmourService underArmourService;
  private static final Log logger = LogFactory.getLog( UnderArmourController.class );

  @SuppressWarnings( "rawtypes" )
  @RequestMapping( value = "/underArmourCallback.action", method = RequestMethod.GET )
  public String underArmourCallback( @RequestParam String code, HttpServletRequest request ) throws Exception
  {
    logger.debug( "authCode=" + code );
    String state = request.getParameter( "state" );
    Map deserialize = ClientStateSerializer.deserialize( state, ClientStatePasswordManager.getGlobalPassword() );
    underArmourService.authorizeParticipant( UserManager.getUserId(), code );

    return "redirect:" + RequestUtil.getWebappRootUrl( request ) + deserialize.get( "redirectUrl" ) + "clientState=" + URLEncoder.encode( state, "UTF-8" ) + "&activeStep=stepUAConnect&cryptoPass=1";

  }
}

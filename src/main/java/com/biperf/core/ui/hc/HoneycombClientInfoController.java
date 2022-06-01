
package com.biperf.core.ui.hc;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.service.jms.GJavaMessageService;
import com.biperf.core.utils.jms.HoneycombInitializationMessage;

@Controller
@RequestMapping( "/hc" )
public class HoneycombClientInfoController
{

  private static final Log logger = LogFactory.getLog( HoneycombClientInfoController.class );

  @Autowired
  private GJavaMessageService gJavaMessageService;

  @RequestMapping( value = "/secureClientInfo.action", method = RequestMethod.POST )
  public @ResponseBody String secureClientInfo( @RequestBody SecureClientView view, HttpServletRequest httpRequest )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Got Honeycomb Client Info. Salt: " + view.getSalt() );
    }

    // Publish to JMS topic.
    // Any instance might get this call, so broadcast the message. Only the instance with a matching
    // token will actually process it.
    HoneycombInitializationMessage message = new HoneycombInitializationMessage();
    message.setToken( view.getToken() );
    message.setClientCode( view.getClientCode() );
    message.setSalt( view.getSalt() );
    message.setFarm( view.getFarm() );
    gJavaMessageService.sendToJmsTopic( message );

    // Returning empty string, otherwise it will try looking for a JSP
    return "";
  }

}

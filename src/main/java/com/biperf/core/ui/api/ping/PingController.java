
package com.biperf.core.ui.api.ping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.utils.FileExtractUtils;

@Controller
@RequestMapping( "/ping" )
public class PingController extends SpringBaseController
{

  @RequestMapping( value = "/version.action", method = RequestMethod.GET )
  public @ResponseBody Version ping()
  {
    Version version = new Version();
    try
    {
      version.setVersion( FileExtractUtils.getVersion() );
      return version;
    }
    catch( Exception e )
    {
      return version;
    }
  }
}

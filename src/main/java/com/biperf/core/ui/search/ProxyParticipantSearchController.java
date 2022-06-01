
package com.biperf.core.ui.search;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;

@Controller
@RequestMapping( "/proxyParticipantSearch" )
public class ProxyParticipantSearchController extends ParticipantSearchController
{
  @RequestMapping( value = "/proxyParticipantSearch.action", method = RequestMethod.POST )
  public @ResponseBody ParticipantSearchView proxyParticipantSearch( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest httpRequest ) throws Exception
  {
    Long loggedInUserId = UserManager.getUserId();
    model.setExcludeUserIds( Arrays.asList( String.valueOf( loggedInUserId ) ) );
    return participantPaxSearch( model, httpRequest );
  }

  @Override
  protected String getUrlEdit()
  {
    return PageConstants.PARTICIPANT_PROXY_NEW;
  }
}


package com.biperf.core.ui.search;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( "/participantFolloweeSearch" )
public class ParticipantFolloweeSearchController extends ParticipantSearchController
{
  @RequestMapping( value = "/participantFolloweeSearch.action", method = RequestMethod.POST )
  public @ResponseBody ParticipantSearchView proxyParticipantSearch( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest httpRequest ) throws Exception
  {
    return participantPaxSearch( model, httpRequest );
  }
}

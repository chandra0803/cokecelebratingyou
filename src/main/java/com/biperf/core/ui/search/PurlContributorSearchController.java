
package com.biperf.core.ui.search;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.objectpartners.cms.util.CmsResourceBundle;

@Controller
@RequestMapping( "/purlSearch" )
public class PurlContributorSearchController extends ParticipantSearchController
{
  @RequestMapping( value = "/purlContributors.action", method = RequestMethod.POST )
  public @ResponseBody ParticipantSearchView purlContributors( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest httpRequest ) throws Exception
  {
    ParticipantSearchView paxSearchView = participantPaxSearch( model, httpRequest );
    paxSearchView.setSourceType( CmsResourceBundle.getCmsBundle().getString( "participant.search.TEAM" ) );
    return paxSearchView;
  }
}

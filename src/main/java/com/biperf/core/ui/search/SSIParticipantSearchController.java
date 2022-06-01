
package com.biperf.core.ui.search;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.utils.UserManager;

@Controller
@RequestMapping( "/ssiParticipantSearch" )
public class SSIParticipantSearchController extends ParticipantSearchController
{
  @RequestMapping( value = "/ssiParticipantSearch.action", method = RequestMethod.POST )
  public @ResponseBody ParticipantSearchView ssiParticipantPaxSearch( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest request ) throws Exception
  {
    String userId = String.valueOf( UserManager.getUserId() );
    model.setExcludeUserIds( Arrays.asList( userId ) );
    ParticipantSearchView participantPaxSearch = participantPaxSearch( model, request );
    return participantPaxSearch;
  }
}

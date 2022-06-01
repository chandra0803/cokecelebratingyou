
package com.biperf.core.ui.search;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping( "/goalQuestSearch" )
public class GoalQuestParticipantSearchController extends ParticipantSearchController
{
  @RequestMapping( value = "/partners.action", method = RequestMethod.POST )
  public @ResponseBody ParticipantSearchView partnersSearch( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest request ) throws Exception
  {
    return super.participantPaxSearch( model, request );
  }
}


package com.biperf.core.ui.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;

public class FindOthersSearchAction extends ParticipantAutoCompleteSearchAction
{

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return super.generatePaxSearchView( mapping, actionForm, request, response );
  }

  @Override
  protected List<Participant> searchParticipants( HttpServletRequest request )
  {
    String searchType = request.getParameter( "searchType" );
    if ( !StringUtils.isEmpty( searchType ) && searchType.equalsIgnoreCase( "advanced" ) )
    {
      return super.searchParticipants( request );
    }

    ParticipantSearchCriteria criteria = getSearchCriteria( request );

    List<String> paxs = getParticipantService().searchCriteriaAutoComplete( criteria );

    // now, use what would show in the autocomplete (smith becomes Smith) and search on that
    List<Participant> allResults = new ArrayList<Participant>();
    if ( paxs != null && !paxs.isEmpty() )
    {
      // search using each item that would otherwise appear in the drop down autocomplete
      for ( String result : paxs )
      {
        criteria.setLastName( result );

        // search again!
        List<Participant> results = getParticipantService().searchParticipants( criteria );

        for ( Participant pax : results )
        {
          if ( !pax.getId().equals( UserManager.getUserId() ) )
          {
            allResults.add( pax );
          }
        }
      }
    }

    return allResults;
  }

  @Override
  protected List<Participant> filterUsers( List<Participant> participants, HttpServletRequest request )
  {
    // filter paxes
    for ( Iterator<Participant> paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Participant pax = paxIter.next();
      if ( pax.getId().equals( UserManager.getUserId() ) )
      {
        paxIter.remove();
      }
    }

    return participants;
  }

  @Override
  protected boolean getIsLocked( Participant pax )
  {
    if ( !pax.isAllowPublicInformation() )
    {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

}

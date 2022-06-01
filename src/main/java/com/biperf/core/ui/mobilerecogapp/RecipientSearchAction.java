
package com.biperf.core.ui.mobilerecogapp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.utils.UserManager;

public class RecipientSearchAction extends com.biperf.core.ui.recognition.RecipientSearchAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return super.generatePaxSearchView( mapping, form, request, response );
  }

  @Override
  protected List<Participant> searchParticipants( HttpServletRequest request )
  {
    ParticipantSearchCriteria submittedCriteria = getSearchCriteria( request );

    // FIRST: search on the last name
    ParticipantSearchCriteria criteria = new ParticipantSearchCriteria();
    criteria.setLastName( submittedCriteria.getLastName() );
    List<String> paxs = getParticipantService().searchCriteriaAutoComplete( criteria );

    // use what would show in the autocomplete for lastName (smith becomes Smith) and search on that
    List<Participant> allResults = new ArrayList<Participant>();
    if ( paxs != null && !paxs.isEmpty() )
    {
      // search using each item that would otherwise appear in the drop down autocomplete
      for ( String result : paxs )
      {
        criteria.setLastName( result );

        // search again!
        List<Participant> results = getParticipantService().searchParticipants( criteria );

        // filter
        List<Participant> filteredResults = filterUsers( results, request );

        for ( Participant pax : filteredResults )
        {
          if ( !pax.getId().equals( UserManager.getUserId() ) )
          {
            allResults.add( pax );
          }
        }
      }
    }

    // SECOND: only include the results where the first name starts with the submitted firstName
    List<Participant> results = new ArrayList<Participant>();
    String paxFirstName;
    String submittedFirstName = submittedCriteria.getFirstName();
    if ( submittedFirstName != null && submittedFirstName.trim().length() > 0 )
    {
      submittedFirstName = submittedFirstName.trim().toLowerCase();

      for ( Participant p : allResults )
      {
        paxFirstName = p.getFirstName();
        if ( paxFirstName != null && paxFirstName.trim().length() > 0 )
        {
          if ( paxFirstName.trim().toLowerCase().startsWith( submittedFirstName ) )
          {
            results.add( p );
          }
        }
      }
    }
    else
    {
      results.addAll( allResults );
    }

    return results;
  }
}

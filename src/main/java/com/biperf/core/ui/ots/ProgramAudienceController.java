
package com.biperf.core.ui.ots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseController;


public class ProgramAudienceController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    OTSProgramAudienceForm audienceForm = (OTSProgramAudienceForm)request.getAttribute( "otsProgramAudienceForm" );

    OTSProgram otsProgram = getOTSProgramService().getOTSProgramByProgramNumber( audienceForm.getProgramNumber() );
    request.setAttribute( "otsProgramDetails", otsProgram );
    List availableAudiences = getAudienceService().getAll();
    if ( !Objects.isNull( audienceForm ) )
    {
      List<Audience> availableProgramAudiences = getAvailableAudiences( audienceForm.getProgramAudienceAsList(), new ArrayList( availableAudiences ) );
      Collections.sort( availableProgramAudiences, new AudienceComparator() );
      request.setAttribute( "availableProgramAudiences", availableProgramAudiences );
    }
    else
    {
      request.setAttribute( "availableProgramAudiences", availableAudiences );

    }
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private OTSProgramService getOTSProgramService()
  {
    return (OTSProgramService)getService( OTSProgramService.BEAN_NAME );
  }

  public class AudienceComparator implements Comparator<Audience>
  {
    public int compare( Audience o1, Audience o2 )
    {
      return o1.getName().toLowerCase().compareTo( o2.getName().toLowerCase() );
    }
  }

  private List<Audience> getAvailableAudiences( List<OTSProgramAudienceFormBean> audiences, List<Audience> availableAudiences )
  {
    if ( audiences != null )
    {
      Iterator<Audience> audienceIterator = availableAudiences.iterator();
      while ( audienceIterator.hasNext() )
      {
        Audience audience = (Audience)audienceIterator.next();
        Iterator<OTSProgramAudienceFormBean> assignedIterator = audiences.iterator();
        while ( assignedIterator.hasNext() )
        {
          OTSProgramAudienceFormBean audienceBean = assignedIterator.next();
          if ( audienceBean.getAudienceId().equals( audience.getId() ) )
          {
            audienceIterator.remove();
          }
        }
      }
    }
    return availableAudiences;
  }
}

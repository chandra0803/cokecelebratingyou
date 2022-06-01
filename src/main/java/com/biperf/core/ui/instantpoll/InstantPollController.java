/**
 * 
 */

package com.biperf.core.ui.instantpoll;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.instantpoll.InstantPollAudienceFormBean;

/**
 * @author poddutur
 *
 */
public class InstantPollController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List audienceMethodList = new ArrayList();

    request.setAttribute( "audienceMethodList", audienceMethodList );

    InstantPollForm instantPollForm = (InstantPollForm)request.getAttribute( "instantPollForm" );

    List availableAudiences = getAudienceService().getAll();

    request.setAttribute( "availablePrimaryAudiences", getAvailableAudiences( instantPollForm.getPrimaryAudienceListAsList(), new ArrayList( availableAudiences ) ) );
    request.setAttribute( "instantPollForm", instantPollForm );

    boolean isDisabledFlag = isDisabledFlag( instantPollForm.getId(), instantPollForm.getStartDate() );
    request.setAttribute( "isDisabledFlag", isDisabledFlag );
  }

  private List getAvailableAudiences( List audiences, List availableAudiences )
  {
    if ( audiences != null )
    {
      Iterator audienceIterator = availableAudiences.iterator();
      while ( audienceIterator.hasNext() )
      {
        Audience audience = (Audience)audienceIterator.next();
        Iterator assignedIterator = audiences.iterator();
        while ( assignedIterator.hasNext() )
        {
          InstantPollAudienceFormBean audienceBean = (InstantPollAudienceFormBean)assignedIterator.next();
          if ( audienceBean.getAudienceId().equals( audience.getId() ) )
          {
            audienceIterator.remove();
          }
        }
      }
    }
    return availableAudiences;
  }

  private boolean isDisabledFlag( Long instantPollId, String startDate )
  {
    boolean isDisabledFlag = false;
    if ( instantPollId != null && !instantPollId.equals( new Long( 0 ) ) )
    {
      Date submissionStartDate = DateUtils.toDate( startDate );
      Date currentDate = DateUtils.getCurrentDate();
      isDisabledFlag = submissionStartDate != null && currentDate.after( submissionStartDate ) ? true : false;
    }
    return isDisabledFlag;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

}

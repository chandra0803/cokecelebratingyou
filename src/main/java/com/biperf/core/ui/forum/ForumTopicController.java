/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.forum.ForumAudienceFormBean;

/**
 * @author poddutur
 *
 */
public class ForumTopicController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List audienceMethodList = new ArrayList();

    request.setAttribute( "audienceMethodList", audienceMethodList );

    ForumTopicForm audienceForm = (ForumTopicForm)request.getAttribute( "forumTopicForm" );

    List availableAudiences = getAudienceService().getAll();

    request.setAttribute( "availablePrimaryAudiences", getAvailableAudiences( audienceForm.getPrimaryAudienceListAsList(), new ArrayList( availableAudiences ) ) );
    request.setAttribute( "forumTopicForum", audienceForm );
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
          ForumAudienceFormBean audienceBean = (ForumAudienceFormBean)assignedIterator.next();
          if ( audienceBean.getAudienceId().equals( audience.getId() ) )
          {
            audienceIterator.remove();
          }
        }
      }
    }
    return availableAudiences;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

}

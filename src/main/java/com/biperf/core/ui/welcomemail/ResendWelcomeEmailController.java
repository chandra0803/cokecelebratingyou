
package com.biperf.core.ui.welcomemail;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;

public class ResendWelcomeEmailController extends BaseController
{

  @SuppressWarnings( "rawtypes" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ResendWelcomeEmailForm resendWelcomeEmailForm = (ResendWelcomeEmailForm)request.getAttribute( "resendWelcomeEmailForm" );

    List allAudiences = getAudienceService().getAll();
    request.setAttribute( "availableAudiences", getAvailableAudiences( resendWelcomeEmailForm.getAudienceList(), allAudiences ) );

  }

  @SuppressWarnings( "rawtypes" )
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
          PromotionAudienceFormBean audienceBean = (PromotionAudienceFormBean)assignedIterator.next();
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

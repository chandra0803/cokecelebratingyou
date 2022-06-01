
package com.biperf.core.ui.diycommunication;

import java.util.Collections;
import java.util.Comparator;
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

public class DIYCommunicationsController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)request.getAttribute( "diyCommunicationsForm" );

    List<Audience> bannerAudiences = getAudienceService().getAll();
    List<Audience> newsStoriesAudiences = getAudienceService().getAll();
    List<Audience> resourceCenterAudiences = getAudienceService().getAll();
    List<Audience> tipsAudiences = getAudienceService().getAll();

    List<PromotionAudienceFormBean> selectedBannerAudiences = diyForm.getDiyBannersAudienceListAsList();
    Collections.sort( bannerAudiences, new AudienceBeanComparator() );
    request.setAttribute( "availableBannersAudiences", getAvailableAudiences( selectedBannerAudiences, bannerAudiences ) );

    List<PromotionAudienceFormBean> selectedNewsStoriesAudiences = diyForm.getDiyNewsStoriesAudienceListAsList();
    Collections.sort( newsStoriesAudiences, new AudienceBeanComparator() );
    request.setAttribute( "availableNewsStoriesAudiences", getAvailableAudiences( selectedNewsStoriesAudiences, newsStoriesAudiences ) );

    List<PromotionAudienceFormBean> selectedResourceCenterAudiences = diyForm.getDiyResourceCenterAudienceListAsList();
    Collections.sort( resourceCenterAudiences, new AudienceBeanComparator() );
    request.setAttribute( "availableResourceCenterAudiences", getAvailableAudiences( selectedResourceCenterAudiences, resourceCenterAudiences ) );

    List<PromotionAudienceFormBean> selectedTipsAudiences = diyForm.getDiyTipsAudienceListAsList();
    Collections.sort( tipsAudiences, new AudienceBeanComparator() );
    request.setAttribute( "availableTipsAudiences", getAvailableAudiences( selectedTipsAudiences, tipsAudiences ) );
  }

  private List getAvailableAudiences( List<PromotionAudienceFormBean> audiences, List<Audience> availableAudiences )
  {
    if ( audiences != null )
    {
      Iterator<Audience> audienceIterator = availableAudiences.iterator();
      while ( audienceIterator.hasNext() )
      {
        Audience audience = (Audience)audienceIterator.next();
        Iterator<PromotionAudienceFormBean> assignedIterator = audiences.iterator();
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

  public class AudienceBeanComparator implements Comparator<Audience>
  {
    public int compare( Audience o1, Audience o2 )
    {
      return o1.getName().toLowerCase().compareTo( o2.getName().toLowerCase() );
    }
  }

}

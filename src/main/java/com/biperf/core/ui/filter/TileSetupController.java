
package com.biperf.core.ui.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;

public class TileSetupController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    TileSetupForm setupForm = (TileSetupForm)request.getAttribute( "tileSetupForm" );

    List availableAudiences = getAudienceService().getAll();
    List availableAudiencesList = getAvailableAudiences( setupForm.getPrimaryAudienceListAsList(), new ArrayList( availableAudiences ) );
    Collections.sort( availableAudiencesList, new AudienceComparator() );
    request.setAttribute( "availablePrimaryAudiences", availableAudiencesList );
    List tileList = getFilterAppSetupService().getAllModuleApps();
    request.setAttribute( "tilesList", tileList );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

  private List getAvailableAudiences( List audiences, List availableAudiences )
  {
    if ( audiences != null && !audiences.isEmpty() )
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

  public class AudienceComparator implements Comparator<Audience>
  {
    public int compare( Audience o1, Audience o2 )
    {
      return o1.getName().toLowerCase().compareTo( o2.getName().toLowerCase() );
    }
  }

}

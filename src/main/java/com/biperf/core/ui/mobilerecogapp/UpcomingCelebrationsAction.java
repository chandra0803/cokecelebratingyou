
package com.biperf.core.ui.mobilerecogapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.purl.PurlCelebration;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.mobilerecogapp.view.UpcomingCelebrationsView;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

public class UpcomingCelebrationsAction extends BaseDispatchAction
{

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    UpcomingCelebrationsView celebrationsView = new UpcomingCelebrationsView();

    int pageSize = 20;
    int startIndex = 1;
    String sortedBy = "asc";
    String sortedOn = "4";

    if ( (String)request.getParameter( "sortedBy" ) != null )
    {
      sortedBy = request.getParameter( "sortedBy" );
    }
    if ( (String)request.getParameter( "sortedOn" ) != null )
    {

      String SORTED_ON = request.getParameter( "sortedOn" );
      if ( SORTED_ON.equalsIgnoreCase( "recipientName" ) )
      {
        sortedOn = "1";
      }
      else
      {
        sortedOn = "4";
      }
    }

    if ( (String)request.getParameter( "pageSize" ) != null )
    {
      String PAGE_SIZE = request.getParameter( "pageSize" );
      if ( !StringUtil.isEmpty( PAGE_SIZE ) )
      {
        pageSize = Integer.parseInt( PAGE_SIZE );
      }
    }

    if ( (String)request.getParameter( "startIndex" ) != null )
    {
      String START_INDEX = request.getParameter( "startIndex" );
      if ( !StringUtil.isEmpty( START_INDEX ) )
      {
        startIndex = Integer.parseInt( START_INDEX );
      }
    }

    List<PurlCelebration> purlCelebrationList = new ArrayList<PurlCelebration>();
    int celebrationCount = 0;
    // celebrationSetValueList = getPurlService().getUpcomingPurlRecipients("global", 50, "asc",
    // "3", 1, "Davis", null);
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      celebrationsView.setNewSAEnabled( true );
      purlCelebrationList = getServiceAnniversaryService().getUpcomingCelebrationRecipients( pageSize, sortedBy, sortedOn, startIndex );
      celebrationCount = getServiceAnniversaryService().getUpcomingCelebrationCount();
    }
    else
    {
      purlCelebrationList = getPurlService().getUpcomingPurlRecipients( pageSize, sortedBy, sortedOn, startIndex );
      celebrationCount = getPurlService().getUpcomingPurlRecipientsCount();
    }

    celebrationsView.setCelebrations( purlCelebrationList );
    celebrationsView.setCelebrationCount( celebrationCount );

    writeAsJsonToResponse( celebrationsView, response );

    // Need to set locale and primaryCountryCode for UserManager object.
    // These are
    // consumed by some service methods down the chain.
    Locale locale = UserManager.getLocale();

    return null;
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private ServiceAnniversaryService getServiceAnniversaryService()
  {
    return (ServiceAnniversaryService)getService( ServiceAnniversaryService.BEAN_NAME );
  }

}

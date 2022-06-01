
package com.biperf.core.ui.homepage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class PurlManagerController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.PURL_AVAILABLE ).getBooleanVal() )
    {
      AuthenticatedUser user = UserManager.getUser();
      if ( user.isParticipant() )
      {
        Participant participant = getParticipantService().getParticipantById( user.getUserId() );
        if ( participant.isOwner() )
        {
          List<Promotion> eligiblePromoList = getPurlService().getEligiblePurlPromotionsForInvitation( user.getUserId() );
          if ( eligiblePromoList != null && eligiblePromoList.size() > 0 )
          {
            List<Promotion> purlManagerPromoList = new ArrayList<Promotion>();
            for ( Promotion promotion : eligiblePromoList )
            {
              List<PurlRecipient> invitations = getPurlService().getAllPurlInvitationsForManager( participant.getId(), promotion.getId() );
              if ( !invitations.isEmpty() )
              {
                purlManagerPromoList.add( promotion );
              }
            }
            request.setAttribute( "purlManagerPromoList", purlManagerPromoList );
            request.setAttribute( "userId", user.getUserId() );
          }
        }
      }
    }
  }

  public static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  public static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  public static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  public static MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

}

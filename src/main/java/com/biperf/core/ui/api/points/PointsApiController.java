
package com.biperf.core.ui.api.points;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.objectpartners.cms.util.CmsResourceBundle;

@Controller
@RequestMapping( "/api/pointsapi/1" )
public class PointsApiController extends SpringBaseController
{

  public static String API_GATEWAY_HEADER = "API-Gateway-Auth";

  private static final Log logger = LogFactory.getLog( PointsApiController.class );

  public @Autowired SystemVariableService systemVariableService;
  public @Autowired ParticipantService participantService;

  @RequestMapping( value = "/pointBalance.action", method = RequestMethod.GET )
  public @ResponseBody PointBalanceView pointBalance( @RequestParam( value = "username", required = true ) String username, HttpServletRequest httpRequest )
  {
    PointBalanceView pointBalanceView = new PointBalanceView();
    pointBalanceView.setReturnCode( 99L );
    pointBalanceView.setPointBalance( 0L );

    if ( isAuthenticated( httpRequest ) )
    {
      ProjectionCollection projections = new ProjectionCollection();
      projections.add( new ProjectionAttribute( "awardBanqNumberDecrypted" ) );
      Participant pax = participantService.getParticipantByUserNameWithProjections( username, projections );

      if ( pax == null )
      {
        pointBalanceView.setReturnMessage( CmsResourceBundle.getCmsBundle().getString( "api.point.balance.PAX_NOT_FOUND" ) );
        return pointBalanceView;
      }

      try
      {
        Long balance = getAwardBanQService().getAccountBalanceForParticipantWebService( pax.getId(), pax.getAwardBanqNumber() );

        pointBalanceView.setPointBalance( balance );
        pointBalanceView.setReturnMessage( CmsResourceBundle.getCmsBundle().getString( "api.point.balance.SUCCESS" ) );
        pointBalanceView.setReturnCode( 0L );
      }
      catch( Exception e )
      {
        logger.error( "Error getting balance for username: " + username, e );
        pointBalanceView.setReturnMessage( CmsResourceBundle.getCmsBundle().getString( "api.point.balance.BALANCE_ERROR" ) );
        return pointBalanceView;
      }
    }
    else
    {
      pointBalanceView.setReturnMessage( CmsResourceBundle.getCmsBundle().getString( "api.point.balance.INVALID_HEADER" ) );
    }

    return pointBalanceView;
  }

  private boolean isAuthenticated( HttpServletRequest httpRequest )
  {
    String kongHeaderValue = httpRequest.getHeader( API_GATEWAY_HEADER );

    String contextName = systemVariableService.getContextName();
    String contextNameHash = new SHA256Hash().encrypt( contextName, false, false );

    return contextNameHash != null && contextNameHash.equals( kongHeaderValue );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }

}

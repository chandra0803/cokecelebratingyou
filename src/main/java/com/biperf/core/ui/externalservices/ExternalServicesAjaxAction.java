
package com.biperf.core.ui.externalservices;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;

/**
 * ExternalServicesAjaxAction.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>kothanda</td>
 * <td>Feb. 28, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ExternalServicesAjaxAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ExternalServicesAjaxAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return super.execute( mapping, form, request, response );
  }

  public ActionForward getBalance( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( isIncludeBalance() )
    {
      PrintWriter out = response.getWriter();
      try
      {
        AuthenticatedUser authUser = UserManager.getUser();
        if ( authUser != null && authUser.getUserId() != null && !isGiftCodeOnlyPax() )
        {
          if ( UserManager.getUser().isParticipant() )
          {
            Long balance = getAwardBanQService().getAccountBalanceForParticipantId( authUser.getUserId() );
            authUser.setAwardBanQBalance( balance );
            out.print( NumberFormatUtil.getLocaleBasedNumberFormat( balance ) );
          }
        }
      }
      catch( Exception e )
      {
        logger.error( "Error during getting user account balance from shared services." );
        out.print( "0" );
      }
    }
    return null;
  }

  private boolean isGiftCodeOnlyPax()
  {
    return UserManager.getUser().isParticipant() && UserManager.getUser().isGiftCodeOnlyPax();
  }

  private boolean isIncludeBalance()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.BOOLEAN_INCLUDE_BALANCE ).getBooleanVal();
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return factory.getAwardBanQService();
  }

  private static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}

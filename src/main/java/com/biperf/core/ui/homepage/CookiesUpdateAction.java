package com.biperf.core.ui.homepage;

import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.user.UserCookiesAcceptance;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.participant.UserCookiesAcceptanceService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class CookiesUpdateAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( CookiesUpdateAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      AuthenticatedUser authUser = UserManager.getUser();
      if ( authUser != null && authUser.getUserId() != null )
      {
        if ( UserManager.getUser().isParticipant() && !UserManager.getUser().isDelegate() && !UserManager.getUser().isLaunched() )
        {
          UserCookiesAcceptance userAccpObj = getUserCookiesAcceptanceService().getUserCookiesAcceptanceDetailsByPaxID( UserManager.getUserId() );
          if ( Objects.isNull( userAccpObj ) )
          {
            UserCookiesAcceptance userCookiesAcceptance = new UserCookiesAcceptance();
            userCookiesAcceptance.setUserId( UserManager.getUserId() );
            userCookiesAcceptance.setAcceptanceDate( new Date() );
            Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "admin.privacy.cookies" ) );
            userCookiesAcceptance.setPolicyVersion( (long)0 );
            if ( !Objects.isNull( content ) )
            {
              userCookiesAcceptance.setPolicyVersion( new Long( content.getVersion() ) );
            }
            getUserCookiesAcceptanceService().saveUserCookiesAcceptanceDetails( userCookiesAcceptance );
          }
        }
      }
    }
    catch( Exception e )
    {
      logger.error( "Error while saving user, might be already accepted/concurrent request: " + e.getMessage() );
    }
    super.writeAsJsonToResponse( "", response );
    return null;
    // return mapping.findForward( "success" );
  }

  private static UserCookiesAcceptanceService getUserCookiesAcceptanceService()
  {
    return (UserCookiesAcceptanceService)getService( UserCookiesAcceptanceService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}

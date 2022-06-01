
package com.biperf.core.ui.message;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

public class MessageDetailController extends BaseController
{
  private static final Log logger = LogFactory.getLog( MessageDetailController.class );

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      Long commLogId = null;
      CommLog commLog = null;
      try
      {
        commLogId = (Long)clientStateMap.get( "commLogId" );
      }
      catch( ClassCastException e )
      {
        commLogId = new Long( (String)clientStateMap.get( "commLogId" ) );
      }

      if ( commLogId != null )
      {
        // Commented based on the bug 72219 for performance issue
        // AssociationRequestCollection requestCollection = new AssociationRequestCollection();
        // requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
        // requestCollection.add( new CommLogAssociationRequest(
        // CommLogAssociationRequest.MAILING_WITH_RECIPIENTS_AND_LOCALES ) );
        commLog = getCommLogService().getCommLogById( commLogId, new AssociationRequestCollection() );
        String messgae = commLog.getMessage();
        String customMessage = messgae.replaceAll( "\"\"", "''" );
        // QC bug #1959 fix
        customMessage = customMessage.replace( "color: purple !important;", "" );
        customMessage = customMessage.replace( "color: blue !important;", "" );

        String headBodyString = StringUtils.substringBetween( customMessage, "<head>", "</body>" );
        String headBodyStringNew = "<head>" + headBodyString + "</body>";

        request.setAttribute( "htmlFullMessage", messgae );
        request.setAttribute( "commLog", headBodyStringNew );
        request.setAttribute( "messageTitle", commLog.getSubject() );
        request.setAttribute( "messageDate", DateUtils.toDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( commLog.getDateInitiated(), UserManager.getTimeZoneID() ) ) ) );
        Locale locale = UserManager.getLocale();
        request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( locale ) );
      }
      else
      {
        logger.error( "commLogId not found in client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  private CommLogService getCommLogService()
  {
    return (CommLogService)getService( CommLogService.BEAN_NAME );
  }
}

/**
 * 
 */

package com.biperf.core.ui.purl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.PurlCelebrationTabType;
import com.biperf.core.domain.enums.PurlRecipientType;
import com.biperf.core.domain.purl.PurlCelebrateMainView;
import com.biperf.core.domain.purl.PurlCelebrationSet;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;

/**
 * @author poddutur
 *
 */
public class PurlCelebrateAction extends BaseDispatchAction
{
  private static final Log log = LogFactory.getLog( PurlCelebrateAction.class );
  public static final String UPCOMING = PurlRecipientType.UPCOMING.getCode();

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   * @throws ServletException
   * @throws InvalidClientStateException 
   * @throws ServiceErrorException 
   */
  public ActionForward fetchRecognitionPurls( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
      throws IOException, ServletException, InvalidClientStateException
  {
    int PAGE_SIZE = 50;
    String sortedBy = null;
    String sortedOn = "4";
    String purlPastPresentSelect = UPCOMING;
    String tabType = request.getParameter( "celebrationId" );
    String lastName = request.getParameter( "name" );
    String tile = request.getParameter( "tile" );
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    String listValue = request.getParameter( "listValue" );
    if( null == listValue || "".equals(listValue) || listValue.isEmpty() ){
    	listValue = getUserCharacteristicService().getCharacteristicValueByDivisionKeyAndUserId(UserManager.getUserId() );
    }
    
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      if ( (String)clientStateMap.get( "purlPastPresentSelect" ) != null )
      {
        purlPastPresentSelect = (String)clientStateMap.get( "purlPastPresentSelect" );
      }
    }

    if ( tile != null )
    {
      PAGE_SIZE = 15;
    }

    if ( request.getParameter( "purlPastPresentSelect" ) != null )
    {
      purlPastPresentSelect = request.getParameter( "purlPastPresentSelect" );
    }

    if ( (String)request.getParameter( "sortedOn" ) != null && (String)request.getParameter( "sortedBy" ) != null )
    {
      sortedOn = request.getParameter( "sortedOn" );
      sortedBy = request.getParameter( "sortedBy" ) != null && !StringUtils.isEmpty( request.getParameter( "sortedBy" ) ) ? request.getParameter( "sortedBy" ) : "asc";
    }
    else
    {
      sortedBy = "asc";
    }

    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    PurlCelebrateMainView view = new PurlCelebrateMainView();
    try
    {
      if ( tabType == null )
      {
        tabType = getSystemVariableService().getPropertyByName( SystemVariableService.PURL_CELEBRATION_DEFAULT_TAB_NAME ).getStringVal();
        if ( tabType == null )
        {
          tabType = PurlCelebrationTabType.lookup( PurlCelebrationTabType.RECOMMENDED_TAB ).getCode();
        }
      }
      if ( purlPastPresentSelect.equals( UPCOMING ) )
      {
        celebrationSetValueList = getPurlService().getUpcomingPurlRecipients( tabType, PAGE_SIZE, sortedBy, sortedOn, getPageNumber( request ), lastName, tile, listValue );
      }
      else
      {
        celebrationSetValueList = getPurlService().getAwardedPurlRecipients( tabType, PAGE_SIZE, sortedBy, sortedOn, getPageNumber( request ), lastName, tile, listValue );
      }
      view.setCelebrationSets( celebrationSetValueList );
    }
    catch( ServiceErrorException se )
    {
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( se.getServiceErrorsCMText().get( 0 ) );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    request.setAttribute( "purlPastPresentSelect", purlPastPresentSelect );
    return null;
  }

  private PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
 
  /**
   * 
   * @param request
   * @return pageNumber
   */
  private int getPageNumber( HttpServletRequest request )
  {
    String pageNumber = request.getParameter( "currentPage" );
    if ( !StringUtil.isEmpty( pageNumber ) )
    {
      return Integer.parseInt( pageNumber );
    }
    return 1;
  }

}

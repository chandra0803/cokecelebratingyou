
package com.biperf.core.ui.serviceanniversary;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.serviceanniversary.SAValueBean;

public class SAContributionListAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( SAContributionListAction.class );

  @SuppressWarnings( "unchecked" )
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    Long userId = UserManager.getUser().getUserId();
    Long programId = getProgramId( request );

    List<SAValueBean> saContributionsList = getServiceAnniversaryService().getAllPendingSAContributions( userId, programId );
    logger.info( saContributionsList );

    request.setAttribute( "saContributionList", saContributionsList );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private Long getProgramId( HttpServletRequest request ) throws InvalidClientStateException
  {
    Long programId = null;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    try
    {
      programId = (Long)clientStateMap.get( "programId" );
    }
    catch( ClassCastException cce )
    {
      programId = new Long( (String)clientStateMap.get( "programId" ) );
    }
    return programId;
  }

  private ServiceAnniversaryService getServiceAnniversaryService()
  {
    return (ServiceAnniversaryService)getService( ServiceAnniversaryService.BEAN_NAME );
  }

}

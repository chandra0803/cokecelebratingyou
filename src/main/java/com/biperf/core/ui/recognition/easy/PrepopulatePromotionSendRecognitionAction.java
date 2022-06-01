
package com.biperf.core.ui.recognition.easy;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.user.UserNode;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.recognition.SendRecognitionForm;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

public class PrepopulatePromotionSendRecognitionAction extends BaseRecognitionAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    final Long PROMOTION_ID = getPromotionId( request );

    SendRecognitionForm state = new SendRecognitionForm();
    state.setPromotionId( PROMOTION_ID );
    populatePrimaryNodeId( state );

    RecognitionStateManager.store( state, request );
    return mapping.findForward( "success" );
  }

  @SuppressWarnings( "rawtypes" )
  private Long getPromotionId( HttpServletRequest request ) throws InvalidClientStateException
  {
    Long promotionId = null;

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
      promotionId = (Long)clientStateMap.get( REQUEST_PARAM_PROMOTION_ID );
    }
    catch( ClassCastException cce )
    {
      promotionId = new Long( (String)clientStateMap.get( REQUEST_PARAM_PROMOTION_ID ) );
    }
    return promotionId;
  }

  public void populatePrimaryNodeId( SendRecognitionForm state )
  {
    Long userId = UserManager.getUserId();

    Set<UserNode> userNodes = getUserService().getUserNodes( userId );

    if ( userNodes.size() == 1 )
    {
      state.setNodeId( getUserService().getPrimaryUserNode( userId ).getNode().getId() );
    }
  }

}

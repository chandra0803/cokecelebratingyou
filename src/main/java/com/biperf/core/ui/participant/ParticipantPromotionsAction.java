/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantPromotionsAction.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/*
 * ParticipantPromotionsAction <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Nov
 * 16, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ParticipantPromotionsAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( ParticipantPromotionsAction.class );

  public ActionForward firstRun( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    // Clear the picklist from the session.
    request.getSession().setAttribute( "moduleTypeList", null );

    ParticipantPromotionsForm form = (ParticipantPromotionsForm)actionForm;

    // put user_id on form
    String userId = "";
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
      try
      {
        userId = (String)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        userId = ( (Long)clientStateMap.get( "userId" ) ).toString();
      }
      if ( userId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
      form.setUserId( userId );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // put module type on form
    String moduleType = RequestUtils.getRequiredParamString( request, "moduleType" );
    form.setModuleType( moduleType );

    // put list of promotionpaxvalue objects on form
    List paxPromotions = new ArrayList();
    if ( moduleType.equals( "all" ) )
    {
      paxPromotions = getPromotionService().getAllLiveAndExpiredByUserId( new Long( userId ) );
    }
    else
    {
      paxPromotions = getPromotionService().getAllLiveAndExpiredByTypeAndUserId( moduleType, new Long( userId ) );
    }

    PropertyComparator.sort( paxPromotions, new MutableSortDefinition( "promotion.promotionName", true, true ) );
    PropertyComparator.sort( paxPromotions, new MutableSortDefinition( "moduleCode", true, true ) );

    form.setPromotions( paxPromotions );
    request.setAttribute( "promotionPaxValueList", paxPromotions );

    return actionMapping.findForward( forwardAction );

  }

  public ActionForward display( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "display";

    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    ParticipantPromotionsForm participantPromotionsForm = (ParticipantPromotionsForm)actionForm;

    // put user_id on form
    String userId = RequestUtils.getOptionalParamString( request, "userId" );
    if ( userId == null || userId.length() == 0 )
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
        userId = (String)clientStateMap.get( "userId" );
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }
    participantPromotionsForm.setUserId( userId );

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", participantPromotionsForm.getUserId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=display" } );
    }

    // put module type on form
    String moduleType = RequestUtils.getRequiredParamString( request, "moduleType" );
    participantPromotionsForm.setModuleType( moduleType );

    // put list of promotionpaxvalue objects on form
    List paxPromotions = new ArrayList();
    if ( moduleType.equals( "all" ) )
    {
      paxPromotions = getPromotionService().getAllLiveAndExpiredByUserId( new Long( userId ) );
    }
    else
    {
      paxPromotions = getPromotionService().getAllLiveAndExpiredByTypeAndUserId( moduleType, new Long( userId ) );
    }

    PropertyComparator.sort( paxPromotions, new MutableSortDefinition( "promotion.promotionName", true, true ) );
    PropertyComparator.sort( paxPromotions, new MutableSortDefinition( "moduleCode", true, true ) );

    participantPromotionsForm.setPromotions( paxPromotions );
    request.setAttribute( "promotionPaxValueList", paxPromotions );

    return actionMapping.findForward( forwardAction );
  }

  /**
   * Gets the Promotion Service
   * 
   * @return Promotion Service
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  } // end getPromotionService
}

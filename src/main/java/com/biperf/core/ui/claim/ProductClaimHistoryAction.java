/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/QuizHistoryAction.java,v $
 *
 */

package com.biperf.core.ui.claim;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;

/**
 * ProductClaimHistoryAction.
 * 
 * @author arasi
 * @since Jul 30, 2013
 * @version 1.0
 */
public class ProductClaimHistoryAction extends BaseDispatchAction
{
  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

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

      String isFullPage = (String)clientStateMap.get( "isFullPage" );
      if ( !StringUtil.isEmpty( isFullPage ) )
      {
        if ( !Boolean.valueOf( isFullPage ) )
        {
          forward = mapping.findForward( ActionConstants.SHEET_VIEW );
        }
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return forward;
  }

}

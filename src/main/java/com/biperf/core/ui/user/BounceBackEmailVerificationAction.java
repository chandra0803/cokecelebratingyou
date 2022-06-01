
package com.biperf.core.ui.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.SystemVariableType;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;

public class BounceBackEmailVerificationAction extends BaseDispatchAction
{

  /**
   * Updates the "bounce back email verification" system variable to verified.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ActionMessages errors = new ActionMessages();

    try
    {
      PropertySetItem prop = null;
      prop = getSystemVariableService().getPropertyByName( SystemVariableService.BOUNCEBACK_EMAIL_VERIFIED );

      if ( prop == null )
      {
        prop = new PropertySetItem();
        prop.setEntityName( SystemVariableService.BOUNCEBACK_EMAIL_VERIFIED );
        prop.setKey( "bounce back email verified?" );
        prop.setType( SystemVariableType.lookup( SystemVariableType.BOOLEAN ) );
      }

      // mark verified
      prop.setBooleanVal( new Boolean( true ) );

      getSystemVariableService().saveProperty( prop );
    }
    catch( ServiceErrorException serviceException )
    {
      List serviceErrors = serviceException.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forwardTo );
  }

  // Private Methods
  /**
   * Get the SystemVariableService From the bean factory through a locator.
   * 
   * @return SystemVariableService
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}

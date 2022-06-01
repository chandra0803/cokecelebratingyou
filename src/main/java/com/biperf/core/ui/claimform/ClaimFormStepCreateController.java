/*
 (c) 2005 BI, Inc.  All rights reserved.
 File: ClaimFormStepCreateController.java
 
 Change History:

 Author       Date      Version  Comments
 -----------  --------  -------  -----------------------------
 crosenquest      Jun 3, 2005   1.0      Created
 
 */

package com.biperf.core.ui.claimform;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.ClaimFormStepApprovalType;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * @author crosenquest
 */
public class ClaimFormStepCreateController extends BaseController
{

  /** LOG */
  private static final Log LOG = LogFactory.getLog( ClaimFormStepCreateController.class );

  /**
   * Manages preparing the request for creating a new ClaimFormStep. Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    LOG.debug( ">>> execute" );

    // Get the required claimFormId from the request
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
      String claimFormId = (String)clientStateMap.get( "claimFormId" );
      if ( claimFormId != null )
      {
        // Load the claimForm by claimFormId and put it into the request
        ClaimForm claimForm = getClaimFormService().getClaimFormById( new Long( claimFormId ) );
        request.setAttribute( "claimForm", claimForm );

        // Put the pickList items for ClaimFormStepApprovalTypeList into the request.
        request.setAttribute( "claimFormStepApprovalTypeList", ClaimFormStepApprovalType.getList() );

        if ( claimForm.getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) )
        {
          // Put the pickList items for ClaimFormStepEmailNotificationType into the request.
          request.setAttribute( "claimFormStepEmailNotificationTypeList", ClaimFormStepEmailNotificationType.getClaimNotificationList() );
          request.setAttribute( "showProofOfSale", new Boolean( true ) );
        }
        else if ( claimForm.getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_RECOGNITION ) )
        {
          // Put the pickList items for ClaimFormStepEmailNotificationType into the request.
          request.setAttribute( "claimFormStepEmailNotificationTypeList", ClaimFormStepEmailNotificationType.getRecognitionNotificationList() );
          request.setAttribute( "showProofOfSale", new Boolean( false ) );
        }
        else if ( claimForm.getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_GOALQUEST ) )
        {
          // Put the pickList items for ClaimFormStepEmailNotificationType into the request.
          // request
          // .setAttribute( "claimFormStepEmailNotificationTypeList",
          // ClaimFormStepEmailNotificationType.getRecognitionNotificationList() );
          request.setAttribute( "showProofOfSale", new Boolean( false ) );
        }
        else if ( claimForm.getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_NOMINATION ) )
        {
          //Client Customization wip :59461
          request.setAttribute( "claimFormStepEmailNotificationTypeList", ClaimFormStepEmailNotificationType.getNominationNotificationList() );
          //Client Customization wip :59461 end
          request.setAttribute( "showProofOfSale", new Boolean( false ) );
        }
      }
      else
      {
        LOG.error( "claimFormId not found in client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    LOG.debug( "<<< execute" );
  }

  /**
   * Get the ClaimFormService.
   * 
   * @return ClaimFormService
   */
  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

}

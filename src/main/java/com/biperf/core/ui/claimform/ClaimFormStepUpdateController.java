/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/ClaimFormStepUpdateController.java,v $
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
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;

/**
 * ClaimFormStepUpdateController.
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
 * <td>crosenquest</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepUpdateController extends BaseController
{

  /** LOG */
  private static final Log LOG = LogFactory.getLog( ClaimFormStepUpdateController.class );

  /**
   * Control display for updating the ClaimFormStep. Overridden from
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
    Long claimFormId = new Long( 0 );

    // Get the required ClaimFormId from the request.
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

      if ( RequestUtils.containsAttribute( request, "claimFormId" ) )
      {
        claimFormId = RequestUtils.getRequiredAttributeLong( request, "claimFormId" );
      }
      else
      {
        String s = (String)clientStateMap.get( "claimFormId" );
        claimFormId = new Long( s );
      }
    }
    catch( IllegalArgumentException iae )
    {
      LOG.error( "ClaimFormStepUpdateController.execute failed to get required request param claimFormId: " + iae.getMessage() );
    }

    // Load the claimForm by claimFormId and put it into the request
    ClaimForm claimForm = getClaimFormService().getClaimFormById( claimFormId );
    request.setAttribute( "claimForm", claimForm );

    if ( claimForm.getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) )
    {
      request.setAttribute( "showProofOfSale", new Boolean( true ) );
      // Put the pickList items for ClaimFormStepEmailNotificationType into the request.
      request.setAttribute( "claimFormStepEmailNotificationTypeList", ClaimFormStepEmailNotificationType.getClaimNotificationList() );
    }
    else if ( claimForm.getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_RECOGNITION ) )
    {
      request.setAttribute( "showProofOfSale", new Boolean( false ) );
      // Put the pickList items for ClaimFormStepEmailNotificationType into the request.
      request.setAttribute( "claimFormStepEmailNotificationTypeList", ClaimFormStepEmailNotificationType.getRecognitionNotificationList() );
    }
    else if ( claimForm.getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_GOALQUEST ) )
    {
      request.setAttribute( "showProofOfSale", new Boolean( false ) );
      // Put the pickList items for ClaimFormStepEmailNotificationType into the request.
      // request.setAttribute( "claimFormStepEmailNotificationTypeList",
      // ClaimFormStepEmailNotificationType.getRecognitionNotificationList() );
    }
    else if ( claimForm.getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_NOMINATION ) )
    {
      request.setAttribute( "showProofOfSale", new Boolean( false ) );
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

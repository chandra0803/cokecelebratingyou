/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/ClaimFormStepViewController.java,v $
 */

package com.biperf.core.ui.claimform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimFormStepAssociationRequest;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ClaimFormStepViewController.
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
 * <td>zahler</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepViewController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( ClaimFormStepViewController.class );

  /**
   * Overridden from
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
    ClaimFormStepViewForm form = (ClaimFormStepViewForm)request.getAttribute( ClaimFormStepViewForm.FORM_NAME );
    if ( form == null )
    {
      form = new ClaimFormStepViewForm();
    }

    Long claimFormStepId = null;
    if ( RequestUtils.containsAttribute( request, "claimFormStepId" ) )
    {
      claimFormStepId = RequestUtils.getRequiredAttributeLong( request, "claimFormStepId" );
    }
    else
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
        try
        {
          claimFormStepId = (Long)clientStateMap.get( "claimFormStepId" );
        }
        catch( ClassCastException cce )
        {
          String s = (String)clientStateMap.get( "claimFormStepId" );
          if ( s != null && !s.equals( "" ) )
          {
            claimFormStepId = new Long( s );
          }
        }
        if ( claimFormStepId == null )
        {
          LOG.error( "promotionId not found in client state" );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }
    if ( claimFormStepId != null )
    {
      form.setClaimFormStepId( String.valueOf( claimFormStepId ) );
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimFormStepAssociationRequest() );
      ClaimFormStep claimFormStep = getClaimFormService().getClaimFormStepWithAssociations( claimFormStepId, associationRequestCollection );
      request.setAttribute( "claimFormStep", claimFormStep );
      request.setAttribute( "claimFormStepElements", claimFormStep.getClaimFormStepElements() );
      if ( claimFormStep.getClaimForm().getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_PRODUCT_CLAIMS ) )
      {
        request.setAttribute( "showProofOfSale", new Boolean( true ) );
      }
      else if ( claimFormStep.getClaimForm().getClaimFormModuleType().getCode().equals( ClaimFormModuleType.MODULE_RECOGNITION ) )
      {
        request.setAttribute( "showProofOfSale", new Boolean( false ) );
      }

      form.setClaimFormId( String.valueOf( claimFormStep.getClaimForm().getId() ) );
      form.setModuleType( claimFormStep.getClaimForm().getClaimFormModuleType().getCode() );
      if ( claimFormStep.getClaimFormStepElements() != null )
      {
        form.setClaimFormStepElementsSize( claimFormStep.getClaimFormStepElements().size() );
      }
      request.setAttribute( ClaimFormStepViewForm.FORM_NAME, form );

      List selectableClaimFormElements = new ArrayList( ClaimFormElementType.getList() );
      selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.ADDRESS_BOOK_SELECTION ) );
      selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.BOOLEAN_CHECKBOX ) );
      selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.ADDRESS_BLOCK ) );
      // REMOVE data types based on module
      if ( claimFormStep.getClaimForm().getClaimFormModuleType().isSsi() )
      {
        selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.BOOLEAN ) );
        selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.BUTTON ) );
        selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.COPY_BLOCK ) );
        selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.LINK ) );
        selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.MULTI_SELECTION ) );
        selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.SECTION_HEADING ) );
        selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.SELECTION ) );
      }
      else
      {
        selectableClaimFormElements.remove( ClaimFormElementType.lookup( ClaimFormElementType.FILE ) );
      }
      request.setAttribute( "claimFormStepElementTypes", selectableClaimFormElements );
    }
  }

  /**
   * @return ClaimFormDefinitionService
   */
  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

}

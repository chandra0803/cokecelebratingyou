/**
 * 
 */

package com.biperf.core.ui.productclaim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author poddutur
 *
 */
public class ProductClaimPreviewValidationAction extends BaseDispatchAction
{
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return previewClaim( mapping, form, request, response );
  }

  private ActionForward previewClaim( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ProductClaimSubmissionForm productClaimSubmissionForm = (ProductClaimSubmissionForm)form;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.TEAM_POSITIONS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_PARTICIPANTS ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( productClaimSubmissionForm.getPromotionId(), associationRequestCollection );
    productClaimSubmissionForm.setPromotionName( promotion.getName() );

    Node node = getNodeService().getNodeById( productClaimSubmissionForm.getNodeId() );
    if ( node != null )
    {
      productClaimSubmissionForm.setOrgUnitName( node.getName() );
    }

    if ( productClaimSubmissionForm.getClaimElements() != null )
    {
      ClaimFormDefinitionService claimFormDefinitionService = getClaimFormDefinitionService();
      for ( Iterator<ClaimElementForm> iter = productClaimSubmissionForm.getClaimElements().iterator(); iter.hasNext(); )
      {
        ClaimElementForm claimElementForm = iter.next();
        claimElementForm.setClaimFormStepElement( claimFormDefinitionService.getClaimFormStepElementById( claimElementForm.getClaimFormStepElementId() ) );
      }
    }

    Claim claim = productClaimSubmissionForm.toDomainObject( null, promotion, node );

    // validate claim elements
    WebErrorMessageList messages = new WebErrorMessageList();
    List<WebErrorMessage> webErrorMessages = new ArrayList<WebErrorMessage>();
    List serviceErrors = getClaimService().validateClaimElements( claim, productClaimSubmissionForm.getParticipantsCount() );
    if ( !serviceErrors.isEmpty() )
    {
      for ( Object obj : serviceErrors )
      {
        WebErrorMessage message = new WebErrorMessage();
        ServiceError error = (ServiceError)obj;
        String errorMessage = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
        if ( StringUtils.isNotEmpty( error.getArg1() ) )
        {
          message.setName( error.getArg1() );
          errorMessage = errorMessage.replace( "{0}", error.getArg1() );
        }
        if ( StringUtils.isNotEmpty( error.getArg2() ) )
        {
          message.setName( error.getArg2() );
          errorMessage = errorMessage.replace( "{1}", error.getArg2() );
        }
        if ( StringUtils.isNotEmpty( error.getArg3() ) )
        {
          message.setName( error.getArg3() );
          errorMessage = errorMessage.replace( "{2}", error.getArg3() );
        }
        errorMessage = errorMessage.replace( "???", "" );
        errorMessage = errorMessage.replace( "???", "" );
        message.setText( errorMessage );
        message.setType( "error" );
        error.setArg1( errorMessage );
        webErrorMessages.add( message );
      }

      messages.setMessages( webErrorMessages );

      request.setAttribute( "productClaimSubmissionForm", productClaimSubmissionForm );

      ProductClaimStateManager.store( productClaimSubmissionForm, request );
      ProductClaimStateManager.addToRequest( productClaimSubmissionForm, request );

      super.writeAsJsonToResponse( messages, response );
      return null;
    }

    request.setAttribute( "productClaimSubmissionForm", productClaimSubmissionForm );

    ProductClaimStateManager.store( productClaimSubmissionForm, request );
    ProductClaimStateManager.addToRequest( productClaimSubmissionForm, request );

    super.writeAsJsonToResponse( messages, response );
    return null;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }
}

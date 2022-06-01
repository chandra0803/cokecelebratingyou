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
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.biperf.core.ui.productclaim.ProductPreviewRedirectView.Message;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.PageConstants;

/**
 * @author poddutur
 *
 */
public class ProductClaimPreviewAction extends BaseDispatchAction
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
      for ( Iterator iter = productClaimSubmissionForm.getClaimElements().iterator(); iter.hasNext(); )
      {
        ClaimElementForm claimElementForm = (ClaimElementForm)iter.next();
        claimElementForm.setClaimFormStepElement( claimFormDefinitionService.getClaimFormStepElementById( claimElementForm.getClaimFormStepElementId() ) );
      }
    }

    Claim claim = productClaimSubmissionForm.toDomainObject( null, promotion, node );

    HttpSession session = request.getSession();
    session.setAttribute( "productClaimSubmissionForm", productClaimSubmissionForm );

    ProductClaimStateManager.store( productClaimSubmissionForm, request );
    ProductClaimStateManager.addToRequest( productClaimSubmissionForm, request );

    String forumRedirectUrl = RequestUtils.getBaseURI( request ) + PageConstants.PRODUCT_CLAIM_PREVIEW_PAGE_URL;

    ProductPreviewRedirectView productPreviewRedirectView = new ProductPreviewRedirectView();
    List<Message> messages = new ArrayList<Message>();
    Message message = new Message();

    message.setType( "serverCommand" );
    message.setCommand( "redirect" );
    message.setRedirectUrl( forumRedirectUrl );

    messages.add( message );
    productPreviewRedirectView.setMessages( messages );

    super.writeAsJsonToResponse( productPreviewRedirectView, response );
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

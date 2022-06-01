/**
 * 
 */

package com.biperf.core.ui.productclaim;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author poddutur
 *
 */
public class ProductClaimSubmitAction extends BaseDispatchAction
{
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return submitClaim( mapping, form, request, response );
  }

  private ActionForward submitClaim( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ProductClaimSubmissionForm productClaimSubmissionForm = ProductClaimStateManager.get( request );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.TEAM_POSITIONS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_PARTICIPANTS ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( productClaimSubmissionForm.getPromotionId(), associationRequestCollection );
    Node node = getNodeService().getNodeById( productClaimSubmissionForm.getNodeId() );

    Long claimId = (Long)request.getSession().getAttribute( "claimId" );
    ProductClaim claim = null;
    if ( claimId != null )
    {
      AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
      claim = (ProductClaim)getClaimService().getClaimByIdWithAssociations( claimId, claimAssociationRequestCollection );
    }
    request.getSession().removeAttribute( "claimId" );

    claim = productClaimSubmissionForm.toDomainObject( claim, promotion, node );
    Participant submitter = getParticipantService().getParticipantById( UserManager.getUserId() );
    claim.setSubmitter( submitter );

    ClaimForm claimForm = promotion.getClaimForm();
    Long claimFormStepId = null;
    if ( claimForm != null )
    {
      for ( ClaimFormStep claimFormStep : claimForm.getClaimFormSteps() )
      {
        claimFormStepId = claimFormStep.getId();
      }
    }

    Claim savedClaim = null;
    try
    {
      savedClaim = getClaimService().saveClaim( claim, claimFormStepId, null, false, true );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      for ( Object obj : serviceErrors )
      {
        ServiceError error = (ServiceError)obj;
        String errorMessage = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
        if ( StringUtils.isNotEmpty( error.getArg1() ) )
        {
          errorMessage = errorMessage.replace( "{0}", error.getArg1() );
        }
        if ( StringUtils.isNotEmpty( error.getArg2() ) )
        {
          errorMessage = errorMessage.replace( "{1}", error.getArg2() );
        }
        if ( StringUtils.isNotEmpty( error.getArg3() ) )
        {
          errorMessage = errorMessage.replace( "{2}", error.getArg3() );
        }
        errorMessage = errorMessage.replace( "???", "" );
        errorMessage = errorMessage.replace( "???", "" );
        error.setArg1( errorMessage );
      }
      request.setAttribute( "submitClaimValidationErrors", serviceErrors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    // clear out the ProductClaimState
    ProductClaimStateManager.remove( request );

    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    // we got this far, so put the ClaimSubmittedBean in session so the confirmation can be
    // displayed
    ClaimSubmittedBean.addToSession( request, savedClaim.getId(), savedClaim.getPromotion().getId(), savedClaim.getClaimNumber(), savedClaim.getSubmissionDate() );

    refreshPointBalance( request );

    if ( UserManager.getUser().isDelegate() )
    {
      response.sendRedirect( request.getContextPath() + "/homePage.do" );
    }
    else
    {
      response.sendRedirect( request.getContextPath() + "/homePage.do" + RequestUtils.getHomePageFilterToken( request ) );
    }
    return null;
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}

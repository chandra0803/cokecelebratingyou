/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsClaimDetailsAction.java,v $
 */

package com.biperf.core.ui.approvals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * ApprovalsClaimDetailsAction.
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
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsClaimDetailsAction extends BaseDispatchAction
{
  public static final String ATTR_PROMOTION_CLAIMS_VALUE_LIST = "promotionClaimsValueList";

  protected ActionForward cancelled( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  /**
   * Prepares anything necessary before displaying the update screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ApprovalsClaimDetailsForm claimProductApprovalDetailsForm = (ApprovalsClaimDetailsForm)form;
    claimProductApprovalDetailsForm.getClaimProductApprovalFormByClaimProductIdString().clear();

    // Get the claim.
    Long claimId = null;
    claimId = Long.parseLong( request.getParameter( "claimId" ) );
    if ( claimId != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
      ProductClaim claim = (ProductClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );

      request.setAttribute( "claimDetails", claim );
      claimProductApprovalDetailsForm.load( claim );
    }
    else
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimId as part of clientState" ) );
      saveErrors( request, errors );
    }

    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * Prepares anything necessary before displaying the update screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward saveApprovals( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException, IOException
  {
    ActionMessages errors = new ActionMessages();
    ApprovalsClaimDetailsForm claimProductApprovalDetailsForm = (ApprovalsClaimDetailsForm)form;

    String approverComments = RequestUtils.getRequiredParamString( request, "approverComments" );
    String adminComments = RequestUtils.getRequiredParamString( request, "adminComments" );
    User approver = getUserService().getUserById( UserManager.getUserId() );
    // Get the claim.
    String claimId = null;
    claimId = request.getSession().getAttribute( "claimId" ).toString();
    request.getSession().removeAttribute( "claimId" );
    if ( claimId != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
      ProductClaim claim = (ProductClaim)getClaimService().getClaimByIdWithAssociations( new Long( claimId ), associationRequestCollection );
      claim.setApproverComments( approverComments );
      claim.setAdminComments( adminComments );
      claimProductApprovalDetailsForm.populateClaimProductDomainObjects( claim, approver );

      try
      {
        getClaimService().saveClaim( claim, null, approver, false, true );
      }
      catch( ServiceErrorException e )
      {
        List<ServiceError> serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
      }
    }
    else
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimId as part of clientState" ) );
      saveErrors( request, errors );
    }

    // return to list
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "saveOccurred", Boolean.TRUE );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ) } );
  }

  public ActionForward populateProductInfo( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ApprovalsClaimDetailsForm form = (ApprovalsClaimDetailsForm)actionForm;

    // Get the claim.
    Long claimId = null;
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
      claimId = (Long)clientStateMap.get( "claimId" );
      if ( claimId != null )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_PRODUCTS ) );
        ProductClaim claim = (ProductClaim)getClaimService().getClaimByIdWithAssociations( new Long( form.getClaimId() ), associationRequestCollection );

        AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
        promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );

        claim.setPromotion( getPromotionService().getPromotionByIdWithAssociations( claim.getPromotion().getId(), promotionAssociationRequestCollection ) );

        ClaimProductDetailsBean claimProductDetailsBean = new ClaimProductDetailsBean();

        for ( Iterator iter = claim.getClaimProducts().iterator(); iter.hasNext(); )
        {
          ClaimProduct cp = (ClaimProduct)iter.next();
          Product product = cp.getProduct();
          claimProductDetailsBean.addClaimIndex( product.getId(),
                                                 product.getName(),
                                                 product.getProductCategoryName(),
                                                 product.getProductSubCategoryName(),
                                                 cp.getQuantity(),
                                                 cp.getClaimProductCharacteristics(),
                                                 cp.getId(),
                                                 cp.getApprovalStatusType().getCode(),
                                                 cp.getClaim().getPromotion().getApprovalOptionTypes() );
        }
        request.setAttribute( "claimDetails", claim );
        writeAsJsonToResponse( claimProductDetailsBean, response );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return null;
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}

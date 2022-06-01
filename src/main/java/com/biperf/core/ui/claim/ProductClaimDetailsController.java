/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/ProductClaimDetailsController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.activity.impl.ManagerOverrideActivityAssociationRequest;
import com.biperf.core.service.activity.impl.SalesActivityAssociationRequest;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * ProductClaimDetailsController.
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
 * <td>Jul 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductClaimDetailsController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
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
    String claimId = null;
    String proxyUserId = null;
    String userId = null;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( clientState != null && !clientState.equals( "" ) )
      {
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
          claimId = (String)clientStateMap.get( "claimId" );
        }
        catch( ClassCastException cce )
        {
          Long claimid = (Long)clientStateMap.get( "claimId" );
          if ( claimid != null )
          {
            claimId = claimid.toString();
          }
        }
        try
        {
          proxyUserId = (String)clientStateMap.get( "proxyUserId" );
        }
        catch( ClassCastException cce )
        {
          Long proxid = (Long)clientStateMap.get( "proxyUserId" );
          if ( proxid != null )
          {
            proxyUserId = proxid.toString();
          }
        }
        try
        {
          userId = (String)clientStateMap.get( "userId" );
        }
        catch( ClassCastException cce )
        {
          Long usid = (Long)clientStateMap.get( "userId" );
          if ( usid != null )
          {
            userId = usid.toString();
          }
        }
        // START-g3Redux
        if ( clientStateMap.get( "returnURL" ) != null )
        {
          request.setAttribute( "returnURL", (String)clientStateMap.get( "returnURL" ) );
        }
        // End-g3Redux
      }
      else
      {
        claimId = String.valueOf( RequestUtils.getOptionalParamLong( request, "claimId" ) );
        proxyUserId = String.valueOf( RequestUtils.getOptionalParamLong( request, "proxyUserId" ) ); // may
        userId = String.valueOf( RequestUtils.getOptionalParamLong( request, "submitterId" ) );

      }

      // if claimDetails is being accessed from a link sent in an email, it would
      // have gone through the form to get it's parameters because they would have
      // been encrypted via ClientState functionality
      if ( claimId == null || claimId.equals( "0" ) )
      {
        ProductClaimDetailsForm productClaimDetailsForm = (ProductClaimDetailsForm)request.getAttribute( "productClaimDetailsForm" );
        claimId = productClaimDetailsForm.getId();
      }

      Claim claim = null;
      if ( request.getAttribute( "claimDetails" ) == null )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
        claim = getClaimService().getClaimByIdWithAssociations( new Long( claimId ), associationRequestCollection );
        request.setAttribute( "claimDetails", claim );
      }
      else
      {
        claim = (Claim)request.getAttribute( "claimDetails" );
      }

      List<ClaimProductsViewBean> claimProductsList = new ArrayList<ClaimProductsViewBean>();
      ProductClaim productClaimObj = (ProductClaim)claim;
      Set claimProductsSet = productClaimObj.getClaimProducts();

      for ( Iterator iter = claimProductsSet.iterator(); iter.hasNext(); )
      {
        ClaimProduct product = (ClaimProduct)iter.next();
        ClaimProductsViewBean claimProductsViewBean = new ClaimProductsViewBean();
        if ( product.getProduct().getProductCategory().getParentProductCategory() == null )
        {
          claimProductsViewBean.setProductCategoryName( product.getProduct().getProductCategory().getName() );
        }
        else
        {
          claimProductsViewBean.setProductCategoryName( product.getProduct().getProductCategory().getParentProductCategory().getName() );
        }

        if ( product.getProduct().getProductCategory().getParentProductCategory() == null )
        {
          claimProductsViewBean.setProductSubCategory( "" );
        }
        else
        {
          claimProductsViewBean.setProductSubCategory( product.getProduct().getProductCategory().getName() );
        }

        claimProductsViewBean.setProductName( product.getProduct().getName() );

        claimProductsViewBean.setClaimProductCharacteristics( product.getClaimProductCharacteristics() );

        claimProductsViewBean.setApprovalStatusTypeName( product.getApprovalStatusType().getName() );

        claimProductsViewBean.setQuantity( product.getQuantity() );

        if ( product.getApprovalStatusType() != null && product.getPromotionApprovalOptionReasonType() != null && product.getApprovalStatusType().isPendingDeniedHold()
            && !product.getPromotionApprovalOptionReasonType().getCode().isEmpty() )
        {
          claimProductsViewBean.setPromotionApprovalOptionReasonTypeName( product.getPromotionApprovalOptionReasonType().getName() );
        }

        claimProductsList.add( claimProductsViewBean );
      }

      Collections.sort( claimProductsList, new Comparator<ClaimProductsViewBean>()
      {
        @Override
        public int compare( ClaimProductsViewBean o1, ClaimProductsViewBean o2 )
        {
          return o1.getProductCategoryName().compareTo( o2.getProductCategoryName() );
        }
      } );

      request.setAttribute( "claimProductsList", claimProductsList );

      ClaimFormUtils.populateClaimElementPickLists( claim );

      // Get claim submitter's user id
      if ( userId == null )
      {
        userId = claim.getSubmitter().getId().toString(); // submitter user id
      }

      // If the currently logged in user is the team member, get journals for the team member
      // instead of the submitter
      ProductClaimPromotion pcPromo = (ProductClaimPromotion)claim.getPromotion();
      if ( pcPromo.isTeamUsed() )
      {
        List claimPaxList = ProductClaimUtils.getClaimParticipantList( (ProductClaim)claim );
        for ( Iterator claimPaxIter = claimPaxList.iterator(); claimPaxIter.hasNext(); )
        {
          ProductClaimParticipant claimParticipant = (ProductClaimParticipant)claimPaxIter.next();
          // If currently logged in user is a team member then use team member's user id
          if ( claimParticipant.getParticipant() != null && claimParticipant.getParticipant().getId().equals( UserManager.getUserId() ) )
          {
            userId = claimParticipant.getParticipant().getId().toString();
            break;
          }
        }
      }

      if ( !claim.isOpen() )
      {
        AssociationRequestCollection journalAssociationRequest = new AssociationRequestCollection();
        journalAssociationRequest.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION_DEPROXY ) );
        journalAssociationRequest.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );

        // Get the journals
        List journals = getJournalService().getJournalsByClaimIdAndUserId( claim.getId(), new Long( userId ), journalAssociationRequest );

        for ( Iterator journalIter = journals.iterator(); journalIter.hasNext(); )
        {
          Journal journal = (Journal)journalIter.next();

          Set activityJournals = journal.getActivityJournals();

          for ( Iterator activityJournalsIter = activityJournals.iterator(); activityJournalsIter.hasNext(); )
          {
            ActivityJournal activityJournal = (ActivityJournal)activityJournalsIter.next();

            SalesActivity salesActivity = null;
            ManagerOverrideActivity managerOverrideActivity = null;

            if ( activityJournal.getActivity() instanceof SalesActivity )
            {

              salesActivity = (SalesActivity)activityJournal.getActivity();

              AssociationRequestCollection activityAssociationRequestCollection = new AssociationRequestCollection();
              activityAssociationRequestCollection.add( new SalesActivityAssociationRequest( SalesActivityAssociationRequest.CLAIM ) );

              SalesActivity hydatedSalesActivity = getActivityService().getSalesActivityByIdWithAssociations( salesActivity.getId(), activityAssociationRequestCollection );

              salesActivity.setClaim( hydatedSalesActivity.getClaim() );

            }
            else if ( activityJournal.getActivity() instanceof ManagerOverrideActivity )
            {

              managerOverrideActivity = (ManagerOverrideActivity)activityJournal.getActivity();

              AssociationRequestCollection activityAssociationRequestCollection = new AssociationRequestCollection();
              activityAssociationRequestCollection.add( new ManagerOverrideActivityAssociationRequest( ManagerOverrideActivityAssociationRequest.CLAIM ) );

              ManagerOverrideActivity hydatedManagerOverrideActivity = getActivityService().getManagerOverrideActivityByIdWithAssociations( managerOverrideActivity.getId(),
                                                                                                                                            activityAssociationRequestCollection );

              managerOverrideActivity.setClaim( hydatedManagerOverrideActivity.getClaim() );

            }
          }
        }

        request.setAttribute( "journals", journals );

        ProductClaim productClaim = (ProductClaim)claim;

        List claimProducts = new ArrayList( productClaim.getClaimProducts() );

        PropertyComparator.sort( claimProducts, new MutableSortDefinition( "claimProduct.dateApproved", true, true ) );

        ClaimProduct mostRecentClaimProduct = (ClaimProduct)claimProducts.get( 0 );

        Boolean systemApproved = new Boolean( false );

        if ( mostRecentClaimProduct.getCurrentClaimItemApprover() != null )
        {
          if ( productClaim.getPromotion().getApprovalType().getCode().equals( ApprovalType.AUTOMATIC_DELAYED )
              || productClaim.getPromotion().getApprovalType().getCode().equals( ApprovalType.AUTOMATIC_IMMEDIATE ) || mostRecentClaimProduct.getCurrentClaimItemApprover().getApproverUser() == null )
          {
            systemApproved = new Boolean( true );
          }
        }

        request.setAttribute( "systemApproved", systemApproved );
        request.setAttribute( "mostRecentClaimProduct", mostRecentClaimProduct );
      }

      request.setAttribute( "claimParticipantList", ProductClaimUtils.getClaimParticipantList( (ProductClaim)claim ) );
      request.setAttribute( "showDetail", "true" );
      User user = getUserService().getUserById( UserManager.getUserId() );
      request.setAttribute( "proxyUserId", proxyUserId );
      request.setAttribute( "userNodeSize", new Integer( user.getUserNodes().size() ) );
      request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  /**
   * Get the claimService from the beanFactory
   * 
   * @return ClaimService
   */
  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  /**
   * Get the claimService from the beanFactory
   * 
   * @return ClaimService
   */
  private ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  /**
   * Get the claimService from the beanFactory
   * 
   * @return ClaimService
   */
  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  /**
   * Returns a reference to the User service.
   * 
   * @return a reference to the User service.
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}

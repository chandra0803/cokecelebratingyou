/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsNominationDetailsAction.java,v $
 */

package com.biperf.core.ui.approvals;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.ApprovableTypeEnum;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.claim.ClaimFormUtils;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.domain.claim.ApprovableItem;

/**
 * RecognitionApprovalDetailsAction.
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
 * <td></td>
 * <td></td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsNominationDetailsAction extends BaseDispatchAction
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

    ApprovalsNominationDetailsForm nominationApprovalDetailsForm = (ApprovalsNominationDetailsForm)form;

    Approvable approvable = getHyrdatedApprovable( request, nominationApprovalDetailsForm );

    request.setAttribute( "approvable", approvable );

    nominationApprovalDetailsForm.load( approvable );

    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  @SuppressWarnings( "unchecked" )
  public static Approvable getHyrdatedApprovable( HttpServletRequest request, ApprovalsNominationDetailsForm nominationApprovalDetailsForm )
  {
    Long approvableId = null;

    // client state security
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
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        String approvableIdString = (String)clientStateMap.get( "approvableId" );
        try
        {
          approvableId = new Long( approvableIdString );
        }
        catch( NumberFormatException nfe )
        {
          // do nothing. approvableId is an optional parm
        }
      }
      catch( ClassCastException cce )
      {
        approvableId = (Long)clientStateMap.get( "approvableId" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Processing starts here
    Approvable approvable;
    if ( approvableId == null )
    {
      approvable = buildTransientClaimGroup( request, nominationApprovalDetailsForm );
    }
    else
    {
      // Existing approvable
      String approvableTypeCode;
      approvableTypeCode = RequestUtils.getOptionalParamString( request, "approvableTypeCode" );

      if ( approvableTypeCode == null || approvableTypeCode.length() <= 0 )
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
          Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
          approvableTypeCode = (String)clientStateMap.get( "approvableTypeCode" );
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }
      }
      if ( ApprovableTypeEnum.getEnum( approvableTypeCode ).isClaim() )
      {
        AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_ADDRESS ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_EMPLOYERS ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ELEMENTS ) );
        // Client customization for WIP #39189 starts
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_DOCUMENTS ) );
        // Client customization for WIP #39189 ends
        approvable = getClaimService().getClaimByIdWithAssociations( approvableId, claimAssociationRequestCollection );
        ClaimFormUtils.populateClaimElementPickLists( (Claim)approvable );
      }
      else
      {
        AssociationRequestCollection claimGroupAssociationRequestCollection = new AssociationRequestCollection();
        claimGroupAssociationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.ALL_WITH_CLAIMS_ALL ) );
        claimGroupAssociationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.NOMINEE ) );
        approvable = getClaimGroupService().getClaimGroupByIdWithAssociations( approvableId, claimGroupAssociationRequestCollection );
        for ( Object claimObject : ( (ClaimGroup)approvable ).getClaims() )
        {
          Claim claim = (Claim)claimObject;
          ClaimFormUtils.populateClaimElementPickLists( claim );
        }
      }

    }
    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );

    // Reset promotion with hyrdated version.
    approvable.setPromotion( getPromotionService().getPromotionByIdWithAssociations( new Long( nominationApprovalDetailsForm.getPromotionId() ), promotionAssociationRequestCollection ) );

    if ( approvable instanceof NominationClaim )
    {
      NominationClaim nc = (NominationClaim)approvable;
      String userLanguage = UserManager.getUserLanguage();
      if ( userLanguage != null && nc.getSubmitterCommentsLanguageType() != null )
      {
        if ( !userLanguage.equals( nc.getSubmitterCommentsLanguageType().toString() ) )
        {

          if ( getSystemVariableService().getPropertyByName( SystemVariableService.MACHINE_LANGUAGE_ALLOW_TRANSLATION ).getBooleanVal() )
          {
            request.setAttribute( "allowTranslate", true );
          }
          else
          {
            request.setAttribute( "allowTranslate", false );
          }

          request.setAttribute( "translateClientState",
                                "clientState=" + urlEncode( RequestUtils.getRequiredParamString( request, "clientState" ) ) + "&cryptoPass="
                                    + RequestUtils.getOptionalParamString( request, "cryptoPass" ) );
        }
      }
    }
    return approvable;
  }

  private static String urlEncode( String value )
  {
    try
    {
      return URLEncoder.encode( value, "UTF-8" );
    }
    catch( Throwable t )
    {
      return value;
    }
  }

  @SuppressWarnings( "unchecked" )
  private static Approvable buildTransientClaimGroup( HttpServletRequest request, ApprovalsNominationDetailsForm nominationApprovalDetailsForm )
  {
    Approvable approvable = null;
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
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      Long claimGroupPaxId = null;
      try
      {
        claimGroupPaxId = (Long)clientStateMap.get( "claimGroupPaxId" );
      }
      catch( ClassCastException e )
      {
        claimGroupPaxId = new Long( (String)clientStateMap.get( "claimGroupPaxId" ) );
      }

      Long claimGroupNodeId = null;
      try
      {
        claimGroupNodeId = (Long)clientStateMap.get( "claimGroupNodeId" );
      }
      catch( ClassCastException e2 )
      {
        claimGroupNodeId = new Long( (String)clientStateMap.get( "claimGroupNodeId" ) );
      }

      // Must be transient claim group, which has no id yet, build it back up
      boolean claimGroupMatchFound = false;

      if ( claimGroupPaxId != null && claimGroupNodeId != null )
      {
        String startDateString = (String)clientStateMap.get( "claimGroupStartDate" );
        Date claimGroupStartDate = null;
        if ( !StringUtils.isBlank( startDateString ) )
        {
          DateUtils.toStartDate( DateUtils.toDate( startDateString ) );
        }

        String endDateString = (String)clientStateMap.get( "claimGroupEndDate" );
        Date claimGroupEndDate = null;
        if ( !StringUtils.isBlank( endDateString ) )
        {
          DateUtils.toStartDate( DateUtils.toDate( endDateString ) );
        }

        String approvalStatus = nominationApprovalDetailsForm.getViewApprovalStatusCode() != null ? nominationApprovalDetailsForm.getViewApprovalStatusCode() : ApprovalStatusType.PENDING;
        // Retrieve entire approvables list and extract transient claim group with the select pax
        // and node
        AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_SUBMITTER_ADDRESS ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_SUBMITTER_EMPLOYER ) );

        List<Approvable> allApprovables = ApprovalsNominationListAction
            .buildNominationsApprovablesList( claimAssociationRequestCollection,
                                              claimGroupStartDate,
                                              claimGroupEndDate,
                                              new Long( nominationApprovalDetailsForm.getPromotionId() ),
                                              approvalStatus,
                                              false );

        if ( allApprovables == null || allApprovables.size() <= 0 )
        {

          allApprovables = ApprovalsNominationListAction
              .buildNominationsApprovablesList( claimAssociationRequestCollection,
                                                claimGroupStartDate,
                                                claimGroupEndDate,
                                                new Long( nominationApprovalDetailsForm.getPromotionId() ),
                                                ApprovalStatusType.WINNER,
                                                false );

        }
        if ( allApprovables == null || allApprovables.size() <= 0 )
        {

          allApprovables = ApprovalsNominationListAction
              .buildNominationsApprovablesList( claimAssociationRequestCollection,
                                                claimGroupStartDate,
                                                claimGroupEndDate,
                                                new Long( nominationApprovalDetailsForm.getPromotionId() ),
                                                ApprovalStatusType.NON_WINNER,
                                                false );

        }
        if ( allApprovables == null || allApprovables.size() <= 0 )
        {

          allApprovables = ApprovalsNominationListAction
              .buildNominationsApprovablesList( claimAssociationRequestCollection,
                                                claimGroupStartDate,
                                                claimGroupEndDate,
                                                new Long( nominationApprovalDetailsForm.getPromotionId() ),
                                                ApprovalStatusType.EXPIRED,
                                                false );

        }

        for ( Approvable approvableCandidate : allApprovables )
        {
          if ( approvableCandidate instanceof ClaimGroup )
          {
            ClaimGroup approvableCandidateAsClaimGroup = (ClaimGroup)approvableCandidate;
            if ( approvableCandidateAsClaimGroup.getParticipant().getId().equals( claimGroupPaxId ) && approvableCandidateAsClaimGroup.getNode().getId().equals( claimGroupNodeId ) )
            {
              approvable = approvableCandidateAsClaimGroup;
              claimGroupMatchFound = true;
              break;
            }
          }
        }

        if ( !claimGroupMatchFound )
        {
          throw new BeaconRuntimeException( "No matching transient claim group form for the given " + "nodeId(" + claimGroupNodeId + "), paxId(" + claimGroupPaxId + "), start date("
              + claimGroupStartDate + ") and end date(" + claimGroupEndDate + ")" );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    return approvable;
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
  @SuppressWarnings( "unchecked" )
  public ActionForward saveApprovals( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ApprovalsNominationDetailsForm nominationApprovalDetailsForm = (ApprovalsNominationDetailsForm)form;

    // Only save if switched from pending
    if ( !nominationApprovalDetailsForm.getApprovalStatusType().equals( ApprovalStatusType.PENDING ) )
    {
      Approvable approvable = getHyrdatedApprovable( request, nominationApprovalDetailsForm );

      User approver = getUserService().getUserById( UserManager.getUserId() );

      // Client customization for WIP #56492 starts
      ApprovableItem approvableItem = ( (ApprovableItem)approvable.getApprovableItems().iterator().next() );
      approvableItem.setLevelSelect( nominationApprovalDetailsForm.getLevel() );
      // Client customization for WIP #56492 ends
      // Client customization for WIP #58122
      NominationPromotion np=(NominationPromotion)approvable.getPromotion();
      if(np.isLevelPayoutByApproverAvailable()){
	  if(nominationApprovalDetailsForm.getAwardQuantity()!=null)
      approvableItem.setAwardQuantity( new Long(nominationApprovalDetailsForm.getAwardQuantity() ));
      }
      nominationApprovalDetailsForm.toDomain( approvable, approver );

      Promotion promotion = approvable.getPromotion();

      List<Approvable> approvables = new ArrayList<Approvable>();
      approvables.add( approvable );
      try
      {
        getClaimService().saveNominationApprovables( approvables, null, approver, false, promotion, null );
      }
      catch( ServiceErrorException e )
      {
        ActionMessages errors = new ActionMessages();
        List<ServiceError> serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }
    // return to list
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "saveOccurred", Boolean.TRUE );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ) } );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private static ClaimService getClaimService()
  {
    return (ClaimService)BeanLocator.getBean( ClaimService.BEAN_NAME );
  }

  private static ClaimGroupService getClaimGroupService()
  {
    return (ClaimGroupService)BeanLocator.getBean( ClaimGroupService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }
}

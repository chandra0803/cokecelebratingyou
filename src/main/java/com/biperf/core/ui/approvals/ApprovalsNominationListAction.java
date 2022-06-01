/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsNominationListAction.java,v $
 */

package com.biperf.core.ui.approvals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
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
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.claim.ClaimFormUtils;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.ListPageInfo;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionApprovableValue;

/**
 * RecognitionApprovalListAction.
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
public class ApprovalsNominationListAction extends BaseDispatchAction
{
  public static final String ATTR_APPROVABLE_LIST = "approvableList";
  private static final Long APPROVAL_LIST_PAGE_SIZE = 20L;

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
   * @throws InvalidClientStateException 
   */
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException
  {
    ApprovalsNominationListForm nominationApprovalListForm = (ApprovalsNominationListForm)form;
    nominationApprovalListForm.getNominationApprovalFormByApprovableUid().clear();
    // Bug Fix 64764
    SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" );
    if ( nominationApprovalListForm.getStartDate() == null )
    {
      Date startDate = DateUtils.getOneMonthBeforeAsString( DateUtils.getCurrentDate() );
      nominationApprovalListForm.setStartDate( sdf.format( startDate ) );
    }
    if ( nominationApprovalListForm.getEndDate() == null )
    {
      Date endDate = DateUtils.getCurrentDate();
      nominationApprovalListForm.setEndDate( sdf.format( endDate ) );
    }
    // end bugfix
    Boolean isAllApprovablesApproved = false;
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      @SuppressWarnings( "unchecked" )
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      isAllApprovablesApproved = (Boolean)clientStateMap.get( "isAllApprovablesApproved" );
    }

    List<Approvable> approvableList = buildApprovablesList( nominationApprovalListForm, true, isAllApprovablesApproved );
    request.setAttribute( ApprovalsNominationListAction.ATTR_APPROVABLE_LIST, approvableList );

    Long promotionId = null;
    if ( !approvableList.isEmpty() )
    {
      Approvable approvable = approvableList.get( 0 );
      if ( approvable != null && approvable.getPromotion() != null )
      {
        promotionId = approvable.getPromotion().getId();
      }
    }

    if ( promotionId != null )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)getPromotionService().getPromotionById( promotionId );
      nominationApprovalListForm.load( approvableList, nominationPromotion );
    }

    checkForSaveOccurred( request );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward preparePrinterFriendlyList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ApprovalsNominationListForm recognitionApprovalListForm = (ApprovalsNominationListForm)form;
    recognitionApprovalListForm.getNominationApprovalFormByApprovableUid().clear();
    List<Approvable> approvableList = buildApprovablesList( recognitionApprovalListForm, request );

    // populate picklists for claim element display
    for ( Approvable approvable : approvableList )
    {
      if ( approvable instanceof ClaimGroup )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvable;
        for ( Object object : claimGroup.getClaims() )
        {
          Claim claim = (Claim)object;
          ClaimFormUtils.populateClaimElementPickLists( claim );
        }
      }
      else
      {
        Claim claim = (Claim)approvable;
        ClaimFormUtils.populateClaimElementPickLists( claim );
      }

    }

    request.setAttribute( ApprovalsNominationListAction.ATTR_APPROVABLE_LIST, approvableList );

    recognitionApprovalListForm.load( approvableList, null );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
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
    ActionMessages errors = new ActionMessages();
    ApprovalsNominationListForm nominationApprovalListForm = (ApprovalsNominationListForm)form;

    for ( String key : nominationApprovalListForm.getNominationApprovalFormByApprovableUid().keySet() )
    {
      ApprovalsNominationForm approvalsForm = nominationApprovalListForm.getNominationApprovalFormByApprovableUid().get( key );
      String statusType = approvalsForm.getApprovalStatusType();
      if ( statusType.equalsIgnoreCase( ApprovalStatusType.NON_WINNER ) )
      {
        approvalsForm.setAwardQuantity( "" );
      }
    }

    if ( ApprovalStatusType.WINNER.equals( nominationApprovalListForm.getFilterApprovalStatusCode() ) )
    {
      return mapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    nominationApprovalListForm.setRequestedPage( nominationApprovalListForm.getListPageInfo().getCurrentPage() );
    List<Approvable> approvables = buildApprovablesList( nominationApprovalListForm, true, false );

    User approver = getUserService().getUserById( UserManager.getUserId() );

    NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionById( new Long( nominationApprovalListForm.getPromotionId() ) );
    nominationApprovalListForm.toDomainObjects( promotion, approvables, approver );

    // Remove pending approvables, since no action is required.
    List<Approvable> nonPendingApprovables = filterPendingApprovables( approvables );

    if ( !nonPendingApprovables.isEmpty() )
    {
      try
      {
        getClaimService().saveNominationApprovables( nonPendingApprovables, null, approver, false, promotion, null );
      }
      catch( ServiceErrorException e )
      {
        List<ServiceError> serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }

    // reload form
    request.setAttribute( "saveOccurred", Boolean.TRUE );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Remove pending approvables.
   */
  private List<Approvable> filterPendingApprovables( List<Approvable> approvables )
  {
    List<Approvable> nonPendingApprovables = new ArrayList<Approvable>();

    for ( Approvable approvable : approvables )
    {
      ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();
      if ( !approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.PENDING ) )
      {
        nonPendingApprovables.add( approvable );
      }
    }

    return nonPendingApprovables;
  }

  @SuppressWarnings( "unchecked" )
  public static List<Approvable> buildApprovablesList( ApprovalsNominationListForm nominationApprovalListForm, HttpServletRequest request )
  {
    Date startDate = DateUtils.toDate( nominationApprovalListForm.getStartDate() );
    boolean includeNodeAssociation = false;

    if ( nominationApprovalListForm.getMethod() != null && nominationApprovalListForm.getMethod().equalsIgnoreCase( "extractAsCsv" ) )
    {
      includeNodeAssociation = true;
    }
    if ( startDate == null )
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
        startDate = DateUtils.toDate( (String)clientStateMap.get( "startDate" ) );
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    Date endDate = DateUtils.toDate( nominationApprovalListForm.getEndDate() );
    if ( endDate == null )
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
        endDate = DateUtils.toDate( (String)clientStateMap.get( "endDate" ) );
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    Long promotionId = null;
    if ( StringUtils.isBlank( nominationApprovalListForm.getPromotionId() ) )
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
        String promotionIdString = (String)clientStateMap.get( "promotionId" );
        promotionId = new Long( promotionIdString );
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }
    else
    {
      promotionId = new Long( nominationApprovalListForm.getPromotionId() );
    }
    String filterApprovalStatusCode = nominationApprovalListForm.getFilterApprovalStatusCode();
    if ( filterApprovalStatusCode == null )
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
        filterApprovalStatusCode = (String)clientStateMap.get( "filterApprovalStateCode" );
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    return buildNominationsApprovablesList( claimAssociationRequestCollection, startDate, endDate, promotionId, filterApprovalStatusCode, includeNodeAssociation );
  }

  public static List<Approvable> buildApprovablesList( ApprovalsNominationListForm nominationApprovalListForm, boolean isPaginated, Boolean isAllApprovablesApproved )
  {
    Date startDate = DateUtils.toDate( nominationApprovalListForm.getStartDate() );
    Date endDate = DateUtils.toDate( nominationApprovalListForm.getEndDate() );
    Long promotionId = new Long( nominationApprovalListForm.getPromotionId() );
    String filterApprovalStatusCode = nominationApprovalListForm.getFilterApprovalStatusCode();
    boolean includeNodeAssociation = false;

    if ( nominationApprovalListForm.getMethod() != null && nominationApprovalListForm.getMethod().equalsIgnoreCase( "extractAsCsv" ) )
    {
      includeNodeAssociation = true;
    }
    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    List<Approvable> approvableList = new ArrayList<Approvable>();
    if ( isAllApprovablesApproved != null && !isAllApprovablesApproved )
    {
      approvableList = buildNominationsApprovablesList( claimAssociationRequestCollection, startDate, endDate, promotionId, filterApprovalStatusCode, includeNodeAssociation );
    }

    if ( isPaginated )
    {
      ListPageInfo<Approvable> listPageInfo = new ListPageInfo<Approvable>( approvableList,
                                                                            APPROVAL_LIST_PAGE_SIZE,
                                                                            nominationApprovalListForm.getRequestedPage() == null
                                                                                ? ListPageInfo.DEFAULT_INITIAL_PAGE
                                                                                : nominationApprovalListForm.getRequestedPage() );
      nominationApprovalListForm.setListPageInfo( listPageInfo );
      nominationApprovalListForm.setRequestedPage( 1L );
      approvableList = listPageInfo.getCurrentPageList();
    }

    return approvableList;
  }

  /* new */
  public static List<Approvable> buildNominationsApprovablesList( AssociationRequestCollection claimAssociationRequestCollection,
                                                                  Date startDate,
                                                                  Date endDate,
                                                                  Long promotionId,
                                                                  String filterApprovalStatusCode,
                                                                  boolean includeNodeAssociation )
  {
    if ( claimAssociationRequestCollection.isEmpty() )
    {
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    }
    if ( includeNodeAssociation )
    {
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_USER_NODES ) );
    }

    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );

    NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionById( promotionId );

    List<Approvable> approvableList = new ArrayList<Approvable>();

    Boolean open;
    Boolean expired;
    if ( ApprovalStatusType.PENDING.equals( filterApprovalStatusCode ) )
    {
      open = Boolean.TRUE;
      expired = Boolean.FALSE;
    }
    else if ( ApprovalStatusType.EXPIRED.equals( filterApprovalStatusCode ) )
    {
      open = null;
      expired = Boolean.TRUE;
    }
    else
    {
      open = Boolean.FALSE;
      expired = Boolean.FALSE;
    }

    if ( promotion.isCumulative() )
    {
      AssociationRequestCollection claimGroupAssociationRequestCollection = new AssociationRequestCollection();
      claimGroupAssociationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.ALL_WITH_CLAIMS_ALL ) );

      // First collect add any existing claim groups (will only happen for during 2nd and later
      // rounds of approval. For first level approval, the group doesn't yet exist.
      List<PromotionApprovableValue> promotionClaimGroupsValueList = getClaimGroupService().getNominationClaimGroupsForApprovalByUser( UserManager.getUserId(),
                                                                                                                                       null,
                                                                                                                                       promotionId != null ? new Long[] { promotionId } : null,
                                                                                                                                       open,
                                                                                                                                       PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                                                                       claimGroupAssociationRequestCollection,
                                                                                                                                       promotionAssociationRequestCollection,
                                                                                                                                       expired,
                                                                                                                                       filterApprovalStatusCode,
                                                                                                                                       null );

      approvableList.addAll( extractApprovables( promotionClaimGroupsValueList ) );

      if ( open != null && open || expired )
      {
        approvableList = getPendingApprovableList( approvableList,
                                                   filterApprovalStatusCode,
                                                   promotion,
                                                   open,
                                                   startDate,
                                                   endDate,
                                                   PromotionType.lookup( PromotionType.NOMINATION ),
                                                   claimAssociationRequestCollection,
                                                   promotionAssociationRequestCollection,
                                                   expired,
                                                   Boolean.FALSE );
      }
    }
    else
    {
      approvableList = getPendingApprovableList( approvableList,
                                                 filterApprovalStatusCode,
                                                 promotion,
                                                 open,
                                                 startDate,
                                                 endDate,
                                                 PromotionType.lookup( PromotionType.NOMINATION ),
                                                 claimAssociationRequestCollection,
                                                 promotionAssociationRequestCollection,
                                                 expired,
                                                 Boolean.FALSE );
    }
    return approvableList;
  }

  private static List<Approvable> getPendingApprovableList( List<Approvable> approvableList,
                                                            String filterApprovalStatusCode,
                                                            NominationPromotion promotion,
                                                            Boolean open,
                                                            Date startDate,
                                                            Date endDate,
                                                            PromotionType promotionType,
                                                            AssociationRequestCollection claimAssociationRequestCollection,
                                                            AssociationRequestCollection promotionAssociationRequestCollection,
                                                            Boolean expired,
                                                            Boolean allApprovers )
  {
    List<PromotionApprovableValue> promotionClaimsValueList = new ArrayList<PromotionApprovableValue>();
    promotionClaimsValueList = getClaimService().getNominationClaimsForApprovalByUser( UserManager.getUserId(),
                                                                                       null,
                                                                                       promotion.getId() != null ? new Long[] { promotion.getId() } : null,
                                                                                       open,
                                                                                       startDate,
                                                                                       endDate,
                                                                                       PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                       claimAssociationRequestCollection,
                                                                                       promotionAssociationRequestCollection,
                                                                                       expired,
                                                                                       filterApprovalStatusCode,
                                                                                       null );

    if ( !promotionClaimsValueList.isEmpty() )
    {
      if ( promotion.isCumulative() )
      {
        approvableList.addAll( extractTransientClaimGroups( promotionClaimsValueList, filterApprovalStatusCode ) );
      }
      else
      {
        // just extract all claims
        approvableList.addAll( extractApprovables( promotionClaimsValueList ) );
      }
    }
    return approvableList;
  }

  /*
   * private static Comparator<ApprovableItemApprover> approvableItemApproverSort = new
   * Comparator<ApprovableItemApprover>() { public int compare( ApprovableItemApprover
   * approvableItemApprover1, ApprovableItemApprover approvableItemApprover2 ) { Long item1 =
   * approvableItemApprover1.getApprovalRound(); Long item2 =
   * approvableItemApprover2.getApprovalRound(); return ( item1.longValue() > item2.longValue() ? -1
   * : ( item1 == item2 ? 0 : 1 ) ); } };
   */

  /**
   * Group claims into ClaimGroups grouped by node and nominee
   * 
   * @param promotionClaimsValueList
   */
  private static List<ClaimGroup> extractTransientClaimGroups( List<PromotionApprovableValue> promotionClaimsValueList, String code )
  {
    Map<String, ClaimGroup> claimGroupByNomineeAndNodeId = new LinkedHashMap<String, ClaimGroup>();

    List<Approvable> claims = extractApprovables( promotionClaimsValueList );
    for ( Approvable approvable : claims )
    {
      NominationClaim claim = (NominationClaim)approvable;
      ClaimRecipient claimRecipient = (ClaimRecipient)claim.getClaimRecipients().iterator().next();
      if ( claimRecipient != null )
      {
        if ( claimRecipient.getNode() != null && claimRecipient.getRecipient() != null )
        {
          Long nomineeNodeId = claimRecipient.getNode().getId();
          Long nomineeId = claimRecipient.getRecipient().getId();
          if ( claimRecipient.getRecipient().getStatus() != null && claimRecipient.getRecipient().getStatus().getCode().equalsIgnoreCase( "active" ) )
          {
            String key = nomineeId + "-" + nomineeNodeId;

            ClaimGroup claimGroup = (ClaimGroup)claimGroupByNomineeAndNodeId.get( key );
            if ( claimGroup == null )
            {
              claimGroup = new ClaimGroup( GuidUtils.generateGuid() );
              claimGroup.setOpen( true );
              claimGroup.setNode( claimRecipient.getNode() );
              claimGroup.setParticipant( claimRecipient.getRecipient() );
              claimGroup.setPromotion( claim.getPromotion() );
              claimGroup.setApprovalStatusType( ApprovalStatusType.lookup( code ) );
              claimGroupByNomineeAndNodeId.put( key, claimGroup );
            }
            claimGroup.addClaim( claim );
          }
        }
      }
    }
    return new ArrayList<ClaimGroup>( claimGroupByNomineeAndNodeId.values() );
  }

  @SuppressWarnings( "unchecked" )
  private static List<Approvable> extractApprovables( List<PromotionApprovableValue> promotionApprovableValueList )
  {
    ArrayList<Approvable> approvables = new ArrayList<Approvable>();

    for ( PromotionApprovableValue promotionApprovableValue : promotionApprovableValueList )
    {
      List<Approvable> approvableList = promotionApprovableValue.getApprovables();
      if ( approvableList != null && !approvableList.isEmpty() )
      {
        for ( Approvable approv : approvableList )
        {
          if ( approv instanceof ClaimGroup )
          {
            ClaimGroup claimGroup = (ClaimGroup)approv;
            if ( claimGroup.getClaims().iterator().hasNext() )
            {
              NominationClaim claim = (NominationClaim)claimGroup.getClaims().iterator().next();
              if ( claim.isActiveNomClaim() )
              {
                approvables.add( approv );
              }
            }
          }
          if ( approv instanceof NominationClaim )
          {
            if ( ( (NominationClaim)approv ).isActiveNomClaim() )
            {
              approvables.add( approv );
            }
          }
        }
      }
    }
    return approvables;
  }

  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private static ClaimGroupService getClaimGroupService()
  {
    return (ClaimGroupService)getService( ClaimGroupService.BEAN_NAME );
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private static NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  @SuppressWarnings( "unchecked" )
  private void checkForSaveOccurred( HttpServletRequest request ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      request.setAttribute( "saveOccurred", clientStateMap.get( "saveOccurred" ) );
    }
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward extractAsCsv( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );

    Long promotionId = Long.valueOf( ( (ApprovalsNominationListForm)form ).getPromotionId() );

    ApprovalsNominationListExportBean exportBean = new ApprovalsNominationListExportBean();
    exportBean.setExportList( buildApprovablesList( (ApprovalsNominationListForm)form, false, false ) );
    exportBean.setNodeService( getNodeService() );
    exportBean.extractAsCsv( getPromotionService().getPromotionByIdWithAssociations( promotionId, arc ), response );
    return null;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward extractAsPdf( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );

    Long promotionId = Long.valueOf( ( (ApprovalsNominationListForm)form ).getPromotionId() );

    ApprovalsNominationListExportBean exportBean = new ApprovalsNominationListExportBean();
    exportBean.setExportPdfList( buildApprovablesList( (ApprovalsNominationListForm)form, false, false ) );
    exportBean.setSubmissionStartDate( ( (ApprovalsNominationListForm)form ).getStartDate() );
    exportBean.setSubmissionEndDate( ( (ApprovalsNominationListForm)form ).getEndDate() );
    exportBean.setNodeService( getNodeService() );
    exportBean.extractAsPdf( getPromotionService().getPromotionByIdWithAssociations( promotionId, arc ), response, "nomination" );
    return null;
  }

}

/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.approvals;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverLevelType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.approvals.CumulativeInfoTableDataViewBean.CumulativeInfoView;
import com.biperf.core.ui.approvals.CumulativeInfoTableDataViewBean.CumulativeInfoView.CustomFieldView;
import com.biperf.core.ui.approvals.NominationsApprovalPageDataView.NominationPromotionApproval;
import com.biperf.core.ui.approvals.NominationsApprovalPageDataView.NominationPromotionApproval.ApprovalLevel;
import com.biperf.core.ui.approvals.NominationsApprovalPageDataView.NominationPromotionApproval.TimePeriod;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.MetaView;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.MetaView.Column;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.NextApprover;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.PreviousApprover;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.Result;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.Result.Attachment;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.Result.Behavior;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.Result.CustomField;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.Result.TeamMemeber;
import com.biperf.core.ui.approvals.NominationsApprovalPageTableDataView.NominationsApprovalPageTableData.Result.TimePeriodViewBean;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.nomination.EligibleNominationPromotionListViewObject;
import com.biperf.core.ui.nomination.EligibleNominationPromotionViewObject;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CumulativeInfoTableDataValueBean;
import com.biperf.core.value.NominationsApprovalAwardDetailsValueBean;
import com.biperf.core.value.NominationsApprovalBehaviorsValueBean;
import com.biperf.core.value.NominationsApprovalBoxValueBean;
import com.biperf.core.value.NominationsApprovalCustomValueBean;
import com.biperf.core.value.NominationsApprovalDetailsValueBean;
import com.biperf.core.value.NominationsApprovalPageDataValueBean;
import com.biperf.core.value.NominationsApprovalPageDetailsValueBean;
import com.biperf.core.value.NominationsApprovalPageNextLevelApproversValueBean;
import com.biperf.core.value.NominationsApprovalPagePreviousLevelApproversValueBean;
import com.biperf.core.value.NominationsApprovalPagePromotionLevelsValueBean;
import com.biperf.core.value.NominationsApprovalPageTableValueBean;
import com.biperf.core.value.NominationsApprovalPageTimePeriodsValueBean;
import com.biperf.core.value.NominationsApprovalTeamMembersValueBean;
import com.biperf.core.value.NominationsApprovalTimePeriodsValueBean;
import com.biperf.core.value.PromotionApprovableValue;
import com.biperf.core.value.client.ApproverLevelTypeBean;
import com.biperf.core.value.client.TcccClaimFileValueBean;
import com.biperf.core.value.nomination.CumulativeApprovalNominatorInfoValueBean;
import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * @author poddutur
 * @since Apr 18, 2016
 */
public class NominationsApprovalPageAction extends BaseDispatchAction
{
  private static final String US_DOLLAR = "USD";
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    
	NominationsApprovalPageForm approvalForm = (NominationsApprovalPageForm)form;
    Map<String, Object> parameters = new HashMap<String, Object>();
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      parameters = ClientStateSerializer.deserialize( clientState, password );
    }

    StringBuilder r = new StringBuilder( "{ " ).append( "promotionId:" ).append( parameters.get( "promotionId" ) ).append( "," ).append( " claimId:" ).append( parameters.get( "claimId" ) )
        .append( "," ).append( " levelNumber:" ).append( parameters.get( "levelNumber" ) ).append( "," ).append( " status:" ).append( "\"" ).append( parameters.get( "status" ) ).append( "\"" )
        .append( " }" );

    String idJson = r.toString();

    request.setAttribute( "idJson", idJson );

    // set the role in the scope
    request.setAttribute( "isAdmin", isAdmin() );
    request.setAttribute( "isDelegate", isDelegate() );

    if ( request.getHeader( "referer" ) != null )
    {
      if ( request.getHeader( "referer" ).contains( "participantProfilePage" ) )
      {
        request.getSession().setAttribute( "nominationApprovalsRedirectUrl", request.getHeader( "referer" ).concat( "#profile/AlertsAndMessages" ) );
      }
      else
      {
        request.getSession().setAttribute( "nominationApprovalsRedirectUrl", request.getHeader( "referer" ) );
      }
    }

    if ( StringUtils.isNotEmpty( (String)parameters.get( "claimId" ) ) )
    {
      Long claimId = Long.parseLong( (String)parameters.get( "claimId" ) );
      Claim approvable = getClaimService().getClaimById( claimId );
      if ( approvable instanceof NominationClaim )
      {
        NominationClaim nc = (NominationClaim)approvable;
        String userLanguage = UserManager.getUserLocale();
        if ( userLanguage != null && nc.getSubmitterCommentsLanguageType() != null )
        {
          if ( getSystemVariableService().getPropertyByName( SystemVariableService.MACHINE_LANGUAGE_ALLOW_TRANSLATION ).getBooleanVal() )
          {
            if ( !userLanguage.equals( nc.getSubmitterCommentsLanguageType().getCode() ) )
            {
              request.setAttribute( "allowTranslate", true );
            }
            else
            {
              request.setAttribute( "allowTranslate", false );
            }

          }
          else
          {
            request.setAttribute( "allowTranslate", false );
          }

          request.setAttribute( "translateClientState",
                                "clientState=" + URLEncoder.encode( RequestUtils.getRequiredParamString( request, "clientState" ), "UTF-8" ) + "&cryptoPass="
                                    + RequestUtils.getOptionalParamString( request, "cryptoPass" ) );
        }
        if(StringUtils.isNotEmpty( nc.getTeamName()))
        {
        	approvalForm.setTeam(true);
        }
        
        }
    }

    request.setAttribute( "pdfServiceUrl", getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PDF_SERVICE_URL ).getStringVal() );
    NominationPromotion promotion = null;
    int capPerPax=0;
    if(null != parameters.get( "promotionId" )){    	  
    	  promotion = (NominationPromotion)getPromotionService().getPromotionById((Long)parameters.get( "promotionId" ));      
      if(promotion.getCapPerPax()!=null){
    	  capPerPax=promotion.getCapPerPax();
    	  approvalForm.setCapPerPax(capPerPax);
      }
        
      String approvalTypeCode = promotion.getApprovalType().getCode();
      
      if ( approvalTypeCode.equals( ApprovalType.COKE_CUSTOM ))// && !nomPromo.isAwardActive())
      {
    	  approvalForm.setCustomApprovalType(true);        
       }
    if(promotion.isLevelPayoutByApproverAvailable() )
    {
    	approvalForm.setCustomPayoutType(true);
    }
    }
    NominationPromotion nomPromo = (NominationPromotion)getNominationPromotionService().getNominationPromotion( (Long)parameters.get( "promotionId" ) );
    
    // Client customization for WIP 58122
    List<TccNomLevelPayout> levelPayouts=getCokeClientService().getLevelTotalPoints( promotion.getId());
    Collections.sort( levelPayouts, ASCE_COMPARATOR_LEVEL_PAYOUTS );
    
    int i=1;
    for ( Iterator iter = levelPayouts.iterator(); iter.hasNext(); )
    {
    	TccNomLevelPayout t = (TccNomLevelPayout)iter.next();
    	t.setLevelId("level"+i);    	 
    	i++;
    }
    approvalForm.setLevelPayouts(levelPayouts);
    request.setAttribute( "levelPayouts", levelPayouts );
 
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward saveApprovals( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, UniqueConstraintViolationException
  {
    NominationsApprovalPageForm approvalForm = (NominationsApprovalPageForm)form;

    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionByIdWithAssociations( approvalForm.getPromotionId(), arc );
    approvalForm.setCumulativeNomination( promotion.isCumulative() );

    List<Approvable> approvables = buildNominationsApprovablesList( approvalForm, false );

    User approver = getUserService().getUserById( UserManager.getUserId() );
    
    List<Approvable> nonPendingApprovables = approvalForm.toDomain( approvables, approver, promotion );

    if ( !nonPendingApprovables.isEmpty() )
    {
      try
      {
        getClaimService().saveNominationApprovables( nonPendingApprovables, null, approver, false, promotion, approvalForm.getAwardType() );
      }
      catch( ServiceErrorException e )
      {
        writeAsJsonToResponse( getMessageFromServiceErrors( e.getServiceErrors() ), response );
        return null;
      }
    }

    String nominationApprovalsRedirectUrl = (String)request.getSession().getAttribute( "nominationApprovalsRedirectUrl" );
    request.getSession().removeAttribute( nominationApprovalsRedirectUrl );
    List<ServiceError> serviceErrors = new ArrayList<ServiceError>();
    writeAsJsonToResponse( getMessageFromServiceErrors( serviceErrors ), response );
    return null;
  }

  public static List<Approvable> buildNominationsApprovablesList( NominationsApprovalPageForm approvalForm, boolean includeNodeAssociation )
  {
    Date startDate = DateUtils.toDate( approvalForm.getDateStart() );
    Date endDate = DateUtils.toDate( approvalForm.getDateEnd() );
    Long promotionId = approvalForm.getPromotionId();
    Long approvalRound = approvalForm.getLevelId();
    String filterApprovalStatusCode = ApprovalStatusType.PENDING;
  
    String claimIds = approvalForm.getClaimIds();

    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_SUBMITTER_ADDRESS ) );
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_BEHAVIORS ) );

    if ( includeNodeAssociation )
    {
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_USER_NODES ) );
    }

    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );

    List<Approvable> approvableList = new ArrayList<Approvable>();

    Boolean open = Boolean.TRUE;
    Boolean expired = Boolean.FALSE;

    if ( approvalForm.isCumulativeNomination() )
    {
      AssociationRequestCollection claimGroupAssociationRequestCollection = new AssociationRequestCollection();
      claimGroupAssociationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.ALL_WITH_CLAIMS_ALL ) );

      // First collect add any existing claim groups (will only happen for during 2nd and later
      // rounds of approval. For first level approval, the group doesn't yet exist.
      List<PromotionApprovableValue> promotionClaimGroupsValueList = getClaimGroupService().getNominationClaimGroupsForApprovalByUser( UserManager.getUserId(),
                                                                                                                                       claimIds,
                                                                                                                                       promotionId != null ? new Long[] { promotionId } : null,
                                                                                                                                       open,
                                                                                                                                       PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                                                                       claimGroupAssociationRequestCollection,
                                                                                                                                       promotionAssociationRequestCollection,
                                                                                                                                       expired,
                                                                                                                                       filterApprovalStatusCode,
                                                                                                                                       approvalRound );

      approvableList.addAll( extractApprovables( promotionClaimGroupsValueList ) );

      if ( open != null && open || expired )
      {
        approvableList = getPendingApprovableList( approvableList,
                                                   claimIds,
                                                   filterApprovalStatusCode,
                                                   approvalRound,
                                                   promotionId,
                                                   open,
                                                   startDate,
                                                   endDate,
                                                   PromotionType.lookup( PromotionType.NOMINATION ),
                                                   claimAssociationRequestCollection,
                                                   promotionAssociationRequestCollection,
                                                   expired,
                                                   Boolean.FALSE,
                                                   approvalForm.isCumulativeNomination() );
      }
    }
    else
    {
      approvableList = getPendingApprovableList( approvableList,
                                                 claimIds,
                                                 filterApprovalStatusCode,
                                                 approvalRound,
                                                 promotionId,
                                                 open,
                                                 startDate,
                                                 endDate,
                                                 PromotionType.lookup( PromotionType.NOMINATION ),
                                                 claimAssociationRequestCollection,
                                                 promotionAssociationRequestCollection,
                                                 expired,
                                                 Boolean.FALSE,
                                                 approvalForm.isCumulativeNomination() );
    }
    return approvableList;
  }

  private static List<Approvable> getPendingApprovableList( List<Approvable> approvableList,
                                                            String claimIds,
                                                            String filterApprovalStatusCode,
                                                            Long approvalRound,
                                                            Long promotionId,
                                                            Boolean open,
                                                            Date startDate,
                                                            Date endDate,
                                                            PromotionType promotionType,
                                                            AssociationRequestCollection claimAssociationRequestCollection,
                                                            AssociationRequestCollection promotionAssociationRequestCollection,
                                                            Boolean expired,
                                                            Boolean allApprovers,
                                                            boolean isCumulativeNomination )
  {
    List<PromotionApprovableValue> promotionClaimsValueList = new ArrayList<PromotionApprovableValue>();
    promotionClaimsValueList = getClaimService().getNominationClaimsForApprovalByUser( UserManager.getUserId(),
                                                                                       claimIds,
                                                                                       promotionId != null ? new Long[] { promotionId } : null,
                                                                                       open,
                                                                                       startDate,
                                                                                       endDate,
                                                                                       PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                       claimAssociationRequestCollection,
                                                                                       promotionAssociationRequestCollection,
                                                                                       expired,
                                                                                       filterApprovalStatusCode,
                                                                                       approvalRound );

    if ( !promotionClaimsValueList.isEmpty() )
    {
      if ( isCumulativeNomination )
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
          approvables.add( approv );
        }
      }
    }
    return approvables;
  }

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

  public ActionForward fetchNominationsApprovalPageData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationsApprovalPageForm nominationsApprovalPageForm = (NominationsApprovalPageForm)form;
    Map<String, Object> parameters = new HashMap<String, Object>();

    parameters.put( "promotionId", nominationsApprovalPageForm.getPromotionId() );
    parameters.put( "approverUserId", UserManager.getUserId() );
    parameters.put( "userLocale", UserManager.getUserLocale() );
    parameters.put( "levelNumber", nominationsApprovalPageForm.getLevelNumber() != null ? nominationsApprovalPageForm.getLevelNumber() : 1 );

    NominationsApprovalPageDataValueBean nominationsApprovalPageDataValueBean = getNominationClaimService().getNominationsApprovalPageData( parameters );

    buildNominationsApprovalPageDataView( nominationsApprovalPageDataValueBean, parameters, response );

    return null;
  }

  private void buildNominationsApprovalPageDataView( NominationsApprovalPageDataValueBean nominationsApprovalPageDataValueBean, Map<String, Object> parameters, HttpServletResponse response )
      throws IOException
  {
    NominationsApprovalPageDataView nominationsApprovalPageDataView = new NominationsApprovalPageDataView();
    NominationPromotionApproval nominationPromotionApproval = new NominationPromotionApproval();

    List<NominationsApprovalPageDetailsValueBean> detailsList = nominationsApprovalPageDataValueBean.getDetailsList();

    nominationPromotionApproval.setPromoId( (Long)parameters.get( "promotionId" ) );
    for ( NominationsApprovalPageDetailsValueBean nominationsApprovalPageDetailsValueBean : detailsList )
    {
      nominationPromotionApproval.setName( nominationsApprovalPageDetailsValueBean.getPromotionName() );
      if ( UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
      {
        nominationPromotionApproval.setAdmin( true );
      }
      else
      {
        nominationPromotionApproval.setAdmin( false );
      }
      nominationPromotionApproval.setDelegate( UserManager.getUser().isDelegate() );
      UserNode userNode = getUserService().getPrimaryUserNode( UserManager.getUser().getUserId() );
      nominationPromotionApproval.setNodeId( userNode != null ? userNode.getNode().getId() : null );
      nominationPromotionApproval.setFinalLevelApprover( nominationsApprovalPageDetailsValueBean.isFinalLevelApprover() );
      nominationPromotionApproval.setTotalPromotionCount( nominationsApprovalPageDetailsValueBean.getTotalPromotionCount() );
      nominationPromotionApproval.setTimePeriodEnabled( nominationsApprovalPageDetailsValueBean.isTimePeriodEnabled() );

      if ( nominationsApprovalPageDetailsValueBean.isPayoutAtEachLevel() )
      {
        nominationPromotionApproval.setNotificationDateEnabled( nominationsApprovalPageDetailsValueBean.isPayoutAtEachLevel() );
      }
      else if ( nominationsApprovalPageDetailsValueBean.isFinalLevelApprover() )
      {
        nominationPromotionApproval.setNotificationDateEnabled( nominationsApprovalPageDetailsValueBean.isFinalLevelApprover() );
      }
      else
      {
        nominationPromotionApproval.setNotificationDateEnabled( Boolean.FALSE );
      }

      List<TimePeriod> timePeriods = new ArrayList<TimePeriod>();
      for ( NominationsApprovalPageTimePeriodsValueBean timePeriodsValueBean : nominationsApprovalPageDataValueBean.getTimePeriodsList() )
      {
        TimePeriod timePeriod = new TimePeriod();

        timePeriod.setId( timePeriodsValueBean.getTimePeriodId() );
        timePeriod.setName( timePeriodsValueBean.getTimePeriodName() );
        timePeriods.add( timePeriod );
      }
      nominationPromotionApproval.setTimePeriods( timePeriods );

      nominationPromotionApproval.setPayoutAtEachLevel( nominationsApprovalPageDetailsValueBean.isPayoutAtEachLevel() );
      nominationPromotionApproval.setStatus( ApprovalStatusType.PENDING );
      nominationPromotionApproval.setCurrecnyLabel( nominationsApprovalPageDetailsValueBean.getCurrencyLabel() );
      nominationPromotionApproval.setPayoutType( nominationsApprovalPageDetailsValueBean.getAwardType() );
      nominationPromotionApproval.setRulesText( nominationsApprovalPageDetailsValueBean.getRulesText() );
      if ( nominationsApprovalPageDetailsValueBean.getAwardType() != null && nominationsApprovalPageDetailsValueBean.getAwardType().equals( PromotionAwardsType.POINTS )
          && UserManager.getUser().getPrimaryCountryCode() != null && !UserManager.getUser().getPrimaryCountryCode().equals( "us" ) )
      {
        nominationPromotionApproval.setShowConversionLink( true );
      }
      else
      {
        nominationPromotionApproval.setShowConversionLink( false );
      }
    }

    List<ApprovalLevel> approvalLevels = new ArrayList<ApprovalLevel>();
    for ( NominationsApprovalPagePromotionLevelsValueBean promotionLevelsValueBean : nominationsApprovalPageDataValueBean.getPromotionLevelsList() )
    {
      ApprovalLevel approvalLevel = new ApprovalLevel();
      approvalLevel.setId( promotionLevelsValueBean.getLevelIndex() );
      if ( StringUtils.isNotEmpty( promotionLevelsValueBean.getLevelLabel() ) )
      {
        approvalLevel.setName( promotionLevelsValueBean.getLevelLabel() );
        approvalLevel.setDisplayName( promotionLevelsValueBean.getLevelLabel() );
      }
      else
      {
        approvalLevel.setName( ContentReaderManager.getText( "nomination.approvals.module", "LEVEL" ) + " " + promotionLevelsValueBean.getLevelIndex().toString() );
        approvalLevel.setDisplayName( ContentReaderManager.getText( "nomination.approvals.module", "LEVEL" ) + " " + promotionLevelsValueBean.getLevelIndex().toString() );
      }
      if ( parameters.get( "levelNumber" ).equals( promotionLevelsValueBean.getLevelIndex() ) )
      {
        approvalLevel.setSelected( true );
      }
      else
      {
        approvalLevel.setSelected( false );
      }
      approvalLevels.add( approvalLevel );
    }
    nominationPromotionApproval.setApprovalLevels( approvalLevels );

    nominationsApprovalPageDataView.setNominationPromotionApproval( nominationPromotionApproval );
    super.writeAsJsonToResponse( nominationsApprovalPageDataView, response );

  }

  public ActionForward fetchNominationsApprovalPageTableData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> parameters = new HashMap<String, Object>();
    NominationsApprovalPageForm nominationsApprovalPageForm = (NominationsApprovalPageForm)form;
    parameters.put( "promotionId", nominationsApprovalPageForm.getPromotionId() );
    parameters.put( "startDate", DateUtils.toDate( nominationsApprovalPageForm.getDateStart() ) );
    parameters.put( "endDate", DateUtils.toDate( nominationsApprovalPageForm.getDateEnd() ) );
    if ( nominationsApprovalPageForm.getTimePeriodFilter() != null && nominationsApprovalPageForm.getTimePeriodFilter() != 0 )
    {
      parameters.put( "timePeriodId", nominationsApprovalPageForm.getTimePeriodFilter() );
    }
    if ( nominationsApprovalPageForm.getLevelsFilter() != 0 )
    {
      parameters.put( "levelNumber", nominationsApprovalPageForm.getLevelsFilter() );
    }
    else
    {
      parameters.put( "levelNumber", nominationsApprovalPageForm.getLevelId() );
    }
    if ( nominationsApprovalPageForm.getStatusFilter() != null )
    {
      parameters.put( "status", nominationsApprovalPageForm.getStatusFilter() );
    }
    else
    {
      parameters.put( "status", ApprovalStatusType.PENDING );
    }
    parameters.put( "approverUserId", UserManager.getUserId() );

    int pageNumber;
    int rowNumStart;
    int rowNumEnd;
    int approvalsPerPage = 50;

    if ( request.getParameter( "currentPage" ) != null && request.getParameter( "sorting" ) != null )
    {
      pageNumber = Integer.parseInt( request.getParameter( "currentPage" ) );
      rowNumStart = 0;
      rowNumEnd = pageNumber * approvalsPerPage;
    }
    else if ( request.getParameter( "currentPage" ) != null )
    {
      pageNumber = Integer.parseInt( request.getParameter( "currentPage" ) ) + 1;
      rowNumStart = ( pageNumber - 1 ) * approvalsPerPage;
      rowNumEnd = pageNumber * approvalsPerPage;
    }
    else
    {
      pageNumber = 1;
      rowNumStart = ( pageNumber - 1 ) * approvalsPerPage;
      rowNumEnd = pageNumber * approvalsPerPage;
    }
    parameters.put( "pageNumber", pageNumber );
    parameters.put( "rowNumStart", rowNumStart );
    parameters.put( "rowNumEnd", rowNumEnd + 1 );

    if ( request.getParameter( "sortedBy" ) != null )
    {
      String sortedBy = request.getParameter( "sortedBy" );
      parameters.put( "sortedBy", sortedBy );
    }
    else
    {
      parameters.put( "sortedBy", "asc" );
    }
    if ( request.getParameter( "sortedOn" ) != null )
    {
      String sortedOn = request.getParameter( "sortedOn" );
      parameters.put( "sortedOn", sortedOn );
    }
    else
    {
      parameters.put( "sortedOn", "date_submitted" );
    }
    parameters.put( "approvalsPerPage", approvalsPerPage );

    parameters.put( "isCumulativeNomination", nominationsApprovalPageForm.isCumulativeNomination() );

    parameters.put( "locale", UserManager.getUserLocale() );
    NominationsApprovalPageTableValueBean resultData = getNominationClaimService().getNominationsApprovalPageTableData( parameters );

    parameters.put( "isMore", request.getParameter( "isMore" ) );
    buildNominationsApprovalPageTableDataView( resultData, nominationsApprovalPageForm, request, response, parameters );

    return null;
  }

  private void buildNominationsApprovalPageTableDataView( NominationsApprovalPageTableValueBean resultData,
                                                          NominationsApprovalPageForm nominationsApprovalPageForm,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response,
                                                          Map<String, Object> parameters )
      throws IOException
  {
    NominationsApprovalPageTableDataView nominationsApprovalPageTableDataView = new NominationsApprovalPageTableDataView();
    
    NominationsApprovalPageTableData nominationsApprovalPageTableData = new NominationsApprovalPageTableData();
    boolean isCumulativeNomination = false;
    boolean isCustomApprovalType = false;
    boolean isCustomPayoutType = false;
    NominationsApprovalBoxValueBean boxValueBean = new NominationsApprovalBoxValueBean();
 // Client customization for WIP #56492 starts
    NominationPromotion nomPromo = (NominationPromotion)getNominationPromotionService().getNominationPromotion( (Long)parameters.get( "promotionId" ) );
    String approvalTypeCode = nomPromo.getApprovalType().getCode();
   if ( approvalTypeCode.equals( ApprovalType.COKE_CUSTOM ))// && !nomPromo.isAwardActive())   
    {
      isCustomApprovalType =  true;
      request.setAttribute( "isCustomApprovalType", isCustomApprovalType );
    }
  
   //if(nominationsApprovalPageForm.isCustomPayoutType() && !nominationsApprovalPageForm.isCustomApprovalType())
   if(nomPromo.isLevelPayoutByApproverAvailable() ) //  && !isCustomApprovalType ) 
   {
	  isCustomPayoutType = true;
	  request.setAttribute( "isCustomPayoutType", isCustomPayoutType );
    }
 // Client customization for WIP #56492 ends
    
    if ( !resultData.getApprovalBoxList().isEmpty() )
    {
      boxValueBean = resultData.getApprovalBoxList().get( 0 );
      nominationsApprovalPageTableData.setCumulativeNomination( boxValueBean.isCumulativeNomination() );
      isCumulativeNomination = boxValueBean.isCumulativeNomination();
      
      nominationsApprovalPageTableData.setCustomApprovalType( isCustomApprovalType );// Client customization for WIP #56492
      nominationsApprovalPageTableData.setPreviousLevelName( boxValueBean.getPreviousLevelName() );
      nominationsApprovalPageTableData.setNextLevelName( boxValueBean.getNextLevelName() );
      nominationsApprovalPageTableData.setPendingNominations( boxValueBean.getPendingNominationsCount() );
      nominationsApprovalPageTableData.setLastBudgetRequestDate( DateUtils.toDisplayString( boxValueBean.getLastBudgetRequestDate() ) );
      nominationsApprovalPageTableData.setPayoutDescription( boxValueBean.getPayoutDescription() );

      if ( boxValueBean.getAwardPayoutType() != null && boxValueBean.getAwardPayoutType().equals( PromotionAwardsType.OTHER ) )
      {
        if ( StringUtils.isEmpty( boxValueBean.getOtherAwardQuantity() ) )
        {
          nominationsApprovalPageTableData.setBudgetBalance( boxValueBean.getPayoutDescription() );
        }
        else
        {
          nominationsApprovalPageTableData
              .setBudgetBalance( NumberFormatUtil.getLocaleBasedNumberFormat( Long.parseLong( boxValueBean.getOtherAwardQuantity() ) ) + " (" + boxValueBean.getPayoutDescription() + ")" );
        }
      }
      else if ( boxValueBean.getBudgetBalance() != null )
      {
        if ( boxValueBean.getAwardPayoutType() != null && boxValueBean.getAwardPayoutType().equals( PromotionAwardsType.CASH ) )
        {
          String approverCurrencyCode = getParticipantService().getParticipantCurrencyCode( UserManager.getUserId() );
          // If the currency code is missing for the country please make the entry in
          // DB(CURRENCY Table) for respective currency
          if ( approverCurrencyCode != null && !US_DOLLAR.equalsIgnoreCase( approverCurrencyCode ) )
          {
            BigDecimal convertedAmount = getCashCurrencyService().convertCurrency( "USD", approverCurrencyCode, boxValueBean.getBudgetBalance(), null );
            nominationsApprovalPageTableData.setBudgetBalance( NumberFormatUtil.getLocaleBasedBigDecimalFormat( convertedAmount, 2, UserManager.getLocale() ) );
          } 
          else
          {
            // US Currency. No need to convert the values
            nominationsApprovalPageTableData.setBudgetBalance( NumberFormatUtil.getLocaleBasedNumberFormat( boxValueBean.getBudgetBalance().longValue() ) );
          }

        }
        else
        {
          nominationsApprovalPageTableData.setBudgetBalance( NumberFormatUtil.getLocaleBasedNumberFormat( boxValueBean.getBudgetBalance().longValue() ) );
        }
        nominationsApprovalPageTableData.setBudgetPeriodName( boxValueBean.getBudgetPeriodName() );
        nominationsApprovalPageTableData.setPotentialAwardExceeded( boxValueBean.isPotentialBudgetExceeded() );
      }
      else
      {
        nominationsApprovalPageTableData.setBudgetPeriodName( boxValueBean.getBudgetPeriodName() );
        nominationsApprovalPageTableData.setBudgetBalance( null );
      }
      nominationsApprovalPageTableData.setCustomPayoutType(isCustomPayoutType);
    }
    int approvalsPerPage = (int)parameters.get( "approvalsPerPage" );
    nominationsApprovalPageTableData.setSortedOn( (String)parameters.get( "sortedOn" ) );
    nominationsApprovalPageTableData.setSortedBy( (String)parameters.get( "sortedBy" ) );
    nominationsApprovalPageTableData.setTotalRows( resultData.getTotalNumberOfApprovals() );
    nominationsApprovalPageTableData.setRowsPerPage( approvalsPerPage );
    nominationsApprovalPageTableData.setCurrentPage( (int)parameters.get( "pageNumber" ) );
    nominationsApprovalPageTableData.setTotalPages( (int)Math.ceil( (double)resultData.getTotalNumberOfApprovals() / (double)approvalsPerPage ) );
    if ( parameters.get( "isMore" ) != null && parameters.get( "isMore" ).equals( "true" ) )
    {
      nominationsApprovalPageTableData.setMore( true );
    }
    else
    {
      nominationsApprovalPageTableData.setMore( false );
    }

    nominationsApprovalPageTableData.setPreviousApproverCount( resultData.getPreviousLevelApproversList().size() );
    List<PreviousApprover> previousApprovers = new ArrayList<PreviousApprover>();
    for ( NominationsApprovalPagePreviousLevelApproversValueBean prevLevelAproversValueBean : resultData.getPreviousLevelApproversList() )
    {
      PreviousApprover previousApprover = new PreviousApprover();
      previousApprover.setId( prevLevelAproversValueBean.getUserId() );
      previousApprover.setName( prevLevelAproversValueBean.getLastName() + ", " + prevLevelAproversValueBean.getFirstName() );
      previousApprovers.add( previousApprover );
    }
    nominationsApprovalPageTableData.setPreviousApprovers( previousApprovers );

    nominationsApprovalPageTableData.setNextApproverCount( resultData.getNextLevelApproversList().size() );
    List<NextApprover> nextApprovers = new ArrayList<NextApprover>();
    for ( NominationsApprovalPageNextLevelApproversValueBean nextLevelAproversValueBean : resultData.getNextLevelApproversList() )
    {
      NextApprover nextApprover = new NextApprover();
      nextApprover.setId( nextLevelAproversValueBean.getApproverUserId() );
      nextApprover.setName( nextLevelAproversValueBean.getLastName() + ", " + nextLevelAproversValueBean.getFirstName() );
      nextApprovers.add( nextApprover );
    }
    nominationsApprovalPageTableData.setNextApprovers( nextApprovers );

    if ( !resultData.getNominationsApprovalAwardDetailsList().isEmpty() )
    {
      NominationsApprovalAwardDetailsValueBean nominationsApprovalAwardDetailsValueBean = resultData.getNominationsApprovalAwardDetailsList().get( 0 );
      if ( !StringUtils.isEmpty( boxValueBean.getPayoutDescription() ) )
      {
        nominationsApprovalPageTableData.setAwardType( "other" );
      }
      else if ( nominationsApprovalAwardDetailsValueBean.isAwardAmountTypeFixed() )
      {
        nominationsApprovalPageTableData.setAwardType( "fixed" );
      }
      else if ( !nominationsApprovalAwardDetailsValueBean.isAwardAmountTypeFixed() && nominationsApprovalAwardDetailsValueBean.getCalculatorId() != null
          && nominationsApprovalAwardDetailsValueBean.getCalculatorId() != 0 )
      {
        nominationsApprovalPageTableData.setAwardType( "calculated" );
      }
      else if ( nominationsApprovalAwardDetailsValueBean.isAwardAmountTypeRange() )
      {
        nominationsApprovalPageTableData.setAwardType( "range" );
      }
      else if ( nominationsApprovalAwardDetailsValueBean.isAwardAmountTypeNone() )
      {
        nominationsApprovalPageTableData.setAwardType( "none" );
      }
    
      if ( boxValueBean.getAwardPayoutType() != null && boxValueBean.getAwardPayoutType().equals( PromotionAwardsType.CASH ) )
      {
        String approverCurrencyCode = getParticipantService().getParticipantCurrencyCode( UserManager.getUserId() );
        // If the currency code is missing for the country please make the entry in
        // DB(CURRENCY Table) for respective currency
        if ( approverCurrencyCode != null && !US_DOLLAR.equalsIgnoreCase( approverCurrencyCode ) )
        {
          BigDecimal convertedAwardAmountMin = getCashCurrencyService().convertCurrency( "USD", approverCurrencyCode, nominationsApprovalAwardDetailsValueBean.getAwardAmountMin(), null );
          nominationsApprovalPageTableData.setAwardMin( convertedAwardAmountMin );

          BigDecimal convertedAwardAmountMax = getCashCurrencyService().convertCurrency( "USD", approverCurrencyCode, nominationsApprovalAwardDetailsValueBean.getAwardAmountMax(), null );
          nominationsApprovalPageTableData.setAwardMax( convertedAwardAmountMax );
        }
        else
        {
          nominationsApprovalPageTableData.setAwardMin( nominationsApprovalAwardDetailsValueBean.getAwardAmountMin() );
          nominationsApprovalPageTableData.setAwardMax( nominationsApprovalAwardDetailsValueBean.getAwardAmountMax() );
        }
      }
      else
      {
        nominationsApprovalPageTableData.setAwardMin( nominationsApprovalAwardDetailsValueBean.getAwardAmountMin() );
        nominationsApprovalPageTableData.setAwardMax( nominationsApprovalAwardDetailsValueBean.getAwardAmountMax() );
      }
    }

    MetaView metaView = new MetaView();
    List<Column> columns = new ArrayList<Column>();
    buildColumnsListView( columns, isCumulativeNomination, (String)parameters.get( "status" ) );
    metaView.setColumns( columns );
    nominationsApprovalPageTableData.setMetaView( metaView );
    
    List<Result> results = new ArrayList<Result>();
    buildResultListView( results, resultData, boxValueBean, request, parameters );
    nominationsApprovalPageTableData.setResults( results );

    parameters.put( "claimIds", nominationsApprovalPageForm.getTabularClaimIds( nominationsApprovalPageTableData ) );
    nominationsApprovalPageTableData.setExcelUrl( buildExtractCsvUrl( request, parameters ) );
    nominationsApprovalPageTableData.setPdfUrl( buildExtractPdfUrl( request, parameters ) );

    nominationsApprovalPageTableDataView.setNominationsApprovalPageTableData( nominationsApprovalPageTableData );
    super.writeAsJsonToResponse( nominationsApprovalPageTableDataView, response );
  }

  private String buildExtractCsvUrl( HttpServletRequest request, Map<String, Object> parameters )
  {
    return ClientStateUtils.generateEncodedLink( request.getContextPath(), "/claim/nominationsApprovalPage.do?method=extractCsv", parameters );
  }

  private String buildExtractPdfUrl( HttpServletRequest request, Map<String, Object> parameters )
  {
    return ClientStateUtils.generateEncodedLink( request.getContextPath(), "/claim/nominationsApprovalPage.do?method=extractPdf", parameters );
  }

  private void buildResultListView( List<Result> results,
                                    NominationsApprovalPageTableValueBean resultData,
                                    NominationsApprovalBoxValueBean nominationsApprovalBoxValueBean,
                                    HttpServletRequest request,
                                    Map<String, Object> parameters )
  {
    int resultIndex = (int)parameters.get( "rowNumStart" );
    NominationPromotion nomPromo = (NominationPromotion)getNominationPromotionService().getNominationPromotion( (Long)parameters.get( "promotionId" ) );
    
 // Client customization for WIP #56492 starts
    List<ApproverLevelTypeBean> levels = new ArrayList<ApproverLevelTypeBean>();
    
    List<ApproverLevelType> approverLevelTypes = ApproverLevelType.getList( );
    
    for(Iterator iterator =approverLevelTypes.iterator();iterator.hasNext(); )
    {
      ApproverLevelType approverLevelTpe = (ApproverLevelType)iterator.next();
      ApproverLevelTypeBean approverLevelData = new ApproverLevelTypeBean();
      approverLevelData.setCode( approverLevelTpe.getCode() );
      approverLevelData.setName( approverLevelTpe.getName() );
      levels.add( approverLevelData );
    }
    
 // Client customization for WIP #56492 ends  
    /*List<TccNomLevelPayout> levelPayouts =  getCokeClientService().getLevelTotalPoints( nomPromo.getId());
     
    int i=1;
    List<LevelPayoutDataBean> payoutLevels = new ArrayList<LevelPayoutDataBean>();
    for(Iterator iterator =levelPayouts.iterator();iterator.hasNext(); )
    {
      TccNomLevelPayout levelPayout = (TccNomLevelPayout)iterator.next();
      LevelPayoutDataBean levelData = new LevelPayoutDataBean();      
      levelData.setLevelName( levelPayout.getLevelDescription() );
      levelData.setLevelValue( levelPayout.getTotalPoints().toString() );
      levelPayout.setLevelId("level"+i);    	
  		i++;
      payoutLevels.add( levelData );
    }*/
    
    for ( NominationsApprovalDetailsValueBean nominationsApprovalDetailsValueBean : resultData.getNominationsApprovalDetailsList() )
    {
      Result result = new Result();     
      buildNonCumulativeResultView( nominationsApprovalDetailsValueBean, nominationsApprovalBoxValueBean, resultData, result, request, resultIndex, nomPromo.getPublicationDate(), levels );
      results.add( result );
      resultIndex++;
    }

    for ( NominationsApprovalDetailsValueBean cumulativeNomApprovalDetailsValueBean : resultData.getCumulativeNominationsApprovalDetailsList() )
    {
      Result result = new Result();
      results = buildCumulativeResultView( cumulativeNomApprovalDetailsValueBean, nominationsApprovalBoxValueBean, resultData, result, results, resultIndex, nomPromo.getPublicationDate(), levels);
      resultIndex++;
    }
  }

  private List<Result> buildCumulativeResultView( NominationsApprovalDetailsValueBean nominationsApprovalDetailsValueBean,
                                                  NominationsApprovalBoxValueBean nominationsApprovalBoxValueBean,
                                                  NominationsApprovalPageTableValueBean resultData,
                                                  Result result,
                                                  List<Result> results,
                                                  int resultIndex,
                                                  Date date, List<ApproverLevelTypeBean> levels)
  {
    result.setClaimId( nominationsApprovalDetailsValueBean.getClaimId() );
    result.setClaimGroupId( nominationsApprovalDetailsValueBean.getClaimGroupId() );
    result.setIndex( resultIndex );
    result.setPaxId( nominationsApprovalDetailsValueBean.getPaxId() );
    result.setNominee( nominationsApprovalDetailsValueBean.getNomineeName() );
    result.setAvatarUrl( nominationsApprovalDetailsValueBean.getAvatarUrl() );
    result.setCountryCode( nominationsApprovalDetailsValueBean.getCountryName() );
    result.setOrgName( nominationsApprovalDetailsValueBean.getOrgName() );
    result.setJobName( nominationsApprovalDetailsValueBean.getJobPositionName() );
    result.setDerpartmentName( nominationsApprovalDetailsValueBean.getDepartmentName() );
    result.setPastWinner( nominationsApprovalDetailsValueBean.isWonFlag() );
    result.setPastWinnerMaxLimit( nominationsApprovalDetailsValueBean.isExceedFlag() );
    result.setRecentTimePeriodWon( nominationsApprovalDetailsValueBean.getRecentTimePeriodWon() );
    result.setMostRecentTimeDate( DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getMostRecentTimeDate() ) );
    result.setDeinalReason( nominationsApprovalDetailsValueBean.getDenialReason() );
    result.setWinnerMessage( nominationsApprovalDetailsValueBean.getWinnerMessage() );
    result.setMoreinfoMessage( nominationsApprovalDetailsValueBean.getMoreInfoMessage() );
    result.setNominatorCount( nominationsApprovalDetailsValueBean.getNominatorCount() );
    result.setNotificationDate( ( DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getNotificationDate() ) != null )
        && ( StringUtils.isNotEmpty( DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getNotificationDate() ) ) )
            ? DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getNotificationDate() )
            : DateUtils.toDisplayString( date ) );
    result.setOptOutAwards( nominationsApprovalDetailsValueBean.isOptOutAwards() );
    if ( StringUtils.isNotEmpty( nominationsApprovalDetailsValueBean.getLevelName() ) )
    {
      result.setLevelName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.LEVEL" ) + " " + nominationsApprovalDetailsValueBean.getLevelName() );
    }
    else
    {
      result.setLevelName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.LEVEL" ) + " " + nominationsApprovalDetailsValueBean.getLevelIndex() );
    }

    if( null != result.getAward()){
    	nominationsApprovalDetailsValueBean.setAwardAmount(new BigDecimal(result.getAward()) ); 
    }
    if ( !StringUtils.isEmpty( nominationsApprovalBoxValueBean.getPayoutDescription() ) )
    {
      result.setAward( nominationsApprovalBoxValueBean.getPayoutDescription() );
    }
    else
    {
      if ( nominationsApprovalDetailsValueBean.getAwardAmount() != null )
      {
        if ( nominationsApprovalDetailsValueBean.isOptOutAwards() )
        {
          result.setAward( "0" );
        }
        else
        {
          result.setAward( nominationsApprovalDetailsValueBean.getAwardAmount().toString() );
        }
      }
    }
    result.setStatus( nominationsApprovalDetailsValueBean.getStatus() );

    List<TimePeriodViewBean> timePeriods = new ArrayList<TimePeriodViewBean>();
    for ( NominationsApprovalTimePeriodsValueBean nominationsApprovalTimePeriodsValueBean : resultData.getNominationsApprovalTimePeriodsList() )
    {
      List<String> claimIds = Arrays.asList( nominationsApprovalDetailsValueBean.getClaimId().split( "," ) );
      if ( claimIds.contains( nominationsApprovalTimePeriodsValueBean.getClaimId().toString() ) )
      {
        TimePeriodViewBean timePeriod = new TimePeriodViewBean();

        timePeriod.setId( nominationsApprovalTimePeriodsValueBean.getTimePeriodId() );
        timePeriod.setName( nominationsApprovalTimePeriodsValueBean.getTimePeriodName() );

        if ( nominationsApprovalTimePeriodsValueBean.getMaxWinsllowed() != 0 )
        {
          timePeriod.setMaxWinsllowed( nominationsApprovalTimePeriodsValueBean.getMaxWinsllowed() );
        }
        if ( nominationsApprovalTimePeriodsValueBean.getNoOfWinnners() != 0 )
        {
          timePeriod.setNoOfWinnners( nominationsApprovalTimePeriodsValueBean.getNoOfWinnners() );
        }
        timePeriods.add( timePeriod );
      }
    }
    result.setTimePeriods( timePeriods );
    result.setLevels( levels );// Client customization for WIP #56492
    results.add( result );

    return results;
  }

  private void buildNonCumulativeResultView( NominationsApprovalDetailsValueBean nominationsApprovalDetailsValueBean,
                                             NominationsApprovalBoxValueBean nominationsApprovalBoxValueBean,
                                             NominationsApprovalPageTableValueBean resultData,
                                             Result result,
                                             HttpServletRequest request,
                                             int resultIndex,
                                             Date date, List<ApproverLevelTypeBean> levels  )
  {
    String siteUrlPrefix = "";
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_PRE ).getStringVal();
    }
    else
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    }

    result.setContextPath( siteUrlPrefix + "/assets/" );
    result.setClaimId( nominationsApprovalDetailsValueBean.getClaimId() );
    // Client customization for WIP #39189 starts
    List<Attachment> attachments = new ArrayList<Attachment>();
    if ( !StringUtil.isNullOrEmpty( nominationsApprovalDetailsValueBean.getClaimId() ) )
    {
      List<TcccClaimFileValueBean> files = getClaimService().getClaimFiles( Long.parseLong( nominationsApprovalDetailsValueBean.getClaimId() ) );
      if ( Objects.nonNull( files ) )
      {
        files.forEach( file ->
        {
          Attachment att = new Attachment();
          att.setAttachmentName( file.getFileName() );
          att.setAttachmentUrl( file.getUrl() );
          attachments.add( att );
        } );
      }
    }
    result.setAttachments( attachments );
    // Client customization for WIP #39189 ends
	if (!nominationsApprovalDetailsValueBean.isTeam() && null != result.getClaimId()) {
		Claim approvable = getClaimService().getClaimById(new Long(result.getClaimId()));
		if (approvable instanceof NominationClaim) {
			NominationClaim nc = (NominationClaim) approvable;
			nominationsApprovalDetailsValueBean.setTeam(nc.isTeam());
		}
	}        
    result.setIndex( resultIndex );
    result.setPaxId( nominationsApprovalDetailsValueBean.getPaxId() );
    result.setTeamId( nominationsApprovalDetailsValueBean.getTeamId() );
    result.setNominee( nominationsApprovalDetailsValueBean.getNomineeName() );
    result.setAvatarUrl( nominationsApprovalDetailsValueBean.getAvatarUrl() );
    result.setCountryCode( nominationsApprovalDetailsValueBean.getCountryName() );
    result.setOrgName( nominationsApprovalDetailsValueBean.getOrgName() );
    result.setJobName( nominationsApprovalDetailsValueBean.getJobPositionName() );
    result.setDerpartmentName( nominationsApprovalDetailsValueBean.getDepartmentName() );
    result.setPastWinner( nominationsApprovalDetailsValueBean.isWonFlag() );
    result.setPastWinnerMaxLimit( nominationsApprovalDetailsValueBean.isExceedFlag() );
    result.setRecentTimePeriodWon( nominationsApprovalDetailsValueBean.getRecentTimePeriodWon() );
    result.setMostRecentTimeDate( DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getMostRecentTimeDate() ) );
    result.setTeam( nominationsApprovalDetailsValueBean.isTeam() );
    result.setTeamMemberCount( nominationsApprovalDetailsValueBean.getTeamCount() );
    result.setDateSubmitted( DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getSubmittedDate() ) );
    result.setNominator( nominationsApprovalDetailsValueBean.getNominatorName() );
    result.setNominatorId( nominationsApprovalDetailsValueBean.getNominatorPaxId() );
    result.setDeinalReason( nominationsApprovalDetailsValueBean.getDenialReason() );
    result.setWinnerMessage( nominationsApprovalDetailsValueBean.getWinnerMessage() );
    result.setMoreinfoMessage( nominationsApprovalDetailsValueBean.getMoreInfoMessage() );
    result.setNotificationDate( ( DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getNotificationDate() ) != null )
        && ( StringUtils.isNotEmpty( DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getNotificationDate() ) ) )
            ? DateUtils.toDisplayString( nominationsApprovalDetailsValueBean.getNotificationDate() )
            : DateUtils.toDisplayString( date ) );
    result.setOptOutAwards( nominationsApprovalDetailsValueBean.isOptOutAwards() );
    if ( StringUtils.isNotEmpty( nominationsApprovalDetailsValueBean.getLevelName() ) )
    {
      result.setLevelName( nominationsApprovalDetailsValueBean.getLevelName() );
    }
    else
    {
      result.setLevelName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.LEVEL" ) + " " + nominationsApprovalDetailsValueBean.getLevelIndex() );
    }

    if( null != result.getAward()){
    	nominationsApprovalDetailsValueBean.setAwardAmount(new BigDecimal(result.getAward()) ); 
    }
    if ( !StringUtils.isEmpty( nominationsApprovalBoxValueBean.getPayoutDescription() ) )
    {
      result.setAward( nominationsApprovalBoxValueBean.getPayoutDescription() );
    }
    else
    {
      if ( nominationsApprovalDetailsValueBean.getAwardAmount() != null )
      {
        if ( nominationsApprovalDetailsValueBean.isTeam() )
        {
          result.setTeamAward( nominationsApprovalDetailsValueBean.getAwardAmount() );
        }
        else
        {
          if ( nominationsApprovalDetailsValueBean.isOptOutAwards() )
          {
            result.setAward( "0" );
          }
          else
          {
            result.setAward( nominationsApprovalDetailsValueBean.getAwardAmount().toString() );
          }
        }
      }
    }
    result.setStatus( nominationsApprovalDetailsValueBean.getStatus() );
    if ( !StringUtils.isEmpty( nominationsApprovalDetailsValueBean.getEcardUrl() ) )
    {
      result.seteCardImg( nominationsApprovalDetailsValueBean.getEcardUrl() );
    }
    else if ( nominationsApprovalDetailsValueBean.getEcardImage() != null )
    {
      result.seteCardImg( nominationsApprovalDetailsValueBean.getEcardImage() );
    }
    // MTC- To be changed
    String eCardVideoLink = null;
    String videoImage = null;
    if ( StringUtils.isNotBlank( nominationsApprovalDetailsValueBean.getCardVideoUrl() ) && Objects.nonNull( nominationsApprovalDetailsValueBean.getCardVideoUrl() ) )
    {

      if ( nominationsApprovalDetailsValueBean.getCardVideoUrl().contains( ActionConstants.REQUEST_ID ) )
      {

        MTCVideo mtcVideo = getMTCVideoService().getMTCVideoByRequestId( nominationsApprovalDetailsValueBean.getRequestId( nominationsApprovalDetailsValueBean.getCardVideoUrl() ) );

        if ( Objects.nonNull( mtcVideo ) )
        {
          eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
          videoImage = mtcVideo.getThumbNailImageUrl();
        }
        else
        {
          eCardVideoLink = nominationsApprovalDetailsValueBean.getActualCardUrl( nominationsApprovalDetailsValueBean.getCardVideoUrl() );
          videoImage = nominationsApprovalDetailsValueBean.getActualCardUrl( nominationsApprovalDetailsValueBean.getCardImageUrl() );
        }
        result.setVideoUrl( eCardVideoLink );
        result.setVideoImg( videoImage );
      }

    }
    else
    {
      result.setVideoUrl( nominationsApprovalDetailsValueBean.getCardVideoUrl() );
      result.setVideoImg( nominationsApprovalDetailsValueBean.getCardImageUrl() );

    }

    if ( StringUtils.isNotBlank( nominationsApprovalDetailsValueBean.getSubmitterComments() ) )
    {
      result.setReason( nominationsApprovalDetailsValueBean.getSubmitterComments() );
    }
    result.setSubmitterLangId( null ); // nominationsApprovalDetailsValueBean.getSubmitterCommentsLangId()
    if ( StringUtils.isNotBlank( nominationsApprovalDetailsValueBean.getMoreInfoComments() ) )
    {
      result.setMoreInfo( nominationsApprovalDetailsValueBean.getMoreInfoComments() );
    }
    result.setAttachmentUrl( nominationsApprovalDetailsValueBean.getWhyAttachmentUrl() );
    result.setAttachmentName( nominationsApprovalDetailsValueBean.getWhyAttachmentName() );

    // Not really doing anything with this attribute except FE is checking if string contains any
    // value. Calling amazon service based on the check
    if ( nominationsApprovalDetailsValueBean.getCertificateId() != null && nominationsApprovalDetailsValueBean.getCertificateId() != 0 )
    {
      result.seteCertUrl( "Certificate exists" );
    }
    else
    {
      result.seteCertUrl( null );
    }

    List<TeamMemeber> teamMembers = new ArrayList<TeamMemeber>();
    for ( NominationsApprovalTeamMembersValueBean nominationsApprovalTeamMembersValueBean : resultData.getNominationsApprovalTeamMembersList() )
    {
      if ( nominationsApprovalDetailsValueBean.getClaimId().equals( nominationsApprovalTeamMembersValueBean.getClaimId().toString() ) )
      {
        TeamMemeber teamMember = new TeamMemeber();

        teamMember.setId( nominationsApprovalTeamMembersValueBean.getUserId() );
        teamMember.setName( nominationsApprovalTeamMembersValueBean.getUserName() );
        teamMember.setCountryCode( nominationsApprovalTeamMembersValueBean.getCountryName() );
        teamMember.setOrgName( nominationsApprovalTeamMembersValueBean.getOrgName() );
        teamMember.setJobName( nominationsApprovalTeamMembersValueBean.getJobPositionName() );
        teamMember.setDepartmentName( nominationsApprovalTeamMembersValueBean.getDepartmentName() );
        teamMember.setOptOutAwards( nominationsApprovalTeamMembersValueBean.isOptOutAwards() );
        if ( nominationsApprovalTeamMembersValueBean.isOptOutAwards() )
        {
          teamMember.setAward( BigDecimal.ZERO );
        }
        else
        {
		 if( null != result.getAward()){
			 nominationsApprovalTeamMembersValueBean.setAwardAmount(new BigDecimal(result.getAward()) ); 
		    }
          teamMember.setAward( nominationsApprovalTeamMembersValueBean.getAwardAmount() );
        }

        teamMembers.add( teamMember );
      }
    }
    result.setTeamMembers( teamMembers );

    List<Behavior> behaviors = new ArrayList<Behavior>();
    List<CustomField> customFields = new ArrayList<CustomField>();

    for ( NominationsApprovalBehaviorsValueBean nominationsApprovalBehaviorsValueBean : resultData.getNominationsApprovalBehaviorsList() )
    {
      if ( nominationsApprovalDetailsValueBean.getClaimId().equals( nominationsApprovalBehaviorsValueBean.getClaimId().toString() ) )
      {
        Behavior behavior = new Behavior();
        behavior.setName( nominationsApprovalBehaviorsValueBean.getBehaviorName() );

        if ( StringUtils.isNotBlank( nominationsApprovalBehaviorsValueBean.getBadgeName() ) )
        {
          List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( nominationsApprovalBehaviorsValueBean.getBadgeName() );
          Iterator itr = earnedNotEarnedImageList.iterator();
          while ( itr.hasNext() )
          {
            BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
            behavior.setImg( siteUrlPrefix + badgeLib.getEarnedImageSmall() );
          }
        }
        behaviors.add( behavior );
      }
    }
    result.setBehaviors( behaviors );

    for ( NominationsApprovalCustomValueBean nominationsApprovalCustomValueBean : resultData.getNominationsApprovalCustomList() )
    {
      if ( nominationsApprovalDetailsValueBean.getClaimId().equals( nominationsApprovalCustomValueBean.getClaimId().toString() ) )
      {
        CustomField customField = new CustomField();
        customField.setId( nominationsApprovalCustomValueBean.getClaimFormStepElementId() );
        customField.setName( nominationsApprovalCustomValueBean.getClaimFormStepElementName() );
        customField.setDescription( nominationsApprovalCustomValueBean.getDescription() );
        customFields.add( customField );
      }
    }
    result.setCustomFields( customFields );

    List<TimePeriodViewBean> timePeriods = new ArrayList<TimePeriodViewBean>();
    for ( NominationsApprovalTimePeriodsValueBean nominationsApprovalTimePeriodsValueBean : resultData.getNominationsApprovalTimePeriodsList() )
    {
      if ( nominationsApprovalDetailsValueBean.getClaimId().equals( nominationsApprovalTimePeriodsValueBean.getClaimId().toString() ) )
      {
        TimePeriodViewBean timePeriod = new TimePeriodViewBean();

        timePeriod.setId( nominationsApprovalTimePeriodsValueBean.getTimePeriodId() );
        timePeriod.setName( nominationsApprovalTimePeriodsValueBean.getTimePeriodName() );

        if ( nominationsApprovalTimePeriodsValueBean.getMaxWinsllowed() != 0 )
        {
          timePeriod.setMaxWinsllowed( nominationsApprovalTimePeriodsValueBean.getMaxWinsllowed() );
        }
        if ( nominationsApprovalTimePeriodsValueBean.getNoOfWinnners() != 0 )
        {
          timePeriod.setNoOfWinnners( nominationsApprovalTimePeriodsValueBean.getNoOfWinnners() );
        }

        timePeriods.add( timePeriod );
      }
    }
    result.setTimePeriods( timePeriods );
    result.setLevels( levels ); 
  }

  private void buildColumnsListView( List<Column> columns, boolean isCumulativeNomination, String status )
  {
    Column column1 = new Column();
    column1.setId( (long)1 );
    column1.setName( "expand" );
    column1.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.EXPAND" ) );
    column1.setSortable( false );
    columns.add( column1 );

    Column column2 = new Column();
    column2.setId( (long)2 );
    column2.setName( "nominee_name" );
    column2.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.NOMINEE" ) );
    column2.setSortable( true );
    columns.add( column2 );

    Column column3 = new Column();
    column3.setId( (long)3 );
    column3.setName( "submitted_date" );
    column3.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.DATE_SUBMITTED" ) );

    Column column4 = new Column();
    column4.setId( (long)4 );

    if ( isCumulativeNomination )
    {
      column3.setSortable( false );
      column4.setName( "number_of_nominators" );
      column4.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.NUMBER_OF_NOMINATORS" ) );
      column4.setSortable( false );
    }
    else
    {
      column3.setSortable( true );
      column4.setName( "nominator_name" );
      column4.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.NOMINATOR" ) );
      column4.setSortable( true );
    }

    columns.add( column3 );
    columns.add( column4 );

    Column column5 = new Column();
    column5.setId( (long)5 );
    column5.setName( "status" );
    column5.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.STATUS" ) );
    column5.setSortable( false );
    columns.add( column5 );

    Column column6 = new Column();
    column6.setId( (long)6 );
    column6.setName( "level" );
    column6.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.LEVEL" ) );
    column6.setSortable( false );
    columns.add( column6 );

    Column column7 = new Column();
    column7.setId( (long)7 );
    column7.setName( "award" );
    column7.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.AWARD" ) );
    column7.setSortable( false );
    columns.add( column7 );

    Column column8 = new Column();
    column8.setId( (long)8 );
    column8.setName( "notificationDate" );
    column8.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.NOTIFICATION_DATE" ) );
    column8.setSortable( false );
    columns.add( column8 );

    Column column9 = new Column();
    column9.setId( (long)9 );
    column9.setName( "timePeriod" );
    column9.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.TIME_PERIOD" ) );
    column9.setSortable( false );
    columns.add( column9 );

    Column column10 = new Column();
    column10.setId( (long)10 );
    column10.setName( "compare" );
    column10.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.approvals.module.COMPARE" ) );
    column10.setSortable( false );
    columns.add( column10 );

  }

  public ActionForward fetchCumulativeApprovalNominatorTableData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationsApprovalPageForm nominationsApprovalPageForm = (NominationsApprovalPageForm)form;
    Map<String, Object> parameters = new HashMap<String, Object>();

    parameters.put( "promotionId", nominationsApprovalPageForm.getPromotionId() );
    parameters.put( "paxId", nominationsApprovalPageForm.getPaxId() );
    if ( nominationsApprovalPageForm.getClaimGroupId() != null && nominationsApprovalPageForm.getClaimGroupId() != 0 )
    {
      parameters.put( "claimGroupId", nominationsApprovalPageForm.getClaimGroupId() );
    }
    parameters.put( "startDate", DateUtils.toDate( nominationsApprovalPageForm.getDateStart() ) );
    parameters.put( "endDate", DateUtils.toDate( nominationsApprovalPageForm.getDateEnd() ) );
    if ( nominationsApprovalPageForm.getTimePeriodFilter() != null && nominationsApprovalPageForm.getTimePeriodFilter() != 0 )
    {
      parameters.put( "timePeriodId", nominationsApprovalPageForm.getTimePeriodFilter() );
    }
    if ( nominationsApprovalPageForm.getLevelsFilter() != 0 )
    {
      parameters.put( "levelNumber", nominationsApprovalPageForm.getLevelsFilter() );
    }
    else
    {
      parameters.put( "levelNumber", nominationsApprovalPageForm.getLevelId() );
    }
    if ( nominationsApprovalPageForm.getStatusFilter() != null )
    {
      parameters.put( "status", nominationsApprovalPageForm.getStatusFilter() );
    }
    else
    {
      parameters.put( "status", ApprovalStatusType.PENDING );
    }
    parameters.put( "approverUserId", UserManager.getUserId() );

    parameters.put( "locale", UserManager.getUserLocale() );

    CumulativeInfoTableDataValueBean cumulativeInfoTableDataValueBean = getNominationClaimService().getCumulativeApprovalNominatorTableData( parameters );

    buildCumulativeTableDataInfoView( cumulativeInfoTableDataValueBean, parameters, response );

    return null;
  }

  private void buildCumulativeTableDataInfoView( CumulativeInfoTableDataValueBean cumulativeInfoTableDataValueBean, Map<String, Object> parameters, HttpServletResponse response ) throws IOException
  {
    CumulativeInfoTableDataViewBean cumulativeInfoTableDataViewBean = new CumulativeInfoTableDataViewBean();

    List<CumulativeInfoView> cumulativeInfoList = new ArrayList<CumulativeInfoView>();

    for ( CumulativeApprovalNominatorInfoValueBean cumulativeApprovalNominatorInfoValueBean : cumulativeInfoTableDataValueBean.getCumulativeApprovalNominatorInfoList() )
    {
      CumulativeInfoView cumulativeInfoView = new CumulativeInfoView();

      cumulativeInfoView.setClaimId( cumulativeApprovalNominatorInfoValueBean.getClaimId() );
      cumulativeInfoView.setDateSubmitted( cumulativeApprovalNominatorInfoValueBean.getSubmittedDate() );
      if ( cumulativeApprovalNominatorInfoValueBean.getCertificateId() != null && cumulativeApprovalNominatorInfoValueBean.getCertificateId() != 0 )
      {
        cumulativeInfoView.seteCertUrl( "Certificate exists" );
      }
      else
      {
        cumulativeInfoView.seteCertUrl( null );
      }
      cumulativeInfoView.setMoreInfo( cumulativeApprovalNominatorInfoValueBean.getMoreInfo() );
      cumulativeInfoView.setNominator( cumulativeApprovalNominatorInfoValueBean.getNominatorName() );
      cumulativeInfoView.setNominatorId( cumulativeApprovalNominatorInfoValueBean.getNominatorPaxId() );
      cumulativeInfoView.setReason( cumulativeApprovalNominatorInfoValueBean.getReason() );
      cumulativeInfoView.setWhyAttachmentName( cumulativeApprovalNominatorInfoValueBean.getWhyAttachmentName() );
      cumulativeInfoView.setWhyAttachmentUrl( cumulativeApprovalNominatorInfoValueBean.getWhyAttachmentUrl() );

      List<CustomFieldView> customFields = new ArrayList<CustomFieldView>();

      for ( NominationsApprovalCustomValueBean nominationsApprovalCustomValueBean : cumulativeInfoTableDataValueBean.getNominationsApprovalCustomList() )
      {
        CustomFieldView customFieldView = new CustomFieldView();

        customFieldView.setId( nominationsApprovalCustomValueBean.getClaimFormStepElementId() );
        customFieldView.setName( nominationsApprovalCustomValueBean.getClaimFormStepElementName() );
        customFieldView.setDescription( nominationsApprovalCustomValueBean.getDescription() );

        customFields.add( customFieldView );
      }
      cumulativeInfoView.setCustomFields( customFields );
      cumulativeInfoList.add( cumulativeInfoView );
    }

    cumulativeInfoTableDataViewBean.setCumulativeInfoList( cumulativeInfoList );

    super.writeAsJsonToResponse( cumulativeInfoTableDataViewBean, response );
  }

  public ActionForward fetchEligiblePromotions( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    EligibleNominationPromotionListViewObject jsonResponse = new EligibleNominationPromotionListViewObject();

    Map<String, Object> output = getNominationClaimService().getEligibleNominationPromotionsForApprover( UserManager.getUserId() );
    List<EligibleNominationPromotionValueObject> nomPromos = (List<EligibleNominationPromotionValueObject>)output.get( "p_out_promotion_detail" );

    for ( EligibleNominationPromotionValueObject vo : nomPromos )
    {
      jsonResponse.addPromotionView( new EligibleNominationPromotionViewObject( vo.getPromoId(), vo.getName(), null, null, null ) );
    }

    writeAsJsonToResponse( jsonResponse, response, ContentType.JSON );

    return null;
  }

  public ActionForward extractPdf( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationsApprovalPageForm pageForm = (NominationsApprovalPageForm)form;

    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_TIME_PERIODS ) );

    Map<String, Object> clientState = ClientStateUtils.getClientStateMap( request );
    Long promotionId = ClientStateUtils.getParameterAsLong( clientState, "promotionId" );

    pageForm.setPromotionId( promotionId );
    pageForm.setDateStart( ClientStateUtils.getParameterValue( request, clientState, "startDate" ) );
    pageForm.setDateEnd( ClientStateUtils.getParameterValue( request, clientState, "endDate" ) );
    Object level = ClientStateUtils.getParameterValueAsObject( request, clientState, "levelNumber" );
    if ( level instanceof Integer )
    {
      Integer levelId = (Integer)ClientStateUtils.getParameterValueAsObject( request, clientState, "levelNumber" );
      pageForm.setLevelId( new Long( levelId ) );
    }
    else if ( level instanceof Long )
    {
      pageForm.setLevelId( (Long)ClientStateUtils.getParameterValueAsObject( request, clientState, "levelNumber" ) );
    }
    pageForm.setCumulativeNomination( (Boolean)ClientStateUtils.getParameterValueAsObject( request, clientState, "isCumulativeNomination" ) );
    pageForm.setClaimIds( ClientStateUtils.getParameterValue( request, clientState, "claimIds" ) );

    ApprovalsNominationListExportBean exportBean = new ApprovalsNominationListExportBean();
    exportBean.setExportPdfList( buildNominationsApprovablesList( pageForm, false ) );
    exportBean.setSubmissionStartDate( pageForm.getDateStart() );
    exportBean.setSubmissionEndDate( pageForm.getDateEnd() );
    exportBean.setNodeService( getNodeService() );
    exportBean.extractAsPdf( getPromotionService().getPromotionByIdWithAssociations( promotionId, arc ), response, "nomination" );

    return null;
  }

  public ActionForward extractCsv( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> parameters = new HashMap<String, Object>();
    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      parameters = ClientStateSerializer.deserialize( clientState, password );
    }

    Map<String, Object> resultMap = getNominationClaimService().getNominationsApprovalPageExtractCsvData( parameters );

    ApprovalsNominationListExportBean exportBean = new ApprovalsNominationListExportBean();
    exportBean.extractAsCsv( response, resultMap );

    return null;
  }

  /**
   * Ajax call to request more budget when there might not be enough to approve everything in the list. 
   * Sends an email to the budget approver. 
   * FE does not expect a response.
   */

  public ActionForward requestMoreBudget( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationsApprovalPageForm nominationsApprovalPageForm = (NominationsApprovalPageForm)form;

    AssociationRequestCollection promotionARC = new AssociationRequestCollection();
    promotionARC.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOM_BUDGET_APPROVER ) );
    promotionARC.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOM_DEFAULT_APPROVER ) );
    promotionARC.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_PROMOTION_BUDGET_MASTER ) );
    NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionByIdWithAssociations( nominationsApprovalPageForm.getPromotionId(), promotionARC );
    PromotionAwardsType awardType = PromotionAwardsType.lookup( nominationsApprovalPageForm.getAwardType() );
    BigDecimal amountRequested = new BigDecimal( nominationsApprovalPageForm.getBudgetIncrease() );
    AssociationRequestCollection claimApproverARC = new AssociationRequestCollection();
    claimApproverARC.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    claimApproverARC.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
    Participant claimApprover = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), claimApproverARC );
    String claimApproverTimeZoneId = UserManager.getTimeZoneID();

    // Must be cash or points
    if ( awardType == null || ! ( awardType.isCashAwardType() || awardType.isPointsAwardType() ) )
    {
      return null;
    }

    // Save last request date
    if ( awardType.isCashAwardType() )
    {
      promotion.setLastCashBudgetRequestDate( new Date() );
    }
    else if ( awardType.isPointsAwardType() )
    {
      promotion.setLastPointBudgetRequestDate( new Date() );
    }
    getPromotionService().savePromotion( promotion );

    // Send mailing immediately. Fun fact, 'processMailing' explicitly documents not to use it
    // directly. But plenty of stuff does.
    Mailing mailing = getMailingService().buildNominationRequestMoreBudgetMailing( promotion, awardType, claimApprover, claimApproverTimeZoneId, amountRequested );
    mailing = getMailingService().submitMailingWithoutScheduling( mailing, null );
    getMailingService().processMailing( mailing.getId() );

    return null;
  }

  public WebErrorMessageList getMessageFromServiceErrors( List<ServiceError> serviceErrors )
  {

    WebErrorMessageList messageList = new WebErrorMessageList();
    WebErrorMessage msg = null;

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

      msg = new WebErrorMessage();
      msg.setText( errorMessage );
      WebErrorMessage.addErrorMessage( msg );
      messageList.getMessages().add( msg );

    }

    return messageList;
  }
private boolean isTeam(NominationClaim nc){
	return StringUtils.isNotEmpty( nc.getTeamName());
}
  // Client customization for WIP 58122
  private static CokeClientService getCokeClientService()
  {
    return (CokeClientService)getService( CokeClientService.BEAN_NAME );
  }
  private static Comparator<TccNomLevelPayout> ASCE_COMPARATOR_LEVEL_PAYOUTS = new Comparator<TccNomLevelPayout>()
  {
    public int compare( TccNomLevelPayout c1, TccNomLevelPayout c2 )
    {
      return c1.getLevelDescription().compareTo( c2.getLevelDescription() );
    }
  }; 

  private NominationClaimService getNominationClaimService()
  {
    return (NominationClaimService)getService( NominationClaimService.BEAN_NAME );
  }

  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private static ClaimGroupService getClaimGroupService()
  {
    return (ClaimGroupService)getService( ClaimGroupService.BEAN_NAME );
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private static NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  public MTCVideoService getMTCVideoService()
  {
    return (MTCVideoService)getService( MTCVideoService.BEAN_NAME );
  }

  public NominationPromotionService getNominationPromotionService()
  {
    return (NominationPromotionService)getService( NominationPromotionService.BEAN_NAME );
  }

  private CashCurrencyService getCashCurrencyService()
  {
    return (CashCurrencyService)getService( CashCurrencyService.BEAN_NAME );
  }

}

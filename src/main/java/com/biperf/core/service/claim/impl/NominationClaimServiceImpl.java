
package com.biperf.core.service.claim.impl;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.NominationClaimDAO;
import com.biperf.core.dao.claim.hibernate.NominationClaimQueryConstraint;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.ParticipantGroupDAO;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.claim.CalculatorResponse;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.NominationClaimBehaviors;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.client.TcccClaimFile;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationAwardSpecifierType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantGroup;
import com.biperf.core.domain.participant.ParticipantGroupDetails;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.service.claim.CalculatorResponseBean;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.claim.RecognitionClaimRecipient;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.mtc.MTCService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.RandomNumberStrategy;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CumulativeInfoTableDataValueBean;
import com.biperf.core.value.NominationInProgressModuleBean;
import com.biperf.core.value.NominationsApprovalPageDataValueBean;
import com.biperf.core.value.NominationsApprovalPageTableValueBean;
import com.biperf.core.value.PendingNominationsApprovalMainValueBean;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;
import com.biperf.core.value.nomination.NominationStepWizard;
import com.biperf.core.value.nomination.NominationSubmitDataAttachmentValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsParticipantDataValueBean.ParticipantValueBean;
import com.biperf.core.value.participant.ParticipantGroupList;
import com.biperf.core.value.participant.ParticipantGroupList.Group;
import com.objectpartners.cms.util.CmsResourceBundle;

public class NominationClaimServiceImpl implements NominationClaimService
{
  private static final String INDIVIDUAL_AWARD = "individual";
  private ClaimService claimService;
  private ClaimGroupService claimGroupService;
  private NominationClaimDAO nominationClaimDAO;
  private ClaimDAO claimDAO;
  private PromotionService promotionService;
  private ParticipantService participantService;
  private NodeDAO nodeDAO;
  private UserService userService;
  private CalculatorService calculatorService;
  private MultimediaService multimediaService;
  private ParticipantGroupDAO participantGroupDAO;
  private RandomNumberStrategy randomNumberStrategy;
  private CashCurrencyService cashCurrencyService;
  @Autowired
  private MTCService mtcService;
  @Autowired
  private SystemVariableService systemVariableService;
  @Autowired
  private UserCharacteristicService userCharacteristicService;
  
  public  final String DIVISION_KEY="Division Key";

  @SuppressWarnings( "unused" )
  private ClaimFormDefinitionService claimFormDefinitionService;

  @Override
  public boolean inProgressNominationClaimExist( Long submitterId )
  {
    if ( submitterId == null )
    {
      return false;
    }

    int count = nominationClaimDAO.inProgressCount( submitterId );

    return count > 0 ? true : false;

  }

  private NominationClaimQueryConstraint getInProgressNominationClaimQryConstraint( Long submitterId )
  {
    NominationClaimQueryConstraint qryConstrint = new NominationClaimQueryConstraint();
    qryConstrint.setSubmitterId( submitterId );
    qryConstrint.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.INCOMPLETE ) );
    return qryConstrint;
  }

  @Override
  public int getInProgressNominationClaimsCount( Long submitterId )
  {
    if ( submitterId == null )
    {
      return 0;
    }
    NominationClaimQueryConstraint qryConstrint = getInProgressNominationClaimQryConstraint( submitterId );
    return claimService.getClaimListCount( qryConstrint );
  }

  @Override
  public Map<String, Object> getNominationClaimsInProgress( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getNominationClaimsInProgress( parameters );
  }

  @Override
  public int getNominationClaimsSubmittedCountByPeriod( Long timePeriodId, Long submitterId )
  {
    return nominationClaimDAO.getClaimsSubmittedCountByPeriod( timePeriodId, submitterId );
  }

  @Override
  public int getNominationClaimsSubmittedCount( Long timePeriodId, Long submitterId, Long recipientId )
  {
    return nominationClaimDAO.getClaimsSubmittedCountByPeriodAndNominee( timePeriodId, submitterId, recipientId );
  }

  @Override
  public void saveNominationClaim( Claim claim )
  {
    claimDAO.saveClaim( claim );
  }

  @Override
  public RecognitionClaimSubmissionResponse stepNominating( RecognitionClaimSubmission submission, Long groupId ) throws ServiceErrorException
  {
    RecognitionClaimSubmissionResponse response = new RecognitionClaimSubmissionResponse();
    List<ServiceError> errors = new ArrayList<ServiceError>();

    NominationSubmitDataPromotionValueBean submissionPromotion = submission.getNomSubmitDataPromotionValueBean();
    NominationPromotion promotion = (NominationPromotion)promotionService.getPromotionById( submissionPromotion.getId() );
    NominationClaim claim = null;

    if ( submission.getClaimId() != null && submission.getClaimId() != 0 )
    { // work for draft claim
      claim = (NominationClaim)claimService.getClaimById( submission.getClaimId() );
    }
    else
    { // work for new claim
      claim = buildNomClaimBasics( promotion, submission );
    }

    Node node = nodeDAO.getNodeById( submission.getNodeId() );
    claim.setNode( node );
    claim.setAwardGroupType( NominationAwardGroupType.lookup( submissionPromotion.getIndividualOrTeam().toLowerCase() ) );
    claim.setStepNumber( NominationStepWizard.NOMINATING.getId() );
    Claim savedClaim = claimDAO.saveClaim( claim );
    response.setClaimId( savedClaim.getId() );
    return response;
  }

  @Override
  public RecognitionClaimSubmissionResponse stepNominee( RecognitionClaimSubmission submission, Long groupId ) throws ServiceErrorException
  {
    RecognitionClaimSubmissionResponse response = new RecognitionClaimSubmissionResponse();
    List<ServiceError> errors = new ArrayList<ServiceError>();

    NominationSubmitDataPromotionValueBean submissionPromotion = submission.getNomSubmitDataPromotionValueBean();
    NominationPromotion promotion = (NominationPromotion)promotionService.getPromotionById( submissionPromotion.getId() );
    NominationClaim claim = null;
    ClaimGroup claimGroup = null;

    // validation starts here -------------
    // Time period validation
    if ( submissionPromotion.isIndividualSelectedByNominator() )
    {
      errors.addAll( getNomPromoService().validateNomineeCountByTimePeriod( submission ) );
    }

    // Team name length validation
    if ( !submission.isDraft() && submissionPromotion.isTeamSelectedByNominator() )
    {
      if ( submissionPromotion.getTeamName() == null || submissionPromotion.getTeamName().length() == 0 || submissionPromotion.getTeamName().length() > 50 )
      {
        errors.add( new ServiceError( "promotion.nomination.submit.TEAM_NAME_SIZE_ERROR" ) );
      }
    }

    // Group name validation (must exist and not be way way too long. Like why is this column so
    // large)
    if ( submissionPromotion.isSaveTeamAsGroup() )
    {
      if ( StringUtil.isEmpty( submissionPromotion.getGroupName() ) )
      {
        errors.add( new ServiceError( "promotion.nomination.submit.NO_GROUP_NAME_ERROR" ) );
      }
      else if ( submissionPromotion.getGroupName().length() > 4000 )
      {
        errors.add( new ServiceError( "promotion.nomination.submit.GROUP_NAME_TOO_LONG" ) );
      }
    }

    if ( !CollectionUtils.isEmpty( errors ) )
    {
      response.addErrors( errors );
      throw new ServiceErrorExceptionWithRollback( errors );
    }
    // validation end here -------------

    if ( submission.getClaimId() != null && submission.getClaimId() != 0 )
    { // work for draft claim
      claim = (NominationClaim)claimService.getClaimById( submission.getClaimId() );
    }
    else
    { // work for new claim
      claim = buildNomClaimBasics( promotion, submission );
    }

    populateTimePeriod( claim, submission.getPromotionId(), UserManager.getTimeZoneID(), UserManager.getUserId() );
    populateClaimRecipients( submission, promotion, claim );

    if ( submission.getParticipants().size() > 1 ) // Team based
    {
      claim.setTeamName( submissionPromotion.getTeamName() );
      claim.setTeamId( claim.getTeamId() != null ? claim.getTeamId() : claimDAO.getNextTeamId() );
      claim.setAwardGroupType( NominationAwardGroupType.lookup( NominationAwardGroupType.TEAM ) );
      if ( submissionPromotion.isSaveTeamAsGroup() )
      {
        saveParticipantGroup( submission, groupId );
      }

    }
    else // Individual
    {
      com.biperf.core.value.nomination.NominationsParticipantDataValueBean.ParticipantValueBean indivitualRecipint = submission.getParticipants().get( 0 );
      Long teamId = null;
      if ( NominationEvaluationType.isCumulative( promotion.getEvaluationType() ) )
      {
        teamId = claimDAO.getExistingTeamIdForClaim( promotion.getId(), indivitualRecipint.getId() );
      }
      claim.setTeamId( teamId != null ? teamId : claimDAO.getNextTeamId() );
      claim.setAwardGroupType( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ) );
    }
    List<ParticipantValueBean> receipientList = submission.getParticipants();
    for ( ParticipantValueBean participantValueBean : receipientList )
    {
      ClaimRecipient claimRecipient = null;
      if ( !Objects.nonNull( claim.getClaimRecipients() ) )
      {
        claimRecipient = new ClaimRecipient();
        claimRecipient.setRecipient( participantService.getParticipantById( participantValueBean.getId() ) );
        claimRecipient.setAwardQuantity( new Long( participantValueBean.getAwardQuantity() ) );
        claimRecipient.setNode( nodeDAO.getNodeById( submission.getNodeId() ) );
        claim.addClaimRecipient( claimRecipient );

      }
    }
    Node node = nodeDAO.getNodeById( submission.getNodeId() );
    claim.setNode( node );
    claim.setAwardGroupType( NominationAwardGroupType.lookup( submissionPromotion.getIndividualOrTeam().toLowerCase() ) );
    claim.setStepNumber( NominationStepWizard.NOMINEEE.getId() );
    Claim savedClaim = claimDAO.saveClaim( claim );
    response.setClaimId( savedClaim.getId() );
    return response;
  }

  private ClaimGroup buildClaimGroup( RecognitionClaimSubmission submission, NominationPromotion promotion, NominationClaim claim ) throws ServiceErrorException
  {
    ClaimGroup claimGroup = new ClaimGroup();

    ParticipantValueBean nominee = null;
    if ( submission.getParticipants() == null || submission.getParticipants().size() != 1 )
    {
      throw new ServiceErrorException( "There may only be one nominee for a cumulative promotion" );
    }
    nominee = submission.getParticipants().get( 0 );

    NominationSubmitDataPromotionValueBean nominationVB = submission.getNomSubmitDataPromotionValueBean();
    String awardType = nominationVB.getAwardType();

    Participant pax = participantService.getParticipantById( UserManager.getUserId() );
    String paxCurrencyCode = pax.getPrimaryAddress().getAddress().getCountry().getCurrencyCode();

    claimGroup.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
    claimGroup.setParticipant( participantService.getParticipantById( nominee.getId() ) );
    claimGroup.setPromotion( promotion );
    claimGroup.setOpen( claim.getOpen() );
    claimGroup.setApprovalRound( claim.getApprovalRound() );
    claimGroup.setLastApprovalNode( claim.getLastApprovalNode() );
    claimGroup.setSerialId( GuidUtils.generateGuid() );
    if ( nominee.getNodes() != null && !nominee.getNodes().isEmpty() )
    {
      claimGroup.setNode( nodeDAO.getNodeById( new Long( nominee.getNodes().get( 0 ).getId() ) ) );
    }

    if ( promotion.isAwardActive() )
    {
      NominationPromotionLevel level = promotion.getNominationLevels().iterator().next();
      if ( level.getAwardPayoutType().isPointsAwardType() )
      {
        if ( "pointsFixed".equalsIgnoreCase( awardType ) )
        {
          claimGroup.setAwardQuantity( StringUtil.isNullOrEmpty( nominationVB.getAwardFixed() ) ? null : Long.valueOf( nominationVB.getAwardFixed() ) );
        }
        else if ( "finalLevel".equals( promotion.getPayoutLevel() ) && "pointsRange".equalsIgnoreCase( awardType ) )
        {
          claimGroup.setAwardQuantity( nominee.getConvertedAwardQuantity() == null ? null : nominee.getConvertedAwardQuantity().longValue() );
        }
      }

      else if ( level.getAwardPayoutType().isCashAwardType() )
      {
        BigDecimal convertedtoUSD = null;

        if ( "pointsFixed".equalsIgnoreCase( awardType ) && nominationVB.getAwardFixed() != null )
        {
          convertedtoUSD = cashCurrencyService.convertCurrency( paxCurrencyCode, "USD", new BigDecimal( nominationVB.getAwardFixed() ), null );
          claimGroup.setCashAwardQuantity( convertedtoUSD );
        }
        else if ( "finalLevel".equals( promotion.getPayoutLevel() ) && "pointsRange".equalsIgnoreCase( awardType ) && nominee.getConvertedAwardQuantity() != null )
        {
          convertedtoUSD = cashCurrencyService.convertCurrency( paxCurrencyCode, "USD", nominee.getConvertedAwardQuantity(), null );
          claimGroup.setCashAwardQuantity( convertedtoUSD );
        }
      }
    }

    return claimGroup;
  }

  private void populateClaimRecipients( RecognitionClaimSubmission submission, NominationPromotion promotion, NominationClaim claim )
  {

    List<ClaimRecipient> mergeRecipientList = buildIndividualClaimRecipient( promotion, submission.getParticipants(), submission );

    Map<Long, ClaimRecipient> existingRecipientMap = claim.getClaimPaxRecipientsMap();
    List<Long> mergePaxIdList = new ArrayList<Long>();

    for ( ClaimRecipient mergeRecipient : mergeRecipientList )
    {
      mergePaxIdList.add( mergeRecipient.getRecipient().getId() );
      ClaimRecipient existing = existingRecipientMap.get( mergeRecipient.getRecipient().getId() );

      if ( existing != null )
      {
        existing.setAwardQuantity( mergeRecipient.getAwardQuantity() );
        existing.setCashAwardQuantity( mergeRecipient.getCashAwardQuantity() );
      }
      else
      {
        claim.addClaimRecipient( mergeRecipient );
      }

    }

    Set<Long> existingPaxIds = existingRecipientMap.keySet();

    for ( Long paxId : existingPaxIds )
    {
      if ( !mergePaxIdList.contains( paxId ) )
      {
        claim.getClaimRecipients().remove( existingRecipientMap.get( paxId ) );
      }
    }

  }

  private void saveParticipantGroup( RecognitionClaimSubmission submission, Long groupId )
  {
    ParticipantGroup participantGroup = null;
    String groupName = submission.getNomSubmitDataPromotionValueBean().getGroupName();
    Long userId = UserManager.getUserId();
    ParticipantGroupList participantGroupList = participantService.getGroupList( userId );
    List<Group> groups = participantGroupList.getGroups();
    boolean createNewGroup = true;
    for ( Group group : groups )
    {
      if ( group.getName().equalsIgnoreCase( groupName ) )
      {
        // Override the passed in group ID at this point. We've got a matching group, we want to
        // update it.
        groupId = group.getId();

        createNewGroup = false;
        break;
      }
    }

    List<ParticipantValueBean> recipients = submission.getParticipants();
    if ( createNewGroup )
    {
      participantGroup = createNewGroup( recipients, groupName );
    }
    else
    {
      if ( groupId != null )
      {
        ParticipantGroup group = participantGroupDAO.find( groupId );
        Set<ParticipantGroupDetails> participantGroupDetailsList = group.getGroupDetails();
        participantGroupDetailsList.clear();
        participantGroup = editGroup( recipients, group );
      }
    }
    participantGroupDAO.saveOrUpdate( participantGroup );
  }

  private ParticipantGroup createNewGroup( List<ParticipantValueBean> recipients, String groupName )
  {
    ParticipantGroup group = new ParticipantGroup();
    group.setDateCreated( new Date() );
    group.setGroupName( groupName );
    group.setGroupCreatedBy( participantService.getParticipantById( UserManager.getUserId() ) );

    return createParticipantGroup( recipients, group );
  }

  private ParticipantGroup editGroup( List<ParticipantValueBean> recipients, ParticipantGroup group )
  {
    return createParticipantGroup( recipients, group );
  }

  private ParticipantGroup createParticipantGroup( List<ParticipantValueBean> recipients, ParticipantGroup group )
  {
    ParticipantGroupDetails detail;
    for ( ParticipantValueBean recipient : recipients )
    {
      detail = new ParticipantGroupDetails();
      detail.setGroup( group );
      detail.setParticipant( participantService.getParticipantById( recipient.getId() ) );
      group.addDetails( detail );
    }
    return group;
  }

  private void populateTimePeriod( NominationClaim claim, Long pomoId, String timeZone, Long submitterId )
  {
    NominationPromotionService nomPromoService = getNomPromoService();
    claim.setTimPeriod( nomPromoService.getCurrentTimePeriod( pomoId, timeZone, submitterId ) );
  }

  private List<ClaimRecipient> buildIndividualClaimRecipient( NominationPromotion promotion, List<ParticipantValueBean> nomineesList, RecognitionClaimSubmission submission )
  {
    List<ClaimRecipient> recipientsList = new ArrayList<ClaimRecipient>();
    NominationSubmitDataPromotionValueBean nominationVB = submission.getNomSubmitDataPromotionValueBean();
    String awardType = nominationVB.getAwardType();

    Participant pax = participantService.getParticipantById( UserManager.getUserId() );
    String paxCurrencyCode = pax.getPrimaryAddress().getAddress().getCountry().getCurrencyCode();

    if ( promotion.isAwardActive() )
    {
      NominationPromotionLevel level = promotion.getNominationLevels().iterator().next();
      for ( ParticipantValueBean nominee : nomineesList )
      {
        ClaimRecipient claimRecipient = new ClaimRecipient();
        claimRecipient.setRecipient( participantService.getParticipantById( nominee.getId() ) );

        if ( level.getAwardPayoutType().isPointsAwardType() )
        {
          if ( claimRecipient.getRecipient().getOptOutAwards() )
          {
            claimRecipient.setAwardQuantity( 0L );
          }
          else if ( "pointsFixed".equalsIgnoreCase( awardType ) )
          {
            claimRecipient.setAwardQuantity( StringUtil.isNullOrEmpty( nominationVB.getAwardFixed() ) ? null : Long.valueOf( nominationVB.getAwardFixed() ) );
          }
          else if ( "finalLevel".equals( promotion.getPayoutLevel() ) && "pointsRange".equalsIgnoreCase( awardType ) )
          {
            claimRecipient.setAwardQuantity( nominee.getConvertedAwardQuantity() == null ? null : nominee.getConvertedAwardQuantity().longValue() );
          }
          else if ( "finalLevel".equals( promotion.getPayoutLevel() ) && "calculated".equalsIgnoreCase( awardType ) )
          {
            claimRecipient.setAwardQuantity( nominee.getConvertedAwardQuantity() == null ? null : nominee.getConvertedAwardQuantity().longValue() );
          }

          if ( nominee.getNodes() != null && !nominee.getNodes().isEmpty() )
          {
            claimRecipient.setNode( nodeDAO.getNodeById( new Long( nominee.getNodes().get( 0 ).getId() ) ) );
          }
          claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
          recipientsList.add( claimRecipient );
        }

        else if ( level.getAwardPayoutType().isCashAwardType() )
        {
          BigDecimal convertedtoUSD = null;

          if ( claimRecipient.getRecipient().getOptOutAwards() )
          {
            claimRecipient.setCashAwardQuantity( BigDecimal.ZERO );
          }
          else if ( "pointsFixed".equalsIgnoreCase( awardType ) && nominationVB.getAwardFixed() != null )
          {
            convertedtoUSD = cashCurrencyService.convertCurrency( paxCurrencyCode, "USD", new BigDecimal( nominationVB.getAwardFixed() ), null );
            claimRecipient.setCashAwardQuantity( convertedtoUSD.setScale( 8, BigDecimal.ROUND_HALF_DOWN ) );
          }
          else if ( "finalLevel".equals( promotion.getPayoutLevel() ) && "pointsRange".equalsIgnoreCase( awardType ) && nominee.getConvertedAwardQuantity() != null )
          {
            convertedtoUSD = cashCurrencyService.convertCurrency( paxCurrencyCode, "USD", nominee.getConvertedAwardQuantity(), null );
            claimRecipient.setCashAwardQuantity( convertedtoUSD.setScale( 8, BigDecimal.ROUND_HALF_DOWN ) );
          }
        }

        if ( nominee.getNodes() != null && !nominee.getNodes().isEmpty() )
        {
          claimRecipient.setNode( nodeDAO.getNodeById( new Long( nominee.getNodes().get( 0 ).getId() ) ) );
        }
        claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
        recipientsList.add( claimRecipient );
      }
    }
    else
    {
      for ( ParticipantValueBean nominee : nomineesList )
      {
        ClaimRecipient claimRecipient = new ClaimRecipient();
        claimRecipient.setRecipient( participantService.getParticipantById( nominee.getId() ) );
        if ( nominee.getNodes() != null && !nominee.getNodes().isEmpty() )
        {
          claimRecipient.setNode( nodeDAO.getNodeById( new Long( nominee.getNodes().get( 0 ).getId() ) ) );
        }
        claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
        recipientsList.add( claimRecipient );
      }
    }
    return recipientsList;
  }

  private ClaimRecipient buildTeamClaimRecipient( AbstractRecognitionPromotion promotion )
  {
    ClaimRecipient claimRecipient = new ClaimRecipient();

    claimRecipient.setAwardQuantity( getNominationAwardAmount( (NominationPromotion)promotion ) );
    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );

    return claimRecipient;
  }

  private Long getNominationAwardAmount( NominationPromotion promotion )
  {
    Long awardAmount = null;

    if ( promotion.isAwardAmountTypeFixed() )
    {
      awardAmount = promotion.getAwardAmountFixed();
    }
    else
    {
      NominationAwardSpecifierType awardSpecifier = promotion.getAwardSpecifierType();
    }

    return awardAmount;
  }
  @Override
  public Set<ProductClaimParticipant> buildTeamMembers( RecognitionClaimSubmission submission )
  {
    Set<ProductClaimParticipant> teamMembers = new LinkedHashSet<ProductClaimParticipant>();

    for ( com.biperf.core.value.nomination.NominationsParticipantDataValueBean.ParticipantValueBean recipient : submission.getParticipants() )
    {
      ProductClaimParticipant teamMember = new ProductClaimParticipant();

      Participant participant = participantService.getParticipantById( recipient.getId() );
      teamMember.setParticipant( participant );

      List<com.biperf.core.value.nomination.NominationsParticipantDataValueBean.NodeValueBean> nodes = recipient.getNodes();

      if ( nodes != null && nodes.size() > 0 )
      {
        Node node = nodeDAO.getNodeById( nodes.get( 0 ).getId().longValue() );
        teamMember.setNode( node );
      }
      teamMember.setAwardQuantity( recipient.getConvertedAwardQuantity() == null ? null : recipient.getConvertedAwardQuantity().longValue() );
      teamMembers.add( teamMember );
    }
    return teamMembers;
  }

  @Override
  public RecognitionClaimSubmissionResponse submitClaim( RecognitionClaimSubmission submission, NominationClaimStatusType statusType, String individualOrTeam, int stepNumber )
      throws ServiceErrorException
  {
    RecognitionClaimSubmissionResponse response = new RecognitionClaimSubmissionResponse();
    Long claimId = submission.getClaimId();
    List<ServiceError> errors = new ArrayList<ServiceError>();
    // NominationClaim claim = (NominationClaim)claimService.getClaimById( claimId );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    NominationClaim claim = (NominationClaim)claimService.getClaimByIdWithAssociations( claimId, associationRequestCollection );

    NominationPromotion nomPromo = (NominationPromotion)claim.getPromotion();
    if ( submission.getCertificateId() != null && nomPromo.isCertificateActive() && nomPromo.isOneCertPerPromotion() )
    {
      PromotionCert promotionCert = (PromotionCert)nomPromo.getPromotionCertificates().iterator().next();
      claim.setCertificateId( Long.valueOf( promotionCert.getCertificateId() ) );
    }
 // Client customization for WIP #39189 starts
    Set<TcccClaimFile> claimFiles = null;
    if ( nomPromo.isNominationPromotion() && ( (NominationPromotion)nomPromo ).isEnableFileUpload() )
    {
      claimFiles = buildClaimFiles( submission );
    }
    // Client customization for WIP #39189 ends
    if ( nomPromo.isNominationPromotion() && ( (NominationPromotion)nomPromo ).isTeam() )
    {
     // ClaimRecipient claimRecipient = buildTeamClaimRecipient( nomPromo );
     // claim.addClaimRecipient( claimRecipient );
      Set<ProductClaimParticipant> teamMembers = buildTeamMembers( submission );
      for ( ProductClaimParticipant teamMember : teamMembers )
      {
    	  claim.addTeamMember( teamMember );
      }
     
    }
    List<ServiceError> validationErrors = validateParticipants( claim, individualOrTeam );
    if ( !CollectionUtils.isEmpty( validationErrors ) )
    {
      errors.addAll( validationErrors );
      throw new ServiceErrorExceptionWithRollback( validationErrors );
    }
    if ( statusType != null )
    {
      claim.setNominationStatusType( statusType );
    }
    claim.setStepNumber( stepNumber );
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
    claim.setSubmissionDate( referenceDate );
    claim.setNode( getNodeService().getNodeById( submission.getNodeId() ) );
    claim.setSubmitNomination( true );   
    //addDivisionToClaim( claim, individualOrTeam );
    Claim savedClaim = claimService.saveClaim( claim, submission.getClaimFormStepId(), null, false, true );
    response.setClaimId( savedClaim.getId() );
    return response;
  }
    
  private void addDivisionToClaim( NominationClaim claim )
  {
    List<ServiceError> validationErrors = new ArrayList<ServiceError>();
    Set<ClaimRecipient> claimRecipients = claim.getClaimRecipients();
    boolean userInactive = false;
    String[] promoIds = systemVariableService.getPropertyByName( systemVariableService.COKE_DIV_KEY_PROMO_IDS ).getStringVal().split( "," );
    boolean divison_promotion = false;

    if ( claimRecipients != null && claimRecipients.size() > 0 )
    {
      if ( claimRecipients.iterator().hasNext() )
      {

        for ( String promoId : promoIds )
        {
          if ( promoId.equals( claim.getPromotion().getId().toString() ) )
          {
            List<Characteristic> characteristicList = userCharacteristicService.getAllCharacteristics();
            for ( Characteristic charc : characteristicList )
            {
              if ( charc.getCharacteristicName().equalsIgnoreCase( DIVISION_KEY ) && claim != null )
              {
                String recipientDivisionValue = userCharacteristicService.getCharacteristicValueByUserAndCharacterisiticId( claimRecipients.iterator().next().getRecipient().getId(), charc.getId() );
                String SubmitterDivisionValue = userCharacteristicService.getCharacteristicValueByUserAndCharacterisiticId( claim.getSubmitter().getId(), charc.getId() );
                claim.setRecieverDivisonKey( recipientDivisionValue );
                claim.setGiverDevisionKey( SubmitterDivisionValue );
              }
            }
          }
        }

      }

    }
  }

//Client customization for WIP #39189 starts
 private Set<TcccClaimFile> buildClaimFiles( RecognitionClaimSubmission submission )
 {
   Set<TcccClaimFile> claimFiles = new LinkedHashSet<TcccClaimFile>();
   if ( submission.getNomSubmitDataPromotionValueBean() != null && submission.getNomSubmitDataPromotionValueBean().getNominationLinks() != null )
   {
     for ( NominationSubmitDataAttachmentValueBean claimFile : submission.getNomSubmitDataPromotionValueBean().getNominationLinks() )
     {
       TcccClaimFile file = new TcccClaimFile();
       file.setFileName( claimFile.getFileName() );
       file.setFileUrl( StringUtil.isNullOrEmpty( claimFile.getNominationLink() ) ? claimFile.getNominationUrl() : claimFile.getNominationLink() );
       claimFiles.add( file );
//       log.error( "Lenovo Claim File Details: " + file.toString() );
       //String nominationFileUrl = ;
       // An attached URL needs to have http before we save it, one way or another
       // if ( !nominationFileUrl.startsWith( "http://" ) || !nominationFileUrl.startsWith( "https://" ) )
       //  nominationFileUrl = "http://" + nominationFileUrl;
     }
   }
   return claimFiles;
 }
 // Client customization for WIP #39189 ends  
    

  @Override
  public void stepWhy( RecognitionClaimSubmission submission, boolean isDraft ) throws ServiceErrorException
  {
    Long claimId = submission.getClaimId();
    List<ClaimElement> claimElements = claimService.getClaimElementDomain( submission.getCustomElements() );
    List<ServiceError> errors = new ArrayList<ServiceError>();
    NominationClaim claim = (NominationClaim)claimService.getClaimById( claimId );
    List<ServiceError> validationErrors = claimService.validateClaimElements( claimElements, claim.getNode(), claim.getPromotion(), 0, isDraft );
    if ( !CollectionUtils.isEmpty( validationErrors ) )
    {
      errors.addAll( validationErrors );
      throw new ServiceErrorExceptionWithRollback( validationErrors );
    }
    
 // Client customization for WIP #39189 starts
    Set<TcccClaimFile> claimFiles = buildClaimFiles( submission );
    if ( claimFiles.size() > 0 )
    {
      for ( TcccClaimFile claimFile : claimFiles )
      {
        claim.addClaimFile( claimFile );
      }
    }
    // Client customization for WIP #39189 ends

    // attach form element with claim
    claimService.populateClaimElements( claim, submission.getCustomElements() );

    claim.setSubmitterComments( submission.getNomSubmitDataPromotionValueBean().getComments() );
    claim.setSubmitterCommentsLanguageType( userService.getPreferredLanguageFor( claim.getSubmitter() ) );
    claim.setHidePublicRecognition( submission.getNomSubmitDataPromotionValueBean().isPrivateNomination() );
    claim.setWhyAttachmentName( submission.getNomSubmitDataPromotionValueBean().getFileName() );
    claim.setWhyAttachmentUrl( null );
    claim.setStepNumber( NominationStepWizard.WHY.getId() );

   /* if ( !StringUtils.isEmpty( submission.getNomSubmitDataPromotionValueBean().getNominationUrl() ) )
    {
      // An attached URL needs to have http before we save it, one way or another
      String nominationUrl = submission.getNomSubmitDataPromotionValueBean().getNominationUrl();
      if ( !nominationUrl.startsWith( "http://" ) )
      {
        nominationUrl = "http://" + nominationUrl;
      }
      claim.setWhyAttachmentUrl( nominationUrl );
    }

    if ( !StringUtils.isEmpty( submission.getNomSubmitDataPromotionValueBean().getNominationLink() ) )
    {
      claim.setWhyAttachmentUrl( submission.getNomSubmitDataPromotionValueBean().getNominationLink() );
    }*/
  }

  @SuppressWarnings( "unused" )
  private void addCalculatorResponse( RecognitionClaimRecipient recipint, NominationClaim newClaim )
  {
    for ( CalculatorResponseBean calculatorResponseBean : recipint.getCalculatorResponseBeans() )
    {
      CalculatorResponse calculatorResponse = new CalculatorResponse();

      CalculatorCriterion cc = calculatorService.getCalculatorCriterionByIdWithAssociations( calculatorResponseBean.getCriterionId(), null );
      calculatorResponse.setCriterion( cc );

      if ( cc.getCalculator().isWeightedScore() )
      {
        calculatorResponse.setCriterionWeight( new Integer( cc.getWeightValue() ) );
      }

      if ( calculatorResponseBean.getSelectedRating() != null )
      {
        CalculatorCriterionRating selectedRating = calculatorService.getCriterionRatingById( new Long( calculatorResponseBean.getSelectedRating() ) );
        calculatorResponse.setSelectedRating( selectedRating );
        calculatorResponse.setRatingValue( selectedRating.getRatingValue() );
        newClaim.addCalculatorResponse( calculatorResponse );
      }
    }
  }

  @Override
  public NominationClaim buildNomClaimBasics( NominationPromotion promotion, RecognitionClaimSubmission submission )
  {
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
    NominationClaim claim = new NominationClaim();
    claim.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.INCOMPLETE ) );
    claim.setSubmissionDate( referenceDate );
    claim.setPromotion( promotion );
    claim.setOpen( true );
    claim.setApprovalRound( 1L );

    claim.setSubmitter( participantService.getParticipantById( UserManager.getUserId() ) );
    if ( submission.getProxyUserId() != null )
    {
      claim.setProxyUser( userService.getUserById( submission.getProxyUserId() ) );
    }
    claim.setSource( submission.getSourceIfNotExitsDefaultToUnknown() );
    claim.setClaimNumber( getRandomNumberStrategy().getRandomizedClaimNumber() );
    return claim;
  }

  @Override
  public void stepBehavior( RecognitionClaimSubmission submission, boolean isEditMode )
  {
    NominationClaim nomClaim = (NominationClaim)claimService.getClaimById( submission.getClaimId() );

    if ( !isEditMode || nomClaim.getNominationClaimBehaviors().size() == 0 )
    {
      nomClaim.getNominationClaimBehaviors().clear();
      if ( submission.getNominationBehaviors() != null && submission.getNominationBehaviors().size() > 0 )
      {
        for ( NominationClaimBehaviors behavior : submission.getNominationBehaviors() )
        {
          nomClaim.addNominationClaimBehaviors( behavior );
        }
      }
    }
    else
    {
      Set<NominationClaimBehaviors> removedBehaviors = new HashSet<NominationClaimBehaviors>();

      if ( nomClaim.getNominationClaimBehaviors() != null && submission.getNominationBehaviors() != null )
      {
        for ( NominationClaimBehaviors existingBehavior : nomClaim.getNominationClaimBehaviors() )
        {
          if ( !submission.getNominationBehaviors().contains( existingBehavior ) )
          {
            removedBehaviors.add( existingBehavior );
          }
          else
          {
            nomClaim.addNominationClaimBehaviors( existingBehavior );
          }
        }

        nomClaim.getNominationClaimBehaviors().removeAll( removedBehaviors );
        submission.getNominationBehaviors().removeAll( removedBehaviors );

        for ( NominationClaimBehaviors behavior : submission.getNominationBehaviors() )
        {
          nomClaim.addNominationClaimBehaviors( behavior );
        }
      }
    }
    nomClaim.setStepNumber( NominationStepWizard.BEHAVIOUR.getId() );
    addDivisionToClaim( nomClaim );
    saveNominationClaim( nomClaim );
  }

  @Override
  public void stepEcard( RecognitionClaimSubmission submission )
  {
    NominationClaim nomClaim = (NominationClaim)claimService.getClaimById( submission.getClaimId() );

    // Update card information
    nomClaim.setCertificateId( submission.getCertificateId() );

    if ( submission.getCardId() != null )
    {
      nomClaim.setCard( multimediaService.getCardById( submission.getCardId() ) );
    }
    else
    {
      nomClaim.setCard( null );
    }

    nomClaim.setCardType( submission.getCardType() );
    nomClaim.setOwnCardName( submission.getOwnCardName() );
    UploadResponse uploadResponse = null;

    if ( Objects.nonNull( submission.getVideoUrl() ) )
    {
      try
      {

        uploadResponse = getMtcService().uploadVideo( new URL( submission.getVideoUrl() ) );
      }
      catch( ServiceErrorException e )
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      catch( JSONException e )
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      catch( MalformedURLException e )
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      nomClaim.setCardVideoUrl( submission.getVideoUrl() + ActionConstants.REQUEST_ID + uploadResponse.getRequestId() );
      nomClaim.setCard( null );// ( ActionConstants.REQUEST_ID +
                               // uploadResponse.getRequestId() );

      nomClaim.setCardVideoImageUrl( submission.getVideoImageUrl() + ActionConstants.REQUEST_ID + uploadResponse.getRequestId() );
    }
    else
    {
      nomClaim.setCardVideoUrl( submission.getVideoUrl() );
      nomClaim.setCardVideoImageUrl( submission.getVideoImageUrl() );

    }

    nomClaim.setDrawingDataUrl( submission.getDrawingDataUrl() );
    nomClaim.setStepNumber( NominationStepWizard.ECARD.getId() );

    claimDAO.saveClaim( nomClaim );
  }

  @Override
  public PendingNominationsApprovalMainValueBean getPendingNominationClaimsForApprovalByUser( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getPendingNominationClaimsForApprovalByUser( parameters );
  }

  @Override
  public void reverseWinnerStatus( Long claimGroupId, Long claimId )
  {
    // If claimGroupId is provided, do this for all of the claims in the group.
    // otherwise, only reverse the one given claim
    if ( claimGroupId != null && claimGroupId != 0 )
    {
      ClaimGroup claimGroup = claimGroupService.getClaimGroupById( claimGroupId );
      claimGroup.getClaims().forEach( ( nominationClaim ) -> reverseWinnerStatus( (NominationClaim)nominationClaim ) );

      claimGroup.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.NON_WINNER ) );
    }
    else
    {
      NominationClaim nominationClaim = (NominationClaim)claimService.getClaimById( claimId );
      reverseWinnerStatus( nominationClaim );
    }
  }

  private void reverseWinnerStatus( NominationClaim nominationClaim )
  {
    for ( ClaimRecipient recipient : nominationClaim.getClaimRecipients() )
    {
      recipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.NON_WINNER ) );
    }

    saveNominationClaim( nominationClaim );
  }

  @Override
  public Map<String, Object> getEligibleNominationPromotionsForApprover( Long userId )
  {
    return nominationClaimDAO.getEligibleNominationPromotionsForApprover( userId );
  }

  @Override
  public NominationsApprovalPageDataValueBean getNominationsApprovalPageData( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getNominationsApprovalPageData( parameters );
  }

  @Override
  public NominationsApprovalPageTableValueBean getNominationsApprovalPageTableData( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getNominationsApprovalPageTableData( parameters );
  }

  @Override
  public NominationInProgressModuleBean getInProgressNominationClaimAndPromotionId( Long userId )
  {
    return nominationClaimDAO.getInProgressNominationClaimAndPromotionId( userId );
  }

  private List<ServiceError> validateParticipants( NominationClaim claim, String individualOrTeam )
  {
    List<ServiceError> validationErrors = new ArrayList<ServiceError>();
    Set<ClaimRecipient> claimRecipients = claim.getClaimRecipients();
    boolean userInactive = false;

    if ( claimRecipients != null && claimRecipients.size() > 0 )
    {
      if ( individualOrTeam.equalsIgnoreCase( INDIVIDUAL_AWARD ) )
      {

        if ( claimRecipients.iterator().hasNext() )
        {
          if ( claimRecipients.iterator().next().getRecipient() != null && !claimRecipients.iterator().next().getRecipient().isActive() )
          {
            userInactive = true;
          }
        }
      }
      else
      {
        for ( ClaimRecipient recipient : claimRecipients )
        {
          if ( recipient.getRecipient().isActive() )
          {
            userInactive = false;
            break;
          }
        }
      }
    }

    if ( userInactive )
    {
      validationErrors.add( new ServiceError( CmsResourceBundle.getCmsBundle().getString( "promotion.nomination.submit.PARTICIPANT_INACTIVE_ERROR" ) ) );
    }
    return validationErrors;
  }

  @Override
  public void addWhyAttachmentUrlReference( Long claimId, String fileUrl, String fileName )
  {
    NominationClaim claim = (NominationClaim)claimService.getClaimById( claimId );
    claim.setWhyAttachmentUrl( fileUrl );
    claim.setWhyAttachmentName( fileName );
    claimDAO.saveClaim( claim );
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public MTCService getMtcService()
  {
    return mtcService;
  }

  public void setMtcService( MTCService mtcService )
  {
    this.mtcService = mtcService;
  }

  public void setClaimGroupService( ClaimGroupService claimGroupService )
  {
    this.claimGroupService = claimGroupService;
  }

  public void setNominationClaimDAO( NominationClaimDAO nominationClaimDAO )
  {
    this.nominationClaimDAO = nominationClaimDAO;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setCalculatorService( CalculatorService calculatorService )
  {
    this.calculatorService = calculatorService;
  }

  public void setMultimediaService( MultimediaService multimediaService )
  {
    this.multimediaService = multimediaService;
  }

  public NominationPromotionService getNomPromoService()
  {
    return (NominationPromotionService)BeanLocator.getBean( NominationPromotionService.BEAN_NAME );
  }

  public void setParticipantGroupDAO( ParticipantGroupDAO participantGroupDAO )
  {
    this.participantGroupDAO = participantGroupDAO;
  }

  public void setRandomNumberStrategy( RandomNumberStrategy randomNumberStrategy )
  {
    this.randomNumberStrategy = randomNumberStrategy;
  }

  public RandomNumberStrategy getRandomNumberStrategy()
  {
    return randomNumberStrategy;
  }

  public void setClaimFormDefinitionService( ClaimFormDefinitionService claimFormDefinitionService )
  {
    this.claimFormDefinitionService = claimFormDefinitionService;
  }

  @Override
  public void removeWhyAttachment( Long claimId ) throws ServiceErrorException
  {
    NominationClaim claim = (NominationClaim)claimService.getClaimById( claimId );
    String fileName = "nomination" + File.separator + claim.getWhyAttachmentName();

    if ( claim.getWhyAttachmentName() != null )
    {
      deleteMediaFromWebdav( fileName );
    }
    claim.setWhyAttachmentName( null );
    claimDAO.saveClaim( claim );
  }

  private static FileUploadStrategy getWebDavFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.WEBDAV );
  }

  @Override
  public String getAwardTypeForCurrentLevel( Long id )
  {
    NominationClaim claim = (NominationClaim)claimDAO.getClaimById( id );
    Long currentLevel = claim.getApprovalRound();

    NominationPromotion promotion = (NominationPromotion)claim.getPromotion();

    for ( NominationPromotionLevel l : promotion.getNominationLevels() )
    {
      if ( promotion.getPayoutLevel().equalsIgnoreCase( "finalLevel" ) )
      {
        return l.getAwardPayoutType().getCode();
      }
      else if ( l.getLevelIndex().equals( currentLevel ) )
      {
        return l.getAwardPayoutType().getCode();
      }

    }

    return null;
  }

  public void setCashCurrencyService( CashCurrencyService cashCurrencyService )
  {
    this.cashCurrencyService = cashCurrencyService;
  }

  @Override
  public Map<String, Object> getNominationPastWinnersList( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getNominationPastWinnersList( parameters );
  }

  @Override
  public Map<String, Object> getEligiblePastWinnersPromotions( Long userId )
  {
    return nominationClaimDAO.getEligiblePastWinnersPromotions( userId );
  }

  @Override
  public Map<String, Object> getNominationPastWinnersDetail( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getNominationPastWinnersDetail( parameters );
  }

  @Override
  public Map<String, Object> getNominationWinnerModalDetails()
  {
    return nominationClaimDAO.getNominationWinnerModalDetails();
  }

  @Override
  public Map<String, Object> getNominationMyWinnersList( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getNominationMyWinnersList( parameters );
  }

  public Map<String, Object> getNominationsApprovalPageExtractCsvData( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getNominationsApprovalPageExtractCsvData( parameters );
  }

  @Override
  public CumulativeInfoTableDataValueBean getCumulativeApprovalNominatorTableData( Map<String, Object> parameters )
  {
    return nominationClaimDAO.getCumulativeApprovalNominatorTableData( parameters );
  }

  @Override
  public int getNominationApprovalsByClaimCount( Long userId )
  {
    return nominationClaimDAO.getNominationApprovalsByClaimCount( userId );
  }

  @Override
  public int getNominationApprovalsByClaimCountForSideBar( Long userId )
  {
    return nominationClaimDAO.getNominationApprovalsByClaimCountForSideBar( userId );
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  public List<NameIdBean> getTeamMembersByTeamName( Long teamId )
  {
    return nominationClaimDAO.getTeamMembersByTeamName( teamId );
  }

  @Override
  public boolean isCardMapped( Long cardId )
  {
    // TODO Auto-generated method stub
    return nominationClaimDAO.isCardMapped( cardId );
  }

  public boolean deleteMediaFromWebdav( String filePath )
  {
    if ( StringUtil.isEmpty( filePath ) )
    {
      return false;
    }

    try
    {
      try
      {
        byte[] data = getWebDavFileUploadStrategy().getFileData( filePath );
        if ( null == data )
        {
          // If file data does not exist, there is nothing to delete
          // Might have got deleted in previous attempt
          return true;
        }
      }
      catch( Throwable e )
      {
        // If file data does not exist, there is nothing to delete
        // Might have got deleted in previous attempt
        return true;
      }

      // Delete Media from Stage(WebDav)
      return getWebDavFileUploadStrategy().delete( filePath );
    }
    catch( Throwable e )
    {
      return false;
    }
  }

}


package com.biperf.core.service.goalquest.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.goalquest.GoalQuestPaxActivityDAO;
import com.biperf.core.dao.participant.ParticipantPartnerDAO;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.goalquest.GoalLevelView;
import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.goalquest.GoalQuestReviewProgress;
import com.biperf.core.domain.goalquest.GoalsView;
import com.biperf.core.domain.goalquest.ParticipantView;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.goalquest.PendingGoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PromotionView;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.domain.promotion.PromotionPartnerPayout;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.process.MerchOrderCreateProcess;
import com.biperf.core.process.PointsDepositProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.goalquest.GoalQuestSurveyUtils;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.service.promotion.engine.GoalPayoutStrategyFactory;
import com.biperf.core.service.promotion.engine.ManagerOverrideGoalStrategyFactory;
import com.biperf.core.service.promotion.engine.PartnerGoalStrategyFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.goalquest.ManagerGoalquestViewBean;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.goalquest.GoalquestProgramRole;
import com.biperf.core.value.goalquest.HoneycombGoalQuestViewStatus;
import com.biperf.core.value.hc.GoalquestDetailsResponse;
import com.biperf.core.value.hc.GoalquestDetailsResponse.GoalquestProgramResponse;
import com.biw.hc.contest.api.response.EligibleProgramVO;
import com.biw.hc.contest.api.response.GoalSelectionResponse;
import com.biw.hc.core.service.HCServices;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * GoalQuestServiceImpl.
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
 * <td>Satish & Todd</td>
 * <td>Jan 15, 2007</td>
 * <td>1.0</td>
 * <td>Handles Goal Quest Calculation and processes Calculation results</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestServiceImpl implements GoalQuestService
{
  private PaxGoalService paxGoalService;
  private UserService userService;
  private ParticipantService participantService;
  private AudienceService audienceService;
  private SystemVariableService systemVariableService;
  private MessageService messageService;
  private MailingService mailingService;
  private PromotionService promotionService;
  private GoalQuestPaxActivityService goalQuestPaxActivityService;

  private ListBuilderService listBuilderService;
  private HierarchyService hierarchyService;

  private ParticipantPartnerDAO participantPartnerDAO;
  private GoalQuestPaxActivityDAO goalQuestPaxActivityDAO;

  private AwardBanQServiceFactory awardBanQServiceFactory;
  private GoalPayoutStrategyFactory goalStrategyFactory;
  private ManagerOverrideGoalStrategyFactory managerOverrideGoalStrategyFactory;
  private PartnerGoalStrategyFactory partnerGoalStrategyFactory;

  private CharacteristicDAO characteristicDAO;
  private ProcessService processService;

  private static final String UnixFileSeparator = "/";
  private static final String WindowsFileSeparator = "\\";

  private static final String DETAIL_REPORT_HEADER = "Promotion,Login Id,PAX Status,PAX First Name,PAX Last Name,Email Address,"
      + "Phone,Address,City,State,Zip Code,Country,Node Role,Node Owner,Base Objective,Goal Level,Goal Value,Goal Name," + "Total Performance,Percent to goal,Award";

  private static final String DETAIL_REPORT_PARTNER_HEADER = "Promotion,Login Id,PAX Status,PAX First Name,PAX Last Name,PAX Type,Email Address,"
      + "Phone,Address,City,State,Zip Code,Country,Node Role,Node Owner,Base Objective,Goal Level,Goal Value,Goal Name," + "Total Performance,Percent to goal,Award, Participant selected";

  private static final String PAX_TYPE_PARTNER = "Partner";
  private static final String PAX_TYPE_MANAGER = "Manager";
  private static final String PAX_TYPE_OWNER = "Owner";
  private static final String PAX_TYPE_PARTICIPANT = "Participant";
  private static final Log log = LogFactory.getLog( GoalQuestServiceImpl.class );

  private static final BigDecimal BAD_OUTPUT = new BigDecimal( "99" );
  private static final Object OUTPUT_RETURN_CODE = "p_out_return_code";

  public void setGoalQuestPaxActivityDAO( GoalQuestPaxActivityDAO goalQuestPaxActivityDAO )
  {
    this.goalQuestPaxActivityDAO = goalQuestPaxActivityDAO;
  }

  @SuppressWarnings( "unchecked" )
  public List<Long> getEligibleParticipantsForPromotion( GoalQuestPromotion promotion )
  {
    List<Long> paxIds = new ArrayList<Long>();
    Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();

    if ( promotion.getPrimaryAudienceType() != null && promotion.getPrimaryAudienceType().getCode().equalsIgnoreCase( "allactivepaxaudience" ) )
    {
      paxIds.addAll( participantService.getAllActivePaxIds() );
    }
    else
    {
      List<FormattedValueBean> fvbPaxList = listBuilderService.searchParticipants( promotion.getPrimaryAudiences(), primaryHierarchy.getId(), true, null, true );

      if ( null != fvbPaxList )
      {
        for ( FormattedValueBean fmv : fvbPaxList )
        {
          paxIds.add( fmv.getId() );
        }
      }
    }

    List<FormattedValueBean> fvbPaxSecondList = listBuilderService.searchParticipants( promotion.getSecondaryAudiences(), primaryHierarchy.getId(), true, null, true );
    if ( null != fvbPaxSecondList )
    {
      for ( FormattedValueBean fmv : fvbPaxSecondList )
      {
        paxIds.add( fmv.getId() );
      }
    }

    return paxIds;
  }

  /**
   * Get the map containing the summary lists for participant and manager override levels by
   * promotion id.
   * 
   * @param promotionId
   * @return map
   */
  @SuppressWarnings( "unchecked" )
  public PendingGoalQuestAwardSummary getGoalQuestAwardSummaryByPromotionId( Long promotionId )
  {
    PendingGoalQuestAwardSummary pendingGoalQuestAwardSummary = new PendingGoalQuestAwardSummary();

    List<GoalQuestAwardSummary> participantGoalQuestAwardSummaryList = new ArrayList<GoalQuestAwardSummary>();
    List<GoalCalculationResult> managerGoalQuestAwardSummaryList = new ArrayList<GoalCalculationResult>();
    List<GoalQuestAwardSummary> partnerGoalQuestAwardSummaryList = new ArrayList<GoalQuestAwardSummary>();
    List<GoalCalculationResult> participantGQResults = new ArrayList<GoalCalculationResult>();

    // Get Promotion by ID
    GoalQuestPromotion promotion = (GoalQuestPromotion)promotionService.getPromotionById( promotionId );

    // Get the list of PAX goals selected for this promotion
    AssociationRequestCollection arCollection = new AssociationRequestCollection();
    arCollection.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.GOAL_LEVEL ) );
    List<PaxGoal> paxGoalList = paxGoalService.getPaxGoalByPromotionId( promotionId, arCollection );
    List<Long> promotionEligiblePax = getEligibleParticipantsForPromotion( promotion );

    if ( paxGoalList != null && paxGoalList.size() > 0 )
    {
      /* Map of pax goals for managers - key is Participant */
      Map<Participant, PaxGoal> managerGoals = new HashMap<Participant, PaxGoal>();
      /* Map of GoalCalculationResults by node id */
      Map<Long, List<GoalCalculationResult>> goalResultsByNode = new HashMap<Long, List<GoalCalculationResult>>();
      /* Map of Participant GoalAwardSummary by level id */
      Map<Long, GoalQuestAwardSummary> goalQuestAwardSummaryParticipantMap = getEmptyGoalQuestAwardSummaryMap( promotion );
      /* Map of Partner GoalAwardSummary by level id */
      Map<Long, GoalQuestAwardSummary> goalQuestAwardSummaryPartnerMap = getEmptyGoalQuestAwardSummaryMap( promotion );
      // Map of GoalCalculationResults by PAX id
      Map<Long, GoalCalculationResult> goalResultsByPax = new HashMap<Long, GoalCalculationResult>();
      // Map of payoutEligibility by PAX id
      Map<Long, Boolean> payoutEligibilityByPax = new HashMap<Long, Boolean>();

      // hydrate notifications as it will be used later.
      PromotionAssociationRequest promotionAssociationRequest = new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS );
      promotionAssociationRequest.execute( promotion );

      Iterator<PaxGoal> iter = paxGoalList.iterator();
      while ( iter.hasNext() )
      {
        PaxGoal paxGoal = iter.next();

        // Check if Participant is in eligible audience
        if ( !isPaxEligibleForPayout( payoutEligibilityByPax, paxGoal.getParticipant(), promotionEligiblePax ) )
        {
          iter.remove();
          continue;
        }

        List<Node> ownedNodes = paxGoal.getParticipant().getNodes( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
        // if the user is manager save the goal for override calculation
        if ( ownedNodes.size() > 0 )
        {
          managerGoals.put( paxGoal.getParticipant(), paxGoal );
        }
        // ok proceed
        if ( paxGoal.isManagerOverrideGoal() )
        {
          throw new BeaconRuntimeException( "Found a manager goal level on a non-manager" );
        }
        if ( paxGoal.getGoalLevel() != null )
        {
          GoalCalculationResult goalCalculationResult = addOrIncrementTotals( goalQuestAwardSummaryParticipantMap, paxGoal.getGoalLevel().getId(), paxGoal );
          if ( goalCalculationResult != null )
          {
            goalCalculationResult.setReciever( paxGoal.getParticipant() );
            participantGQResults.add( goalCalculationResult );
            goalResultsByPax.put( paxGoal.getParticipant().getId(), goalCalculationResult );
          }
          // Set<UserNode> userNodes = paxGoal.getParticipant().getUserNodes();
          UserNode userNode = paxGoal.getParticipant().getPrimaryUserNode();
          if ( userNode != null )
          {
            // UserNode userNode = ( (UserNode)userNodes.iterator().next() );
            if ( userNode.getNode() != null )
            {
              goalCalculationResult.setNodeRole( userNode.getHierarchyRoleType() );
              List<GoalCalculationResult> calculationResults = goalResultsByNode.get( userNode.getNode().getId() );
              if ( calculationResults == null )
              {
                calculationResults = new ArrayList<GoalCalculationResult>();
                goalResultsByNode.put( userNode.getNode().getId(), calculationResults );
              }
              calculationResults.add( goalCalculationResult );
            }
          }
        }
      }
      if ( promotion.getPartnerAudienceType() != null )
      {
        List<GoalCalculationResult> partnerGQResults = computePartnerGQPayout( promotion, goalResultsByPax, goalQuestAwardSummaryPartnerMap, payoutEligibilityByPax );
        if ( partnerGQResults != null && partnerGQResults.size() > 0 )
        {
          pendingGoalQuestAwardSummary.setPartnerGQResults( partnerGQResults );
          partnerGoalQuestAwardSummaryList = partnerGoalStrategyFactory.getPartnerGoalStrategy( promotion.getPartnerEarnings().getCode() ).summarizeResults( goalQuestAwardSummaryPartnerMap );
        }
      }
      if ( promotion.getOverrideStructure() != null && !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) )
      {
        List<GoalCalculationResult> managerOverrideResults = computeManagerOverrides( promotion, goalResultsByNode, managerGoals );
        pendingGoalQuestAwardSummary.setManagerOverrideResults( managerOverrideResults );
        managerGoalQuestAwardSummaryList = managerOverrideGoalStrategyFactory.getManagerOverrideGoalStrategy( promotion.getOverrideStructure().getCode() )
            .summarizeResults( promotion, managerOverrideResults, paxGoalList );
      }
      if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
      {
        participantGoalQuestAwardSummaryList = goalStrategyFactory.getGoalPayoutStrategy( promotion.getPayoutStructure().getCode() ).summarizeResults( goalQuestAwardSummaryParticipantMap );
      }
      else
      // Merch or Travel
      {
        participantGoalQuestAwardSummaryList = goalStrategyFactory.getGoalPayoutStrategy().summarizeResults( goalQuestAwardSummaryParticipantMap );
      }
    }

    pendingGoalQuestAwardSummary.setManagerGoalQuestAwardSummaryList( managerGoalQuestAwardSummaryList );
    pendingGoalQuestAwardSummary.setParticipantGoalQuestAwardSummaryList( participantGoalQuestAwardSummaryList );
    pendingGoalQuestAwardSummary.setPartnerGoalQuestAwardSummaryList( partnerGoalQuestAwardSummaryList );
    pendingGoalQuestAwardSummary.setParticipantGQResults( participantGQResults );
    pendingGoalQuestAwardSummary.setPromotion( promotion );

    return pendingGoalQuestAwardSummary;
  }

  private boolean isPaxEligibleForPayout( Map<Long, Boolean> map, Participant participant, List<Long> promotionEligiblePax )
  {
    Boolean eligible = map.get( participant.getId() );
    if ( null == eligible )
    {
      // PAX should be active and belong to PAX Audience
      eligible = participant.isActive().booleanValue() && promotionEligiblePax.contains( participant.getId() ) ? Boolean.TRUE : Boolean.FALSE;
      map.put( participant.getId(), eligible );
    }

    return eligible.booleanValue();
  }

  /**
   * @param promotionId 
   * @param userId
   * @return Map containing list of GoalQuestReviewProgress objects, promotion if any
   */
  @SuppressWarnings( "unchecked" )
  public Map<String, Object> getGoalQuestProgressByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    if ( promotionId == null || userId == null )
    {
      final String errorMessage = "promotion id or user id cannot be null for" + " requesting GoalQuest Progress information";
      log.error( errorMessage );
      throw new BeaconRuntimeException( errorMessage );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new GoalQuestPaxActivityAssociationRequest( GoalQuestPaxActivityAssociationRequest.ALL ) );

    Map<String, Object> goalQuestProgressMap = new HashMap<String, Object>();
    List<GoalQuestReviewProgress> goalQuestProgressList = new ArrayList<GoalQuestReviewProgress>();
    List<GoalQuestParticipantActivity> goalQuestPaxActivityList = goalQuestPaxActivityService.getGoalQuestParticipantActivityByPromotionIdAndUserId( promotionId,
                                                                                                                                                     userId,
                                                                                                                                                     associationRequestCollection );

    associationRequestCollection.clear();
    associationRequestCollection.add( new PaxGoalAssociationRequest( PaxGoalAssociationRequest.ALL ) );
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotionId, userId, associationRequestCollection );
    GoalQuestPromotion goalQuestPromotion = null;
    BigDecimal amountToAchieve = new BigDecimal( 0 );
    if ( paxGoal != null )
    {
      goalQuestProgressMap.put( "paxGoal", paxGoal );
      amountToAchieve = SelectGoalUtil.getCalculatedGoalAmount( paxGoal.getGoalLevel(), paxGoal );
      if ( null != paxGoal.getGoalLevel() )
      {
        goalQuestPromotion = (GoalQuestPromotion)paxGoal.getGoalLevel().getPromotion();
      }
    }
    else
    {
      goalQuestPromotion = (GoalQuestPromotion)promotionService.getPromotionById( promotionId );
    }
    if ( goalQuestPaxActivityList != null && goalQuestPaxActivityList.size() > 0 )
    {
      BigDecimal cumulativeTotal = new BigDecimal( "0" );
      for ( Iterator<GoalQuestParticipantActivity> goalQuestPaxActivityListIter = goalQuestPaxActivityList.iterator(); goalQuestPaxActivityListIter.hasNext(); )
      {
        GoalQuestParticipantActivity goalQuestParticipantActivity = goalQuestPaxActivityListIter.next();
        GoalQuestReviewProgress goalQuestReviewProgress = new GoalQuestReviewProgress();
        goalQuestReviewProgress.setSubmissionDate( goalQuestParticipantActivity.getSubmissionDate() );
        goalQuestReviewProgress.setAmountToAchieve( amountToAchieve );

        goalQuestReviewProgress.setRoundingType( goalQuestPromotion != null ? goalQuestPromotion.getRoundingMethod().getBigDecimalRoundingMode() : BigDecimal.ROUND_HALF_UP );
        if ( goalQuestParticipantActivity.getQuantity() != null )
        {
          if ( goalQuestParticipantActivity.getType().equals( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.INCREMENTAL ) ) )
          {
            BigDecimal quantity = paxGoal.getGoalQuestPromotion().roundValue( goalQuestParticipantActivity.getQuantity() );
            goalQuestReviewProgress.setQuantity( quantity );
            cumulativeTotal = cumulativeTotal.add( quantity );
          }
          else if ( goalQuestParticipantActivity.getType().equals( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.REPLACE ) ) )
          {
            BigDecimal quantity = paxGoal.getGoalQuestPromotion().roundValue( goalQuestParticipantActivity.getQuantity() );
            goalQuestReviewProgress.setQuantity( quantity.subtract( cumulativeTotal ) );
            cumulativeTotal = quantity;
          }
        }
        goalQuestReviewProgress.setAutomotive( goalQuestParticipantActivity.isAutomotive() );
        if ( goalQuestParticipantActivity.isAutomotive() )
        {
          goalQuestReviewProgress.setSaleDate( goalQuestParticipantActivity.getSalesDate() );
          goalQuestReviewProgress.setDeliveryDate( goalQuestParticipantActivity.getDeliveryDate() );
          goalQuestReviewProgress.setModel( goalQuestParticipantActivity.getModel() );
          goalQuestReviewProgress.setVin( goalQuestParticipantActivity.getVin() );
          goalQuestReviewProgress.setTransactionType( goalQuestParticipantActivity.getTransactionType() );
          goalQuestReviewProgress.setDealerCode( goalQuestParticipantActivity.getDealerCode() );
          goalQuestReviewProgress.setDealerName( goalQuestParticipantActivity.getDealerName() );
        }
        goalQuestReviewProgress.setLoadType( goalQuestParticipantActivity.getType().getName() );
        goalQuestReviewProgress.setCumulativeTotal( cumulativeTotal );
        goalQuestProgressList.add( goalQuestReviewProgress );
      }
    }
    // if the arraylist is empty add
    if ( goalQuestProgressList.size() == 0 )
    {
      BigDecimal zero = new BigDecimal( "0" );
      GoalQuestReviewProgress goalQuestReviewProgress = new GoalQuestReviewProgress();
      goalQuestReviewProgress.setAmountToAchieve( amountToAchieve );
      goalQuestReviewProgress.setQuantity( zero );
      goalQuestReviewProgress.setCumulativeTotal( zero );
      goalQuestProgressList.add( goalQuestReviewProgress );
    }
    goalQuestProgressMap.put( "goalQuestProgressList", goalQuestProgressList );

    return goalQuestProgressMap;
  }

  /**
   * This method returns user defined path where the csv file should be saved. i.e. /tmp/ on Unix.
   * It makes sure the file separator (On Windows this is \  On Unix this is /) works with the
   * current operating system
   * 
   * @return a system variable driven path where the extract will be saved
   */
  private String getExtractLocation()
  {
    String extractLocation = null;

    // On Windows this is \ On Unix this is /
    String currentSystemFileSeparator = File.separator;

    // user defined path where the csv file should be saved. i.e. /tmp/ on Unix
    extractLocation = System.getProperty( "appdatadir" );
    log.info( "Extract location value of getproperty -->>>>>>>>>>>>" + extractLocation );

    // make sure the user defined directory works with the current system
    if ( !StringUtils.isBlank( extractLocation ) )
    {
      // e.g. Developers running on localhosts on Windows
      // but the system variable specifies an Unix file separator
      if ( extractLocation.indexOf( UnixFileSeparator ) >= 0 && currentSystemFileSeparator.equals( WindowsFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '/', '\\' );
      }
      // e.g. QA, PPRD, PROD - CTECH environments running on Unix
      // but the system variable specifies a Windows file separator
      if ( extractLocation.indexOf( WindowsFileSeparator ) >= 0 && currentSystemFileSeparator.equals( UnixFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '\\', '/' );
      }
    }

    return extractLocation;
  }

  public boolean generateAndMailExtractReport( List goalCalculationList, GoalQuestPromotion promotion )
  {
    return generateAndMailExtractReport( goalCalculationList, promotion, null );
  }

  /**
   * Generate and mail a detail extract from the goalCalculationList
   * 
   * @param goalCalculationList
   * @return boolean 
   */
  public boolean generateAndMailExtractReport( List goalCalculationList, GoalQuestPromotion promotion, String batchComments )
  {
    try
    {
      String extractLocation = getExtractLocation();
      String fileName = generateUniqueFileName();

      FileExtractUtils.createDirIfNeeded( extractLocation );

      File extractFile = new File( extractLocation, fileName );
      if ( extractFile.createNewFile() )
      {
        BufferedWriter writer = new BufferedWriter( new FileWriter( extractFile ) );
        boolean incPartnerDtlsInExtract = promotion.getPartnerAudienceType() != null;
        writer.write( incPartnerDtlsInExtract ? DETAIL_REPORT_PARTNER_HEADER : DETAIL_REPORT_HEADER );
        writer.newLine();
        for ( Iterator goalCalculationResultsIter = goalCalculationList.iterator(); goalCalculationResultsIter.hasNext(); )
        {
          GoalCalculationResult goalCalculationResult = (GoalCalculationResult)goalCalculationResultsIter.next();
          if ( goalCalculationResult.getReciever() != null )
          {
            goalCalculationResult.setReciever( getParticipant( goalCalculationResult.getReciever().getId() ) );
          }
          else if ( goalCalculationResult.getNodeOwner() != null )
          {
            goalCalculationResult.setReciever( getParticipant( goalCalculationResult.getNodeOwner().getId() ) );
          }
          else if ( goalCalculationResult.getReciever() == null && goalCalculationResult.getNodeOwner() == null )
          {
            continue;
          }

          String csvRow = createDetailLine( goalCalculationResult, promotion, incPartnerDtlsInExtract );
          writer.write( csvRow );
          writer.newLine();
        }
        writer.close();
        if ( extractFile != null && extractFile.exists() )
        {
          // Email the file to the user.
          sendMessage( extractFile.getAbsolutePath(), extractFile.getName(), promotion, batchComments );
        }
      }
      else
      {
        log.error( "Error creating temporary file for extract" );
        throw new BeaconRuntimeException( "Error creating temporary file for extract" );

      }
      return false;
    }
    catch( Exception e )
    {
      log.error( "Error generating extract file", e );
      throw new BeaconRuntimeException( "Error generating extract file", e );
    }
  }

  private Participant getParticipant( Long id )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ALL ) );
    return getParticipantService().getParticipantByIdWithAssociations( id, associationRequestCollection );
  }

  private String createDetailLine( GoalCalculationResult goalCalculationResult, GoalQuestPromotion promotion, boolean incPartnerDtlsInExtract )
  {
    StringBuffer detailLine = new StringBuffer();
    detailLine.append( promotion.getName() );
    detailLine.append( ",=\"" );
    detailLine.append( goalCalculationResult.getReciever().getUserName() );
    detailLine.append( "\"," );
    detailLine.append( "\"" );
    if ( goalCalculationResult.getReciever().isActive() )
    {
      detailLine.append( "active" );
    }
    else
    {
      detailLine.append( "inactive" );
    }
    detailLine.append( "\"," );
    detailLine.append( goalCalculationResult.getReciever().getFirstName() );
    detailLine.append( ",\"" );
    detailLine.append( goalCalculationResult.getReciever().getLastName() );
    detailLine.append( "\"," );

    if ( incPartnerDtlsInExtract )
    {
      detailLine.append( "\"" );
      String role = null;
      if ( goalCalculationResult.isPartner() )
      {
        role = PAX_TYPE_PARTNER;
      }
      else if ( goalCalculationResult.getReciever() != null && goalCalculationResult.getReciever().isOwner() )
      {
        role = PAX_TYPE_OWNER;
      }
      else if ( goalCalculationResult.isManager() )
      {
        role = PAX_TYPE_MANAGER;
      }
      else
      {
        role = PAX_TYPE_PARTICIPANT;
      }
      detailLine.append( role );
      detailLine.append( "\"," );
    }
    detailLine.append( "\"" );
    if ( goalCalculationResult.getReciever().getPrimaryEmailAddress() != null )
    {
      detailLine.append( goalCalculationResult.getReciever().getPrimaryEmailAddress().getEmailAddr() );
    }
    detailLine.append( "\"," );
    detailLine.append( "\"" );
    if ( goalCalculationResult.getReciever().getPrimaryPhone() != null )
    {
      detailLine.append( goalCalculationResult.getReciever().getPrimaryPhone().getPhoneNbr() );
    }
    detailLine.append( "\"," );

    if ( goalCalculationResult.getReciever().getPrimaryAddress() != null && goalCalculationResult.getReciever().getPrimaryAddress().getAddress() != null )
    {
      detailLine.append( "\"" );
      if ( goalCalculationResult.getReciever().getPrimaryAddress().getAddress() != null )
      {
        detailLine.append( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getAddr1() );
        if ( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getAddr2() != null )
        {
          detailLine.append( " " + goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getAddr2() );
        }
        if ( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getAddr3() != null )
        {
          detailLine.append( " " + goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getAddr3() );
        }
      }
      detailLine.append( "\"," );
      detailLine.append( "\"" );
      // Fix 22917 - For other than united states pax.
      if ( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getCity() != null )
      {
        detailLine.append( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getCity() );
      }
      detailLine.append( "\"," );
      detailLine.append( "\"" );
      if ( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getStateType() != null )
      {
        detailLine.append( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getStateType().getName() );
      }
      detailLine.append( "\"," );
      detailLine.append( "\"" );
      if ( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getPostalCode() != null )
      {
        detailLine.append( goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getPostalCode() );
      }
      detailLine.append( "\"," );
      detailLine.append( "\"" );
      Country country = goalCalculationResult.getReciever().getPrimaryAddress().getAddress().getCountry();
      if ( country != null )
      {
        if ( country.getCmAssetCode() != null && country.getNameCmKey() != null )
        {
          detailLine.append( ContentReaderManager.getText( country.getCmAssetCode(), country.getNameCmKey() ) );
        }
      }
      detailLine.append( "\"," );

    }
    else
    {
      detailLine.append( ",,,,," );
    }
    detailLine.append( "\"" );
    if ( goalCalculationResult.getNodeRole() != null )
    {

      detailLine.append( goalCalculationResult.getNodeRole().getName() );
    }
    detailLine.append( "\"," );
    detailLine.append( "\"" );
    Set userNodes = goalCalculationResult.getReciever().getUserNodes();
    if ( userNodes != null && userNodes.size() > 0 )
    {
      Iterator iter = userNodes.iterator();
      iter.hasNext();
      UserNode userNode = (UserNode)iter.next();
      if ( participantService.getNodeOwner( userNode.getNode().getId() ) != null )
      {
        detailLine.append( participantService.getNodeOwner( userNode.getNode().getId() ).getNameLFMWithComma() );
      }
    }
    detailLine.append( "\"," );
    detailLine.append( "\"" );
    if ( goalCalculationResult.getBaseObjective() != null )
    {
      detailLine.append( goalCalculationResult.getBaseObjective() );

    }
    detailLine.append( "\"," );

    if ( goalCalculationResult.getGoalLevel() != null )
    {
      detailLine.append( "\"" );
      detailLine.append( goalCalculationResult.getGoalLevel().getSequenceNumber() );
      detailLine.append( "\"," );

      if ( goalCalculationResult.getAmountToAchieve() != null )
      {
        detailLine.append( "\"" );
        detailLine.append( goalCalculationResult.getAmountToAchieve() );
        detailLine.append( "\"," );
      }
      else
      {
        detailLine.append( "," );
      }

      detailLine.append( "\"" );
      detailLine.append( goalCalculationResult.getGoalLevel().getGoalLevelName() );
      detailLine.append( "\"," );

    }
    else
    {
      detailLine.append( ",,," );
    }
    detailLine.append( "\"" );
    if ( goalCalculationResult.getTotalPerformance() != null && goalCalculationResult.getGoalLevel() != null )
    {
      detailLine.append( goalCalculationResult.getTotalPerformance() );

    }
    detailLine.append( "\"," );
    detailLine.append( "\"" );
    if ( goalCalculationResult.getPercentageAchieved() != null )
    {

      detailLine.append( goalCalculationResult.getPercentageAchieved() );

    }
    detailLine.append( "\"," );
    detailLine.append( "\"" );
    if ( goalCalculationResult.getCalculatedPayout() != null )
    {

      detailLine.append( goalCalculationResult.getCalculatedPayout() );

    }
    detailLine.append( "\"," );
    if ( incPartnerDtlsInExtract )
    {
      if ( null != goalCalculationResult.getPartnerToParticipant() )
      {

        detailLine.append( "\"" );
        detailLine.append( goalCalculationResult.getPartnerToParticipant().getNameLFMWithComma() );
        detailLine.append( "\"," );
      }
      else
      {
        detailLine.append( "," );
      }
    }
    return detailLine.toString();

  }

  /**
   * Creates a .csv file name that is unique to: - the client name - the report requested - the current datetime.
   * 
   * @return an unique file name
   */
  private String generateUniqueFileName()
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_GQCalcExtract_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  /**
   * Sends an e-mail message to the user  with the file attachment of the dataextract
   */
  private void sendMessage( String fullFileName, String attachmentFileName, GoalQuestPromotion promotion, String batchComments )
  {
    User currentUser = userService.getUserByUserName( UserManager.getUserName() );
    // Set up mailing-level personalization data.
    Map objectMap = new HashMap();
    if ( promotion != null )
    {
      objectMap.put( "promotionName", promotion.getName() );
    }
    if ( !StringUtils.isEmpty( batchComments ) )
    {
      objectMap.put( "batchComments", batchComments );
    }
    else
    {
      objectMap.put( "batchComments", "  " );
    }

    // Compose the e-mail message.

    Mailing mailing = composeMail( MessageService.GOALQUEST_DETAIL_EXTRACT_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );

    mailing.addMailingRecipient( createRecipient( currentUser ) );

    // Is there a file to attach?
    if ( fullFileName != null )
    {
      // Attach the file to the e-mail.
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, fullFileName, attachmentFileName ) );
    }

    // Send the e-mail message.
    try
    {
      mailing = mailingService.submitMailingWithoutScheduling( mailing, objectMap );

      // process mailing
      mailingService.processMailing( mailing.getId() );

      log.info( "Successfully sent email to  " + currentUser.getFirstName() + " " + currentUser.getLastName() + "." + " (mailing ID = " + mailing.getId() + ")" );

    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending goalquest detail extract. " + " (mailing ID = " + mailing.getId() + ")" );
      log.error( msg, e );
      throw new BeaconRuntimeException( "Error during submission of mail. The exception caused by: " + e.getCause().getMessage() );
    }
  }

  /**
   * @param mailing
   * @param fullFileName
   * @return MailingAttachmentInfo
   */
  protected MailingAttachmentInfo addMailingAttachmentInfo( Mailing mailing, String fullFileName, String attachmentFileName )
  {
    MailingAttachmentInfo mailingAttachmentInfo = new MailingAttachmentInfo();
    mailingAttachmentInfo.setFullFileName( fullFileName );
    mailingAttachmentInfo.setMailing( mailing );
    mailingAttachmentInfo.setAttachmentFileName( attachmentFileName );
    return mailingAttachmentInfo;
  }

  /**
   * Takes in a user and returns a mailing recipient object suitable for mailing service
   * 
   * @param recipient
   * @return a mailingRecipient object
   */
  private MailingRecipient createRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );

    return mailingRecipient;
  }

  /**
   * Adds the message by name and mailing type to a mailing object
   * 
   * @param cmAssetCode
   * @param mailingType
   * @return a mailing object that is mostly assembled, except for the mailingRecipient(s)
   */
  private Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = messageService.getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );

    return mailing;
  }

  /**
   * Creates a new mailing object and add Guid, Sender and Delivery Date to it
   * 
   * @return a partially assembled mailing object
   */
  private Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );

    return mailing;
  }

  private List<GoalCalculationResult> computeManagerOverrides( GoalQuestPromotion promotion, Map<Long, List<GoalCalculationResult>> goalResults, Map<Participant, PaxGoal> managerGoals )
  {
    List<GoalCalculationResult> managerOverrides = new ArrayList<GoalCalculationResult>();
    if ( goalResults != null )
    {
      for ( Iterator<Long> nodeIdIter = goalResults.keySet().iterator(); nodeIdIter.hasNext(); )
      {
        Long nodeId = nodeIdIter.next();
        User nodeOwner = getNodeManager( nodeId );

        /*
         * if ( nodeOwner != null ) {
         */
        // Participant pax = participantService.getParticipantById( nodeOwner.getId() );

        // if the owner is not in audience then ignore
        /*
         * if(!getAudienceService().isParticipantInPrimaryAudience( promotion, pax ) &&
         * !getAudienceService().isParticipantInSecondaryAudience( promotion, pax, null)) {
         * continue; }
         */
        List<GoalCalculationResult> goalResultsForNode = goalResults.get( nodeId );
        // Loop through goal results and add node owner to results.
        for ( Iterator<GoalCalculationResult> nodeGoalResultsIter = goalResultsForNode.iterator(); nodeGoalResultsIter.hasNext(); )
        {
          GoalCalculationResult currentResult = (GoalCalculationResult)nodeGoalResultsIter.next();
          currentResult.setNodeOwner( nodeOwner );
        }
        if ( !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) )
        {
          PaxGoal managerOverrideGoal = null;
          if ( nodeOwner != null )
          {
            managerOverrideGoal = (PaxGoal)managerGoals.get( nodeOwner );
          }
          List<GoalCalculationResult> goalCalculationResult = managerOverrideGoalStrategyFactory.getManagerOverrideGoalStrategy( promotion.getOverrideStructure().getCode() )
              .processGoal( promotion, managerOverrideGoal, goalResults.get( nodeId ), nodeId );
          managerOverrides.addAll( goalCalculationResult );
        }
        // }
      }
      if ( promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.STACK_RANKING_LEVEL ) )
      {
        List<GoalCalculationResult> sortedPets = new ArrayList<GoalCalculationResult>( managerOverrides );
        PropertyComparator.sort( sortedPets, new MutableSortDefinition( "percentAchieved", true, false ) );
        managerOverrides.removeAll( managerOverrides );
        int indx = 0;
        int rankIncrement = 0;
        BigDecimal prevPayout = new BigDecimal( 0 );
        for ( Iterator goalLevelIter = sortedPets.iterator(); sortedPets.size() >= indx + 1; )
        {
          GoalCalculationResult goalCalcResult = sortedPets.get( indx );
          if ( indx - 1 >= 0 )
          {
            GoalCalculationResult prevGoalCalcResult = sortedPets.get( indx - 1 );
            if ( prevGoalCalcResult.getPercentAchieved() != null )
            {
              prevPayout = prevGoalCalcResult.getPercentAchieved();
            }
          }
          if ( goalCalcResult.getPercentAchieved() != null && prevPayout.compareTo( goalCalcResult.getPercentAchieved() ) != 0 )
          {
            rankIncrement++;
          }
          indx++;
          for ( Iterator managerOverrideIter = promotion.getManagerOverrideGoalLevels().iterator(); managerOverrideIter.hasNext(); )
          {
            ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)managerOverrideIter.next();
            long totalAward = 0;
            if ( new Long( rankIncrement ) >= managerOverrideGoalLevel.getMoStartRank() && new Long( rankIncrement ) <= managerOverrideGoalLevel.getMoEndRank() )
            {
              BigDecimal managerAward = new BigDecimal( managerOverrideGoalLevel.getMoAwards() );
              totalAward += managerAward.longValue();
              goalCalcResult.setGoalLevel( managerOverrideGoalLevel );
              goalCalcResult.incrementTotalAchieved();
              if ( totalAward > 0 )
              {
                goalCalcResult.setTotalAward( new BigDecimal( totalAward ) );
                goalCalcResult.setCalculatedPayout( new BigDecimal( totalAward ) );
              }
              managerOverrides.add( goalCalcResult );
              break;
            }
          }
        }
      }
    }

    return managerOverrides;
  }

  @SuppressWarnings( "unchecked" )
  private User getNodeManager( Long nodeId )
  {
    if ( nodeId != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );
      List users = userService.getAllUsersOnNodeHavingRole( nodeId, HierarchyRoleType.lookup( HierarchyRoleType.OWNER ), associationRequestCollection );
      if ( users != null && users.size() > 0 )
      {
        return (User)users.get( 0 );
      }
    }
    return null;
  }

  private GoalCalculationResult addOrIncrementTotals( Map<Long, GoalQuestAwardSummary> map, Long levelId, PaxGoal paxGoal )
  {
    GoalCalculationResult goalCalculationResult;
    if ( paxGoal.getGoalQuestPromotion().getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      goalCalculationResult = goalStrategyFactory.getGoalPayoutStrategy( paxGoal.getGoalQuestPromotion().getPayoutStructure().getCode() ).processGoal( paxGoal );
    }
    else // Merch or Travel
    {
      goalCalculationResult = goalStrategyFactory.getGoalPayoutStrategy().processGoal( paxGoal );
    }

    GoalQuestAwardSummary levelAwardSummary = map.get( levelId );
    levelAwardSummary.incrementTotalSelected();
    if ( goalCalculationResult.isAchieved() )
    {
      levelAwardSummary.incrementTotalAchieved();
      levelAwardSummary.incrementTotalAward( goalCalculationResult.getCalculatedPayout() );
    }

    return goalCalculationResult;
  }

  private List<GoalCalculationResult> computePartnerGQPayout( GoalQuestPromotion promotion,
                                                              Map<Long, GoalCalculationResult> goalResultsByPax,
                                                              Map<Long, GoalQuestAwardSummary> goalQuestAwardSummaryPartnerMap,
                                                              Map<Long, Boolean> payoutEligibilityByPax )
  {
    List<GoalCalculationResult> partnerPayouts = new ArrayList<GoalCalculationResult>();

    Map<Long, List<GoalCalculationResult>> paxGoalResultsByPartner = new HashMap<Long, List<GoalCalculationResult>>();
    List<ParticipantPartner> paxPartners = getEligiblePaxPartners( promotion, paxGoalResultsByPartner, goalResultsByPax, payoutEligibilityByPax );

    Map<Long, PromotionPartnerPayout> partnerGoalLevelsBySequence = new HashMap<Long, PromotionPartnerPayout>();
    for ( Iterator<PromotionPartnerPayout> iter = promotion.getPartnerGoalLevels().iterator(); iter.hasNext(); )
    {
      PromotionPartnerPayout partnerGoal = (PromotionPartnerPayout)iter.next();
      partnerGoalLevelsBySequence.put( new Long( partnerGoal.getSequenceNumber() ), partnerGoal );
    }

    for ( Iterator<ParticipantPartner> iter = paxPartners.iterator(); iter.hasNext(); )
    {
      ParticipantPartner paxPartner = iter.next();
      if ( promotion.getPartnerEarnings() != null )
      {
        GoalCalculationResult goalCalculationResult = partnerGoalStrategyFactory.getPartnerGoalStrategy( promotion.getPartnerEarnings().getCode() )
            .processGoal( paxPartner, goalResultsByPax, paxGoalResultsByPartner, partnerGoalLevelsBySequence );
        partnerPayouts.add( goalCalculationResult );

        GoalQuestAwardSummary levelSummary = (GoalQuestAwardSummary)goalQuestAwardSummaryPartnerMap.get( goalCalculationResult.getGoalLevel().getId() );
        if ( null != levelSummary )
        {
          levelSummary.incrementTotalSelected();
          if ( goalCalculationResult.isAchieved() )
          {
            levelSummary.incrementTotalAchieved();
            levelSummary.incrementTotalAward( goalCalculationResult.getCalculatedPayout() );
          }
        }
      }
    }

    return partnerPayouts;
  }

  private List<ParticipantPartner> getEligiblePaxPartners( GoalQuestPromotion promotion,
                                                           Map<Long, List<GoalCalculationResult>> paxGoalResultsByPartner,
                                                           Map<Long, GoalCalculationResult> goalResultsByPax,
                                                           Map<Long, Boolean> payoutEligibilityByPax )
  {
    // Map of eligibility by Partner ID
    Map<Long, Boolean> eligibilityByPartner = new HashMap<Long, Boolean>();

    // Get All Pax Partners for this promotion
    List<ParticipantPartner> paxPartners = participantPartnerDAO.getParticipantPartnersByPromotion( promotion.getId() );

    // Organize partners PAX in a MAP
    for ( Iterator<ParticipantPartner> iter = paxPartners.iterator(); iter.hasNext(); )
    {
      ParticipantPartner paxPartner = iter.next();

      // Check if Partner is in eligible audience
      if ( !isPartnerEligibleForPayout( paxPartner, promotion, payoutEligibilityByPax, eligibilityByPartner ) )
      {
        iter.remove();
        continue;
      }

      paxPartner.setPartner( gethydratedPartnerForExtract( paxPartner.getPartner() ) );
      GoalCalculationResult paxGoalCalculationResult = goalResultsByPax.get( paxPartner.getParticipant().getId() );
      // If PAX who selected the partner has a Goal Result
      if ( null != paxGoalCalculationResult )
      {
        List<GoalCalculationResult> partnerPaxGoalResults = paxGoalResultsByPartner.get( paxPartner.getPartner().getId() );
        if ( null == partnerPaxGoalResults )
        {
          partnerPaxGoalResults = new ArrayList<GoalCalculationResult>();
          paxGoalResultsByPartner.put( paxPartner.getPartner().getId(), partnerPaxGoalResults );
        }
        // Add PAX to the partner list
        partnerPaxGoalResults.add( paxGoalCalculationResult );
      }
    }

    return paxPartners;
  }

  private boolean isPartnerEligibleForPayout( ParticipantPartner paxPartner, GoalQuestPromotion promotion, Map<Long, Boolean> payoutEligibilityByPax, Map<Long, Boolean> eligibilityByPartner )
  {
    Boolean partnerEligibility = (Boolean)eligibilityByPartner.get( paxPartner.getPartner().getId() );
    if ( null == partnerEligibility )
    {
      if ( promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE ) )
      {
        // Partner should be active and should belong to the Partner Audience List
        partnerEligibility = paxPartner.getPartner().isActive().booleanValue() && audienceService.isUserInPromotionPartnerAudiences( paxPartner.getPartner(), promotion.getPartnerAudiences() )
            ? Boolean.TRUE
            : Boolean.FALSE;
      }
      else
      {
        partnerEligibility = Boolean.TRUE;
      }
      eligibilityByPartner.put( paxPartner.getPartner().getId(), partnerEligibility );
    }

    boolean isEligible = false;

    Boolean paxPayoutEligibility = payoutEligibilityByPax.get( paxPartner.getParticipant().getId() );
    // PAX who picked the Partner should be eligible for payout and so should the partner
    if ( null != paxPayoutEligibility && paxPayoutEligibility.booleanValue() && partnerEligibility.booleanValue() )
    {
      isEligible = true;
    }

    return isEligible;
  }

  private Map<Long, GoalQuestAwardSummary> getEmptyGoalQuestAwardSummaryMap( GoalQuestPromotion promotion )
  {
    Map<Long, GoalQuestAwardSummary> summaryMap = new TreeMap<Long, GoalQuestAwardSummary>();
    Set<AbstractGoalLevel> goalLevels = promotion.getGoalLevels();
    if ( goalLevels != null )
    {
      for ( Iterator<AbstractGoalLevel> iter = goalLevels.iterator(); iter.hasNext(); )
      {
        AbstractGoalLevel goalLevel = iter.next();
        GoalQuestAwardSummary goalQuestAwardSummary = new GoalQuestAwardSummary();
        goalQuestAwardSummary.setGoalLevel( goalLevel );
        goalQuestAwardSummary.setTotalAward( new BigDecimal( "0" ) );
        summaryMap.put( goalLevel.getId(), goalQuestAwardSummary );
      }
    }
    return summaryMap;
  }

  private Participant gethydratedPartnerForExtract( User user )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.PHONE ) );

    return (Participant)userService.getUserByIdWithAssociations( user.getId(), associationRequestCollection );
  }

  public void setGoalStrategyFactory( GoalPayoutStrategyFactory goalStrategyFactory )
  {
    this.goalStrategyFactory = goalStrategyFactory;
  }

  public void setManagerOverrideGoalStrategyFactory( ManagerOverrideGoalStrategyFactory managerOverrideGoalStrategyFactory )
  {
    this.managerOverrideGoalStrategyFactory = managerOverrideGoalStrategyFactory;
  }

  public void setPartnerGoalStrategyFactory( PartnerGoalStrategyFactory partnerGoalStrategyFactory )
  {
    this.partnerGoalStrategyFactory = partnerGoalStrategyFactory;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public GoalQuestPaxActivityService getGoalQuestPaxActivityService()
  {
    return goalQuestPaxActivityService;
  }

  public void setGoalQuestPaxActivityService( GoalQuestPaxActivityService goalQuestPaxActivityService )
  {
    this.goalQuestPaxActivityService = goalQuestPaxActivityService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setParticipantPartnerDAO( ParticipantPartnerDAO participantPartnerDAO )
  {
    this.participantPartnerDAO = participantPartnerDAO;
  }

  public void setListBuilderService( ListBuilderService listBuilderService )
  {
    this.listBuilderService = listBuilderService;
  }

  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  /**
   * Get the Encrypted External Survey Link
   * Use 3DES for Encryption
   * @author kumarse
   * @param 
   * 
   */
  public String getEncyptedExternalSurveyLink( String url )
  {
    return GoalQuestSurveyUtils.encrypt( url );
  }

  /**
   * Get the promotion and user id and survey type information 
   * Survey Type ex. WINNER, POSTCHOICE Etc.
   * Use 3DES to decrypt
   * @author kumarse
   * @param encryptedSurveyQueryParam
   * @return Map with promotionId, userId and Survey Type
   * 
   */
  public Map<String, String> getSurveyPromoInformation( String encryptedSurveyLnk )
  {
    // Decrypt the url received from participant email - Use 3DES for decryption.
    // Passing only Query string to Decrypt - Encrypt will have only queryParams after "?" in the
    // url
    String surveyLnk = "";
    String decryptedSurveyLnk = GoalQuestSurveyUtils.decrypt( encryptedSurveyLnk, "" );
    if ( decryptedSurveyLnk != null )
    {
      Map<String, String> surveyMap = GoalQuestSurveyUtils.urlTokenizer( decryptedSurveyLnk );
      return surveyMap;
    }

    return new HashMap<String, String>();
  }

  public boolean isParticipantPayoutComplete( Long userId, Long promotionId )
  {
    return goalQuestPaxActivityDAO.isParticipantPayoutComplete( userId, promotionId );
  }

  public List<ParticipantSearchView> getGoalQuestMiniProfilesForUserAndPromotion( Long promotionId, Long userId )
  {
    List<ParticipantSearchView> paxList = null;
    Map results = participantService.getParticipatForMiniProfile( promotionId.toString(), userId.toString(), getUserLocale().toString() );

    if ( BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
    {
      try
      {
        throw new Exception( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
    }
    else
    {
      paxList = (List<ParticipantSearchView>)results.get( "p_out_data" );
    }
    return paxList;
  }

  public void setCharacteristicDAO( CharacteristicDAO characteristicDAO )
  {
    this.characteristicDAO = characteristicDAO;
  }

  public boolean isParticipantInUserCharType( Participant participant, GoalQuestPromotion promotion )
  {
    boolean isValid = false;

    List<Characteristic> characteristics = characteristicDAO.getAllCharacteristics();
    Characteristic charac = characteristicDAO.getCharacteristicById( new Long( promotion.getPreSelectedPartnerChars() ) );
    String paxCharValue = null;
    Characteristic characteristic = null;
    if ( characteristics != null )
    {
      for ( Iterator<Characteristic> iter = characteristics.iterator(); iter.hasNext(); )
      {
        characteristic = iter.next();
        if ( characteristic.getCharacteristicName().equalsIgnoreCase( charac.getCharacteristicName() ) )
        {
          AssociationRequestCollection associationReq = new AssociationRequestCollection();
          associationReq.add( new UserAssociationRequest( UserAssociationRequest.CHARACTERISTIC ) );
          participant = getParticipantService().getParticipantByIdWithAssociations( participant.getId(), associationReq );

          Set<UserCharacteristic> userChars = participant.getUserCharacteristics();
          Iterator<UserCharacteristic> itr = userChars.iterator();
          while ( itr.hasNext() )
          {
            UserCharacteristic userChar = itr.next();
            if ( userChar.getUserCharacteristicType().getCharacteristicName().equalsIgnoreCase( charac.getCharacteristicName() ) )
            {
              paxCharValue = userChar.getCharacteristicValue();
              break;
            }
          }
          if ( paxCharValue != null )
          {
            List userCharacteristicsList = getParticipantService().getAllForCharIDAndValue( characteristic.getId(), paxCharValue );
            if ( userCharacteristicsList != null )
            {
              for ( Iterator iterator = userCharacteristicsList.iterator(); iterator.hasNext(); )
              {
                UserCharacteristic userChar = (UserCharacteristic)iterator.next();
                if ( userChar.getUser().getId().equals( UserManager.getUserId() ) )
                {
                  isValid = true;
                  break;
                }
              }
            }
          }
        }
      }
    }

    return isValid;
  }

  public boolean isParticipantInNodeType( Participant participant, GoalQuestPromotion promotion )
  {
    boolean isValid = false;

    AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
    ascReqColl.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
    participant = getParticipantService().getParticipantByIdWithAssociations( participant.getId(), ascReqColl );

    Participant pax = getParticipantService().getParticipantById( UserManager.getUserId() );

    Set<UserNode> nodes = userService.getUserNodes( UserManager.getUserId() );
    if ( nodes != null && nodes.size() > 0 )
    {
      for ( UserNode userNode : nodes )
      {
        if ( pax.isManagerOf( userNode.getNode() ) || pax.isOwnerOf( userNode.getNode() ) )
        {
          List<User> usersInNode = userService.getAllParticipantsOnNode( userNode.getNode().getId() );

          for ( User user : usersInNode )
          {
            if ( user.getId().equals( participant.getId() ) )
            {
              isValid = true;
              break;
            }
          }
        }
      }
    }
    return isValid;
  }

  public ParticipantPartner saveParticipantPartnerAssoc( ParticipantPartner participantPartnerAssoc )
  {
    return participantPartnerDAO.saveParticipantPartnerAssoc( participantPartnerAssoc );
  }

  @Override
  public void getJournalsDeposited( List<DepositProcessBean> depositProcessPointsList, Long promotionId )
  {
    Process process = processService.createOrLoadSystemProcess( PointsDepositProcess.PROCESS_NAME, PointsDepositProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "depositProcessPointsList", depositProcessPointsList );
    parameterValueMap.put( "promotionId", promotionId );
    parameterValueMap.put( "isRetriable", String.valueOf( Boolean.FALSE ) ); //bug 73458

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processService.scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );

  }

  @Override
  public void createMerchOrders( List<DepositProcessBean> depositProcessMerchList, Long promotionId )
  {
    Process process = processService.createOrLoadSystemProcess( MerchOrderCreateProcess.PROCESS_NAME, MerchOrderCreateProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "depositProcessMerchList", depositProcessMerchList );
    parameterValueMap.put( "promotionId", promotionId );
    parameterValueMap.put( "retry", "0" ); //bug 66870
    parameterValueMap.put( "isRetriable", String.valueOf( Boolean.TRUE ) ); //bug 66870

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processService.scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );

  }
  
  @Override
  public List<PromotionView> getHoneycombProgramDetails( Participant participant )
  {
    List<PromotionView> views = new ArrayList<>();

    // Without the honeycomb user ID, the call will fail. Need user ID back from account sync, first.
    if ( participant.getHoneycombUserId() == null )
    {
      return new ArrayList<>();
    }

    // Get details from honeycomb. Don't want to let an exception break the results completely. Stopping
    // exception here.
    GoalquestDetailsResponse hcDetails = null;
    try
    {
      hcDetails = getHCServices().getGoalquestProgramDetails( participant.getHoneycombUserId() );
    }
    catch( Exception e )
    {
      log.error( "Error getting goalquest program details from Honeycomb. Returning empty list.", e );
      return new ArrayList<>();
    }
    
    if ( hcDetails == null || hcDetails.getPrograms() == null )
    {
      return new ArrayList<>();
    }

    // Map from honeycomb data structure to G data structure. Again catching any exception to avoid site
    // breaking.
    Iterator<GoalquestProgramResponse> hcGoalIterator = hcDetails.getPrograms().iterator();
    while ( hcGoalIterator.hasNext() )
    {
      GoalquestProgramResponse programResponse = hcGoalIterator.next();
      try
      {
        views.add( buildPromotionView( participant, programResponse ) );
      }
      catch( Exception e )
      {
        log.error( "Error building goalquest promotion view from Honeycomb", e );
      }
    }

    return views;
  }

  private PromotionView buildPromotionView( Participant participant, GoalquestProgramResponse programResponse )
  {
    PromotionView view = new PromotionView();
    EligibleProgramVO program = programResponse.getProgram();
    GoalSelectionResponse goalSelection = programResponse.getGoalSelection();

    String displayableStartDate = getHCServices().convertDateString( program.getGoalSelectionFromDate() );
    String displayableEndDate = getHCServices().convertDateString( program.getEndDate() );

    view.setId( String.valueOf( program.getProgramId() ) );
    view.setName( program.getProgramName() );
    view.setStartDate( getHCServices().convertDateString( program.getGoalSelectionToDate() ) );

    HoneycombGoalQuestViewStatus status = HoneycombGoalQuestViewStatus.findByHCCode( program.getProgramViewStatus() );
    view.setStatus( status == null ? "ended" : status.getgCode() );

    view.setPromoStartDate( displayableStartDate );
    view.setPromoEndDate( displayableEndDate );
    view.setFinalProcessDate( displayableEndDate );
    view.setAwardIssueRun( "final_issuance".equals( program.getProgramViewStatus() ) ? "true" : "false" );
    // Honeycomb does not have a display end date, yet. (They will in the future.) For now, always
    // display.
    view.setTileDisplayEndDate( "01/01/2100" );
    view.setUa( false );

    // Goals
    List<GoalsView> goalsList = new ArrayList<>();
    GoalsView goal = new GoalsView();
    goal.setIsPartner( false );
    boolean beforeGoalSelectionEnd = new Date().before( DateUtils.toDateWithPattern( program.getGoalSelectionToDate(), HCServices.HONEYCOMB_DATE_FORMAT ) );
    goal.setCanChange( "goal_selection".equals( program.getProgramViewStatus() )
                       || ( "goal_selected".equals( program.getProgramViewStatus() ) && beforeGoalSelectionEnd ) );
    goal.setShowProgress( true );
    if ( goalSelection != null && goalSelection.getPaxProgressResponse() != null )
    {
      if ( goalSelection.getPaxProgressResponse().getProgressPercentage() != null )
      {
        goal.setProgressValue( goalSelection.getPaxProgressResponse().getProgressPercentage().toString() );
      }
      else
      {
        goal.setProgressValue( null );
      }
      if ( StringUtils.isNotBlank( goalSelection.getPaxProgressResponse().getDateThrough() ) )
      {
        goal.setProgressDate( getHCServices().convertDateString( goalSelection.getPaxProgressResponse().getDateThrough() ) );
      }

      // If MQ enabled, they must achieve both the minimum qualifier and the goal
      boolean goalAchieved = false;
      if ( goal.getProgressValue() != null && Integer.valueOf( goal.getProgressValue() ) >= 100 )
      {
        goalAchieved = true;
      }
      if ( program.getIsMinimumQualifierEnabled() && !goalSelection.getPaxProgressResponse().getIsMinQualifierAchieved() )
      {
        goalAchieved = false;
      }
      goal.setIsAchieved( goalAchieved );

      goal.setPercentageExceeds( goal.getProgressValue() != null && Integer.valueOf( goal.getProgressValue() ) >= 100 );
    }

    buildGoalquestSSOLinks( goal, programResponse );

    // Goal Levels
    if ( goalSelection != null && goalSelection.getPaxGoalSelection() != null )
    {
      GoalLevelView goalLevel = new GoalLevelView();
      goalLevel.setName( goalSelection.getPaxGoalSelection().getName() );
      goalLevel.setDescription( goalSelection.getPaxGoalSelection().getDescription() );
      goal.setGoalLevel( goalLevel );
    }

    // Participant. Use our participant object. hcGoal.userInfo doesn't seem to come back reliably.
    ParticipantView participantView = new ParticipantView();
    participantView.setId( String.valueOf( participant.getId() ) );
    participantView.setFirstName( participant.getFirstName() );
    participantView.setLastName( participant.getLastName() );
    goal.setParticipant( participantView );

    goalsList.add( goal );
    view.setGoals( goalsList );
    view.setHoneycombProgram( true );

    return view;
  }

  private void buildGoalquestSSOLinks( GoalsView goalsView, GoalquestProgramResponse programResponse )
  {
    String programRole = GoalquestProgramRole.PARTICIPANT.getHoneycombCode();
    String programId = programResponse.getProgram().getProgramId().toString();
    goalsView.setSelectGoalLink( buildGoalquestSSOLink( programRole, programId, null ) );
    goalsView.setRulesLink( buildGoalquestSSOLink( programRole, programId, "rules" ) );
    goalsView.setProgressLink( buildGoalquestSSOLink( programRole, programId, null ) );
    goalsView.setResultsLink( buildGoalquestSSOLink( programRole, programId, null ) );
  }
  
  @Override
  public String buildGoalquestSSOLink( String programRole, String programId, String section )
  {
    String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map<String, String> parameterMap = new HashMap<>();
    parameterMap.put( "programRole", programRole );
    parameterMap.put( "programId", programId );
    if ( section != null )
    {
      parameterMap.put( "section", section );
    }
    return ClientStateUtils.generateEncodedLink( siteUrl, "/goalquest/honeycombSSO.do?method=honeycombSSO", parameterMap );
  }
  
  @Override
  public List<ManagerGoalquestViewBean> getHoneycombManagerPrograms( Long honeycombUserId )
  {
    List<ManagerGoalquestViewBean> views = new ArrayList<>();

    // Without the honeycomb user ID, the call will fail. Need user ID back from account sync, first.
    if ( honeycombUserId == null )
    {
      return new ArrayList<>();
    }

    // Get details from honeycomb. Don't want to let an exception break the results completely. Stopping
    // exception here.
    GoalquestDetailsResponse hcDetails = null;
    try
    {
      hcDetails = getHCServices().getGoalquestManagerPrograms( honeycombUserId );
    }
    catch( Exception e )
    {
      log.error( "Error getting goalquest manager program details from Honeycomb. Returning empty list.", e );
      return new ArrayList<>();
    }
    
    if ( hcDetails == null )
    {
      return new ArrayList<>();
    }

    // Map from honeycomb data structure to G data structure. Again catching any exception to avoid site
    // breaking.
    try
    {
      Iterator<GoalquestProgramResponse> hcGoalIterator = hcDetails.getPrograms().iterator();
      while ( hcGoalIterator.hasNext() )
      {
        GoalquestProgramResponse programResponse = hcGoalIterator.next();
        if ( programResponse != null && programResponse.getProgram() != null )
        {
          views.add( buildGoalquestManagerView( programResponse ) );
        }
      }
    }
    catch( Exception e )
    {
      log.error( "Error building goalquest promotion views from Honeycomb", e );
      return new ArrayList<>();
    }

    return views;
  }

  private ManagerGoalquestViewBean buildGoalquestManagerView( GoalquestProgramResponse hcProgram )
  {
    ManagerGoalquestViewBean view = new ManagerGoalquestViewBean();

    String displayableStartDate = getHCServices().convertDateString( hcProgram.getProgram().getStartDate() );
    String displayableEndDate = getHCServices().convertDateString( hcProgram.getProgram().getEndDate() );

    view.setHoneycombProgram( true );
    view.setPromotionId( hcProgram.getProgram().getProgramId() );
    view.setPromotionName( hcProgram.getProgram().getProgramName() );
    view.setRole( hcProgram.getProgram().getRole() );
    view.setStartDate( displayableStartDate );
    view.setEndDate( displayableEndDate );
    view.setDetailsLink( buildGoalquestSSOLink( hcProgram.getProgram().getRole(), hcProgram.getProgram().getProgramId().toString(), null ) );

    return view;
  }
  
  @Override
  public List<PromotionMenuBean> getManagerPromotionMenuBeans( List<PromotionMenuBean> eligiblePromotions, Participant participant, String promotionTypeCode )
  {
    List<PromotionMenuBean> goalQuestPromotionMenuBeans = new ArrayList<PromotionMenuBean>();

    PromotionType promotionType = PromotionType.lookup( promotionTypeCode );

    for ( PromotionMenuBean promotionBean : eligiblePromotions )
    {
      if ( promotionBean.getPromotion().getPromotionType().getCode().equals( promotionType.getCode() ) && promotionBean.getPromotion().isWebRulesActive() )
      {
        Long promotionId = promotionBean.getPromotion().getId();
        AssociationRequestCollection webRulesAudienceAssociation = new AssociationRequestCollection();
        webRulesAudienceAssociation.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );
        webRulesAudienceAssociation.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );

        GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, webRulesAudienceAssociation );

        boolean isUserInPromotionManagerWebRulesAudiences = false;
        boolean isUserInPromotionPrimaryAudiences = false;

        if ( goalQuestPromotion != null )
        {
          if ( goalQuestPromotion.getManagerWebRulesAudienceType() != null && goalQuestPromotion.getManagerWebRulesAudienceType().getCode().equals( ManagerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) )
          {
            isUserInPromotionManagerWebRulesAudiences = true;
          }
          else if ( goalQuestPromotion.getManagerWebRulesAudienceType().getCode().equals( ManagerWebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) )
          {
            if ( goalQuestPromotion.getPrimaryAudienceType() != null && goalQuestPromotion.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
            {
              isUserInPromotionPrimaryAudiences = true;
            }
            else
            {
              isUserInPromotionPrimaryAudiences = getAudienceService().isUserInPromotionAudiences( participant, goalQuestPromotion.getPromotionPrimaryAudiences() );
            }
          }
          else
          {
            isUserInPromotionManagerWebRulesAudiences = getAudienceService().isUserInPromotionAudiences( participant, goalQuestPromotion.getPromotionManagerWebRulesAudience() );
          }
        }

        if ( isUserInPromotionPrimaryAudiences || isUserInPromotionManagerWebRulesAudiences )
        {
          goalQuestPromotionMenuBeans.add( promotionBean );
        }
      }
    }

    return goalQuestPromotionMenuBeans;
  }
  
  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }
  
  public HCServices getHCServices()
  {
    return (HCServices)BeanLocator.getBean( HCServices.BEAN_NAME );
  }
  
  // This method exists so it can be overridden during unit testing
  Locale getUserLocale()
  {
    return UserManager.getLocale();
  }

}

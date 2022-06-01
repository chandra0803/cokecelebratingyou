
package com.biperf.core.service.throwdown.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.dao.throwdown.MatchTeamOutcomeDAO;
import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.StackStandingActivity;
import com.biperf.core.domain.activity.ThrowdownHeadToHeadActivity;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.BadgeLevelType;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.MatchStatusType;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.TeamUnavailableResolverType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.gamification.BadgeInfo;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.DivisionPayout;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.domain.promotion.StackStandingPayout;
import com.biperf.core.domain.promotion.StackStandingPayoutGroup;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.process.ThrowdownMatchSchedulerProcess;
import com.biperf.core.process.ThrowdownTeamGenerationProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.DivisionCalculationResult;
import com.biperf.core.service.throwdown.MatchAssociationRequest;
import com.biperf.core.service.throwdown.MatchResolution;
import com.biperf.core.service.throwdown.NodeRankingResult;
import com.biperf.core.service.throwdown.StackRankingCalculationResult;
import com.biperf.core.service.throwdown.StackRankingResult;
import com.biperf.core.service.throwdown.StackStandingService;
import com.biperf.core.service.throwdown.TeamCalculationResult;
import com.biperf.core.service.throwdown.TeamRankingResult;
import com.biperf.core.service.throwdown.ThrowdownAudienceValidationResult;
import com.biperf.core.service.throwdown.ThrowdownRoundCalculationResult;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.PromotionRoundValue;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class ThrowdownServiceImpl implements ThrowdownService
{
  private Log log = LogFactory.getLog( ThrowdownServiceImpl.class );
  public static final String OPT_OUT_TEXT = "Opt Out Of Awards";

  private ProcessService processService;
  private MessageService messageService;
  private UserService userService;
  private MailingService mailingService;
  private PromotionService promotionService;
  private ParticipantService participantService;
  private HierarchyService hierarchyService;
  private ListBuilderService listBuilderService;
  private SystemVariableService systemVariableService;
  private JournalService journalService;
  private StackStandingService stackStandingService;
  private GamificationService gamificationService;
  private DivisionDAO divisionDAO;
  private RoundDAO roundDAO;
  private MatchDAO matchDAO;
  private ActivityDAO activityDAO;
  private TeamDAO teamDAO;
  private MatchTeamOutcomeDAO matchTeamOutcomeDAO;

  public Date getNextMatchSchedulerFiringTimeForPromotion( Long promotionId )
  {
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    Division division = promotion.getDivisions().iterator().next();
    Round roundToSchedule = null;
    for ( Round round : division.getRounds() )
    {
      // get the next round that needs matches
      if ( round.getMatches().isEmpty() )
      {
        roundToSchedule = round;
        break;
      }
    }
    // This need to be more involved. We can't schedule the next round's
    // match scheduling might be less than 2 days. Also, the "-2" should be
    // a System variable. Lastly, we need to have a System variable that we can
    // check for testing. If testing, the -2 should represent minutes.
    if ( null != roundToSchedule )
    {
      Calendar scheduleTime = Calendar.getInstance();
      scheduleTime.setTime( roundToSchedule.getStartDate() );
      scheduleTime.add( Calendar.DATE, promotion.getDaysPriorToRoundStartSchedule() * -1 );
      return scheduleTime.getTime();
    }

    return null;
  }

  @Override
  public void buildRoundsForPromotion( Long promotionId )
  {
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    int numberOfRounds = promotion.getNumberOfRounds();
    int lengthOfRound = promotion.getLengthOfRound();

    // build the round for each division in the promotion
    for ( Division division : promotion.getDivisions() )
    {
      Calendar startDate = Calendar.getInstance();
      startDate.setTime( DateUtils.toStartDate( promotion.getHeadToHeadStartDate() ) );
      Date previousRoundsEndDate = null;

      for ( int i = 0; i < numberOfRounds; i++ )
      {
        Calendar roundStartDate = null;
        if ( previousRoundsEndDate == null )
        {
          roundStartDate = startDate;
        }
        else
        {
          Calendar tempStart = Calendar.getInstance();
          tempStart.setTime( previousRoundsEndDate );
          // kick it over to the next day
          tempStart.add( Calendar.DATE, 1 );
          roundStartDate = tempStart;
        }
        Round round = new Round();
        round.setDivision( division );
        round.setPayoutsIssued( false );
        round.setRoundNumber( i + 1 );
        round.setStartDate( DateUtils.toStartDate( roundStartDate.getTime() ) );

        if ( i > 0 )
        {
          startDate.add( Calendar.DATE, lengthOfRound );
        }
        else
        {
          startDate.add( Calendar.DATE, lengthOfRound - 1 );
        }

        round.setEndDate( DateUtils.toEndDate( startDate.getTime() ) );
        previousRoundsEndDate = startDate.getTime();
        division = divisionDAO.getDivision( division.getId() );
        division.getRounds().add( round );
      }
    }
  }

  public void scheduleFirstRound( Long promotionId, Date processStartTime )
  {
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    Process process = getProcessService().createOrLoadSystemProcess( "throwdownTeamGenerationProcess", ThrowdownTeamGenerationProcess.BEAN_NAME );

    Map<String, String[]> parameterMap = new LinkedHashMap<String, String[]>();
    parameterMap.put( "promotionId", new String[] { promotion.getId().toString() } );

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( processStartTime );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    getProcessService().scheduleProcess( process, processSchedule, parameterMap, UserManager.getUserId() );
  }

  public void scheduleMatchesForRound( Long promotionId, int roundNumber, Date processStartTime )
  {
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    Process process = getProcessService().createOrLoadSystemProcess( "throwdownMatchSchedulerProcess", ThrowdownMatchSchedulerProcess.BEAN_NAME );

    Map<String, String[]> parameterMap = new LinkedHashMap<String, String[]>();
    parameterMap.put( "promotionId", new String[] { promotion.getId().toString() } );
    parameterMap.put( "roundNumber", new String[] { String.valueOf( roundNumber ) } );

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( processStartTime );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    getProcessService().scheduleProcess( process, processSchedule, parameterMap, UserManager.getUserId() );
  }

  @SuppressWarnings( "unchecked" )
  private ThrowdownRoundCalculationResult buildThrowdownRoundCalculationResult( Long promotionId, int roundNumber )
  {
    ThrowdownRoundCalculationResult calculationSummary = new ThrowdownRoundCalculationResult();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISION_MATCH_OUTCOMES ) );
    ThrowdownPromotion promotion = getThrowdownPromotionByIdWithAssociations( promotionId, associationRequestCollection );
    calculationSummary.setPromotion( promotion );
    calculationSummary.setRoundNumber( roundNumber );
    return calculationSummary;
  }

  public ThrowdownRoundCalculationResult getHeadToHeadAwardSummaryForRound( Long promotionId, int roundNumber )
  {
    ThrowdownRoundCalculationResult calculationSummary = buildThrowdownRoundCalculationResult( promotionId, roundNumber );
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    List<Round> rounds = roundDAO.getRoundsForPromotionByRoundNumber( promotionId, roundNumber );
    // for each round/division
    for ( Round round : rounds )
    {
      DivisionCalculationResult divisionResults = buildDivisionCalculationResult( round );
      // for each match, produce a payout(s) for those not already marked
      List<Match> matches = matchDAO.getUnplayedMatchesForPromotionAndRound( round.getId() );
      boolean allMatchesProcessed = true;

      for ( Match match : matches )
      {
        try
        {
          // go through the interface to apply the new transaction
          Set<TeamCalculationResult> matchResults = processMatchPayout( promotion, match );
          // ok - the matchResults should be committed to the database, add them to the results
          divisionResults.getTeamResults().addAll( matchResults );
        }
        catch( ServiceErrorExceptionWithRollback e )
        {
          log.error( e.getMessage(), e );
          // ok, make sure we set the flag since there was an error
          calculationSummary.setAnyPayoutFailed( true );
          allMatchesProcessed = false;
        }
      }
      calculationSummary.getDivisionResults().add( divisionResults );
      // mark the round as payout processed (if this is committed)
      round.setPayoutsIssued( allMatchesProcessed );
    }
    return calculationSummary;
  }

  // this method should commit or rollback the entire operation.
  @SuppressWarnings( "unchecked" )
  public Set<TeamCalculationResult> processMatchPayout( ThrowdownPromotion promotion, Match match ) throws ServiceErrorExceptionWithRollback
  {
    Set<TeamCalculationResult> results = new HashSet<TeamCalculationResult>();
    try
    {
      AssociationRequestCollection requestCollection = new AssociationRequestCollection();
      requestCollection.add( new MatchAssociationRequest( MatchAssociationRequest.ALL ) );
      match = matchDAO.getMatch( match.getId(), requestCollection );
      Iterator<MatchTeamOutcome> matchTeamOutcomeIter = match.getTeamOutcomes().iterator();
      MatchTeamOutcome matchTeam1 = matchTeamOutcomeIter.next();
      MatchTeamOutcome matchTeam2 = matchTeamOutcomeIter.next();
      MatchResolution matchResults = null;
      boolean resolvedNow = false;

      // check if any outcomes already resolved and then try to resolve the match
      if ( !isMatchResolved( matchTeam1, matchTeam2 ) )
      {
        matchResults = resolveMatch( matchTeam1, matchTeam2, true );
        // START apply the results to the domain (we skip this for the summary only)
        matchTeam1.setOutcome( matchResults.getOutcomeResults( matchTeam1.getId() ) );
        matchTeam2.setOutcome( matchResults.getOutcomeResults( matchTeam2.getId() ) );
        resolvedNow = true;
      }
      else
      {
        // populate results with updates done earlier
        matchResults = new MatchResolution( matchTeam1, matchTeam1.getOutcome(), matchTeam2, matchTeam2.getOutcome() );
      }

      // create activities/journal if match is resolved now or if activities/journal doesn't exist
      // already
      if ( resolvedNow || !isActivityExistsForOutcome( matchTeam1 ) )
      {
        processPayoutForHeadToHeadMatch( matchTeam1, match.getRound().getDivision() );
      }
      if ( resolvedNow || !isActivityExistsForOutcome( matchTeam2 ) )
      {
        processPayoutForHeadToHeadMatch( matchTeam2, match.getRound().getDivision() );
      }
      // END
      buildTeamResults( match.getRound().getDivision(), matchTeam1, matchTeam1.getOutcome(), results );
      buildTeamResults( match.getRound().getDivision(), matchTeam2, matchTeam2.getOutcome(), results );

      // process un-defeated badges in division pay outs since rankings are optional
      processUndefeatedBadges( matchTeam1 );
      processUndefeatedBadges( matchTeam2 );

      match.setStatus( MatchStatusType.lookup( MatchStatusType.PLAYED ) );

      processEmailForPaxAwards( promotion, matchResults, matchTeam1 );
      processEmailForPaxAwards( promotion, matchResults, matchTeam2 );

      return results;
    }
    catch( Exception e )
    {
      throw new ServiceErrorExceptionWithRollback( e.getMessage(), e );
    }
  }

  private boolean isMatchResolved( MatchTeamOutcome matchTeam1, MatchTeamOutcome matchTeam2 )
  {
    return !matchTeam1.getOutcome().isNone() || !matchTeam2.getOutcome().isNone();
  }

  private void buildTeamResults( Division division, MatchTeamOutcome matchTeamOutcome, MatchTeamOutcomeType outcomeType, Set<TeamCalculationResult> teamCalculationResults )
  {
    if ( matchTeamOutcome.getTeam().isShadowPlayer() )
    {
      return;
    }
    TeamCalculationResult teamResult = new TeamCalculationResult();
    teamResult.setTeam( matchTeamOutcome.getTeam() );
    teamResult.setTotalProgress( matchTeamOutcome.getTotalProgressWithPrecisionAndRounding() );
    teamResult.setOutcomePayoutType( outcomeType );
    DivisionPayout divisionPayout = buildDivisionPayout( division, outcomeType );
    if ( divisionPayout != null && divisionPayout.getPoints() > 0
        && matchTeamOutcome.getTotalProgressWithPrecisionAndRounding().compareTo( matchTeamOutcome.getMatch().getRound().getDivision().getMinimumQualifierWithPrecisionAndRounding() ) >= 0 )
    {
      teamResult.setPayoutAmount( new BigDecimal( divisionPayout.getPoints() ) );
    }
    teamCalculationResults.add( teamResult );
  }

  public void processEmailForPaxAwards( ThrowdownPromotion promotion, MatchResolution matchResults, MatchTeamOutcome matchTeamOutcome ) throws ServiceErrorException
  {
    Participant participant = matchTeamOutcome.getTeam().getParticipant();
    if ( matchTeamOutcome.getTeam().isShadowPlayer() || participant != null && !participant.isActive() )
    {
      return;
    }

    Match match = matchTeamOutcome.getMatch();
    Division division = match.getRound().getDivision();
    Integer payout = new Integer( 0 );
    BigDecimal shadowScore = null;
    if ( promotion != null && participant != null && promotion.isNotificationRequired( PromotionEmailNotificationType.TD_MATCH_OUTCOME ) )
    {
      MatchTeamOutcome currentPlayerOutcome = null;
      MatchTeamOutcome oppositionPlayerOutcome = null;
      for ( MatchTeamOutcome matchTO : match.getTeamOutcomes() )
      {
        if ( !matchTO.getTeam().isShadowPlayer() && participant.getId().equals( matchTO.getTeam().getParticipant().getId() ) )
        {
          currentPlayerOutcome = matchTO;
        }
        else
        {
          oppositionPlayerOutcome = matchTO;
        }
      }

      if ( oppositionPlayerOutcome.getTeam().isShadowPlayer() )
      {
        shadowScore = getShadowScore( match.getRound().getId(), currentPlayerOutcome.getTeam().getId() );
      }

      Message message = null;
      if ( matchResults.getOutcomeResults( currentPlayerOutcome.getId() ).equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.WIN ) ) )
      {
        message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_MATCH_OUTCOME_LEAD_MESSAGE_CM_ASSET_CODE );
      }
      else if ( matchResults.getOutcomeResults( currentPlayerOutcome.getId() ).equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.TIE ) ) )
      {
        message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_MATCH_OUTCOME_TIE_MESSAGE_CM_ASSET_CODE );
      }
      else if ( matchResults.getOutcomeResults( currentPlayerOutcome.getId() ).equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.LOSS ) ) )
      {
        message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_MATCH_OUTCOME_TRAIL_MESSAGE_CM_ASSET_CODE );
      }

      if ( message != null )
      {
        Integer eligiblePax = stackStandingService.getTotalUsersInHierarchyRanking( promotion.getId(), match.getRound().getRoundNumber() );
        eligiblePax = eligiblePax != null ? eligiblePax : 0;

        StackStandingParticipant rankingDetails = stackStandingService.getHierarchyRankDetailsForPax( promotion.getId(), match.getRound().getRoundNumber(), participant.getId() );
        Long rankingPayout = null;
        BigDecimal cumulativeTotal = null;
        Integer rank = null;

        if ( rankingDetails != null )
        {
          rankingPayout = rankingDetails.getPayout();
          rankingPayout = rankingPayout != null ? rankingPayout : 0;
          cumulativeTotal = rankingDetails.getStackStandingFactor();
          cumulativeTotal = cumulativeTotal != null ? cumulativeTotal : BigDecimal.ZERO;
          rank = rankingDetails.getStanding();
          rank = rank != null ? rank : 0;
        }

        DivisionPayout divisionPayout = buildDivisionPayout( division, currentPlayerOutcome.getOutcome() );
        if ( divisionPayout != null && divisionPayout.getPoints() > 0
            && matchTeamOutcome.getTotalProgressWithPrecisionAndRounding().compareTo( division.getMinimumQualifierWithPrecisionAndRounding() ) >= 0 )
        {
          payout = divisionPayout.getPoints();
        }

        MailingRecipient mailingRecipient = mailingService
            .buildMailingRecipientForThrowdownEmail( promotion,
                                                     participant,
                                                     match,
                                                     currentPlayerOutcome,
                                                     oppositionPlayerOutcome,
                                                     rank,
                                                     payout,
                                                     eligiblePax,
                                                     cumulativeTotal,
                                                     rankingPayout,
                                                     null,
                                                     shadowScore );
        // Create mailing object
        Mailing mailing = new Mailing();
        mailing.setMessage( message );
        mailing.addMailingRecipient( mailingRecipient );
        mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
        mailing.setSender( "Throwdown Promotion" );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setGuid( GuidUtils.generateGuid() );
        mailingService.submitMailing( mailing, null );
      }
    }

    // email sent to participants for points deposited
    if ( promotion != null && participant != null && payout.intValue() > 0 )
    {
      Message message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_POINTS_DEPOSITED_MESSAGE_CM_ASSET_CODE );
      if ( message != null )
      {
        MailingRecipient mailingRecipient = mailingService.buildMailingRecipientForThrowdownEmail( promotion, participant, match, null, null, 0, payout, null, null, null, null, shadowScore );
        // Create mailing object
        Mailing mailing = new Mailing();
        mailing.setMessage( message );
        mailing.addMailingRecipient( mailingRecipient );
        mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
        mailing.setSender( "Throwdown Promotion" );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setGuid( GuidUtils.generateGuid() );
        mailingService.submitMailing( mailing, null );
      }
    }
  }

  @SuppressWarnings( "rawtypes" )
  public void sendEndOfRoundEmailToPaxManager( ThrowdownPromotion promotion, List<Round> rounds )
  {
    Round round = !rounds.isEmpty() ? rounds.iterator().next() : null;
    if ( round == null )
    {
      return;
    }

    // sending promotion launch email to eligible audience managers.
    Map<User, Set<Participant>> managers = new HashMap<User, Set<Participant>>();
    managers = participantService.getManagerForCompetitorAudience( promotion.getId() );

    if ( managers != null )
    {
      for ( User user : managers.keySet() )
      {
        Message message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_MANAGER_ROUND_END_MESSAGE_CM_ASSET_CODE );

        StringBuilder sb = new StringBuilder();
        sb.append( "<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">" );
        sb.append( "<tr><td><b>" + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.NAME" ) + "</b></td><td><b>"
            + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.ROUND" ) + " " + round.getRoundNumber() + " "
            + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.TOTAL" ) + "</b></td><td><b>Win/Loss/Tie</b></td><td><b>"
            + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.ALL_ROUND_CUMULATIVE_TOTAL" ) + "</b></td><td><b>"
            + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.OVERALL_RANKING" ) + "</b></td><td><b>"
            + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.BADGE_EARNED" ) + "</b></td></tr>" );
        boolean hasAnyPaxGotPaidOut = false;
        for ( Participant participant : managers.get( user ) )
        {
          MatchTeamOutcome participantOutcome = matchTeamOutcomeDAO.getOutcomeForTeamInSpecificRound( participant.getId(), round.getRoundNumber(), promotion.getId() );
          // participant might be competitor, but may not have match for round being processed and
          // hence that is also handled
          if ( participantOutcome != null && !participantOutcome.getOutcome().equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.NONE ) ) )
          {
            hasAnyPaxGotPaidOut = true;
            BigDecimal progress = participantOutcome.getCurrentValueWithPrecisionAndRounding() != null ? participantOutcome.getCurrentValueWithPrecisionAndRounding() : BigDecimal.ZERO;
            StackStandingParticipant rankingDetails = stackStandingService.getHierarchyRankDetailsForPax( promotion.getId(), round.getRoundNumber(), participant.getId() );
            Integer rank = null;
            BigDecimal cumulativeTotal = null;
            if ( rankingDetails != null )
            {
              cumulativeTotal = rankingDetails.getStackStandingFactor();
              rank = rankingDetails.getStanding();
            }
            List<BadgeInfo> badges = getGamificationService().getTDRankingBadgesEarnable( promotion.getId(),
                                                                                          getHierarchyPlaceHolderNodeType(),
                                                                                          rank,
                                                                                          BadgeLevelType.lookup( BadgeLevelType.STACK_STAND ) );
            String badge = "";
            if ( !badges.isEmpty() && !badges.iterator().next().getBadgeDetails().isEmpty() )
            {
              Map paxBadgeMap = new HashMap();

              paxBadgeMap = buildTDBadgeHtmlString( badges.iterator().next().getBadgeDetails().iterator().next() );
              String paxBadgeEarnedString = "";
              if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
              {
                paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
              }
              if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
              {
                badge = paxBadgeEarnedString;
              }
            }
            sb.append( "<tr><td>" + participant.getNameFLNoComma() + "</td><td>" + progress + "</td><td>" + participantOutcome.getOutcome().getName() + "</td><td>" + cumulativeTotal + "</td><td>"
                + rank + "</td><td>" + badge + "</td></tr>" );
          }
        }
        sb.append( "</table>" );

        if ( !hasAnyPaxGotPaidOut )
        {
          // dont sent manager email if no participants under his team got match resolved and paid
          // out.
          continue;
        }

        MailingRecipient mailingRecipient = mailingService.buildMailingRecipientForThrowdownEmail( promotion, sb.toString(), round, DateUtils.getCurrentDate(), user );
        // Create mailing object
        Mailing mailing = new Mailing();
        mailing.setMessage( message );
        mailing.addMailingRecipient( mailingRecipient );
        mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
        mailing.setSender( "Throwdown Promotion" );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setGuid( GuidUtils.generateGuid() );
        mailingService.submitMailing( mailing, null );
      }
    }
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public Map buildTDBadgeHtmlString( BadgeDetails badgeDetail )
  {
    StringBuilder paxBadgesEarnedStr = new StringBuilder( "" );
    String badgeImageUrl = "";
    Map paxBadgeMap = new HashMap();

    paxBadgesEarnedStr.append( "<br />" );
    paxBadgesEarnedStr.append( "<table>" );
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    badgeImageUrl = siteUrlPrefix + "/" + badgeDetail.getImg();
    paxBadgesEarnedStr.append( "<br />" );
    paxBadgesEarnedStr.append( "<tr align='center'><td>" );
    paxBadgesEarnedStr.append( "<img src='" + badgeImageUrl + "'/>" );
    paxBadgesEarnedStr.append( "</td></tr><tr align='center'><td>" );
    paxBadgesEarnedStr.append( badgeDetail.getBadgeName() + "</td></tr>" );
    paxBadgesEarnedStr.append( "</table>" );

    paxBadgeMap.put( "paxBadgeEarnedString", paxBadgesEarnedStr );
    return paxBadgeMap;
  }

  private NodeType getHierarchyPlaceHolderNodeType()
  {
    NodeType all = new NodeType();
    all.setId( -1L );
    all.setCmAssetCode( "system.general" );
    all.setNameCmKey( "ALL" );
    return all;
  }

  @SuppressWarnings( "unchecked" )
  public ThrowdownAudienceValidationResult getAudienceValidationResults( Set<Division> divisions )
  {
    ThrowdownAudienceValidationResult results = new ThrowdownAudienceValidationResult();
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();
    ProjectionCollection collection = new ProjectionCollection();
    collection.add( new ProjectionAttribute( "id" ) );
    collection.add( new ProjectionAttribute( "firstName" ) );
    collection.add( new ProjectionAttribute( "lastName" ) );
    collection.add( new ProjectionAttribute( "userName" ) ); // heh, needed for the equals method!

    for ( Division division : divisions )
    {
      Set<Audience> audiences = new HashSet<Audience>();
      for ( DivisionCompetitorsAudience competitorAudience : division.getCompetitorsAudience() )
      {
        audiences.add( competitorAudience.getAudience() );
      }

      List<FormattedValueBean> paxesInAudience = (List<FormattedValueBean>)getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, false );
      List<Long> participantIds = new ArrayList<Long>();

      for ( FormattedValueBean paxBean : paxesInAudience )
      {
        participantIds.add( paxBean.getId() );
      }
      results.addDivisionAudienceParticipants( division, getParticipantService().getParticipantsByIdWithProjections( participantIds, collection ) );
    }
    return results;
  }

  private DivisionCalculationResult buildDivisionCalculationResult( Round round )
  {
    DivisionCalculationResult divisionResults = new DivisionCalculationResult();
    divisionResults.setDivision( round.getDivision() );
    divisionResults.setRound( round );
    return divisionResults;
  }

  public ThrowdownRoundCalculationResult generateHeadToHeadAwardSummaryForRound( Long promotionId, int roundNumber )
  {
    ThrowdownRoundCalculationResult calculationSummary = buildThrowdownRoundCalculationResult( promotionId, roundNumber );

    List<Round> rounds = roundDAO.getRoundsForPromotionByRoundNumber( promotionId, roundNumber );
    // for each round/division
    for ( Round round : rounds )
    {
      DivisionCalculationResult divisionResults = buildDivisionCalculationResult( round );
      // for each match, produce a payout(s) for those not already marked
      List<Match> matches = matchDAO.getUnplayedMatchesForPromotionAndRound( round.getId() );

      for ( Match match : matches )
      {
        Iterator<MatchTeamOutcome> matchTeamOutcomeIter = match.getTeamOutcomes().iterator();
        MatchTeamOutcome matchTeam1 = matchTeamOutcomeIter.next();
        MatchTeamOutcome matchTeam2 = matchTeamOutcomeIter.next();
        // don't alter the match status - this is just a summary request
        MatchResolution matchResults = resolveMatch( matchTeam1, matchTeam2 );

        buildTeamResults( divisionResults, matchTeam1, matchResults.getOutcomeResults( matchTeam1.getId() ) );
        buildTeamResults( divisionResults, matchTeam2, matchResults.getOutcomeResults( matchTeam2.getId() ) );
      }
      calculationSummary.getDivisionResults().add( divisionResults );
    }
    return calculationSummary;
  }

  private void buildTeamResults( DivisionCalculationResult divisionResults, MatchTeamOutcome matchTeamOutcome, MatchTeamOutcomeType outcomeType )
  {
    if ( matchTeamOutcome.getTeam().isShadowPlayer() )
    {
      return;
    }
    TeamCalculationResult teamResult = new TeamCalculationResult();
    teamResult.setTeam( matchTeamOutcome.getTeam() );
    teamResult.setTotalProgress( matchTeamOutcome.getTotalProgressWithPrecisionAndRounding() );
    teamResult.setOutcomePayoutType( outcomeType );
    DivisionPayout divisionPayout = buildDivisionPayout( divisionResults.getDivision(), outcomeType );
    if ( divisionPayout != null && divisionPayout.getPoints() > 0
        && matchTeamOutcome.getTotalProgressWithPrecisionAndRounding().compareTo( matchTeamOutcome.getMatch().getRound().getDivision().getMinimumQualifierWithPrecisionAndRounding() ) >= 0 )
    {
      teamResult.setPayoutAmount( new BigDecimal( divisionPayout.getPoints() ) );
    }
    divisionResults.getTeamResults().add( teamResult );
  }

  @SuppressWarnings( "unchecked" )
  private StackRankingCalculationResult buildStackRankingCalculationResult( Long promotionId, int roundNumber, boolean pay )
  {
    StackRankingCalculationResult calculationResult = new StackRankingCalculationResult();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );
    ThrowdownPromotion promotion = getThrowdownPromotionByIdWithAssociations( promotionId, associationRequestCollection );

    StackStanding roundRanking = stackStandingService.getUnapprovedRankingForPromotionAndRound( promotionId, roundNumber );
    StackRankingResult roundStackResult = buildStackRankingResult( promotion, roundRanking, roundNumber, pay );
    calculationResult.setRoundStackRanking( roundStackResult );

    return calculationResult;
  }

  private StackRankingResult buildStackRankingResult( ThrowdownPromotion promotion, StackStanding ranking, Integer roundNumber, boolean pay )
  {
    StackRankingResult stackResult = new StackRankingResult();
    stackResult.setPromotion( promotion );
    stackResult.setRoundNumber( roundNumber );
    boolean isLastRound = roundNumber == promotion.getNumberOfRounds();
    try
    {
      if ( ranking != null )
      {
        for ( StackStandingNode rankingNode : ranking.getStackStandingNodes() )
        {
          StackStandingPayoutGroup payoutGroup = rankingNode.isHierarchyRanking()
              ? promotion.getHierarchyStackStandingPayoutGroup()
              : promotion.getStackStandingPayoutGroup( rankingNode.getNode().getNodeType() );
          if ( payoutGroup.hasPayout() )
          {
            NodeRankingResult nodeResult = buildNodeRankingResult( rankingNode, payoutGroup, pay, isLastRound );
            stackResult.addNodeRanking( nodeResult );
          }
        }
        if ( pay )
        {
          ranking.setPayoutsIssued( true );
        }
      }
    }
    catch( ServiceErrorException e )
    {
      stackResult.setAnyPayoutFailed( true );
      e.printStackTrace();
    }
    return stackResult;
  }

  private NodeRankingResult buildNodeRankingResult( StackStandingNode rankingNode, StackStandingPayoutGroup payoutGroup, boolean pay, boolean isLastRound ) throws ServiceErrorException
  {
    NodeRankingResult nodeResult = new NodeRankingResult();
    nodeResult.setNode( rankingNode.getNode() );
    for ( StackStandingParticipant pax : rankingNode.getStackStandingParticipants() )
    {
      if ( !pax.isPayoutsIssued() )
      {
        TeamRankingResult teamResult = buildTeamRankingResult( pax, payoutGroup );
        nodeResult.addTeamResult( teamResult );
        if ( pay )
        {
          if ( isActiveParticipant( teamResult ) && hasAwardPayout( teamResult ) )
          {
            pax.setPayout( new Long( teamResult.getPayoutAmount() ) );
            processRankingPayout( pax );
          }
          if ( isActiveParticipant( teamResult ) )
          {
            processRoundRankingBadges( pax, rankingNode.getNodeTypeName() );
            if ( isLastRound )
            {
              processOverallRankingBadges( pax, rankingNode.getNodeTypeName() );
            }
          }
          pax.setPayoutsIssued( true );
        }
      }
    }
    return nodeResult;
  }

  public void processRankingPayout( TeamRankingResult teamResult, boolean isLastRound ) throws ServiceErrorExceptionWithRollback
  {
    try
    {
      StackStandingParticipant pax = teamResult.getRankingPax();
      if ( isActiveParticipant( teamResult ) && hasAwardPayout( teamResult ) )
      {
        pax.setPayout( new Long( teamResult.getPayoutAmount() ) );
        if ( !isActivityExistsForRankingPax( pax ) )
        {
          processRankingPayout( pax );
        }
      }
      if ( isActiveParticipant( teamResult ) )
      {
        processRoundRankingBadges( pax, pax.getStackStandingNode().getNodeTypeName() );
        if ( isLastRound )
        {
          processOverallRankingBadges( pax, pax.getStackStandingNode().getNodeTypeName() );
        }
      }
      pax.setPayoutsIssued( true );
      stackStandingService.saveStackStandingParticipant( pax );
    }
    catch( Exception e )
    {
      throw new ServiceErrorExceptionWithRollback( e.getMessage(), e );
    }
  }

  private TeamRankingResult buildTeamRankingResult( StackStandingParticipant pax, StackStandingPayoutGroup payoutGroup )
  {
    TeamRankingResult teamResult = new TeamRankingResult();
    Team tdTeam = new Team();
    tdTeam.setParticipant( pax.getParticipant() );
    teamResult.setTeam( tdTeam );
    teamResult.setRank( pax.getStanding() );
    teamResult.setTied( pax.isTied() );
    teamResult.setRankingFactor( pax.getStackStandingFactor() );
    teamResult.setRankingPax( pax );
    // apply ranking payouts
    applyRankingPayout( teamResult, payoutGroup );
    return teamResult;
  }

  private void applyRankingPayout( TeamRankingResult team, StackStandingPayoutGroup payoutGroup )
  {
    for ( StackStandingPayout payout : payoutGroup.getPromotionStackStandingPayout() )
    {
      if ( team.getRank() <= payout.getEndStanding() && team.getRank() >= payout.getStartStanding() )
      {
        team.setPayoutAmount( payout.getPayout() );
        break;
      }
    }
  }

  private Activity processRankingPayout( StackStandingParticipant pax ) throws ServiceErrorException
  {
    return createActivityForRanking( pax );
  }

  private Activity createActivityForRanking( StackStandingParticipant pax ) throws ServiceErrorException
  {
    StackStandingActivity rankingActivity = new StackStandingActivity( GuidUtils.generateGuid() );
    rankingActivity.setPosted( true );
    rankingActivity.setSubmissionDate( DateUtils.getCurrentDate() );
    rankingActivity.setPromotion( pax.getStackStandingNode().getStackStanding().getPromotion() );
    rankingActivity.setParticipant( pax.getParticipant() );
    rankingActivity.setStackStandingParticipant( pax );
    rankingActivity.setNode( pax.getStackStandingNode().getNode() );
    rankingActivity.setAwardQuantity( pax.getPayout() );
    activityDAO.saveActivity( rankingActivity );
    createJournal( pax, rankingActivity );
    return rankingActivity;
  }

  private Journal createJournal( StackStandingParticipant pax, StackStandingActivity rankingActivity ) throws ServiceErrorException
  {
    ThrowdownPromotion promotion = (ThrowdownPromotion)rankingActivity.getPromotion();
    Journal journal = new Journal();
    journal.setGuid( GuidUtils.generateGuid() );
    journal.setPromotion( rankingActivity.getPromotion() );
    journal.setParticipant( rankingActivity.getParticipant() );
    journal.setAccountNumber( rankingActivity.getParticipant().getAwardBanqNumber() );
    journal.setTransactionDate( new Date() );
    journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    journal.setJournalType( Journal.STACK_STANDING );
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) ); //bug 73458

    // Bug 53535 set promotion-name based on recipient Locale
    String promotionName = promotion.getPromotionName();
    if ( pax != null && pax.getParticipant() != null && pax.getParticipant().getLanguageType() != null )
    {
      promotionName = promotionService.getPromotionNameByLocale( promotion.getPromoNameAssetCode(), pax.getParticipant().getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }
    if ( pax.getStackStandingNode().isHierarchyRanking() )
    {
      journal.setTransactionDescription( promotionName + " for a rank :" + pax.getStanding() + ", in Round: " + pax.getStackStandingNode().getStackStanding().getRoundNumber()
          + ", within entire hierarchy" );
    }
    else
    {
      journal.setTransactionDescription( promotionName + " for a rank :" + pax.getStanding() + ", in Round: " + pax.getStackStandingNode().getStackStanding().getRoundNumber() + ", within node :"
          + pax.getStackStandingNode().getNode().getName() );
    }

    if ( journal.getParticipant().isOptOutAwards() )
    {
      journal.setTransactionAmount( new Long( 0 ) );
      journal.setTransactionDescription( promotionName + "-" + OPT_OUT_TEXT );
    }
    else
    {
      journal.setTransactionAmount( pax.getPayout() );
    }

    Set<Activity> activitySet = new HashSet<Activity>();
    activitySet.add( rankingActivity );
    journal.addActivities( activitySet );
    journalService.saveAndLaunchPointsDepositProcess( journal, false ); //bug 73458

    return journal;
  }

  private void processUndefeatedBadges( MatchTeamOutcome matchTeam )
  {
    ThrowdownPromotion promotion = matchTeam.getPromotion();
    Team team = matchTeam.getTeam();
    int lastRoundNumber = promotion.getNumberOfRounds();
    int currentRound = matchTeam.getMatch().getRound().getRoundNumber();
    // check if user is not active
    // check if it is not last round
    // check if it is not shadow player
    // check if current processing round he is not lost. tie also considered not defeated.
    // check if he has won all earlier rounds
    if ( team.isShadowPlayer() || currentRound != lastRoundNumber || matchTeam.getOutcome().getCode().equals( MatchTeamOutcomeType.LOSS ) || !team.getParticipant().isActive() )
    {
      return;
    }
    if ( !teamDAO.isTeamUndefeatedTillNow( team.getId(), promotion.getId(), currentRound ) )
    {
      return;
    }
    processUndefeatedBadges( matchTeam.getTeam(), promotion );
  }

  private void processUndefeatedBadges( Team team, ThrowdownPromotion promotion )
  {
    processBadges( promotion, team.getParticipant(), null, BadgeLevelType.lookup( BadgeLevelType.UNDEFEATED ), null );
  }

  private void processRoundRankingBadges( StackStandingParticipant pax, String levelName )
  {
    processBadges( pax.getStackStandingNode().getStackStanding().getPromotion(), pax.getParticipant(), pax.getStanding(), BadgeLevelType.lookup( BadgeLevelType.STACK_STAND ), levelName );
  }

  private void processOverallRankingBadges( StackStandingParticipant pax, String levelName )
  {
    processBadges( pax.getStackStandingNode().getStackStanding().getPromotion(), pax.getParticipant(), pax.getStanding(), BadgeLevelType.lookup( BadgeLevelType.OVERALL ), levelName );
  }

  private void processBadges( ThrowdownPromotion promotion, Participant pax, Integer rank, BadgeLevelType levelType, String levelName )
  {
    List<Badge> badgeList = new ArrayList<Badge>();
    badgeList = gamificationService.getBadgeByPromotion( promotion.getId() );
    Iterator<Badge> badgeItr = badgeList.iterator();

    while ( badgeItr.hasNext() )
    {
      Badge badge = badgeItr.next();

      if ( badge.getBadgeType().getCode().equalsIgnoreCase( BadgeType.EARNED_OR_NOT_EARNED ) )
      {
        gamificationService.populateEarnedBadgesTD( rank, pax, promotion, badge, levelType, levelName );
      }
    }
  }

  private boolean hasAwardPayout( TeamRankingResult teamResult )
  {
    if ( teamResult.getPayoutAmount() <= 0 )
    {
      return false;
    }
    return true;
  }

  private boolean isActiveParticipant( TeamRankingResult teamResult )
  {
    if ( !teamResult.getParticipant().isActive() )
    {
      return false;
    }
    return true;
  }

  public StackRankingCalculationResult getRankingAwardSummaryForRound( Long promotionId, int roundNumber )
  {
    return buildStackRankingCalculationResult( promotionId, roundNumber, true );
  }

  public StackRankingCalculationResult generateRankingAwardSummaryForRound( Long promotionId, int roundNumber )
  {
    return buildStackRankingCalculationResult( promotionId, roundNumber, false );
  }

  public List<Round> getRoundsByDivision( Long divisionId )
  {
    return roundDAO.getRoundsByDivision( divisionId );
  }

  private Activity processPayoutForHeadToHeadMatch( MatchTeamOutcome matchTeam, Division division ) throws ServiceErrorException
  {
    Activity activity = null;
    // Skip payout generation for the following reasons:
    // 1. The player is actually the shadow player
    // 2. The player didin't meet the minimum qualifier for it's division's payout
    // 3. The player is no longer an active participant
    boolean isShadow = matchTeam.getTeam().isShadowPlayer();

    if ( isShadow || !matchTeam.getTeam().getParticipant().isActive() || matchTeam.getTotalProgressWithPrecisionAndRounding().compareTo( division.getMinimumQualifierWithPrecisionAndRounding() ) < 0 )
    {
      // no payout
      return activity;// this needs to be refactored into a report holder
    }
    // ok, valid results - lets see if there's a payout associated with the MatchOutComeTYpe
    DivisionPayout payout = buildDivisionPayout( division, matchTeam.getOutcome() );
    // make sure there's a positive payout
    if ( payout != null && payout.getPoints() > 0
        && matchTeam.getTotalProgressWithPrecisionAndRounding().compareTo( matchTeam.getMatch().getRound().getDivision().getMinimumQualifierWithPrecisionAndRounding() ) >= 0 )
    {
      activity = createActivityForHeadToHeadMatch( payout, matchTeam );
    }

    return activity;
  }

  private DivisionPayout buildDivisionPayout( Division division, MatchTeamOutcomeType outcomeType )
  {
    for ( DivisionPayout payout : division.getPayouts() )
    {
      if ( payout.getOutcome().getCode().equals( outcomeType.getCode() ) )
      {
        return payout;
      }
    }
    return null;
  }

  private Activity createActivityForHeadToHeadMatch( DivisionPayout payout, MatchTeamOutcome matchTeamOutcome ) throws ServiceErrorException
  {
    ThrowdownHeadToHeadActivity h2hActivity = new ThrowdownHeadToHeadActivity( GuidUtils.generateGuid() );
    h2hActivity.setPosted( true );
    h2hActivity.setSubmissionDate( DateUtils.getCurrentDate() );
    h2hActivity.setPromotion( matchTeamOutcome.getPromotion() );
    h2hActivity.setParticipant( matchTeamOutcome.getTeam().getParticipant() );
    h2hActivity.setMatchTeamOutcome( matchTeamOutcome );
    h2hActivity.setNode( matchTeamOutcome.getTeam().getParticipant().getPrimaryUserNode().getNode() );
    h2hActivity.setAwardQuantity( new Long( payout.getPoints() ) );
    activityDAO.saveActivity( h2hActivity );
    createJournal( payout, h2hActivity );

    return h2hActivity;
  }

  private Journal createJournal( DivisionPayout payout, ThrowdownHeadToHeadActivity h2hActivity ) throws ServiceErrorException
  {
    ThrowdownPromotion promotion = (ThrowdownPromotion)h2hActivity.getPromotion();
    Journal journal = new Journal();
    journal.setGuid( GuidUtils.generateGuid() );
    journal.setPromotion( h2hActivity.getPromotion() );
    journal.setParticipant( h2hActivity.getParticipant() );
    journal.setAccountNumber( h2hActivity.getParticipant().getAwardBanqNumber() );
    journal.setTransactionDate( new Date() );
    journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
    journal.setJournalType( Journal.THROWDOWN_HEAD2HEAD );
    journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) ); //bug 73458

    // Bug 53535 set promotion-name based on recipient Locale
    String promotionName = promotion.getPromotionName();
    if ( h2hActivity != null && h2hActivity.getParticipant() != null && h2hActivity.getParticipant().getLanguageType() != null )
    {
      promotionName = promotionService.getPromotionNameByLocale( promotion.getPromoNameAssetCode(), h2hActivity.getParticipant().getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }

    if ( journal.getParticipant().isOptOutAwards() )
    {
      journal.setTransactionAmount( new Long( 0 ) );
      journal.setTransactionDescription( promotionName + "-" + OPT_OUT_TEXT );
    }
    else
    {
      journal.setTransactionAmount( new Long( payout.getPoints() ) );
      journal.setTransactionDescription( promotionName + " for a " + payout.getOutcome().getCode() + " in Round: " + h2hActivity.getMatchTeamOutcome().getMatch().getRound().getRoundNumber() );
    }

    Set<Activity> activitySet = new HashSet<Activity>();
    activitySet.add( h2hActivity );
    journal.addActivities( activitySet );
    journalService.saveAndLaunchPointsDepositProcess( journal, false ); //bug 73458

    return journal;
  }

  public MatchResolution resolveMatch( MatchTeamOutcome matchTeam1, MatchTeamOutcome matchTeam2 )
  {
    return resolveMatch( matchTeam1, matchTeam2, false );
  }

  private MatchResolution resolveMatch( MatchTeamOutcome matchTeam1, MatchTeamOutcome matchTeam2, boolean payout )
  {
    BigDecimal progress1 = null;
    BigDecimal progress2 = null;

    if ( matchTeam1.getTeam().isShadowPlayer() )
    {
      // if team 1 is shadow player pass team 2 to exclude his progress
      progress1 = getShadowScore( matchTeam1.getMatch().getRound().getId(), matchTeam2.getTeam().getId() );
      if ( payout )
      {
        // store the system score for reference when match is paid out
        matchTeam1.setCurrentValue( progress1 );
      }
    }
    else if ( matchTeam2.getTeam().isShadowPlayer() )
    {
      // if team 2 is shadow player pass team 1 to exclude his progress
      progress2 = getShadowScore( matchTeam2.getMatch().getRound().getId(), matchTeam1.getTeam().getId() );
      if ( payout )
      {
        // store the system score for reference when match is paid out
        matchTeam2.setCurrentValue( progress2 );
      }
    }
    if ( null == progress1 )
    {
      progress1 = matchTeam1.getTotalProgressWithPrecisionAndRounding();
    }
    if ( null == progress2 )
    {
      progress2 = matchTeam2.getTotalProgressWithPrecisionAndRounding();
    }

    MatchTeamOutcomeType WIN = MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.WIN );
    MatchTeamOutcomeType LOSS = MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.LOSS );
    MatchTeamOutcomeType TIE = MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.TIE );

    // matchTeam1 is greater
    if ( progress1.compareTo( progress2 ) > 0 )
    {
      return new MatchResolution( matchTeam1, WIN, matchTeam2, LOSS );
    }
    // matchTeam2 is greater
    else if ( progress2.compareTo( progress1 ) > 0 )
    {
      return new MatchResolution( matchTeam1, LOSS, matchTeam2, WIN );
    }
    // TIE!
    else if ( progress1.compareTo( progress2 ) == 0 )
    {
      return new MatchResolution( matchTeam1, TIE, matchTeam2, TIE );
    }
    return null;
  }

  public BigDecimal getShadowScore( Long roundId, Long teamId )
  {
    Round round = roundDAO.getRoundById( roundId );
    ThrowdownPromotion promotion = round.getDivision().getPromotion();
    TeamUnavailableResolverType resolver = promotion.getTeamUnavailableResolverType();

    if ( resolver.getCode().equals( TeamUnavailableResolverType.MINIMUM_QUALIFIER ) )
    {
      return round.getDivision().getMinimumQualifierWithPrecisionAndRounding();
    }

    return getCalculatedAverageForRound( promotion, roundId, teamId );
  }

  private static final String UnixFileSeparator = "/";
  private static final String WindowsFileSeparator = "\\";

  private static final String DETAIL_REPORT_HEADER = "Promotion,Round Number,Login Id,PAX first name, PAX last name, Email address, "
      + "address, city,state,zip,country,Node role, Node Name, Node owner, Award Type, Outcome Type, Matchgroup Name/Node Type, Progress per round, Award Amount";

  public boolean generateAndMailExtractReport( ThrowdownPromotion promotion, int roundNumber, ThrowdownRoundCalculationResult calculationResults, StackRankingCalculationResult rankPayoutResults )
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

        writer.write( DETAIL_REPORT_HEADER );
        writer.newLine();

        if ( calculationResults != null )
        {
          for ( DivisionCalculationResult divisionResult : calculationResults.getDivisionResults() )
          {
            for ( TeamCalculationResult teamResult : divisionResult.getTeamResults() )
            {
              if ( teamResult.getPayoutAmount() != null )
              {
                String csvRow = createDetailLine( calculationResults, divisionResult, teamResult );
                writer.write( csvRow );
                writer.newLine();
              }
            }
          }
        }

        if ( rankPayoutResults != null )
        {
          for ( NodeRankingResult nodeResult : rankPayoutResults.getRoundStackRanking().getNodeRankings() )
          {
            for ( TeamRankingResult teamResult : nodeResult.getTeamResults() )
            {
              if ( teamResult.getPayoutAmount() != 0 )
              {
                String csvRow = createDetailLine( rankPayoutResults, nodeResult, teamResult );
                writer.write( csvRow );
                writer.newLine();
              }
            }
          }
        }

        writer.close();
        if ( extractFile != null && extractFile.exists() )
        {
          // Email the file to the user.
          sendMessage( promotion, roundNumber, extractFile.getAbsolutePath(), extractFile.getName() );
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

  private void sendMessage( ThrowdownPromotion promotion, int roundNumber, String fullFileName, String attachmentFileName )
  {
    User currentUser = userService.getUserByUserName( UserManager.getUserName() );
    // Set up mailing-level personalization data.
    Map<String, String> objectMap = new HashMap<String, String>();
    objectMap.put( "promotionName", promotion.getName() );
    objectMap.put( "roundNumber", roundNumber + "" );

    // Compose the e-mail message.
    Mailing mailing = composeMail( MessageService.THROWDOWN_ROUND_PAYOUT_EXTRACT_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );
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
      if ( log.isInfoEnabled() )
      {
        log.info( "Successfully sent email to  " + currentUser.getFirstName() + " " + currentUser.getLastName() + "." + " (mailing ID = " + mailing.getId() + ")" );
      }
    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending Throwdown Round payout detail extract. " + " (mailing ID = " + mailing.getId() + ")" );
      log.error( msg, e );
      throw new BeaconRuntimeException( "Error during submission of mail. The exception caused by: " + e.getCause().getMessage() );
    }
  }

  protected MailingAttachmentInfo addMailingAttachmentInfo( Mailing mailing, String fullFileName, String attachmentFileName )
  {
    MailingAttachmentInfo mailingAttachmentInfo = new MailingAttachmentInfo();
    mailingAttachmentInfo.setFullFileName( fullFileName );
    mailingAttachmentInfo.setMailing( mailing );
    mailingAttachmentInfo.setAttachmentFileName( attachmentFileName );
    return mailingAttachmentInfo;
  }

  private Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();
    mailing.setMailingType( MailingType.lookup( mailingType ) );
    Message message = messageService.getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );

    return mailing;
  }

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

  private String createDetailLine( ThrowdownRoundCalculationResult calculationResults, DivisionCalculationResult divisionResult, TeamCalculationResult teamResult )
  {
    StringBuilder detailLine = new StringBuilder();
    Participant pax = getParticipant( teamResult.getTeam().getParticipant().getId() );
    // promotion name
    detailLine.append( calculationResults.getPromotion().getName() );
    // round number
    detailLine.append( ",=\"" );
    detailLine.append( calculationResults.getRoundNumber() );
    detailLine.append( "\"," );

    addPaxDetails( detailLine, pax );

    detailLine.append( "\"" );
    detailLine.append( "H2H" );
    detailLine.append( "\"," );

    detailLine.append( "\"" );
    detailLine.append( teamResult.getOutcomePayoutType().getCode() );
    detailLine.append( "\"," );

    // division name if H2H else Node Type
    detailLine.append( "\"" );
    detailLine.append( divisionResult.getDivision().getDivisionNameFromCM() );
    detailLine.append( "\"," );

    // total progress for round
    detailLine.append( "\"" );
    detailLine.append( teamResult.getTotalProgress() );
    detailLine.append( "\"," );

    // award amount
    detailLine.append( "\"" );
    detailLine.append( teamResult.getPayoutAmount() );
    detailLine.append( "\"" );

    return detailLine.toString();
  }

  private String createDetailLine( StackRankingCalculationResult calculationResults, NodeRankingResult nodeResult, TeamRankingResult teamResult )
  {
    StringBuilder detailLine = new StringBuilder();
    Participant pax = getParticipant( teamResult.getParticipant().getId() );
    // promotion name
    detailLine.append( calculationResults.getRoundStackRanking().getPromotion().getName() );
    // round number
    detailLine.append( ",=\"" );
    detailLine.append( calculationResults.getRoundStackRanking().getRoundNumber() );
    detailLine.append( "\"," );

    addPaxDetails( detailLine, pax );

    detailLine.append( "\"" );
    detailLine.append( "STACK RANK" );
    detailLine.append( "\"," );

    detailLine.append( "\"" );
    detailLine.append( teamResult.getRank() );
    detailLine.append( "\"," );

    // division name if H2H else Node Type
    detailLine.append( "\"" );
    if ( !nodeResult.isHierarchyRanking() )
    {
      detailLine.append( nodeResult.getNode().getNodeType().getNodeTypeName() );
    }
    else
    {
      detailLine.append( ContentReaderManager.getText( "system.general", "ALL" ) );
    }
    detailLine.append( "\"," );

    // total progress for round
    detailLine.append( "\"" );
    detailLine.append( teamResult.getRankingFactor() );
    detailLine.append( "\"," );

    // award amount
    detailLine.append( "\"" );
    detailLine.append( teamResult.getPayoutAmount() );
    detailLine.append( "\"" );

    return detailLine.toString();
  }

  private void addPaxDetails( StringBuilder detailLine, Participant pax )
  {
    // Username
    detailLine.append( "\"" );
    detailLine.append( pax.getUserName() );
    detailLine.append( "\"," );
    // First name
    detailLine.append( pax.getFirstName() );
    detailLine.append( ",\"" );
    // last name
    detailLine.append( pax.getLastName() );
    detailLine.append( "\"," );

    detailLine.append( "\"" );
    // email
    if ( pax.getPrimaryEmailAddress() != null )
    {
      detailLine.append( pax.getPrimaryEmailAddress().getEmailAddr() );
    }
    detailLine.append( "\"," );
    // address details
    if ( pax.getPrimaryAddress() != null && pax.getPrimaryAddress().getAddress() != null )
    {
      detailLine.append( "\"" );
      if ( pax.getPrimaryAddress().getAddress() != null )
      {
        detailLine.append( pax.getPrimaryAddress().getAddress().getAddr1() );
      }
      detailLine.append( "\"," );
      detailLine.append( "\"" );
      // Fix 22917 - For other than united states pax.
      if ( pax.getPrimaryAddress().getAddress().getCity() != null )
      {
        detailLine.append( pax.getPrimaryAddress().getAddress().getCity() );
      }
      detailLine.append( "\"," );
      detailLine.append( "\"" );
      if ( pax.getPrimaryAddress().getAddress().getStateType() != null )
      {
        detailLine.append( pax.getPrimaryAddress().getAddress().getStateType().getName() );
      }
      detailLine.append( "\"," );
      detailLine.append( "\"" );
      if ( pax.getPrimaryAddress().getAddress().getPostalCode() != null )
      {
        detailLine.append( pax.getPrimaryAddress().getAddress().getPostalCode() );
      }
      detailLine.append( "\"," );
      detailLine.append( "\"" );
      Country country = pax.getPrimaryAddress().getAddress().getCountry();
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
    // user node hierararcy type
    detailLine.append( "\"" );
    UserNode userNode = pax.getPrimaryUserNode();
    detailLine.append( userNode.getHierarchyRoleType().getName() );
    detailLine.append( "\"," );

    // node name
    detailLine.append( "\"" );
    detailLine.append( userNode.getNode().getName() );
    detailLine.append( "\"," );

    // node owner
    detailLine.append( "\"" );
    User owner = participantService.getNodeOwner( userNode.getNode().getId() );
    detailLine.append( owner != null ? owner.getNameLFMWithComma() : "" );
    detailLine.append( "\"," );

  }

  @SuppressWarnings( "unchecked" )
  private Participant getParticipant( Long id )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ALL ) );
    return getParticipantService().getParticipantByIdWithAssociations( id, associationRequestCollection );
  }

  private String generateUniqueFileName()
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_ThrowdownCalcExtract_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  private String getExtractLocation()
  {
    String extractLocation = null;

    // On Windows this is \ On Unix this is /
    String currentSystemFileSeparator = File.separator;

    // user defined path where the csv file should be saved. i.e. /tmp/ on Unix
    extractLocation = System.getProperty( "appdatadir" );
    if ( log.isInfoEnabled() )
    {
      log.info( "Extract location value of getproperty -->>>>>>>>>>>>" + extractLocation );
    }

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

  @SuppressWarnings( "rawtypes" )
  public Set<PromotionRoundValue> getPromotionsForPayout()
  {
    Set<PromotionRoundValue> promoValues = new HashSet<PromotionRoundValue>();
    List promotionList = promotionService.getAllLiveAndExpiredByType( PromotionType.THROWDOWN );
    Iterator promoIterator = promotionList.iterator();
    while ( promoIterator.hasNext() )
    {
      ThrowdownPromotion promotion = (ThrowdownPromotion)promoIterator.next();
      int numberOfRounds = promotion.getNumberOfRounds();
      for ( int roundNumber = 1; roundNumber <= numberOfRounds; roundNumber++ )
      {
        // check if round is completed before proceeding.
        if ( roundDAO.isRoundCompleted( promotion.getId(), roundNumber ) )
        {
          // if division pay out is not approved or
          // if round ranking pay out is not approved or
          if ( !roundDAO.isRoundPaidForDivisionPayouts( promotion.getId(), roundNumber ) || stackStandingService.getUnapprovedRankingForPromotionAndRound( promotion.getId(), roundNumber ) != null )
          {
            PromotionRoundValue promoValue = new PromotionRoundValue();
            promoValue.setPromotion( promotion );
            promoValue.setRoundNumber( roundNumber );
            promoValues.add( promoValue );
          }
        }
      }
    }
    return promoValues;
  }

  @SuppressWarnings( "rawtypes" )
  public Set<PromotionRoundValue> getPromotionsForProgressLoad()
  {
    Set<PromotionRoundValue> promoValues = new HashSet<PromotionRoundValue>();
    List promotionList = promotionService.getAllLiveByType( PromotionType.THROWDOWN );
    Iterator promoIterator = promotionList.iterator();
    while ( promoIterator.hasNext() )
    {
      ThrowdownPromotion promotion = (ThrowdownPromotion)promoIterator.next();
      promoValues.addAll( getRoundsForProgressLoad( promotion.getId() ) );
    }
    return promoValues;
  }

  public Set<PromotionRoundValue> getRoundsForProgressLoad( Long promotionId )
  {
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionId );
    Set<PromotionRoundValue> promoValues = new HashSet<PromotionRoundValue>();
    int numberOfRounds = promotion.getNumberOfRounds();
    for ( int roundNumber = 1; roundNumber <= numberOfRounds; roundNumber++ )
    {
      // check if round is started before proceeding.
      if ( roundDAO.isRoundStarted( promotion.getId(), roundNumber ) )
      {
        // if division pay out is not approved or
        // if round ranking pay out is not approved or
        if ( !roundDAO.isRoundPaidForDivisionPayouts( promotion.getId(), roundNumber ) || stackStandingService.getUnapprovedRankingForPromotionAndRound( promotion.getId(), roundNumber ) != null )
        {
          PromotionRoundValue promoValue = new PromotionRoundValue();
          promoValue.setPromotion( promotion );
          promoValue.setRoundNumber( roundNumber );
          promoValues.add( promoValue );
        }
      }
    }
    return promoValues;
  }

  public ThrowdownPromotion getThrowdownPromotion( Long promotionId )
  {
    return (ThrowdownPromotion)promotionService.getPromotionById( promotionId );
  }

  public ThrowdownPromotion getThrowdownPromotionByIdWithAssociations( Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    return (ThrowdownPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
  }

  public List<Match> getThrowdownMatchesByPromotionAndRoundNumber( Long promotionId, int roundNumber, AssociationRequestCollection associationRequestCollection )
  {
    return matchDAO.getMatchesByPromotionAndRoundNumber( promotionId, roundNumber, associationRequestCollection );
  }

  public List<Round> getThrowdownRoundsForPromotionByRoundNumber( Long promotionId, int roundNumber )
  {
    return roundDAO.getRoundsForPromotionByRoundNumber( promotionId, roundNumber );
  }

  @Override
  public Division getDivision( Long divisionId )
  {
    return divisionDAO.getDivision( divisionId );
  }

  @Override
  public void deleteDivision( Long divisionId )
  {
    divisionDAO.delete( divisionId );
  }

  @Override
  public List<Division> getDivisionsByPromotionId( Long promotionId )
  {
    return divisionDAO.getDivisionsByPromotionId( promotionId );
  }

  public DivisionDAO getDivisionDAO()
  {
    return divisionDAO;
  }

  public void setDivisionDAO( DivisionDAO divisionDAO )
  {
    this.divisionDAO = divisionDAO;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public ActivityDAO getActivityDAO()
  {
    return activityDAO;
  }

  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

  public RoundDAO getRoundDAO()
  {
    return roundDAO;
  }

  public void setRoundDAO( RoundDAO roundDAO )
  {
    this.roundDAO = roundDAO;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public ProcessService getProcessService()
  {
    return processService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public JournalService getJournalService()
  {
    return journalService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public MatchDAO getMatchDAO()
  {
    return matchDAO;
  }

  public void setMatchDAO( MatchDAO matchDAO )
  {
    this.matchDAO = matchDAO;
  }

  @Override
  public BigDecimal getCalculatedAverageForRound( ThrowdownPromotion promotion, Long roundId, Long teamId )
  {
    RoundingMode mode = promotion.getRoundingMethod().getRoundingMode();
    BigDecimal average = roundDAO.getCalculatedAverageForRound( roundId, teamId, mode );

    int precision = promotion.getAchievementPrecision().getPrecision();
    int roundingMode = promotion.getRoundingMethod().getBigDecimalRoundingMode();
    BigDecimal roundedAverageValue = average.setScale( precision, roundingMode );

    return roundedAverageValue;
  }

  private boolean isActivityExistsForOutcome( MatchTeamOutcome matchTeam1 )
  {
    boolean isActivityExists = activityDAO.getActivityForMatchTeamOutcomeId( matchTeam1.getId() );
    return isActivityExists;
  }

  private boolean isActivityExistsForRankingPax( StackStandingParticipant pax )
  {
    boolean isActivityExists = activityDAO.getActivityForStackStandingPaxId( pax.getId() );
    return isActivityExists;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public MessageService getMessageService()
  {
    return messageService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public StackStandingService getStackStandingService()
  {
    return stackStandingService;
  }

  public void setStackStandingService( StackStandingService stackStandingService )
  {
    this.stackStandingService = stackStandingService;
  }

  public GamificationService getGamificationService()
  {
    return gamificationService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public TeamDAO getTeamDAO()
  {
    return teamDAO;
  }

  public void setTeamDAO( TeamDAO teamDAO )
  {
    this.teamDAO = teamDAO;
  }

  public MatchTeamOutcomeDAO getMatchTeamOutcomeDAO()
  {
    return matchTeamOutcomeDAO;
  }

  public void setMatchTeamOutcomeDAO( MatchTeamOutcomeDAO matchTeamOutcomeDAO )
  {
    this.matchTeamOutcomeDAO = matchTeamOutcomeDAO;
  }

  public HierarchyService getHierarchyService()
  {
    return hierarchyService;
  }

  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  public ListBuilderService getListBuilderService()
  {
    return listBuilderService;
  }

  public void setListBuilderService( ListBuilderService listBuilderService )
  {
    this.listBuilderService = listBuilderService;
  }

}

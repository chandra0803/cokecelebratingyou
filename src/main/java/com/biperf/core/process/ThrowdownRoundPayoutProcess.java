
package com.biperf.core.process;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.throwdown.DivisionCalculationResult;
import com.biperf.core.service.throwdown.NodeRankingResult;
import com.biperf.core.service.throwdown.StackRankingCalculationResult;
import com.biperf.core.service.throwdown.StackStandingService;
import com.biperf.core.service.throwdown.TeamCalculationResult;
import com.biperf.core.service.throwdown.TeamRankingResult;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownRoundCalculationResult;
import com.biperf.core.service.throwdown.ThrowdownService;

public class ThrowdownRoundPayoutProcess extends BaseProcessImpl
{

  public static final String BEAN_NAME = "throwdownRoundPayoutProcess";
  public static final String MESSAGE_NAME = "Throwdown Round Payout Process";

  private ThrowdownService throwdownService;
  private TeamService teamService;
  private StackStandingService stackStandingService;
  private GamificationService gamificationService;

  private String promotionId;
  private String roundNumber;

  private static final Log log = LogFactory.getLog( ThrowdownRoundPayoutProcess.class );

  @Override
  protected void onExecute()
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "Processing Round " + getRoundNumber() + " for Throwdown Promotion " + promotionId );
    }

    // convert process parameters from string to primitives for service usage
    Long promotionIdLong = new Long( promotionId );
    int roundNumberInt = Integer.parseInt( roundNumber );

    // get promotion
    ThrowdownPromotion promotion = getThrowdownPromotion( promotionIdLong );

    // create ranking
    boolean rankingHasErrors = createRankings( promotionIdLong, roundNumberInt );

    // ranking pay outs
    StackRankingCalculationResult rankPayoutResults = doRankingPayouts( promotion, roundNumberInt, rankingHasErrors );

    // division pay outs
    ThrowdownRoundCalculationResult calculationResults = doDivisionPayouts( promotion, roundNumberInt );

    // email report to admin
    emailReport( promotion, roundNumberInt, calculationResults, rankPayoutResults );

  }

  @SuppressWarnings( "unchecked" )
  private ThrowdownPromotion getThrowdownPromotion( Long promotionId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISION_MATCH_OUTCOMES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    return throwdownService.getThrowdownPromotionByIdWithAssociations( promotionId, associationRequestCollection );
  }

  private boolean createRankings( Long promotionId, int roundNumber )
  {
    // recreate rankings based on latest progress
    boolean rankingHasErrors = false;
    try
    {
      stackStandingService.createRankingForRound( promotionId, roundNumber );
      addComment( "Round rankings created successfully." );
    }
    catch( ServiceErrorException e )
    {
      log.error( e.getMessage(), e );
      addComment( "Round ranking was paid out for few participants already. It wont be recreated again for promotion : " + promotionId + " and round : " + roundNumber + "." );
    }
    catch( Exception e )
    {
      StringBuilder failMessage = new StringBuilder( "Error Occurred while creating round ranking for promotion " + getPromotionId() );
      failMessage.append( " and round " + roundNumber + ".  " );
      failMessage.append( "The error caused by: " );
      failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
      log.error( failMessage, e );
      addComment( failMessage.toString() );
      rankingHasErrors = true;
    }
    return rankingHasErrors;
  }

  private StackRankingCalculationResult doRankingPayouts( ThrowdownPromotion promotion, int roundNumber, boolean rankingHasErrors )
  {
    StackRankingCalculationResult rankPayoutResults = null;
    try
    {
      if ( !rankingHasErrors )
      {
        rankPayoutResults = getRankingPayouts( promotion, roundNumber );
        if ( !rankPayoutResults.getRoundStackRanking().isAnyPayoutFailed() )
        {
          addComment( "Successfully issued stack ranking payout for promotion : " + promotionId + " and round : " + roundNumber + "." );
        }
        else
        {
          addComment( "Stack ranking payout for promotion : " + promotionId + " and round : " + roundNumber + " is partially successful." );
        }
      }
      else
      {
        addComment( "Stack ranking payout for promotion not initiated since ranking creation failed." );
      }
    }
    catch( Exception e )
    {
      StringBuilder failMessage = new StringBuilder( "Error Occurred while stack ranking payout for promotion " + getPromotionId() );
      failMessage.append( " and round " + roundNumber + ".  " );
      failMessage.append( "The error caused by: " );
      failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
      log.error( failMessage, e );
      addComment( failMessage.toString() );
    }
    return rankPayoutResults;
  }

  private StackRankingCalculationResult getRankingPayouts( ThrowdownPromotion promotion, int roundNumber )
  {
    boolean isLastRound = roundNumber == promotion.getNumberOfRounds() ? true : false;
    StackRankingCalculationResult rankPayoutResults = throwdownService.generateRankingAwardSummaryForRound( new Long( promotionId ), roundNumber );
    boolean allRankingsProcessed = true;

    List<NodeRankingResult> nodeRankings = rankPayoutResults.getRoundStackRanking().getNodeRankings();
    for ( NodeRankingResult nodeResult : nodeRankings )
    {
      List<TeamRankingResult> teamResults = nodeResult.getTeamResults();
      for ( Iterator<TeamRankingResult> teamResultsIter = teamResults.iterator(); teamResultsIter.hasNext(); )
      {
        TeamRankingResult teamResult = teamResultsIter.next();
        try
        {
          throwdownService.processRankingPayout( teamResult, isLastRound );
        }
        catch( ServiceErrorExceptionWithRollback e )
        {
          log.error( e.getMessage(), e );
          teamResultsIter.remove();
          // ok, make sure we set the flag since there was an error
          rankPayoutResults.getRoundStackRanking().setAnyPayoutFailed( true );
          allRankingsProcessed = false;
        }
      }
    }

    if ( allRankingsProcessed )
    {
      StackStanding ranking = stackStandingService.getUnapprovedRankingForPromotionAndRound( promotion.getId(), roundNumber );
      // ranking might have got paid out already, so you wont get one un-approved
      if ( ranking != null )
      {
        ranking.setPayoutsIssued( true );
        stackStandingService.save( ranking );
      }
    }

    return rankPayoutResults;
  }

  private ThrowdownRoundCalculationResult doDivisionPayouts( ThrowdownPromotion promotion, int roundNumber )
  {
    ThrowdownRoundCalculationResult calculationResults = null;
    try
    {
      calculationResults = getDivisionPayouts( promotion, roundNumber );
      if ( !calculationResults.isAnyPayoutFailed() )
      {
        addComment( "Successfully issued division payout for promotion : " + promotionId + " and round : " + roundNumber + "." );
      }
      else
      {
        addComment( "Division payout for promotion : " + promotionId + " and round : " + roundNumber + " is partially successful." );
      }
    }
    catch( Exception e )
    {
      StringBuilder failMessage = new StringBuilder( "Error Occurred while division payout for promotion " + getPromotionId() );
      failMessage.append( " and round " + roundNumber + ".  " );
      failMessage.append( "The error caused by: " );
      failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
      log.error( failMessage, e );
      addComment( failMessage.toString() );
    }
    return calculationResults;
  }

  private ThrowdownRoundCalculationResult getDivisionPayouts( ThrowdownPromotion promotion, int roundNumberInt )
  {
    ThrowdownRoundCalculationResult calculationResults = buildThrowdownRoundCalculationResult( promotion, roundNumberInt );
    List<Round> rounds = teamService.getRoundsForPromotionByRoundNumber( promotion.getId(), roundNumberInt );
    // for each round/division
    for ( Round round : rounds )
    {
      DivisionCalculationResult divisionResults = buildDivisionCalculationResult( round );
      // for each match, produce a payout(s) for those not already marked
      List<Match> matches = teamService.getUnplayedMatchesForPromotionAndRound( round.getId() );
      boolean allMatchesProcessed = true;

      for ( Match match : matches )
      {
        try
        {
          Set<TeamCalculationResult> matchResults = throwdownService.processMatchPayout( promotion, match );// go
                                                                                                            // through
                                                                                                            // the
                                                                                                            // interface
                                                                                                            // to
                                                                                                            // apply
                                                                                                            // the
                                                                                                            // new
                                                                                                            // transaction
          // ok - the matchResults should be committed to the database, add them to the results
          divisionResults.getTeamResults().addAll( matchResults );
        }
        catch( ServiceErrorExceptionWithRollback e )
        {
          log.error( e.getMessage(), e );
          // ok, make sure we set the flag since there was an error
          calculationResults.setAnyPayoutFailed( true );
          allMatchesProcessed = false;
        }
      }
      calculationResults.getDivisionResults().add( divisionResults );
      // mark the round as payout processed (if this is committed)
      if ( allMatchesProcessed )
      {
        teamService.markRoundPaidout( round.getId() );
      }
    }
    // send manager emails
    throwdownService.sendEndOfRoundEmailToPaxManager( promotion, rounds );
    return calculationResults;
  }

  private ThrowdownRoundCalculationResult buildThrowdownRoundCalculationResult( ThrowdownPromotion promotion, int roundNumber )
  {
    ThrowdownRoundCalculationResult calculationSummary = new ThrowdownRoundCalculationResult();
    calculationSummary.setPromotion( promotion );
    calculationSummary.setRoundNumber( roundNumber );
    return calculationSummary;
  }

  private DivisionCalculationResult buildDivisionCalculationResult( Round round )
  {
    DivisionCalculationResult divisionResults = new DivisionCalculationResult();
    divisionResults.setDivision( round.getDivision() );
    divisionResults.setRound( round );
    return divisionResults;
  }

  private void emailReport( ThrowdownPromotion promotion, int roundNumber, ThrowdownRoundCalculationResult calculationResults, StackRankingCalculationResult rankPayoutResults )
  {
    try
    {
      throwdownService.generateAndMailExtractReport( promotion, roundNumber, calculationResults, rankPayoutResults );
    }
    catch( Exception e )
    {
      StringBuilder failMessage = new StringBuilder( "Error Occurred while sending mail " + getPromotionId() );
      failMessage.append( " and round " + roundNumber + ".  " );
      failMessage.append( "The error caused by: " );
      failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
      log.error( failMessage, e );
      addComment( failMessage.toString() );
    }
  }

  public ThrowdownService getThrowdownService()
  {
    return throwdownService;
  }

  public void setThrowdownService( ThrowdownService throwdownService )
  {
    this.throwdownService = throwdownService;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( String roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public void setStackStandingService( StackStandingService stackStandingService )
  {
    this.stackStandingService = stackStandingService;
  }

  public TeamService getTeamService()
  {
    return teamService;
  }

  public void setTeamService( TeamService teamService )
  {
    this.teamService = teamService;
  }

  public GamificationService getGamificationService()
  {
    return gamificationService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

}


package com.biperf.core.process;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.throwdown.StackStandingService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.service.util.ServiceError;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ThrowdownRoundRankingProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "throwdownRoundRankingProcess";
  public static final String MESSAGE_NAME = "Throwdown Round Ranking Process";

  private StackStandingService stackStandingService;
  private ThrowdownService throwdownService;
  private RoundDAO roundDAO;

  // properties set from jobDataMap
  private String promotionId;
  private String roundNumber;

  private static final Log log = LogFactory.getLog( ThrowdownRoundRankingProcess.class );

  public void onExecute()
  {
    try
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "Processing Round " + getRoundNumber() + " for Throwdown Promotion " + promotionId + "." );
      }
      int roundNumberInt = Integer.parseInt( roundNumber );
      ThrowdownPromotion promotion = throwdownService.getThrowdownPromotion( new Long( promotionId ) );
      StringBuilder messages = new StringBuilder();
      boolean runRanking = true;

      if ( promotion != null )
      {
        StackStanding stackStanding = stackStandingService.getApprovedRankingForPromotionAndRound( new Long( promotionId ), roundNumberInt );
        // round checks
        if ( roundNumberInt > promotion.getNumberOfRounds() )
        {
          messages.append( "Given round number (" + roundNumber + ") not available for promotion:" + getPromotionId() + "." );
          addComment( messages.toString() );
          runRanking = false;
        }
        else if ( !roundDAO.isRoundStarted( new Long( promotionId ), roundNumberInt ) )
        {
          messages.append( "Given round number (" + roundNumber + ")  not started for promotion:" + getPromotionId() + "." );
          addComment( messages.toString() );
          runRanking = false;
        }
        // approved exists checks
        else if ( stackStanding != null && ( stackStanding.isPayoutsIssued() || stackStandingService.isAnyPaxPaidOutForRanking( stackStanding.getId() ) ) )
        {
          messages.append( "Given round number (" + roundNumber + ")  is already paid out completely or partially for promotion:" + getPromotionId() + "." );
          addComment( messages.toString() );
          runRanking = false;
        }
        if ( runRanking )
        {
          StackStanding ranking = stackStandingService.createRankingForRound( new Long( promotionId ), roundNumberInt );
          messages.append( "Successfully created rankings for promotion : " + promotionId + " and round : " + roundNumberInt + "." );

          int totalPaxRanked = 0;
          for ( StackStandingNode node : ranking.getStackStandingNodes() )
          {
            totalPaxRanked = totalPaxRanked + node.getStackStandingParticipants().size();
          }
          messages.append( totalPaxRanked + " participants belonging to " + ranking.getStackStandingNodes().size() + " nodes have been ranked." );
          addComment( messages.toString() );
        }
      }
      else
      {
        messages.append( "There is no promotion for promotionId:" + getPromotionId() + "." );
        addComment( messages.toString() );
      }
      sendSummaryMessage( promotion, messages );
    }
    catch( ServiceErrorException e )
    {
      StringBuilder failMessage = new StringBuilder( "Error Occurred while ranking users for promotion " + getPromotionId() + "." );
      failMessage.append( " and round " + roundNumber + ".  " );
      failMessage.append( "The error caused by: " );
      for ( Iterator iter = e.getServiceErrors().iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        failMessage.append( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
      }
      log.error( failMessage, e );
      addComment( failMessage.toString() );
    }
    catch( Exception e )
    {
      StringBuilder failMessage = new StringBuilder( "Error Occurred while ranking users for promotion " + getPromotionId() + "." );
      failMessage.append( " and round " + roundNumber + ".  " );
      failMessage.append( "The error caused by: " );
      failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
      log.error( failMessage, e );
      addComment( failMessage.toString() );
    }
  }

  private void sendSummaryMessage( ThrowdownPromotion promotion, StringBuilder processMessage )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "<html><body>" );
    sb.append( processMessage.toString() );
    sb.append( "</body></html>" );
    String promotionName = promotion != null ? promotion.getPromoNameFromCM() : "";
    getMailingService().submitSystemMailing( "Throwdown Round Ranking Process For Promotion :" + promotionName, sb.toString(), sb.toString() );
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public StackStandingService getStackStandingService()
  {
    return stackStandingService;
  }

  public void setStackStandingService( StackStandingService stackStandingService )
  {
    this.stackStandingService = stackStandingService;
  }

  public String getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( String roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public RoundDAO getRoundDAO()
  {
    return roundDAO;
  }

  public void setRoundDAO( RoundDAO roundDAO )
  {
    this.roundDAO = roundDAO;
  }

  public ThrowdownService getThrowdownService()
  {
    return throwdownService;
  }

  public void setThrowdownService( ThrowdownService throwdownService )
  {
    this.throwdownService = throwdownService;
  }

}


package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.throwdown.TeamService;

public class ThrowdownMatchSchedulerProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "throwdownMatchSchedulerProcess";
  public static final String MESSAGE_NAME = "Throwdown Match Scheduler Process";

  private TeamService teamService;

  // properties set from jobDataMap
  private String promotionId;
  private String roundNumber;

  private static final Log log = LogFactory.getLog( ThrowdownMatchSchedulerProcess.class );

  public void onExecute()
  {
    try
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "Processing Round " + getRoundNumber() + " for Throwdown Promotion " + promotionId );
      }
      int roundNumberInt = Integer.parseInt( roundNumber );
      String processComments = teamService.createMatchesForPromotionRound( new Long( promotionId ), roundNumberInt );
      addComment( processComments );
    }
    catch( Exception e )
    {
      StringBuilder failMessage = new StringBuilder( "Error Occurred while Scheduling matches for promotion " + getPromotionId() );
      failMessage.append( " and while generating Round " + roundNumber + ".  " );
      failMessage.append( "The error caused by: " );
      failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
      log.error( failMessage, e );
      addComment( failMessage.toString() );
    }
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public TeamService getTeamService()
  {
    return teamService;
  }

  public void setTeamService( TeamService teamService )
  {
    this.teamService = teamService;
  }

  public String getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( String roundNumber )
  {
    this.roundNumber = roundNumber;
  }
}

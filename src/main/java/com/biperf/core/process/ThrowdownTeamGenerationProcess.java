
package com.biperf.core.process;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.throwdown.TeamAssociationRequest;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.objectpartners.cms.util.DateUtils;

public class ThrowdownTeamGenerationProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "throwdownTeamGenerationProcess";
  public static final String MESSAGE_NAME = "Throwdown Team Generation Process";

  private TeamService teamService;
  private ThrowdownService throwdownService;
  private PromotionService promotionService;

  // properties set from jobDataMap
  private String promotionId;

  private static final Log log = LogFactory.getLog( ThrowdownTeamGenerationProcess.class );

  @SuppressWarnings( "unchecked" )
  public void onExecute()
  {
    try
    {
      List<Team> teams = teamService.saveTeamsForPromotion( new Long( getPromotionId() ) );
      AssociationRequestCollection arCollection = new AssociationRequestCollection();
      arCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISION_ROUNDS ) );
      ThrowdownPromotion promotion = (ThrowdownPromotion)getPromotionService().getPromotionByIdWithAssociations( new Long( promotionId ), arCollection );
      addComment( "Successfully created " + teams.size() + " teams for promotion " + promotionId );
      addComment( "Successfully created " + promotion.getDivisions().iterator().next().getRounds().size() + " rounds for the promotion." );
      addComment( "Successfully scheduled the match scheduler for Promotion " + promotionId );
      sendSummaryMessage( promotion, teams );
    }
    catch( Exception e )
    {
      StringBuilder failMessage = new StringBuilder( "Error Occurred while generating the teams for promotion " + getPromotionId() + "." );
      failMessage.append( "The error caused by: " );
      failMessage.append( e.getCause() == null ? e.getMessage() : e.getCause().getMessage() );
      log.error( failMessage, e );
      addComment( failMessage.toString() );
    }
  }

  @SuppressWarnings( "unchecked" )
  private void sendSummaryMessage( ThrowdownPromotion promotion, List<Team> teams )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "<html><body>" );
    sb.append( "<h2>Throwdown Promotion " + promotion.getPromoNameFromCM() + " (promotion id " + getPromotionId() + ")</h2> " );
    sb.append( "<p>Throwdown Promotion Team Generation Process for promotion <b>" + promotion.getPromoNameFromCM() + "</b> (promotion id " + getPromotionId() + ") " );
    sb.append( "successfully generated " + teams.size() + " teams across " + promotion.getDivisions().size() + " Matchgroups.</p>" );

    sb.append( "<p><b>Promotion Start Date:</b> " + DateUtils.toDisplayTimeString( promotion.getSubmissionStartDate() ) + "<br/>" );
    sb.append( "<b>Promotion End Date:</b> " + DateUtils.toDisplayTimeString( promotion.getSubmissionEndDate() ) + "<br/>" );

    sb.append( "<br/><p><b>OVERVIEW:</b></p>" );
    sb.append( "<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">" );
    sb.append( "<tr><td><b>Matchgroup</b></td><td><b>Number of Teams</b></td></tr>" );
    for ( Division division : promotion.getDivisions() )
    {
      sb.append( "<tr><td>" + division.getDivisionNameFromCM() + "</td><td>" + teamService.findAllActiveTeamsForPromotionAndDivision( promotion.getId(), division.getId() ).size()
          + " teams.</td></tr>" );
    }
    sb.append( "</table>" );

    sb.append( "<br/><p><b>DETAILS:</b></p>" );

    AssociationRequestCollection arCollection = new AssociationRequestCollection();
    arCollection.add( new TeamAssociationRequest( TeamAssociationRequest.PARTICIPANT ) );

    for ( Division division : promotion.getDivisions() )
    {
      sb.append( "<p><b>" + division.getDivisionNameFromCM() + "</b><br/>" );

      List<Team> teamsForDivision = teamService.findAllActiveTeamsAndPaxForPromotionAndDivision( promotion.getId(), division.getId(), arCollection );
      for ( Team team : teamsForDivision )
      {
        sb.append( team.getParticipant().getNameLFMWithComma() + "<br/>" );
      }
      sb.append( "</p>" );
    }

    sb.append( "</body></html>" );
    getMailingService().submitSystemMailing( "Team Generation Process For promotion " + promotion.getPromoNameFromCM(), sb.toString(), sb.toString() );
  }

  public TeamService getTeamService()
  {
    return teamService;
  }

  public void setTeamService( TeamService teamService )
  {
    this.teamService = teamService;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
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

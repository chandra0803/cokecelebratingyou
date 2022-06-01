/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/leaderBoard/LeaderBoardForm.java,v $
 */

/**
  * BudgetForm.
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
  * <td>dudam</td>
  * <td>Aug 15, 2012</td>
  * <td>1.0</td>
  * <td>created</td>
  * </tr>
  * </table>
  * 
  *
  */

package com.biperf.core.ui.leaderBoard;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderboardParticipantView;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class LeaderBoardForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private String leaderBoardId;
  private String leaderBoardName;
  private String status;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private String leaderBoardDisplayEndDate = DateUtils.displayDateFormatMask;
  private String activityTitle;
  private String activityDate = DateUtils.displayDateFormatMask;
  private String sortOrder;
  private String leaderBoardRules;
  private String leaderBoardRulesText;
  private String notifyMessage = "";
  private int paxCount;
  private String notifyPaxChecked;
  private boolean notifyParticipants;

  private String[] deleteLeaderBoards;

  private String[] deleteLeaderBoardsProgress;
  private String[] deleteLeaderBoardsLive;
  private String[] deleteLeaderBoardsActive;

  // leaderboard owner
  private User user;

  private ArrayList<LeaderboardParticipantView> participants = new ArrayList<LeaderboardParticipantView>(); // form
                                                                                                            // pax

  private List<LeaderboardParticipantView> refPaxs = new ArrayList<LeaderboardParticipantView>(); // preview
                                                                                                  // page
                                                                                                  // to
                                                                                                  // edit
                                                                                                  // page
                                                                                                  // to
                                                                                                  // retain
                                                                                                  // pax

  private List<LeaderboardParticipantView> beans = new ArrayList<LeaderboardParticipantView>();

  // This two properties are used to make a navigation for leaderboard create,edit,preview,cancel
  // and edit leaderboard pax
  private String source;
  private String type;
  private String clientState;
  private String cryptoPassword;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    // Verify that end date, if used, is not in the past
    if ( DateUtils.toDate( endDate ) != null )
    {
      if ( DateUtils.toDate( endDate ).before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
      {
        // The date is before current date
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE ) );
      }
    }

    // Verify that end date is after start date
    if ( DateUtils.toDate( startDate ) != null && DateUtils.toDate( endDate ) != null && DateUtils.toDate( startDate ).after( DateUtils.toDate( endDate ) ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_START_DATE ) );
    }
    if ( StringUtils.isEmpty( this.leaderBoardName ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.PROMOTION_NAME" ) ) );
    }
    if ( StringUtils.isEmpty( this.startDate ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.PROMOTION_START_DATE" ) ) );
    }
    if ( StringUtils.isEmpty( this.activityTitle ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.ACTIVITY_TITLE" ) ) );
    }
    if ( StringUtils.isEmpty( this.leaderBoardRulesText ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.PROMOTION_RULES" ) ) );
    }
    return actionErrors;
  }

  public String getLeaderBoardId()
  {
    return leaderBoardId;
  }

  public void setLeaderBoardId( String leaderBoardId )
  {
    this.leaderBoardId = leaderBoardId;
  }

  public String getLeaderBoardName()
  {
    return leaderBoardName;
  }

  public void setLeaderBoardName( String leaderBoardName )
  {
    this.leaderBoardName = leaderBoardName;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getLeaderBoardDisplayEndDate()
  {
    return leaderBoardDisplayEndDate;
  }

  public void setLeaderBoardDisplayEndDate( String leaderBoardDisplayEndDate )
  {
    this.leaderBoardDisplayEndDate = leaderBoardDisplayEndDate;
  }

  public String getActivityTitle()
  {
    return activityTitle;
  }

  public void setActivityTitle( String activityTitle )
  {
    this.activityTitle = activityTitle;
  }

  public String getSortOrder()
  {
    return sortOrder;
  }

  public void setSortOrder( String sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  public String getLeaderBoardRules()
  {
    return leaderBoardRules;
  }

  public void setLeaderBoardRules( String leaderBoardRules )
  {
    this.leaderBoardRules = leaderBoardRules;
  }

  public int getPaxCount()
  {
    return paxCount;
  }

  public boolean isNotifyParticipants()
  {
    return notifyParticipants || "true".equalsIgnoreCase( getNotifyPaxChecked() );
  }

  public void setNotifyParticipants( boolean notifyParticipants )
  {
    this.notifyParticipants = notifyParticipants;
  }

  public void setPaxCount( int paxCount )
  {
    this.paxCount = paxCount;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public String getLeaderBoardRulesText()
  {
    return leaderBoardRulesText;
  }

  public void setLeaderBoardRulesText( String leaderBoardRulesText )
  {
    this.leaderBoardRulesText = leaderBoardRulesText;
  }

  public String getActivityDate()
  {
    return activityDate;
  }

  public void setActivityDate( String activityDate )
  {
    this.activityDate = activityDate;
  }

  public String getNotifyMessage()
  {
    return notifyMessage;
  }

  public void setNotifyMessage( String notifyMessage )
  {
    this.notifyMessage = notifyMessage;
  }

  public ArrayList<LeaderboardParticipantView> getParticipantsAsList()
  {
    return participants;
  }

  public LeaderboardParticipantView getParticipants( int index )
  {
    while ( index >= participants.size() )
    {
      participants.add( new LeaderboardParticipantView() );
    }
    return (LeaderboardParticipantView)participants.get( index );
  }

  public void setParticipantsAsList( LeaderboardParticipantView participant )
  {
    participants.add( participant );
  }

  public List<LeaderboardParticipantView> getRefPaxs()
  {
    return refPaxs;
  }

  public void setRefPaxs( List<LeaderboardParticipantView> refPaxs )
  {
    this.refPaxs = refPaxs;
  }

  public String getSource()
  {
    return source;
  }

  public void setSource( String source )
  {
    this.source = source;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String[] getDeleteLeaderBoards()
  {
    return deleteLeaderBoards;
  }

  public void setDeleteLeaderBoards( String[] deleteLeaderBoards )
  {
    this.deleteLeaderBoards = deleteLeaderBoards;
  }

  public String[] getDeleteLeaderBoardsProgress()
  {
    return deleteLeaderBoardsProgress;
  }

  public void setDeleteLeaderBoardsProgress( String[] deleteLeaderBoardsProgress )
  {
    this.deleteLeaderBoardsProgress = deleteLeaderBoardsProgress;
  }

  public String[] getDeleteLeaderBoardsLive()
  {
    return deleteLeaderBoardsLive;
  }

  public void setDeleteLeaderBoardsLive( String[] deleteLeaderBoardsLive )
  {
    this.deleteLeaderBoardsLive = deleteLeaderBoardsLive;
  }

  public String[] getDeleteLeaderBoardsActive()
  {
    return deleteLeaderBoardsActive;
  }

  public void setDeleteLeaderBoardsActive( String[] deleteLeaderBoardsActive )
  {
    this.deleteLeaderBoardsActive = deleteLeaderBoardsActive;
  }

  public List<LeaderboardParticipantView> getBeans()
  {
    return beans;
  }

  public String getNotifyPaxChecked()
  {
    return notifyPaxChecked;
  }

  public void setNotifyPaxChecked( String notifyPaxChecked )
  {
    this.notifyPaxChecked = notifyPaxChecked;
  }

  public void setBeans( List<LeaderboardParticipantView> beans )
  {
    this.beans = beans;
  }

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
  }

  public String getCryptoPassword()
  {
    return cryptoPassword;
  }

  public void setCryptoPassword( String cryptoPassword )
  {
    this.cryptoPassword = cryptoPassword;
  }

  public void load( LeaderBoard leaderBoard )
  {
    this.setActivityTitle( leaderBoard.getActivityTitle() );
    this.setEndDate( DateUtils.toDisplayString( leaderBoard.getEndDate() ) );
    this.setLeaderBoardDisplayEndDate( DateUtils.toDisplayString( leaderBoard.getDisplayEndDate() ) );
    this.setLeaderBoardId( leaderBoard.getId().toString() );
    this.setUser( leaderBoard.getUser() );
    this.setLeaderBoardName( leaderBoard.getName() );
    this.setLeaderBoardRules( leaderBoard.getRulescmAsset() );
    this.setSortOrder( leaderBoard.getSortOrder() );
    this.setStartDate( DateUtils.toDisplayString( leaderBoard.getStartDate() ) );
    this.setStatus( leaderBoard.getStatus() );
    this.setActivityDate( DateUtils.toDisplayString( leaderBoard.getActivityDate() ) );
    if ( leaderBoard.getRulescmAsset() != null )
    {
      this.setLeaderBoardRulesText( leaderBoard.getRulesTextFromCM().trim() );
      this.setLeaderBoardRules( leaderBoard.getRulescmAsset() );
    }
    if ( !StringUtils.isEmpty( leaderBoard.getNotifyMessage() ) )
    {
      this.setNotifyMessage( leaderBoard.getNotifyMessage() );
    }
    if ( leaderBoard.getId() != null )
    {
      this.setLeaderBoardId( leaderBoard.getId().toString() );
    }
    if ( leaderBoard.getParticipants() != null && leaderBoard.getParticipants().size() > 0 )
    {
      this.setPaxCount( leaderBoard.getParticipants().size() );
    }
    else
    {
      this.setPaxCount( 0 );
    }
  }

  public LeaderBoard toDomainObject( LeaderBoard leaderBoard )
  {
    if ( this.leaderBoardId != null && !this.leaderBoardId.equals( "" ) )
    {
      leaderBoard.setId( Long.parseLong( this.leaderBoardId ) );
    }
    leaderBoard.setName( this.leaderBoardName );
    leaderBoard.setStartDate( DateUtils.toDate( this.startDate ) );
    leaderBoard.setDisplayEndDate( DateUtils.toDate( this.leaderBoardDisplayEndDate ) );
    leaderBoard.setActivityTitle( this.activityTitle );
    leaderBoard.setActivityDate( DateUtils.toDate( this.activityDate ) );
    leaderBoard.setSortOrder( this.sortOrder );
    leaderBoard.setEndDate( DateUtils.toDate( this.endDate ) );
    leaderBoard.setRulescmAssetText( this.leaderBoardRulesText );
    leaderBoard.setNotifyMessage( this.notifyMessage );
    if ( this.user != null )
    {
      leaderBoard.setUser( this.user );
    }
    if ( this.status != null )
    {
      leaderBoard.setStatus( this.status );
    }
    return leaderBoard;
  }
}


package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.leaderboard.LeaderBoardAssociationRequest;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.utils.StringUtil;

public class LeaderBoardMailingProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( LeaderBoardMailingProcess.class );

  public static final String PROCESS_NAME = "LeaderBoard Mailing Process";
  public static final String BEAN_NAME = "leaderBoardMailingProcess";

  private MailingService mailingService;
  private MessageService messageService;
  private LeaderBoardService leaderBoardService;

  private static final String SORT_DESCENDING = "desc";
  private String leaderBoardId;
  private String createLb;
  private String notifyMessage;
  private boolean createNewLB;

  protected void onExecute()
  {
    LeaderBoard leaderBoard = null;

    if ( !StringUtil.isEmpty( leaderBoardId ) )
    {
      try
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new LeaderBoardAssociationRequest( LeaderBoardAssociationRequest.ALL_PAX ) );
        leaderBoard = leaderBoardService.getLeaderBoardByIdWithAssociations( Long.parseLong( leaderBoardId ), associationRequestCollection );
        createNewLB = Boolean.valueOf( createLb ).booleanValue();

        sendLeaderBoardCreateUpdateEmail( leaderBoard, notifyMessage, createNewLB );
      }

      catch( ServiceErrorException e )
      {
        log.error( "Cannot process Leader Board" + e );
      }
    }
  }

  /**
   * @param leaderBoard
   * @param notifyMessage
   * @param lbPaxs
   */
  private void sendLeaderBoardCreateUpdateEmail( LeaderBoard leaderBoard, String notifyMessage, boolean createNewLB )
  {
    int rank = 1;
    int sameRankCount = 0;
    int paxCount = leaderBoard.getParticipants().size();
    Set<LeaderBoardParticipant> lbPaxs = leaderBoard.getParticipants();
    Message message = messageService.getMessageByCMAssetCode( MessageService.LEADERBOARD_CREATE_UPDATE_NOTIFICATION_MESSAGE_CM_ASSET_CODE );
    List<LeaderBoardParticipant> leaderPaxList = new ArrayList<LeaderBoardParticipant>( lbPaxs );
    if ( leaderBoard.getSortOrder().equals( SORT_DESCENDING ) )
    {
      Collections.sort( leaderPaxList, DESC_COMPARATOR );
    }
    else
    {
      Collections.sort( leaderPaxList, ASCE_COMPARATOR );
    }
    LeaderBoardParticipant currentObj = null;
    for ( LeaderBoardParticipant lbPax : leaderPaxList )
    {
      if ( currentObj != null && !currentObj.getScore().equalsIgnoreCase( lbPax.getScore() ) )
      {
        rank++;
        rank = rank + sameRankCount;
        sameRankCount = 0;
      }
      else if ( currentObj != null && currentObj.getScore().equalsIgnoreCase( lbPax.getScore() ) )
      {
        sameRankCount++;
      }

      lbPax.setLeaderBoardPaxRank( rank );
      Mailing mailing = mailingService.buildLeaderboardNotificationMailing( leaderBoard, lbPax, paxCount, message, createNewLB, notifyMessage );
      mailingService.submitMailing( mailing, null );
      currentObj = lbPax;
    }
  }

  private static Comparator<LeaderBoardParticipant> ASCE_COMPARATOR = new Comparator<LeaderBoardParticipant>()
  {
    public int compare( LeaderBoardParticipant lbp1, LeaderBoardParticipant lbp2 )
    {
      return Integer.parseInt( lbp1.getScore() ) - Integer.parseInt( lbp2.getScore() );
    }
  };

  private static Comparator<LeaderBoardParticipant> DESC_COMPARATOR = new Comparator<LeaderBoardParticipant>()
  {
    public int compare( LeaderBoardParticipant lbp1, LeaderBoardParticipant lbp2 )
    {
      return Integer.parseInt( lbp2.getScore() ) - Integer.parseInt( lbp1.getScore() );
    }
  };

  public void setLeaderBoardId( String leaderBoardId )
  {
    this.leaderBoardId = leaderBoardId;
  }

  public void setCreateLb( String createLb )
  {
    this.createLb = createLb;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setLeaderBoardService( LeaderBoardService leaderBoardService )
  {
    this.leaderBoardService = leaderBoardService;
  }

  public void setNotifyMessage( String notifyMessage )
  {
    this.notifyMessage = notifyMessage;
  }

}


package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class SSIStackRankTableViewBean
{
  private List<SSIStackRankParticipantViewBean> stackRankParticipants;
  private List<WebErrorMessage> messages;
  private Integer total;
  private Integer perPage;
  private Integer current;

  public SSIStackRankTableViewBean()
  {

  }

  public SSIStackRankTableViewBean( WebErrorMessage message )
  {
    this.messages = new ArrayList<WebErrorMessage>();
    this.messages.add( message );
  }

  public SSIStackRankTableViewBean( List<SSIContestStackRankPaxValueBean> stackRanks, String role, int precision, SSIContestValueBean valueBean, Long contestId )
  {
    if ( stackRanks != null && stackRanks.size() > 0 )
    {
      this.stackRankParticipants = new ArrayList<SSIStackRankParticipantViewBean>();
      String activityPrefix = SSIContestUtil.getActivityPrefix( valueBean );
      for ( SSIContestStackRankPaxValueBean stackRank : stackRanks )
      {
        SSIStackRankParticipantViewBean ssiStackRankParticipantViewBean = new SSIStackRankParticipantViewBean();
        ssiStackRankParticipantViewBean.setAvatarUrl( stackRank.getAvatarUrl() );
        ssiStackRankParticipantViewBean.setFirstName( stackRank.getFirstName() );
        ssiStackRankParticipantViewBean.setLastName( stackRank.getLastName() );
        ssiStackRankParticipantViewBean.setParticipantId( stackRank.getParticipantId() );
        ssiStackRankParticipantViewBean.setRank( stackRank.getRank() );
        ssiStackRankParticipantViewBean.setScore( stackRank.getScore() != null ? activityPrefix + SSIContestUtil.getFormattedValue( stackRank.getScore(), precision ) : "" );

        if ( SSIContest.CONTEST_ROLE_MGR.equals( role ) )
        {
          if ( stackRank.getTeamMember() )
          {
            ssiStackRankParticipantViewBean.setTeamMember( stackRank.getTeamMember() );
          }
        }
        else if ( SSIContest.CONTEST_ROLE_PAX.equals( role ) )
        {
          // TO HIGHLIGHT CURRENT USER RECORD
          ssiStackRankParticipantViewBean.setTeamMember( UserManager.getUserId().equals( stackRank.getParticipantId() ) );
        }
        this.stackRankParticipants.add( ssiStackRankParticipantViewBean );
        if ( stackRank.getTeamMember() != null && stackRank.getTeamMember().booleanValue() || SSIContest.CONTEST_ROLE_CREATOR.equals( role ) )
        {
          ssiStackRankParticipantViewBean.setContestUrl( SSIContestUtil.populateParticipantDetailPageUrl( contestId, stackRank.getParticipantId() ) );
        }
      }
    }

  }

  public void addPaginationParams( int total, int perPage, int current )
  {
    this.total = total;
    this.perPage = perPage;
    this.current = current;
  }

  public Integer getTotal()
  {
    return total;
  }

  public void setTotal( Integer total )
  {
    this.total = total;
  }

  public Integer getPerPage()
  {
    return perPage;
  }

  public void setPerPage( Integer perPage )
  {
    this.perPage = perPage;
  }

  public Integer getCurrent()
  {
    return current;
  }

  public void setCurrent( Integer current )
  {
    this.current = current;
  }

  public List<SSIStackRankParticipantViewBean> getStackRankParticipants()
  {
    return stackRankParticipants;
  }

  public void setStackRankParticipants( List<SSIStackRankParticipantViewBean> stackRankParticipants )
  {
    this.stackRankParticipants = stackRankParticipants;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

}

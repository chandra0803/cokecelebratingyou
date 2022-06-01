
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.value.ssi.SSIContestStackRankParticipantPayoutValueBean;

public class SSIContestPayoutsForm extends BaseForm
{
  private String id; // contest id;
  private Long activityId; // DTGT Contest Activity Id;
  private String sortedBy;
  private String sortedOn;
  private String contestPayoutsTotalJson;
  private String closeContest;
  private String method;
  private int currentPage;

  private List<SSIContestStackRankParticipantPayoutValueBean> participants = new ArrayList<SSIContestStackRankParticipantPayoutValueBean>();

  public SSIContestStackRankParticipantPayoutValueBean getParticipants( int index )
  {
    while ( index >= participants.size() )
    {
      participants.add( new SSIContestStackRankParticipantPayoutValueBean() );
    }
    return participants.get( index );
  }

  public List<SSIContestStackRankParticipantPayoutValueBean> getParticipantsAsList()
  {
    return participants;
  }

  public void setParticipantsAsList( SSIContestStackRankParticipantPayoutValueBean participantPayout )
  {
    this.participants.add( participantPayout );
  }

  public int getParticipantsSize()
  {
    if ( this.participants != null )
    {
      return this.participants.size();
    }
    return 0;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getContestPayoutsTotalJson()
  {
    return contestPayoutsTotalJson;
  }

  public void setContestPayoutsTotalJson( String contestPayoutsTotalJson )
  {
    this.contestPayoutsTotalJson = contestPayoutsTotalJson;
  }

  public String getCloseContest()
  {
    return closeContest;
  }

  public void setCloseContest( String closeContest )
  {
    this.closeContest = closeContest;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

}

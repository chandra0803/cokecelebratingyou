
package com.biperf.core.domain.participant;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class ParticipantSearchListView
{
  private List<WebErrorMessage> messages;
  private List<ParticipantSearchView> participants = new ArrayList<ParticipantSearchView>();
  private int totalCount;

  private boolean allowBadge;
  private boolean allowPublicRecFollowList;

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public List<ParticipantSearchView> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<ParticipantSearchView> participants )
  {
    this.participants = participants;
  }

  public int getTotalCount()
  {
    return totalCount;
  }

  public void setTotalCount( int totalCount )
  {
    this.totalCount = totalCount;
  }

  public void setAllowBadge( boolean allowBadge )
  {
    this.allowBadge = allowBadge;
  }

  public boolean isAllowBadge()
  {
    return allowBadge;
  }

  public void setAllowPublicRecFollowList( boolean allowPublicRecFollowList )
  {
    this.allowPublicRecFollowList = allowPublicRecFollowList;
  }

  public boolean isAllowPublicRecFollowList()
  {
    return allowPublicRecFollowList;
  }

}

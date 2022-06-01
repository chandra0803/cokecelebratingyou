
package com.biperf.core.ui.groups;

import java.util.List;

public class ParticipantGroupModel
{
  private Long groupId;
  private String groupName;
  private String searchToken;
  private List<Long> groupMembers;
  private Long userId;

  public Long getGroupId()
  {
    return groupId;
  }

  public void setGroupId( Long groupId )
  {
    this.groupId = groupId;
  }

  public String getSearchToken()
  {
    return searchToken;
  }

  public void setSearchToken( String searchToken )
  {
    this.searchToken = searchToken;
  }

  public List<Long> getGroupMembers()
  {
    return groupMembers;
  }

  public void setGroupMembers( List<Long> groupMembers )
  {
    this.groupMembers = groupMembers;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName( String groupName )
  {
    this.groupName = groupName;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }
}

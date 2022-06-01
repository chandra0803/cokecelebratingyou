
package com.biperf.core.ui.engagement;

import java.util.List;

/**
 * 
 * EngagementTeamIndividualsMembersView.
 * 
 * @author kandhi
 * @since May 29, 2014
 * @version 1.0
 */
public class EngagementTeamIndividualsMembersView
{

  public static final int TEAM_MEMBERS_PER_PAGE = 10;

  private Long nodeId;
  private Long userId;
  private String userName;
  private String avatarUrl;
  private boolean unrecognized;
  private List<EngagementTeamIndividualsMembersDataView> data;
  private String firstName;
  private String lastName;

  public EngagementTeamIndividualsMembersView( Long nodeId,
                                               Long userId,
                                               String firstName,
                                               String lastName,
                                               String avatarUrl,
                                               boolean unrecognized,
                                               List<EngagementTeamIndividualsMembersDataView> data )
  {
    super();
    this.nodeId = nodeId;
    this.userId = userId;
    this.userName = lastName + ", " + firstName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.unrecognized = unrecognized;
    this.data = data;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public boolean isUnrecognized()
  {
    return unrecognized;
  }

  public void setUnrecognized( boolean unrecognized )
  {
    this.unrecognized = unrecognized;
  }

  public List<EngagementTeamIndividualsMembersDataView> getData()
  {
    return data;
  }

  public void setData( List<EngagementTeamIndividualsMembersDataView> data )
  {
    this.data = data;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

}


package com.biperf.core.domain.leaderboard;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

//This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class LeaderBoardActionUrls implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String editLeaderBoardUrl;
  private String copyLeaderBoardUrl;

  @JsonProperty( "editPromoInfo" )
  public String getEditLeaderBoardUrl()
  {
    return editLeaderBoardUrl;
  }

  public void setEditLeaderBoardUrl( String editLeaderBoardUrl )
  {
    this.editLeaderBoardUrl = editLeaderBoardUrl;
  }

  @JsonProperty( "copyToNew" )
  public String getCopyLeaderBoardUrl()
  {
    return copyLeaderBoardUrl;
  }

  public void setCopyLeaderBoardUrl( String copyLeaderBoardUrl )
  {
    this.copyLeaderBoardUrl = copyLeaderBoardUrl;
  }

}

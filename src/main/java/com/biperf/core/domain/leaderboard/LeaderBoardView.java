
package com.biperf.core.domain.leaderboard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class object is used to generate json object for new G5 layout.
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
 * <td>sep 7, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LeaderBoardView
{
  // This is for future use to keep additional messages
  private String[] messages = {};
  @JsonProperty( "leaderboardSets" )
  private List<LeaderBoardSet> leaderBoardSets = new ArrayList<LeaderBoardSet>();

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<LeaderBoardSet> getLeaderBoardSets()
  {
    return leaderBoardSets;
  }

  public void setLeaderBoardSets( List<LeaderBoardSet> leaderBoardSets )
  {
    this.leaderBoardSets = leaderBoardSets;
  }

}


package com.biperf.core.domain.leaderboard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class object can handle list of leaderboards with name and nameid, nameId can be active,pending or archived.
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
public class LeaderBoardSet
{
  private String nameId;
  private String name;
  private String emptyMessage;
  private List<LeaderBoard> leaderBoardsList = new ArrayList<LeaderBoard>();

  public String getNameId()
  {
    return nameId;
  }

  public void setNameId( String nameId )
  {
    this.nameId = nameId;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getEmptyMessage()
  {
    return emptyMessage;
  }

  public void setEmptyMessage( String emptyMessage )
  {
    this.emptyMessage = emptyMessage;
  }

  @JsonProperty( "leaderboards" )
  public List<LeaderBoard> getLeaderBoardsList()
  {
    return leaderBoardsList;
  }

  public void setLeaderBoardsList( List<LeaderBoard> leaderBoardsList )
  {
    this.leaderBoardsList = leaderBoardsList;
  }

}

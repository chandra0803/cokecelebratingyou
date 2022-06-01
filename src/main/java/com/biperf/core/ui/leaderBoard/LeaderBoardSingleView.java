
package com.biperf.core.ui.leaderBoard;

import com.biperf.core.domain.leaderboard.LeaderBoard;

public class LeaderBoardSingleView
{

  private String[] messages = {};
  private LeaderBoard leaderboard = new LeaderBoard();

  public LeaderBoardSingleView()
  {

  }

  public LeaderBoardSingleView( LeaderBoard leaderBoard )
  {
    this.leaderboard = leaderBoard;
  }

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public LeaderBoard getLeaderboard()
  {
    return leaderboard;
  }

  public void setLeaderboard( LeaderBoard leaderboard )
  {
    this.leaderboard = leaderboard;
  }

}

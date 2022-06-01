
package com.biperf.core.domain.promotion;

import java.io.Serializable;

import com.biperf.core.domain.enums.SmackTalkTabType;

public class SmackTalkSetCountBean implements Serializable
{

  private static final long serialVersionUID = 1L;

  private long globalTabCount;
  private long teamTabCount;
  private long mineTabCount;

  public long getGlobalTabCount()
  {
    return globalTabCount;
  }

  public void setGlobalTabCount( long globalTabCount )
  {
    this.globalTabCount = globalTabCount;
  }

  public long getTeamTabCount()
  {
    return teamTabCount;
  }

  public void setTeamTabCount( long teamTabCount )
  {
    this.teamTabCount = teamTabCount;
  }

  public long getMineTabCount()
  {
    return mineTabCount;
  }

  public void setMineTabCount( long mineTabCount )
  {
    this.mineTabCount = mineTabCount;
  }

  public void setCount( SmackTalkTabType tabType, long count )
  {
    if ( tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.GLOBAL_TAB ) ) )
    {
      this.globalTabCount = count;
    }
    else if ( tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.TEAM_TAB ) ) )
    {
      this.teamTabCount = count;
    }
    else if ( tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.ME_TAB ) ) )
    {
      this.mineTabCount = count;
    }
  }

  public long getCount( SmackTalkTabType tabType )
  {
    if ( tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.GLOBAL_TAB ) ) )
    {
      return globalTabCount;
    }
    else if ( tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.TEAM_TAB ) ) )
    {
      return teamTabCount;
    }
    else if ( tabType.equals( SmackTalkTabType.lookup( SmackTalkTabType.ME_TAB ) ) )
    {
      return mineTabCount;
    }
    else
    {
      return 0;
    }
  }

}

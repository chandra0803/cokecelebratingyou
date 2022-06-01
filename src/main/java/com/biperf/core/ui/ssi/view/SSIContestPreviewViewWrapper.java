
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;

public class SSIContestPreviewViewWrapper
{
  private SSIContest contest;
  private int participantsCount;
  private int managersCount;
  private int superViewersCount;
  private String ssiContestClientState;
  private SSIContestValueBean contestValueBean;
  private SSIContestPayoutObjectivesTotalsValueBean contestPayoutTotalsValueBean;
  private SSIContestUniqueCheckValueBean contestUniqueCheckValueBean;
  private SSIContest contestWithActivities;
  private Long totalMaxPayout;
  private List<SSIContestLevel> ssiContestLevels;
  private String bonusRow;
  private String sysUrl;
  private String message;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public int getParticipantsCount()
  {
    return participantsCount;
  }

  public void setParticipantsCount( int participantsCount )
  {
    this.participantsCount = participantsCount;
  }

  public int getManagersCount()
  {
    return managersCount;
  }

  public void setManagersCount( int managersCount )
  {
    this.managersCount = managersCount;
  }

  public int getSuperViewersCount()
  {
    return superViewersCount;
  }

  public void setSuperViewersCount( int superViewersCount )
  {
    this.superViewersCount = superViewersCount;
  }

  public String getSsiContestClientState()
  {
    return ssiContestClientState;
  }

  public void setSsiContestClientState( String ssiContestClientState )
  {
    this.ssiContestClientState = ssiContestClientState;
  }

  public SSIContestValueBean getContestValueBean()
  {
    return contestValueBean;
  }

  public void setContestValueBean( SSIContestValueBean contestValueBean )
  {
    this.contestValueBean = contestValueBean;
  }

  public SSIContestPayoutObjectivesTotalsValueBean getContestPayoutTotalsValueBean()
  {
    return contestPayoutTotalsValueBean;
  }

  public void setContestPayoutTotalsValueBean( SSIContestPayoutObjectivesTotalsValueBean contestPayoutTotalsValueBean )
  {
    this.contestPayoutTotalsValueBean = contestPayoutTotalsValueBean;
  }

  public SSIContestUniqueCheckValueBean getContestUniqueCheckValueBean()
  {
    return contestUniqueCheckValueBean;
  }

  public void setContestUniqueCheckValueBean( SSIContestUniqueCheckValueBean contestUniqueCheckValueBean )
  {
    this.contestUniqueCheckValueBean = contestUniqueCheckValueBean;
  }

  public SSIContest getContestWithActivities()
  {
    return contestWithActivities;
  }

  public void setContestWithActivities( SSIContest contestWithActivities )
  {
    this.contestWithActivities = contestWithActivities;
  }

  public Long getTotalMaxPayout()
  {
    return totalMaxPayout;
  }

  public void setTotalMaxPayout( Long totalMaxPayout )
  {
    this.totalMaxPayout = totalMaxPayout;
  }

  public List<SSIContestLevel> getSsiContestLevels()
  {
    return ssiContestLevels;
  }

  public void setSsiContestLevels( List<SSIContestLevel> ssiContestLevels )
  {
    this.ssiContestLevels = ssiContestLevels;
  }

  public String getBonusRow()
  {
    return bonusRow;
  }

  public void setBonusRow( String bonusRow )
  {
    this.bonusRow = bonusRow;
  }

  public String getSysUrl()
  {
    return sysUrl;
  }

  public void setSysUrl( String sysUrl )
  {
    this.sysUrl = sysUrl;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

}

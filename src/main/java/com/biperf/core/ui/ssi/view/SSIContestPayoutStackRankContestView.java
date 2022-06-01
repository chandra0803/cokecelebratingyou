
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestStackRankPayout;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.value.ssi.SSIContestRankValueBean;

/**
 * SSIContestPayoutStackRankContestView.
 * 
 * @author dudam
 * @since Feb 16, 2015
 * @version 1.0
 */
public class SSIContestPayoutStackRankContestView extends SSIContestPayoutContestView
{

  private String activityDescription;
  private String stackRankingOrder;
  private String includeMinimumQualifier;
  private Double minimumQualifier;
  private Double contestGoal;
  private int participantCount;
  private List<SSIContestRankValueBean> ranks = new ArrayList<SSIContestRankValueBean>();

  public SSIContestPayoutStackRankContestView( SSIContest contest, String ssiContestClientState, List<Currency> currencies, String sysUrl, List<SSIContestBillCodeView> billCodes )
  {
    super( contest, ssiContestClientState, currencies, billCodes );
    setNextUrl( sysUrl + PageConstants.SSI_MANAGE_STACK_RANK );
    this.activityDescription = contest.getActivityDescription();
    this.stackRankingOrder = contest.getStackRankOrder();
    this.includeMinimumQualifier = contest.getIncludeStackRankQualifier() != null ? contest.getIncludeStackRankQualifier() ? "yes" : "no" : null;
    this.minimumQualifier = contest.getStackRankQualifierAmount();
    this.contestGoal = contest.getContestGoal();
    populateRanks( contest );
  }

  private void populateRanks( SSIContest contest )
  {
    for ( SSIContestStackRankPayout stackRank : contest.getStackRankPayouts() )
    {
      SSIContestRankValueBean rank = new SSIContestRankValueBean();
      rank.setBadgeId( stackRank.getBadgeRule() != null ? stackRank.getBadgeRule().getId() : null );
      rank.setId( stackRank.getId() );
      rank.setPayoutAmount( stackRank.getPayoutAmount() );
      rank.setPayoutDescription( stackRank.getPayoutDescription() );
      rank.setRank( stackRank.getRankPosition() );
      this.ranks.add( rank );
    }
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getStackRankingOrder()
  {
    return stackRankingOrder;
  }

  public void setStackRankingOrder( String stackRankingOrder )
  {
    this.stackRankingOrder = stackRankingOrder;
  }

  public String getIncludeMinimumQualifier()
  {
    return includeMinimumQualifier;
  }

  public void setIncludeMinimumQualifier( String includeMinimumQualifier )
  {
    this.includeMinimumQualifier = includeMinimumQualifier;
  }

  public Double getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( Double minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public Double getContestGoal()
  {
    return contestGoal;
  }

  public void setContestGoal( Double contestGoal )
  {
    this.contestGoal = contestGoal;
  }

  public int getParticipantCount()
  {
    return participantCount;
  }

  public void setParticipantCount( int participantCount )
  {
    this.participantCount = participantCount;
  }

  public List<SSIContestRankValueBean> getRanks()
  {
    return ranks;
  }

  public void setRanks( List<SSIContestRankValueBean> ranks )
  {
    this.ranks = ranks;
  }

}

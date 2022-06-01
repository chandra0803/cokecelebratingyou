
package com.biperf.core.ui.ssi.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * SSIContestStackRankPayoutView.
 * 
 * @author dudam
 * @since Mar 26, 2015
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestStackRankPayoutView
{

  private Long rank;
  private String payout;
  private String payoutDescription;
  private SSIPaxContestBadgeView badge;

  public SSIContestStackRankPayoutView()
  {

  }

  public SSIContestStackRankPayoutView( Long rank, String payout, String payoutDescription, Long badgeId, String badgeName, String badgeUrl )
  {
    this.rank = rank;
    this.payout = payout;
    this.payoutDescription = payoutDescription;
    this.badge = new SSIPaxContestBadgeView( badgeId, badgeName, badgeUrl );
  }

  public Long getRank()
  {
    return rank;
  }

  public void setRank( Long rank )
  {
    this.rank = rank;
  }

  public String getPayout()
  {
    return payout;
  }

  public void setPayout( String payout )
  {
    this.payout = payout;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public SSIPaxContestBadgeView getBadge()
  {
    return badge;
  }

  public void setBadge( SSIPaxContestBadgeView badge )
  {
    this.badge = badge;
  }

}

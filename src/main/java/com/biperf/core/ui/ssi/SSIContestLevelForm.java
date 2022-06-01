
package com.biperf.core.ui.ssi;

import java.text.MessageFormat;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.ui.ssi.view.SSIPaxContestBadgeView;
import com.biperf.core.utils.SSIContestUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestLevelForm implements Comparable<SSIContestLevelForm>
{

  private Long id;
  private String name;
  private Long payout;
  private String activityLabel;
  private String payoutDescription;
  private SSIPaxContestBadgeView badge = new SSIPaxContestBadgeView();
  private Integer sequenceNumber;
  private Double amount;
  private Long badgeId;

  public SSIContestLevelForm()
  {

  }

  public SSIContestLevelForm( SSIContestLevel ssiContestLevel, SSIContest contest )
  {
    this.id = ssiContestLevel.getId();
    this.name = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { ssiContestLevel.getSequenceNumber() } );
    this.payout = ssiContestLevel.getPayoutAmount();
    this.activityLabel = SSIContestUtil.getBaselineTypeLabel( contest );
    this.payoutDescription = ssiContestLevel.getPayoutDesc();
    this.sequenceNumber = ssiContestLevel.getSequenceNumber();
    this.amount = ssiContestLevel.getGoalAmount();
    this.badgeId = ssiContestLevel.getBadgeRule() != null ? ssiContestLevel.getBadgeRule().getId() : null;
    this.badge = new SSIPaxContestBadgeView( ssiContestLevel.getBadgeRule() );
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Long getPayout()
  {
    return payout;
  }

  public void setPayout( Long payout )
  {
    this.payout = payout;
  }

  public String getActivityLabel()
  {
    return activityLabel;
  }

  public void setActivityLabel( String activityLabel )
  {
    this.activityLabel = activityLabel;
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

  public Integer getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( Integer sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public Double getAmount()
  {
    return amount;
  }

  public void setAmount( Double amount )
  {
    this.amount = amount;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  @Override
  public int compareTo( SSIContestLevelForm o )
  {
    return this.name.compareToIgnoreCase( ( (SSIContestLevelForm)o ).getName() );
  }

}

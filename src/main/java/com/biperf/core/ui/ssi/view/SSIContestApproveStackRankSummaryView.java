
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestPayoutStackRankTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;

/**
 * @author dudam
 * @since Dec 17, 2014
 * @version 1.0
 */
public class SSIContestApproveStackRankSummaryView extends SSIContestApproveSummaryView
{
  private Long ranksToPayout;
  private String minimumQualifier;
  private String goal;
  private String maximumPayout;
  private List<OrgTypes> orgTypes;

  public SSIContestApproveStackRankSummaryView()
  {

  }

  public SSIContestApproveStackRankSummaryView( SSIContest contest, SSIContestValueBean valueBean, SSIContestPayoutStackRankTotalsValueBean totals )
  {
    super( contest, valueBean );
    int decimalPrecision = SSIContestUtil.getPrecision( valueBean.getActivityMeasureType().getCode() );

    this.ranksToPayout = totals.getTotalRanks();
    this.maximumPayout = SSIContestUtil.getPayoutPrefix( valueBean ) + SSIContestUtil.getFormattedValue( totals.getTotalPayout(), 0 ) + SSIContestUtil.getPayoutSuffix( valueBean );
    this.minimumQualifier = SSIContestUtil.getFormattedValue( contest.getStackRankQualifierAmount(), decimalPrecision ) != null
        ? SSIContestUtil.getPayoutPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contest.getStackRankQualifierAmount(), decimalPrecision )
        : "";
    this.goal = SSIContestUtil.getActivityPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contest.getContestGoal(), decimalPrecision );
  }

  public Long getRanksToPayout()
  {
    return ranksToPayout;
  }

  public void setRanksToPayout( Long ranksToPayout )
  {
    this.ranksToPayout = ranksToPayout;
  }

  public String getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( String minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public String getGoal()
  {
    return goal;
  }

  public void setGoal( String goal )
  {
    this.goal = goal;
  }

  public String getMaximumPayout()
  {
    return maximumPayout;
  }

  public void setMaximumPayout( String maximumPayout )
  {
    this.maximumPayout = maximumPayout;
  }

  public List<OrgTypes> getOrgTypes()
  {
    return orgTypes;
  }

  public void setOrgTypes( List<OrgTypes> orgTypes )
  {
    this.orgTypes = orgTypes;
  }

  class OrgTypes
  {
    private Long id;
    private String name;
    private int count;
    private String payoutType;
    private String ranksToPayout;
    private String payoutAmount;

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

    public int getCount()
    {
      return count;
    }

    public void setCount( int count )
    {
      this.count = count;
    }

    public String getPayoutType()
    {
      return payoutType;
    }

    public void setPayoutType( String payoutType )
    {
      this.payoutType = payoutType;
    }

    public String getRanksToPayout()
    {
      return ranksToPayout;
    }

    public void setRanksToPayout( String ranksToPayout )
    {
      this.ranksToPayout = ranksToPayout;
    }

    public String getPayoutAmount()
    {
      return payoutAmount;
    }

    public void setPayoutAmount( String payoutAmount )
    {
      this.payoutAmount = payoutAmount;
    }

  }

}

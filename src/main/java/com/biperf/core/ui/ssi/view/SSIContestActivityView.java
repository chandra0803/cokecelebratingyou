
package com.biperf.core.ui.ssi.view;

import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestValueBean;

/**
 * 
 * SSIContestActivityView.
 * 
 * @author kandhi
 * @since Dec 22, 2014
 * @version 1.0
 */
public class SSIContestActivityView implements Comparable<SSIContestActivityView>
{

  private Long id;
  private String forEvery; // Double
  private String minQualifier; // Double
  private String maxPayout; // participants X payoutAmount //Long
  private String maxPotential;
  private String goal;
  private String payoutDescription;// redundant attribute of value description
  private String description;
  private String individualPayoutCap;
  private String willEarn;
  private String payoutAmount;
  private String valueDescription;

  public SSIContestActivityView()
  {
    super();
  }

  public SSIContestActivityView( SSIContest ssiContest, SSIContestActivity contestActivity, int participantsCount )
  {
    int precision = ssiContest.getActivityMeasureType() != null ? SSIContestUtil.getPrecision( ssiContest.getActivityMeasureType().getCode() ) : 2;
    this.id = contestActivity.getId();
    this.description = contestActivity.getDescription();
    if ( ssiContest.getActivityMeasureType() != null && SSIActivityMeasureType.CURRENCY_CODE.equals( ssiContest.getActivityMeasureType().getCode() ) )
    {
      this.forEvery = contestActivity.getIncrementAmount() != null ? String.valueOf( contestActivity.getIncrementAmount() ) : null;
      this.minQualifier = contestActivity.getMinQualifier() != null ? String.valueOf( contestActivity.getMinQualifier() ) : null;
      this.goal = contestActivity.getGoalAmount() != null ? String.valueOf( contestActivity.getGoalAmount() ) : null;
    }
    else if ( ssiContest.getActivityMeasureType() != null && SSIActivityMeasureType.UNIT_CODE.equals( ssiContest.getActivityMeasureType().getCode() ) )
    {
      this.forEvery = contestActivity.getIncrementAmount() != null ? String.valueOf( contestActivity.getIncrementAmount() ) : null;
      this.minQualifier = contestActivity.getMinQualifier() != null ? String.valueOf( contestActivity.getMinQualifier() ) : null;
      this.goal = contestActivity.getGoalAmount() != null ? String.valueOf( contestActivity.getGoalAmount() ) : null;
    }
    this.willEarn = contestActivity.getPayoutAmount() != null ? String.valueOf( contestActivity.getPayoutAmount() ) : null;
    this.payoutAmount = contestActivity.getPayoutAmount() != null ? String.valueOf( contestActivity.getPayoutAmount() ) : null;
    this.individualPayoutCap = contestActivity.getPayoutCapAmount() != null ? String.valueOf( contestActivity.getPayoutCapAmount() ) : null;
    this.maxPayout = contestActivity.getPayoutCapAmount() != null ? String.valueOf( participantsCount * contestActivity.getPayoutCapAmount() ) : null;
    // this.payoutDescription = contestActivity.getPayoutDescription();
    this.valueDescription = contestActivity.getPayoutDescription();
    this.maxPotential = SSIContestUtil.getFormattedValue( calculateMaxPotential( contestActivity, participantsCount ), precision );
  }

  public SSIContestActivityView( SSIContest contest, SSIContestValueBean valueBean, SSIContestActivity contestActivity )
  {
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    int participantsCount = valueBean.getParticipantCount();

    this.id = contestActivity.getId();
    this.description = contestActivity.getDescription();
    this.forEvery = contestActivity.getIncrementAmount() != null
        ? SSIContestUtil.getActivityPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestActivity.getIncrementAmount(), precision )
        : null;
    this.minQualifier = contestActivity.getMinQualifier() != null
        ? SSIContestUtil.getActivityPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestActivity.getMinQualifier(), precision )
        : null;
    this.maxPayout = contestActivity.getPayoutCapAmount() != null
        ? SSIContestUtil.getPayoutPrefix( valueBean ) + SSIContestUtil.getFormattedValue( participantsCount * contestActivity.getPayoutCapAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
        : null;
    Double maxPotential = calculateMaxPotential( contestActivity, participantsCount );
    if ( contest.getPayoutType().isOther() )
    {
      this.willEarn = contestActivity.getPayoutAmount() != null
          ? SSIContestUtil.getPayoutPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestActivity.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
          : null;
      this.payoutAmount = contestActivity.getPayoutAmount() != null
          ? SSIContestUtil.getPayoutPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestActivity.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
          : null;
      this.individualPayoutCap = contestActivity.getPayoutCapAmount() != null
          ? SSIContestUtil.getPayoutPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestActivity.getPayoutCapAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
          : null;
    }
    else if ( contest.getPayoutType().isPoints() )
    {
      this.willEarn = SSIContestUtil.getFormattedValue( contestActivity.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
      this.payoutAmount = SSIContestUtil.getFormattedValue( contestActivity.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
      this.individualPayoutCap = SSIContestUtil.getFormattedValue( contestActivity.getPayoutCapAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
    }
    this.maxPotential = maxPotential != null ? SSIContestUtil.getActivityPrefix( valueBean ) + SSIContestUtil.getFormattedValue( maxPotential, precision ) : null;
    this.goal = contestActivity.getGoalAmount() != null ? SSIContestUtil.getActivityPrefix( valueBean ) + SSIContestUtil.getFormattedValue( contestActivity.getGoalAmount(), precision ) : null;
    // this.payoutDescription = contestActivity.getPayoutDescription();
    this.valueDescription = contestActivity.getPayoutDescription();
  }

  private Double calculateMaxPotential( SSIContestActivity contestActivity, int participantsCount )
  {
    Long payoutCap = contestActivity.getPayoutCapAmount() != null ? contestActivity.getPayoutCapAmount() : 0L;
    Long earn = contestActivity.getPayoutAmount() != null ? contestActivity.getPayoutAmount() : 0L;
    Double forEach = contestActivity.getIncrementAmount() != null ? contestActivity.getIncrementAmount() : 0D;
    Double minQualifier = contestActivity.getMinQualifier() != null ? contestActivity.getMinQualifier() : 0D;
    if ( payoutCap == 0L || earn == 0L || participantsCount == 0 )
    {
      return null;
    }
    else
    {
      return ( payoutCap * forEach / earn + minQualifier ) * participantsCount;
    }
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getForEvery()
  {
    return forEvery;
  }

  public void setForEvery( String forEvery )
  {
    this.forEvery = forEvery;
  }

  public String getMinQualifier()
  {
    return minQualifier;
  }

  public void setMinQualifier( String minQualifier )
  {
    this.minQualifier = minQualifier;
  }

  public String getIndividualPayoutCap()
  {
    return individualPayoutCap;
  }

  public void setIndividualPayoutCap( String individualPayoutCap )
  {
    this.individualPayoutCap = individualPayoutCap;
  }

  public String getMaxPayout()
  {
    return maxPayout;
  }

  public void setMaxPayout( String maxPayout )
  {
    this.maxPayout = maxPayout;
  }

  public String getMaxPotential()
  {
    return maxPotential;
  }

  public void setMaxPotential( String maxPotential )
  {
    this.maxPotential = maxPotential;
  }

  public String getGoal()
  {
    return goal;
  }

  public void setGoal( String goal )
  {
    this.goal = goal;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public String getWillEarn()
  {
    return willEarn;
  }

  public void setWillEarn( String willEarn )
  {
    this.willEarn = willEarn;
  }

  public String getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( String payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getValueDescription()
  {
    return valueDescription;
  }

  public void setValueDescription( String valueDescription )
  {
    this.valueDescription = valueDescription;
  }

  @Override
  public int compareTo( SSIContestActivityView contestActivityView )
  {
    return this.id.compareTo( ( (SSIContestActivityView)contestActivityView ).getId() );
  }

}

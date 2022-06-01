
package com.biperf.core.ui.ssi.view;

import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;

public abstract class SSIContestPayoutsTotalView
{
  private String id;
  private String contestType;
  private String payoutType;
  private String name;
  private boolean hasQualifiedPayouts;
  private boolean includeSubmitClaim;

  public SSIContestPayoutsTotalView( SSIContest contest, SSIContestPayoutsValueBean ssiContestPayouts, String contestName, boolean hasQualifiedPayouts, boolean includeSubmitClaim )
  {
    this.id = contest.getId().toString();
    this.contestType = contest.getContestTypeName();
    this.payoutType = contest.getPayoutType().getCode();
    this.name = contestName;
    this.hasQualifiedPayouts = hasQualifiedPayouts;
    this.includeSubmitClaim = includeSubmitClaim;
  }

  public abstract void populateContestPayoutsTotal( SSIContestPayoutsValueBean ssiContestPayouts, String activityPrefix, String payoutPrefix, String payoutSuffix );

  public boolean isPayoutInPoints()
  {
    return SSIPayoutType.POINTS_CODE.equalsIgnoreCase( payoutType );
  }

  public boolean isPayoutInOther()
  {
    return SSIPayoutType.OTHER_CODE.equalsIgnoreCase( payoutType );
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public boolean isHasQualifiedPayouts()
  {
    return hasQualifiedPayouts;
  }

  public void setHasQualifiedPayouts( boolean hasQualifiedPayouts )
  {
    this.hasQualifiedPayouts = hasQualifiedPayouts;
  }

  public boolean isIncludeSubmitClaim()
  {
    return includeSubmitClaim;
  }

  public void setIncludeSubmitClaim( boolean includeSubmitClaim )
  {
    this.includeSubmitClaim = includeSubmitClaim;
  }

  /*
   * ublic String getDataURL() { return dataURL; } public void setDataURL( String dataURL ) {
   * this.dataURL = dataURL; }
   */

}

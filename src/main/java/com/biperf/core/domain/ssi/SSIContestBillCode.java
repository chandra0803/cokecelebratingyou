
package com.biperf.core.domain.ssi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.biperf.core.domain.promotion.BillCode;

public class SSIContestBillCode extends BillCode
{

  private static final long serialVersionUID = 1L;

  private SSIContest ssiContest;

  public SSIContest getSsiContest()
  {
    return ssiContest;
  }

  public void setSsiContest( SSIContest ssiContest )
  {
    this.ssiContest = ssiContest;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( getTrackBillCodeBy() ).append( getBillCode() ).append( getContestId() ).hashCode();
  }

  public Long getContestId()
  {
    return getSsiContest() != null ? getSsiContest().getId() : null;
  }

  @Override
  public boolean equals( Object obj )
  {
    SSIContestBillCode other = (SSIContestBillCode)obj;

    return new EqualsBuilder().append( this.getTrackBillCodeBy(), other.getTrackBillCodeBy() ).append( this.getBillCode(), other.getBillCode() ).append( this.getContestId(), other.getContestId() )
        .isEquals();
  }
}

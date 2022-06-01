/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.audit;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;

/**
 * ClaimBasedPayoutCalculationAudit.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>zahler</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimBasedPayoutCalculationAudit extends PayoutCalculationAudit
{
  /**
   * The claim submitted for payout.
   */
  private Claim claim;
  private ClaimGroup claimGroup;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ClaimBasedPayoutCalculationAudit ) )
    {
      return false;
    }

    final ClaimBasedPayoutCalculationAudit that = (ClaimBasedPayoutCalculationAudit)object;

    if ( this.getParticipant() != null ? !this.getParticipant().equals( that.getParticipant() ) : that.getParticipant() != null )
    {
      return false;
    }

    if ( this.getClaim() != null ? !this.getClaim().equals( that.getClaim() ) : that.getClaim() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;

    result = this.getParticipant() != null ? this.getParticipant().hashCode() * 13 : 0;
    result += this.getClaim() != null ? this.getClaim().hashCode() : 0;
    return result;
  }

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public ClaimGroup getClaimGroup()
  {
    return claimGroup;
  }

  public void setClaimGroup( ClaimGroup claimGroup )
  {
    this.claimGroup = claimGroup;
  }

}

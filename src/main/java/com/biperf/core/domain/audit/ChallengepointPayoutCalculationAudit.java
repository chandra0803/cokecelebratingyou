/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/audit/ChallengepointPayoutCalculationAudit.java,v $
 */

package com.biperf.core.domain.audit;

/**
 * ChallengepointPayoutCalculationAudit.
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
 * <td>Babu</td>
 * <td>08/11/08</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChallengepointPayoutCalculationAudit extends PayoutCalculationAudit
{

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

    if ( ! ( object instanceof ChallengepointPayoutCalculationAudit ) )
    {
      return false;
    }

    final ChallengepointPayoutCalculationAudit that = (ChallengepointPayoutCalculationAudit)object;

    if ( this.getParticipant() != null ? !this.getParticipant().equals( that.getParticipant() ) : that.getParticipant() != null )
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
    return result;
  }

}

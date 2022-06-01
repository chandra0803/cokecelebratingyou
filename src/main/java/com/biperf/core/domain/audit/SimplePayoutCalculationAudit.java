/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/audit/SimplePayoutCalculationAudit.java,v $
 */

package com.biperf.core.domain.audit;

/*
 * SimplePayoutCalculationAudit <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug
 * 26, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class SimplePayoutCalculationAudit extends PayoutCalculationAudit
{

  /**
   * Overridden from
   * 
   * @param object
   * @return boolean
   * @see Object#equals(Object)
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SimplePayoutCalculationAudit ) )
    {
      return false;
    }

    final SimplePayoutCalculationAudit that = (SimplePayoutCalculationAudit)object;

    if ( this.getJournal() != null ? !this.getJournal().equals( that.getJournal() ) : that.getJournal() != null )
    {
      return false;
    }

    if ( this.getReasonType() != null ? !this.getReasonType().equals( that.getReasonType() ) : that.getReasonType() != null )
    {
      return false;
    }

    if ( this.getReasonText() != null ? !this.getReasonText().equals( that.getReasonText() ) : that.getReasonText() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @return int
   * @see Object#hashCode()
   */
  public int hashCode()
  {
    int result;

    result = this.getJournal() != null ? this.getJournal().hashCode() * 13 : 0;
    result += this.getReasonType() != null ? this.getReasonType().hashCode() : 0;
    result += this.getReasonText() != null ? this.getReasonText().hashCode() : 0;

    return result;
  }
}

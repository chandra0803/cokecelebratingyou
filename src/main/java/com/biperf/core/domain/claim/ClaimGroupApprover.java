/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

/**
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
 * <td>wadzinsk</td>
 * <td>Oct 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimGroupApprover extends ApprovableItemApprover
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param o
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof ClaimGroupApprover ) )
    {
      return false;
    }

    final ClaimGroupApprover rhs = (ClaimGroupApprover)o;

    if ( getApprovalRound() != null ? !getApprovalRound().equals( rhs.getApprovalRound() ) : rhs.getApprovalRound() != null )
    {
      return false;
    }
    if ( getClaimGroup() != null ? !getClaimGroup().equals( rhs.getClaimGroup() ) : rhs.getClaimGroup() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    int result;
    result = getApprovalRound() != null ? getApprovalRound().hashCode() : 0;
    result = 31 * result + ( getClaimGroup() != null ? getClaimGroup().hashCode() : 0 );

    return result;
  }

  /**
   * used only by hibernate
   */
  public ClaimGroup getClaimGroup()
  {
    return (ClaimGroup)getApprovableItem();
  }

  /**
   * Used only by hibernate
   */
  public void setClaimGroup( ClaimGroup ClaimGroup )
  {
    setApprovableItem( ClaimGroup );
  }

}

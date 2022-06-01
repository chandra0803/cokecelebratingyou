/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryValueObject.java,v $
 */

package com.biperf.core.ui.claim;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.user.User;
/*
 * TransactionHistoryValueObject
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
 * <td>Thomas Eaton</td>
 * <td>May 23, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class TransactionHistoryValueObject
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The claim associated with this value object.
   */
  private Claim claim;

  /**
   * The earnings for the claim associated with this value object and the
   * participant whose transaction history the administrator is viewing.
   */
  private Long earnings;

  /**
   * The award type. e.g. points. Used when displaying recognitions received.
   */
  private String awardTypeName;

  /**
   * True if this value object represents a claim against a product claim
   * promotion whose payout type is "stack rank;" false otherwise.
   */
  private boolean stackRankClaim;

  /**
   * True if this value object represents a non-claim award that is a result
   * of a Discretionary Award; false otherwise.
   */
  private boolean isDiscretionary;

  private boolean isSweepstakes;

  private boolean isManagerOverride;

  private boolean isStackRank;

  private String reversalDescription = null;

  /**
   * Contains createdBy information.
   */
  private User createdBy;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public Long getEarnings()
  {
    return earnings;
  }

  public void setEarnings( Long earnings )
  {
    this.earnings = earnings;
  }

  public boolean isStackRankClaim()
  {
    return stackRankClaim;
  }

  public void setStackRankClaim( boolean stackRankClaim )
  {
    this.stackRankClaim = stackRankClaim;
  }

  public boolean isDiscretionary()
  {
    return isDiscretionary;
  }

  public void setDiscretionary( boolean isDiscretionary )
  {
    this.isDiscretionary = isDiscretionary;
  }

  public boolean isSweepstakes()
  {
    return isSweepstakes;
  }

  public void setSweepstakes( boolean isSweepstakes )
  {
    this.isSweepstakes = isSweepstakes;
  }

  public boolean isManagerOverride()
  {
    return isManagerOverride;
  }

  public void setManagerOverride( boolean isManagerOverride )
  {
    this.isManagerOverride = isManagerOverride;
  }

  public boolean isStackRank()
  {
    return isStackRank;
  }

  public void setStackRank( boolean isStackRank )
  {
    this.isStackRank = isStackRank;
  }

  public String getAwardTypeName()
  {
    return awardTypeName;
  }

  public void setAwardTypeName( String awardTypeName )
  {
    this.awardTypeName = awardTypeName;
  }

  public User getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( User createdBy )
  {
    this.createdBy = createdBy;
  }

  public String getReversalDescription()
  {
    return reversalDescription;
  }

  public void setReversalDescription( String reversalDescription )
  {
    this.reversalDescription = reversalDescription;
  }
}

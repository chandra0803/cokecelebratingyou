/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.activity;

import java.math.BigDecimal;

import com.biperf.core.domain.claim.ClaimGroup;

public class NominationActivity extends AbstractRecognitionActivity
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * This activity is associated with either a claim or a claim group.  This field
   * is null when the activity is associated with a claim.
   */
  private ClaimGroup claimGroup;
  private BigDecimal cashAwardQuantity;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  public NominationActivity()
  {
    // default
  }

  public NominationActivity( String guid )
  {
    super( guid );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public ClaimGroup getClaimGroup()
  {
    return claimGroup;
  }

  public void setClaimGroup( ClaimGroup claimGroup )
  {
    this.claimGroup = claimGroup;
  }

  public BigDecimal getCashAwardQuantity()
  {
    return cashAwardQuantity;
  }

  public void setCashAwardQuantity( BigDecimal cashAwardQuantity )
  {
    this.cashAwardQuantity = cashAwardQuantity;
  }

}

/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/SweepstakesClaimEligibilityType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/*
 * SweepstakesClaimEligibilityType <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Feb
 * 3, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class SweepstakesClaimEligibilityType extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset codes
   */
  public static final String ALL_CLOSED_CLAIMS = "all_closed_claims";
  public static final String ALL_CLOSED_CLAIMS_WITH_ONE_OR_MORE_ITEMS_APPROVED = "all_closed_claims_with_one_or_more_items_approved";
  public static final String ALL_CLOSED_CLAIMS_WITH_ALL_ITEMS_APPROVED = "all_closed_claims_with_all_items_approved";

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.sweepstakes.claimEligibilityType";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all claim eligibility types for promotion sweepstakes.
   * 
   * @return a list of all claim eligibility types for promotion sweepstakes, as a <code>List</code>
   *         of <code>SweepstakesClaimEligibilityType</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SweepstakesClaimEligibilityType.class );
  }

  /**
   * Returns the specified <code>SweepstakesClaimEligibilityType</code> object; returns null if
   * the given code is null or invalid.
   * 
   * @param code a promotion sweepstakes claim eligibility type code.
   * @return the specified <code>SweepstakesClaimEligibilityType</code> object, or returns null if
   *         the given code is null or invalid.
   */
  public static SweepstakesClaimEligibilityType lookup( String code )
  {
    return (SweepstakesClaimEligibilityType)getPickListFactory().getPickListItem( SweepstakesClaimEligibilityType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the PickListFactory class should create
   * instances of this class.
   */
  /* package */ SweepstakesClaimEligibilityType()
  {
    // empty constructor
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public boolean isAllClosedClaims()
  {
    return this.getCode().equals( ALL_CLOSED_CLAIMS );
  }

  public boolean isAllClosedClaimsAndAtleastOneApproved()
  {
    return this.getCode().equals( ALL_CLOSED_CLAIMS_WITH_ONE_OR_MORE_ITEMS_APPROVED );
  }

  public boolean isAllClosedClaimsAndAllApproved()
  {
    return this.getCode().equals( ALL_CLOSED_CLAIMS_WITH_ALL_ITEMS_APPROVED );
  }

}

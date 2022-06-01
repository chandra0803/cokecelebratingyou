/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/activity/ManagerOverrideActivity.java,v $
 */

package com.biperf.core.domain.activity;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.utils.GuidUtils;

/*
 * ManagerOverrideActivity <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug
 * 16, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public class ManagerOverrideActivity extends Activity
{
  private Long submitterPayout;
  private MinimumQualifierStatus minimumQualifierStatus;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------
  public ManagerOverrideActivity()
  {
    // empty constructor
  }

  public ManagerOverrideActivity( String guid )
  {
    super( guid );
  }

  /**
   * @param claim
   * @param submitterPayout
   */
  public ManagerOverrideActivity( Claim claim, Long submitterPayout )
  {
    super( GuidUtils.generateGuid() );

    this.claim = claim;
    this.submitterPayout = submitterPayout;
  }

  /**
   * Construct an activity copying properties of the input sourceManagerOverrideActivity, creating a
   * transient ManagerOverrideActivity with a new guid.
   * 
   * @param sourceManagerOverrideActivity
   */
  public ManagerOverrideActivity( ManagerOverrideActivity sourceManagerOverrideActivity )
  {
    super( GuidUtils.generateGuid() );

    claim = sourceManagerOverrideActivity.getClaim();
    isPosted = sourceManagerOverrideActivity.isPosted();
    node = sourceManagerOverrideActivity.getNode();
    participant = sourceManagerOverrideActivity.getParticipant();
    promotion = sourceManagerOverrideActivity.getPromotion();
    submitterPayout = sourceManagerOverrideActivity.getSubmitterPayout();
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getSubmitterPayout()
  {
    return submitterPayout;
  }

  public void setSubmitterPayout( Long submitterPayout )
  {
    this.submitterPayout = submitterPayout;
  }

  /**
   * @return value of minimumQualifierStatus property
   */
  public MinimumQualifierStatus getMinimumQualifierStatus()
  {
    return minimumQualifierStatus;
  }

  /**
   * @param minimumQualifierStatus value for minimumQualifierStatus property
   */
  public void setMinimumQualifierStatus( MinimumQualifierStatus minimumQualifierStatus )
  {
    this.minimumQualifierStatus = minimumQualifierStatus;
  }
}

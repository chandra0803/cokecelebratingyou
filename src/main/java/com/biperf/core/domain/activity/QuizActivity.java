/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/activity/QuizActivity.java,v $
 */

package com.biperf.core.domain.activity;

import com.biperf.core.utils.GuidUtils;

/**
 * SalesActivity.
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
 * <td>OPI Admin</td>
 * <td>Jul 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class QuizActivity extends Activity
{

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------
  public QuizActivity()
  {
    // empty constructor
  }

  public QuizActivity( String guid )
  {
    super( guid );
  }

  /**
   * Construct an activity copying properties of the input sourceActivity, creating a transient
   * SalesActivity with a new guid.
   * 
   * @param sourceActivity
   */
  public QuizActivity( QuizActivity sourceActivity )
  {
    super( GuidUtils.generateGuid() );

    claim = sourceActivity.getClaim();
    isPosted = sourceActivity.isPosted();
    node = sourceActivity.getNode();
    participant = sourceActivity.getParticipant();
    promotion = sourceActivity.getPromotion();
  }
}

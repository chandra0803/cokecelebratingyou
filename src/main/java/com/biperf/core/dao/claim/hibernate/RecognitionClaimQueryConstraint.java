/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim.hibernate;

import com.biperf.core.domain.claim.RecognitionClaim;

/**
 * RecognitionClaimQueryConstraint.
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
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class RecognitionClaimQueryConstraint extends AbstractRecognitionClaimQueryConstraint
{
  public Class getResultClass()
  {
    return RecognitionClaim.class;
  }
}

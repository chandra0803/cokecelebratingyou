/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/ClaimElementValidator.java,v $
 */

package com.biperf.core.utils;

import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.promotion.Promotion;

/*
 * ClaimElementValidator <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 26, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public interface ClaimElementValidator
{
  /**
   * Returns true if the value of the given claim element is valid, returns false otherwise.
   * 
   * @param claimElement the claim element whose value will be validated.
   * @param promotion the promotion associated with the given claim element
   * @return true if the given claim element's value is valid, false otherwise.
   */
  boolean isValid( ClaimElement claimElement, Promotion promotion );
}

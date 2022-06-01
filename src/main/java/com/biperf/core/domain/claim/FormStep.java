/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/claim/FormStep.java,v $
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;

/**
 * FormStep.
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
 * <td>crosenquest</td>
 * <td>Oct 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class FormStep extends BaseDomain
{

  protected Form form;
  protected String cmKeyFragment;

  public String getCmKeyFragment()
  {
    return cmKeyFragment;
  }

  public void setCmKeyFragment( String cmKeyFragment )
  {
    this.cmKeyFragment = cmKeyFragment;
  }

  public int hashCode()
  {
    int result = 0;

    result += this.getCmKeyFragment() != null ? this.getCmKeyFragment().hashCode() : 0;

    return result;
  }

}

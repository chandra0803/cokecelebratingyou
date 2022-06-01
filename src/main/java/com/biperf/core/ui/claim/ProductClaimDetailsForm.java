/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/ProductClaimDetailsForm.java,v $
 */

package com.biperf.core.ui.claim;

import com.biperf.core.ui.BaseForm;

/**
 * ProductClaimDetailsForm.
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
 * <td>zahler</td>
 * <td>Jan 3, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductClaimDetailsForm extends BaseForm
{
  private String id;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }
}

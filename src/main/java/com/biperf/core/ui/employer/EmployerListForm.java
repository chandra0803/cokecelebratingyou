/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/employer/EmployerListForm.java,v $
 */

package com.biperf.core.ui.employer;

import com.biperf.core.ui.BaseForm;

/**
 * EmployerListForm.
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
 * <td>robinsra</td>
 * <td>May 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmployerListForm extends BaseForm
{
  private String method;
  private String employerId;

  public String getEmployerId()
  {
    return employerId;
  }

  public void setEmployerId( String employerId )
  {
    this.employerId = employerId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

} // end EmployerListForm

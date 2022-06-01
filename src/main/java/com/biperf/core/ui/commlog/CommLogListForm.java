/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/commlog/CommLogListForm.java,v $
 */

package com.biperf.core.ui.commlog;

import com.biperf.core.ui.BaseForm;

/**
 * CommLogListForm.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author </th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Ashok Attada</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogListForm extends BaseForm
{
  private String status;
  private String source;
  private String userId;
  private String category;
  private String reason;
  private String assignedTo;

  public String getCategory()
  {
    return category;
  }

  public void setCategory( String category )
  {
    this.category = category;
  }

  public String getReason()
  {
    return reason;
  }

  public void setReason( String reason )
  {
    this.reason = reason;
  }

  public String getSource()
  {
    return source;
  }

  public void setSource( String source )
  {
    this.source = source;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getAssignedTo()
  {
    return assignedTo;
  }

  public void setAssignedTo( String assignedTo )
  {
    this.assignedTo = assignedTo;
  }

}

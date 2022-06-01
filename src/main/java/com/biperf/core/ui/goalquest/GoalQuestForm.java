/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/goalquest/GoalQuestForm.java,v $
 */

package com.biperf.core.ui.goalquest;

import com.biperf.core.ui.BaseForm;

/**
 * GoalQuestForm.
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
 * <td>Sep 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestForm extends BaseForm
{
  private String actionURL = "";
  private String programCode = "";
  private String userId = "";
  private String method = "";

  public String getActionURL()
  {
    return actionURL;
  }

  public void setActionURL( String actionURL )
  {
    this.actionURL = actionURL;
  }

  public String getProgramCode()
  {
    return programCode;
  }

  public void setProgramCode( String programCode )
  {
    this.programCode = programCode;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}

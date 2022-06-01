/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserPhoneListForm.java,v $
 */

package com.biperf.core.ui.user;

import com.biperf.core.ui.BaseForm;

/**
 * UserPhoneListForm.
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
 * <td>Apr 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserPhoneListForm extends BaseForm
{
  private String primary;
  private String[] delete;
  private String userId;
  private String method;
  private String phoneType;
  private boolean fromPaxScreen;

  public boolean isFromPaxScreen()
  {
    return fromPaxScreen;
  }

  public void setFromPaxScreen( boolean fromPaxScreen )
  {
    this.fromPaxScreen = fromPaxScreen;
  }

  public String[] getDelete()
  {
    return delete;
  }

  public void setDelete( String[] delete )
  {
    this.delete = delete;
  }

  public String getPrimary()
  {
    return primary;
  }

  public void setPrimary( String primary )
  {
    this.primary = primary;
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

  public String getPhoneType()
  {
    return phoneType;
  }

  public void setPhoneType( String selectedPhoneType )
  {
    this.phoneType = selectedPhoneType;
  }

}

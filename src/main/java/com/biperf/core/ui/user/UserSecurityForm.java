/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserSecurityForm.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Set;

import com.biperf.core.ui.BaseActionForm;

/**
 * UserSecurityForm.
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
 * <td>Sep 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserSecurityForm extends BaseActionForm
{

  public String userId;
  public String[] removeUserAcls;
  private String method;
  private int userAclListSize;
  private Set userAcls;

  public String[] getRemoveUserAcls()
  {
    return this.removeUserAcls;
  }

  public void setRemoveUserAcls( String[] removeUserAcls )
  {
    this.removeUserAcls = removeUserAcls;
  }

  public String getUserId()
  {
    return this.userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMethod()
  {
    return this.method;
  }

  public void setUserAclListSize( int userAclListSize )
  {
    this.userAclListSize = userAclListSize;
  }

  public int getUserAclListSize()
  {
    return this.userAclListSize;
  }

  public void setUserAclList( Set userAcls )
  {
    this.userAcls = userAcls;
  }

  public Set getUserAclList()
  {
    return this.userAcls;
  }

}

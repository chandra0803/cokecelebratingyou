/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/login/ForgotPasswordForm.java,v $
 */

package com.biperf.core.ui.login;

import com.biperf.core.ui.BaseForm;

/**
 * SeamlessLogonForm
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
 * <td>arasi</td>
 * <td>Mar 19, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SeamlessLogonForm extends BaseForm
{
  private String actionURL = "";
  private String method = "";

  private String uniqueId;
  private String timeStamp;
  private String hashString;
  private String classObjName;
  private String jusernameEncrypted;

  public String getActionURL()
  {
    return actionURL;
  }

  public void setActionURL( String actionURL )
  {
    this.actionURL = actionURL;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getUniqueId()
  {
    return uniqueId;
  }

  public void setUniqueId( String uniqueId )
  {
    this.uniqueId = uniqueId;
  }

  public String getTimeStamp()
  {
    return timeStamp;
  }

  public void setTimeStamp( String timeStamp )
  {
    this.timeStamp = timeStamp;
  }

  public String getHashString()
  {
    return hashString;
  }

  public void setHashString( String hashString )
  {
    this.hashString = hashString;
  }

  public String getClassObjName()
  {
    return classObjName;
  }

  public void setClassObjName( String classObjName )
  {
    this.classObjName = classObjName;
  }

  public String getJusernameEncrypted()
  {
    return jusernameEncrypted;
  }

  public void setJusernameEncrypted( String jusernameEncrypted )
  {
    this.jusernameEncrypted = jusernameEncrypted;
  }

}

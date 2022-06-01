/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/login/ForgotPasswordForm.java,v $
 */

package com.biperf.core.ui.login;

import com.biperf.core.ui.BaseActionForm;

/**
 * Used for the ForgotPassword screens.
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
 * <td>Apr 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ForgotPasswordForm extends BaseActionForm
{
  private String newPassword = "";
  private String confirmNewPassword = "";

  private String userName = "";
  private String secretQuestion = "";
  private String secretAnswer = "";

  public String getConfirmNewPassword()
  {
    return confirmNewPassword;
  }

  public void setConfirmNewPassword( String confirmNewPassword )
  {
    this.confirmNewPassword = confirmNewPassword;
  }

  public String getNewPassword()
  {
    return newPassword;
  }

  public void setNewPassword( String newPassword )
  {
    this.newPassword = newPassword;
  }

  public String getSecretAnswer()
  {
    return secretAnswer;
  }

  public void setSecretAnswer( String secretAnswer )
  {
    this.secretAnswer = secretAnswer;
  }

  public String getSecretQuestion()
  {
    return secretQuestion;
  }

  public void setSecretQuestion( String secretQuestion )
  {
    this.secretQuestion = secretQuestion;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

} // end ForgotPasswordForm

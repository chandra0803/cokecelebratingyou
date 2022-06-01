/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/profile/ChangePasswordForm.java,v $
 */

package com.biperf.core.ui.profile;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.StringUtil;

/**
 * Used for the ChangePassword screen.
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
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ChangePasswordForm extends BaseForm
{
  private String newPassword = "";
  private String confirmNewPassword = "";

  private String secretQuestion;
  private String secretQuestionDesc;
  private String secretAnswer;

  private String oldPassword = "";
  private String emailAddress;
  private String phoneNumber;
  private String countryPhoneCode;
  private boolean displayOldPassword = false;
  
  private boolean isEmailVerified;
  private boolean isPhoneVerified;

  public boolean isDisplayOldPassword()
  {
    return displayOldPassword;
  }

  public void setDisplayOldPassword( boolean displayOldPassword )
  {
    this.displayOldPassword = displayOldPassword;
  }

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

  /**
   * We are validating to make sure the two password entries typed by the user match Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return errors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    // TODO have to change the error mesgs keys.
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( StringUtil.isEmpty( this.newPassword ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "pwd.req" ) );
    }

    return actionErrors;
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

  public String getSecretQuestionDesc()
  {
    return secretQuestionDesc;
  }

  public void setSecretQuestionDesc( String secretQuestionDesc )
  {
    this.secretQuestionDesc = secretQuestionDesc;
  }

  public String getOldPassword()
  {
    return oldPassword;
  }

  public void setOldPassword( String oldPassword )
  {
    this.oldPassword = oldPassword;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

  public boolean isEmailVerified()
  {
    return isEmailVerified;
  }

  public void setEmailVerified( boolean isEmailVerified )
  {
    this.isEmailVerified = isEmailVerified;
  }

  public boolean isPhoneVerified()
  {
    return isPhoneVerified;
  }

  public void setPhoneVerified( boolean isPhoneVerified )
  {
    this.isPhoneVerified = isPhoneVerified;
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/help/ContactUsForm.java,v $
 */

package com.biperf.core.ui.help;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Used for the contact us page.
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
public class ContactUsForm extends BaseForm
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 3257570624384546869L;
  private String emailAddress = "";
  private String subject = "";
  private String comments = "";
  private String showMsg = "";
  private String lastName = "";
  private String firstName = "";
  private String returnUrl;
  private Long countryId;

  /**
   * @return comments
   */
  public String getComments()
  {
    return comments;
  }

  /**
   * @param comments
   */
  public void setComments( String comments )
  {
    this.comments = comments;
  }

  /**
   * @return email address
   */
  public String getEmailAddress()
  {
    return emailAddress;
  }

  /**
   * @param emailAddress
   */
  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  /**
   * @return subject of email
   */
  public String getSubject()
  {
    return subject;
  }

  /**
   * @param subject
   */
  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  /**
   * determines which message to show on the screen as this screen is used in multiple places
   * 
   * @return String
   */
  public String getShowMsg()
  {
    return showMsg;
  }

  /**
   * @param showMsg
   */
  public void setShowMsg( String showMsg )
  {
    this.showMsg = showMsg;
  }

  /**
   * @return first name
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * @param firstName
   */
  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  /**
   * @return the last name
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * @param lastName
   */
  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getReturnUrl()
  {
    return returnUrl;
  }

  public void setReturnUrl( String returnUrl )
  {
    this.returnUrl = returnUrl;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( this.getCountryId() == null || this.getCountryId().longValue() <= 0 )
    {
      actionErrors.add( "countryId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "help.contact.us", "COUNTRY" ) ) );
    }

    return actionErrors;
  }

}

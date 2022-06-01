/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/help/ContactUsService.java,v $
 */

package com.biperf.core.service.help;

import com.biperf.core.service.SAO;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * Service for the contactUs Functionality.
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
 * <td>Apr 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ContactUsService extends SAO
{

  /**
   * This is the key that will be used to look up in the system variables the email address which
   * the contact us screen will use as a destination address
   */
  public static final String CONTACT_US_EMAIL_ADDRESS_KEY = SystemVariableService.CONTACT_US_EMAIL_ADDRESS_KEY;

  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "contactUsService";

  /**
   * Sends an e-mail to a system-defined email address This method will validate the
   * fromEmailAddress to make sure it is a correctly formed address Overridden from
   * 
   * @param fromEmailAddress
   * @param subject TODO
   * @param text
   * @param firstName
   * @param lastName
   */
  void submitComments( String fromEmailAddress, String subject, String text, String firstName, String lastName, String countryName );

  void submitComments( String fromEmailAddress, String subject, String text, String firstName, String lastName, Long countryId );

  /**
   * Set the EmailService.
   * 
   * @param emailService
   */
  public void setEmailService( EmailService emailService );

  /**
   * Set the SystemVariableService.
   * 
   * @param systemVariableService
   */
  public void setSystemVariableService( SystemVariableService systemVariableService );

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/email/EmailService.java,v $
 */

package com.biperf.core.service.email;

import java.util.Set;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionNoRollback;
import com.biperf.core.service.SAO;

/**
 * EmailService.
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
 * <td>sharma</td>
 * <td>Apr 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface EmailService extends SAO
{
  public static final String BEAN_NAME = "emailService";

  /**
   * Send an Email using supplied header and body.
   * 
   * @param header
   * @param textBody
   * @throws ServiceErrorException
   */
  public abstract void sendMessage( EmailHeader header, EmailBody textBody ) throws ServiceErrorException;

  /**
   * Send an Email using supplied header and body.
   * 
   * @param header
   * @param textBody
   * @param htmlBody
   * @throws ServiceErrorExceptionNoRollback
   */
  public abstract void sendMessage( EmailHeader header, EmailBody textBody, EmailBody htmlBody ) throws ServiceErrorExceptionNoRollback;

  /**
   * Send an Email using supplied header and body and attachment.
   * 
   * @param header
   * @param textBody
   * @param htmlBody
   * @param mailingAttachmentInfos
   * @throws ServiceErrorExceptionNoRollback
   */
  public abstract void sendMessage( EmailHeader header, EmailBody textBody, EmailBody htmlBody, Set mailingAttachmentInfos ) throws ServiceErrorExceptionNoRollback;

  public void sendStageFailNotificationMessage();

  // /**
  // * Send an Email using supplied header and body with template variables substitution in th body
  // * text.
  // *
  // * @param header
  // * @param body
  // * @param substiutions
  // * @throws ServiceErrorException
  // */
  // public abstract void sendMessage( EmailHeader header, EmailBody body, Map substiutions ) throws
  // ServiceErrorException;
  //
  /**
   * @param mailSender value for mailSender property
   */

  public EmailCredentials getSMTPEmailCredentials();
}

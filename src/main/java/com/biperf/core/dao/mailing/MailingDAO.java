/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/mailing/MailingDAO.java,v $
 */

package com.biperf.core.dao.mailing;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.service.SAO;

/**
 * MailingDAO.
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
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface MailingDAO extends SAO
{
  public static final String BEAN_NAME = "mailingDAO";

  /**
   * Save the Mailing.
   * 
   * @param mailing
   * @return Mailing
   */
  public Mailing saveMailing( Mailing mailing );

  /**
   * Get all mailing attachment info for mailings that have been sent
   * in which contains fullFileName which can be used to delete temp files
   * previously written on the app server
   * 
   * @return List of MailingAttachmentInfo
   */
  public List getAllMailingAttachmentInfoAlreadySent();

  /**
   * Get the Mailing by id
   * 
   * @param id
   * @return Mailing
   */
  public Mailing getMailingById( Long id );

  /**
   * @param messageId
   * @return List of Users
   */
  public List getUsersWhoReceivedMessage( Long messageId );

  /**
   * @param mailingId
   * @return List of Users
   */
  public List getUsersWhoSuccessfullyReceivedMessage( Long mailingId );

  /**
   * @param mailingId
   * @return user Id who launched the process
   */
  public Long getRunAsUserIdByMailingId( Long mailingId );

  public List getFailedMailingIds( Date startDate, Date endDate );
}

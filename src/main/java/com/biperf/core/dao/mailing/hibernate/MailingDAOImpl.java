/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/mailing/hibernate/MailingDAOImpl.java,v $
 */

package com.biperf.core.dao.mailing.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.mailing.MailingDAO;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * MailingDAOImpl.
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
public class MailingDAOImpl extends BaseDAO implements MailingDAO
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingDAO#getMailingById(java.lang.Long)
   * @param id
   * @return Mailing
   */
  public Mailing getMailingById( Long id )
  {
    return (Mailing)getSession().get( Mailing.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingDAO#saveMailing(com.biperf.core.domain.mailing.Mailing)
   * @param mailing
   * @return Mailing
   */
  public Mailing saveMailing( Mailing mailing )
  {
    return (Mailing)HibernateUtil.saveOrUpdateOrShallowMerge( mailing );
  }

  /**
   * Get all mailing attachment info for mailings that have been sent
   * in which contains fullFileName which can be used to delete temp files
   * previously written on the app server
   * 
   * @return List of MailingAttachmentInfo
   */
  public List getAllMailingAttachmentInfoAlreadySent()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.mailing.GetAllMailingAttachmentInfoSent" );

    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingDAO#getUsersWhoReceivedMessage(java.lang.Long)
   * @param messageId
   * @return List of User who have received the message
   */
  public List getUsersWhoReceivedMessage( Long messageId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.mailing.GetAllUsersWhoReceivedMessage" );
    query.setLong( "messageId", messageId.longValue() );

    return query.list();
  }

  /**
   * @param mailingId
   * @return List of User who have successfully received the message (dateSent not null on the
   *         Mailing Recipient)
   */
  public List getUsersWhoSuccessfullyReceivedMessage( Long mailingId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.mailing.GetUsersWhoSuccessfullyReceivedMessage" );
    query.setLong( "mailingId", mailingId.longValue() );

    return query.list();
  }

  /**
   * THIS IS USED BY the Welcome Email Process special handling - DO NOT USE Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingDAO#getRunAsUserIdByMailingId(java.lang.Long)
   * @param mailingId
   * @return Long the user ID of the Admin who launched the Welcome Email Process
   */
  public Long getRunAsUserIdByMailingId( Long mailingId )
  {
    Long result = null;
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.mailing.GetRunAsUserIdByMailingId" );
    query.setString( "mailingId", mailingId.toString() );

    // There may be multiple just take the first one
    List results = query.list();
    if ( results.size() > 0 )
    {
      result = new Long( results.get( 0 ).toString() );
    }
    return result;
  }

  public List getFailedMailingIds( Date startDate, Date endDate )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.mailing.MailingRecipient.GetFailedMailingIds" );
    query.setDate( "startDate", startDate );
    query.setDate( "endDate", endDate );
    return query.list();
  }
}

/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/commlog/CommLogAssociationRequest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.commlog;

import java.util.Iterator;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.commlog.CommLogComment;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * CommentsToCommLogAssociationRequest.
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
 * <td>jenniget</td>
 * <td>May 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int COMMENTS = 2;
  public static final int USER = 3;
  public static final int MAILING = 4;
  public static final int ASSIGNED_TO_USER = 5;
  public static final int ASSIGNED_BY_USER = 6;
  public static final int MAILING_WITH_RECIPIENTS_AND_LOCALES = 7;

  public static final int COMMENTS_USER = 10;

  public CommLogAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    CommLog log = (CommLog)domainObject;
    switch ( hydrateLevel )
    {
      case ALL:
      {
        initialize( log.getComments() ); // hydrate the association to Comments
        for ( Iterator iter = log.getComments().iterator(); iter.hasNext(); )
        {
          CommLogComment comment = (CommLogComment)iter.next();
          initialize( comment.getCommentUser() ); // hydrate the association to CommentUser
        }
        initialize( log.getUser() ); // hydrate the association to User
        if ( log.getUser() != null )
        {
          initialize( log.getUser().getUserEmailAddresses() );// hydrate the association to
        }
        // UserEmailAddresses
        initialize( log.getAssignedByUser() ); // hydrate the association to AssignedByUser
        initialize( log.getAssignedToUser() ); // hydrate the association to AssignedToUser

        break;
      }

      case COMMENTS:
      {
        initialize( log.getComments() ); // hydrate the association to Comments
        break;
      }
      case USER:
      {
        initialize( log.getUser() ); // hydrate the association to User
        break;
      }
      case ASSIGNED_BY_USER:
      {
        initialize( log.getAssignedByUser() ); // hydrate the association to AssignedByUser
        break;
      }
      case ASSIGNED_TO_USER:
      {
        initialize( log.getAssignedToUser() ); // hydrate the association to AssignedToUser
        break;
      }
      case MAILING:
      {
        initialize( log.getMailing() ); // hydrate the association to Mailing
        break;
      }

      case MAILING_WITH_RECIPIENTS_AND_LOCALES:
      {
        initialize( log.getMailing() ); // hydrate the association to Mailing
        initialize( log.getMailing().getMessage() );
        for ( Iterator iter = log.getMailing().getMailingRecipients().iterator(); iter.hasNext(); )
        {
          MailingRecipient mailingRecipient = (MailingRecipient)iter.next();
          initialize( mailingRecipient );
          for ( Iterator iter2 = mailingRecipient.getMailingRecipientDataSet().iterator(); iter2.hasNext(); )
          {
            MailingRecipientData mailingRecipientData = (MailingRecipientData)iter2.next();
            initialize( mailingRecipientData );
          }
          initialize( mailingRecipient.getMailingMessageLocale() );
        }

        break;
      }

      case COMMENTS_USER:
      {
        initialize( log.getComments() ); // hydrate the association to Comments
        for ( Iterator iter = log.getComments().iterator(); iter.hasNext(); )
        {
          CommLogComment comment = (CommLogComment)iter.next();
          initialize( comment.getCommentUser() ); // hydrate the association to CommentUser
        }
        break;
      }
      default:
      {
        break;
      }
    } // switch
  }
}

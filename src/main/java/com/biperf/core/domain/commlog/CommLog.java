/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/commlog/CommLog.java,v $
 *
 */

package com.biperf.core.domain.commlog;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.CommLogCategoryType;
import com.biperf.core.domain.enums.CommLogReasonType;
import com.biperf.core.domain.enums.CommLogSourceType;
import com.biperf.core.domain.enums.CommLogStatusType;
import com.biperf.core.domain.enums.CommLogUrgencyType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.GuidUtils;

/**
 * CommLog <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CommLog extends BaseDomain
{
  public static String MESSAGE_TYPE_EMAIL = "email";
  public static String MESSAGE_TYPE_SMS = "sms";

  private String guid;
  private String message;
  private String subject;
  private Timestamp dateInitiated;
  private Timestamp dateEscalated;
  private CommLogStatusType commLogStatusType;
  private CommLogSourceType commLogSourceType;
  private CommLogCategoryType commLogCategoryType;
  private CommLogReasonType commLogReasonType;
  private CommLogUrgencyType commLogUrgencyType;
  private User assignedToUser;
  private User assignedByUser;
  private User user;
  private Mailing mailing;
  private Set comments = new HashSet();
  private String plainMessage;
  private String messageType;

  public CommLog()
  {
    super();
    defaultGuidAndDateInitiated();
  }

  public CommLog( Long id )
  {
    super( id );
    defaultGuidAndDateInitiated();
  }

  public CommLog( Long id, Long version )
  {
    super( id, version );
    defaultGuidAndDateInitiated();
  }

  private void defaultGuidAndDateInitiated()
  {
    guid = GuidUtils.generateGuid();
    dateInitiated = new Timestamp( System.currentTimeMillis() );
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public Timestamp getDateInitiated()
  {
    return dateInitiated;
  }

  public void setDateInitiated( Timestamp dateInitiated )
  {
    this.dateInitiated = dateInitiated;
  }

  public CommLogStatusType getCommLogStatusType()
  {
    return commLogStatusType;
  }

  public void setCommLogStatusType( CommLogStatusType commLogStatusType )
  {
    this.commLogStatusType = commLogStatusType;
  }

  public CommLogSourceType getCommLogSourceType()
  {
    return commLogSourceType;
  }

  public void setCommLogSourceType( CommLogSourceType commLogSourceType )
  {
    this.commLogSourceType = commLogSourceType;
  }

  public CommLogCategoryType getCommLogCategoryType()
  {
    return commLogCategoryType;
  }

  public void setCommLogCategoryType( CommLogCategoryType commLogCategoryType )
  {
    this.commLogCategoryType = commLogCategoryType;
  }

  public CommLogReasonType getCommLogReasonType()
  {
    return commLogReasonType;
  }

  public void setCommLogReasonType( CommLogReasonType commLogReasonType )
  {
    this.commLogReasonType = commLogReasonType;
  }

  public CommLogUrgencyType getCommLogUrgencyType()
  {
    return commLogUrgencyType;
  }

  public void setCommLogUrgencyType( CommLogUrgencyType commLogUrgencyType )
  {
    this.commLogUrgencyType = commLogUrgencyType;
  }

  public User getAssignedToUser()
  {
    return assignedToUser;
  }

  public void setAssignedToUser( User assignedToUser )
  {
    this.assignedToUser = assignedToUser;
  }

  public User getAssignedByUser()
  {
    return assignedByUser;
  }

  public void setAssignedByUser( User assignedByUser )
  {
    this.assignedByUser = assignedByUser;
  }

  public Mailing getMailing()
  {
    return mailing;
  }

  public void setMailing( Mailing mailing )
  {
    this.mailing = mailing;
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public void addComment( CommLogComment comment )
  {
    comment.setCommLog( this );
    comments.add( comment );
  }

  public Set getComments()
  {
    return comments;
  }

  public void setComments( Set comments )
  {
    this.comments = comments;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof CommLog ) )
    {
      return false;
    }

    final CommLog log = (CommLog)o;

    if ( guid != null ? !guid.equals( log.guid ) : log.guid != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return guid != null ? guid.hashCode() : 0;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public Timestamp getDateEscalated()
  {
    return dateEscalated;
  }

  public void setDateEscalated( Timestamp dateEscalated )
  {
    this.dateEscalated = dateEscalated;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public String getPlainMessage()
  {
    return plainMessage;
  }

  public void setPlainMessage( String plainMessage )
  {
    this.plainMessage = plainMessage;
  }

  public String getMessageType()
  {
    return messageType;
  }

  public void setMessageType( String messageType )
  {
    this.messageType = messageType;
  }

}

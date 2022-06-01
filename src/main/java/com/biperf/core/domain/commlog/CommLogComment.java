/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/commlog/CommLogComment.java,v $
 *
 */

package com.biperf.core.domain.commlog;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.GuidUtils;

/**
 * CommLogComment <p/> <b>Change History:</b><br>
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
public class CommLogComment extends BaseDomain
{
  private String guid;
  private String comments;
  private User commentUser;
  private CommLog commLog;

  public CommLogComment()
  {
    super();
    guid = generateGuid();
  }

  public CommLogComment( Long id )
  {
    super( id );
    guid = generateGuid();
  }

  public CommLogComment( Long id, Long version )
  {
    super( id, version );
    guid = generateGuid();
  }

  private String generateGuid()
  {
    return GuidUtils.generateGuid();
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public User getCommentUser()
  {
    return commentUser;
  }

  public void setCommentUser( User commentUser )
  {
    this.commentUser = commentUser;
  }

  public CommLog getCommLog()
  {
    return commLog;
  }

  public void setCommLog( CommLog commLog )
  {
    this.commLog = commLog;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof CommLogComment ) )
    {
      return false;
    }

    final CommLogComment comment = (CommLogComment)o;

    if ( guid != null ? !guid.equals( comment.guid ) : comment.guid != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return guid != null ? guid.hashCode() : 0;
  }
}

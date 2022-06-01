
package com.biperf.core.domain.welcomemail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * WelcomeMessage.
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
 * <td>Ramesh Kunasekaran</td>
 * <td>Sep 18, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class WelcomeMessage extends BaseDomain
{
  private Date notificationDate;
  private Long messageId;
  private Long secondaryMessageId;
  private Set audienceSet = new HashSet();
  private List audienceList = new ArrayList();

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    else
    {
      if ( object instanceof WelcomeMessage )
      {
        WelcomeMessage castObj = (WelcomeMessage)object;
        if ( notificationDate.equals( castObj.getNotificationDate() ) && messageId.equals( castObj.getMessageId() ) && secondaryMessageId.equals( castObj.getSecondaryMessageId() )
            && audienceSet.equals( castObj.getAudienceSet() ) && audienceList.equals( castObj.getAudienceList() ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  public int hashCode()
  {
    return 0;
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "WelcomeMessage [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{notificationDate=" ).append( this.getNotificationDate() ).append( "}, " );
    buf.append( "{messageId=" ).append( this.getMessageId() ).append( "}" );
    buf.append( "{secondaryMessageId=" ).append( this.getSecondaryMessageId() ).append( "}" );
    buf.append( "]" );
    return buf.toString();
  }

  public Long getMessageId()
  {
    return messageId;
  }

  public Long getSecondaryMessageId()
  {
    return secondaryMessageId;
  }

  public void setSecondaryMessageId( Long secondaryMessageId )
  {
    this.secondaryMessageId = secondaryMessageId;
  }

  public void setMessageId( Long messageId )
  {
    this.messageId = messageId;
  }

  public Date getNotificationDate()
  {
    return notificationDate;
  }

  public void setNotificationDate( Date notificationDate )
  {
    this.notificationDate = notificationDate;
  }

  public Set getAudienceSet()
  {
    return audienceSet;
  }

  public void setAudienceSet( Set audienceSet )
  {

    this.audienceSet = audienceSet;
  }

  public List getAudienceList()
  {
    return audienceList;
  }

  public void setAudienceList( List audienceList )
  {
    this.audienceList = audienceList;
  }

  /**
   * Returns the types of audience set object.
   *
   * @return the types of audience that can assume this role, as a <code>Set</code>
   *         of {@link Set} objects.
   */
  public Set getAudienceId()
  {
    if ( audienceSet == null )
    {
      audienceSet = new HashSet();

      for ( Iterator iter = audienceList.iterator(); iter.hasNext(); )
      {
        Long audienceId = (Long)iter.next();
        audienceSet.add( audienceId );
      }
    }

    return audienceSet;
  }

}

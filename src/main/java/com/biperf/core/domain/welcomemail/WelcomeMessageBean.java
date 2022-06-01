
package com.biperf.core.domain.welcomemail;

import java.io.Serializable;

/**
 * 
 * TileSetupBean.
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
public class WelcomeMessageBean implements Serializable
{

  long welcomeMessageId;
  String message;
  String secondaryMessage;
  String audienceList;
  String notificationDate;
  long audienceId;
  long messageId;
  long secondaryMessageId;

  public long getAudienceId()
  {
    return audienceId;
  }

  public void setAudienceId( long audienceId )
  {
    this.audienceId = audienceId;
  }

  public long getMessageId()
  {
    return messageId;
  }

  public void setMessageId( long messageId )
  {
    this.messageId = messageId;
  }

  public String getAudienceList()
  {
    return audienceList;
  }

  public void setAudienceList( String audienceList )
  {
    this.audienceList = audienceList;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getNotificationDate()
  {
    return notificationDate;
  }

  public void setNotificationDate( String notificationDate )
  {
    this.notificationDate = notificationDate;
  }

  public long getWelcomeMessageId()
  {
    return welcomeMessageId;
  }

  public void setWelcomeMessageId( long welcomeMessageId )
  {
    this.welcomeMessageId = welcomeMessageId;
  }

  public long getSecondaryMessageId()
  {
    return secondaryMessageId;
  }

  public void setSecondaryMessageId( long secondaryMessageId )
  {
    this.secondaryMessageId = secondaryMessageId;
  }

  public String getSecondaryMessage()
  {
    return secondaryMessage;
  }

  public void setSecondaryMessage( String secondaryMessage )
  {
    this.secondaryMessage = secondaryMessage;
  }

}

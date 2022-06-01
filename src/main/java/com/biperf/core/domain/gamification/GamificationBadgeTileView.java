
package com.biperf.core.domain.gamification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class object is used to generate gamification json object for new G5 layout.
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
 * <td>sharafud</td>
 * <td>sep 14, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class GamificationBadgeTileView
{
  // This is for future use to keep additional messages
  private String[] messages = {};
  private BadgeInfo badgeInfo;
  // Added successMessage for Testing Purpose
  @JsonIgnore
  private boolean successMessage;

  public GamificationBadgeTileView( String[] messages )
  {
    this.messages = messages;
  }

  public GamificationBadgeTileView()
  {

  }

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public BadgeInfo getBadgeInfo()
  {
    return badgeInfo;
  }

  public void setBadgeInfo( BadgeInfo badgeInfo )
  {
    this.badgeInfo = badgeInfo;
  }

  // Added for Testing Purpose
  public boolean isSuccessMessage()
  {
    return successMessage;
  }

  public void setSuccessMessage( boolean successMessage )
  {
    this.successMessage = successMessage;
  }

}

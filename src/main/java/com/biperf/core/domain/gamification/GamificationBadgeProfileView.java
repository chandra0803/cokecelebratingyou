
package com.biperf.core.domain.gamification;

import java.util.ArrayList;
import java.util.List;

/**
 * This class object is used to generate gamification json object for participant profile in new G5 layout.
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
 * <td>sep 25, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GamificationBadgeProfileView
{
  // This is for future use to keep additional messages
  private String[] messages = {};
  private List<BadgeInfo> badgeGroups = new ArrayList<BadgeInfo>();

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<BadgeInfo> getBadgeGroups()
  {
    return badgeGroups;
  }

  public void setBadgeGroups( List<BadgeInfo> badgeGroups )
  {
    this.badgeGroups = badgeGroups;
  }

}

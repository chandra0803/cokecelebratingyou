
package com.biperf.core.value;

import java.io.Serializable;
import java.util.Date;

/**
 * AwardGenFileExtractValueBean
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
 * <td>chowdhur</td>
 * <td>Jul 31, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGenFileExtractValueBean implements Serializable
{
  private Long userId;
  private String userName;
  private Long awardAmount;
  private String levelName;
  private Date awardDate;
  private Integer anniversaryNumberOfDays = new Integer( 0 );
  private Integer anniversaryNumberOfYears = new Integer( 0 );
  private Date issueDate;
  private long ordinalPosition;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public Long getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( Long awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public Integer getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( Integer anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

  public Integer getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( Integer anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public Date getIssueDate()
  {
    return issueDate;
  }

  public void setIssueDate( Date issueDate )
  {
    this.issueDate = issueDate;
  }

  public long getOrdinalPosition()
  {
    return ordinalPosition;
  }

  public void setOrdinalPosition( long ordinalPosition )
  {
    this.ordinalPosition = ordinalPosition;
  }

}

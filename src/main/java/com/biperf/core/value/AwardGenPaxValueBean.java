
package com.biperf.core.value;

import java.io.Serializable;

/**
 * AwardGenPaxValueBean
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
 * <td>Jul 26, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGenPaxValueBean implements Serializable
{
  private Long userId;
  private Long year;
  private Long days;
  private String anniversaryDate;

  public AwardGenPaxValueBean()
  {

  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getYear()
  {
    return year;
  }

  public void setYear( Long year )
  {
    this.year = year;
  }

  public Long getDays()
  {
    return days;
  }

  public void setDays( Long days )
  {
    this.days = days;
  }

  public String getAnniversaryDate()
  {
    return anniversaryDate;
  }

  public void setAnniversaryDate( String anniversaryDate )
  {
    this.anniversaryDate = anniversaryDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "AwardGenPaxValueBean [userId=" + userId + ", year=" + year + ", days=" + days + ", anniversaryDate=" + anniversaryDate + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( anniversaryDate == null ? 0 : anniversaryDate.hashCode() );
    result = prime * result + ( userId == null ? 0 : userId.hashCode() );
    result = prime * result + ( year == null ? 0 : year.hashCode() );
    result = prime * result + ( days == null ? 0 : days.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    AwardGenPaxValueBean other = (AwardGenPaxValueBean)obj;
    if ( anniversaryDate == null )
    {
      if ( other.anniversaryDate != null )
      {
        return false;
      }
    }
    else if ( !anniversaryDate.equals( other.anniversaryDate ) )
    {
      return false;
    }
    if ( userId == null )
    {
      if ( other.userId != null )
      {
        return false;
      }
    }
    else if ( !userId.equals( other.userId ) )
    {
      return false;
    }
    if ( year == null )
    {
      if ( other.year != null )
      {
        return false;
      }
    }
    else if ( !year.equals( other.year ) )
    {
      return false;
    }
    if ( days == null )
    {
      if ( other.days != null )
      {
        return false;
      }
    }
    else if ( !days.equals( other.days ) )
    {
      return false;
    }
    return true;
  }

}

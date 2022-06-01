/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/awardgenerator/AwardGenAward.java,v $
 */

package com.biperf.core.domain.awardgenerator;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;

/**
 * AwardGenAward
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
 * <td>Jul 15, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardGenAward extends BaseDomain
{
  private AwardGenerator awardGen;
  private Integer years = new Integer( 0 );
  private Integer days = new Integer( 0 );
  private Long awardAmount = new Long( 0 );
  private PromoMerchProgramLevel level;
  private boolean deleted = false;

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "AwardGenAward [awardGen=" + awardGen + ", years=" + years + ", days=" + days + ", awardAmount=" + awardAmount + ", level=" + level + ", deleted=" + deleted + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( awardAmount == null ? 0 : awardAmount.hashCode() );
    result = prime * result + ( awardGen == null ? 0 : awardGen.hashCode() );
    result = prime * result + ( level == null ? 0 : level.hashCode() );
    result = prime * result + ( years == null ? 0 : years.hashCode() );
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
    AwardGenAward other = (AwardGenAward)obj;
    if ( awardAmount == null )
    {
      if ( other.awardAmount != null )
      {
        return false;
      }
    }
    else if ( !awardAmount.equals( other.awardAmount ) )
    {
      return false;
    }
    if ( awardGen == null )
    {
      if ( other.awardGen != null )
      {
        return false;
      }
    }
    else if ( !awardGen.equals( other.awardGen ) )
    {
      return false;
    }
    if ( level == null )
    {
      if ( other.level != null )
      {
        return false;
      }
    }
    else if ( !level.equals( other.level ) )
    {
      return false;
    }
    if ( years == null )
    {
      if ( other.years != null )
      {
        return false;
      }
    }
    else if ( !years.equals( other.years ) )
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

  public AwardGenerator getAwardGen()
  {
    return awardGen;
  }

  public void setAwardGen( AwardGenerator awardGen )
  {
    this.awardGen = awardGen;
  }

  public Integer getYears()
  {
    return years;
  }

  public void setYears( Integer years )
  {
    this.years = years;
  }

  public Long getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( Long awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public PromoMerchProgramLevel getLevel()
  {
    return level;
  }

  public void setLevel( PromoMerchProgramLevel level )
  {
    this.level = level;
  }

  public Integer getDays()
  {
    return days;
  }

  public void setDays( Integer days )
  {
    this.days = days;
  }

  public boolean isDeleted()
  {
    return deleted;
  }

  public void setDeleted( boolean deleted )
  {
    this.deleted = deleted;
  }

}

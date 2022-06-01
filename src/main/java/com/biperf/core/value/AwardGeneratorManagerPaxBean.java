/**
 * 
 */

package com.biperf.core.value;

import java.util.Date;

public class AwardGeneratorManagerPaxBean
{
  private Long userId;
  private String name;
  private Date anniversaryDate;
  private Date awardDate;
  private Long awardGenPaxId;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Date getAnniversaryDate()
  {
    return anniversaryDate;
  }

  public void setAnniversaryDate( Date anniversaryDate )
  {
    this.anniversaryDate = anniversaryDate;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public Long getAwardGenPaxId()
  {
    return awardGenPaxId;
  }

  public void setAwardGenPaxId( Long awardGenPaxId )
  {
    this.awardGenPaxId = awardGenPaxId;
  }

}

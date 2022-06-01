/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.vo.ots;

import java.util.Date;

/**
 * TODO Javadoc for OTSProgramVO.
 * 
 * @author rajadura
 * @since Dec 6, 2017
 * 
 */
public class OTSProgramVO
{

  private String programNumber;
  private String recieverAudience;
  private String programStatus;
  private Date lastEditedDate;
  private String lastEditedBy;

  public String getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

  public String getProgramStatus()
  {
    return programStatus;
  }

  public void setProgramStatus( String programStatus )
  {
    this.programStatus = programStatus;
  }

  public String getRecieverAudience()
  {
    return recieverAudience;
  }

  public void setRecieverAudience( String recieverAudience )
  {
    this.recieverAudience = recieverAudience;
  }

  public Date getLastEditedDate()
  {
    return lastEditedDate;
  }

  public void setLastEditedDate( Date lastEditedDate )
  {
    this.lastEditedDate = lastEditedDate;
  }

  public String getLastEditedBy()
  {
    return lastEditedBy;
  }

  public void setLastEditedBy( String lastEditedBy )
  {
    this.lastEditedBy = lastEditedBy;
  }

}

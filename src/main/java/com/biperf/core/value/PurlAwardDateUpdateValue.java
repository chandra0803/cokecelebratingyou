
package com.biperf.core.value;

import java.io.Serializable;
import java.util.Date;

public class PurlAwardDateUpdateValue implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Date awardDate;
  private Date closeDate;
  private String rowName;
  private Long purlRecipientId;

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  public void setCloseDate( Date closeDate )
  {
    this.closeDate = closeDate;
  }

  public Date getCloseDate()
  {
    return closeDate;
  }

  public String getRowName()
  {
    return rowName;
  }

  public void setRowName( String rowName )
  {
    this.rowName = rowName;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

}

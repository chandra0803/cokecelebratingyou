
package com.biperf.core.value.plateauawards;

import java.util.Date;

public class PlateauAwardCodeIssuanceReportValue
{
  private Date reportDate;
  private Long redeemedCnt;
  private Long notRedeemedCnt;
  private Long expiredCnt;

  public Date getReportDate()
  {
    return reportDate;
  }

  public void setReportDate( Date reportDate )
  {
    this.reportDate = reportDate;
  }

  public Long getRedeemedCnt()
  {
    return redeemedCnt;
  }

  public void setRedeemedCnt( Long redeemedCnt )
  {
    this.redeemedCnt = redeemedCnt;
  }

  public Long getNotRedeemedCnt()
  {
    return notRedeemedCnt;
  }

  public void setNotRedeemedCnt( Long notRedeemedCnt )
  {
    this.notRedeemedCnt = notRedeemedCnt;
  }

  public Long getExpiredCnt()
  {
    return expiredCnt;
  }

  public void setExpiredCnt( Long expiredCnt )
  {
    this.expiredCnt = expiredCnt;
  }

}

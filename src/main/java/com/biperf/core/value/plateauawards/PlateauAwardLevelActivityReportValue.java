
package com.biperf.core.value.plateauawards;

import java.math.BigDecimal;

public class PlateauAwardLevelActivityReportValue
{
  private Long nodeId;
  private String orgName;
  private String levelName;
  private Long issuedCnt;
  private Long redeemedCnt;
  private Long notRedeemedCnt;
  private Long expiredCnt;
  private BigDecimal redeemedPct;
  private BigDecimal notRedeemedPct;
  private BigDecimal expiredPct;

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public Long getIssuedCnt()
  {
    return issuedCnt;
  }

  public void setIssuedCnt( Long issuedCnt )
  {
    this.issuedCnt = issuedCnt;
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

  public BigDecimal getRedeemedPct()
  {
    return redeemedPct;
  }

  public void setRedeemedPct( BigDecimal redeemedPct )
  {
    this.redeemedPct = redeemedPct;
  }

  public BigDecimal getNotRedeemedPct()
  {
    return notRedeemedPct;
  }

  public void setNotRedeemedPct( BigDecimal notRedeemedPct )
  {
    this.notRedeemedPct = notRedeemedPct;
  }

  public BigDecimal getExpiredPct()
  {
    return expiredPct;
  }

  public void setExpiredPct( BigDecimal expiredPct )
  {
    this.expiredPct = expiredPct;
  }

}

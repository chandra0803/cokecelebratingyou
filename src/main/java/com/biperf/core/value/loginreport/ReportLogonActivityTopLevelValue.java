
package com.biperf.core.value.loginreport;

import java.math.BigDecimal;

/**
 * ReportLogonActivityTopLevelValue.
 * 
 * @author kandhi
 * @since Jul 6, 2012
 * @version 1.0
 */
public class ReportLogonActivityTopLevelValue
{

  private Long nodeId;
  private String nodeName;
  private int elgParticipantsCnt;
  private int loggedInCnt;
  private BigDecimal loggedInPct;
  private int notLoggedInCnt;
  private BigDecimal notLoggedInPct;
  private int totalVisitsCnt;

  public ReportLogonActivityTopLevelValue()
  {
    super();
  }

  public ReportLogonActivityTopLevelValue( Long nodeId,
                                           String nodeName,
                                           int elgParticipantsCnt,
                                           int loggedInCnt,
                                           BigDecimal loggedInPct,
                                           int notLoggedInCnt,
                                           BigDecimal notLoggedInPct,
                                           int totalVisitsCnt )
  {
    super();
    this.nodeId = nodeId;
    this.nodeName = nodeName;
    this.elgParticipantsCnt = elgParticipantsCnt;
    this.loggedInCnt = loggedInCnt;
    this.loggedInPct = loggedInPct;
    this.notLoggedInCnt = notLoggedInCnt;
    this.notLoggedInPct = notLoggedInPct;
    this.totalVisitsCnt = totalVisitsCnt;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public int getElgParticipantsCnt()
  {
    return elgParticipantsCnt;
  }

  public void setElgParticipantsCnt( int elgParticipantsCnt )
  {
    this.elgParticipantsCnt = elgParticipantsCnt;
  }

  public int getLoggedInCnt()
  {
    return loggedInCnt;
  }

  public void setLoggedInCnt( int loggedInCnt )
  {
    this.loggedInCnt = loggedInCnt;
  }

  public BigDecimal getLoggedInPct()
  {
    return loggedInPct;
  }

  public void setLoggedInPct( BigDecimal loggedInPct )
  {
    this.loggedInPct = loggedInPct;
  }

  public int getNotLoggedInCnt()
  {
    return notLoggedInCnt;
  }

  public void setNotLoggedInCnt( int notLoggedInCnt )
  {
    this.notLoggedInCnt = notLoggedInCnt;
  }

  public BigDecimal getNotLoggedInPct()
  {
    return notLoggedInPct;
  }

  public void setNotLoggedInPct( BigDecimal notLoggedInPct )
  {
    this.notLoggedInPct = notLoggedInPct;
  }

  public int getTotalVisitsCnt()
  {
    return totalVisitsCnt;
  }

  public void setTotalVisitsCnt( int totalVisitsCnt )
  {
    this.totalVisitsCnt = totalVisitsCnt;
  }

}

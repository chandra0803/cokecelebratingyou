
package com.biperf.core.value.ssi;

import java.util.List;

/**
 * 
 * SSIContestContentValueBean.
 * 
 * @author chowdhur
 * @since Jan 13, 2015
 */
public class SSIContestSummaryValueBean
{
  private String contestType;
  private String payoutType;
  private boolean includeBonus;
  private boolean includeBaseline;
  private List<SSIContestSummaryTDPaxResultBean> paxResults;
  private List<SSIContestSummaryTDColumnBean> columns;
  private List<SSIContestSummaryTDSubColumnBean> subColumns;
  private boolean footerActive;

  private int total;
  private int perPage;
  private int current;
  private String sortedBy;
  private String sortedOn;

  private Long contestId;

  public void addPaxResults( List<SSIContestSummaryTDPaxResultBean> paxResults, String measureType )
  {
    for ( SSIContestSummaryTDPaxResultBean paxResult : paxResults )
    {
      paxResult.setActivityMeasureType( measureType );
    }
    this.paxResults = paxResults;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public boolean isIncludeBaseline()
  {
    return includeBaseline;
  }

  public void setIncludeBaseline( boolean includeBaseline )
  {
    this.includeBaseline = includeBaseline;
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public int getPerPage()
  {
    return perPage;
  }

  public void setPerPage( int perPage )
  {
    this.perPage = perPage;
  }

  public int getCurrent()
  {
    return current;
  }

  public void setCurrent( int current )
  {
    this.current = current;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public List<SSIContestSummaryTDColumnBean> getColumns()
  {
    return columns;
  }

  public void setColumns( List<SSIContestSummaryTDColumnBean> columns )
  {
    this.columns = columns;
  }

  public List<SSIContestSummaryTDSubColumnBean> getSubColumns()
  {
    return subColumns;
  }

  public void setSubColumns( List<SSIContestSummaryTDSubColumnBean> subColumns )
  {
    this.subColumns = subColumns;
  }

  public boolean isFooterActive()
  {
    return footerActive;
  }

  public void setFooterActive( boolean footerActive )
  {
    this.footerActive = footerActive;
  }

  public List<SSIContestSummaryTDPaxResultBean> getPaxResults()
  {
    return paxResults;
  }

  public void setPaxResults( List<SSIContestSummaryTDPaxResultBean> paxResults )
  {
    this.paxResults = paxResults;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

}

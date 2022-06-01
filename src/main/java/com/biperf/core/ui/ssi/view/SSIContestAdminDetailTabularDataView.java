
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.value.ssi.SSIContestSummaryTDColumnBean;
import com.biperf.core.value.ssi.SSIContestSummaryTDPaxResultBean;
import com.biperf.core.value.ssi.SSIContestSummaryTDSubColumnBean;

/**
 * 
 * SSIContestAdminDetailTabularDataView.
 * 
 * @author chowdhur
 * @since Jan 13, 2015
 */
public class SSIContestAdminDetailTabularDataView
{
  private List<SSIContestSummaryTDColumnBean> columns;
  private List<SSIContestSummaryTDSubColumnBean> subColumns;
  private List<SSIContestSummaryTDPaxResultBean> paxResults;

  private boolean footerActive;

  public SSIContestAdminDetailTabularDataView()
  {

  }

  /**
   * @param columns
   * @param subColumns
   * @param footerActive
   * @param results
   */
  public SSIContestAdminDetailTabularDataView( List<SSIContestSummaryTDColumnBean> columns,
                                               List<SSIContestSummaryTDSubColumnBean> subColumns,
                                               List<SSIContestSummaryTDPaxResultBean> paxResults,
                                               boolean footerActive )
  {
    super();
    this.columns = columns;
    this.subColumns = subColumns;
    this.paxResults = paxResults;
    this.footerActive = footerActive;
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

}

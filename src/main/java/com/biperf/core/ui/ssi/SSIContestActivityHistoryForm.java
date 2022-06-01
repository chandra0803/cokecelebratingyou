
package com.biperf.core.ui.ssi;

import com.biperf.core.ui.BaseActionForm;

/**
 * @author dudam
 * @since Dec 23, 2014
 * @version 1.0
 */
public class SSIContestActivityHistoryForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String id;
  private String sortedOn;
  private String sortedBy;
  private int perPage;
  private int total;
  private int page;

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public int getPerPage()
  {
    return perPage;
  }

  public void setPerPage( int perPage )
  {
    this.perPage = perPage;
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

}

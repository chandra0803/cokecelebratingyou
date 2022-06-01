
package com.biperf.core.value.ssi;

/**
 * SSIContestPaginationValueBean.
 * 
 * @author dudam
 * @since Jun 5, 2015
 * @version 1.0
 */
public class SSIContestPaginationValueBean
{
  private int currentPage;
  private int pageSize;
  private String sortedOn;
  private String sortedBy;

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

  public int getPageSize()
  {
    return pageSize;
  }

  public void setPageSize( int pageSize )
  {
    this.pageSize = pageSize;
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

}

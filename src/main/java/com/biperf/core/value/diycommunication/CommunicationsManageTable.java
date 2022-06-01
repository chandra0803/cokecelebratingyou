
package com.biperf.core.value.diycommunication;

import java.util.List;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.value.BaseJsonView;

public class CommunicationsManageTable extends BaseJsonView
{
  private static final long serialVersionUID = 1L;

  private int total;
  private int currentPage;
  private String sortedOn;
  private String sortedBy;

  public CommunicationsManageTable()
  {
  }

  public CommunicationsManageTable( List<DIYCommunications> communicationList )
  {
    this.total = 100;
    this.currentPage = 1;
    this.sortedOn = "1";
    this.sortedBy = "asc";
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
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

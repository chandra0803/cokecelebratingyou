
package com.biperf.core.ui.nomination;

import com.biperf.core.ui.BaseForm;

public class NomInProgressForm extends BaseForm
{
  private static final long serialVersionUID = 1L;
  private int startIndex;
  private String sortBy;
  private String sortOn;
  private int endIndex;
  private int currentPage;

  public int getStartIndex()
  {
    return startIndex;
  }

  public void setStartIndex( int startIndex )
  {
    this.startIndex = startIndex;
  }

  public int getEndIndex()
  {
    return endIndex;
  }

  public void setEndIndex( int endIndex )
  {
    this.endIndex = endIndex;
  }

  public String getSortBy()
  {
    return sortBy;
  }

  public void setSortBy( String sortBy )
  {
    this.sortBy = sortBy;
  }

  public String getSortOn()
  {
    return sortOn;
  }

  public void setSortOn( String sortOn )
  {
    this.sortOn = sortOn;
  }

  public int getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( int currentPage )
  {
    this.currentPage = currentPage;
  }

}

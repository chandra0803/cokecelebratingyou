
package com.biperf.core.utils;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

/**
 * DisplaytagPage is a wrapper for a ListPageInfo object. By sending displaytag an object that
 * implements the PaginatedList interface, displaytag will assume that the list is externally sorted
 * and paginated.
 */
public class DisplaytagPage implements PaginatedList
{
  private AdvancedListPageInfo pageInfo;

  public DisplaytagPage()
  {
  }

  public DisplaytagPage( AdvancedListPageInfo pageInfo )
  {
    this.pageInfo = pageInfo;
  }

  public AdvancedListPageInfo getPageInfo()
  {
    return pageInfo;
  }

  public void setPageInfo( AdvancedListPageInfo pageInfo )
  {
    this.pageInfo = pageInfo;
  }

  public List getList()
  {
    return pageInfo.getList();
  }

  public int getPageNumber()
  {
    return pageInfo.getPageNumber();
  }

  public int getObjectsPerPage()
  {
    return pageInfo.getObjectsPerPage();
  }

  public int getFullListSize()
  {
    return pageInfo.getFullListSize();
  }

  public String getSortCriterion()
  {
    return pageInfo.getSortCriterion();
  }

  public SortOrderEnum getSortDirection()
  {
    return pageInfo.isSortAscending() ? SortOrderEnum.ASCENDING : SortOrderEnum.DESCENDING;
  }

  public String getSearchId()
  {
    return null;
  }
}

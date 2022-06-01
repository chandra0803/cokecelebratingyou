
package com.biperf.core.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ListPageInfo<T> implements Serializable
{
  private static final long serialVersionUID = 1L;

  public static final Long DEFAULT_INITIAL_PAGE = 1L;

  private List<T> fullList;
  private Long resultsPerPage;
  private Long currentPage;

  public ListPageInfo( List<T> fullList, Long resultsPerPage, Long currentPage )
  {
    super();
    this.fullList = fullList;
    this.resultsPerPage = resultsPerPage;
    this.currentPage = currentPage;
  }

  public Long getTotalPages()
  {
    BigDecimal listSize = BigDecimal.valueOf( fullList.size() );
    BigDecimal pageSize = BigDecimal.valueOf( resultsPerPage );
    return listSize.divide( pageSize, RoundingMode.UP ).longValue();
  }

  public List<T> getCurrentPageList()
  {
    Long totalPages = getTotalPages();

    if ( totalPages < 1 )
    {
      return new ArrayList<T>();
    }
    if ( totalPages == 1 )
    {
      return fullList;
    }

    Long fromIndex = ( currentPage - 1 ) * resultsPerPage;
    Long toIndex = currentPage * resultsPerPage;
    if ( toIndex > fullList.size() )
    {
      toIndex = Long.valueOf( fullList.size() );
    }

    return fullList.subList( fromIndex.intValue(), toIndex.intValue() );
  }

  public void setFullList( List<T> fullList )
  {
    this.fullList = fullList;
  }

  public void setResultsPerPage( Long resultsPerPage )
  {
    this.resultsPerPage = resultsPerPage;
  }

  public Long getResultsPerPage()
  {
    return resultsPerPage;
  }

  public List<T> getFullList()
  {
    return fullList;
  }

  public Long getCurrentPage()
  {
    return currentPage;
  }

  public void setCurrentPage( Long currentPage )
  {
    this.currentPage = currentPage;
  }

}

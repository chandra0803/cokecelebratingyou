
package com.biperf.core.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Holds the (paging and sorting) state of a display table or list.
 */
public class AdvancedListPageInfo<T> implements Serializable
{
  public static final String PARAM_PAGE_NUMBER = "page";
  public static final int DEFAULT_PAGE_SIZE = 25;
  public static final int FULL_LIST_PAGE_SIZE = 0;

  private int pageNumber = 1;
  private int objectsPerPage = AdvancedListPageInfo.DEFAULT_PAGE_SIZE;
  private int fullListSize;
  private String sortCriterion;
  private boolean sortAscending = true;
  private String[] defaultSortCriteria = new String[0];
  private boolean exporting = false;
  private List<T> list;

  public AdvancedListPageInfo()
  {
  }

  public AdvancedListPageInfo( AdvancedListPageInfo src )
  {
    this.pageNumber = src.pageNumber;
    this.objectsPerPage = src.objectsPerPage;
    this.fullListSize = src.fullListSize;
    this.sortCriterion = src.sortCriterion;
    this.sortAscending = src.sortAscending;
    this.defaultSortCriteria = src.defaultSortCriteria;
    this.exporting = src.exporting;
  }

  public int getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber( int pageNumber )
  {
    this.pageNumber = pageNumber;
  }

  public int getObjectsPerPage()
  {
    return objectsPerPage;
  }

  public void setObjectsPerPage( int objectsPerPage )
  {
    this.objectsPerPage = objectsPerPage;
  }

  public int getFullListSize()
  {
    return fullListSize;
  }

  public void setFullListSize( int fullListSize )
  {
    this.fullListSize = fullListSize;
  }

  public String getSortCriterion()
  {
    return sortCriterion;
  }

  public void setSortCriterion( String sortCriterion )
  {
    this.sortCriterion = sortCriterion;
  }

  public String getSortDirection()
  {
    if ( isSortAscending() )
    {
      return "asc";
    }
    else
    {
      return "desc";
    }
  }

  public boolean isSortAscending()
  {
    return sortAscending;
  }

  public void setSortAscending( boolean sortAscending )
  {
    this.sortAscending = sortAscending;
  }

  public String[] getDefaultSortCriteria()
  {
    return defaultSortCriteria;
  }

  public void setDefaultSortCriteria( String[] defaultSortCriteria )
  {
    if ( defaultSortCriteria != null )
    {
      this.defaultSortCriteria = defaultSortCriteria;
    }
    else
    {
      this.defaultSortCriteria = new String[0];
    }
  }

  public boolean isExporting()
  {
    return exporting;
  }

  public void setExporting( boolean exporting )
  {
    this.exporting = exporting;
  }

  public List<T> getList()
  {
    return list;
  }

  public void setList( List<T> list )
  {
    this.list = list;
  }

  public int getFirstResultIndex()
  {
    return objectsPerPage * ( pageNumber - 1 );
  }

  public int getLastResultIndex( int resultSize )
  {
    int firstResultIndex = getFirstResultIndex();
    int lastResultIndex = firstResultIndex + objectsPerPage - 1;

    if ( lastResultIndex >= resultSize - 1 )
    {
      lastResultIndex = resultSize - 1;
    }

    return lastResultIndex;
  }

  public boolean isFirstPage()
  {
    return pageNumber == 1;
  }

  public boolean isLastPage()
  {
    return fullListSize == ( pageNumber - 1 ) * objectsPerPage + ( list == null ? 0 : list.size() );
  }

  public void setFullList( List<T> fullList )
  {
    // set the full list size property
    this.fullListSize = fullList.size();

    if ( objectsPerPage == FULL_LIST_PAGE_SIZE )
    {
      list = fullList;
      return;
    }

    // get the subset of the list based on the pagination
    List<T> temp = new ArrayList<T>();
    int startIndex = objectsPerPage * ( pageNumber - 1 );
    for ( int i = startIndex; i < startIndex + objectsPerPage; i++ )
    {
      try
      {
        temp.add( fullList.get( i ) );
      }
      catch( IndexOutOfBoundsException e )
      {
        // do nothing, just exit the for loop
      }
    }

    // set the internal list to be the temp
    list = temp;
  }

  public void setFullList( List<T> fullList, Comparator<T> comparator )
  {
    Collections.sort( fullList, comparator );
    setFullList( fullList );
  }
}

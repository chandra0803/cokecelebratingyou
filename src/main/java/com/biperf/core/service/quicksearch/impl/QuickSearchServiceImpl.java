/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.quicksearch.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.quicksearch.QuickSearchDAO;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.quicksearch.QuickSearchService;

/**
 * QuickSearchServiceImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>wadzinsk</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuickSearchServiceImpl implements QuickSearchService
{

  private QuickSearchDAO quickSearchDAO;
  private Map resultsFormatterMap;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.quicksearch.QuickSearchService#getHeaderFullKeys(java.lang.String)
   * @param quickSearchSearchForFieldCode
   * @return List
   */
  public List getHeaderFullKeys( String quickSearchSearchForFieldCode )
  {
    return getFormatter( quickSearchSearchForFieldCode ).getHeaderFullKeys();
  }

  /**
   * Return the search results as a List of FormatterBean Objects Overridden from
   * 
   * @see com.biperf.core.service.quicksearch.QuickSearchService#search(java.lang.String,
   *      java.lang.String, java.lang.String)
   * @param quickSearchSearchForFieldCode
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @return the search results as a List of FormatterBean Objects
   */
  public List search( String quickSearchSearchForFieldCode, String quickSearchSearchByFieldCode, String quickSearchValue )
  {

    List results = quickSearchDAO.search( quickSearchSearchByFieldCode, quickSearchValue );
    if ( results.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    QuickSearchFormatter formatter = getFormatter( quickSearchSearchForFieldCode );

    List formattedResults = new ArrayList( results.size() );
    for ( Iterator iter = results.iterator(); iter.hasNext(); )
    {
      Object result = iter.next();
      formattedResults.add( formatter.format( result ) );
    }

    // Sort results by result "column 1" value ascending
    PropertyComparator.sort( formattedResults, new MutableSortDefinition( "formattedValueBeans[0].value", true, true ) );

    return formattedResults;
  }

  /**
   * Overridden from @see com.biperf.core.service.quicksearch.QuickSearchService#searchByPage(java.lang.String, java.lang.String, java.lang.String, int, int)
   * @param quickSearchSearchForFieldCode
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @param pageNumber
   * @param pageSize
   * @return search results as a list according to page number
   */

  public List searchByPage( String quickSearchSearchForFieldCode, String quickSearchSearchByFieldCode, String quickSearchValue, int sortField, boolean sortAscending, int pageNumber, int pageSize )
  {

    List results = quickSearchDAO.searchByPage( quickSearchSearchByFieldCode, quickSearchValue, sortField, sortAscending, pageNumber, pageSize );

    if ( results.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    QuickSearchFormatter formatter = getFormatter( quickSearchSearchForFieldCode );

    List formattedResults = new ArrayList( results.size() );
    for ( Iterator iter = results.iterator(); iter.hasNext(); )
    {
      Object result = iter.next();
      formattedResults.add( formatter.format( result ) );
    }

    return formattedResults;
  }

  private QuickSearchFormatter getFormatter( String quickSearchSearchForFieldCode )
  {
    QuickSearchFormatter formatter = (QuickSearchFormatter)resultsFormatterMap.get( quickSearchSearchForFieldCode );
    if ( formatter == null )
    {
      throw new BeaconRuntimeException( "No formatter defined in spring config for 'Search For' field code: " + quickSearchSearchForFieldCode );
    }
    return formatter;
  }

  /**
   * @param quickSearchDAO value for quickSearchDAO property
   */
  public void setQuickSearchDAO( QuickSearchDAO quickSearchDAO )
  {
    this.quickSearchDAO = quickSearchDAO;
  }

  /**
   * @param resultsFormatterMap value for resultsFormatterMap property
   */
  public void setResultsFormatterMap( Map resultsFormatterMap )
  {
    this.resultsFormatterMap = resultsFormatterMap;
  }

  /**
   * @return value of resultsFormatterMap property
   */
  public Map getResultsFormatterMap()
  {
    return resultsFormatterMap;
  }

  /**
   * 
   * Overridden from @see com.biperf.core.service.quicksearch.QuickSearchService#sizeOfResult(java.lang.String, java.lang.String)
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @return the total number of records returned by quick search 
   */

  public int sizeOfResult( String quickSearchSearchByFieldCode, String quickSearchValue )
  {
    return quickSearchDAO.sizeOfResult( quickSearchSearchByFieldCode, quickSearchValue );
  }

}

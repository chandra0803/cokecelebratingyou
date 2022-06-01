/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.quicksearch;

import java.util.List;

import com.biperf.core.service.SAO;

/**
 * QuickSearchService.
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
public interface QuickSearchService extends SAO
{

  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "quickSearchService";

  /**
   * Return the search results as a List of FormatterBean Objects
   * 
   * @param quickSearchSearchForFieldCode
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @return List
   */
  public List search( String quickSearchSearchForFieldCode, String quickSearchSearchByFieldCode, String quickSearchValue );

  /**
   * 
   * @param quickSearchSearchForFieldCode
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @param pageNumber
   * @param pageSize
   * @return List
   */

  public List searchByPage( String quickSearchSearchForFieldCode, String quickSearchSearchByFieldCode, String quickSearchValue, int sortField, boolean sortAscending, int pageNumber, int pageSize );

  /**
   * Returns a List of the full cm keys to be used as table headers for the quicksearch restuls. The
   * element order corresponds to the element order of FormatterBean.formattedValueBeans
   * @param quickSearchSearchForFieldCode
   * @return a List of the full cm keys to be used as table headers for the quicksearch restuls.
   */

  public List getHeaderFullKeys( String quickSearchSearchForFieldCode );

  /**
   * 
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @return int
   */

  public int sizeOfResult( String quickSearchSearchByFieldCode, String quickSearchValue );

}

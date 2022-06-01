/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/quicksearch/QuickSearchDAO.java,v $
 */

package com.biperf.core.dao.quicksearch;

import java.util.List;

import com.biperf.core.dao.DAO;

/**
 * QuickSearchDAOImpl.
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
 */
public interface QuickSearchDAO extends DAO
{

  public static final int SORT_LASTNAME = 0;
  public static final int SORT_EMAIL = 1;
  public static final int SORT_LOGINID = 2;
  public static final int SORT_BANKACCOUNT = 3;
  public static final int SORT_STATUS = 4;
  public static final int SORT_COUNTRY = 5;

  public List search( String quickSearchSearchByFieldCode, String quickSearchValue );

  /**
   * This method will return records according to page Number in order to display long lists where performance is issue
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @param pageNumber
   * @param pageSize
   * @return List 
   */

  public List searchByPage( String quickSearchSearchByFieldCode, String quickSearchValue, int sortField, boolean sortAscending, int pageNumber, int pageSize );

  /**
   * This method returns the total number of records in search list.
   * @param quickSearchSearchByFieldCode
   * @param quickSearchValue
   * @return int
   */

  public int sizeOfResult( String quickSearchSearchByFieldCode, String quickSearchValue );

}

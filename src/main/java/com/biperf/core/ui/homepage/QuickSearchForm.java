/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.homepage;

import com.biperf.core.ui.BaseActionForm;

/**
 * QuickSearchForm.
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
 * <td>wadfzinski</td>
 * <td>Sep 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuickSearchForm extends BaseActionForm
{
  private String quickSearchSearchForFieldCode;
  private String quickSearchSearchByFieldCode;
  private String quickSearchValue;

  /**
   * @return value of quickSearchSearchByFieldCode property
   */
  public String getQuickSearchSearchByFieldCode()
  {
    return quickSearchSearchByFieldCode;
  }

  /**
   * @param quickSearchSearchByFieldCode value for quickSearchSearchByFieldCode property
   */
  public void setQuickSearchSearchByFieldCode( String quickSearchSearchByFieldCode )
  {
    this.quickSearchSearchByFieldCode = quickSearchSearchByFieldCode;
  }

  /**
   * @return value of quickSearchSearchForFieldCode property
   */
  public String getQuickSearchSearchForFieldCode()
  {
    return quickSearchSearchForFieldCode;
  }

  /**
   * @param quickSearchSearchForFieldCode value for quickSearchSearchForFieldCode property
   */
  public void setQuickSearchSearchForFieldCode( String quickSearchSearchForFieldCode )
  {
    this.quickSearchSearchForFieldCode = quickSearchSearchForFieldCode;
  }

  /**
   * @return value of quickSearchValue property
   */
  public String getQuickSearchValue()
  {
    return quickSearchValue;
  }

  /**
   * @param quickSearchValue value for quickSearchValue property
   */
  public void setQuickSearchValue( String quickSearchValue )
  {
    this.quickSearchValue = quickSearchValue;
  }

}

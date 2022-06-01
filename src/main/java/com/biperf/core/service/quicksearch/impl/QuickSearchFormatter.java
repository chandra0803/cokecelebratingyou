/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.quicksearch.impl;

import java.util.List;

import com.biperf.core.value.FormatterBean;

/**
 * QuickSearchFormatter.
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
public interface QuickSearchFormatter
{
  /**
   * @return List of String objects contain column header content manager keys.
   */
  public List getHeaderFullKeys();

  /**
   * Format the given object for display in a quicksearch results view.
   * 
   * @param object
   * @return a FormatterBean object.
   */
  public FormatterBean format( Object object );
}

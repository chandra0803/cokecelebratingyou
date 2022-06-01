/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/budget/CheckItem.java,v $
 */

package com.biperf.core.ui.budget;

/**
 * CheckItem.
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
 * <td>meadows</td>
 * <td>Aug 16, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class CheckItem
{
  private String code;
  private String title;
  private boolean checked;

  public CheckItem()
  {
  }

  public CheckItem( String code, String title )
  {
    this.code = code;
    this.title = title;
  }

  public boolean getChecked()
  {
    return checked;
  }

  public void setChecked( boolean checked )
  {
    this.checked = checked;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

}

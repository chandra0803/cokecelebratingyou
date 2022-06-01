/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FormatterBean.
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
 * <td>Sep 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class FormatterBean implements Serializable
{

  private List formattedValueBeans = new ArrayList();

  public FormatterBean( List formattedValueBeans )
  {
    super();
    this.formattedValueBeans = formattedValueBeans;
  }

  /**
   * @return value of formattedValueBeans property
   */
  public List getFormattedValueBeans()
  {
    return formattedValueBeans;
  }

  /**
   * @param formattedValueBeans value for formattedValueBeans property
   */
  public void setFormattedValueBeans( List formattedValueBeans )
  {
    this.formattedValueBeans = formattedValueBeans;
  }

}

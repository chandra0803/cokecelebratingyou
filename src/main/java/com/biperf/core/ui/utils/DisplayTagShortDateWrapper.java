/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/utils/DisplayTagShortDateWrapper.java,v $
 *
 */

package com.biperf.core.ui.utils;

import java.util.Date;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.time.FastDateFormat;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.properties.MediaTypeEnum;

/**
 * DisplayTagShortDateWrapper is a decorator class for display tag to format a date in the form
 * MM-dd-yyyy <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class DisplayTagShortDateWrapper implements DisplaytagColumnDecorator
{

  /**
   * FastDateFormat used to format the date object.
   */
  private FastDateFormat dateFormat = FastDateFormat.getInstance( "MM-dd-yyyy" );

  /**
   * transform the given object into a String representation. The object is supposed to be a date.
   * 
   * @param columnValue Object
   * @param pageContext PageContext
   * @param mediaType MediaTypeEnum
   * @return Object
   */
  public final Object decorate( Object columnValue, PageContext pageContext, MediaTypeEnum mediaType )
  {
    if ( columnValue == null || ! ( columnValue instanceof Date ) )
    {
      return columnValue == null ? null : columnValue.toString();
    }
    Date date = (Date)columnValue;
    return this.dateFormat.format( date );
  }

}

/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/utils/CustomCsvView.java,v $
 */

package com.biperf.core.ui.utils;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.displaytag.export.CsvView;
import org.displaytag.model.TableModel;

/**
 * CustomCsvView.
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
 * <td>viswanat</td>
 * <td>May 7, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CustomCsvView extends CsvView
{
  /**
   * TableModel to render.
   */
  private TableModel model;

  /**
   * export full list?
   */
  private boolean exportFull;

  /**
   * include header in export?
   */
  private boolean header;

  /**
   * decorate export?
   */
  private boolean decorated;

  /**
   * @see org.displaytag.export.ExportView#setParameters(org.displaytag.model.TableModel, boolean,
   *      boolean, boolean)
   */
  public void setParameters( TableModel tableModel, boolean exportFullList, boolean includeHeader, boolean decorateValues )
  {
    super.setParameters( tableModel, exportFullList, includeHeader, decorateValues );
    this.model = tableModel;
    this.exportFull = exportFullList;
    this.header = includeHeader;
    this.decorated = decorateValues;
  }

  /**
   * @see org.displaytag.export.TextExportView#doExport(java.io.Writer)
   */
  public void doExport( Writer out ) throws IOException, JspException
  {
    super.doExport( out );
    // note finishRow will be called only once
    if ( this.model.getTableDecorator() != null )
    {
      String stringFinishRow = this.model.getTableDecorator().finishRow();
      write( out, stringFinishRow );
    }

  }

  /**
   * Write a String, checking for nulls value.
   * 
   * @param out output writer
   * @param string String to be written
   * @throws IOException thrown by out.write
   */
  private void write( Writer out, String string ) throws IOException
  {
    if ( string != null )
    {
      out.write( string );
    }
  }

  /**
   * Overridden from
   * 
   * @see org.displaytag.export.CsvView#escapeColumnValue(java.lang.Object) Escape certain values
   * that are not permitted in csv cells.
   * @param rawValue
   * @return returnString
   */

  // Introduced for BugFix#17702
  protected String escapeColumnValue( Object rawValue )
  {
    String stringValue = StringUtils.trim( rawValue.toString() );

    if ( stringValue == null )
    {
      return null;
    }

    // unescape so that \n gets back to newline
    stringValue = StringUtils.replace( StringUtils.trim( stringValue ), "<br/>", " " );
    stringValue = StringUtils.replace( StringUtils.trim( stringValue ), "<br />", " " );
    stringValue = StringUtils.replace( StringUtils.trim( stringValue ), "<br>", " " );
    stringValue = "\"" + //$NON-NLS-1$
        StringUtils.replace( stringValue, "\"", "\\\"" ) + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    // escape the String to get the tabs, returns, newline explicit as \t \r \n
    stringValue = StringEscapeUtils.escapeJava( StringUtils.trimToEmpty( stringValue ) );
    // remove tabs, insert four whitespaces instead
    stringValue = StringUtils.replace( StringUtils.trim( stringValue ), "\\t", "    " );
    // remove the return, only newline valid in excel
    stringValue = StringUtils.replace( StringUtils.trim( stringValue ), "\\r", " " );
    // remove the new Line
    stringValue = StringUtils.replace( StringUtils.trim( stringValue ), "\\n", " " );
    // remove the &nbsp, insert whitespace instead // To fix 20801
    stringValue = StringUtils.replace( StringUtils.trim( stringValue ), "&nbsp;", "  " );
    stringValue = StringUtils.replace( StringUtils.trim( stringValue ), "&#034;", "\"" );
    stringValue = StringEscapeUtils.unescapeJava( stringValue );

    return stringValue;
  }

}

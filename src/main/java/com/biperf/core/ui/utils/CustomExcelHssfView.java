/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/utils/CustomExcelHssfView.java,v $
 */

package com.biperf.core.ui.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.displaytag.decorator.hssf.DecoratesHssf;
import org.displaytag.export.excel.ExcelHssfView;
import org.displaytag.model.Column;
import org.displaytag.model.ColumnIterator;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;

import com.biperf.core.exception.BeaconRuntimeException;

/**
 * CustomExcelHssfView.
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
public class CustomExcelHssfView extends ExcelHssfView
{
  /**
   * TableModel to render.
   */
  /**
   *
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
   * Generated sheet.
   */
  private HSSFSheet sheet;

  /**
   * logger.
   */
  private static Log log = LogFactory.getLog( CustomPdfView.class );

  /**
   * @see org.displaytag.export.ExportView#setParameters(TableModel, boolean, boolean, boolean)
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
   * @return "application/vnd.ms-excel"
   * @see org.displaytag.export.BaseExportView#getMimeType()
   */
  public String getMimeType()
  {
    return "application/vnd.ms-excel"; //$NON-NLS-1$
  }

  /**
   * @see org.displaytag.export.BinaryExportView#doExport(OutputStream)
   */
  public void doExport( OutputStream out ) throws JspException
  {
    try
    {
      HSSFWorkbook wb = new HSSFWorkbook();
      sheet = wb.createSheet( "-" );

      int rowNum = 0;
      int colNum = 0;

      if ( this.header )
      {
        // Create an header row
        HSSFRow xlsRow = sheet.createRow( rowNum++ );

        HSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFillPattern( HSSFCellStyle.FINE_DOTS );
        headerStyle.setFillBackgroundColor( HSSFColor.BLUE_GREY.index );
        HSSFFont bold = wb.createFont();
        bold.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );
        bold.setColor( HSSFColor.WHITE.index );
        headerStyle.setFont( bold );

        Iterator iterator = this.model.getHeaderCellList().iterator();

        while ( iterator.hasNext() )
        {
          HeaderCell headerCell = (HeaderCell)iterator.next();

          String columnHeader = headerCell.getTitle();

          if ( columnHeader == null )
          {
            columnHeader = StringUtils.capitalize( headerCell.getBeanPropertyName() );
          }

          HSSFCell cell = xlsRow.createCell( colNum++ );
          // cell.setEncoding( HSSFCell.ENCODING_UTF_16 );
          cell.setCellValue( columnHeader );
          cell.setCellStyle( headerStyle );

        }
      }

      // get the correct iterator (full or partial list according to the exportFull field)
      RowIterator rowIterator = this.model.getRowIterator( this.exportFull );
      // iterator on rows

      while ( rowIterator.hasNext() )
      {
        Row row = rowIterator.next();
        HSSFRow xlsRow = sheet.createRow( rowNum++ );
        colNum = 0;

        // iterator on columns
        ColumnIterator columnIterator = row.getColumnIterator( this.model.getHeaderCellList() );

        while ( columnIterator.hasNext() )
        {
          Column column = columnIterator.nextColumn();

          // Get the value to be displayed for the column
          Object value = column.getValue( this.decorated );

          HSSFCell cell = xlsRow.createCell( colNum++ );
          // cell.setEncoding( HSSFCell.ENCODING_UTF_16 );

          if ( value instanceof Number )
          {
            Number num = (Number)value;
            cell.setCellValue( num.doubleValue() );
          }
          else if ( value instanceof Date )
          {
            cell.setCellValue( (Date)value );
          }
          else if ( value instanceof Calendar )
          {
            cell.setCellValue( (Calendar)value );
          }
          else
          {
            cell.setCellValue( escapeColumnValue( value ) );
          }

        }
      }
      callTableDecorator( sheet );
      wb.write( out );
    }
    catch( Exception e )
    {
      throw new JspException( e );
    }
    export( out );

  }

  /**
   * The overall PDF table generator.
   * 
   * @param sheet
   */
  protected void callTableDecorator( HSSFSheet sheet )
  {
    if ( this.model.getTableDecorator() != null )
    {
      try
      {
        DecoratesHssf decorator = (DecoratesHssf)model.getTableDecorator();
        decorator.setSheet( sheet );
        this.model.getTableDecorator().finishRow();
      }
      catch( Exception e )
      {
        log.warn( "The finishRow could not be called due to " + e.getMessage() );
      }
    }
  }

  /**
   * Overridden from
   * 
   * @see org.displaytag.export.CsvView#escapeColumnValue(java.lang.Object) Escape certain values
   *      that are not permitted in csv cells.
   * @param rawValue
   * @return returnString
   */

  // Introduced for BugFix#17702
  protected String escapeColumnValue( Object rawValue )
  {
    if ( rawValue == null )
    {
      return null;
    }

    String returnString = ObjectUtils.toString( rawValue );
    // unescape so that \n gets back to newline
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "<br/>", " " );
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "<br />", " " );
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "<br>", " " );
    // escape the String to get the tabs, returns, newline explicit as \t \r \n
    returnString = StringEscapeUtils.escapeJava( StringUtils.trimToEmpty( returnString ) );
    // remove tabs, insert four whitespaces instead
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "\\t", "    " );
    // remove the return, only newline valid in excel
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "\\r", " " );
    // remove the new Line
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "\\n", " " );
    // remove the &nbsp, insert whitespace instead // To fix 20801
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "&nbsp;", "  " );
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "&#034;", "\"" );
    // unescape so that \n gets back to newline
    returnString = StringEscapeUtils.unescapeJava( returnString );

    return returnString;
  }

  private void export( OutputStream out )
  {
    Workbook workbook = null;
    try
    {
      workbook = new XSSFWorkbook();
      XSSFSheet sheet = (XSSFSheet)workbook.createSheet( "-" );

      int rowNum = 0;
      int colNum = 0;

      if ( this.header )
      {
        // Create an header row
        XSSFRow xlsRow = sheet.createRow( rowNum );

        XSSFCellStyle headerStyle = (XSSFCellStyle)workbook.createCellStyle();
        headerStyle.setFillPattern( FillPatternType.FINE_DOTS );
        headerStyle.setFillBackgroundColor( HSSFColor.BLUE_GREY.index );
        XSSFFont bold = (XSSFFont)workbook.createFont();
        bold.setBold( Boolean.TRUE );
        bold.setColor( HSSFColor.WHITE.index );
        headerStyle.setFont( bold );

        Iterator iterator = this.model.getHeaderCellList().iterator();

        while ( iterator.hasNext() )
        {
          HeaderCell headerCell = (HeaderCell)iterator.next();

          String columnHeader = headerCell.getTitle();

          if ( columnHeader == null )
          {
            columnHeader = StringUtils.capitalize( headerCell.getBeanPropertyName() );
          }

          XSSFCell cell = xlsRow.createCell( colNum );
          cell.setCellValue( new XSSFRichTextString( columnHeader ) );
          cell.setCellStyle( headerStyle );
        }
      }

      // get the correct iterator (full or partial list according to the exportFull field)
      RowIterator rowIterator = this.model.getRowIterator( this.exportFull );
      // iterator on rows

      while ( rowIterator.hasNext() )
      {
        Row row = rowIterator.next();
        XSSFRow xlsRow = sheet.createRow( rowNum );
        colNum = 0;

        // iterator on columns
        ColumnIterator columnIterator = row.getColumnIterator( this.model.getHeaderCellList() );

        while ( columnIterator.hasNext() )
        {
          Column column = columnIterator.nextColumn();

          // Get the value to be displayed for the column
          Object value = column.getValue( this.decorated );

          XSSFCell cell = xlsRow.createCell( colNum++ );

          writeCell( value, cell );
        }
      }

      // adjust the column widths
      int colCount = 0;
      while ( colCount <= colNum )
      {
        sheet.autoSizeColumn( (short)colCount++ );
      }

      workbook.write( out );

    }
    catch( Exception e )
    {
      throw new BeaconRuntimeException( e );
    }
    finally
    {
      if ( workbook != null )
      {
        try
        {
          workbook.close();
        }
        catch( IOException e )
        {
          log.warn( "Unable to close workbook " + e );
        }
      }
    }
  }

  protected void writeCell( Object value, XSSFCell cell )
  {
    if ( value instanceof Number )
    {
      Number num = (Number)value;
      cell.setCellValue( num.doubleValue() );
    }
    else if ( value instanceof Date )
    {
      cell.setCellValue( (Date)value );
    }
    else if ( value instanceof Calendar )
    {
      cell.setCellValue( (Calendar)value );
    }
    else
    {
      cell.setCellValue( new XSSFRichTextString( escapeColumnValue( value ) ) );
    }
  }
}

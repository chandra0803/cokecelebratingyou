/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/utils/CustomPdfView.java,v $
 */

package com.biperf.core.ui.utils;

import java.awt.Color;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.export.BinaryExportView;
import org.displaytag.export.PdfView;
import org.displaytag.model.Column;
import org.displaytag.model.ColumnIterator;
import org.displaytag.model.HeaderCell;
import org.displaytag.model.Row;
import org.displaytag.model.RowIterator;
import org.displaytag.model.TableModel;
import org.displaytag.render.ItextTableWriter.ItextDecorator;
import org.displaytag.util.TagConstants;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * CustomPdfView.
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
 * <td>May 4, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CustomPdfView extends PdfView implements BinaryExportView
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
   * This is the table, added as an Element to the PDF document. It contains all the data, needed to represent the
   * visible table into the PDF
   */
  private Table tablePDF;

  /**
   * The default font used in the document.
   */
  private Font smallFont;

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
   * @see org.displaytag.export.BinaryExportView#doExport(OutputStream)
   */
  public void doExport( OutputStream out ) throws JspException
  {
    try
    {
      // Initialize the table with the appropriate number of columns
      initTable();

      // Initialize the Document and register it with PdfWriter listener and the OutputStream
      Document document = new Document( PageSize.A4.rotate(), 60, 60, 40, 40 );
      document.addCreationDate();
      HeaderFooter footer = new HeaderFooter( new Phrase( TagConstants.EMPTY_STRING, smallFont ), true );
      footer.setBorder( Rectangle.NO_BORDER );
      footer.setAlignment( Element.ALIGN_CENTER );

      PdfWriter.getInstance( document, out );

      // Fill the virtual PDF table with the necessary data
      generatePDFTable();
      callTableDecorator();
      document.open();
      document.setFooter( footer );
      document.add( this.tablePDF );
      document.close();

    }
    catch( Exception e )
    {
      throw new JspException( e );
    }
  }

  /**
   * Initialize the main info holder table.
   * @throws BadElementException for errors during table initialization
   */
  protected void initTable() throws BadElementException
  {
    tablePDF = new Table( this.model.getNumberOfColumns() );
    // tablePDF.setDefaultVerticalAlignment(Element.ALIGN_TOP);
    tablePDF.setAlignment( Element.ALIGN_TOP );
    tablePDF.setCellsFitPage( true );
    tablePDF.setWidth( 100 );

    tablePDF.setPadding( 2 );
    tablePDF.setSpacing( 0 );

    smallFont = FontFactory.getFont( FontFactory.HELVETICA, 7, Font.NORMAL, new Color( 0, 0, 0 ) );

  }

  /**
   * The overall PDF table generator.
   * @throws JspException for errors during value retrieving from the table model
   * @throws BadElementException IText exception
   */
  protected void generatePDFTable() throws JspException, BadElementException
  {
    if ( this.header )
    {
      generateHeaders();
    }
    tablePDF.endHeaders();
    generateRows();
  }

  /**
   * Generates the header cells, which persist on every page of the PDF document.
   * @throws BadElementException IText exception
   */
  protected void generateHeaders() throws BadElementException
  {
    Iterator iterator = this.model.getHeaderCellList().iterator();

    while ( iterator.hasNext() )
    {
      HeaderCell headerCell = (HeaderCell)iterator.next();

      String columnHeader = headerCell.getTitle();

      if ( columnHeader == null )
      {
        columnHeader = StringUtils.capitalize( headerCell.getBeanPropertyName() );
      }

      Cell hdrCell = getCell( columnHeader );
      hdrCell.setGrayFill( 0.9f );
      hdrCell.setHeader( true );
      tablePDF.addCell( hdrCell );

    }
  }

  /**
   * Generates all the row cells.
   * @throws JspException for errors during value retrieving from the table model
   * @throws BadElementException errors while generating content
   */
  protected void generateRows() throws JspException, BadElementException
  {
    // get the correct iterator (full or partial list according to the exportFull field)
    RowIterator rowIterator = this.model.getRowIterator( this.exportFull );
    // iterator on rows
    while ( rowIterator.hasNext() )
    {
      Row row = rowIterator.next();

      // iterator on columns
      ColumnIterator columnIterator = row.getColumnIterator( this.model.getHeaderCellList() );

      while ( columnIterator.hasNext() )
      {
        Column column = columnIterator.nextColumn();

        // Get the value to be displayed for the column
        Object value = column.getValue( this.decorated );

        Cell cell = getCell( escapeColumnValue( value ) );
        tablePDF.addCell( cell );
      }
    }
  }

  /**
   * Returns a formatted cell for the given value.
   * @param value cell value
   * @return Cell
   * @throws BadElementException errors while generating content
   */
  private Cell getCell( String value ) throws BadElementException
  {
    Cell cell = new Cell( new Chunk( StringUtils.trimToEmpty( value ), smallFont ) );
    cell.setVerticalAlignment( Element.ALIGN_TOP );
    cell.setLeading( 8 );
    return cell;
  }

  /**
   * The overall PDF table generator.
   */
  protected void callTableDecorator()
  {
    if ( this.model.getTableDecorator() != null )
    {
      try
      {
        ItextDecorator decorator = (ItextDecorator)model.getTableDecorator();
        decorator.setTable( this.tablePDF );
        decorator.setFont( this.smallFont );
        this.model.getTableDecorator().finishRow();
      }
      catch( Exception e )
      {
        log.warn( "The finishRow could not be called due to " + e.getMessage() );
      }
    }
  }

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
    // remove the &nbsp, insert whitespace instead //To fix 20801
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "&nbsp;", "  " );
    returnString = StringUtils.replace( StringUtils.trim( returnString ), "&#034;", "\"" );
    returnString = StringEscapeUtils.unescapeJava( returnString );

    return returnString;
  }
}

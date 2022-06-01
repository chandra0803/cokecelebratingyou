
package com.biperf.core.utils.fileprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.biperf.core.service.util.StringPattern;
import com.biperf.core.service.util.WebDavConfiguration;
import com.biperf.core.value.fileprocessing.FileDetailInfo;
import com.biperf.core.value.fileprocessing.FileDetailInfoOutputOrderComparator;
import com.biperf.core.value.fileprocessing.FileInfo;
import com.biperf.core.value.fileprocessing.OperationInfo;
import com.biperf.core.value.fileprocessing.OperationResultInfo;
import com.biperf.core.value.fileprocessing.WebDavFileWriteInfo;

public class FileOperation implements Operation
{
  public static final String CLASS_NAME = "FileOperation";

  protected static final Log logger = LogFactory.getLog( FileOperation.class );

  protected static final String ERROR_SEPERATOR = "~~";
  protected static final String ERROR_MSG_SEPERATOR = "^";

  private static final DecimalFormat NUMBER_FMT = new DecimalFormat( "0" );
  private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

  public static final String INPUT_FILE_TYPE_FIXED = "Text Fixed Field";
  public static final String INPUT_FILE_TYPE_COMMA = "Comma Seperated Text";
  public static final String INPUT_FILE_TYPE_PIPE = "Pipe Seperated Text";
  public static final String INPUT_FILE_TYPE_TAB = "Tab Seperated Text";
  public static final String INPUT_FILE_TYPE_EXCEL = "Excel";
  public static final String INPUT_FILE_TYPE_DATABASE_DUMP = "Database Dump File";

  public static final String OUTPUT_FILE_TYPE_FIXED = "Text Fixed Field";
  public static final String OUTPUT_FILE_TYPE_COMMA = "Comma Seperated Text";
  public static final String OUTPUT_FILE_TYPE_PIPE = "Pipe Seperated Text";
  public static final String OUTPUT_FILE_TYPE_TAB = "Tab Seperated Text";
  public static final String OUTPUT_FILE_TYPE_DATABASE_DUMP = "Database Dump File Output";

  protected String newLine = System.getProperty( "line.separator" );
  protected int numberOfLinesToSkip = 0;
  protected int numberOfFooterLinesToSkip = 0;
  protected boolean removeDoubleQuote = false;
  protected boolean removeLineIfEmpty = false;
  private int goodLineNumber = 0;

  public String getGroup()
  {
    return null;
  }

  /**
   * This class returns the seperator for different format type
   */
  public String seperator( String formatType )
  {
    if ( formatType == null )
    {
      return "";
    }

    if ( formatType.equals( OUTPUT_FILE_TYPE_COMMA ) )
    {
      return ",";
    }
    else if ( formatType.equals( OUTPUT_FILE_TYPE_PIPE ) )
    {
      return "|";
    }
    else if ( formatType.equals( OUTPUT_FILE_TYPE_TAB ) )
    {
      return "\t";
    }

    return "";
  }

  public void perform( File inputFile, File outputFolder, String prefix, FileInfo fileInfo, OperationResultInfo operationResultInfo ) throws Exception
  {
  }

  public void perform( InputStream inputFile, File outputFolder, String prefix, FileInfo fileInfo, OperationResultInfo operationResultInfo, String userId ) throws Exception
  {
    String t_outputFileName = operationResultInfo.getOutputFileName();
    // Now empty the output file name in result info so we can track if both
    // bad and good file names are written
    operationResultInfo.setOutputFileName( "" );
    if ( t_outputFileName != null )
    {
      WebDavFileWriteInfo fileWriterInfo = new WebDavFileWriteInfo( outputFolder, t_outputFileName, operationResultInfo );
      String outUrl = fileInfo.getInputURL();

      WebDavConfiguration outConfiguration = new WebDavConfiguration( outUrl );
      fileWriterInfo.setOutConfiguration( outConfiguration );
      WebDavConfiguration configuration = new WebDavConfiguration( fileInfo.getInputURL() );
      fileWriterInfo.setConfiguration( configuration );
      fileWriterInfo.setFileInfo( fileInfo );
      fileWriterInfo.setUserId( userId );
      fileWriterInfo.setInputFileName( operationResultInfo.getInputFileName() );

      if ( fileInfo == null || fileInfo.getFileFormat() == null )
      {
        fileWriterInfo.close();
      }
      operationResultInfo.setMaxErrorCount( fileInfo.getErrorCount() );
      if ( fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_EXCEL ) )
      {
        handleExcelFile( inputFile, fileWriterInfo, fileInfo, operationResultInfo );
      }
      else if ( fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_COMMA ) || fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_PIPE ) || fileInfo.getFileFormat().equals( INPUT_FILE_TYPE_TAB ) )
      {
        handleCSVFile( inputFile, fileWriterInfo, fileInfo, operationResultInfo );
      }

      fileWriterInfo.close();
    }
    return;
  }

  public void handleExcelFile( InputStream inputFile, WebDavFileWriteInfo fileWriterInfo, FileInfo fileInfo, OperationResultInfo operationResultInfo ) throws IOException, Exception
  {

    int errorCount = 0;
    // POIFSFileSystem poifs = new POIFSFileSystem(inputFile);
    // get the Workbook (excel part) stream in a InputStream

    try
    {

      Workbook workbook = WorkbookFactory.create( inputFile );
      Sheet sheet = workbook.getSheetAt( 0 );
      operationResultInfo.setRowCount( 0 );

      // This array list is kind of queue that will hold up n number of lines
      // that needs
      // to be skipped. Like, when it is reading 3 line, it will write 3- n th
      // line where
      // n is number of footer lines to be skipped. So, when we reach last
      // line, it will
      // write last line - nth line and skipp all other lines.
      ArrayList<ArrayList<String>> skipFooterList = new ArrayList<ArrayList<String>>();
      int totalRows = sheet.getLastRowNum() + 1;

      for ( int rowIndex = getNumberOfLinesToSkip(); rowIndex < totalRows; rowIndex++ )
      {
        operationResultInfo.setRowCount( operationResultInfo.getRowCount() + 1 );
        ArrayList<String> outputLine = new ArrayList<String>();
        // Read the rows
        Row row = sheet.getRow( rowIndex );
        int totalCols = row == null ? 0 : row.getLastCellNum();
        for ( int col = 0; col < totalCols; col++ )
        {
          Cell cell = row.getCell( col );
          // If last row, calculate the column count
          if ( rowIndex == totalRows - 1 )
          {
            operationResultInfo.setColumnCount( operationResultInfo.getColumnCount() + 1 );
          }
          // TODO: Column formatting/Detail rule need to be inserted
          // here..
          String token = "";

          if ( cell != null && cell.getCellTypeEnum().equals( CellType.NUMERIC ) )
          {
            if ( HSSFDateUtil.isCellDateFormatted( row.getCell( col ) ) )
            {
              if ( cell.toString().trim().length() > 0 )
              {
                Date datec2 = cell.getDateCellValue();

                // TimeZone gmtZone =
                // TimeZone.getTimeZone("GMT");

                FileDetailInfo t_fileDetailInfo = fileInfo.getFileDetails( col + 1 );

                if ( t_fileDetailInfo != null && t_fileDetailInfo.getColumnAddlQualifier() != null && t_fileDetailInfo.getColumnAddlQualifier().trim().length() > 0 )
                {
                  SimpleDateFormat t_simpleDF = new SimpleDateFormat( t_fileDetailInfo.getColumnAddlQualifier() );
                  // t_simpleDF.setTimeZone(gmtZone);
                  token = t_simpleDF.format( datec2 );
                }
                else
                {
                  SimpleDateFormat t_simpleDF = new SimpleDateFormat( DEFAULT_DATE_FORMAT );
                  // t_simpleDF.setTimeZone(gmtZone);
                  token = t_simpleDF.format( datec2 );
                }
              }
            }
            else
            {
              double value = cell.getNumericCellValue();
              HSSFDataFormatter dataFormat = new HSSFDataFormatter();
              String stringValue = dataFormat.formatCellValue( cell );
              StringBuffer strBuff = new StringBuffer();
              char c;
              for ( int i = 0; i < stringValue.length(); i++ )
              {
                c = stringValue.charAt( i );
                if ( Character.isDigit( c ) || c == '.' )
                {
                  strBuff.append( c );
                }
                else if ( i == 0 && c == '-' )
                {
                  strBuff.append( c );
                }
              }
              stringValue = strBuff.toString();
              long round = (long)value;
              if ( value - round != 0 )
              {
                token = "" + stringValue;
              }
              else
              {
                token = "" + NUMBER_FMT.format( Double.valueOf( stringValue ) );
              }
            }
          }
          else if ( cell != null && cell.getCellTypeEnum().equals( CellType.BOOLEAN ) )
          {
            token = "" + cell.getBooleanCellValue();
          }
          else if ( cell != null )
          {
            token = cell.getRichStringCellValue().getString();

          }
          else
          {
            token = "";
          }

          outputLine.add( token );

        } // end of columns

        // Implement Queue
        skipFooterList.add( outputLine );

        if ( skipFooterList.size() > numberOfFooterLinesToSkip )
        {
          outputLine = skipFooterList.remove( 0 );

          // Do File Level Operations on Column
          if ( fileOperations( outputLine ) )
          {
            boolean isError = false;
            StringBuffer errorMsg = new StringBuffer();
            for ( int col = 0; col < outputLine.size() && fileInfo.getFileDetails().size() > 0; col++ )
            {
              String t_token = (String)outputLine.get( col );
              FileDetailInfo t_columnDetail = fileInfo.getFileDetails( col + 1 );
              if ( t_columnDetail != null )
              {
                t_columnDetail.setErrorMessage( new ArrayList<String>() );

              }

              t_token = performColumnOperation( t_token, t_columnDetail, operationResultInfo );

              validateDataType( t_token, t_columnDetail, operationResultInfo );

              if ( t_columnDetail != null && t_columnDetail.getErrorMessageSize() > 0 )
              {
                isError = true;
                logger.error( "Error occurred!! --Field Name - " + t_columnDetail.getColumnName() + " Field Value -" + t_columnDetail.getValue() );

                for ( int i = 0; i < t_columnDetail.getErrorMessageSize(); i++ )
                {
                  errorMsg.append( (String)t_columnDetail.getErrorMessage().get( i ) + ERROR_SEPERATOR );
                  logger.error( (String)t_columnDetail.getErrorMessage().get( i ) + ERROR_SEPERATOR );
                }
              }
              outputLine.set( col, t_token );

            }
            if ( isError )
            {
              errorCount++;
            }
            else
            {
              operationResultInfo.setValidOutputRecCount( operationResultInfo.getValidOutputRecCount() + 1 );
            }

            writeToFile( fileWriterInfo, fileInfo, rowIndex + 1, outputLine, isError, errorCount, errorMsg.toString() );
            operationResultInfo.setRowCountBad( errorCount );
            if ( errorCount > fileInfo.getErrorCount() )
            {
              rowIndex = totalRows;
              continue;
            }
          }
        }

      }
      inputFile.close();
    }
    catch( Exception exception )
    {
      logger.error( "Error while reading Excel file while processing [good row count=" + operationResultInfo.getRowCount() + ", bad row count=" + operationResultInfo.getRowCountBad() + ", column="
          + operationResultInfo.getColumnCount() + "]", exception );
      // exception.printStackTrace();

    }
    finally
    {
      if ( inputFile != null )
      {
        inputFile.close();
      }
    }

  }

  public void handleCSVFile( InputStream inputFile, WebDavFileWriteInfo fileWriterInfo, FileInfo fileInfo, OperationResultInfo operationResultInfo ) throws Exception
  {
    int errorCount = 0;

    // This array list is kind of queue that will hold up n number of lines
    // that needs
    // to be skipped. Like, when it is reading 3 line, it will write 3- n th
    // line where
    // n is number of footer lines to be skipped. So, when we reach last
    // line, it will
    // write last line - nth line and skipp all other lines.
    ArrayList<ArrayList<String>> skipFooterList = new ArrayList<ArrayList<String>>();

    BufferedReader inputFileReader = new BufferedReader( new InputStreamReader( inputFile, "UTF-8" ) );
    String line = inputFileReader.readLine();
    int counter = 1;

    while ( line != null )
    {
      String nextToken = "";
      if ( counter > getNumberOfLinesToSkip() )
      {
        operationResultInfo.setRowCount( operationResultInfo.getRowCount() + 1 );
        StringTokenizer stok = new StringTokenizer( line, seperator( fileInfo.getFileFormat() ), true );
        operationResultInfo.setColumnCount( 0 );

        ArrayList<String> outputLine = new ArrayList<String>();

        boolean addOneMoreEmptyColumn = false;
        // Read the columns
        while ( stok.hasMoreTokens() || addOneMoreEmptyColumn )
        {
          String token = null;
          if ( addOneMoreEmptyColumn )
          {
            addOneMoreEmptyColumn = false;
            token = "";
          }
          else
          {
            token = (String)stok.nextToken();
          }
          if ( token.equals( seperator( fileInfo.getFileFormat() ) ) )
          {
            token = "";
            if ( !stok.hasMoreTokens() )
            {
              addOneMoreEmptyColumn = true;
            }
          }
          else
          {
            // Skip the token
            if ( stok.hasMoreTokens() )
            {
              nextToken = stok.nextToken();
              if ( nextToken.equals( seperator( fileInfo.getFileFormat() ) ) )
              {
                if ( !stok.hasMoreTokens() )
                {
                  addOneMoreEmptyColumn = true;
                }
              }
            }
          }
          operationResultInfo.setColumnCount( operationResultInfo.getColumnCount() + 1 );
          outputLine.add( token );
        }

        // Implement Queue
        skipFooterList.add( outputLine );
        if ( skipFooterList.size() > numberOfFooterLinesToSkip )
        {
          outputLine = skipFooterList.remove( 0 );

          // Do File Level Operations on Column
          if ( fileOperations( outputLine ) )
          {
            boolean isError = false;
            StringBuffer errorMsg = new StringBuffer();
            for ( int col = 0; col < outputLine.size() && fileInfo.getFileDetails().size() > 0; col++ )
            {
              String token = (String)outputLine.get( col );
              FileDetailInfo columnDetail = fileInfo.getFileDetails( col + 1 );
              if ( columnDetail != null )
              {
                columnDetail.setErrorMessage( new ArrayList<String>() );
              }

              token = performColumnOperation( token, columnDetail, operationResultInfo );
              validateDataType( token, columnDetail, operationResultInfo );

              // t_token = performColumnOperation(t_token,
              // t_columnDetail, operationResultInfo);
              if ( columnDetail != null && columnDetail.getErrorMessageSize() > 0 )
              {
                isError = true;
                for ( int i = 0; i < columnDetail.getErrorMessageSize(); i++ )
                {
                  errorMsg.append( (String)columnDetail.getErrorMessage().get( i ) + ERROR_SEPERATOR );
                }
              }
              outputLine.set( col, token );
            }
            if ( isError )
            {
              errorCount++;
            }
            else
            {
              operationResultInfo.setValidOutputRecCount( operationResultInfo.getValidOutputRecCount() + 1 );
            }

            writeToFile( fileWriterInfo, fileInfo, counter, outputLine, isError, errorCount, errorMsg.toString() );
            operationResultInfo.setRowCountBad( errorCount );
          }
        }
      }
      line = inputFileReader.readLine();
      counter++;
    } // while t_line != null
    inputFileReader.close();
  }

  public void writeToFile( WebDavFileWriteInfo fileWriterInfo, FileInfo fileInfo, int row, ArrayList<String> outputLine, boolean error, int errorCount, String errorMsg ) throws IOException
  {
    String t_seperator = seperator( fileInfo.getOutFileFormat() );
    // If it is second line write a new line before starting
    if ( error )
    {
      if ( errorCount > 1 )
      {
        fileWriterInfo.write( error, getNewLine() );
      }
    }
    else
    {
      if ( this.goodLineNumber > 0 )
      {
        fileWriterInfo.write( error, getNewLine() );
      }
      this.goodLineNumber++;
    }

    if ( fileInfo.getFileDetails() != null && fileInfo.getFileDetails().size() > 0 )
    {
      mergeColumnValues( outputLine, fileInfo.getFileDetails() );

      Collections.sort( fileInfo.getFileDetails(), new FileDetailInfoOutputOrderComparator() );
      for ( int outputCol = 0; outputCol < fileInfo.getFileDetails().size(); outputCol++ )
      {
        if ( ( (FileDetailInfo)fileInfo.getFileDetails().get( outputCol ) ).getOutputOrder() > 0 )
        {
          fileWriterInfo.write( error, getValue( (FileDetailInfo)fileInfo.getFileDetails().get( outputCol ), fileInfo.getOutFileFormat() ) );
        }
        if ( outputCol < fileInfo.getFileDetails().size() - 1 )
        {
          fileWriterInfo.write( error, t_seperator );
        }
      }
    }
    else
    {
      for ( int outputCol = 0; outputCol < outputLine.size(); outputCol++ )
      {
        fileWriterInfo.write( error, (String)outputLine.get( outputCol ) );
        if ( outputCol < outputLine.size() - 1 )
        {
          fileWriterInfo.write( error, t_seperator );
        }
      }
    }
    if ( error )
    {
      fileWriterInfo.write( error, t_seperator );
      if ( errorMsg != null && errorMsg.length() > 0 )
      {
        if ( errorMsg.endsWith( ERROR_SEPERATOR ) )
        {
          errorMsg = errorMsg.substring( 0, errorMsg.lastIndexOf( ERROR_SEPERATOR ) );
        }
        fileWriterInfo.write( error, errorMsg );
      }
    }
  }

  public static String pad( int value, int length, char padLetter )
  {

    StringBuffer t_strValue = new StringBuffer( "" + value );
    int t_originalLength = t_strValue.length();
    if ( length <= t_originalLength )
    {
      return t_strValue.toString();
    }
    int numOfPads = length - t_originalLength;
    for ( int i = 0; i < numOfPads; i++ )
    {
      t_strValue.insert( 0, padLetter );
    }

    return t_strValue.toString();
  }

  public static String pad( String value, int length, char padLetter )
  {
    if ( value == null )
    {
      value = "";
    }
    StringBuffer t_strValue = new StringBuffer( "" + value );
    int t_originalLength = t_strValue.length();
    if ( length <= t_originalLength )
    {
      return t_strValue.toString();
    }
    for ( int i = t_originalLength; i < length; i++ )
    {
      t_strValue.append( padLetter );
    }
    return t_strValue.toString();
  }

  public static String removeNonNum( String value )
  {
    StringBuffer newValue = new StringBuffer();
    int size = value.length();
    for ( int idx = 0; idx < size; idx++ )
    {
      char c = value.charAt( idx );
      if ( Character.isDigit( c ) )
      {
        newValue.append( c );
      }
    }
    return newValue.toString();
  }

  /**
   * Getter for property numberOfLinesToSkip.
   * 
   * @return Value of property numberOfLinesToSkip.
   */
  public int getNumberOfLinesToSkip()
  {
    return numberOfLinesToSkip;
  }

  /**
   * Setter for property numberOfLinesToSkip.
   * 
   * @param numberOfLinesToSkip
   *            New value of property numberOfLinesToSkip.
   */
  public void setNumberOfLinesToSkip( int numberOfLinesToSkip )
  {
    this.numberOfLinesToSkip = numberOfLinesToSkip;
  }

  /**
   * Getter for property newLine.
   * 
   * @return Value of property newLine.
   */
  public java.lang.String getNewLine()
  {
    return newLine;
  }

  /**
   * Setter for property newLine.
   * 
   * @param newLine
   *            New value of property newLine.
   */
  public void setNewLine( java.lang.String newLine )
  {
    this.newLine = newLine;
  }

  /**
   * This method sets the properties of File Operations so that it can do all
   * the operations.
   */
  public void setFileOperationProperty( OperationInfo operationInfo )
  {
    if ( operationInfo.getOperationText().equals( FileLevelOperation.UNIX_FORMAT.toString() ) )
    {
      setNewLine( "\n" );
    }
    else if ( operationInfo.getOperationText().equals( FileLevelOperation.WINDOWS_FORMAT.toString() ) )
    {
      setNewLine( "\r\n" );
    }
    else if ( operationInfo.getOperationText().equals( FileLevelOperation.REMOVE_DOUBLEQUOTES.toString() ) )
    {
      setRemoveDoubleQuote( true );
    }
    else if ( operationInfo.getOperationText().equals( FileLevelOperation.REMOVE_EMPTY_LINES.toString() ) )
    {
      setRemoveLineIfEmpty( true );
    }
    else if ( operationInfo.getOperationText().equals( FileLevelOperation.REMOVE_HEADER.toString() ) )
    {
      int numOfLines = 0;
      try
      {
        numOfLines = Integer.parseInt( operationInfo.getValue() );

      }
      catch( NumberFormatException nfe )
      {
        logger.debug( CLASS_NAME );
      }
      setNumberOfLinesToSkip( numOfLines );
    }
    else if ( operationInfo.getOperationText().equals( FileLevelOperation.REMOVE_TRAILER.toString() ) )
    {
      int numOfLines = 0;
      try
      {
        numOfLines = Integer.parseInt( operationInfo.getValue() );
      }
      catch( NumberFormatException nfe )
      {
        logger.debug( CLASS_NAME + " File Operation \"" + REMOVE_TRAILER + "\" value is not a number" );
      }
      setNumberOfFooterLinesToSkip( numOfLines );
    }
  }

  /**
   * This method sorts the source array list using information on filedetails
   * arraylist and returns the sorted arraylist.
   */
  public void mergeColumnValues( ArrayList<String> sourceList, ArrayList<FileDetailInfo> fileDetails )
  {
    // Do File Operations
    for ( int count = 0; count < fileDetails.size(); count++ )
    {
      FileDetailInfo fileDetailInfo = (FileDetailInfo)fileDetails.get( count );
      fileDetailInfo.setValue( "" );
    }
    if ( sourceList == null || sourceList.size() == 0 || fileDetails == null || fileDetails.size() == 0 )
    {
      return;
    }

    for ( int i = 0; i < sourceList.size(); i++ )
    {
      for ( int j = 0; j < fileDetails.size(); j++ )
      {
        FileDetailInfo t_fileDetailInfo = (FileDetailInfo)fileDetails.get( j );
        if ( t_fileDetailInfo.getInputOrder() == i + 1 )
        {
          t_fileDetailInfo.setValue( (String)sourceList.get( i ) );
        }
        if ( t_fileDetailInfo.getInputOrder() == 0 && t_fileDetailInfo.getOutputOrder() > 0 )
        {
          // Try to see if there is a insert column for this and add
          // it
          OperationInfo t_operationInfo = null;
          for ( int opCount = 0; opCount < t_fileDetailInfo.getFileDetailOperations().size(); opCount++ )
          {
            t_operationInfo = (OperationInfo)t_fileDetailInfo.getFileDetailOperations().get( opCount );
            if ( t_operationInfo.getOperationGroup().equals( "OPERATIONS" ) )
            {
              t_fileDetailInfo.setValue( t_operationInfo.getValue() );
            }
          }
        }
      }
    }
    return;
  }

  public boolean fileOperations( ArrayList<String> values )
  {
    if ( values == null )
    {
      return false;
    }
    ArrayList<String> modifiedValues = new ArrayList<String>();
    boolean isEmpty = true;
    for ( int count = 0; count < values.size(); count++ )
    {
      String value = (String)values.get( count );
      if ( removeDoubleQuote )
      {
        if ( value.startsWith( "\"" ) && value.endsWith( "\"" ) && value.length() >= 2 )
        {
          value = value.substring( 1 );
          value = value.substring( 0, value.length() - 1 );
        }
      }
      if ( value.length() > 0 )
      {
        isEmpty = false;
      }
      modifiedValues.add( value );
    }
    if ( removeLineIfEmpty && isEmpty )
    {
      return false;
    }
    values.clear();
    values.addAll( modifiedValues );
    return true;
  }

  /**
   * This method pads value with space if output file type is fixed length
   */
  public String getValue( FileDetailInfo fileDetailInfo, String formatType )
  {
    if ( formatType == null )
    {
      return "";
    }
    String t_value = fileDetailInfo.getValue();

    if ( formatType.equals( OUTPUT_FILE_TYPE_FIXED ) )
    {
      if ( t_value != null && t_value.length() > fileDetailInfo.getColumnLength() )
      {
        return t_value.substring( 0, fileDetailInfo.getColumnLength() );
      }
      else
      {
        if ( fileDetailInfo.getColumnDataType().equals( "Decimal" ) )
        {
          t_value = t_value.trim();
          StringBuffer t_pattern = new StringBuffer();
          for ( int i = 0; i < fileDetailInfo.getColumnLength(); i++ )
          {
            t_pattern.append( "0" );
          }
          if ( fileDetailInfo.getColumnPrecision() < fileDetailInfo.getColumnLength() )
          {
            t_pattern.setCharAt( fileDetailInfo.getColumnLength() - fileDetailInfo.getColumnPrecision() - 1, '.' );
          }
          DecimalFormat df = new DecimalFormat( t_pattern.toString() );
          try
          {
            BigDecimal t_doubleValue = new BigDecimal( t_value );
            return df.format( t_doubleValue );
          }
          catch( NumberFormatException nfe )
          {
            return pad( t_value, fileDetailInfo.getColumnLength(), ' ' );
          }

        }
        else if ( fileDetailInfo.getColumnDataType().equals( "Integer" ) )
        {
          try
          {

            return pad( Integer.parseInt( t_value.trim() ), fileDetailInfo.getColumnLength(), '0' );
          }
          catch( NumberFormatException nfe )
          {
            return pad( t_value, fileDetailInfo.getColumnLength(), ' ' );
          }
          // return pad(t_value,fileDetailInfo.getColumnLength(),
          // '0');
        }
        else
        {
          return pad( t_value, fileDetailInfo.getColumnLength(), ' ' );
        }

      }
    } // end OUTPUT_FILE_TYPE_FIXED

    if ( formatType.equals( OUTPUT_FILE_TYPE_COMMA ) )
    {

      t_value = t_value.trim();
      if ( fileDetailInfo.getColumnDataType().equals( "Text" ) )
      {
        return t_value.replaceAll( ",", "\",\"" );
        // return pad(t_value, fileDetailInfo.getColumnLength(), ' ');
      }
    }
    // if it is comma,tab,pipe
    // added for new data types
    if ( fileDetailInfo.getColumnDataType().equals( "Phone" ) )
    {
      if ( t_value != null )
      {
        t_value = removeNonNum( t_value );
        if ( t_value.length() > 10 )
        {
          t_value = t_value.substring( 0, 10 );
        }
      }
      return t_value;
    }
    else if ( fileDetailInfo.getColumnDataType().equals( "SSN" ) )
    {
      if ( t_value != null )
      {
        t_value = removeNonNum( t_value ); // mandy put back in 6/13
        StringBuffer zeroPaddedSSN = new StringBuffer( t_value );
        int firstIndexOfNumber = -1;
        int numberOfDigits = 0;
        for ( int i = 0; i < zeroPaddedSSN.length(); i++ )
        {
          if ( Character.isDigit( zeroPaddedSSN.charAt( i ) ) )
          {
            if ( firstIndexOfNumber < 0 )
            {
              firstIndexOfNumber = i;
            }
            numberOfDigits++;
          }
        }
        int numberOfZeros = 9 - numberOfDigits;
        if ( numberOfZeros == 1 )
        {
          zeroPaddedSSN.insert( firstIndexOfNumber, "0" );
        }
        else if ( numberOfZeros == 2 )
        {
          zeroPaddedSSN.insert( firstIndexOfNumber, "00" );
        }
        t_value = zeroPaddedSSN.toString();
        if ( t_value.length() <= 9 && formatType.equals( OUTPUT_FILE_TYPE_FIXED ) )
        {
          if ( t_value.length() >= 7 )
          {
            try
            {
              t_value = pad( Integer.parseInt( t_value ), 9, '0' );
            }
            catch( NumberFormatException nfe )
            {
              t_value = pad( t_value, 9, ' ' );
            }
          }
        }
        else if ( formatType.equals( OUTPUT_FILE_TYPE_FIXED ) )
        {
          t_value = t_value.substring( 0, 9 );
        }
        return t_value;
      }

    }
    else if ( fileDetailInfo.getColumnDataType().equals( "Zip" ) )
    {
      if ( t_value != null )
      {
        t_value = removeNonNum( t_value );
        if ( t_value.length() <= 5 )
        {
          if ( t_value.length() >= 3 )
          {
            try
            {
              t_value = pad( Integer.parseInt( t_value ), 5, '0' );
            }
            catch( NumberFormatException nfe )
            {
              t_value = pad( t_value, 5, ' ' );
            }
          }
        }
        else
        {
          t_value = t_value.substring( 0, 5 );
        }
        return t_value;
      }
    }
    else if ( fileDetailInfo.getColumnDataType().equals( "Zip_4" ) )
    {
      if ( t_value != null )
      {
        t_value = removeNonNum( t_value );
        if ( t_value.length() <= 9 )
        {
          if ( t_value.length() >= 3 )
          {
            try
            {
              t_value = pad( Integer.parseInt( t_value ), 9, '0' );
            }
            catch( NumberFormatException nfe )
            {
              t_value = pad( t_value, 9, ' ' );
            }
          }
        }
        else
        {
          t_value = t_value.substring( 0, 9 );
        }
        return t_value;
      }

    }
    else if ( fileDetailInfo.getColumnDataType().equals( "Integer" ) )
    {
      try
      {
        return "" + (int)Integer.parseInt( t_value );
      }
      catch( NumberFormatException nfe )
      {

      }
      return t_value;
    }
    // end added for new datatypes

    return t_value;

  }

  /**
   * This method performs the operation and returns info object
   * 
   * @param File
   *            inputFile
   * @param File
   *            output folder
   * @param String
   *            Prefix
   * @param String
   *            pid
   * @param FileInfo
   *            file info
   * @return OperationResultInfo Result
   */
  public String perform( String inputValue, FileDetailInfo fileDetailInfo, OperationInfo operationInfo )
  {
    return inputValue;
  }

  public String performColumnOperation( String inputValue, FileDetailInfo fileDetailInfo, OperationResultInfo operationResultInfo ) throws Exception
  {
    if ( fileDetailInfo == null || fileDetailInfo.getFileDetailOperations() == null || fileDetailInfo.getFileDetailOperations().size() <= 0 )
    {
      // No Operation required
      return inputValue;
    }

    for ( int i = 0; i < fileDetailInfo.getFileDetailOperations().size(); i++ )
    {
      OperationInfo t_operationInfo = (OperationInfo)fileDetailInfo.getFileDetailOperations().get( i );
      Operation t_operation = OperationsFactory.getDetailOperation( t_operationInfo.getOperationText() );
      if ( t_operation != null )
      {
        if ( t_operation.getGroup().equalsIgnoreCase( Operation.OPERATION_GROUP_VALIDATION ) )
        {
          String t_result = t_operation.perform( inputValue, fileDetailInfo, t_operationInfo );
          if ( t_result.equalsIgnoreCase( "FALSE" ) )
          {
            fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ":" + t_operationInfo.getOperationDesc() + " operation failed" );
            operationResultInfo.setErrorMessage( "[" + t_operationInfo.getOperationId() + "] " + t_operationInfo.getOperationDesc() + " on input value = " + inputValue );
            operationResultInfo.setResult( -1 );
          }
        }
        else if ( t_operation.getGroup().equalsIgnoreCase( Operation.OPERATION_GROUP_FORMAT ) )
        {
          inputValue = t_operation.perform( inputValue, fileDetailInfo, t_operationInfo );
        }
      }
      else
      {
        fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ":" + t_operationInfo.getOperationDesc() + " is not present" );
      }
    }
    return inputValue;
  }

  public boolean validateDataType( String inputValue, FileDetailInfo fileDetailInfo, OperationResultInfo operationResultInfo ) throws Exception
  {
    if ( inputValue.trim().length() == 0 )
    {
      return true;
    }
    if ( fileDetailInfo == null || fileDetailInfo.getColumnDataType().equals( "Text" ) )
    {
      // No validation required
      return true;
    }

    if ( fileDetailInfo.getColumnDataType().equals( "Integer" ) )
    {
      try
      {
        if ( inputValue != null )
        {
          int intValue = (int)Integer.parseInt( inputValue );
        }
      }
      catch( NumberFormatException nfe )
      {
        nfe.printStackTrace();
        logger.error( "Col" + fileDetailInfo.getOutputOrder() + ": is not of data type " + "Integer" );
        logger.error( "Datatype Validation on input value = " + inputValue + " failed." );
        fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": is not of data type " + "Integer" );
        operationResultInfo.setErrorMessage( "Datatype Validation on input value = " + inputValue + " failed." );
        operationResultInfo.setResult( -1 );
        return false;
      }
      if ( fileDetailInfo.getColumnLength() > 0 && inputValue.trim().length() > fileDetailInfo.getColumnLength() )
      {
        fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": length exceeds specified value " + fileDetailInfo.getColumnLength() );
        operationResultInfo.setErrorMessage( "Datatype Length Validation on input value = " + inputValue + " failed." );
        operationResultInfo.setResult( -1 );
        return false;
      }

    }
    else if ( fileDetailInfo.getColumnDataType().equals( "Decimal" ) )
    {
      inputValue = inputValue.trim();
      if ( !isDecimal( inputValue ) )
      {
        fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": is not of data type " + "Decimal" );
        operationResultInfo.setErrorMessage( "Datatype Validation on input value = " + inputValue + " failed." );
        operationResultInfo.setResult( -1 );
        return false;
      }
      if ( fileDetailInfo.getColumnLength() > 0 && inputValue.trim().length() > fileDetailInfo.getColumnLength() )
      {
        fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": length exceeds specified value " + fileDetailInfo.getColumnLength() );
        operationResultInfo.setErrorMessage( "Datatype Length Validation on input value = " + inputValue + " failed." );
        operationResultInfo.setResult( -1 );
        return false;
      }
      if ( fileDetailInfo.getColumnPrecision() > 0 )
      {
        int indexOfPeriod = inputValue.indexOf( "." );
        if ( indexOfPeriod != -1 )
        {
          if ( inputValue.length() > indexOfPeriod + 1 )
          {
            String decimalPart = inputValue.substring( indexOfPeriod + 1 );
            if ( decimalPart != null && decimalPart.length() > fileDetailInfo.getColumnPrecision() )
            {
              fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": length of decimal part exceeds specified precision " + fileDetailInfo.getColumnPrecision() );
              operationResultInfo.setErrorMessage( "Datatype Precision Validation on input value = " + inputValue + " failed." );
              operationResultInfo.setResult( -1 );
              return false;
            }
          }
        }
      }
    }
    else if ( fileDetailInfo.getColumnDataType().equals( "Date" ) )
    {
      if ( fileDetailInfo.getColumnAddlQualifier() != null && fileDetailInfo.getColumnAddlQualifier().trim().length() > 0 )
      {
        SimpleDateFormat t_sdf = new SimpleDateFormat( fileDetailInfo.getColumnAddlQualifier().trim() );
        t_sdf.setLenient( false );
        try
        {
          inputValue = inputValue.trim();
          if ( inputValue != null && inputValue.length() > 0 )
          {
            Date inputValueDate = t_sdf.parse( inputValue );
          }
        }
        catch( ParseException pe )
        {
          pe.printStackTrace();
          logger.error( "Col" + fileDetailInfo.getOutputOrder() + ": is not in given format " + fileDetailInfo.getColumnAddlQualifier() );
          logger.error( "Datatype Parsing Validation on input value = " + inputValue + " failed." );
          fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": is not in given format " + fileDetailInfo.getColumnAddlQualifier() );
          operationResultInfo.setErrorMessage( "Datatype Parsing Validation on input value = " + inputValue + " failed." );
          operationResultInfo.setResult( -1 );
          return false;
        }
      }
    }
    else if ( fileDetailInfo.getColumnDataType().equals( "SSN" ) )
    {
      if ( fileDetailInfo.getColumnAddlQualifier() != null && fileDetailInfo.getColumnAddlQualifier().trim().length() > 0 )
      {
        // inputValue = removeNonNum(inputValue);
        StringBuffer zeroPaddedSSN = new StringBuffer( inputValue );
        int firstIndexOfNumber = -1;
        int numberOfDigits = 0;
        int numberOfNondigits = 0;
        for ( int i = 0; i < zeroPaddedSSN.length(); i++ )
        {
          if ( Character.isDigit( zeroPaddedSSN.charAt( i ) ) )
          {
            if ( firstIndexOfNumber < 0 )
            {
              firstIndexOfNumber = i;
            }
            numberOfDigits++;
          }
          else
          {
            numberOfNondigits++;
          }
        }
        int numberOfZeros = 9 - numberOfDigits;
        if ( numberOfZeros > 2 )
        {
          fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": does not have required number of digits: " + fileDetailInfo.getColumnAddlQualifier() );
          operationResultInfo.setErrorMessage( "Datatype Digit [few] Validation on input value = " + inputValue + " failed." );
          operationResultInfo.setResult( -1 );
          return false;
        }
        if ( numberOfZeros < 0 )
        {
          fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": have too many number of digits: " + fileDetailInfo.getColumnAddlQualifier() );
          operationResultInfo.setErrorMessage( "Datatype Digit [too many] Validation on input value = " + inputValue + " failed." );
          operationResultInfo.setResult( -1 );
          return false;
        }
        if ( numberOfZeros == 1 )
        {
          zeroPaddedSSN.insert( firstIndexOfNumber, "0" );
        }
        else if ( numberOfZeros == 2 )
        {
          zeroPaddedSSN.insert( firstIndexOfNumber, "00" );
        }

        if ( !StringPattern.matchIgnoreCase( zeroPaddedSSN.toString(), fileDetailInfo.getColumnAddlQualifier() ) )
        {
          fileDetailInfo.setErrorMessage( "Col" + fileDetailInfo.getOutputOrder() + ": pattern match failed for pattern " + fileDetailInfo.getColumnAddlQualifier() + " and value " + inputValue );
          operationResultInfo.setErrorMessage( "Datatype pattern match failed on input value = " + inputValue + " failed." );
          operationResultInfo.setResult( -1 );
          return false;
        }
      }
    }
    return true;
  }

  public boolean isDecimal( String str )
  {
    // remove the decimal check as we can also pass whole numbers in fileload.
    boolean isDecimal = false;
    try
    {
      Double.parseDouble( str );
      isDecimal = true;
    }
    catch( NumberFormatException nme )
    {
      isDecimal = false;
    }

    return isDecimal;
  }

  public void setRemoveDoubleQuotes()
  {
    this.removeDoubleQuote = true;
  }

  public boolean isRemoveDoubleQuote()
  {
    return removeDoubleQuote;
  }

  public boolean isRemoveLineIfEmpty()
  {
    return removeLineIfEmpty;
  }

  public void setRemoveDoubleQuote( boolean b )
  {
    removeDoubleQuote = b;
  }

  public void setRemoveLineIfEmpty( boolean b )
  {
    removeLineIfEmpty = b;
  }

  public int getNumberOfFooterLinesToSkip()
  {
    return numberOfFooterLinesToSkip;
  }

  public void setNumberOfFooterLinesToSkip( int i )
  {
    numberOfFooterLinesToSkip = i;
  }

}

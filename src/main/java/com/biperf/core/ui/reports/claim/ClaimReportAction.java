/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/claim/ClaimReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.claim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.biperf.core.service.reports.ClaimReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.claim.ClaimDynamicReportValue;

/**
 * ClaimReportAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 8, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public abstract class ClaimReportAction extends BaseReportsAction
{
  private static final String STRING_NULL = "NULL";

  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
   * Result set returned from the stored procedure
   */
  public static final String OUTPUT_RESULT_SET = "p_out_result_set";

  /**
   * Stored proc returns this code when the stored procedure executed without
   * errors
   */
  public static final String GOOD = "00";

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "CLAIM",
                                                             "CLAIM_STATUS",
                                                             "DATE_SUBMITTED",
                                                             "FINAL_APPROVER",
                                                             "PROMO_NAME",
                                                             "PRODUCT_NAME",
                                                             "PRODUCT_CATEGORY",
                                                             "PRODUCT_SUBCATEGORY",
                                                             "PRODUCT_QUANTITY",
                                                             "SUBMITTER_FIRST_NAME",
                                                             "SUBMITTER_MIDDLE_NAME",
                                                             "SUBMITTER_LAST_NAME",
                                                             "PROXY_NAME",
                                                             "SUBMITTER_LOGIN_ID",
                                                             "SUBMITTER_COUNTRY",
                                                             "SUBMITTER_NODE_NAME",
                                                             "SUBMITTER_JOB_POSITION",
                                                             "SUBMITTER_DEPARTMENT",
                                                             "TEAM_MEMBER_CNT",
                                                             "TEAM_MEMBER_POINTS",
                                                             "POINTS",
                                                             "SWEEPSTAKES_WON",
                                                             "PAX_CHAR_1",
                                                             "PAX_CHAR_2",
                                                             "PAX_CHAR_3",
                                                             "PAX_CHAR_4",
                                                             "PAX_CHAR_5",
                                                             "PAX_CHAR_6",
                                                             "PAX_CHAR_7",
                                                             "PAX_CHAR_8",
                                                             "PAX_CHAR_9",
                                                             "PAX_CHAR_10",
                                                             "PAX_CHAR_11",
                                                             "PAX_CHAR_12",
                                                             "PAX_CHAR_13",
                                                             "PAX_CHAR_14",
                                                             "PAX_CHAR15",
  															  "PAX_CHAR16",
  															  "PAX_CHAR17",
  															  "PAX_CHAR18",
  															  "PAX_CHAR19",
  															  "PAX_CHAR20",
  															  "PAX_CHAR21",
  															  "PAX_CHAR22",
  															  "PAX_CHAR23",
  															  "PAX_CHAR24",
  															  "PAX_CHAR25",
  															  "PAX_CHAR26",
  															  "PAX_CHAR27",
  															  "PAX_CHAR28",
  															  "PAX_CHAR29",
  															  "PAX_CHAR30",
  															  "PAX_CHAR31",
  															  "PAX_CHAR32",
  															  "PAX_CHAR33",
  															  "PAX_CHAR34",
  															  "PAX_CHAR35"};


  private int p = 0;

  protected ClaimReportsService getClaimReportsService()
  {
    return (ClaimReportsService)getService( ClaimReportsService.BEAN_NAME );
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.claim.extract";
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getClaimReportsService().getClaimExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.CLAIM_REPORT;
  }

  @Override
  protected ReportBeanMethod getSecondaryReportBeanMethod()
  {
    return ReportBeanMethod.CLAIM_ITEM_REPORT;
  }

  /**
   * This whole code needs to be revisited when product claims are implemented.
   */
  @SuppressWarnings( "rawtypes" )
  protected String getSecondaryExtractReportData( Map<String, Object> reportParameters )
  {
    reportParameters.put( "submittedType", "show_all" );
    reportParameters.put( "languageCode", "en_US" );

    StringBuffer contentBuf = new StringBuffer();

    Map outParams = getClaimReportsService().getItemsClaimExtractResults( reportParameters );
    List reportExtractData = (ArrayList)outParams.get( OUTPUT_RESULT_SET );
    List<String> columnNames = new ArrayList<String>();
    for ( int i = 0; i < reportExtractData.size(); i++ )
    {
      List eachRow = (ArrayList)reportExtractData.get( i );
      for ( int j = 0; j < eachRow.size(); j++ )
      {
        ClaimDynamicReportValue report = (ClaimDynamicReportValue)eachRow.get( j );
        // Stored procedure is returning string "NULL" when there are no columns to display.
        if ( report.getColumnName() != null && !STRING_NULL.equalsIgnoreCase( report.getColumnName() ) && !columnNames.contains( report.getColumnName() ) )
        {
          columnNames.add( report.getColumnName() );
        }
      }
    }
    if ( !columnNames.isEmpty() )
    {
      buildCSVColumnNamesWithoutCMDesc( contentBuf, columnNames );
      buildCSVExtractContent( contentBuf, reportExtractData, columnNames );
    }
    else
    {
      contentBuf.append( "" ); // add empty row in case of no data
      contentBuf.append( NEW_LINE );
    }
    return contentBuf.toString();

  }

  @Override
  protected String getSecondExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "Items_Claimed_" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

  protected StringBuffer buildCSVColumnNamesWithoutCMDesc( StringBuffer contentBuf, List<String> columnNames )
  {
    if ( columnNames != null )
    {
      for ( int i = 0; i < columnNames.size(); i++ )
      {
        if ( i != 0 )
        {
          contentBuf.append( REGEX_COMMA );
        }
        contentBuf.append( columnNames.get( i ) );
      }
      contentBuf.append( NEW_LINE );
    }
    return contentBuf;
  }

  @SuppressWarnings( "rawtypes" )
  private void buildCSVExtractContent( StringBuffer contentBuf, List reportExtractData, List columnNames )
  {
    if ( reportExtractData != null && columnNames != null )
    {
      Long[] totals = new Long[columnNames.size()];
      for ( int t = 0; t < totals.length; t++ )
      {
        totals[t] = (long)0;
      }
      for ( int i = 0; i < reportExtractData.size(); i++ )
      {
        List eachRow = (ArrayList)reportExtractData.get( i );
        for ( int j = 0; j < eachRow.size(); j++ )
        {
          ClaimDynamicReportValue report = (ClaimDynamicReportValue)eachRow.get( j );
          for ( int k = 0; k < columnNames.size(); k++ )
          {
            String columnName = (String)columnNames.get( k );
            if ( columnName != null && report.getColumnName() != null && columnName.equals( report.getColumnName() ) )
            {
              contentBuf.append( formatReportValue( report.getValue() ) );
            }
          }
        }
        for ( int j = 1; j < eachRow.size(); j++ )
        {
          ClaimDynamicReportValue report = (ClaimDynamicReportValue)eachRow.get( j );
          for ( int k = 1; k < columnNames.size() - 1; k++ )
          {
            String columnName = (String)columnNames.get( k );
            if ( columnName != null && report.getColumnName() != null && columnName.equals( report.getColumnName() ) )
            {
              totals[p] = totals[p] + Long.parseLong( report.getValue() );
              p++;
            }
          }
        }
        p = 0;

        contentBuf.append( NEW_LINE );
      }
      contentBuf.append( formatReportValue( "Totals" ) );
      for ( int k = 0; k < columnNames.size() - 2; k++ )
      {
        contentBuf.append( formatReportValue( totals[k] ) );
      }
    }
  }

}

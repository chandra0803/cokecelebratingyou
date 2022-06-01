/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/budget/BudgetReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.budget;

import java.util.Map;

import com.biperf.core.service.reports.BudgetReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.DateUtils;

/**
 * BudgetReportAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 29, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public abstract class BudgetReportAction extends BaseReportsAction
{
  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "PROMOTION_NAME", "BUDGET_MASTER_NAME",   "BUDGET_OWNER", "BUDGET_PERIOD", "ORIGINAL_BUDGET", "ADJUSTMENTS", "AWARDED", "AVAILABLE_BALANCE" };

  public static final String[] EXTRACT_ISSUANCE_COLUMN_NAMES = { "BUDGET_MASTER_NAME",
                                                                 "BUDGET_TYPE",
                                                                 "BUDGET_OWNER",
                                                                 "AWARD_DATE",
                                                                 "GIVER_NAME",
                                                                 "GIVER_COUNTRY",
                                                                 "GIVER_ORG_UNIT",
                                                                 "RECIPIENT_FIRST_NAME",
                                                                 "RECIPIENT_LAST_NAME",
                                                                 "RECVR_COUNTRY",
                                                                 "AWARD_AMOUNT",
                                                                 "PROMOTION_NAME",
                                                                 "RECVR_ORG_UNIT",
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
                                                                 "PAX_CHAR_15",
 																  "PAX_CHAR_16",
 																  "PAX_CHAR_17",
 																  "PAX_CHAR_18",
 																  "PAX_CHAR_19",
 																  "PAX_CHAR_20",
 																  "PAX_CHAR_21",
 																  "PAX_CHAR_22",
 																  "PAX_CHAR_23",
 																  "PAX_CHAR_24",
 																  "PAX_CHAR_25",
 																  "PAX_CHAR_26",
 																  "PAX_CHAR_27",
 																  "PAX_CHAR_28",
 																  "PAX_CHAR_29",
 																  "PAX_CHAR_30",
 																  "PAX_CHAR_31",
 																  "PAX_CHAR_32",
 																  "PAX_CHAR_33",
 																  "PAX_CHAR_34",
 																  "PAX_CHAR_35"};


  public static final String[] EXTRACT_BUDGET_SECOND_LEVEL_COLUMN_NAMES = { "BUDGET_OWNER", "BUDGET_MASTER_NAME", "BUDGET_PERIOD", "ORIGINAL_BUDGET", "ADJUSTMENTS", "AWARDED", "AVAILABLE_BALANCE" };

  protected BudgetReportsService getBudgetReportsService()
  {
    return (BudgetReportsService)getService( BudgetReportsService.BEAN_NAME );
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.budget.extract";
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    reportParameters.put( "extractType", "primary" );
    reportParameters.put( "countryRatio", countryMediaValue() );
    Map reportExtractData = doExtractResultsCall( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  /** Override this and return the results from the appropriate extract service method */
  protected abstract Map doExtractResultsCall( Map<String, Object> reportParameters );

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getSecondaryExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    reportParameters.put( "countryRatio", countryMediaValue() );
    Map reportExtractData = doSecondaryExtractResultsCall( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ISSUANCE_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  /** Override this and return the results from the appropriate extract service method */
  protected abstract Map doSecondaryExtractResultsCall( Map<String, Object> reportParameters );

  @Override
  protected String getSecondExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "BudgetIssuance" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

  @Override
  protected String getBudgetSecondLevelExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    reportParameters.put( "extractType", "secondary" );
    reportParameters.put( "budgetId", null );
    reportParameters.put( "countryRatio", countryMediaValue() );
    Map reportExtractData = doBudgetSecondLevelExtractResultsCall( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_BUDGET_SECOND_LEVEL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  /** Override this and return the results from the appropriate extract service method */
  protected abstract Map doBudgetSecondLevelExtractResultsCall( Map<String, Object> reportParameters );

  @Override
  protected String getBudgetSecondLevelExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "BudgetSecondLevelExtract" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

  @Override
  protected String getBudgetThirdLevelExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    reportParameters.put( "extractType", "secondary" );
    reportParameters.put( "countryRatio", countryMediaValue() );
    Map reportExtractData = doBudgetThirdLevelExtractResultsCall( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_BUDGET_SECOND_LEVEL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }
  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.BUDGET_REPORT ;
  }
  
  /** Override this and return the results from the appropriate extract service method */
  protected abstract Map doBudgetThirdLevelExtractResultsCall( Map<String, Object> reportParameters );

  @Override
  protected String getBudgetThirdLevelExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "BudgetThirdLevelExtract" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

}

/**
 * 
 */

package com.biperf.core.ui.reports.awards;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.report.Report;
import com.biperf.core.service.reports.AwardsReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.utils.UserManager;

/**
 * @author poddutur
 *
 */
public abstract class AwardsReportAction extends BaseReportsAction
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( AwardsReportAction.class );
  private static final String[] EXTRACT_ALL_POINTS_ONLY_COLUMN_NAMES = { "TRANSACTION_ID",
                                                                         "TRANSACTION_DATE",
                                                                         "LOGIN_ID",
                                                                         "FIRST_NAME",
                                                                         "MIDDLE_NAME",
                                                                         "LAST_NAME",
                                                                         "COUNTRY",
                                                                         "PRIMARY_ORG_UNIT",
                                                                         "DEPARTMENT",
                                                                         "JOB_POSITION",
                                                                         "PROMOTION_TYPE",
                                                                         "PROMOTION_NAME",
                                                                         "TAXABLE",
                                                                         "POINTS_RECEIVED",
                                                                         "SWEEPSTAKES_WON",
                                                                         "ONTHESPOT_SERIAL",
                                                                         "PAX_CHAR1",
                                                                         "PAX_CHAR2",
                                                                         "PAX_CHAR3",
                                                                         "PAX_CHAR4",
                                                                         "PAX_CHAR5",
                                                                         "PAX_CHAR6",
                                                                         "PAX_CHAR7",
                                                                         "PAX_CHAR8",
                                                                         "PAX_CHAR9",
                                                                         "PAX_CHAR10",
                                                                         "PAX_CHAR11",
                                                                         "PAX_CHAR12",
                                                                         "PAX_CHAR13",
                                                                         "PAX_CHAR14",
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
     																	 "PAX_CHAR35" };

  private static final String[] EXTRACT_ALL_PLATEAU_ONLY_COLUMN_NAMES = { "TRANSACTION_ID",
                                                                          "TRANSACTION_DATE",
                                                                          "LOGIN_ID",
                                                                          "FIRST_NAME",
                                                                          "MIDDLE_NAME",
                                                                          "LAST_NAME",
                                                                          "COUNTRY",
                                                                          "PRIMARY_ORG_UNIT",
                                                                          "DEPARTMENT",
                                                                          "JOB_POSITION",
                                                                          "PROMOTION_TYPE",
                                                                          "PROMOTION_NAME",
                                                                          "TAXABLE",
                                                                          "LEVEL_NAME",
                                                                          "PAX_CHAR1",
                                                                          "PAX_CHAR2",
                                                                          "PAX_CHAR3",
                                                                          "PAX_CHAR4",
                                                                          "PAX_CHAR5",
                                                                          "PAX_CHAR6",
                                                                          "PAX_CHAR7",
                                                                          "PAX_CHAR8",
                                                                          "PAX_CHAR9",
                                                                          "PAX_CHAR10",
                                                                          "PAX_CHAR11",
                                                                          "PAX_CHAR12",
                                                                          "PAX_CHAR13",
                                                                          "PAX_CHAR14",
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


  private static final String[] EXTRACT_ALL_CASH_ONLY_COLUMN_NAMES = { "TRANSACTION_ID",
                                                                       "TRANSACTION_DATE",
                                                                       "LOGIN_ID",
                                                                       "FIRST_NAME",
                                                                       "MIDDLE_NAME",
                                                                       "LAST_NAME",
                                                                       "COUNTRY",
                                                                       "PRIMARY_ORG_UNIT",
                                                                       "DEPARTMENT",
                                                                       "JOB_POSITION",
                                                                       "PROMOTION_TYPE",
                                                                       "PROMOTION_NAME",
                                                                       "TAXABLE",
                                                                       "CASH_AMOUNT",
                                                                       "PAX_CHAR1",
                                                                       "PAX_CHAR2",
                                                                       "PAX_CHAR3",
                                                                       "PAX_CHAR4",
                                                                       "PAX_CHAR5",
                                                                       "PAX_CHAR6",
                                                                       "PAX_CHAR7",
                                                                       "PAX_CHAR8",
                                                                       "PAX_CHAR9",
                                                                       "PAX_CHAR10",
                                                                       "PAX_CHAR11",
                                                                       "PAX_CHAR12",
                                                                       "PAX_CHAR13",
                                                                       "PAX_CHAR14",
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
  "PAX_CHAR35" };

  private static final String[] EXTRACT_ALL_OTHER_ONLY_COLUMN_NAMES = { "TRANSACTION_ID",
                                                                        "TRANSACTION_DATE",
                                                                        "LOGIN_ID",
                                                                        "FIRST_NAME",
                                                                        "MIDDLE_NAME",
                                                                        "LAST_NAME",
                                                                        "COUNTRY",
                                                                        "PRIMARY_ORG_UNIT",
                                                                        "DEPARTMENT",
                                                                        "JOB_POSITION",
                                                                        "PROMOTION_TYPE",
                                                                        "PROMOTION_NAME",
                                                                        "TAXABLE",
                                                                        "OTHER",
                                                                        "OTHER_VALUE",
                                                                        "PAX_CHAR1",
                                                                        "PAX_CHAR2",
                                                                        "PAX_CHAR3",
                                                                        "PAX_CHAR4",
                                                                        "PAX_CHAR5",
                                                                        "PAX_CHAR6",
                                                                        "PAX_CHAR7",
                                                                        "PAX_CHAR8",
                                                                        "PAX_CHAR9",
                                                                        "PAX_CHAR10",
                                                                        "PAX_CHAR11",
                                                                        "PAX_CHAR12",
                                                                        "PAX_CHAR13",
                                                                        "PAX_CHAR14",
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
    																	"PAX_CHAR35" };

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "TRANSACTION_ID",
                                                             "TRANSACTION_DATE",
                                                             "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "LOGIN_ID",
                                                             "COUNTRY",
                                                             "PRIMARY_ORG_UNIT",
                                                             "JOB_POSITION",
                                                             "DEPARTMENT",
                                                             "PROMOTION_TYPE",
                                                             "PROMOTION_NAME",
                                                             "TAXABLE",
                                                             "POINTS_RECEIVED",
                                                             "PLATEAU_EARNED",
                                                             "SWEEPSTAKES_WON",
                                                             "ONTHESPOT_SERIAL",
                                                             "OTHER",
                                                             "OTHER_VALUE",
                                                             "PAX_CHAR1",
                                                             "PAX_CHAR2",
                                                             "PAX_CHAR3",
                                                             "PAX_CHAR4",
                                                             "PAX_CHAR5",
                                                             "PAX_CHAR6",
                                                             "PAX_CHAR7",
                                                             "PAX_CHAR8",
                                                             "PAX_CHAR9",
                                                             "PAX_CHAR10",
                                                             "PAX_CHAR11",
                                                             "PAX_CHAR12",
                                                             "PAX_CHAR13",
                                                             "PAX_CHAR14",
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


  protected AwardsReportsService getAwardsReportsService()
  {
    return (AwardsReportsService)getService( AwardsReportsService.BEAN_NAME );
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.awards.extract";
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    buildOnTheSpotReportParameter( reportParameters );
    Map reportExtractData = getAwardsReportsService().getAwardsActivityReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, getColumnHeaders( (String)reportParameters.get( "awardType" ) ) );
    buildCSVExtractContent( contentBuf, reportExtractData );
    String regex = "{$}";
    String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
    return contentBuf.toString().replace( regex, currencyCode );
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.AWARDS_REPORT;
  }

  public static String[] getColumnHeaders( String awardType )
  {
    if ( awardType.equals( Report.POINTS_AWARD ) )
    {
      return EXTRACT_ALL_POINTS_ONLY_COLUMN_NAMES;
    }
    else if ( awardType.equals( Report.CASH_AWARD ) )
    {
      return EXTRACT_ALL_CASH_ONLY_COLUMN_NAMES;
    }
    else if ( awardType.equals( Report.OTHER_AWARD ) )
    {
      return EXTRACT_ALL_OTHER_ONLY_COLUMN_NAMES;
    }
    else if ( awardType.equals( Report.PLATEAU_AWARD ) )
    {
      return EXTRACT_ALL_PLATEAU_ONLY_COLUMN_NAMES;
    }
    else
    {
      return EXTRACT_ALL_COLUMN_NAMES;
    }
  }
}


package com.biperf.core.process;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.reports.WorkHappierReportsService;
import com.biperf.core.utils.UserManager;

public class WorkHappierReportExtractProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "workhappierReportExtractProcess";
  public static final String MESSAGE_NAME = "WorkHappier Report Extract Process";

  private static final Log log = LogFactory.getLog( WorkHappierReportExtractProcess.class );

  private WorkHappierReportsService workHappierReportsService;
  private CMAssetService cmAssetService;
  private String selectMonth;
  private static final String REGEX_COMMA = ",";
  private static final String REGEX_DOUBLE_CODE = "\"";

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "PREFIX",
                                                             "FIRST_NAME",
                                                             "LAST_NAME",
                                                             "LOGIN_ID",
                                                             "ORG_NAME",
                                                             "COMPANY_NAME",
                                                             "COUNTRY",
                                                             "BIRTH_DATE",
                                                             "HIRE_DATE",
                                                             "SLIDER_SCORE",
                                                             "DATE_STAMP",
                                                             "TIME_STAMP",
                                                             "PAX_CHAR1_NAME",
                                                             "PAX_CHAR1_VALUE",
                                                             "PAX_CHAR2_NAME",
                                                             "PAX_CHAR2_VALUE",
                                                             "PAX_CHAR3_NAME",
                                                             "PAX_CHAR3_VALUE",
                                                             "PAX_CHAR4_NAME",
                                                             "PAX_CHAR4_VALUE",
                                                             "PAX_CHAR5_NAME",
                                                             "PAX_CHAR5_VALUE",
                                                             "PAX_CHAR6_NAME",
                                                             "PAX_CHAR6_VALUE",
                                                             "PAX_CHAR7_NAME",
                                                             "PAX_CHAR7_VALUE",
                                                             "PAX_CHAR8_NAME",
                                                             "PAX_CHAR8_VALUE",
                                                             "PAX_CHAR9_NAME",
                                                             "PAX_CHAR9_VALUE",
                                                             "PAX_CHAR10_NAME",
                                                             "PAX_CHAR10_VALUE",
                                                             "PAX_CHAR11_NAME",
                                                             "PAX_CHAR11_VALUE",
                                                             "PAX_CHAR12_NAME",
                                                             "PAX_CHAR12_VALUE",
                                                             "PAX_CHAR13_NAME",
                                                             "PAX_CHAR13_VALUE",
                                                             "PAX_CHAR14_NAME",
                                                             "PAX_CHAR14_VALUE",
                                                             "PAX_CHAR15_NAME",
                                                             "PAX_CHAR15_VALUE",
                                                             "PAX_CHAR16_NAME",
                                                             "PAX_CHAR16_VALUE",
                                                             "PAX_CHAR17_NAME",
                                                             "PAX_CHAR17_VALUE",
                                                             "PAX_CHAR18_NAME",
                                                             "PAX_CHAR18_VALUE",
                                                             "PAX_CHAR19_NAME",
                                                             "PAX_CHAR19_VALUE",
                                                             "PAX_CHAR20_NAME",
                                                             "PAX_CHAR20_VALUE" };

  public WorkHappierReportExtractProcess()
  {
    super();
  }

  @Override
  protected void onExecute()
  {
    try
    {
      Map<String, Object> extractParameters = new HashMap<String, Object>();

      String selectMonth = this.selectMonth.substring( 4, 6 ) + "/" + this.selectMonth.substring( 6, this.selectMonth.length() ) + "/" + this.selectMonth.substring( 0, 4 );
      extractParameters.put( "selectMonth", selectMonth );
      extractParameters.put( "cvHeaders", buildCSVColumnHeaders( getColumnHeaders() ) );

      getWorkHappierReportsService().generateAndSendEmailWorkHappierReportExtract( extractParameters );

    }
    catch( Throwable e )
    {
      log.error( "Error processing report extraction", e );
    }
  }

  protected String buildCSVColumnHeaders( String[] columnCMKeys )
  {
    Locale locale = UserManager.getLocale();
    CMAssetService service = getCmAssetService();
    StringBuffer cvHeaders = new StringBuffer();

    if ( columnCMKeys != null )
    {
      for ( int i = 0; i < columnCMKeys.length; i++ )
      {
        if ( i != 0 )
        {
          cvHeaders.append( REGEX_COMMA );
        }
        String columnDesc = REGEX_DOUBLE_CODE + service.getString( getReportExtractCmAssetCode(), columnCMKeys[i], locale, false ) + REGEX_DOUBLE_CODE;
        cvHeaders.append( columnDesc );
      }
    }
    return cvHeaders.toString();
  }

  protected String getReportExtractCmAssetCode()
  {
    return "report.work.happier.extract";
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  public WorkHappierReportsService getWorkHappierReportsService()
  {
    return workHappierReportsService;
  }

  public void setWorkHappierReportsService( WorkHappierReportsService workHappierReportsService )
  {
    this.workHappierReportsService = workHappierReportsService;
  }

  public CMAssetService getCmAssetService()
  {
    return cmAssetService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public String getSelectMonth()
  {
    return selectMonth;
  }

  public void setSelectMonth( String selectMonth )
  {
    this.selectMonth = selectMonth;
  }
}

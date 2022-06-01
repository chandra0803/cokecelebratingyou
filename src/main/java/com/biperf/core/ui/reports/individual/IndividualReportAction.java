/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/individual/IndividualReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.individual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.reports.IndividualReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.individualactivity.IndividualActivityReportValue;

/**
 * IndividualReportAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 14, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public abstract class IndividualReportAction extends BaseReportsAction
{

  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_ONE = { "ACTIVITY", "POINTS", "PLATEAU_EARNED", "SWEEPSTAKES_WON", "RECEIVED", "SENT" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_TWO = { "PROMOTION", "LEVEL_SELECTED", "BASE", "GOAL", "ACTUAL_RESULTS", "PERCENTAGE_GOAL", "ACHIEVED", "POINTS", "PLATEAU_EARNED" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_THREE = { "PROMOTION", "DATE_APPROVED", "NOMINATOR", "POINTS", "SWEEPSTAKES_WON", "STATUS" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_FOUR = { "PROMOTION", "DATE_APPROVED", "NOMINEE", "POINTS", "SWEEPSTAKES_WON" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_FIVE = { "DATE", "RECIPIENT", "ORG_NAME", "POINTS", "OTS_SERIAL" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_SIX = { "PROMOTION", "DATE_SENT", "RECEIVER", "POINTS", "SWEEPSTAKES_WON" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_SEVEN = { "PROMOTION", "DATE_SENT", "SENDER", "POINTS", "PLATEAU_EARNED", "SWEEPSTAKES_WON", "STATUS" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_EIGHT = { "PROMOTION",
                                                                     "QUIZ_ATTEMPTS",
                                                                     "ATTEMPTS_PASSED",
                                                                     "ATTEMPTS_FAILED",
                                                                     "ATTEMPTS_IN_PROGRESS",
                                                                     "POINTS",
                                                                     "SWEEPSTAKES_WON",
                                                                     "RESULT" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_NINE = { "DATE", "RECIPIENT", "SENDER", "ORG_NAME", "PROMOTION", "POINTS", "PLATEAU_EARNED", "SWEEPSTAKES_WON", "OTS_SERIAL" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_TEN = { "PROMOTION", "DATE_COMPLETED", "RESULT", "POINTS", "SWEEPSTAKES_WON" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_ELEVEN = { "CLAIMS_NUMBER", "PROMOTION", "DATE_SUBMITTED", "SOLD_TO", "STATUS", "POINTS" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_TWELVE = { "PROMOTION", "ROUND", "WIN", "LOSS", "TIE", "ACTUAL_RESULTS", "RANKING", "POINTS_EARNED" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_THIRTEEN = { "PROMOTION",
                                                                        "LEVEL_SELECTED",
                                                                        "BASE",
                                                                        "CHALLENGEPOINT",
                                                                        "ACTUAL_RESULTS",
                                                                        "PERCENTAGE_CHALLENGEPOINT",
                                                                        "ACHIEVED",
                                                                        "POINTS",
                                                                        "PLATEAU_EARNED" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_FOURTEEN = { "PROMOTION", "MEDIA_DATE", "POINTS" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_FIFTEEN = { "SSI_CONTEST_NAME", "DATE", "POINTS", "OTHER" };
  private static final String[] EXTRACT_COLUMN_NAMES_TABLE_SIXTEEN = { "SSI_CONTEST_NAME", "DATE", "POINTS", "OTHER", "OTHER_VALUE" };

  protected IndividualReportsService getIndividualReportsService()
  {
    return (IndividualReportsService)getService( IndividualReportsService.BEAN_NAME );
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  @Override
  protected String[] getColumnHeaders( Map<String, Object> reportParameters )
  {
    return null;
  }

  // Individual extract has more than one table with column headers.
  @Override
  protected void addColumnHeaders( Map<String, Object> reportParameters )
  {
    reportParameters.put( "csHeaders_summary", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_ONE ) );
    reportParameters.put( "csHeaders_goalquest", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_TWO ) );
    reportParameters.put( "csHeaders_nominationsReceived", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_THREE ) );
    reportParameters.put( "csHeaders_nominationsGiven", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_FOUR ) );
    reportParameters.put( "csHeaders_onTheSpot", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_FIVE ) );
    reportParameters.put( "csHeaders_recognitionsGiven", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_SIX ) );
    reportParameters.put( "csHeaders_recognitionsReceived", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_SEVEN ) );
    reportParameters.put( "csHeaders_quizSummary", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_EIGHT ) );
    reportParameters.put( "csHeaders_allAwards", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_NINE ) );
    reportParameters.put( "csHeaders_quizDetail", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_TEN ) );
    reportParameters.put( "csHeaders_productClaims", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_ELEVEN ) );
    reportParameters.put( "csHeaders_throwdown", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_TWELVE ) );
    reportParameters.put( "csHeaders_challengepoint", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_THIRTEEN ) );
    reportParameters.put( "csHeaders_badge", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_FOURTEEN ) );
    reportParameters.put( "csHeaders_ssicontests", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_FIFTEEN ) );
    reportParameters.put( "csHeaders_ssicontests_sec", buildCSVColumnHeaders( EXTRACT_COLUMN_NAMES_TABLE_SIXTEEN ) );
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.individual.extract";
  }

  @Override
  protected String getExtractFileName( ReportParametersForm form )
  {
    return form.getPaxFirstName() + "_" + form.getPaxLastName() + "_" + super.getExtractFileName( form );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    reportParameters.put( "isExtract", Boolean.TRUE );
    addColumnHeaders( reportParameters );
    Map reportExtractData = getIndividualReportsService().getIndividualActivityReportExtract( reportParameters );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  @SuppressWarnings( "rawtypes" )
  protected void buildCSVExtractContent( StringBuffer contentBuf, Map output )
  {
    List results = (List)output.get( OUTPUT_RESULT_SET );

    if ( GOOD.equals( output.get( OUTPUT_RETURN_CODE ) ) && null != results )
    {
      for ( int i = 0; i < results.size(); i++ )
      {
        contentBuf.append( results.get( i ) ).append( NEW_LINE );
      }
    }
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    throw new RuntimeException( "Operation not supported" );
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod( Map<String, Object> extractParameters )
  {
    return ReportBeanMethod.INDIVIDUAL_ACTIVITY;
  }

  protected List<IndividualActivityReportValue> validateForOntheSpotList( List<IndividualActivityReportValue> activityList )
  {
    List<IndividualActivityReportValue> dummyactivityList = new ArrayList<IndividualActivityReportValue>( activityList );
    Iterator iter = activityList.iterator();
    while ( iter.hasNext() )
    {
      IndividualActivityReportValue individualActivity = (IndividualActivityReportValue)iter.next();
      if ( individualActivity.getModuleAssetCode() != null && individualActivity.getModuleAssetCode().equals( "onTheSpot" ) )
      {
        if ( !getSystemVariableService().getPropertyByName( SystemVariableService.AWARDBANQ_CONVERTCERT_IS_USED ).getBooleanVal() )
        {
          dummyactivityList.remove( individualActivity );
        }
      }
    }
    return dummyactivityList;
  }

  protected List<IndividualActivityReportValue> changeOrderForViewAllAwardsList( List<IndividualActivityReportValue> activityList )
  {
    List<IndividualActivityReportValue> dummyactivityList = new ArrayList<IndividualActivityReportValue>( activityList );
    Iterator iter = activityList.iterator();
    IndividualActivityReportValue individualActivity1 = null;
    while ( iter.hasNext() )
    {
      IndividualActivityReportValue individualActivity = (IndividualActivityReportValue)iter.next();
      if ( individualActivity.getViewAllAwards() != null )
      {
        dummyactivityList.remove( individualActivity );
        individualActivity1 = individualActivity;
      }
    }
    if ( individualActivity1 != null )
    {
      dummyactivityList.add( individualActivity1 );
    }
    return dummyactivityList;
  }

}

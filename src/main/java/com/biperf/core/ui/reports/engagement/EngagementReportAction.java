
package com.biperf.core.ui.reports.engagement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.service.reports.EngagementReportsService;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * EngagementReportAction.
 * @author kandhi
 * @since Aug 21, 2014
 * @version 1.0

 * This report is for the participation score extract in the engagement module. 
 * This report is available to eligible manager's and owner's of a team who has engagement eligible promotion audiences.
 * The parameters come from the engagement team dash board page unlike reports which come from change filters screen driven by database.
 * This report is not shown in the reports module.
 * This does not use report filters page like the other reports.
 */
public class EngagementReportAction extends BaseReportsAction
{
  private static final String TO_DATE = "toDate";
  private static final String FROM_DATE = "fromDate";
  private static final String TEAM = "team";
  private static final String END_YEAR = "endYear";
  private static final String END_MONTH = "endMonth";
  private static final String LOCALE = "locale";
  private static final String USER_ID = "userId";
  private static final String NODE_IDS = "nodeId";
  private static final String START_DATE = "startDate";
  private static final String END_DATE = "endDate";
  private static final String TIMEFRAME_TYPE = "timeframeType";
  private static final String MODE = "mode";
  private static final String MONTH = "month";

  protected static final String[] EXTRACT_COLUMN_NAMES = { "PAX_FIRST_NAME",
                                                           "PAX_LAST_NAME",
                                                           "ORG_NAME",
                                                           "DEPARTMENT",
                                                           "JOB_TITLE",
                                                           "COUNTRY",
                                                           "RECOGNITION_SENT",
                                                           "RECOGNITION_SENT_TARGET",
                                                           "RECOGNITION_RECEIVED",
                                                           "RECOGNITION_RECEIVED_TARGET",
                                                           "RECOGNIZED",
                                                           "RECOGNIZED_TARGET",
                                                           "RECOGNIZED_BY",
                                                           "RECOGNIZED_BY_TARGET",
                                                           "SITE_VISITS",
                                                           "SITE_VISITS_TARGET",
                                                           "PARTICIPATION_SCORE",
                                                           "PARTICIPATION_SCORE_TARGET" };

  @Override
  protected String getReportCode()
  {
    return null;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.engagement.participationscore.extract";
  }

  public ActionForward getParticipationScoreExtract( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = populateQueryParams( request );
    String filename = "EngagementParticipationScoreExtract" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";

    // large audience or standard?
    if ( !isLargeAudienceReport() )
    {
      processStandardExtract( response, filename, queryParams );
    }
    else
    {
    	/*WIP 20160 customization start */
        processLargeAudienceExtract( filename, queryParams, false, request);
    	/*WIP 20160 customization end */

      writeAsJsonToResponse( buildModal( false ), response );
    }
    return null;
  }

  @Override
  protected WebErrorMessageList buildModal( boolean isSecondaryReport )
  {
    WebErrorMessageList messages = new WebErrorMessageList();
    WebErrorMessage message = new WebErrorMessage();
    message = WebErrorMessage.addServerCmd( message );
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    message.setText( CmsResourceBundle.getCmsBundle().getString( "report.extract.confirm.YOUR" ) + " " + CmsResourceBundle.getCmsBundle().getString( getReportAssetName() ) + " "
        + CmsResourceBundle.getCmsBundle().getString( "report.extract.confirm.PROCESSING_TEXT1" ) );
    messages.getMessages().add( message );
    return messages;
  }

  @Override
  protected String getReportAssetName()
  {
    return this.getReportExtractCmAssetCode() + "." + "PARTICIPATION_SCORE_EXTRACT";
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map<String, Object> reportExtractData = getEngagementReportsService().getParticipationScoreReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, getColumnHeaders( reportParameters ) );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.ENGAGEMENT_PARTICIPATION_SCORE_REPORT;
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_COLUMN_NAMES;
  }

  private Map<String, Object> populateQueryParams( HttpServletRequest request )
  {
    Map<String, Object> queryParams = new HashMap<String, Object>();
    if ( ClientStateUtils.getClientStateMap( request ) != null )
    {
      String nodeId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), NODE_IDS );
      String fromDate = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), FROM_DATE );
      String toDate = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), TO_DATE );
      String timeframeType = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), TIMEFRAME_TYPE );
      String reqUserId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), USER_ID );

      String mode = TEAM;
      nodeId = StringUtils.isEmpty( nodeId ) ? null : nodeId;
      timeframeType = StringUtils.isEmpty( timeframeType ) ? MONTH : timeframeType;
      Long userId = StringUtils.isEmpty( reqUserId ) ? UserManager.getUserId() : Long.parseLong( reqUserId );
      Date startDate = DateUtils.toDate( fromDate );
      Date endDate = DateUtils.toDate( toDate );
      int endMonth = DateUtils.getMonthFromDate( endDate );
      int endYear = DateUtils.getYearFromDate( endDate );

      queryParams.put( NODE_IDS, nodeId );
      queryParams.put( USER_ID, userId );
      queryParams.put( TIMEFRAME_TYPE, timeframeType );
      queryParams.put( MODE, mode );
      queryParams.put( LOCALE, UserManager.getLocale() );
      queryParams.put( END_MONTH, endMonth + 1 );
      queryParams.put( END_YEAR, endYear );
      queryParams.put( START_DATE, startDate );
      queryParams.put( END_DATE, endDate );
    }
    return queryParams;
  }

  private EngagementReportsService getEngagementReportsService()
  {
    return (EngagementReportsService)getService( EngagementReportsService.BEAN_NAME );
  }

}


package com.biperf.core.ui.ssi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

/**
 * SSIContestDownloadAction.
 * 
 * @author dudam
 * @since Jan 14, 2015
 * @version 1.0
 */
public class SSIContestDownloadAction extends BaseReportsAction
{

  protected static final String[] EXTRACT_COLUMN_NAMES = { "CONTEST_ID",
                                                           "LOGIN_ID",
                                                           "FIRST_NAME",
                                                           "LAST_NAME",
                                                           "ORG_UNIT",
                                                           "EMAIL_ADDRESS",
                                                           "ACTIVITY_DESCRIPTION",
                                                           "PROGRESS_TOTAL",
                                                           "ACTIVITY_DATE" };

  private static final Log log = LogFactory.getLog( SSIContestDownloadAction.class );

  /**
   * Download Contest Data
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward downloadContest( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = new HashMap<String, Object>();
    Long contestId = getContestIdFromClientState( request.getParameter( "id" ), true );
    queryParams.put( "contestId", contestId );

    /* Bug# 61394, commenting out feature for large audience, moved to next phase */
    processStandardExtract( response, getFilename( contestId ), queryParams );
    /*
     * if ( isLargeAudienceDownload() ) { request.setAttribute( "largeAudience", Boolean.TRUE );
     * processLargeAudienceExtract( getFilename( contestId ), queryParams, false );
     * writeAsJsonToResponse( buildModal( false ), response ); } else { processStandardExtract(
     * response, getFilename( contestId ), queryParams ); }
     */
    return null;
  }

  private Map<String, Object> getSSIContestClientStateMap( String clientState )
  {
    try
    {
      String password = ClientStatePasswordManager.getPassword();
      return ClientStateSerializer.deserialize( clientState, password );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "Invalid client state" );
    }
  }

  protected Long getContestIdFromClientState( String clientState, boolean isDecoded )
  {
    try
    {
      if ( !isDecoded )
      {
        clientState = URLDecoder.decode( clientState, "UTF-8" );
      }
      Map<String, Object> clientStateMap = getSSIContestClientStateMap( clientState );
      return Long.parseLong( clientStateMap.get( "contestId" ).toString() );
    }
    catch( UnsupportedEncodingException e )
    {
      throw new RuntimeException( "Unable Decode Client State: " + e );
    }
  }

  private boolean isLargeAudienceDownload()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.ENABLE_LARGE_AUDIENCE_REPORT_GENERATION ).getBooleanVal();
  }

  private String getFilename( Long contestId )
  {
    return ImageUtils.getSSIContestProgressADCFilePath( contestId, ".csv" );
  }

  @Override
  protected String getReportCode()
  {
    return null;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "ssi_contest.preview";
  }

  @Override
  protected String getReportAssetName()
  {
    return this.getReportExtractCmAssetCode() + "." + "SSI_CONTEST_DATA_EXTRACT";
  }

  @Override
  protected void buildCSVExtractContent( StringBuffer contentBuf, Map output )
  {
    String today = DateUtils.toDisplayString( new Date(), Locale.US );
    List<SSIContestParticipantValueBean> results = (List<SSIContestParticipantValueBean>)output.get( OUTPUT_RESULT_SET );
    if ( results != null && !results.isEmpty() )
    {
      Collections.sort( results, new SSIContestParticipantComparator() );
      for ( int i = 0; i < results.size(); i++ )
      {
        SSIContestParticipantValueBean valueBean = results.get( i );
        contentBuf.append( valueBean.getContestId() ).append( "," );
        contentBuf.append( valueBean.getLoginId() ).append( "," );
        contentBuf.append( valueBean.getFirstName() ).append( "," );
        contentBuf.append( valueBean.getLastName() ).append( "," );
        contentBuf.append( valueBean.getOrgName() ).append( "," );
        contentBuf.append( valueBean.getEmailAddress() ).append( "," );
        contentBuf.append( valueBean.getActivityDescription() ).append( "," );
        contentBuf.append( "" ).append( "," );
        contentBuf.append( today );
        contentBuf.append( NEW_LINE );
      }
    }
  }

  @Override
  protected String getExtractReportData( Map<String, Object> inParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map<String, Object> extractData = new HashMap<String, Object>();
    try
    {
      extractData = getSSIContestService().downloadContestData( inParameters );
    }
    catch( Exception e )
    {
      log.error( e );
    }
    buildCSVColumnNames( contentBuf, getColumnHeaders( inParameters ) );
    buildCSVExtractContent( contentBuf, extractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.SSI_CONTEST_DATA_EXTRACT;
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_COLUMN_NAMES;
  }

  protected SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  class SSIContestParticipantComparator implements Comparator<SSIContestParticipantValueBean>
  {

    public int compare( SSIContestParticipantValueBean contestParticipantValueBeanOne, SSIContestParticipantValueBean contestParticipantValueBeanTwo )
    {
      int nameComp = contestParticipantValueBeanOne.getLastName().compareToIgnoreCase( contestParticipantValueBeanTwo.getLastName() );
      return nameComp == 0 ? contestParticipantValueBeanOne.getFirstName().compareToIgnoreCase( contestParticipantValueBeanTwo.getFirstName() ) : nameComp;
    }

  }

}

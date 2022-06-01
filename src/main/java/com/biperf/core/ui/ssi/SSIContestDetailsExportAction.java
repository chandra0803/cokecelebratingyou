
package com.biperf.core.ui.ssi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestDetailsExportAction.
 * 
 * @author kandhi
 * @since Feb 18, 2015
 * @version 1.0
 */
public class SSIContestDetailsExportAction extends SSIContestDownloadAction
{

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
  @Override
  public ActionForward downloadContest( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = new HashMap<String, Object>();

    String role = request.getParameter( "role" );

    if ( SSIContest.CONTEST_ROLE_MGR.equals( role ) )
    {
      queryParams.put( "userId", UserManager.getUserId() );
    }
    else if ( SSIContest.CONTEST_ROLE_CREATOR.equals( role ) )
    {
      queryParams.put( "userId", null );
    }
    else
    {
      // Something is wrong.
      return null;
    }

    queryParams.put( "userLocale", UserManager.getLocale() );
    queryParams.put( "contestId", SSIContestUtil.getContestIdFromClientState( request, request.getParameter( "id" ), !request.getMethod().equalsIgnoreCase( "GET" ) ) );

    /* Bug# 61394, commenting out feature for large audience, moved to next phase */
    processStandardExtract( response, getFilename(), queryParams );
    /*
     * if ( isLargeAudienceReport() ) { processLargeAudienceExtract( getFilename(), queryParams,
     * false ); writeAsJsonToResponse( buildModal( false ), response ); } else {
     * processStandardExtract( response, getFilename(), queryParams ); }
     */
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

  private String getFilename()
  {
    return "SSIContestDetailsExport" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "ssi_contest.details.export";
  }

  @Override
  protected String getReportAssetName()
  {
    return this.getReportExtractCmAssetCode() + "." + "SSI_CONTEST_DETAILS_EXPORT";
  }

  @Override
  protected void buildCSVExtractContent( StringBuffer contentBuf, Map output )
  {
    List<String> results = (List<String>)output.get( "p_out_ref_cursor" );
    if ( results != null && results.size() > 0 )
    {
      for ( int i = 0; i < results.size(); i++ )
      {
        contentBuf.append( results.get( i ) );
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
      extractData = getSSIContestService().downloadContestCreatorExtractData( inParameters );
    }
    catch( Exception e )
    {
      log.error( e );
    }
    buildCSVColumnNames( contentBuf, getColumnHeaders( extractData ) );
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
    return null;
  }

}

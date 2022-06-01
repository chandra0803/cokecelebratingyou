
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

import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;

/**
 * 
 * 
 * @author Ravi Kancherla
 * @since Jun 5, 2015
 * @version 1.0
 */

public class ContestClaimsExtractAction extends BaseReportsAction
{

  private static final Log log = LogFactory.getLog( ContestClaimsExtractAction.class );

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward extractFinalReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, request.getParameter( "id" ), false );
    Map<String, Object> queryParams = new HashMap<String, Object>();
    queryParams.put( "contestId", contestId );
    queryParams.put( "p_in_locale", UserManager.getLocale() );
    processStandardExtract( response, getFilename(), queryParams );
    return null;
  }

  private String getFilename()
  {
    return "SSIContestClaimsDataExtract" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
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
      extractData = getSSIContestService().extractContestClaimData( inParameters );
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

  @Override
  protected String getReportCode()
  {
    return null;
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

}

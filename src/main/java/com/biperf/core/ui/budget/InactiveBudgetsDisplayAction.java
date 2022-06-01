/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/InactiveBudgetsDisplayAction.java,v $
 */

package com.biperf.core.ui.budget;

import java.nio.charset.Charset;
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

import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/**
 * 
 * @author poddutur
 * @since Oct 26, 2015
 */
public class InactiveBudgetsDisplayAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( InactiveBudgetsDisplayAction.class );

  protected static final String REGEX_COMMA = ",";
  protected static final String NEW_LINE = "\n";
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";
  public static final String OUTPUT_RESULT_SET = "p_out_result_set";
  public static final String GOOD = "00";

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "BUDGET_MASTER_NAME",
                                                             "SOURCE_BUDGET_TIME_PERIOD",
                                                             "ORG_UNIT_ID",
                                                             "BUDGET_OWNER_NAME",
                                                             "BUDGET_OWNER_LOGIN_ID",
                                                             "BUDGET_AMOUNT",
                                                             "TRANSFER_TO_OWNER1",
                                                             "AMOUNT_OWNER1",
                                                             "TRANSFER_TO_OWNER2",
                                                             "AMOUNT_OWNER2",
                                                             "TRANSFER_TO_OWNER3",
                                                             "AMOUNT_OWNER3" };

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, form, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward extract( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    InactiveBudgetsForm inactiveBudgetsForm = (InactiveBudgetsForm)form;
    Map<String, Object> extractParameters = new HashMap<String, Object>();
    extractParameters.put( "budgetMasterId", inactiveBudgetsForm.getBudgetMasterId() );
    extractParameters.put( "budgetSegmentId", inactiveBudgetsForm.getBudgetSegmentId() );

    String filename = getExtractFileName();
    writeHeader( response, filename );
    String content = getExtractReportData( extractParameters );
    writeContent( content, response );

    return null;
  }

  protected String getExtractReportData( Map<String, Object> extractParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map<String, Object> reportExtractData = getBudgetMasterService().getExtractInactiveBudgetsData( extractParameters );
    buildCSVColumnNames( contentBuf, getColumnHeaders() );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  protected StringBuffer buildCSVColumnNames( StringBuffer contentBuf, String[] columnCMKeys )
  {
    Locale locale = UserManager.getLocale();
    CMAssetService service = getCMAssetService();
    if ( columnCMKeys != null )
    {
      for ( int i = 0; i < columnCMKeys.length; i++ )
      {
        if ( i != 0 )
        {
          contentBuf.append( REGEX_COMMA );
        }
        String columnDesc = service.getString( "inactive.budgets.extract", columnCMKeys[i], locale, false );
        contentBuf.append( columnDesc );
      }
      contentBuf.append( NEW_LINE );
    }
    return contentBuf;
  }

  protected void buildCSVExtractContent( StringBuffer contentBuf, Map output )
  {
    List results = (List)output.get( OUTPUT_RESULT_SET );

    if ( GOOD.equals( output.get( OUTPUT_RETURN_CODE ) ) && null != results && results.size() > 0 )
    {
      for ( int i = 0; i < results.size(); i++ )
      {
        contentBuf.append( results.get( i ) ).append( NEW_LINE );
      }
    }
  }

  public void writeHeader( HttpServletResponse response, String fileName )
  {
    response.setContentType( "text/csv; charset=UTF-8" );
    response.setCharacterEncoding( "UTF-8" );
    response.setHeader( "Pragma", "public" );
    response.setHeader( "Cache-Control", "max-age=0" );
    response.setHeader( "Content-disposition", "attachment; filename=" + fileName );
    try
    {
      response.flushBuffer();
    }
    catch( Exception ex )
    {
      logger.error( ex );
    }
  }

  public void writeContent( String content, HttpServletResponse response ) throws Exception
  {
    response.getOutputStream().write( new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF } );
    response.getOutputStream().write( content.getBytes( Charset.forName( "UTF-8" ) ) );
  }

  protected String getExtractFileName()
  {
    String formattedDate = DateUtils.toDisplayString( DateUtils.getCurrentDate() ).replaceAll( "/", "" );
    return ImportFileTypeType.BUDGET_DISTRIBUTION + "_" + formattedDate + ".csv";
  }

  protected CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

}

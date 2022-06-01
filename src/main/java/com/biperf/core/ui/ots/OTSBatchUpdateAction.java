
package com.biperf.core.ui.ots;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.ots.OTSService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.value.ots.v1.program.Batch;
import com.biperf.core.value.ots.v1.program.Program;

public class OTSBatchUpdateAction extends BaseDispatchAction
{
  private static final Log log = LogFactory.getLog( OTSBatchUpdateAction.class );

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward updateBatch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionMessages errors = new ActionMessages();
    OTSBillCodesForm otsBillCodesForm = (OTSBillCodesForm)form;
    Program program = null;
    String otsProgramNumber = null;
    String batchNumber = null;

    if ( !Objects.isNull( request.getParameter( "programNumber" ) ) )
    {
      otsProgramNumber = request.getParameter( "programNumber" );
    }
    else if ( !Objects.isNull( otsBillCodesForm.getProgramNumber() ) )
    {
      otsProgramNumber = otsBillCodesForm.getProgramNumber();
    }
    if ( !Objects.isNull( request.getParameter( "batchNumber" ) ) )
    {
      batchNumber = request.getParameter( "batchNumber" );
    }
    else if ( !Objects.isNull( otsBillCodesForm.getBatchNumber() ) )
    {
      batchNumber = otsBillCodesForm.getBatchNumber();
    }
    try
    {
      program = getOTSService().getOTSProgramInfo( otsProgramNumber );
      request.setAttribute( "otsProgramDetails", program );
      request.setAttribute( "batchNumber", batchNumber );
      Batch batch = getOTSProgramService().getBatchDetails( batchNumber, program );
      otsBillCodesForm.load( batch );
      request.setAttribute( "batch", batch );
    }
    catch( ServiceErrorException serviceException )
    {
      String exceptionValue = serviceException.getServiceErrors().get( 0 ).toString();
      exceptionValue = exceptionValue.substring( exceptionValue.indexOf( "[key:" ) + 5, exceptionValue.indexOf( "arg0:" ) - 2 );
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( exceptionValue.trim() ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_SEARCH );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );

  }

 
  private OTSProgramService getOTSProgramService()
  {
    return (OTSProgramService)getService( OTSProgramService.BEAN_NAME );
  }

  private OTSService getOTSService()
  {
    return (OTSService)getService( OTSService.BEAN_NAME );
  }
}


package com.biperf.core.ui.ots;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;

import com.biperf.core.domain.enums.OTSProgramStatusType;
import com.biperf.core.domain.ots.OTSBatch;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.ots.OTSService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.value.ots.v1.program.Batch;
import com.biperf.core.value.ots.v1.program.BatchDescription;
import com.biperf.core.value.ots.v1.program.Program;
import com.objectpartners.cms.util.CmsUtil;

public class OTSBatchUpdateSaveAction extends BaseDispatchAction
{

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
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

    program = getOTSService().getOTSProgramInfo( otsProgramNumber );

    List<Batch> batches = new ArrayList<Batch>();
    Batch batch = getOTSProgramService().getBatchDetails( batchNumber, program );

    batch = otsBillCodesForm.toDomainObject( batch );
    program.getBatches().clear();
    batches.add( batch );
    program.setBatches( batches );
    try
    {
      getOTSService().updateOTSBatchDetails( program );

    }
    catch( ServiceErrorException serviceException )
    {
      String exceptionValue = serviceException.getServiceErrors().get( 0 ).toString();
      exceptionValue = exceptionValue.substring( exceptionValue.indexOf( "[key:" ) + 5, exceptionValue.indexOf( "arg0:" ) - 2 );
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( exceptionValue.trim() ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_SEARCH );
    }
    // Save the details in OTS_BATCH table

    OTSProgram prgm = getOTSProgramService().getOTSProgramByProgramNumber( new Long( program.getProgramNumber() ) );
    if ( !Objects.nonNull( prgm ) )
    {
      prgm = new OTSProgram();
      prgm.setProgramNumber( new Long( program.getProgramNumber() ) );
      prgm.setDescription( program.getProgramDescription() );
      prgm.setClientName( program.getClientName() );
      prgm.setProgramStatus( OTSProgramStatusType.lookup( OTSProgramStatusType.INCOMPLETED ) );
      getOTSProgramService().save( prgm );

    }
    // Logic for CM asset
    OTSBatch otsBatch = getOTSProgramService().getOTSBatchByBatchNumber( new Long( batch.getBatchNumber() ) );
    if ( Objects.isNull( otsBatch ) )
    {
      otsBatch = new OTSBatch();
    }
    otsBatch.setBatchNumber( new Long( batch.getBatchNumber() ) );
    otsBatch.setOtsProgram( prgm );
    for ( BatchDescription batchDescription : batch.getBatchDescription() )
    {
      otsBatch = getOTSProgramService().saveBatchCmAsset( otsBatch, batchDescription.getCmText(), CmsUtil.getLocale( batchDescription.getLocale() ) );
    }

    getOTSProgramService().saveBatch( otsBatch );
    request.setAttribute( "programNumber", otsProgramNumber );
    ActionRedirect redirect = new ActionRedirect( mapping.findForward( "program_details" ) );
    redirect.addParameter( "programNumber", otsProgramNumber );

    return redirect;
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

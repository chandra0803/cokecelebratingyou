
package com.biperf.core.ui.ots;

import java.util.Objects;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.OTSProgramStatusType;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.ots.OTSService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.OTSUtil;
import com.biperf.core.value.ots.v1.program.Program;

public class AddOTSProgramAction extends BaseDispatchAction
{
  private static final Log log = LogFactory.getLog( AddOTSProgramAction.class );

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward addProgram( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionMessages errors = new ActionMessages();
    AddOTSProgramForm addOTSProgramForm = (AddOTSProgramForm)form;
    Program program = null;
    String programDetails = "program_details";

    if ( !Objects.isNull( addOTSProgramForm ) )
    {
      String otsProgramNumber = null;
      if ( !Objects.isNull( addOTSProgramForm.getProgramNumber() ) )
      {
        otsProgramNumber = addOTSProgramForm.getProgramNumber();
      }
      else if ( !Objects.isNull( request.getParameter( "programNumber" ) ) )
      {
        otsProgramNumber = request.getParameter( "programNumber" );
      }
      else if ( !Objects.isNull( request.getAttribute( "programNumber" ) ) )
      {
        otsProgramNumber = (String)request.getAttribute( "programNumber" );
      }

      otsProgramNumber = OTSUtil.checkLength( otsProgramNumber );
      if ( isValidProgramNumber( otsProgramNumber ) )
      {

        try
        {
          program = getOTSService().getOTSProgramInfo( otsProgramNumber );
          request.setAttribute( "otsProgramDetails", program );
          request.setAttribute( "batches", program.getBatches() );
        }
        catch( ServiceErrorException serviceException )
        {
          String exceptionValue = serviceException.getServiceErrors().get( 0 ).toString();
          exceptionValue = exceptionValue.substring( exceptionValue.indexOf( "[key:" ) + 5, exceptionValue.indexOf( "arg0:" ) - 2 );

          if ( !exceptionValue.contains( "IALS FOUND FOR GIVEN" ) && !exceptionValue.contains( "UNKNOWN" ) && !exceptionValue.contains( "UND WITH THESE VALUES" )
              && exceptionValue.contains( "ots.settings.info" ) )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( exceptionValue.trim() ) );

          }
          else
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "hometile.onTheSpotCard.SUBMISSION_SERVER_DOWN" ) );
          }

          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.FAIL_SEARCH );
        }

      }
      else
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "ots.settings.info.INVALIDPROGRAMID" ) );
        saveErrors( request, errors );
        log.error( "The program id is not valid : " + otsProgramNumber );
        return mapping.findForward( ActionConstants.FAIL_SEARCH );
      }
    }
    else
    {
      // Re-directing the user to program id entering page instead of throwing exception.
      log.error( "The program id form have null value " );
      return mapping.findForward( ActionConstants.FAIL_SEARCH );
    }

    return mapping.findForward( programDetails );
  }

  public ActionForward overView( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    saveProgramDetails( (AddOTSProgramForm)form );

    return mapping.findForward( ActionConstants.CANCEL_TO_TC_VIEW );
  }
  
  public ActionForward back( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    

    return mapping.findForward( ActionConstants.CANCEL_TO_TC_VIEW );
  }

  public ActionForward displayAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    OTSProgram otsProgram = null;

    AddOTSProgramForm addOTSProgramForm = (AddOTSProgramForm)form;
    if ( !Objects.isNull( addOTSProgramForm ) && !Objects.isNull( addOTSProgramForm.getProgramNumber() ) )
    {
      otsProgram = saveProgramDetails( addOTSProgramForm );

    }
    else if ( !Objects.isNull( request.getParameter( "programNumber" ) ) )
    {
      String otsProgramNumber = request.getParameter( "programNumber" );
      Program program = getOTSService().getOTSProgramInfo( otsProgramNumber );
      otsProgram = new OTSProgram();
      otsProgram.setProgramNumber( new Long( program.getProgramNumber() ) );
      otsProgram.setDescription( program.getProgramDescription() );
      otsProgram.setClientName( program.getClientName() );
      otsProgram.setProgramStatus( OTSProgramStatusType.lookup( OTSProgramStatusType.INCOMPLETED ) );
    }
    request.setAttribute( "otsProgramDetails", otsProgram );
    return mapping.findForward( ActionConstants.WIZARD_SAVE_AND_CONTINUE_ATTRIBUTE );
  }

  private boolean isValidProgramNumber( String otsProgramNumber ) throws PatternSyntaxException
  {
    String otsProgramPattern = "\\d{5}";
    return otsProgramNumber.matches( otsProgramPattern );
  }

  private OTSProgram saveProgramDetails( AddOTSProgramForm form )
  {
    AddOTSProgramForm programForm = form;
    OTSProgram program = null;
    if ( isValidProgramNumber( programForm.getProgramNumber() ) )
    {
      program = getOTSProgramService().getOTSProgramByProgramNumber( new Long( programForm.getProgramNumber() ) );

      if ( Objects.isNull( program ) )
      {
        program = new OTSProgram();
        program.setProgramStatus( OTSProgramStatusType.lookup( OTSProgramStatusType.INCOMPLETED ) );
      }

      program.setProgramNumber( new Long( programForm.getProgramNumber() ) );
      program.setDescription( programForm.getDescription() );
      program.setClientName( programForm.getClientName() );

      getOTSProgramService().save( program );

    }
    return program;
  }

  /**
   * Get the OTSService from the beanFactory locator.
   * 
   * @return OTSService
   */
  protected OTSProgramService getOTSProgramService()
  {
    return (OTSProgramService)getService( OTSProgramService.BEAN_NAME );
  }

  private OTSService getOTSService()
  {
    return (OTSService)getService( OTSService.BEAN_NAME );
  }
}
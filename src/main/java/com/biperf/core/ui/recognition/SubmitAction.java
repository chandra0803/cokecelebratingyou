
package com.biperf.core.ui.recognition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.BudgetUsageOverAllocallatedException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.ui.recognitionadvisor.RARecognitionFlowBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;

public class SubmitAction extends BaseSubmitRecognitionAction
{
  @Override
  protected SendRecognitionForm getRecognitionStateFor( HttpServletRequest request, ActionForm form )
  {
    return RecognitionStateManager.get( request );
  }

  @Override
  protected RecognitionClaimSource getSource()
  {
    return RecognitionClaimSource.WEB;
  }

  @Override
  protected ActionForward onSuccessfulSubmit( HttpServletRequest request,
                                              HttpServletResponse response,
                                              Long claimId,
                                              Long promotionId,
                                              String promotionType,
                                              List<BadgeDetails> badgeDetails,
                                              boolean isPurlRecognition,
                                              boolean allowManagerAward )
      throws IOException
  {
    // we got this far, so put the RecognitionSendBean in session so the confirmation can be
    // displayed
    RecognitionSentBean.addToSession( request, claimId, promotionId, promotionType, badgeDetails, isPurlRecognition, false );
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() && !UserManager.isUserDelegateOrLaunchedAs()
        && ( UserManager.getUser().isManager() || UserManager.getUser().isOwner() ) && promotionType.equals( "recognition" ) )
    {

      RARecognitionFlowBean.addToSession( request, "yes" );
      response.sendRedirect( request.getContextPath() + "/homePage.do" + RequestUtils.getFilterToken( request, FilterSetupType.HOME ) );

    }
    else if ( UserManager.getUser().isDelegate() )
    {
      response.sendRedirect( request.getContextPath() + "/homePage.do" );
    }
    else
    {
      response.sendRedirect( request.getContextPath() + "/homePage.do" + RequestUtils.getHomePageFilterToken( request ) );
    }

    return null;
  }

  @Override
  protected ActionForward onFailedSubmit( HttpServletRequest request,
                                          HttpServletResponse response,
                                          ActionMapping mapping,
                                          SendRecognitionForm recognitionForm,
                                          RecognitionClaimSubmissionResponse submissionResponse )
      throws IOException
  {
    request.setAttribute( "submitRecognitionValidationErrors", submissionResponse.getErrors() );
    RecognitionStateManager.store( recognitionForm, request );
    return mapping.findForward( "failure" );
  }

  @Override
  protected ActionForward onError( ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, SendRecognitionForm form, Throwable t, RecognitionClaimSubmission submission )
      throws IOException
  {
    Throwable cause = t.getCause();
    if ( cause instanceof BudgetUsageOverAllocallatedException )
    {
      BudgetUsageOverAllocallatedException exception = (BudgetUsageOverAllocallatedException)cause;
      return onBudgetUsageOverAllocatedException( request, response, mapping, form, exception );
    }

    logUnknownErrorDetailsToLog( t, submission );

    return onUnknownException( request, response, mapping, t );
  }

  @Override
  protected ActionForward onBudgetUsageOverAllocatedException( HttpServletRequest request,
                                                               HttpServletResponse response,
                                                               ActionMapping mapping,
                                                               SendRecognitionForm form,
                                                               BudgetUsageOverAllocallatedException exception )
      throws IOException
  {
    List<ServiceError> msgs = new ArrayList<>();
    for ( String message : exception.getServiceErrorsCMText() )
    {
      msgs.add( new ServiceError( "key", message ) );
    }

    request.setAttribute( "submitRecognitionValidationErrors", msgs );
    return mapping.findForward( "failure" );
  }

  @Override
  protected ActionForward onUnknownException( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, Throwable t ) throws IOException
  {

    if ( t instanceof ServiceErrorException )
    {
      List<ServiceError> serviceErrors = new ArrayList<>();
      List errors = ( (ServiceErrorException)t ).getServiceErrors();
      Iterator errorsItr = errors.iterator();

      while ( errorsItr.hasNext() )
      {
        ServiceError serviceError = new ServiceError( "key" );
        serviceError.setArg1( ( (ServiceError)errorsItr.next() ).getKey() );
        serviceErrors.add( serviceError );
      }

      request.setAttribute( "submitRecognitionValidationErrors", serviceErrors );
    }
    else if ( t instanceof BeaconRuntimeException )
    {
      ServiceError serviceError = new ServiceError( "key" );
      serviceError.setArg1( t.getLocalizedMessage() );
      List<ServiceError> serviceErrors = new ArrayList<>();
      serviceErrors.add( serviceError );
      request.setAttribute( "submitRecognitionValidationErrors", serviceErrors );
    }
    else
    {
      ServiceError serviceError = new ServiceError( "key" );
      serviceError.setArg1( "An unknown error occurred.  Please try again later." );
      List<ServiceError> serviceErrors = new ArrayList<>();
      serviceErrors.add( serviceError );
      request.setAttribute( "submitRecognitionValidationErrors", serviceErrors );
    }
    return mapping.findForward( "failure" );
  }

}

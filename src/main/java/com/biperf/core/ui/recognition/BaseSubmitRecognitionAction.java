
package com.biperf.core.ui.recognition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.exception.BudgetUsageOverAllocallatedException;
import com.biperf.core.service.claim.RecognitionClaimRecipient;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.utils.StringUtil;

public abstract class BaseSubmitRecognitionAction extends BaseRecognitionAction
{
  private static final Log logger = LogFactory.getLog( BaseSubmitRecognitionAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SendRecognitionForm recognitionState = getRecognitionStateFor( request, actionForm );
    boolean isPurlRecognition = recognitionState.getClaimContributorFormBeansCount() > 0;
    String promotionType = recognitionState.getPromotionType();
    String raFlow = request.getParameter( "isRARecognitionFlow" );
    RecognitionClaimSubmission submission = null;
    if ( !StringUtil.isEmpty( raFlow ) && getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() && promotionType.equalsIgnoreCase( "recognition" )
        && raFlow.equalsIgnoreCase( "yes" ) && !isPurlRecognition )
    {
      submission = RecognitionClaimSubmissionFactory.buildFrom( RecognitionClaimSource.WEB_RA, recognitionState );
    }
    else
    {
      submission = RecognitionClaimSubmissionFactory.buildFrom( getSource(), recognitionState );
    }
    Long claimId;
    List<BadgeDetails> badgeDetails = new ArrayList<>();
    try
    {
      RecognitionClaimSubmissionResponse submissionResponse = getClaimService().submitRecognition( submission );
      if ( !submissionResponse.isSuccess() )
      {
        return onFailedSubmit( request, response, mapping, recognitionState, submissionResponse );
      }
      claimId = submissionResponse.getClaimId();

      if ( claimId != null )
      {
        badgeDetails = getBadgeDetailsFor( claimId );
      }
    }
    catch( Throwable t )
    {
      return onError( mapping, request, response, recognitionState, t, submission );
    }

    // clear out the RecognitionState
    RecognitionStateManager.remove( request );
    request.getSession().removeAttribute( "submitPromotionPath" );
    return onSuccessfulSubmit( request, response, claimId, submission.getPromotionId(), promotionType, badgeDetails, isPurlRecognition, false );
  }

  protected abstract RecognitionClaimSource getSource();

  protected abstract SendRecognitionForm getRecognitionStateFor( HttpServletRequest request, ActionForm form );

  protected abstract ActionForward onSuccessfulSubmit( HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       Long claimId,
                                                       Long promotionId,
                                                       String promotionType,
                                                       List<BadgeDetails> badgeDetails,
                                                       boolean isPurlRecognition,
                                                       boolean allowManagerAward )
      throws IOException;

  protected abstract ActionForward onFailedSubmit( HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   ActionMapping mapping,
                                                   SendRecognitionForm recognitionForm,
                                                   RecognitionClaimSubmissionResponse submissionResponse )
      throws IOException;

  protected abstract ActionForward onError( ActionMapping mapping,
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            SendRecognitionForm form,
                                            Throwable t,
                                            RecognitionClaimSubmission submission )
      throws IOException;

  protected abstract ActionForward onBudgetUsageOverAllocatedException( HttpServletRequest request,
                                                                        HttpServletResponse response,
                                                                        ActionMapping mapping,
                                                                        SendRecognitionForm form,
                                                                        BudgetUsageOverAllocallatedException exception )
      throws IOException;

  protected abstract ActionForward onUnknownException( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, Throwable t ) throws IOException;

  protected void logUnknownErrorDetailsToLog( Throwable t, RecognitionClaimSubmission rcs )
  {
    String message = createMessageForLog( t, rcs );
    logger.error( message );
  }

  protected String createMessageForLog( Throwable t, RecognitionClaimSubmission submission )
  {
    StringBuilder sb = new StringBuilder();
    int index = 0;
    for ( RecognitionClaimRecipient rcr : submission.getRecognitionClaimRecipients() )
    {
      if ( index > 0 )
      {
        sb.append( ", " );
      }
      sb.append( rcr.getUserId() );
      index++;
    }

    String message = "\n*****************************\nERROR in " + this.getClass().getName() + "#execute: \n*****************************\n" + "\nError type: " + t.getClass().getName()
        + "\nRecognition submission details:" + "\n\t--> Submitter ID: " + submission.getSubmitterId() + "\n\t--> Promotion ID: " + submission.getPromotionId() + "\n\t--> Node ID:"
        + submission.getNodeId() + "\n\t--> Behavior: " + submission.getBehavior() + "\n\t--> Card ID:" + submission.getCardId() + "\n\t--> Certificate ID:" + submission.getCertificateId()
        + "\n\t--> recipient IDs: " + sb.toString().trim() + "\n\t--> Comments: " + submission.getComments() + "\n\nError stack trace:\n\n" + ExceptionUtils.getStackTrace( t )
        + "\n\n*****************************\nEND ERROR\n*****************************\n\n";

    return message;
  }

  protected List<BadgeDetails> getBadgeDetailsFor( Long claimId )
  {
    return getClaimService().getBadgeDetailsFor( claimId );
  }

}

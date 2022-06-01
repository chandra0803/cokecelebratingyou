
package com.biperf.core.ui.claim;

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
import com.biperf.core.service.claim.RecognitionClaimRecipient;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.recognition.RecognitionSentBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;

public class ManagerAddPointsSubmitAction extends ManagerAddPointsAction
{
  private static final Log logger = LogFactory.getLog( ManagerAddPointsSubmitAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // ManagerAddPointsState state = (ManagerAddPointsState)
    // request.getSession().getAttribute(ManagerAddPointsState.SESSION_KEY);
    ManagerAddPointsForm state = (ManagerAddPointsForm)request.getSession().getAttribute( ManagerAddPointsForm.SESSION_KEY );
    final Long SUBMITTER_ID = UserManager.getUserId();
    final Long PROMOTION_ID = state.getMgrPromotionId();
    final Long SUBMITTER_NODE_ID = state.getSubmitterNodeId();

    ManagerAddPointsForm form = (ManagerAddPointsForm)actionForm;

    // create the recognition claim submission
    RecognitionClaimSubmission submission = new RecognitionClaimSubmission( RecognitionClaimSource.WEB, SUBMITTER_ID, SUBMITTER_NODE_ID, PROMOTION_ID );
    submission.setComments( form.getComment() );
    submission.setManagerAward( true );

    // add recipient
    submission.addRecognitionClaimRecipient( state.getRecipientInfo().getId(),
                                             state.getRecipientBean().getNodeId(),
                                             form.getClaimRecipientFormBeans( 0 ).getAwardQuantity(),
                                             null,
                                             state.getRecipientInfo().getCountryCode());

    // now submit it
    List<BadgeDetails> badgeDetails = new ArrayList<>();
    Long claimId;
    try
    {
      RecognitionClaimSubmissionResponse submissionResponse = getClaimService().submitRecognition( submission );
      if ( !submissionResponse.isSuccess() )
      {
        // repopulate the form and redisplay
        form.setAwardType( state.getAwardType() );
        form.setAwardFixed( state.getAwardFixed() );
        form.setAwardMin( state.getAwardMin() );
        form.setAwardMax( state.getAwardMax() );

        form.setNodeBudgetJson( state.getNodeBudgetJson() );
        form.setRecipientInfo( state.getRecipientInfo() );
        form.setSubmitterInfo( state.getRecipientInfo() );

        // set the errors for display
        request.setAttribute( "managerAddPointsValidationErrors", submissionResponse.getErrors() );

        return mapping.findForward( "failure" );
      }

      claimId = submissionResponse.getClaimId();

      if ( claimId != null )
      {
        badgeDetails = getClaimService().getBadgeDetailsFor( claimId );
      }

    }
    catch( Throwable t )
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
      logger.error( message );

      ServiceError serviceError = new ServiceError( "key" );
      serviceError.setArg1( "An unknown error occurred.  Please try again later." );
      List<ServiceError> serviceErrors = new ArrayList<>();
      serviceErrors.add( serviceError );
      request.setAttribute( "managerAddPointsValidationErrors", serviceErrors );

      return mapping.findForward( "failure" );
    }

    // we got this far, so put the RecognitionSendBean in session so the confirmation can be
    // displayed
    RecognitionSentBean.addToSession( request, claimId, submission.getPromotionId(), "recognition", badgeDetails, false, true );

    // remove the state from session
    request.getSession().removeAttribute( ManagerAddPointsForm.SESSION_KEY );

    if ( UserManager.getUser().isDelegate() )
    {
      response.sendRedirect( request.getContextPath() + "/homePage.do" );
    }
    else
    {
      response.sendRedirect( request.getContextPath() + "/homePage.do" + RequestUtils.getHomePageFilterToken( request ) );
    }
    return null;
  }

}

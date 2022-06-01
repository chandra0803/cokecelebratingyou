
package com.biperf.core.ui.recognition.easy;

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
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.claim.RecognitionClaimRecipient;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.ui.recognition.RecognitionClaimSubmissionFactory;
import com.biperf.core.ui.recognition.RecognitionSentBean;
import com.biperf.core.ui.recognition.SendRecognitionForm;
import com.biperf.core.ui.recognition.SubmitAction;

public class SubmitEasyRecognitionAction extends SubmitAction
{
  private static final Log logger = LogFactory.getLog( SubmitEasyRecognitionAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SendRecognitionForm form = (SendRecognitionForm)actionForm;

    RecognitionClaimSubmission submission = RecognitionClaimSubmissionFactory.buildFrom( RecognitionClaimSource.WEB, form );

    SubmitEasyRecognitionBean bean;
    try
    {
      RecognitionClaimSubmissionResponse submitResponse = getClaimService().submitRecognition( submission );
      if ( submitResponse.isSuccess() )
      {
        // get the promotion type and set it on the form
        Promotion promotion = getPromotionService().getPromotionById( form.getPromotionId() );
        form.setPromotionType( promotion.getPromotionType().getCode() );

        // get the badge information
        List<BadgeDetails> badgeDetails = super.getBadgeDetailsFor( submitResponse.getClaimId() );
        RecognitionSentBean.addToRequest( request, submitResponse.getClaimId(), promotion.getId(), form.getPromotionType(), badgeDetails, false );
        return mapping.findForward( "success" );
      }
      else
      {
        bean = SubmitEasyRecognitionBean.failure( submitResponse.getErrors().get( 0 ).getArg1() );
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

      bean = SubmitEasyRecognitionBean.failure( "An unknown failure occurred." );
    }

    writeAsJsonToResponse( bean, response );

    return null;
  }
}

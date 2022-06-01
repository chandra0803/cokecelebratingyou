
package com.biperf.core.ui.mobilerecogapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BudgetUsageOverAllocallatedException;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.recognition.BaseSubmitRecognitionAction;
import com.biperf.core.ui.recognition.RecognitionSentBean;
import com.biperf.core.ui.recognition.SendRecognitionForm;
import com.biperf.core.ui.recognition.SupportedEcardVideoTypes;
import com.objectpartners.cms.util.ContentReaderManager;

public class SubmitRecognitionAction extends BaseSubmitRecognitionAction
{

  @Override
  protected SendRecognitionForm getRecognitionStateFor( HttpServletRequest request, ActionForm form )
  {
    SendRecognitionForm sendRecognitionForm = (SendRecognitionForm)form;

    // since the mobile app doesn't pass in any info about copy manager, check to see if the
    // promotion is setup for mandatory copy of manager
    RecognitionPromotion promo = (RecognitionPromotion)getPromotionService().getPromotionById( sendRecognitionForm.getPromotionId() );
    sendRecognitionForm.setSendCopyToManager( promo.isCopyRecipientManager() );

    // The app does not send both video and image url for videos. It will only send the video url,
    // in the 'cardUrl' field.
    // In the case of a video upload, we need to flip the fields around. cardUrl is the video, and
    // using the video, we'll look up the thumbnail image.
    if ( SendRecognitionForm.CARD_TYPE_UPLOAD.equals( sendRecognitionForm.getCardType() ) && StringUtils.isNotBlank( sendRecognitionForm.getCardUrl() ) )
    {
      if ( SupportedEcardVideoTypes.isSupportedVideo( sendRecognitionForm.getCardUrl() ) )
      {
        String videoUrl = sendRecognitionForm.getCardUrl();
        sendRecognitionForm.setCardUrl( null );
        sendRecognitionForm.setVideoUrl( videoUrl );

        String thumbnailUrl = getMultimediaService().getThumbnailByVideo( videoUrl );
        sendRecognitionForm.setVideoImageUrl( thumbnailUrl );
      }
    }

    return sendRecognitionForm;
  }

  @Override
  protected RecognitionClaimSource getSource()
  {
    return RecognitionClaimSource.MOBILE;
  }

  @Override
  protected ActionForward onFailedSubmit( HttpServletRequest request,
                                          HttpServletResponse response,
                                          ActionMapping mapping,
                                          SendRecognitionForm recognitionForm,
                                          RecognitionClaimSubmissionResponse submissionResponse )
      throws IOException
  {
    List<String> errorMessages = new ArrayList<>();
    for ( ServiceError error : submissionResponse.getErrors() )
    {
      errorMessages.add( error.getKey() );
    }

    SuccessfulRecognitionSubmit submitBean = new SuccessfulRecognitionSubmit( errorMessages );

    super.writeAsJsonToResponse( submitBean, response );
    return null;
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
    String title = ContentReaderManager.getText( "recognition.confirmation", "THANK_YOU" );
    String message = ContentReaderManager.getText( "recognition.confirmation", "SUBMITTED_MESSAGE" );
    RecognitionSentBean bean = RecognitionSentBean.create( claimId, promotionId, promotionType, badgeDetails, isPurlRecognition, false, allowManagerAward );

    SuccessfulRecognitionSubmit submitBean = new SuccessfulRecognitionSubmit( title, message, bean );
    super.writeAsJsonToResponse( submitBean, response );

    return null;
  }

  @Override
  protected ActionForward onUnknownException( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, Throwable t ) throws IOException
  {
    SuccessfulRecognitionSubmit submitBean = new SuccessfulRecognitionSubmit( Arrays.asList( "An unknown error occurred." ) );

    super.writeAsJsonToResponse( submitBean, response );
    return null;
  }

  @Override
  protected ActionForward onBudgetUsageOverAllocatedException( HttpServletRequest request,
                                                               HttpServletResponse response,
                                                               ActionMapping mapping,
                                                               SendRecognitionForm form,
                                                               BudgetUsageOverAllocallatedException exception )
      throws IOException
  {
    List<String> errors = exception.getServiceErrorsCMText();
    SuccessfulRecognitionSubmit submitBean = new SuccessfulRecognitionSubmit( errors );

    super.writeAsJsonToResponse( submitBean, response );
    return null;
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

  private MultimediaService getMultimediaService()
  {
    return (MultimediaService)getService( MultimediaService.BEAN_NAME );
  }

  public static class SuccessfulRecognitionSubmit
  {
    private final boolean success;
    private final String title;
    private final String message;
    private final RecognitionSentBean details;
    private final List<String> errorMessages;

    public SuccessfulRecognitionSubmit( String title, String message, RecognitionSentBean details )
    {
      this.success = true;
      this.title = title;
      this.message = message;
      this.details = details;
      this.errorMessages = null;
    }

    public SuccessfulRecognitionSubmit( List<String> errorMessages )
    {
      this.success = false;
      this.title = null;
      this.message = null;
      this.details = null;
      this.errorMessages = errorMessages;
    }

    public boolean isSuccess()
    {
      return success;
    }

    public String getTitle()
    {
      return title;
    }

    public String getMessage()
    {
      return message;
    }

    public RecognitionSentBean getDetails()
    {
      return details;
    }

    public List<String> getErrorMessages()
    {
      return errorMessages;
    }

  }

}

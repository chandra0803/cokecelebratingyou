/**
 * 
 */

package com.biperf.core.ui.recognition;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.value.RecognitionBean;

/**
 * @author poddutur
 *
 */
public class SendNominationDisplayAction extends BaseSendRecognitionDisplayAction
{

  @Override
  protected SendRecognitionForm getRecognitionState( HttpServletRequest request )
  {
    return RecognitionStateManager.get( request );
  }

  @Override
  public SendRecognitionForm getRecognitionForm( HttpServletRequest request )
  {
    return RecognitionStateManager.get( request );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected List<RecognitionBean> getPromotions( Long userId, boolean isUserAParticipant, List eligiblePromotions )
  {
    return getPromotionService().getNominationSubmissionList( userId, isUserAParticipant );
  }

  @Override
  protected void setPromotionType( SendRecognitionForm form )
  {
    form.setPromotionType( SendRecognitionForm.PROMO_TYPE_NOMINATION );
  }

}

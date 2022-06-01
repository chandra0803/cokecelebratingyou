
package com.biperf.core.ui.recognition;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;

public class SendRecognitionDisplayAction extends BaseSendRecognitionDisplayAction
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

  @Override
  protected void setPromotionType( SendRecognitionForm form )
  {
    Long promotionId = form.getPromotionId();

    if ( promotionId != null )
    {
      AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)getPromotionService().getPromotionById( promotionId );
      if ( !promotion.isNominationPromotion() )
      {
        form.setPromotionType( SendRecognitionForm.PROMO_TYPE_RECOGNITION );
      }
    }

  }

}

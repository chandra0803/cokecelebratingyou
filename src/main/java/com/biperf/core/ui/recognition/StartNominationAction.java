
package com.biperf.core.ui.recognition;

import java.util.List;

import com.biperf.core.value.RecognitionBean;

public class StartNominationAction extends StartAction
{
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

  // git pul request

}

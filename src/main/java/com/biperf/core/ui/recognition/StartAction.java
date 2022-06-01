
package com.biperf.core.ui.recognition;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.utils.ClientStateUtils;

public class StartAction extends BaseSendRecognitionDisplayAction
{
  @Override
  protected SendRecognitionForm getRecognitionState( HttpServletRequest request )
  {
    // make sure any previous state is removed
    RecognitionStateManager.get( request );

    SendRecognitionForm form = new SendRecognitionForm();

    if ( ClientStateUtils.getClientStateMap( request ) != null )
    {
      String promotionId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" );
      if ( promotionId != null )
      {
        form.setPromotionId( new Long( promotionId ) );
      }
    }

    return form;
  }

  public SendRecognitionForm getRecognitionForm( HttpServletRequest request )
  {
    Long promotionId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );

    SendRecognitionForm form = new SendRecognitionForm();

    if ( promotionId != null )
    {
      form.setPromotionId( promotionId );
    }

    return form;
  }

  @Override
  protected void setPromotionType( SendRecognitionForm form )
  {
    form.setPromotionType( SendRecognitionForm.PROMO_TYPE_RECOGNITION );
  }
}


package com.biperf.core.ui.mobilerecogapp;

import java.util.concurrent.ExecutorService;

import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.BeanLocator;

public class BaseSendRecognitionAction extends BaseDispatchAction
{
  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected ExecutorService getExecutorService()
  {
    return (ExecutorService)BeanLocator.getBean( "executorService" );
  }

  protected static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}

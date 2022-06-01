
package com.biperf.core.ui.survey;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

public class SurveyPageController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "surveySubmissionList", getPromotionService().getPendingSurveysList( UserManager.getUserId() ) );
  }

  /**
   * Get ParticipantService
   * @return
   */
  public static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Gets a PromotionService
   * 
   * @return PromotionService
   * @throws Exception
   */
  private PromotionService getPromotionService() throws Exception
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}

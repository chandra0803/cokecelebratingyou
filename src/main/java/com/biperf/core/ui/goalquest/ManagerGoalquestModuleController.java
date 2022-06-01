
package com.biperf.core.ui.goalquest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

public class ManagerGoalquestModuleController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    String promotionTypeCode = PromotionType.GOALQUEST;
    List<PromotionMenuBean> goalquestPromotionMenuBeans = getGoalquestService().getManagerPromotionMenuBeans( eligiblePromotions, participant, promotionTypeCode );
    List<ManagerGoalquestViewBean> honeycombViewBeans = getGoalquestService().getHoneycombManagerPrograms( participant.getHoneycombUserId() );

    // A single honeycomb goalquest program means we SSO over to honeycomb in a new tab
    if ( goalquestPromotionMenuBeans.isEmpty() && honeycombViewBeans.size() == 1 )
    {
      ManagerGoalquestViewBean viewBean = honeycombViewBeans.get( 0 );
      String programId = viewBean.getPromotionId().toString();
      String honeycombSSOLink = getGoalquestService().buildGoalquestSSOLink( viewBean.getRole(), programId, null );
      request.setAttribute( "honeycombSSOLink", honeycombSSOLink );
      request.setAttribute( "isHoneycombLink", true );
    }
  }
  
  private GoalQuestService getGoalquestService()
  {
    return (GoalQuestService)BeanLocator.getBean( GoalQuestService.BEAN_NAME );
  }
  
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }
  
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}

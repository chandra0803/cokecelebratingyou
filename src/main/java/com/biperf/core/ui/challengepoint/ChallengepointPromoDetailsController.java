/**
 * 
 */

package com.biperf.core.ui.challengepoint;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.challengepoint.ChallengepointReviewProgress;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ChallengePointIncrementStrategyFactory;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.ChallengepointPaxValueBean;

/**
 * ChallengepointPromoDetailsController.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>reddy</td>
 * <td>Jul 16, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class ChallengepointPromoDetailsController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long userId = null;
    Long promotionId = null;
    Boolean adminView = null;
    ChallengepointPaxValueBean cpPaxValueBean = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      if ( clientStateMap.get( "userId" ) != null && clientStateMap.get( "userId" ).toString().length() > 0 )
      {
        try
        {
          userId = (Long)clientStateMap.get( "userId" );
        }
        catch( ClassCastException cce )
        {
          userId = new Long( (String)clientStateMap.get( "userId" ) );
        }
      }
      if ( clientStateMap.get( "promotionId" ) != null && clientStateMap.get( "promotionId" ).toString().length() > 0 )
      {
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
        }
      }
      if ( userId == null || promotionId == null )
      {
        clientStateMap = (Map)request.getAttribute( "clientStateParameterMap" );
        try
        {
          userId = (Long)clientStateMap.get( "userId" );
        }
        catch( ClassCastException cce )
        {
          userId = new Long( (String)clientStateMap.get( "userId" ) );
        }
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
        }
      }

      if ( promotionId != null )
      {
        request.setAttribute( "promotionId", promotionId );
      }
      if ( userId != null )
      {
        request.setAttribute( "userId", userId );
      }
      else
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
      }

      if ( promotionId != null && userId != null )
      {
        ChallengePointPromotion cpPromotion = (ChallengePointPromotion)getPromotionService().getPromotionById( promotionId );
        PaxGoal paxGoal = getChallengePointService().getPaxChallengePoint( cpPromotion.getId(), userId );
        if ( paxGoal != null )
        {
          cpPaxValueBean = getChallengePointService().populateChallengepointPaxValueBean( paxGoal, (ChallengePointPromotion)paxGoal.getGoalQuestPromotion(), (GoalLevel)paxGoal.getGoalLevel() );
        }
        else
        {
          cpPaxValueBean = new ChallengepointPaxValueBean();
          cpPaxValueBean.setPromotion( cpPromotion );
          cpPaxValueBean.setCalculatedThreshold( getChallengePointService().getCalculatedThreshold( promotionId, userId ) );
          ChallengePointIncrementStrategyFactory challengePointIncrementStrategyFactory = getChallengePointIncrementStrategyFactory();
          cpPaxValueBean
              .setCalculatedIncrementAmount( challengePointIncrementStrategyFactory.getChallengePointIncrementStrategy( cpPromotion.getAwardIncrementType() ).processIncrement( cpPromotion, userId ) );

        }

        Map<String, Object> progressMap = getChallengepointProgressService().getChallengepointProgressByPromotionIdAndUserId( promotionId, userId );
        List progressList = (List)progressMap.get( "challengepointProgressList" );
        ChallengepointReviewProgress cpReviewProgress = null;
        if ( progressList != null && progressList.size() > 0 )
        {
          // latest will be the first row change made for progress page
          cpReviewProgress = (ChallengepointReviewProgress)progressList.get( 0 );
          request.setAttribute( "cpReviewProgress", cpReviewProgress );
        }
        // Fix 21285 update cp should display upto cpcalculaiton end date in admin view
        if ( cpPromotion.getGoalCollectionStartDate() != null && cpPromotion.getFinalProcessDate() != null )
        {
          if ( DateUtils.isTodaysDateBetween( cpPromotion.getGoalCollectionStartDate(), cpPromotion.getFinalProcessDate() ) )
          {
            request.setAttribute( "displayUpdateCp", Boolean.TRUE );
          }
          else
          {
            request.setAttribute( "displayUpdateCp", Boolean.FALSE );
          }
        }
        else
        {
          request.setAttribute( "displayUpdateCp", Boolean.FALSE );
        }

        // to display the update base button
        if ( cpPromotion.getFinalProcessDate().after( DateUtils.getCurrentDate() ) )
        {
          request.setAttribute( "afterFinalProcessDate", Boolean.TRUE );
        }
        else
        {
          request.setAttribute( "afterFinalProcessDate", Boolean.FALSE );
        }
        request.setAttribute( "promotionId", promotionId );
        request.setAttribute( "paxValueBean", cpPaxValueBean );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private ChallengePointService getChallengePointService()
  {
    return (ChallengePointService)getService( ChallengePointService.BEAN_NAME );
  }

  private ChallengepointProgressService getChallengepointProgressService()
  {
    return (ChallengepointProgressService)getService( ChallengepointProgressService.BEAN_NAME );
  }

  /**
   * Get the ChallengePointIncrementStrategyFactory from the beanLocator.
   * 
   * @return ChallengePointIncrementStrategyFactory
   */
  private ChallengePointIncrementStrategyFactory getChallengePointIncrementStrategyFactory()
  {
    return (ChallengePointIncrementStrategyFactory)BeanLocator.getBean( ChallengePointIncrementStrategyFactory.BEAN_NAME );
  }
}

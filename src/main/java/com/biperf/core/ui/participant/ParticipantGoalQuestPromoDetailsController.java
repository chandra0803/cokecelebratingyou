/**
 * 
 */

package com.biperf.core.ui.participant;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.awardslinqDataRetriever.client.ProductGroupDescription;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.goalquest.GoalQuestReviewProgress;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.MerchLinqProductDataUtils;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.GoalLevelValueBean;

/**
 * ParticipantGoalQuestPromoDetailsController.
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
 * <td>sedey</td>
 * <td>Jan , 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantGoalQuestPromoDetailsController extends BaseController
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
        GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionById( promotionId );
        Map progressMap = getGoalQuestService().getGoalQuestProgressByPromotionIdAndUserId( promotionId, userId );

        GoalQuestReviewProgress goalQuestReviewProgress = null;

        List progressList = (List)progressMap.get( "goalQuestProgressList" );

        if ( progressList != null && progressList.size() > 0 )
        {
          goalQuestReviewProgress = (GoalQuestReviewProgress)progressList.get( progressList.size() - 1 );
        }
        request.setAttribute( "goalQuestProgress", goalQuestReviewProgress );
        PaxGoal paxGoal = (PaxGoal)progressMap.get( "paxGoal" );
        if ( paxGoal != null )
        {
          GoalLevelValueBean goalLevelValueBean = null;
          // GoalQuestPromotion promotion = paxGoal.getGoalQuestPromotion();
          // if(promotion!=null) {
          if ( goalQuestPromotion.getFinalProcessDate().after( DateUtils.getCurrentDate() ) )
          {
            request.setAttribute( "afterFinalProcessDate", Boolean.TRUE );
          }
          else
          {
            request.setAttribute( "afterFinalProcessDate", Boolean.FALSE );

          }
          // }
          if ( paxGoal.getGoalLevel() instanceof ManagerOverrideGoalLevel )
          {
            goalLevelValueBean = SelectGoalUtil.populateGoalLevelValueBean( (ManagerOverrideGoalLevel)paxGoal.getGoalLevel() );
          }
          else
          {
            goalLevelValueBean = SelectGoalUtil.populateGoalLevelValueBean( paxGoal, goalQuestPromotion, (GoalLevel)paxGoal.getGoalLevel() );
          }
          if ( goalQuestPromotion.getProgramId() != null && paxGoal.getProductSetId() != null )
          {
            goalLevelValueBean.setSelectedProduct( MerchLinqProductDataUtils.getProduct( goalQuestPromotion.getProgramId(), paxGoal.getProductSetId() ) );
            Locale locale = LocaleUtils.findClosestMatchingLocale( UserManager.getLocale(), goalLevelValueBean.getSelectedProduct().getProductGroupDescriptions().keySet() );
            ProductGroupDescription description = goalLevelValueBean.getSelectedProduct().getProductGroupDescriptions().get( locale );
            request.setAttribute( "productThumb", goalLevelValueBean.getSelectedProduct().getThumbnailImageURL().toString() );
            if ( description != null )
            {
              request.setAttribute( "productDescription", description.getDescription() );
            }

          }
          request.setAttribute( "paxGoal", paxGoal );
          request.setAttribute( "goalLevelValueBean", goalLevelValueBean );
        }
        else if ( goalQuestPromotion != null )
        {
          paxGoal = new PaxGoal();
          paxGoal.setGoalQuestPromotion( goalQuestPromotion );
          request.setAttribute( "paxGoal", paxGoal );
        }
        request.setAttribute( "promotionId", promotionId );
        if ( goalQuestPromotion != null && goalQuestPromotion.getAchievementRule().getCode().equalsIgnoreCase( AchievementRuleType.PERCENT_OF_BASE ) )
        {
          request.setAttribute( "isBaseGoal", Boolean.TRUE );
        }
        else
        {
          request.setAttribute( "isBaseGoal", Boolean.FALSE );
        }

        if ( goalQuestPromotion != null && goalQuestPromotion.getProgressLoadType() != null && goalQuestPromotion.getProgressLoadType().getCode().equals( ProgressLoadType.AUTOMOTIVE ) )
        {
          request.setAttribute( "isAutomotive", Boolean.TRUE );
        }
        else
        {
          request.setAttribute( "isAutomotive", Boolean.FALSE );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  /**
   * Get the GoalQuestService from the beanLocator.
   * 
   * @return GoalQuestService
   */
  private GoalQuestService getGoalQuestService()
  {
    return (GoalQuestService)getService( GoalQuestService.BEAN_NAME );
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
}

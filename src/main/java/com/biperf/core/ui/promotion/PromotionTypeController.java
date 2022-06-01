/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

/**
 * PromotionTypeController.
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
 * <td>sondgero</td>
 * <td>Jun 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionTypeController extends BaseController
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

    List promotionTypeList = getDisplayPromotionList();

    // PromotionTypes
    request.setAttribute( "promotionTypeList", promotionTypeList );
  }

  // Only one live DIY promotion allowed. If there is already a live promotion then do not display
  // the DIY Quiz. Same with Engagement Promotion.
  protected List getDisplayPromotionList()
  {
    List promotionTypeList = PromotionType.getPromotionList();
    List<PromotionType> filteredPromotionList = new ArrayList<PromotionType>();

    QuizPromotion liveOrCompletedDIYQuizPromotion = null;
    EngagementPromotion liveEngagementPromotion = null;
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_QUIZZES ).getBooleanVal() )
    {
      liveOrCompletedDIYQuizPromotion = getPromotionService().getLiveOrCompletedDIYQuizPromotion();
    }

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_ENGAGEMENT ).getBooleanVal() )
    {
      liveEngagementPromotion = getPromotionService().getLiveOrCompletedEngagementPromotion();
    }

    Iterator promotionTypeIter = promotionTypeList.iterator();
    while ( promotionTypeIter.hasNext() )
    {
      PromotionType promotionType = (PromotionType)promotionTypeIter.next();

      if ( ! ( liveOrCompletedDIYQuizPromotion != null && PromotionType.DIY_QUIZ.equals( promotionType.getCode() ) ) && !PromotionType.BADGE.equals( promotionType.getCode() )
          && ! ( liveEngagementPromotion != null && PromotionType.ENGAGEMENT.equals( promotionType.getCode() ) ) )
      {
        filteredPromotionList.add( promotionType );
      }
    }
    return filteredPromotionList;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}

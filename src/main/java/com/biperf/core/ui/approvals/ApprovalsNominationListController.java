/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsNominationListController.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ApproverLevelType;
import com.biperf.core.domain.enums.PickListItemSortOrderComparator;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.process.NominationAutoNotificationProcess;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionsValueBean;

/**
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
 * <td></td>
 * <td></td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsNominationListController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ApprovalsNominationListForm nominationApprovalListForm = (ApprovalsNominationListForm)request.getSession().getAttribute( "approvalsNominationListForm" );

    buildPromotionList( request );

    List<ApprovalStatusType> approvalStatusTypes = ApprovalStatusType.getList( PromotionType.NOMINATION, new PickListItemSortOrderComparator() );

    request.setAttribute( "approvalStatusTypes", approvalStatusTypes );
    // Client customization for WIP #56492 starts
    List<ApproverLevelType> approverLevelTypes = ApproverLevelType.getList( );
    request.setAttribute( "approverLevelTypes", approverLevelTypes );
    // Client customization for WIP #56492 ends
    
    // Client customization for WIP 58122
    List<TccNomLevelPayout> levelPayouts=getCokeClientService().getLevelTotalPoints( new Long( nominationApprovalListForm.getPromotionId()));
    Collections.sort( levelPayouts, ASCE_COMPARATOR_LEVEL_PAYOUTS );
    int i=1;
    for ( Iterator iter = levelPayouts.iterator(); iter.hasNext(); )
    {
    	TccNomLevelPayout t = (TccNomLevelPayout)iter.next();
    	t.setLevelId("level"+i);    	
    	i++;
    }
    request.setAttribute( "levelPayouts", levelPayouts );
    // Client customization for WIP 58122

    // Build notification Process Time, if a notification process has been scheduled.
    buildNotificationProcessTime( request );
    request.setAttribute( "tomorrowDate", com.biperf.core.utils.DateUtils.toDisplayString( DateUtils.addDays( new Date(), 1 ) ) );

    if ( nominationApprovalListForm != null && StringUtils.isNotEmpty( nominationApprovalListForm.getPromotionId() )
        && StringUtils.isNotEmpty( nominationApprovalListForm.getFilterApprovalStatusCode() ) )
    {
      List<Approvable> approvables = (List<Approvable>)request.getAttribute( ApprovalsNominationListAction.ATTR_APPROVABLE_LIST );
      if ( approvables == null )
      {
        approvables = ApprovalsNominationListAction.buildApprovablesList( nominationApprovalListForm, true, false );
        nominationApprovalListForm.setRequestedPage( 1L );
        request.setAttribute( ApprovalsNominationListAction.ATTR_APPROVABLE_LIST, approvables );
        NominationPromotion nominationPromotion = (NominationPromotion)getPromotionService().getPromotionById( Long.valueOf( nominationApprovalListForm.getPromotionId() ) );
        nominationApprovalListForm.load( approvables, nominationPromotion );
      }
      // Client customization for WIP 58122
      String approvalRound=null;
      if(!approvables.isEmpty())
       approvalRound =approvables.get(0).getApprovalRound().toString();
      request.setAttribute( "approvalRound", approvalRound );
      request.setAttribute( "showExports", approvables == null ? false : approvables.size() > 0 ? true : false );
      NominationPromotion promotion = (NominationPromotion)getPromotionService().getPromotionById( new Long( nominationApprovalListForm.getPromotionId() ) );
      request.setAttribute( "promotion", promotion );
      // Client customization for WIP 58122
      int capPerPax=0;
      if(promotion.getCapPerPax()!=null)
       capPerPax=promotion.getCapPerPax();
      boolean final_Approver=false;
      if(promotion.getApprovalNodeLevels()!=null && promotion.getApprovalNodeLevels().toString().equals(approvalRound))
      {
    	  final_Approver=true; 
      }
      request.setAttribute( "final_Approver",final_Approver);
      request.setAttribute( "capPerPax", capPerPax );
      // Client customization for WIP 58122
      String filterApprovalStatusCode = nominationApprovalListForm.getFilterApprovalStatusCode();
      if ( StringUtils.isNotEmpty( filterApprovalStatusCode ) )
      {
        request.setAttribute( "filterApprovalStatusName", ApprovalStatusType.lookup( filterApprovalStatusCode ).getName() );
        request.setAttribute( "filterApprovalStatusCode", filterApprovalStatusCode );
      }

    }

  }

  /**
   * BUild a list of all live and expired nomination promotions. Mark those with pending approval
   * required by the current approver wiht an asterisk after the promotion name.
   * 
   * @param request
   * @throws Exception
   */
  private void buildPromotionList( HttpServletRequest request ) throws Exception
  {
    List<PromotionsValueBean> promotionList = getPromotionService().getAllSortedApproverPromotions( UserManager.getUserId(), PromotionType.NOMINATION );

    request.setAttribute( "promotionList", promotionList );
  }

  public static void buildNotificationProcessTime( HttpServletRequest request )
  {
    Long timeOfDayMillis = getProcessService().getTimeOfDayMillisOfFirstSchedule( NominationAutoNotificationProcess.BEAN_NAME );

    Date timeOfDayAsDate = getProcessService().getTimeOfDayOfFirstScheduleDate( NominationAutoNotificationProcess.BEAN_NAME );

    if ( timeOfDayMillis != null )
    {
      request.setAttribute( "autoNotificationTimeOfDay", timeOfDayAsDate );

      request.setAttribute( "autoNotificationHour", String.valueOf( timeOfDayMillis.longValue() / DateUtils.MILLIS_PER_HOUR ) );
      request.setAttribute( "autoNotificationMinute", String.valueOf( timeOfDayMillis.longValue() / DateUtils.MILLIS_PER_MINUTE % 60 ) );
    }
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the processService from the applicationContext.
   * 
   * @return ProcessService
   */
  private static ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }
  // Client customization for WIP 58122
  private static CokeClientService getCokeClientService()
  {
    return (CokeClientService)getService( CokeClientService.BEAN_NAME );
  }
  private static Comparator<TccNomLevelPayout> ASCE_COMPARATOR_LEVEL_PAYOUTS = new Comparator<TccNomLevelPayout>()
  {
    public int compare( TccNomLevelPayout c1, TccNomLevelPayout c2 )
    {
      return c1.getLevelDescription().compareTo( c2.getLevelDescription() );
    }
  };

}

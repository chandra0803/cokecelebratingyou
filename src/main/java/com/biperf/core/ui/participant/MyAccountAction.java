/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/MyAccountAction.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.participant.AccountSummary;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.claim.RecognitionHistoryListController.ValueObjectBuilder;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.MyAccountController.GoalQuestBuilder;
import com.biperf.core.ui.participant.MyAccountController.RecognitionReceiveBuilder;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/**
 * MyAccountAction.
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
 * <td>zahler</td>
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MyAccountAction extends BaseDispatchAction
{
  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    MyAccountForm myAccountForm = (MyAccountForm)form;
    ActionMessages errors = new ActionMessages();

    String forward = ActionConstants.DETAILS_FORWARD;

    // front end already doing date comparison, just a double check
    if ( myAccountForm.getStartDate() != null && myAccountForm.getEndDate() != null )
    {
      if ( DateUtils.toDate( myAccountForm.getStartDate() ).after( DateUtils.toDate( myAccountForm.getEndDate() ) ) )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "participant.myaccount.errors.START_DATE_AFTER_END_DATE" ) );
        saveErrors( request, errors );
      }
    }
    prepareForDisplay( myAccountForm, request, errors );

    if ( !errors.isEmpty() )
    {
      forward = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forward );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward printerFriendly( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    MyAccountForm myAccountForm = (MyAccountForm)form;

    ActionMessages errors = new ActionMessages();
    String forward = ActionConstants.PRINTER_FRIENDLY_FORWARD;

    prepareForDisplay( myAccountForm, request, errors );

    if ( !errors.isEmpty() )
    {
      forward = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forward );
  }

  private void prepareForDisplay( MyAccountForm myAccountForm, HttpServletRequest request, ActionMessages errors )
  {
    Date startDate;
    Date endDate;

    String timeZoneID = getUserService().getUserTimeZone( UserManager.getUserId() );
    // set defaults if startDate or endDate are null
    if ( myAccountForm.getStartDate() == null || myAccountForm.getStartDate().equals( "" ) )
    {
      startDate = DateUtils.getOneMonthBeforeAsString( DateUtils.getCurrentDate() );
      myAccountForm.setStartDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( startDate, timeZoneID ) ) );
    }
    else
    {
      startDate = DateUtils.toDate( myAccountForm.getStartDate() );
    }

    if ( myAccountForm.getEndDate() == null || myAccountForm.getEndDate().equals( "" ) )
    {
      endDate = DateUtils.getCurrentDate();
      myAccountForm.setEndDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( endDate, timeZoneID ) ) );
    }
    else
    {
      endDate = DateUtils.toDate( myAccountForm.getEndDate() );
    }

    try
    {
      boolean displaySummary = false;

      if ( !isGiftCodeOnlyPax() )
      {
        int accountTransactionCount = 0;
        if ( !UserManager.getUser().isHasAwardbanqNbrAtLogin() )
        {
          getAwardBanQService().enrollParticipantInAwardBanQ( UserManager.getUserId() );
        }
        AccountSummary accountSummary = getAwardBanQService().getAccountSummaryByParticipantIdAndDateRange( UserManager.getUserId(), startDate, endDate );

        accountTransactionCount = accountSummary.getAccountTransactions().size();

        displaySummary = true;

        request.setAttribute( "accountSummary", accountSummary );
        request.setAttribute( "accountTransactionCount", accountTransactionCount );
        request.setAttribute( "displaySummary", displaySummary );
      }

      Long[] merchPromotionIds = getMerchandisePromotionIds();
      if ( merchPromotionIds != null && merchPromotionIds.length > 0 )
      {
        Long submitterId = UserManager.getUserId();
        ValueObjectBuilder builder = null;
        builder = new RecognitionReceiveBuilder( submitterId, startDate, endDate, merchPromotionIds );
        List valueObjectList = new ArrayList();
        valueObjectList.addAll( builder.buildValueObjects() );
        builder = new GoalQuestBuilder( submitterId, startDate, endDate, merchPromotionIds );
        valueObjectList.addAll( builder.buildValueObjects() );
        request.setAttribute( "valueObjects", valueObjectList );
      }

    }
    catch( ServiceErrorException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "participant.myaccount.errors.AWARDBANQ_ERROR" ) );
      saveErrors( request, errors );
    }
    catch( Exception e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "participant.myaccount.errors.AWARDBANQ_ERROR" ) );
      saveErrors( request, errors );
    }
  }

  private boolean isGiftCodeOnlyPax()
  {
    return UserManager.getUser().isParticipant() && UserManager.getUser().isGiftCodeOnlyPax();
  }

  private Long[] getMerchandisePromotionIds()
  {
    List merchPromotionIds = getPromotionService().getMerchandisePromotionIds();

    int size = merchPromotionIds.size();
    Long[] merchPromotionIdArray = new Long[size];
    for ( int i = 0; i < size; i++ )
    {
      merchPromotionIdArray[i] = (Long)merchPromotionIds.get( i );
    }

    return merchPromotionIdArray;
  }

  // bug fix # 25747
  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception

  {
    return display( mapping, form, request, response );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }

  /**
   * Get the ShoppingService From the bean factory through a locator.
   * 
   * @return ShoppingService
   */
  private ShoppingService getShoppingService()
  {
    return (ShoppingService)getService( ShoppingService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
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

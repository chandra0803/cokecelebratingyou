/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/process/ProcessLaunchAction.java,v $
 *
 */

package com.biperf.core.ui.process;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.process.Process;
import com.biperf.core.process.AdminTestBadgeReceivedEmailProcess;
import com.biperf.core.process.AdminTestBudgetReminderNotificationEmailProcess;
import com.biperf.core.process.AdminTestCelebMgrNonResponseEmailProcess;
import com.biperf.core.process.AdminTestCelebMgrNotificationEmailProcess;
import com.biperf.core.process.AdminTestCelebRecogReceivedEmailProcess;
import com.biperf.core.process.AdminTestCelebrationPurlRecipientEmailProcess;
import com.biperf.core.process.AdminTestDepositNoticeEmailProcess;
import com.biperf.core.process.AdminTestEstatementEmailProcess;
import com.biperf.core.process.AdminTestGoalQuestLoginPasswordEmailProcess;
import com.biperf.core.process.AdminTestPurlContributorsInvitationEmailProcess;
import com.biperf.core.process.AdminTestPurlMgrNotificationEmailProcess;
import com.biperf.core.process.AdminTestPurlRecipientEmailProcess;
import com.biperf.core.process.AdminTestRecogGiverBudgetInactivityEmailProcess;
import com.biperf.core.process.AdminTestRecogPlateauNonRedemptionReminderEmailProcess;
import com.biperf.core.process.AdminTestRecogPurlContributorsNonResponseEmailProcess;
import com.biperf.core.process.AdminTestRecogPurlManagersNonResponseEmailProcess;
import com.biperf.core.process.AdminTestRecogReceivedEmailProcess;
import com.biperf.core.process.AdminTestWelcomeEmailProcess;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.util.ProcessUtil;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.UserManager;

/**
 * ClaimSubmissionAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ProcessLaunchAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ProcessLaunchAction.class );

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayLaunch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    logger.debug( "Display Launch Method" );
    ProcessLaunchForm processLaunchForm = (ProcessLaunchForm)form;
    Process process = getProcessService().getProcessById( new Long( processLaunchForm.getProcessId() ) );
    processLaunchForm.setProcessBeanName( process.getProcessBeanName() );
    if ( processLaunchForm.getProcessBeanName().equals( AdminTestWelcomeEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestGoalQuestLoginPasswordEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestRecogPurlContributorsNonResponseEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestRecogPurlManagersNonResponseEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestCelebrationPurlRecipientEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestPurlRecipientEmailProcess.BEAN_NAME ) || processLaunchForm.getProcessBeanName().equals( AdminTestEstatementEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestRecogReceivedEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestBadgeReceivedEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestCelebRecogReceivedEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestCelebMgrNonResponseEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestCelebMgrNotificationEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestPurlContributorsInvitationEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestPurlMgrNotificationEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestRecogPlateauNonRedemptionReminderEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestBudgetReminderNotificationEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestDepositNoticeEmailProcess.BEAN_NAME )
        || processLaunchForm.getProcessBeanName().equals( AdminTestRecogGiverBudgetInactivityEmailProcess.BEAN_NAME ) )
    {
      processLaunchForm.setMethod( "testProcesslaunch" );
      return mapping.findForward( ActionConstants.SUCCESS_TEST_PROCESS_FORWARD );
    }
    else
    {
      processLaunchForm.setMethod( "launch" );
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward launch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = null;
    ProcessLaunchForm processLaunchForm = (ProcessLaunchForm)form;

    if ( isCancelled( request ) )
    {
      processLaunchForm.setMethod( null );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    Process process = getProcessService().getProcessById( new Long( processLaunchForm.getProcessId() ) );

    processLaunchForm.setProcessName( process.getName() );

    Map valueProcessParameterMap = processLaunchForm.returnParametersAsMap();

    // Client customizations for wip #23129 starts
    if ( process.getProcessBeanName().equals( "clientGiftCodeSweepProcess" ) )
    {
      String[] valueArray;
      valueArray = new String[1];
      valueArray[0] = processLaunchForm.getSweepPromoId().toString();
      valueProcessParameterMap.put( "sweepPromoId", valueArray );
      valueArray = new String[1];
      valueArray[0] = processLaunchForm.getSweepMonthYear();
      valueProcessParameterMap.put( "sweepMonthYear", valueArray );
    }
    // Client customizations for wip #23129 ends
    getProcessService().launchProcess( process, valueProcessParameterMap, UserManager.getUserId() );

    request.setAttribute( "parameterValueMap", ProcessUtil.getFormattedParameterValueMap( process, valueProcessParameterMap ) );
    request.setAttribute( "process", process );

    forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward testProcesslaunch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = null;
    ProcessLaunchForm processLaunchForm = (ProcessLaunchForm)form;

    if ( isCancelled( request ) )
    {
      processLaunchForm.setMethod( null );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    Process process = getProcessService().getProcessById( new Long( processLaunchForm.getProcessId() ) );

    processLaunchForm.setProcessName( process.getName() );

    Map valueProcessParameterMap = processLaunchForm.returnParametersAsMap();

    getProcessService().launchProcess( process, valueProcessParameterMap, UserManager.getUserId() );

    request.setAttribute( "parameterValueMap", ProcessUtil.getFormattedParameterValueMap( process, valueProcessParameterMap ) );
    request.setAttribute( "process", process );

    forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );

    return forward;
  }

  // Client customizations for wip #23129 starts
  public ActionForward changeSweepPromotion( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ProcessLaunchForm processLaunchForm = (ProcessLaunchForm)form;
    // promotion has changed on the Form, used just to reload it, and forward back again
    Process process = getProcessService().getProcessById( new Long( processLaunchForm.getProcessId() ) );
    processLaunchForm.setProcessBeanName( process.getProcessBeanName() );
    processLaunchForm.setMethod( "launch" );
    request.setAttribute( "sweepMonthYearList", getProcessService().getClientGiftCodeSweepBean( processLaunchForm.getSweepPromoId() ) );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }
  // Client customizations for wip #23129 ends
  /**
   * Gets a AudienceService
   * 
   * @return AudienceService
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }
}

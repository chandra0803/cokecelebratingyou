/**
 * 
 */

package com.biperf.core.ui.process;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessRole;
import com.biperf.core.domain.process.ProcessRoleEdit;
import com.biperf.core.domain.process.ProcessRoleLaunch;
import com.biperf.core.domain.process.ProcessRoleViewLog;
import com.biperf.core.domain.user.Role;
import com.biperf.core.process.AudienceExtractionProcess;
import com.biperf.core.process.BatchModePromotionEngineProcess;
import com.biperf.core.process.ClaimApproverSnapshotRefreshProcess;
import com.biperf.core.process.DelayedClaimApprovalProcess;
import com.biperf.core.process.DepositProcess;
import com.biperf.core.process.EStatementProcess;
import com.biperf.core.process.EnrollmentProcess;
import com.biperf.core.process.ManagerOverridePayoutProcess;
import com.biperf.core.process.NominationAutoNotificationProcess;
import com.biperf.core.process.ParticipantImportProcess;
import com.biperf.core.process.ParticipantUpdateProcess;
import com.biperf.core.process.ProactiveEmailProcess;
import com.biperf.core.process.ReportTableRefreshProcess;
import com.biperf.core.process.SampleProcess;
import com.biperf.core.process.StackRankCreationProcess;
import com.biperf.core.process.WelcomeEmailProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.process.impl.ProcessAssociationRequest;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.util.ProcessUtil;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.UserManager;

/**
 * ProcessDetailAction.
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
 * <td>asondgeroth</td>
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessDetailAction extends BaseDispatchAction
{
  /**
   * unspecified will display list
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
  }

  /**
   * unspecified will display list
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.DISPLAY_FORWARD;
    Process process;

    ProcessDetailForm processDetailForm = (ProcessDetailForm)actionForm;
    String program = processDetailForm.getProgram();

    processDetailForm.setMethod( "save" );

    String processId = processDetailForm.getProcessId();

    // If a program has been passed in for a redisplay, then
    // override any data set by the load() method
    if ( program != null && !program.equals( "" ) )
    {
      processDetailForm.setProgram( program );

      // If there is a program selected, then we know not to refetch the process
    }
    else if ( processId != null && processId.length() > 0 )
    {
      AssociationRequestCollection processAssociationRequestCollection = new AssociationRequestCollection();
      processAssociationRequestCollection.add( new ProcessAssociationRequest( ProcessAssociationRequest.ROLES ) );

      process = getProcessService().getProcessById( new Long( processId ), processAssociationRequestCollection );
      processDetailForm.load( process );
    }

    Map parameterMap = ProcessUtil.getProcessParameterDefinitions( processDetailForm.getProgram() );

    Set parameterList = new HashSet();
    if ( parameterMap != null )
    {
      parameterList = parameterMap.keySet();
    }

    request.setAttribute( "parameterList", parameterList );
    request.setAttribute( "parameterMap", parameterMap );

    return mapping.findForward( forwardTo );
  }

  /**
   * save the process information
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    Process process = new Process();
    ActionMessages errors = new ActionMessages();

    ProcessDetailForm processDetailForm = (ProcessDetailForm)actionForm;

    String processId = processDetailForm.getProcessId();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    if ( processId != null && processId.length() > 0 )
    {
      AssociationRequestCollection processAssociationRequestCollection = new AssociationRequestCollection();
      processAssociationRequestCollection.add( new ProcessAssociationRequest( ProcessAssociationRequest.ROLES ) );

      process = getProcessService().getProcessById( new Long( processId ), processAssociationRequestCollection );
    }

    process = processDetailForm.toDomain( process );

    // ** BEGIN EDIT ROLES **
    Set formEditRoles = new HashSet();
    String[] editRolesArray = processDetailForm.getEditRoles();
    for ( int i = 0; i < editRolesArray.length; i++ )
    {
      formEditRoles.add( new Long( editRolesArray[i] ) );
    }

    Set domainEditRoles = process.getEditRoles();
    Iterator editIter = domainEditRoles.iterator();
    while ( editIter.hasNext() )
    {
      ProcessRole role = (ProcessRole)editIter.next();
      if ( !formEditRoles.contains( role.getId() ) )
      {
        editIter.remove();
      }
    }

    editIter = formEditRoles.iterator();
    while ( editIter.hasNext() )
    {
      Long id = (Long)editIter.next();
      Role role = getRoleService().getRoleById( id );
      if ( !domainEditRoles.contains( role ) )
      {
        process.addEditRole( new ProcessRoleEdit( role, process ) );
      }
    }
    // ** END EDIT ROLES **

    // ** BEGIN LAUNCH ROLES **
    // User processes have launch roles; system processes do not.
    if ( !processDetailForm.getIsSystemProcess() )
    {
      Set formLaunchRoles = new HashSet();
      String[] launchRolesArray = processDetailForm.getLaunchRoles();
      for ( int i = 0; i < launchRolesArray.length; i++ )
      {
        formLaunchRoles.add( new Long( launchRolesArray[i] ) );
      }

      Set domainLaunchRoles = process.getLaunchRoles();
      Iterator launchIter = domainLaunchRoles.iterator();
      while ( launchIter.hasNext() )
      {
        ProcessRole role = (ProcessRole)launchIter.next();
        if ( !formLaunchRoles.contains( role.getId() ) )
        {
          launchIter.remove();
        }
      }

      launchIter = formLaunchRoles.iterator();
      while ( launchIter.hasNext() )
      {
        Long id = (Long)launchIter.next();
        Role role = getRoleService().getRoleById( id );
        if ( !domainLaunchRoles.contains( role ) )
        {
          process.addLaunchRole( new ProcessRoleLaunch( role, process ) );
        }
      }
    }
    // ** END LAUNCH ROLES **

    // ** BEGIN VIEW LOG ROLES **
    Set formViewLogRoles = new HashSet();
    String[] viewLogRolesArray = processDetailForm.getViewLogRoles();
    for ( int i = 0; i < viewLogRolesArray.length; i++ )
    {
      formViewLogRoles.add( new Long( viewLogRolesArray[i] ) );
    }

    Set domainViewLogRoles = process.getViewLogRoles();
    Iterator viewLogIter = domainViewLogRoles.iterator();
    while ( viewLogIter.hasNext() )
    {
      ProcessRole role = (ProcessRole)viewLogIter.next();
      if ( !formViewLogRoles.contains( role.getId() ) )
      {
        viewLogIter.remove();
      }
    }

    viewLogIter = formViewLogRoles.iterator();
    while ( viewLogIter.hasNext() )
    {
      Long id = (Long)viewLogIter.next();
      Role role = getRoleService().getRoleById( id );
      if ( !domainViewLogRoles.contains( role ) )
      {
        process.addViewLogRole( new ProcessRoleViewLog( role, process ) );
      }
    }
    // ** END VIEW LOG ROLES **

    getProcessService().save( process );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forwardTo );
  }

  public ActionForward testTrigger( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testProcess1", SampleProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "sampleParamName1", new String[] { "foo", "foo1" } );
    parameterValueMap.put( "sampleParamName2", new String[] { "bar" } );
    parameterValueMap.put( "sampleParamName3", new String[] { "foo", "foo1", "foo2" } );

    getProcessService().launchProcess( process, parameterValueMap, null );
    // try
    // {
    // Thread.sleep( 6000 );
    // }
    // catch( InterruptedException e )
    // {
    // log.error( e.getMessage(), e );
    // }
    // getProcessService().interruptJob( process.getId() );

    return null;
  }

  public ActionForward testBatchPromoEngineProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testBatchPromo", BatchModePromotionEngineProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "promotionId", new String[] { "5" } );

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );

    return null;
  }

  public ActionForward testDepositProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "DepositProcess", DepositProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "promotionId", new String[] { "14" } );

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );

    return null;
  }

  public ActionForward testParticipantImportProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "ParticipantImportProcess", ParticipantImportProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "importFileId", new String[] { "505" } );

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );

    return null;
  }

  public ActionForward testReportRefreshProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "ReportRefreshProcess", ReportTableRefreshProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "promotionTypes", new String[] { PromotionType.RECOGNITION, PromotionType.QUIZ, PromotionType.PRODUCT_CLAIM, PromotionType.NOMINATION } );

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );

    return null;
  }

  public ActionForward testStackRankCreationProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testStackRankCreation", StackRankCreationProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "stackRankId", new String[] { "1" } );

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );

    return null;
  }

  public ActionForward testAudienceExtractionProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testAudienceExtraction", AudienceExtractionProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "promotionId", new String[] { "1" } );

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );

    return null;
  }

  public ActionForward testProactiveEmailProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testProactiveEmail", ProactiveEmailProcess.BEAN_NAME );

    // no parameters needed, this process sends emails to all appropriate pax of eligible promos.
    getProcessService().launchProcess( process, null, UserManager.getUserId() );
    return null;
  }

  public ActionForward testClaimApproverSnapshotRefreshProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testClaimApproverSnapshotRefreshProcess", ClaimApproverSnapshotRefreshProcess.BEAN_NAME );

    // no parameters needed, this process sends emails to all appropriate pax of eligible promos.
    getProcessService().launchProcess( process, null, UserManager.getUserId() );
    return null;
  }

  public ActionForward testWelcomeEmailProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "WelcomeEmail", WelcomeEmailProcess.BEAN_NAME );

    // no parameters needed, this process sends emails to all pax who need a welcome email.
    getProcessService().launchProcess( process, null, UserManager.getUserId() );
    return null;
  }

  public ActionForward testNominationAutoNotificationProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "NominationAutoNotificationProcess", NominationAutoNotificationProcess.BEAN_NAME );

    // no parameters needed,
    getProcessService().launchProcess( process, null, UserManager.getUserId() );
    return null;
  }

  public ActionForward testEnrollmentProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testEnrollmentProcess", EnrollmentProcess.BEAN_NAME );

    getProcessService().launchProcess( process, null, UserManager.getUserId() );

    return null;
  }

  public ActionForward testParticipantUpdateProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testParticipantUpdateProcess", ParticipantUpdateProcess.BEAN_NAME );

    getProcessService().launchProcess( process, null, UserManager.getUserId() );

    return null;
  }

  public ActionForward testEStatementProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "testEStatementProcess", EStatementProcess.BEAN_NAME );

    getProcessService().launchProcess( process, null, UserManager.getUserId() );

    return null;
  }

  public ActionForward testDelayedClaimApprovalProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "DelayedClaimApprovalProcess", DelayedClaimApprovalProcess.BEAN_NAME );

    // LinkedHashMap parameterValueMap = new LinkedHashMap();
    // parameterValueMap.put( "promotionId", new String[] { "5" } );

    getProcessService().launchProcess( process, null, UserManager.getUserId() );

    return null;
  }

  public ActionForward testManagerOverridePayoutProcess( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Process process = getProcessService().createOrLoadSystemProcess( "ManagerOverridePayoutProcess", ManagerOverridePayoutProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "promotionId", new String[] { "5" } );
    parameterValueMap.put( "startDate", new String[] { "01/01/2004" } );
    parameterValueMap.put( "endDate", new String[] { "12/31/2006" } );

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );

    return null;
  }

  public ActionForward testListTriggers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    getProcessService().testListTriggers();

    // Also show bean names
    List processJobBeanNames = getProcessService().getProcessJobBeanNames();
    for ( Iterator iter = processJobBeanNames.iterator(); iter.hasNext(); )
    {
      String processJobBeanName = (String)iter.next();
      log.error( "Process Job Bean: " + processJobBeanName );

    }

    return null;
  }

  /**
   * Get the processService from the applicationContext.
   * 
   * @return ProcessService
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  /**
   * Get the RoleService from the applicationContext.
   * 
   * @return RoleService
   */
  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }
}

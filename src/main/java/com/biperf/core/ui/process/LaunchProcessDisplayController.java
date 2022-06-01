
package com.biperf.core.ui.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessParameter;
import com.biperf.core.process.EStatementProcess;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

public class LaunchProcessDisplayController extends BaseController
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
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ProcessLaunchForm processLaunchForm = (ProcessLaunchForm)request.getAttribute( "processLaunchForm" );

    Process process = getProcessService().getProcessById( new Long( processLaunchForm.getProcessId() ) );

    ArrayList processParameterList;

    if ( processLaunchForm.getProcessParameterValueList() != null && processLaunchForm.getProcessParameterValueList().size() > 0 )
    {
      processParameterList = (ArrayList)processLaunchForm.getProcessParameterValueList();
    }
    else
    {
      processParameterList = buildProcessParameterList( process );
    }

    request.setAttribute( "process", process );
    request.setAttribute( "processParameterList", processParameterList );
    request.setAttribute( "countOfParameters", new Long( processParameterList.size() ) );
    request.setAttribute( "allowPasswordFieldAutoComplete", getSystemVariableService().getPropertyByName( SystemVariableService.ALLOW_PASSWORD_FIELD_AUTO_COMPLETE ).getBooleanVal() );
    Boolean isEStatementProcess = process.getProcessBeanName().equals( EStatementProcess.BEAN_NAME );
    if ( isEStatementProcess )
    {
      request.setAttribute( "showSendAllQuestionForEstatmentProcess",
                            getSystemVariableService().getPropertyByName( SystemVariableService.SHOW_SEND_ALL_QUESTION_ON_ESTATEMENT_PROCESS ).getBooleanVal() );
    }
    // Client customizations for wip #23129 starts
    if ( process.getProcessBeanName().equals( "clientGiftCodeSweepProcess" ) )
    {
      request.setAttribute( "sweepPromoList", getProcessService().getClientGiftCodeSweepPromotions() );
      if ( processLaunchForm.getSweepPromoId() == null || processLaunchForm.getSweepPromoId() < 1 )
      {
        request.setAttribute( "sweepMonthYearList", getProcessService().getClientGiftCodeSweepBean( new Long( 0 ) ) );
      }
      Integer daystoSweep = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_GIFT_CODE_SWEEP_MIN_DAYS).getIntVal();
      request.setAttribute( "daysToSweep", daystoSweep );
    }
    // Client customizations for wip #23129 ends 
    return;
  }

  public static ArrayList buildProcessParameterList( Process process )
  {
    ArrayList processParameterList;
    processParameterList = new ArrayList();
    Map processParameterMap = process.getProcessParameters();
    Set processParameterKeySet = processParameterMap.keySet();

    for ( Iterator processParameterIter = processParameterKeySet.iterator(); processParameterIter.hasNext(); )
    {

      String processParameterKey = (String)processParameterIter.next();
      ProcessParameter processParameter = (ProcessParameter)processParameterMap.get( processParameterKey );

      ProcessParameterValue processParameterValue = new ProcessParameterValue();
      processParameterValue.setDataType( processParameter.getProcessParameterDataType().getCode() );
      processParameterValue.setFormatType( processParameter.getProcessParameterInputFormatType().getCode() );
      processParameterValue.setName( processParameter.getName() );
      processParameterValue.setSecret( processParameter.isSecret() );
      if ( processParameter.getProcessParameterSourceTypeCode() != null )
      {
        processParameterValue.setSourceType( processParameter.getProcessParameterSourceTypeCode() );
        processParameterValue.setProcessBeanName( process.getProcessBeanName() );
      }
      processParameterList.add( processParameterValue );
    }
    return processParameterList;
  }

  /**
   * Get the ClaimService from the beanLocator.
   * 
   * @return ClaimService
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}

/**
 * 
 */

package com.biperf.core.ui.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.AmPmType;
import com.biperf.core.domain.enums.DayOfMonthType;
import com.biperf.core.domain.enums.DayOfWeekType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.process.EStatementProcess;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ScheduleProcessController.
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
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ScheduleProcessController extends BaseController
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
    Long processId = null;
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
      try
      {
        processId = (Long)clientStateMap.get( "processId" );
      }
      catch( ClassCastException cce )
      {
        processId = new Long( (String)clientStateMap.get( "processId" ) );
      }
      if ( processId == null )
      {
        throw new IllegalArgumentException( "processId as part of clientState was missing" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Process process = getProcessService().getProcessById( processId );
    ArrayList processParameterList = LaunchProcessDisplayController.buildProcessParameterList( process );

    ScheduleProcessForm myForm = (ScheduleProcessForm)request.getAttribute( ScheduleProcessForm.FORM_NAME );
    ArrayList processParamList = (ArrayList)myForm.getProcessParameterValueList();
    ArrayList newParamList = new ArrayList();

    if ( processParamList == null || processParamList.size() == 0 )
    {
      request.setAttribute( "processParameterList", processParameterList );
    }
    else
    {
      Iterator iterator = processParamList.iterator();
      boolean flag = false;
      while ( iterator.hasNext() )
      {
        ProcessParameterValue temp = (ProcessParameterValue)iterator.next();
        String[] temps = temp.getValues();
        if ( temps != null && temps.length > 0 )
        {
          Iterator iterator2 = processParameterList.iterator();
          while ( iterator2.hasNext() )
          {
            flag = true;
            ProcessParameterValue temp2 = (ProcessParameterValue)iterator2.next();
            temp2.setValues( temps );
            newParamList.add( temp2 );
          }
        }
      }
      if ( flag )
      {
        request.setAttribute( "processParameterList", newParamList );
      }
      else
      {
        request.setAttribute( "processParameterList", processParameterList );
      }
    }

    Boolean isEStatementProcess = process.getProcessBeanName().equals( EStatementProcess.BEAN_NAME );
    if ( isEStatementProcess )
    {
      request.setAttribute( "showSendAllQuestionForEstatmentProcess",
                            getSystemVariableService().getPropertyByName( SystemVariableService.SHOW_SEND_ALL_QUESTION_ON_ESTATEMENT_PROCESS ).getBooleanVal() );
    }

    request.setAttribute( "process", process );

    request.setAttribute( "countOfParameters", new Long( processParameterList.size() ) );

    request.setAttribute( "frequencyList", ProcessFrequencyType.getList() );
    request.setAttribute( "dayOfWeekList", DayOfWeekType.getList() );
    request.setAttribute( "dayOfMonthList", DayOfMonthType.getList() );
    request.setAttribute( "amPmList", AmPmType.getList() );
    request.setAttribute( "allowPasswordFieldAutoComplete", getSystemVariableService().getPropertyByName( SystemVariableService.ALLOW_PASSWORD_FIELD_AUTO_COMPLETE ).getBooleanVal() );
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

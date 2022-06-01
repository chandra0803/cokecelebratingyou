
package com.biperf.core.utils;

import java.util.List;

import com.biperf.core.domain.enums.SAProcessRemoveEnum;
import com.biperf.core.domain.process.Process;
import com.biperf.core.service.system.SystemVariableService;

public class NewServiceAnniversaryUtil
{

  public static List<Process> removePurlAndCelebrationProcess( List<Process> listOfProcess )
  {
    List<String> processNames = getRemoveProcess();

    processNames.stream().forEach( processName ->
    {
      listOfProcess.removeIf( e -> e.getProcessBeanName().equals( processName ) );

    } );
    return listOfProcess;

  }

  public static List<String> removePurlAndCelebrationProcessNames( List<String> listOfProcess )
  {
    List<String> processNames = getRemoveProcess();

    processNames.stream().forEach( processName ->
    {
      listOfProcess.removeIf( e -> e.equals( processName ) );

    } );

    return listOfProcess;
  }

  public static boolean isNewServiceAnniversaryEnabled()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.NEW_SERVICE_ANNIVERSARY ).getBooleanVal()
        && getSystemVariableService().getPropertyByName( SystemVariableService.PURL_AVAILABLE ).getBooleanVal();
  }

  private static List<String> getRemoveProcess()
  {
    return SAProcessRemoveEnum.getProcessList();
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }
}

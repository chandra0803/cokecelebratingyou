
package com.biperf.core.ui.el;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ServiceLocator;

public class ElFunctions
{
  public static int systemVarInteger( String systemVar )
  {
    return getSystemVariableService().getPropertyByName( systemVar ).getIntVal();
  }

  public static boolean systemVarBoolean( String systemVar )
  {
    return getSystemVariableService().getPropertyByName( systemVar ).getBooleanVal();
  }
  
  public static String systemVarString( String systemVar )
  {
    return getSystemVariableService().getPropertyByName( systemVar ).getStringVal();
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)ServiceLocator.getService( SystemVariableService.BEAN_NAME );
  }
}

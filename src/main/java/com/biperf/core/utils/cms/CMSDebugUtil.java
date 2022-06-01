
package com.biperf.core.utils.cms;

import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;

public class CMSDebugUtil
{
  public static boolean isCMSDebugEnabled()
  {
    return !Environment.getEnvironment().equals( Environment.ENV_PROD ) && getSystemVariableService().getPropertyByName( SystemVariableService.CMS_ASSET_DEBUG ).getBooleanVal()
        && ( getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS ) || getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_BI_ADMIN ) );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private static AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)BeanLocator.getBean( AuthorizationService.BEAN_NAME );
  }
}

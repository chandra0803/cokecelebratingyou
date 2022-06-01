
package com.biperf.core.ui.search;

import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.utils.ServiceLocator;

public class AutoCompleteFunctions
{
  public static int getAutoCompleteDelay()
  {
    return getAutoCompleteService().getAutoCompleteDelay();
  }

  private static AutoCompleteService getAutoCompleteService()
  {
    return (AutoCompleteService)ServiceLocator.getService( AutoCompleteService.BEAN_NAME );
  }
}

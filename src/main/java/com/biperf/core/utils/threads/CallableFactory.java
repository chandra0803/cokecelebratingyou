
package com.biperf.core.utils.threads;

import java.util.concurrent.Callable;

import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.ContentReaderManager;

public class CallableFactory
{
  private CallableFactory()
  {
  }

  public static CallableWrapper createCallable( Callable callable )
  {
    return new CallableWrapper( ContentReaderManager.getContentReader(), UserManager.getUser(), callable );
  }
}

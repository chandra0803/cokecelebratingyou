
package com.biperf.core.utils.threads;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class CallableWrapper implements Callable
{
  private final ContentReader contentReader;
  private final AuthenticatedUser user;
  private final Callable callable;

  private static final Logger log = Logger.getLogger( CallableWrapper.class );

  public CallableWrapper( ContentReader contentReader, AuthenticatedUser user, Callable callable )
  {
    this.contentReader = contentReader;
    this.user = user;
    this.callable = callable;
  }

  @Override
  public Object call() throws Exception
  {
    ContentReaderManager.setContentReader( contentReader );
    UserManager.setUser( user );

    try
    {
      return callable.call();
    }
    catch( Throwable t )
    {
      String originalStackTrace = ExceptionUtils.getStackTrace( t );
      log.error( "\n\n************************************************" + "\nERROR in " + getClass().getName() + "#call" + "\n************************************************"
          + "\nOriginal exception stack trace: " + "\n\n" + originalStackTrace + "\n\n************************************************" + "\n END ERROR"
          + "\n************************************************\n\n" );
    }
    finally
    {
      ContentReaderManager.removeContentReader();
      UserManager.removeUser();
    }
    return null;
  }

}

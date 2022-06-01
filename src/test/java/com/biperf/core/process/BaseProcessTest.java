
package com.biperf.core.process;

import static org.easymock.EasyMock.createNiceMock;

import java.util.TimeZone;

import org.jmock.MockObjectTestCase;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListFactoryImpl;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.MockContentReader;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.ContentReaderManager;

public class BaseProcessTest extends MockObjectTestCase
{
  public static final Long PAX_ID = 5583L;

  protected ApplicationContext applicationContext = createNiceMock( ApplicationContext.class );

  protected void setUp() throws Exception
  {
    super.setUp();
    
    // setup of the mock pick list factory
    PickListItem.setPickListFactory( new MockPickListFactory() );
    // check if the ContentReader is already set - true if we are in container.
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }

    if ( UserManager.getUser() == null )
    {
      UserManager.setUser( getDefaultAuthenticatedUser() );
    }

    ApplicationContextFactory.setApplicationContext( applicationContext );

  }
  
  protected void tearDown() throws Exception
  {
    super.tearDown();
    
    PickListItem.setPickListFactory( new PickListFactoryImpl() );
  }
  
  @Test
  public void testFoo()
  {
    // Avoiding warning when there are no tests in a class
    assertTrue( true );
  }

  /**
   * Obtain a value to provide as the processInvocationId for the execute method
   */
  protected Long getTestProcessInvocationId()
  {
    return System.currentTimeMillis() % 5503032;
  }

  public static AuthenticatedUser getDefaultAuthenticatedUser()
  {
    AuthenticatedUser user = new AuthenticatedUser();
    user.setUserId( new Long( PAX_ID ) );
    user.setTimeZoneId( TimeZone.getDefault().getID() );
    UserManager.setUser( user );
    ClientStatePasswordManager.setPassword( "password" );
    return user;
  }

}

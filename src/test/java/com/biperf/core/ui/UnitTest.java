
package com.biperf.core.ui;

import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListFactoryImpl;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.MockContentReader;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.ContentReaderManager;

import junit.framework.TestCase;

public class UnitTest extends TestCase
{
  public static final Long TEST_USER_ID = 100000L;

  @BeforeClass
  public static void beforeClass()
  {

    PickListItem.setPickListFactory( new MockPickListFactory() );

    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }

    if ( UserManager.getUser() == null )
    {
      UserManager.setUser( getDefaultAuthenticatedUser() );
    }
  }

  @AfterClass
  public static void afterClass()
  {
    PickListItem.setPickListFactory( new PickListFactoryImpl() );
  }
  
  @Test
  public void testFoo()
  {
    // Avoiding warning when there are no tests in a class
    assertTrue( true );
  }

  public static AuthenticatedUser getDefaultAuthenticatedUser()
  {
    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( TEST_USER_ID );
    authUser.setTimeZoneId( TimeZone.getDefault().getID() );
    UserManager.setUser( authUser );
    ClientStatePasswordManager.setPassword( "password" );
    return authUser;
  }

  public static String getUniqueString()
  {
    return String.valueOf( System.currentTimeMillis() % 5503032 );
  }

}

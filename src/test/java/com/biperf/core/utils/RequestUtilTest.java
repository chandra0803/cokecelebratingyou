
package com.biperf.core.utils;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

public class RequestUtilTest
{

  private HttpServletRequest mockServletRequest;

  @Before
  public void setUp()
  {
    mockServletRequest = createMock( HttpServletRequest.class );
  }

  @Test
  public void testGetPreferredLanguagesFrom_HappyPath()
  {
    final String LANGUAGE_LIST = "da, en-gb;q=0.8, en;q=0.7";

    expect( mockServletRequest.getHeader( "Accept-Language" ) ).andReturn( LANGUAGE_LIST );

    replayAll();

    // do the test!!!
    List<String> languages = RequestUtil.getPreferredLanguageListFrom( mockServletRequest );

    verifyAll();

    assertNotNull( languages );
    assertEquals( 3, languages.size() );
    assertEquals( "da", languages.get( 0 ) );
    assertEquals( "en", languages.get( 1 ) );
    assertEquals( "en", languages.get( 2 ) );
  }

  @Test
  public void testGetPreferredLanguagesFrom_NullAcceptLanguagesHeaderValue()
  {
    final String LANGUAGE_LIST = null;

    expect( mockServletRequest.getHeader( "Accept-Language" ) ).andReturn( LANGUAGE_LIST );

    replayAll();

    // do the test!!!
    List<String> languages = RequestUtil.getPreferredLanguageListFrom( mockServletRequest );

    verifyAll();

    assertNotNull( languages );
    assertEquals( 0, languages.size() );
  }

  @Test
  public void testGetPreferredLanguagesFrom_EmptyStringLanguagesHeaderValue()
  {
    final String LANGUAGE_LIST = "";

    expect( mockServletRequest.getHeader( "Accept-Language" ) ).andReturn( LANGUAGE_LIST );

    replayAll();

    // do the test!!!
    List<String> languages = RequestUtil.getPreferredLanguageListFrom( mockServletRequest );

    verifyAll();

    assertNotNull( languages );
    assertEquals( 0, languages.size() );
  }

  public void replayAll()
  {
    replay( mockServletRequest );
  }

  public void verifyAll()
  {
    verify( mockServletRequest );
  }
}


package com.biperf.core.utils;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase
{

  // Tests if the email masking is correct
  public void testMaskEmailAddress()
  {
    assertTrue( "Email Address did not get masked as expected", StringUtil.maskEmailAddress( "test123@biworldwide.com" ).equals( "tes***3@biw*******e.com" ) );
    assertTrue( "Email Address did not get masked as expected", StringUtil.maskEmailAddress( "test@biw.com" ).equals( "t**t@b*w.com" ) );
    assertTrue( "Email Address did not get masked as expected", StringUtil.maskEmailAddress( "t@b.com" ).equals( "*@*.com" ) );
  }

  // Tests if the phone number masking is correct
  public void testMaskPhoneNumber()
  {
    assertTrue( "Phone Number 1 did not get masked as expected", StringUtil.maskPhoneNumber( "123-45-6789" ).equals( "1******789" ) );
    assertTrue( "Phone Number 2 did not get masked as expected", StringUtil.maskPhoneNumber( "123456789" ).equals( "1******789" ) );
    assertTrue( "Phone Number 3 did not get masked as expected", StringUtil.maskPhoneNumber( "(123)-45-6789" ).equals( "1******789" ) );
    assertTrue( "Phone Number 4 did not get masked as expected", StringUtil.maskPhoneNumber( "123.45.6789" ).equals( "1******789" ) );
  }

}


package com.biperf.core.utils;

import junit.framework.TestCase;

public class PhoneNumberValidatorTest extends TestCase
{

  public void testPhoneNumbers()
  {
    assertTrue( "Phone number is not valid", PhoneNumberValidator.isValidPhoneNumberLength( "651.398.3566" ) );
    assertTrue( "Phone number is not valid", PhoneNumberValidator.isValidPhoneNumberLength( "1231*-2222" ) );
    assertTrue( "Phone number is not valid", PhoneNumberValidator.isValidPhoneNumberLength( "1111" ) );
    assertTrue( "Phone number is not valid", PhoneNumberValidator.isValidPhoneNumberLength( "1234567890-12345" ) );
    assertFalse( "Phone number is not valid", PhoneNumberValidator.isValidPhoneNumberLength( "1d4" ) );
    assertFalse( "Phone number is not valid", PhoneNumberValidator.isValidPhoneNumberLength( "ddddd" ) );
    assertFalse( "Phone number is not valid", PhoneNumberValidator.isValidPhoneNumberLength( "112312091230123112222" ) );
  }

}

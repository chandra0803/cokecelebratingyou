
package com.biperf.core.utils.crypto;

import org.apache.commons.codec.binary.Base64;

import junit.framework.TestCase;

public class Base64EncodeTest extends TestCase
{
  public void testEncode() throws Exception
  {
    String expected = "SGVsbG8gd29ybGQ=";
    String toEncrypt = "Hello world";
    String encoded = new String( Base64.encodeBase64( toEncrypt.getBytes() ) );
    assertEquals( "Ecoding/decoding failed", expected, encoded );
  }

  public void testDecode() throws Exception
  {
    // clear text:"this is a test decode string" - no quotes
    // encoded: dGhpcyBpcyBhIHRlc3QgZGVjb2RlIHN0cmluZw==
    String expected = "this is a test decode string";
    assertEquals( expected, new String( Base64.decodeBase64( "dGhpcyBpcyBhIHRlc3QgZGVjb2RlIHN0cmluZw==".getBytes() ) ) );
  }
}

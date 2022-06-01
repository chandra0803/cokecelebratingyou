
package com.biperf.core.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HtmlUtilsTest
{

  @Test
  public void testRemoveFormatting()
  {
    String value1 = "<b><script >Text< /sCript></b>";
    String result1 = "  Text  ";
    assertEquals( "Value and result did not match", result1, HtmlUtils.removeFormatting( value1 ) );

    String value2 = "Text";
    String result2 = "Text";
    assertEquals( "Value and result did not match", result2, HtmlUtils.removeFormatting( value2 ) );

    String value3 = "";
    String result3 = "";
    assertEquals( "Value and result did not match", result3, HtmlUtils.removeFormatting( value3 ) );
  }

  @Test
  public void testWhitelistFormatting()
  {
    String value1 = "<b><script >Text< /sCript></b>";
    String result1 = "<b>Text</b>";
    assertEquals( "Value and result did not match", result1, HtmlUtils.whitelistFormatting( value1 ) );

    String value2 = "Text";
    String result2 = "Text";
    assertEquals( "Value and result did not match", result2, HtmlUtils.whitelistFormatting( value2 ) );

    String value3 = "";
    String result3 = "";
    assertEquals( "Value and result did not match", result3, HtmlUtils.whitelistFormatting( value3 ) );
  }

}

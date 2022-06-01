
package com.biperf.core.utils;

public class OTSUtil
{
  public static String checkLength( String programNumber )
  {
    String otsProgramPattern = "\\d{5}";
    StringBuilder programnbr = new StringBuilder();
    if ( programNumber.matches( otsProgramPattern ) )
    {
      return programNumber.toString();
    }
    else if ( programNumber.matches( "\\d+" ) )
    {
      programnbr.append( "0" + programNumber.toString() );
      if ( programnbr.toString().matches( otsProgramPattern ) )
      {
        return programnbr.toString();
      }
      else
      {
        checkLength( programnbr.toString() );
      }
    }
    return programnbr.toString();

  }

}

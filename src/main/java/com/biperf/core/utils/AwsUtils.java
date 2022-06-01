
package com.biperf.core.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.EnvironmentTypeEnum;

public class AwsUtils
{
  private static final Log log = LogFactory.getLog( AwsUtils.class );

  public static boolean isAws()
  {
    boolean truth = EnvironmentTypeEnum.isAws();
    if ( log.isDebugEnabled() )
    {
      log.debug( "is system AWS: " + truth );
    }
    return truth;
  }
}

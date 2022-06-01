
package com.biperf.core.utils;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CmxTranslateHelperBean
{

  private static final String CMX_DEFAULT_LOCALE = "en_US";

  private Map<String, String> translateBundles;

  public CmxTranslateHelperBean( Map<String, String> translateBundles )
  {
    this.translateBundles = translateBundles;
  }

  public String getCmxTranslatedValue( String key, String defaultContent )
  {
    String translateContent = null;

    if ( null != translateBundles )
    {
      translateContent = translateBundles.get( key );
      if ( StringUtils.isEmpty( translateContent ) )
      {
        translateContent = defaultContent;
      }
    }
    return translateContent;
  }

  public static boolean isCmxDefaultLocale()
  {
    return CMX_DEFAULT_LOCALE.equals( UserManager.getUserLocale() );
  }

}

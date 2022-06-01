
package com.biperf.core.ui.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.impl.DefaultModuleConfigFactory;

@SuppressWarnings( "serial" )
public class BIModuleConfigFactory extends DefaultModuleConfigFactory
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( BIModuleConfigFactory.class );

  @Override
  public ModuleConfig createModuleConfig( String prefix )
  {
    return new BIModuleConfigImpl( prefix );
  }
}

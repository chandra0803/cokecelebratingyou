
package com.biperf.core.ui.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.impl.ModuleConfigImpl;

import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.Environment;

@SuppressWarnings( "serial" )
public class BIModuleConfigImpl extends ModuleConfigImpl
{
  private static final Log logger = LogFactory.getLog( BIModuleConfigImpl.class );

  public BIModuleConfigImpl( String prefix )
  {
    super( prefix );
  }

  @Override
  public void setControllerConfig( ControllerConfig cc )
  {
    cc.setTempDir( buildTempDir( cc ) );
    if ( logger.isInfoEnabled() )
    {
      logger.info( prefix + " redirecting the tempDir to " + cc.getTempDir() );
    }
    super.setControllerConfig( cc );
  }

  protected String buildTempDir( ControllerConfig cc )
  {
    // if AWS, use the system variable
    if ( AwsUtils.isAws() )
    {
      return Environment.getTmpDir();
    }
    else
    {
      return cc.getTempDir();
    }
  }
}

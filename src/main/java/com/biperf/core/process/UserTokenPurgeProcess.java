
package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.participant.PasswordResetService;

public class UserTokenPurgeProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( UserTokenPurgeProcess.class );

  private PasswordResetService passwordResetService;

  @Override
  protected void onExecute()
  {
    try
    {
      passwordResetService.purgeUserTokens();
      addComment( "User Tokens have been purged successfully" );
    }
    catch( Exception ex )
    {
      log.error( "User Token Purge process failed: " + ex.getMessage() );
    }
  }

  public void setPasswordResetService( PasswordResetService passwordResetService )
  {
    this.passwordResetService = passwordResetService;
  }
}

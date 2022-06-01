
package com.biperf.core.ui.user;

@SuppressWarnings( "serial" )
public class UserActivationRecoveryView extends UserActivationView
{
  private String tokenValidation = null;
  private boolean hasRecoveryMethods = false;
  private boolean isTermedUserAllowToRedeem = false;
  private Long termedUserId = null;
  private boolean termedUserToolTip = false;

  public String getTokenValidation()
  {
    return tokenValidation;
  }

  public void setTokenValidation( String tokenValidation )
  {
    this.tokenValidation = tokenValidation;
  }

  public boolean isHasRecoveryMethods()
  {
    return hasRecoveryMethods;
  }

  public void setHasRecoveryMethods( boolean hasRecoveryMethods )
  {
    this.hasRecoveryMethods = hasRecoveryMethods;
  }

  public boolean isTermedUserAllowToRedeem()
  {
    return isTermedUserAllowToRedeem;
  }

  public void setTermedUserAllowToRedeem( boolean isTermedUserAllowToRedeem )
  {
    this.isTermedUserAllowToRedeem = isTermedUserAllowToRedeem;
  }

  public Long getTermedUserId()
  {
    return termedUserId;
  }

  public void setTermedUserId( Long termedUserId )
  {
    this.termedUserId = termedUserId;
  }

  @Override
  public String toString()
  {
    return "UserActivationRecoveryView [tokenValidation=" + tokenValidation + ", hasRecoveryMethods=" + hasRecoveryMethods + ", isTermedUserAllowToRedeem=" + isTermedUserAllowToRedeem
        + ", termedUserId=" + termedUserId + "]";
  }

  public boolean isTermedUserToolTip()
  {
    return termedUserToolTip;
  }

  public void setTermedUserToolTip( boolean termedUserToolTip )
  {
    this.termedUserToolTip = termedUserToolTip;
  }

}

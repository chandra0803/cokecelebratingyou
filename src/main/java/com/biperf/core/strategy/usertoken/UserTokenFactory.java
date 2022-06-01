
package com.biperf.core.strategy.usertoken;

import java.util.Map;

public class UserTokenFactory
{
  /** Injected */
  private Map<String, UserTokenStrategy> entries;

  public UserTokenStrategy getStrategy( UserTokenType type )
  {
    return entries.get( type.toString() );
  }

  public Map<String, UserTokenStrategy> getEntries()
  {
    return entries;
  }

  public void setEntries( Map<String, UserTokenStrategy> entries )
  {
    this.entries = entries;
  }

}

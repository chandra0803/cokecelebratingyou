
package com.biperf.core.value.hc;

/**
 * Singleton instance holding honeycomb data.
 * The data originates from the HoneycombInitializationServlet.  
 * It's various fields we need to interact with the honeycomb system, that aren't suitable for the gsaltproperties file
 */
public class HoneycombInitializationData
{

  private static HoneycombInitializationData instance;

  /** Random ID token used when receiving the salt initialization JMS message, to make sure only originating server acts on message */
  private String token;
  
  /** Honeycomb client ID. Kept here since it may change, and the salt properties come from HC Config (which is a different ID) */
  private Long clientId = null;

  private HoneycombInitializationData()
  {
  }

  public static HoneycombInitializationData getInstance()
  {
    if ( instance == null )
    {
      synchronized ( HoneycombInitializationData.class )
      {
        if ( instance == null )
        {
          instance = new HoneycombInitializationData();
        }
      }
    }

    return instance;
  }

  public String getToken()
  {
    return token;
  }

  public void setToken( String token )
  {
    this.token = token;
  }

  public Long getClientId()
  {
    return clientId;
  }

  public void setClientId( Long clientId )
  {
    this.clientId = clientId;
  }

}

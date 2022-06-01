/*
 * (c) 2009 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/exception/BeaconConnectionException.java,v $
 *
 */

package com.biperf.core.exception;

/**
 * BeaconConnectionException. <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>babu</td>
 * <td>Jul 9, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author babu
 *
 */
public class BeaconConnectionException extends BeaconRuntimeException
{
  public BeaconConnectionException()
  {
    super();
  }

  public BeaconConnectionException( String pMessage )
  {
    super( pMessage );
  }

  public BeaconConnectionException( Exception pException )
  {
    super( pException );
  }

  public BeaconConnectionException( String pMessage, Exception pException )
  {
    super( pMessage, pException );
  }

}

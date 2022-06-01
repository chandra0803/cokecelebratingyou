/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/exception/BeaconRuntimeException.java,v $
 */

package com.biperf.core.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * BeaconRuntimeException - Wrapper for all runtime application exceptions Runtime exceptions are
 * those which can not be handled gracefully within the normal operation of the application.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public class BeaconRuntimeException extends NestableRuntimeException
{

  public BeaconRuntimeException()
  {
    super();
  }

  public BeaconRuntimeException( String pMessage )
  {
    super( pMessage );
  }

  public BeaconRuntimeException( Exception pException )
  {
    super( pException );
  }

  public BeaconRuntimeException( String pMessage, Exception pException )
  {
    super( pMessage, pException );
  }

}

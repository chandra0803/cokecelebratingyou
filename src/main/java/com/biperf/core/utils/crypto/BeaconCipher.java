/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/crypto/BeaconCipher.java,v $
 */

package com.biperf.core.utils.crypto;

/**
 * BeaconCipher.
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
 * <td>jdunne</td>
 * <td>Mar 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface BeaconCipher
{
  public String encrypt( String clearText ) throws CryptoException;

  public String decrypt( String encodedText ) throws CryptoException;

  public void setSeed( String seed );
}

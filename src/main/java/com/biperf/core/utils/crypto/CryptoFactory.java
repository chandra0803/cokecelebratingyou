/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/crypto/CryptoFactory.java,v $
 */

package com.biperf.core.utils.crypto;

/**
 * CryptoFactory.
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
public class CryptoFactory
{

  private static BeaconCipher cipher;
  private static BeaconCipher seededCipher;

  /**
   * Get the instance of the BeaconCipher.
   * 
   * @return BeaconCipher
   */
  public static synchronized BeaconCipher getTripleDesCipherInstance()
  {
    if ( cipher == null )
    {
      cipher = new TripleDesCipher();
      // TODO this seed must come from the database - for now it is hard coded.
      cipher.setSeed( "PASSWDKEY_B12345" );
    }
    return cipher;
  }

  /**
   * This method is provided for the database java stored proc.
   * 
   * @param seed
   * @return BeaconCipher
   */
  public static synchronized BeaconCipher getTripleDesCipherInstance( String seed )
  {
    if ( seededCipher == null || seed != null && !seed.equals( ( (TripleDesCipher)seededCipher ).getSeed() ) )
    {
      seededCipher = new TripleDesCipher();
      seededCipher.setSeed( seed );
    }
    return seededCipher;
  }

}

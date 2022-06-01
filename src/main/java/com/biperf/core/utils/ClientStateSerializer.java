/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/ClientStateSerializer.java,v $
 */

package com.biperf.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.crypto.Base64;
import com.biperf.core.utils.crypto.ByteArrayGuard;
import com.biperf.core.utils.crypto.InvalidMacException;

/**
 * ClientStateSerializer.
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
 * <td>Thomas Eaton</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class ClientStateSerializer
{
  /**
   * Decodes, decrypts, and deserializes data that was serialized by the method
   * ClientStateSerializer.serialize(String, Map).
   * 
   * @param clientState the data to be deserialized.
   * @param password the password used to decrypt the client state.
   * @return the decoded, decrypted, and deserialized data, as a Map of String/Serializable pairs
   *         that represent the name and value of a piece of data.
   * @throws com.biperf.core.utils.InvalidClientStateException if the client state was modified
   *           while on the client or in transit.
   */
  public static Map deserialize( String clientState, String password ) throws InvalidClientStateException
  {
    Map clientStateMap = new HashMap();

    try
    {
      // Decode the client state.
      byte[] encryptedClientState = Base64.decode( clientState );

      // Decrypt the client state.
      ByteArrayGuard byteArrayGuard = new ByteArrayGuard( password );
      byte[] serializedClientState = byteArrayGuard.decrypt( encryptedClientState );

      // Deserialize the client state.
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( serializedClientState );
      ObjectInputStream objectInputStream = new ObjectInputStream( byteArrayInputStream );
      clientStateMap = (Map)objectInputStream.readObject();
      objectInputStream.close();
    }
    catch( InvalidMacException e )
    {
      // The method ByteArrayGuard.decrypt(byte[]) determined that someone
      // tampered with the client state.
      throw (InvalidClientStateException)new InvalidClientStateException().initCause( e );
    }
    catch( ClassNotFoundException e )
    {
      // The method ObjectInputStream.readObject() could not find the class
      // definition for an object in the input stream.
      throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
    }
    catch( IOException e )
    {
      // Possible causes:
      // 1. An I/O error occurred while the constructor ObjectInputStream() was
      // reading the stream header.
      // 2. An I/O error occurred while ObjectInputStream.readObject() was
      // reading the input stream.
      // 3. An I/O error occurred while ObjectInputStream.close() was closing
      // the input stream.
      throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
    }

    return clientStateMap;
  }

  /**
   * Serializes, encrypts, and encodes data that will be stored on the client.
   * 
   * @param clientStateMap the data to be serialized, as a Map of String/Serializable pairs that
   *          represent the name and value of a piece of data.
   * @param password the password used to encrypt the client state map.
   * @return the serialized, encrypted, and base-64 encoded data.
   */
  public static String serialize( Map clientStateMap, String password )
  {
    // Serialize the client state.
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    try
    {
      ObjectOutputStream objectOutputStream = new ObjectOutputStream( byteArrayOutputStream );
      objectOutputStream.writeObject( clientStateMap );
      objectOutputStream.close();
    }
    catch( IOException e )
    {
      // An I/O error occurred while the constructor ObjectOutputStream() was
      // writing the stream header.
      throw (BeaconRuntimeException)new BeaconRuntimeException().initCause( e );
    }

    byte[] serializedClientState = byteArrayOutputStream.toByteArray();

    // Encrypt the client state.
    ByteArrayGuard byteArrayGuard = new ByteArrayGuard( password );
    byte[] encryptedClientState = byteArrayGuard.encrypt( serializedClientState );

    // Encode the client state.
    return Base64.encodeBytes( encryptedClientState );
  }
}

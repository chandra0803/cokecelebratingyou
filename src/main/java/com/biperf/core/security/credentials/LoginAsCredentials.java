/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.security.credentials;

import java.io.Serializable;

/**
 * Holder of LoginAs Credential. Just a placeholder class.
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
 * <td>zahler</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LoginAsCredentials implements Serializable
{
  // placeholder class - might have state later to hold old credentials so when we logout we switch
  // back to the original user.
}

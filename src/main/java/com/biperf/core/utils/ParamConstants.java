/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/ParamConstants.java,v $
 */

package com.biperf.core.utils;

/**
 * This class contains the constants used for passing parameters into DAOs.
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
 * <td>Feb 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ParamConstants
{

  /** AUTHENTICATED_USER */
  public static final String AUTHENTICATED_USER = "AuthenticatedUser";
  /** CM_READER */
  public static final String CM_READER = "CMReader";
  /** Used to see if password needs to be upper case before encryption */
  public static final String DEFAULT_UPPERCASE = "defaultUpperCase";
  /** Http Session */
  public static final String HTTP_SESSION = "currentHttpSession";
  
  public static final String ENCRYPTION_PASSWORD = "Enc4&@iut";

}

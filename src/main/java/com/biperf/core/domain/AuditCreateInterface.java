/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/AuditCreateInterface.java,v $
 */

package com.biperf.core.domain;

/**
 * Domain objects implement this interface that track created by and created date properties.
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
 * <td>dunne</td>
 * <td>Apr 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AuditCreateInterface
{
  /**
   * Return a non-null instance of AuditCreateInfo.
   * 
   * @return AuditCreateInfo
   */
  public AuditCreateInfo getAuditCreateInfo();
}

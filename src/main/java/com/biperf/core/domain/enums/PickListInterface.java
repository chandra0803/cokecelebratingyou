/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/PickListInterface.java,v $
 */

package com.biperf.core.domain.enums;

/**
 * Interface for PickLists.
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
 * <td>Apr 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PickListInterface
{

  /**
   * getCode
   * 
   * @return String
   */
  public String getCode();

  /**
   * getName
   * 
   * @return String
   */
  public String getName();

  /**
   * getDescription
   * 
   * @return String
   */
  public String getDescription();

  /**
   * getAbbreviation
   * 
   * @return String
   */
  public String getAbbreviation();

}

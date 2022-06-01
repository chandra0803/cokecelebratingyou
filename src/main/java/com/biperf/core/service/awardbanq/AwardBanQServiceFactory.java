/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/awardbanq/AwardBanQServiceFactory.java,v $
 */

package com.biperf.core.service.awardbanq;

/**
 * AwardBanQServiceFactory.
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
 * <td>Aug 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AwardBanQServiceFactory
{
  public static final String BEAN_NAME = "awardBanQServiceFactory";

  public static final String MOCK = "mock";
  public static final String AWARDBANQ = "awardbanq";
  public static final String NO_AWARDBANQ = "none";

  /**
   * Construct the AwardBanQService instance based on the system variable It should return the Real
   * impl is useBank is True, otherwise it should return the mock impl.
   * 
   * @return AwardBanQService
   */
  public AwardBanQService getAwardBanQService();
}

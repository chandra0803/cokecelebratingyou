/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/GuidUtils.java,v $
 */

package com.biperf.core.utils;

import java.rmi.dgc.VMID;

/*
 * GuidUtils <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug 22, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class GuidUtils
{

  /**
   * Returns a new global unique identifier (GUID).
   * 
   * @return a new GUID.
   */
  public static String generateGuid()
  {
    return new VMID().toString();
  }

}

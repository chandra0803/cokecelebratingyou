/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/product/ProductDAOSuite.java,v $
 */

package com.biperf.core.dao.product;

import com.biperf.core.dao.product.hibernate.ProductCategoryDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ProductDAOSuite is a container for running all Product DAO tests out of container.
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
 * <td>Sathish</td>
 * <td>June 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductDAOSuite extends TestSuite
{

  /**
   * suite to run all Product DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.product.ProductDAOSuite" );

    suite.addTestSuite( ProductDAOImplTest.class );
    suite.addTestSuite( ProductCategoryDAOImplTest.class );

    return suite;
  }

}

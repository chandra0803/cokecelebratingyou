/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/product/ProductServiceSuite.java,v $
 */

package com.biperf.core.service.product;

import com.biperf.core.service.product.impl.ProductCategoryServiceImplTest;
import com.biperf.core.service.product.impl.ProductServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Product Service test suite for running all product service tests out of container.
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
public class ProductServiceSuite extends TestSuite
{

  /**
   * Product Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.product.ProductServiceSuite" );

    suite.addTestSuite( ProductCategoryServiceImplTest.class );
    suite.addTestSuite( ProductServiceImplTest.class );
    return suite;
  }

}

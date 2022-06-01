/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/home/impl/HomePageContentServiceTest.java,v $
 */

package com.biperf.core.service.home.impl;

// import org.jmock.Mock;

import junit.framework.TestCase;

// import com.biperf.core.service.home.impl.HomePageContentServiceImpl;

/**
 * HomePageContentServiceTest.
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
 * <td>Adam</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HomePageContentServiceTest extends TestCase
{
  /** messageServiceImplementation */
  // private HomePageContentServiceImpl homePageContentService = new HomePageContentServiceImpl();

  /** TODO: mock ContentReaderManager */
  // private Mock mockContentReaderManager = null;
  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    // mockContentReaderManager = new Mock( ContentReaderManager.class );
    // homePageContentService.set???( (ContentReaderManager)homePageContentService.proxy() );
  }

  /**
   * Test when ContentReaderManager returns null for a given key *
   */
  public void testGetBannerAdNullTest()
  {
    // ToDO: When mock ContentReaderManager is figured out
  }

  /**
   * Test when ContentReaderManager returns a List for a given key *
   */
  public void testGetBannerAdListTest()
  {
    // ToDO: When mock ContentReaderManager is figured out
  }

  /**
   * Test when ContentReaderManager returns a type other than a List for a given key *
   */
  public void testGetBannerAdNonListTest()
  {
    // ToDO: When mock ContentReaderManager is figured out
  }
}

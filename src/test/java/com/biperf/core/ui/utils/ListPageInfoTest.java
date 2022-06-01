/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/utils/BudgetUtilsTest.java,v $
 */

package com.biperf.core.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.biperf.core.utils.ListPageInfo;

import junit.framework.TestCase;

public class ListPageInfoTest extends TestCase
{
  public void testGetTotalPages()
  {
    ListPageInfo<String> listPageInfo = buildListPageInfo( 50L, 10L, 1L );
    Assert.assertEquals( Long.valueOf( 5 ), listPageInfo.getTotalPages() );

    listPageInfo = buildListPageInfo( 50L, 12L, 1L );
    Assert.assertEquals( Long.valueOf( 5 ), listPageInfo.getTotalPages() );

    listPageInfo = buildListPageInfo( 50L, 15L, 1L );
    Assert.assertEquals( Long.valueOf( 4 ), listPageInfo.getTotalPages() );

    listPageInfo = buildListPageInfo( 50L, 50L, 1L );
    Assert.assertEquals( Long.valueOf( 1 ), listPageInfo.getTotalPages() );

    listPageInfo = buildListPageInfo( 50L, 51L, 1L );
    Assert.assertEquals( Long.valueOf( 1 ), listPageInfo.getTotalPages() );

    listPageInfo = buildListPageInfo( 50L, 1051L, 1L );
    Assert.assertEquals( Long.valueOf( 1 ), listPageInfo.getTotalPages() );

    listPageInfo = buildListPageInfo( 1L, 1051L, 1L );
    Assert.assertEquals( Long.valueOf( 1 ), listPageInfo.getTotalPages() );

    listPageInfo = buildListPageInfo( 0L, 1051L, 1L );
    Assert.assertEquals( Long.valueOf( 0 ), listPageInfo.getTotalPages() );
  }

  public void getCurrentPageList()
  {
    ListPageInfo<String> listPageInfo = buildListPageInfo( 0L, 1051L, 1L );
    assertTrue( listPageInfo.getCurrentPageList().isEmpty() );

    listPageInfo = buildListPageInfo( 1L, 50L, 1L );
    List<String> currentPage = listPageInfo.getCurrentPageList();
    assertEquals( 1, currentPage.size() );
    assertEquals( "object_index_0", currentPage.get( 0 ) );

    listPageInfo = buildListPageInfo( 50L, 50L, 1L );
    currentPage = listPageInfo.getCurrentPageList();
    assertEquals( 50, currentPage.size() );
    assertEquals( "object_index_0", currentPage.get( 0 ) );
    assertEquals( "object_index_49", currentPage.get( 49 ) );

    listPageInfo = buildListPageInfo( 150L, 50L, 1L );
    currentPage = listPageInfo.getCurrentPageList();
    assertEquals( 50, currentPage.size() );
    assertEquals( "object_index_0", currentPage.get( 0 ) );
    assertEquals( "object_index_49", currentPage.get( 49 ) );

    listPageInfo = buildListPageInfo( 150L, 50L, 2L );
    currentPage = listPageInfo.getCurrentPageList();
    assertEquals( 50, currentPage.size() );
    assertEquals( "object_index_50", currentPage.get( 0 ) );
    assertEquals( "object_index_99", currentPage.get( 49 ) );

    listPageInfo = buildListPageInfo( 150L, 50L, 3L );
    currentPage = listPageInfo.getCurrentPageList();
    assertEquals( 50, currentPage.size() );
    assertEquals( "object_index_100", currentPage.get( 0 ) );
    assertEquals( "object_index_149", currentPage.get( 49 ) );

    listPageInfo = buildListPageInfo( 155L, 50L, 3L );
    currentPage = listPageInfo.getCurrentPageList();
    assertEquals( 50, currentPage.size() );
    assertEquals( "object_index_100", currentPage.get( 0 ) );
    assertEquals( "object_index_149", currentPage.get( 49 ) );

    listPageInfo = buildListPageInfo( 155L, 50L, 4L );
    currentPage = listPageInfo.getCurrentPageList();
    assertEquals( 5, currentPage.size() );
    assertEquals( "object_index_150", currentPage.get( 0 ) );
    assertEquals( "object_index_154", currentPage.get( 4 ) );
  }

  public static ListPageInfo<String> buildListPageInfo( Long desiredListSize, Long desiredPageSize, Long currentPage )
  {
    List<String> fullList = new ArrayList<String>();
    for ( int i = 0; i < desiredListSize; i++ )
    {
      fullList.add( "object_index_" + i );
    }

    return new ListPageInfo<String>( fullList, desiredPageSize, currentPage );
  }

}

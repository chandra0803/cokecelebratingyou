/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/StateTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

import org.junit.Ignore;

/**
 * StateTypeTest.
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
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StateTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List stateList = StateType.getList();
    assertNotNull( stateList );
    assertNotSame( stateList, Collections.EMPTY_LIST );
    assertTrue( stateList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item by country and state.
   */
  @Ignore
  public void testLookupPickListItemByStateAndCountry()
  {
    // We're using a mock picklist factory, so the country codes and abbreviations aren't there
    // The method under test here is never actually called.
//    List stateList = StateType.getList( Country.UNITED_STATES );
//    StateType state = (StateType)stateList.get( 0 );
//    StateType state2 = StateType.lookup( Country.UNITED_STATES, state.getAbbr().toLowerCase() );
//    assertEquals( state, state2 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List stateList = StateType.getList();
    StateType state = (StateType)stateList.get( 0 );
    StateType state2 = StateType.lookup( state.getCode() );
    assertEquals( state, state2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List stateList = StateType.getList();
    for ( int i = 0; i < stateList.size(); i++ )
    {
      StateType state = (StateType)stateList.get( i );
      assertNotNull( state );
      assertNotNull( state.getName() );
    }
  }

}

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

public class EnclosureCardOccasionTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List occasionList = EnclosureCardOccasion.getList();
    assertNotNull( occasionList );
    assertNotSame( occasionList, Collections.EMPTY_LIST );
    assertTrue( occasionList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List occasionList = EnclosureCardOccasion.getList();
    EnclosureCardOccasion occasion = (EnclosureCardOccasion)occasionList.get( 0 );
    EnclosureCardOccasion occasion2 = EnclosureCardOccasion.lookup( occasion.getCode() );
    assertEquals( occasion, occasion2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List occasionList = EnclosureCardOccasion.getList();
    for ( int i = 0; i < occasionList.size(); i++ )
    {
      EnclosureCardOccasion occasion = (EnclosureCardOccasion)occasionList.get( i );
      assertNotNull( occasion );
      assertNotNull( occasion.getName() );
    }
  }
}
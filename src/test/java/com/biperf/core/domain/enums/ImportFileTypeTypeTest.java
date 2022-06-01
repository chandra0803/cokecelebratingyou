/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/enums/ImportFileTypeTypeTest.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

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
public class ImportFileTypeTypeTest extends BaseEnumTest
{

  /**
   * Test the ability to get the List
   */
  public void testGetPickList()
  {
    List fileTypeList = ImportFileTypeType.getList();
    assertNotNull( fileTypeList );
    assertNotSame( fileTypeList, Collections.EMPTY_LIST );
    assertTrue( fileTypeList.size() > 0 );
  }

  /**
   * Test the ability to lookup a single list item
   */
  public void testLookupPickListItem()
  {
    List fileTypeList = ImportFileTypeType.getList();
    ImportFileTypeType fileType1 = (ImportFileTypeType)fileTypeList.get( 0 );
    ImportFileTypeType fileType2 = ImportFileTypeType.lookup( fileType1.getCode() );
    assertEquals( fileType1, fileType2 );
  }

  /**
   * Test to make sure the item name is not null - verify description asset is named right.
   */
  public void testPickListItem()
  {
    List fileTypeList = ImportFileTypeType.getList();
    for ( int i = 0; i < fileTypeList.size(); i++ )
    {
      ImportFileTypeType fileType1 = (ImportFileTypeType)fileTypeList.get( i );
      assertNotNull( fileType1 );
      assertNotNull( fileType1.getName() );
    }
  }

}
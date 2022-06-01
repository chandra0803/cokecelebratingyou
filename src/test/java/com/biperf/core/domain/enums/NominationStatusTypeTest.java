
package com.biperf.core.domain.enums;

import java.util.Collections;
import java.util.List;

public class NominationStatusTypeTest extends BaseEnumTest
{

  public void testGetPickList()
  {
    List<NominationClaimStatusType> NominationStatusTypeList = NominationClaimStatusType.getList();
    assertNotNull( NominationStatusTypeList );
    assertNotSame( NominationStatusTypeList, Collections.EMPTY_LIST );
    assertTrue( NominationStatusTypeList.size() > 0 );
  }

  public void testLookupPickListItem()
  {
    List<NominationClaimStatusType> NominationStatusTypeList = NominationClaimStatusType.getList();
    NominationClaimStatusType NominationStatusType1 = (NominationClaimStatusType)NominationStatusTypeList.get( 0 );
    NominationClaimStatusType NominationStatusType2 = NominationClaimStatusType.lookup( NominationStatusType1.getCode() );
    assertEquals( NominationStatusType1, NominationStatusType2 );
  }

  public void testPickListItem()
  {
    List<NominationClaimStatusType> NominationStatusTypeList = NominationClaimStatusType.getList();
    for ( int i = 0; i < NominationStatusTypeList.size(); i++ )
    {
      NominationClaimStatusType NominationStatusType = (NominationClaimStatusType)NominationStatusTypeList.get( i );
      assertNotNull( NominationStatusType );
      assertNotNull( NominationStatusType.getName() );
    }
  }

}

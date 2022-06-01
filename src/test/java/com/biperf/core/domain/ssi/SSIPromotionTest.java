
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomainTest;

public class SSIPromotionTest extends BaseDomainTest
{
  public void testContestSelected()
  {
    SSIPromotion ssiPromotion = new SSIPromotion();

    ssiPromotion.setSelectedContests( 1L );
    assertTrue( ssiPromotion.isAwardThemNowSelected() );

    ssiPromotion.setSelectedContests( 2L );
    assertTrue( ssiPromotion.isDoThisGetThatSelected() );

    ssiPromotion.setSelectedContests( 4L );
    assertTrue( ssiPromotion.isObjectivesSelected() );

    ssiPromotion.setSelectedContests( 8L );
    assertTrue( ssiPromotion.isStackRankSelected() );

    ssiPromotion.setSelectedContests( 16L );
    assertTrue( ssiPromotion.isStepItUpSelected() );

    ssiPromotion.setSelectedContests( 3L );
    assertTrue( ssiPromotion.isAwardThemNowSelected() );
    assertTrue( ssiPromotion.isDoThisGetThatSelected() );

    ssiPromotion.setSelectedContests( 5L );
    assertTrue( ssiPromotion.isAwardThemNowSelected() );
    assertTrue( ssiPromotion.isObjectivesSelected() );

    ssiPromotion.setSelectedContests( 6L );
    assertTrue( ssiPromotion.isObjectivesSelected() );
    assertTrue( ssiPromotion.isDoThisGetThatSelected() );

    ssiPromotion.setSelectedContests( 9L );
    assertTrue( ssiPromotion.isStackRankSelected() );
    assertTrue( ssiPromotion.isAwardThemNowSelected() );

    ssiPromotion.setSelectedContests( 31L );
    assertTrue( ssiPromotion.isAwardThemNowSelected() );
    assertTrue( ssiPromotion.isDoThisGetThatSelected() );
    assertTrue( ssiPromotion.isObjectivesSelected() );
    assertTrue( ssiPromotion.isStackRankSelected() );
    assertTrue( ssiPromotion.isStepItUpSelected() );
  }

  public void testContestNotSelected()
  {
    SSIPromotion ssiPromotion = new SSIPromotion();

    ssiPromotion.setSelectedContests( 0L );
    assertFalse( ssiPromotion.isAwardThemNowSelected() );
    assertFalse( ssiPromotion.isDoThisGetThatSelected() );
    assertFalse( ssiPromotion.isObjectivesSelected() );
    assertFalse( ssiPromotion.isStackRankSelected() );
    assertFalse( ssiPromotion.isStepItUpSelected() );

    ssiPromotion.setSelectedContests( null );
    assertFalse( ssiPromotion.isAwardThemNowSelected() );
    assertFalse( ssiPromotion.isDoThisGetThatSelected() );
    assertFalse( ssiPromotion.isObjectivesSelected() );
    assertFalse( ssiPromotion.isStackRankSelected() );
    assertFalse( ssiPromotion.isStepItUpSelected() );

    ssiPromotion.setSelectedContests( 1L );
    assertFalse( ssiPromotion.isDoThisGetThatSelected() );

    ssiPromotion.setSelectedContests( 2L );
    assertFalse( ssiPromotion.isAwardThemNowSelected() );

    ssiPromotion.setSelectedContests( 4L );
    assertFalse( ssiPromotion.isStepItUpSelected() );

    ssiPromotion.setSelectedContests( 8L );
    assertFalse( ssiPromotion.isObjectivesSelected() );

    ssiPromotion.setSelectedContests( 16L );
    assertFalse( ssiPromotion.isStackRankSelected() );
  }
}

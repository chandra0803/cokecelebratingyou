
package com.biperf.core.service.goalquest.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.value.FormattedValueBean;

public class GetEligibleParticipantsForPromotionTest extends BaseGQTest
{
  GoalQuestPromotion promotion = null;
  Hierarchy primaryHierarchy = null;
  List<Long> activePaxIds = null;
  List<FormattedValueBean> fvbList = null;
  List<FormattedValueBean> fvbList2 = null;

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    // promotion.primaryAudienceType is modified in tests to exercise the conditionals
    promotion = new GoalQuestPromotion();
    // For use in our mockListBuilderService calls
    primaryHierarchy = new Hierarchy();
    primaryHierarchy.setId( 1L );

    activePaxIds = new ArrayList<>();
    fvbList = new ArrayList<>();
    fvbList2 = new ArrayList<>();

    // initialize activePaxIds
    activePaxIds.add( 1L );
    activePaxIds.add( 2L );
    activePaxIds.add( 3L );

    // Initialize fvblists
    FormattedValueBean[] fvbs = new FormattedValueBean[6];
    for ( Integer i = 0; i < fvbs.length; i++ )
    {
      fvbs[i] = new FormattedValueBean();
      fvbs[i].setId( i + 4L );
      if ( i < 3 )
      {
        fvbList.add( fvbs[i] );
      }
      else
      {
        fvbList2.add( fvbs[i] );
      }
    }

  }

  /**
   * <b>Goal:</b> Test that the expected calls are made when the primaryAudienceType
   * is ALL_ACTIVE_PAX
   * <br/><br/>
   * <b>Setup:</b> Initialize test id lists, declare our expectations 
   * <br/><br/>
   * <b>Expected Behavior:</b> If the proper calls are made, a concatenation of
   * activePaxIds and fvblist2 should be returned.
   */
  @Test
  public void testAudienceTypeAllActivePaxAudience()
  {
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    EasyMock.expect( mockHierarchyService.getPrimaryHierarchy() ).andReturn( primaryHierarchy );
    EasyMock.expect( mockParticipantService.getAllActivePaxIds() ).andReturn( activePaxIds );
    EasyMock.expect( mockListBuilderService.searchParticipants( promotion.getSecondaryAudiences(), primaryHierarchy.getId(), true, null, true ) ).andReturn( fvbList2 );

    // activePaxIds + fvbList2
    List<Long> expected = Arrays.asList( new Long[] { 1L, 2L, 3L, 7L, 8L, 9L } );
    mockControl.replay();
    List<Long> result = testInstance.getEligibleParticipantsForPromotion( promotion );
    mockControl.verify();
    assertTrue( result.equals( expected ) );
  }

  /**
   * <b>Goal:</b> Test that the expected calls are made when the primaryAudienceType
   * is null
   * <br/><br/>
   * <b>Setup:</b> Initialize test id lists, declare our expectations 
   * <br/><br/>
   * <b>Expected Behavior:</b> If the proper calls are made, a concatenation of
   * fvblist1 and fvblist2 will be returned (activePaxIds skipped)
   */
  @Test
  public void testAudienceTypeNull()
  {
    promotion.setPrimaryAudienceType( null );

    EasyMock.expect( mockHierarchyService.getPrimaryHierarchy() ).andReturn( primaryHierarchy );
    EasyMock.expect( mockListBuilderService.searchParticipants( promotion.getPrimaryAudiences(), primaryHierarchy.getId(), true, null, true ) ).andReturn( fvbList );
    EasyMock.expect( mockListBuilderService.searchParticipants( promotion.getSecondaryAudiences(), primaryHierarchy.getId(), true, null, true ) ).andReturn( fvbList2 );
    // fvblist1 + fvblist2
    List<Long> expected = Arrays.asList( new Long[] { 4L, 5L, 6L, 7L, 8L, 9L } );
    mockControl.replay();
    List<Long> result = testInstance.getEligibleParticipantsForPromotion( promotion );
    mockControl.verify();
    assertTrue( result.equals( expected ) );
  }

  /**
   * <b>Goal:</b> Test that the expected calls are made when the second
   * list of FormattedValueBeans is null
   * <br/><br/>
   * <b>Setup:</b> Initialize test id lists, declare our expectations 
   * <br/><br/>
   * <b>Expected Behavior:</b> If the proper calls are made, only members of
   * activePaxIds should be returned (Everything else skipped)
   */
  @Test
  public void testSecondPaxListNull()
  {
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );

    EasyMock.expect( mockHierarchyService.getPrimaryHierarchy() ).andReturn( primaryHierarchy );
    EasyMock.expect( mockParticipantService.getAllActivePaxIds() ).andReturn( activePaxIds );
    EasyMock.expect( mockListBuilderService.searchParticipants( promotion.getSecondaryAudiences(), primaryHierarchy.getId(), true, null, true ) ).andReturn( null );
    // Just activePaxIds
    List<Long> expected = Arrays.asList( new Long[] { 1L, 2L, 3L } );
    mockControl.replay();
    List<Long> result = testInstance.getEligibleParticipantsForPromotion( promotion );
    mockControl.verify();
    assertTrue( result.equals( expected ) );
  }
}
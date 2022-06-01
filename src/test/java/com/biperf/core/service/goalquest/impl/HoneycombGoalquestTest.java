
package com.biperf.core.service.goalquest.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.goalquest.GoalsView;
import com.biperf.core.domain.goalquest.PromotionView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.goalquest.ManagerGoalquestViewBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.hc.GoalquestDetailsResponse;
import com.biperf.core.value.hc.GoalquestDetailsResponse.GoalquestProgramResponse;
import com.biw.hc.contest.api.response.EligibleProgramVO;
import com.biw.hc.contest.api.response.GoalSelectionResponse;
import com.biw.hc.contest.api.response.PaxProgressResponse;
import com.biw.hc.contest.api.response.PayoutTierVO;

public class HoneycombGoalquestTest extends BaseGQTest
{

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
  }
  
  @Test
  public void testGetHoneycombProgramDetails()
  {
    Long honeycombUserId = 200L;
    Participant participant = new Participant();
    participant.setFirstName( "Pax" );
    participant.setLastName( "McGee" );
    participant.setId( 100L );
    participant.setHoneycombUserId( honeycombUserId );

    GoalquestDetailsResponse goalquestDetailsResponse = buildGoalquestDetailsResponse();
    EasyMock.expect( mockHCServices.getGoalquestProgramDetails( EasyMock.eq( honeycombUserId ) ) ).andReturn( goalquestDetailsResponse );
    EasyMock.expect( mockHCServices.convertDateString( EasyMock.anyObject() ) ).andReturn( "01/01/74" ).atLeastOnce();
    PropertySetItem siteUrlProperty = new PropertySetItem();
    siteUrlProperty.setEntityName( SystemVariableService.SITE_URL_PREFIX );
    siteUrlProperty.setStringVal( "gyoda" );
    EasyMock.expect( mockSystemVariableService.getPropertyByNameAndEnvironment( EasyMock.eq( SystemVariableService.SITE_URL_PREFIX ) ) ).andReturn( siteUrlProperty ).anyTimes();
    mockControl.replay();

    List<PromotionView> promotions = testInstance.getHoneycombProgramDetails( participant );
    mockControl.verify();
    
    assertTrue( "Expecting one program to come back", promotions.size() == 1 );
    PromotionView promotion = promotions.get( 0 );
    assertTrue( promotion.getId().equals( "1000" ) );
    assertTrue( promotion.getStatus().equals( "started" ) );
    assertTrue( promotion.getAwardIssueRun().equals( "false" ) );
    assertFalse( promotion.isUa() );

    assertTrue( "Expecting one goal", promotion.getGoals().size() == 1 );
    GoalsView goal = promotion.getGoals().get( 0 );
    assertFalse( goal.isCanChange() );
    assertTrue( goal.getProgressValue().equals( "125" ) );
    assertTrue( goal.getIsAchieved() );
    assertTrue( goal.isPercentageExceeds() );
    assertNotNull( goal.getSelectGoalLink() );
    assertNotNull( goal.getGoalLevel() );

    assertTrue( goal.getParticipant().getId().equals( "100" ) );
    assertTrue( goal.getParticipant().getLastName().equals( "McGee" ) );
  }
  
  @Test
  public void testGetHoneycombManagerPrograms()
  {
    Long honeycombUserId = 200L;
    Participant participant = new Participant();
    participant.setFirstName( "Manny" );
    participant.setLastName( "McGee" );
    participant.setId( 100L );
    participant.setHoneycombUserId( honeycombUserId );
    
    GoalquestDetailsResponse goalquestDetailsResponse = new GoalquestDetailsResponse();
    GoalquestProgramResponse programResponse = new GoalquestProgramResponse();
    programResponse.setProgram( buildEligibleProgram() );
    goalquestDetailsResponse.getPrograms().add( programResponse );
    EasyMock.expect( mockHCServices.getGoalquestManagerPrograms( EasyMock.eq( honeycombUserId ) ) ).andReturn( goalquestDetailsResponse );
    EasyMock.expect( mockHCServices.convertDateString( EasyMock.anyObject() ) ).andReturn( "01/01/74" ).atLeastOnce();
    PropertySetItem siteUrlProperty = new PropertySetItem();
    siteUrlProperty.setEntityName( SystemVariableService.SITE_URL_PREFIX );
    siteUrlProperty.setStringVal( "gyoda" );
    EasyMock.expect( mockSystemVariableService.getPropertyByNameAndEnvironment( EasyMock.eq( SystemVariableService.SITE_URL_PREFIX ) ) ).andReturn( siteUrlProperty );
    mockControl.replay();
    
    List<ManagerGoalquestViewBean> results = testInstance.getHoneycombManagerPrograms( honeycombUserId );
    mockControl.verify();
    
    assertTrue( "Expecting one program to come back", results.size() == 1 );
    ManagerGoalquestViewBean view = results.get( 0 );
    assertTrue( view.getPromotionId().equals( 1000L ) );
    assertTrue( view.getPromotionName().equals( "hcgq" ) );
    assertTrue( view.getStartDate().equals( "01/01/74" ) );
    assertTrue( view.isHoneycombProgram() );
  }
  
  @Test
  public void testGetManagerPromotionMenuBeans()
  {
    GoalQuestPromotion gqPromotion = new GoalQuestPromotion();
    gqPromotion.setPromotionType( PromotionType.lookup( PromotionType.GOALQUEST ) );
    gqPromotion.setWebRulesActive( true );
    gqPromotion.setId( 1000L );
    gqPromotion.setManagerWebRulesAudienceType( ManagerWebRulesAudienceType.lookup( ManagerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) );
    PromotionMenuBean promoMenuBean = new PromotionMenuBean();
    promoMenuBean.setPromotion( gqPromotion );
    List<PromotionMenuBean> eligiblePromotions = new ArrayList<>();
    eligiblePromotions.add( promoMenuBean );
    
    EasyMock.expect( mockPromotionService.getPromotionByIdWithAssociations( EasyMock.eq( 1000L ), EasyMock.anyObject() ) ).andReturn( gqPromotion );
    mockControl.replay();
    
    List<PromotionMenuBean> results = testInstance.getManagerPromotionMenuBeans( eligiblePromotions, null, PromotionType.GOALQUEST );
    assertTrue( "Expecting one manager promotion", results.size() == 1 );
    assertTrue( results.get( 0 ).getPromotion() == gqPromotion );
  }

  public static GoalquestDetailsResponse buildGoalquestDetailsResponse()
  {
    GoalquestDetailsResponse details = new GoalquestDetailsResponse();
    GoalquestProgramResponse programResponse = new GoalquestProgramResponse();
    programResponse.setProgram( buildEligibleProgram() );
    programResponse.setGoalSelection( buildGoalSelectionResponse() );
    details.getPrograms().add( programResponse );
    return details;
  }

  public static EligibleProgramVO buildEligibleProgram()
  {
    EligibleProgramVO programVO = new EligibleProgramVO();
    programVO.setProgramId( 1000L );
    programVO.setProgramName( "hcgq" );
    programVO.setProgramType( "goalquest" );
    programVO.setGoalSelectionFromDate( "2017-04-06T00:00:00-0500" );
    programVO.setGoalSelectionToDate( "2017-04-28T23:59:59-0500" );
    programVO.setStartDate( "2017-04-07T00:00:00-0500" );
    programVO.setEndDate( "2017-05-05T23:59:59-0500" );
    programVO.setIsGoalSelected( true );
    programVO.setTimePeriodId( 1L );
    programVO.setProgramViewStatus( "progress_loaded_no_issuance" );
    programVO.setIsMinimumQualifierEnabled( false );
    return programVO;
  }

  public static GoalSelectionResponse buildGoalSelectionResponse()
  {
    GoalSelectionResponse response = new GoalSelectionResponse();
    response.setProgramId( 1000L );
    PayoutTierVO paxGoalSelection = new PayoutTierVO();
    paxGoalSelection.setName( "get goals" );
    paxGoalSelection.setDescription( "description of getting goals" );
    response.setPaxGoalSelection( paxGoalSelection );
    PaxProgressResponse progress = new PaxProgressResponse();
    progress.setProgressPercentage( 125 );
    progress.setDateThrough( "2017-03-15T12:28:00-0500" );
    response.setPaxProgressResponse( progress );
    return response;
  }

}

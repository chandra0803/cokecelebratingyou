
package com.biperf.core.service.goalquest.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserNode;

public class GetGoalQuestMiniProfilesForUserAndPromotionTest extends BaseGQTest
{
  Long promotionId, userId;
  GoalQuestPromotion promotion;
  PartnerAudienceType type;
  CharacteristicDAO mockCharacteristicDAO;

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    promotionId = 1L;
    userId = 2L;
    promotion = new GoalQuestPromotion();
    type = null;
    mockCharacteristicDAO = mockControl.createMock( CharacteristicDAO.class );
    testInstance.setCharacteristicDAO( mockCharacteristicDAO );

  }

  /**
   * <b>Goal:</b> Test the block for node based audiences
   * <br/><br/>
   * <b>Setup:</b> Set the audience type to node based, initialize users and nodes.
   * <br/><br/>
   * <b>Expected Behavior:</b> The result should match paxList
   */
  public void testAudienceTypeNodeBased()
  {
    type = PartnerAudienceType.lookup( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE );
    promotion.setPartnerAudienceType( type );
    // Make a devastatingly large list of usersNodes to loop though
    Set<UserNode> userNodes = new HashSet<UserNode>();
    UserNode node = new UserNode();
    Node innerNode = new Node();
    node.setNode( innerNode );
    userNodes.add( node );
    // We will pretend that userService.getAllParticipantsOnNode gives us this.
    List<User> usersInNode = new ArrayList<User>();
    User user1 = new User(); // Current user
    User user2 = new User(); // Some rando who isn't current user
    user1.setId( userId );
    user2.setId( 3L );
    usersInNode.add( user1 );
    usersInNode.add( user2 );

    List<ParticipantSearchView> paxList = new ArrayList<ParticipantSearchView>();
    Map<String, Object> miniProfileResults = new HashMap<>();
    miniProfileResults.put( "p_out_data", paxList );
    miniProfileResults.put( "p_out_return_code", 0 );

    expect( mockParticipantService.getParticipatForMiniProfile( anyObject(), anyObject(), anyObject() ) ).andReturn( miniProfileResults );
    mockControl.replay();
    List res = testInstance.getGoalQuestMiniProfilesForUserAndPromotion( promotionId, userId );
    mockControl.verify();
    assertTrue( res.equals( paxList ) );
  }

  /**
   * <b>Goal:</b> Test the block for user_char audiences
   * <br/><br/>
   * <b>Setup:</b> Populate some characteristics to iterate over, and some
   * UserCharacteristics for the inner loop.
   * <br/><br/>
   * <b>Expected Behavior:</b> All mock expected calls should be made, meaning
   * the conditionals progressed as expected.
   */
  public void testAudienceTypeUserChar()
  {
    type = PartnerAudienceType.lookup( PartnerAudienceType.USER_CHAR );
    promotion.setPartnerAudienceType( type );
    promotion.setPreSelectedPartnerChars( "5" );
    Participant participant = new Participant();
    participant.setId( 5L );
    List<Characteristic> characteristics = new ArrayList<Characteristic>();
    Characteristic char1 = new UserCharacteristicType();
    Characteristic char2 = new UserCharacteristicType();
    char1.setCharacteristicName( "Link" );
    char1.setId( 3L );
    characteristics.add( char1 );
    char2.setCharacteristicName( "Navi" );
    characteristics.add( char2 );

    UserCharacteristic userChar = new UserCharacteristic();
    userChar.setUserCharacteristicType( (UserCharacteristicType)char1 );
    userChar.setCharacteristicValue( "Hello world!" );
    userChar.setId( 1L );
    participant.addUserCharacteristic( userChar );
    List<UserCharacteristic> userCharList = new ArrayList<UserCharacteristic>();
    userCharList.add( userChar );
    
    List<ParticipantSearchView> paxList = new ArrayList<ParticipantSearchView>();
    Map<String, Object> miniProfileResults = new HashMap<>();
    miniProfileResults.put( "p_out_data", paxList );
    miniProfileResults.put( "p_out_return_code", 0 );

    expect( mockParticipantService.getParticipatForMiniProfile( anyObject(), anyObject(), anyObject() ) ).andReturn( miniProfileResults );

    mockControl.replay();
    List res = testInstance.getGoalQuestMiniProfilesForUserAndPromotion( promotionId, userId );
    mockControl.verify();
    assertTrue( res.equals( paxList ) );
  }

  /**
   * <b>Goal:</b> Test the else clause at the end
   * <br/><br/>
   * <b>Setup:</b> Give the promotion PartnerAudiences, one that is a CriteriaAudience,
   * and another that is not. Do not give it one of the audience types that triggers
   * the previous conditionals (and skips this one). 
   * <br/><br/>
   * <b>Expected Behavior:</b> 
   */
  @Test
  public void testOther()
  {
    type = PartnerAudienceType.lookup( PartnerAudienceType.ENTIRE_PARENT_AUDIENCE_CODE );
    promotion.setPartnerAudienceType( type );
    Set<Audience> audiences = new HashSet<Audience>();
    List<AudienceParticipant> participants = new ArrayList<AudienceParticipant>();

    AudienceParticipant p1 = new AudienceParticipant();
    Participant p1a = new Participant();
    p1a.setId( 1L );
    participants.add( p1 );

    AudienceParticipant p2 = new AudienceParticipant();
    Participant p2a = new Participant();
    p2a.setId( 2L );
    participants.add( p2 );

    CriteriaAudience audience1 = new CriteriaAudience();
    audience1.setAudienceParticipants( participants );

    // audiences.add( audience1 );
    promotion.setPromotionPartnerAudiences( audiences );
    
    List<ParticipantSearchView> paxList = new ArrayList<ParticipantSearchView>();
    Map<String, Object> miniProfileResults = new HashMap<>();
    miniProfileResults.put( "p_out_data", paxList );
    miniProfileResults.put( "p_out_return_code", 0 );

    expect( mockParticipantService.getParticipatForMiniProfile( anyObject(), anyObject(), anyObject() ) ).andReturn( miniProfileResults );

    mockControl.replay();
    List res = testInstance.getGoalQuestMiniProfilesForUserAndPromotion( promotionId, userId );
    mockControl.verify();
  }
}
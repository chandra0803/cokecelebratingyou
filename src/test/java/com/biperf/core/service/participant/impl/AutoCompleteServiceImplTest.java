
package com.biperf.core.service.participant.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CountryValueBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.indexing.Searchable;
import com.biperf.core.value.participant.PaxCard;
import com.biperf.core.value.participant.PaxIndexData;

public class AutoCompleteServiceImplTest extends BaseServiceTest
{
  public static final Long TEST_USER_ID = 100000L;

  PromotionService promotionService;
  ParticipantService participantService;
  
  AutoCompleteServiceImpl testObject;
  
  protected void setUp() throws Exception
  {
    super.setUp();
    
    promotionService = createMock( PromotionService.class );
    participantService = createMock( ParticipantService.class );
    
    testObject = new AutoCompleteServiceImpl()
    {
      protected String getAvatarUrl( String avatar )
      {
        return "123.jpg";
      }
    };
    testObject.setPromotionService( promotionService );
    testObject.setParticipantService( participantService );
  }

  public void testGetPaxCardsWithFrontViewDetails_WhenNoPaxData() throws Exception
  {
    List<PaxCard> paxCards = testObject.getPaxCardsWithFrontViewDetails( null, null );
    assertNotNull( paxCards );
  }

  public void testBuildCardWithFrontViewDetails() throws Exception
  {
    Map<Long, String> map = new HashMap<Long, String>();
    map.put( 1L, "ONE" );
    map.put( 2L, "TWO" );

    PaxIndexData paxdata = new PaxIndexData( TEST_USER_ID, "firName", "lastName" );
    paxdata.setDepartmentTypeCode( DepartmentType.SALES );
    paxdata.setPositionTypeCode( PositionType.EMPLOYEE );
    paxdata.setPrimaryNodeId( 1L );
    paxdata.setAvatar( "123.jpg" );
    
    List<Map<Long, CountryValueBean>> countryResults = new ArrayList<Map<Long, CountryValueBean>>();
    Map<Long, CountryValueBean> countryMap = new HashMap<Long, CountryValueBean>();
    CountryValueBean countryBean = new CountryValueBean();
    countryBean.setCountryCode( "us" );
    countryMap.put( TEST_USER_ID, countryBean );
    countryResults.add( countryMap );
    
    expect( participantService.getParticipantPublicUrl( anyObject() ) ).andReturn( "" );

    PaxCard card = testObject.buildCardWithFrontViewDetails( map, paxdata, Arrays.asList(), countryResults );

    assertEquals( "ONE", card.getOrganization() );
    assertEquals( DepartmentType.SALES, card.getDepartmentName() );
    assertEquals( "123.jpg", card.getAvatarUrl() );

  }

  public void testGetPromotionBean_WhenAudienceSetUpAsAllAudience()
  {
    Promotion p = new NominationPromotion();
    p.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    p.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    p.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );

    PromotionMenuBean menuBean = new PromotionMenuBean();
    menuBean.setPromotion( p );
    
    PaxIndexData receiver = new PaxIndexData();
    receiver.setAudienceIds( Arrays.asList( 1L, 2L ) );
    receiver.setUserId( 5583L );

    assertNotNull( testObject.getPromotionBean( receiver, menuBean ) );
  }

  public void testGetPromotionBean_WhenAudienceSetUpAsSpecificAudience()
  {
    Audience a = new PaxAudience();
    a.setId( 1L );

    Promotion p = new NominationPromotion();
    p.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    p.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) );
    p.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) );

    PromotionSecondaryAudience secondatAud = new PromotionSecondaryAudience( a, p );

    Set<PromotionSecondaryAudience> set = new HashSet<PromotionSecondaryAudience>();
    set.add( secondatAud );

    p.setPromotionSecondaryAudiences( set );

    PaxIndexData receiver = new PaxIndexData();
    receiver.setAudienceIds( Arrays.asList( 1L, 2L ) );
    receiver.setUserId( 5583L );

    PromotionMenuBean menuBean = new PromotionMenuBean();
    menuBean.setPromotion( p );
    
    expect( promotionService.isEasyPromotion( anyObject() ) ).andReturn( true );

    assertNotNull( testObject.getPromotionBean( receiver, menuBean ) );
  }

  public void testGetPromotionBean_WhenAudienceSetUpAsSpecificNode()
  {

    Promotion p = new NominationPromotion();
    p.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    p.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    p.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) );

    PromotionMenuBean menuBean = new PromotionMenuBean();
    menuBean.setPromotion( p );

    PaxIndexData searcher = new PaxIndexData();
    searcher.setPrimaryNodeId( 1L );

    PaxIndexData receiver = new PaxIndexData();
    receiver.setPrimaryNodeId( 1L );
    UserManager.getUser().setPrimaryNodeId( 1L );
    receiver.setUserId( 5583L );

    assertNotNull( testObject.getPromotionBean( receiver, menuBean ) );
  }

  public void testGetPromotionBean_WhenAudienceSetUpAsSpecificNodeButReciverNotInSameNode()
  {
    Promotion p = new NominationPromotion();
    p.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    p.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    p.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) );

    PromotionMenuBean menuBean = new PromotionMenuBean();
    menuBean.setPromotion( p );

    PaxIndexData searcher = new PaxIndexData();
    searcher.setPrimaryNodeId( 1L );

    PaxIndexData receiver = new PaxIndexData();
    receiver.setPrimaryNodeId( 2L );
    receiver.setUserId( 5583L );

    assertNull( testObject.getPromotionBean( receiver, menuBean ) );
  }

  public void testGetPromotionBean_WhenAudienceSetUpAsSpecificNodeAndBelow()
  {
    Promotion p = new NominationPromotion();
    p.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    p.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    p.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) );

    PromotionMenuBean menuBean = new PromotionMenuBean();
    menuBean.setPromotion( p );

    PaxIndexData searcher = new PaxIndexData();
    searcher.setPrimaryNodeId( 2L );

    PaxIndexData receiver = new PaxIndexData();
    receiver.setPrimaryNodeId( 4L );
    UserManager.getUser().setPrimaryNodeId( 4L );
    receiver.setPaths( Arrays.asList( "/1/2/3/4", "/1/2/5/6/7" ) );
    receiver.setUserId( 5583L );
    
    expect( promotionService.isEasyPromotion( anyObject() ) ).andReturn( true );
    replay( promotionService );

    assertNotNull( testObject.getPromotionBean( receiver, menuBean ) );
  }

  public static List<Searchable> createPaxIndexData( int totalData )
  {
    return IntStream.range( 1, totalData + 1 ).mapToObj( n -> new PaxIndexData( (long)n, "firstname", "lastName" ) ).collect( Collectors.toList() );
  }

  public static Searchable getUserPaxIndexData()
  {
    Long userId = UserManager.getUserId();
    return new PaxIndexData( userId, "firstname", "lastName" );
  }

  public static PromotionMenuBean createPromoMenuBean( boolean canSubmit, Promotion promotion )
  {
    PromotionMenuBean b = new PromotionMenuBean();
    b.setCanSubmit( canSubmit );
    b.setPromotion( promotion );
    return b;
  }

  public static List<Promotion> createPromotion( PromotionType promotionType, String name, Long... ids )
  {
    List<Promotion> list = new ArrayList<Promotion>();

    for ( Long id : ids )
    {

      Promotion p = null;
      switch ( promotionType.getCode() )
      {
        case PromotionType.RECOGNITION:
          p = new RecognitionPromotion();
          break;
        case PromotionType.NOMINATION:
          p = new NominationPromotion();
          break;
        default:
          break;
      }
      p.setId( id );
      p.setPromotionType( promotionType );
      list.add( p );

    }

    return list;
  }

  public void createSecondaryAudience( Promotion promotion, Audience audience )
  {
    Set<PromotionSecondaryAudience> secondaryAudience = new HashSet<PromotionSecondaryAudience>( Arrays.asList( new PromotionSecondaryAudience( audience, promotion ) ) );
    promotion.setPromotionSecondaryAudiences( secondaryAudience );

  }

}
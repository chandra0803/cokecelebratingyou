
package com.biperf.core.ui.homepage;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.UserTypeEnum;
import com.biperf.core.process.BaseProcessTest;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.rewardoffering.RewardOfferingsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.services.rest.rewardoffering.domain.RewardOffering;

public class GlobalHeaderControllerTest extends BaseProcessTest
{

  private HttpServletRequest httpRequest = createNiceMock( HttpServletRequest.class );
  private HttpSession httpSession = createNiceMock( HttpSession.class );
  private UserService userService = createNiceMock( UserService.class );
  private CountryService countryService = createNiceMock( CountryService.class );
  private RewardOfferingsService rewardOfferingsService = createNiceMock( RewardOfferingsService.class );
  private MainContentService mainContentService = createNiceMock( MainContentService.class );
  private static SystemVariableService systemVariableService = createNiceMock( SystemVariableService.class );
  private static ParticipantService participantService = createNiceMock( ParticipantService.class );
  private List<RewardOffering> rewardOfferings = new ArrayList<RewardOffering>();

  GlobalHeaderController globalHeaderController = new GlobalHeaderController();

  public GlobalHeaderControllerTest()
  {
    globalHeaderController.setUserService( userService );
    globalHeaderController.setRewardOfferingsService( rewardOfferingsService );
    globalHeaderController.setCountryService( countryService );
    globalHeaderController.setMainContentService( mainContentService );
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    
    GlobalHeaderController.systemVariableService = systemVariableService;
    GlobalHeaderController.participantService = participantService;
  }

  public static AuthenticatedUser getDefaultAuthenticatedUser()
  {
    AuthenticatedUser user = new AuthenticatedUser();
    user.setUserId( new Long( 5583L ) );
    user.setTimeZoneId( TimeZone.getDefault().getID() );
    user.setDelegate( false );
    user.setUserType( UserTypeEnum.PARTICIPANT );
    UserManager.setUser( user );
    ClientStatePasswordManager.setPassword( "password" );
    return user;
  }

  @Test
  @Transactional
  public void test_displayShopLink_With_Plateau_Only() throws Exception
  {
    ShopAndTravelView shopAndTravel = null;

    PromotionType challengePointType = createPromotionType( PromotionType.CHALLENGE_POINT );
    PromotionType nominationsType = createPromotionType( PromotionType.NOMINATION );
    List<PromotionMenuBean> promotionMenuBean = createPromotionMenuBean( Arrays.asList( challengePointType, nominationsType ) );

    Participant participant = createParticipant();
    Country userCountry = createCountry();
    PropertySetItem propertySetItem = createPropertySetItem();

    expect( httpRequest.getSession() ).andReturn( httpSession ).times( 2 );
    expect( httpSession.getAttribute( "showAndTravelView" ) ).andReturn( shopAndTravel ).times( 1 );
    expect( httpSession.getAttribute( "eligiblePromotions" ) ).andReturn( promotionMenuBean ).times( 1 );
    expect( userService.getCountryProgramId( UserManager.getUserId() ) ).andReturn( "1000" ).times( 1 );
    expect( userService.getPrimaryUserAddressCountry( UserManager.getUserId() ) ).andReturn( userCountry ).times( 1 );
    expect( participantService.getParticipantById( UserManager.getUserId() ) ).andReturn( participant ).times( 1 );
    expect( countryService.checkUserSupplier( userCountry.getCountryCode(), Supplier.BII ) ).andReturn( true ).times( 1 );
    expect( rewardOfferingsService.getRewardOfferings( "1000" ) ).andReturn( rewardOfferings ).times( 2 );
    expect( systemVariableService.getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ) ).andReturn( propertySetItem ).times( 1 );
    expect( systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_CONVERTCERT_IS_USED ) ).andReturn( propertySetItem ).times( 1 );
    expect( systemVariableService.getPropertyByName( SystemVariableService.RECOGNITION_ONLY_ENABLED ) ).andReturn( propertySetItem ).times( 1 );
    expect( systemVariableService.getPropertyByName( SystemVariableService.SALES_MAKER ) ).andReturn( propertySetItem ).times( 1 );

    httpSession.setAttribute( "showAndTravelView", shopAndTravel );
    replay( httpRequest, httpSession, userService, countryService, rewardOfferingsService, systemVariableService, participantService, mainContentService );

    globalHeaderController.onExecute( null, httpRequest, null, null );

    assertTrue( true );
  }

  private PropertySetItem createPropertySetItem()
  {
    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setBooleanVal( false );
    return propertySetItem;
  }

  private Country createCountry()
  {
    Country userCountry = new Country();
    userCountry.setCountryCode( "us" );
    return userCountry;
  }

  private Participant createParticipant()
  {
    Participant participant = new Participant();
    participant.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    return participant;
  }

  public List<PromotionMenuBean> createPromotionMenuBean( List<PromotionType> promotionTypes )
  {
    List<PromotionMenuBean> menuBeanList = new ArrayList<PromotionMenuBean>();

    for ( PromotionType promotionType : promotionTypes )
    {
      PromotionMenuBean menuBean = new PromotionMenuBean();
      Promotion promotion = new DummyPromotion();
      promotion.setPromotionType( promotionType );
      promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.MERCHANDISE ) );

      menuBean.setPromotion( promotion );
      menuBean.setCanSubmit( true );
      menuBean.setViewTile( true );
      menuBeanList.add( menuBean );
    }

    return menuBeanList;

  }

  private class DummyPromotion extends Promotion
  {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean hasParent()
    {
      return false;
    }

    @Override
    public boolean isClaimFormUsed()
    {
      return false;
    }
  }

  public PromotionType createPromotionType( String promotionTypeCode )
  {
    PromotionType t = new PromotionSubType();
    t.setCode( promotionTypeCode );
    return t;
  }

  private class PromotionSubType extends PromotionType
  {
    private static final long serialVersionUID = 1L;
  }
}

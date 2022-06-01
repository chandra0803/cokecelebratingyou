
package com.biperf.core.utils.fedresources;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

import junit.framework.TestCase;

public class FEDResourceLocatorFactoryImplTest extends TestCase
{

  private MainContentService mainContentService = createNiceMock( MainContentService.class );

  HttpServletRequest httpRequest = createNiceMock( HttpServletRequest.class );
  HttpSession httpSession = createNiceMock( HttpSession.class );
  AuthorizationService authorizationService = createNiceMock( AuthorizationService.class );
  FilterAppSetupService filterAppSetupService = createNiceMock( FilterAppSetupService.class );
  FEDResourceLocatorFactoryImpl classUnderTest = new FEDResourceLocatorFactoryImpl();

  @Before
  public void setUp()
  {
    this.classUnderTest.setMainContentService( mainContentService );
    this.classUnderTest.setAuthorizationService( authorizationService );
    this.classUnderTest.setFilterAppSetupService( filterAppSetupService );
    AuthenticatedUser user = new AuthenticatedUser();
    user.setUserId( 100L );
    user.setManager( true );
    user.setOwner( true );
    UserManager.setUser( user );
  }

  @After
  public void tearDown()
  {
  }

  @Test
  public void testgetUserFEDResourcesByHttpRequest_WhenMoreModulesIntalled_ButFewAreEligible()
  {
    PromotionType challengePointType = createPromotionType( PromotionType.CHALLENGE_POINT );
    PromotionType nominationsType = createPromotionType( PromotionType.NOMINATION );
    List<PromotionMenuBean> promotionMenuBean = createPromotionMenuBean( Arrays.asList( challengePointType, nominationsType ) );

    expect( httpRequest.getSession() ).andReturn( httpSession ).times( 1 );
    expect( httpSession.getAttribute( EasyMock.anyObject() ) ).andReturn( promotionMenuBean ).times( 1 );
    expect( filterAppSetupService.isUserInDIYAudience( EasyMock.anyObject() ) ).andReturn( true ).times( 1 );
    expect( authorizationService.isUserInRole( EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( true ).times( 1 );
    replay( httpRequest, httpSession, filterAppSetupService, authorizationService );

    UserFEDResources resources = classUnderTest.getUserFEDResources( httpRequest );

    assertNotNull( resources );
    assertTrue( !resources.isRecognitionsEnabled() );
    assertTrue( resources.isGoalquestEnabled() );
    assertTrue( resources.isNominationsEnabled() );
    assertTrue( resources.isReportsEnabled() );

    verify( httpRequest, httpSession, filterAppSetupService, authorizationService );
  }

  @Test
  public void testgetUserFEDResourcesByHttpRequest_WithNoReports()
  {
    PromotionType challengePointType = createPromotionType( PromotionType.CHALLENGE_POINT );
    PromotionType nominationsType = createPromotionType( PromotionType.NOMINATION );
    List<PromotionMenuBean> promotionMenuBean = createPromotionMenuBean( Arrays.asList( challengePointType, nominationsType ) );

    expect( httpRequest.getSession() ).andReturn( httpSession ).times( 1 );
    expect( httpSession.getAttribute( EasyMock.anyObject() ) ).andReturn( promotionMenuBean ).times( 1 );
    expect( filterAppSetupService.isUserInDIYAudience( EasyMock.anyObject() ) ).andReturn( true ).times( 1 );
    expect( authorizationService.isUserInRole( EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( false ).times( 1 );
    replay( httpRequest, httpSession, filterAppSetupService, authorizationService );

    UserFEDResources resources = classUnderTest.getUserFEDResources( httpRequest );

    assertNotNull( resources );
    assertTrue( !resources.isRecognitionsEnabled() );
    assertTrue( !resources.isReportsEnabled() );
    assertTrue( resources.isGoalquestEnabled() );
    assertTrue( resources.isNominationsEnabled() );

    verify( httpRequest, httpSession, filterAppSetupService, authorizationService );
  }

  public List<PromotionMenuBean> createPromotionMenuBean( List<PromotionType> promotionTypes )
  {
    List<PromotionMenuBean> menuBeanList = new ArrayList<PromotionMenuBean>();

    for ( PromotionType promotionType : promotionTypes )
    {
      PromotionMenuBean menuBean = new PromotionMenuBean();
      Promotion promotion = new DummyPromotion();
      promotion.setPromotionType( promotionType );

      menuBean.setPromotion( promotion );
      menuBean.setCanSubmit( true );
      menuBean.setViewTile( true );
      menuBeanList.add( menuBean );
    }

    return menuBeanList;

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
}

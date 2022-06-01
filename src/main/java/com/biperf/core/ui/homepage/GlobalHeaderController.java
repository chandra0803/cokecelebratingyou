
package com.biperf.core.ui.homepage;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.home.MainMenu;
import com.biperf.core.service.home.MenuItems;
import com.biperf.core.service.home.RewardsMenu;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.rewardoffering.RewardOfferingsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.constants.RedeemConstants;
import com.biperf.core.ui.recognition.BaseSubmitRecognitionAction;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.services.rest.rewardoffering.domain.RewardOffering;
import com.google.gson.Gson;
import com.objectpartners.cms.util.CmsUtil;

/**
 * HomePageController.
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
 * <td>robinsra</td>
 * <td>Dec. 6, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class GlobalHeaderController extends BaseController
{
  private static final Log logger = LogFactory.getLog( GlobalHeaderController.class );

  private UserService userService = null;
  private CountryService countryService = null;
  private RewardOfferingsService rewardOfferingsService = null;
  private MainContentService mainContentService = null;
  public static SystemVariableService systemVariableService = null;
  public static ParticipantService participantService = null;

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    buildGlobalHeaderLinks( request );
  }

  private void buildGlobalHeaderLinks( HttpServletRequest request )
  {
    if ( isValidUser() )
    {
      Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
      ShopAndTravelView shopAndTravel = getShowAndTravelView( request );
      List<RewardOffering> rewardsList = getRewardOfferingsService().getRewardOfferings( shopAndTravel.getInternalSupplierProgramId() );
      
      // Changes related to WIP #60999 starts
      /*
       * logger.debug("Rewards List Size--->"+rewardsList.size()); if(logger.isDebugEnabled()) {
       * logger.debug("Rewards List--->"); rewardsList.forEach( System.out::println ); }
       */
      // Changes related to WIP #60999 ends
    
      
      String languageCode = null;
      RewardsMenu rewardsMenu = new RewardsMenu();
      if ( !StringUtil.isEmpty( (String)request.getSession().getAttribute( "cmsLocaleCode" ) ) )
      {
        languageCode = (String)request.getSession().getAttribute( "cmsLocaleCode" );
        rewardsMenu.setMenuName( getMenuTextfromCM( languageCode, participant, RedeemConstants.REDEEM ) );
      }
      verifyEligibleForTravelOfferings( request, rewardsList, shopAndTravel, rewardsMenu, participant );
      paxEligibleForShopOffering( request, rewardsList, shopAndTravel, rewardsMenu, participant );
      Boolean termsAndConditionsUsed = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
      Boolean showTermsAndConditions = isShowTermsAndConditions( participant );
      if ( termsAndConditionsUsed && showTermsAndConditions )
      {

        request.setAttribute( "tAndC", true );
      }
      else
      {
        request.setAttribute( "tAndC", false );
      }
      
      if ( !CollectionUtils.isEmpty( rewardsMenu.getMenu() ) )
      {
        rewardsMenu.setMenu( rewardsMenu.getMenu().stream().sorted( Comparator.comparingInt( MenuItems::getPos ) ).collect( Collectors.toList() ) );
      }
      request.getSession().setAttribute( "newRedeemMenu", new Gson().toJson( new MainMenu( rewardsMenu ) ) );
    }
  }

  private boolean isValidUser()
  {
    return null != UserManager.getUser() && UserManager.getUser().isParticipant() && !UserManager.getUser().isDelegate();
  }

  /*
   * For performance, this is only evaluated at login time
   */
  private ShopAndTravelView getShowAndTravelView( HttpServletRequest request )
  {
    HttpSession httpSession = request.getSession();
    ShopAndTravelView shopAndTravel = (ShopAndTravelView)httpSession.getAttribute( "showAndTravelView" );
    if ( null == shopAndTravel )
    {
      shopAndTravel = new ShopAndTravelView();
      // String programId = getUserService().getCountryProgramIdByUserId( UserManager.getUserId() );
      String programId = getUserService().getCountryProgramId( UserManager.getUserId() );

      if ( programId != null )
      {
        shopAndTravel.setInternalSupplierProgramId( programId );
        List<RewardOffering> rewardsList = getRewardOfferingsService().getRewardOfferings( programId );

        // travel check
        if ( Objects.nonNull( findRewardOffering( "travel", rewardsList ) ) )
        {
          shopAndTravel.setShowTravel( true );
        }
        // merchandise check
        if ( displayShopLink( request ) )
        {
          shopAndTravel.setShowShop( true );
          Country userCountry = getUserService().getPrimaryUserAddressCountry( UserManager.getUserId() );
          boolean isBiiSupplier = checkCurrentUserSupplierIsBii( userCountry.getCountryCode() );
          if ( isBiiSupplier )
          {
            boolean isExperience = checkCurrentUserBiiExperienceEnabled( userCountry.getCountryCode(), programId );
            shopAndTravel.setBiiExperience( isExperience );
          }
          shopAndTravel.setBiiSupplier( isBiiSupplier );
        }

      }
    }
    httpSession.setAttribute( "showAndTravelView", shopAndTravel );

    return shopAndTravel;
  }

  @SuppressWarnings( "unchecked" )
  private boolean displayShopLink( HttpServletRequest request )
  {
    boolean driveEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();
    boolean otsEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.AWARDBANQ_CONVERTCERT_IS_USED ).getBooleanVal();
    boolean dayMakerEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.RECOGNITION_ONLY_ENABLED ).getBooleanVal();
    boolean salesMakerEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.SALES_MAKER ).getBooleanVal();

    if ( driveEnabled )
    {
      return false;
    }

    if ( dayMakerEnabled || salesMakerEnabled )
    {
      return true;
    }

    if ( !otsEnabled )
    {
      Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
      if ( StringUtils.isEmpty( participant.getAwardBanqNumber() ) )
      {
        List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)request.getSession().getAttribute( "eligiblePromotions" );
        if ( null == eligiblePromotions )
        {
          eligiblePromotions = getMainContentService().buildEligiblePromoList( UserManager.getUser() );
          request.getSession().setAttribute( "eligiblePromotions", eligiblePromotions );
        }
        if ( CollectionUtils.isNotEmpty( eligiblePromotions ) )
        {
          // first filter out the nomination promotions since we they are not part of Plateau award
          // type.
          List<PromotionMenuBean> filteredEligiblePromotions = eligiblePromotions.stream().filter( p -> !p.getPromotion().isNominationPromotion() ).collect( Collectors.toList() );
          if ( CollectionUtils.isNotEmpty( filteredEligiblePromotions ) )
          {
            long countOfPlateauPromotionsOnly = filteredEligiblePromotions.stream()
                .filter( p -> p.getPromotion().getAwardType() != null && p.getPromotion().getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.MERCHANDISE ) ) ).count();
            // Just making sure if all the eligible promotions are of Plateau promotion only.
            if ( countOfPlateauPromotionsOnly == filteredEligiblePromotions.size() )
            {
              return false;
            }
          }
        }
      }
    }

    return true;

  }

  private void verifyEligibleForTravelOfferings( HttpServletRequest request, List<RewardOffering> rewardsList, ShopAndTravelView shopAndTravel, RewardsMenu rewardsMenu, Participant participant )
  {
    if ( !shopAndTravel.isShowTravel() )
    {
      return;
    }

    RewardOffering rewardOffering = findRewardOffering( "travel", rewardsList );
    if ( Objects.nonNull( rewardOffering ) )
    {
        setExperience( request, rewardOffering, rewardsMenu, participant );
    }
  }

  private void setExperience( HttpServletRequest request, RewardOffering rewardOffering, RewardsMenu rewardsMenu, Participant participant )
  {
    request.setAttribute( "rewardOfferingTravelExist", true );
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    MenuItems items = new MenuItems();
    String languageCode = null;
    String menuExperience = null;
    int displayOrder = 0;
    if ( Objects.nonNull( rewardOffering ) )
    {
      parameterMap.put( "page", rewardOffering.getSsoDestination() );
      displayOrder = Objects.nonNull( rewardOffering.getDisplayOrder() ) ? rewardOffering.getDisplayOrder() : 2;
    }
    String url = ClientStateUtils.generateEncodedLink( "", "submitRewardOffering.do?method=displayExperience", parameterMap );
    request.setAttribute( "rewardOfferingTravelUrl", url );

    if ( !StringUtil.isEmpty( (String)request.getSession().getAttribute( "cmsLocaleCode" ) ) )
    {
      languageCode = (String)request.getSession().getAttribute( "cmsLocaleCode" );
      menuExperience = getMenuTextfromCM( languageCode, participant, RedeemConstants.MENU_EXPERIENCES );
      items = new MenuItems( menuExperience, url, getCMValue( participant, RedeemConstants.REDEEM_MENU, RedeemConstants.EXPERIENCELOGO ), displayOrder > 0 ? displayOrder : 2 );
    }
    
    rewardsMenu.getMenu().add( items );
  }

  private void paxEligibleForShopOffering( HttpServletRequest request, List<RewardOffering> rewardsList, ShopAndTravelView shopAndTravel, RewardsMenu rewardsMenu, Participant participant )
  {
    if ( shopAndTravel.isShowShop() )
    {
      MenuItems menuItems = new MenuItems();
      request.setAttribute( "rewardOfferingShopExist", true );
      Map<String, Object> parameterMap = new HashMap<String, Object>();

      if ( shopAndTravel.isBiiSupplier() )
      {
        RewardOffering rewardOffering = findRewardOffering( "travel", rewardsList );
        RewardOffering shopOffering = null;
        parameterMap.put( "externalSupplierId", getSupplierService().getSupplierByName( Supplier.BII ).getId() );
        String url = ClientStateUtils.generateEncodedLink( "", PageConstants.SHOPPING_EXTERNAL, parameterMap );
        request.setAttribute( "rewardOfferingShopUrl", url );
        request.getSession().setAttribute( "isExternalSupplier", true );
        String languageCode;
        String menuMerchandise = null;
        if ( !StringUtil.isEmpty( (String)request.getSession().getAttribute( "cmsLocaleCode" ) ) )
        {
          languageCode = (String)request.getSession().getAttribute( "cmsLocaleCode" );
          menuMerchandise = getMenuTextfromCM( languageCode, participant, RedeemConstants.MENU_MERCHANDISE );
          menuItems = new MenuItems( menuMerchandise, url, getCMValue( participant, RedeemConstants.REDEEM_MENU, RedeemConstants.MERCHANDISELOGO ), 1 );
        }

        rewardsMenu.getMenu().add( menuItems );
        if ( Objects.isNull( rewardOffering ) )
        {
          if ( shopAndTravel.isBiiExperience() )
          {
            setExperience( request, shopOffering, rewardsMenu, participant );
          }
        }
      }
      else
      {
        RewardOffering shopOffering = findRewardOffering( "merchandise", rewardsList );
        String imagePath = "";
        if ( null != shopOffering )
        {
          parameterMap.put( "page", shopOffering.getSsoDestination() );
          builRewardsMenu( request, participant, parameterMap, rewardsMenu, menuItems, imagePath, shopOffering );

        }
        else
        {
          builRewardsMenu( request, participant, parameterMap, rewardsMenu, menuItems, imagePath, shopOffering );
        }
      }
    }
    
  }

  private String getCMValue( Participant participant, String asset, String key )
  {
      String cmValue = getCMAssetService().getString( asset,
              key,
              CmsUtil.getLocale( participant.getLanguageType() != null
                  ? participant.getLanguageType().getCode()
                  : getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() ),
              true );
return cmValue;
}

private String getCMValue( String languageCode, String asset, String key )
{
String cmValue = getCMAssetService().getString( asset, key, CmsUtil.getLocale( languageCode ), true );
return cmValue;
}

private void builRewardsMenu( HttpServletRequest request,
Participant participant,
Map<String, Object> parameterMap,
RewardsMenu rewardsMenu,
MenuItems menuItems,
String imagePath,
RewardOffering shopOffering )
{
String url = ClientStateUtils.generateEncodedLink( "", "submitRewardOffering.do?method=displayInternal", parameterMap );
request.setAttribute( "rewardOfferingShopUrl", url );
request.getSession().setAttribute( "isExternalSupplier", false );
imagePath = getCMValue( participant, RedeemConstants.REDEEM_MENU, RedeemConstants.MERCHANDISELOGO );
String languageCode;
String menuMerchandise = null;
if ( !StringUtil.isEmpty( (String)request.getSession().getAttribute( "cmsLocaleCode" ) ) )
    {
    languageCode = (String)request.getSession().getAttribute( "cmsLocaleCode" );
    menuMerchandise = getMenuTextfromCM( languageCode, participant, RedeemConstants.MENU_MERCHANDISE );
    menuItems = new MenuItems( menuMerchandise, url, imagePath, Objects.nonNull( shopOffering ) ? shopOffering.getDisplayOrder() : 1 );
    }

rewardsMenu.getMenu().add( menuItems );

  }

  private RewardOffering findRewardOffering( String type, List<RewardOffering> rewardsList )
  {
    if ( !isNotEmpty( rewardsList ) )
    {
      return null;
    }
    Optional<RewardOffering> optRewardOffering = rewardsList.stream().filter( e -> type.equalsIgnoreCase( e.getType() ) ).findFirst();
    if ( optRewardOffering.isPresent() )
    {
      return optRewardOffering.get();
    }
    return null;
  }

  private UserService getUserService()
  {
    if ( null == userService )
    {
      return (UserService)getService( UserService.BEAN_NAME );
    }
    return userService;
  }

  private RewardOfferingsService getRewardOfferingsService()
  {
    if ( null == rewardOfferingsService )
    {
      return (RewardOfferingsService)getService( RewardOfferingsService.BEAN_NAME );
    }
    return rewardOfferingsService;
  }

  private boolean checkCurrentUserSupplierIsBii( String countryCode )
  {
    return getCountryService().checkUserSupplier( countryCode, Supplier.BII );
  }
  
  private boolean checkCurrentUserBiiExperienceEnabled( String countryCode, String programId )
  {
    return getCountryService().checkBiiExperience( countryCode, programId );
  }

  private boolean isShowTermsAndConditions( Participant sessionUser )
  {
    if ( sessionUser.getTermsAcceptance() != null
        && ( sessionUser.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.NOTACCEPTED ) || sessionUser.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.DECLINED ) ) )
    {
      return true;
    }
    return false;
  }
  
  private String getMenuTextfromCM( String languageCode, Participant participant, String key )
  {
    String menuName = null;
    if ( !languageCode.equals( "en_US" ) )
    {
      menuName = getCMValue( languageCode, RedeemConstants.REDEEM_MENU, key );
    }
    else
    {
      menuName = getCMValue( participant, RedeemConstants.REDEEM_MENU, key );
    }
    return menuName;
  }

  private CountryService getCountryService()
  {
    if ( null == countryService )
    {
      return (CountryService)getService( CountryService.BEAN_NAME );
    }
    return countryService;
  }

  private MainContentService getMainContentService()
  {
    if ( null == mainContentService )
    {
      return (MainContentService)getService( MainContentService.BEAN_NAME );
    }
    return mainContentService;
  }

  private static SystemVariableService getSystemVariableService()
  {
    if ( null == systemVariableService )
    {
      return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
    }
    return systemVariableService;

  }

  public static ParticipantService getParticipantService()
  {
    if ( null == participantService )
    {
      return (ParticipantService)getService( ParticipantService.BEAN_NAME );
    }
    return participantService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  public void setRewardOfferingsService( RewardOfferingsService rewardOfferingsService )
  {
    this.rewardOfferingsService = rewardOfferingsService;
  }

  public void setMainContentService( MainContentService mainContentService )
  {
    this.mainContentService = mainContentService;
  }

  private SupplierService getSupplierService()
  {
    return (SupplierService)getService( SupplierService.BEAN_NAME );
  }
  
  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }
}

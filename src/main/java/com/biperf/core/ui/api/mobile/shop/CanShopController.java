
package com.biperf.core.ui.api.mobile.shop;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.rewardoffering.RewardOfferingsService;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.services.rest.rewardoffering.domain.RewardOffering;
import com.objectpartners.cms.util.CmsResourceBundle;

@Controller
@RequestMapping( "/mobile" )
public class CanShopController extends SpringBaseController
{
  private static final Log logger = LogFactory.getLog( CanShopController.class );

  public @Autowired MainContentService mainContentService;
  public @Autowired ParticipantService participantService;
  public @Autowired ShoppingService shoppingService;
  public @Autowired UserService userService;
  public @Autowired RewardOfferingsService rewardOfferingsService;

  @RequestMapping( value = "/canShop.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody MobileShopView canShop()
  {
    MobileShopView mobileShopView = new MobileShopView();

    mobileShopView.setReturnCode( 99L );
    mobileShopView.setBalance( 0L );

    boolean showShopLink = mainContentService.checkShowShopORTravel();
    mobileShopView.setShowShopLink( showShopLink );
    mobileShopView.setSupplierType( shoppingService.checkShoppingType( UserManager.getUserId() ) );
    if ( showShopLink )
    {
      mobileShopView.setMediaType( CmsResourceBundle.getCmsBundle().getString( "participant.myaccount.POINTS" ) );

      ProjectionCollection projections = new ProjectionCollection();
      projections.add( new ProjectionAttribute( "awardBanqNumberDecrypted" ) );
      Participant pax = participantService.getParticipantByIdWithProjections( UserManager.getUserId(), projections );

      try
      {
        Long balance = getAwardBanQService().getAccountBalanceForParticipantWebService( pax.getId(), pax.getAwardBanqNumber() );
        mobileShopView.setBalance( balance );

        // travel check
        String programId = userService.getCountryProgramId( UserManager.getUserId() );

        if ( programId != null )
        {
          List<RewardOffering> rewardsList = rewardOfferingsService.getRewardOfferings( programId );

          if ( isNotEmpty( rewardsList ) )
          {
            String type = "travel";
            Optional<RewardOffering> optRewardOffering = rewardsList.stream().filter( e -> type.equalsIgnoreCase( e.getType() ) ).findFirst();
            mobileShopView.setShowTravelLink( optRewardOffering.isPresent() );
          }
        }

        mobileShopView.setReturnCode( 0L );
      }
      catch( Exception e )
      {
        logger.error( "Error getting balance for username: " + pax.getFirstName() + " " + pax.getLastName(), e );
        mobileShopView.setReturnMessage( CmsResourceBundle.getCmsBundle().getString( "Error getting balance for username: " + pax.getFirstName() + " " + pax.getLastName() ) );
      }
    }
   
    return mobileShopView;
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }
}

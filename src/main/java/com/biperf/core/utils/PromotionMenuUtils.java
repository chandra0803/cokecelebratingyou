
package com.biperf.core.utils;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.participant.UserService;

public class PromotionMenuUtils
{
  /**
   * Return 'Browse Awards' URL.
   *
   * @param baseURI
   * @param promotion
   * @return
   */
  public static String getBrowseAwardsLink( String baseURI, Promotion promotion )
  {
    String link = null;

    Country country = getUserService().getPrimaryUserAddressCountry( UserManager.getUserId() );

    if ( country != null )
    {
      Map params = new HashMap();
      params.put( "promotionId", promotion.getId() );
      params.put( "countryId", country.getId() );
      AbstractRecognitionPromotion recPromotion = (AbstractRecognitionPromotion)promotion;
      PromoMerchCountry promoMerchCountry = recPromotion.getPromoMerchCountryForCountryCode( country.getCountryCode() );
      if ( promoMerchCountry == null && recPromotion.getPromoMerchCountries() != null && recPromotion.getPromoMerchCountries().size() > 0 )
      {
        promoMerchCountry = (PromoMerchCountry)recPromotion.getPromoMerchCountries().iterator().next();
      }
      if ( promoMerchCountry != null )
      {
        String action = "/promotionRecognition/displayMerchLevelsDetail.do";
        // Below line is added to fix Bug 24737 -- START
        params.put( "promotionIdToDisplayLevelNames", promotion.getId() );
        // END
        params.put( "programId", promoMerchCountry.getProgramId() );
        String url = ClientStateUtils.generateEncodedLink( "", action, params, true );
        link = "openModalPopup('" + url + "',POPUP_SIZE_LARGE,true)";
      }
    }

    return link;
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }
}

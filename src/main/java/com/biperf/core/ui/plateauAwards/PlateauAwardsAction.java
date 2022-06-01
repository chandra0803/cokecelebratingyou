
package com.biperf.core.ui.plateauAwards;

/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/plateauAwards/PlateauAwardsAction.java,v $
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.plateauawards.PlateauAwardsDetailBean;
import com.biperf.core.domain.plateauawards.PlateauAwardsDetailsView;
import com.biperf.core.domain.plateauawards.PlateauMerchLevels;
import com.biperf.core.domain.plateauawards.PlateauMerchProducts;
import com.biperf.core.domain.plateauawards.PlateauMerchProductsView;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelProductValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.awardbanq.impl.ProductEntryVO;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.WhatsNewBean;

/**
 * WhatsNewController.
 * 
 *
 */
public class PlateauAwardsAction extends BaseDispatchAction
{

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.plateauAwardResult( mapping, form, request, response );
    }
  }

  public ActionForward view( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String clientState = request.getParameter( "clientState" );
    String cryptoPass = request.getParameter( "cryptoPass" );
    if ( StringUtils.isNotEmpty( clientState ) && StringUtils.isNotEmpty( cryptoPass ) )
    {
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      String proId = (String)clientStateMap.get( "promotionId" );
      Long promotionId = Long.valueOf( proId );
      request.setAttribute( "promotionId", promotionId );
    }
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward plateauAwardResult( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    // Build a list of eligible promotions if the user is a participant, then put that list
    // in the request so other controllers used in building the home page can use the list from
    // the request instead of getting the list again.
    List eligiblePromoList = new ArrayList();
    List whatsNewList = new ArrayList();
    String baseUri = RequestUtils.getBaseURI( request );
    Country country = null;
    if ( UserManager.getUser().isParticipant() )
    {
      eligiblePromoList = getEligiblePromotions( request );
      country = getUserService().getPrimaryUserAddressCountry( UserManager.getUserId() );
    }
    if ( eligiblePromoList != null && eligiblePromoList.size() > 0 && country != null )
    {
      MainContentService mainContentService = (MainContentService)getService( MainContentService.BEAN_NAME );
      whatsNewList = mainContentService.getWhatsNewList( eligiblePromoList, country );
      if ( whatsNewList != null && whatsNewList.size() > 0 )
      {
        request.setAttribute( "promotionName", ( (WhatsNewBean)whatsNewList.get( 0 ) ).getHomePageItem().getPromotion().getName() );
      }
    }
    PlateauMerchProductsView productsView = new PlateauMerchProductsView();
    if ( whatsNewList != null )
    {
      productsView.setProducts( buildProductsView( whatsNewList, baseUri ) );
      super.writeAsJsonToResponse( productsView, response );
    }
    return null;

  }

  public ActionForward detailViewResult( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<PlateauAwardsDetailBean> promotionMenuList = new ArrayList<PlateauAwardsDetailBean>();
    Country country = getUserService().getPrimaryUserAddressCountry( UserManager.getUserId() );
    Promotion promotion = null;
    if ( !StringUtil.isEmpty( request.getParameter( "promotionId" ) ) )
    {
      promotion = getPromotionService().getPromotionById( Long.parseLong( request.getParameter( "promotionId" ) ) );
      AbstractRecognitionPromotion recPromotion = (AbstractRecognitionPromotion)promotion;
      PromoMerchCountry promoMerchCountry = recPromotion.getPromoMerchCountryForCountryCode( country.getCountryCode() );
      String programId = null;
      if ( promoMerchCountry == null && recPromotion.getPromoMerchCountries() != null && recPromotion.getPromoMerchCountries().size() > 0 )
      {
        promoMerchCountry = (PromoMerchCountry)recPromotion.getPromoMerchCountries().iterator().next();
        programId = promoMerchCountry.getProgramId();
      }
      else if ( promoMerchCountry != null )
      {
        programId = promoMerchCountry.getProgramId();
      }
      AwardBanqMerchResponseValueObject data = getMerchLevelService().getMerchlinqLevelDataWebService( programId, true, true );

      if ( UserManager.getUser().isParticipant() )
      {
        UserAddress userAddress = getUserService().getPrimaryUserAddress( UserManager.getUserId() );
        AssociationRequestCollection arCollection = new AssociationRequestCollection();
        arCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
        List countryList = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promotion.getId(), arCollection );
        getMerchLevelService().mergeMerchLevelWithOMList( countryList );

        // Pick the right Country data from CountryList based on user country
        boolean isPAXCountryIsInPromoCountriesList = false;
        for ( int i = 0; i < countryList.size(); i++ )
        {
          promoMerchCountry = (PromoMerchCountry)countryList.get( i );
          if ( userAddress.getAddress().getCountry().getCountryCode().equals( promoMerchCountry.getCountry().getCountryCode() ) )
          {
            isPAXCountryIsInPromoCountriesList = true;
            break;
          }
        }
        // set the required Levels data in request.
        // Note:- if PAX Country is in Promo Countries then display corresponding Level names.
        // Example 'silver' , 'Gold' etc..,
        // If PAX country is not in Promo countries then display 'Level1', 'Level2','Level3' etc..,
        if ( isPAXCountryIsInPromoCountriesList )
        {
          List<PlateauMerchLevels> listLevels = buildMerchLevelList( promoMerchCountry.getLevels(), data );
          PlateauAwardsDetailBean plateauAwardBean = new PlateauAwardsDetailBean();
          plateauAwardBean.setId( promotion.getId().toString() );
          plateauAwardBean.setName( promotion.getName() );
          listLevels.sort( Comparator.comparing( PlateauMerchLevels::getName ) );
          plateauAwardBean.setLevels( listLevels );
          promotionMenuList.add( plateauAwardBean );
        }
      }
    }
    // above code added to fix Bug 24737 -- END
    promotionMenuList = getEligiblePromotionsDropdown( promotionMenuList, promotion == null ? 0L : promotion.getId(), country );
    PlateauAwardsDetailsView detailView = new PlateauAwardsDetailsView();
    detailView.setPromotions( promotionMenuList );
    super.writeAsJsonToResponse( detailView, response );
    return null;
  }

  private List<PlateauAwardsDetailBean> getEligiblePromotionsDropdown( List<PlateauAwardsDetailBean> promotionMenuList, Long promotionId, Country country )
  {
    List eligiblePromotions = new ArrayList();
    List whatsNewList = new ArrayList();
    PlateauAwardsDetailBean promotionsEligiable = null;
    eligiblePromotions = getMainContentService().buildEligiblePromoList( UserManager.getUser() );
    whatsNewList = getMainContentService().getWhatsNewList( eligiblePromotions, country );
    for ( int i = 0; i < whatsNewList.size(); i++ )
    {
      Long promoId;
      String promoName = "";
      WhatsNewBean whatNewBean = (WhatsNewBean)whatsNewList.get( i );
      promoId = whatNewBean.getHomePageItem().getPromotion().getId();
      promoName = whatNewBean.getHomePageItem().getPromotion().getName();
      if ( promoId.intValue() != promotionId.intValue() )
      {
        promotionsEligiable = new PlateauAwardsDetailBean();
        promotionsEligiable.setId( promoId.toString() );
        promotionsEligiable.setName( promoName );
        promotionMenuList.add( promotionsEligiable );
      }
    }

    return promotionMenuList;
  }

  private List<PlateauMerchLevels> buildMerchLevelList( Set levelSet, AwardBanqMerchResponseValueObject data )
  {
    List<PlateauMerchLevels> listLevels = new ArrayList<PlateauMerchLevels>();
    PlateauMerchLevels levels = null;
    Iterator setItr = null;
    setItr = levelSet.iterator();

    while ( setItr.hasNext() )
    {
      PromoMerchProgramLevel programLevel = (PromoMerchProgramLevel)setItr.next();
      levels = new PlateauMerchLevels();
      levels.setId( programLevel.getId().toString() );
      levels.setDesc( programLevel.getLevelName() );
      levels.setName( programLevel.getDisplayLevelName() );
      levels.setProducts( getMerchLevelProducts( data, programLevel.getLevelName() ) );
      listLevels.add( levels );
    }
    return listLevels;

  }

  private List<PlateauMerchProducts> getMerchLevelProducts( AwardBanqMerchResponseValueObject data, String levelName )
  {

    List list = (List)data.getMerchLevel();
    Iterator listItr = null;
    List<PlateauMerchProducts> productsList = new ArrayList<PlateauMerchProducts>();
    PlateauMerchProducts levelProducts = null;
    if ( list != null )
    {
      listItr = list.iterator();
    }
    if ( listItr != null )
    {
      while ( listItr.hasNext() )
      {
        MerchLevelValueObject merchLevel = (MerchLevelValueObject)listItr.next();
        if ( merchLevel.getName().equalsIgnoreCase( levelName ) )
        {
          Collection col = (Collection)merchLevel.getMerchLevelProduct();
          if ( col != null )
          {
            Iterator itr = col.iterator();
            while ( itr.hasNext() )
            {
              MerchLevelProductValueObject mlp = (MerchLevelProductValueObject)itr.next();
              levelProducts = new PlateauMerchProducts();
              levelProducts.setId( mlp.getProductSetId() );

              for ( Iterator<ProductEntryVO> entryIter = mlp.getProductGroupDescriptions().getEntry().iterator(); entryIter.hasNext(); )
              {
                ProductEntryVO entryVO = entryIter.next();

                if ( entryVO.getValue().getLocale().equals( UserManager.getUserLocale() ) )
                {
                  if ( entryVO != null )
                  {
                    String desc = entryVO.getValue().getCopy().replace( "\"", "'" );

                    levelProducts.setDesc( desc );
                    levelProducts.setName( entryVO.getValue().getDescription() );
                  }
                }
              }

              levelProducts.setThumbnail( mlp.getThumbnailImageURL() + "" );
              levelProducts.setImg( mlp.getDetailImageURL() + "" );

              // update image urls with https
              if ( !StringUtils.isEmpty( levelProducts.getThumbnail() ) )
              {
                levelProducts.setThumbnail( levelProducts.getThumbnail().toString().replaceFirst( "http:", "https:" ) );
              }
              if ( !StringUtils.isEmpty( levelProducts.getImg() ) )
              {
                levelProducts.setImg( levelProducts.getImg().toString().replaceFirst( "http:", "https:" ) );
              }

              productsList.add( levelProducts );
            }
          }
        }
      }
    }
    return productsList;
  }

  private String escapeColumnValue( Object rawValue )
  {
    String stringValue = StringUtils.trim( rawValue.toString() );

    if ( stringValue == null )
    {
      return null;
    }
    stringValue = StringUtils.replace( stringValue, "\"", "&quot" );

    return stringValue;

  }

  public List<PlateauMerchProducts> buildProductsView( List<WhatsNewBean> productList, String baseUri )
  {

    List<PlateauMerchProducts> productsList = new ArrayList<PlateauMerchProducts>();

    for ( WhatsNewBean whatsNewBean : productList )
    {
      PlateauMerchProducts productsView = new PlateauMerchProducts();
      String promotionId = whatsNewBean.getHomePageItem().getPromotion().getId().toString();
      productsView.setId( promotionId );
      productsView.setName( whatsNewBean.getHomePageItem().getPromotion().getName() );
      productsView.setThumbnail( whatsNewBean.getHomePageItem().getDetailImgUrl() );
      Map params = new HashMap();
      params.put( "promotionId", promotionId );
      String action = "/plateauAwardsViewDetail.do?method=view";
      productsView.setUrl( ClientStateUtils.generateEncodedLink( baseUri, action, params ) );
      productsList.add( productsView );

    }
    return productsList;
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

}

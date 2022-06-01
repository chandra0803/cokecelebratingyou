
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class PromotionSSITranslationsForm extends BaseActionForm
{

  private String method;

  private String returnActionUrl;

  private Long promotionId;

  private String promotionName;

  private String promotionTypeCode;

  private String promotionTypeName;

  private String promotionStatus;

  private String version;

  private List<PromotionSSITranslationsFormBean> translations = new ArrayList<PromotionSSITranslationsFormBean>();

  private List<PromotionSSITranslationsFormBean> levelTranslations = new ArrayList<PromotionSSITranslationsFormBean>();

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public List<PromotionSSITranslationsFormBean> getTranslations()
  {
    return translations;
  }

  public void setTranslations( List<PromotionSSITranslationsFormBean> translations )
  {
    this.translations = translations;
  }

  public PromotionSSITranslationsFormBean getTranslation( int index )
  {

    // indexes to not come in order, populate empty spots
    while ( index >= this.translations.size() )
    {
      this.translations.add( new PromotionSSITranslationsFormBean() );
    }

    // return the requested item
    return this.translations.get( index );
  }

  public List<PromotionSSITranslationsFormBean> getLevelTranslations()
  {
    return levelTranslations;
  }

  public void setLevelTranslations( List<PromotionSSITranslationsFormBean> levelTranslations )
  {
    this.levelTranslations = levelTranslations;
  }

  public PromotionSSITranslationsFormBean getLevelTranslation( int index )
  {

    // indexes to not come in order, populate empty spots
    while ( index >= this.levelTranslations.size() )
    {
      this.levelTranslations.add( new PromotionSSITranslationsFormBean() );
    }

    // return the requested item
    return this.levelTranslations.get( index );
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public void load( Promotion promotion )
  {
    // Promotion display information
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionStatus = promotion.getPromotionStatus().getCode();

    if ( promotion.isSSIPromotion() )
    {
      createBadges( promotion );

      /* merchandise option move SSI_Phase_2 */
      // createMerchLevels( promotion );
    }
  }

  private void createMerchLevels( Promotion promotion )
  {
    List<Content> localeItems = getCMAssetService().getSupportedLocales( true );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );

    List<PromoMerchCountry> countryList = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promotionId, associationRequestCollection );

    for ( PromoMerchCountry country : countryList )
    {
      String countryName = ContentReaderManager.getText( country.getCountry().getCmAssetCode(), country.getCountry().getNameCmKey() );

      for ( PromoMerchProgramLevel level : country.getLevels() )
      {
        PromotionSSITranslationsFormBean bean = new PromotionSSITranslationsFormBean();
        levelTranslations.add( bean );

        String levelName = CmsResourceBundle.getCmsBundle().getString( level.getCmAssetKey() + ".LEVEL_NAME" );
        bean.setTranslationCMKey( level.getCmAssetKey() );
        bean.setDescription( countryName );
        bean.setTranslationEN( "Level " + level.getOrdinalPosition() + " - " + levelName );

        for ( Content content : localeItems )
        {
          if ( content != null )
          {
            String localeCode = (String)content.getContentDataMap().get( "CODE" );
            Locale locale = CmsUtil.getLocale( localeCode );
            String localeDesc = (String)content.getContentDataMap().get( "DESC" );

            String name = "";
            if ( level.getCmAssetKey() != null )
            {
              name = getCMAssetService().getString( level.getCmAssetKey(), PromoMerchProgramLevel.SPOTLIGHT_LEVEL_NAME_KEY, locale, true );
            }

            PromotionSSITranslationsDetailBean detail = new PromotionSSITranslationsDetailBean();

            if ( !ContentReaderManager.getCurrentLocale().equals( locale ) )
            {
              detail.setLocaleCode( localeCode );
              detail.setLocaleDesc( localeDesc );
              detail.setName( name );
              bean.getDetails().add( detail );
            }
          }
        }

      }
    }

  }

  private void createBadges( Promotion promo )
  {
    if ( promo.getBadge() != null && promo.getBadge().getBadgeRules() != null )
    {
      List<Content> localeItems = getCMAssetService().getSupportedLocales( true );
      for ( BadgeRule rule : promo.getBadge().getBadgeRules() )
      {
        PromotionSSITranslationsFormBean bean = new PromotionSSITranslationsFormBean();
        bean.setTranslationEN( rule.getBadgeNameTextFromCM() );
        bean.setTranslationCMKey( rule.getBadgeName() );

        translations.add( bean );

        for ( Content content : localeItems )
        {
          if ( content != null )
          {
            String localeCode = (String)content.getContentDataMap().get( "CODE" );
            Locale locale = CmsUtil.getLocale( localeCode );
            String localeDesc = (String)content.getContentDataMap().get( "DESC" );

            String badgeName = "";
            if ( rule.getBadgeLibraryCMKey() != null )
            {
              badgeName = getCMAssetService().getString( rule.getBadgeName(), BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY, locale, true );
            }

            PromotionSSITranslationsDetailBean detail = new PromotionSSITranslationsDetailBean();

            if ( !ContentReaderManager.getCurrentLocale().equals( locale ) )
            {
              detail.setLocaleCode( localeCode );
              detail.setLocaleDesc( localeDesc );
              detail.setName( badgeName );
              bean.getDetails().add( detail );
            }
          }
        }

      }
    }
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
  }

  protected static PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)BeanLocator.getBean( PromoMerchCountryService.BEAN_NAME );
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion( String version )
  {
    this.version = version;
  }

}

/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.value.PromotionBasicsBadgeFormBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * PromotionSSIAwardsForm.
 * 
 * @author chowdhur
 * @since Oct 22, 2014
 */
public class PromotionSSIAwardsForm extends BaseActionForm
{
  private static final String CM_BADGE_NAME_HTML_KEY = "HTML_KEY";

  private String awardsType;

  // DIY Contest
  private boolean allowAwardPoints;
  // private boolean allowAwardMerchandise; // SSI_Phase_2
  private boolean allowAwardOther;

  private List activeCountryList = new ArrayList();

  private Long badgeId;
  private List<PromotionBasicsBadgeFormBean> promotionSSIBadgeFormBeanList = new ArrayList<PromotionBasicsBadgeFormBean>();

  private String method;

  private String returnActionUrl;

  private Long promotionId;

  private String promotionName;

  private String promotionTypeCode;

  private String promotionTypeName;

  private String promotionStatus;

  private List countryList = null;

  private boolean taxable;

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
    activeCountryList = getEmptyPromoMerchCountryValueList( RequestUtils.getOptionalParamInt( request, "activeCountryListCount" ) );
    countryList = null;
    countryList = getEmptyPromoMerchCountryBeanList( RequestUtils.getOptionalParamInt( request, "countryListCount" ) );
  }

  /**
   * resets the value list with empty PromotionApprovalParticipantBeans
   * 
   * @param listCount
   * @return List
   */
  private List getEmptyPromoMerchCountryBeanList( int listCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < listCount; i++ )
    {
      PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = new PromoMerchProgramLevelFormBean();
      valueList.add( promoMerchProgramLevelFormBean );
    }

    return valueList;
  }

  private List getEmptyPromoMerchCountryValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      PromoMerchCountryFormBean promoMerchCountryFormBean = new PromoMerchCountryFormBean();
      valueList.add( promoMerchCountryFormBean );
    }

    return valueList;
  }

  /**
   * Accessor for the number of PromoMerchCountry objects in the list.
   * 
   * @return int
   */
  public int getCountryListCount()
  {
    if ( countryList == null )
    {
      return 0;
    }

    return countryList.size();
  }

  /**
   * Load the form with the domain object value;
   * 
   * @param promotion
   */
  public void load( Promotion promotion )
  {
    // Promotion display information
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.taxable = promotion.isTaxable();

    if ( promotion.isSSIPromotion() )
    {
      SSIPromotion ssiPromotion = (SSIPromotion)promotion;

      // award types
      this.allowAwardPoints = ssiPromotion.getAllowAwardPoints();
      // this.allowAwardMerchandise = ssiPromotion.getAllowAwardMerchandise();
      this.allowAwardOther = ssiPromotion.getAllowAwardOther();

      // promo merch country
      // SSI_Phase_2
      // loadSSIPromoMerchCountryLists( promotion.getId());

      // badge
      if ( ssiPromotion.getBadge() != null )
      {
        this.badgeId = ssiPromotion.getBadge().getId();
        if ( ssiPromotion.getBadge().getBadgeRules() != null && !ssiPromotion.getBadge().getBadgeRules().isEmpty() )
        {
          List<BadgeLibrary> badgeList = getPromotionService().buildBadgeLibraryList();
          List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>( ssiPromotion.getBadge().getBadgeRules() );
          for ( BadgeLibrary badge : badgeList )
          {
            boolean found = false;
            for ( BadgeRule badgeRule : badgeRulesList )
            {
              if ( badgeRule.getBadgeLibraryCMKey().equals( badge.getBadgeLibraryId() ) )
              {
                PromotionBasicsBadgeFormBean badgeFormBean = new PromotionBasicsBadgeFormBean();
                badgeFormBean.setBadgeRuleId( badgeRule.getId() );
                badgeFormBean.setBadgeId( badgeRule.getBadgePromotion().getId() );
                badgeFormBean.setCmAssetKey( badgeRule.getBadgeLibraryCMKey() );
                String desc = CmsResourceBundle.getCmsBundle().getString( badgeRule.getBadgeName().trim(), CM_BADGE_NAME_HTML_KEY );
                badgeFormBean.setBadgeName( desc );
                badgeFormBean.setSelected( true );
                if ( ssiPromotion.isLive() )
                {
                  badgeFormBean.setDisable( "true" );
                }
                else
                {
                  badgeFormBean.setDisable( "false" );
                }
                this.promotionSSIBadgeFormBeanList.add( badgeFormBean );
                found = true;
                break;
              }
            }
            if ( !found )
            {
              PromotionBasicsBadgeFormBean badgeFormBean = new PromotionBasicsBadgeFormBean();
              badgeFormBean.setDisable( "false" );
              this.promotionSSIBadgeFormBeanList.add( badgeFormBean );
            }
          }
        }
      }
    }

  }

  public void loadCountryList( List domainCountryList )
  {
    if ( domainCountryList == null )
    {
      return;
    }
    countryList = new ArrayList();
    for ( Iterator countryIter = domainCountryList.iterator(); countryIter.hasNext(); )
    {
      PromoMerchCountry promoMerchCountry = (PromoMerchCountry)countryIter.next();
      PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = new PromoMerchProgramLevelFormBean();
      promoMerchProgramLevelFormBean.setNewCountry( true );
      if ( promoMerchCountry != null && promoMerchCountry.getLevels() != null )
      {

        for ( Iterator levelIter = promoMerchCountry.getLevels().iterator(); levelIter.hasNext(); )
        {
          PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelIter.next();
          promoMerchProgramLevelFormBean.setCountryId( promoMerchCountry.getCountry().getId() );
          promoMerchProgramLevelFormBean.setPromoMerchCountryId( promoMerchCountry.getId() );
          promoMerchProgramLevelFormBean.setOrdinalPosition( promoMerchProgramLevel.getOrdinalPosition() );
          promoMerchProgramLevelFormBean.setPromoMerchLevelId( promoMerchProgramLevel.getId() );
          promoMerchProgramLevelFormBean.setOmLevelName( promoMerchProgramLevel.getLevelName() );
          promoMerchProgramLevelFormBean.setMaxValue( promoMerchProgramLevel.getMaxValue() );
          promoMerchProgramLevelFormBean.setMinValue( promoMerchProgramLevel.getMinValue() );
          promoMerchProgramLevelFormBean.setCountryAssetKey( promoMerchCountry.getCountry().getCmAssetCode() );
          promoMerchProgramLevelFormBean.setProgramId( promoMerchCountry.getProgramId() );
          if ( promoMerchProgramLevel.getCmAssetKey() != null && promoMerchProgramLevel.getCmAssetKey().length() > 0 )
          {
            promoMerchProgramLevelFormBean.setLevelName( CmsResourceBundle.getCmsBundle().getString( promoMerchProgramLevel.getCmAssetKey() + ".LEVEL_NAME" ) );
          }
          else
          {
            promoMerchProgramLevelFormBean.setLevelName( "" );
          }
          countryList.add( promoMerchProgramLevelFormBean );
          promoMerchProgramLevelFormBean = new PromoMerchProgramLevelFormBean();
        }
      }
    }
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return RecognitionPromotion
   */
  public SSIPromotion toDomainObject( Promotion promotion )
  {

    SSIPromotion ssiPromotion = (SSIPromotion)promotion;

    ssiPromotion.setAllowAwardPoints( allowAwardPoints );
    // ssiPromotion.setAllowAwardMerchandise( allowAwardMerchandise );
    ssiPromotion.setAllowAwardOther( allowAwardOther );
    ssiPromotion.setTaxable( taxable );

    // Disable bill codes if points is not enabled. Delete bill codes if there are any selected
    // previously.
    if ( !allowAwardPoints )
    {
      ssiPromotion.setBillCodesActive( false );
      if ( !CollectionUtils.isEmpty( ssiPromotion.getPromotionBillCodes() ) )
      {
        ssiPromotion.getPromotionBillCodes().clear();
      }
    }

    return ssiPromotion;
  }

  public void buildSSIDomainPromoMerchCountrySet( Promotion promotion )
  {
    Set promoMerchCountrySet = new LinkedHashSet();

    for ( Iterator requiredCountryIter = activeCountryList.iterator(); requiredCountryIter.hasNext(); )
    {
      PromoMerchCountryFormBean promoMerchCountryFormBean = (PromoMerchCountryFormBean)requiredCountryIter.next();
      if ( promoMerchCountryFormBean.getProgramId() != null && promoMerchCountryFormBean.getProgramId().length() > 0 )
      {
        promoMerchCountrySet.add( toPromoMerchCountryDomainObject( promoMerchCountryFormBean, promotion ) );
      }
    }

    promotion.setPromoMerchCountries( promoMerchCountrySet );

  }

  private PromoMerchCountry toPromoMerchCountryDomainObject( PromoMerchCountryFormBean promoMerchCountryFormBean, Promotion promotion )
  {
    PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
    promoMerchCountry.setPromotion( promotion );
    Country country = getCountryService().getCountryById( promoMerchCountryFormBean.getCountryId() );
    promoMerchCountry.setCountry( country );
    if ( promoMerchCountryFormBean.getPromoMerchCountryId() != null )
    {
      promoMerchCountry.setId( promoMerchCountryFormBean.getPromoMerchCountryId() );
    }
    promoMerchCountry.setProgramId( promoMerchCountryFormBean.getProgramId() );
    return promoMerchCountry;

  }

  public List toPromoMerchProgramLevelDomainObject( List domainCountryList )
  {

    if ( countryList != null )
    {
      int ordinalPositionCounter = 0;
      for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
      {
        ordinalPositionCounter++;
        PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = (PromoMerchProgramLevelFormBean)countryIter.next();
        PromoMerchProgramLevel promoMerchProgramLevel = new PromoMerchProgramLevel();
        promoMerchProgramLevel.setLevelName( promoMerchProgramLevelFormBean.getOmLevelName() );
        // Iterate to domain country list
        for ( Iterator domainCountryIter = domainCountryList.iterator(); domainCountryIter.hasNext(); )
        {
          PromoMerchCountry promoMerchCountry = (PromoMerchCountry)domainCountryIter.next();
          if ( promoMerchCountry.getId().longValue() == promoMerchProgramLevelFormBean.getPromoMerchCountryId().longValue() )
          {
            boolean found = false;
            Collection levels = promoMerchCountry.getLevels();
            if ( levels != null )
            {
              // Iterate each level in domain Country
              for ( Iterator levelIter = levels.iterator(); levelIter.hasNext(); )
              {
                PromoMerchProgramLevel promoMerchProgramLevelLoop = (PromoMerchProgramLevel)levelIter.next();
                if ( promoMerchProgramLevelLoop != null && promoMerchProgramLevelLoop.getLevelName().equals( promoMerchProgramLevel.getLevelName() ) )
                {
                  promoMerchProgramLevelLoop.setDisplayLevelName( promoMerchProgramLevelFormBean.getLevelName() );
                  found = true;
                  break;
                }
              }
            }
            // If level is not found in this country, then add it to
            // the list..
            if ( !found )
            {
              promoMerchProgramLevel.setDisplayLevelName( promoMerchProgramLevelFormBean.getLevelName() );
              promoMerchProgramLevel.setOrdinalPosition( ordinalPositionCounter );
              promoMerchProgramLevel.setLevelName( promoMerchProgramLevelFormBean.getOmLevelName() );
              promoMerchProgramLevel.setProgramId( promoMerchProgramLevelFormBean.getProgramId() );
              promoMerchProgramLevel.setPromoMerchCountry( promoMerchCountry );
              if ( promoMerchCountry.getLevels() == null )
              {
                promoMerchCountry.setLevels( new LinkedHashSet() );
              }
              promoMerchCountry.getLevels().add( promoMerchProgramLevel );
            }
          } // If country id match
        }
      }
    }
    return domainCountryList;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param mapping the mapping used to select this instance.
   * @param request the servlet request we are processing.
   * @return <code>ActionErrors</code> object that encapsulates any validation errors.
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {

    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( promotionTypeCode.equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      // if method type save or lookup
      SSIPromotion ssiPromotion = null;
      if ( promotionId != null )
      {
        ssiPromotion = (SSIPromotion)getPromotionService().getPromotionById( promotionId );
      }
      if ( null != ssiPromotion && ssiPromotion.isLive() )// skip/validation/live/promotion
      {
        allowAwardPoints = ssiPromotion.getAllowAwardPoints();
        allowAwardOther = ssiPromotion.getAllowAwardOther();
      }
      else
      {
        if ( !allowAwardPoints && !allowAwardOther )
        {
          errors.add( "awardTypes", new ActionMessage( "promotion.ssi.awards.AWARD_TYPE_ERROR" ) );
          return errors;
        }
      }
      // Add badge validation
      // validateMerchendise( errors ); //SSI_Phase_2

      // if method type save
      if ( "save".equals( method ) )
      {
        if ( this.promotionSSIBadgeFormBeanList != null )
        {
          // add validation
          if ( ssiPromotion != null && ssiPromotion.getBadge() != null )
          {
            for ( PromotionBasicsBadgeFormBean promotionBasicsBadgeFormBean : promotionSSIBadgeFormBeanList )
            {
              if ( promotionBasicsBadgeFormBean.isSelected() && StringUtils.isEmpty( promotionBasicsBadgeFormBean.getBadgeName() ) )
              {
                errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.ssi.awards.BADGE_NAME_REQUIRED" ) );
                break;
              }
              Set<BadgeRule> badgeRules = ssiPromotion.getBadge().getBadgeRules();
              if ( badgeRules != null && !badgeRules.isEmpty() )
              {
                List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>( badgeRules );

                for ( BadgeRule badgeRule : badgeRulesList )
                {
                  if ( badgeRule.getId().equals( promotionBasicsBadgeFormBean.getBadgeRuleId() ) )
                  {
                    int badgeCountInSsiContest = getSSIContestService().getBadgeCountInSsiContest( ssiPromotion.getId(), badgeRule.getId() );
                    if ( badgeCountInSsiContest > 0 && !promotionBasicsBadgeFormBean.isSelected() )
                    {
                      // add error
                      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.ssi.awards.BADGE_USED_IN_CONTEST", badgeRule.getBadgeNameTextFromCM(), badgeCountInSsiContest ) );
                      break;
                    }
                    break;
                  }
                }
              }
            }
          }
          else
          {
            for ( PromotionBasicsBadgeFormBean promotionBasicsBadgeFormBean : promotionSSIBadgeFormBeanList )
            {
              if ( promotionBasicsBadgeFormBean.isSelected() && StringUtils.isEmpty( promotionBasicsBadgeFormBean.getBadgeName() ) )
              {
                errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.ssi.awards.BADGE_NAME_REQUIRED" ) );
                break;
              }
            }
          }
        }
      }
    }

    return errors;
  }

  public void validateMerchendise( ActionErrors errors )
  {
    // if method type lookup
    if ( "saveAndPopulateMerchLevels".equals( method ) )
    {
      // if ( allowAwardMerchandise )
      {
        if ( countryList != null )
        {
          for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
          {
            PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = (PromoMerchProgramLevelFormBean)countryIter.next();
            if ( StringUtils.isEmpty( promoMerchProgramLevelFormBean.getProgramId() ) )
            {
              errors.add( "programId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.awards.PROGRAM_ID" ) ) );
              break;
            }
          }
        }
      }
    }

    // if method type save
    if ( "save".equals( method ) )
    {
      // if ( allowAwardMerchandise )
      {
        if ( countryList != null )
        {
          for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
          {
            PromoMerchProgramLevelFormBean promoMerchProgramLevelFormBean = (PromoMerchProgramLevelFormBean)countryIter.next();
            if ( StringUtils.isNotEmpty( promoMerchProgramLevelFormBean.getProgramId() )
                && ( promoMerchProgramLevelFormBean.getLevelName() == null || promoMerchProgramLevelFormBean.getLevelName().trim().length() <= 0 ) )
            {
              errors.add( "levelNames", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.basics.AWARD_LEVEL_NAMES" ) ) );
              break;
            }
          }
        }
      }
    }
  }

  public void loadSSIPromoMerchCountryLists( Long promotionId )
  {
    activeCountryList = toPromoMerchCountryFormBean( getPromoMerchCountryService().getSSIAwardPromoMerchCountry( promotionId ) );
  }

  private List toPromoMerchCountryFormBean( List promoMerchCountryList )
  {

    List promoMerchCountryFormBeanList = new ArrayList();

    if ( promoMerchCountryList != null )
    {
      for ( Iterator promoMerchCountryIter = promoMerchCountryList.iterator(); promoMerchCountryIter.hasNext(); )
      {
        PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIter.next();
        PromoMerchCountryFormBean pmcfb = new PromoMerchCountryFormBean();

        if ( promoMerchCountry.getId() != null )
        {
          pmcfb.setPromoMerchCountryId( promoMerchCountry.getId() );
        }
        pmcfb.setCountryId( promoMerchCountry.getCountry().getId() );
        pmcfb.setCountryName( promoMerchCountry.getCountry().getCmAssetCode() );
        pmcfb.setProgramId( promoMerchCountry.getProgramId() );
        promoMerchCountryFormBeanList.add( pmcfb );

      }
    }

    return promoMerchCountryFormBeanList;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getAwardsType()
  {
    return awardsType;
  }

  public void setAwardsType( String type )
  {
    this.awardsType = type;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public List getCountryList()
  {
    return countryList;
  }

  /**
   * @param countryList the countryList to set
   */
  public void setCountryList( List countryList )
  {
    this.countryList = countryList;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  public List<PromotionBasicsBadgeFormBean> getPromotionSSIBadgeFormBeanList()
  {
    return promotionSSIBadgeFormBeanList;
  }

  public void setPromotionSSIBadgeFormBeanList( List<PromotionBasicsBadgeFormBean> promotionSSIBadgeFormBeanList )
  {
    this.promotionSSIBadgeFormBeanList = promotionSSIBadgeFormBeanList;
  }

  public PromotionBasicsBadgeFormBean getPromotionSSIBadgeFormBean( int index )
  {
    if ( this.promotionSSIBadgeFormBeanList == null )
    {
      this.promotionSSIBadgeFormBeanList = new ArrayList<PromotionBasicsBadgeFormBean>();
    }

    // indexes to not come in order, populate empty spots
    while ( index >= this.promotionSSIBadgeFormBeanList.size() )
    {
      this.promotionSSIBadgeFormBeanList.add( new PromotionBasicsBadgeFormBean() );
    }

    // return the requested item
    return promotionSSIBadgeFormBeanList.get( index );
  }

  public boolean isAllowAwardPoints()
  {
    return allowAwardPoints;
  }

  public void setAllowAwardPoints( boolean allowAwardPoints )
  {
    this.allowAwardPoints = allowAwardPoints;
  }

  /*
   * public boolean isAllowAwardMerchandise() { return allowAwardMerchandise; } public void
   * setAllowAwardMerchandise( boolean allowAwardMerchandise ) { this.allowAwardMerchandise =
   * allowAwardMerchandise; }
   */

  public boolean isAllowAwardOther()
  {
    return allowAwardOther;
  }

  public void setAllowAwardOther( boolean allowAwardOther )
  {
    this.allowAwardOther = allowAwardOther;
  }

  public List getActiveCountryList()
  {
    return activeCountryList;
  }

  public void setActiveCountryList( List activeCountryList )
  {
    this.activeCountryList = activeCountryList;
  }

  public int getActiveCountryListCount()
  {
    if ( activeCountryList == null )
    {
      return 0;
    }

    return activeCountryList.size();
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)ServiceLocator.getService( PromotionService.BEAN_NAME );
  }

  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)ServiceLocator.getService( PromoMerchCountryService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)ServiceLocator.getService( CountryService.BEAN_NAME );
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)ServiceLocator.getService( SSIContestService.BEAN_NAME );
  }

  public boolean isTaxable()
  {
    return taxable;
  }

  public void setTaxable( boolean taxable )
  {
    this.taxable = taxable;
  }

}

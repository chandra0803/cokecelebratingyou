/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/impl/PromoMerchCountryServiceImpl.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.dao.participant.ListBuilderDAO;
import com.biperf.core.dao.promotion.PromoMerchCountryDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.value.FormattedValueBean;
import com.objectpartners.cms.domain.enums.DataTypeEnum;

/**
 * PromoMerchCountryServiceImpl.
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
 * <td>ernste</td>
 * <td>July 19, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromoMerchCountryServiceImpl implements PromoMerchCountryService
{
  private static final String SPOTLIGHT_LEVEL_DATA_ASSET_PREFIX = "spotlight_levels_data.";
  private static final String SPOTLIGHT_LEVEL_NAME_KEY = "LEVEL_NAME";
  private static final String SPOTLIGHT_LEVEL_DATA_SECTION_CODE = "spotlight_levels_data";
  private static final String SPOTLIGHT_LEVEL_ASSET_TYPE_NAME = "_SPOTLIGHT_LEVELS_DATA";
  private PromoMerchCountryDAO promoMerchCountryDAO = null;
  private CountryDAO countryDAO = null;
  private PromotionDAO promotionDAO = null;
  private ListBuilderDAO listBuilderDAO = null;
  private HierarchyDAO hierarchyDAO = null;
  private CMAssetService cmAssetService = null;
  private ParticipantService participantService = null;
  private MerchLevelService merchLevelService = null;

  /**
   * Get the promotion merchandise country by id.  
   * 
   * @param id
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryById( Long promoMerchCountryId )
  {
    return promoMerchCountryDAO.getPromoMerchCountryById( promoMerchCountryId );
  }

  /**
   * 
   * @param id
   * @param associationRequestCollection
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryByIdWithAssociations( Long promoMerchCountryId, AssociationRequestCollection associationRequestCollection )
  {
    return promoMerchCountryDAO.getPromoMerchCountryByIdWithAssociations( promoMerchCountryId, associationRequestCollection );
  }

  /**
   * Get the list of promo merch countries for the given promotion
   * 
   * @param promotionId
   * @return List
   */
  public List getPromoMerchCountriesByPromotionId( Long promotionId )
  {
    return getPromoMerchCountriesByPromotionId( promotionId, null );
  }

  /**
   * Get the list of promo merch countries for the given promotion
   * 
   * @param promotionId
   * @param associationRequestCollection
   * @return List
   */
  public List getPromoMerchCountriesByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    return promoMerchCountryDAO.getPromoMerchCountriesByPromotionId( promotionId, associationRequestCollection );
  }

  /**
   * 
   * Overridden from @see com.biperf.core.service.promotion.PromoMerchCountryService#getActiveCountryByPromoRecAudience(java.lang.Long)
   * 
   * There are 2 ways this function will get called:
   *    1.  When we don't know the last 4 parameters, primary and secondary audiences for a promotion - so 
   *                        we need to get those values from the DB for a promotion
   *    2.  From the Screen, we'll have temporary variables for the last 4 parameters - they are not saved
   *                        with the promotion yet, that's why we have to pass them in.
   *                        
   * @param promotionId
   * @return List of active countries based on the promotion receivers audience
   */
  public List getActiveCountriesInPromoRecAudience( Long promotionId,
                                                    PrimaryAudienceType primaryAudienceType,
                                                    SecondaryAudienceType secondaryAudienceType,
                                                    Set primaryAudiences,
                                                    Set secondaryAudiences )
  {
    // first get list of currently entered promo merch countries
    List currentPromoMerchCountries = this.getPromoMerchCountriesByPromotionId( promotionId );

    // finally get the list of countries used within the promotion receiver's audience.
    Promotion promotion = getPromotionWithAudiences( promotionId );

    // These values are passed in from Ajax - they have not been saved yet on the promotion. If they
    // are null,
    // we are not coming from Ajax and just get the promotion's primary and secondary types.
    if ( primaryAudienceType == null )
    {
      primaryAudienceType = promotion.getPrimaryAudienceType();
    }
    if ( secondaryAudienceType == null )
    {
      secondaryAudienceType = promotion.getSecondaryAudienceType();
    }

    List promoMerchCountries = new ArrayList();
    if ( promotion != null && secondaryAudienceType != null && primaryAudienceType != null )
    {
      List activeCountriesInPromoRecAud = new ArrayList();

      // conditions for getting all active countries for all active participants
      if ( isAllActiveParticipants( primaryAudienceType, secondaryAudienceType ) )
      {
        activeCountriesInPromoRecAud = countryDAO.getActiveCountriesForAllActivePax();
      }
      // conditions for getting specified audience selections - either primary or secondary uses
      // specified audiences
      else if ( isSpecifiedAudience( primaryAudienceType, secondaryAudienceType ) )
      {
        // get promotion audiences either specified in the primary or secondary audience (giver or
        // receiver audience)
        Set audiences = new HashSet();
        if ( secondaryAudienceType.isSpecifyAudienceType() )
        {
          if ( secondaryAudiences == null )
          {
            audiences.addAll( promotion.getPromotionSecondaryAudiences() );
          }
          else
          {
            audiences.addAll( secondaryAudiences );
          }
        }
        else
        {
          if ( primaryAudienceType.isSpecifyAudienceType() )
          {
            if ( primaryAudiences == null )
            {
              audiences.addAll( promotion.getPromotionPrimaryAudiences() );
            }
            else
            {
              audiences.addAll( primaryAudiences );
            }
          }
        }

        // for each audience, get the active countries for the participants and add them to the list
        // of active countries
        Set criteriaAudiences = new LinkedHashSet();
        for ( Iterator audienceIter = audiences.iterator(); audienceIter.hasNext(); )
        {
          Object obj = audienceIter.next();
          if ( obj instanceof Audience )
          {
            Audience audience = (Audience)obj;
            // if the audience is of type pax, we will run a query direct for performance purposes
            if ( audience instanceof PaxAudience || audience instanceof CriteriaAudience )
            {
              List paxAudienceActiveCountries = countryDAO.getActiveCountriesForPaxBasedAudience( audience.getId() );
              activeCountriesInPromoRecAud = addCountriesFromList( paxAudienceActiveCountries, activeCountriesInPromoRecAud );
            }
          }
        }

      }
      // Create promoMerchCountries objects from the list of current promoMerchCountries and
      // possibly new countries to be added.
      promoMerchCountries = toPromoMerchCountryDomainObjects( promotion, currentPromoMerchCountries, activeCountriesInPromoRecAud );

    }

    return promoMerchCountries;
  }

  private List getCountriesFromPaxList( List userIdList )
  {
    Set countries = new LinkedHashSet();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    for ( Iterator userIdListIterator = userIdList.iterator(); userIdListIterator.hasNext(); )
    {
      FormattedValueBean fvb = (FormattedValueBean)userIdListIterator.next();
      Participant pax = participantService.getParticipantByIdWithAssociations( fvb.getId(), associationRequestCollection );
      Set userAddresses = pax.getUserAddresses();
      for ( Iterator userAddressIter = userAddresses.iterator(); userAddressIter.hasNext(); )
      {
        UserAddress userAddress = (UserAddress)userAddressIter.next();
        if ( userAddress.isPrimary() )
        {
          Address address = userAddress.getAddress();
          countries.add( address.getCountry() );
        }
      }
    }
    List criteriaAudienceActiveCountries = new ArrayList();
    for ( Iterator countryIter = countries.iterator(); countryIter.hasNext(); )
    {
      criteriaAudienceActiveCountries.add( (Country)countryIter.next() );
    }
    return criteriaAudienceActiveCountries;

  }

  /**
   * 
   * Overridden from @see com.biperf.core.service.promotion.PromoMerchCountryService#getActiveCountriesNotInPromoRecAudience(java.lang.Long)
   * @param promotionId
   * @return
   */
  public List getActiveCountriesNotInPromoRecAudience( Long promotionId,
                                                       PrimaryAudienceType primaryAudienceType,
                                                       SecondaryAudienceType secondaryAudienceType,
                                                       Set primaryAudiences,
                                                       Set secondaryAudiences )
  {
    // first get list of promo Merch countries, then subtract out list
    List promoMerchCountriesInRecAudience = getActiveCountriesInPromoRecAudience( promotionId, primaryAudienceType, secondaryAudienceType, primaryAudiences, secondaryAudiences );
    Promotion promotion = getPromotionWithAudiences( promotionId );

    return getActiveCountriesNotInPromoRecAudience( promotion, promoMerchCountriesInRecAudience );

  }

  /**
   * Performance enhancement - Call this function if we need to get both required promoMerchCountries and non-required.  
   *            Pass in the promotion and result from getActiveCountriesInPromoRecAudience.   
   * 
   * Overridden from @see com.biperf.core.service.promotion.PromoMerchCountryService#getActiveCountriesNotInPromoRecAudience(com.biperf.core.domain.promotion.RecognitionPromotion, java.util.List)
   * @param promotion
   * @param promoMerchCountriesInRecAudience
   * @return
   */
  public List getActiveCountriesNotInPromoRecAudience( Promotion promotion, List promoMerchCountriesInRecAudience )
  {
    List allActiveCountries = countryDAO.getAllActive();

    List activeCountriesNotInPromoRecAudience = new ArrayList();

    // First get the list of all the countries not in the promotion audience and create a
    // promoMerchCountry object for them
    for ( Iterator allCountryIter = allActiveCountries.iterator(); allCountryIter.hasNext(); )
    {
      Country country = (Country)allCountryIter.next();
      boolean isFound = false;
      for ( Iterator recAudCountryIter = promoMerchCountriesInRecAudience.iterator(); recAudCountryIter.hasNext(); )
      {
        PromoMerchCountry promoMerchCountry = (PromoMerchCountry)recAudCountryIter.next();
        Country recAudCountry = promoMerchCountry.getCountry();
        if ( country.equals( recAudCountry ) )
        {
          isFound = true;
          break;
        }
      }
      if ( !isFound )
      {
        PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
        promoMerchCountry.setPromotion( promotion );
        promoMerchCountry.setCountry( country );
        activeCountriesNotInPromoRecAudience.add( promoMerchCountry );
      }
    }

    // Now compare the list of countries not in the promotion audience to what exists in the DB and
    // populate the programID field
    List currentPromoMerchCountries = this.getPromoMerchCountriesByPromotionId( promotion.getId() );
    for ( Iterator activeNotPromoAudIter = activeCountriesNotInPromoRecAudience.iterator(); activeNotPromoAudIter.hasNext(); )
    {
      PromoMerchCountry activeNotPromoAudCountry = (PromoMerchCountry)activeNotPromoAudIter.next();
      for ( Iterator currPromMerIter = currentPromoMerchCountries.iterator(); currPromMerIter.hasNext(); )
      {
        PromoMerchCountry currPromMer = (PromoMerchCountry)currPromMerIter.next();
        if ( activeNotPromoAudCountry.equals( currPromMer ) )
        {
          activeNotPromoAudCountry.setProgramId( currPromMer.getProgramId() );
          break;
        }
      }
    }

    return activeCountriesNotInPromoRecAudience;
  }

  /*
   * 
   */
  private Promotion getPromotionWithAudiences( Long promotionId )
  {
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );

    return promotionDAO.getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
  }

  /*
   * 
   */
  private boolean isAllActiveParticipants( PrimaryAudienceType primaryAudienceType, SecondaryAudienceType secondaryAudienceType )
  {
    if ( secondaryAudienceType.equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
        || secondaryAudienceType.equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
            && primaryAudienceType.equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
        || secondaryAudienceType.equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) )
            && primaryAudienceType.equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
        || secondaryAudienceType.equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) )
            && primaryAudienceType.equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      return true;
    }
    return false;
  }

  private boolean isSpecifiedAudience( PrimaryAudienceType primaryAudienceType, SecondaryAudienceType secondaryAudienceType )
  {
    if ( secondaryAudienceType.equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
        && primaryAudienceType.equals( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) )
        // start check - to actually check node and node and below, remove these 2 lines. For now,
        // we're not checking these.
        || secondaryAudienceType.equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) )
            && primaryAudienceType.equals( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) )
        || secondaryAudienceType.equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) )
            && primaryAudienceType.equals( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) )
        // end check
        || secondaryAudienceType.equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) ) )
    {
      return true;
    }
    return false;
  }

  /**
   * 
   * @param newCountryList
   * @param baseCountryList
   * @return
   */
  private List addCountriesFromList( List newCountryList, List baseCountryList )
  {
    if ( baseCountryList == null )
    {
      if ( newCountryList != null )
      {
        baseCountryList = new ArrayList();
        baseCountryList.addAll( newCountryList );
      }
    }
    else
    {
      if ( newCountryList != null )
      {
        for ( Iterator newCountryIter = newCountryList.iterator(); newCountryIter.hasNext(); )
        {
          boolean isFound = false;
          Country newCountry = (Country)newCountryIter.next();
          for ( Iterator baseCountryIter = baseCountryList.iterator(); baseCountryIter.hasNext(); )
          {
            Country baseCountry = (Country)baseCountryIter.next();
            if ( baseCountry.equals( newCountry ) )
            {
              isFound = true;
              break;
            }
          }
          if ( !isFound )
          {
            baseCountryList.add( newCountry );
          }
        }
      }
    }
    return baseCountryList;
  }

  private List toPromoMerchCountryDomainObjects( Promotion promotion, List currentPromoMerchCountries, List newCountryList )
  {
    List promoMerchCountries = new ArrayList();
    if ( newCountryList != null )
    {
      for ( Iterator countryIter = newCountryList.iterator(); countryIter.hasNext(); )
      {
        Country newCountry = (Country)countryIter.next();
        boolean isFound = false;
        if ( currentPromoMerchCountries != null )
        {
          for ( Iterator promoMerchCountriesIter = currentPromoMerchCountries.iterator(); promoMerchCountriesIter.hasNext(); )
          {
            PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountriesIter.next();
            if ( newCountry.equals( promoMerchCountry.getCountry() ) )
            {
              // Add the current promoMerchCountry
              promoMerchCountries.add( promoMerchCountry );
              isFound = true;
              break;
            }
          }
        }
        if ( !isFound )
        {
          // New country found, create a new promoMerchCountry
          PromoMerchCountry promoMerchCountry = new PromoMerchCountry();
          promoMerchCountry.setPromotion( promotion );
          promoMerchCountry.setCountry( newCountry );
          promoMerchCountries.add( promoMerchCountry );
        }
      }
    }
    else
    {
      // TODO: This would mean there are no countries, should we delete the current
      // promoMerchCountries??
      if ( currentPromoMerchCountries != null )
      {
        promoMerchCountries.addAll( currentPromoMerchCountries );
      }
    }
    return promoMerchCountries;
  }

  /**
   * 
   * @param promoMerchCountry
   * @return
   */
  public PromoMerchCountry savePromoMerchCountry( PromoMerchCountry promoMerchCountry ) throws ServiceErrorException
  {
    if ( promoMerchCountry.getLevels() != null )
    {
      for ( Iterator levelsIter = promoMerchCountry.getLevels().iterator(); levelsIter.hasNext(); )
      {
        PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelsIter.next();
        if ( promoMerchProgramLevel.getCmAssetKey() == null || promoMerchProgramLevel.getCmAssetKey().trim().length() == 0 )
        {
          promoMerchProgramLevel.setCmAssetKey( cmAssetService.getUniqueAssetCode( SPOTLIGHT_LEVEL_DATA_ASSET_PREFIX ) );
          // promoMerchProgramLevel.setCmAssetKey( SPOTLIGHT_LEVEL_NAME_KEY );
        }
        // save asset for budget name
        CMDataElement cmDataElement = new CMDataElement( "Promo Merch Program Level Name", SPOTLIGHT_LEVEL_NAME_KEY, promoMerchProgramLevel.getDisplayLevelName(), false );

        cmAssetService.createOrUpdateAsset( SPOTLIGHT_LEVEL_DATA_SECTION_CODE,
                                            SPOTLIGHT_LEVEL_ASSET_TYPE_NAME,
                                            promoMerchProgramLevel.getDisplayLevelName() + " value",
                                            promoMerchProgramLevel.getCmAssetKey(),
                                            cmDataElement );

      }

    }

    return promoMerchCountryDAO.savePromoMerchCountry( promoMerchCountry );
  }

  /**
   * delete all entries in promomerchprogramlevel that are mapped to this promo merch country
   * @param promoMerchCountry
   */
  public void deleteProgramLevels( PromoMerchCountry promoMerchCountry )
  {
    if ( promoMerchCountry.getLevels() != null )
    {
      for ( Iterator levelsIter = promoMerchCountry.getLevels().iterator(); levelsIter.hasNext(); )
      {
        PromoMerchProgramLevel programLevel = (PromoMerchProgramLevel)levelsIter.next();
        levelsIter.remove();
        promoMerchCountryDAO.deleteProgramLevel( programLevel );
      }
    }
    // promoMerchCountry.setLevels(new ArrayList());
  }

  /**
   * Validates all basic promotion information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  public boolean validateAndDeleteInvalidProgramLevels( Promotion promotion, boolean checkIfLabelAbsent ) throws ServiceErrorException
  {
    boolean valid = true;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
    List countryList = getPromoMerchCountriesByPromotionId( promotion.getId(), associationRequestCollection );
    for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
    {
      PromoMerchCountry promoMerchCountry = (PromoMerchCountry)countryIter.next();
      String currentProgramId = getProgramIdForCountry( promotion, promoMerchCountry.getCountry() );
      Collection levelList = promoMerchCountry.getLevels();
      if ( levelList != null && levelList.size() > 0 )
      {
        for ( Iterator levelIter = levelList.iterator(); levelIter.hasNext(); )
        {
          PromoMerchProgramLevel programLevel = (PromoMerchProgramLevel)levelIter.next();
          if ( programLevel.getProgramId() == null || !programLevel.getProgramId().equals( currentProgramId ) )
          {
            valid = false;
            deleteProgramLevels( promoMerchCountry );
            break;
          }
        }
      }
    }
    if ( valid && checkIfLabelAbsent )
    {
      for ( Iterator countryIter = countryList.iterator(); countryIter.hasNext(); )
      {
        PromoMerchCountry promoMerchCountry = (PromoMerchCountry)countryIter.next();
        AwardBanqMerchResponseValueObject merchlinqLevelData = merchLevelService.getMerchlinqLevelDataWebService( promoMerchCountry.getProgramId() );
        Collection levelList = promoMerchCountry.getLevels();
        int dbLevelSize = 0;
        if ( levelList != null && levelList.size() > 0 )
        {
          dbLevelSize = levelList.size();
        }
        if ( merchlinqLevelData != null && merchlinqLevelData.getMerchLevel() != null && promoMerchCountry.getLevels() != null )
        {
          if ( merchlinqLevelData.getMerchLevel().size() != dbLevelSize )
          {
            valid = false;
          }
        }
      }
    }

    return valid;
  }

  /**
   * Get the program id for the specified country in the specified promotion
   * @param promotion
   * @param country
   * @return the program id
   */
  private String getProgramIdForCountry( Promotion promotion, Country country )
  {
    for ( Iterator promoMerchCountryIter = promotion.getPromoMerchCountries().iterator(); promoMerchCountryIter.hasNext(); )
    {
      PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIter.next();
      if ( promoMerchCountry.getCountry().equals( country ) )
      {
        return promoMerchCountry.getProgramId();
      }
    }
    return null;
  }

  public List getSSIAwardPromoMerchCountry( Long promotionId )
  {
    // first get list of currently entered promo merch countries
    List currentPromoMerchCountries = this.getPromoMerchCountriesByPromotionId( promotionId );
    Promotion promotion = promotionDAO.getPromotionById( promotionId );

    // US Only
    List ssiActiveCountries = new ArrayList();
    Country country = countryDAO.getCountryByCode( Country.UNITED_STATES );
    ssiActiveCountries.add( country );

    // Create promoMerchCountries objects from the list of current promoMerchCountries and possibly
    // new countries to be added.
    List promoMerchCountries = toPromoMerchCountryDomainObjects( promotion, currentPromoMerchCountries, ssiActiveCountries );

    return promoMerchCountries;
  }

  public String saveLevelCmText( String levelCode, String levelName, Locale locale ) throws ServiceErrorException
  {
    String cmAsset = null;
    try
    {

      CMDataElement cmDataElement = new CMDataElement( PromoMerchProgramLevel.SPOTLIGHT_LEVEL_CMASSET_NAME, PromoMerchProgramLevel.SPOTLIGHT_LEVEL_NAME_KEY, levelName, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( PromoMerchProgramLevel.SPOTLIGHT_LEVEL_SECTION_CODE,
                                          PromoMerchProgramLevel.SPOTLIGHT_LEVEL_CMASSET_TYPE_NAME,
                                          PromoMerchProgramLevel.SPOTLIGHT_LEVEL_CMASSET_NAME,
                                          levelCode,
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return cmAsset;
  }

  public PromoMerchCountryDAO getPromoMerchCountryDAO()
  {
    return promoMerchCountryDAO;
  }

  public void setPromoMerchCountryDAO( PromoMerchCountryDAO promoMerchCountryDAO )
  {
    this.promoMerchCountryDAO = promoMerchCountryDAO;
  }

  public CountryDAO getCountryDAO()
  {
    return countryDAO;
  }

  public void setCountryDAO( CountryDAO countryDAO )
  {
    this.countryDAO = countryDAO;
  }

  public PromotionDAO getPromotionDAO()
  {
    return promotionDAO;
  }

  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  public CMAssetService getCmAssetService()
  {
    return cmAssetService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public ListBuilderDAO getListBuilderDAO()
  {
    return listBuilderDAO;
  }

  public void setListBuilderDAO( ListBuilderDAO listBuilderDAO )
  {
    this.listBuilderDAO = listBuilderDAO;
  }

  public HierarchyDAO getHierarchyDAO()
  {
    return hierarchyDAO;
  }

  public void setHierarchyDAO( HierarchyDAO hierarchyDAO )
  {
    this.hierarchyDAO = hierarchyDAO;
  }

  public MerchLevelService getMerchLevelService()
  {
    return merchLevelService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

}

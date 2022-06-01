
package com.biperf.core.service.participant.impl;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.containsAny;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.ParticipantGroupDAO;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.ParticipantSearchFilterEnum;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.indexing.BIElasticSearchIndexerService;
import com.biperf.core.indexing.BIElasticSearchSearcher;
import com.biperf.core.indexing.BIIndexType;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.underarmour.UnderArmourService;
import com.biperf.core.utils.BICollectionUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.cms.ContentReaderUtils;
import com.biperf.core.value.CountryValueBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;
import com.biperf.core.value.indexing.ESResultWrapper;
import com.biperf.core.value.indexing.IndexSearchResult;
import com.biperf.core.value.indexing.Searchable;
import com.biperf.core.value.participant.PaxCard;
import com.biperf.core.value.participant.PaxCard.PromotionAttributes;
import com.biperf.core.value.participant.PaxCard.PromotionBean;
import com.biperf.core.value.participant.PaxIndexData;
import com.biperf.core.value.participant.PaxIndexSearchCriteria;
import com.objectpartners.cms.util.ContentReaderManager;

/**
  *  AutoCompleteService implementation 
  */
@Service( "autoCompleteService" )
public class AutoCompleteServiceImpl implements AutoCompleteService
{
  private static final Log logger = LogFactory.getLog( AutoCompleteServiceImpl.class );

  private @Autowired SystemVariableService systemVariableService;
  private @Autowired ParticipantService participantService;
  private @Autowired ParticipantDAO participantDAO;
  private @Autowired BIElasticSearchIndexerService esIndexer;
  private @Autowired BIElasticSearchSearcher esSearcher;
  private @Autowired NodeDAO nodeDAO;
  private @Autowired CountryService countryService;
  private @Autowired PromotionService promotionService;
  private @Autowired ParticipantGroupDAO participantGroupDAO;
  private @Autowired UnderArmourService underArmourService;
  private @Autowired NodeService nodeService;
  private @Autowired UserService userService;
  private @Autowired UserCharacteristicService userCharacteristicService;//Client customization

  @Override
  public IndexSearchResult<PaxIndexData> search( PaxIndexSearchCriteria criteria ) throws Exception
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Search Criteria: " + criteria );
    }
    ESResultWrapper wrapper = esSearcher.search( criteria, BIIndexType.PARTICIPANT );
    List<Searchable> searchRecords = wrapper.getSearchRecords();
    return new IndexSearchResult<PaxIndexData>( searchRecords, wrapper.getHits(), wrapper.getExecutionTime() );
  }

  @Override
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  /**
   * {@inheritDoc}
   * Creates All Active PAX Index
   * 
   * @return    Total record count indexed.
   */
  public Long indexAllActiveParticipants() throws Exception
  {
    PropertySetItem propertyByName = systemVariableService.getPropertyByName( SystemVariableService.ELASTICSEARCH_INDEX_BATCH_SIZE );
    int batchSize = propertyByName.getIntVal();

    if ( propertyByName == null || batchSize <= 0 )
    {
      return 0L;
    }

    List allPaxIds = getParticipantService().getAllPaxIds();

    if ( isEmpty( allPaxIds ) )
    {
      return 0L;
    }

    Stream<List<Long>> batches = BICollectionUtils.batches( allPaxIds, batchSize );
    long totalRecordsIndexed = batches.mapToLong( this::indexParticipants ).sum();
    return totalRecordsIndexed;
  }

  @Override
  /** 
   * {@inheritDoc} 
   * @return    Total record count indexed.
   */
  public Long indexParticipants( List<Long> paxIds )
  {
    boolean preparedContentReader = false;
    List<PaxIndexData> paxIndexData = null;

    try
    {
      paxIndexData = participantDAO.getParticipantIndexData( paxIds );

      preparedContentReader = prepareContentReader();

      for ( PaxIndexData pax : paxIndexData )
      {
        if ( !pax.isActive() && underArmourService.uaEnabled() && underArmourService.isParticipantAuthorized( pax.getUserId() ) )
        {
          underArmourService.deAuthorizeParticipant( pax.getUserId() );
        }
      }

      esIndexer.index( paxIndexData, BIIndexType.PARTICIPANT );
    }
    catch( Exception e )
    {
      logger.error( "Exception while indexParticipants for pax ids : " + paxIds, e );
      throw new BeaconRuntimeException( e );
    }
    finally
    {
      // If we created a content reader for this thread, remove it
      if ( preparedContentReader )
      {
        removeContentReader();
      }
    }
    return (long)paxIndexData.size();
  }

  @Override
  public List<PaxCard> getPaxCardsWithFrontViewDetails( List<PaxIndexData> paxIndexData, List<Long> paxFollowersList )
  {
    List<PaxCard> cards = new ArrayList<PaxCard>();

    if ( isEmpty( paxIndexData ) )
    {
      return cards;
    }

    List<Long> allNodeIds = paxIndexData.stream().map( p -> p.getPrimaryNodeId() ).collect( toList() );
    List<Long> allPaxIds = paxIndexData.stream().map( p -> p.getUserId() ).collect( toList() );
    Map<Long, String> nodeDescription = nodeDAO.getNodeDescription( allNodeIds );
    List<Long> paxIdsWhoDisabledPublicProfile = participantDAO.findPaxIdsWhoDisabledPublicProfile( allPaxIds );
    List<Map<Long, CountryValueBean>> countryResults = getParticipantService().populateCountriesForUsers( allPaxIds.toArray( new Long[allPaxIds.size()] ) );

    List<PaxCard> paxCards = paxIndexData.stream().map( p -> buildCardWithFrontViewDetails( nodeDescription, p, paxIdsWhoDisabledPublicProfile, countryResults ) ).collect( toList() );

    if ( isNotEmpty( paxFollowersList ) )
    {

      for ( PaxCard card : paxCards )
      {
        if ( paxFollowersList.contains( card.getPaxId() ) )
        {
          card.setFollow( true );
        }
      }
    }
    return paxCards;
  }

  @Override
  public List<PaxCard> populateEligiblePromotionBean( List<PaxCard> cards, List<PaxIndexData> indexData, List<PromotionMenuBean> eligiblePromotions )
  {
    for ( PaxCard card : cards )
    {
      PaxIndexData receiverIndexData = indexData.stream().filter( r -> r.getUserId().equals( card.getPaxId() ) ).findFirst().get();

      List<PromotionBean> promoBeanList = eligiblePromotions.stream().map( menuBean -> getPromotionBean( receiverIndexData, menuBean ) ).filter( p -> p != null ).collect( toList() );

      card.setPromotions( promoBeanList );
    }

    return cards;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public PromotionBean getPromotionBean( PaxIndexData receiver, PromotionMenuBean menuBean )
  {
    if ( receiver == null || menuBean == null || UserManager.getUser().isSSIAdmin() )
    {
      return null;
    }

    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)menuBean.getPromotion();
    PrimaryAudienceType primaryAudienceType = promotion.getPrimaryAudienceType();
    SecondaryAudienceType secondaryAudienceType = promotion.getSecondaryAudienceType();
    PromotionBean promotionBean = null;

    if ( promotion instanceof RecognitionPromotion && receiver.getUserId().equals( UserManager.getUserId() ) )
    {
      if ( ! ( (RecognitionPromotion)promotion ).isSelfRecognitionEnabled() 
          || ( (RecognitionPromotion)promotion ).isCheersPromotion() ) //Client customizations for WIP #62128 starts
      {
        return null;
      }
    }
    else if ( promotion instanceof NominationPromotion && receiver.getUserId().equals( UserManager.getUserId() ) )
    {
      if ( ! ( (NominationPromotion)promotion ).isSelfNomination() )
      {
        return null;
      }
    }

    if ( secondaryAudienceType.isAllActivePaxType() || primaryAudienceType.isAllActivePaxType() && secondaryAudienceType.isSameAsPrimaryType() )
    {
      promotionBean = new PromotionBean( promotion, menuBean );
    }
    else if ( secondaryAudienceType.isSpecifyAudienceType() )
    {
      Set<Audience> secondaryAudiences = promotion.getSecondaryAudiences();
      List<Long> secondaryAudienceIds = secondaryAudiences.stream().map( audience -> audience.getId() ).collect( toList() );
      if ( containsAny( secondaryAudienceIds, receiver.getAudienceIds() ) )
      {
        promotionBean = new PromotionBean( promotion, menuBean );
      }
    }
    else if ( primaryAudienceType.isSpecifyAudienceType() && secondaryAudienceType.isSameAsPrimaryType() )
    {
      Set<Audience> primaryAudiences = promotion.getPrimaryAudiences();
      List<Long> primaryAudienceIds = primaryAudiences.stream().map( audience -> audience.getId() ).collect( toList() );
      if ( containsAny( primaryAudienceIds, receiver.getAudienceIds() ) )
      {
        promotionBean = new PromotionBean( promotion, menuBean );
      }
    }
    else if ( secondaryAudienceType.isSpecificNodeType() )
    {
      if ( receiver.isOnSameNode( UserManager.getPrimaryNodeId() ) )
      {
        promotionBean = new PromotionBean( promotion, menuBean );
      }
    }
    else if ( secondaryAudienceType.isSpecificNodeAndBelowType() )
    {
      if ( receiver.isOnSameOrBelowMyNode( UserManager.getPrimaryNodeId() ) )
      {
        promotionBean = new PromotionBean( promotion, menuBean );
      }
    }

    if ( promotionBean != null )
    {
      populateAttributes( promotionBean, promotion );
    }

    return promotionBean;
  }

  private void populateAttributes( PromotionBean promotionBean, AbstractRecognitionPromotion recogPromotion )
  {
    boolean isEasy = getPromotionService().isEasyPromotion( recogPromotion );
    boolean commentsActive = !PublicRecognitionFormattedValueBean.PROMO_TYPE_PURL.equalsIgnoreCase( recogPromotion.getPromotionType().getCode() );
    boolean isCardActive = recogPromotion.isCardActive();
    //Client customizations for WIP #62128 
    boolean isCheers = recogPromotion.isRecognitionPromotion() && ((RecognitionPromotion)recogPromotion).isCheersPromotion();

    PromotionAttributes attributes = new PromotionAttributes( isEasy, commentsActive, isCardActive );
    attributes.setCheers( isCheers ); //Client customizations for WIP #62128 
    promotionBean.setAttributes( attributes );
  }

  @Override
  public Optional<PaxIndexData> getIndexDataForUser( Long userId ) throws Exception
  {
    PaxIndexSearchCriteria criteria = new PaxIndexSearchCriteria();
    criteria.setIncludeUserIds( Arrays.asList( String.valueOf( userId ) ) );
    criteria.setRecordsMaxSize( 1 );
    return search( criteria ).getSearchResults().stream().findFirst();
  }

  @Override
  public PaxCard buildCardWithFrontViewDetails( Map<Long, String> nodeDescription, PaxIndexData indexData, List<Long> paxIdsWhoDisabledPublicProfile, List<Map<Long, CountryValueBean>> countryResults )
  {
    PaxCard card = new PaxCard( indexData, null );
    //Participant pax = participantService.getParticipantById( indexData.getUserId() );

    if ( !StringUtils.isBlank( indexData.getDepartmentTypeCode() ) )
    {
    // customization tuning start
      /*
      PickListValueBean pickListDeptValueBean = userService.getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                        pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                                                                        indexData.getDepartmentTypeCode() );
      if ( !Objects.isNull( pickListDeptValueBean ) )
      {
        card.setDepartmentName( pickListDeptValueBean.getName() );
      }
      */
      card.setDepartmentName( indexData.getDepartmentTypeCode() );
      // customization end
    }
    if ( !StringUtils.isBlank( indexData.getPositionTypeCode() ) )
    {
    // customization tuning start
      /*
      PickListValueBean pickListPositionValueBean = userService.getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                            pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                                                                            indexData.getPositionTypeCode() );
      if ( !Objects.isNull( pickListPositionValueBean ) )
      {
        card.setJobName( pickListPositionValueBean.getName() );
      }
      */
      card.setJobName( indexData.getPositionTypeCode());
      // customization end
    }
    card.setOrganization( nodeDescription.get( indexData.getPrimaryNodeId() ) );
    card.setAvatarUrl( getAvatarUrl( indexData.getAvatar() ) );

    for ( Map<Long, CountryValueBean> countryInfo : countryResults )
    {
      if ( countryInfo != null )
      {
        CountryValueBean country = countryInfo.get( indexData.getUserId() );
        if ( country != null )
        {
          card.setCountryCode( country.getCountryCode() );
        }
      }
    }
    List<NameableBean> nodeList = new ArrayList<NameableBean>();
    List<Long> nodeIds = indexData.getAllNodeIds();
    if ( CollectionUtils.isNotEmpty( nodeIds ) )
    {
      nodeIds.forEach( ( nodeId ) ->
      {
        Node node = nodeService.getNodeById( nodeId );
        NameableBean bean = new NameableBean( nodeId, node.getName() );
        nodeList.add( bean );
      } );
    }
    card.setNodes( nodeList );
    card.setNodeId( indexData.getPrimaryNodeId() );

    if ( !paxIdsWhoDisabledPublicProfile.contains( indexData.getUserId() ) )
    {
      card.setParticipantUrl( getParticipantService().getParticipantPublicUrl( indexData.getUserId() ) );
    }
    
    //Client customization start
    Long charaterticId = systemVariableService.getPropertyByName( SystemVariableService.COKE_USER_CURRENCY_CHAR ).getLongVal();
    String currency = userCharacteristicService.getCharacteristicValueByUserAndCharacterisiticId(indexData.getUserId(), charaterticId);    
    card.setCurrency( currency );
    if(StringUtils.isBlank( currency ))
    {
      card.setCurrency( "USD" );
    }
    //Client customization end
    
    return card;
  }

  @Override
  public List<PaxCard> removePromotionFromCardThatNotEligibleToRecive( List<PaxCard> paxCards, Long userId )
  {
    if ( isEmpty( paxCards ) )
    {
      return new ArrayList<PaxCard>();
    }

    Optional<PaxCard> searcherCard = paxCards.stream().filter( card -> card.getPaxId().equals( userId ) ).findFirst();

    if ( !searcherCard.isPresent() )
    {
      return paxCards;
    }

    PaxCard card = searcherCard.get();
    List<PromotionBean> notEligibleToReceive = card.getPromotions().stream().filter( promo -> promo != null && !promo.isCanReceive() ).collect( toList() );

    card.getPromotions().removeAll( notEligibleToReceive );

    return paxCards;
  }

  @Override
  public List<PaxCard> buildPaxCards( List<PaxIndexData> paxIndexList, List<PromotionMenuBean> allEligiblePromotions, List<Long> paxFollowersList ) throws Exception
  {
    Long searcherUserId = UserManager.getUserId();
    List<PromotionMenuBean> eligiblePromoForSubmit = allEligiblePromotions.parallelStream().filter( p ->
    {
      return p.isCanSubmit() && p.getPromotion().isAbstractRecognitionPromotion();
    } ).collect( toList() );

    List<PaxCard> allCards = getPaxCardsWithFrontViewDetails( paxIndexList, paxFollowersList );

    populateEligiblePromotionBean( allCards, paxIndexList, eligiblePromoForSubmit );
    return removePromotionFromCardThatNotEligibleToRecive( allCards, searcherUserId );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SearchFilterTypeCountValue> getSearchFilterCountByName( Long userId, String startsWith )
  {
    List<SearchFilterTypeCountValue> typeCounts = new ArrayList<SearchFilterTypeCountValue>();
    List<PickListValueBean> jobPositions = userService.getPickListValuesFromCM( PositionType.PICKLIST_ASSET + ".items", UserManager.getUserLocale() );
    List<PickListValueBean> departments = userService.getPickListValuesFromCM( DepartmentType.PICKLIST_ASSET + ".items", UserManager.getUserLocale() );
    typeCounts.add( new SearchFilterTypeCountValue( ParticipantSearchFilterEnum.LOCATION, nodeDAO.getCountByNodeName( startsWith ) ) );
    typeCounts.add( new SearchFilterTypeCountValue( ParticipantSearchFilterEnum.JOBTITLE, filterPickList( startsWith, jobPositions ).size() ) );
    typeCounts.add( new SearchFilterTypeCountValue( ParticipantSearchFilterEnum.DEPARTMENT, filterPickList( startsWith, departments ).size() ) );
    typeCounts.add( new SearchFilterTypeCountValue( ParticipantSearchFilterEnum.COUNTRY, getCountryCountByNameStartsWith( startsWith ) ) );
    // NOTE, do we want to keep a boolean in the session to skip this search if we KNOW the user has
    // no Participant Groups?
    typeCounts.add( new SearchFilterTypeCountValue( ParticipantSearchFilterEnum.PAX_GROUP, getPaxGroupCountByNameStartsWith( userId, startsWith ) ) );

    return typeCounts;
  }

  @Override
  public int getAutoCompleteDelay()
  {
    PropertySetItem prop = systemVariableService.getPropertyByName( SystemVariableService.AUTOCOMPLETE_SEARCH_DELAY_MILLIS );
    if ( null == prop )
    {
      return 200;
    }
    return prop.getIntVal();
  }

  public int getCountryCountByNameStartsWith( String startsWith )
  {
    return countryService.getCountryCountByStartsWith( startsWith );
  }

  public int getPaxGroupCountByNameStartsWith( Long userId, String startsWith )
  {
    return participantGroupDAO.findGroupCountByUserIdAndStartsWith( userId, startsWith );
  }

  private List<PickListValueBean> filterPickList( String query, List<PickListValueBean> picklistItems )
  {
    List results = new ArrayList();
    if ( !StringUtil.isEmpty( query ) )
    {
      if ( picklistItems != null )
      {
        for ( PickListValueBean i : picklistItems )
        {
          if ( i.getName() != null && i.getName().toLowerCase().startsWith( query.toLowerCase() ) )
          {
            results.add( i );
          }
        }
      }
    }

    return results;
  }

  @Override
  public List<PaxIndexData> findIndexData( List<String> paxIds ) throws Exception
  {
    PaxIndexSearchCriteria criteria = new PaxIndexSearchCriteria();
    criteria.setIncludeUserIds( paxIds );
    criteria.setRecordsMaxSize( paxIds.size() );
    List<PaxIndexData> searchResults = search( criteria ).getSearchResults();
    return searchResults;
  }

  // We want to be able to override this for unit testing - to avoid unneeded static call
  boolean prepareContentReader()
  {
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderUtils.prepareContentReader();
      return true;
    }
    return false;
  }

  void removeContentReader()
  {
    ContentReaderManager.removeContentReader();
  }

  protected String getAvatarUrl( String avatar )
  {
    return avatar;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }
}

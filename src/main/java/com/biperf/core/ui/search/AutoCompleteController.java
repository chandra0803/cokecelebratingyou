
package com.biperf.core.ui.search;

import static com.biperf.core.service.system.SystemVariableService.AUTOCOMPLETE_ELASTIC_SEARCH_MAX_ALLOWED_TO_RECOGNIZE;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.ParticipantSearchFilterEnum;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.groups.ParticipantGroupService;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.service.participant.impl.SearchFilterTypeCountValue;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.ResponseEntity;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.ui.groups.ParticipantGroupView;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.UserSessionAttributes;
import com.biperf.core.value.ParticipantPreviewBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.indexing.IndexSearchResult;
import com.biperf.core.value.participant.PaxCard;
import com.biperf.core.value.participant.PaxCard.PurlData;
import com.biperf.core.value.participant.PaxIndexData;
import com.biperf.core.value.recognition.PurlRecipientValue;
import com.objectpartners.cms.util.CmsResourceBundle;

@Controller
@RequestMapping( "/search" )
public class AutoCompleteController extends SpringBaseController
{
  private static final Log LOGGER = LogFactory.getLog( AutoCompleteController.class );

  public @Autowired AutoCompleteService autoCompleteService;
  public @Autowired SystemVariableService systemVariableService;
  private @Autowired PromotionService promotionService;
  private @Autowired PurlService purlService;
  private @Autowired FilterAppSetupService filterAppSetupService;
  private @Autowired ParticipantGroupService participantGroupService;

  private AutoCompleteControllerHelper helper = new AutoCompleteControllerHelper();

  @RequestMapping( value = "/filters.action", method = RequestMethod.POST )
  public @ResponseBody FilterView getFilterList() throws Exception
  {
    return new FilterView( ParticipantSearchFilterEnum.getActiveFilters() );
  }

  @RequestMapping( value = "/paxHeroSearch.action", method = RequestMethod.POST )
  public @ResponseBody HeroPaxSearchView paxHeroSearch( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest httpRequest ) throws Exception
  {
    if ( StringUtils.isNotEmpty( model.getFilter() ) )
    {
      return searchViewWithCountsOnly( model );
    }

    if ( model.isAudienceSpecific() )
    {
      buildSecondaryAudienceCriteria( model, httpRequest );
    }

    PaxSearchView paxSearchView = paxSearch( model, httpRequest );

    if ( filterAppSetupService.isFilterEnabled( httpRequest, TileMappingType.PURL_CELEBRATE ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      @SuppressWarnings( "unchecked" )
      List<PurlRecipientValue> upComingPurlRecipients = (List<PurlRecipientValue>)httpRequest.getSession().getAttribute( UserSessionAttributes.UPCOMING_CELEBRATION_IDS );
      if ( upComingPurlRecipients == null )
      {
        upComingPurlRecipients = purlService.getUpComingCelebrationList();
        httpRequest.getSession().setAttribute( UserSessionAttributes.UPCOMING_CELEBRATION_IDS, upComingPurlRecipients );
      }
      populatePurlUrls( paxSearchView, upComingPurlRecipients );
    }

    HeroPaxSearchView heroPaxSearchView = new HeroPaxSearchView( paxSearchView );

    if ( model.getName() != null && !model.getName().isEmpty() )
    {
      heroPaxSearchView.setSearchFilterTypeCounts( buildSearchTypeCountViews( model ) );
    }

    return heroPaxSearchView;
  }

  @RequestMapping( value = "/filterTypeCount.action", method = RequestMethod.POST )
  public @ResponseBody List<SearchFilterTypeCountView> getFilterTypeCount( @ModelAttribute PaxSearchQueryModel model ) throws Exception
  {
    return buildSearchTypeCountViews( model );
  }

  @SuppressWarnings( "unchecked" )
  @RequestMapping( value = "/paxSearch.action", method = RequestMethod.POST )
  public @ResponseBody PaxSearchView paxSearch( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest httpRequest ) throws Exception
  {
    if ( StringUtils.isNotEmpty( model.getFilter() ) )
    {
      return searchViewWithCountsOnly( model );
    }

    if ( model.isAudienceSpecific() )
    {
      buildSecondaryAudienceCriteria( model, httpRequest );
    }

    if ( model.getPaxGroup() != null )
    {
      model.setIncludeUserIds( buildPaxIdsBasedOnPaxGroup( model ) );
    }

    IndexSearchResult<PaxIndexData> paxIndexData = autoCompleteService.search( helper.buildPaxSearchCriteria( model ) );

    List<PaxIndexData> paxIndexList = paxIndexData.getSearchResults();

    List<PromotionMenuBean> allEligiblePromotions = getEligiblePromotions( httpRequest );

    List<Long> paxFollowersList = (List<Long>)httpRequest.getSession().getAttribute( UserSessionAttributes.PARTICIPANT_FOLLOWERS );
    List<PaxCard> allCards = autoCompleteService.buildPaxCards( paxIndexList, allEligiblePromotions, paxFollowersList );

    SearchViewHeaderInfo header = new SearchViewHeaderInfo( paxIndexData.getHitCount() );

    PaxSearchView paxSearchView = new PaxSearchView( allCards, header );

    paxSearchView.setMaxAllowedToRecognize( systemVariableService.getPropertyByName( AUTOCOMPLETE_ELASTIC_SEARCH_MAX_ALLOWED_TO_RECOGNIZE ).getIntVal() );
    return paxSearchView;
  }

  @RequestMapping( value = "/ownerSearch.action", method = RequestMethod.POST )
  public @ResponseBody PaxSearchView ownerSearch( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest httpRequest ) throws Exception
  {
    Long loggedInUserId = UserManager.getUserId();
    model.setExcludeUserIds( Arrays.asList( String.valueOf( loggedInUserId ) ) );
    model.setRoleType( HierarchyRoleType.OWNER );

    if ( StringUtils.isNotEmpty( model.getFilter() ) )
    {
      return searchViewWithCountsOnly( model );
    }

    if ( model.isAudienceSpecific() )
    {
      buildSecondaryAudienceCriteria( model, httpRequest );
    }

    if ( model.getPaxGroup() != null )
    {
      model.setIncludeUserIds( buildPaxIdsBasedOnPaxGroup( model ) );
    }

    IndexSearchResult<PaxIndexData> paxIndexData = autoCompleteService.search( helper.buildPaxSearchCriteria( model ) );

    List<PaxIndexData> paxIndexList = paxIndexData.getSearchResults();

    List<PromotionMenuBean> allEligiblePromotions = getEligiblePromotions( httpRequest );

    List<Long> paxFollowersList = (List<Long>)httpRequest.getSession().getAttribute( UserSessionAttributes.PARTICIPANT_FOLLOWERS );
    List<PaxCard> allCards = autoCompleteService.buildPaxCards( paxIndexList, allEligiblePromotions, paxFollowersList );

    SearchViewHeaderInfo header = new SearchViewHeaderInfo( paxIndexData.getHitCount() );

    PaxSearchView paxSearchView = new PaxSearchView( allCards, header );

    paxSearchView.setMaxAllowedToRecognize( systemVariableService.getPropertyByName( AUTOCOMPLETE_ELASTIC_SEARCH_MAX_ALLOWED_TO_RECOGNIZE ).getIntVal() );
    return paxSearchView;

  }

  private HeroPaxSearchView searchViewWithCountsOnly( PaxSearchQueryModel model )
  {
    HeroPaxSearchView heroPaxSearchView = new HeroPaxSearchView();
    heroPaxSearchView.setSearchFilterTypeCounts( buildSearchTypeCountViews( model ) );
    return heroPaxSearchView;
  }

  private List<SearchFilterTypeCountView> buildSearchTypeCountViews( PaxSearchQueryModel model )
  {
    List<SearchFilterTypeCountView> viewValues = new ArrayList<SearchFilterTypeCountView>();

    String name = StringUtils.isNotEmpty( model.getFilter() ) ? model.getFilter() : model.getName();

    List<SearchFilterTypeCountValue> values = autoCompleteService.getSearchFilterCountByName( UserManager.getUserId(), name );
    for ( SearchFilterTypeCountValue value : values )
    {
      viewValues.add( new SearchFilterTypeCountView( value.getPaxSearchFilterType().getCode(), value.getCount() ) );
    }
    return viewValues;
  }

  @SuppressWarnings( "unchecked" )
  public void buildSecondaryAudienceCriteria( PaxSearchQueryModel model, HttpServletRequest httpRequest ) throws Exception
  {
    List<PromotionMenuBean> ep = getEligiblePromotions( httpRequest );
    Promotion promotion = null;

    if ( model.getPromotionId() != null )
    {
      promotion = (Promotion)ep.parallelStream().filter( p -> p.getPromotion().getId().equals( model.getPromotionId() ) ).findFirst().get().getPromotion();
      if ( promotion instanceof RecognitionPromotion )
      {
        if ( ! ( (RecognitionPromotion)promotion ).isSelfRecognitionEnabled() )
        {
          model.setExcludeUserIds( Arrays.asList( UserManager.getUserId() + "" ) );
        }
      }
      else if ( promotion instanceof NominationPromotion )
      {
        if ( ! ( (NominationPromotion)promotion ).isSelfNomination() )
        {
          model.setExcludeUserIds( Arrays.asList( UserManager.getUserId() + "" ) );
        }
      }
    }

    if ( model.getContestId() != null )
    {
      promotion = (SSIPromotion)ep.parallelStream().filter( p -> p.getPromotion().isSSIPromotion() && p.getPromotion().isLive() ).findFirst().get().getPromotion();
    }

    PrimaryAudienceType primaryAudienceType = promotion.getPrimaryAudienceType();

    SecondaryAudienceType secondaryAudienceType = promotion.getSecondaryAudienceType();
    if ( secondaryAudienceType != null )
    {
      if ( secondaryAudienceType.isSpecifyAudienceType() )
      {
        Set<Audience> secondaryAudiences = promotion.getSecondaryAudiences();
        List<Long> secondaryAudienceIds = secondaryAudiences.stream().map( audience -> audience.getId() ).collect( toList() );
        model.setAudienceId( secondaryAudienceIds );
      }
      else if ( secondaryAudienceType.isSpecificNodeType() )
      {
        model.setLocation( UserManager.getPrimaryNodeId() );
      }
      else if ( secondaryAudienceType.isSpecificNodeAndBelowType() )
      {
        model.setPath( UserManager.getPrimaryNodeId() );
      }
      else if ( secondaryAudienceType.isSameAsPrimaryType() && !primaryAudienceType.isAllActivePaxType() )
      {
        if ( primaryAudienceType.isSpecifyAudienceType() )
        {
          Set<Audience> primaryAudiences = promotion.getPrimaryAudiences();
          List<Long> primaryAudienceIds = primaryAudiences.stream().map( audience -> audience.getId() ).collect( toList() );
          model.setAudienceId( primaryAudienceIds );
        }
      }
    }

    // Partner Audience implementation for Goalquest promotion
    PartnerAudienceType partnerAudienceType = promotion.getPartnerAudienceType();
    if ( partnerAudienceType != null )
    {
      if ( partnerAudienceType.isSpecifyAudienceType() )
      {
        Set<Audience> partnerAudiences = promotion.getPartnerAudiences();
        List<Long> partnerAudienceIds = partnerAudiences.stream().map( audience -> audience.getId() ).collect( toList() );
        model.setAudienceId( partnerAudienceIds );
      }
      else if ( partnerAudienceType.isPreSelectedPartnerAudienceType() )
      {
        model.setLocation( UserManager.getPrimaryNodeId() );
      }
      else if ( partnerAudienceType.isUserCharacteristics() )
      {
        model.setPath( UserManager.getPrimaryNodeId() );
      }
    }

  }

  @ExceptionHandler( Exception.class )
  @ResponseStatus( value = INTERNAL_SERVER_ERROR )
  public @ResponseBody ResponseEntity<List<WebErrorMessage>, Object> handleInternalException( HttpServletRequest request, Exception ex )
  {
    LOGGER.error( "Requested URL=" + request.getRequestURL(), ex );
    return new ResponseEntity<List<WebErrorMessage>, Object>( buildAppExceptionMessage(), null );
  }

  /**
   * Method used to build the pax ids based on the group id
   * @param model
   * @return
   * @throws Exception 
   */
  private List<String> buildPaxIdsBasedOnPaxGroup( PaxSearchQueryModel model ) throws Exception
  {
    List<String> paxIds = new ArrayList<String>();

    ParticipantGroupView participantGroupView = participantGroupService.getGroupDetailsByGroupId( model.getPaxGroup() );

    List<ParticipantPreviewBean> participantPreviewBeans = participantGroupView.getParticipants();

    for ( ParticipantPreviewBean participantPreviewBean : participantPreviewBeans )
    {
      paxIds.add( Long.toString( participantPreviewBean.getId() ) );
    }

    return paxIds;
  }

  private void populatePurlUrls( PaxSearchView paxSearchView, List<PurlRecipientValue> upComingPurlRecipients )
  {
    if ( !CollectionUtils.isEmpty( upComingPurlRecipients ) )
    {
      List<PaxCard> cards = paxSearchView.getPaxCards();

      for ( PaxCard paxCard : cards )
      {
        List<PurlRecipientValue> filteredPurlRecipients = upComingPurlRecipients.stream().filter( p -> p.getUserId().longValue() == paxCard.getPaxId().longValue() ).collect( Collectors.toList() );

        if ( !CollectionUtils.isEmpty( filteredPurlRecipients ) )
        {
          for ( PurlRecipientValue purlRecipientValue : filteredPurlRecipients )
          {
            paxCard.getPurlData().add( createPurlData( purlRecipientValue ) );
          }
        }
      }
    }
  }

  private PurlData createPurlData( PurlRecipientValue purlRecipientValue )
  {
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    PurlData purlData = new PurlData();

    if ( purlRecipientValue.getPromotionId() != null )
    {
      clientStateParameterMap.put( "promotionId", purlRecipientValue.getPromotionId() );
    }
    clientStateParameterMap.put( "purlRecipientId", purlRecipientValue.getPurlRecipientId() );
    clientStateParameterMap.put( "userId", UserManager.getUserId() );

    purlData.setAnniversary( purlRecipientValue.getAnniversary() );
    purlData.setId( purlRecipientValue.getUserId() );
    purlData.setViewUrl( "" );

    String contributeUrl = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                 PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_SINGLE,
                                                                 clientStateParameterMap );
    purlData.setContributeUrl( contributeUrl );

    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    purlData.setPromotion( promotionService.getPromotionByIdWithAssociations( new Long( purlRecipientValue.getPromotionId() ), promotionAssociationRequestCollection ).getPromotionName() );

    purlData.setExpirationDate( DateUtils.toDisplayString( purlRecipientValue.getAwardDate() ) );

    int numberOfDaysLeft = DateUtils.getNumberOfDaysLeft( DateUtils.getCurrentDate(), purlRecipientValue.getAwardDate() );
    String timeLeftMessage = null;
    if ( numberOfDaysLeft == 1 )
    {
      timeLeftMessage = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.TODAY" );
      purlData.setToday( true );
    }
    else
    {
      timeLeftMessage = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.DAYS_LEFT" ), new Object[] { numberOfDaysLeft } );
    }
    purlData.setTimeLeft( timeLeftMessage );

    return purlData;
  }
}

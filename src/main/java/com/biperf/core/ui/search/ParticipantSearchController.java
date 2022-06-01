
package com.biperf.core.ui.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.BaseJsonView;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.BadgeView;
import com.biperf.core.domain.participant.DelegatorView;
import com.biperf.core.domain.participant.ParticipantAlertView;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.UserSessionAttributes;
import com.biperf.core.value.CountryValueBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.participant.PaxCard;
import com.biperf.core.value.participant.PaxIndexData;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.ContentReaderManager;

@Controller
@RequestMapping( "/participantSearch" )
public class ParticipantSearchController extends AutoCompleteController
{

  @Autowired
  private ParticipantService participantService;
  @Autowired
  private PromotionService promotionService;
  @Autowired
  private UserService userService;
  @Autowired
  private NodeService nodeService;

  protected static final String REQUEST_PARAM_PROMOTION_ID = "promotionId";

  @RequestMapping( value = "/participantSearch.action", method = RequestMethod.POST )
  public @ResponseBody ParticipantSearchView participantPaxSearch( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest request ) throws Exception
  {
    ParticipantSearchView participantSearchView = new ParticipantSearchView();

    model.setNodeId( null );
    model.setNodeId( model.getLocation() );

    HeroPaxSearchView heroPaxSearchView = paxHeroSearch( model, request );

    List<PaxCard> paxCards = heroPaxSearchView.getPaxCards();
    paxCards = filterUsers( request, paxCards );

    List<Long> paxIds = new ArrayList<Long>();
    for ( PaxCard paxCard : paxCards )
    {
      paxIds.add( paxCard.getPaxId() );
    }

    Long[] ids = paxIds.toArray( new Long[paxIds.size()] );

    if ( ids.length > 0 && CollectionUtils.isNotEmpty( paxCards ) )
    {
      List<Map<Long, CountryValueBean>> countryResults = participantService.populateCountriesForUsers( ids );
      Long purlRecipientId = getRecipientId( request );
      if ( purlRecipientId != null )
      {
        participantSearchView = this.buildRecognitionPaxSearchView( paxCards, getPromotionId( request ), countryResults, purlRecipientId );
      }
      else
      {
        participantSearchView = this.buildRecognitionPaxSearchView( paxCards, getPromotionId( request ), countryResults, null );
      }
      flagInvalidCountries( getPromotion( request ), participantSearchView );
    }

    participantSearchView.setMaxAllowedToRecognize( heroPaxSearchView.getMaxAllowedToRecognize() );
    participantSearchView.setSearchFilterTypeCounts( heroPaxSearchView.getSearchFilterTypeCounts() );

    SearchViewHeaderInfo recognitionHeader = new SearchViewHeaderInfo();
    recognitionHeader.setTotalRecordsFound( heroPaxSearchView.getHeader().getTotalRecordsFound() );
    participantSearchView.setHeader( recognitionHeader );
    return participantSearchView;
  }

  @SuppressWarnings( "unchecked" )
  @RequestMapping( value = "/validatePaxForRecog.action", method = RequestMethod.POST )
  public @ResponseBody PaxPromoValidationView validatePax( @ModelAttribute PaxSearchQueryModel model, HttpServletRequest request ) throws Exception
  {
    List<PaxIndexData> paxIndexList = new ArrayList<PaxIndexData>();
    List<PromotionMenuBean> selectedPromotion = null;

    Long promotionId = Long.valueOf( request.getParameter( "promotionId" ) );
    String users = Objects.nonNull( request.getParameter( "users" ) ) ? request.getParameter( "users" ).trim() : null;
    String[] paxIdsToValidate = !StringUtils.isEmpty( users ) ? users.split( "," ) : null;
    List<PromotionMenuBean> allEligiblePromotions = getEligiblePromotions( request );

    paxIndexList = autoCompleteService.findIndexData( Objects.nonNull( paxIdsToValidate ) ? Arrays.asList( paxIdsToValidate ) : Collections.emptyList() );

    for ( PromotionMenuBean promotionMenuBean : allEligiblePromotions )
    {
      if ( promotionMenuBean.getPromotion().getId().equals( promotionId ) )
      {
        selectedPromotion = Arrays.asList( promotionMenuBean );
        break;
      }
    }

    List<Long> paxFollowersList = (List<Long>)request.getSession().getAttribute( UserSessionAttributes.PARTICIPANT_FOLLOWERS );
    List<PaxCard> paxCards = autoCompleteService.buildPaxCards( paxIndexList, selectedPromotion, paxFollowersList );
    List<PaxPromoValidationView.Recipient> p = new ArrayList<PaxPromoValidationView.Recipient>();

    for ( PaxCard paxCard : paxCards )
    {
      PaxPromoValidationView.Recipient pax = new PaxPromoValidationView.Recipient( paxCard.getPaxId(), CollectionUtils.isNotEmpty( paxCard.getPromotions() ) );
      pax.setCountryRatio( getBudgetConversionRatio( paxCard.getPaxId(), UserManager.getUserId(), promotionId ) );
      p.add( pax );
    }

    return new PaxPromoValidationView( p );
  }

  private ParticipantSearchController.ParticipantSearchView buildRecognitionPaxSearchView( List<PaxCard> paxResults,
                                                                                           Long promotionId,
                                                                                           List<Map<Long, CountryValueBean>> countryResults,
                                                                                           Long purlRecipientId )
  {
    ParticipantSearchController.ParticipantSearchView recognitionPaxSearchView = new ParticipantSearchController.ParticipantSearchView();
    for ( Object object : paxResults )
    {
      if ( object instanceof PaxCard )
      {
        PaxCard pax = (PaxCard)object;
        ParticipantSearchController.ParticipantSearchView.Participant recognitionParticipant = new ParticipantSearchController.ParticipantSearchView.Participant();
        if ( ( purlRecipientId == null ) || ( ( purlRecipientId != null ) && ( !purlRecipientId.equals( pax.getPaxId() ) ) ) )
        {
          recognitionParticipant.setPaxId( pax.getPaxId() );
          recognitionParticipant.setLastName( pax.getLastName() );
          recognitionParticipant.setFirstName( pax.getFirstName() );
          recognitionParticipant.setAvatarUrl( pax.getAvatarUrl() );
          recognitionParticipant.setOrganization( pax.getOrganization() );
          recognitionParticipant.setOrgName( pax.getOrganization() );
          recognitionParticipant.setDepartmentName( pax.getDepartmentName() );
          recognitionParticipant.setJobName( pax.getJobName() );
          recognitionParticipant.setSelf( UserManager.getUserId().equals( pax.getPaxId() ) );
          recognitionParticipant.setParticipantUrl( pax.getParticipantUrl() );
          recognitionParticipant.setPromotions( pax.getPromotions() );
          recognitionParticipant.setFollow( pax.isFollow() );
          recognitionParticipant.setOptOutAwards( pax.isOptOutAwards() );

          List<NameableBean> nodeList = new ArrayList<NameableBean>();
          List<Long> nodeIds = pax.getAllNodeIds();
          if ( CollectionUtils.isNotEmpty( nodeIds ) )
          {
            for ( Long nodeId : nodeIds )
            {
              Node node = nodeService.getNodeById( nodeId );
              NameableBean bean = new NameableBean( nodeId, node.getName() );
              nodeList.add( bean );
            }
          }

          recognitionParticipant.setNodes( nodeList );
          recognitionParticipant.setNodeId( pax.getNodeId() );
          recognitionParticipant.setSelected( getIsSelected() );
          recognitionParticipant.setLocked( getIsLocked( pax ) );
          recognitionParticipant.setUrlEdit( getUrlEdit() );
          recognitionParticipant.setCountryRatio( getBudgetConversionRatio( pax.getPaxId(), UserManager.getUserId(), promotionId ).doubleValue() );
         
       // Client customizations for wip #42701 starts
          recognitionParticipant.setCurrency( userService.getUserCurrencyCharValue( pax.getPaxId() ) );
       // Client customizations for wip #42701 ends

          if ( CollectionUtils.isNotEmpty( pax.getPromotions() ) )
          {
            recognitionParticipant.setEligibleForPromotion( true );
          }

          Map<String, String> codes = new HashMap<String, String>();
          for ( Map<Long, CountryValueBean> countryInfo : countryResults )
          {
            if ( countryInfo != null )
            {
              CountryValueBean country = countryInfo.get( pax.getPaxId() );

              String countryName = "";
              if ( country != null )
              {
                String assetCode = country.getCmAssetCode();
                if ( codes.containsKey( assetCode ) )
                {
                  countryName = codes.get( assetCode );
                }
                else
                {
                  countryName = ContentReaderManager.getText( country.getCmAssetCode(), "COUNTRY_NAME" );
                  codes.put( assetCode, countryName );
                }
                recognitionParticipant.setCountryName( countryName );
                recognitionParticipant.setCountryCode( country.getCountryCode() );
              }
            }
          }
          recognitionPaxSearchView.getParticipants().add( recognitionParticipant );
        }
      }
    }
    return recognitionPaxSearchView;
  }

  protected void flagInvalidCountries( Promotion promotion, ParticipantSearchView listViewObj )
  {
    Set<String> validCountries = getValidRecipientCountries( promotion );
    if ( validCountries != null )
    {
      flagInvalidCountries( validCountries, listViewObj );
    }
  }

  public Set<String> getValidRecipientCountries( Promotion promotion )
  {
    if ( promotion != null && promotion.getAwardType() != null && promotion.getAwardType().isMerchandiseAwardType() )
    {
      Set<String> validCountries = new HashSet<String>();

      if ( promotion.getPromoMerchCountries() != null )
      {
        for ( Iterator<PromoMerchCountry> promoMerchCountryIter = promotion.getPromoMerchCountries().iterator(); promoMerchCountryIter.hasNext(); )
        {
          PromoMerchCountry promoMerchCountry = promoMerchCountryIter.next();
          validCountries.add( promoMerchCountry.getCountry().getCountryCode() );
        }
      }
      return validCountries;
    }
    return null;
  }

  private void flagInvalidCountries( Set<String> validCountries, ParticipantSearchView listViewObj )
  {
    List<ParticipantSearchController.ParticipantSearchView.Participant> paxs = listViewObj.getParticipants();
    if ( paxs != null )
    {
      for ( int i = 0; i < paxs.size(); i++ )
      {
        ParticipantSearchController.ParticipantSearchView.Participant participant = paxs.get( i );
        if ( !validCountries.contains( participant.getCountryCode() ) )
        {
          paxs.remove( i );
          i--;
        }
      }
    }
  }

  protected List<PaxCard> filterUsers( HttpServletRequest request, List<PaxCard> paxCards )
  {
    return paxCards;
  }

  protected BigDecimal getBudgetConversionRatio( Long receiverId, Long submitterId, Long promotionId )
  {
    return BudgetUtils.getBudgetConversionRatio( userService, promotionService, promotionId, receiverId, submitterId );
  }

  protected boolean getIsSelected()
  {
    return false;
  }

  protected boolean getIsLocked( PaxCard pax )
  {
    return false;
  }

  protected String getUrlEdit()
  {
    return "";
  }

  protected Long getPromotionId( HttpServletRequest request )
  {
    String promotionId = request.getParameter( "promotionId" );
    return StringUtils.isBlank( promotionId ) ? null : Long.valueOf( promotionId );
  }

  @SuppressWarnings( "unchecked" )
  protected Promotion getPromotion( HttpServletRequest request )
  {
    Long promotionId = null;

    Promotion promotion = null;

    if ( request.getParameter( "promoId" ) != null )
    {
      promotionId = Long.valueOf( request.getParameter( "promoId" ) );
    }
    else if ( request.getParameter( "promotionId" ) != null )
    {
      promotionId = Long.valueOf( request.getParameter( "promotionId" ) );
    }

    if ( promotionId != null )
    {
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
      promotion = promotionService.getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
    }

    return promotion;
  }

  @JsonInclude( Include.NON_NULL )
  public static class ParticipantSearchView extends BaseJsonView
  {
    @JsonProperty
    private SearchViewHeaderInfo header;
    @JsonProperty( "participants" )
    private List<Participant> participants = new ArrayList<Participant>();
    @JsonProperty( "totalCount" )
    private int totalCount;
    @JsonProperty( "allowBadge" )
    private boolean allowBadge;
    @JsonProperty( "allowPublicRecFollowList" )
    private boolean allowPublicRecFollowList;
    @JsonProperty( "maxAllowedToRecognize" )
    private int maxAllowedToRecognize;
    @JsonProperty( "searchFilterTypeCounts" )
    public List<SearchFilterTypeCountView> searchFilterTypeCounts = new ArrayList<SearchFilterTypeCountView>();
    private String sourceType;

    public SearchViewHeaderInfo getHeader()
    {
      return header;
    }

    public void setHeader( SearchViewHeaderInfo header )
    {
      this.header = header;
    }

    public List<Participant> getParticipants()
    {
      return participants;
    }

    public void setParticipants( List<Participant> participants )
    {
      this.participants = participants;
    }

    public int getTotalCount()
    {
      return totalCount;
    }

    public void setTotalCount( int totalCount )
    {
      this.totalCount = totalCount;
    }

    public boolean isAllowBadge()
    {
      return allowBadge;
    }

    public void setAllowBadge( boolean allowBadge )
    {
      this.allowBadge = allowBadge;
    }

    public boolean isAllowPublicRecFollowList()
    {
      return allowPublicRecFollowList;
    }

    public void setAllowPublicRecFollowList( boolean allowPublicRecFollowList )
    {
      this.allowPublicRecFollowList = allowPublicRecFollowList;
    }

    public int getMaxAllowedToRecognize()
    {
      return maxAllowedToRecognize;
    }

    public void setMaxAllowedToRecognize( int maxAllowedToRecognize )
    {
      this.maxAllowedToRecognize = maxAllowedToRecognize;
    }

    public List<SearchFilterTypeCountView> getSearchFilterTypeCounts()
    {
      return searchFilterTypeCounts;
    }

    public void setSearchFilterTypeCounts( List<SearchFilterTypeCountView> searchFilterTypeCounts )
    {
      this.searchFilterTypeCounts = searchFilterTypeCounts;
    }

    @JsonInclude( Include.NON_NULL )
    public static class Participant extends PaxCard
    {
      @JsonProperty( "isSelected" )
      private Boolean selected;
      @JsonProperty( "isLocked" )
      private Boolean locked;
      @JsonProperty( "urlEdit" )
      private String urlEdit;
      @JsonProperty( "canRecognize" )
      private Boolean canRecognize;
      @JsonProperty( "countryName" )
      private String countryName;
      @JsonProperty( "countryRatio" )
      private Double countryRatio;
      @JsonProperty( "allowPublicRecognition" )
      private Boolean allowPublicRecognition;
      @JsonProperty( "allowPublicInformation" )
      private Boolean allowPublicInformation;
      @JsonProperty( "largeAudience" )
      private Boolean globalParticipantSearchEnabled;
      @JsonProperty( "profileUrl" )
      private String profileUrl;
      @JsonProperty( "isSelf" )
      private Boolean self;
      @JsonProperty( "isPublic" )
      private Boolean isPublic;
      @JsonProperty( "throwdownEnabled" )
      private boolean throwdownEnabled;
      @JsonProperty( "orgName" )
      private String orgName;
      @JsonProperty( "badges" )
      private List<BadgeView> badges = new ArrayList<BadgeView>();
      @JsonProperty( "delegators" )
      private List<DelegatorView> delegators = new ArrayList<DelegatorView>();
      @JsonProperty( "alert" )
      private ParticipantAlertView alert = new ParticipantAlertView();
      @JsonProperty( "eligibleForPromotion" )
      private boolean eligibleForPromotion;
      @JsonProperty( "currency" )
      private String currency;

      public Participant()
      {
      }

      public Boolean getSelected()
      {
        return selected;
      }

      public void setSelected( Boolean selected )
      {
        this.selected = selected;
      }

      public Boolean getLocked()
      {
        return locked;
      }

      public void setLocked( Boolean locked )
      {
        this.locked = locked;
      }

      public String getUrlEdit()
      {
        return urlEdit;
      }

      public void setUrlEdit( String urlEdit )
      {
        this.urlEdit = urlEdit;
      }

      public Boolean getCanRecognize()
      {
        return canRecognize;
      }

      public void setCanRecognize( Boolean canRecognize )
      {
        this.canRecognize = canRecognize;
      }

      public String getCountryName()
      {
        return countryName;
      }

      public void setCountryName( String countryName )
      {
        this.countryName = countryName;
      }

      public Double getCountryRatio()
      {
        return countryRatio;
      }

      public void setCountryRatio( Double countryRatio )
      {
        this.countryRatio = countryRatio;
      }

      public Boolean getAllowPublicRecognition()
      {
        return allowPublicRecognition;
      }

      public void setAllowPublicRecognition( Boolean allowPublicRecognition )
      {
        this.allowPublicRecognition = allowPublicRecognition;
      }

      public Boolean getAllowPublicInformation()
      {
        return allowPublicInformation;
      }

      public void setAllowPublicInformation( Boolean allowPublicInformation )
      {
        this.allowPublicInformation = allowPublicInformation;
      }

      public void setGlobalParticipantSearchEnabled( Boolean globalParticipantSearchEnabled )
      {
        this.globalParticipantSearchEnabled = globalParticipantSearchEnabled;
      }

      public Boolean isGlobalParticipantSearchEnabled()
      {
        return globalParticipantSearchEnabled;
      }

      public String getProfileUrl()
      {
        return profileUrl;
      }

      public void setProfileUrl( String profileUrl )
      {
        this.profileUrl = profileUrl;
      }

      public Boolean getSelf()
      {
        return self;
      }

      public void setSelf( Boolean self )
      {
        this.self = self;
      }

      public Boolean getIsPublic()
      {
        return this.allowPublicInformation;
      }

      public void setIsPublic( Boolean isPublic )
      {
        this.isPublic = isPublic;
      }

      public boolean isThrowdownEnabled()
      {
        return throwdownEnabled;
      }

      public void setThrowdownEnabled( boolean throwdownEnabled )
      {
        this.throwdownEnabled = throwdownEnabled;
      }

      public String getOrgName()
      {
        return orgName;
      }

      public void setOrgName( String orgName )
      {
        this.orgName = orgName;
      }

      public List<BadgeView> getBadges()
      {
        return badges;
      }

      public void setBadges( List<BadgeView> badges )
      {
        this.badges = badges;
      }

      public List<DelegatorView> getDelegators()
      {
        return delegators;
      }

      public void setDelegators( List<DelegatorView> delegators )
      {
        this.delegators = delegators;
      }

      public ParticipantAlertView getAlert()
      {
        return alert;
      }

      public void setAlert( ParticipantAlertView alert )
      {
        this.alert = alert;
      }

      public boolean isEligibleForPromotion()
      {
        return eligibleForPromotion;
      }

      public void setEligibleForPromotion( boolean eligibleForPromotion )
      {
        this.eligibleForPromotion = eligibleForPromotion;
      }

      public String getCurrency()
      {
        return currency;
      }

      public void setCurrency( String currency )
      {
        this.currency = currency;
      }
      
    }
    
    

    public void setSourceType( String sourceType )
    {
      this.sourceType = sourceType;
    }

    public String getSourceType()
    {
      return sourceType;
    }
  }

  private Long getRecipientId( HttpServletRequest request )
  {
    try
    {
      return RequestUtils.getRequiredParamLong( request, "recipientId" );
    }
    catch( Exception ex )
    {
      return null;
    }
  }
}

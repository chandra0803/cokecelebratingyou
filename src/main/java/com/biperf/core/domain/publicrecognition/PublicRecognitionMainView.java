
package com.biperf.core.domain.publicrecognition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PublicRecognitionTabType;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.promotion.PublicRecognitionBadges;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetValueBean;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;
import com.biperf.core.value.UserDivisionValueBean;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PublicRecognitionMainView
{

  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private String userLang;
  private List<PublicRecognitionSet> recognitionSets = new ArrayList<PublicRecognitionSet>();
  private boolean allowPublicRecFollowList;
  private boolean newSAEnabled = false;

  public PublicRecognitionMainView()
  {
    super();
  }

  /**
   * 
   * @param userLang
   * @param recognitionSets
   * @param baseContextPath
   * @return PublicRecognitionMainView
   */
  public PublicRecognitionMainView( String userLang, List<PublicRecognitionSet> recognitionSets, String baseContextPath, BigDecimal usMediaValue, BigDecimal userMediaValue, String listValue )
  {
    this.userLang = userLang;
    if ( userLang == null )
    {
      userLang = Locale.ENGLISH.getLanguage();
    }
    boolean machineLanguageAllowTranslation = getSystemVariableService().getPropertyByName( SystemVariableService.MACHINE_LANGUAGE_ALLOW_TRANSLATION ).getBooleanVal();
    
    //Client customizations for WIP #62128 starts
    Long cheersPromotionId = getPromotionService().getCheersPromotionId();
    //Client customizations for WIP #62128 ends

    for ( PublicRecognitionSet prs : recognitionSets )
    {
      PublicRecognitionSet set = new PublicRecognitionSet( prs.getNameId(), prs.getName(), prs.getDesc(), prs.getHasFollowees(), prs.getTotalCount(), prs.getIsDefault() );

      if ( !StringUtil.isNullOrEmpty( listValue ) )
      {
        set.setSelectedListValue( listValue );
      }

      for ( PublicRecognitionFormattedValueBean valueBean : prs.getClaims() )
      {
        PublicRecognitionRecognitionView recognitionView = new PublicRecognitionRecognitionView();

        // Override allowing point additions if there is a single recipient that is opted out of
        // awards
        if ( null != valueBean.getRecipients() && valueBean.getRecipients().stream().anyMatch( recipient -> recipient.isOptOutAwards() ) )
        {
          recognitionView.setAllowAddPoints( false );
        }
        else
        {
          recognitionView.setAllowAddPoints( valueBean.isAllowAddPoints() );
        }

        List<BudgetValueBean> newBudgetValueBeanList = new ArrayList<BudgetValueBean>();
        for ( Iterator<BudgetValueBean> iter = valueBean.getBudgets().iterator(); iter.hasNext(); )
        {
          BudgetValueBean budgetValueBean = iter.next();

          if ( budgetValueBean.getId() != null )
          {
            budgetValueBean.setRemaining( BudgetUtils.applyMediaConversion( budgetValueBean.getRemaining(), usMediaValue, userMediaValue ) );
            newBudgetValueBeanList.add( budgetValueBean );
          }
          else
          {
            break;
          }
        }
        recognitionView.setBudgets( newBudgetValueBeanList );
        recognitionView.setComment( valueBean.getSubmitterComments() );
        if ( machineLanguageAllowTranslation && !StringUtil.isNullOrEmpty( recognitionView.getComment() ) )
        {
          recognitionView.setAllowTranslate( !userLang.equals( valueBean.getSubmitterCommentsLanguageType() ) );
        }
        else
        {
          recognitionView.setAllowTranslate( false );
        }
        recognitionView.setPromotionName( valueBean.getPromotionName() );
        recognitionView.setPromotionType( extractPromotionType( valueBean ) );

        if ( valueBean.isIncludePurl() && valueBean.getPurlRecipientId() != null )
        {
          Map<String, String> paramMap = new HashMap<String, String>();
          paramMap.put( "purlRecipientId", valueBean.getPurlRecipientId().toString() );
          recognitionView.setPurlUrl( ClientStateUtils.generateEncodedLink( baseContextPath, "/purl/purlRecipient.do?method=display", paramMap ) );
        }

        recognitionView.setTeamId( valueBean.getTeamId() );
        recognitionView.setTeamName( valueBean.getTeamName() );

        if ( valueBean.getUserComments() != null )
        {
          for ( PublicRecognitionComment comment : valueBean.getUserComments() )
          {
            recognitionView.getComments()
                .add( new PublicRecognitionCommentViewBean( comment,
                                                            !UserManager.getUserLocale().equals( comment.getCommentsLanguageType() != null ? comment.getCommentsLanguageType().getCode() : "" ) ) );
          }
        }

        recognitionView.setId( valueBean.getClaimId() );
        recognitionView.setLiked( "true".equals( valueBean.getIsLiked() ) ? true : false );
        recognitionView.setMine( "true".equals( valueBean.getIsMine() ) ? true : false );
        recognitionView.setHidden( UserManager.getUserId().equals( valueBean.getRecipientId() ) ? false : true );
        recognitionView.setNumLikers( valueBean.getNumLikers() );
        recognitionView.setPublicClaim( valueBean.getIsPublicClaim() );
        recognitionView.setTime( valueBean.getRelativeClaimApprovedDate() );

        if ( valueBean.isCumulative() )
        {
          Set<PublicRecognitionParticipantView> recipient = new HashSet<PublicRecognitionParticipantView>();
          if ( null != valueBean.getRecipients() )
          {
            for ( PublicRecognitionParticipantView publicRecognitionParticipantView : valueBean.getRecipients() )
            {
              recipient.add( publicRecognitionParticipantView );
              break;
            }
          }
          recognitionView.setRecipients( recipient );
        }
        else
        {
          if ( null != valueBean.getRecipients() )
          {
            Set<PublicRecognitionParticipantView> recipient = new HashSet<PublicRecognitionParticipantView>();
            recipient.addAll( valueBean.getRecipients() );
            recognitionView.setRecipients( recipient );
          }
        }
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put( "claimId", valueBean.getClaimId() );
        if ( valueBean.getClaimId() == null )
        {
          if ( null != recognitionView.getRecipients() && !recognitionView.getRecipients().isEmpty() )
          {
            recognitionView.setId( recognitionView.getRecipients().iterator().next().getClaimId() );
            paramMap.put( "claimId", recognitionView.getRecipients().iterator().next().getClaimId() );
          }
        }

        String detailUrl = StringUtils.EMPTY;

        if ( valueBean.isNominationClaim() )
        {
          Map<String, Object> urlParams = new HashMap<String, Object>();
          urlParams.put( "teamId", valueBean.getTeamId() );
          urlParams.put( "winnerId", valueBean.getRecipientId() );
          urlParams.put( "timePeriodId", valueBean.getTimePeriodId() );
          urlParams.put( "activityId", valueBean.getActivityId() );

          detailUrl = ClientStateUtils.generateEncodedLink( baseContextPath, PageConstants.NOMINATOR_MY_WINNER, urlParams );
        }
        else
        {
          detailUrl = ClientStateUtils.generateEncodedLink( baseContextPath, PageConstants.CLAIM_DETAIL_URL, paramMap );
        }

        recognitionView.setPublicRecognitionPageDetailUrl( detailUrl );

        recognitionView.setRecognizer( new PublicRecognitionParticipantView( valueBean.getSubmitterId(),
                                                                             valueBean.getSubmitterFirstName(),
                                                                             valueBean.getSubmitterLastName(),
                                                                             valueBean.getSubmitterAvatarSmallFullPath() ) );

        if ( recognitionView.isMine() )
        {
          recognitionView.setShareLinks( valueBean.getSocialLinks() );
        }
        else
        {
          recognitionView.setCountryRatio( valueBean.getCountryRatio() );
          if ( valueBean.isAwardAmountFixed() )
          {
            recognitionView.setAwardAmountFixed( valueBean.getFixedAwardAmount() );
          }
          else
          {
            recognitionView.setAwardAmountMin( valueBean.getAwardAmountMin() );
            recognitionView.setAwardAmountMax( valueBean.getAwardAmountMax() );
          }
        }

        recognitionView.seteCardImg( valueBean.geteCardImageUrl() ); // added for the ecard image
        if ( StringUtils.isNotBlank( valueBean.getVideoUrl() ) && Objects.nonNull( valueBean.getVideoUrl() ) )
        {
          if ( valueBean.getVideoUrl().contains( ActionConstants.REQUEST_ID ) )
          {

            MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( valueBean.getRequestId( valueBean.getVideoUrl() ) );
            String eCardVideoLink = null;
            if ( Objects.nonNull( mtcVideo ) )
            {
              eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
              recognitionView.setVideoThumbUrl( mtcVideo.getThumbNailImageUrl() );
            }
            else
            {
              eCardVideoLink = valueBean.getActualCardUrl( valueBean.getVideoUrl() );
              recognitionView.setVideoThumbUrl( valueBean.getActualCardUrl( valueBean.getVideoThumbUrl() ) );
            }
            recognitionView.setVideoUrl( eCardVideoLink );

          }
          else
          {
            recognitionView.setVideoUrl( valueBean.getVideoUrl() ); // added for the video url
            recognitionView.setVideoThumbUrl( valueBean.getVideoThumbUrl() ); // added for the video
                                                                              // thumb image url

          }
        }
        else
        {
          recognitionView.setVideoUrl( valueBean.getVideoUrl() ); // added for the video url
          recognitionView.setVideoThumbUrl( valueBean.getVideoThumbUrl() ); // added for the video
                                                                            // thumb image url

        }

        // populate the badges here
        if ( valueBean.getUserBadges() != null && valueBean.getUserBadges().size() > 0 )
        {
          // for nomination we need to add the additionalBadges field which should show the count of
          // all badges minus 1
          if ( valueBean.isNominationClaim() )
          {
            recognitionView.setAdditionalBadges( valueBean.getUserBadges().size() - 1 );
          }

          // we populate here the first badgeImg and badgeName
          String badgeUrl = ( (PublicRecognitionBadges)valueBean.getUserBadges().get( 0 ) ).getBadgeUrl();
          if ( badgeUrl != null )
          {
            String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
            recognitionView.setBadgeImg( siteUrl + badgeUrl );
          }

          String badgeName = ( (PublicRecognitionBadges)valueBean.getUserBadges().get( 0 ) ).getBadgeName();
          if ( badgeName != null )
          {
            recognitionView.setBadgeName( badgeName );
          }
        }

        // To display it in PR wall for New SA enabled
        if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          recognitionView.setRecognitionSequence( valueBean.getRecognitionSequence() );
          recognitionView.setPrimaryColor( valueBean.getPrimaryColor() );
          recognitionView.setSecondaryColor( valueBean.getSecondaryColor() );
          recognitionView.setCelebMessage( valueBean.getCelebMessage() );
          recognitionView.setCelebImgDesc( valueBean.getCelebImgDesc() );
          recognitionView.setProgramHeader( valueBean.getProgramHeader() );
          recognitionView.setUniqueId( valueBean.getUniqueId() );
          recognitionView.setCelebrationId( valueBean.getCelebrationId() );
          recognitionView.setCelebImageUrl( valueBean.getCelebImageUrl() );
          // Added ClaimId to handle update comments & Like
          recognitionView.setClaimId( valueBean.getClaimId() );
        }
        else
        {
          recognitionView.setUniqueId( recognitionView.getId() );
        }
        
        // Client customizations for WIP #62128 starts
        if ( null != valueBean && null != valueBean.getPromotionId() )
        {
          if ( Objects.nonNull( cheersPromotionId ) && cheersPromotionId.equals( valueBean.getPromotionId() ) )
          {
            recognitionView.setCheersPromotion( true );
          }
        }
        // Client customizations for WIP #62128 ends

        set.getRecognitions().add( recognitionView );
      }

      if ( set.getNameId().equals( PublicRecognitionTabType.COUNRTY_TAB ) )
      {
        set.setList( getCountryService().getAllActiveCountriesCodesAbbrevs() );
      }
      if ( set.getNameId().equals( PublicRecognitionTabType.DEPARTMENT_TAB ) )
      {
        /*customization start */
        //set.setList( DepartmentType.getList() );
        set.setList( getMainContentService().getPublicRecognitionDepartmentCache() );
        /*customization end */
      }
      
      if ( set.getNameId().equals( PublicRecognitionTabType.DIVISION_TAB ) )
      {
        set.setList(getDivisionKey());
      }
      set.mergeRecognitions();
      this.recognitionSets.add( set );
    }

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.PROFILE_FOLLOWLIST_SHOW ).getBooleanVal() )
    {
      this.setAllowPublicRecFollowList( true );
    }
  }

  public static String extractPromotionType( PublicRecognitionFormattedValueBean bean )
  {
    if ( bean.isRecognitionClaim() )
    {
      if ( bean.isIncludePurl() && bean.getPurlRecipientId() != null )
      {
        return PublicRecognitionFormattedValueBean.PROMO_TYPE_PURL;
      }
      else
      {
        return PublicRecognitionFormattedValueBean.PROMO_TYPE_RECOGNITION;
      }
    }
    else if ( bean.isNominationClaim() )
    {
      return PublicRecognitionFormattedValueBean.PROMO_TYPE_NOMINATION;
    }
    return null;
  }
/**
 * 
 * @return
 */
  private   List<UserDivisionValueBean>  getDivisionKey(){	 
	 String charactristicValue = "";
	 UserDivisionValueBean userDivisionValueBean = new UserDivisionValueBean();
	 List<UserDivisionValueBean> returnDivisionLst = new ArrayList<UserDivisionValueBean>();
	 charactristicValue = getUserService().getUserDivisionKeyCharValue(UserManager.getUserId());
	 if( null != charactristicValue ){		 
		 userDivisionValueBean.setDivision(charactristicValue);
		 returnDivisionLst.add(userDivisionValueBean);
	 }	 
	  return returnDivisionLst; 
  }
  
  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  @JsonIgnore
  public String getUserLang()
  {
    return userLang;
  }

  public List<PublicRecognitionSet> getRecognitionSets()
  {
    return recognitionSets;
  }

  public void setRecognitionSets( List<PublicRecognitionSet> recognitionSets )
  {
    this.recognitionSets = recognitionSets;
  }

  public boolean isNewSAEnabled()
  {
    return newSAEnabled;
  }

  public void setNewSAEnabled( boolean newSAEnabled )
  {
    this.newSAEnabled = newSAEnabled;
  }
  
  private static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private static CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

  public void setAllowPublicRecFollowList( boolean allowPublicRecFollowList )
  {
    this.allowPublicRecFollowList = allowPublicRecFollowList;
  }

  public boolean isAllowPublicRecFollowList()
  {
    return allowPublicRecFollowList;
  }

  private static MTCVideoService getMtcVideoService()
  {
    return (MTCVideoService)BeanLocator.getBean( MTCVideoService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }
  
  /*customization start */
  private static MainContentService getMainContentService()
  {
    return (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME );
  }
  /*customization end */
}

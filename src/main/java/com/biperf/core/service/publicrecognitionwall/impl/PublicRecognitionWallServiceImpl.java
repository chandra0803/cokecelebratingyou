
package com.biperf.core.service.publicrecognitionwall.impl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.biperf.core.dao.publicrecognitionwall.PublicRecognitionWallDAO;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cache.CacheManagementService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.publicrecognitionwall.PublicRecognitionWallService;
import com.biperf.core.service.publicrecognitionwall.dto.LocaleTranslation;
import com.biperf.core.service.publicrecognitionwall.dto.PublicRecognitionParticipant;
import com.biperf.core.service.publicrecognitionwall.dto.PublicRecognitionWall;
import com.biperf.core.service.publicrecognitionwall.dto.RecognitionEntry;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.publicrecognitionwall.PublicRecognitionWallBean;
import com.biperf.core.value.publicrecognitionwall.PublicRecognitionWallGiverBean;
import com.biperf.core.value.publicrecognitionwall.PublicRecognitionWallReceiverBean;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class PublicRecognitionWallServiceImpl implements PublicRecognitionWallService
{
  private SystemVariableService systemVariableService;
  private ParticipantService participantService;
  private UserService userService;

  @Override
  public PublicRecognitionWall getPublicRecognitionWall() throws ServiceErrorException
  {
    PublicRecognitionWall publicRecognitionWall = new PublicRecognitionWall();
    if ( isPublicRecognitionWallFeedEnabled() ) // it's enabled, process normall
    {
      PublicRecognitionWall cachedWall = getCachedWall( "global" );
      if ( cachedWall != null )
      {
        publicRecognitionWall = cachedWall;
      }
    }
    return publicRecognitionWall; // this will be populated if enabled, or empty if the system
                                  // variable is turned off.
  }

  @Override
  public PublicRecognitionWall refresh() throws ServiceErrorException
  {
    int wallPageCount = 50;
    PropertySetItem prop = systemVariableService.getPropertyByName( SystemVariableService.PUBLIC_RECOG_WALL_SP_PAGECOUNT );
    if ( prop != null )
    {
      wallPageCount = prop.getIntVal();
    }
    wallPageCount = wallPageCount >= 200 ? 200 : wallPageCount;

    List<PublicRecognitionWallBean> publicRecognitionWallBean = getPublicRecognitionWallByPageCount( wallPageCount );
    PublicRecognitionWall publicRecognitionWall = updateCacheObject( publicRecognitionWallBean );
    return publicRecognitionWall;
  }

  private PublicRecognitionWall updateCacheObject( List<PublicRecognitionWallBean> publicRecognitionWallBeanList )
  {
    PublicRecognitionWall publicRecognitionWall = new PublicRecognitionWall();
    Set<RecognitionEntry> recognitionEntrySet = new LinkedHashSet<RecognitionEntry>();

    for ( PublicRecognitionWallBean publicRecognitionWallBean : publicRecognitionWallBeanList )
    {
      RecognitionEntry recognitionEntry = new RecognitionEntry();
      if ( publicRecognitionWallBean.getPromotionType() != null && "purl".equalsIgnoreCase( publicRecognitionWallBean.getPromotionType() ) )
      {
        PublicRecognitionParticipant giver = new PublicRecognitionParticipant();
        recognitionEntry.setGiver( giver );
      }
      else
      {
        recognitionEntry.setGiver( buildGiver( publicRecognitionWallBean.getPublicRecognitionWallGiverBean() ) );
      }
      recognitionEntry.setReceivers( buildReceivers( publicRecognitionWallBean.getPublicRecognitionWallReceiverBean() ) );
      recognitionEntry.setRecognitionDate( publicRecognitionWallBean.getPublicRecognitionWallGiverBean().getDateTimeRecognition() );
      recognitionEntry.setComments( publicRecognitionWallBean.getComments() );
      recognitionEntry.setPromotionName( publicRecognitionWallBean.getPromotionName() );
      recognitionEntry.setPromotionType( publicRecognitionWallBean.getPromotionType() );
      if ( !"".equals( publicRecognitionWallBean.getBehavior() ) )
      {
        recognitionEntry.setBehavior( buildBehavior( publicRecognitionWallBean.getPromotionType(), publicRecognitionWallBean.getBehavior() ) );
      }

      recognitionEntry.seteCardUrl( publicRecognitionWallBean.geteCardLink() );
      recognitionEntrySet.add( recognitionEntry );
    }
    publicRecognitionWall.setRecognitions( recognitionEntrySet );
    publicRecognitionWall.setAsOfDate( new java.util.Date() );

    putCachedWall( "global", publicRecognitionWall );

    return publicRecognitionWall;
  }

  @Override
  public boolean isPublicRecognitionWallFeedEnabled()
  {
    PropertySetItem prop = systemVariableService.getPropertyByName( SystemVariableService.PUBLIC_RECOG_WALL_FEED_ENABLED );
    if ( prop == null )
    {
      return false;
    }
    return prop.getBooleanVal();
  }

  private PublicRecognitionParticipant buildGiver( PublicRecognitionWallGiverBean publicRecognitionWallGiverBean )
  {
    PublicRecognitionParticipant giver = new PublicRecognitionParticipant();

    giver.setFirstName( publicRecognitionWallGiverBean.getGiverFirstName() );
    giver.setLastName( publicRecognitionWallGiverBean.getGiverLastName() );
    giver.setAvatarUrl( publicRecognitionWallGiverBean.getGiverAvatarURL() );
    giver.setOrgUnit( publicRecognitionWallGiverBean.getGiverOrgUnit() );
    giver.setCountry( publicRecognitionWallGiverBean.getGiverCountry() );
    giver.setCountryFlagUrl( publicRecognitionWallGiverBean.getGiverCountryFlag() );

    Participant pax = participantService.getParticipantById( publicRecognitionWallGiverBean.getGiverUserId() );

    PickListValueBean pickListPositionValueBean = userService.getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                          pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                                                                          publicRecognitionWallGiverBean.getGiverJobTitle() );
    if ( pickListPositionValueBean != null )
    {
      giver.setJobTitle( pickListPositionValueBean.getName() );
    }

    PickListValueBean pickListDeptValueBean = userService.getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                      pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                                                                      publicRecognitionWallGiverBean.getGiverLocation() );
    if ( pickListDeptValueBean != null )
    {
      giver.setLocation( pickListDeptValueBean.getName() );
    }

    return giver;
  }

  private Set<PublicRecognitionParticipant> buildReceivers( List<PublicRecognitionWallReceiverBean> publicRecognitionWallReceiverBeanList )
  {
    Set<PublicRecognitionParticipant> receiversSet = new HashSet<PublicRecognitionParticipant>();

    for ( PublicRecognitionWallReceiverBean publicRecognitionWallReceiverBean : publicRecognitionWallReceiverBeanList )
    {
      PublicRecognitionParticipant reciever = new PublicRecognitionParticipant();
      reciever.setFirstName( publicRecognitionWallReceiverBean.getReceiverFirstName() );
      reciever.setLastName( publicRecognitionWallReceiverBean.getReceiverLastName() );
      reciever.setAvatarUrl( publicRecognitionWallReceiverBean.getReceiverAvatarURL() );
      reciever.setCountry( publicRecognitionWallReceiverBean.getReceiverCountry() );
      reciever.setCountryFlagUrl( publicRecognitionWallReceiverBean.getReceiverCountryFlag() );
      reciever.setOrgUnit( publicRecognitionWallReceiverBean.getReceiverOrgUnit() );

      Participant pax = participantService.getParticipantById( publicRecognitionWallReceiverBean.getReceiverUserId() );

      PickListValueBean pickListPositionValueBean = userService.getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                            pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                                                                            publicRecognitionWallReceiverBean.getReceiverJobTitle() );

      if ( pickListPositionValueBean != null )
      {
        reciever.setJobTitle( pickListPositionValueBean.getName() );
      }

      PickListValueBean pickListDeptValueBean = userService.getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                        pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                                                                        publicRecognitionWallReceiverBean.getReceiverLocation() );
      if ( pickListDeptValueBean != null )
      {
        reciever.setLocation( pickListDeptValueBean.getName() );
      }

      receiversSet.add( reciever );
    }

    return receiversSet;
  }

  private Cache getCache()
  {
    CacheManager manager = CacheManager.getCacheManager( getCacheManagementService().getCacheManagerName() );
    return manager.getCache( "recognitionWall" ); // TODO: inject this value
                                                  // from spring config, don't
                                                  // hard code
  }

  protected PublicRecognitionWall getCachedWall( String key )
  {
    Element element = getCache().get( key );
    if ( element != null )
    {
      return (PublicRecognitionWall)element.getObjectValue();
    }
    return null;
  }

  protected void putCachedWall( String key, PublicRecognitionWall wall )
  {
    getCache().put( new Element( key, wall ) );
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  @SuppressWarnings( "unchecked" )
  private Set<LocaleTranslation> buildBehavior( String promotionType, String behaviorCode )
  {
    Locale initialLocale = ContentReaderManager.getContentReader().getLocale();
    Set<LocaleTranslation> behaviorTranslations = new HashSet<LocaleTranslation>();

    List<LanguageType> supportLanguages = LanguageType.getList();
    try
    {
      for ( LanguageType type : supportLanguages )
      {
        Locale locale = CmsUtil.getLocale( type.getCode() );
        ContentReaderManager.getContentReader().setLocale( locale );

        if ( PromotionType.RECOGNITION.equals( promotionType ) )
        {
          PromoRecognitionBehaviorType promoRecognitionBehaviorType = PromoRecognitionBehaviorType.lookup( behaviorCode );
          behaviorTranslations.add( new LocaleTranslation( type.getCode(), promoRecognitionBehaviorType.getName() ) );
        }
        else if ( PromotionType.NOMINATION.equals( promotionType ) )
        {
          PromoNominationBehaviorType promoNominationBehaviorType = PromoNominationBehaviorType.lookup( behaviorCode );
          behaviorTranslations.add( new LocaleTranslation( type.getCode(), promoNominationBehaviorType.getName() ) );
        }
      }
    }
    finally
    {
      ContentReaderManager.getContentReader().setLocale( initialLocale );
    }
    return behaviorTranslations;
  }

  private CacheManagementService getCacheManagementService()
  {
    return (CacheManagementService)BeanLocator.getBean( CacheManagementService.BEAN_NAME );
  }

  protected PublicRecognitionWallDAO getPublicRecognitionWallDAO()
  {
    return (PublicRecognitionWallDAO)BeanLocator.getBean( PublicRecognitionWallDAO.BEAN_NAME );
  }

  private List<PublicRecognitionWallBean> getPublicRecognitionWallByPageCount( final int pageCount )
  {
    List<PublicRecognitionWallBean> publicRecognitionWallBeanList = getPublicRecognitionWallDAO().getPublicRecognitionWallByPageCount( pageCount );

    processPublicRecognitionWallBeanList( publicRecognitionWallBeanList );
    return publicRecognitionWallBeanList;
  }

  private void processPublicRecognitionWallBeanList( List<PublicRecognitionWallBean> publicRecognitionWallBeanList )
  {
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/assets/img/flags/";

    for ( PublicRecognitionWallBean publicRecognitionWallBean : publicRecognitionWallBeanList )
    {
      if ( publicRecognitionWallBean != null )
      {
        // Setting date&time of recognition > default is GMT
        publicRecognitionWallBean.getPublicRecognitionWallGiverBean().setDateTimeRecognition( publicRecognitionWallBean.getPublicRecognitionWallGiverBean().getDateApproved() );

        String giverFlagUrl = siteUrl + publicRecognitionWallBean.getPublicRecognitionWallGiverBean().getGiverCountry() + ".png";
        // Setting Country flag based on Country
        publicRecognitionWallBean.getPublicRecognitionWallGiverBean().setGiverCountryFlag( giverFlagUrl );
        // Setting Ecard Image link
        publicRecognitionWallBean.seteCardLink( getEcardImageUrlForFBPost( publicRecognitionWallBean ) );
        // Setting Ecard Video link
        publicRecognitionWallBean.seteCardVideoLink( "" );

        List<PublicRecognitionWallReceiverBean> publicRecognitionWallReceiverBeanList = publicRecognitionWallBean.getPublicRecognitionWallReceiverBean();
        if ( publicRecognitionWallReceiverBeanList != null )
        {
          for ( PublicRecognitionWallReceiverBean publicRecognitionWallReceiverBean : publicRecognitionWallReceiverBeanList )
          {
            if ( publicRecognitionWallReceiverBean != null )
            {
              // Setting Country flag based on Country
              publicRecognitionWallReceiverBean.setReceiverCountryFlag( siteUrl + publicRecognitionWallReceiverBean.getReceiverCountry() + ".png" );
            }
          }
        }
      }
    }
  }

  private String getEcardImageUrlForFBPost( PublicRecognitionWallBean bean )
  {
    String eCardImageUrl = null;
    if ( bean.geteCard() != null )
    {
      if ( bean.geteCard().getId() != null && bean.geteCard().getId() != 0L )
      {
        eCardImageUrl = bean.geteCard().getImgUrl();
      }
      else if ( bean.geteCard().getImgUrl() != null )
      {
        eCardImageUrl = bean.geteCard().getImgUrl();
      }
      else
      {
        eCardImageUrl = "";
      }
    }
    return eCardImageUrl;
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionECardForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.multimedia.EcardFormBean;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionECard;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.CertUtils;
import com.biperf.core.utils.MeshServicesUtil;
import com.biperf.core.value.ecard.ECardResponse;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionECardForm.
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
 * <td>sedey</td>
 * <td>Oct 05, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionECardForm extends BaseForm
{
  private Long promotionId;
  private String promotionName;
  private Long version;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;
  private String method;
  private boolean eCardActive;
  private boolean certificateActive;
  private boolean sendECardSelector;
  private boolean clientEmailSent;
  private boolean clientUser;
  private String cardClientEmailAddress;
  private List cardList;
  private List certificateList;
  private boolean supriseGift;
  private List ecardLocaleList;

  private boolean allowYourOwnCard = false;
  private boolean drawYourOwnCard = false;

  private boolean oneCertPerPromotion;
  private String paxDisplayOrder;
  
  //Client customization start
  private boolean allowMeme = false;
  private boolean allowSticker = false;
  private boolean allowUploadOwnMeme = false;
  //Client customization end

  // Transient flag variable fields for enabling / disabling controls
  private boolean cumulativeApproval;

  private List<EcardFormBean> localeList = new ArrayList<EcardFormBean>();

  private int localeListSize = 0;

  private static final String IMAGE_SERVICE_LARGE_IMG = "/images/size/432/432/";
  private static final String IMAGE_SERVICE_SMALL_IMG = "/images/size/144/144/";
  private static final String IMAGE_SERVICE_FLASH_IMG = "/images/size/864/864/";

  private static final Log logger = LogFactory.getLog( PromotionECardAction.class );

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // PromotionECardFormBeans. If this is not done, the form wont initialize
    // properly.
    cardList = getEmptyCardValueList( RequestUtils.getOptionalParamInt( request, "cardListCount" ), RequestUtils.getOptionalParamInt( request, "localeListCount" ) );
    certificateList = getEmptyCertificateValueList( RequestUtils.getOptionalParamInt( request, "certificateListCount" ) );
    // ecardLocaleList = getEmptyCardValueList( RequestUtils.getOptionalParamInt( request,
    // "ecardLocaleListCount" ) );
  }

  /**
   * resets the notificationList with empty PromotionNotificationForm beans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyCardValueList( int valueListCount, int localeListCount )
  {
    PromotionECardFormBean formBean = new PromotionECardFormBean();
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionECardFormBean
      PromotionECardFormBean promoECardBean = new PromotionECardFormBean();
      for ( int j = 0; j < localeListCount; j++ )
      {
        EcardFormBean cardBean = new EcardFormBean();
        promoECardBean.getLocaleListAsList().add( cardBean );
      }
      valueList.add( promoECardBean );
    }

    return valueList;
  }

  /**
   * resets the certificateList with empty PromotionCertificateForm beans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyCertificateValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionCertificateFormBean
      PromotionCertificateFormBean certificateFormBean = new PromotionCertificateFormBean();
      valueList.add( certificateFormBean );
    }

    return valueList;
  }

  /**
   * Load all the information needed for displaying the page
   * 
   * @param promotion - the looked up promotion object
   */
  public void load( AbstractRecognitionPromotion promotion, List allCardList, List allCertificateList, String cmsLocaleAvailabeinUrl, String requestBaseURI )
  {
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.version = promotion.getVersion();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.eCardActive = promotion.isCardActive();
    this.sendECardSelector = promotion.isCardClientSetupDone();
    // Since sendECardSelector is a field that may be disabled on the screen, clientEmailSent
    // is a hidden field on the form to preserve that the email has been sent, and should not
    // be sent again.
    this.clientEmailSent = promotion.isCardClientSetupDone();
    this.cardClientEmailAddress = promotion.getCardClientEmailAddress();
    this.supriseGift = promotion.isNoNotification();

    this.allowYourOwnCard = promotion.isAllowYourOwnCard();
    this.drawYourOwnCard = promotion.isDrawYourOwnCard();

    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nomPromo = (NominationPromotion)promotion;

      this.paxDisplayOrder = nomPromo.getPaxDisplayOrder();

      if ( this.paxDisplayOrder == null || "".equals( this.paxDisplayOrder ) )
      {
        this.paxDisplayOrder = AbstractRecognitionPromotion.PAX_STANDARDORRANDOM_DISP_ORDER;
      }

      this.certificateActive = nomPromo.isCertificateActive();
      this.oneCertPerPromotion = nomPromo.isOneCertPerPromotion();

      // Set the cumulative approval flag - so that one cert per promotion remains 'no' when
      // cumulative
      if ( nomPromo.isCumulative() )
      {
        this.setCumulativeApproval( true );
      }
      
      this.allowMeme = nomPromo.isAllowMeme();
      this.allowSticker = nomPromo.isAllowSticker();
      this.allowUploadOwnMeme = nomPromo.isAllowUploadOwnMeme();

    }
    else if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;

      this.paxDisplayOrder = recPromo.getPaxDisplayOrder();

      if ( this.paxDisplayOrder == null || "".equals( this.paxDisplayOrder ) )
      {
        this.paxDisplayOrder = AbstractRecognitionPromotion.PAX_STANDARDORRANDOM_DISP_ORDER;
      }
      this.allowMeme = recPromo.isAllowMeme();
      this.allowSticker = recPromo.isAllowSticker();
      this.allowUploadOwnMeme = recPromo.isAllowUploadOwnMeme();
    }

    // Load all the eCards, and certificates from the full list that is passed in
    // Later a comparison will be done to see which ones are selected for this promotion
    if ( allCardList != null && allCardList.size() > 0 )
    {
      int count = 0;
      Iterator cardListIterator = allCardList.iterator();
      while ( cardListIterator.hasNext() )
      {
        localeList = new ArrayList<EcardFormBean>();
        PromotionECardFormBean cardFormBean = new PromotionECardFormBean();
        ECardResponse cardResp = (ECardResponse)cardListIterator.next();
        if ( cardResp.getMigrateId() != 0.0 )
        {
          cardFormBean.setCardId( (long)cardResp.getMigrateId() );
        }
        else
        {
          cardFormBean.setCardId( (long)cardResp.getCardId() );
        }
        cardFormBean.setName( cardResp.getCardName() );
        cardFormBean.setCategoryName( cardResp.getCardTitle() );
        if ( cardResp.getImages().size() > 1 )
        {
          cardFormBean.setTranslatable( true );
        }
        else
        {
          cardFormBean.setTranslatable( false );
        }
        if ( StringUtils.equalsIgnoreCase( cardResp.getStatus(), "Y" ) )
        {
          cardFormBean.setActive( true );
        }
        else
        {
          cardFormBean.setActive( false );
        }
        for ( Object imageDet : cardResp.getImages() )
        {
          LinkedHashMap linkedHashMap = (LinkedHashMap)imageDet;
          EcardFormBean localeFormBean = null;
          if ( StringUtils.equals( (String)linkedHashMap.get( "default" ), "Y" ) )
          {
            String path = (String)linkedHashMap.get( "path" );
            if ( StringUtils.isNotEmpty( path ) )
            {
              try
              {
                localeFormBean = new EcardFormBean();
                URL aURL = new URL( path );
                String urlPath = aURL.getPath();
                String[] splitArr = urlPath.split( "/" );
                cardFormBean.setFlashName( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_FLASH_IMG + splitArr[splitArr.length - 1] );
                cardFormBean.setPreviewImageName( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_LARGE_IMG + splitArr[splitArr.length - 1] );
                cardFormBean.setImageName( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_SMALL_IMG + splitArr[splitArr.length - 1] );
                cardFormBean.setDisplayName( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_SMALL_IMG + splitArr[splitArr.length - 1] );
                cardFormBean.setLocale( (String)linkedHashMap.get( "locale" ) );
                localeFormBean.setLocale( (String)linkedHashMap.get( "locale" ) );
                localeFormBean.setCardId( (long)cardResp.getCardId() );
                localeFormBean.setDisplayName( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_SMALL_IMG + splitArr[splitArr.length - 1] );
                localeList.add( localeFormBean );
              }
              catch( MalformedURLException e )
              {
                // TODO Auto-generated catch block
                logger.error( e.getMessage(), e );
              }
            }

          }
          else if ( StringUtils.equals( (String)linkedHashMap.get( "default" ), "N" ) )
          {
            localeFormBean = new EcardFormBean();
            String path = (String)linkedHashMap.get( "path" );
            localeFormBean.setLocale( (String)linkedHashMap.get( "locale" ) );
            localeFormBean.setCardId( (long)cardResp.getCardId() );
            if ( StringUtils.isNotEmpty( path ) )
            {
              try
              {
                URL aURL = new URL( path );
                String urlPath = aURL.getPath();
                String[] splitArr = urlPath.split( "/" );
                localeFormBean.setDisplayName( MeshServicesUtil.getNackleMeshServicesBaseEndPoint() + IMAGE_SERVICE_SMALL_IMG + splitArr[splitArr.length - 1] );
                localeList.add( localeFormBean );
              }
              catch( MalformedURLException e )
              {
                logger.error( e.getMessage(), e );
              }
            }
          }
          // ecardLocaleList.add( localeList );
        }
        if ( localeList.size() > localeListSize )
        {
          localeListSize = localeList.size();
        }
        cardFormBean.setLocaleListAsList( localeList );
        cardList.add( cardFormBean );

      }
    }

    if ( allCertificateList != null && !allCertificateList.isEmpty() )
    {
      if ( promotion.isRecognitionPromotion() )
      {
        Iterator certificateListIterator = allCertificateList.iterator();
        while ( certificateListIterator.hasNext() )
        {
          PromotionCertificateFormBean certificateFormBean = new PromotionCertificateFormBean();
          Content certificate = (Content)certificateListIterator.next();
          certificateFormBean.setCertificateId( (String)certificate.getContentDataMap().get( "ID" ) );
          certificateFormBean.setName( (String)certificate.getContentDataMap().get( "NAME" ) );
          certificateFormBean.setImageName( (String)certificate.getContentDataMap().get( "THUMBNAIL_IMAGE" ) );
          certificateFormBean.setPreviewImageName( (String)certificate.getContentDataMap().get( "LARGE_IMAGE" ) );
          certificateList.add( certificateFormBean );
        }

      }
      else if ( promotion.isNominationPromotion() )
      {
        Iterator certificateListIterator = allCertificateList.iterator();
        while ( certificateListIterator.hasNext() )
        {
          PromotionCertificateFormBean certificateFormBean = new PromotionCertificateFormBean();
          Content certificate = (Content)certificateListIterator.next();
          certificateFormBean.setCertificateId( (String)certificate.getContentDataMap().get( "ID" ) );
          certificateFormBean.setName( (String)certificate.getContentDataMap().get( "NAME" ) );
          certificateFormBean.setImageName( CertUtils.getThumbnailCertificateUrl( certificate, requestBaseURI, promotion.getPromotionType() ) );
          certificateFormBean.setPreviewImageName( CertUtils.getFullCertificateUrl( Long.valueOf( certificateFormBean.getCertificateId() ), requestBaseURI, promotion.getPromotionType() ) );
          certificateList.add( certificateFormBean );
        }
      }
    }

    // check the promotion object to see if it has any eCards and certificates
    if ( promotion.getPromotionECard() != null && promotion.getPromotionECard().size() > 0 )
    {
      Iterator promoECardIterator = promotion.getPromotionECard().iterator();
      while ( promoECardIterator.hasNext() )
      {
        PromotionECard promoECard = (PromotionECard)promoECardIterator.next();
        Iterator cardListIterator = cardList.iterator();
        while ( cardListIterator.hasNext() )
        {
          PromotionECardFormBean cardFormBean = (PromotionECardFormBean)cardListIterator.next();
          if ( promoECard.geteCard().getId().equals( cardFormBean.getCardId() ) )
          {
            cardFormBean.setId( promoECard.getId() );
            cardFormBean.setSelected( true );

            if ( promotion.isAbstractRecognitionPromotion() )
            {
              AbstractRecognitionPromotion abstractRecPromotion = (AbstractRecognitionPromotion)promotion;
              if ( abstractRecPromotion.getPaxDisplayOrder() != null && abstractRecPromotion.getPaxDisplayOrder().equals( AbstractRecognitionPromotion.PAX_CUSTOM_DISPLAY_ORDER ) )
              {
                cardFormBean.setOrderNumber( promoECard.getOrderNumber() );
              }
            }
            promoECardIterator.remove();
          }
        }
      }
    }
    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
      if ( recognitionPromotion.getPromotionCertificates() != null && !recognitionPromotion.getPromotionCertificates().isEmpty() )
      {
        Iterator promoCertificateIterator = recognitionPromotion.getPromotionCertificates().iterator();
        while ( promoCertificateIterator.hasNext() )
        {
          PromotionCert promoCert = (PromotionCert)promoCertificateIterator.next();
          Iterator certificateIterator = certificateList.iterator();
          while ( certificateIterator.hasNext() )
          {
            PromotionCertificateFormBean certificateFormBean = (PromotionCertificateFormBean)certificateIterator.next();
            if ( StringUtils.equals( promoCert.getCertificateId(), certificateFormBean.getCertificateId() ) )
            {
              certificateFormBean.setId( promoCert.getId() );
              certificateFormBean.setSelected( true );

              if ( recognitionPromotion.getPaxDisplayOrder() != null && recognitionPromotion.getPaxDisplayOrder().equals( AbstractRecognitionPromotion.PAX_CUSTOM_DISPLAY_ORDER ) )
              {
                certificateFormBean.setOrderNumber( promoCert.getOrderNumber() );
              }
            }
          }
        }
      }
    }
    // Loading certificates for a nomination promotion
    else if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      if ( nominationPromotion.getPromotionCertificates() != null && !nominationPromotion.getPromotionCertificates().isEmpty() )
      {
        Iterator<PromotionCert> promoCertificateIterator = nominationPromotion.getPromotionCertificates().iterator();
        while ( promoCertificateIterator.hasNext() )
        {
          PromotionCert promoCert = promoCertificateIterator.next();
          Iterator certificateIterator = certificateList.iterator();
          while ( certificateIterator.hasNext() )
          {
            PromotionCertificateFormBean certificateFormBean = (PromotionCertificateFormBean)certificateIterator.next();
            if ( StringUtils.equals( promoCert.getCertificateId(), certificateFormBean.getCertificateId() ) )
            {
              certificateFormBean.setId( promoCert.getId() );
              certificateFormBean.setSelected( true );

              if ( nominationPromotion.getPaxDisplayOrder() != null && nominationPromotion.getPaxDisplayOrder().equals( AbstractRecognitionPromotion.PAX_CUSTOM_DISPLAY_ORDER ) )
              {
                certificateFormBean.setOrderNumber( promoCert.getOrderNumber() );
              }
            }
          }
        }
      }
    }

  }

  private boolean isCardMappedtoNomination( Long cardId )
  {
    // TODO Auto-generated method stub
    return getNominationService().isCardMapped( cardId );
  }

  private boolean isCardMappedtoRecognition( Long cardId )
  {
    return getClaimService().isCardMapped( cardId );
  }

  /**
   * Build a detached Promotion domain object to be persisted
   * 
   * @param isRecognition
   * @return Promotion
   */
  public Promotion toDomainObject( boolean isRecognition )
  {

    AbstractRecognitionPromotion promotion;

    if ( isRecognition )
    {
      promotion = new RecognitionPromotion();
    }
    else
    {
      promotion = new NominationPromotion();
      ( (NominationPromotion)promotion ).setCertificateActive( isCertificateActive() );
      ( (NominationPromotion)promotion ).setOneCertPerPromotion( isOneCertPerPromotion() );
    }

    promotion.setId( promotionId );
    promotion.setName( getPromotionName() );
    promotion.setVersion( getVersion() );
    promotion.setCardActive( isECardActive() );
    promotion.setCardClientSetupDone( isSendECardSelector() );
    promotion.setCardClientEmailAddress( getCardClientEmailAddress() );
    promotion.setPaxDisplayOrder( getPaxDisplayOrder() );
    promotion.setAllowYourOwnCard( isAllowYourOwnCard() );
    promotion.setDrawYourOwnCard( isDrawYourOwnCard() );

    if ( getCardAsList() != null && getCardAsList().size() > 0 )
    {
      Iterator cardListIterator = getCardAsList().iterator();
      while ( cardListIterator.hasNext() )
      {
        PromotionECardFormBean cardFormBean = (PromotionECardFormBean)cardListIterator.next();
        if ( cardFormBean != null && cardFormBean.isSelected() )
        {
          ECard eCard = new ECard();
          eCard.setId( cardFormBean.getCardId() );
          eCard.setName( cardFormBean.getName() );

          PromotionECard promoEcard = new PromotionECard();
          if ( cardFormBean != null )
          {
            if ( cardFormBean.getId() != null )
            {
              if ( cardFormBean.getId().longValue() != 0 )
              {
                promoEcard.setId( cardFormBean.getId() );
              }
            }
          }
          promoEcard.seteCard( eCard );

          if ( this.paxDisplayOrder != null && this.paxDisplayOrder.equals( AbstractRecognitionPromotion.PAX_CUSTOM_DISPLAY_ORDER ) )
          {
            promoEcard.setOrderNumber( cardFormBean.getOrderNumber() );
            promotion.addPromotionEcard( eCard, promoEcard.getOrderNumber() );
          }
          else
          {
            promotion.addECard( promoEcard );
          }
        }
      }
    }

    if ( isRecognition && getCertificateList() != null && !getCertificateList().isEmpty() )
    {
      Iterator certificateIterator = getCertificateList().iterator();
      while ( certificateIterator.hasNext() )
      {
        PromotionCertificateFormBean certificateFormBean = (PromotionCertificateFormBean)certificateIterator.next();
        if ( certificateFormBean != null && certificateFormBean.isSelected() )
        {
          PromotionCert promotionCert = new PromotionCert();
          if ( certificateFormBean != null )
          {
            if ( certificateFormBean.getId() != null )
            {
              if ( certificateFormBean.getId().longValue() != 0 )
              {
                promotionCert.setId( certificateFormBean.getId() );

              }
            }
          }
          promotionCert.setCertificateId( certificateFormBean.getCertificateId() );

          if ( this.paxDisplayOrder != null && this.paxDisplayOrder.equals( AbstractRecognitionPromotion.PAX_CUSTOM_DISPLAY_ORDER ) )
          {
            promotionCert.setOrderNumber( certificateFormBean.getOrderNumber() );
            ( (RecognitionPromotion)promotion ).addPromotionCertificate( promotionCert.getCertificateId(), promotionCert.getOrderNumber() );
          }
          else
          {
            ( (RecognitionPromotion)promotion ).addCertificate( promotionCert );
          }
        }
      }
    }
    // Certificates to domain object for nomination promotion
    else if ( !isRecognition && getCertificateList() != null && !getCertificateList().isEmpty() )
    {
      Iterator certificateIterator = getCertificateList().iterator();
      while ( certificateIterator.hasNext() )
      {
        PromotionCertificateFormBean certificateFormBean = (PromotionCertificateFormBean)certificateIterator.next();
        if ( certificateFormBean != null && certificateFormBean.isSelected() )
        {
          PromotionCert promotionCert = new PromotionCert();
          if ( certificateFormBean != null )
          {
            if ( certificateFormBean.getId() != null )
            {
              if ( certificateFormBean.getId().longValue() != 0 )
              {
                promotionCert.setId( certificateFormBean.getId() );
              }
            }
          }
          promotionCert.setCertificateId( certificateFormBean.getCertificateId() );
          if ( this.paxDisplayOrder != null && this.paxDisplayOrder.equals( AbstractRecognitionPromotion.PAX_CUSTOM_DISPLAY_ORDER ) )
          {
            promotionCert.setOrderNumber( certificateFormBean.getOrderNumber() );
            ( (NominationPromotion)promotion ).addPromotionCertificate( promotionCert.getCertificateId(), promotionCert.getOrderNumber() );
          }
          else
          {
            ( (NominationPromotion)promotion ).addCertificate( promotionCert );
          }
        }
      }
    }
    
    //client customization start
    if(isRecognition )
    {
      ( (RecognitionPromotion)promotion ).setAllowMeme( allowMeme );
      ( (RecognitionPromotion)promotion ).setAllowSticker( allowSticker );
      ( (RecognitionPromotion)promotion ).setAllowUploadOwnMeme( allowUploadOwnMeme );
    }
    else if(isNominationPromotion())
    {
      ( (NominationPromotion)promotion ).setAllowMeme( allowMeme );
      ( (NominationPromotion)promotion ).setAllowSticker( allowSticker );
      ( (NominationPromotion)promotion ).setAllowUploadOwnMeme( allowUploadOwnMeme );
    }

    return promotion;
  }

  /**
   * Validate the form before submitting Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    // Get promotion object - will need to cast further for each section of validation
    Promotion promotion = toDomainObject( PromotionType.RECOGNITION.equals( promotionTypeCode ) );

    if ( PromotionType.RECOGNITION.equals( promotionTypeCode ) )
    {
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;

      if ( eCardActive && !isSendECardSelector() )
      {
        if ( ( recPromo.getPromotionECard() == null || recPromo.getPromotionECard().size() == 0 )
            && ( recPromo.getPromotionCertificates() == null || recPromo.getPromotionCertificates().size() == 0 ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.ecards.errors.NO_ECARDS_OR_CERTIFICATES" ) );
        }
      }
    }
    else if ( PromotionType.NOMINATION.equals( promotionTypeCode ) )
    {
      NominationPromotion nomPromo = (NominationPromotion)promotion;

      // If ecards are active, must select at least one ecard
      if ( eCardActive && ( nomPromo.getPromotionECard() == null || nomPromo.getPromotionECard().size() == 0 ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.ecards.errors.NO_ECARDS" ) );
      }

      // Break down certificates - there's another quantity modifier on it
      if ( certificateActive )
      {
        // If the "one per promotion" option is chosen, must choose exactly one certificate
        if ( oneCertPerPromotion && ( nomPromo.getPromotionCertificates() == null || nomPromo.getPromotionCertificates().size() != 1 ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.ecards.errors.ONE_CERTIFICATE" ) );
        }

        // If "one per promotion" is not chosen, then just must be at least greater than one
        if ( !oneCertPerPromotion && ( nomPromo.getPromotionCertificates() == null || nomPromo.getPromotionCertificates().size() == 0 ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.ecards.errors.NO_CERTIFICATES" ) );
        }
      }
      if ( !certificateActive )
      {
        if ( nomPromo.getPromotionCertificates() != null && nomPromo.getPromotionCertificates().size() > 0 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.ecards.errors.CERTIFICATE_NOT_ACTIVE" ) );
        }
      }
    }

    if ( PromotionType.NOMINATION.equals( promotionTypeCode ) || PromotionType.RECOGNITION.equals( promotionTypeCode ) )
    {
      if ( this.paxDisplayOrder != null && this.paxDisplayOrder.equals( AbstractRecognitionPromotion.PAX_CUSTOM_DISPLAY_ORDER ) )
      {
        int selectedListSize = 0;
        Set<String> ecardOrderSet = new HashSet<String>();
        List<String> duplicates = new ArrayList<String>();
        List<Integer> order = new ArrayList<Integer>();
        if ( getCardAsList() != null && getCardAsList().size() > 0 )
        {
          Iterator cardListIterator = getCardAsList().iterator();
          while ( cardListIterator.hasNext() )
          {
            PromotionECardFormBean cardFormBean = (PromotionECardFormBean)cardListIterator.next();
            if ( cardFormBean != null && cardFormBean.isSelected() )
            {
              if ( cardFormBean.getOrderNumber() == null || cardFormBean.getOrderNumber().isEmpty() )
              {
                errors.add( "PromotionECardForm", new ActionMessage( "promotion.ecards.errors.ECARD_ORDER_EMPTY" ) );
                break;
              }
              if ( cardFormBean.getOrderNumber() != null )
              {
                selectedListSize++;

                try
                {
                  if ( Integer.parseInt( cardFormBean.getOrderNumber() ) <= 0 )
                  {
                    errors.add( "PromotionECardForm", new ActionMessage( "promotion.ecards.errors.ECARD_ORDER_RANGE" ) );
                    break;
                  }
                  order.add( Integer.parseInt( cardFormBean.getOrderNumber() ) );
                }
                catch( NumberFormatException e )
                {
                  errors.add( "PromotionECardForm", new ActionMessage( "promotion.ecards.errors.ORDER_INVALID" ) );
                  break;
                }

              }
              if ( !ecardOrderSet.add( cardFormBean.getOrderNumber() ) )
              {
                duplicates.add( cardFormBean.getOrderNumber() );
              }
            }
          }
        }

        if ( !oneCertPerPromotion && getCertificateList() != null && getCertificateList().size() > 0 )
        {
          Iterator certificateListIterator = getCertificateList().iterator();
          while ( certificateListIterator.hasNext() )
          {
            PromotionCertificateFormBean certificateFormBean = (PromotionCertificateFormBean)certificateListIterator.next();
            if ( certificateFormBean != null && certificateFormBean.isSelected() )
            {
              if ( certificateFormBean.getOrderNumber() == null || certificateFormBean.getOrderNumber().isEmpty() )
              {
                errors.add( "PromotionECardForm", new ActionMessage( "promotion.ecards.errors.CERT_ORDER_EMPTY" ) );
                break;
              }
              if ( certificateFormBean.getOrderNumber() != null )
              {
                selectedListSize++;

                try
                {
                  if ( Integer.parseInt( certificateFormBean.getOrderNumber() ) <= 0 )
                  {
                    errors.add( "PromotionECardForm", new ActionMessage( "promotion.ecards.errors.CERT_ORDER_RANGE" ) );
                    break;
                  }
                  order.add( Integer.parseInt( certificateFormBean.getOrderNumber() ) );
                }
                catch( NumberFormatException e )
                {
                  errors.add( "PromotionECardForm", new ActionMessage( "promotion.ecards.errors.CERT_ORDER_INVALID" ) );
                  break;
                }

              }
              if ( !ecardOrderSet.add( certificateFormBean.getOrderNumber() ) )
              {
                duplicates.add( certificateFormBean.getOrderNumber() );
              }
            }
          }
        }
        if ( duplicates.size() > 0 )
        {
          String duplicateValues = StringUtils.join( duplicates, "," );
          String message = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "promotion.ecards.errors.DUPLICATE_ORDER_NUMBER" ), new Object[] { duplicateValues } );
          errors.add( "PromotionECardForm", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_EMPTY, message ) );
        }

        if ( order.size() > 0 )
        {
          Integer maxOrder = Collections.max( order );
          if ( maxOrder != selectedListSize )
          {
            errors.add( "PromotionECardForm", new ActionMessage( "promotion.ecards.errors.SORT_ORDER_INVALID" ) );
          }
        }
      }
    }

    return errors;
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

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
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

  public boolean isECardActive()
  {
    return eCardActive;
  }

  public void setECardActive( boolean cardActive )
  {
    eCardActive = cardActive;
  }

  public boolean isCertificateActive()
  {
    return certificateActive;
  }

  public void setCertificateActive( boolean certificateActive )
  {
    this.certificateActive = certificateActive;
  }

  public boolean isSendECardSelector()
  {
    return sendECardSelector;
  }

  public void setSendECardSelector( boolean sendECardSelector )
  {
    this.sendECardSelector = sendECardSelector;
  }

  public String getCardClientEmailAddress()
  {
    return cardClientEmailAddress;
  }

  public void setCardClientEmailAddress( String cardClientEmailAddress )
  {
    this.cardClientEmailAddress = cardClientEmailAddress;
  }

  /**
   * @return List of PromotionECardFormBean objects
   */
  public List getCardAsList()
  {
    return cardList;
  }

  public void setCardAsList( List cardList )
  {
    this.cardList = cardList;
  }

  /**
   * Accessor for the number of PromotionECardFormBean objects in the list.
   * 
   * @return int
   */
  public int getCardListCount()
  {
    if ( cardList == null )
    {
      return 0;
    }

    return cardList.size();
  }

  public List getCertificateList()
  {
    return certificateList;
  }

  public void setCertificateList( List certificateList )
  {
    this.certificateList = certificateList;
  }

  /**
   * Accessor for the number of PromotionCertificateFormBean objects in the list.
   * 
   * @return int
   */
  public int getCertificateListCount()
  {
    if ( certificateList == null )
    {
      return 0;
    }

    return certificateList.size();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionECardFormBean from the value list
   */
  public PromotionECardFormBean getCardList( int index )
  {
    try
    {
      PromotionECardFormBean bean = (PromotionECardFormBean)cardList.get( index );
      List<EcardFormBean> beanList = bean.getLocaleListAsList();
      return (PromotionECardFormBean)cardList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public PromotionECardFormBean getEcardLocaleList( int index )
  {
    try
    {
      return (PromotionECardFormBean)ecardLocaleList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public boolean isClientUser()
  {
    return clientUser;
  }

  public void setClientUser( boolean clientUser )
  {
    this.clientUser = clientUser;
  }

  public boolean isClientEmailSent()
  {
    return clientEmailSent;
  }

  public void setClientEmailSent( boolean clientEmailSent )
  {
    this.clientEmailSent = clientEmailSent;
  }

  public boolean isNominationPromotion()
  {
    return promotionTypeCode.equals( PromotionType.NOMINATION );
  }

  public boolean isRecognitionPromotion()
  {
    return promotionTypeCode.equals( PromotionType.RECOGNITION );
  }

  public boolean isSupriseGift()
  {
    return supriseGift;
  }

  public void setSupriseGift( boolean supriseGift )
  {
    this.supriseGift = supriseGift;
  }

  public void setAllowYourOwnCard( boolean allowYourOwnCard )
  {
    this.allowYourOwnCard = allowYourOwnCard;
  }

  public boolean isAllowYourOwnCard()
  {
    return allowYourOwnCard;
  }

  public void setDrawYourOwnCard( boolean drawYourOwnCard )
  {
    this.drawYourOwnCard = drawYourOwnCard;
  }

  public boolean isDrawYourOwnCard()
  {
    return drawYourOwnCard;
  }

  public boolean isOneCertPerPromotion()
  {
    return oneCertPerPromotion;
  }

  public void setOneCertPerPromotion( boolean oneCertPerPromotion )
  {
    this.oneCertPerPromotion = oneCertPerPromotion;
  }

  public String getPaxDisplayOrder()
  {
    return paxDisplayOrder;
  }

  public void setPaxDisplayOrder( String paxDisplayOrder )
  {
    this.paxDisplayOrder = paxDisplayOrder;
  }

  public boolean isCumulativeApproval()
  {
    return cumulativeApproval;
  }

  public void setCumulativeApproval( boolean cumulativeApproval )
  {
    this.cumulativeApproval = cumulativeApproval;
  }

  public int getEcardLocaleListCount()
  {
    if ( ecardLocaleList == null )
    {
      return 0;
    }

    return ecardLocaleList.size();

  }

  public int getLocaleListCount()
  {
    return this.localeListSize;
  }

  public List getEcardLocaleAsList()
  {
    return ecardLocaleList;
  }

  public void setEcardLocaleAsList( List ecardLocaleList )
  {
    this.ecardLocaleList = ecardLocaleList;
  }

  private static NominationClaimService getNominationService()
  {
    return (NominationClaimService)BeanLocator.getBean( NominationClaimService.BEAN_NAME );
  }

  private static ClaimService getClaimService()
  {
    return (ClaimService)BeanLocator.getBean( ClaimService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  public List<EcardFormBean> getLocaleList()
  {
    return localeList;
  }

  public void setLocaleList( List<EcardFormBean> localeList )
  {
    this.localeList = localeList;
  }

  public int getLocaleListSize()
  {
    return localeListSize;
  }

  public void setLocaleListSize( int localeListSize )
  {
    this.localeListSize = localeListSize;
  }
  
  public boolean isAllowMeme()
  {
    return allowMeme;
  }

  public void setAllowMeme( boolean allowMeme )
  {
    this.allowMeme = allowMeme;
  }

  public boolean isAllowSticker()
  {
    return allowSticker;
  }

  public void setAllowSticker( boolean allowSticker )
  {
    this.allowSticker = allowSticker;
  }

  public boolean isAllowUploadOwnMeme()
  {
    return allowUploadOwnMeme;
  }

  public void setAllowUploadOwnMeme( boolean allowUploadOwnMeme )
  {
    this.allowUploadOwnMeme = allowUploadOwnMeme;
  }
}

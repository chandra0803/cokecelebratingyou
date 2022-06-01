/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/merchorder/impl/MerchOrderServiceImpl.java,v $
 */

package com.biperf.core.service.merchorder.impl;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.awardbanq.delegate.AwardbanqDelegateException;
import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.awardslinqDataRetriever.client.MerchLevelProduct;
import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.dao.merchandise.hibernate.MerchOrderActivityQueryConstraint;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.merchandise.ActivityMerchOrder;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.PlateauRedemptionTracking;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.awardbanq.GiftCodes;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.awardbanq.OMRemoteDelegate;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ProxyUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;

public class MerchOrderServiceImpl implements MerchOrderService
{
  private static final Log logger = LogFactory.getLog( MerchOrderServiceImpl.class );

  private String orderXmlBinder;
  private MerchLevelService merchLevelService;
  private SystemVariableService systemVariableService;
  private MailingService mailingService;
  private MerchOrderDAO merchOrderDAO;
  private AwardBanQServiceFactory awardBanQServiceFactory;
  private ClaimService claimService;
  private ActivityDAO activityDAO;
  private JournalService journalService;

  public static final String DEFAULT_CYBERSOURCE_EMAIL_ADDRESS = "null@cybersource.com";

  public List getMerchOrderList( MerchOrderActivityQueryConstraint constraint )
  {
    return merchOrderDAO.getMerchOrderList( constraint );
  }

  public List getUnredeemedMerchOrdersAndUpdateStatus( MerchOrderActivityQueryConstraint constraint ) throws ServiceErrorException
  {
    int merchOrderCount = 0;
    // get all for the criteria
    List merchOrders = getMerchOrderList( constraint );
    merchOrderCount = merchOrders.size();
    List unredeemedGiftCodes = new ArrayList();

    // since the status may have changed in OM, check each one before adding to the list.
    // if the status is now 'redeemed', update the G3 status
    for ( int i = 0; i < merchOrderCount; i++ )
    {
      MerchOrder order = (MerchOrder)merchOrders.get( i );
      try
      {
        order = updateOrderStatus( order );
      }
      catch( ServiceErrorException see )
      {
        /*
         * If one of the giftcode is invalid, continue updating other giftcodes Log that giftcode
         * and exception details Continue processing if even any OM error happens
         */
        if ( see.getServiceErrors().size() == 1 && ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey() != null
            && ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey().equals( ServiceErrorMessageKeys.RECOGNITION_OM_INVALID_ORDER_ERROR ) )
        {
          logger.error( "Exception while updating the Giftcode status for Giftcode : " + order.getFullGiftCode(), see );
          order.setOrderStatus( "invalid" );
          merchOrderDAO.saveMerchOrder( order );
          continue;
        }
        else if ( see.getServiceErrors().size() == 1 && ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey() != null
            && ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey().equals( ServiceErrorMessageKeys.RECOGNITION_OM_PAX_SERVICE_ERROR ) )
        {
          logger.error( "Exception while updating the Giftcode status for Giftcode : " + order.getFullGiftCode(), see );
          continue;
        }
        else if ( see.getServiceErrors().size() == 1 && ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey() != null
            && ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey().equals( ServiceErrorMessageKeys.RECOGNITION_OM_ORDER_DEFAULT_ERROR ) )
        {
          logger.error( "Exception while updating the Giftcode status for Giftcode : " + order.getFullGiftCode(), see );
          continue;
        }
        else
        {
          throw see;
        }
      }
      if ( !order.isRedeemed() )
      {
        unredeemedGiftCodes.add( order );
      }
    }

    return unredeemedGiftCodes;
  }

  @SuppressWarnings( "rawtypes" )
  public MerchOrder updateOrderStatus( MerchOrder order ) throws ServiceErrorException
  {
    try
    {
      String programId = null;
      if ( order != null && order.getPromoMerchProgramLevel() != null )
      {
        programId = order.getPromoMerchProgramLevel().getProgramId();
      }
      else if ( order != null && order.getActivityMerchOrders() != null )
      {
        Set activityMerchOrders = order.getActivityMerchOrders();
        for ( Iterator iter = activityMerchOrders.iterator(); iter.hasNext(); )
        {
          ActivityMerchOrder activityMerchOrder = (ActivityMerchOrder)iter.next();
          Promotion promotion = activityMerchOrder.getActivity().getPromotion();
          programId = promotion instanceof GoalQuestPromotion ? ( (GoalQuestPromotion)promotion ).getProgramId() : null;
          break;
        }
      }

      GiftcodeStatusResponseValueObject status = null;
      if ( order != null && order.getFullGiftCode() != null )
      {
        String giftCode = order.getFullGiftCode();
        status = merchLevelService.getGiftCodeStatusWebService( giftCode, programId, order.getOrderNumber(), order.getReferenceNumber() );
      }
      else
      {
        logger.error( "Giftcode:" + order.getFullGiftCode() + "ProgramId:" + programId + "OrderNumber:" + order.getOrderNumber() + "ReferenceNumber:" + order.getReferenceNumber() );
      }

      if ( status != null && status.getBalanceAvailable() == 0 )
      {
        order.setRedeemed( true );
        merchOrderDAO.saveMerchOrder( order );
      }
      if ( order.getExpirationDate() != null )
      {
        Date today = new Date();
        if ( today.after( DateUtils.toStartDate( order.getExpirationDate() ) ) )
        {
          order.setOrderStatus( "expired" );
          merchOrderDAO.saveMerchOrder( order );
        }
      }

      if ( status != null && status.getErrCode() != 0 )
      {
        order.setOrderStatus( "invalid" );
        merchOrderDAO.saveMerchOrder( order );
      }
    }
    catch( ServiceErrorException see )
    {
      /*
       * If one of the giftcode is invalid, continue updating other giftcodes Log that giftcode and
       * exception details Continue processing if even any OM error happens
       */
      if ( see.getServiceErrors().size() == 1 && ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey() != null
          && ( (ServiceError)see.getServiceErrors().get( 0 ) ).getKey().equals( ServiceErrorMessageKeys.RECOGNITION_OM_INVALID_ORDER_ERROR ) )
      {
        logger.error( "Exception while updating the Giftcode status for Giftcode : " + order.getFullGiftCode(), see );
        order.setOrderStatus( "invalid" );
        merchOrderDAO.saveMerchOrder( order );
        throw see;
      }
      else
      {
        throw see;
      }
    }

    return order;
  }

  public List getMerchAwardReminders( Long participantId )
  {
    return merchOrderDAO.getMerchAwardReminders( participantId );
  }

  public MerchOrder getMerchOrderByGiftCode( String giftCode )
  {
    return merchOrderDAO.getMerchOrderByGiftCode( giftCode );
  }

  public MerchOrder getMerchOrderByReferenceNumber( String referenceNumber )
  {
    return merchOrderDAO.getMerchOrderByReferenceNumber( referenceNumber );
  }

  public String getOnlineShoppingUrl( Promotion promotion, Long merchOrderId, Country country, Participant participant, boolean doubleEncode )
  {
    PromoMerchCountry promoMerchCountry = promotion.getPromoMerchCountryForCountryCode( country.getCountryCode() );
    MerchOrder merchOrder = merchOrderDAO.getMerchOrderById( merchOrderId );

    Map parms = new HashMap();
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();

    if ( promotion != null && promotion.isRecognitionPromotion() )
    {
      parms.put( "conversionFlag", Boolean.valueOf( ( (RecognitionPromotion)promotion ).isApqConversion() ) );
    }
    if ( participant != null )
    {
      parms.put( "accountNum", participant.getAwardBanqNumber() );
      parms.put( "centraxId", participant.getCentraxId() );
      parms.put( "locale", participant.getLanguageType() != null ? participant.getLanguageType().getCode() : localeCode );
    }
    if ( promoMerchCountry != null )
    {
      parms.put( "campaignId", promoMerchCountry.getCountry().getCampaignNbr() );
      parms.put( "campaignPassword", promoMerchCountry.getCountry().getCampaignPassword() );
      parms.put( "promoMerchCountryId", promoMerchCountry.getId() );
    }
    if ( merchOrder != null )
    {
      parms.put( "giftCode", merchOrder.getFullGiftCode() );
      parms.put( "productSetId", merchOrder.getProductId() );
    }
    parms.put( "isPax", "true" );

    String url = ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                       "/merchLevelShopping.do?method=displayThanqOnline" + "&jsessionid=" + UserManager.getUser().getSessionId() + ":"
                                                           + UserManager.getUser().getRouteId() + "&sessionId=" + UserManager.getUser().getSessionId(),
                                                       parms,
                                                       doubleEncode );

    return url;
  }

  public String getOnlineShoppingProductDetailUrl( Promotion promotion, Long merchOrderId, Country country, Participant participant, boolean doubleEncode, String productSetId, String catalogId )
  {
    PromoMerchCountry promoMerchCountry = promotion.getPromoMerchCountryForCountryCode( country.getCountryCode() );
    MerchOrder merchOrder = merchOrderDAO.getMerchOrderById( merchOrderId );

    Map parms = new HashMap();
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();

    if ( promotion != null && promotion.isRecognitionPromotion() )
    {
      parms.put( "conversionFlag", Boolean.valueOf( ( (RecognitionPromotion)promotion ).isApqConversion() ) );
    }
    if ( participant != null )
    {
      parms.put( "accountNum", participant.getAwardBanqNumber() );
      parms.put( "centraxId", participant.getCentraxId() );
      parms.put( "locale", participant.getLanguageType() != null ? participant.getLanguageType().getCode() : localeCode );
    }
    if ( promoMerchCountry != null )
    {
      parms.put( "campaignId", promoMerchCountry.getCountry().getCampaignNbr() );
      parms.put( "campaignPassword", promoMerchCountry.getCountry().getCampaignPassword() );
      parms.put( "promoMerchCountryId", promoMerchCountry.getId() );
    }
    if ( merchOrder != null )
    {
      parms.put( "giftCode", merchOrder.getFullGiftCode() );
      parms.put( "productSetId", merchOrder.getProductId() );
    }
    parms.put( "isPax", "true" );

    parms.put( "pageName", "productDetail" );
    parms.put( "productSetId", productSetId );
    parms.put( "catalogId", catalogId );

    String url = ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                       "/merchLevelShopping.do?method=displayThanqOnline" + "&jsessionid=" + UserManager.getUser().getSessionId() + ":"
                                                           + UserManager.getUser().getRouteId() + "&sessionId=" + UserManager.getUser().getSessionId(),
                                                       parms,
                                                       doubleEncode );

    return url;
  }

  public void replaceGiftCodeAndSendEmail( Long merchOrderId, String programId, String giftCode, String emailAddress, String message ) throws ServiceErrorException
  {
    GiftcodeStatusResponseValueObject newGiftCode = refundGiftCodeWebService( giftCode, programId );

    // update merch order
    MerchOrder merchOrder = merchOrderDAO.getMerchOrderById( merchOrderId );
    merchOrder.setFullGiftCode( newGiftCode.getGiftCode() );
    // merchOrder.setReferenceNumber( newGiftCode.getReferenceNumber() );
    merchOrder.setRedeemed( false ); // Fix 24307
    SimpleDateFormat sdf = new SimpleDateFormat( OMRemoteDelegate.OM_DATE_FORMAT );
    /*
     * try { merchOrder.setExpirationDate( sdf.parse( newGiftCode.getExpireDate() )); } catch
     * (ParseException pe) { logger.warn( "Invalid expire date returned for merchOrderId: " +
     * merchOrderId + " : " + newGiftCode.getExpireDate() ); merchOrder.setExpirationDate( null ); }
     */
    merchOrderDAO.saveMerchOrder( merchOrder );

    // send email
    Mailing mailing = getMailingService().buildMerchOrderGiftCodeRefundMailing( merchOrder, emailAddress, message );
    mailingService.submitMailing( mailing, null );
  }

  public MerchOrder getMerchOrderByClaimIdUserId( Long userId, Long claimId )
  {
    MerchOrder merchOrder = null;
    // Get merch order for claim and recipient
    List activities = activityDAO.getMerchOrderActivitiesByClaimAndUserId( claimId, userId );

    // If list is empty, it might be a point award
    if ( activities.isEmpty() )
    {
      return null;
    }

    // Get the first merch order and return the giftcode(There should be ONLY one merch order)
    MerchOrderActivity activity = (MerchOrderActivity)activities.get( 0 );

    if ( activity.getMerchOrder() != null )
    {
      merchOrder = merchOrderDAO.getMerchOrderById( activity.getMerchOrder().getId() );
    }
    return merchOrder;
  }

  private GiftcodeStatusResponseValueObject refundGiftCodeWebService( String giftCode, String programId ) throws ServiceErrorException
  {
    GiftcodeStatusResponseValueObject newGiftCode = null;

    try
    {
      newGiftCode = getAwardBanQServiceFactory().getAwardBanQService().refundGiftCodeWebService( programId, giftCode );
    }
    catch( Exception e )
    {
      boolean omMIA = false;
      Throwable t = e.getCause();
      if ( t instanceof AwardbanqDelegateException )
      {
        AwardbanqDelegateException ade = (AwardbanqDelegateException)t;
        Exception parent = ade.getParentException();
        omMIA = parent instanceof RemoteException || parent instanceof CommunicationException || parent instanceof NamingException;
      }

      if ( omMIA )
      {
        logger.error( "refundGiftCode failed because OM is MIA (Missing In Action)", e );
        throw new ServiceErrorExceptionWithRollback( ServiceErrorMessageKeys.OM_NOT_AVAILABLE, e );
      }
      else
      {
        logger.warn( "refundGiftCode failed because of OM veto", e );
        throw new ServiceErrorExceptionWithRollback( ServiceErrorMessageKeys.OM_NO_REPLACEMENT_GIFTCODE, e );
      }
    }
    return newGiftCode;
  }

  public List<Long> getGiftCodeFailures( Long promotionId )
  {
    return merchOrderDAO.getGiftCodeFailures( promotionId );
  }

  public MerchOrder processGiftCodes( MerchOrder merchOrder, List<GiftCodes> giftCodes, boolean isLevel, DepositProcessBean depositProcessBean, MerchLevel omLevel )
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "Giftcode list generated for merch order id " + merchOrder.getId() );
    }
    if ( !CollectionUtils.isEmpty( giftCodes ) )
    {
      GiftCodes giftCode = (GiftCodes)giftCodes.get( 0 );
      if ( giftCode == null )
      {
        logger.error( "giftCode is null from retrieveGiftCodesForMerchandiseOrTravel for merch order id " + merchOrder.getId() );
      }
      else if ( logger.isDebugEnabled() )
      {
        logger.debug( "giftCode is : " + giftCode.getGiftCode() );
      }

      if ( isLevel )
      {
        merchOrder.setFullGiftCode( giftCode.getGiftCode() );
        if ( null != giftCode.getExpirationDate() )
        {
          merchOrder.setExpirationDate( getExpirationDate( giftCode.getExpirationDate() ) );
        }
        merchOrder.setReferenceNumber( giftCode.getReferenceNumber() );
        merchOrder.setMerchGiftCodeType( MerchGiftCodeType.lookup( MerchGiftCodeType.LEVEL ) );
      }
      if ( !isLevel && depositProcessBean != null && omLevel != null )
      {
        merchOrder.setProductId( depositProcessBean.getProductId() );
        merchOrder.setProductDescription( this.getProductDescription( omLevel, depositProcessBean.getProductId() ) );
        merchOrder.setMerchGiftCodeType( MerchGiftCodeType.lookup( MerchGiftCodeType.PRODUCT ) );
      }

      // Bug 73088 merchOrder.setBatchId( merchOrderService.getNextBatchId() );
      this.saveMerchOrder( merchOrder );
      // To change the status to 'POST' for merch order
      Activity activity = activityDAO.getActivityForMerchOrderId( merchOrder.getId() );
      if ( activity != null )
      {
        Set<ActivityJournal> journals = activity.getActivityJournals();
        for ( Iterator iter1 = journals.iterator(); iter1.hasNext(); )
        {
          ActivityJournal journalObj = (ActivityJournal)iter1.next();
          Journal journal = journalObj.getJournal();
          journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
          journalService.saveJournalEntry( journal );
        }
      }
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Successfully saved merch order " + merchOrder.getId() );
      }
    }
    return merchOrder;
  }

  private Date getExpirationDate( String expirationDateStr )
  {
    Date expirationDate = null;
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );// date format returned from
                                                                  // webservice
      expirationDate = expirationDateStr != null && expirationDateStr.length() > 0 ? sdf.parse( expirationDateStr ) : null;
    }
    catch( ParseException e )
    {
      expirationDate = null;
    }
    return expirationDate;
  }

  public Mailing buildMerchRecognitionMailing( Long claimId )
  {
    logger.debug( "Processing merch claimId " + claimId );

    RecognitionClaim recognitionClaim = getMerchRecognitionClaim( claimId );
    Mailing mailing = mailingService.buildRecognitionMailing( recognitionClaim, null );

    return mailing;
  }

  public Mailing buildMerchCelebrationMailing( Long claimId )
  {
    logger.debug( "Processing merch claimId " + claimId );

    RecognitionClaim recognitionClaim = getMerchRecognitionClaim( claimId );
    Mailing mailing = mailingService.buildRecognitionCelebrationMailing( recognitionClaim, null, false );

    return mailing;
  }

  private RecognitionClaim getMerchRecognitionClaim( Long claimId )
  {
    Claim claim = claimService.getClaimById( claimId );
    // Insure we don't have proxied version
    claim = (Claim)ProxyUtil.deproxy( claim );
    RecognitionClaim recognitionClaim = (RecognitionClaim)claim;

    return recognitionClaim;
  }

  public String getProductDescription( MerchLevel omLevel, String productId )
  {
    String description = "";

    Iterator iter = omLevel.getProducts().iterator();
    while ( iter.hasNext() )
    {
      MerchLevelProduct product = (MerchLevelProduct)iter.next();
      String levelProductId = (String)product.getProductIds().iterator().next();
      if ( null != levelProductId && levelProductId.equals( productId ) )
      {
        return product.getProductGroupDescriptions().get( UserManager.getLocale() ).getDescription();
      }
    }

    return description;
  }

  public JournalService getJournalService()
  {
    return journalService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public ActivityDAO getActivityDAO()
  {
    return activityDAO;
  }

  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

  public MerchOrderDAO getMerchOrderDAO()
  {
    return merchOrderDAO;
  }

  public void setMerchOrderDAO( MerchOrderDAO merchOrderDAO )
  {
    this.merchOrderDAO = merchOrderDAO;
  }

  public MerchLevelService getMerchLevelService()
  {
    return merchLevelService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public String getOrderXmlBinder()
  {
    return orderXmlBinder;
  }

  public void setOrderXmlBinder( String orderXmlBinder )
  {
    this.orderXmlBinder = orderXmlBinder;
  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public ClaimService getClaimService()
  {
    return claimService;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public MerchOrder getMerchOrderById( Long merchOrderId )
  {
    return merchOrderDAO.getMerchOrderById( merchOrderId );
  }

  // Alerts Performance Tuning
  public MerchOrder getMerchOrderByIdWithProjections( Long merchOrderId, ProjectionCollection collection )
  {
    return merchOrderDAO.getMerchOrderByIdWithProjections( merchOrderId, collection );
  }

  public MerchOrder saveMerchOrder( MerchOrder merchOrder )
  {
    return merchOrderDAO.saveMerchOrder( merchOrder );
  }

  public PlateauRedemptionTracking savePlateauRedemptionTracking( PlateauRedemptionTracking plateauRedemptionTracking )
  {
    return merchOrderDAO.savePlateauRedemptionTracking( plateauRedemptionTracking );
  }

  public Long getNextBatchId()
  {
    return merchOrderDAO.getNextBatchId();
  }

  @Override
  public Long getPromotionIdByMerchOrderId( Long merchOrderId )
  {

    return merchOrderDAO.getPromotionIdByMerchOrderId( merchOrderId );
  }

  public List<Long> getMerchOrderIds()
  {
    return merchOrderDAO.getMerchOrderIds();
  }

  public List getMerchOrdersList( Long merchOrderId )
  {
    return merchOrderDAO.getMerchOrdersList( merchOrderId );
  }
  
  //Client customizations for wip #23129 starts
  public List getAllUnredeemedOrdersByPromotion( Long promoId, String mmyyyy )
  {
     return merchOrderDAO.getAllUnredeemedOrdersByPromotion( promoId, mmyyyy );
  }
   
  public MerchOrder updateOrderBillingCodeAndRedeem( Long merchOrderId, String billingCode )
  {
     MerchOrder order = merchOrderDAO.getMerchOrderById( merchOrderId );
     order.setBillingCode( billingCode );
     order.setRedeemed( true );
     merchOrderDAO.saveMerchOrder( order );
     return order;
   }
   // Client customizations for wip #23129 ends

}

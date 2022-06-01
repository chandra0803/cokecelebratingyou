/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/shopping/impl/ShoppingServiceImpl.java,v $
 */

package com.biperf.core.service.shopping.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ExternalSupplierValue;
import com.biperf.core.value.ShoppingValueBean;
import com.biperf.web.singlesignon.SingleSignOnRequest;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ShoppingServiceImpl.
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
 * <td>robinsra</td>
 * <td>Sep 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ShoppingServiceImpl implements ShoppingService
{

  private static final Log log = LogFactory.getLog( ShoppingServiceImpl.class );

  private ParticipantService participantService;
  private SystemVariableService systemVariableService;
  private AwardBanQServiceFactory awardBanQServiceFactory;
  private SupplierService supplierService;
  private PromoMerchCountryService promoMerchCountryService;
  private MerchOrderService merchOrderService;
  private UserService userService;
  private CountryService countryService;

  private String inactivatedUserUrl;
  private String multipleSupplierUrl;
  private String externalSupplierUrl;

  private static final String PAYROLL_EXTRACT = "Payroll Extract";

  /**
   * gets the shopping type for a user Overridden from
   * 
   * @see com.biperf.core.service.shopping.ShoppingService#checkShoppingType(java.lang.Long)
   * @param userId
   * @return String with the shopping type for the user
   */
  public String checkShoppingType( Long userId )
  {

    String shoppingType = ShoppingService.NONE;

    // ----------------------------------
    // Bug # 36681 start
    // ----------------------------------
    // Check if the shopping link should even be displayed or not
    /*
     * if ( !systemVariableService.getPropertyByName( SystemVariableService.SHOPPING_LINK_USED )
     * .getBooleanVal() ) { return ShoppingService.NONE; }
     */
    // ----------------------------------
    // Bug # 36681 end
    // ----------------------------------
    try
    {
      // Find the participant
      Participant participant = participantService.getParticipantById( userId );
      if ( participant == null )
      {
        return ShoppingService.NONE;
      }

      // Find the primary address
      Address address = participant.getPrimaryAddress().getAddress();
      if ( address == null )
      {
        return ShoppingService.NONE;
      }

      // Check the Supplier Name and Supplier Type
      if ( address.getCountry().getPrimarySupplier().getSupplierName().equals( PAYROLL_EXTRACT ) )
      {
        shoppingType = ShoppingService.PAYROLL_EXTRACT;
      }
      else
      {
        String supplierType = address.getCountry().getPrimarySupplier().getSupplierType();
        if ( supplierType.equals( Supplier.INTERNAL ) )
        {
          shoppingType = ShoppingService.INTERNAL;
        }
        else if ( supplierType.equals( Supplier.EXTERNAL ) )
        {
          shoppingType = ShoppingService.EXTERNAL;
        }
      }
    }
    catch( Exception e )
    {
      return ShoppingService.NONE;
    }

    return shoppingType;
  }

  /*
   * Delegates and Launched-as users are not allowed access to the Catalog
   */
  private void validateUserAccess() throws ServiceErrorException
  {
    if ( UserManager.isUserDelegateOrLaunchedAs() )
    {
      throw new ServiceErrorException( "Access Denied" );
    }
  }

  /**
   * Setup shopping information for Internal suppliers, including enrolling in awardbanq if needed
   * Overridden from
   * 
   * @see com.biperf.core.service.shopping.ShoppingService#setupInternalShopping(java.lang.Long)
   * @param userId
   * @return ShoppingValueBean
   * @throws ServiceErrorException
   */
  public ShoppingValueBean setupInternalShopping( Long userId ) throws ServiceErrorException
  {
    validateUserAccess();

    List serviceErrors = new ArrayList();
    ShoppingValueBean shoppingValue = new ShoppingValueBean();

    // --------------------
    // Find the participant
    // --------------------
    Participant participant = participantService.getParticipantById( userId );
    if ( participant == null )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SHOPPING_INVALID_PARTICIPANT );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors );
    }

    // ----------------------------
    // Awardbanq Enrollment check
    // ----------------------------
    if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
    {
      // need to enroll into awardbanq
      try
      {
        participant = awardBanQServiceFactory.getAwardBanQService().enrollParticipantInAwardBanQWebService( participant );
      }
      catch( JsonGenerationException e )
      {
        log.error( e.getMessage(), e );
        throw new ServiceErrorException( e.getMessage() );
      }
      catch( JsonMappingException e )
      {
        log.error( e.getMessage(), e );
        throw new ServiceErrorException( e.getMessage() );
      }
      catch( IOException e )
      {
        log.error( e.getMessage(), e );
        throw new ServiceErrorException( e.getMessage() );
      }
      catch( Exception e )
      {
        log.error( e.getMessage(), e );
        throw new ServiceErrorException( e.getMessage() );
      }
    }
    
    boolean isBiiSupplier = checkCurrentUserSupplierIsBii( participant.getPrimaryAddress().getAddress().getCountry().getCountryCode() );
    
    shoppingValue.setFirstName( participant.getFirstName() );
    shoppingValue.setLastName( participant.getLastName() );
    shoppingValue.setAccount( participant.getAwardBanqNumber() );

    // get data off of country
    shoppingValue.setProgramId( participant.getPrimaryAddress().getAddress().getCountry().getProgramId() );
    shoppingValue.setProgramPassword( participant.getPrimaryAddress().getAddress().getCountry().getProgramPassword() );

    // get data out of system variables
    shoppingValue.setRemoteURL( getRemoteURL() );
    shoppingValue.setProxy( Environment.getProxy() );
    shoppingValue.setProxyPort( Environment.getProxyPort() );
    if ( participant.getLanguageType() != null )
    {
      shoppingValue.setLanguagePreference( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }

    // -------------------------------
    // Verify that everything is setup
    // -------------------------------
    if ( shoppingValue.getProgramId() == null || shoppingValue.getProgramId().equals( "" ) )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SHOPPING_PROGRAM_ID_SETUP_ERROR );
      serviceErrors.add( error );
    }
    if ( !isBiiSupplier )
    {
      if ( shoppingValue.getProgramPassword() == null || shoppingValue.getProgramPassword().equals( "" ) )
      {
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.SHOPPING_PROGRAM_PASSWORD_SETUP_ERROR );
        serviceErrors.add( error );
      }
    }
    if ( shoppingValue.getRemoteURL() == null || shoppingValue.getRemoteURL().equals( "" ) )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SHOPPING_REMOTE_URL_SETUP_ERROR );
      serviceErrors.add( error );
    }
    /*
     * if ( shoppingValue.getProxy() == null || shoppingValue.getProxy().equals( "" ) ) {
     * ServiceError error = new ServiceError( ServiceErrorMessageKeys.SHOPPING_PROXY_SETUP_ERROR );
     * serviceErrors.add( error ); }
     */

    if ( !serviceErrors.isEmpty() )
    {
      throw new ServiceErrorException( serviceErrors );
    }

    return shoppingValue;
  }

  /**
   * Setup shopping information for Internal suppliers, including enrolling in awardbanq if needed
   * Overridden from
   * 
   * @see com.biperf.core.service.shopping.ShoppingService#setupInternalShopping(java.lang.Long)
   * @param userId
   * @return ExternalSupplierValue
   * @throws ServiceErrorException
   */
  public ExternalSupplierValue setupExternalSupplier( Long userId, String externalSupplierId ) throws ServiceErrorException
  {
    validateUserAccess();

    List serviceErrors = new ArrayList();
    ExternalSupplierValue externalSupplierValue = new ExternalSupplierValue();

    // --------------------
    // Find the participant
    // --------------------
    Participant participant = participantService.getParticipantById( userId );
    if ( participant == null )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SHOPPING_INVALID_PARTICIPANT );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors );
    }

    // ----------------------------
    // Awardbanq Enrollment check
    // ----------------------------
    if ( participant.getAwardBanqNumber() == null || participant.getAwardBanqNumber().equals( "" ) )
    {
      // need to enroll into awardbanq
      try
      {
        try
        {
          participant = awardBanQServiceFactory.getAwardBanQService().enrollParticipantInAwardBanQWebService( participant );
        }
        catch( JsonGenerationException e )
        {
          log.error( e.getMessage(), e );
          throw new ServiceErrorException( e.getMessage() );
        }
        catch( JsonMappingException e )
        {
          log.error( e.getMessage(), e );
          throw new ServiceErrorException( e.getMessage() );
        }
        catch( IOException e )
        {
          log.error( e.getMessage(), e );
          throw new ServiceErrorException( e.getMessage() );
        }
        catch( Exception e )
        {
          log.error( e.getMessage(), e );
          throw new ServiceErrorException( e.getMessage() );
        }
      }
      catch( ServiceErrorException e )
      {
        // TODO Send an email to let admin know enrollment failed for some reason.
        log.error( "enrollment to bank failed in setupExternal Supplier", e );
      }
    }

    Country country = participant.getPrimaryAddress().getAddress().getCountry();

    Supplier supplier = null;

    // if request comes from multiple supplier awards page then use externalSupplier Id param
    if ( externalSupplierId != null )
    {
      supplier = supplierService.getSupplierById( new Long( externalSupplierId ) );
    }
    else
    {
      supplier = country.getPrimarySupplier();
    }

    if ( Objects.nonNull( supplier ) )
    {
      if ( StringUtils.isEmpty( participant.getAwardBanqNumber() ) )
      {
        externalSupplierValue.setBankAccountNumber( "*" );
      }
      else
      {
        externalSupplierValue.setBankAccountNumber( participant.getAwardBanqNumber() );
      }

      externalSupplierValue.setTargetPageId( supplier.getCatalogTargetId() );
      externalSupplierValue.setActionURL( supplier.getCatalogUrl() );
      externalSupplierValue.setStatementActionURL( supplier.getStatementUrl() );
      externalSupplierValue.setStatementTargetPageId( supplier.getStatementTargetId() );
    }
    Locale[] locales = Locale.getAvailableLocales();
    Map<String, Locale> COUNTRY_TO_LOCALE_MAP = new HashMap<String, Locale>();
    for ( Locale locale : locales )
    {
      COUNTRY_TO_LOCALE_MAP.put( locale.getLanguage(), locale );
    }
    Locale userLocale = COUNTRY_TO_LOCALE_MAP.get( UserManager.getUser().getPrimaryCountryCode() );
    externalSupplierValue.setLanguageCode( userLocale != null ? userLocale.getLanguage() : "" );
    externalSupplierValue.setCountryCode( country.getCountryCode() );

    // -------------------------------
    // Verify that everything is setup
    // -------------------------------
    if ( externalSupplierValue.getActionURL() == null || externalSupplierValue.getActionURL().equals( "" ) )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.EXTERNAL_SUPPLIER_CATALOG_URL_ERROR );
      serviceErrors.add( error );
    }

    if ( !serviceErrors.isEmpty() )
    {
      throw new ServiceErrorException( serviceErrors );
    }
    return externalSupplierValue;
  }

  /**
   * Get the Remote URL for the current environment (dev, qa, preprod, prod)
   * 
   * @return String
   */
  private String getRemoteURL()
  {
    StringBuffer systemVariableName = new StringBuffer();

    systemVariableName.append( SystemVariableService.SHOPPING_INTERNAL_REMOTE_URL_PREFIX );
    systemVariableName.append( "." );
    systemVariableName.append( Environment.getEnvironment() );

    String remoteURL = systemVariableService.getPropertyByName( systemVariableName.toString() ).getStringVal();

    return remoteURL;
  }

  /**
   * Get the Post Login URL for the current environment (dev, qa, preprod, prod)
   * 
   * @return String
   */
  private String getPostLoginURL()
  {
    String postLoginURL = "";

    return postLoginURL;
  }

  public Map<String, String> getShopUrlMapping( Long userId, PromotionAwardsType awardType, String baseURI ) throws ServiceErrorException
  {
    Map<String, String> shopUrlMap = new HashMap<String, String>();

    String promotionAwardType = awardType.getCode();

    if ( PromotionAwardsType.POINTS.equals( promotionAwardType ) )
    {
      String shoppingType = checkShoppingType( userId );

      if ( shoppingType.equals( ShoppingService.INTERNAL ) )
      {
        String ssoUrl = getRemoteURL();
        shopUrlMap.put( "sso", ssoUrl );
      }
      else if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
      {
        Map paramMap = new HashMap();
        paramMap.put( "externalSupplierId", supplierService.getSupplierByName( Supplier.BII ).getId() );
        String externalUrl = ClientStateUtils.generateEncodedLink( baseURI, "/externalSupplier.do?method=displayExternal", paramMap );
        shopUrlMap.put( "redirect", externalUrl );
      }
    }
    else
    // merch type promotion
    {
      String ssoUrl = getMerchlinqSsoURL();
      shopUrlMap.put( "sso", ssoUrl );
    }

    return shopUrlMap;
  }

  public SingleSignOnRequest loadLevelLabels( SingleSignOnRequest signOnRequest, Long promoMerchCountryId )
  {

    PromoMerchCountry promoMerchCountry = null;
    if ( promoMerchCountryId != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
      promoMerchCountry = promoMerchCountryService.getPromoMerchCountryByIdWithAssociations( promoMerchCountryId, associationRequestCollection );
    }

    if ( promoMerchCountry != null && promoMerchCountry.getLevels() != null )
    {
      for ( Iterator levelIter = promoMerchCountry.getLevels().iterator(); levelIter.hasNext(); )
      {
        PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelIter.next();
        signOnRequest.setParameter( "levelLabel_" + promoMerchProgramLevel.getLevelName(), CmsResourceBundle.getCmsBundle().getString( promoMerchProgramLevel.getCmAssetKey() + ".LEVEL_NAME" ) );
      }
    }

    return signOnRequest;
  }

  public SingleSignOnRequest loadParticipantAddressInfo( SingleSignOnRequest signOnRequest, Participant pax )
  {
    if ( null != pax )
    {
      AssociationRequestCollection userAssociations = new AssociationRequestCollection();
      userAssociations.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ALL ) );

      pax = participantService.getParticipantByIdWithAssociations( pax.getId(), userAssociations );
      signOnRequest.setParameter( "firstName", pax.getFirstName() );
      signOnRequest.setParameter( "lastName", pax.getLastName() );

      // pass billing code
      if ( pax.getUserCharacteristics() != null && systemVariableService.getPropertyByName( SystemVariableService.MERCHANDISE_BILLING_CODE_CHAR ).getStringVal() != null )
      {
        String billingName = "";
        String promotionCodes = "";
        Set userCharacteristics = pax.getUserCharacteristics();
        Iterator itr = userCharacteristics.iterator();
        while ( itr.hasNext() )
        {
          UserCharacteristic userChar = (UserCharacteristic)itr.next();
          billingName = userChar.getUserCharacteristicType().getCharacteristicName();
          if ( billingName.equalsIgnoreCase( systemVariableService.getPropertyByName( SystemVariableService.MERCHANDISE_BILLING_CODE_CHAR ).getStringVal() ) )
          {
            if ( userChar.getCharacteristicValue() != null )
            {
              signOnRequest.setParameter( "promotionCodes", userChar.getCharacteristicValue() );
            }
            break;
          }
        }

      }

      if ( pax.getPrimaryAddress() != null )
      {
        signOnRequest.setParameter( "accountNum", pax.getAwardBanqNumber() );
        signOnRequest.setParameter( "centraxId", pax.getCentraxId() );
        signOnRequest.setParameter( "omPaxId", pax.getCentraxId() );
        if ( null != pax.getLanguageType() )
        {
          signOnRequest.setParameter( "locale", pax.getLanguageType().getCode() );
        }

        Country country = null;
        if ( pax.getPrimaryAddress().getAddress() != null )
        {
          country = pax.getPrimaryAddress().getAddress().getCountry();
        }

        if ( country != null )
        {
          signOnRequest.setParameter( "campaignId", country.getCampaignNbr() );
          signOnRequest.setParameter( "campaignPassword", country.getCampaignPassword() );
        }

        UserAddress userAddress = pax.getPrimaryAddress();
        Address address = userAddress.getAddress();

        if ( address != null )
        {
          signOnRequest.setParameter( "addressLine1", address.getAddr1() );
          signOnRequest.setParameter( "addressLine2", address.getAddr2() );
          signOnRequest.setParameter( "addressLine3", address.getAddr3() );
          signOnRequest.setParameter( "city", address.getCity() );

          if ( address.getStateType() != null )
          {
            signOnRequest.setParameter( "stateCode", address.getStateType().getAbbr() );
          }

          signOnRequest.setParameter( "zipCode", address.getPostalCode() );

          if ( address.getCountry() != null )
          {
            signOnRequest.setParameter( "country", address.getCountry().getAwardbanqAbbrev() );
          }

          if ( null != pax.getPrimaryEmailAddress() )
          {
            signOnRequest.setParameter( "emailAddress", pax.getPrimaryEmailAddress().getEmailAddr() );
          }

          if ( null != pax.getPrimaryPhone() )
          {
            signOnRequest.setParameter( "daytimePhone", pax.getPrimaryPhone().getPhoneNbr() );
          }
        }
      }
    }
    return signOnRequest;
  }

  @Override
  public String getShoppingUrlForInactiveUser( Long userId )
  {
    String shoppingType = ShoppingService.NONE;
    boolean multiplieSupplier = false;

    // if country has more than one supplier or one supplier and has display travel award true then
    // takes to multiple supplier awards page.
    UserAddress userAddress = getUserService().getPrimaryUserAddress( userId );
    if ( userAddress != null )
    {
      Address address = userAddress.getAddress();
      if ( address != null )
      {
        if ( address.getCountry().getCountrySuppliers() != null && address.getCountry().getCountrySuppliers().size() > 1
            || address.getCountry().getCountrySuppliers() != null && address.getCountry().getCountrySuppliers().size() == 1 && address.getCountry().getDisplayTravelAward() )
        {
          multiplieSupplier = true;
        }
        else
        {
          shoppingType = checkShoppingType( userId );
        }
      }
    }

    if ( multiplieSupplier )
    {
      return multipleSupplierUrl;
    }
    else if ( shoppingType.equals( ShoppingService.INTERNAL ) )
    {
      return inactivatedUserUrl;
    }
    else if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
    {
      return externalSupplierUrl;
    }
    return inactivatedUserUrl;
  }

  private String getMerchlinqSsoURL()
  {
    StringBuffer systemVariableName = new StringBuffer();
    systemVariableName.append( SystemVariableService.GOALQUEST_MERCHLINQ_SSO_URL_PREFIX );
    systemVariableName.append( "." );
    systemVariableName.append( Environment.getEnvironment() );
    return systemVariableService.getPropertyByName( systemVariableName.toString() ).getStringVal();
  }

  private boolean checkCurrentUserSupplierIsBii( String countryCode )
  {
    return countryService.checkUserSupplier( countryCode, Supplier.BII );
  }
  
  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setSupplierService( SupplierService supplierService )
  {
    this.supplierService = supplierService;
  }

  public void setPromoMerchCountryService( PromoMerchCountryService promoMerchCountryService )
  {
    this.promoMerchCountryService = promoMerchCountryService;
  }

  public MerchOrderService getMerchOrderService()
  {
    return merchOrderService;
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setInactivatedUserUrl( String inactivatedUserUrl )
  {
    this.inactivatedUserUrl = inactivatedUserUrl;
  }

  public void setMultipleSupplierUrl( String multipleSupplierUrl )
  {
    this.multipleSupplierUrl = multipleSupplierUrl;
  }

  public void setExternalSupplierUrl( String externalSupplierUrl )
  {
    this.externalSupplierUrl = externalSupplierUrl;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

}

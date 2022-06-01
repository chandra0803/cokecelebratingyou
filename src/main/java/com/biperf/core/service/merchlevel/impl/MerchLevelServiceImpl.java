/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/merchlevel/impl/MerchLevelServiceImpl.java,v $
 */

package com.biperf.core.service.merchlevel.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.awardslinqDataRetriever.client.MerchLevelProduct;
import com.biperf.awardslinqDataRetriever.client.MerchlinqLevelData;
import com.biperf.awardslinqDataRetriever.client.ProductGroupDescription;
import com.biperf.awardslinqDataRetriever.util.AwardslinqDataRetrieverException;
import com.biperf.cache.Cache;
import com.biperf.cache.CacheFactory;
import com.biperf.core.dao.merchandise.PromoMerchProgramLevelDAO;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.PromoMerchProgramLevelGiftCodes;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.DataRetrieverDelegate;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.awardbanq.MerchLevelValueObjectComparator;
import com.biperf.core.service.awardbanq.OMRemoteDelegate;
import com.biperf.core.service.awardbanq.impl.MerchLevelProductValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.awardbanq.impl.ProductEntryVO;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.ejb.participant.OMParticipantServicesException;
import com.biperf.om.delegate.OMDelegateException;
import com.biperf.util.log.Logger;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * MerchLevelService interfaces with Awardlinq for maintenance of Merchlinq Level information
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
 * <td>babu</td>
 * <td>Jul 12, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class MerchLevelServiceImpl implements MerchLevelService
{
  private PromoMerchProgramLevelDAO promoMerchProgramLevelDAO;
  private SystemVariableService systemVariableService;

  private Cache spotlightProductDataCache;
  private Cache merchLevelDataCache;

  private static final String OM_ORDER_PROGRAM_NOT_FOUND_1_ERROR = "-16";
  private static final String OM_ORDER_PROGRAM_NOT_FOUND_2_ERROR = "-17";
  private static final String OM_ORDER_PROGRAM_MUST_BE_UNIQUE_ERROR = "-20";
  private static final String OM_ORDER_INVALID_GIFT_CODE_ERROR = "-47";
  private static final String OM_ORDER_INVALID_ORDER_NUMBER_1_ERROR = "-50";
  private static final String OM_ORDER_INVALID_ORDER_NUMBER_2_ERROR = "-51";
  private static final String OM_ORDER_INVALID_GIFT_CODE_XREF_1_ERROR = "-70";
  private static final String OM_ORDER_INVALID_GIFT_CODE_XREF_2_ERROR = "-71";

  public void setCacheFactory( CacheFactory cacheFactory )
  {
    spotlightProductDataCache = cacheFactory.getCache( "spotlightProductData" );
    merchLevelDataCache = cacheFactory.getCache( "merchLevelData" );
  }

  public void clearPropertyFromCache()
  {
    if ( merchLevelDataCache != null )
    {
      merchLevelDataCache.clear();
    }
    if ( spotlightProductDataCache != null )
    {
      spotlightProductDataCache.clear();
    }
  }

  @SuppressWarnings( "unused" )
  public GiftcodeStatusResponseValueObject getGiftCodeStatusWebService( String giftCode, String programId, String orderNumber, String refNumber ) throws ServiceErrorException
  {
    List<ServiceError> serviceErrors = new ArrayList<ServiceError>();
    GiftcodeStatusResponseValueObject status = null;
    try
    {
      status = OMRemoteDelegate.getInstance().getGiftCodeInfoWebService( giftCode, programId, orderNumber, refNumber );
      return status;
    }
    catch( OMParticipantServicesException opse )
    {
      if ( status == null )
      {
        serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.RECOGNITION_OM_INVALID_ORDER_ERROR, opse.getParentException().getMessage() ) );
        throw new ServiceErrorException( serviceErrors, opse );
      }
      else if ( status != null && status.getErrCode() != 0 )
      {
        throw createServiceErrorExceptionForPaxServiceInvalidOrderException( status.getErrCode(), opse );
      }
      else
      {
        String message = opse.getParentException().getMessage();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.RECOGNITION_OM_PAX_SERVICE_ERROR, message );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors, opse );
      }
    }
    catch( OMDelegateException adre )
    {
      String message = adre.getMessage();
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.RECOGNITION_OM_ORDER_DEFAULT_ERROR, message );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors, adre );
    }
  }

  private ServiceErrorException createServiceErrorExceptionForPaxServiceInvalidOrderException( int returnCode, OMParticipantServicesException opse )
  {
    List<ServiceError> serviceErrors = new ArrayList<ServiceError>();

    String message = opse.getParentException().getMessage();
    if ( OM_ORDER_PROGRAM_NOT_FOUND_1_ERROR.equals( Integer.toString( returnCode ) ) || OM_ORDER_PROGRAM_NOT_FOUND_2_ERROR.equals( Integer.toString( returnCode ) )
        || OM_ORDER_PROGRAM_MUST_BE_UNIQUE_ERROR.equals( Integer.toString( returnCode ) ) || OM_ORDER_INVALID_GIFT_CODE_ERROR.equals( Integer.toString( returnCode ) )
        || OM_ORDER_INVALID_ORDER_NUMBER_1_ERROR.equals( Integer.toString( returnCode ) ) || OM_ORDER_INVALID_ORDER_NUMBER_2_ERROR.equals( Integer.toString( returnCode ) )
        || OM_ORDER_INVALID_GIFT_CODE_XREF_1_ERROR.equals( Integer.toString( returnCode ) ) || OM_ORDER_INVALID_GIFT_CODE_XREF_2_ERROR.equals( Integer.toString( returnCode ) ) )
    {
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.RECOGNITION_OM_INVALID_ORDER_ERROR, message ) );
    }
    else
    {
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.RECOGNITION_OM_PAX_SERVICE_ERROR ) );
    }

    return new ServiceErrorException( serviceErrors, opse );
  }

  private String getDestEnvironment()
  {
    StringBuffer sysVarName = new StringBuffer();
    sysVarName.append( SystemVariableService.GOALQUEST_MERCHLINQ_DEST_ENV_PREFIX );
    // sysVarName.append( "." );
    // sysVarName.append( Environment.getEnvironment() );
    return systemVariableService.getPropertyByNameAndEnvironment( sysVarName.toString() ).getStringVal();
  }

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, String environmentId ) throws ServiceErrorException
  {
    List serviceErrors = new ArrayList();
    try
    {
      AwardBanqMerchResponseValueObject data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, false, false, environmentId );
      return data;
    }
    catch( AwardslinqDataRetrieverException adre )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SPOTLIGHT_MERCHLEVEL_DATARETRIEVER_ERROR );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors, adre );
    }
  }

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId ) throws ServiceErrorException
  {
    List serviceErrors = new ArrayList();
    try
    {
      AwardBanqMerchResponseValueObject data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId );
      String environmentId = getDestEnvironment();
      if ( StringUtils.isNotBlank( environmentId ) )
      {
        data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, environmentId );
      }
      else
      {
        data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId );
      }
      return data;
    }
    catch( AwardslinqDataRetrieverException adre )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SPOTLIGHT_MERCHLEVEL_DATARETRIEVER_ERROR );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors, adre );
    }
  }

  /*
   * private MerchlinqLevelData sortMerchLevels (MerchlinqLevelData data) { MerchLevelComparator
   * comp = new MerchLevelComparator() ; java.util.Collections.sort( (List)data.getLevels(), comp );
   * return data ; }
   */

  private AwardBanqMerchResponseValueObject sortMerchLevels( AwardBanqMerchResponseValueObject data )
  {
    Collections.sort( data.getMerchLevel(), new MerchLevelValueObjectComparator() );
    return data;
  }

  /*
   * Merge list of Program Levels from DB with list from MerchLinq
   */
  public void mergeMerchLevelWithOMList( List countryList ) throws ServiceErrorException
  {
    if ( countryList == null )
    {
      return;
    }
    // Do the merging for each country
    for ( int index = 0; index < countryList.size(); index++ )
    {
      int ordinalPositionCounter = 1;
      PromoMerchCountry promoMerchCountry = (PromoMerchCountry)countryList.get( index );

      AwardBanqMerchResponseValueObject merchlinqLevelData = null;
      if ( System.getProperty( "environment.name" ) != null && System.getProperty( "environment.name" ).equals( "dev" ) )
      {
        merchlinqLevelData = getMerchlinqLevelDataWebService( promoMerchCountry.getProgramId(), "qa" );
      }
      else
      {
        merchlinqLevelData = getMerchlinqLevelDataWebService( promoMerchCountry.getProgramId() );
      }
      Collection levels = merchlinqLevelData.getMerchLevel();
      if ( levels != null )
      {
        for ( Iterator iter = levels.iterator(); iter.hasNext(); )
        {
          MerchLevelValueObject merchLevel = (MerchLevelValueObject)iter.next();
          PromoMerchProgramLevel levelObj = new PromoMerchProgramLevel();
          levelObj.setLevelName( merchLevel.getName() );
          boolean found = false;
          if ( promoMerchCountry.getLevels() != null && promoMerchCountry.getLevels().size() > 0 )
          {
            for ( Iterator levelIter = promoMerchCountry.getLevels().iterator(); levelIter.hasNext(); )
            {
              PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelIter.next();
              if ( promoMerchProgramLevel.getLevelName().equals( levelObj.getLevelName() ) )
              {
                promoMerchProgramLevel.setMinValue( merchLevel.getMinValue() );
                promoMerchProgramLevel.setMaxValue( merchLevel.getMaxValue() );
                promoMerchProgramLevel.setLevelName( merchLevel.getName() );
                promoMerchProgramLevel.setDisplayLevelName( CmsResourceBundle.getCmsBundle().getString( promoMerchProgramLevel.getCmAssetKey(), PromoMerchProgramLevel.SPOTLIGHT_LEVEL_NAME_KEY ) );
                if ( promoMerchProgramLevel.getOrdinalPosition() == 0 )
                {
                  promoMerchProgramLevel.setOrdinalPosition( ordinalPositionCounter );
                  ordinalPositionCounter++;
                }
                found = true;
                break;
              }
            }
          }
          if ( !found )
          {
            levelObj.setMinValue( merchLevel.getMinValue() );
            levelObj.setMaxValue( merchLevel.getMaxValue() );
            levelObj.setLevelName( merchLevel.getName() );
            if ( promoMerchCountry.getLevels() == null )
            {
              promoMerchCountry.setLevels( new LinkedHashSet() );
            }
            levelObj.setOrdinalPosition( ordinalPositionCounter );
            ordinalPositionCounter++;
            promoMerchCountry.getLevels().add( levelObj );
          }
        }
      }
      countryList.set( index, promoMerchCountry );
    }

  }

  public Set getUniqueMerchlinqLevelDataForPrograms( List programIds ) throws ServiceErrorException
  {
    Set merchProducts = new java.util.HashSet();

    for ( int i = 0; i < programIds.size(); i++ )
    {
      String programId = (String)programIds.get( i );

      // standard gift code products
      AwardBanqMerchResponseValueObject data = getMerchlinqLevelDataWebService( programId, true, true );

      Iterator levelIter = data.getMerchLevel().iterator();
      // iterate the Levels for this program
      while ( levelIter.hasNext() )
      {
        MerchLevelValueObject level = (MerchLevelValueObject)levelIter.next();
        // iterate through the products of this level
        if ( level.getMerchLevelProduct() != null )
        {
          Iterator productIter = level.getMerchLevelProduct().iterator();
          while ( productIter.hasNext() )
          {
            MerchLevelProductValueObject product = (MerchLevelProductValueObject)productIter.next();
            if ( !containsProduct( merchProducts, product ) )
            {
              merchProducts.add( product );
            }
          }
        }
      }
    }

    return merchProducts;
  }

  // hack, remove and have Avinash implement an equals on the MerchLevelProduct object
  private boolean containsProduct( Set products, MerchLevelProductValueObject product )
  {
    if ( products.isEmpty() )
    {
      return false;
    }

    Iterator iter = products.iterator();
    while ( iter.hasNext() )
    {
      MerchLevelProductValueObject inListProduct = (MerchLevelProductValueObject)iter.next();
      if ( inListProduct.getProductSetId().equals( product.getProductSetId() ) && inListProduct.getCatalogId().equals( product.getCatalogId() ) )
      {
        return true;
      }
    }
    return false;
  }

  public MerchLevel getMerchLevelData( PromoMerchProgramLevel level, boolean productData, boolean productIds )
  {
    String programId = null;

    try
    {
      programId = level.getPromoMerchCountry().getProgramId();
      AwardBanqMerchResponseValueObject levelData = getMerchlinqLevelDataWebService( programId, productData, productIds );
      Iterator iter = levelData.getMerchLevel().iterator();
      long position = level.getOrdinalPosition();
      long currentPosition = 1;
      while ( iter.hasNext() )
      {
        MerchLevelValueObject omLevel = (MerchLevelValueObject)iter.next();

        // grab the correct level
        if ( position == currentPosition )
        {
          MerchLevel merchLevel = new MerchLevel();
          merchLevel.setMaxValue( omLevel.getMaxValue() );
          merchLevel.setMinValue( omLevel.getMinValue() );
          merchLevel.setName( omLevel.getName() );
          merchLevel.setProductsReturned( omLevel.isProductsReturned() );

          List<MerchLevelProduct> products = new ArrayList<MerchLevelProduct>();
          for ( Iterator<MerchLevelProductValueObject> productIter = omLevel.getMerchLevelProduct().iterator(); productIter.hasNext(); )
          {
            MerchLevelProductValueObject merchProduct = productIter.next();
            MerchLevelProduct product = new MerchLevelProduct();

            product.setCatalogId( merchProduct.getCatalogId() );
            try
            {
              product.setDetailImageURL( new URL( merchProduct.getDetailImageURL() ) );
              product.setThumbnailImageURL( new URL( merchProduct.getThumbnailImageURL() ) );
            }
            catch( MalformedURLException e )
            {
              e.printStackTrace();
            }
            // product.setProductGroupDescriptions( merchProduct.getProductDescription() );
            product.setProductIds( merchProduct.getProductIds() );
            product.setProductSetId( merchProduct.getProductSetId() );

            products.add( product );
          }

          merchLevel.setProducts( products );
          return merchLevel;
        }
        currentPosition++;
      }
      // should not get here
      throw new BeaconRuntimeException();
    }
    catch( ServiceErrorException see )
    {
      // append the second and third parameter to include the program number and participant id to
      // the existing
      // service error
      ServiceError se = (ServiceError)see.getServiceErrors().get( 0 );
      String[] args = new String[3];
      args[0] = se.getArg1();
      args[1] = se.getArg2();
      args[2] = programId;
      se.setArgs( args );
      // throw BeaconRuntimeException so that transaction will be rolled back automatically
      throw new BeaconRuntimeException( see );
    }
  }

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts ) throws ServiceErrorException
  {
    List serviceErrors = new ArrayList();
    try
    {
      AwardBanqMerchResponseValueObject data = null;
      String environmentId = getDestEnvironment();
      if ( StringUtils.isNotBlank( environmentId ) )
      {
        data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, includeProducts, environmentId );
      }
      else
      {
        data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, includeProducts );

      }
      return sortMerchLevels( data );
    }
    catch( AwardslinqDataRetrieverException adre )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SPOTLIGHT_MERCHLEVEL_DATARETRIEVER_ERROR );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors, adre );
    }
  }

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts, boolean includeDetails ) throws ServiceErrorException
  {
    String cacheKey = programId + "," + includeProducts + "," + includeDetails;
    AwardBanqMerchResponseValueObject data = (AwardBanqMerchResponseValueObject)merchLevelDataCache.get( cacheKey );
    if ( data != null )
    {
      return data;
    }
    List serviceErrors = new ArrayList();
    try
    {
      String environmentId = getDestEnvironment();
      Logger logger = Logger.getInstance( this.getClass() );
      logger.debug( "Environment =>> " + environmentId );
      if ( StringUtils.isNotBlank( environmentId ) )
      {
        data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, includeProducts, includeDetails, environmentId );
      }
      else
      {
        data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, includeProducts, includeDetails );
      }
      data = sortMerchLevels( data );
      merchLevelDataCache.put( cacheKey, data );
      return data;
    }
    catch( AwardslinqDataRetrieverException adre )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SPOTLIGHT_MERCHLEVEL_DATARETRIEVER_ERROR );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors, adre );
    }
  }

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts, String environmentId ) throws ServiceErrorException
  {
    List serviceErrors = new ArrayList();
    try
    {
      AwardBanqMerchResponseValueObject data = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, includeProducts, environmentId );
      return sortMerchLevels( data );
    }
    catch( AwardslinqDataRetrieverException adre )
    {
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.SPOTLIGHT_MERCHLEVEL_DATARETRIEVER_ERROR );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors, adre );
    }
  }

  public MerchlinqLevelData buildMerchLinqLevelData( AwardBanqMerchResponseValueObject data )
  {
    MerchlinqLevelData merchLinqResponse = new MerchlinqLevelData();
    merchLinqResponse.setMerchlinqLevelReturned( data.isMerchlinqLevelReturned() );
    merchLinqResponse.setReturnMessage( data.getErrDescription() );

    List<MerchLevel> levels = new ArrayList<MerchLevel>();
    for ( Iterator<MerchLevelValueObject> levelIter = data.getMerchLevel().iterator(); levelIter.hasNext(); )
    {
      MerchLevelValueObject merchLevelVo = levelIter.next();
      MerchLevel level = new MerchLevel();
      level.setMaxValue( merchLevelVo.getMaxValue() );
      level.setMinValue( merchLevelVo.getMinValue() );
      level.setName( merchLevelVo.getName() );
      level.setProductsReturned( merchLevelVo.isProductsReturned() );

      List<MerchLevelProduct> products = new ArrayList<MerchLevelProduct>();
      for ( Iterator<MerchLevelProductValueObject> productIter = merchLevelVo.getMerchLevelProduct().iterator(); productIter.hasNext(); )
      {
        MerchLevelProductValueObject product = productIter.next();
        MerchLevelProduct merchProduct = new MerchLevelProduct();
        merchProduct.setCatalogId( product.getCatalogId() );
        try
        {
          merchProduct.setDetailImageURL( new URL( product.getDetailImageURL() ) );
          merchProduct.setThumbnailImageURL( new URL( product.getThumbnailImageURL() ) );
        }
        catch( MalformedURLException e )
        {
          e.printStackTrace();
        }

        // map group descriptions when available
        if ( product.getProductGroupDescriptions() != null && product.getProductGroupDescriptions().getEntry() != null && !product.getProductGroupDescriptions().getEntry().isEmpty() )
        {
          Map<Locale, ProductGroupDescription> productGroupDescriptions = new HashMap<Locale, ProductGroupDescription>();

          for ( Iterator<ProductEntryVO> productEntryVOIt = product.getProductGroupDescriptions().getEntry().iterator(); productEntryVOIt.hasNext(); )
          {
            ProductEntryVO productEntryVO = productEntryVOIt.next();
            ProductGroupDescription productGroupDescr = new ProductGroupDescription();
            if ( productEntryVO != null && productEntryVO.getValue() != null )
            {
              productGroupDescr.setCopy( productEntryVO.getValue().getCopy() );
              productGroupDescr.setDescription( productEntryVO.getValue().getDescription() );
              productGroupDescr.setLocale( LocaleUtils.getLocale( productEntryVO.getValue().getLocale() ) );
            }
            productGroupDescriptions.put( LocaleUtils.getLocale( productEntryVO.getKey() ), productGroupDescr );
          }
          merchProduct.setProductGroupDescriptions( productGroupDescriptions );
        }

        merchProduct.setProductIds( product.getProductIds() );
        merchProduct.setProductSetId( product.getProductSetId() );
        products.add( merchProduct );
      }
      level.setProducts( products );
      levels.add( level );
    }
    merchLinqResponse.setLevels( levels );
    return merchLinqResponse;
  }
  
  //Client customizations for wip #23129 starts
  public GiftcodeStatusResponseValueObject getGiftCodeStatus( String giftCode, String programId, String orderNumber, String refNumber ) throws ServiceErrorException
  {
    List<ServiceError> serviceErrors = new ArrayList<ServiceError>();
    //GiftCodeInfoVO status = null;
    GiftcodeStatusResponseValueObject status = null;
    try
    {
      status = OMRemoteDelegate.getInstance().getGiftCodeInfoWebService( giftCode, programId, orderNumber, refNumber );
      //status = OMRemoteDelegate.getInstance().getGiftCodeInfo( giftCode, programId, orderNumber, refNumber );
      return status;
    }
    catch( OMParticipantServicesException opse )
    {
      if ( status == null )
      {
        serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.RECOGNITION_OM_INVALID_ORDER_ERROR, opse.getParentException().getMessage() ) );
        throw new ServiceErrorException( serviceErrors, opse );
      }
      else if ( status != null && status.getErrCode() != 0 )
      {
        throw createServiceErrorExceptionForPaxServiceInvalidOrderException( status.getErrCode(), opse );
      }
      else
      {
        String message = opse.getParentException().getMessage();
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.RECOGNITION_OM_PAX_SERVICE_ERROR, message );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors, opse );
      }
    }
    catch( OMDelegateException adre )
    {
      String message = adre.getMessage();
      ServiceError error = new ServiceError( ServiceErrorMessageKeys.RECOGNITION_OM_ORDER_DEFAULT_ERROR, message );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors, adre );
    }
  }
  //Client customizations for wip #23129 end

  public PromoMerchProgramLevel getPromoMerchProgramLevelById( Long id )
  {
    return promoMerchProgramLevelDAO.getPromoMerchProgramLevelById( id );
  }

  public PromoMerchProgramLevelDAO getPromoMerchProgramLevelDAO()
  {
    return promoMerchProgramLevelDAO;
  }

  public void setPromoMerchProgramLevelDAO( PromoMerchProgramLevelDAO promoMerchProgramLevelDAO )
  {
    this.promoMerchProgramLevelDAO = promoMerchProgramLevelDAO;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public List<PromoMerchProgramLevelGiftCodes> getPromoMerchProgramLevelGiftCodes( Long importFileId )
  {
    return promoMerchProgramLevelDAO.getPromoMerchProgramLevelGiftCodes( importFileId );
  }

}

/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/MerchLinqProductDataUtils.java,v $
 */

package com.biperf.core.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.awardslinqDataRetriever.client.MerchLevelProduct;
import com.biperf.awardslinqDataRetriever.client.MerchlinqLevelData;
import com.biperf.awardslinqDataRetriever.client.ProductGroupDescription;
import com.biperf.awardslinqDataRetriever.util.AwardslinqDataRetrieverException;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.DataRetrieverDelegate;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;
import com.biperf.core.service.awardbanq.OMRemoteDelegate;
import com.biperf.core.service.awardbanq.impl.MerchLevelProductValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.system.SystemVariableService;

/**
 * MerchLinqProductDataUtils.
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
 * <td>viswanat</td>
 * <td>Feb 9, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MerchLinqProductDataUtils
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog( MerchLinqProductDataUtils.class );

  /**
   * Get a List of MerchLevelProducts for the specified program and level
   * @param programId
   * @param maxValue
   * @return List of MerchLevelProduct objects
   */
  public static List getProducts( String programId, int maxValue ) throws AwardslinqDataRetrieverException
  {
    if ( programId != null )
    {
      try
      {
        AwardBanqMerchResponseValueObject levelData = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, true, getDestEnvironment() );
        MerchlinqLevelData merchlinqLevelData = buildMerchLinqLevelData( levelData );
        for ( Iterator iter = merchlinqLevelData.getLevels().iterator(); iter.hasNext(); )
        {
          MerchLevel merchLevel = (MerchLevel)iter.next();
          if ( merchLevel.getMaxValue() == maxValue )
          {
            return (List)merchLevel.getProducts();
          }
        }
      }
      catch( AwardslinqDataRetrieverException e )
      {
        logger.error( "Unable to get Products for " + programId, e );
        throw e;
      }
    }
    return null;
  }

  /**
   * Get a List of MerchLevelProducts for the specified program and level
   * @param programId
   * @param maxValue
   * @return List of MerchLevelProduct objects
   */
  public static List getProducts( String programId, int maxValue, PlatformTransactionManager transactionManager ) throws Exception
  {
    // ------------------------
    // Suspend the Transaction
    // ------------------------
    JtaTransactionManager jtaTransactionManager = (JtaTransactionManager)transactionManager;
    Transaction trx = jtaTransactionManager.getTransactionManager().suspend();
    try
    {
      return getProducts( programId, maxValue );
    }
    catch( Exception e )
    {
      logger.error( "Unable to get Products for " + programId, e );
      throw e;
    }
    finally
    {
      try
      {
        if ( jtaTransactionManager != null && trx != null )
        {
          // --------------------------
          // Resume the transaction
          // --------------------------
          jtaTransactionManager.getTransactionManager().resume( trx );
        }
      }
      catch( InvalidTransactionException e )
      {
        logger.error( "Unable to get products for " + programId, e );
      }
      catch( SystemException e )
      {
        logger.error( "Unable to get products for " + programId, e );
      }
    }
  }

  /**
   * Get a list of all products for the specified program
   * @param programId
   * @param maxValue
   * @return List of MerchLevelProduct objects
   */
  public static List getProducts( String programId ) throws AwardslinqDataRetrieverException
  {
    if ( programId != null )
    {
      try
      {
        List products = new ArrayList();
        AwardBanqMerchResponseValueObject levelData = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, true, getDestEnvironment() );
        MerchlinqLevelData merchlinqLevelData = buildMerchLinqLevelData( levelData );
        for ( Iterator iter = merchlinqLevelData.getLevels().iterator(); iter.hasNext(); )
        {
          MerchLevel merchLevel = (MerchLevel)iter.next();
          if ( merchLevel.getProducts() != null )
          {
            products.addAll( merchLevel.getProducts() );
          }
        }
      }
      catch( AwardslinqDataRetrieverException e )
      {
        logger.error( "Unable to get Products for " + programId, e );
        throw e;
      }
    }
    return null;
  }

  /**
   * @param programId
   * @return Map keyed by Max Value containing list of List of MerchLevelProduct
   */
  public static Map getProductMap( String programId ) throws AwardslinqDataRetrieverException
  {
    try
    {
      AwardBanqMerchResponseValueObject levelData = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, true, getDestEnvironment() );
      MerchlinqLevelData merchlinqLevelData = buildMerchLinqLevelData( levelData );
      Map levels = new HashMap();
      for ( Iterator iter = merchlinqLevelData.getLevels().iterator(); iter.hasNext(); )
      {
        MerchLevel merchLevel = (MerchLevel)iter.next();
        if ( merchLevel.getProducts() != null )
        {
          Iterator itr = merchLevel.getProducts().iterator();
          while ( itr.hasNext() )
          {
            MerchLevelProduct mlp = (MerchLevelProduct)itr.next();
            ProductGroupDescription productDescription = mlp.getProductGroupDescriptions().get( UserManager.getLocale() );
            productDescription.setDescription( escapeColumnValue( mlp.getProductGroupDescriptions().get( UserManager.getLocale() ).getDescription() ) );
            productDescription.setCopy( escapeColumnValue( mlp.getProductGroupDescriptions().get( UserManager.getLocale() ).getCopy() ) );
            levels.put( new Integer( merchLevel.getMaxValue() ), merchLevel.getProducts() );
          }
        }

      }
      return levels;
    }
    catch( AwardslinqDataRetrieverException e )
    {
      logger.error( "Unable to get Products for " + programId, e );
      throw e;
    }
  }

  /**
   * Get a random product from the specified list
   * @return MerchLevelProduct
   */
  public static MerchLevelProduct getRandomProduct( List products )
  {
    if ( products != null && products.size() > 0 )
    {
      int randomNum = (int)Math.round( Math.random() * ( products.size() - 1 ) );
      return (MerchLevelProduct)products.get( randomNum );
    }
    return null;
  }

  private static String getDestEnvironment()
  {
    StringBuffer sysVarName = new StringBuffer();
    sysVarName.append( SystemVariableService.GOALQUEST_MERCHLINQ_DEST_ENV_PREFIX );
    sysVarName.append( "." );
    sysVarName.append( Environment.getEnvironment() );
    return getSystemVariableService().getPropertyByName( sysVarName.toString() ).getStringVal();
  }

  /**
   * @param programId
   * @param catalogId
   * @param productId
   * @return MerchLevelProduct
   */
  public static MerchLevelProduct getProduct( String programId, String catalogId, String productId ) throws AwardslinqDataRetrieverException
  {
    try
    {
      AwardBanqMerchResponseValueObject levelData = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, true, getDestEnvironment() );
      MerchlinqLevelData merchlinqLevelData = buildMerchLinqLevelData( levelData );
      for ( Iterator levelIter = merchlinqLevelData.getLevels().iterator(); levelIter.hasNext(); )
      {
        MerchLevel merchLevel = (MerchLevel)levelIter.next();
        if ( merchLevel.getProducts() != null )
        {
          for ( Iterator prodIter = merchLevel.getProducts().iterator(); prodIter.hasNext(); )
          {
            MerchLevelProduct merchProd = (MerchLevelProduct)prodIter.next();
            if ( merchProd.getCatalogId().equals( catalogId ) && merchProd.getProductSetId().equals( productId ) )
            {
              return merchProd;
            }
          }
        }
      }
      return null;
    }
    catch( AwardslinqDataRetrieverException e )
    {
      logger.error( "Unable to get Product for " + programId, e );
      throw e;
    }
  }

  public static MerchLevelProduct getProduct( String programId, String productId ) throws AwardslinqDataRetrieverException
  {
    try
    {
      AwardBanqMerchResponseValueObject levelData = DataRetrieverDelegate.getInstance().getMerchlinqLevelDataWebService( programId, true, getDestEnvironment() );
      MerchlinqLevelData merchlinqLevelData = buildMerchLinqLevelData( levelData );
      for ( MerchLevel merchLevel : merchlinqLevelData.getLevels() )
      {
        if ( merchLevel.getProducts() != null )
        {
          for ( MerchLevelProduct product : merchLevel.getProducts() )
          {
            if ( product.getProductSetId().equals( productId ) )
            {
              return product;
            }
          }
        }
      }
      return null;
    }
    catch( AwardslinqDataRetrieverException e )
    {
      logger.error( "Unable to get Product for " + programId, e );
      throw e;
    }
  }

  /**
   * @param giftCode
   * @return boolean indicating if gift code has been redeemed - default to false
   */
  public static boolean isGiftCodeRedeemed( MerchOrder merchOrder )
  {
    try
    {
      GiftcodeStatusResponseValueObject giftCodeInfoVO = OMRemoteDelegate.getInstance()
          .getGiftCodeInfoWebService( merchOrder.getFullGiftCode(), merchOrder.getPromoMerchProgramLevel().getProgramId(), merchOrder.getOrderNumber(), merchOrder.getReferenceNumber() );
      if ( giftCodeInfoVO != null )
      {
        return giftCodeInfoVO.getBalanceAvailable() == 0;
      }
    }
    catch( Exception e )
    {
      logger.error( "Exception retrieving gift code status", e );
    }
    return false;
  }

  /**
   * @param products
   * @param productId
   * @param catalogId
   * @return MerchLevelProduct
   */
  public static MerchLevelProduct getProductByIdAndCatalogId( List products, String productId, String catalogId )
  {
    if ( products != null && productId != null && catalogId != null )
    {
      for ( Iterator iter = products.iterator(); iter.hasNext(); )
      {
        MerchLevelProduct product = (MerchLevelProduct)iter.next();
        if ( product.getProductSetId().equals( productId ) && product.getCatalogId().equals( catalogId ) )
        {
          return product;
        }
      }
    }
    return null;
  }

  /**
   * Get the SystemVariableService from the beanLocator.
   * 
   * @return SystemVariableService
   */
  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)ServiceLocator.getService( SystemVariableService.BEAN_NAME );
  }

  private static String escapeColumnValue( Object rawValue )
  {
    String stringValue = StringUtils.trim( rawValue.toString() );

    if ( stringValue == null )
    {
      return null;
    }
    stringValue = StringUtils.replace( stringValue, "&", "&amp" );
    stringValue = StringUtils.replace( stringValue, "\"", "&quot" );
    stringValue = StringUtils.replace( stringValue, ",", "&#44" );
    stringValue = StringUtils.replace( stringValue, "&reg;", "&reg" );
    stringValue = StringUtils.replace( stringValue, "'", "&#39" );
    // Fix Bug # 30982
    stringValue = StringUtils.replace( stringValue, "\r\n\r\n", "&#010" );
    stringValue = StringUtils.replace( stringValue, "\r\n", "&#010" );

    // stringValue = StringUtils.replace( StringUtils.trimToEmpty(stringValue), "attention.", "<br>"
    // );

    return stringValue;

  }

  private static MerchlinqLevelData buildMerchLinqLevelData( AwardBanqMerchResponseValueObject data )
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
        // merchProduct.setProductGroupDescriptions( product.getProductDescription() );
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

}

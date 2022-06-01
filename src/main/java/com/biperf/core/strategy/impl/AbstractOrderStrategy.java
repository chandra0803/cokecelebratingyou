/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/strategy/impl/AbstractOrderStrategy.java,v $
 */

package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.OrderStrategy;
import com.biperf.core.utils.BeanLocator;
import com.biperf.databeans.order.ShippingHandlingChargesDataBean;
import com.biperf.databeans.order.TaxInformationDataBean;
import com.biperf.databeans.product.OMProductDataBean;
import com.biperf.om.cart.DropShipDetail;
import com.biperf.om.cart.IShippingMethod;
import com.biperf.om.cart.LineItem;
import com.biperf.om.cart.LineItemAttribute;
import com.biperf.om.cart.LineItemValidationException;
import com.biperf.om.cart.ShippingMethod;
import com.biperf.om.cart.ShoppingCart;
import com.biperf.om.cart.ShoppingCartLineItemTaxDetail;
import com.biperf.om.order.Application;
import com.biperf.om.order.CertificateList;
import com.biperf.om.order.CreditCard;
import com.biperf.om.order.Order;
import com.biperf.om.order.OrderCreationException;
import com.biperf.om.participant.ParticipantValidationException;
import com.biperf.om.participant.ShipToAddress;
import com.biperf.util.ValidationException;

/**
 * This class will contain methods relating to management of order creation
 * and order submission.
 * 
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * </table>
 *
 */

public abstract class AbstractOrderStrategy extends BaseStrategy implements OrderStrategy
{
  public static final String APPLICATION_ID = "CELEB";
  private static final String HANDLING_POINTS = "HNDLGPNTS";
  private static final String FREIGHT_NEXT_DAY = "FRGHTNDAY";
  private static final String FREIGHT_SECOND_DAY = "FRGHTSDAY";
  private static final String LINE_NUMBER = "LN";

  private AwardBanQServiceFactory awardBanQServiceFactory;

  /**
   * This method will creat an application object for omservice
   * 
   * 
   * @param  programId
   * @return application
   */
  protected Application createOMApplication( String programId )
  {
    Application omApplication = null;
    try
    {
      omApplication = new Application( APPLICATION_ID, programId );
    }
    catch( ValidationException e )
    {
      throw new BeaconRuntimeException( "Validation exception in createOMApplication: " + e );
    }

    return omApplication;
  }

  /**
   * This method will creat a participant object for omservice
   * 
   * @param  programId
   * @param  participant
   * @return com.biperf.om.participant.Participant
   */
  protected com.biperf.om.participant.Participant createOMParticipant( String programId, Participant participant )
  {
    com.biperf.om.participant.Participant omParticipant = null;
    UserEmailAddress emailAddress = participant.getPrimaryEmailAddress();
    String email = null;
    if ( emailAddress != null && emailAddress.getEmailAddr() != null )
    {
      email = emailAddress.getEmailAddr();
    }
    try
    {
      omParticipant = new com.biperf.om.participant.Participant( participant.getFirstName(),
                                                                 participant.getLastName(),
                                                                 participant.getUserName(),
                                                                 programId,
                                                                 participant.getAwardBanqNumber(),
                                                                 email,
                                                                 participant.getCentraxId(),
                                                                 true );
    }
    catch( ParticipantValidationException e )
    {
      throw new BeaconRuntimeException( "Validation exception in createOMApplication: " + e );
    }
    return omParticipant;
  }

  /**
   * This method will creat a shopping cart object for omservice
   * 
   * @param programId
   * @param omProduct
   * @param omGiftWrapProduct
   * @param omCardProduct
   * @param cardText
   * @param shippingMethod
   * @param taxInformationDataBean
   * @return
   */
  protected ShoppingCart createOMShoppingCart( String programId,
                                               OMProductDataBean omProduct,
                                               OMProductDataBean omGiftWrapProduct,
                                               OMProductDataBean omCardProduct,
                                               String cardText,
                                               String shippingMethod,
                                               TaxInformationDataBean taxInformationDataBean )
  {
    ShoppingCart omShoppingCart = new ShoppingCart();
    try
    {
      LineItem lineItem = new LineItem( programId, omProduct.getProductID(), 1, omProduct.getOrderPrice(), new ShippingMethod( shippingMethod ), new DropShipDetail(), omProduct );
      Collection lineItemAttributes = new ArrayList();
      if ( omGiftWrapProduct != null )
      {
        LineItemAttribute giftWrap = new LineItemAttribute( programId,
                                                            omGiftWrapProduct.getProductID(),
                                                            1,
                                                            omGiftWrapProduct.getOrderPrice(),
                                                            new ShippingMethod( shippingMethod ),
                                                            new DropShipDetail(),
                                                            omGiftWrapProduct,
                                                            null,
                                                            LineItemAttribute.GIFT_TYPE,
                                                            null );

        lineItemAttributes.add( giftWrap );
      }
      if ( omCardProduct != null )
      {
        LineItemAttribute giftCard = new LineItemAttribute( programId,
                                                            omCardProduct.getProductID(),
                                                            1,
                                                            omCardProduct.getOrderPrice(),
                                                            new ShippingMethod( shippingMethod ),
                                                            new DropShipDetail(),
                                                            omCardProduct,
                                                            null,
                                                            LineItemAttribute.CARD_TYPE,
                                                            cardText );
        lineItemAttributes.add( giftCard );
      }

      lineItem.setOrderLineAttributes( lineItemAttributes );
      omShoppingCart.addItem( lineItem );
      ArrayList newLineItemTaxList = new ArrayList();
      if ( taxInformationDataBean != null && taxInformationDataBean.getLineItemsTaxDetail() != null )
      {
        for ( Iterator taxDetailIter = taxInformationDataBean.getLineItemsTaxDetail().iterator(); taxDetailIter.hasNext(); )
        {
          ShoppingCartLineItemTaxDetail lineItemTax = (ShoppingCartLineItemTaxDetail)taxDetailIter.next();
          if ( LINE_NUMBER.equalsIgnoreCase( lineItemTax.getRowType() ) )
          {
            newLineItemTaxList.add( lineItemTax );
          }
          else
          {
            lineItemTax.setLineNumber( 0 );
            if ( IShippingMethod.GROUND.equalsIgnoreCase( shippingMethod ) && HANDLING_POINTS.equalsIgnoreCase( lineItemTax.getProductId() ) )
            {
              newLineItemTaxList.add( lineItemTax );
            }
            else if ( IShippingMethod.NEXTDAY.equalsIgnoreCase( shippingMethod ) && FREIGHT_NEXT_DAY.equalsIgnoreCase( lineItemTax.getProductId() ) )
            {
              newLineItemTaxList.add( lineItemTax );
            }
            else if ( IShippingMethod.SECONDDAY.equalsIgnoreCase( shippingMethod ) && FREIGHT_SECOND_DAY.equalsIgnoreCase( lineItemTax.getProductId() ) )
            {
              newLineItemTaxList.add( lineItemTax );
            }
          }
        }
      }
      omShoppingCart.setItemsTaxDetail( newLineItemTaxList );
      // if (taxCharge > 0)
      // {
      // ShoppingCartLineItemTaxDetail taxDetail = new ShoppingCartLineItemTaxDetail()
      // }
      // If gift warp exists, add it to the shopping cart

    }
    catch( LineItemValidationException e )
    {
      throw new BeaconRuntimeException( "line item validation exception in createOMShoppingCart: " + e );
    }
    catch( ValidationException e )
    {
      throw new BeaconRuntimeException( "validation exception in createOMShoppingCart: " + e );
    }

    return omShoppingCart;
  }

  /**
   * This method will create an omservice order
   * @param programId
   * @param participant
   * @param omShipto
   * @param omProduct
   * @param omGiftWrapProduct
   * @param omCardProduct
   * @param cardText 
   * @param shippingMethod
   * @param omCreditCard
   * @param level
   * @param shippingCharge
   * @param handlingCharge
   * @param taxCharge
   * @param taxInformationDataBean
   * @param shippingHandlingChargesDataBean
   * @return
   */
  public abstract Order orderCreation( String programId,
                                       Participant participant,
                                       ShipToAddress omShipto,
                                       OMProductDataBean omProduct,
                                       OMProductDataBean omGiftWrapProduct,
                                       OMProductDataBean omCardProduct,
                                       String cardText,
                                       String shippingMethod,
                                       CreditCard omCreditCard,
                                       PromoMerchProgramLevel level,
                                       long shippingCharge,
                                       long handlingCharge,
                                       long taxCharge,
                                       TaxInformationDataBean taxInformationDataBean,
                                       ShippingHandlingChargesDataBean shippingHandlingChargesDataBean );
  // {
  // Order omOrder = null;
  // try
  // {
  // omOrder = new Order (this.createOMApplication(programId), this.createOMShoppingCart(programId,
  // omProduct, omGiftWrapProduct, shippingMethod ), createOMParticipant(programId,participant),
  // omShipto,omCreditCard, null,null, 0, shippingCharge, handlingCharge, true, taxCharge);
  //
  // }
  // catch(ValidationException e)
  // {
  // throw new BeaconRuntimeException( "validation exception in orderCreation: " + e );
  // }
  // catch(OrderCreationException e)
  // {
  // throw new BeaconRuntimeException( "order creation exception in orderCreation: " + e );
  // }
  // return omOrder;
  // }

  /**
   * 
   * This method will create an omservice order  
   * 
   * @param programId
   * @param participant
   * @param omShipto
   * @param omProduct
   * @param omGiftWrapProduct
   * @param omCardProduct
   * @param cardText
   * @param shippingMethod
   * @param omCreditCard
   * @param omCertificates
   * @param shippingHandlingChargesDataBean
   * @param taxInformationData
   * @return
   */
  public Order performOrderCreation( String programId,
                                     Participant participant,
                                     ShipToAddress omShipto,
                                     OMProductDataBean omProduct,
                                     OMProductDataBean omGiftWrapProduct,
                                     OMProductDataBean omCardProduct,
                                     String cardText,
                                     String shippingMethod,
                                     CreditCard omCreditCard,
                                     CertificateList omCertificates,
                                     ShippingHandlingChargesDataBean shippingHandlingChargesDataBean,
                                     TaxInformationDataBean taxInformationData )
  {
    Order omOrder = null;
    try
    {
      ShoppingCart omShoppingCart = this.createOMShoppingCart( programId, omProduct, omGiftWrapProduct, omCardProduct, cardText, shippingMethod, taxInformationData );
      Application omApplication = this.createOMApplication( programId );
      com.biperf.om.participant.Participant omParticipant = this.createOMParticipant( programId, participant );

      long totalTax = 0;
      if ( taxInformationData != null )
      {
        totalTax = taxInformationData.getTotalProductTax() + shippingHandlingChargesDataBean.getTaxCharges( shippingMethod );
      }

      long orderSubTotal = omShoppingCart.getTotalCost();
      long shippingHandlingCharge = 0;
      long shippingCharge = 0;
      long handlingCharge = 0;

      if ( shippingHandlingChargesDataBean != null )
      {
        shippingHandlingCharge = shippingHandlingChargesDataBean.getShippingHandlingCharges( shippingMethod );
        shippingCharge = shippingHandlingChargesDataBean.getShippingCharge( shippingMethod );
        handlingCharge = shippingHandlingChargesDataBean.getHandlingCharge( shippingMethod );
      }
      double totalAmountCharged = 0.0;
      if ( omCreditCard != null )
      {
        totalAmountCharged = ( orderSubTotal + shippingHandlingCharge + totalTax ) / 100.0;
        omCreditCard.setAmount( totalAmountCharged );
      }

      omOrder = new Order( omApplication,
                           omShoppingCart,
                           omParticipant,
                           omShipto,
                           omCreditCard,
                           omCertificates,
                           null,
                           totalAmountCharged,
                           shippingCharge,
                           handlingCharge,
                           taxInformationData != null ? taxInformationData.getIsActualTaxProgram() : false,
                           totalTax );
    }
    catch( ValidationException e )
    {
      throw new BeaconRuntimeException( "validation exception in orderCreation: " + e );
    }
    catch( OrderCreationException e )
    {
      throw new BeaconRuntimeException( "order creation exception in orderCreation: " + e );
    }

    return omOrder;
  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    if ( awardBanQServiceFactory != null )
    {
      return awardBanQServiceFactory;
    }
    awardBanQServiceFactory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return awardBanQServiceFactory;
  }

  public MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)BeanLocator.getBean( MerchLevelService.BEAN_NAME );
  }

  public MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)BeanLocator.getBean( MerchOrderService.BEAN_NAME );
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }
}

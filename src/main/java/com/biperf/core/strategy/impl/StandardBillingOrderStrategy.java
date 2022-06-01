/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/strategy/impl/StandardBillingOrderStrategy.java,v $
 */

package com.biperf.core.strategy.impl;

import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.databeans.order.ShippingHandlingChargesDataBean;
import com.biperf.databeans.order.TaxInformationDataBean;
import com.biperf.databeans.product.OMProductDataBean;
import com.biperf.om.order.CreditCard;
import com.biperf.om.order.IOrder;
import com.biperf.om.order.Order;
import com.biperf.om.participant.ParticipantValidationException;
import com.biperf.om.participant.ShipToAddress;
import com.biperf.util.ValidationException;

/**
 * StandardBillingOrderStrategy is startegy for standard billing order.
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
 * <td>FMO</td>
 * <td>Nov 6, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StandardBillingOrderStrategy extends AbstractOrderStrategy
{
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
  public Order orderCreation( String programId,
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
                              ShippingHandlingChargesDataBean shippingHandlingChargesDataBean )
  {
    Order omOrder = null;
    MerchLevel omLevel = getMerchLevelService().getMerchLevelData( level, true, false );

    omOrder = super.performOrderCreation( programId,
                                          participant,
                                          omShipto,
                                          omProduct,
                                          omGiftWrapProduct,
                                          omCardProduct,
                                          cardText,
                                          shippingMethod,
                                          omCreditCard,
                                          null,
                                          shippingHandlingChargesDataBean,
                                          taxInformationDataBean );
    return omOrder;
  }

  /**
   * This method will submit an order to the omservice
   * and return merch order object with order number
   * giftcode and reference number.
   * 
   * @param order
   * @param levelValue
   * @return order number
   * @exception ServiceErrorException
   */
  public MerchOrder orderSubmission( IOrder order, int levelValue ) throws ServiceErrorException, ValidationException
  {
    MerchOrder merchOrder = new MerchOrder();
    // Get OMOrder from OMService
    /*
     * OMOrder omOrder = this.getAwardBanQServiceFactory().getAwardBanQService().getOMOrder(order);
     * //Submit OMOrder to OMService GiftCodeOrderResponse orderResponse =
     * this.getAwardBanQServiceFactory().getAwardBanQService().submitGiftCodeOrder( omOrder,
     * levelValue ); if (orderResponse != null) { if(omOrder!=null)
     * merchOrder.setOrderNumber(omOrder.getOrderNumber()); else
     * merchOrder.setOrderNumber(orderResponse.getOrderNumber()); if(orderResponse.getGiftCode() !=
     * null && orderResponse.getGiftCode().length() == 16) {
     * merchOrder.setGiftCode(orderResponse.getGiftCode().substring(0,8));
     * merchOrder.setGiftCodeKey(orderResponse.getGiftCode().substring(8)); }
     * merchOrder.setReferenceNumber(orderResponse.getReferenceNumber()); merchOrder.setBatchId(
     * this.getMerchOrderService().getNextBatchId() ); }
     */
    return merchOrder;
  }

  /**
   * This method overrides the parent.  We do NOT want to pass Participant account/centrax IDs to
   * OM, since this requires that the account belong to the program number for the segmented catalog 
   * 
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
      omParticipant = new com.biperf.om.participant.Participant( participant.getFirstName(), participant.getLastName(), participant.getUserName(), programId, null, email, null, true );
    }
    catch( ParticipantValidationException e )
    {
      throw new BeaconRuntimeException( "Validation exception in createOMApplication: " + e );
    }
    return omParticipant;
  }

}

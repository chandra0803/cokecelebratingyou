
package com.biperf.core.strategy;

import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.databeans.order.ShippingHandlingChargesDataBean;
import com.biperf.databeans.order.TaxInformationDataBean;
import com.biperf.databeans.product.OMProductDataBean;
import com.biperf.om.order.CreditCard;
import com.biperf.om.order.IOrder;
import com.biperf.om.order.Order;
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
public interface OrderStrategy extends Strategy
{

  /**
   * This method will create an omservice order  
   * 
   * 
   * @param  programId
   * @param  participant
   * @param  omShipto
   * @param  omProduct
   * @param  omGiftWrapProduct
   * @param  shippingMethod 
   * @param  omCreditCard
   * @return order  
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
                              ShippingHandlingChargesDataBean shippingHandlingChargesDataBean );

  /**
   * This method will submit an order to the omservice
   * and return merch order object with order number
   * giftcode and reference number.
   * 
   * @param  omOrder
   * @param  levelValue
   * @return order number
   */
  public MerchOrder orderSubmission( IOrder order, int levelValue ) throws ServiceErrorException, ValidationException;

}

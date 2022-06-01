/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/OMGiftCodeRedemptionSynchronizationProcess.java,v $
 */

package com.biperf.core.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.merchorder.MerchOrderService;

/**
 * .
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
 * <td>jenniget</td>
 * <td>Feb 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class OMGiftCodeRedemptionSynchronizationProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "omGiftCodeRedemptionSynchronizationProcess";
  public static final String MESSAGE_NAME = "OM Gift Code synchronization";

  private MerchOrderService merchOrderService;
  private static final Log logger = LogFactory.getLog( OMGiftCodeRedemptionSynchronizationProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    List<MerchOrder> unredeemedGiftCodes = new ArrayList<MerchOrder>();
    int merchOrderCount = 0;
    try
    {
      List<Long> merchOrderIds = merchOrderService.getMerchOrderIds();
      if ( merchOrderIds != null && !merchOrderIds.isEmpty() )
      {
        for ( Long merchOrderId : merchOrderIds )
        {
          List merchOrders = merchOrderService.getMerchOrdersList( merchOrderId );
          merchOrderCount = merchOrders.size();
          for ( int i = 0; i < merchOrders.size(); i++ )
          {
            MerchOrder order = (MerchOrder)merchOrders.get( i );
            try
            {
              order = merchOrderService.updateOrderStatus( order );
            }

            catch( ServiceErrorException see )
            {
              addComment( "Error processing Giftcode: " + order.getFullGiftCode() + " | " + see.getMessage() + "(process invocation ID = " + getProcessInvocationId() + ")" );
              logger.error( "ServiceErrorException", see );
            }
            catch( Exception ex )
            {
              addComment( "Error processing Giftcode: " + order.getFullGiftCode() + " | " + ex.getMessage() + "(process invocation ID = " + getProcessInvocationId() + ")" );
              logger.error( "Exception", ex );
            }

            if ( !order.isRedeemed() )
            {
              unredeemedGiftCodes.add( order );
            }
          }
        }
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );

      String errorMessage = "An exception occurred while running the process. Please See the log file for additional information." + "(process invocation ID = " + getProcessInvocationId() + ")";
      addComment( errorMessage );
    }

    addComment( "Total merchOrders processed = " + merchOrderCount );
    addComment( "Total merchOrders not redeemed = " + unredeemedGiftCodes.size() );
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  public MerchOrderService getMerchOrderService()
  {
    return this.merchOrderService;
  }

}

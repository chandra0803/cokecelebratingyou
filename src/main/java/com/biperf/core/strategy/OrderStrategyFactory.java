
package com.biperf.core.strategy;

/**
 * OrderStrategyFactory.
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
 * <td>FeiMo</td>
 * <td>Nov 06, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface OrderStrategyFactory
{
  /**
   * Construct a OrderStrategy instance. The type of strategy is determined by the payment method.
   * 
   * @param orderType
   * @return OrderStrategy
   */
  public OrderStrategy getOrderStrategy( String orderType );
}

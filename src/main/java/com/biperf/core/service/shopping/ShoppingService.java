/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/shopping/ShoppingService.java,v $
 */

package com.biperf.core.service.shopping;

import java.util.Map;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ExternalSupplierValue;
import com.biperf.core.value.ShoppingValueBean;
import com.biperf.web.singlesignon.SingleSignOnRequest;

/**
 * ShoppingService.
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
public interface ShoppingService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "shoppingService";

  public static final String NONE = "none";
  public static final String INTERNAL = "internal";
  public static final String EXTERNAL = "external";
  public static final String PAYROLL_EXTRACT = "payroll extract";

  /**
   * Setup shopping information for Internal suppliers, including enrolling in awardbanq if needed
   * 
   * @param userId
   * @return ShoppingValueBean
   * @throws ServiceErrorException
   */
  public ShoppingValueBean setupInternalShopping( Long userId ) throws ServiceErrorException;

  /**
   * Setup all external supplier information
   * 
   * @param userId
   * @return ExternalSupplierValue
   * @throws ServiceErrorException
   */
  public ExternalSupplierValue setupExternalSupplier( Long userId, String externalSupplierId ) throws ServiceErrorException;

  /**
   * gets the shopping type for a user
   * 
   * @param userId
   * @return String with the shopping type for the user
   */
  public String checkShoppingType( Long userId );

  public Map<String, String> getShopUrlMapping( Long userId, PromotionAwardsType awardType, String baseURI ) throws ServiceErrorException;

  public SingleSignOnRequest loadLevelLabels( SingleSignOnRequest signOnRequest, Long promoMerchCountryId );

  public SingleSignOnRequest loadParticipantAddressInfo( SingleSignOnRequest signOnRequest, Participant pax );
  
  public String getShoppingUrlForInactiveUser( Long userId );

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/merchlevel/MerchLevelService.java,v $
 */

package com.biperf.core.service.merchlevel;

import java.util.List;

import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.awardslinqDataRetriever.client.MerchlinqLevelData;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.PromoMerchProgramLevelGiftCodes;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.GiftcodeStatusResponseValueObject;

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
public interface MerchLevelService extends SAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Static final for the BEAN_NAME for use in the applicationContext.xml
   */
  public static final String BEAN_NAME = "merchLevelService";

  public void clearPropertyFromCache();

  /*
   * Merge list of Program Levels from DB with list from MerchLinq
   */
  public void mergeMerchLevelWithOMList( List countryList ) throws ServiceErrorException;

  /**
   * @param id
   * @return the PromoMerchProgramLevel
   */
  public PromoMerchProgramLevel getPromoMerchProgramLevelById( Long id );

  public java.util.Set getUniqueMerchlinqLevelDataForPrograms( List programIds ) throws ServiceErrorException;

  public MerchLevel getMerchLevelData( PromoMerchProgramLevel level, boolean productData, boolean productIds );

  public List<PromoMerchProgramLevelGiftCodes> getPromoMerchProgramLevelGiftCodes( Long importFileId );

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, String environmentId ) throws ServiceErrorException;

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId ) throws ServiceErrorException;

  public GiftcodeStatusResponseValueObject getGiftCodeStatusWebService( String giftCode, String programId, String orderNumber, String refNumber ) throws ServiceErrorException;

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts ) throws ServiceErrorException;

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts, boolean includeDetails ) throws ServiceErrorException;

  public MerchlinqLevelData buildMerchLinqLevelData( AwardBanqMerchResponseValueObject data );

  public AwardBanqMerchResponseValueObject getMerchlinqLevelDataWebService( String programId, boolean includeProducts, String environmentId ) throws ServiceErrorException;
  
  // Client customizations for wip #23129 starts
  public GiftcodeStatusResponseValueObject getGiftCodeStatus(String giftCode, String programId, String orderNumber, String refNumber) throws ServiceErrorException;
  // Client customizations for wip #23129 ends
}

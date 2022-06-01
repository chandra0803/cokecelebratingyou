/*
 * (c) 2006 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.merchandise;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.PromoMerchProgramLevelGiftCodes;

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
 * <td>ernste</td>
 * <td>Aug 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface PromoMerchProgramLevelDAO extends DAO
{

  public static final String BEAN_NAME = "promoMerchProgramLevelDAO";

  public PromoMerchProgramLevel getPromoMerchProgramLevelById( Long id );

  public List<PromoMerchProgramLevelGiftCodes> getPromoMerchProgramLevelGiftCodes( Long importFileId );

}

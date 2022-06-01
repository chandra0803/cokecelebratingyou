/*
 * (c) 2006 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.merchandise.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.merchandise.PromoMerchProgramLevelDAO;
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
public class PromoMerchProgramLevelDAOImpl extends BaseDAO implements PromoMerchProgramLevelDAO
{
  public PromoMerchProgramLevel getPromoMerchProgramLevelById( Long id )
  {
    return (PromoMerchProgramLevel)getSession().get( PromoMerchProgramLevel.class, id );
  }

  public List<PromoMerchProgramLevelGiftCodes> getPromoMerchProgramLevelGiftCodes( Long importFileId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromoMerchProgramLevel.getGiftCodesCount" );

    query.setParameter( "importFileId", importFileId );
    query.setResultTransformer( Transformers.aliasToBean( PromoMerchProgramLevelGiftCodes.class ) );

    return query.list();
  }

}

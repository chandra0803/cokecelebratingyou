
package com.biperf.core.dao.promotion.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.PromoMerchCountryDAO;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class PromoMerchCountryDAOImpl extends BaseDAO implements PromoMerchCountryDAO
{

  /**
   * 
   * Overridden from @see com.biperf.core.dao.promotion.PromoMerchCountryDAO#getPromoMerchCountryProgramById(java.lang.Long)
   * @param promoMerchCountryid
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryById( Long promoMerchCountryid )
  {
    return getPromoMerchCountryByIdWithAssociations( promoMerchCountryid, null );
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.promotion.PromoMerchCountryDAO#getPromoMerchCountryProgramByIdWithAssociations(java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param promoMerchCountryid
   * @param associationRequestCollection
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryByIdWithAssociations( Long promoMerchCountryid, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    PromoMerchCountry promoMerchCountry = (PromoMerchCountry)session.get( PromoMerchCountry.class, promoMerchCountryid );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( promoMerchCountry );
    }

    return promoMerchCountry;
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.promotion.PromoMerchCountryDAO#savePromoMerchCountryProgram(com.biperf.core.domain.promotion.PromoMerchCountryProgram)
   * @param promoMerchCountryProgram
   * @return
   */
  public PromoMerchCountry savePromoMerchCountry( PromoMerchCountry promoMerchCountry )
  {

    return (PromoMerchCountry)HibernateUtil.saveOrUpdateOrDeepMerge( promoMerchCountry );
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.promotion.PromoMerchCountryProgramDAO#getPromoMerchCountryProgramsByPromotionId(java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param promotionId
   * @param associationRequestCollection
   * @return
   */
  public List getPromoMerchCountriesByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection )
  {
    List promoMerchCountryList = null;

    promoMerchCountryList = getSession().getNamedQuery( "com.biperf.core.domain.promotion.PromoMerchCountriesByPromotionId" ).setLong( "promotionId", promotionId.longValue() ).list();

    Iterator iter = promoMerchCountryList.iterator();
    while ( iter.hasNext() )
    {
      PromoMerchCountry promoMerchCountry = (PromoMerchCountry)iter.next();
      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( promoMerchCountry );
      }
    }
    return promoMerchCountryList;
  }

  /**
   * Delete the Promo Merch Program Level. Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromoMerchCountryProgramDAO#deleteProgramLevel(com.biperf.core.domain.promotion.PromoMerchProgramLevel)
   * @param programLevel
   */
  public void deleteProgramLevel( PromoMerchProgramLevel programLevel )
  {
    Session session = HibernateSessionManager.getSession();
    session.delete( programLevel );
  }
}

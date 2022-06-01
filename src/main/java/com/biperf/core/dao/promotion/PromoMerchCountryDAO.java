
package com.biperf.core.dao.promotion;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.service.AssociationRequestCollection;

public interface PromoMerchCountryDAO extends DAO
{
  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "promoMerchCountryDAO";

  /**
   * Get the promotion merchandise country by id.  
   * 
   * @param id
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryById( Long promoMerchCountryid );

  /**
   * 
   * @param id
   * @param associationRequestCollection
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryByIdWithAssociations( Long promoMerchCountryid, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the list of promo merch countrys for the given promotion
   * 
   * @param promotionId
   * @param associationRequestCollection
   * @return List
   */
  public List getPromoMerchCountriesByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection );

  /**
   * 
   * @param promoMerchCountryProgram
   * @return promoMerchCountryProgram
   */
  public PromoMerchCountry savePromoMerchCountry( PromoMerchCountry promoMerchCountry );

  /**
   * Delete the Promo Merch Program Level. Overridden from
   * 
   * @param programLevel
   */
  public void deleteProgramLevel( PromoMerchProgramLevel programLevel );

}

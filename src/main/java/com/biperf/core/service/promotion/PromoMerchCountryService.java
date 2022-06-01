/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromoMerchCountryService.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * PromoMerchCountryService.
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
 * <td>July 19, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PromoMerchCountryService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "promoMerchCountryService";

  /**
   * Get the promotion merchandise country by promoMerchCountryid.  
   * 
   * @param id
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryById( Long promoMerchCountryId );

  /**
   * 
   * @param id
   * @param associationRequestCollection
   * @return
   */
  public PromoMerchCountry getPromoMerchCountryByIdWithAssociations( Long promoMerchCountryId, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the list of promo merch countries for the given promotion
   * 
   * @param promotionId
   * @return List
   */
  public List getPromoMerchCountriesByPromotionId( Long promotionId );

  /**
   * Get the list of promo merch countries for the given promotion
   * 
   * @param promotionId
   * @param associationRequestCollection
   * @return List
   */
  public List getPromoMerchCountriesByPromotionId( Long promotionId, AssociationRequestCollection associationRequestCollection );

  /**
   * Save the promoMerchCountry object
   * 
   * @param promoMerchCountry
   * @return
   */
  public PromoMerchCountry savePromoMerchCountry( PromoMerchCountry promoMerchCountry ) throws ServiceErrorException;

  /**
   * Get a list of all promotion Merchandise Countries where the promotion recipient audience is from that country
   *        and the country is active
   *        
   * There are 2 ways this function will get called:
   *    1.  When we don't know the last 4 parameters, primary and secondary audiences for a promotion - so 
   *                        we need to get those values from the DB for a promotion
   *    2.  From the Screen, we'll have temporary variables for the last 4 parameters - they are not saved
   *                        with the promotion yet, that's why we have to pass them in.
  
   * 
   * @param promotionId
   * @return List of active countries based on the promotion receivers audience
   */
  public List getActiveCountriesInPromoRecAudience( Long promotionId,
                                                    PrimaryAudienceType primaryAudienceType,
                                                    SecondaryAudienceType secondaryAudienceType,
                                                    Set primaryAudiences,
                                                    Set secondaryAudiences );

  /**
   * Get a list of all promotion Merchandise Countries where the promotion recipient audience is not from that country
   *        and the country is active
   *        
   * There are 2 ways this function will get called:
   *    1.  When we don't know the last 4 parameters, primary and secondary audiences for a promotion - so 
   *                        we need to get those values from the DB for a promotion
   *    2.  From the Screen, we'll have temporary variables for the last 4 parameters - they are not saved
   *                        with the promotion yet, that's why we have to pass them in.
   *                        
   * @param promotionId
   * @return
   */
  public List getActiveCountriesNotInPromoRecAudience( Long promotionId,
                                                       PrimaryAudienceType primaryAudienceType,
                                                       SecondaryAudienceType secondaryAudienceType,
                                                       Set primaryAudiences,
                                                       Set secondaryAudiences );

  /**
   * For performance purposes, call this function if we have already called getActiveCountriesInPromoRecAudience.  Pass in 
   *        the result from getActiveCountriesInPromoRecAudience.  
   * 
   * Get a list of all promotion Merchandise Countries where the promotion recipient audience is not from that country
   *        and the country is active
   * 
   * @param promotion
   * @param promoMerchCountriesInRecAudience
   * @return
   */
  public List getActiveCountriesNotInPromoRecAudience( Promotion promotion, List promoMerchCountriesInRecAudience );

  /**
   * delete all entries in promomerchprogramlevel that are mapped to this promo merch country
   * @param promoMerchCountry
   */
  public void deleteProgramLevels( PromoMerchCountry promoMerchCountry );

  /**
   * Validates all basic promotion information has been provided
   * 
   * @param promotion
   * @return boolean
   */
  public boolean validateAndDeleteInvalidProgramLevels( Promotion promotion, boolean checkIfLabelAbsent ) throws ServiceErrorException;

  public List getSSIAwardPromoMerchCountry( Long promotionId );

  public String saveLevelCmText( String levelCode, String levelName, Locale locale ) throws ServiceErrorException;
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/maincontent/MainContentService.java,v $
 */

package com.biperf.core.service.maincontent;

import java.util.List;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.SAO;
import com.biperf.core.value.BudgetMeter;
import com.biperf.core.value.DailyTipValueBean;
import com.biperf.core.value.PromotionMenuBean;

/**
 * MainContentService to capture the data necessary for header and footer of every page.
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
 * <td>sharma</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface MainContentService extends SAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------
  /**
   * Static final for the BEAN_NAME for use in the applicationContext.xml
   */
  public static final String BEAN_NAME = "mainContentService";

  // ---------------------------------------------------------------------------
  // Login/Logout Methods
  // ---------------------------------------------------------------------------
  /**
   * Flushs from caches objects associated with the specified user.
   * 
   * @param authenticatedUser
   */
  public void processLogout( AuthenticatedUser authenticatedUser );

  // ---------------------------------------------------------------------------
  // Menu Methods
  // ---------------------------------------------------------------------------
  /**
   * Clear the menu cache, which will be reloaded on next menu build call.
   * 
   * @param authenticatedUser
   */
  public void clearMenuCache( AuthenticatedUser authenticatedUser );

  public void clearBudgetTrackerCache( AuthenticatedUser authenticatedUser );

  /**
   * Get the menus for the user/participant
   * @param authenticatedUser
   * @param eligiblePromoList
   * @return List of Menu objects
   */
  public List getUserMenus( AuthenticatedUser authenticatedUser, List eligiblePromoList );

  /* eligible promotion cache methods */
  public void refreshAllLivePromotionCache();

  public List<Promotion> getAllLivePromotionsFromCache();

  /**
   * Get the list of eligible promotions user/participant
   * 
   * @param authenticatedUser
   * @return List or Promotion objects
   */
  public List<PromotionMenuBean> buildEligiblePromoList( AuthenticatedUser authenticatedUser );

  /**
   * Get the list of eligible promotions user/participant
   * 
   * @param authenticatedUser
   * @return List or Promotion objects
   */
  public List buildEligiblePromoList( AuthenticatedUser authenticatedUser, boolean activityFlag );

  /**
   * Get the list of What's New products for the pax
   * 
   * @param eligiblePromotionList
   * @param country
   * @return List of HomePageItem
   */
  public List getWhatsNewList( List eligiblePromotionList, Country country );

  public List getMerchAwardRemindersForAcivityList( Long participantId, List eligiblePromotions, Country country );

  public List<DailyTipValueBean> getDailyTips();

  public List getReportsAccessibleToUser();

  public List getAllReports();

  public BudgetMeter getBudgetMeter( Long participantId, String timezone, List<PromotionMenuBean> eligiblePromotions );

  public boolean isParticipantHasBudgetMeter( Long participantId, List<PromotionMenuBean> eligiblePromotions );

  public boolean checkIfReceivable( Promotion promotion, Participant participant );

  /**
   * Builds the G3 Redux Admin Menu Page
   * @return pax admin Menu
   */
  public Menu buildG3ReduxParticipantAdminMenuPage();

  // public String getWellnessEncodedUrl();

  public boolean checkShowShopORTravel();
  
  /*START Client customization (home page tuning)*/
  List<PromotionMenuBean> buildOnlineEligiblePromoList( AuthenticatedUser authenticatedUser );
  
  public List getPublicRecognitionDepartmentCache();
  /*END Client customization (home page tuning)*/

}

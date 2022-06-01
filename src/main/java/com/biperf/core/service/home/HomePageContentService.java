/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/home/HomePageContentService.java,v $
 */

package com.biperf.core.service.home;

import java.util.List;

import com.biperf.core.domain.banner.GenericBannerDetailBean;
import com.biperf.core.service.SAO;
import com.objectpartners.cms.domain.Content;

/**
 * Home page Content Service interface.
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
 * <td>waldal</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface HomePageContentService extends SAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Static final for the BEAN_NAME for use in the applicationContext.xml
   */
  public static final String BEAN_NAME = "homePageContentService";

  // ---------------------------------------------------------------------------
  // Widget Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns active instant polls available to the current user's audiences.
   * 
   * @return active instant polls available to the current user's audiences, as a List of Content
   *         objects.
   */
  public List getInstantPolls();

  /**
   * Get the navigation keys for the menu.
   * 
   * @return String
   */
  public String getNavigationAssetKey();

  /**
   * Get User Logo asset key for the CM by a users segment or heirarchy.
   * 
   * @return String Logo asset key for ContentManager
   */
  public String getWelcomeMessageAssetKey();

  public List getGenericBannerAds();

  public List<Content> getGenericBannerAdsForSlides();

  public List<GenericBannerDetailBean> getGenericBannerSlides( String contextPath );

}

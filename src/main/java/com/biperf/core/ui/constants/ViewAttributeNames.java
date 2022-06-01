/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/constants/ViewAttributeNames.java,v $
 */

package com.biperf.core.ui.constants;

/**
 * This class is a Constants container for all constants used by controllers and other view classes.
 * Usage: prefix all ComponentContext attributes with c_, and all session scoped attributes with s_
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
 * <td>Apr 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ViewAttributeNames
{
  // User name
  public static final String USER_NAME = "s_user_name";

  public static final String INSTANT_POLL = "c_intant_poll";
  public static final String FEATURED_AWARDS_ASSET_KEY = "c_featured_awards_asset_key";
  public static final String RANKINGS_LIST = "c_rankigs_list";
  public static final String NAVIGATION_ASSET_KEYS = "c_navigation_asset_keys";
  public static final String MYGOAL_ASSET_KEY = "c_my_goal_asset_key";
  public static final String USER_BANK_BALANCE = "c_user_bank_balance";
  public static final String USER_NEWS_ASSET_KEY = "c_user_news_asset_key";
  public static final String MY_FAVORITES_LIST = "c_my_favorites_list";

  // Forgot User Constants
  public static String FORGOT_PWD_USER = "s_forgot_pwd_user";

  // Registration Code Constants
  public static String REGISTRATION_CODE = "s_registration_code";
  public static String REGISTRATION_PROMOTION = "s_registration_promotion";
  public static String REGISTRATION_AUDIENCE = "s_registration_audience";
  public static String REGISTRATION_NODE = "s_registration_node";

  public static final String TOP_NAV_MENUS = "topNavMenus";

  public static final String SEARCH_ALL_OPTION = "-987654321";
  public static final Long SEARCH_ALL_OPTION_LONG = new Long( SEARCH_ALL_OPTION );

  // PageMode for promotion wizard/stand alone functionality
  public static final String PAGE_MODE = "s_pageMode";
  public static final String WIZARD_MODE = "c_wizard";
  public static final String NORMAL_MODE = "c_normal";
  public static final String SWEEPS_MODE = "c_sweeps";

}

/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/SelectGoalUtil.java,v $
 */

package com.biperf.core.utils;

import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.cms.CMAssetService;

/**
 * ChallengepointUtil.
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
 * <td>dudam</td>
 * <td>Dec 29, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * This class is created as part of WIP #40866 fix
 *
 */
public class ChallengepointUtil
{

  public static String getGoalLevelName( String cmAssetCode, Locale locale )
  {
    String goalLevelName = getCMAssetService().getString( cmAssetCode, Promotion.CM_GOALS_KEY, locale, true );
    if ( goalLevelName.startsWith( "???" ) )
    {
      goalLevelName = "";
    }
    return goalLevelName;
  }

  public static String getGoalLevelDescription( String cmAssetCode, Locale locale )
  {
    String goalLevelDescription = getCMAssetService().getString( cmAssetCode, Promotion.CM_GOAL_DESCRIPTION_KEY, locale, true );
    if ( goalLevelDescription.startsWith( "???" ) )
    {
      goalLevelDescription = "";
    }
    return goalLevelDescription;
  }

  public static String getBaseUnitText( String baseUnitValue, Locale locale )
  {
    String baseUnit = null;
    if ( baseUnitValue != null )
    {
      baseUnit = getCMAssetService().getString( baseUnitValue, Promotion.GQ_CP_PROMO_BASE_UNIT_KEY_PREFIX, locale, true );
    }
    return StringEscapeUtils.unescapeHtml4( baseUnit );
  }

  private static CMAssetService getCMAssetService()
  {
    return (CMAssetService)ServiceLocator.getService( CMAssetService.BEAN_NAME );
  }

}

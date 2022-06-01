/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/homepage/HomePageUserInfoController.java,v $
 */

package com.biperf.core.ui.homepage;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.home.HomePageContentService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

/**
 * HomePageUserInfoController.
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
 * <td>sedey</td>
 * <td>Sept,25 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HomePageUserInfoController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */

  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    HomePageContentService homePageService = (HomePageContentService)getService( HomePageContentService.BEAN_NAME );

    // Build a list of eligible promotions if the user is a participant, then put that list
    // in the request so other controllers used in building the home page can use the list from
    // the request instead of getting the list again.
    if ( UserManager.getUser().isParticipant() )
    {
      List eligiblePromoList = getEligiblePromotions( request );
      request.getSession().setAttribute( "eligiblePromotions", eligiblePromoList );

      UserAddress address = getUserService().getPrimaryUserAddress( UserManager.getUserId() );
      if ( address != null && address.getAddress() != null )
      {
        Country country = address.getAddress().getCountry();
        request.setAttribute( "paxPrimaryCountry", country );
      }
    }

    context.putAttribute( "userLogoAssetKey", homePageService.getWelcomeMessageAssetKey() );
    /*
     * request.setAttribute( "userLanguage", LanguageType.lookup( "" +
     * request.getSession().getAttribute( "language" ) ) );
     */
    request.setAttribute( "userLanguage", LanguageType.lookup( "" + request.getSession().getAttribute( "cmsLocaleCode" ) ) );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

}

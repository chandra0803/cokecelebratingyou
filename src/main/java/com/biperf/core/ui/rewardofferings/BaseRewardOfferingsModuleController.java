
package com.biperf.core.ui.rewardofferings;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.shop.RewardOfferingView;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.rewardoffering.RewardOfferingsService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.services.rest.rewardoffering.domain.RewardOffering;

public abstract class BaseRewardOfferingsModuleController extends BaseController
{
  public abstract String getTargetRewardOfferingType();

  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    RewardOfferingView rewardOfferingView = null;
    String programId = getUserService().getCountryProgramIdByUserId( UserManager.getUserId() );
    if ( programId != null )
    {
      List<RewardOffering> rewardOfferings = getRewardOfferingsService().getRewardOfferings( programId );
      if ( rewardOfferings != null )
      {
        for ( RewardOffering rewardOffering : rewardOfferings )
        {
          if ( getTargetRewardOfferingType().equalsIgnoreCase( rewardOffering.getType() ) )
          {
            // Reward offering type
            rewardOfferingView = new RewardOfferingView();
            rewardOfferingView.setType( rewardOffering.getType() );

            // Reward offering page
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put( "page", rewardOffering.getSsoDestination() );
            rewardOfferingView.setUrlString( ClientStateUtils.generateEncodedLink( "", "submitRewardOffering.do?method=displayInternal", parameterMap ) );
          }
        }
      }
    }
    request.setAttribute( "rewardOfferingView", rewardOfferingView );
  }

  private RewardOfferingsService getRewardOfferingsService()
  {
    return (RewardOfferingsService)getService( RewardOfferingsService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  public Locale buildLocale( String locale )
  {
    if ( locale == null )
    {
      return Locale.getDefault();
    }
    else
    {
      int index = locale.indexOf( '_' );
      if ( index == -1 )
      {
        return new Locale( locale );
      }
      String language = locale.substring( 0, index );
      String rest = locale.substring( index + 1 );
      index = rest.indexOf( '_' );
      if ( index == -1 )
      {
        return new Locale( language, rest );
      }
      String country = rest.substring( 0, index );
      rest = rest.substring( index + 1 );
      return new Locale( language, country, rest );
    }
  }
}

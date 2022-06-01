
package com.biperf.core.ui.homepage;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.banner.GenericBannerDetailBean;
import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.news.NewsDetailsView;
import com.biperf.core.service.diycommunications.ParticipantDIYCommunicationsService;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.home.HomePageContentService;
import com.biperf.core.ui.ResponseEntity;
import com.biperf.core.ui.SpringBaseController;

@Controller
@RequestMapping( "/xpromo" )
public class XPromotionalController extends SpringBaseController
{
  private static final Log LOGGER = LogFactory.getLog( XPromotionalController.class );

  @Autowired
  private FilterAppSetupService filterAppSetupService;
  @Autowired
  private HomePageContentService homePageContentService;
  @Autowired
  private ParticipantDIYCommunicationsService participantDIYCommunicationsService;

  @RequestMapping( value = "/content.action", method = { RequestMethod.POST } )
  public @ResponseBody XPromotionalView getCrossPromotionalContent( HttpServletRequest request ) throws Exception
  {
    XPromotionalView view = new XPromotionalView();
    List<CrossPromotionalModuleApp> xPromos = filterAppSetupService.getCrossPromotionalModuleApps();

    for ( CrossPromotionalModuleApp xPromo : xPromos )
    {
      // added order condition where in case the order is greater than 0 select only those cross
      // promotions
      if ( xPromo != null && xPromo.getOrder() > 0 && xPromo.getTileMappingType().getCode().equals( TileMappingType.NEWS ) )
      {
        NewsDetailsView news = getPrimaryNewsItem( request );
        if ( null != news )
        {
          view.getXpromos().add( new XPromotionalNewsView( news ) );
        }
      }
      else if ( xPromo != null && xPromo.getOrder() > 0 && xPromo.getTileMappingType().getCode().equals( TileMappingType.SHOP_BANNERS ) )
      {
        GenericBannerDetailBean banner = getPrimaryBannerAd( request );
        if ( null != banner )
        {
          view.getXpromos().add( new XPromotionalBannerAdsView( banner ) );
        }
      }
    }

    return view;
  }

  private NewsDetailsView getPrimaryNewsItem( HttpServletRequest request ) throws Exception
  {
    List<NewsDetailsView> newsItems = getNews( request );
    if ( !newsItems.isEmpty() )
    {
      return newsItems.get( 0 );
    }
    else
    {
      return null;
    }
  }

  private GenericBannerDetailBean getPrimaryBannerAd( HttpServletRequest request ) throws Exception
  {
    List<GenericBannerDetailBean> bannerAds = getBannerAds( request );
    if ( !bannerAds.isEmpty() )
    {
      return bannerAds.get( 0 );
    }
    else
    {
      return null;
    }
  }

  @RequestMapping( value = "/bannerAds.action", method = RequestMethod.GET )
  public @ResponseBody List<GenericBannerDetailBean> getBannerAds( HttpServletRequest request ) throws Exception
  {
    return homePageContentService.getGenericBannerSlides( request.getContextPath() );
  }

  @RequestMapping( value = "/news.action", method = RequestMethod.GET )
  public @ResponseBody List<NewsDetailsView> getNews( HttpServletRequest request ) throws Exception
  {
    return participantDIYCommunicationsService.getNewsList( request.getContextPath() );
  }

  @ExceptionHandler( Exception.class )
  @ResponseStatus( value = INTERNAL_SERVER_ERROR )
  public @ResponseBody ResponseEntity<List<WebErrorMessage>, Object> handleInternalException( HttpServletRequest request, Exception ex )
  {
    LOGGER.error( "Requested URL=" + request.getRequestURL(), ex );
    return new ResponseEntity<List<WebErrorMessage>, Object>( buildAppExceptionMessage(), null );
  }

}

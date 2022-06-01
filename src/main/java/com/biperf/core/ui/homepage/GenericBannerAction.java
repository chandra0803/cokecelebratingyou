
package com.biperf.core.ui.homepage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.banner.GenericBannerDetailBean;
import com.biperf.core.domain.banner.GenericBannerDetailsView;
import com.biperf.core.domain.homepage.HeroImageDetailBean;
import com.biperf.core.domain.homepage.HeroImageDetailsView;
import com.biperf.core.service.home.HomePageContentService;
import com.biperf.core.ui.utils.RequestUtils;
import com.objectpartners.cms.util.ContentReaderManager;

public class GenericBannerAction extends BaseXPromotionalAction
{
  @SuppressWarnings( "unchecked" )
  public ActionForward fetchBannersForTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    GenericBannerDetailsView genericBannerDetailsView = new GenericBannerDetailsView();

    List<GenericBannerDetailBean> bannerAdds = getHomePageContentService().getGenericBannerSlides( RequestUtils.getBaseURI( request ) );
    if ( isXpromotionEnabled( request ) )
    {
      bannerAdds = (List<GenericBannerDetailBean>)shiftCollection( bannerAdds );
    }
    genericBannerDetailsView.setSlides( bannerAdds );

    writeAsJsonToResponse( genericBannerDetailsView, response );
    return null;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward fetchHeroImage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    HeroImageDetailsView heroImageDetailsView = new HeroImageDetailsView();

    String heroImage = ContentReaderManager.getText( "participant.search.view", "BILL_BOARD_IMAGE" );
    HeroImageDetailBean heroImageDetailBean = new HeroImageDetailBean();
    heroImageDetailBean.setHeroImageUrl( heroImage );

    List<HeroImageDetailBean> heroImages = new ArrayList<HeroImageDetailBean>();
    heroImages.add( heroImageDetailBean );

    heroImageDetailsView.setSlides( heroImages );
    writeAsJsonToResponse( heroImageDetailsView, response );
    return null;
  }

  private static HomePageContentService getHomePageContentService()
  {
    return (HomePageContentService)getService( HomePageContentService.BEAN_NAME );
  }
}


package com.biperf.core.ui.homepage;

import com.biperf.core.domain.banner.GenericBannerDetailBean;

public class XPromotionalBannerAdsView extends XPromotionalObject
{
  private GenericBannerDetailBean data = null;

  public XPromotionalBannerAdsView()
  {
    setType( "banner" );
  }

  public XPromotionalBannerAdsView( GenericBannerDetailBean bannerAd )
  {
    setType( "banner" );
    this.data = bannerAd;
  }

  public GenericBannerDetailBean getData()
  {
    return data;
  }

  public void setData( GenericBannerDetailBean bannerAd )
  {
    this.data = bannerAd;
  }
}

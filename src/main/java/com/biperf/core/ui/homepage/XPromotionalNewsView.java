
package com.biperf.core.ui.homepage;

import com.biperf.core.domain.news.NewsDetailsView;

public class XPromotionalNewsView extends XPromotionalObject
{
  private NewsDetailsView data = null;

  public XPromotionalNewsView()
  {
    setType( "news" );
  }

  public XPromotionalNewsView( NewsDetailsView newsItem )
  {
    setType( "news" );
    this.data = newsItem;
  }

  public NewsDetailsView getData()
  {
    return data;
  }

  public void setData( NewsDetailsView newsItem )
  {
    this.data = newsItem;
  }
}

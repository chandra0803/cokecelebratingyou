
package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

public class CommunicationsNewsData
{
  private static final long serialVersionUID = 1L;

  private NewsTableBean newsTable;
  private String newsTitle;
  private String newsStartDate;
  private String newsEndDate;
  private NewsImages newsImages;

  public CommunicationsNewsData()
  {
  }

  public CommunicationsNewsData( String siteUrlPrefix, List<ResourceList> newsContents )
  {
    newsTable = new NewsTableBean();
    this.newsImages = new NewsImages( siteUrlPrefix, newsContents );
  }

  public CommunicationsNewsData( DIYCommunications communications, List<ResourceList> newsContents, String siteUrlPrefix )
  {
    this.newsTitle = communications.getContentTitle();
    this.newsStartDate = DateUtils.toDisplayDateString( communications.getStartDate(), UserManager.getLocale() );
    this.newsEndDate = DateUtils.toDisplayDateString( communications.getEndDate(), UserManager.getLocale() );
    this.newsTable = new NewsTableBean( newsContents );
    this.newsImages = new NewsImages( siteUrlPrefix, newsContents );

  }

  @JsonProperty( "newsTable" )
  public NewsTableBean getNewsTable()
  {
    return newsTable;
  }

  public void setNewsTable( NewsTableBean newsTable )
  {
    this.newsTable = newsTable;
  }

  @JsonProperty( "newsTitle" )
  public String getNewsTitle()
  {
    return newsTitle;
  }

  public void setNewsTitle( String newsTitle )
  {
    this.newsTitle = newsTitle;
  }

  @JsonProperty( "newsStartDate" )
  public String getNewsStartDate()
  {
    return newsStartDate;
  }

  public void setNewsStartDate( String newsStartDate )
  {
    this.newsStartDate = newsStartDate;
  }

  @JsonProperty( "newsEndDate" )
  public String getNewsEndDate()
  {
    return newsEndDate;
  }

  public void setNewsEndDate( String newsEndDate )
  {
    this.newsEndDate = newsEndDate;
  }

  @JsonProperty( "newsImages" )
  public NewsImages getNewsImages()
  {
    return newsImages;
  }

  public void setNewsImages( NewsImages newsImages )
  {
    this.newsImages = newsImages;
  }

  public class NewsTableBean
  {
    private List<ResourceList> news = new ArrayList<ResourceList>();

    public NewsTableBean()
    {

    }

    public NewsTableBean( List<ResourceList> newsContents )
    {
      this.news.addAll( newsContents );
    }

    public List<ResourceList> getNews()
    {
      return news;
    }

    public void setNews( List<ResourceList> news )
    {
      this.news = news;
    }

  }

  public class NewsImages
  {
    private List<CommunicationsImage> images = new ArrayList<CommunicationsImage>();

    public NewsImages()
    {

    }

    public NewsImages( String siteUrlPrefix, List<ResourceList> newsContents )
    {
      CommunicationsImage bannerImage = null;

      List<Content> contents = (List)ContentReaderManager.getContentReader().getContent( "diyCommunications.defaultNews" );
      if ( contents != null )
      {
        for ( Content newsContent : contents )
        {
          if ( newsContent.getContentKey() != null && newsContent.getContentKey().getId() != null )
          {
            Map<String, String> contentDataMap = newsContent.getContentDataMap();
            String imageUrl = contentDataMap.get( DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE );
            String imageUrlMax = contentDataMap.get( DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE_MAX );
            String imageUrlMobile = contentDataMap.get( DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE_MOBILE );

            bannerImage = new CommunicationsImage();
            bannerImage.setId( newsContent.getContentKey().getId().toString() + newsContent.getLocale().toString() );
            bannerImage.setImageSize( siteUrlPrefix + "/" + imageUrl );
            bannerImage.setImageSizeMax( siteUrlPrefix + "/" + imageUrlMax );
            bannerImage.setImageSizeMobile( siteUrlPrefix + "/" + imageUrlMobile );

            this.images.add( bannerImage );
          }
        }
      }

      if ( newsContents != null )
      {
        for ( ResourceList resource : newsContents )
        {
          bannerImage = new CommunicationsImage();
          bannerImage.setId( resource.getImageId() );
          bannerImage.setImageSize( resource.getImageSize() );
          bannerImage.setImageSizeMax( resource.getImageSize_max() );
          bannerImage.setImageSizeMobile( resource.getImageSize_mobile() );
          this.images.add( bannerImage );
        }
      }
    }

    public void setImages( List<CommunicationsImage> images )
    {
      this.images = images;
    }

    public List<CommunicationsImage> getImages()
    {
      return images;
    }

  }

}


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

public class CommunicationsBannerData
{
  private static final long serialVersionUID = 1L;

  private BannerTableBean bannerTable;
  private String bannerTitle;
  private String bannerStartDate;
  private String bannerEndDate;
  private BannerImages bannerImages;

  public CommunicationsBannerData()
  {
  }

  public CommunicationsBannerData( String siteUrlPrefix )
  {
    bannerTable = new BannerTableBean();
    this.bannerImages = new BannerImages( siteUrlPrefix );
  }

  public CommunicationsBannerData( DIYCommunications communications, List<ResourceList> bannerContents, String siteUrlPrefix )
  {
    this.bannerTitle = communications.getContentTitle();
    this.bannerStartDate = DateUtils.toDisplayDateString( communications.getStartDate(), UserManager.getLocale() );
    this.bannerEndDate = DateUtils.toDisplayDateString( communications.getEndDate(), UserManager.getLocale() );
    this.bannerTable = new BannerTableBean( bannerContents );
    this.bannerImages = new BannerImages( siteUrlPrefix );

  }

  @JsonProperty( "bannerTable" )
  public BannerTableBean getBannerTable()
  {
    return bannerTable;
  }

  public void setBannerTable( BannerTableBean bannerTable )
  {
    this.bannerTable = bannerTable;
  }

  @JsonProperty( "bannerTitle" )
  public String getBannerTitle()
  {
    return bannerTitle;
  }

  public void setBannerTitle( String bannerTitle )
  {
    this.bannerTitle = bannerTitle;
  }

  @JsonProperty( "bannerStartDate" )
  public String getBannerStartDate()
  {
    return bannerStartDate;
  }

  public void setBannerStartDate( String bannerStartDate )
  {
    this.bannerStartDate = bannerStartDate;
  }

  @JsonProperty( "bannerEndDate" )
  public String getBannerEndDate()
  {
    return bannerEndDate;
  }

  public void setBannerEndDate( String bannerEndDate )
  {
    this.bannerEndDate = bannerEndDate;
  }

  @JsonProperty( "bannerImages" )
  public BannerImages getBannerImages()
  {
    return bannerImages;
  }

  public void setBannerImages( BannerImages bannerImages )
  {
    this.bannerImages = bannerImages;
  }

  public class BannerTableBean
  {
    private List<ResourceList> banners = new ArrayList<ResourceList>();

    public BannerTableBean()
    {

    }

    public BannerTableBean( List<ResourceList> bannerContents )
    {
      this.banners.addAll( bannerContents );
    }

    public List<ResourceList> getBanners()
    {
      return banners;
    }

    public void setBanners( List<ResourceList> banners )
    {
      this.banners = banners;
    }

  }

  public class BannerImages
  {
    private List<CommunicationsImage> images = new ArrayList<CommunicationsImage>();

    public BannerImages()
    {

    }

    public BannerImages( String siteUrlPrefix )
    {
      CommunicationsImage bannerImage = null;

      List<Content> contents = (List)ContentReaderManager.getContentReader().getContent( "diyCommunications.defaultBanners" );
      if ( contents != null )
      {
        for ( Content imageContent : contents )
        {
          if ( imageContent.getContentKey() != null && imageContent.getContentKey().getId() != null )
          {
            Map<String, String> contentDataMap = imageContent.getContentDataMap();
            String imageUrl = contentDataMap.get( DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE );
            String imageUrlMax = contentDataMap.get( DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE_MAX );
            String imageUrlMobile = contentDataMap.get( DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE_MOBILE );

            bannerImage = new CommunicationsImage();
            bannerImage.setId( imageContent.getContentKey().getId().toString() + imageContent.getLocale().toString() );
            bannerImage.setImageSize( siteUrlPrefix + "/" + imageUrl );
            bannerImage.setImageSizeMax( siteUrlPrefix + "/" + imageUrlMax );
            bannerImage.setImageSizeMobile( siteUrlPrefix + "/" + imageUrlMobile );

            this.images.add( bannerImage );
          }
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

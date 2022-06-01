
package com.biperf.core.domain.diycommunications;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

public class DIYCommunications extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  private User managerUser;
  private String communicationType;
  private Date startDate;
  private Date endDate;
  private String cmAssetCode;
  private String contentTitle;

  public static final String COMMUNICATION_TYPE_BANNER = "banner";
  public static final String COMMUNICATION_TYPE_RESOURCE_CENTER = "resourcecenter";
  public static final String COMMUNICATION_TYPE_NEWS = "news";
  public static final String COMMUNICATION_TYPE_TIPS = "tips";
  public static final String SOURCE_LOCALE = "SOURCE_LOCALE";

  // references of assets and asset type specific to Resource Center
  public static final String DIY_RESOURCE_SECTION_CODE = "diy_resourcecenter";
  public static final String DIY_RESOURCE_CMASSET_TYPE_NAME = "DIY Communication Resource Center";
  public static final String DIY_RESOURCE_CMASSET_NAME = "DIY Communications Resource Center Data";
  public static final String DIY_RESOURCE_CMASSET_PREFIX = "diy_resourcecenter.resourcecontent";
  public static final String DIY_RESOURCE_CMASSET_LINK_TITLE = "TEXT";
  public static final String DIY_RESOURCE_CMASSET_DOC_PATH = "TYPE";
  public static final String DIY_RESOURCE_CMASSET_LINK_URL = "URL";

  // references of assets and asset type specific to Banners
  public static final String DIY_BANNER_SECTION_CODE = "diy_banner";
  public static final String DIY_BANNER_CMASSET_TYPE_NAME = "DIY Communication Banner";
  public static final String DIY_BANNER_CMASSET_NAME = "DIY Communications Banner Data";
  public static final String DIY_BANNER_CMASSET_PREFIX = "diy_banner.content";
  // public static final String DIY_BANNER_CMASSET_IMAGESIZE2X1 = "BANNER_IMAGE_URL_2X1";
  // public static final String DIY_BANNER_CMASSET_IMAGESIZE2X2 = "BANNER_IMAGE_URL_2X2";
  // public static final String DIY_BANNER_CMASSET_IMAGESIZE4X2 = "BANNER_IMAGE_URL_4X2";
  // public static final String DIY_BANNER_CMASSET_IMAGESIZE4X4 = "BANNER_IMAGE_URL_4X4";

  public static final String DIY_BANNER_CMASSET_IMAGESIZE = "BANNER_IMAGE";
  public static final String DIY_BANNER_CMASSET_IMAGESIZE_MAX = "BANNER_IMAGE_MAX";
  public static final String DIY_BANNER_CMASSET_IMAGESIZE_MOBILE = "BANNER_IMAGE_MOBILE";

  public static final String DIY_BANNER_CMASSET_LINK_URL = "LINK_URL";
  public static final String DIY_BANNER_CMASSET_TARGET = "TARGET";
  public static final String DIY_BANNER_CMASSET_REFERENCE_CONTENTKEY = "REFERENCE_BANNER_CONTENTKEY";

  // references of assets and asset type specific to News
  public static final String DIY_NEWS_SECTION_CODE = "diy_news";
  public static final String DIY_NEWS_CMASSET_TYPE_NAME = "DIY Communication News";
  public static final String DIY_NEWS_CMASSET_NAME = "DIY Communications News Data";
  public static final String DIY_NEWS_CMASSET_PREFIX = "diy_news.content";
  public static final String DIY_NEWS_CMASSET_HEADLINE = "TITLE";
  public static final String DIY_NEWS_CMASSET_UNIQUE_NEWS_ID = "UNIQUE_NEWS_ID";
  public static final String DIY_NEWS_CMASSET_STORY_CONTENT = "FULL_TXT";
  public static final String DIY_NEWS_CMASSET_START_DATE = "START_DATE";
  public static final String DIY_NEWS_CMASSET_END_DATE = "END_DATE";
  /*
   * public static final String DIY_NEWS_CMASSET_IMAGESIZE2X1 = "STORY_IMAGE_URL_LIST_PAGE"; public
   * static final String DIY_NEWS_CMASSET_IMAGESIZE2X2 = "STORY_IMAGE_URL_2x2"; public static final
   * String DIY_NEWS_CMASSET_IMAGESIZE4X2 = "STORY_IMAGE_URL_4x2"; public static final String
   * DIY_NEWS_CMASSET_IMAGESIZE4X4 = "STORY_IMAGE_URL_4x4";
   */

  public static final String DIY_NEWS_CMASSET_IMAGESIZE = "NEWS_IMAGE";
  public static final String DIY_NEWS_CMASSET_IMAGESIZE_MAX = "NEWS_IMAGE_MAX";
  public static final String DIY_NEWS_CMASSET_IMAGESIZE_MOBILE = "NEWS_IMAGE_MOBILE";

  public static final String DIY_NEWS_CMASSET_IMAGESIZE162x162 = "STORY_IMAGE_URL_162x162";
  public static final String DIY_NEWS_CMASSET_REFERENCE_CONTENTKEY = "REFERENCE_NEWS_CONTENTKEY";

  public static final String IMAGE_4X4 = "imageSize4x4";
  public static final String IMAGE_4X2 = "imageSize4x2";
  public static final String IMAGE_2X2 = "imageSize2x2";
  public static final String IMAGE_2X1 = "imageSize2x1";
  public static final String IMAGE_162X162 = "imageSize162x162";

  public static final String DIY_TIPS_SECTION_CODE = "diy_tips";
  public static final String DIY_TIPS_CMASSET_TYPE_NAME = "DIY Communications Tips";
  public static final String DIY_TIPS_CMASSET_TITLE = "TITLE";
  public static final String DIY_TIPS_CMASSET_NAME = "DIY Communications Tips Data";
  public static final String DIY_TIPS_CMASSET_PREFIX = "diy_tips.tipscontent";

  public static final String SESSION_BANNER_FORM = "diyCommunicationsBannerForm";
  public static final String SESSION_RESOURCE_CENTER_FORM = "diyCommunicationsResourceCenterForm";
  public static final String SESSION_NEWS_FORM = "diyCommunicationsNewsForm";
  public static final String SESSION_TIPS_FORM = "diyCommunicationsTipsForm";

  public User getManagerUser()
  {
    return managerUser;
  }

  public void setManagerUser( User managerUser )
  {
    this.managerUser = managerUser;
  }

  public String getCommunicationType()
  {
    return communicationType;
  }

  public void setCommunicationType( String communicationType )
  {
    this.communicationType = communicationType;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getContentTitle()
  {
    return contentTitle;
  }

  public void setContentTitle( String contentTitle )
  {
    this.contentTitle = contentTitle;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof DIYCommunications ) )
    {
      return false;
    }

    DIYCommunications diyCommunications = (DIYCommunications)object;

    if ( this.getId() != null && this.getId().equals( diyCommunications.getId() ) )
    {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

}

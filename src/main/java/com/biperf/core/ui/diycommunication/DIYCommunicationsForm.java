
package com.biperf.core.ui.diycommunication;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.RequestUtils;

public class DIYCommunicationsForm extends BaseForm
{
  public static final String SESSION_KEY = "diyCommunicationsForm";

  private boolean diyBannersAvailable;
  private boolean diyNewsStoriesAvailable;
  private boolean diyResourceCenterAvailable;
  private boolean diyTipsAvailable;

  private String diyBannersAudienceId;
  private String[] deleteDIYBannersAudience;
  private List<PromotionAudienceFormBean> diyBannersAudienceList = new ArrayList<PromotionAudienceFormBean>();

  private String diyNewsStoriesAudienceId;
  private String[] deleteDIYNewsAudience;
  private List<PromotionAudienceFormBean> diyNewsStoriesAudienceList = new ArrayList<PromotionAudienceFormBean>();

  private String diyResourceCenterAudienceId;
  private String[] deleteDIYResourceCenterAudience;
  private List<PromotionAudienceFormBean> diyResourceCenterAudienceList = new ArrayList<PromotionAudienceFormBean>();

  private String diyTipsAudienceId;
  private String[] deleteDIYTipsAudience;
  private List<PromotionAudienceFormBean> diyTipsAudienceList = new ArrayList<PromotionAudienceFormBean>();

  private String method;

  private String bannerAudienceName = "DIY_BANNER_ADMIN";
  private String newsAudienceName = "DIY_NEWS_ADMIN";
  private String resourceAudienceName = "DIY_RESOURCE_ADMIN";
  private String tipsAudienceName = "DIY_TIPS_ADMIN";

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    if ( this.diyBannersAvailable && diyBannersAudienceList.isEmpty() )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.diy.communication.setup.DIY_BANNER_AUDIENCE_REQUIRED" ) );
    }

    if ( this.diyNewsStoriesAvailable && diyNewsStoriesAudienceList.isEmpty() )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.diy.communication.setup.DIY_NEWS_AUDIENCE_REQUIRED" ) );
    }

    if ( this.diyResourceCenterAvailable && diyResourceCenterAudienceList.isEmpty() )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.diy.communication.setup.DIY_RESOURCE_AUDIENCE_REQUIRED" ) );
    }

    if ( this.diyTipsAvailable && diyTipsAudienceList.isEmpty() )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.diy.communication.setup.DIY_TIPS_AUDIENCE_REQUIRED" ) );
    }

    return errors;
  }

  public boolean isDiyBannersAvailable()
  {
    return diyBannersAvailable;
  }

  public void setDiyBannersAvailable( boolean diyBannersAvailable )
  {
    this.diyBannersAvailable = diyBannersAvailable;
  }

  public boolean isDiyNewsStoriesAvailable()
  {
    return diyNewsStoriesAvailable;
  }

  public void setDiyNewsStoriesAvailable( boolean diyNewsStoriesAvailable )
  {
    this.diyNewsStoriesAvailable = diyNewsStoriesAvailable;
  }

  public boolean isDiyResourceCenterAvailable()
  {
    return diyResourceCenterAvailable;
  }

  public void setDiyResourceCenterAvailable( boolean diyResourceCenterAvailable )
  {
    this.diyResourceCenterAvailable = diyResourceCenterAvailable;
  }

  public boolean isDiyTipsAvailable()
  {
    return diyTipsAvailable;
  }

  public void setDiyTipsAvailable( boolean diyTipsAvailable )
  {
    this.diyTipsAvailable = diyTipsAvailable;
  }

  public String getDiyBannersAudienceId()
  {
    return diyBannersAudienceId;
  }

  public void setDiyBannersAudienceId( String diyBannersAudienceId )
  {
    this.diyBannersAudienceId = diyBannersAudienceId;
  }

  public String[] getDeleteDIYBannersAudience()
  {
    return deleteDIYBannersAudience;
  }

  public void setDeleteDIYBannersAudience( String[] deleteDIYBannersAudience )
  {
    this.deleteDIYBannersAudience = deleteDIYBannersAudience;
  }

  public List<PromotionAudienceFormBean> getDiyBannersAudienceListAsList()
  {
    return diyBannersAudienceList;
  }

  public void setDiyBannersAudienceListAsList( List<PromotionAudienceFormBean> diyBannersAudienceList )
  {
    this.diyBannersAudienceList = diyBannersAudienceList;
  }

  public String getDiyNewsStoriesAudienceId()
  {
    return diyNewsStoriesAudienceId;
  }

  public void setDiyNewsStoriesAudienceId( String diyNewsStoriesAudienceId )
  {
    this.diyNewsStoriesAudienceId = diyNewsStoriesAudienceId;
  }

  public String[] getDeleteDIYNewsAudience()
  {
    return deleteDIYNewsAudience;
  }

  public void setDeleteDIYNewsAudience( String[] deleteDIYNewsAudience )
  {
    this.deleteDIYNewsAudience = deleteDIYNewsAudience;
  }

  public List<PromotionAudienceFormBean> getDiyNewsStoriesAudienceListAsList()
  {
    return diyNewsStoriesAudienceList;
  }

  public void setDiyNewsStoriesAudienceListAsList( List<PromotionAudienceFormBean> diyNewsStoriesAudienceList )
  {
    this.diyNewsStoriesAudienceList = diyNewsStoriesAudienceList;
  }

  public String getDiyResourceCenterAudienceId()
  {
    return diyResourceCenterAudienceId;
  }

  public void setDiyResourceCenterAudienceId( String diyResourceCenterAudienceId )
  {
    this.diyResourceCenterAudienceId = diyResourceCenterAudienceId;
  }

  public String[] getDeleteDIYResourceCenterAudience()
  {
    return deleteDIYResourceCenterAudience;
  }

  public void setDeleteDIYResourceCenterAudience( String[] deleteDIYResourceCenterAudience )
  {
    this.deleteDIYResourceCenterAudience = deleteDIYResourceCenterAudience;
  }

  public List<PromotionAudienceFormBean> getDiyResourceCenterAudienceListAsList()
  {
    return diyResourceCenterAudienceList;
  }

  public void setDiyResourceCenterAudienceListAsList( List<PromotionAudienceFormBean> diyResourceCenterAudienceList )
  {
    this.diyResourceCenterAudienceList = diyResourceCenterAudienceList;
  }

  public String getDiyTipsAudienceId()
  {
    return diyTipsAudienceId;
  }

  public void setDiyTipsAudienceId( String diyTipsAudienceId )
  {
    this.diyTipsAudienceId = diyTipsAudienceId;
  }

  public String[] getDeleteDIYTipsAudience()
  {
    return deleteDIYTipsAudience;
  }

  public void setDeleteDIYTipsAudience( String[] deleteDIYTipsAudience )
  {
    this.deleteDIYTipsAudience = deleteDIYTipsAudience;
  }

  public List<PromotionAudienceFormBean> getDiyTipsAudienceListAsList()
  {
    return diyTipsAudienceList;
  }

  public void setDiyTipsAudienceListAsList( List<PromotionAudienceFormBean> diyTipsAudienceList )
  {
    this.diyTipsAudienceList = diyTipsAudienceList;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getBannerAudienceName()
  {
    return bannerAudienceName;
  }

  public void setBannerAudienceName( String bannerAudienceName )
  {
    this.bannerAudienceName = bannerAudienceName;
  }

  public String getNewsAudienceName()
  {
    return newsAudienceName;
  }

  public void setNewsAudienceName( String newsAudienceName )
  {
    this.newsAudienceName = newsAudienceName;
  }

  public String getResourceAudienceName()
  {
    return resourceAudienceName;
  }

  public void setResourceAudienceName( String resourceAudienceName )
  {
    this.resourceAudienceName = resourceAudienceName;
  }

  public String getTipsAudienceName()
  {
    return tipsAudienceName;
  }

  public void setTipsAudienceName( String tipsAudienceName )
  {
    this.tipsAudienceName = tipsAudienceName;
  }

  public PromotionAudienceFormBean getDiyBannersAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)diyBannersAudienceList.get( index );
    }
    catch( Exception e )
    {
      return null;
    }
  }

  public int getBannerAudienceListCount()
  {
    if ( diyBannersAudienceList == null )
    {
      return 0;
    }
    return diyBannersAudienceList.size();
  }

  public PromotionAudienceFormBean getDiyNewsStoriesAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)diyNewsStoriesAudienceList.get( index );
    }
    catch( Exception e )
    {
      return null;
    }
  }

  public int getNewsStoriesAudienceListCount()
  {
    if ( diyNewsStoriesAudienceList == null )
    {
      return 0;
    }
    return diyNewsStoriesAudienceList.size();
  }

  public PromotionAudienceFormBean getDiyResourceCenterAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)diyResourceCenterAudienceList.get( index );
    }
    catch( Exception e )
    {
      return null;
    }
  }

  public int getResourceCenterAudienceListCount()
  {
    if ( diyResourceCenterAudienceList == null )
    {
      return 0;
    }
    return diyResourceCenterAudienceList.size();
  }

  public PromotionAudienceFormBean getDiyTipsAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)diyTipsAudienceList.get( index );
    }
    catch( Exception e )
    {
      return null;
    }
  }

  public int getTipsAudienceListCount()
  {
    if ( diyTipsAudienceList == null )
    {
      return 0;
    }
    return diyTipsAudienceList.size();
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    diyBannersAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "bannerAudienceListCount" ) );
    diyNewsStoriesAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "newsStoriesAudienceListCount" ) );
    diyResourceCenterAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "resourceCenterAudienceListCount" ) );
    diyTipsAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "tipsAudienceListCount" ) );
  }

  private List<PromotionAudienceFormBean> getEmptyValueList( int valueListCount )
  {
    List<PromotionAudienceFormBean> valueList = new ArrayList<PromotionAudienceFormBean>();
    for ( int i = 0; i < valueListCount; i++ )
    {
      PromotionAudienceFormBean promoAudienceBean = new PromotionAudienceFormBean();
      valueList.add( promoAudienceBean );
    }
    return valueList;
  }
}

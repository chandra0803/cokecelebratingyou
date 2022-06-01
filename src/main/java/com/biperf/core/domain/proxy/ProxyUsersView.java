
package com.biperf.core.domain.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.enums.ProxyCoreAccessType;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author dudam
 * @since Mar 11, 2013
 * @version 1.0
 */
public class ProxyUsersView
{

  private String[] messages = {};
  private List<ProxyView> proxyView = new ArrayList<ProxyView>();

  public ProxyUsersView()
  {

  }

  public ProxyUsersView( List<Proxy> proxies )
  {
    for ( Proxy proxy : proxies )
    {
      ProxyView proxyView = new ProxyView( proxy );
      this.proxyView.add( proxyView );
    }
  }

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  @JsonProperty( "proxies" )
  public List<ProxyView> getProxyView()
  {
    return proxyView;
  }

  public void setProxyView( List<ProxyView> proxyView )
  {
    this.proxyView = proxyView;
  }

  public static class ProxyView
  {
    private String id;
    private String lastName;
    private String firstName;
    private String avatarUrl;
    private String orgName;
    private String departmentName;
    private String jobName;
    private String countryName;
    private String countryCode;
    private String editProxyUrl;
    private String removeProxyUrl;
    private List<String> coreAccess = new ArrayList<String>();
    private List<Module> module = new ArrayList<Module>();

    public ProxyView()
    {

    }

    public ProxyView( Proxy proxy )
    {
      setId( proxy.getProxyUser().getId().toString() );
      setLastName( proxy.getProxyUser().getLastName() );
      setFirstName( proxy.getProxyUser().getFirstName() );
      setAvatarUrl( proxy.getProxyUser().getAvatarOriginalFullPath() );
      setOrgName( proxy.getProxyUser().getPaxOrgName() );
      setDepartmentName( proxy.getProxyUser().getPaxDeptName() );
      setJobName( proxy.getProxyUser().getPaxJobName() );
      setCountryName( proxy.getProxyUser().getPrimaryCountryName() );
      setCountryCode( proxy.getProxyUser().getPrimaryCountryCode() );

      Map parameterMap = new HashMap();
      parameterMap.put( "proxyId", proxy.getId() );
      parameterMap.put( "mainUserId", proxy.getUser().getId() );

      setEditProxyUrl( buildEditProxyUserUrl( parameterMap ) );
      setRemoveProxyUrl( buildRemoveProxyUserUrl( parameterMap ) );
      for ( ProxyCoreAccessType proxyCoreAccessType : proxy.getCoreAccessList() )
      {
        if ( proxyCoreAccessType != null )
        {
          getCoreAccess().add( proxyCoreAccessType.getName() );
        }
      }
      if ( proxy.isAllowLeaderboard() )
      {
        Module module = new Module();
        module.setTitle( CmsResourceBundle.getCmsBundle().getString( "profile.proxies.tab.LEADERBOARD" ) );
        module.getPromotions().add( CmsResourceBundle.getCmsBundle().getString( "profile.proxies.tab.LEADERBOARD_MESG" ) );
        this.getModule().add( module );
      }
      if ( proxy.isAllModules() )
      {
        Module module = new Module();
        module.setTitle( CmsResourceBundle.getCmsBundle().getString( "profile.proxies.tab.ALL_MODULES" ) );
        module.getPromotions().add( CmsResourceBundle.getCmsBundle().getString( "profile.proxies.tab.ALL_PROMOTIONS" ) );
        this.getModule().add( module );
      }
      else
      {
        for ( Object obj : proxy.getProxyModules() )
        {
          ProxyModule proxyModule = (ProxyModule)obj;
          Module module = new Module();
          if ( StringUtil.isEmpty( module.getTitle() ) )
          {
            module.setTitle( proxyModule.getPromotionType().getName() );
          }
          if ( module.getTitle() != null && module.getTitle().equalsIgnoreCase( proxyModule.getPromotionType().getName() ) )
          {
            if ( proxyModule.isAllPromotions() )
            {
              module.getPromotions().add( CmsResourceBundle.getCmsBundle().getString( "profile.proxies.tab.ALL_PROMOTIONS" ) );
            }
            else
            {
              for ( Object object : proxyModule.getProxyModulePromotions() )
              {
                ProxyModulePromotion proxyModulePromotion = (ProxyModulePromotion)object;
                module.getPromotions().add( proxyModulePromotion.getPromotion().getName() );
              }
            }
          }
          this.getModule().add( module );
        }
      }
    }

    public String getId()
    {
      return id;
    }

    public void setId( String id )
    {
      this.id = id;
    }

    public String getLastName()
    {
      return lastName;
    }

    public void setLastName( String lastName )
    {
      this.lastName = lastName;
    }

    public String getFirstName()
    {
      return firstName;
    }

    public void setFirstName( String firstName )
    {
      this.firstName = firstName;
    }

    public String getAvatarUrl()
    {
      return avatarUrl;
    }

    public void setAvatarUrl( String avatarUrl )
    {
      this.avatarUrl = avatarUrl;
    }

    public String getOrgName()
    {
      return orgName;
    }

    public void setOrgName( String orgName )
    {
      this.orgName = orgName;
    }

    public String getDepartmentName()
    {
      return departmentName;
    }

    public void setDepartmentName( String departmentName )
    {
      this.departmentName = departmentName;
    }

    public String getJobName()
    {
      return jobName;
    }

    public void setJobName( String jobName )
    {
      this.jobName = jobName;
    }

    public String getCountryName()
    {
      return countryName;
    }

    public void setCountryName( String countryName )
    {
      this.countryName = countryName;
    }

    public String getCountryCode()
    {
      return countryCode;
    }

    public void setCountryCode( String countryCode )
    {
      this.countryCode = countryCode;
    }

    @JsonProperty( "urlEdit" )
    public String getEditProxyUrl()
    {
      return editProxyUrl;
    }

    public void setEditProxyUrl( String editProxyUrl )
    {
      this.editProxyUrl = editProxyUrl;
    }

    @JsonProperty( "urlRemove" )
    public String getRemoveProxyUrl()
    {
      return removeProxyUrl;
    }

    public void setRemoveProxyUrl( String removeProxyUrl )
    {
      this.removeProxyUrl = removeProxyUrl;
    }

    public static class Module
    {

      private String title;
      private List<String> promotions = new ArrayList<String>();

      public String getTitle()
      {
        return title;
      }

      public void setTitle( String title )
      {
        this.title = title;
      }

      public List<String> getPromotions()
      {
        return promotions;
      }

      public void setPromotions( List<String> promotions )
      {
        this.promotions = promotions;
      }

    }

    public List<String> getCoreAccess()
    {
      return coreAccess;
    }

    public void setCoreAccess( List<String> coreAccess )
    {
      this.coreAccess = coreAccess;
    }

    public List<Module> getModule()
    {
      return module;
    }

    public void setModule( List<Module> module )
    {
      this.module = module;
    }

    private String buildEditProxyUserUrl( Map parameterMap )
    {
      return ClientStateUtils.generateEncodedLink( "", PageConstants.PARTICIPANT_PROXY_EDIT, parameterMap );
    }

    private String buildRemoveProxyUserUrl( Map parameterMap )
    {
      return ClientStateUtils.generateEncodedLink( "", PageConstants.PARTICIPANT_PROXY_REMOVE, parameterMap );
    }

  }

}


package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BaseJsonView;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommunicationsResourceCenterData extends BaseJsonView
{
  private static final long serialVersionUID = 1L;

  private ResourceTableBean resourceTable;
  private String resourceTitle;
  private String resourceStartDate;
  private String resourceEndDate;

  public CommunicationsResourceCenterData()
  {
    resourceTable = new ResourceTableBean();
  }

  public CommunicationsResourceCenterData( DIYCommunications communications, List<ResourceList> resourceContentList )
  {
    this.resourceTitle = communications.getContentTitle();
    this.resourceStartDate = DateUtils.toDisplayDateString( communications.getStartDate(), UserManager.getLocale() );
    this.resourceEndDate = DateUtils.toDisplayDateString( communications.getEndDate(), UserManager.getLocale() );
    this.resourceTable = new ResourceTableBean( communications, resourceContentList );

  }

  @JsonProperty( "resourceTable" )
  public ResourceTableBean getResourceTable()
  {
    return resourceTable;
  }

  public void setResourceTable( ResourceTableBean resourceTable )
  {
    this.resourceTable = resourceTable;
  }

  @JsonProperty( "resourceTitle" )
  public String getResourceTitle()
  {
    return resourceTitle;
  }

  public void setResourceTitle( String resourceTitle )
  {
    this.resourceTitle = resourceTitle;
  }

  @JsonProperty( "resourceStartDate" )
  public String getResourceStartDate()
  {
    return resourceStartDate;
  }

  public void setResourceStartDate( String resourceStartDate )
  {
    this.resourceStartDate = resourceStartDate;
  }

  @JsonProperty( "resourceEndDate" )
  public String getResourceEndDate()
  {
    return resourceEndDate;
  }

  public void setResourceEndDate( String resourceEndDate )
  {
    this.resourceEndDate = resourceEndDate;
  }

  public class ResourceTableBean
  {
    private List<ResourceList> resources = new ArrayList<ResourceList>();

    public ResourceTableBean()
    {

    }

    public ResourceTableBean( DIYCommunications communications, List<ResourceList> resourceContentList )
    {
      List<ResourceList> newResourceContentList = new ArrayList<ResourceList>();
      for ( Iterator<ResourceList> iter = resourceContentList.iterator(); iter.hasNext(); )
      {
        ResourceList resourceList = iter.next();
        resourceList.setLanguageDisplay( LanguageType.lookup( resourceList.getLanguage() ).getName() );
        newResourceContentList.add( resourceList );
      }
      this.resources.addAll( newResourceContentList );
    }

    public List<ResourceList> getResources()
    {
      return resources;
    }

    public void setResources( List<ResourceList> resources )
    {
      this.resources = resources;
    }

  }

}

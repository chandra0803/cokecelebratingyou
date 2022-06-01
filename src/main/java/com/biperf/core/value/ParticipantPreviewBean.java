/**
 * 
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author poddutur
 *
 */
public class ParticipantPreviewBean implements Serializable
{
  private boolean isSelected;
  private Long id;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private String departmentName;
  private String jobName;
  private String countryName;
  private String countryCode;
  private Long countryRatio;
  private boolean isSelf;
  private boolean allowPublicInformation;
  private boolean allowPublicRecognition;
  private boolean isLocked;
  private boolean isPublic;
  private String orgName;
  private String profileUrl;
  private String urlEdit;
  private Map<Integer, NodePreviewBean> nodes = new HashMap<Integer, NodePreviewBean>();

  public boolean isSelected()
  {
    return isSelected;
  }

  public void setSelected( boolean isSelected )
  {
    this.isSelected = isSelected;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
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

  public Long getCountryRatio()
  {
    return countryRatio;
  }

  public void setCountryRatio( Long countryRatio )
  {
    this.countryRatio = countryRatio;
  }

  public boolean isSelf()
  {
    return isSelf;
  }

  public void setSelf( boolean isSelf )
  {
    this.isSelf = isSelf;
  }

  public boolean isAllowPublicInformation()
  {
    return allowPublicInformation;
  }

  public void setAllowPublicInformation( boolean allowPublicInformation )
  {
    this.allowPublicInformation = allowPublicInformation;
  }

  public boolean isAllowPublicRecognition()
  {
    return allowPublicRecognition;
  }

  public void setAllowPublicRecognition( boolean allowPublicRecognition )
  {
    this.allowPublicRecognition = allowPublicRecognition;
  }

  public boolean isLocked()
  {
    return isLocked;
  }

  public void setLocked( boolean isLocked )
  {
    this.isLocked = isLocked;
  }

  public boolean isPublic()
  {
    return isPublic;
  }

  public void setPublic( boolean isPublic )
  {
    this.isPublic = isPublic;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getProfileUrl()
  {
    return profileUrl;
  }

  public void setProfileUrl( String profileUrl )
  {
    this.profileUrl = profileUrl;
  }

  public String getUrlEdit()
  {
    return urlEdit;
  }

  public void setUrlEdit( String urlEdit )
  {
    this.urlEdit = urlEdit;
  }

  public int getNodesCount()
  {
    if ( nodes == null || nodes.isEmpty() )
    {
      return 0;
    }
    return nodes.size();
  }

  public NodePreviewBean getNodes( int index )
  {
    NodePreviewBean result = nodes.get( index );
    if ( result == null )
    {
      result = new NodePreviewBean();
      nodes.put( index, result );
    }
    return result;
  }

  public List<NodePreviewBean> getNodeValues()
  {
    return new ArrayList<NodePreviewBean>( nodes.values() );
  }

  public void setNodes( Map<Integer, NodePreviewBean> nodes )
  {
    this.nodes = nodes;
  }

}

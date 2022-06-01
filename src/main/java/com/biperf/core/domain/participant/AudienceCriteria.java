/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/participant/AudienceCriteria.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;

/**
 * AudienceCriteria.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sharma</td>
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AudienceCriteria extends BaseDomain
{
  private Audience audience;
  private String firstName;
  private String lastName;
  private String loginId;
  private Long employerId;
  private String positionType;
  private String departmentType;
  private LanguageType languageType;
  private String nodeName;
  private Long nodeId;
  private Long nodeTypeId;
  private HierarchyRoleType nodeRole;
  private boolean childNodesIncluded;
  private Set characteristicCriterias = new LinkedHashSet();
  private Long countryId;
  private String emailAddr;
  private Long userId;
  private String stateName;

  private String excludePositionType;
  private String excludeDepartmentType;
  private String excludeNodeName;
  private Long excludeNodeId;
  private Long excludeCountryId;
  private Long excludeNodeTypeId;
  private HierarchyRoleType excludeNodeRole;
  private boolean excludeChildNodesIncluded;

  public Audience getAudience()
  {
    return audience;
  }

  public void setAudience( Audience audience )
  {
    this.audience = audience;
  }

  public Set getCharacteristicCriterias()
  {
    return characteristicCriterias;
  }

  public void setCharacteristicCriterias( Set characteristicCriterias )
  {
    this.characteristicCriterias = characteristicCriterias;
  }

  public boolean isChildNodesIncluded()
  {
    return childNodesIncluded;
  }

  public void setChildNodesIncluded( boolean childNodesIncluded )
  {
    this.childNodesIncluded = childNodesIncluded;
  }

  public String getDepartmentType()
  {
    return departmentType;
  }

  public void setDepartmentType( String departmentType )
  {
    this.departmentType = departmentType;
  }

  public LanguageType getLanguageType()
  {
    return languageType;
  }

  public void setLanguageType( LanguageType languageType )
  {
    this.languageType = languageType;
  }

  public Long getEmployerId()
  {
    return employerId;
  }

  public void setEmployerId( Long employerId )
  {
    this.employerId = employerId;
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

  public String getLoginId()
  {
    return loginId;
  }

  public void setLoginId( String loginId )
  {
    this.loginId = loginId;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public HierarchyRoleType getNodeRole()
  {
    return nodeRole;
  }

  public void setNodeRole( HierarchyRoleType nodeRole )
  {
    this.nodeRole = nodeRole;
  }

  public Long getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( Long nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public String getPositionType()
  {
    return positionType;
  }

  public void setPositionType( String positionType )
  {
    this.positionType = positionType;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    // TODO: Use a GUID
    return this == object;
  }

  /**
   * Returns true if any criteria element has critieria. (i.e. is not null).
   * 
   * @return boolean
   */
  public boolean hasConstraints()
  {
    return getFirstName() != null || getLastName() != null || getStateName() != null || getLoginId() != null || getEmailAddr() != null || getUserId() != null || getEmployerId() != null
        || getPositionType() != null || getDepartmentType() != null || getLanguageType() != null || getCountryId() != null
        || getIncludeCharacteristicCriterias() != null && !getIncludeCharacteristicCriterias().isEmpty() || getNodeRole() != null || getNodeName() != null || getNodeId() != null
        || getNodeTypeId() != null;
  }

  public boolean hasExcludeConstraints()
  {
    return getExcludePositionType() != null || getExcludeDepartmentType() != null || getExcludeCountryId() != null
        || getExcludeCharacteristicCriterias() != null && !getExcludeCharacteristicCriterias().isEmpty() || getExcludeNodeRole() != null || getExcludeNodeName() != null || getExcludeNodeId() != null
        || getExcludeNodeTypeId() != null;
  }

  /**
   * Returns true if any node criteria element has critieria. (i.e. is not null).
   * 
   * @return boolean
   */
  public boolean hasNodeConstraints()
  {
    return !getNodeTypeCharacteristicCriterias().isEmpty() || getNodeRole() != null || getNodeName() != null || getNodeId() != null || getNodeTypeId() != null;
  }

  public boolean hasExcludeNodeConstraints()
  {
    return !getExcludeNodeTypeCharacteristicCriterias().isEmpty() || getExcludeNodeRole() != null || getExcludeNodeName() != null || getExcludeNodeId() != null || getExcludeNodeTypeId() != null;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    // TODO: Use a GUID
    return 0;
  }

  /**
   * Get the subset of characteristicCriterias that are for User Characteristics
   * 
   * @return Set
   */
  public Set getUserCharacteristicCriterias()
  {
    if ( characteristicCriterias == null )
    {
      return new LinkedHashSet();
    }

    Set userCharacteristicCriterias = new LinkedHashSet();

    for ( Iterator iter = characteristicCriterias.iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();
      if ( audienceCriteriaCharacteristic.getCharacteristic() instanceof UserCharacteristicType )
      {
        userCharacteristicCriterias.add( audienceCriteriaCharacteristic );
      }
    }

    return userCharacteristicCriterias;
  }

  /**
   * Get the subset of characteristicCriterias that are for Node Type Characteristics
   * 
   * @return Set
   */
  public Set getNodeTypeCharacteristicCriterias()
  {
    if ( characteristicCriterias == null )
    {
      return new LinkedHashSet();
    }

    Set nodeTypeCharacteristicCriterias = new LinkedHashSet();

    for ( Iterator iter = characteristicCriterias.iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();
      if ( audienceCriteriaCharacteristic.getCharacteristic() instanceof NodeTypeCharacteristicType )
      {
        if ( audienceCriteriaCharacteristic.getSearchType() == null || audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "include" ) )
        {
          nodeTypeCharacteristicCriterias.add( audienceCriteriaCharacteristic );
        }
      }
    }

    return nodeTypeCharacteristicCriterias;
  }

  public Set getExcludeNodeTypeCharacteristicCriterias()
  {
    if ( characteristicCriterias == null )
    {
      return new LinkedHashSet();
    }

    Set nodeTypeCharacteristicCriterias = new LinkedHashSet();

    for ( Iterator iter = characteristicCriterias.iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();
      if ( audienceCriteriaCharacteristic.getCharacteristic() instanceof NodeTypeCharacteristicType )
      {
        if ( audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "exclude" ) )
        {
          nodeTypeCharacteristicCriterias.add( audienceCriteriaCharacteristic );
        }
      }
    }

    return nodeTypeCharacteristicCriterias;
  }

  public Set getIncludeCharacteristicCriterias()
  {
    if ( characteristicCriterias == null )
    {
      return new LinkedHashSet();
    }

    Set includeCharacteristicCriterias = new LinkedHashSet();

    for ( Iterator iter = characteristicCriterias.iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();
      if ( audienceCriteriaCharacteristic.getSearchType() == null || audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "include" ) )
      {
        includeCharacteristicCriterias.add( audienceCriteriaCharacteristic );
      }
    }

    return includeCharacteristicCriterias;
  }

  public Set getExcludeCharacteristicCriterias()
  {
    if ( characteristicCriterias == null )
    {
      return new LinkedHashSet();
    }

    Set excludeCharacteristicCriterias = new LinkedHashSet();

    for ( Iterator iter = characteristicCriterias.iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();
      if ( audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "exclude" ) )
      {
        excludeCharacteristicCriterias.add( audienceCriteriaCharacteristic );
      }
    }

    return excludeCharacteristicCriterias;
  }

  /**
   * @return value of nodeId property
   */
  public Long getNodeId()
  {
    return nodeId;
  }

  /**
   * @param nodeId value for nodeId property
   */
  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  /**
   * Returns a deep copy of this object.
   * @return a deep copy of this <code>AudienceCriteria</code> object.
   */
  public AudienceCriteria deepCopy()
  {
    AudienceCriteria clone = new AudienceCriteria();
    clone.setAudience( getAudience() );
    clone.setCharacteristicCriterias( new LinkedHashSet() );
    if ( null != characteristicCriterias && characteristicCriterias.size() > 0 )
    {
      for ( Iterator iter = getCharacteristicCriterias().iterator(); iter.hasNext(); )
      {
        AudienceCriteriaCharacteristic audienceCriteriaChar = (AudienceCriteriaCharacteristic)iter.next();
        AudienceCriteriaCharacteristic audClone = audienceCriteriaChar.deepCopy();
        clone.addAudienceCriteriaCharacteristic( audClone );

      }
    }

    clone.setDepartmentType( getDepartmentType() );
    clone.setEmployerId( getEmployerId() );
    clone.setFirstName( getFirstName() );
    clone.setId( getId() );
    clone.setLanguageType( getLanguageType() );
    clone.setCountryId( getCountryId() );
    clone.setLastName( getLastName() );
    clone.setLoginId( getLoginId() );
    clone.setNodeId( getNodeId() );
    clone.setNodeName( getNodeName() );
    clone.setNodeRole( getNodeRole() );
    clone.setNodeTypeId( getNodeTypeId() );
    clone.setPositionType( getPositionType() );
    clone.setChildNodesIncluded( this.childNodesIncluded );

    clone.setExcludeDepartmentType( getExcludeDepartmentType() );
    clone.setExcludeCountryId( getExcludeCountryId() );
    clone.setExcludePositionType( getExcludePositionType() );
    clone.setExcludeNodeId( getExcludeNodeId() );
    clone.setExcludeNodeName( getExcludeNodeName() );
    clone.setExcludeNodeRole( getExcludeNodeRole() );
    clone.setExcludeNodeTypeId( getExcludeNodeTypeId() );
    clone.setExcludeChildNodesIncluded( this.excludeChildNodesIncluded );

    return clone;
  }

  public void addAudienceCriteriaCharacteristic( AudienceCriteriaCharacteristic audienceCriteriaCharacteristic )
  {
    audienceCriteriaCharacteristic.setAudienceCriteria( this );
    characteristicCriterias.add( audienceCriteriaCharacteristic );
  }

  public void setAudienceCriteriaCharacteristic( Set characteristicCriterias )
  {
    this.characteristicCriterias = characteristicCriterias;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  public void setExcludePositionType( String excludePositionType )
  {
    this.excludePositionType = excludePositionType;
  }

  public String getExcludePositionType()
  {
    return excludePositionType;
  }

  public void setExcludeDepartmentType( String excludeDepartmentType )
  {
    this.excludeDepartmentType = excludeDepartmentType;
  }

  public String getExcludeDepartmentType()
  {
    return excludeDepartmentType;
  }

  public void setExcludeNodeName( String excludeNodeName )
  {
    this.excludeNodeName = excludeNodeName;
  }

  public String getExcludeNodeName()
  {
    return excludeNodeName;
  }

  public void setExcludeNodeId( Long excludeNodeId )
  {
    this.excludeNodeId = excludeNodeId;
  }

  public Long getExcludeNodeId()
  {
    return excludeNodeId;
  }

  public void setExcludeCountryId( Long excludeCountryId )
  {
    this.excludeCountryId = excludeCountryId;
  }

  public Long getExcludeCountryId()
  {
    return excludeCountryId;
  }

  public void setExcludeNodeTypeId( Long excludeNodeTypeId )
  {
    this.excludeNodeTypeId = excludeNodeTypeId;
  }

  public Long getExcludeNodeTypeId()
  {
    return excludeNodeTypeId;
  }

  public void setExcludeNodeRole( HierarchyRoleType excludeNodeRole )
  {
    this.excludeNodeRole = excludeNodeRole;
  }

  public HierarchyRoleType getExcludeNodeRole()
  {
    return excludeNodeRole;
  }

  public void setExcludeChildNodesIncluded( boolean excludeChildNodesIncluded )
  {
    this.excludeChildNodesIncluded = excludeChildNodesIncluded;
  }

  public boolean isExcludeChildNodesIncluded()
  {
    return excludeChildNodesIncluded;
  }

  public String getEmailAddr()
  {
    return emailAddr;
  }

  public void setEmailAddr( String emailAddr )
  {
    this.emailAddr = emailAddr;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getStateName()
  {
    return stateName;
  }

  public void setStateName( String stateName )
  {
    this.stateName = stateName;
  }

}

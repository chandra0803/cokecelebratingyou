
package com.biperf.core.value.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.biperf.core.value.indexing.BIElasticSearchCriteria;
import com.biperf.core.value.indexing.PaxIndexMappingEnum;

/**
 * Search Criteria Object to find  PAX data set  from Index/Cache.   * 
 *  {@value fieldSortOrders }  holds  PAX index object field name with expecting sort order.
 */
public class PaxIndexSearchCriteria implements BIElasticSearchCriteria, Serializable
{
  private static final long serialVersionUID = 7009059588766014605L;

  private List<String> includeUserIds;
  private String name;
  private String departmentTypeCode;
  private String positionTypeCode;

  private Long countryId;
  private List<Long> allNodeIds;
  private List<Long> audienceIds;
  private long fromIndex = 0;

  private List<PaxIndexSortOrder> fieldSortOrders = new ArrayList<PaxIndexSortOrder>();
  private List<PaxIndexMappingEnum> fields = new ArrayList<PaxIndexMappingEnum>();
  private int recordsMaxSize;
  private Long primaryNodeId;
  private Long path;

  private List<String> excludeUserIds;

  // Roster Related Attributes

  // For Roster Search Pagination
  private UUID rosterUserId;
  private String userName;

  private Integer paginationRowStart;
  private Integer paginationRowEnd;
  private String personCountry;
  private String languagePreference;
  private String roleType;
  private String emailAddress;
  private String phoneNumber;
  private String personAddressCountry;
  private String state;

  public List<Long> getAudienceIds()
  {
    return audienceIds;
  }

  public void setAudienceIds( List<Long> audienceIds )
  {
    this.audienceIds = audienceIds;
  }

  public List<PaxIndexSortOrder> getFieldSortOrders()
  {
    return fieldSortOrders;
  }

  public void setFieldSortOrders( List<PaxIndexSortOrder> fieldSortOrders )
  {
    this.fieldSortOrders = fieldSortOrders;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String query )
  {
    this.name = query;
  }

  public String getPositionTypeCode()
  {
    return positionTypeCode;
  }

  public void setPositionTypeCode( String positionTypeCode )
  {
    this.positionTypeCode = positionTypeCode;
  }

  public String getDepartmentTypeCode()
  {
    return departmentTypeCode;
  }

  public void setDepartmentTypeCode( String departmentTypeCode )
  {
    this.departmentTypeCode = departmentTypeCode;
  }

  public Long getCountryId()
  {
    return countryId;
  }

  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  @Override
  public long getFromIndex()
  {
    return fromIndex;
  }

  @Override
  public int getRecordsMaxSize()
  {
    return recordsMaxSize;
  }

  public void setFromIndex( long startIndex )
  {
    this.fromIndex = startIndex;
  }

  @Override
  public List<PaxIndexMappingEnum> getFields()
  {
    return fields;
  }

  public List<String> getAllFieldsAsString()
  {
    return fields.stream().map( f -> f.getFieldName() ).collect( Collectors.toList() );
  }

  public void setFields( List<PaxIndexMappingEnum> fileds )
  {
    this.fields = fileds;
  }

  public void setRecordsMaxSize( int recordsMaxSize )
  {
    this.recordsMaxSize = recordsMaxSize;
  }

  public List<Long> getAllNodeIds()
  {
    return allNodeIds;
  }

  public void setAllNodeIds( List<Long> nodeIds )
  {
    this.allNodeIds = nodeIds;
  }

  public Long getPrimaryNodeId()
  {
    return primaryNodeId;
  }

  public void setPrimaryNodeId( Long primaryNodeId )
  {
    this.primaryNodeId = primaryNodeId;
  }

  public List<String> getIncludeUserIds()
  {
    return includeUserIds;
  }

  public void setIncludeUserIds( List<String> includeUserIds )
  {
    this.includeUserIds = includeUserIds;
  }

  public Long getPath()
  {
    return path;
  }

  public void setPath( Long path )
  {
    this.path = path;
  }

  public List<String> getExcludeUserIds()
  {

    return excludeUserIds;
  }

  public void setExcludeUserId( List<String> excludeUserIds )
  {
    this.excludeUserIds = excludeUserIds;
  }

  public UUID getRosterUserId()
  {
    return rosterUserId;
  }

  public void setRosterUserId( UUID rosterUserId )
  {
    this.rosterUserId = rosterUserId;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public void setExcludeUserIds( List<String> excludeUserIds )
  {
    this.excludeUserIds = excludeUserIds;
  }

  public Integer getPaginationRowStart()
  {
    return paginationRowStart;
  }

  public void setPaginationRowStart( Integer paginationRowStart )
  {
    this.paginationRowStart = paginationRowStart;
  }

  public Integer getPaginationRowEnd()
  {
    return paginationRowEnd;
  }

  public void setPaginationRowEnd( Integer paginationRowEnd )
  {
    this.paginationRowEnd = paginationRowEnd;
  }

  public String getPersonCountry()
  {
    return personCountry;
  }

  public void setPersonCountry( String personCountry )
  {
    this.personCountry = personCountry;
  }

  public String getLanguagePreference()
  {
    return languagePreference;
  }

  public void setLanguagePreference( String languagePreference )
  {
    this.languagePreference = languagePreference;
  }

  public String getRoleType()
  {
    return roleType;
  }

  public void setRoleType( String roleType )
  {
    this.roleType = roleType;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public String getPersonAddressCountry()
  {
    return personAddressCountry;
  }

  public void setPersonAddressCountry( String personAddressCountry )
  {
    this.personAddressCountry = personAddressCountry;
  }

  public String getState()
  {
    return state;
  }

  public void setState( String state )
  {
    this.state = state;
  }

}

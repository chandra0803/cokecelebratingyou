/**
 * 
 */

package com.biperf.core.domain.purl;

import java.io.Serializable;

/**
 * @author poddutur
 *
 */
public class PurlCelebrationSetCountBean implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -6108254722842695187L;
  private long globalTabCount;
  private long teamTabCount;
  private long followersTabCount;
  private long searchTabCount;
  private long relevantCount;
  private long countryCount;
  private long departmentCount;
  private long divisionCount;
  
  public long getGlobalTabCount()
  {
    return globalTabCount;
  }

  public void setGlobalTabCount( long globalTabCount )
  {
    this.globalTabCount = globalTabCount;
  }

  public long getTeamTabCount()
  {
    return teamTabCount;
  }

  public void setTeamTabCount( long teamTabCount )
  {
    this.teamTabCount = teamTabCount;
  }

  public long getFollowersTabCount()
  {
    return followersTabCount;
  }

  public void setFollowersTabCount( long followersTabCount )
  {
    this.followersTabCount = followersTabCount;
  }

  public long getSearchTabCount()
  {
    return searchTabCount;
  }

  public void setSearchTabCount( long searchTabCount )
  {
    this.searchTabCount = searchTabCount;
  }

  public long getRelevantCount()
  {
    return relevantCount;
  }

  public void setRelevantCount( long relevantCount )
  {
    this.relevantCount = relevantCount;
  }

  public long getCountryCount()
  {
    return countryCount;
  }

  public void setCountryCount( long countryCount )
  {
    this.countryCount = countryCount;
  }

  public long getDepartmentCount()
  {
    return departmentCount;
  }

  public void setDepartmentCount( long departmentCount )
  {
    this.departmentCount = departmentCount;
  }

/**
 * @return the divisionCount
 */
public long getDivisionCount() {
	return divisionCount;
}

/**
 * @param divisionCount the divisionCount to set
 */
public void setDivisionCount(long divisionCount) {
	this.divisionCount = divisionCount;
}

  
}

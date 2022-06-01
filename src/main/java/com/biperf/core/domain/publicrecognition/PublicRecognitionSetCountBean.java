
package com.biperf.core.domain.publicrecognition;

import java.io.Serializable;

/**
 * 
 * @author dudam
 * @since Dec 26, 2012
 * @version 1.0
 * 
 * This bean is to get the count of public recognition by tab name
 */
public class PublicRecognitionSetCountBean implements Serializable
{

  private static final long serialVersionUID = 1L;

  private long recommendedTabCount;
  private long globalTabCount;
  private long teamTabCount;
  private long followersTabCount;
  private long mineTabCount;
  private long countryTabCount;
  private long departmentTabCount;
  private long divisionTabCount;
  
  public long getRecommendedTabCount()
  {
    return recommendedTabCount;
  }

  public void setRecommendedTabCount( long recommendedTabCount )
  {
    this.recommendedTabCount = recommendedTabCount;
  }

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

  public long getMineTabCount()
  {
    return mineTabCount;
  }

  public void setMineTabCount( long mineTabCount )
  {
    this.mineTabCount = mineTabCount;
  }

  public long getCountryTabCount()
  {
    return countryTabCount;
  }

  public void setCountryTabCount( long countryTabCount )
  {
    this.countryTabCount = countryTabCount;
  }

  public long getDepartmentTabCount()
  {
    return departmentTabCount;
  }

  public void setDepartmentTabCount( long departmentTabCount )
  {
    this.departmentTabCount = departmentTabCount;
  }

/**
 * @return the divisionTabCount
 */
public long getDivisionTabCount() {
	return divisionTabCount;
}

/**
 * @param divisionTabCount the divisionTabCount to set
 */
public void setDivisionTabCount(long divisionTabCount) {
	this.divisionTabCount = divisionTabCount;
}

}

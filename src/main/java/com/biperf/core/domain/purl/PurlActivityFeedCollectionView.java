
package com.biperf.core.domain.purl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author drahn
 * @since Jan 12, 2013
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class PurlActivityFeedCollectionView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private List<PurlActivityPodsView> activityPods = new ArrayList<PurlActivityPodsView>();
  private String userLang = "";

  public PurlActivityFeedCollectionView( List<PurlContributorComment> comments, String siteUrl, String userLang )
  {
    for ( PurlContributorComment comment : comments )
    {
      activityPods.add( new PurlActivityPodsView( comment, siteUrl ) );
    }

    this.userLang = userLang;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public String getUserLang()
  {
    return userLang;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public void setActivityPods( List<PurlActivityPodsView> activityPods )
  {
    this.activityPods = activityPods;
  }

  public List<PurlActivityPodsView> getActivityPods()
  {
    return activityPods;
  }

}

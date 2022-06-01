
package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DIYBannerListTabularData
{
  private DIYCommunicationsMetaView meta;
  private List<DIYCommunicationsResultsBean> results = new ArrayList<DIYCommunicationsResultsBean>();

  public DIYBannerListTabularData()
  {

  }

  public DIYBannerListTabularData( HttpServletRequest request, List<DIYCommunications> communicationList, boolean isActive )
  {
    this.meta = new DIYCommunicationsMetaView( communicationList, "diyCommunications.banner.labels.BANNER_CONTENT" );

    for ( Iterator<DIYCommunications> iter = communicationList.iterator(); iter.hasNext(); )
    {
      DIYCommunications diyCommunication = iter.next();
      DIYCommunicationsResultsBean result = new DIYCommunicationsResultsBean();
      result.setId( diyCommunication.getId().toString() );
      result.setName( diyCommunication.getContentTitle() );
      result.setStartDate( DateUtils.toDisplayDateString( diyCommunication.getStartDate(), UserManager.getLocale() ) );
      result.setEndDate( DateUtils.toDisplayDateString( diyCommunication.getEndDate(), UserManager.getLocale() ) );
      Map<String, Object> parameterMap = new HashMap<String, Object>();
      parameterMap.put( "bannerId", diyCommunication.getId() );
      String bannerEditUrl = ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/participant/createDiyBanner.do?method=display", parameterMap );
      result.setUrl( bannerEditUrl );
      results.add( result );
    }
  }

  public void setMeta( DIYCommunicationsMetaView meta )
  {
    this.meta = meta;
  }

  @JsonProperty( "meta" )
  public DIYCommunicationsMetaView getMeta()
  {
    return meta;
  }

  @JsonProperty( "results" )
  public List<DIYCommunicationsResultsBean> getResults()
  {
    return results;
  }

  public void setResults( List<DIYCommunicationsResultsBean> results )
  {
    this.results = results;
  }

}

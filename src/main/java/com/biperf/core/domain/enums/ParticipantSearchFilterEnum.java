
package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.biperf.core.domain.participant.NameIdBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public enum ParticipantSearchFilterEnum
{
  NAME( "name", "participant.search.NAME", true ), LOCATION( "location", "participant.search.SEARCH_BY_LOCATION", true ), JOBTITLE( "jobTitle", "participant.search.SEARCH_BY_JOB_TITLE",
      true ), DEPARTMENT( "department", "participant.search.SEARCH_BY_DEPARTMENT",
          true ), COUNTRY( "country", "participant.search.COUNTRY", true ), PAX_GROUP( "paxGroup", "participant.search.PAX_GROUP", true );

  private String code;
  private String desc;
  private boolean active;

  private ParticipantSearchFilterEnum( String code, String desc, boolean active )
  {
    this.code = code;
    this.desc = desc;
    this.active = active;
  }

  public static List<NameIdBean> getActiveFilters()
  {
    List<NameIdBean> filters = new ArrayList<NameIdBean>();

    Stream.of( values() ).filter( e -> e.isActive() ).forEach( f -> filters.add( new NameIdBean( f.getCode(), f.getDesc() ) ) );

    return filters;
  }

  public String getCode()
  {
    return code;
  }

  public String getDesc()
  {
    return CmsResourceBundle.getCmsBundle().getString( desc );
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

}

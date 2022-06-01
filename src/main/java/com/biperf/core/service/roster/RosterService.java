
package com.biperf.core.service.roster;

import java.util.List;

import com.biperf.core.service.SAO;
import com.biperf.core.service.roster.value.AudienceDetails;
import com.biperf.core.service.roster.value.EmailDetails;
import com.biperf.core.service.roster.value.HierarchyDetails;
import com.biperf.core.service.roster.value.PhoneDetails;
import com.biperf.core.service.roster.value.UserAddressDetail;

public interface RosterService extends SAO
{
  public static final String BEAN_NAME = "rosterService";

  public Long getHierarchyIdByHierarchyUUId( String rosterHierarchyUUId );

  public Long getUserIdByRosterPersonUUId( String rosterPersonUUId );

  public Long getAudienceIdByAudienceUUId( String rosterAudienceUUId );

  public Long getNodeIdByRosterNodeUUId( String rosterNodeUUId );

  public String getRosterPersonUUIdByUserId( Long userId );

  public String getAudienceUUIdByAudienceId( Long audienceId );

  public String getHierarchyUUIdByHierarchyId( Long hierarchyId );

  public String getNodeUUIdByNodeId( Long nodeId );

  public List<String> getAudienceUUIDsByAudienceIds( List<Long> audienceIds );

  public List<String> getNodeUUIDsByNodeIds( List<Long> allNodeIds );

  public List<AudienceDetails> getAudiencesDetailsByUserId( Long userId );

  public List<HierarchyDetails> getHierarchyDetailsByHierarchyId( Long userId );

  public List<EmailDetails> getEmailAddressesByUserId( Long userId );

  public List<PhoneDetails> getPhonesDetailsByUserId( Long userId );

  public List<UserAddressDetail> getAddressesDetailsByUserId( Long userId );

}
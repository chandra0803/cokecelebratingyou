
package com.biperf.core.service.roster.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.roster.RosterDAO;
import com.biperf.core.service.roster.RosterService;
import com.biperf.core.service.roster.value.AudienceDetails;
import com.biperf.core.service.roster.value.EmailDetails;
import com.biperf.core.service.roster.value.HierarchyDetails;
import com.biperf.core.service.roster.value.PhoneDetails;
import com.biperf.core.service.roster.value.UserAddressDetail;

public class RosterServiceImpl implements RosterService
{
  private static final Log logger = LogFactory.getLog( RosterServiceImpl.class );
  private static RosterDAO rosterDAO;

  @Override
  public Long getHierarchyIdByHierarchyUUId( String rosterHierarchyUUId )
  {
    return rosterDAO.getHierarchyIdByHierarchyUUId( rosterHierarchyUUId );
  }

  @Override
  public Long getUserIdByRosterPersonUUId( String rosterPersonUUId )
  {
    return rosterDAO.getUserIdByRosterPersonUUId( rosterPersonUUId );
  }

  @Override
  public Long getNodeIdByRosterNodeUUId( String rosterNodeUUId )
  {
    return rosterDAO.getNodeIdByRosterNodeUUId( rosterNodeUUId );
  }

  @Override
  public Long getAudienceIdByAudienceUUId( String rosterAudienceUUId )
  {
    return rosterDAO.getAudienceIdByAudienceUUId( rosterAudienceUUId );
  }

  @Override
  public String getRosterPersonUUIdByUserId( Long userId )
  {
    return rosterDAO.getRosterPersonUUIdByUserId( userId );
  }

  @Override
  public String getAudienceUUIdByAudienceId( Long audienceId )
  {
    return rosterDAO.getAudienceUUIdByAudienceId( audienceId );
  }

  @Override
  public String getHierarchyUUIdByHierarchyId( Long hierarchyId )
  {
    return rosterDAO.getHierarchyUUIdByHierarchyId( hierarchyId );
  }

  public RosterDAO getRosterDAO()
  {
    return rosterDAO;
  }

  public void setRosterDAO( RosterDAO rosterDAO )
  {
    this.rosterDAO = rosterDAO;
  }

  @Override
  public String getNodeUUIdByNodeId( Long nodeId )
  {
    return rosterDAO.getNodeUUIdByNodeId( nodeId );
  }

  @Override
  public List<String> getAudienceUUIDsByAudienceIds( List<Long> audienceIds )
  {
    return rosterDAO.getAudienceUUIDsByAudienceIds( audienceIds );
  }

  @Override
  public List<String> getNodeUUIDsByNodeIds( List<Long> allNodeIds )
  {
    return rosterDAO.getNodeUUIDsByNodeIds( allNodeIds );
  }

  @Override
  public List<AudienceDetails> getAudiencesDetailsByUserId( Long userId )
  {
    return rosterDAO.getAudiencesDetailsByUserId( userId );
  }

  @Override
  public List<HierarchyDetails> getHierarchyDetailsByHierarchyId( Long userId )
  {
    return rosterDAO.getHierarchyDetailsByHierarchyId( userId );
  }

  @Override
  public List<EmailDetails> getEmailAddressesByUserId( Long userId )
  {
    return rosterDAO.getEmailAddressesByUserId( userId );
  }

  @Override
  public List<PhoneDetails> getPhonesDetailsByUserId( Long userId )
  {
    return rosterDAO.getPhonesDetailsByUserId( userId );
  }

  @Override
  public List<UserAddressDetail> getAddressesDetailsByUserId( Long userId )
  {
    return rosterDAO.getAddressesDetailsByUserId( userId );
  }

}

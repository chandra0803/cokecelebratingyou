
package com.biperf.core.value.ssi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class SSIContestListValueBean implements Comparable<SSIContestListValueBean>
{
  private static final String CONTEST_ID = "contestId";
  private static final String USER_ID = "userId";
  private String id;
  private String name;
  private String managerDetailPageUrl;
  private String status;
  private String statusLabel;
  private String startDate;
  private String endDate;
  private String role;
  private String roleLabel;
  private String originalContestId;
  private Date dateModified;
  private Date claimSubmissionLastDate;
  private String contestType;
  private String contestTypeName;
  private String readOnlyUrl;
  private String deniedReason;
  private String detailPageUrl;
  private String updatedOn;
  private String updatedBy;

  private boolean canShowActionLinks = true;

  private static Map<String, Integer> contestStatusOrder = new HashMap<String, Integer>();

  static
  {
    contestStatusOrder.put( SSIContestStatus.LIVE, 0 );
    contestStatusOrder.put( SSIContestStatus.PENDING, 1 );
    contestStatusOrder.put( SSIContestStatus.WAITING_FOR_APPROVAL, 2 );
    contestStatusOrder.put( SSIContestStatus.DRAFT, 3 );
  }

  public SSIContestListValueBean( String id, String name, String status, String startDate, String endDate )
  {
    setId( id );
    this.name = name;
    this.status = status;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public SSIContestListValueBean()
  {

  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    originalContestId = id;

    // put the client state instead of direct id
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( CONTEST_ID, id );
    clientStateParamMap.put( USER_ID, UserManager.getUserId() );
    String password = getClientStatePassword();
    try
    {
      this.id = URLEncoder.encode( ClientStateSerializer.serialize( clientStateParamMap, password ), "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      throw new RuntimeException( "Unable Encode Client State: " + e );
    }
  }

  protected String getClientStatePassword()
  {
    return ClientStatePasswordManager.getPassword() == null ? "123" : ClientStatePasswordManager.getPassword();
  }

  public Long getContestId()
  {
    return Long.parseLong( originalContestId );
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getStatusLabel()
  {
    return statusLabel;
  }

  public void setStatusLabel( String statusLabel )
  {
    this.statusLabel = statusLabel;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public String getRoleLabel()
  {
    return roleLabel;
  }

  public void setRoleLabel( String roleLabel )
  {
    this.roleLabel = roleLabel;
  }

  public Date getDateModified()
  {
    return dateModified;
  }

  public void setDateModified( Date dateModified )
  {
    this.dateModified = dateModified;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getReadOnlyUrl()
  {
    return readOnlyUrl;
  }

  public void setReadOnlyUrl( String readOnlyUrl )
  {
    this.readOnlyUrl = readOnlyUrl;
  }

  public String getDeniedReason()
  {
    return deniedReason;
  }

  public void setDeniedReason( String deniedReason )
  {
    this.deniedReason = deniedReason;
  }

  @Override
  // Sort by custom status order and then contest name
  public int compareTo( SSIContestListValueBean o )
  {
    int statusOrder = getStatusOrder( this.getStatus() ) - getStatusOrder( o.getStatus() );
    return statusOrder == 0 ? this.getName().compareTo( o.getName() ) : statusOrder;
  }

  private static int getStatusOrder( String status )
  {
    return contestStatusOrder.get( status ) != null ? contestStatusOrder.get( status ) : Integer.MAX_VALUE;
  }

  public String getManagerDetailPageUrl()
  {
    this.managerDetailPageUrl = "ssi/managerContestList.do?method=display";
    return managerDetailPageUrl;
  }

  public void setManagerDetailPageUrl( String managerDetailPageUrl )
  {
    this.managerDetailPageUrl = managerDetailPageUrl;
  }

  public Date getClaimSubmissionLastDate()
  {
    return claimSubmissionLastDate;
  }

  public void setClaimSubmissionLastDate( Date claimSubmissionLastDate )
  {
    this.claimSubmissionLastDate = claimSubmissionLastDate;
  }

  public String getDetailPageUrl()
  {
    return detailPageUrl;
  }

  public void setDetailPageUrl( String detailPageUrl )
  {
    this.detailPageUrl = detailPageUrl;
  }

  public String getContestTypeName()
  {
    return contestTypeName;
  }

  public void setContestTypeName( String contestTypeName )
  {
    this.contestTypeName = contestTypeName;
  }

  public String getUpdatedOn()
  {
    return updatedOn;
  }

  public void setUpdatedOn( String updatedOn )
  {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy()
  {
    return updatedBy;
  }

  public void setUpdatedBy( String updatedBy )
  {
    this.updatedBy = updatedBy;
  }

  public boolean isCanShowActionLinks()
  {
    return canShowActionLinks;
  }

  public void setCanShowActionLinks( boolean canShowActionLinks )
  {
    this.canShowActionLinks = canShowActionLinks;
  }

}

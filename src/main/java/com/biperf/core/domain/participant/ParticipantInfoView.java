/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.NameableBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ParticipantInfoView.
 * 
 * Use this Object for all pax JSON needs. Add to/Extend this class for additional Pax properties
 * as needed.
 * 
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>putta</th>
 * <th>12/03/2012</th>
 * <th>1.0</th>
 */
@JsonInclude( value = Include.NON_EMPTY )
public class ParticipantInfoView implements Serializable
{
  private static final long serialVersionUID = 1L;
  private long id;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private String largeAvatarUrl;
  private String countryName;
  private String countryCode;
  private String city;
  private Double countryRatio;
  // @JsonInclude( value = Include.NON_EMPTY ) will not include non empty or null value property
  // while
  // writing to response, We are using this in many places. If we use boolean instead as Boolean
  // default value
  // will be false and jackson library is writing to response to avoid it I have changed it to
  // Boolean
  private Boolean allowPublicRecognition;
  private Boolean allowPublicInformation;

  private Boolean followed;
  private String profileUrl;
  private String participantUrl;
  private Boolean self;
  private String jobName;
  private String departmentName;
  private Boolean isDelegate;
  private Boolean isLaunched;
  private String delegateFirstName;
  private String delegateLastName;
  private boolean globalParticipantSearchEnabled;
  private boolean throwdownEnabled;
  private String playerStatsUrl;
  private String orgName;
  private List<NameableBean> nodes;
  private List<BadgeView> badges = new ArrayList<BadgeView>();
  private List<DelegatorView> delegators = new ArrayList<DelegatorView>();
  private ParticipantAlertView alert = new ParticipantAlertView();
  private List<SidebarModuleBean> applicableSidebarModules = new ArrayList<SidebarModuleBean>();
  private String sourceType;
  private String termsAcceptedMsg;
  // Client customizations for wip #42701 starts
  private String currency;
  private Long awardMin;
  private Long awardMax;
  // Client customizations for wip #42701 ends
  /* coke customization start */
  /* isOptOut needs to be a String because it is a hidden field in participantRowAwardItem.jsp
   * Otherwise when you go from easy recog tile to submit rec page it does not retain
   * the optOut value. Or if you go from preview page back to first page it does not 
   * retain the value.
   */
  private String isOptOut;
  /* coke customization end */
  // Client customization for WIP 58122
  private boolean levelPayoutByApproverAvailable;
  // Client customizations for wip #26532 starts
  private boolean purlAllowOutsideDomains;
  // Client customizations for wip #26532 ends
  public ParticipantInfoView( Participant pax )
  {
    this.setId( pax.getId() );
    this.setFirstName( pax.getFirstName() );
    this.setLastName( pax.getLastName() );
    this.setAvatarUrl( pax.getAvatarSmallFullPath() );
    // this.setCountryName( pax.getPrimaryCountryName() );
    // this.setCountryCode( pax.getPrimaryCountryCode() );
    // this.setCity( pax.getPrimaryAddress().getAddress().getCity() );
    this.setCountryRatio( 1D ); // set to one by default
    this.setAllowPublicRecognition( pax.isAllowPublicRecognition() );
    this.setAllowPublicInformation( pax.isAllowPublicInformation() );
    this.setSelf( UserManager.getUserId().equals( pax.getId() ) );
    this.setProfileUrl( "" ); // set to empty by default
    /*customization start */
    if ( pax.getDepartmentType() != null )
    {
      //DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET, pax.getDepartmentType() );
      //this.setDepartmentName( departmentItem != null ? departmentItem.getName() : null );
      this.setDepartmentName( pax.getDepartmentType() );
    }
    if ( pax.getPositionType() != null )
    {
      //DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET, pax.getPositionType() );
      //this.setJobName( jobPositionItem != null ? jobPositionItem.getName() : null );
      this.setJobName( pax.getPositionType() );
    }
    /*customization end */
    // built using helper methods
    this.setNodes( getNodesAsList( new ArrayList<UserNode>( pax.getUserNodes() ) ) );
    this.setOrgName( getOrganizationName() );
    this.setSourceType( pax.getSourceType() );

  }

  public ParticipantInfoView( Promotion promotion, Participant pax )
  {
    this.setId( pax.getId() );
    this.setFirstName( pax.getFirstName() );
    this.setLastName( pax.getLastName() );
    this.setAvatarUrl( pax.getAvatarSmallFullPath() );
    // this.setCountryName( pax.getPrimaryCountryName() );
    // this.setCountryCode( pax.getPrimaryCountryCode() );
    // this.setCity( pax.getPrimaryAddress().getAddress().getCity() );
    this.setCountryRatio( 1D ); // set to one by default
    this.setAllowPublicRecognition( pax.isAllowPublicRecognition() );
    this.setAllowPublicInformation( pax.isAllowPublicInformation() );
    this.setSelf( UserManager.getUserId().equals( pax.getId() ) );
    this.setProfileUrl( "" ); // set to empty by default
    if ( pax.getDepartmentType() != null )
    {
      //DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET, pax.getDepartmentType() );
      //this.setDepartmentName( departmentItem != null ? departmentItem.getName() : null );
      this.setDepartmentName( pax.getDepartmentType() );
    }
    if ( pax.getPositionType() != null )
    {
      //DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET, pax.getPositionType() );
      //this.setJobName( jobPositionItem != null ? jobPositionItem.getName() : null );
      this.setJobName( pax.getPositionType() );
    }
    // built using helper methods
    this.setNodes( getNodesAsList( new ArrayList<UserNode>( pax.getUserNodes() ) ) );

    /* coke customization start */
    this.setIsOptOut( String.valueOf( getParticipantService().isOptedOut( pax.getId() ) ) );
    /* coke customization end */

    // Client customizations for WIP #42701 starts
    if ( promotion != null && promotion.getAdihCashOption() != null && promotion.getAdihCashOption().booleanValue() )
    {
      this.setCurrency( getUserService().getUserCurrencyCharValue( pax.getId() ) );
    }
    // Client customizations for WIP #42701 ends
    
    // Client customization for WIP 58122 starts
    if ( promotion != null &&  promotion.isNominationPromotion()  )
    {
    	NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      this.setLevelPayoutByApproverAvailable(nominationPromotion.isLevelPayoutByApproverAvailable() );
    }
    // Client customization for WIP 58122 ends
  }

  public ParticipantInfoView()
  {
    //
  }

  public ParticipantAlertView getAlert()
  {
    return alert;
  }

  public void setAlert( ParticipantAlertView alert )
  {
    this.alert = alert;
  }

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    if ( StringUtils.isNotBlank( firstName ) && ! ( firstName.startsWith( "${" ) || firstName.startsWith( "#{" ) ) )
    {
      this.firstName = firstName;
    }
    else
    {
      this.firstName = "";
    }
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    if ( StringUtils.isNotBlank( lastName ) && ! ( lastName.startsWith( "${" ) || lastName.startsWith( "#{" ) ) )
    {
      this.lastName = lastName;
    }
    else
    {
      this.lastName = "";
    }

  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getLargeAvatarUrl()
  {
    return largeAvatarUrl;
  }

  public void setLargeAvatarUrl( String largeAvatarUrl )
  {
    this.largeAvatarUrl = largeAvatarUrl;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public String getCountryCode()
  {
    return countryCode;
  }

  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity( String city )
  {
    this.city = city;
  }

  public Double getCountryRatio()
  {
    return countryRatio;
  }

  public void setCountryRatio( Double countryRatio )
  {
    this.countryRatio = countryRatio;
  }

  public Boolean isAllowPublicRecognition()
  {
    return allowPublicRecognition;
  }

  public void setAllowPublicRecognition( Boolean allowPublicRecognition )
  {
    this.allowPublicRecognition = allowPublicRecognition;
  }

  public Boolean isAllowPublicInformation()
  {
    return allowPublicInformation;
  }

  public void setAllowPublicInformation( Boolean allowPublicInformation )
  {
    this.allowPublicInformation = allowPublicInformation;
  }

  public List<NameableBean> getNodes()
  {
    return nodes;
  }

  public void setNodes( List<NameableBean> nodes )
  {
    this.nodes = nodes;
  }

  @JsonProperty( "isFollowed" )
  public Boolean getFollowed()
  {
    return followed;
  }

  public void setFollowed( Boolean followed )
  {
    this.followed = followed;
  }

  public String getProfileUrl()
  {
    return profileUrl;
  }

  public void setProfileUrl( String profileUrl )
  {
    this.profileUrl = profileUrl;
  }

  @JsonProperty( "isSelf" )
  public Boolean isSelf()
  {
    return self;
  }

  public void setSelf( Boolean self )
  {
    this.self = self;
  }

  public List<BadgeView> getBadges()
  {
    return badges;
  }

  public void setBadges( List<BadgeView> badges )
  {
    this.badges = badges;
  }

  public void addBadge( BadgeView badge )
  {
    badges.add( badge );
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) ( id ^ id >>> 32 );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    ParticipantInfoView other = (ParticipantInfoView)obj;
    if ( id != other.id )
    {
      return false;
    }
    return true;
  }

  @JsonProperty( "isPublic" )
  public Boolean getIsPublic()
  {
    return this.isAllowPublicInformation();
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String jobName )
  {
    this.jobName = jobName;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  //
  // Helper methods
  //
  public static List<NameableBean> getNodesAsList( List<UserNode> userNodes )
  {
    List<NameableBean> nodeList = new ArrayList<NameableBean>();
    Collections.sort( userNodes, new Comparator<UserNode>()
    {
      public int compare( UserNode un1, UserNode un2 )
      {
        if ( un1.getIsPrimary() )
        {
          return -1;
        }
        else if ( un2.getIsPrimary() )
        {
          return 1;
        }
        else
        {
          return un1.getNode().getName().compareTo( un1.getNode().getName() );
        }
      }
    } );

    for ( Iterator<UserNode> it = userNodes.iterator(); it.hasNext(); )
    {
      UserNode userNode = it.next();
      NameableBean bean = new NameableBean( userNode.getNode().getId(), userNode.getNode().getName() );
      nodeList.add( bean );
    }

    return nodeList;
  }

  public List<DelegatorView> getDelegators()
  {
    return delegators;
  }

  public void setDelegators( List<DelegatorView> delegators )
  {
    this.delegators = delegators;
  }

  @JsonProperty( "isDelegate" )
  public Boolean isDelegate()
  {
    return isDelegate;
  }

  public void setDelegate( Boolean isDelegate )
  {
    this.isDelegate = isDelegate;
  }

  @JsonProperty( "isLaunched" )
  public Boolean isLaunched()
  {
    return isLaunched;
  }

  public void setLaunched( Boolean isLaunched )
  {
    this.isLaunched = isLaunched;
  }

  public String getDelegateFirstName()
  {
    return delegateFirstName;
  }

  public void setDelegateFirstName( String delegateFirstName )
  {
    this.delegateFirstName = delegateFirstName;
  }

  public String getDelegateLastName()
  {
    return delegateLastName;
  }

  public void setDelegateLastName( String delegateLastName )
  {
    this.delegateLastName = delegateLastName;
  }

  public void setGlobalParticipantSearchEnabled( boolean globalParticipantSearchEnabled )
  {
    this.globalParticipantSearchEnabled = globalParticipantSearchEnabled;
  }

  @JsonProperty( "largeAudience" )
  public boolean isGlobalParticipantSearchEnabled()
  {
    return globalParticipantSearchEnabled;
  }

  public void setParticipantUrl( String participantUrl )
  {
    this.participantUrl = participantUrl;
  }

  public String getParticipantUrl()
  {
    return participantUrl;
  }

  public void setThrowdownEnabled( boolean throwdownEnabled )
  {
    this.throwdownEnabled = throwdownEnabled;
  }

  @JsonProperty( "isThrowdownEnabled" )
  public boolean isThrowdownEnabled()
  {
    return throwdownEnabled;
  }

  public String getPlayerStatsUrl()
  {
    return playerStatsUrl;
  }

  public void setPlayerStatsUrl( String playerStatsUrl )
  {
    this.playerStatsUrl = playerStatsUrl;
  }

  public String getOrganizationName()
  {
    if ( this.getNodes() != null && this.getNodes().size() > 0 )
    {
      return this.getNodes().get( 0 ).getName();
    }

    return "";
  }

  public void setOrganizationName( String organizationName )
  {
    this.nodes = new ArrayList<NameableBean>();
    this.nodes.add( new NameableBean( new Long( orgName.hashCode() ), orgName ) );
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public List<SidebarModuleBean> getApplicableSidebarModules()
  {
    return applicableSidebarModules;
  }

  public void setApplicableSidebarModules( List<SidebarModuleBean> applicableSidebarModules )
  {
    this.applicableSidebarModules = applicableSidebarModules;
  }

  public void addSidebarModule( String moduleName, String appName )
  {
    if ( this.applicableSidebarModules == null )
    {
      this.applicableSidebarModules = new ArrayList<SidebarModuleBean>();
    }

    SidebarModuleBean sidebarModuleBean = new SidebarModuleBean();
    sidebarModuleBean.setName( moduleName );
    sidebarModuleBean.setAppName( appName );
    this.applicableSidebarModules.add( sidebarModuleBean );

  }

  public String getSourceType()
  {
    return sourceType;
  }

  public void setSourceType( String sourceType )
  {
    this.sourceType = sourceType;
  }

  public String getTermsAcceptedMsg()
  {
    return termsAcceptedMsg;
  }

  public void setTermsAcceptedMsg( String termsAcceptedMsg )
  {
    this.termsAcceptedMsg = termsAcceptedMsg;
  }

  // Client customizations for wip #26532 ends

  // Client customizations for wip #42701 starts
  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  public String getCurrency()
  {
    return currency;
  }

  public void setCurrency( String currency )
  {
    this.currency = currency;
  }

  public Long getAwardMin()
  {
    return awardMin;
  }

  public void setAwardMin( Long awardMin )
  {
    this.awardMin = awardMin;
  }

  public Long getAwardMax()
  {
    return awardMax;
  }

  public void setAwardMax( Long awardMax )
  {
    this.awardMax = awardMax;
  }
  // Client customizations for wip #42701 ends
  /* coke customization start */
  private static ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  public String getIsOptOut()
  {
    return isOptOut;
  }

  public void setIsOptOut( String isOptOut )
  {
    this.isOptOut = isOptOut;
  }
  /* coke customization end */

  // Client customization for WIP 58122
public boolean isLevelPayoutByApproverAvailable() {
	return levelPayoutByApproverAvailable;
}

public void setLevelPayoutByApproverAvailable(boolean levelPayoutByApproverAvailable) {
	this.levelPayoutByApproverAvailable = levelPayoutByApproverAvailable;
}

// Client customizations for wip #26532 starts
public boolean isPurlAllowOutsideDomains()
{
  return purlAllowOutsideDomains;
}

public void setPurlAllowOutsideDomains( boolean purlAllowOutsideDomains )
{
  this.purlAllowOutsideDomains = purlAllowOutsideDomains;
}
// Client customizations for wip #26532 ends
}

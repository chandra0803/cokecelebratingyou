
package com.biperf.core.ui.roster;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.company.Company;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.domain.enums.ProcessParameterDataType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.BadRequestException;
import com.biperf.core.exception.ResourceNotFoundException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.mobileapp.recognition.service.UserDeviceService;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.company.CompanyService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.employer.EmployerService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToNodeCharacteristicAssociationRequest;
import com.biperf.core.service.hierarchy.NodeToNodeTypeAssociationRequest;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.participant.impl.AudienceListValueBean;
import com.biperf.core.service.quicksearch.QuickSearchService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.participant.CriteriaAudienceValue;
import com.biperf.core.ui.participant.ListBuilderForm;
import com.biperf.core.ui.roster.enums.GenericTypeEnum;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.participant.AudienceDetail;
import com.biw.digs.rest.enums.NamedEnum;
import com.biw.digs.rest.enums.RoleTypeEnum;
import com.biw.digs.rest.request.CreateHierarchyNodeAttributeRequest;
import com.biw.digs.rest.request.GroupCreateRequest;
import com.biw.digs.rest.request.HierarchyNodeRequest;
import com.biw.digs.rest.response.AttributeDescriptionView;
import com.biw.digs.rest.response.AttributeDescriptions;
import com.biw.digs.rest.response.AttributeView;
import com.biw.digs.rest.response.GroupView;
import com.biw.digs.rest.response.HierarchyNodeDetail;
import com.biw.digs.rest.response.HierarchyNodeView;
import com.biw.digs.rest.response.HierarchyView;
import com.biw.digs.rest.response.PersonAddressView;
import com.biw.digs.rest.response.PersonEmailView;
import com.biw.digs.rest.response.PersonFullView;
import com.biw.digs.rest.response.PersonHierarchyNodeView;
import com.biw.digs.rest.response.PersonPhoneView;
import com.biw.digs.rest.response.PersonPronouns;
import com.biw.digs.rest.response.PersonPushNotificationSubscriptionView;
import com.biw.digs.rest.response.PersonSearch;
import com.biw.digs.rest.response.PersonView;
import com.objectpartners.cms.util.CmsResourceBundle;

public abstract class RosterBaseController
{
  protected @Autowired CountryService countryService;
  protected @Autowired UserService userService;
  protected @Autowired AuthenticationService authenticationService;
  protected @Autowired SystemVariableService systemVariableService;
  protected @Autowired UserDeviceService userDeviceService;
  protected @Autowired AudienceService audienceService;
  protected @Autowired ParticipantService participantService;
  protected @Autowired UserCharacteristicService userCharacteristicService;
  protected @Autowired HierarchyService hierarchyService;
  protected @Autowired NodeService nodeService;
  protected @Autowired QuickSearchService quickSearch;
  protected @Autowired ListBuilderService listBuilderService;
  protected @Autowired NodeTypeService nodeTypeService;
  protected @Autowired AutoCompleteService autoCompleteService;
  protected @Autowired NodeTypeCharacteristicService nodeTypeCharacteristicService;
  protected @Autowired ProfileService profileService;
  protected @Autowired EmployerService employerService;
  protected @Autowired CompanyService companyService;

  protected static final String CM_KEY_PREFIX = "roster.compliance.pronouns.info.";
  public static final String PRIMARY_EMAIL_DELETE_ERR = "user.email.errors.CANNOT_DELETE";
  public static final String PARTICIPANT_ADDRESS_DELETE_PRIMARY = "participant.address.errors.DELETE_PRIMARY_ERROR";
  public static final String PARTICIPANT_PHONE_DELETE_PRIMARY = "participant.phone.list.PRIMARY_PHONE_DO_NOT_DELETE";

  // TODO : Name will be decide later
  public static final String HIERARCHY_ROSTER = "hierarchy.roster";

  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";
  public static final String GOOD = "00";

  private static final Log log = LogFactory.getLog( RosterBaseController.class );

  protected PersonView buildPersonView( User person )
  {
    return assignPersonViewAttributes( new PersonView(), person );
  }

  @SuppressWarnings( { "unchecked", "deprecation" } )
  protected PersonView assignPersonViewAttributes( PersonView view, User person )
  {
    view.setGivenName( person.getFirstName() );
    view.setId( person.getRosterUserId() );
    view.setMiddleName( person.getMiddleName() );
    view.setNickName( null );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant participant = participantService.getParticipantByIdWithAssociations( person.getId(), associationRequestCollection );
    ParticipantEmployer participantEmplr = participantService.getCurrentParticipantEmployer( participant );

    if ( Objects.nonNull( participantEmplr ) )
    {
      if ( Objects.nonNull( participantEmplr.getHireDate() ) )
      {
        view.setHireDate( new java.sql.Date( participantEmplr.getHireDate().getTime() ).toLocalDate() );
      }
      if ( Objects.nonNull( participantEmplr.getTerminationDate() ) )
      {
        view.setTerminationDate( new java.sql.Date( participantEmplr.getTerminationDate().getTime() ).toLocalDate() );
      }
    }
    view.setPronouns( buildPersonPronouns( person ) );
    if ( Objects.nonNull( person.getSuffixType() ) )
    {
      view.setSuffix( person.getSuffixType().getCode() );
    }
    if ( Objects.nonNull( person.getTitleType() ) )
    {
      view.setTitle( person.getTitleType().getCode() );
    }

    view.setSurname( person.getLastName() );
    view.setExternalId( person.getUserName() );
    view.setUsername( person.getUserName() );
    if ( person.getLanguageType() != null )
    {
      String languageCode = person.getLanguageType().getCode();
      view.setLocale( LocaleUtils.getLocale( languageCode ) );
    }
    Country country = userService.getPrimaryUserAddressCountry( person.getId() );
    if ( Objects.nonNull( country ) )
    {
      view.setCountry( country.getAwardbanqAbbrev() );
    }

    return view;
  }

  protected PersonPronouns buildPersonPronouns( User person )
  {
    PersonPronouns pp = new PersonPronouns();
    if ( Objects.nonNull( person.getGenderType() ) )
    {
      if ( person.getGenderType().getCode().equalsIgnoreCase( "m" ) )
      {
        pp.setObjective( CmsResourceBundle.getCmsBundle().getString( CM_KEY_PREFIX + "PRONOUNS_HE" ) );
        pp.setSubjective( CmsResourceBundle.getCmsBundle().getString( CM_KEY_PREFIX + "PRONOUNS_HIM" ) );

      }
      else
      {
        pp.setObjective( CmsResourceBundle.getCmsBundle().getString( CM_KEY_PREFIX + "PRONOUNS_SHE" ) );
        pp.setSubjective( CmsResourceBundle.getCmsBundle().getString( CM_KEY_PREFIX + "PRONOUNS_HER" ) );
      }
    }
    return pp;
  }

  protected UUID getCompanyId()
  {
    UUID companyUUID = null;

    Company company = companyService.getCompanyDetail();

    if ( Objects.nonNull( company.getCompanyId() ) )
    {
      companyUUID = company.getCompanyId();
    }
    return companyUUID;
  }

  public String buildCMSMessage( String key )
  {
    return CmsResourceBundle.getCmsBundle().getString( key );
  }

  // Person email view
  protected PersonEmailView buildPersonEmailView( UserEmailAddress email )
  {
    PersonEmailView view = new PersonEmailView();
    view.setId( email.getRosterEmailId() );
    view.setAddress( email.getEmailAddr() );
    view.setLabel( getType( email.getEmailType().getCode() ) );
    return view;
  }

  // get all person postal address.
  protected PersonAddressView buildPersonAddressView( UserAddress address )
  {
    PersonAddressView view = new PersonAddressView();
    view.setId( address.getRosterAddressId() );
    view.setLabel( getType( address.getAddressType().getCode() ) );

    if ( Objects.nonNull( address.getAddress() ) )
    {
      view.setCity( address.getAddress().getCity() );
      view.setLine1( address.getAddress().getAddr1() );
      view.setLine2( address.getAddress().getAddr2() );
      view.setLine3( address.getAddress().getAddr3() );
      view.setPostalCode( address.getAddress().getPostalCode() );
    }

    if ( Objects.nonNull( address.getAddress().getCountry() ) )
    {
      view.setCountryCode( address.getAddress().getCountry().getAwardbanqAbbrev() );
    }

    if ( Objects.nonNull( address.getAddress().getStateType() ) )
    {
      view.setState( address.getAddress().getStateType().getName() );
      view.setProvince( address.getAddress().getStateType().getName() );

    }
    return view;
  }

  // get all person phone numbers
  protected PersonPhoneView buildPersonPhoneView( UserPhone phone )
  {
    PersonPhoneView view = new PersonPhoneView();
    view.setId( phone.getRosterPhoneId() );
    view.setCountryPhoneCode( phone.getCountryPhoneCode() );
    view.setPhoneExt( phone.getPhoneExt() );
    view.setPhoneNumber( phone.getPhoneNbr().replaceAll( "-", "" ) );
    view.setLabel( getType( phone.getPhoneType().getCode() ) );
    return view;
  }

  public AttributeDescriptions getAttributeDescriptionsForHierarchyNodes( UUID companyId )
  {
    // TODO Auto-generated method stub
    return null;
  }

  protected List<GroupView> buildGroupView( List<AudienceListValueBean> audienceListValueBean )
  {
    List<GroupView> groupView = new ArrayList<GroupView>();

    if ( !audienceListValueBean.isEmpty() )
    {
      audienceListValueBean.forEach( val ->
      {
        GroupView response = new GroupView();
        response.setCompanyId( getCompanyId() );
        response.setId( val.getRosterAudienceId() );
        response.setName( val.getAudienceName() );
        response.setType( getType( val.getAudienceType() ) );
        response.setGroupPublic( val.getPublicAudience() );
        groupView.add( response );

      } );
    }
    return groupView;
  }

  protected GroupView buildGroupView( Audience audience )
  {
    GroupView groupView = new GroupView();

    if ( Objects.nonNull( audience ) )
    {
      groupView.setCompanyId( getCompanyId() );
      groupView.setId( audience.getRosterAudienceId() );
      groupView.setName( audience.getName() );
      groupView.setType( getType( audience.getAudienceType().getCode() ) );
      groupView.setGroupPublic( audience.getPublicAudience() );

    }
    return groupView;
  }

  protected AttributeView buildPersonAttributeView( UserCharacteristic attribute )
  {
    AttributeView view = new AttributeView();

    if ( attribute.getUserCharacteristicType().getCharacteristicDataType().isTextType() || attribute.getUserCharacteristicType().getCharacteristicDataType().isSingleSelect()
        || attribute.getUserCharacteristicType().getCharacteristicDataType().isMultiSelect() )
    {
      view.setDataFormat( ProcessParameterDataType.String );
      view.setDataType( ProcessParameterDataType.String );

    }
    else if ( attribute.getUserCharacteristicType().getCharacteristicDataType().isDecimalType() )
    {
      view.setDataFormat( CharacteristicDataType.DECIMAL );
      view.setDataType( CharacteristicDataType.DECIMAL );
    }
    else if ( attribute.getUserCharacteristicType().getCharacteristicDataType().isIntegerType() )
    {
      view.setDataFormat( CharacteristicDataType.INTEGER );
      view.setDataType( CharacteristicDataType.INTEGER );

    }
    else if ( attribute.getUserCharacteristicType().getCharacteristicDataType().isBooleanType() )
    {
      view.setDataFormat( CharacteristicDataType.BOOLEAN );
      view.setDataType( CharacteristicDataType.BOOLEAN );

    }
    else if ( attribute.getUserCharacteristicType().getCharacteristicDataType().isDateType() )
    {
      view.setDataFormat( CharacteristicDataType.DATE );
      view.setDataType( CharacteristicDataType.DATE );

    }

    view.setName( attribute.getUserCharacteristicType().getCharacteristicName() );
    view.setValue( attribute.getCharacteristicValue() );
    view.setId( attribute.getRosterUserCharId() );

    return view;
  }

  protected List<PersonView> buildPersonView( List<User> users )
  {
    List<PersonView> views = new ArrayList<PersonView>();

    users.forEach( v ->
    {
      PersonView view = new PersonView();

      view.setGivenName( v.getFirstName() );
      view.setId( v.getRosterUserId() );
      view.setMiddleName( v.getMiddleName() );
      view.setNickName( null );

      view.setPronouns( buildPersonPronouns( v ) );
      if ( Objects.nonNull( v.getSuffixType() ) )
      {
        view.setSuffix( v.getSuffixType().getCode() );
      }
      if ( Objects.nonNull( v.getTitleType() ) )
      {
        view.setTitle( v.getTitleType().getCode() );
      }

      view.setSurname( v.getLastName() );
      view.setExternalId( v.getId().toString() );
      view.setLocale( UserManager.getLocale() );
      Country country = userService.getPrimaryUserAddressCountry( v.getId() );
      if ( Objects.nonNull( country ) )
      {
        view.setCountry( country.getAwardbanqAbbrev() );
      }

      views.add( view );

    } );

    return views;

  }

  // TODO : Half implementation will re-work later based on the digs implementation.
  @SuppressWarnings( { "unused", "null" } )
  public GroupView createAudienceGroup( GroupCreateRequest groupCreateRequest )
  {
    Audience audience;
    GroupView groupView = new GroupView();
    Long audienceId = new Long( 0 );

    if ( groupCreateRequest.getType().equalsIgnoreCase( ListBuilderForm.CRITERIA_SEARCH_TYPE ) )
    {
      // Persist criteria audience

      audience = new CriteriaAudience();
      CriteriaAudience criteriaAudience = (CriteriaAudience)audience;

      // Here we've to place the list of participants belongs to this group.
      CriteriaAudienceValue criteriaAudienceValue = null;

      if ( Objects.nonNull( criteriaAudienceValue ) )
      {
        // Get AudienceCriteria objects from view object.
        criteriaAudienceValue.getAudienceCriteriaValueList().forEach( audienceCriteriaValue -> criteriaAudience.addAudienceCriteria( audienceCriteriaValue.getAudienceCriteria() ) );
      }

      criteriaAudience.setName( groupCreateRequest.getName() );
      criteriaAudience.setPublicAudience( groupCreateRequest.isGroupPublic() );
      criteriaAudience.getAudienceType().setCode( groupCreateRequest.getType() );

      // If insert or name change, check and see if audience name is unique.
      String existingName = null;
      if ( Objects.nonNull( criteriaAudience.getId() ) )
      {
        Audience existingAudience = audienceService.getAudienceById( criteriaAudience.getId(), null );
        existingName = existingAudience.getName();
      }
      boolean nameIsUnique = audienceService.isAudienceNameUnique( criteriaAudience.getName() );
      boolean isUpdate = criteriaAudience.getId() != null;

      if ( isUpdate && !criteriaAudience.getName().equals( existingName ) && !nameIsUnique || !isUpdate && !nameIsUnique )
      {
        throw new BadRequestException( CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.AUDIENCE_DUPLICATE ) );

      }

      // save the criteria
      // save the list of participants who match the criteria
      try
      {
        audienceService.save( criteriaAudience );
        audienceService.recreateCriteriaAudienceParticipants( criteriaAudience.getId() );
        audienceId = criteriaAudience.getId();
      }
      catch( ServiceErrorException e )
      {
        throw new BadRequestException( e.getMessage() );
      }

      groupView.setCompanyId( getCompanyId() );
      groupView.setName( criteriaAudience.getName() );
      groupView.setId( criteriaAudience.getRosterAudienceId() );
      groupView.setType( criteriaAudience.getAudienceType().getCode() );
      groupView.setGroupPublic( criteriaAudience.getPublicAudience() );

    }
    else if ( groupCreateRequest.getType().equalsIgnoreCase( ListBuilderForm.PARTICIPANT_SEARCH_TYPE ) )
    {
      // Persist participant audience
      PaxAudience paxAudience = new PaxAudience();

      paxAudience.setName( groupCreateRequest.getName() );
      paxAudience.setPublicAudience( groupCreateRequest.isGroupPublic() );
      paxAudience.getAudienceType().setCode( groupCreateRequest.getType() );

      paxAudience.getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
      paxAudience.getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );

      // If insert or "update with name change", check and see if audience name is unique.
      String existingName = null;
      if ( Objects.nonNull( paxAudience.getId() ) )
      {
        Audience existingAudience = audienceService.getAudienceById( paxAudience.getId(), null );
        existingName = existingAudience.getName();
      }
      boolean nameIsUnique = audienceService.isAudienceNameUnique( paxAudience.getName() );
      boolean isUpdate = paxAudience.getId() != null;
      if ( isUpdate && !paxAudience.getName().equals( existingName ) && !nameIsUnique || !isUpdate && !nameIsUnique )
      {
        throw new BadRequestException( CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.AUDIENCE_DUPLICATE ) );

      }

      // As of now placed empty,
      // List paxList = ListBuilderController.buildSelectedResultList(
      // listBuilderForm.getSelectedBox() );
      List<FormattedValueBean> paxList = new ArrayList<FormattedValueBean>();

      try
      {

        if ( Objects.nonNull( paxList ) )
        {
          paxList.forEach( formattedValueBean ->
          {
            Long paxId = formattedValueBean.getId();
            Participant participant = participantService.getParticipantById( paxId );
            paxAudience.addParticipant( participant );
          } );

        }
        audienceService.save( paxAudience );
        audienceId = paxAudience.getId();
      }
      catch( ServiceErrorException se )
      {
        throw new BadRequestException( se.getMessage() );
      }

      groupView.setCompanyId( getCompanyId() );
      groupView.setName( paxAudience.getName() );
      groupView.setId( paxAudience.getRosterAudienceId() );
      groupView.setType( paxAudience.getAudienceType().getCode() );
      groupView.setGroupPublic( paxAudience.getPublicAudience() );

    }
    else
    {
      throw new BadRequestException( "Invalid group type" );
    }

    return groupView;
  }

  protected HierarchyNodeView buildHierarchyNodeView( UserNode node )
  {
    HierarchyNodeView view = new HierarchyNodeView();
    view.setHierarchyId( node.getNode().getHierarchy().getRosterHierarchyId() );
    view.setId( node.getNode().getRosterNodeId() );
    view.setName( node.getNode().getName() );
    return view;
  }

  protected List<HierarchyView> buildHierarchyView( List<Hierarchy> hierarchyList )
  {
    return hierarchyList.stream().map( this::buildHierarchyView ).collect( Collectors.toList() );
  }

  protected HierarchyView buildHierarchyView( Hierarchy hierarchy )
  {
    HierarchyView hierarchyView = new HierarchyView();

    hierarchyView.setCompanyId( getCompanyId() );
    hierarchyView.setId( hierarchy.getRosterHierarchyId() );
    hierarchyView.setName( CmsResourceBundle.getCmsBundle().getString( hierarchy.getCmAssetCode(), hierarchy.getNameCmKey() ) );

    return hierarchyView;
  }

  protected List<HierarchyView> buildHierarchyView( List<Hierarchy> hierarchyList, Long hierarchyId, String name )
  {
    List<HierarchyView> hierarchyViewList = null;

    if ( !hierarchyList.isEmpty() )
    {
      if ( Objects.nonNull( hierarchyId ) && Objects.nonNull( name ) )
      {
        hierarchyViewList = hierarchyList.stream()
            .filter( val -> val.getId().longValue() == hierarchyId.longValue() && name.equalsIgnoreCase( CmsResourceBundle.getCmsBundle().getString( val.getCmAssetCode(), val.getNameCmKey() ) ) )
            .map( this::buildHierarchyView ).collect( Collectors.toList() );
      }
      else if ( Objects.nonNull( hierarchyId ) )
      {
        hierarchyViewList = hierarchyList.stream().filter( val -> val.getId().longValue() == hierarchyId.longValue() ).map( this::buildHierarchyView ).collect( Collectors.toList() );
      }
      else if ( Objects.nonNull( name ) )
      {
        hierarchyViewList = hierarchyList.stream().filter( val -> name.equalsIgnoreCase( CmsResourceBundle.getCmsBundle().getString( val.getCmAssetCode(), val.getNameCmKey() ) ) )
            .map( this::buildHierarchyView ).collect( Collectors.toList() );
      }
    }

    return hierarchyViewList;
  }

  @SuppressWarnings( "unchecked" )
  protected HierarchyNodeDetail buildHierarchyNodeDetail( Node node )
  {
    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );
    node = nodeService.getNodeWithAssociationsById( node.getId(), nodeAssociationRequestCollection );

    HierarchyNodeDetail detail = new HierarchyNodeDetail();
    detail.setHierarchyId( node.getHierarchy().getRosterHierarchyId() );
    detail.setId( node.getRosterNodeId() );
    detail.setName( node.getName() );
    Node parent = node.getParentNode();
    detail.setParentId( ( null != parent ) ? parent.getRosterNodeId() : null );
    Set<NodeCharacteristic> nodeCharacteristics = node.getActiveNodeCharacteristics();

    if ( !nodeCharacteristics.isEmpty() )
    {
      detail.setAttributes( nodeCharacteristics.stream().map( this::buildHierarchyNodeAttributeView ).collect( Collectors.toList() ) );
    }

    return detail;
  }

  protected AttributeView buildHierarchyNodeAttributeView( NodeCharacteristic attribute )
  {

    AttributeView view = new AttributeView();

    if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isTextType() || attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isSingleSelect()
        || attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isMultiSelect() )
    {
      view.setDataFormat( ProcessParameterDataType.String );
      view.setDataType( ProcessParameterDataType.String );

    }
    else if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isDecimalType() )
    {
      view.setDataFormat( CharacteristicDataType.DECIMAL );
      view.setDataType( CharacteristicDataType.DECIMAL );
    }
    else if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isIntegerType() )
    {
      view.setDataFormat( CharacteristicDataType.INTEGER );
      view.setDataType( CharacteristicDataType.INTEGER );

    }
    else if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isBooleanType() )
    {
      view.setDataFormat( CharacteristicDataType.BOOLEAN );
      view.setDataType( CharacteristicDataType.BOOLEAN );

    }
    else if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isDateType() )
    {
      view.setDataFormat( CharacteristicDataType.DATE );
      view.setDataType( CharacteristicDataType.DATE );

    }

    view.setName( attribute.getNodeTypeCharacteristicType().getCharacteristicName() );
    view.setValue( attribute.getCharacteristicValue() );
    view.setId( attribute.getRosterNodeCharId() );

    return view;

  }

  @SuppressWarnings( "unchecked" )
  protected AttributeDescriptionView buildAttributeDescriptionView( Node node )
  {
    AttributeDescriptionView view = new AttributeDescriptionView();
    Set<NodeCharacteristic> nodeCharacteristics = node.getActiveNodeCharacteristics();
    if ( !nodeCharacteristics.isEmpty() )
    {
      nodeCharacteristics.stream().forEach( attribute ->
      {
        view.setName( attribute.getNodeTypeCharacteristicType().getCharacteristicName() );

        if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isTextType() || attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isSingleSelect()
            || attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isMultiSelect() )
        {
          view.setDataFormat( ProcessParameterDataType.String );
          view.setDataType( ProcessParameterDataType.String );

        }
        else if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isDecimalType() )
        {
          view.setDataFormat( CharacteristicDataType.DECIMAL );
          view.setDataType( CharacteristicDataType.DECIMAL );
        }
        else if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isIntegerType() )
        {
          view.setDataFormat( CharacteristicDataType.INTEGER );
          view.setDataType( CharacteristicDataType.INTEGER );

        }
        else if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isBooleanType() )
        {
          view.setDataFormat( CharacteristicDataType.BOOLEAN );
          view.setDataType( CharacteristicDataType.BOOLEAN );

        }
        else if ( attribute.getNodeTypeCharacteristicType().getCharacteristicDataType().isDateType() )
        {
          view.setDataFormat( CharacteristicDataType.DATE );
          view.setDataType( CharacteristicDataType.DATE );

        }

      } );
    }

    return view;
  }

  /*
   * Roster is creating hierarchy without is active attribute, so automatically it would be false.
   * So pulled all the hierarchies and name is not unique identifier in our application and it has
   * been stored in CM.
   */

  @SuppressWarnings( "unchecked" )
  protected void validStructureRequest( String hierarchyName )
  {
    List<Hierarchy> hierarchies = hierarchyService.getAll();

    Optional<Hierarchy> optionalHierarchyNode = hierarchies.stream()
        .filter( val -> hierarchyName.equalsIgnoreCase( CmsResourceBundle.getCmsBundle().getString( val.getCmAssetCode(), val.getNameCmKey() ) ) ).findFirst();

    if ( optionalHierarchyNode.isPresent() )
    {
      throw new BadRequestException( "Hierarchy with name '" + hierarchyName + "' is already exists" );
    }

  }

  protected boolean isRosterNodeTypeExist( List<NodeType> nodeTypes )
  {
    Optional<NodeType> optionalNodeType = getRosterOptionalNodeType( nodeTypes );

    if ( optionalNodeType.isPresent() )
    {
      return true;
    }

    return false;
  }

  protected NodeType createRosterNodeType()
  {
    NodeType responseNodeType = null;
    NodeType nodeType = new NodeType();
    nodeType.setName( HIERARCHY_ROSTER );

    try
    {
      responseNodeType = nodeTypeService.saveNodeType( nodeType );
    }
    catch( ServiceErrorException e )
    {
      throw new BadRequestException( "Create hierarchy is unsuccessful." );
    }

    return responseNodeType;
  }

  protected NodeType getRosterNodeType( List<NodeType> nodeTypes )
  {
    Optional<NodeType> optionalNodeType = getRosterOptionalNodeType( nodeTypes );

    if ( optionalNodeType.isPresent() )
    {
      return optionalNodeType.get();
    }
    else
    {
      throw new BadRequestException( "Create hierarchy is unsuccessful." );
    }

  }

  protected Optional<NodeType> getRosterOptionalNodeType( List<NodeType> nodeTypes )
  {
    return nodeTypes.stream().filter( val -> HIERARCHY_ROSTER.equalsIgnoreCase( CmsResourceBundle.getCmsBundle().getString( val.getCmAssetCode(), val.getNameCmKey() ) ) ).findFirst();
  }

  protected Node getNodeDomainObject( HierarchyNodeRequest request, Long hierarchyId, Hierarchy createHierarchyObj, NodeType rosterNodetype )
  {
    Node node = new Node();
    node.setName( request.getName() );

    Hierarchy hierarchy = new Hierarchy();

    hierarchy.setId( hierarchyId );
    node.setHierarchy( createHierarchyObj );

    NodeType nodeType = new NodeType();
    nodeType.setId( rosterNodetype.getId() );

    node.setNodeType( rosterNodetype );

    return node;
  }

  protected void buildChildNodes( HierarchyNodeRequest child, Node parentNode, Hierarchy createHierarchyObj, NodeType rosterNodetype )
  {
    if ( !child.getChildren().isEmpty() )
    {
      for ( HierarchyNodeRequest c : child.getChildren() )
      {
        Node savedNode = nodeService.saveNode( getNodeDomainObject( c, createHierarchyObj.getId(), createHierarchyObj, rosterNodetype ), parentNode.getId(), true );

        buildChildNodes( c, savedNode, createHierarchyObj, rosterNodetype );
      }
    }

  }

  protected HierarchyView buildHierarchyViewComplete( Hierarchy hierarchy )
  {
    HierarchyView response = buildHierarchyView( hierarchy );
    Set<HierarchyNodeView> nodes = new HashSet<HierarchyNodeView>();
    Node node = nodeService.getRootNode( hierarchy.getId(), null );

    if ( Objects.nonNull( node ) )
    {
      nodes.add( buildNodeTree( node ) );
    }

    response.setHierarchyNodes( nodes );
    return response;
  }

  @SuppressWarnings( "unchecked" )
  protected HierarchyNodeView buildNodeTree( Node parent )
  {
    HierarchyNodeView view = new HierarchyNodeView();
    view.setId( parent.getRosterNodeId() );
    view.setName( parent.getName() );
    view.setParentId( Objects.nonNull( parent.getParentNode() ) ? parent.getParentNode().getRosterNodeId() : null );
    view.setHierarchyId( parent.getHierarchy().getRosterHierarchyId() );

    AssociationRequestCollection childNodeAssociationRequestCollection = new AssociationRequestCollection();
    childNodeAssociationRequestCollection.add( new NodeToNodeTypeAssociationRequest() );
    List<Node> childNodeList = nodeService.getChildNodesWithAssociationsByParent( parent.getId(), childNodeAssociationRequestCollection );

    if ( !childNodeList.isEmpty() )
    {
      for ( Node child : childNodeList )
      {
        view.getChildren().add( buildNodeTree( child ) );
      }
    }

    return view;
  }

  protected HierarchyNodeView buildHierarchyNodeView( Node node )
  {
    HierarchyNodeView view = new HierarchyNodeView();
    view.setId( node.getRosterNodeId() );
    view.setName( node.getName() );
    view.setParentId( Objects.nonNull( node.getParentNode() ) ? nodeService.getRosterNodeIdByNodeId( node.getParentNode().getId() ) : null );
    view.setHierarchyId( node.getHierarchy().getRosterHierarchyId() );
    return view;
  }

  @SuppressWarnings( { "resource", "unchecked" } )
  protected Set<NodeCharacteristic> saveOrUpdateNodeCharacteristics( List<CreateHierarchyNodeAttributeRequest> hierarchyNodeAttributesList, Node node, NodeType rosterNodetype )
  {
    Set<NodeCharacteristic> nodeCharacteristicList = new HashSet<NodeCharacteristic>();

    if ( !hierarchyNodeAttributesList.isEmpty() )
    {
      for ( CreateHierarchyNodeAttributeRequest req : hierarchyNodeAttributesList )
      {
        Characteristic characteristic = null;
        NodeTypeCharacteristicType nodeCharacteristicType = new NodeTypeCharacteristicType();
        nodeCharacteristicType.setCharacteristicName( req.getName() );

        Scanner scanner = new Scanner( req.getValue() );

        if ( scanner.hasNextBoolean() )
        {
          nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.BOOLEAN ) );
        }
        else if ( scanner.hasNextInt() )
        {
          nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.INTEGER ) );
        }
        else if ( scanner.hasNextFloat() || scanner.hasNextDouble() )
        {
          nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.DECIMAL ) );
        }
        else if ( isDate( req.getValue() ) )
        {
          nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.DATE ) );
        }
        else
        {
          nodeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
        }

        try
        {

          List<NodeTypeCharacteristicType> existingNodeTypeCharacteristicTypes = nodeTypeCharacteristicService.getAllNodeTypeCharacteristicTypesByNodeTypeId( rosterNodetype.getId() );
          Optional<NodeTypeCharacteristicType> findExistingObj = existingNodeTypeCharacteristicTypes.stream().filter( val -> val.getCharacteristicName().equalsIgnoreCase( req.getName() ) )
              .findFirst();

          if ( findExistingObj.isPresent() )
          {
            characteristic = findExistingObj.get();
          }
          else
          {
            nodeCharacteristicType.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );
            nodeCharacteristicType.setDomainId( node.getNodeType().getId() );
            nodeCharacteristicType.setActive( true );
            characteristic = nodeTypeCharacteristicService.saveCharacteristic( nodeCharacteristicType );

          }

        }
        catch( ServiceErrorException e )
        {
          throw new BadRequestException( e.getMessage() );
        }

        NodeCharacteristic nodeCharacteristic = new NodeCharacteristic();
        nodeCharacteristic.setNodeTypeCharacteristicType( (NodeTypeCharacteristicType)nodeTypeCharacteristicService.getCharacteristicById( characteristic.getId() ) );
        nodeCharacteristic.setCharacteristicValue( req.getValue() );

        nodeCharacteristicList.add( nodeCharacteristic );
      }
    }

    return nodeCharacteristicList;
  }

  protected boolean isDate( String inputValue )
  {
    if ( Objects.nonNull( inputValue ) )
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
      dateFormat.setLenient( false );

      try
      {
        dateFormat.parse( inputValue );
      }
      catch( Exception e )
      {
        log.info( "Not Correct Date Format : " + e.getMessage() );
        return false;
      }

    }
    return true;
  }

  protected NodeType createOrGetRosterNodeType( List<NodeType> nodeTypes )
  {
    NodeType rosterNodetype = null;

    if ( !isRosterNodeTypeExist( nodeTypes ) )
    {
      rosterNodetype = createRosterNodeType();
    }
    else
    {
      rosterNodetype = getRosterNodeType( nodeTypes );
    }
    return rosterNodetype;
  }

  protected Node getRosterRootNode( Hierarchy hierarchy )
  {
    Node rootNode = null;

    try
    {
      rootNode = nodeService.getRootNode( hierarchy.getId(), null );
    }
    catch( Exception e )
    {
      log.info( "Master root node is not exist for this hierarchy: " + e.getMessage() );
    }
    return rootNode;
  }

  protected Node createHierarchyNodeWithCharacteristics( String nodeName,
                                                         Long nodeParentId,
                                                         Node rootNode,
                                                         NodeType rosterNodetype,
                                                         Hierarchy hierarchy,
                                                         List<CreateHierarchyNodeAttributeRequest> hierarchyNodeAttributes )
  {
    Node savedNode = null;

    Node node = new Node();
    node.setName( nodeName );
    node.setHierarchy( hierarchy );
    node.setNodeType( rosterNodetype );

    node.setNodeCharacteristics( saveOrUpdateNodeCharacteristics( !hierarchyNodeAttributes.isEmpty() ? hierarchyNodeAttributes : null, node, rosterNodetype ) );
    savedNode = nodeService.saveNode( node, Objects.nonNull( nodeParentId ) ? nodeParentId : Objects.nonNull( rootNode ) ? rootNode.getId() : 0L, true );

    return savedNode;
  }

  protected void nodeCharacteristicNameAndValue( Set<NodeCharacteristic> nodeCharacteristicList )
  {
    // Delete characteristic value.
    for ( NodeCharacteristic nodeCharacteristicOne : nodeCharacteristicList )
    {
      nodeService.deleteNodeCharacteristics( nodeCharacteristicOne );
    }

    // Delete characteristic name and type in CM.
    for ( NodeCharacteristic nodeCharacteristicTwo : nodeCharacteristicList )
    {
      try
      {
        NodeTypeCharacteristicType deleteObj = (NodeTypeCharacteristicType)nodeTypeCharacteristicService.getCharacteristicById( nodeCharacteristicTwo.getId() );
        deleteObj.setActive( false );
        deleteObj.setCharacteristicName( deleteObj.getCharacteristicName() + "_deleted_" + System.currentTimeMillis() );
        nodeTypeCharacteristicService.saveCharacteristic( deleteObj );
      }
      catch( ServiceErrorException e )
      {
        throw new BadRequestException( e.getMessage() );
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  protected Node getNodeWithCharacteristicsAssociationsById( Long nodeId )
  {
    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );

    return nodeService.getNodeWithAssociationsById( nodeId, nodeAssociationRequestCollection );

  }

  protected PersonHierarchyNodeView buildPersonHierarchyNodeView( UserNode personNode )
  {
    PersonHierarchyNodeView view = new PersonHierarchyNodeView();

    view.setId( personNode.getNode().getRosterNodeId() );
    view.setName( personNode.getNode().getName() );
    view.setHierarchyId( personNode.getNode().getHierarchy().getRosterHierarchyId() );
    view.setRoleType( personNode.getHierarchyRoleType().getName().toLowerCase() );

    if ( Objects.nonNull( personNode.getNode().getParentNode() ) )
    {
      view.setParentId( personNode.getNode().getParentNode().getRosterNodeId() );
    }

    return view;

  }

  public RoleTypeEnum buildRoleTypeEnum( String value )
  {
    RoleTypeEnum type = RoleTypeEnum.getByName( value );
    if ( null == type )
    {
      throw new ResourceNotFoundException( "'" + value + "' is an invalid value.  Supported values are " + RoleTypeEnum.values().toString() );
    }
    return type;
  }

  public String buildEnumStringValues( NamedEnum[] names )
  {
    List<String> nameValues = new ArrayList<String>();
    Arrays.asList( names ).forEach( n -> nameValues.add( n.getName() ) );
    return String.join( ",", nameValues );
  }

  protected PersonSearch getEmptyPersonSearchView()
  {
    PersonSearch response = new PersonSearch();
    response.setRecordCount( 0 );

    return response;
  }

  protected Set<PersonPushNotificationSubscriptionView> buildPersonFullPushNotificationSubscriptionView( UserDevice subscription,
                                                                                                         PersonPushNotificationSubscriptionView view,
                                                                                                         Set<PersonPushNotificationSubscriptionView> response )
  {
    view = new PersonPushNotificationSubscriptionView();

    view.setToken( subscription.getRegistrationId() );
    view.setId( subscription.getRosterDeviceId() );
    view.setLabel( subscription.getDeviceType().toString() );

    response.add( view );

    return response;
  }

  protected <D, V> void assignProperties( List<String> requestedProperties,
                                          String propertyName,
                                          Supplier<Set<D>> domainList,
                                          Consumer<Set<V>> setMethod,
                                          Supplier<Set<V>> getMethod,
                                          Function<D, V> buildMethod )
  {
    for ( String requestedProperty : requestedProperties )
    {
      if ( propertyName.equalsIgnoreCase( requestedProperty ) )
      {
        setMethod.accept( new HashSet<>() );
        domainList.get().stream().forEach( domainObject -> getMethod.get().add( buildMethod.apply( domainObject ) ) );
        break;
      }
    }
  }

  protected PersonFullView buildPersonPropsView( Long userId, List<String> personProperties, PersonFullView fullView )
  {
    if ( personProperties.contains( "push_notification_subscriptions" ) )
    {
      Set<PersonPushNotificationSubscriptionView> response = new HashSet<PersonPushNotificationSubscriptionView>();
      PersonPushNotificationSubscriptionView view = null;
      List<UserDevice> existingDevices = userDeviceService.findUserDevicedForUser( userId );

      for ( UserDevice existingDevice : existingDevices )
      {
        fullView.setSubscriptions( buildPersonFullPushNotificationSubscriptionView( existingDevice, view, response ) );
      }
    }

    if ( personProperties.contains( "groups" ) )
    {
      fullView.setGroups( buildUserGroupView( userId ) );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ALL ) );

    User user = userService.getUserByIdWithAssociations( userId, associationRequestCollection );

    if ( Objects.isNull( fullView.getPronouns() ) )
    {
      fullView.setPronouns( buildPersonPronouns( user ) );
    }

    List<String> propValues = Arrays.asList( "personEmails", "personPhones", "personAddresses", "personAttributes", "nodes" );

    if ( personProperties.contains( "emails" ) && !CollectionUtils.isEmpty( user.getUserEmailAddresses() ) )
    {
      assignProperties( propValues, "personEmails", user::getUserEmailAddresses, fullView::setPersonEmails, fullView::getPersonEmails, this::buildPersonEmailView );
    }

    if ( personProperties.contains( "phones" ) && !CollectionUtils.isEmpty( user.getUserPhones() ) )
    {
      assignProperties( propValues, "personPhones", user::getUserPhones, fullView::setPersonPhones, fullView::getPersonPhones, this::buildPersonPhoneView );
    }

    if ( personProperties.contains( "addresses" ) && !CollectionUtils.isEmpty( user.getUserAddresses() ) )
    {
      assignProperties( propValues, "personAddresses", user::getUserAddresses, fullView::setPersonAddresses, fullView::getPersonAddresses, this::buildPersonAddressView );
    }

    if ( personProperties.contains( "attributes" ) && !CollectionUtils.isEmpty( user.getUserCharacteristics() ) )
    {
      assignProperties( propValues, "personAttributes", user::getUserCharacteristics, fullView::setPersonAttributes, fullView::getPersonAttributes, this::buildPersonAttributeView );
    }

    if ( personProperties.contains( "nodes" ) && !CollectionUtils.isEmpty( user.getUserNodes() ) )
    {
      assignProperties( propValues, "nodes", user::getUserNodes, fullView::setNodes, fullView::getNodes, this::buildPersonHierarchyNodeView );
    }

    return fullView;
  }

  protected Set<GroupView> buildUserGroupView( Long userId )
  {
    Set<GroupView> groupView = new HashSet<GroupView>();

    List<AudienceDetail> audDetailList = audienceService.getAudienceDetailsByUserId( userId );

    if ( !audDetailList.isEmpty() )
    {
      audDetailList.stream().forEach( obj ->
      {
        GroupView view = new GroupView();

        view.setId( obj.getRosterAudienceId() );
        view.setCompanyId( obj.getCompanyId() );
        view.setGroupPublic( obj.isPublic() );
        view.setType( getType( obj.getType() ) );
        view.setName( obj.getAudienceName() );

        groupView.add( view );

      } );
    }

    return groupView;

  }

  protected String getType( String labelType )
  {
    return GenericTypeEnum.findTypeByCode( labelType.toLowerCase() ).getValue();
  }

}

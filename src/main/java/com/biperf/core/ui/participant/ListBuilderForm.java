/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ListBuilderForm.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.AudienceCriteriaCharacteristic;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CharacteristicValueBean;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.PickListValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ListBuilder ActionForm transfer object.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>June 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ListBuilderForm extends BaseForm
{
  public static final String PARTICIPANT_SEARCH_TYPE = "pax";
  public static final String CRITERIA_SEARCH_TYPE = "criteria";
  public static final String CRITERIA_INCLUDE_SEARCH = "include";
  public static final String CRITERIA_EXCLUDE_SEARCH = "exclude";

  private String firstName;
  private String lastName;
  private String loginId;
  private String jobPosition;
  private String department;
  private String language;
  private String countryId;
  private Long nodeId;
  private String nameOfNode;
  private String lookedUpNodeName;
  private String method;
  private String[] resultsBox;
  private String[] selectedBox;

  private Long employerId;

  /** default to participant search. Options: criteria,participant* */
  private String searchType = PARTICIPANT_SEARCH_TYPE;

  /** Options: listBuilder,addAudience,editAudience* */
  private String pageType;
  private boolean isAdmin;
  private boolean needSingleResult;

  private List userCharacteristicValueList = new ArrayList();
  private List nodeTypeCharacteristicValueList = new ArrayList();
  private Long nodeTypeId;
  private String hierarchyRoleType;
  private String nodeIncludeType;
  private boolean saveAudienceDefinition;

  private Long audienceCriteriaValueViewIndex = null;
  private List audienceCriteriaValueList = null;
  private String audienceCriteriaValueListCount = null;

  private String saveAudienceReturnUrl = null;

  private String audienceMembersLookupReturnUrl;
  private boolean filterAudienceIncluded;

  // Audience values
  private String audienceName;
  private Long audienceVersion;
  private Long audienceId;
  private String rosterAudienceId;

  // Audiece exclusion parameters
  private Boolean excludeCountry = Boolean.FALSE;
  private Boolean excludeNodeName = Boolean.FALSE;
  private Boolean excludeNodeRole = Boolean.FALSE;
  private Boolean excludeNodeCharacteristic = Boolean.FALSE;
  private Boolean excludeJobPosition = Boolean.FALSE;
  private Boolean excludeDepartment = Boolean.FALSE;
  private Boolean excludePaxCharacteristic = Boolean.FALSE;

  private String excludeCountryId;
  private String excludeNameOfNode;
  private String excludeNodeIncludeType;
  private String excludeHierarchyRoleType;
  private Long excludeNodeTypeId;
  private String jobPositionForExclude;
  private String departmentForExclude;
  private String excludeLookedUpNodeName;
  private Long excludeNodeId;

  private List excludeUserCharacteristicValueList = new ArrayList();
  private List excludeNodeTypeCharacteristicValueList = new ArrayList();

  private String listBuilderReturnActionUrl;

  private Boolean plateauAwardsOnly = Boolean.FALSE;
  private Boolean publicAudience = Boolean.FALSE;

  public String getEncryptedNodeId()
  {
    Map clientStateMap = new HashMap();
    clientStateMap.put( "encryptedNodeId", nodeId );
    String password = ClientStatePasswordManager.getPassword();
    return ClientStateSerializer.serialize( clientStateMap, password );
  }

  public void setEncryptedNodeId( String encryptedString )
  {
    if ( encryptedString != null )
    {
      try
      {
        String password = ClientStatePasswordManager.getPassword();
        Map clientStateMap = ClientStateSerializer.deserialize( encryptedString, password );
        nodeId = (Long)clientStateMap.get( "encryptedNodeId" );
      }
      catch( InvalidClientStateException e )
      {
        // Do Nothing
      }
    }
  }

  public String getEncryptedExcludeNodeId()
  {
    Map clientStateMap = new HashMap();
    clientStateMap.put( "encryptedExcludeNodeId", excludeNodeId );
    String password = ClientStatePasswordManager.getPassword();
    return ClientStateSerializer.serialize( clientStateMap, password );
  }

  public void setEncryptedExcludeNodeId( String encryptedString )
  {
    if ( encryptedString != null )
    {
      try
      {
        String password = ClientStatePasswordManager.getPassword();
        Map clientStateMap = ClientStateSerializer.deserialize( encryptedString, password );
        excludeNodeId = (Long)clientStateMap.get( "encryptedExcludeNodeId" );
      }
      catch( InvalidClientStateException e )
      {
        // Do Nothing
      }
    }
  }

  /**
   * @return value of nodeTypeId property
   */
  public AudienceCriteria toSearchDomainObject()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setFirstName( filterAllSelectionOrBlank( firstName ) );
    audienceCriteria.setLastName( filterAllSelectionOrBlank( lastName ) );
    audienceCriteria.setLoginId( filterAllSelectionOrBlank( loginId ) );
    audienceCriteria.setEmployerId( filterAllSelection( employerId ) );

    String jobpos = filterAllSelectionOrBlank( jobPosition );
    PickListValueBean pickListPositionValueBean = getUserService().getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items", "en_US", jobpos );

    if ( Objects.nonNull( jobpos ) && !Objects.nonNull( pickListPositionValueBean ) )
    {
      audienceCriteria.setPositionType( PositionType.getInvalidItem( PositionType.class ).getCode() );
    }
    else
    {
      audienceCriteria.setPositionType( jobpos );
    }

    String dept = filterAllSelectionOrBlank( department );
    PickListValueBean pickListDeptValueBean = getUserService().getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items", "en_US", dept );
    if ( Objects.nonNull( dept ) && !Objects.nonNull( pickListDeptValueBean ) )
    {
      audienceCriteria.setDepartmentType( DepartmentType.getInvalidItem( DepartmentType.class ).getCode() );
    }
    else
    {
      audienceCriteria.setDepartmentType( dept );
    }

    String lang = filterAllSelectionOrBlank( language );
    if ( Objects.nonNull( lang ) && !Objects.nonNull( LanguageType.lookup( lang ) ) )
    {
      audienceCriteria.setLanguageType( (LanguageType)LanguageType.getInvalidItem( LanguageType.class ) );
    }
    else
    {
      audienceCriteria.setLanguageType( LanguageType.lookup( lang ) );
    }

    if ( countryId != null )
    {
      audienceCriteria.setCountryId( filterAllSelection( new Long( countryId ) ) );
    }
    audienceCriteria.setNodeName( filterAllSelectionOrBlank( nameOfNode ) );
    audienceCriteria.setNodeTypeId( filterAllSelection( nodeTypeId ) );
    audienceCriteria.setNodeId( filterAllSelection( nodeId ) );
    String roleType = filterAllSelectionOrBlank( hierarchyRoleType );
    if ( Objects.nonNull( roleType ) && !Objects.nonNull( HierarchyRoleType.lookup( roleType ) ) )
    {
      audienceCriteria.setNodeRole( (HierarchyRoleType)HierarchyRoleType.getInvalidItem( HierarchyRoleType.class ) );
    }
    else
    {
      audienceCriteria.setNodeRole( HierarchyRoleType.lookup( roleType ) );
    }
    audienceCriteria.setChildNodesIncluded( nodeIncludeType != null && nodeIncludeType.equals( "nac" ) );

    // Audience Exclusion criteria
    audienceCriteria.setExcludePositionType( filterAllSelectionOrBlank( jobPositionForExclude ) );
    audienceCriteria.setExcludeDepartmentType( filterAllSelectionOrBlank( departmentForExclude ) );
    if ( excludeCountryId != null )
    {
      audienceCriteria.setExcludeCountryId( filterAllSelection( new Long( excludeCountryId ) ) );
    }
    audienceCriteria.setExcludeNodeName( filterAllSelectionOrBlank( excludeNameOfNode ) );
    audienceCriteria.setExcludeNodeTypeId( filterAllSelection( excludeNodeTypeId ) );
    audienceCriteria.setExcludeNodeId( filterAllSelection( excludeNodeId ) );
    audienceCriteria.setExcludeNodeRole( HierarchyRoleType.lookup( filterAllSelectionOrBlank( excludeHierarchyRoleType ) ) );
    audienceCriteria.setExcludeChildNodesIncluded( excludeNodeIncludeType != null && excludeNodeIncludeType.equals( "nac" ) );

    populateCharacteristicCriterias( audienceCriteria );

    return audienceCriteria;
  }

  /**
   * @param request
   * @return value of nodeTypeId property
   */
  public Audience toResultsDomainObject( HttpServletRequest request )
  {
    Audience audience;
    if ( this.searchType.equals( CRITERIA_SEARCH_TYPE ) )
    {
      audience = new CriteriaAudience();
      if ( rosterAudienceId != null && !StringUtils.isEmpty( rosterAudienceId ) )
      {
        audience.setRosterAudienceId( UUID.fromString( rosterAudienceId ) );
      }
      CriteriaAudience criteriaAudience = (CriteriaAudience)audience;

      CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)request.getSession().getAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

      if ( criteriaAudienceValue != null )
      {

        // Get AudienceCriteria objects from view object.
        for ( Iterator iter = criteriaAudienceValue.getAudienceCriteriaValueList().iterator(); iter.hasNext(); )
        {
          AudienceCriteriaValue audienceCriteriaValue = (AudienceCriteriaValue)iter.next();
          criteriaAudience.addAudienceCriteria( audienceCriteriaValue.getAudienceCriteria() );
        }
      }
    }
    else
    {
      audience = new PaxAudience();
      // PaxAudience paxAudience = (PaxAudience)audience;

      // Plateau awards only
      audience.setPlateauAwardsOnly( plateauAwardsOnly );
      if ( rosterAudienceId != null && !StringUtils.isEmpty( rosterAudienceId ) )
      {
        audience.setRosterAudienceId( UUID.fromString( rosterAudienceId ) );
      }

    }

    audience.setName( audienceName );
    audience.setPublicAudience( publicAudience );

    if ( audienceId != null && !audienceId.equals( new Long( 0 ) ) )
    {
      audience.setId( audienceId );
      audience.setVersion( audienceVersion );
    }

    return audience;
  }

  private void populateCharacteristicCriterias( AudienceCriteria audienceCriteria )
  {
    boolean isCriteriaSearch = false;
    String criteriaSearchType = null;
    if ( this.searchType.equals( CRITERIA_SEARCH_TYPE ) )
    {
      isCriteriaSearch = true;
    }

    LinkedHashSet characteristicCriterias = new LinkedHashSet();
    populateTypeSpecificCharacteristicCriterias( userCharacteristicValueList, audienceCriteria, characteristicCriterias, CRITERIA_INCLUDE_SEARCH, UserCharacteristicType.class );
    populateTypeSpecificCharacteristicCriterias( nodeTypeCharacteristicValueList, audienceCriteria, characteristicCriterias, CRITERIA_INCLUDE_SEARCH, NodeTypeCharacteristicType.class );
    // Audience exclusion is only for criteria search type.
    if ( this.searchType.equals( CRITERIA_SEARCH_TYPE ) )
    {

      populateTypeSpecificCharacteristicCriterias( excludeUserCharacteristicValueList, audienceCriteria, characteristicCriterias, CRITERIA_EXCLUDE_SEARCH, UserCharacteristicType.class );
      populateTypeSpecificCharacteristicCriterias( excludeNodeTypeCharacteristicValueList, audienceCriteria, characteristicCriterias, CRITERIA_EXCLUDE_SEARCH, NodeTypeCharacteristicType.class );
    }

    audienceCriteria.setCharacteristicCriterias( characteristicCriterias );
  }

  private void populateTypeSpecificCharacteristicCriterias( List typeSpecificList,
                                                            AudienceCriteria audienceCriteria,
                                                            LinkedHashSet characteristicCriterias,
                                                            String criteriaSearchType,
                                                            Class characteristicTypeClass )
  {
    if ( typeSpecificList != null )
    {
      for ( Iterator iter = typeSpecificList.iterator(); iter.hasNext(); )
      {
        CharacteristicValueBean characteristicFormBean = (CharacteristicValueBean)iter.next();
        if ( characteristicFormBean != null )
        {
          // NOTE: With multi-select types, for query we only allow a single selection. JSP stores
          // this value in characteristicValue
          // just like single-select.
          String characteristicValue = filterAllSelectionOrBlank( characteristicFormBean.getCharacteristicValue() );
          if ( !StringUtils.isBlank( characteristicValue ) )
          {
            AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = new AudienceCriteriaCharacteristic();
            Characteristic characteristicType;
            if ( characteristicTypeClass.equals( UserCharacteristicType.class ) )
            {
              characteristicType = new UserCharacteristicType();
            }
            else if ( characteristicTypeClass.equals( NodeTypeCharacteristicType.class ) )
            {
              characteristicType = new NodeTypeCharacteristicType();
              // might need to set domain id when we persist
            }
            else
            {
              throw new BeaconRuntimeException( "characteristicTypeClass must be UserCharacteristicType or NodeTypeCharacteristicType" );
            }

            characteristicType.setId( characteristicFormBean.getCharacteristicId() );
            audienceCriteriaCharacteristic.setCharacteristic( characteristicType );
            audienceCriteriaCharacteristic.setCharacteristicValue( characteristicValue );
            audienceCriteriaCharacteristic.setAudienceCriteria( audienceCriteria );
            audienceCriteriaCharacteristic.setCharactersticDataType( characteristicFormBean.getCharacteristicDataType() );
            // if search type is criteria then save include or exclude type
            if ( this.searchType.equals( CRITERIA_SEARCH_TYPE ) && criteriaSearchType != null )
            {
              audienceCriteriaCharacteristic.setSearchType( criteriaSearchType );
            }

            characteristicCriterias.add( audienceCriteriaCharacteristic );
          }
        }
      }
    }
  }

  /**
   * Filter Criteria Query element so that null is set if the all option has been set or if the
   * field is blank.
   */
  private String filterAllSelectionOrBlank( String value )
  {
    String filteredValue;

    if ( StringUtils.isBlank( value ) || value != null && value.equals( ViewAttributeNames.SEARCH_ALL_OPTION ) )
    {
      // "All" value selected or is blank
      filteredValue = null;
    }
    else
    {
      // Don't filter, just return original
      filteredValue = value;
    }
    return filteredValue;
  }

  /**
   * Filter Criteria Query element so that null is set if the all option has been set.
   */
  private Long filterAllSelection( Long value )
  {
    Long filteredValue;

    if ( value != null && value.equals( ViewAttributeNames.SEARCH_ALL_OPTION_LONG ) )
    {
      // "All" value selected
      filteredValue = null;
    }
    else
    {
      // Temp hack so I can show Satish working page - Longs should be null when no value, not 0,
      // why getting set to 0???
      if ( value != null && !value.equals( new Long( 0 ) ) )
      {
        // Don't filter, just return original
        filteredValue = value;
      }
      else
      {
        filteredValue = null;
      }
    }

    return filteredValue;
  }

  /**
   * @return value of nodeTypeId property
   */
  public Long getNodeTypeId()
  {
    return nodeTypeId;
  }

  /**
   * @param nodeTypeId value for nodeTypeId property
   */
  public void setNodeTypeId( Long nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getLanguage()
  {
    return language;
  }

  public void setLanguage( String language )
  {
    this.language = language;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getLoginId()
  {
    return loginId;
  }

  public void setLoginId( String loginId )
  {
    this.loginId = loginId;
  }

  public String getNameOfNode()
  {
    return nameOfNode;
  }

  public void setNameOfNode( String nameOfNode )
  {
    this.nameOfNode = nameOfNode;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String[] getResultsBox()
  {
    return resultsBox;
  }

  public void setResultsBox( String[] resultsBox )
  {
    this.resultsBox = resultsBox;
  }

  public String[] getSelectedBox()
  {
    return selectedBox;
  }

  public void setSelectedBox( String[] selectedBox )
  {
    this.selectedBox = selectedBox;
  }

  /**
   * Builds a list of FormattedValueBean objects from a list of pax's
   * 
   * @param paxList
   */
  public void setSelectedBoxWithList( List paxList )
  {
    String[] selectedBox = new String[paxList.size()];
    int i = 0;

    // Final decision on whether this will eventually be a list of participants or
    // participantEmployers
    // to be determined

    // for (Iterator iter = paxList.iterator(); iter.hasNext();) {
    // ParticipantEmployer participantEmployer = (ParticipantEmployer) iter.next();
    // FormattedValueBean formattedValueBean = makeParticipantDisplay(participantEmployer);
    // selectedBox[i] = formattedValueBean.getFormattedId();
    // i++;
    // }

    for ( Iterator iter = paxList.iterator(); iter.hasNext(); )
    {
      Participant participant = (Participant)iter.next();
      FormattedValueBean formattedValueBean = makeParticipantDisplay( participant );
      selectedBox[i] = formattedValueBean.getFormattedId();
      i++;
    }

    this.setSelectedBox( selectedBox );
  }

  /**
   * @return value of userCharacteristicValueList property
   */
  public List getUserCharacteristicValueList()
  {
    return userCharacteristicValueList;
  }

  public void setUserCharacteristicValueList( List valueList )
  {
    this.userCharacteristicValueList = valueList;
  }

  public int getUserCharacteristicValueListCount()
  {
    if ( userCharacteristicValueList != null )
    {
      return userCharacteristicValueList.size();
    }
    return 0;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of CharacteristicFormBean from the value list
   */
  public CharacteristicValueBean getUserCharacteristicValueInfo( int index )
  {
    try
    {
      return (CharacteristicValueBean)userCharacteristicValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public CharacteristicValueBean getExcludeUserCharacteristicValueInfo( int index )
  {
    try
    {
      return (CharacteristicValueBean)excludeUserCharacteristicValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public int getNodeTypeCharacteristicValueListCount()
  {
    if ( nodeTypeCharacteristicValueList != null )
    {
      return nodeTypeCharacteristicValueList.size();
    }
    return 0;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of AudienceCriteriaValue from the value list
   */
  public AudienceCriteriaValue getAudienceCriteriaValue( int index )
  {
    try
    {
      return (AudienceCriteriaValue)this.audienceCriteriaValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of CharacteristicFormBean from the value list
   */
  public CharacteristicValueBean getNodeTypeCharacteristicValueInfo( int index )
  {
    try
    {
      return (CharacteristicValueBean)nodeTypeCharacteristicValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public CharacteristicValueBean getExcludeNodeTypeCharacteristicValueInfo( int index )
  {
    try
    {
      return (CharacteristicValueBean)excludeNodeTypeCharacteristicValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
    // reset needs to be used to populate an empty list of
    // UserCharacteristicFormBeans. If this is not done, the form wont initialize
    // properly.
    userCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "userCharacteristicValueListCount" ) );
    nodeTypeCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "nodeTypeCharacteristicValueListCount" ) );
    excludeNodeTypeCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "excludeNodeTypeCharacteristicValueListCount" ) );
    excludeUserCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "excludeUserCharacteristicValueListCount" ) );

    this.employerId = null;
    this.jobPosition = null;
    this.department = null;
    this.language = null;
    this.countryId = null;
    this.nodeId = null;
    this.nameOfNode = null;
    this.lookedUpNodeName = null;
    this.nodeTypeId = null;
    this.hierarchyRoleType = null;
    this.nodeIncludeType = null;
    this.firstName = null;
    this.lastName = null;
    this.loginId = null;
    this.excludeCountry = Boolean.FALSE;
    this.excludeNodeName = Boolean.FALSE;
    this.excludeNodeRole = Boolean.FALSE;
    this.excludeNodeCharacteristic = Boolean.FALSE;
    this.excludeJobPosition = Boolean.FALSE;
    this.excludeDepartment = Boolean.FALSE;
    this.excludePaxCharacteristic = Boolean.FALSE;

    this.excludeCountryId = null;
    this.excludeNameOfNode = null;
    this.excludeNodeIncludeType = null;
    this.excludeHierarchyRoleType = null;
    this.excludeNodeTypeId = null;
    this.jobPositionForExclude = null;
    this.departmentForExclude = null;
    this.excludeLookedUpNodeName = null;
    this.excludeNodeId = null;

    int count = RequestUtils.getOptionalParamInt( request, "audienceCriteriaValueListCount" );

    if ( count > 0 )
    {

      CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)request.getSession().getAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

      if ( criteriaAudienceValue != null )
      {

        for ( int i = 0; i <= count; i++ )
        {
          if ( request.getParameter( "remove_checkbox_" + i ) != null )
          {

            AudienceCriteriaValue audienceCriteriaValue = (AudienceCriteriaValue)criteriaAudienceValue.getAudienceCriteriaValueList().get( i );
            audienceCriteriaValue.setRemove( true );
          }
        }
      }
    }
  } // end reset

  /**
   * @return value of nodeTypeCharacteristicValueList property
   */
  public List getNodeTypeCharacteristicValueList()
  {
    return nodeTypeCharacteristicValueList;
  }

  /**
   * @param nodeTypeCharacteristicValueList value for nodeTypeCharacteristicValueList property
   */
  public void setNodeTypeCharacteristicValueList( List nodeTypeCharacteristicValueList )
  {
    this.nodeTypeCharacteristicValueList = nodeTypeCharacteristicValueList;
  }

  /**
   * @return value of hierarchyRoleType property
   */
  public String getHierarchyRoleType()
  {
    return hierarchyRoleType;
  }

  /**
   * @param hierarchyRoleType value for hierarchyRoleType property
   */
  public void setHierarchyRoleType( String hierarchyRoleType )
  {
    this.hierarchyRoleType = hierarchyRoleType;
  }

  /**
   * @return value of searchType property
   */
  public String getSearchType()
  {
    return searchType;
  }

  /**
   * @param searchType value for searchType property
   */
  public void setSearchType( String searchType )
  {
    this.searchType = searchType;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  /**
   * @return value of nodeIncludeType property
   */
  public String getNodeIncludeType()
  {
    return nodeIncludeType;
  }

  /**
   * @param nodeIncludeType value for nodeIncludeType property
   */
  public void setNodeIncludeType( String nodeIncludeType )
  {
    this.nodeIncludeType = nodeIncludeType;
  }

  public String getLookedUpNodeName()
  {
    return lookedUpNodeName;
  }

  public void setLookedUpNodeName( String lookedUpNodeName )
  {
    this.lookedUpNodeName = lookedUpNodeName;
  }

  /**
   * accessor for which type of list builder version is needed - pax or admin version
   * 
   * @return value of isAdmin property
   */
  public boolean isAdmin()
  {
    return isAdmin;
  }

  /**
   * @param isAdmin value for isAdmin property
   */
  public void setAdmin( boolean isAdmin )
  {
    this.isAdmin = isAdmin;
  }

  /**
   * @return value of pageType property
   */
  public String getPageType()
  {
    return pageType;
  }

  /**
   * @param pageType value for pageType property
   */
  public void setPageType( String pageType )
  {
    this.pageType = pageType;
  }

  /**
   * @return value of employerId property
   */
  public Long getEmployerId()
  {
    return employerId;
  }

  /**
   * @param employerId value for employerId property
   */
  public void setEmployerId( Long employerId )
  {
    this.employerId = employerId;
  }

  /*
   * @return value of audienceName property
   */
  public String getAudienceName()
  {
    return audienceName;
  }

  /**
   * @return value of audienceCriteriaValueViewIndex property
   */
  public Long getAudienceCriteriaValueViewIndex()
  {
    return audienceCriteriaValueViewIndex;
  }

  /**
   * @param audienceName value for audienceName property
   */
  public void setAudienceName( String audienceName )
  {
    this.audienceName = audienceName;
  }

  /**
   * @param viewIndex value for audienceCriteriaValueViewIndex property
   */
  public void setAudienceCriteriaValueViewIndex( Long viewIndex )
  {
    this.audienceCriteriaValueViewIndex = viewIndex;
  }

  /**
   * @return value of saveAudienceDefinition property
   */
  public boolean isSaveAudienceDefinition()
  {
    return saveAudienceDefinition;
  }

  /**
   * @return value of audienceCriteriaViewListIndex property
   */
  public List getAudienceCriteriaValueList()
  {
    return audienceCriteriaValueList;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( saveAudienceDefinition && StringUtils.isBlank( audienceName ) )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE,
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "participant.list.builder.details", "AUDIENCE_NAME_LABEL" ) ) );
    }

    if ( searchType.equals( PARTICIPANT_SEARCH_TYPE ) )
    {
      if ( this.selectedBox == null || this.selectedBox.length == 0 )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "participant.list.builder.details", "RESULTS_MISSING_LABEL" ) ) );
      }
      if ( needSingleResult && this.selectedBox != null && this.selectedBox.length > 1 )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.list.builder.details.SINGLE_RESULT" ) );
      }
    }

    /*
     * if ( searchType.equals( CRITERIA_SEARCH_TYPE ) ) { if ( this.audienceCriteriaValueListCount
     * == null || this.audienceCriteriaValueListCount.equals( "0" ) ) { errors.add(
     * ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
     * ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager .getText(
     * "participant.list.builder.details", "RESULTS_MISSING_LABEL" ) ) ); } }
     */

    return errors;
  }

  /**
   * @param list value for audienceCriteriaRemoveIndexList property
   */
  public void setAudienceCriteriaValueList( List list )
  {
    this.audienceCriteriaValueList = list;
  }

  /**
   * @return value of audienceCriteriaViewListIndex property
   */
  public String getAudienceCriteriaValueListCount()
  {
    return this.audienceCriteriaValueListCount;
  }

  /**
   * @param saveAudienceDefinition value for saveAudienceDefinition property
   */
  public void setSaveAudienceDefinition( boolean saveAudienceDefinition )
  {
    this.saveAudienceDefinition = saveAudienceDefinition;
  }

  /**
   * @param count value for audienceCriteriaValueListCount property
   */
  public void setAudienceCriteriaValueListCount( String count )
  {
    this.audienceCriteriaValueListCount = count;
  }

  /**
   * @return value of saveAudienceReturnUrl property
   */
  public String getSaveAudienceReturnUrl()
  {
    if ( this.saveAudienceReturnUrl == null || this.saveAudienceReturnUrl.equals( "" ) )
    {
      this.saveAudienceReturnUrl = "/homePage.do";
    }

    return saveAudienceReturnUrl;
  }

  /**
   * @param saveAudienceReturnUrl value for saveAudienceReturnUrl property
   */
  public void setSaveAudienceReturnUrl( String saveAudienceReturnUrl )
  {
    this.saveAudienceReturnUrl = saveAudienceReturnUrl;

    if ( this.saveAudienceReturnUrl == null || this.saveAudienceReturnUrl.equals( "" ) )
    {
      this.saveAudienceReturnUrl = "/homePage.do";
    }
  }

  /**
   * @return value of audienceId property
   */
  public Long getAudienceId()
  {
    return audienceId;
  }

  /**
   * @param audienceId value for audienceId property
   */
  public void setAudienceId( Long audienceId )
  {
    this.audienceId = audienceId;
  }

  /**
   * @return value of audienceVersion property
   */
  public Long getAudienceVersion()
  {
    return audienceVersion;
  }

  /**
   * @param audienceVersion value for audienceVersion property
   */
  public void setAudienceVersion( Long audienceVersion )
  {
    this.audienceVersion = audienceVersion;
  }

  /**
   * @param participantEmployer
   * @return FormattedValueBean
   */
  public FormattedValueBean makeParticipantDisplay( ParticipantEmployer participantEmployer )
  {
    // Map valueMapByColumn = (Map)super.mapRow( rs, rowNum );

    Participant participant = participantEmployer.getParticipant();

    FormattedValueBean formattedValueBean = new FormattedValueBean();
    // formattedValueBean.setId(new
    // Long(((BigDecimal)valueMapByColumn.get("USER_ID")).longValue()));
    // formattedValueBean.setId(new Long((BigDecimal)pax.getId()).longValue());
    formattedValueBean.setId( participant.getId() );
    String middleName = null;

    PickListValueBean pickListDeptValueBean = getUserService().getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                           participant.getLanguageType() == null
                                                                                               ? UserManager.getDefaultLocale().toString()
                                                                                               : participant.getLanguageType().getCode(),
                                                                                           participantEmployer.getDepartmentType() );
    PickListValueBean pickListPositionValueBean = getUserService().getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                               participant.getLanguageType() == null
                                                                                                   ? UserManager.getDefaultLocale().toString()
                                                                                                   : participant.getLanguageType().getCode(),
                                                                                               participantEmployer.getPositionType() );

    StringBuffer sb = new StringBuffer();

    // sb.append( valueMapByColumn.get("LAST_NAME") );
    sb.append( participant.getLastName() );
    sb.append( ", " );
    // sb.append( valueMapByColumn.get("FIRST_NAME") );
    sb.append( participant.getFirstName() );
    // middleName = (String) valueMapByColumn.get( "MIDDLE_NAME" );
    middleName = participant.getMiddleName();
    if ( middleName != null )
    {
      sb.append( " " );
      // sb.append( valueMapByColumn.get("MIDDLE_NAME") );
      sb.append( middleName );
    }

    sb.append( " - " );

    // job position
    // DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET,
    // (String) valueMapByColumn.get( "POSITION_TYPE") );
    // DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET,
    // participantEmployer.getPositionType().toString() );
    // sb.append( jobPositionItem.getName() );
    sb.append( pickListPositionValueBean.getName() );

    sb.append( " - " );

    // department
    // DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET,
    // (String) valueMapByColumn.get( "DEPARTMENT_TYPE") );
    // DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET,
    // participantEmployer.getDepartmentType().toString() );
    // sb.append( departmentItem.getName() );
    sb.append( pickListDeptValueBean.getName() );

    formattedValueBean.setValue( sb.toString() );

    return formattedValueBean;
  }

  /**
   * @param participant
   * @return FormattedValueBean
   */
  public FormattedValueBean makeParticipantDisplay( Participant participant )
  {
    // Map valueMapByColumn = (Map)super.mapRow( rs, rowNum );

    // Participant participant = participantEmployer.getParticipant();
    PickListValueBean pickListDeptValueBean = getUserService()
        .getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                     participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode(),
                                     participant.getDepartmentType() );
    PickListValueBean pickListPositionValueBean = getUserService()
        .getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                     participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode(),
                                     participant.getPositionType() );

    FormattedValueBean formattedValueBean = new FormattedValueBean();
    // formattedValueBean.setId(new
    // Long(((BigDecimal)valueMapByColumn.get("USER_ID")).longValue()));
    // formattedValueBean.setId(new Long((BigDecimal)pax.getId()).longValue());
    formattedValueBean.setId( participant.getId() );
    String middleName = null;

    StringBuffer sb = new StringBuffer();

    // sb.append( valueMapByColumn.get("LAST_NAME") );
    sb.append( participant.getLastName() );
    sb.append( ", " );
    // sb.append( valueMapByColumn.get("FIRST_NAME") );
    sb.append( participant.getFirstName() );
    // middleName = (String) valueMapByColumn.get( "MIDDLE_NAME" );
    middleName = participant.getMiddleName();
    if ( middleName != null )
    {
      sb.append( " " );
      // sb.append( valueMapByColumn.get("MIDDLE_NAME") );
      sb.append( middleName );
    }

    // Some users don't have positions
    if ( participant.getPositionType() != null )
    {

      sb.append( " - " );

      // DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET,
      // participant.getPositionType() );
      //
      // if ( jobPositionItem != null )
      // {
      // sb.append( jobPositionItem.getName() );
      // }
      sb.append( pickListPositionValueBean.getName() );

    }

    if ( participant.getDepartmentType() != null )
    {

      sb.append( " - " );

      // department
      // DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET,
      // (String) valueMapByColumn.get( "DEPARTMENT_TYPE") );
      // DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET,
      // participant.getDepartmentType() );
      // if ( departmentItem != null )
      // {
      // sb.append( departmentItem.getName() );
      // }
      sb.append( pickListDeptValueBean.getName() );

    }

    formattedValueBean.setValue( sb.toString() );

    return formattedValueBean;
  }

  /**
   * @return value of audienceMembersLookupReturnUrl property
   */
  public String getAudienceMembersLookupReturnUrl()
  {
    return audienceMembersLookupReturnUrl;
  }

  /**
   * @param audienceMembersLookupReturnUrl value for audienceMembersLookupReturnUrl property
   */
  public void setAudienceMembersLookupReturnUrl( String audienceMembersLookupReturnUrl )
  {
    this.audienceMembersLookupReturnUrl = audienceMembersLookupReturnUrl;
  }

  /**
   * @return value of filterAudienceIncluded property
   */
  public boolean isFilterAudienceIncluded()
  {
    return filterAudienceIncluded;
  }

  /**
   * @param filterAudienceIncluded value for filterAudienceIncluded property
   */
  public void setFilterAudienceIncluded( boolean filterAudienceIncluded )
  {
    this.filterAudienceIncluded = filterAudienceIncluded;
  }

  public boolean isNeedSingleResult()
  {
    return needSingleResult;
  }

  public void setNeedSingleResult( boolean needSingleResult )
  {
    this.needSingleResult = needSingleResult;
  }

  public String getCountryId()
  {
    return countryId;
  }

  public void setCountryId( String countryId )
  {
    this.countryId = countryId;
  }

  public void setExcludeCountryId( String excludeCountryId )
  {
    this.excludeCountryId = excludeCountryId;
  }

  public String getExcludeCountryId()
  {
    return excludeCountryId;
  }

  public void setExcludeNameOfNode( String excludeNameOfNode )
  {
    this.excludeNameOfNode = excludeNameOfNode;
  }

  public String getExcludeNameOfNode()
  {
    return excludeNameOfNode;
  }

  public void setExcludeNodeIncludeType( String excludeNodeIncludeType )
  {
    this.excludeNodeIncludeType = excludeNodeIncludeType;
  }

  public String getExcludeNodeIncludeType()
  {
    return excludeNodeIncludeType;
  }

  public void setExcludeHierarchyRoleType( String excludeHierarchyRoleType )
  {
    this.excludeHierarchyRoleType = excludeHierarchyRoleType;
  }

  public String getExcludeHierarchyRoleType()
  {
    return excludeHierarchyRoleType;
  }

  public void setExcludeNodeTypeId( Long excludeNodeTypeId )
  {
    this.excludeNodeTypeId = excludeNodeTypeId;
  }

  public Long getExcludeNodeTypeId()
  {
    return excludeNodeTypeId;
  }

  public void setJobPositionForExclude( String jobPositionForExclude )
  {
    this.jobPositionForExclude = jobPositionForExclude;
  }

  public String getJobPositionForExclude()
  {
    return jobPositionForExclude;
  }

  public void setDepartmentForExclude( String departmentForExclude )
  {
    this.departmentForExclude = departmentForExclude;
  }

  public String getDepartmentForExclude()
  {
    return departmentForExclude;
  }

  public void setExcludeLookedUpNodeName( String excludeLookedUpNodeName )
  {
    this.excludeLookedUpNodeName = excludeLookedUpNodeName;
  }

  public String getExcludeLookedUpNodeName()
  {
    return excludeLookedUpNodeName;
  }

  public List getExcludeUserCharacteristicValueList()
  {
    return excludeUserCharacteristicValueList;
  }

  public void setExcludeUserCharacteristicValueList( List excludeUserCharacteristicValueList )
  {
    this.excludeUserCharacteristicValueList = excludeUserCharacteristicValueList;
  }

  public int getExcludeUserCharacteristicValueListCount()
  {
    if ( excludeUserCharacteristicValueList != null )
    {
      return excludeUserCharacteristicValueList.size();
    }
    return 0;
  }

  public List getExcludeNodeTypeCharacteristicValueList()
  {
    return excludeNodeTypeCharacteristicValueList;
  }

  public void setExcludeNodeTypeCharacteristicValueList( List excludeNodeTypeCharacteristicValueList )
  {
    this.excludeNodeTypeCharacteristicValueList = excludeNodeTypeCharacteristicValueList;
  }

  public int getExcludeNodeTypeCharacteristicValueListCount()
  {
    if ( excludeNodeTypeCharacteristicValueList != null )
    {
      return excludeNodeTypeCharacteristicValueList.size();
    }
    return 0;
  }

  public void setExcludeCountry( Boolean excludeCountry )
  {
    this.excludeCountry = excludeCountry;
  }

  public Boolean getExcludeCountry()
  {
    return excludeCountry;
  }

  public void setExcludeNodeName( Boolean excludeNodeName )
  {
    this.excludeNodeName = excludeNodeName;
  }

  public Boolean getExcludeNodeName()
  {
    return excludeNodeName;
  }

  public void setExcludeNodeRole( Boolean excludeNodeRole )
  {
    this.excludeNodeRole = excludeNodeRole;
  }

  public Boolean getExcludeNodeRole()
  {
    return excludeNodeRole;
  }

  public void setExcludeNodeCharacteristic( Boolean excludeNodeCharacteristic )
  {
    this.excludeNodeCharacteristic = excludeNodeCharacteristic;
  }

  public Boolean getExcludeNodeCharacteristic()
  {
    return excludeNodeCharacteristic;
  }

  public void setExcludeJobPosition( Boolean excludeJobPosition )
  {
    this.excludeJobPosition = excludeJobPosition;
  }

  public Boolean getExcludeJobPosition()
  {
    return excludeJobPosition;
  }

  public void setExcludeDepartment( Boolean excludeDepartment )
  {
    this.excludeDepartment = excludeDepartment;
  }

  public Boolean getExcludeDepartment()
  {
    return excludeDepartment;
  }

  public void setExcludePaxCharacteristic( Boolean excludePaxCharacteristic )
  {
    this.excludePaxCharacteristic = excludePaxCharacteristic;
  }

  public Boolean getExcludePaxCharacteristic()
  {
    return excludePaxCharacteristic;
  }

  public void setListBuilderReturnActionUrl( String listBuilderReturnActionUrl )
  {
    this.listBuilderReturnActionUrl = listBuilderReturnActionUrl;
  }

  public String getListBuilderReturnActionUrl()
  {
    return listBuilderReturnActionUrl;
  }

  public void setExcludeNodeId( Long excludeNodeId )
  {
    this.excludeNodeId = excludeNodeId;
  }

  public Long getExcludeNodeId()
  {
    return excludeNodeId;
  }

  public void setPlateauAwardsOnly( Boolean plateauAwardsOnly )
  {
    this.plateauAwardsOnly = plateauAwardsOnly;
  }

  public Boolean getPlateauAwardsOnly()
  {
    return plateauAwardsOnly;
  }

  public Boolean getPublicAudience()
  {
    return publicAudience;
  }

  public void setPublicAudience( Boolean publicAudience )
  {
    this.publicAudience = publicAudience;
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  public String getRosterAudienceId()
  {
    return rosterAudienceId;
  }

  public void setRosterAudienceId( String rosterAudienceId )
  {
    this.rosterAudienceId = rosterAudienceId;
  }

  @Override
  public String toString()
  {
    return "ListBuilderForm [firstName=" + firstName + ", lastName=" + lastName + ", loginId=" + loginId + ", jobPosition=" + jobPosition + ", department=" + department + ", language=" + language
        + ", countryId=" + countryId + ", nodeId=" + nodeId + ", nameOfNode=" + nameOfNode + ", lookedUpNodeName=" + lookedUpNodeName + ", method=" + method + ", resultsBox="
        + Arrays.toString( resultsBox ) + ", selectedBox=" + Arrays.toString( selectedBox ) + ", employerId=" + employerId + ", searchType=" + searchType + ", pageType=" + pageType + ", isAdmin="
        + isAdmin + ", needSingleResult=" + needSingleResult + ", userCharacteristicValueList=" + userCharacteristicValueList + ", nodeTypeCharacteristicValueList=" + nodeTypeCharacteristicValueList
        + ", nodeTypeId=" + nodeTypeId + ", hierarchyRoleType=" + hierarchyRoleType + ", nodeIncludeType=" + nodeIncludeType + ", saveAudienceDefinition=" + saveAudienceDefinition
        + ", audienceCriteriaValueViewIndex=" + audienceCriteriaValueViewIndex + ", audienceCriteriaValueList=" + audienceCriteriaValueList + ", audienceCriteriaValueListCount="
        + audienceCriteriaValueListCount + ", saveAudienceReturnUrl=" + saveAudienceReturnUrl + ", audienceMembersLookupReturnUrl=" + audienceMembersLookupReturnUrl + ", filterAudienceIncluded="
        + filterAudienceIncluded + ", audienceName=" + audienceName + ", audienceVersion=" + audienceVersion + ", audienceId=" + audienceId + ", excludeCountry=" + excludeCountry
        + ", excludeNodeName=" + excludeNodeName + ", excludeNodeRole=" + excludeNodeRole + ", excludeNodeCharacteristic=" + excludeNodeCharacteristic + ", excludeJobPosition=" + excludeJobPosition
        + ", excludeDepartment=" + excludeDepartment + ", excludePaxCharacteristic=" + excludePaxCharacteristic + ", excludeCountryId=" + excludeCountryId + ", excludeNameOfNode=" + excludeNameOfNode
        + ", excludeNodeIncludeType=" + excludeNodeIncludeType + ", excludeHierarchyRoleType=" + excludeHierarchyRoleType + ", excludeNodeTypeId=" + excludeNodeTypeId + ", jobPositionForExclude="
        + jobPositionForExclude + ", departmentForExclude=" + departmentForExclude + ", excludeLookedUpNodeName=" + excludeLookedUpNodeName + ", excludeNodeId=" + excludeNodeId
        + ", excludeUserCharacteristicValueList=" + excludeUserCharacteristicValueList + ", excludeNodeTypeCharacteristicValueList=" + excludeNodeTypeCharacteristicValueList
        + ", listBuilderReturnActionUrl=" + listBuilderReturnActionUrl + ", plateauAwardsOnly=" + plateauAwardsOnly + ", publicAudience=" + publicAudience + "]";
  }

}

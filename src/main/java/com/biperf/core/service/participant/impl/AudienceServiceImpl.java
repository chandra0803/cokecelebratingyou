/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/impl/AudienceServiceImpl.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.participant.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.hibernate.JDBCException;

import com.biperf.cache.Cache;
import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.participant.ListBuilderDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PublicRecognitionAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.enums.WebRulesAudienceType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.AudienceCriteriaCharacteristic;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.AudienceRole;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionCompetitorsAudience;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionManagerWebRulesAudience;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToNodeCharacteristicAssociationRequest;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.AudienceToParticipantsAssociationRequest;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.roster.exception.RosterException;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.PaxPromoEligibilityValueBean;
import com.biperf.core.value.participant.AudienceDetail;

/**
 * AudienceServiceImpl.
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
 * <td>sharma</td>
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class AudienceServiceImpl implements AudienceService
{
  private static final String CACHE_CRITERIA_AUDIENCE_KEY = "criteriaAudiences";
  private static final String CACHE_PAX_PROMO_ELIGIBILITY = "paxPromoEligibility";

  @SuppressWarnings( "rawtypes" )
  private Map criteriaAudienceCache = new ConcurrentHashMap();
  @SuppressWarnings( "rawtypes" )
  private Map paxPromoEligibilityCache = new ConcurrentHashMap();

  private AudienceDAO audienceDAO;
  private NodeService nodeService;
  private CMAssetService cmAssetService;
  private PromotionDAO promotionDAO;
  private ParticipantService participantService;
  private ListBuilderDAO listBuilderDAO;
  private UserDAO userDAO;

  // Bug # 34056
  private CountryDAO countryDAO;

  private UserService userService;

  /**
   * Get all audiences from the database.
   *
   * @see com.biperf.core.service.product.ProductService#getAll()
   * @return List
   */
  public List getAll()
  {
    return audienceDAO.getAll();
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.AudienceService#getAudienceById(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param audienceId
   * @param associationRequestCollection
   * @return Audience
   */
  public Audience getAudienceById( Long audienceId, AssociationRequestCollection associationRequestCollection )
  {
    Audience audience = audienceDAO.getAudienceById( audienceId );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( audience );
    }
    return audience;
  }

  /**
   * Get the audience from database by name
   *
   * @param name
   * @return Audience
   */
  public Audience getAudienceByName( String name )
  {
    return audienceDAO.getAudienceByName( name );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.AudienceService#save(com.biperf.core.domain.participant.Audience)
   * @param audience
   * @return Audience
   */
  public Audience save( Audience audience ) throws ServiceErrorException
  {
    try
    {
      if ( audience.getId() != null )
      {
        // audience has been defined. Update the audience in cm if needed
        Audience oldAudience = audienceDAO.getAudienceById( audience.getId() );
        if ( !oldAudience.getName().equals( audience.getName() ) )
        {
          cmAssetService.updateCmsAudience( oldAudience.getName(), audience.getName() );
        }
      }
      else
      {
        // audience is new - we have to create it in cm.
        cmAssetService.createCmsAudience( audience.getName() );
      }

      Audience aud = this.audienceDAO.save( audience );
      if ( audience instanceof CriteriaAudience )
      {
        ConcurrentHashMap<Long, CriteriaAudience> allCriteriaAudiences = (ConcurrentHashMap<Long, CriteriaAudience>)criteriaAudienceCache.get( CACHE_CRITERIA_AUDIENCE_KEY );
        if ( allCriteriaAudiences == null )
        {
          allCriteriaAudiences = new ConcurrentHashMap<Long, CriteriaAudience>();
        }
        allCriteriaAudiences.put( audience.getId(), (CriteriaAudience)audience );
        criteriaAudienceCache.put( CACHE_CRITERIA_AUDIENCE_KEY, allCriteriaAudiences );
      }
      clearPromoEligibilityCache();
      return aud;
    }
    catch( JDBCException e )
    {
      throw new ServiceErrorExceptionWithRollback( "system.errors.SYSTEM_EXCEPTION", e );
    }
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.AudienceService#copyAudience(java.lang.Long,
   *      java.lang.String, java.util.List)
   * @param audienceIdToCopy
   * @param newAudienceName
   * @return Audience (The copied Audience)
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   * @throws BeaconRuntimeException
   */
  public Audience copyAudience( Long audienceIdToCopy, String newAudienceName, List newChildAudienceNameHolders )
      throws UniqueConstraintViolationException, ServiceErrorException, BeaconRuntimeException
  {
    Audience copiedAudience = null;

    Audience originalAudience = audienceDAO.getAudienceById( audienceIdToCopy );

    try
    {
      copiedAudience = (Audience)originalAudience.deepCopy( false, newAudienceName, null );
    }
    catch( CloneNotSupportedException cnse )
    {
      throw new ServiceErrorException( "Audience (id: " + originalAudience.getId() + ") failed cloning", cnse );
    }

    save( copiedAudience );

    return copiedAudience;

  }

  public boolean isUserInPromotionAudiences( Participant participant, Set promotionAudiences )
  {
    boolean isPromotionAudiencesMember = false;

    for ( Iterator iter = promotionAudiences.iterator(); iter.hasNext() && !isPromotionAudiencesMember; )
    {
      PromotionAudience promotionAudience = (PromotionAudience)iter.next();
      Audience audience = promotionAudience.getAudience();

      if ( audience instanceof PaxAudience || audience instanceof CriteriaAudience )
      {
        isPromotionAudiencesMember = isParticipantInAudience( participant.getId(), audience );
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown audience class type: " + audience.getClass().getName() );
      }

    }

    return isPromotionAudiencesMember;
  }

  public boolean isUserInPromotionDivisionAudiences( Participant participant, Set<DivisionCompetitorsAudience> divCompAudiences )
  {
    boolean isPromotionAudiencesMember = false;

    for ( Iterator<DivisionCompetitorsAudience> iter = divCompAudiences.iterator(); iter.hasNext() && !isPromotionAudiencesMember; )
    {
      DivisionCompetitorsAudience divCompAudience = iter.next();
      Audience audience = divCompAudience.getAudience();

      if ( audience instanceof PaxAudience || audience instanceof CriteriaAudience )
      {
        isPromotionAudiencesMember = isParticipantInAudience( participant.getId(), audience );
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown audience class type: " + audience.getClass().getName() );
      }

    }
    return isPromotionAudiencesMember;
  }

  /**
   * Will check the node to see if it is valid for any of the audiences.  If the
   * audience is a pax audience then the node will automatically be valid.  If it is a criteria
   * audience and the criteria contains node criteria the appropriate validations will
   * be done. If roleType passed is null then any node criteria for that node will be valid, otherwise
   * the role will also be validated.
   * 
   * @param node
   * @param promotionAudiences
   * @param roleType
   * @return
   */
  public boolean isNodeInPromotionAudiences( Node node, Set promotionAudiences, HierarchyRoleType roleType )
  {
    boolean isPromotionAudiencesMember = false;

    for ( Iterator iter = promotionAudiences.iterator(); iter.hasNext() && !isPromotionAudiencesMember; )
    {
      PromotionAudience promotionAudience = (PromotionAudience)iter.next();
      Audience audience = promotionAudience.getAudience();

      if ( audience instanceof CriteriaAudience )
      {
        CriteriaAudience criteriaAudience = (CriteriaAudience)audience;
        isPromotionAudiencesMember = isNodeInCriteriaAudience( node, criteriaAudience, roleType );
      }
      else if ( audience instanceof PaxAudience )
      {
        return true;
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown audience class type: " + audience.getClass().getName() );
      }

    }

    return isPromotionAudiencesMember;
  }

  /**
   * Will check each node to see if it is valid for any of the audiences.  The list
   * returned will contain all nodes that meet the specified audience criteria.  If the
   * audience is a pax audience then all nodes will be valid.  If it is a criteria
   * audience and the criteria contains node criteria the appropriate validations will
   * be done.
   * 
   * @param nodeList
   * @param audienceSet
   * @return a list of nodes that are valid for the given audiences
   */
  public List<Node> filterNodesByAudiences( List nodeList, Set audienceSet )
  {
    if ( containsPaxAudience( audienceSet ) )
    {
      // If one of the audiences is a pax type audience then all the nodes are valid
      return nodeList;
    }
    List<Node> filteredNodeList = new ArrayList();
    for ( Iterator nodeIter = nodeList.iterator(); nodeIter.hasNext(); )
    {
      Node currentNode = (Node)nodeIter.next();
      for ( Iterator audienceIter = audienceSet.iterator(); audienceIter.hasNext(); )
      {
        Audience currentAudience = (Audience)audienceIter.next();
        if ( currentAudience instanceof CriteriaAudience )
        {
          if ( isNodeInCriteriaAudience( currentNode, (CriteriaAudience)currentAudience, null ) )
          {
            filteredNodeList.add( currentNode );
            break;
          }
        }
      }
    }
    return filteredNodeList;
  }

  private boolean containsPaxAudience( Set audienceSet )
  {
    for ( Iterator audienceIter = audienceSet.iterator(); audienceIter.hasNext(); )
    {
      Audience currentAudience = (Audience)audienceIter.next();
      if ( currentAudience instanceof PaxAudience )
      {
        return true;
      }
    }
    return false;
  }

  public boolean isParticipantInAudience( Long participantId, Audience audience )
  {
    int size = audienceDAO.checkAudiencesByAudienceIdParticipantId( audience.getId(), participantId );
    boolean inAudience = size > 0 ? true : false;
    return inAudience;
  }

  private boolean isParticipantInCriteriaAudience( Participant participant, CriteriaAudience criteriaAudience )
  {
    for ( Iterator iter = criteriaAudience.getAudienceCriterias().iterator(); iter.hasNext(); )
    {
      // pax is in crit audience if in any Audience Criteria
      AudienceCriteria audienceCriteria = (AudienceCriteria)iter.next();

      // Is in audience if each criteria test is met
      if ( isParticipantInAudienceCriteria( participant, audienceCriteria ) )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isParticipantInAudienceCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    boolean isPAXMetAllExcludeCriterias = false;
    int totalNumberOfExclusions = 0;
    int matchedExclusionsByPAX = 0;
    totalNumberOfExclusions = getTotalNumberOfExclusions( audienceCriteria );
    if ( totalNumberOfExclusions != 0 )
    {
      // Country
      if ( !isParticipantInExcludeCountryCriteria( participant, audienceCriteria ) )
      {
        matchedExclusionsByPAX++;
      }
      // Node Role
      if ( !isParticipantInExcludeNodeRoleCriteria( participant, audienceCriteria ) )
      {
        matchedExclusionsByPAX++;
      }
      // Node Characteristic
      if ( !isParticipantInExcludeNodeCharacteristicCriteria( participant, audienceCriteria ) )
      {
        matchedExclusionsByPAX++;
      }
      // Node Name
      if ( !isParticipantInExcludeNodeNameCriteria( participant, audienceCriteria ) )
      {
        matchedExclusionsByPAX++;
      }
      // Job POsition
      if ( !isParticipantInExcludePositionCriteria( participant, audienceCriteria ) )
      {
        matchedExclusionsByPAX++;
      }
      // Dept Position
      if ( !isParticipantInExcludeDepartmentCriteria( participant, audienceCriteria ) )
      {
        matchedExclusionsByPAX++;
      }
      // PAX Char
      if ( !isParticipantInExcludePaxCharacteristicCriteria( participant, audienceCriteria ) )
      {
        matchedExclusionsByPAX++;
      }
      if ( totalNumberOfExclusions == matchedExclusionsByPAX )
      {
        isPAXMetAllExcludeCriterias = true;
      }
    }

    // Is in audience if each criteria test is met
    return isParticipantInNameCriteria( participant, audienceCriteria ) && isParticipantInPaxCharacteristicCriteria( participant, audienceCriteria )
        && isParticipantInEmployerCriteria( participant, audienceCriteria ) && isParticipantInLangaugeCriteria( participant, audienceCriteria )
        && isParticipantInNodeCriteria( participant, audienceCriteria ) && isParticipantInCountryCriteria( participant, audienceCriteria )
        // audience exclude criteria - if at least one exclude criteria not met then great
        // permission
        && !isPAXMetAllExcludeCriterias;
  }

  private int getTotalNumberOfExclusions( AudienceCriteria audienceCriteria )
  {
    int totalNumberOfExclusions = 0;
    if ( audienceCriteria.getExcludeCountryId() != null )
    {
      totalNumberOfExclusions++;
    }
    if ( audienceCriteria.getExcludeDepartmentType() != null )
    {
      totalNumberOfExclusions++;
    }
    if ( audienceCriteria.getExcludeNodeName() != null )
    {
      totalNumberOfExclusions++;
    }
    if ( audienceCriteria.getExcludeNodeRole() != null )
    {
      totalNumberOfExclusions++;
    }
    if ( audienceCriteria.getExcludePositionType() != null )
    {
      totalNumberOfExclusions++;
    }
    for ( Iterator iter = audienceCriteria.getCharacteristicCriterias().iterator(); iter.hasNext(); )
    {
      if ( iter.next() instanceof AudienceCriteriaCharacteristic )
      {
        totalNumberOfExclusions++;
        break;
      }
    }
    if ( audienceCriteria.getExcludeNodeTypeId() != null )
    {
      totalNumberOfExclusions++;
    }
    return totalNumberOfExclusions;
  }

  /* Bug # 34056 start */
  private boolean isParticipantInCountryCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    UserAddress address = participant.getPrimaryAddress();
    if ( null != audienceCriteria.getCountryId() )
    {
      Country country = countryDAO.getCountryById( audienceCriteria.getCountryId() );
      if ( null != country && ( null == address || null == address.getAddress() || !address.getAddress().getCountry().getId().equals( audienceCriteria.getCountryId() ) ) )
      {
        return false;
      }
    }
    return true;
  }
  /* Bug # 34056 end */

  private boolean isParticipantInExcludeCountryCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    UserAddress address = participant.getPrimaryAddress();
    if ( null != audienceCriteria.getExcludeCountryId() )
    {
      Country country = countryDAO.getCountryById( audienceCriteria.getExcludeCountryId() );
      if ( null != country && ( null == address || null == address.getAddress() || address.getAddress().getCountry().getId().equals( audienceCriteria.getExcludeCountryId() ) ) )
      {
        return false;
      }
    }
    return true;
  }

  private boolean isParticipantInNameCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    String criteriaFirstNamePrefix = audienceCriteria.getFirstName();
    String paxFirstName = participant.getFirstName();
    if ( criteriaFirstNamePrefix != null && ( paxFirstName == null || !paxFirstName.toLowerCase().startsWith( criteriaFirstNamePrefix.toLowerCase() ) ) )
    {
      return false;
    }

    String criteriaLastNamePrefix = audienceCriteria.getLastName();
    String paxLastName = participant.getLastName();
    if ( criteriaLastNamePrefix != null && ( paxLastName == null || !paxLastName.toLowerCase().startsWith( criteriaLastNamePrefix.toLowerCase() ) ) )
    {
      return false;
    }
    return true;
  }

  private boolean isParticipantInLangaugeCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    LanguageType criteriaLanguagePreference = audienceCriteria.getLanguageType();
    LanguageType paxLanguagePreference = participant.getLanguageType();
    if ( null != criteriaLanguagePreference && ( null == paxLanguagePreference || !paxLanguagePreference.getCode().equals( criteriaLanguagePreference.getCode() ) ) )
    {
      return false;
    }
    return true;
  }

  private boolean isParticipantInPaxCharacteristicCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    for ( Iterator iter = audienceCriteria.getUserCharacteristicCriterias().iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

      if ( audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "exclude" ) )
      {
        continue;
      }

      Long audienceCriteriaCharacteristicTypeId = audienceCriteriaCharacteristic.getCharacteristic().getId();

      boolean typeIdMatchFound = false;
      for ( Iterator iterator = participant.getUserCharacteristics().iterator(); iterator.hasNext(); )
      {
        UserCharacteristic userCharacteristic = (UserCharacteristic)iterator.next();
        UserCharacteristicType userCharacteristicType = userCharacteristic.getUserCharacteristicType();
        if ( userCharacteristicType.getId().equals( audienceCriteriaCharacteristicTypeId ) )
        {
          typeIdMatchFound = true;

          if ( userCharacteristic.getCharacteristicValue() == null )
          {
            // Pax had null value for for the required criteria Characteristic, so match not met.
            return false;
          }
          // Bug Fix 38432
          // non-null char type match found, so check for value match
          if ( !userCharacteristic.getCharacteristicValue().toLowerCase().startsWith( audienceCriteriaCharacteristic.getCharacteristicValue().toLowerCase() ) )
          {
            return false;
          }
        }
      }

      if ( !typeIdMatchFound )
      {
        // Pax had no match looking for for the required criteria Characteristic id, so match not
        // met.
        return false;
      }
    }
    return true;
  }

  private boolean isParticipantInExcludePaxCharacteristicCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {

    int totalPAXCharacteristicExcludesSet = 0;
    int characteristicExcludesMet = 0;
    for ( Iterator iter = audienceCriteria.getUserCharacteristicCriterias().iterator(); iter.hasNext(); )
    {
      totalPAXCharacteristicExcludesSet++;
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

      if ( audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "include" ) )
      {
        continue;
      }
      Long audienceCriteriaCharacteristicTypeId = audienceCriteriaCharacteristic.getCharacteristic().getId();
      for ( Iterator iterator = participant.getUserCharacteristics().iterator(); iterator.hasNext(); )
      {
        UserCharacteristic userCharacteristic = (UserCharacteristic)iterator.next();
        UserCharacteristicType userCharacteristicType = userCharacteristic.getUserCharacteristicType();
        if ( userCharacteristicType.getId().equals( audienceCriteriaCharacteristicTypeId ) )
        {
          if ( userCharacteristic.getCharacteristicValue() != null && audienceCriteriaCharacteristic.getCharacteristicValue() != null )
          {

            String[] userCharacteristicValues = userCharacteristic.getCharacteristicValue().toLowerCase().split( "," );
            for ( int i = 0; i < userCharacteristicValues.length; i++ )
            {
              if ( userCharacteristicValues[i].equalsIgnoreCase( audienceCriteriaCharacteristic.getCharacteristicValue().toLowerCase() ) )
              {
                characteristicExcludesMet++;

              }
            }

          }
        }
      }

      if ( characteristicExcludesMet != totalPAXCharacteristicExcludesSet )
      {
        return true;
      }
    }
    if ( totalPAXCharacteristicExcludesSet == 0 )
    {
      return true;
    }
    return characteristicExcludesMet != totalPAXCharacteristicExcludesSet;
  }

  private boolean isParticipantInEmployerCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    Long criteriaEmployerId = audienceCriteria.getEmployerId();
    String criteriaDepartmentType = audienceCriteria.getDepartmentType();
    String criteriaPositionType = audienceCriteria.getPositionType();
    if ( criteriaEmployerId != null || criteriaPositionType != null || criteriaDepartmentType != null )
    {
      List participantEmployers = participant.getParticipantEmployers();
      Set validEmployerIds = new LinkedHashSet();
      Set validDepartmentTypes = new LinkedHashSet();
      Set validPositionTypes = new LinkedHashSet();
      for ( Iterator iterator = participantEmployers.iterator(); iterator.hasNext(); )
      {
        ParticipantEmployer participantEmployer = (ParticipantEmployer)iterator.next();
        if ( participantEmployer.getTerminationDate() == null || participantEmployer.getTerminationDate().after( new Date() ) )
        {
          validEmployerIds.add( participantEmployer.getEmployer().getId() );
          validDepartmentTypes.add( participantEmployer.getDepartmentType() );
          validPositionTypes.add( participantEmployer.getPositionType() );
        }

      }

      if ( criteriaEmployerId != null && !validEmployerIds.contains( criteriaEmployerId ) )
      {
        return false;
      }

      if ( criteriaDepartmentType != null && !validDepartmentTypes.contains( criteriaDepartmentType ) )
      {
        return false;
      }

      if ( criteriaPositionType != null && !validPositionTypes.contains( criteriaPositionType ) )
      {
        return false;
      }
    }
    return true;
  }

  private boolean isParticipantInExcludeDepartmentCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    String criteriaDepartmentType = audienceCriteria.getExcludeDepartmentType();
    // PositionType criteriaPositionType = audienceCriteria.getExcludePositionType();
    if ( criteriaDepartmentType != null )
    {
      List participantEmployers = participant.getParticipantEmployers();
      Set validEmployerIds = new LinkedHashSet();
      Set validDepartmentTypes = new LinkedHashSet();
      // Set validPositionTypes = new LinkedHashSet();
      for ( Iterator iterator = participantEmployers.iterator(); iterator.hasNext(); )
      {
        ParticipantEmployer participantEmployer = (ParticipantEmployer)iterator.next();
        if ( participantEmployer.getTerminationDate() == null || participantEmployer.getTerminationDate().after( new Date() ) )
        {
          validDepartmentTypes.add( participantEmployer.getDepartmentType() );
          // validPositionTypes.add( participantEmployer.getPositionType() );
        }

      }

      if ( criteriaDepartmentType != null && validDepartmentTypes.contains( criteriaDepartmentType ) )
      {
        return false;
      }

      // if ( criteriaPositionType != null && validPositionTypes.contains( criteriaPositionType ) )
      // {
      // return false;
      // }
    }
    return true;
  }

  private boolean isParticipantInExcludePositionCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    // DepartmentType criteriaDepartmentType = audienceCriteria
    // .getExcludeDepartmentType();
    String criteriaPositionType = audienceCriteria.getExcludePositionType();
    if ( criteriaPositionType != null )
    {
      List participantEmployers = participant.getParticipantEmployers();
      Set validEmployerIds = new LinkedHashSet();
      // Set validDepartmentTypes = new LinkedHashSet();
      Set validPositionTypes = new LinkedHashSet();
      for ( Iterator iterator = participantEmployers.iterator(); iterator.hasNext(); )
      {
        ParticipantEmployer participantEmployer = (ParticipantEmployer)iterator.next();
        if ( participantEmployer.getTerminationDate() == null || participantEmployer.getTerminationDate().after( new Date() ) )
        {
          // validDepartmentTypes.add(participantEmployer
          // .getDepartmentType());
          validPositionTypes.add( participantEmployer.getPositionType() );
        }

      }

      // if (criteriaDepartmentType != null
      // && validDepartmentTypes.contains(criteriaDepartmentType)) {
      // return false;
      // }

      if ( criteriaPositionType != null && validPositionTypes.contains( criteriaPositionType ) )
      {
        return false;
      }
    }
    return true;
  }

  private boolean isParticipantInNodeCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    for ( Iterator iterator = participant.getUserNodes().iterator(); iterator.hasNext(); )
    {
      UserNode userNode = (UserNode)iterator.next();
      if ( isNodeInNodeCriteria( userNode.getNode(), audienceCriteria, userNode.getHierarchyRoleType() ) )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isParticipantInExcludeNodeRoleCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    for ( Iterator iterator = participant.getUserNodes().iterator(); iterator.hasNext(); )
    {
      UserNode userNode = (UserNode)iterator.next();
      if ( userNode.getHierarchyRoleType() != null && audienceCriteria.getExcludeNodeRole() != null )
      {
        if ( userNode.getHierarchyRoleType().equals( audienceCriteria.getExcludeNodeRole() ) )
        {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isParticipantInExcludeNodeCharacteristicCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    for ( Iterator iterator = participant.getUserNodes().iterator(); iterator.hasNext(); )
    {
      UserNode userNode = (UserNode)iterator.next();
      if ( audienceCriteria.getExcludeNodeTypeCharacteristicCriterias() != null && audienceCriteria.getExcludeNodeTypeCharacteristicCriterias().size() > 0 )
      {
        // node type check
        if ( audienceCriteria.getExcludeNodeTypeId() != null )
        {
          if ( !userNode.getNode().getNodeType().getId().equals( audienceCriteria.getExcludeNodeTypeId() ) )
          {
            continue;
          }
        }
        // node characteristics check
        if ( !isNodeInExcludeNodeCharacteristicCriteria( userNode.getNode(), audienceCriteria ) )
        {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isParticipantInExcludeNodeNameCriteria( Participant participant, AudienceCriteria audienceCriteria )
  {
    for ( Iterator iterator = participant.getUserNodes().iterator(); iterator.hasNext(); )
    {
      UserNode userNode = (UserNode)iterator.next();
      // node name/id checks
      String criteriaNodeNamePrefix = audienceCriteria.getExcludeNodeName();
      if ( audienceCriteria.getExcludeNodeId() != null )
      {

        if ( audienceCriteria.isExcludeChildNodesIncluded() )
        {
          Node criteriaNode = nodeService.getNodeById( audienceCriteria.getExcludeNodeId() );
          if ( criteriaNode != null )
          {
            if ( userNode.getNode().isMemberOfInputNodeBranch( criteriaNode ) )
            {
              return false;
            }
          }
          else
          {
            // node has been deleted (or maybe data integrity bug???), so this criteria can never be
            // met.
            return false;
          }
        }
        else
        {
          // Child nodes not included just look for nodeId match
          if ( userNode.getNode().getId().equals( audienceCriteria.getExcludeNodeId() ) )
          {
            return false;
          }
        }
      }
      else if ( criteriaNodeNamePrefix != null )
      {
        // node must start with node name
        if ( audienceCriteria.isExcludeChildNodesIncluded() )
        {
          if ( userNode.getNode().isMemberOfNodeBranchesStartingWithInputNodeNamePrefix( criteriaNodeNamePrefix ) )
          {
            return false;
          }
        }
        else
        {
          if ( userNode.getNode().getName().toLowerCase().startsWith( criteriaNodeNamePrefix.toLowerCase() ) )
          {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Test if the node is in the criteria audience.
   * 
   * @param node
   * @param criteriaAudience
   * @param roleType
   * @return boolean
   */
  private boolean isNodeInCriteriaAudience( Node node, CriteriaAudience criteriaAudience, HierarchyRoleType roleType )
  {
    for ( Iterator iter = criteriaAudience.getAudienceCriterias().iterator(); iter.hasNext(); )
    {
      // node is in crit audience if in any Audience Criteria
      AudienceCriteria audienceCriteria = (AudienceCriteria)iter.next();
      if ( isNodeInNodeCriteria( node, audienceCriteria, roleType ) )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isNodeInNodeCriteria( Node node, AudienceCriteria audienceCriteria, HierarchyRoleType roleType )
  {
    if ( !isNodeBasedCriteria( audienceCriteria ) )
    {
      return true;
    }

    // node type check
    if ( audienceCriteria.getNodeTypeId() != null )
    {
      if ( !node.getNodeType().getId().equals( audienceCriteria.getNodeTypeId() ) )
      {
        return false;
      }
    }

    // node characteristics check
    if ( !isNodeInNodeCharacteristicCriteria( node, audienceCriteria ) )
    {
      return false;
    }

    // node name/id checks
    String criteriaNodeNamePrefix = audienceCriteria.getNodeName();
    if ( audienceCriteria.getNodeId() != null )
    {

      if ( audienceCriteria.isChildNodesIncluded() )
      {
        Node criteriaNode = nodeService.getNodeById( audienceCriteria.getNodeId() );
        if ( criteriaNode != null )
        {
          if ( !node.isMemberOfInputNodeBranch( criteriaNode ) )
          {
            return false;
          }
        }
        else
        {
          // node has been deleted (or maybe data integrity bug???), so this criteria can never be
          // met.
          return false;
        }
      }
      else
      {
        // Child nodes not included just look for nodeId match
        if ( !node.getId().equals( audienceCriteria.getNodeId() ) )
        {
          return false;
        }
      }
    }
    else if ( criteriaNodeNamePrefix != null )
    {
      // node must start with node name
      if ( audienceCriteria.isChildNodesIncluded() )
      {
        if ( !node.isMemberOfNodeBranchesStartingWithInputNodeNamePrefix( criteriaNodeNamePrefix ) )
        {
          return false;
        }
      }
      else
      {
        if ( !node.getName().toLowerCase().startsWith( criteriaNodeNamePrefix.toLowerCase() ) )
        {
          return false;
        }
      }
    }
    if ( roleType != null && audienceCriteria.getNodeRole() != null )
    {
      if ( !roleType.equals( audienceCriteria.getNodeRole() ) )
      {
        return false;
      }
    }
    return true;
  }

  private boolean isNodeBasedCriteria( AudienceCriteria audienceCriteria )
  {
    return !StringUtils.isEmpty( audienceCriteria.getNodeName() ) || null != audienceCriteria.getNodeId() || null != audienceCriteria.getNodeTypeId() || null != audienceCriteria.getNodeRole()
        || !audienceCriteria.getNodeTypeCharacteristicCriterias().isEmpty();
  }

  private boolean isNodeInNodeCharacteristicCriteria( Node node, AudienceCriteria audienceCriteria )
  {
    /*
     * Bug # 39863 start - Bug fix 39863 lazy initialization error if recipient audience criteria is
     * based on node type and node type char value
     */
    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );
    node = nodeService.getNodeWithAssociationsById( node.getId(), nodeAssociationRequestCollection );
    /* Bug # 39863 end */
    for ( Iterator iter = audienceCriteria.getNodeTypeCharacteristicCriterias().iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

      if ( audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "exclude" ) )
      {
        continue;
      }
      if ( !isNodeCharacteristicMatchFound( node, audienceCriteriaCharacteristic ) )
      {
        return false;
      }
    }
    return true;
  }

  private boolean isNodeInExcludeNodeCharacteristicCriteria( Node node, AudienceCriteria audienceCriteria )
  {
    for ( Iterator iter = audienceCriteria.getNodeTypeCharacteristicCriterias().iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

      if ( audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "include" ) )
      {
        continue;
      }
      if ( isNodeCharacteristicMatchFound( node, audienceCriteriaCharacteristic ) )
      {
        return false;
      }
    }
    return true;
  }

  private boolean isNodeCharacteristicMatchFound( Node node, AudienceCriteriaCharacteristic audienceCriteriaCharacteristic )
  {
    Long audienceCriteriaCharacteristicTypeId = audienceCriteriaCharacteristic.getCharacteristic().getId();

    for ( Iterator iterator = node.getActiveNodeCharacteristics().iterator(); iterator.hasNext(); )
    {
      NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)iterator.next();
      NodeTypeCharacteristicType nodeTypeCharacteristicType = nodeCharacteristic.getNodeTypeCharacteristicType();
      if ( nodeTypeCharacteristicType.getId().equals( audienceCriteriaCharacteristicTypeId ) )
      {
        if ( nodeCharacteristic.getCharacteristicValue() == null )
        {
          // Pax had null value for for the required criteria Characteristic, so match not found.
          continue;
        }
        // non-null char type match found, so check for value match
        if ( !audienceCriteriaCharacteristic.getCharacteristicValue().toLowerCase().equals( nodeCharacteristic.getCharacteristicValue().toLowerCase() ) )
        {
          return false;
        }
      }
    }

    return true;
  }

  public AudienceDAO getAudienceDAO()
  {
    return audienceDAO;
  }

  public void setAudienceDAO( AudienceDAO audienceDAO )
  {
    this.audienceDAO = audienceDAO;
  }

  /**
   * @param nodeService value for nodeService property
   */
  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  private boolean isParticipantActive( Participant participant )
  {
    return ! ( participant.isActive() == null || !participant.isActive().booleanValue() || participant.getStatus() == null || participant.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.AudienceService#isParticipantInPrimaryAudience(com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.domain.participant.Participant)
   * @param promotion
   * @param participant
   * @return boolean
   */
  private boolean isParticipantInPrimaryAudienceCheck( Promotion promotion, Participant participant )
  {
    boolean result = false;

    // if the user is inactive, there are not in the audience
    if ( !isParticipantActive( participant ) )
    {
      return false;
    }

    if ( promotion.getPrimaryAudienceType() == null )
    {
      return false;
    }

    if ( promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
        || isUserInPromotionAudiences( participant, promotion.getPromotionPrimaryAudiences() ) )
    {
      result = true;
    }
    else if ( promotion.isProductClaimPromotion() && promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ENTIRE_PARENT_AUDIENCE_CODE ) ) )
    {
      ProductClaimPromotion productClaimPromotion = (ProductClaimPromotion)promotion;
      if ( isParticipantInPrimaryAudience( productClaimPromotion.getParentPromotion(), participant ) )
      {
        result = true;
      }
    }
    else if ( promotion.getPrimaryAudienceType().isSpecifyAudienceType() )
    {
      result = isUserInPromotionAudiences( participant, promotion.getPromotionPrimaryAudiences() );
    }
    return result;
  }

  public boolean isParticipantInPrimaryAudience( Promotion promotion, Participant participant )
  {
    if ( participant != null )
    {
      PaxPromoEligibilityValueBean paxEligibility = (PaxPromoEligibilityValueBean)paxPromoEligibilityCache.get( participant.getId() );
      if ( null == paxEligibility )
      {
        paxEligibility = new PaxPromoEligibilityValueBean();
        paxPromoEligibilityCache.put( participant.getId(), paxEligibility );
      }
      Boolean isInPrimaryAud = paxEligibility.isInPrimaryAudience( participant.getId(), promotion.getId() );
      if ( null == isInPrimaryAud )
      {
        Boolean status = isParticipantInPrimaryAudienceCheck( promotion, participant );
        paxEligibility.setInPrimaryAudience( participant.getId(), promotion.getId(), status );
        return status;
      }
      return isInPrimaryAud.booleanValue();
    }
    else
    {
      return false;
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.participant.AudienceService#isParticipantInSecondaryAudience(com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.domain.participant.Participant)
   * @param promotion
   * @param participant
   * @return boolean
   */
  private boolean isParticipantInSecondaryAudienceCheck( Promotion promotion, Participant participant )
  {
    long start = System.currentTimeMillis();
    boolean inAudience = false;
    // if we care about the submitter node, look them up and use them in the determination
    if ( promotion instanceof AbstractRecognitionPromotion && ( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ).equals( promotion.getSecondaryAudienceType() )
        || SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ).equals( promotion.getSecondaryAudienceType() ) ) )
    {
      for ( Iterator iter = getSubmitterNodeList( (AbstractRecognitionPromotion)promotion ).iterator(); iter.hasNext(); )
      {
        inAudience = isParticipantInSecondaryAudience( promotion, participant, (Node)iter.next() );
        if ( inAudience )
        {
          break;
        }
      }
    }
    else
    {
      inAudience = isParticipantInSecondaryAudience( promotion, participant, null );
    }
    long end = System.currentTimeMillis();
    if ( end - start > 1000 )
    {
      LogFactory.getLog( AudienceServiceImpl.class )
          .error( "isParticipantInSecondaryAudiene for promotion " + promotion.getName() + " and participant " + participant.getNameLFMWithComma() + " took " + ( end - start ) / 1000 + " seconds" );
    }
    return inAudience;
  }

  public boolean isParticipantInSecondaryAudience( Promotion promotion, Participant participant )
  {
    PaxPromoEligibilityValueBean paxEligibility = (PaxPromoEligibilityValueBean)paxPromoEligibilityCache.get( participant.getId() );
    if ( null == paxEligibility )
    {
      paxEligibility = new PaxPromoEligibilityValueBean();
      paxPromoEligibilityCache.put( participant.getId(), paxEligibility );
    }

    Boolean isInSecondaryAud = paxEligibility.isInSecondaryAudience( participant.getId(), promotion.getId() );
    if ( null == isInSecondaryAud )
    {
      Boolean status = isParticipantInSecondaryAudienceCheck( promotion, participant );
      paxEligibility.setInSecondaryAudience( participant.getId(), promotion.getId(), status );
      return status;
    }
    return isInSecondaryAud;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.AudienceService#isParticipantInSecondaryAudience(com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.domain.participant.Participant, com.biperf.core.domain.hierarchy.Node)
   * @param promotion
   * @param participant
   * @param submitterNode
   * @return boolean
   */
  private boolean isParticipantInSecondaryAudienceCheck( Promotion promotion, Participant participant, Node submitterNode )
  {
    boolean inAudience = false;

    // if the user is inactive, there are not in the audience
    if ( !isParticipantActive( participant ) )
    {
      return false;
    }

    if ( promotion.isThrowdownPromotion() )
    {
      Set<DivisionCompetitorsAudience> audiences = new HashSet<DivisionCompetitorsAudience>();
      ThrowdownPromotion tdPromo = (ThrowdownPromotion)promotion;
      for ( Division div : tdPromo.getDivisions() )
      {
        for ( DivisionCompetitorsAudience compAud : div.getCompetitorsAudience() )
        {
          audiences.add( compAud );
        }
      }
      inAudience = isUserInPromotionDivisionAudiences( participant, audiences );
      return inAudience;
    }

    if ( promotion.getSecondaryAudienceType() == null )
    {
      return false;
    }

    if ( promotion.isProductClaimPromotion() && ! ( (ProductClaimPromotion)promotion ).isTeamUsed() )
    {
      // Speical case for product claim promo, check isteamUsed. TODO: could be generalized to
      // promotion as isSecondaryUsed? Would need to
      // make sure to check jsps if changing isteamUsed to isSecondaryUsed
      return false;
    }

    if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      inAudience = true;
    }
    else if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) ) )
    {
      inAudience = isParticipantInPrimaryAudience( promotion, participant );
    }
    else if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) ) )
    {
      inAudience = isUserInAnyNodeOfPrimaryAudienceMember( participant, submitterNode, false );
    }
    else if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) ) )
    {
      // team is from specific audience
      inAudience = isUserInPromotionAudiences( participant, promotion.getPromotionSecondaryAudiences() );
    }
    else if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) ) )
    {
      inAudience = isUserInAnyNodeOfPrimaryAudienceMember( participant, submitterNode, true );
    }
    else if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.CREATOR_ORG_ONLY_CODE ) ) )
    {
      inAudience = isUserInAnyNodeOfPrimaryAudienceMember( participant, submitterNode, false );
    }
    else if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.CREATOR_ORG_AND_BELOW_CODE ) ) )
    {
      inAudience = isUserInAnyNodeOfPrimaryAudienceMember( participant, submitterNode, true );
    }

    return inAudience;
  }

  public boolean isParticipantInSecondaryAudience( Promotion promotion, Participant participant, Node submitterNode )
  {
    Long submitterNodeId = null;
    if ( null != submitterNode )
    {
      submitterNodeId = submitterNode.getId();
    }
    PaxPromoEligibilityValueBean paxEligibility = (PaxPromoEligibilityValueBean)paxPromoEligibilityCache.get( participant.getId() );
    if ( null == paxEligibility )
    {
      paxEligibility = new PaxPromoEligibilityValueBean();
      paxPromoEligibilityCache.put( participant.getId(), paxEligibility );
    }

    Boolean isInSecondaryAud = paxEligibility.isInSecondaryAudience( participant.getId(), promotion.getId(), submitterNodeId );
    if ( null == isInSecondaryAud )
    {
      Boolean status = isParticipantInSecondaryAudienceCheck( promotion, participant, submitterNode );
      paxEligibility.setInSecondaryAudience( participant.getId(), promotion.getId(), status, submitterNodeId );
      return status;
    }
    return isInSecondaryAud.booleanValue();
  }

  /**
   * Filter the nodeList based on the secondaryAudience.
   * @param promotion
   * @param nodeCollection
   * @param submitterNode
   * @return a List of Nodes
   */
  public List<Node> filterNodeListBySecondaryAudience( Promotion promotion, Collection nodeCollection, Node submitterNode )
  {
    ArrayList<Node> filteredList = new ArrayList<Node>();
    for ( Iterator nodeIter = nodeCollection.iterator(); nodeIter.hasNext(); )
    {
      Node currentNode = (Node)nodeIter.next();
      if ( isNodeValidForSecondaryAudience( promotion, currentNode, submitterNode ) )
      {
        filteredList.add( currentNode );
      }
    }
    return filteredList;
  }

  /**
   * Check if the node is valid for the secondaryAudience.
   * @param promotion
   * @param node
   * @param submitterNode
   * @return boolean
   */
  private boolean isNodeValidForSecondaryAudience( Promotion promotion, Node node, Node submitterNode )
  {
    if ( node.isDeleted() )
    {
      return false;
    }
    if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      return true;
    }
    if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) ) )
    {
      if ( promotion.getPrimaryAudienceType().isSpecifyAudienceType() )
      {
        return isNodeInPromotionAudiences( node, promotion.getPromotionPrimaryAudiences(), null );
      }
      return true;
    }
    if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) ) )
    {
      return isNodeInSpecificNodeOfPrimaryAudienceMember( node, submitterNode, false );
    }
    if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) ) )
    {
      return isNodeInPromotionAudiences( node, promotion.getPromotionSecondaryAudiences(), null );
    }
    if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) ) )
    {
      return isNodeInSpecificNodeOfPrimaryAudienceMember( node, submitterNode, true );
    }
    return true;

  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.AudienceService#isParticipantInWebRulesAudience(com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.domain.participant.Participant)
   * @param promotion
   * @param participant
   * @return boolean
   */
  private boolean isParticipantInWebRulesAudienceCheck( Promotion promotion, Participant participant )
  {
    boolean inWebRulesAudience = false;

    // if the user is inactive, there are not in the audience
    if ( !isParticipantActive( participant ) )
    {
      return false;
    }

    if ( promotion.getWebRulesAudienceType() == null )
    {
      return false;
    }

    if ( promotion.getWebRulesAudienceType().getCode().equals( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) )
    {
      inWebRulesAudience = true;
    }
    else if ( promotion.getWebRulesAudienceType().getCode().equals( WebRulesAudienceType.SAME_AS_PRIMARY_CODE ) && isParticipantInPrimaryAudience( promotion, participant ) )
    {
      inWebRulesAudience = true;
    }
    else if ( promotion.getWebRulesAudienceType().getCode().equals( WebRulesAudienceType.CREATE_AUDIENCE_CODE ) )
    {
      // web rules has own audiences set
      inWebRulesAudience = isUserInPromotionAudiences( participant, promotion.getPromotionWebRulesAudiences() );
    }
    else if ( promotion.getWebRulesAudienceType().getCode().equals( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) )
    {
      inWebRulesAudience = isParticipantInPrimaryAudience( promotion, participant );
      if ( !inWebRulesAudience )
      {
        // if we care about the submitter node, look them up and use them in the determination
        if ( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ).equals( promotion.getSecondaryAudienceType() )
            || SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ).equals( promotion.getSecondaryAudienceType() ) )
        {
          for ( Iterator iter = getSubmitterNodeList( (AbstractRecognitionPromotion)promotion ).iterator(); iter.hasNext(); )
          {
            inWebRulesAudience = isParticipantInSecondaryAudience( promotion, participant, (Node)iter.next() );
            if ( inWebRulesAudience )
            {
              break;
            }
          }
        }
        else
        {
          inWebRulesAudience = isParticipantInSecondaryAudience( promotion, participant, null );
        }
      }
    }
    else if ( promotion.getWebRulesAudienceType().getCode().equals( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_CODE ) )
    {
      inWebRulesAudience = isParticipantInPrimaryAudience( promotion, participant );
    }
    else if ( promotion.getWebRulesAudienceType().getCode().equals( WebRulesAudienceType.ALL_ELIGIBLE_SECONDARY_CODE ) )
    {
      // if we care about the submitter node, look them up and use them in the determination
      if ( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ).equals( promotion.getSecondaryAudienceType() )
          || SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ).equals( promotion.getSecondaryAudienceType() ) )
      {
        for ( Iterator iter = getSubmitterNodeList( (AbstractRecognitionPromotion)promotion ).iterator(); iter.hasNext(); )
        {
          inWebRulesAudience = isParticipantInSecondaryAudience( promotion, participant, (Node)iter.next() );
          if ( inWebRulesAudience )
          {
            break;
          }
        }
      }
      else
      {
        inWebRulesAudience = isParticipantInSecondaryAudience( promotion, participant, null );
      }
    }
    else if ( promotion.getWebRulesAudienceType().getCode().equals( WebRulesAudienceType.ALL_SUBMITTER_CODE ) )
    {
      inWebRulesAudience = isParticipantInPrimaryAudience( promotion, participant );
    }
    return inWebRulesAudience;
  }

  public boolean isParticipantInWebRulesAudience( Promotion promotion, Participant participant )
  {
    PaxPromoEligibilityValueBean paxEligibility = (PaxPromoEligibilityValueBean)paxPromoEligibilityCache.get( participant.getId() );
    if ( null == paxEligibility )
    {
      paxEligibility = new PaxPromoEligibilityValueBean();
      paxPromoEligibilityCache.put( participant.getId(), paxEligibility );
    }
    Boolean isInWebRulesAudience = paxEligibility.isInWebRulesAudience( participant.getId(), promotion.getId() );
    if ( null == isInWebRulesAudience )
    {
      Boolean status = isParticipantInWebRulesAudienceCheck( promotion, participant );
      paxEligibility.setInWebRulesAudience( participant.getId(), promotion.getId(), status );
      return status;
    }
    return isInWebRulesAudience.booleanValue();
  }

  public boolean isViewWebRulesVisible( Long promotionId, Long participantId )
  {
    boolean isVisible = false;

    Promotion promotion = promotionDAO.getPromotionById( promotionId );

    if ( promotion.isWebRulesActive() )
    {
      if ( DateUtils.isDateBetween( new Date(), promotion.getWebRulesStartDate(), promotion.getWebRulesEndDate() ) )
      {
        // rules are active and the current date falls within the rules dates; so, check
        // to see if the user is in the web rules audience
        isVisible = isParticipantInWebRulesAudience( promotion, participantService.getParticipantById( participantId ) );
      }
    }

    return isVisible;
  }

  /**
   * Compile and return a Set of nodes which have a submitter in them for the given promotion
   * 
   * @param promotion
   * @return Set
   */
  private Set getSubmitterNodeList( AbstractRecognitionPromotion promotion )
  {
    Set nodes = new HashSet();
    for ( Iterator iter = promotion.getPrimaryAudiences().iterator(); iter.hasNext(); )
    {
      Audience audience = (Audience)iter.next();
      if ( audience instanceof CriteriaAudience )
      {
        CriteriaAudience criteriaAudience = (CriteriaAudience)audience;
        List participants = listBuilderDAO.searchParticipants( criteriaAudience, null, false, new HashSet(), true, false );
        for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
        {
          FormattedValueBean paxFormattedValueBean = (FormattedValueBean)paxIter.next();
          List assignedNodeIds = userDAO.getAssignedNodeIds( paxFormattedValueBean.getId() );
          for ( Iterator iterator2 = assignedNodeIds.iterator(); iterator2.hasNext(); )
          {
            FormattedValueBean assignedNodeIdValueBean = (FormattedValueBean)iterator2.next();
            nodes.add( nodeService.getNodeById( assignedNodeIdValueBean.getId() ) );
          }
        }
      }
      else if ( audience instanceof PaxAudience )
      {
        AssociationRequestCollection reqCollection = new AssociationRequestCollection();
        reqCollection.add( new AudienceToParticipantsAssociationRequest() );

        audience = getAudienceById( new Long( audience.getId() ), reqCollection );
        PaxAudience paxAudience = (PaxAudience)audience;
        for ( Iterator paxIter = paxAudience.getAudienceParticipants().iterator(); paxIter.hasNext(); )
        {
          AudienceParticipant audPax = (AudienceParticipant)paxIter.next();
          for ( Iterator nodeIter = audPax.getParticipant().getUserNodes().iterator(); nodeIter.hasNext(); )
          {
            UserNode un = (UserNode)nodeIter.next();
            nodes.add( un.getNode() );
          }
        }
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown audience class type: " + audience.getClass().getName() );
      }
    }
    return nodes;
  }

  private CriteriaAudience buildNodeCritieriaAudience( Node submitterNode, boolean childNodesIncluded )
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();

    audienceCriteria.setNodeId( submitterNode.getId() );
    audienceCriteria.setChildNodesIncluded( childNodesIncluded );
    CriteriaAudience criteriaAudience = new CriteriaAudience();
    criteriaAudience.addAudienceCriteria( audienceCriteria );
    return criteriaAudience;
  }

  /**
   * Return a set of all ParticipantAudiences and all CriteriaAudiences that are assigned to this
   * participant.
   *
   * @param participant
   * @return boolean
   */
  public Set getAllParticipantAudiences( Participant participant )
  {
    Set paxAudiences = new HashSet( audienceDAO.getAllPaxAudiencesByParticipantId( participant.getId() ) );

    ConcurrentHashMap<Long, CriteriaAudience> allCriteriaAudiences = (ConcurrentHashMap<Long, CriteriaAudience>)criteriaAudienceCache.get( CACHE_CRITERIA_AUDIENCE_KEY );
    if ( allCriteriaAudiences == null )
    {
      allCriteriaAudiences = new ConcurrentHashMap<Long, CriteriaAudience>();

      List<CriteriaAudience> allCriteriaAudiencesList = audienceDAO.getAllCriteriaAudiences();
      for ( CriteriaAudience criteriaAudience : allCriteriaAudiencesList )
      {
        allCriteriaAudiences.put( criteriaAudience.getId(), criteriaAudience );
      }
      criteriaAudienceCache.put( CACHE_CRITERIA_AUDIENCE_KEY, allCriteriaAudiences );
    }

    for ( CriteriaAudience criteriaAudienceEntry : allCriteriaAudiences.values() )
    {
      if ( isParticipantInAudience( participant.getId(), criteriaAudienceEntry ) )
      {
        paxAudiences.add( criteriaAudienceEntry );
      }
    }
    return paxAudiences;
  }

  /**
   * Check if any of the users nodes are valid for the given submitters node.
   * 
   * @param user
   * @param submitterNode
   * @param nodeAndBelow
   * @return boolean
   */
  private boolean isUserInAnyNodeOfPrimaryAudienceMember( User user, Node submitterNode, boolean nodeAndBelow )
  {
    // TODO: If submitters node is null we are always assuming the current user is valid - this may
    // not be correct.
    // Bug 4692 - This condition needs to be changed so that it returns false.
    // Even though the participant is not in the promotion audience this is returning true.
    // Not changing this as Pramod is going to look into the impact before we change to false.
    if ( submitterNode == null )
    {
      return true;
    }
    boolean foundPrimaryAudience = false;

    Set userNodeSet = user.getUserNodes();
    Iterator nodeIterator = userNodeSet.iterator();

    while ( nodeIterator.hasNext() )
    {
      UserNode userNode = (UserNode)nodeIterator.next();
      Node node = userNode.getNode();

      if ( isNodeInSpecificNodeOfPrimaryAudienceMember( node, submitterNode, nodeAndBelow ) )
      {
        foundPrimaryAudience = true;
        break;
      }
    }

    return foundPrimaryAudience;
  }

  /**
   * Processes a given node to check if it is applicable for the submitters node.
   * 
   * @param node
   * @param submitterNode
   * @param nodeAndBelow
   * @return boolean
   */
  private boolean isNodeInSpecificNodeOfPrimaryAudienceMember( Node node, Node submitterNode, boolean nodeAndBelow )
  {
    if ( nodeAndBelow )
    {
      return StringUtils.equals( node.getPath(), submitterNode.getPath() ) || node.getPath().startsWith( submitterNode.getPath() + Node.NODE_PATH_DELIMITER );
    }
    return StringUtils.equals( node.getPath(), submitterNode.getPath() );

  }

  /**
   *
   * check if there is a PaxAudience for the supplied audienceId and
   * particpantId
   *
   * @param audienceId
   * @param participantId
   * @return List of PaxAudience objects
   */
  public List checkPaxAudiencesByAudienceIdParticipantId( Participant participant, PaxAudience paxAudience )
  {
    List checkListofAudience = this.audienceDAO.checkPaxAudiencesByAudienceIdParticipantId( paxAudience.getId(), participant.getId() );
    return checkListofAudience;
  }

  /**
   * Is audience name unique
   * 
   * @param audienceName
   * @return boolean
   */
  public boolean isAudienceNameUnique( String audienceName )
  {
    return this.audienceDAO.isAudienceNameUnique( audienceName );
  }

  public boolean isUserInPromotionPartnerAudiences( Participant participant, Set<Audience> promotionPartnerAudiences )
  {
    boolean isPromotionPartnerAudiencesMember = false;

    for ( Iterator<Audience> iter = promotionPartnerAudiences.iterator(); iter.hasNext() && !isPromotionPartnerAudiencesMember; )
    {
      Audience audience = (Audience)iter.next();

      if ( audience instanceof PaxAudience || audience instanceof CriteriaAudience )
      {
        isPromotionPartnerAudiencesMember = isParticipantInAudience( participant.getId(), audience );
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown audience class type: " + audience.getClass().getName() );
      }

    }

    return isPromotionPartnerAudiencesMember;
  }

  public boolean isUserInPromotionManagerAudiences( Participant participant, Set<PromotionManagerWebRulesAudience> promotionPartnerAudiences )
  {
    boolean isPromotionPartnerAudiencesMember = false;

    for ( Iterator<PromotionManagerWebRulesAudience> iter = promotionPartnerAudiences.iterator(); iter.hasNext() && !isPromotionPartnerAudiencesMember; )
    {
      PromotionManagerWebRulesAudience audience = (PromotionManagerWebRulesAudience)iter.next();

      if ( audience instanceof PromotionManagerWebRulesAudience )
      {
        isPromotionPartnerAudiencesMember = audienceDAO.checkAudiencesByAudienceIdParticipantId( audience.getId(), participant.getId() ) > 0 ? true : false;
      }
      else
      {
        throw new BeaconRuntimeException( "Unknown audience class type: " + audience.getClass().getName() );
      }

    }

    return isPromotionPartnerAudiencesMember;
  }

  public boolean isUserInParticipantPartnerAudiences( Participant participant, Promotion promotion )
  {
    return audienceDAO.isPaxInPromotionPartnerAudiences( participant.getId(), promotion.getId() );
  }

  /**
   * Set the CMAssetService through IoC
   * 
   * @param cmAssetService
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  /**
   * @param participantService value for participantService property
   */
  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setListBuilderDAO( ListBuilderDAO listBuilderDAO )
  {
    this.listBuilderDAO = listBuilderDAO;
  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  /**
   * @param cacheFactory
   */
  // public void setCacheFactory( CacheFactory cacheFactory )
  // {
  // criteriaAudienceCache = cacheFactory.getCache( CACHE_CRITERIA_AUDIENCE_KEY );
  // paxPromoEligibilityCache = cacheFactory.getCache( CACHE_PAX_PROMO_ELIGIBILITY );
  // }

  /**
   * Only use for mock testing.
   * 
   * @param systemVariableCache
   */
  public void setCriteriaAudienceCacheForMock( Cache criteriaAudienceCache )
  {
    this.criteriaAudienceCache = criteriaAudienceCache;
  }

  /**
   * Only use for mock testing.
   * 
   * @param paxPromoEligibilityCacheForMock
   */
  public void setPaxPromoEligibilityCacheForMock( Cache paxPromoEligibilityCache )
  {
    this.paxPromoEligibilityCache = paxPromoEligibilityCache;
  }

  /**
   * Overridden from @see com.biperf.core.service.participant.AudienceService#clearCache()
   */
  public void clearCriteriaAudienceCache()
  {
    criteriaAudienceCache.remove( CACHE_CRITERIA_AUDIENCE_KEY );
  }

  public void clearPromoEligibilityCache()
  {
    paxPromoEligibilityCache.clear();
  }

  public void processLogout( AuthenticatedUser authenticatedUser )
  {
    paxPromoEligibilityCache.remove( authenticatedUser.getUserId() );
  }

  public List getAudienceList()
  {
    List<AudienceListValueBean> audience = new ArrayList<AudienceListValueBean>();
    List<Object> audienceList = audienceDAO.getAudienceList();

    for ( Iterator audienceListIterator = audienceList.iterator(); audienceListIterator.hasNext(); )
    {
      AudienceListValueBean audienceListValueBean = new AudienceListValueBean();
      Object[] audienceObject = (Object[])audienceListIterator.next();

      audienceListValueBean.setAudienceId( (Long)audienceObject[0] );
      audienceListValueBean.setAudienceName( (String)audienceObject[1] );
      audienceListValueBean.setAudienceType( (String)audienceObject[2] );
      audienceListValueBean.setDateModified( (String)audienceObject[3] );
      audienceListValueBean.setPublicAudience( Boolean.valueOf( (Boolean)audienceObject[4] ) );
      audienceListValueBean.setRosterAudienceId( (UUID)audienceObject[5] );

      audience.add( audienceListValueBean );
    }
    return audience;

  }

  public Map rematchParticipantForAllCriteriaAudiences( Long participantId ) throws ServiceErrorException
  {
    Map output = audienceDAO.rematchParticipantForAllCriteriaAudiences( participantId );
    validatePrecalculation( output );
    // re-index this user
    getAutoCompleteService().indexParticipants( Arrays.asList( participantId ) );
    return output;
  }

  public Map rematchNodeForAllCriteriaAudiences( Long sourceNodeId, Long destinationPaxNodeId, Long destinationChildNodeId ) throws ServiceErrorException
  {
    Map output = audienceDAO.rematchNodeForAllCriteriaAudiences( sourceNodeId, destinationPaxNodeId, destinationChildNodeId );
    validatePrecalculation( output );
    // reindex the users
    indexParticipants( output.get( "p_out_user_data" ) );
    return output;
  }

  public Map recreateCriteriaAudienceParticipants( Long audienceId ) throws ServiceErrorException
  {
    Map output = audienceDAO.recreateCriteriaAudienceParticipants( audienceId );
    validatePrecalculation( output );
    // reindex the users
    indexParticipants( output.get( "p_out_user_data" ) );
    return output;
  }

  /*
   * When updated Audiences, we need to address those changes in the ElasticSearch index.
   */
  private void indexParticipants( Object procOutput )
  {
    if ( procOutput instanceof List )
    {
      getAutoCompleteService().indexParticipants( (List)procOutput );
    }
  }

  /* Bug # 34056 start */
  public void setCountryDAO( CountryDAO countryDAO )
  {
    this.countryDAO = countryDAO;
  }
  /* Bug # 34056 end */

  public List filterParticipantNodesByAudienceNodeRole( List participants, Set audienceSet )
  {
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Long participantId = ( (User)paxIter.next() ).getId();
      Participant participant = null;
      for ( Object promotionAudience : audienceSet )
      {
        if ( promotionAudience instanceof CriteriaAudience )
        {
          // Refetch pax so know we have lazy navigable object.
          if ( participant == null )
          {
            participant = participantService.getParticipantById( participantId );
          }
          CriteriaAudience criteriaAudience = (CriteriaAudience)promotionAudience;
          if ( isParticipantInAudience( participant.getId(), criteriaAudience ) )
          {
            for ( Object oCriteria : criteriaAudience.getAudienceCriterias() )
            {
              // pax is in crit audience if in any Audience Criteria
              AudienceCriteria audienceCriteria = (AudienceCriteria)oCriteria;
              if ( isParticipantInAudienceCriteria( participant, audienceCriteria ) )
              {
                HierarchyRoleType userNodeRole = audienceCriteria.getNodeRole();
                if ( userNodeRole != null && userNodeRole.getName().length() > 0 )
                {
                  for ( Iterator userNodeIter = participant.getUserNodes().iterator(); userNodeIter.hasNext(); )
                  {
                    UserNode userNode = (UserNode)userNodeIter.next();
                    if ( !userNode.getHierarchyRoleType().equals( userNodeRole ) )
                    {
                      userNodeIter.remove();
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return participants;
  }

  public void validatePrecalculation( Map output ) throws ServiceErrorException
  {
    List errors = new ArrayList();
    int resultCode = ( (BigDecimal)output.get( "po_returncode" ) ).intValue();
    if ( resultCode != 0 )
    {
      errors.add( new ServiceError( ServiceErrorMessageKeys.AUDIENCE_PRECALCULATION_ERROR ) );
      throw new ServiceErrorExceptionWithRollback( errors );
    }
  }

  public boolean isParticipantInPublicRecognitionAudience( Promotion promotion, Participant participant )
  {
    PaxPromoEligibilityValueBean paxEligibility = (PaxPromoEligibilityValueBean)paxPromoEligibilityCache.get( participant.getId() );
    if ( null == paxEligibility )
    {
      paxEligibility = new PaxPromoEligibilityValueBean();
      paxPromoEligibilityCache.put( participant.getId(), paxEligibility );
    }
    if ( null == paxEligibility.isInPublicRecognitionAudience( participant.getId(), promotion.getId() ) )
    {
      Boolean status = isParticipantInPublicRecognitionAudienceCheck( promotion, participant );
      paxEligibility.setInPublicRecognigionAudience( participant.getId(), promotion.getId(), status );
    }
    return paxEligibility.isInPublicRecognitionAudience( participant.getId(), promotion.getId() );
  }

  private boolean isParticipantInPublicRecognitionAudienceCheck( Promotion promotion, Participant participant )
  {
    boolean inPublicRecogAudience = false;

    // if the user is inactive, there are not in the audience
    if ( !isParticipantActive( participant ) )
    {
      return false;
    }

    if ( promotion.getPublicRecognitionAudienceType() == null )
    {
      return false;
    }

    if ( promotion.getPublicRecognitionAudienceType().getCode().equals( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE ) )
    {
      inPublicRecogAudience = true;
    }

    else if ( promotion.getPublicRecognitionAudienceType().getCode().equals( PublicRecognitionAudienceType.CREATE_AUDIENCE_CODE ) )
    {
      RecognitionPromotion promo = (RecognitionPromotion)promotion;
      // web rules has own audiences set
      inPublicRecogAudience = isUserInPromotionAudiences( participant, promo.getPromotionPublicRecognitionAudiences() );
    }
    return inPublicRecogAudience;
  }

  public boolean isPromotionPrimaryAudienceInManagerNode( List<Node> nodes, Long promotionId )
  {
    return audienceDAO.isPromotionPrimaryAudienceInManagerNode( nodes, promotionId );
  }

  @Override
  public Map deleteAudience( Long audienceId )
  {
    return audienceDAO.deleteAudience( audienceId );
  }

  public AudienceRole updateAudienceRole( AudienceRole audienceRole )
  {
    AudienceRole audienceRoleToReturn = null;

    audienceRoleToReturn = audienceDAO.updateAudienceRole( audienceRole );

    return audienceRoleToReturn;
  }

  public List<Audience> getAudienceListFromAudienceRole( String roleCode )
  {
    return audienceDAO.getAudienceListFromAudienceRole( roleCode );
  }

  public AudienceRole deleteAllAudienceRoles()
  {
    AudienceRole role = new AudienceRole();

    List<AudienceRole> audienceRoleList = getAllAudienceRole();
    for ( Iterator iter = audienceRoleList.iterator(); iter.hasNext(); )
    {
      AudienceRole audienceRole = (AudienceRole)iter.next();
      audienceDAO.deleteAudienceRole( audienceRole );
    }
    return role;
  }

  public List<AudienceRole> getAllAudienceRole()
  {
    return audienceDAO.getAllAudienceRole();
  }

  @Override
  public String getAudienceNameById( Long audienceId )
  {
    return audienceDAO.getAudienceNameById( audienceId );
  }

  public boolean isUserInDIYCommAudience( Long userId, String roleCode )
  {
    return audienceDAO.isUserInDIYCommAudience( userId, roleCode );
  }

  @Override
  public Map<String, Object> getCriteriaAudienceActiveandInactive( Map<String, Object> inputParameters )
  {
    return audienceDAO.getCriteriaAudienceActiveandInactive( inputParameters );
  }

  private static AutoCompleteService getAutoCompleteService()
  {
    return (AutoCompleteService)BeanLocator.getBean( AutoCompleteService.BEAN_NAME );
  }

  @Override
  public List<AudienceListValueBean> getAudiencesForRosterSearchGroups( Long audienceId, String audienceName, String audienceType )
  {
    return audienceDAO.getAudiencesForRosterSearchGroups( audienceId, audienceName, audienceType );
  }

  @Override
  public List<User> getAllPersonsByAudienceId( Long audienceId )
  {
    List<Object> userIds = audienceDAO.getAllUsersByAudienceId( audienceId );

    List<User> users = new ArrayList<User>();

    userIds.forEach( val -> users.add( userService.getUserById( (Long)val ) ) );

    return users;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  // TODO : Half implementation will re-work later. Since audience will be taken care by DM.
  // TODO : Test case will be written, once resolve the DB dependency issue.
  @Override
  public Audience updateAudience( Long audienceId, String audienceName, String audienceType, boolean isGroupPublic )
  {
    Audience aud = null;
    try
    {
      // Update the audience name in cm.
      Audience oldAudience = audienceDAO.getAudienceById( audienceId );
      if ( !oldAudience.getName().equals( audienceName ) )
      {
        cmAssetService.updateCmsAudience( oldAudience.getName(), audienceName );
      }

      oldAudience.setName( audienceName );
      oldAudience.getAudienceType().setCode( audienceType );
      oldAudience.setPublicAudience( isGroupPublic );

      aud = audienceDAO.save( oldAudience );
    }
    catch( Exception e )
    {
      throw new RosterException( "Not able to update the group." );
    }
    return aud;
  }

  @Override
  public Long getAudienceIdByRosterAudienceId( UUID rosterAudienceId )
  {
    return audienceDAO.getAudienceIdByRosterAudienceId( rosterAudienceId );
  }

  @Override
  public UUID getRosterAudienceIdByAudienceId( Long audienceId )
  {
    return audienceDAO.getRosterAudienceIdByAudienceId( audienceId );
  }

  @Override
  public List<String> getRosterAudienceIdsByAudienceIds( List<Long> audienceIds )
  {
    return audienceDAO.getRosterAudienceIdsByAudienceIds( audienceIds );
  }

  @Override
  public List<AudienceDetail> getAudienceDetailsByUserId( Long userId )
  {
    return audienceDAO.getAudienceDetailsByUserId( userId );
  }

  @Override
  public Audience getAudienceByRosterAudienceId( UUID rosterAudienceId )
  {
    return this.audienceDAO.getAudienceByRosterAudienceId( rosterAudienceId );
  }

}

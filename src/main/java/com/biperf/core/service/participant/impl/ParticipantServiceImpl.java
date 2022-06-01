/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/impl/ParticipantServiceImpl.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.EmailValidator;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.dao.employer.EmployerDAO;
import com.biperf.core.dao.goalquest.PaxGoalDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.ParticipantGroupDAO;
import com.biperf.core.dao.participant.ParticipantSurveyDAO;
import com.biperf.core.dao.participant.UserFacebookDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PublicRecognitionDAO;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.ContactMethod;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.participant.ParticipantContactMethod;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.ParticipantGroup;
import com.biperf.core.domain.participant.ParticipantGroupDetails;
import com.biperf.core.domain.participant.ParticipantSearchListView;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.ParticipantFollowers;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserFacebook;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserTNCHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToChildrenAssociationRequest;
import com.biperf.core.service.hierarchy.NodeToUserNodesAssociationRequest;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.underarmour.UnderArmourService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.ui.user.UserForm;
import com.biperf.core.utils.AddressUtil;
import com.biperf.core.utils.BICollectionUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.CountryValueBean;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.ParticipantQuizClaimHistory;
import com.biperf.core.value.ParticipantRosterSearchValueBean;
import com.biperf.core.value.ParticipantUpdateProcessSummaryBean;
import com.biperf.core.value.PurlContributorValueBean;
import com.biperf.core.value.client.ClientPublicRecognitionDeptBean;
import com.biperf.core.value.hc.AccountSyncParticipantDetails;
import com.biperf.core.value.hc.AccountSyncRequest;
import com.biperf.core.value.hc.ParticipantSyncResponse;
import com.biperf.core.value.participant.ParticipantGroupList;
import com.biw.hc.core.service.HCServices;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * ParticipantServiceImpl.
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
 * <td>crosenquest</td>
 * <td>May 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantServiceImpl implements ParticipantService
{

  /**
   * Result set returned from the stored procedure
   */
  private static final String OUTPUT_RESULT_SET = "p_out_user_data";

  /**
   * Internal reference to the ParticipantDAO.
   */
  private ParticipantDAO participantDAO;
  private EmployerDAO employerDAO;
  private CharacteristicDAO characteristicDAO;
  private ClaimService claimService;
  private SystemVariableService systemVariableService;
  private AwardBanQServiceFactory awardBanQServiceFactory;
  private PromotionDAO promotionDAO;
  private NodeService nodeService;
  private ListBuilderService listBuilderService;
  private ActivityDAO activityDAO;
  private ClaimDAO claimDAO;
  private BudgetMasterDAO budgetDAO;
  private PaxGoalDAO paxGoalDAO;
  private UserService userService;
  private UserFacebookDAO userFacebookDAO;
  private PublicRecognitionDAO publicRecognitionDAO;
  private ParticipantSurveyDAO participantSurveyDAO;
  private ParticipantGroupDAO participantGroupDAO;

  private static final Log log = LogFactory.getLog( ParticipantServiceImpl.class );

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }
   
  public static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }
  
  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  private static final String DELETE_VALUE = "delete_option";

  public Participant saveParticipantWithTNCHistory( Participant participant, UserTNCHistory userTNCHistory ) throws ServiceErrorException
  {
    participantDAO.saveTNCHistory( userTNCHistory );
    saveParticipant( participant );
    return participant;

  }

  public void saveTNCHistory( UserTNCHistory userTNCHistory )
  {
    participantDAO.saveTNCHistory( userTNCHistory );
  }

  // tccc customization start
  public Participant saveParticipantUser( Participant participant )
  {
    return participantDAO.saveParticipant( participant );
  }

  // tccc customization start
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.participant.ParticipantService#saveParticipant(com.biperf.core.domain.participant.Participant)
   * @param participant
   * @return Participant
   * @throws ServiceErrorException
   */
  public Participant saveParticipant( Participant participant ) throws ServiceErrorException
  {
    boolean updateAwardbanQName = false;
    boolean updateAwardbanQStatus = false;
    if ( participant.getId() != null )
    {
      updateAwardbanQName = awardbanQNameCheck( participant, participant.getId() );
      updateAwardbanQStatus = awardbanQStatusCheck( participant, participant.getId() );
    }
    Participant savedPax = participantDAO.saveParticipant( participant );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQName || updateAwardbanQStatus )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( participant.getId() );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        log.error( " JsonGenerationException" + e );
      }
      catch( JsonMappingException e )
      {
        log.error( " JsonMappingException" + e );
      }
      catch( UniformInterfaceException e )
      {
        log.error( " UniformInterfaceException" + e );
      }
      catch( ClientHandlerException e )
      {
        log.error( " ClientHandlerException" + e );
      }
      catch( IOException e )
      {
        log.error( " IOException" + e );
      }

    }

    // If the participant is inactive, then the participant's node association's and budgets should
    // also be inactive.
    ParticipantStatus status = savedPax.getStatus();
    if ( status != null )
    {
      Boolean isActive = new Boolean( status.getCode().equals( ParticipantStatus.ACTIVE ) );
      for ( Iterator iter = savedPax.getUserNodes().iterator(); iter.hasNext(); )
      {
        UserNode userNode = (UserNode)iter.next();
        userNode.setActive( isActive );
      }
      if ( !isActive.booleanValue() )
      {
        // inactivate all budgets associated to this user.
        List budgets = budgetDAO.getAllBudgetsForUser( savedPax.getId() );
        for ( Iterator iter = budgets.iterator(); iter.hasNext(); )
        {
          Budget budget = (Budget)iter.next();
          budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.CLOSED ) );
          budgetDAO.updateBudget( budget );
        }
      }

      if ( !isActive.booleanValue() || participant.getOptOutOfProgram() )
      {

        UnderArmourService uaService = getUnderArmourService();

        try
        {
          if ( uaService.uaEnabled() )
          {
            if ( UserManager.getUserId() != null )// In Account activation screen, User Id is null
                                                  // which was throwing an exception
            {
              if ( uaService.isParticipantAuthorized( UserManager.getUserId() ) )
              {
                uaService.deAuthorizeParticipant( UserManager.getUserId() );
              }
            }

          }
        }
        catch( Exception e )
        {
          throw new ServiceErrorException( "Exception occured while de-activating UnderArmour account", e );
        }

      }

    }

    return savedPax;
  }

  public Participant updatePaxAuthorizeDate( Participant participant ) throws ServiceErrorException
  {
    Participant savedPax = participantDAO.saveParticipant( participant );
    return savedPax;
  }

  /**
   * awardbanQNameCheck
   * 
   * @param checkPax
   * @return true - if need to update awardbanQ name false - if do not need to update awardbanQ name
   */
  private boolean awardbanQNameCheck( Participant checkPax, Long paxId )
  {

    // If the name changed
    // and the logged in user is a Participant
    // and the pax has an awardbanq number,
    // then see if the name has changed at all -
    // if it has mark that we need to update the Awardbanq name also
    boolean updateAwardbanQName = false;

    Participant dbparticipant = participantDAO.getParticipantOverviewById( paxId );
    if ( dbparticipant != null )
    {
      if ( dbparticipant.getAwardBanqNumber() != null && !dbparticipant.getAwardBanqNumber().equals( "" ) )
      {
        if ( !nullCheckAndTrim( dbparticipant.getFirstName() ).equals( nullCheckAndTrim( checkPax.getFirstName() ) ) )
        {
          // first names are different
          updateAwardbanQName = true;
        }
        if ( !nullCheckAndTrim( dbparticipant.getMiddleName() ).equals( nullCheckAndTrim( checkPax.getMiddleName() ) ) )
        {
          // middle names are different
          updateAwardbanQName = true;
        }
        if ( !nullCheckAndTrim( dbparticipant.getLastName() ).equals( nullCheckAndTrim( checkPax.getLastName() ) ) )
        {
          // last names are different
          updateAwardbanQName = true;
        }
      }
    }

    return updateAwardbanQName;
  }

  /**
   * awardbanQStatusCheck
   * @return true - if need to update awardbanQ status false - if do not need to update awardbanQ status
   */
  private boolean awardbanQStatusCheck( Participant checkPax, Long paxId )
  {
    // If the participant is enrolled in bank and their status has changed, then we need to update
    // bank services
    boolean updateNeeded = false;

    Participant dbparticipant = participantDAO.getParticipantOverviewById( paxId );
    if ( dbparticipant != null )
    {
      if ( dbparticipant.getAwardBanqNumber() != null && !dbparticipant.getAwardBanqNumber().equals( "" ) )
      {
        if ( !nullCheckEquals( dbparticipant.getStatus(), checkPax.getStatus() ) )
        {
          updateNeeded = true;
        }
      }
    }

    return updateNeeded;
  }

  /**
   * nullCheckAndTrim
   * 
   * @param checkString
   * @return If string is null, will make it an empty string, then it will trim the string
   */
  private String nullCheckAndTrim( String checkString )
  {
    if ( checkString == null )
    {
      checkString = "";
    }
    else
    {
      checkString = checkString.trim();
    }
    return checkString;
  }

  /**
   * @return True only if both are null, or both are equal via equals()
   */
  private boolean nullCheckEquals( Object first, Object second )
  {
    if ( first == null && second == null )
    {
      return true;
    }
    else if ( first == null || second == null )
    {
      return false;
    }
    else
    {
      return first.equals( second );
    }
  }

  /**
   * Get all participants.
   * 
   * @return a list of all participants as a <code>List</code> of {@link Participant} objects.
   */
  public List getAll()
  {
    return this.participantDAO.getAll();
  }

  /**
   * Get all ACTIVE participants.
   * 
   * @return a list of all ACTIVE participants as a <code>List</code> of {@link Participant}
   *         objects.
   */
  public List getAllActive()
  {
    return this.participantDAO.getAllActive();
  }

  public List<Long> getAllActivePaxIds()
  {
    return this.participantDAO.getAllActivePaxIds();
  }

  public List<Long> getAllPaxIds()
  {
    return this.participantDAO.getAllPaxIds();
  }

  /**
   * Get all ACTIVE participants Welcome Email not sent.
   * 
   * @return a list of all ACTIVE participants with email not send already as a <code>List</code> of {@link Participant}
   *         objects.
   */
  public List getAllActiveAndWelcomeEmailNotSent()
  {
    return this.participantDAO.getAllActiveAndWelcomeEmailNotSent();
  }

  public List updateAllActiveAllowEstatements( boolean allowEstatements )
  {
    List activeParticipants = this.participantDAO.getAllActive();
    Iterator activeParticipantsIter = activeParticipants.iterator();

    while ( activeParticipantsIter.hasNext() )
    {
      boolean modified = false;

      Participant activeParticipant = (Participant)activeParticipantsIter.next();
      Set participantCommPrefs = activeParticipant.getParticipantCommunicationPreferences();

      // get the primary address for the participant, if pax is international, do not
      // create an estatement participant communication preference.
      boolean isInternational = false;
      Set paxAddresses = activeParticipant.getUserAddresses();
      for ( Iterator paxAddressIter = paxAddresses.iterator(); paxAddressIter.hasNext(); )
      {
        UserAddress userAddress = (UserAddress)paxAddressIter.next();
        if ( userAddress.isPrimary() )
        {
          // check the country code from AddressUtil to see if it's international. bug 14204
          if ( !AddressUtil.isSupplierBIBank( userAddress.getAddress().getCountry() ) )
          {
            isInternational = true;
          }
        }
      }

      // if empty we know we have to add it if allowEstatements
      if ( allowEstatements && participantCommPrefs.isEmpty() && !isInternational )
      {
        ParticipantCommunicationPreference participantCommPref = new ParticipantCommunicationPreference();
        participantCommPref.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.E_STATEMENTS ) );
        activeParticipant.addParticipantCommunicationPreference( participantCommPref );
        modified = true;
      }

      // otherwise we have to look around
      else if ( !participantCommPrefs.isEmpty() )
      {

        // iterate through commPrefs and see if estatements is there
        Iterator participantCommPrefsIter = participantCommPrefs.iterator();
        boolean found = false;
        while ( !found && participantCommPrefsIter.hasNext() )
        {

          ParticipantCommunicationPreference participantCommPref = (ParticipantCommunicationPreference)participantCommPrefsIter.next();
          if ( participantCommPref.getParticipantPreferenceCommunicationsType().getCode().equals( ParticipantPreferenceCommunicationsType.E_STATEMENTS ) )
          {

            // if we don't allow estatements we want to remove it
            if ( !allowEstatements )
            {
              participantCommPrefs.remove( participantCommPref );
              modified = true;
            }

            found = true;
          }
        }

        // if not found and allowEstatements and US or Canada resident, then we add
        if ( !found && allowEstatements && !isInternational )
        {
          ParticipantCommunicationPreference participantCommPref = new ParticipantCommunicationPreference();
          participantCommPref.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.E_STATEMENTS ) );
          activeParticipant.addParticipantCommunicationPreference( participantCommPref );
          modified = true;
        }
      }

      if ( modified )
      {
        this.participantDAO.saveParticipant( activeParticipant );
      }

    }

    return activeParticipants;

  }

  /**
   * Get all ACTIVE participants with associations.
   * 
   * @param associationRequests
   * @return a list of all ACTIVE participants as a <code>List</code> of {@link Participant}
   *         objects.
   */
  @SuppressWarnings( "unchecked" )
  public List<Participant> getAllActiveWithAssociations( AssociationRequestCollection associationRequests )
  {
    List<Participant> paxs = this.participantDAO.getAllActive();
    for ( Iterator<Participant> iter = paxs.iterator(); iter.hasNext(); )
    {
      Participant pax = iter.next();
      for ( Iterator<AssociationRequest> iterator = associationRequests.iterator(); iterator.hasNext(); )
      {
        AssociationRequest req = iterator.next();
        req.execute( pax );
      }
    }
    return paxs;
  }

  /**
   * Get the participant from the database through DAO using the ID. Overridden from
   * 
   * @see com.biperf.core.service.participant.ParticipantService#getParticipantById(java.lang.Long)
   * @param id
   * @return Participant
   */
  public Participant getParticipantById( Long id )
  {
    return this.participantDAO.getParticipantById( id );
  }

  /**
   * Get the runtime proxy of participant
   * 
   * @param id
   * @return Participant
   */
  public Participant getParticipantProxyById( Long id )
  {
    return this.participantDAO.getParticipantProxyById( id );
  }

  /**
   * Get the participant (with associations) from the database through DAO using the ID. Overridden
   * from
   * 
   * @see com.biperf.core.service.participant.ParticipantService#getParticipantById(java.lang.Long)
   * @param id
   * @param associationRequests
   * @return Participant
   */
  public Participant getParticipantByIdWithAssociations( Long id, AssociationRequestCollection associationRequests )
  {
    Participant pax = this.participantDAO.getParticipantById( id );
    for ( Iterator iterator = associationRequests.iterator(); iterator.hasNext(); )
    {
      AssociationRequest req = (AssociationRequest)iterator.next();
      req.execute( pax );
    }
    return pax;
  }

  /**
   * Get the participant from the database through DAO using the userName. Overridden from
   * 
   * @see com.biperf.core.service.participant.ParticipantService#getParticipantByUserName(java.lang.String)
   * @param userName
   * @return Participant
   */
  public Participant getParticipantByUserName( String userName )
  {
    return this.participantDAO.getParticipantByUserName( userName );
  }

  public Participant getParticipantByUserNameWithAssociations( String userName, AssociationRequestCollection associationRequests )
  {
    Participant pax = this.participantDAO.getParticipantByUserName( userName );
    for ( Iterator iterator = associationRequests.iterator(); iterator.hasNext(); )
    {
      AssociationRequest req = (AssociationRequest)iterator.next();
      req.execute( pax );
    }
    return pax;
  }

  /**
   * Get the participant from the database through DAO using the ssn. Overridden from
   * 
   * @see com.biperf.core.service.participant.ParticipantService#getParticipantBySSN(java.lang.String)
   * @param ssn
   * @return Participant
   */
  public Participant getParticipantBySSN( String ssn )
  {
    return this.participantDAO.getParticipantBySSN( ssn );
  }

  /**
   * Get a list of participants through the DAO using the search criteria params. Overridden from
   * 
   * @param searchCriteria
   * @return List
   */
  public List searchParticipant( ParticipantSearchCriteria searchCriteria )
  {
    return this.participantDAO.searchParticipant( searchCriteria );
  }

  /**
   * Get List of Country information with Participant Ids
   * 
   * @param ids
   */
  public List populateCountriesForUsers( Long[] ids )
  {
    return this.participantDAO.populateCountriesForUsers( ids );
  }

  /**
   * Get a list of participants through the DAO using the search criteria params. Overridden from
   * 
   * @param searchCriteria
   * @return List
   */
  public List searchParticipant( ParticipantSearchCriteria searchCriteria, boolean isCriteriaStartingWith )
  {
    return this.participantDAO.searchParticipant( searchCriteria, isCriteriaStartingWith );
  }

  /**
   * Get a list of participants through the DAO using the search criteria params. Overridden from
   * 
   * @param searchCriteria
   * @return List
   */
  public List<String> searchCriteriaAutoComplete( ParticipantSearchCriteria searchCriteria )
  {
    return this.participantDAO.searchCriteriaAutoComplete( searchCriteria );
  }

  /**
   * 
   * {@inheritDoc}
   */
  public List<ParticipantSearchView> getParticipatForMiniProfile( Long participantId )
  {
    List<ParticipantSearchView> paxList = this.participantDAO.getParticipatForMiniProfile( participantId );

    return paxList;
  }

  /**
   * 
   * {@inheritDoc}
   */
  public List<ParticipantSearchView> getParticipatForMiniProfile( String participantIds )
  {
    return this.participantDAO.getParticipatForMiniProfile( participantIds );
  }

  /**
   * Get a list of participants through the DAO using the search criteria params. Overridden from
   * 
   * @param searchCriteria
   * @return List
   */
  public List<Participant> searchParticipants( ParticipantSearchCriteria searchCriteria )
  {
    return this.participantDAO.searchParticipants( searchCriteria );
  }

  /**
   * @param criteria
   * @return pax count
   */
  public Long getPaxCountBasedOnCriteria( ParticipantSearchCriteria criteria )
  {
    return this.participantDAO.getPaxCountBasedOnCriteria( criteria );
  }

  /**
   * Get a list of participants through the DAO using the search criteria params. Overridden from
   * 
   * @param searchCriteria
   * @param associationRequests 
   * @return List
   */
  public List searchParticipantWithAssociations( ParticipantSearchCriteria searchCriteria, boolean isCriteriaStartingWith, AssociationRequestCollection associationRequests )
  {
    List paxs = this.participantDAO.searchParticipant( searchCriteria, isCriteriaStartingWith );
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Participant pax = (Participant)iter.next();
      for ( Iterator iterator = associationRequests.iterator(); iterator.hasNext(); )
      {
        AssociationRequest req = (AssociationRequest)iterator.next();
        req.execute( pax );
      }
    }
    return paxs;
  }

  public Long getPaxCountBasedOnEmailCriteria( ParticipantSearchCriteria paxCriteria )
  {
    return this.participantDAO.getPaxCountBasedOnEmailCriteria( paxCriteria );
  }

  /**
   * Return the current ParticipantEmployment object. This is the record with either a null, or the
   * latest termination date. Overridden from
   * 
   * @see ParticipantService#getCurrentParticipantEmployer(Long)
   * @param id
   * @return ParticipantEmployer
   */
  public ParticipantEmployer getCurrentParticipantEmployer( Long id )
  {
    // Replacing paxOverview call with specific associations
    // Participant participant = getParticipantOverviewById( id );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant participant = getParticipantByIdWithAssociations( id, associationRequestCollection );

    return getCurrentParticipantEmployer( participant );
  }

  /**
   * 
   * {@inheritDoc}
   */
  public Participant saveParticipantEmployer( Long participantId, ParticipantEmployer participantEmployer, UserForm userForm )
  {
    // Get the participant by id.
    Participant participant = this.participantDAO.getParticipantById( participantId );
    participantEmployer.setEmployer( this.employerDAO.getEmployerById( participantEmployer.getEmployer().getId() ) );
    participantEmployer.setParticipant( participant );
    participantEmployer.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );
    participantEmployer.getAuditUpdateInfo().setModifiedBy( UserManager.getUserId() );

    // we want to remove the last record since we are adding it in the saveParticipantEmployer
    // method
    participant.getParticipantEmployers().remove( participant.getParticipantEmployers().size() - 1 );
    saveParticipantEmployer( participant, participantEmployer, false );
    // return the saved participant
    return participant;
  }

  /**
   * Saves the association between Participant and Employer. Overridden from
   * 
   * @see com.biperf.core.service.participant.ParticipantService#saveParticipantEmployer(Long,
   *      com.biperf.core.domain.participant.ParticipantEmployer)
   * @param participantId
   * @param participantEmployer
   * @return Participant
   */
  public Participant saveParticipantEmployer( Long participantId, ParticipantEmployer participantEmployer )
  {

    // Get the participant by id.
    Participant participant = this.participantDAO.getParticipantById( participantId );
    participantEmployer.setEmployer( this.employerDAO.getEmployerById( participantEmployer.getEmployer().getId() ) );
    participantEmployer.setParticipant( participant );

    saveParticipantEmployer( participant, participantEmployer, true );
    // return the saved participant
    return participant;
  }

  /**
   * Saves the association between Participant and Employer and sets the termination date of the
   * previously current employment record.
   * 
   * @param participantId
   * @param participantEmployer
   * @param previousTerminationDate
   * @return Participant
   */
  public Participant saveParticipantEmployer( Long participantId, ParticipantEmployer participantEmployer, Date previousTerminationDate )
  {
    // Replacing paxOverview call with specific associations
    // Participant participant = getParticipantOverviewById( participantId );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant participant = getParticipantByIdWithAssociations( participantId, associationRequestCollection );

    participantEmployer.setEmployer( this.employerDAO.getEmployerById( participantEmployer.getEmployer().getId() ) );
    participantEmployer.setParticipant( participant );

    ParticipantEmployer currentParticpantEmployer = getCurrentParticipantEmployer( participant );
    if ( currentParticpantEmployer != null )
    {
      // remove the existing current employer
      participant.getParticipantEmployers().remove( participant.getParticipantEmployers().size() - 1 );
      currentParticpantEmployer.setTerminationDate( previousTerminationDate );
      saveParticipantEmployer( participant, currentParticpantEmployer, true );
    }
    return saveParticipantEmployer( participant, participantEmployer, false );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.participant.ParticipantService#createFullParticipant(com.biperf.core.domain.participant.Participant)
   * @param participant
   * @return Participant
   * @throws ServiceErrorException
   */
  public Participant createFullParticipant( Participant participant ) throws ServiceErrorException
  {
    // Check to see if the user already exists in the database.
    User dbUser = userService.getUserByUserName( participant.getUserName() );
    if ( dbUser != null )
    {
      // if we found a record in the database with the given username, we are trying to insert a
      // duplicate record.
      throw new ServiceErrorException( "user.error.UNIQUE_CONSTRAINT" );
    }

    // get the children and hold onto them.
    Set addresses = participant.getUserAddresses();
    Set phones = participant.getUserPhones();
    Set emailAddresses = participant.getUserEmailAddresses();
    Set characteristicsNonDomain = participant.getUserCharacteristics();
    List employmentsNonDomain = participant.getParticipantEmployers();
    Set userNodesNonDomain = participant.getUserNodes();
    // Set contactMethods = participant.getParticipantContactMethods();

    // clear out children for clean insert of participant
    participant.setUserAddresses( new LinkedHashSet() );
    participant.setUserPhones( new LinkedHashSet() );
    participant.setUserEmailAddresses( new LinkedHashSet() );
    participant.setUserCharacteristics( new LinkedHashSet() );
    participant.setParticipantEmployers( new ArrayList() );
    participant.setUserNodes( new LinkedHashSet() );

    // Check system variable to see if this participant should be forced to change their
    // password on the first login.
    PropertySetItem forceReset = getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_FORCE_RESET );
    participant.setForcePasswordChange( forceReset.getBooleanVal() );

    // TODO add a default role of Participant

    Iterator addressIterator = addresses.iterator();
    while ( addressIterator.hasNext() )
    {
      participant.addUserAddress( (UserAddress)addressIterator.next() );
    }

    Iterator phoneIterator = phones.iterator();
    while ( phoneIterator.hasNext() )
    {
      participant.addUserPhone( (UserPhone)phoneIterator.next() );
    }

    Iterator emailIterator = emailAddresses.iterator();
    while ( emailIterator.hasNext() )
    {
      participant.addUserEmailAddress( (UserEmailAddress)emailIterator.next() );
    }

    Iterator charIterator = characteristicsNonDomain.iterator();
    while ( charIterator.hasNext() )
    {
      UserCharacteristic userCharacteristic = (UserCharacteristic)charIterator.next();
      UserCharacteristicType characteristicById = (UserCharacteristicType)characteristicDAO.getCharacteristicById( userCharacteristic.getUserCharacteristicType().getId() );
      userCharacteristic.setUserCharacteristicType( characteristicById );
      userCharacteristic.setId( null );
      userCharacteristic.setVersion( null );
      if ( !userCharacteristic.getCharacteristicValue().equals( DELETE_VALUE ) && !userCharacteristic.getCharacteristicValue().equals( "" ) )
      {
        participant.addUserCharacteristic( userCharacteristic );
      }
    }

    Iterator employmentIterator = employmentsNonDomain.iterator();
    while ( employmentIterator.hasNext() )
    {
      ParticipantEmployer participantEmployer = (ParticipantEmployer)employmentIterator.next();
      participantEmployer.setEmployer( employerDAO.getEmployerById( participantEmployer.getEmployer().getId() ) );
      participant.addParticipantEmployer( participantEmployer );
    }

    Iterator nodeIterator = userNodesNonDomain.iterator();
    while ( nodeIterator.hasNext() )
    {
      UserNode userNode = (UserNode)nodeIterator.next();
      Node node = nodeService.getNodeByNameAndHierarchy( userNode.getNode().getName(), userNode.getNode().getHierarchy() );
      if ( node == null )
      {
        List serviceErrors = new ArrayList();
        serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.NODE_BY_NAME_NOT_FOUND, userNode.getNode().getName() ) );
        throw new ServiceErrorExceptionWithRollback( serviceErrors );
      }
      userNode.setNode( node );
      participant.addUserNode( userNode );
    }

    // Create a default participant contact method for the participant.
    ParticipantContactMethod paxContactMethod = new ParticipantContactMethod();
    paxContactMethod.setAuditCreateInfo( participant.getAuditCreateInfo() );
    paxContactMethod.setContactMethodCode( ContactMethod.lookup( "email" ) );
    paxContactMethod.setPrimary( Boolean.valueOf( true ) );
    paxContactMethod.setParticipant( participant );
    participant.addParticipantContactMethod( paxContactMethod );

    return participantDAO.saveParticipant( participant );
  }

  /**
   * Overridden from
   * 
   * @param id
   * @return Participant (fully hydrated)
   * NOTE: Only use this if you need fully hydrated Participant object. 
   * Otherwise use getParticipantByIdWithAssociations
   */
  public Participant getParticipantOverviewById( Long id )
  {
    return participantDAO.getParticipantOverviewById( id );
  }

  public void setCharacteristicDAO( CharacteristicDAO characteristicDAO )
  {
    this.characteristicDAO = characteristicDAO;
  }

  public void setEmployerDAO( EmployerDAO employerDAO )
  {
    this.employerDAO = employerDAO;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  /**
   * Save a single ParticipantEmployer record. All relationships are hydrated at this point.
   * 
   * @param participant
   * @param participantEmployer
   * @return Participant
   */
  private Participant saveParticipantEmployer( Participant participant, ParticipantEmployer participantEmployer, boolean updateCurrent )
  {
    // Keep track of these so we can compare them after update logic to see if they have changed -
    // changes need to be sent to bank
    Participant dbParticipant = getParticipantById( participant.getId() );
    ParticipantStatus originalStatus = dbParticipant.getStatus();
    Date originalStatusChangeDate = dbParticipant.getStatusChangeDate();
    String originalDepartmentType = dbParticipant.getDepartmentType();

    // Update department type on the participant, so that the change is immediately visible.
    // It is a transient field based on the employer table
    if ( participantEmployer.getDepartmentType() != null )
    {
      participant.setDepartmentType( participantEmployer.getDepartmentType() );
    }

    if ( updateCurrent && participant.getParticipantEmployers().size() > 0 )
    {
      // removed the below statement as part of task G6-183
      // participant.getParticipantEmployers().remove( participant.getParticipantEmployers().size()
      // - 1 );
      // We have to set the audit update info here since Hibernate does not treat the
      // participantEmployer as an entity.
      // Hence, the HibernateAuditInterceptor does not know about it.
      participantEmployer.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );
      participantEmployer.getAuditUpdateInfo().setModifiedBy( UserManager.getUserId() );

      // when updating the participant info then set the last record termination date as today
      ParticipantEmployer previousRecord = (ParticipantEmployer)participant.getParticipantEmployers().get( participant.getParticipantEmployers().size() - 1 );
      previousRecord.setTerminationDate( new Date() );
      participant.getParticipantEmployers().remove( participant.getParticipantEmployers().size() - 1 );
      participant.addParticipantEmployer( previousRecord );
    }
    participant.addParticipantEmployer( participantEmployer );

    // If the pax has a termination date on his/her most current employer then
    // set the pax status to inactive
    // NOTE: Per BA even if the termination date is a future date, ok to set the
    // pax status to inactive right away for which we will redirect them to awardlinQ at login!
    ParticipantEmployer currentEmployer = this.getCurrentParticipantEmployer( participant );
    if ( currentEmployer != null && currentEmployer.getTerminationDate() != null && currentEmployer.getTerminationDate().before( DateUtils.getCurrentDate() ) )
    {
      // inactivating pax
      if ( isTermsAndConditionsUsed() ) // if tc=true, really inactive user = inactive
      {
        participant.setStatus( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ) );
        participant.setStatusChangeDate( DateUtils.getCurrentDate() );
      }
      else
      // if tc=false, really inactive user = inactive,notaccepted
      {
        participant.setStatus( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ) );
        participant.setStatusChangeDate( DateUtils.getCurrentDate() );
      }
      // Code Fix to bug#17271 added on 01082007
      userService.updateUserNodeStatus( participant.getId(), Boolean.FALSE );
    }

    // In the case previous employment associations have termination dates
    // but the latest employment doesn't, reset pax to Active
    if ( currentEmployer != null && ( currentEmployer.getTerminationDate() == null || !currentEmployer.getTerminationDate().before( DateUtils.getCurrentDate() ) ) )
    {
      // activating pax
      if ( isTermsAndConditionsUsed() ) // if tc=true, active user = inactive,notaccepted
      {
        participant.setStatus( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ) );
        participant.setStatusChangeDate( DateUtils.getCurrentDate() );
      }
      else
      // if tc=false, active user = active,notaccepted
      {
        participant.setStatus( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) );
        participant.setStatusChangeDate( DateUtils.getCurrentDate() );
      }

      userService.updateUserNodeStatus( participant.getId(), Boolean.TRUE );
    }

    // Update particpant termination date to match employer termination date
    if ( currentEmployer == null )
    {
      participant.setTerminationDate( null );
    }
    else
    {
      participant.setTerminationDate( currentEmployer.getTerminationDate() );
    }

    Participant savedPax = participantDAO.saveParticipant( participant );

    // See if awardbanq needs to be updated, and update it
    boolean updateBank = false;
    if ( savedPax.getAwardBanqNumber() != null && !savedPax.getAwardBanqNumber().equals( "" ) )
    {
      if ( !nullCheckEquals( originalStatus, savedPax.getStatus() ) )
      {
        updateBank = true;
      }
      if ( !nullCheckEquals( originalStatusChangeDate, savedPax.getStatusChangeDate() ) )
      {
        updateBank = true;
      }
      if ( !nullCheckEquals( originalDepartmentType, participantEmployer.getDepartmentType() ) )
      {
        updateBank = true;
      }
    }

    if ( updateBank )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( participant.getId() );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( Exception e )
      {
        log.error( " Exception" + e );
      }
    }

    return savedPax;
  }

  /**
   * Get the current ParticipantEmployer record of the Participant. The last item in the list is
   * always the current.
   * 
   * @param participant
   * @return ParticipantEmployer
   */
  public ParticipantEmployer getCurrentParticipantEmployer( Participant participant )
  {
    if ( participant.getParticipantEmployers().size() == 0 )
    {
      return null;
    }
    return (ParticipantEmployer)participant.getParticipantEmployers().get( participant.getParticipantEmployers().size() - 1 );
  }

  /**
   * Updates the participant preferences on the participant (id) passed in. Overridden from
   * 
   * @see com.biperf.core.service.participant.ParticipantService#updateParticipantPreferences(java.lang.Long,
   *      java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String,
   *      com.biperf.core.domain.user.UserEmailAddress,
   *      com.biperf.core.domain.user.UserEmailAddress)
   * @param participantId
   * @param contactMethodTypes
   * @param activeTextMessages
   * @param contactMethods
   * @param language
   * @param emailAddress
   * @param textMessageAddress
   */
  // Client customizations for wip #26532 starts
  public void updateParticipantPreferences( Long participantId,
                                            String[] contactMethodTypes,
                                            String[] activeTextMessages,
                                            String[] contactMethods,
                                            String language,
                                            UserEmailAddress emailAddress,
                                            UserEmailAddress textMessageAddress,
                                            boolean allowPublicRecognition,
                                            boolean allowPublicInformation,
                                            boolean allowPublicBirthDate,
                                            boolean allowPublicHireDate,
                                            boolean allowSharePurlToOutsiders,
                                            boolean allowPurlContributionsToSeeOthers )
  {

    Participant participant = this.participantDAO.getParticipantById( participantId );
    participant.setAllowPublicRecognition( allowPublicRecognition );
    participant.setAllowPublicInformation( allowPublicInformation );
    participant.setAllowPublicBirthDate( allowPublicBirthDate );
    participant.setAllowPublicHireDate( allowPublicHireDate );
    participant.setAllowSharePurlToOutsiders( allowSharePurlToOutsiders );
    participant.setAllowPurlContributionsToSeeOthers( allowPurlContributionsToSeeOthers );;
    // Client customizations for wip #26532 ends

    // QC BUG #3173. if user selected "No. Do not display my Public Profile." radio button in
    // preferences page then remove all participant who are following this user.
    if ( !allowPublicInformation )
    {
      List<ParticipantFollowers> peopleFollowingMe = this.participantDAO.getParticipantsWhoAreFollowingMe( participantId );
      for ( ParticipantFollowers pf : peopleFollowingMe )
      {
        try
        {
          getParticipantDAO().removeParticipantFollowee( pf );
        }
        catch( ServiceErrorException e )
        {
          log.error( "<<Error>>" + e );
        }
      }
    }

    // ***EMAIL ADDRESS
    if ( emailAddress != null )
    {
      participant.addUserEmailAddress( emailAddress );
    }

    // ***TEXT MESSAGE ADDRESS
    if ( textMessageAddress != null )
    {
      UserEmailAddress oldTextMessageAddress = participant.getTextMessageAddress();
      if ( oldTextMessageAddress != null )
      {
        // The following line is commented to fix Bug# 25898
        // textMessageAddress.setIsPrimary( oldTextMessageAddress.getIsPrimary() );
        participant.getUserEmailAddresses().remove( oldTextMessageAddress );
      }
      participant.addUserEmailAddress( textMessageAddress );
    }
    else
    {
      UserEmailAddress oldTextMessageAddress = participant.getTextMessageAddress();
      if ( oldTextMessageAddress != null )
      {
        participant.getUserEmailAddresses().remove( oldTextMessageAddress );
      }
    }

    // ***LANGUAGE
    participant.setLanguageType( LanguageType.lookup( language ) );

    // ***CONTACT PREFERENCE TYPES (eStatments, text messages)
    Set existingEStatemens = participant.getParticipantCommunicationPreferences();

    for ( Iterator iter = existingEStatemens.iterator(); iter.hasNext(); )
    {
      iter.next();
      iter.remove();
    }

    HibernateSessionManager.getSession().flush();

    // Create a list of selected ContactMethodTypes
    // LinkedHashSet contactPreferences = new LinkedHashSet();
    if ( contactMethodTypes != null )
    {
      for ( int i = 0; i < contactMethodTypes.length; i++ )
      {
        String code = contactMethodTypes[i];
        ParticipantCommunicationPreference preference = new ParticipantCommunicationPreference();
        preference.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( code ) );
        participant.addParticipantCommunicationPreference( preference );
      }
    }

    // Create a list of selected (active text message) ContactMethodTypes
    if ( activeTextMessages != null )
    {
      for ( int i = 0; i < activeTextMessages.length; i++ )
      {
        String smsGroupType = activeTextMessages[i];
        ParticipantCommunicationPreference preference = new ParticipantCommunicationPreference();
        preference.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) );
        preference.setMessageSMSGroupType( MessageSMSGroupType.lookup( smsGroupType ) );
        participant.addParticipantCommunicationPreference( preference );
      }
    }

    // ***CONTACT METHODS (email, phone, fax, postal mail)
    // First Loop through the String[] contactMethods and create domian objects
    ArrayList newContactMethods = new ArrayList();
    if ( contactMethods != null )
    {
      for ( int i = 0; i < contactMethods.length; i++ )
      {
        if ( contactMethods[i] != null )
        {
          ParticipantContactMethod paxContactMethod = new ParticipantContactMethod();
          paxContactMethod.setParticipant( participant );
          paxContactMethod.setContactMethodCode( ContactMethod.lookup( contactMethods[i] ) );
          paxContactMethod.setAuditCreateInfo( participant.getAuditCreateInfo() );
          paxContactMethod.setPrimary( Boolean.valueOf( false ) );
          newContactMethods.add( paxContactMethod );
        }
      }

    }

    Set existingContactMethods = participant.getParticipantContactMethods();

    for ( Iterator iter = existingContactMethods.iterator(); iter.hasNext(); )
    {
      ParticipantContactMethod paxPreference = (ParticipantContactMethod)iter.next();
      if ( !newContactMethods.contains( paxPreference ) )
      {
        iter.remove();
      }
    }

    for ( Iterator newContactMethodsIter = newContactMethods.iterator(); newContactMethodsIter.hasNext(); )
    {

      ParticipantContactMethod contactMethod2 = (ParticipantContactMethod)newContactMethodsIter.next();

      participant.addParticipantContactMethod( contactMethod2 );

    }

  }

  public void updateTextMessagePreferences( Long userId, String[] activeTextMessages )
  {
    Participant participant = this.participantDAO.getParticipantById( userId );

    // remove all of the existing text messages
    for ( Iterator iter = participant.getParticipantCommunicationPreferences().iterator(); iter.hasNext(); )
    {
      ParticipantCommunicationPreference pcp = (ParticipantCommunicationPreference)iter.next();
      if ( pcp.getParticipantPreferenceCommunicationsType().equals( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) ) )
      {
        iter.remove();
      }
    }

    // now add the new ones
    if ( activeTextMessages != null )
    {
      for ( int i = 0; i < activeTextMessages.length; i++ )
      {
        String smsGroupType = activeTextMessages[i];
        ParticipantCommunicationPreference preference = new ParticipantCommunicationPreference();
        preference.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) );
        preference.setMessageSMSGroupType( MessageSMSGroupType.lookup( smsGroupType ) );
        participant.addParticipantCommunicationPreference( preference );
      }
    }
  }

  /**
   * Get all ACTIVE participants that are NOT enrolled in the awardBanq system.
   * 
   * @return a list of all ACTIVE participants NOT enrolled in the awardBanq system as a
   *         <code>List</code> of user ids.
   */
  public List getAllParticipantsNotInBanqSystem()
  {
    return this.participantDAO.getAllActivePaxNotEnrolledInBanqSystem();
  }

  /**
   * Overridden from
   * 
   * @param participantPreferenceCommunicationsType
   * @param associationRequestCollection
   * @return List
   */
  public List getAllActivePaxWithCommunicationPreferenceInCampaign( String campaignNumber,
                                                                    ParticipantPreferenceCommunicationsType participantPreferenceCommunicationsType,
                                                                    AssociationRequestCollection associationRequestCollection,
                                                                    int pageNumber,
                                                                    int pageSize )
  {
    return participantDAO.getAllActivePaxWithCommunicationPreferenceInCampaign( campaignNumber, participantPreferenceCommunicationsType, associationRequestCollection, pageNumber, pageSize );
  }

  public Set getAllEligiblePaxForPromotion( Long promoId, boolean primaryAudience )
  {
    Set paxs = new HashSet();
    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_AUDIENCES_WITH_PAXS ) );
    reqCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );
    Promotion promo = promotionDAO.getPromotionByIdWithAssociations( promoId, reqCollection );
    if ( primaryAudience )
    {
      if ( isAllActivePax( promo, true ) )
      {
        paxs.addAll( getAllActive() );
      }
      else
      {
        Set primaryAudiences = new HashSet();
        if ( promo.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ENTIRE_PARENT_AUDIENCE_CODE ) )
        {
          primaryAudiences.addAll( ( (ProductClaimPromotion)promo ).getParentPromotion().getPromotionPrimaryAudiences() );
        }
        else
        {
          primaryAudiences.addAll( promo.getPromotionPrimaryAudiences() );
        }
        for ( Iterator iter = primaryAudiences.iterator(); iter.hasNext(); )
        {
          PromotionAudience temp = (PromotionAudience)iter.next();
          Audience audience = temp.getAudience();
          if ( audience instanceof PaxAudience )
          {
            for ( Iterator iterator = ( (PaxAudience)audience ).getAudienceParticipants().iterator(); iterator.hasNext(); )
            {
              AudienceParticipant audPax = (AudienceParticipant)iterator.next();
              // Add only ACTIVE pax
              if ( audPax.getParticipant().isActive().booleanValue() )
              {
                paxs.add( audPax.getParticipant() );
              }
            }
          }
          else
          {
            // paxs.addAll( listBuilderService.searchParticipants( ( (CriteriaAudience)audience ),
            // null, false, new HashSet(), true, true ) );
            paxs.addAll( listBuilderService.getCriteriaAudienceParticipants( audience.getId(), true ) ); // Bug
                                                                                                         // 64164
          }
        }
      }
    }
    else
    {
      if ( isAllActivePax( promo, false ) )
      {
        paxs.addAll( getAllActive() );
      }
      else
      {
        if ( promo.getSecondaryAudienceType() != null && promo.getSecondaryAudienceType().getCode().equals( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) )
        {
          for ( Iterator iter = promo.getPromotionSecondaryAudiences().iterator(); iter.hasNext(); )
          {
            PromotionAudience temp = (PromotionAudience)iter.next();
            Audience audience = temp.getAudience();
            if ( audience instanceof PaxAudience )
            {
              for ( Iterator iterator = ( (PaxAudience)audience ).getAudienceParticipants().iterator(); iterator.hasNext(); )
              {
                AudienceParticipant audPax = (AudienceParticipant)iterator.next();
                // Add only ACTIVE pax
                if ( audPax.getParticipant().isActive().booleanValue() )
                {
                  paxs.add( audPax.getParticipant() );
                }
              }
            }
            else
            {
              paxs.addAll( listBuilderService.searchParticipants( (CriteriaAudience)audience, null, false, new HashSet(), true, true ) );
            }
          }
        }
        else if ( promo.getSecondaryAudienceType() != null && promo.getSecondaryAudienceType().getCode().equals( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) )
        {
          if ( !isAllActivePax( promo, true ) )
          {
            Set nodes = getNodesForPaxs( paxs );
            paxs.addAll( getUsersInNodes( nodes, true ) );
          }
          else
          {
            paxs.addAll( getAllActive() );
          }
        }
        else if ( promo.getSecondaryAudienceType() != null && promo.getSecondaryAudienceType().getCode().equals( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) )
        {
          if ( !isAllActivePax( promo, true ) )
          {
            Set nodes = getNodesForPaxs( paxs );
            paxs.addAll( getUsersInNodes( nodes, false ) );
          }
          else
          {
            paxs.addAll( getAllActive() );
          }
        }
        else if ( promo.isThrowdownPromotion() )
        {
          ThrowdownPromotion tdPromo = (ThrowdownPromotion)promo;
          for ( Iterator<Audience> iter = tdPromo.getAllCompetitorAudiences().iterator(); iter.hasNext(); )
          {
            Audience audience = (Audience)iter.next();
            // Audience audience = temp.getAudience();
            if ( audience instanceof PaxAudience )
            {
              for ( Iterator iterator = ( (PaxAudience)audience ).getAudienceParticipants().iterator(); iterator.hasNext(); )
              {
                AudienceParticipant audPax = (AudienceParticipant)iterator.next();
                // Add only ACTIVE pax
                if ( audPax.getParticipant().isActive().booleanValue() )
                {
                  paxs.add( audPax.getParticipant() );
                }
              }
            }
            else
            {
              paxs.addAll( listBuilderService.searchParticipants( (CriteriaAudience)audience, null, false, new HashSet(), true, true ) );
            }
          }
        }
      }
    }
    return paxs;
  }

  /**
   * Retrieves a list of participants who are eligible for the given promotion. The boolean flag
   * tells us whether to return paxs from primary audience (giver/submitter) or secondary audience
   * (reciever/team member)
   * 
   * @param promoId
   * @param primaryAudience
   * @return participants
   * {@inheritDoc}
   */

  public Set getAllEligibleActivePaxForPromotion( Long promoId, boolean primaryAudience )
  {
    Set paxs = new HashSet();
    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_AUDIENCES_WITH_PAXS ) );
    Promotion promo = promotionDAO.getPromotionByIdWithAssociations( promoId, reqCollection );
    if ( primaryAudience )
    {
      if ( isAllActivePax( promo, true ) )
      {
        paxs.addAll( getAllActiveAndWelcomeEmailNotSent() );
      }
      else
      {
        Set primaryAudiences = new HashSet();
        if ( promo.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ENTIRE_PARENT_AUDIENCE_CODE ) )
        {
          primaryAudiences.addAll( ( (ProductClaimPromotion)promo ).getParentPromotion().getPromotionPrimaryAudiences() );
        }
        else
        {
          primaryAudiences.addAll( promo.getPromotionPrimaryAudiences() );
        }
        for ( Iterator iter = primaryAudiences.iterator(); iter.hasNext(); )
        {
          PromotionAudience temp = (PromotionAudience)iter.next();
          Audience audience = temp.getAudience();
          if ( audience instanceof PaxAudience )
          {
            for ( Iterator iterator = ( (PaxAudience)audience ).getAudienceParticipants().iterator(); iterator.hasNext(); )
            {
              AudienceParticipant audPax = (AudienceParticipant)iterator.next();
              // Add only ACTIVE pax
              // or check whether welcome emailSent! if already sent
              if ( audPax.getParticipant().isActive().booleanValue() || !audPax.getParticipant().isWelcomeEmailSent().booleanValue() )
              {
                paxs.add( audPax.getParticipant() );
              }
            }
          }
          else
          {
            paxs.addAll( listBuilderService.searchParticipants( (CriteriaAudience)audience, null, false, new HashSet(), true, true ) );
          }
        }
      }
    }
    else
    {
      if ( isAllActivePax( promo, false ) )
      {
        paxs.addAll( getAllActiveAndWelcomeEmailNotSent() );
      }
      else
      {
        if ( promo.getSecondaryAudienceType() != null && promo.getSecondaryAudienceType().getCode().equals( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) )
        {
          for ( Iterator iter = promo.getPromotionSecondaryAudiences().iterator(); iter.hasNext(); )
          {
            PromotionAudience temp = (PromotionAudience)iter.next();
            Audience audience = temp.getAudience();
            if ( audience instanceof PaxAudience )
            {
              for ( Iterator iterator = ( (PaxAudience)audience ).getAudienceParticipants().iterator(); iterator.hasNext(); )
              {
                AudienceParticipant audPax = (AudienceParticipant)iterator.next();
                // Add only ACTIVE pax
                // or check whether welcome emailSent! if already sent
                if ( audPax.getParticipant().isActive().booleanValue() || !audPax.getParticipant().isWelcomeEmailSent().booleanValue() )
                {
                  paxs.add( audPax.getParticipant() );
                }
              }
            }
            else
            {
              paxs.addAll( listBuilderService.searchParticipants( (CriteriaAudience)audience, null, false, new HashSet(), true, true ) );
            }
          }
        }
        else if ( promo.getSecondaryAudienceType() != null && promo.getSecondaryAudienceType().getCode().equals( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) )
        {
          if ( !isAllActivePax( promo, true ) )
          {
            Set nodes = getNodesForPaxs( paxs );
            paxs.addAll( getUsersInNodes( nodes, true ) );
          }
          else
          {
            paxs.addAll( getAllActiveAndWelcomeEmailNotSent() );
          }
        }
        else if ( promo.getSecondaryAudienceType() != null && promo.getSecondaryAudienceType().getCode().equals( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) )
        {
          if ( !isAllActivePax( promo, true ) )
          {
            Set nodes = getNodesForPaxs( paxs );
            paxs.addAll( getUsersInNodes( nodes, false ) );
          }
          else
          {
            paxs.addAll( getAllActiveAndWelcomeEmailNotSent() );
          }
        }
      }
    }
    return paxs;
  }

  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  public boolean isAllActivePax( Promotion promo, boolean checkPrimary )
  {
    boolean isAllActive = false;
    if ( checkPrimary )
    {
      isAllActive = promo.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE );
      if ( !isAllActive )
      {
        isAllActive = promo.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ENTIRE_PARENT_AUDIENCE_CODE )
            && ( (ProductClaimPromotion)promo ).getParentPromotion().getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE );
      }
    }
    else
    {
      if ( promo.getSecondaryAudienceType() != null )
      {
        isAllActive = promo.getSecondaryAudienceType().getCode().equals( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE )
            || promo.getSecondaryAudienceType().getCode().equals( SecondaryAudienceType.SAME_AS_PRIMARY_CODE )
                && promo.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE );
      }
    }
    return isAllActive;
  }

  public void setListBuilderService( ListBuilderService listBuilderService )
  {
    this.listBuilderService = listBuilderService;
  }

  private Set getNodesForPaxs( Set paxs )
  {
    Set nodes = new HashSet();
    AssociationRequestCollection reqs = new AssociationRequestCollection();
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Participant pax = (Participant)iter.next();
      reqs.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
      pax = getParticipantByIdWithAssociations( pax.getId(), reqs );
      for ( Iterator iterator = pax.getUserNodes().iterator(); iterator.hasNext(); )
      {
        UserNode temp = (UserNode)iterator.next();
        nodes.add( temp.getNode() );
      }
    }
    return nodes;
  }

  /**
   * Overridden from @see com.biperf.core.service.participant.ParticipantService#getAllPaxesInPromotionAndNode(java.lang.Long, java.lang.Long)
   * @param promotionId
   * @param nodeId
   * @return set containing the participants in both primary and secondary audience
   */
  public Set getAllPaxesInPromotionAndNode( Long promotionId, Long nodeId )
  {
    Set promotionParticipants = new HashSet();
    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_AUDIENCES_WITH_PAXS ) );
    Promotion promo = promotionDAO.getPromotionByIdWithAssociations( promotionId, reqCollection );
    boolean isAllPax = false;
    // check if all active pax either for primary or secondary
    if ( isAllActivePax( promo, true ) || isAllActivePax( promo, false ) )
    {
      promotionParticipants.addAll( getAllActive() );
      isAllPax = true;
    }
    if ( !isAllPax )
    {
      promotionParticipants.addAll( getAllEligiblePaxForPromotion( promotionId, true ) );
      promotionParticipants.addAll( getAllEligiblePaxForPromotion( promotionId, false ) );
    }
    Set paxInNode = new HashSet();
    for ( Iterator participantIterator = promotionParticipants.iterator(); participantIterator.hasNext(); )
    {
      Participant currentParticipant = (Participant)participantIterator.next();
      Set userNodes = currentParticipant.getUserNodes();
      for ( Iterator nodeIterator = userNodes.iterator(); nodeIterator.hasNext(); )
      {
        UserNode currentUserNode = (UserNode)nodeIterator.next();
        if ( currentUserNode.getNode().getId().equals( nodeId ) )
        {
          paxInNode.add( currentParticipant );
          continue;
        }
      }
    }
    return paxInNode;
  }

  public Set getPaxInNodes( Collection nodes, boolean includeChildNodes )
  {
    Set users = getUsersInNodes( nodes, includeChildNodes );
    Set paxUsers = new HashSet();
    if ( users != null )
    {
      for ( Iterator iter = users.iterator(); iter.hasNext(); )
      {
        User user = (User)iter.next();
        if ( user.getUserType().equals( UserType.lookup( UserType.PARTICIPANT ) ) )
        {
          paxUsers.add( user );
        }
      }
    }
    return paxUsers;
  }

  private Set getUsersInNodes( Collection nodes, boolean includeChildNodes )
  {
    Set users = new HashSet();
    for ( Iterator iter = nodes.iterator(); iter.hasNext(); )
    {
      Node node = (Node)iter.next();

      AssociationRequestCollection reqs = new AssociationRequestCollection();
      reqs.add( new NodeToUserNodesAssociationRequest() );
      reqs.add( new NodeToChildrenAssociationRequest() );
      node = nodeService.getNodeWithAssociationsById( node.getId(), reqs );
      users.addAll( node.getActiveUserList() );
      if ( includeChildNodes )
      {
        // recursive call to traverse the hierarchy
        users.addAll( getUsersInNodes( nodeService.getChildrenNodes( node.getId() ), true ) );
      }
    }
    return users;
  }

  private void filterPaxs( Set paxs, List userIds, boolean isHave )
  {
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Object temp = iter.next();
      if ( temp instanceof AudienceParticipant )
      {
        if ( isHave && !userIds.contains( ( (AudienceParticipant)temp ).getParticipant().getId() ) )
        {
          iter.remove();
        }
        else if ( !isHave && userIds.contains( ( (AudienceParticipant)temp ).getParticipant().getId() ) )
        {
          iter.remove();
        }
      }
      else if ( temp instanceof Participant )
      {
        if ( isHave && !userIds.contains( ( (Participant)temp ).getId() ) )
        {
          iter.remove();
        }
        else if ( !isHave && userIds.contains( ( (Participant)temp ).getId() ) )
        {
          iter.remove();
        }
      }
      else if ( temp instanceof User )
      {
        if ( isHave && !userIds.contains( ( (User)temp ).getId() ) )
        {
          iter.remove();
        }
        else if ( !isHave && userIds.contains( ( (User)temp ).getId() ) )
        {
          iter.remove();
        }
      }
      else
      {
        if ( isHave && !userIds.contains( ( (FormattedValueBean)temp ).getId() ) )
        {
          iter.remove();
        }
        else if ( !isHave && userIds.contains( ( (FormattedValueBean)temp ).getId() ) )
        {
          iter.remove();
        }
      }
    }
  }

  public Set getAllPaxWhoHaveGivenRecognition( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    // Bug Fix 17726 get the User's who participated in Recognition promo program from CLAIM Table
    // instead of Activity table.
    List userIds = claimDAO.getUserIdsByPromoIdWithinRange( promoId, start, end );
    filterPaxs( paxs, userIds, true );
    return paxs;
  }

  public Set getAllPaxWhoHaveNotGivenRecognition( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    // Bug Fix 17726 get the User's who participated in Recognition promo program from CLAIM Table
    // instead of Activity table.
    List userIds = claimDAO.getUserIdsByPromoIdWithinRange( promoId, start, end );
    filterPaxs( paxs, userIds, false );
    return paxs;
  }

  public Set getAllPaxWhoHaveNotNominated( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = claimDAO.getUserIdsByPromoIdWithinRange( promoId, start, end );
    filterPaxs( paxs, userIds, false );
    return paxs;
  }

  public long getUserIdWhoHaveNotGivenAnyClaim( Long promoId, Long paxId, Date start, Date end )
  {
    return claimDAO.getUserIdByPromoIdWithinRange( promoId, paxId, start, end );
  }

  public Set getAllPaxWhoHaveReceivedRecognition( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = activityDAO.getUserIdsByPromotionDuringTimePeriod( promoId, false, start, end );
    filterPaxs( paxs, userIds, true );
    return paxs;
  }

  public Set getAllPaxWhoHaveNotReceivedRecognition( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = activityDAO.getUserIdsByPromotionDuringTimePeriod( promoId, false, start, end );
    filterPaxs( paxs, userIds, false );
    return paxs;
  }

  public Set getAllPaxWhoHaveUsedBudget( Long promoId, Date start, Date end, boolean usedAll )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );

    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    Promotion promo = promotionDAO.getPromotionByIdWithAssociations( promoId, reqCollection );

    if ( null != promo.getBudgetMaster() && null != promo.getBudgetMaster().getBudgetType() )
    {
      if ( promo.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
      {
        List userIds = activityDAO.getUserIdsByPromoIdWhoUsedNodeBudgetDuringTimePeriod( promoId, start, end, usedAll );
        filterPaxs( paxs, userIds, true );
      }
      else if ( promo.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
      {
        List userIds = activityDAO.getUserIdsByPromoIdWhoUsedPaxBudgetDuringTimePeriod( promoId, start, end, usedAll );
        filterPaxs( paxs, userIds, true );
      }
      else if ( promo.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
      {
        List userIds = activityDAO.getUserIdsByPromoIdWhoUsedCentralBudgetDuringTimePeriod( promoId, start, end, usedAll );
        filterPaxs( paxs, userIds, true );
      }
    }

    return paxs;
  }

  public Set getAllPaxWhoHaveNotUsedBudget( Long promoId, Date start, Date end, boolean usedAll )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );

    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    Promotion promo = promotionDAO.getPromotionByIdWithAssociations( promoId, reqCollection );

    if ( null != promo.getBudgetMaster() && null != promo.getBudgetMaster().getBudgetType() )
    {
      if ( promo.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
      {
        if ( usedAll )
        {
          List userIds = activityDAO.getUserIdsByPromoIdWhoNotUsedAllNodeBudget( promoId );
          filterPaxs( paxs, userIds, true );
        }
        else
        {
          List userIdsForBudgetWithOriginalValue = budgetDAO.getUserIdsByPromoIdWithOriginalNodeBudgetValue( promoId );
          filterPaxs( paxs, userIdsForBudgetWithOriginalValue, true );

          List userIds = activityDAO.getUserIdsByPromoIdWhoUsedNodeBudgetDuringTimePeriod( promoId, start, end, usedAll );
          filterPaxs( paxs, userIds, false );
        }
      }
      else if ( promo.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
      {
        if ( usedAll )
        {
          List userIds = activityDAO.getUserIdsByPromoIdWhoNotUsedAllPaxBudget( promoId );
          filterPaxs( paxs, userIds, true );
        }
        else
        {
          List userIdsForBudgetWithOriginalValue = budgetDAO.getUserIdsByPromoIdWithOriginalPaxBudgetValue( promoId );
          filterPaxs( paxs, userIdsForBudgetWithOriginalValue, true );

          List userIds = activityDAO.getUserIdsByPromoIdWhoUsedPaxBudgetDuringTimePeriod( promoId, start, end, usedAll );
          filterPaxs( paxs, userIds, false );
        }
      }
      else if ( promo.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
      {
        if ( usedAll )
        {
          List userIds = activityDAO.getUserIdsByPromoIdWhoNotUsedAllCentralBudget( promoId );
          filterPaxs( paxs, userIds, true );
        }
        else
        {
          List userIds = activityDAO.getUserIdsByPromoIdWhoUsedCentralBudgetDuringTimePeriod( promoId, start, end, usedAll );
          filterPaxs( paxs, userIds, false );
        }
      }
    }

    return paxs;
  }

  public Set getAllPaxWhoHavePassedQuiz( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = claimDAO.getUserIdsWhoPassedQuizWithinRange( promoId, start, end, true );
    filterPaxs( paxs, userIds, true );
    return paxs;
  }

  public Set getAllPaxWhoHaveNotPassedQuiz( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = claimDAO.getUserIdsWhoPassedQuizWithinRange( promoId, start, end, false );
    filterPaxs( paxs, userIds, true );
    return paxs;
  }

  public Set getAllPaxWhoHaveTakenQuiz( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = claimDAO.getUserIdsByPromoIdWithinRange( promoId, start, end, false );
    filterPaxs( paxs, userIds, true );
    return paxs;
  }

  public Set getAllPaxWhoHaveNotTakenQuiz( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = claimDAO.getUserIdsByPromoIdWithinRange( promoId, start, end, false );
    filterPaxs( paxs, userIds, false );
    // Bug 37625 fixed Remove pax from set when pax already taken Quiz before pax inactivity date
    // range
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Object temp = iter.next();
      if ( temp instanceof Participant )
      {
        long paxId = ( (Participant)temp ).getId();
        Promotion promo = promotionDAO.getPromotionById( promoId );
        // If quiz promotions exist, also get quiz claim history for this user
        QuizClaimQueryConstraint submitterClaimQueryConstraint = new QuizClaimQueryConstraint();
        submitterClaimQueryConstraint.setSubmitterId( paxId );
        Map participantQuizClaimHistoryByPromotionMap = claimService.getParticipantQuizClaimHistoryByPromotionMap( submitterClaimQueryConstraint, null );

        ParticipantQuizClaimHistory participantQuizClaimHistory = (ParticipantQuizClaimHistory)participantQuizClaimHistoryByPromotionMap.get( promo );
        if ( participantQuizClaimHistory != null && !participantQuizClaimHistory.isSavedForLater() && !participantQuizClaimHistory.isAttemptsRemaining() )
        {
          iter.remove();
        }
      }
    }
    return paxs;
  }

  public Set getAllPaxWhoHaveNotTakenSurvey( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = participantSurveyDAO.getUserIdsByPromoIdWithinRange( promoId, start, end );
    filterPaxs( paxs, userIds, false );
    return paxs;
  }

  public Set getAllPaxWhoHaveSubmittedClaim( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = claimDAO.getUserIdsByPromoIdWithinRange( promoId, start, end );
    filterPaxs( paxs, userIds, true );
    return paxs;
  }

  public Set getAllPaxWhoHaveNotSubmittedClaim( Long promoId, Date start, Date end )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = claimDAO.getUserIdsByPromoIdWithinRange( promoId, start, end );
    filterPaxs( paxs, userIds, false );
    return paxs;
  }

  public Set getAllPaxWhoHaveNotSelectedGoal( Long promoId )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = paxGoalDAO.getUserIdsWithPaxGoalByPromotionId( promoId );
    filterPaxs( paxs, userIds, false );
    return paxs;
  }

  public Set getAllPaxWhoHaveNotSelectedChallengepoint( Long promoId )
  {
    Set paxs = getAllEligiblePaxForPromotion( promoId, true );
    List userIds = paxGoalDAO.getUserIdsWithPaxGoalByPromotionId( promoId );
    filterPaxs( paxs, userIds, false );
    return paxs;
  }

  public void updateFacebookInfo( Long userId, UserFacebook userFacebook )
  {
    Participant pax = participantDAO.getParticipantById( userId );
    if ( userFacebook == null && pax.getUserFacebook() != null )
    {
      userFacebookDAO.delete( pax.getUserFacebook() );
    }
    pax.setUserFacebook( userFacebook );
  }

  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

  public void setUserFacebookDAO( UserFacebookDAO userFacebookDAO )
  {
    this.userFacebookDAO = userFacebookDAO;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  public void setBudgetDAO( BudgetMasterDAO budgetDAO )
  {
    this.budgetDAO = budgetDAO;
  }

  public void setPaxGoalDAO( PaxGoalDAO paxGoalDAO )
  {
    this.paxGoalDAO = paxGoalDAO;
  }

  /**
   * Verifies the specified participant import file a page at a time.
   * 
   * @param importFileId
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, Long userId, Long hierarchyId )
  {
    return participantDAO.verifyImportFile( importFileId, "V", userId, hierarchyId );
  }

  /**
   * Import the specified participant import file a page at a time.
   * 
   * @param importFileId
   * @param userId
   * @return Map result set
   */
  public Map importImportFile( Long importFileId, Long userId )
  {
    Map output = participantDAO.verifyImportFile( importFileId, "L", userId, null );
    Object obj = output.get( OUTPUT_RESULT_SET );

    if ( obj instanceof List )
    {
      // Elastic search index
      AutoCompleteService autoCompleteService = getAutoCompleteService();
      Stream<List<Long>> batches = BICollectionUtils.batches( (List)obj, 500 );
      batches.forEach( e ->
      {
        autoCompleteService.indexParticipants( e );
      } );

      // Honeycomb account sync
      HCServices hcServices = getHCServices();
      hcServices.syncParticipantDetails( (List)obj );

    }

    return output;
  }

  /**
   * Overridden from @see com.biperf.core.service.participant.ParticipantService#updateParticipantInAwardbanqByBatch(java.lang.Long, int, com.biperf.core.value.ParticipantUpdateProcessSummaryBean)
   * @param lastId
   * @param batchSize
   * @param summary
   * @return Long
   * @throws ServiceErrorException 
   */
  public Long updateParticipantInAwardbanqByBatch( Long lastId, int batchSize, ParticipantUpdateProcessSummaryBean summary ) throws ServiceErrorException // bug
                                                                                                                                                          // fix:37649
  {
    List paxList = participantDAO.getAllParticipantsInAwardbanqSystem( lastId, batchSize );
    lastId = null;

    if ( !paxList.isEmpty() )
    {
      Iterator iPax = paxList.iterator();
      while ( iPax.hasNext() )
      {
        Participant pax = (Participant)iPax.next();
        summary.incTotalCount();
        try
        {
          lastId = pax.getId();
          if ( pax.getAwardBanqNumber() != null && pax.getAwardBanqNumber().trim().length() > 0 )
          {
            try
            {
              awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( pax );
            }
            catch( UniformInterfaceException | ClientHandlerException | IOException e )
            {
              e.printStackTrace();
            }

            summary.incSuccessCount();
          }

        }
        catch( ServiceErrorException e )
        {
          // bug fix:37649
          ServiceError serviceErrorsTemp = (ServiceError)e.getServiceErrors().get( 0 );

          if ( serviceErrorsTemp != null && serviceErrorsTemp.toString().contains( "[-3]" ) )
          {
            List serviceErrors = new ArrayList();
            ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.AWARDBANQ_ERROR, serviceErrorsTemp.toString() );
            serviceErrors.add( serviceError );

            summary.incFailureCount();
            summary.addError( e,
                              "Fatal Error: An error code of -3 was encountered while updating participant id  " + lastId + " , " + pax.getFirstName() + " " + pax.getLastName() + " "
                                  + " AwardBanqNbr: " + pax.getAwardBanqNumber() + " CentraxId: " + pax.getCentraxId() );
            throw new ServiceErrorException( serviceErrors, e );

          }
          // End of bug fix:37649
          summary.incFailureCount();
          summary.addError( e,
                            "An exception occurred while updating participant " + pax.getFirstName() + " " + pax.getLastName() + " " + " AwardBanqNbr: " + pax.getAwardBanqNumber() + " CentraxId: "
                                + pax.getCentraxId() );
        }
      }
    }

    return lastId;
  }

  /**
  * get most recent participant_employer record and return the termination_date
  * @param userId
  * @return Date
  */
  public Date getCurrentParticipantEmployerTermDate( Long userId )
  {
    return participantDAO.getCurrentParticipantEmployerTermDate( userId );
  }

  /**
   * Get Node owner for a given node.
   * 
   * @return {@link User} object.
   */
  public User getNodeOwner( Long nodeId )
  {
    return participantDAO.getNodeOwner( nodeId );
  }

  /**
   * Get Node owner for a given node.
   * 
   * @return {@link User} object.
   */
  public Set getNodeManager( Long nodeId, String roleType )
  {
    return participantDAO.getNodeManager( nodeId, roleType );
  }

  public List rosterSearch( Long nodeId, String participantStatus, Long excludeUserId, int sortField, boolean sortAscending, int pageNumber, int pageSize )
  {
    List participants = participantDAO.rosterSearch( nodeId, participantStatus, excludeUserId, sortField, sortAscending, pageNumber, pageSize );
    Long paxCount = participantDAO.rosterSearchParticipantCount( nodeId, participantStatus, excludeUserId );
    for ( Object object : participants )
    {
      ParticipantRosterSearchValueBean bean = (ParticipantRosterSearchValueBean)object;
      bean.setParticipantCount( paxCount );
    }
    return participants;
  }

  public void rosterUpdateParticipant( Participant detachedParticipant ) throws ServiceErrorException
  {
    Long participantId = detachedParticipant.getId();
    Participant attachedParticipant = participantDAO.getParticipantOverviewById( participantId );

    if ( detachedParticipant.isActive() && attachedParticipant.isActive() )
    {
      // Update User Node
      if ( null != detachedParticipant.getUserNodes() && !detachedParticipant.getUserNodes().isEmpty() )
      {
        UserNode detachedUserNode = (UserNode)detachedParticipant.getUserNodes().iterator().next();
        userService.updateUserNodeRole( participantId, detachedUserNode.getNode().getId(), detachedUserNode.getHierarchyRoleType() );
      }
    }

    // Update Job Position and department
    if ( null != detachedParticipant.getParticipantEmployers() && !detachedParticipant.getParticipantEmployers().isEmpty() )
    {
      ParticipantEmployer detachedParticipantEmployer = (ParticipantEmployer)detachedParticipant.getParticipantEmployers().iterator().next();
      ParticipantEmployer currentParticipantEmployer = getCurrentParticipantEmployer( participantId );
      boolean updateCurpaxEmp = true;
      if ( currentParticipantEmployer == null )
      {
        currentParticipantEmployer = new ParticipantEmployer();
        currentParticipantEmployer.setEmployer( this.employerDAO.getEmployerById( detachedParticipantEmployer.getEmployer().getId() ) );
        currentParticipantEmployer.setParticipant( attachedParticipant );
      }
      currentParticipantEmployer.setPositionType( detachedParticipantEmployer.getPositionType() );
      currentParticipantEmployer.setDepartmentType( detachedParticipantEmployer.getDepartmentType() );
    }

    // Update Primary Address
    if ( null != detachedParticipant.getUserAddresses() && !detachedParticipant.getUserAddresses().isEmpty() )
    {
      UserAddress detachedUserAddress = (UserAddress)detachedParticipant.getUserAddresses().iterator().next();
      UserAddress primaryUserAddress = userService.getPrimaryUserAddress( participantId );
      if ( primaryUserAddress != null )
      {
        // Update Primary Address
        primaryUserAddress.setUser( detachedUserAddress.getUser() );
        primaryUserAddress.setAddressType( detachedUserAddress.getAddressType() );
        primaryUserAddress.setAddress( detachedUserAddress.getAddress() );
        userService.updateUserAddress( participantId, primaryUserAddress );
      }
    }

    // Update Primary Email
    if ( null != detachedParticipant.getUserEmailAddresses() && !detachedParticipant.getUserEmailAddresses().isEmpty() )
    {
      UserEmailAddress detachedUserEmailAddress = (UserEmailAddress)detachedParticipant.getUserEmailAddresses().iterator().next();
      UserEmailAddress primaryUserEmailAddress = userService.getPrimaryUserEmailAddress( participantId );
      if ( primaryUserEmailAddress == null )
      {
        // Add Primary Email Address
        primaryUserEmailAddress = new UserEmailAddress();
        primaryUserEmailAddress.setIsPrimary( true );
        primaryUserEmailAddress.setUser( detachedUserEmailAddress.getUser() );
        primaryUserEmailAddress.setEmailType( detachedUserEmailAddress.getEmailType() );
        primaryUserEmailAddress.setEmailAddr( detachedUserEmailAddress.getEmailAddr() );
        primaryUserEmailAddress.setVerificationStatus( detachedUserEmailAddress.getVerificationStatus() );
        userService.addUserEmailAddress( participantId, primaryUserEmailAddress );
      }
      else
      {
        // Update Primary Email Address
        primaryUserEmailAddress.setUser( detachedUserEmailAddress.getUser() );
        primaryUserEmailAddress.setEmailType( detachedUserEmailAddress.getEmailType() );
        primaryUserEmailAddress.setEmailAddr( detachedUserEmailAddress.getEmailAddr() );
        primaryUserEmailAddress.setVerificationStatus( detachedUserEmailAddress.getVerificationStatus() );
        userService.updateUserEmailAddress( participantId, primaryUserEmailAddress );
      }
    }

    // Update Primary Telephone
    if ( null != detachedParticipant.getUserPhones() && !detachedParticipant.getUserPhones().isEmpty() )
    {
      UserPhone detachedUserPhone = (UserPhone)detachedParticipant.getUserPhones().iterator().next();
      UserPhone primaryUserPhone = userService.getPrimaryUserPhone( participantId );
      if ( primaryUserPhone == null )
      {
        // Add Primary Phone
        primaryUserPhone = new UserPhone();
        primaryUserPhone.setIsPrimary( true );
        primaryUserPhone.setUser( detachedUserPhone.getUser() );
        primaryUserPhone.setPhoneType( detachedUserPhone.getPhoneType() );
        primaryUserPhone.setVerificationStatus( detachedUserPhone.getVerificationStatus() );
        primaryUserPhone.setPhoneNbr( detachedUserPhone.getPhoneNbr() );
        primaryUserPhone.setPhoneExt( detachedUserPhone.getPhoneExt() );
        primaryUserPhone.setCountryPhoneCode( detachedUserPhone.getCountryPhoneCode() );
        userService.addUserPhone( participantId, primaryUserPhone );
      }
      else
      {
        // Update Primary Phone
        primaryUserPhone.setUser( detachedUserPhone.getUser() );
        primaryUserPhone.setPhoneType( detachedUserPhone.getPhoneType() );
        primaryUserPhone.setVerificationStatus( detachedUserPhone.getVerificationStatus() );
        primaryUserPhone.setPhoneNbr( detachedUserPhone.getPhoneNbr() );
        primaryUserPhone.setPhoneExt( detachedUserPhone.getPhoneExt() );
        primaryUserPhone.setCountryPhoneCode( detachedUserPhone.getCountryPhoneCode() );
        userService.updateUserPhone( participantId, primaryUserPhone );
      }
    }

    // Update Personal Info
    attachedParticipant.setFirstName( detachedParticipant.getFirstName() );
    attachedParticipant.setMiddleName( detachedParticipant.getMiddleName() );
    attachedParticipant.setLastName( detachedParticipant.getLastName() );
    attachedParticipant.setStatus( detachedParticipant.getStatus() );
    attachedParticipant.setLanguageType( detachedParticipant.getLanguageType() );
    saveParticipant( attachedParticipant );
  }

  public Map markAsPlateauAwardsOnly()
  {
    Map results = new HashMap();
    results = participantDAO.markAsPlateauAwardsOnly();
    return results;
  }

  public Map<User, Set<Participant>> getManagerForCompetitorAudience( Set<Participant> paxs )
  {
    Map<User, Set<Participant>> managerPaxMap = new HashMap<User, Set<Participant>>();
    Set<Node> nodes = new HashSet<Node>();

    for ( Participant pax : paxs )
    {
      for ( UserNode usernode : pax.getUserNodes() )
      {
        nodes.add( usernode.getNode() );
      }
    }
    for ( Node node : nodes )
    {
      Set<Participant> participantsByNode = new HashSet<Participant>();
      for ( Participant pax : paxs )
      {
        for ( UserNode usernode : pax.getUserNodes() )
        {
          if ( usernode.getNode().equals( node ) )
          {
            participantsByNode.add( pax );
          }
        }
      }

      Set users = new HashSet();
      AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();
      users.addAll( userService.getAllUsersOnNodeHavingRole( node.getId(), HierarchyRoleType.lookup( HierarchyRoleType.OWNER ), userAssociationRequestCollection ) );
      users.addAll( userService.getAllUsersOnNodeHavingRole( node.getId(), HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ), userAssociationRequestCollection ) );
      for ( Iterator userIter = users.iterator(); userIter.hasNext(); )
      {
        User manager = (User)userIter.next();
        if ( managerPaxMap.containsKey( manager ) )
        {
          managerPaxMap.get( manager ).addAll( participantsByNode );
        }
        else
        {
          managerPaxMap.put( manager, participantsByNode );
        }
      }
    }
    return managerPaxMap;
  }

  public Map<User, Set<Participant>> getManagerForCompetitorAudience( Long promoId )
  {
    Set<Participant> paxs = new HashSet<Participant>();
    paxs = getAllEligiblePaxForPromotion( promoId, false );
    return getManagerForCompetitorAudience( paxs );
  }

  public List getNodePaxCount( Long userId )
  {
    return participantDAO.getNodePaxCount( userId );
  }

  private boolean isTermsAndConditionsUsed()
  {
    boolean isTermsAndConditionsUsed = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
    return isTermsAndConditionsUsed;
  }

  private boolean isPaxInactive( Participant pax )
  {
    if ( pax.getStatus() != null && pax.getStatus().getCode() != null && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
    {
      return true;
    }
    return false;
  }

  private boolean canUserAcceptTermsAndConditionsForPax()
  {
    boolean canUserAcceptTermsAndConditionsForPax = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USER_CAN_ACCEPT ).getBooleanVal();
    return canUserAcceptTermsAndConditionsForPax;
  }

  /**
   * Get Participant by SSO ID.
   * 
   * @param ssoId
   * @return Participant
   */
  public List<Participant> getUserBySSOId( String ssoId )
  {
    return participantDAO.getUserBySSOId( ssoId );
  }

  public void setPublicRecognitionDAO( PublicRecognitionDAO publicRecognitionDAO )
  {
    this.publicRecognitionDAO = publicRecognitionDAO;
  }

  public List getAllForCharIDAndValue( Long id, String charName )
  {
    return participantDAO.getAllForCharIDAndValue( id, charName );
  }

  public Participant getParticipantByIdWithProjections( Long participantId, ProjectionCollection collection )
  {
    return participantDAO.getParticipantByIdWithProjections( participantId, collection );
  }
  
  public Participant getParticipantByUserNameWithProjections( String userName, ProjectionCollection collection )
  {
    return participantDAO.getParticipantByUserNameWithProjections( userName, collection );
  }

  public List<Participant> getParticipantsByIdWithProjections( List<Long> participantIds, ProjectionCollection collection )
  {
    if ( participantIds == null || participantIds.isEmpty() )
    {
      return new ArrayList<Participant>();
    }
    return participantDAO.getParticipantsByIdWithProjections( participantIds, collection );
  }

  public boolean isParticipantFollowed( Long userId, Long loggedinUserId )
  {
    return participantDAO.isParticipantFollowed( userId, loggedinUserId );
  }

  public void setParticipantSurveyDAO( ParticipantSurveyDAO participantSurveyDAO )
  {
    this.participantSurveyDAO = participantSurveyDAO;
  }

  /*
   * public List<Participant> getNodeAndBelowForBudget( Long userId, Long nodeId ) { return
   * this.participantDAO.getNodeAndBelowForBudget( userId, nodeId ); }
   */

  @Override
  public List<Participant> getAllPaxWhoHaveGivenOrReceivedRecognition( Long userId )
  {
    return claimDAO.getAllPaxWhoHaveGivenOrReceivedRecognition( userId );
  }

  public String getLNameFNameByPaxId( Long participantId )
  {
    return this.participantDAO.getLNameFNameByPaxId( participantId );
  }

  public Date getActiveHireDate( Long userId )
  {
    return this.participantDAO.getActiveHireDate( userId );
  }

  @Override
  public List<Participant> getAllPreSelectedContributors( Long recipientId )
  {
    return claimDAO.getAllPreSelectedContributors( recipientId );
  }

  public List<PurlContributorValueBean> getAllExistingContributors( Long userId )
  {
    return claimDAO.getAllExistingContributors( userId );
  }

  @Override
  public List<Participant> getAllParticipantsByAudienceIds( List<Long> audienceIdList )
  {
    return participantDAO.getAllParticipantsByAudienceIds( audienceIdList );
  }

  public Map getParticipatForMiniProfile( String promotionId, String userId, String userLocale )
  {
    return this.participantDAO.getParticipatForMiniProfile( promotionId, userId, userLocale );
  }

  @Override
  public Integer getPaxCountBasedOnNodes( List<Long> nodesId, Boolean isIncludeChild )
  {
    Integer paxCount = 0;
    for ( Long nodeId : nodesId )
    {
      paxCount += nodeService.getPaxCountBasedOnNodes( nodeId, isIncludeChild );
    }
    return paxCount;
  }

  @Override
  public ParticipantGroupList getGroupList( Long userId )
  {
    return participantGroupDAO.findGroupsByPaxId( userId );
  }

  public ParticipantGroupDAO getParticipantGroupDAO()
  {
    return participantGroupDAO;
  }

  public void setParticipantGroupDAO( ParticipantGroupDAO participantGroupDAO )
  {
    this.participantGroupDAO = participantGroupDAO;
  }

  @Override
  public ParticipantSearchListView getParticipantGroupList( Long groupId, Long promotionId )
  {
    ParticipantSearchListView listViewObj = new ParticipantSearchListView();
    List<Participant> paxs = new ArrayList<Participant>();
    ParticipantGroup group = participantGroupDAO.find( groupId );

    Set<ParticipantGroupDetails> groupDetails = group.getGroupDetails();
    for ( ParticipantGroupDetails dtls : groupDetails )
    {
      paxs.add( dtls.getParticipant() );
    }

    List<Long> paxIds = new ArrayList<Long>();
    for ( Participant pax : paxs )
    {
      paxIds.add( pax.getId() );
    }
    Long[] ids = paxIds.toArray( new Long[paxIds.size()] );

    if ( ids.length > 0 )
    {
      List<Map<Long, CountryValueBean>> countryResults = populateCountriesForUsers( ids );
      listViewObj = buildParticipantSearchListView( paxs, promotionId, countryResults );
    }
    else
    {
      addMessageToListView( listViewObj, "participant.search.NO_RESULTS" );
    }

    return listViewObj;
  }

  public void addMessageToListView( ParticipantSearchListView listViewObj, String messageCMAsset )
  {
    String refineSearchMessage = CmsResourceBundle.getCmsBundle().getString( messageCMAsset );
    WebErrorMessage messages = new WebErrorMessage();
    List<WebErrorMessage> messageList = new ArrayList<WebErrorMessage>();
    messages.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    messages.setText( refineSearchMessage );
    messages.setCode( "" );
    messages.setName( "" );
    messageList.add( messages );
    listViewObj.setMessages( messageList );
  }

  private ParticipantSearchListView buildParticipantSearchListView( List<Participant> paxResults, Long promotionId, List<Map<Long, CountryValueBean>> countryResults )
  {
    ParticipantSearchListView paxSearchListView = new ParticipantSearchListView();
    WebErrorMessage messages = new WebErrorMessage();
    List<WebErrorMessage> messageList = new ArrayList<WebErrorMessage>();
    String refineSearchMessage = CmsResourceBundle.getCmsBundle().getString( "participant.search.NO_PARTICIPANTS" );
    if ( paxResults == null || paxResults.isEmpty() )
    {
      messages.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      messages.setText( refineSearchMessage );
      messageList.add( messages );
      paxSearchListView.setMessages( messageList );
    }

    Map<String, String> codes = new HashMap<String, String>();
    if ( paxResults != null )
    {
      for ( Object object : paxResults )
      {
        if ( object instanceof Participant )
        {
          Participant pax = (Participant)object;
          ParticipantSearchView bean = new ParticipantSearchView( pax );
          bean.setCountryRatio( BigDecimal.ONE.doubleValue() );
          for ( Map<Long, CountryValueBean> countryInfo : countryResults )
          {
            if ( countryInfo != null )
            {
              CountryValueBean country = countryInfo.get( pax.getId() );
              String countryName = "";
              if ( country != null )
              {
                String assetCode = country.getCmAssetCode();
                if ( codes.containsKey( assetCode ) )
                {
                  countryName = codes.get( assetCode );
                }
                else
                {
                  countryName = ContentReaderManager.getText( country.getCmAssetCode(), "COUNTRY_NAME" );
                  codes.put( assetCode, countryName );
                }
                bean.setCountryName( countryName );
                bean.setCountryCode( country.getCountryCode() );
                bean.setCurrency(getUserService().getUserCurrencyCharValue( pax.getId() ) );
              }
            }
          }
          paxSearchListView.getParticipants().add( bean );
        }
      }
    }
    return paxSearchListView;
  }

  @Override
  public ParticipantSearchListView getParticipant( Set<ClaimRecipient> recipients, Long promotionId )
  {
    ParticipantSearchListView listViewObj = new ParticipantSearchListView();
    List<Long> paxIds = new ArrayList<Long>();
    List<Participant> paxs = new ArrayList<Participant>();

    for ( ClaimRecipient claimRecipient : recipients )
    {
      Participant participant = claimRecipient.getRecipient();
      paxIds.add( participant.getId() );
      paxs.add( participant );
    }

    Long[] ids = paxIds.toArray( new Long[paxIds.size()] );
    if ( ids.length > 0 )
    {
      List<Map<Long, CountryValueBean>> countryResults = populateCountriesForUsers( ids );
      listViewObj = buildParticipantSearchListView( paxs, promotionId, countryResults );
    }
    else
    {
      addMessageToListView( listViewObj, "participant.search.NO_RESULTS" );
    }
    return listViewObj;
  }

  public ParticipantSearchListView getTeamParticipants( Set<ProductClaimParticipant> claimParticipants, Long promotionId )
  {
    ParticipantSearchListView listViewObj = new ParticipantSearchListView();
    List<Long> paxIds = new ArrayList<Long>();
    List<Participant> paxs = new ArrayList<Participant>();

    for ( ProductClaimParticipant claimParticipant : claimParticipants )
    {
      Participant participant = claimParticipant.getParticipant();
      paxIds.add( participant.getId() );
      paxs.add( participant );
    }

    Long[] ids = paxIds.toArray( new Long[paxIds.size()] );
    if ( ids.length > 0 )
    {
      List<Map<Long, CountryValueBean>> countryResults = populateCountriesForUsers( ids );
      listViewObj = buildParticipantSearchListView( paxs, promotionId, countryResults );
    }
    else
    {
      addMessageToListView( listViewObj, "participant.search.NO_RESULTS" );
    }
    return listViewObj;
  }

  public List<Long> getAllActivePaxIdsInCampaignForEstatements( String campaignNumber, String sendOnlyPaxWithPoints, Long startingUserId )
  {
    return participantDAO.getAllActivePaxIdsInCampaignForEstatements( campaignNumber, sendOnlyPaxWithPoints, startingUserId );
  }

  public Long getPendingNominationCountForApprover( Long approverId )
  {
    return participantDAO.getPendingNominationCountForApprover( approverId );
  }

  public List<Long> getAllEligibleApproversForCustomApproval( Long promotionId )
  {
    return participantDAO.getAllEligibleApproversForCustomApproval( promotionId );
  }

  @Override
  public String getParticipantCurrencyCode( Long paxId )
  {
    Participant pax = getParticipantById( paxId );
    if ( pax.getPrimaryAddress() != null && pax.getPrimaryAddress().getAddress() != null && pax.getPrimaryAddress().getAddress().getCountry() != null )
    {
      return pax.getPrimaryAddress().getAddress().getCountry().getCurrencyCode();
    }

    return "";
  }

  /**
   * Remove ParticipantFollowers
   * @param removeParticiapantFollower
   * @return void
   * @throws ServiceErrorException
   */
  @Override
  public void removeParticipantFollowee( ParticipantFollowers removeParticiapantFollower ) throws ServiceErrorException
  {
    getParticipantDAO().removeParticipantFollowee( removeParticiapantFollower );
  }

  /**
   * Add ParticipantFollowers
   * @param addParticiapantFollower
   * @return void
   * @throws ServiceErrorException
   */
  @Override
  public void addParticipantFollowee( ParticipantFollowers addParticiapantFollower ) throws ServiceErrorException
  {
    getParticipantDAO().addParticipantFollowee( addParticiapantFollower );
  }

  /**
   * Get ParticipantFollowers by participantId and followerId
   * @param participantId
   * @param followerId
   * @return ParticipantFollowers
   * @throws ServiceErrorException
   */
  @Override
  public ParticipantFollowers getById( Long participantId, Long followerId ) throws ServiceErrorException
  {
    return getParticipantDAO().getById( participantId, followerId );
  }

  /** 
   * @return
   */
  private static AutoCompleteService getAutoCompleteService()
  {
    return (AutoCompleteService)BeanLocator.getBean( AutoCompleteService.BEAN_NAME );
  }

  private static HCServices getHCServices()
  {
    return (HCServices)BeanLocator.getBean( HCServices.BEAN_NAME );
  }

  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)BeanLocator.getBean( ParticipantDAO.BEAN_NAME );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );
  }

  private static MailingService getMailingService()
  {
    return (MailingService)BeanLocator.getBean( MailingService.BEAN_NAME );
  }

  @Override
  public List<Long> getFollowersByUserId( Long userId )
  {
    return participantDAO.getFollowersByUserId( userId );
  }

  @Override
  public List<Long> getAllParticipantIDsByLastName( String creatorLastName )
  {
    return participantDAO.getAllParticipantIDsByLastName( creatorLastName );
  }

  @Override
  public String getLNameFNameByPaxIdWithComma( Long contestOwnerId )
  {
    return this.participantDAO.getLNameFNameByPaxIdWithComma( contestOwnerId );
  }

  @Override
  public String getParticipantPublicUrl( Long paxId )
  {
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "paxId", paxId );
    parameterMap.put( "isFullPage", "true" );
    return ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, parameterMap );
  }

  @Override
  public List<Long> findPaxIdsWhoDisabledPublicProfile( List<Long> forPaxIds )
  {
    return participantDAO.findPaxIdsWhoDisabledPublicProfile( forPaxIds );
  }

  @Override
  public boolean isPaxOptedOutOfAwards( Long paxId )
  {
    return participantDAO.isPaxOptedOutOfAwards( paxId );
  }

  private UnderArmourService getUnderArmourService()
  {
    return (UnderArmourService)BeanLocator.getBean( UnderArmourService.BEAN_NAME );
  }

  @Override
  public List<PaxContactType> getValidUserContactMethodsByEmail( String email )
  {
    return participantDAO.getValidUserContactMethodsByEmail( email );
  }

  @Override
  public AccountSyncRequest getGToHoneycombSyncPaxData( List<Long> userIds )
  {
    // Username -> Cursor Name -> Column Name -> Value
    Map<String, Map<String, Map<String, Object>>> data = new HashMap<>( userIds.size() );
    // Cursor Name -> List<Map<COLUMN_NAME, VALUE>>
    Map<String, Object> procedureResults = participantDAO.getGToHoneycombSyncPaxData( userIds );

    procedureResults.forEach( ( cursorName, entryValue ) ->
    {
      // Skip return code output
      if ( cursorName.equals( "p_out_return_code" ) )
      {
        return;
      }

      // Go from map of result sets to mapping by participant
      List<Map<String, Object>> cursorResults = (List<Map<String, Object>>)entryValue;
      cursorResults.forEach( cursorResult ->
      {
        String username = (String)cursorResult.get( "user_name" );
        data.putIfAbsent( username, new HashMap<>() );
        data.get( username ).putIfAbsent( cursorName, new HashMap<>() );
        cursorResult.forEach( ( columnName, columnValue ) ->
        {
          data.get( username ).get( cursorName ).put( columnName, columnValue );
        } );
      } );
    } );

    // Go from raw map construct to domain object
    AccountSyncRequest accountSyncRequest = new AccountSyncRequest();
    data.forEach( ( username, userDataMap ) ->
    {
      AccountSyncParticipantDetails participantDetails = new AccountSyncParticipantDetails();
      participantDetails.setUserId( ( (BigDecimal)userDataMap.get( "p_out_application_user" ).get( "user_id" ) ).longValue() );
      participantDetails.setUserName( (String)userDataMap.get( "p_out_application_user" ).get( "user_name" ) );
      participantDetails.setFirstName( (String)userDataMap.get( "p_out_application_user" ).get( "first_name" ) );
      participantDetails.setLastName( (String)userDataMap.get( "p_out_application_user" ).get( "last_name" ) );
      participantDetails.setAttributeMaps( userDataMap );
      accountSyncRequest.getParticipantDetails().add( participantDetails );
    } );

    return accountSyncRequest;
  }

  @Override
  public List<PaxContactType> getValidUniqueUserContactMethodsByUserId( Long userId )
  {
    return participantDAO.getValidUniqueUserContactMethodsByUserId( userId );
  }

  @Override
  public List<PaxContactType> getValidUniqueUserContactMethodsByEmailOrPhone( String emailOrPhone )
  {
    return participantDAO.getValidUniqueUserContactMethodsByEmailOrPhone( emailOrPhone );
  }

  @Override
  public List<PaxContactType> getValidUserContactMethodsByPhone( String phoneNumber )
  {
    return participantDAO.getValidUserContactMethodsByPhone( phoneNumber );
  }

  @Override
  public List<PaxContactType> getAdditionalContactMethodsByEmailOrPhone( String emailOrPhone )
  {
    return participantDAO.getAdditionalContactMethodsByEmailOrPhone( emailOrPhone );
  }

  @Override
  public void saveAccountSyncResponse( ParticipantSyncResponse syncResponse )
  {
    syncResponse.getSuccessfulUsernames().forEach( ( username, data ) ->
    {
      Participant participant = getParticipantByUserName( username );
      if ( participant != null )
      {
        participant.setHoneycombUserId( data.getHoneycombUserId() );
        participantDAO.saveParticipant( participant );
      }
    } );
  }

  @Override
  public List<PaxContactType> getValidUserContactMethodsByUserId( Long userId )
  {
    return participantDAO.getValidUserContactMethodsByUserId( userId );
  }

  @Override
  public boolean isParticipantRecoveryContactsAvailable( Long paxId )
  {
    return participantDAO.isParticipantRecoveryContactsAvailable( paxId );
  }

  @Override
  public List<PaxContactType> getContactsAutocomplete( String initialQuery, String searchQuery )
  {
    // Two procedures - one for email, one for phone
    // Initial query was already chosen by the user, so it is an existing contact. So it's valid.
    // We can reliably decide which procedure to use
    if ( EmailValidator.getInstance().isValid( initialQuery ) )
    {
      return participantDAO.getContactsAutocompleteEmail( initialQuery, searchQuery );
    }
    else
    {
      // Strip leading zeros from the initial phone, for country codes.
      // Can't do this with searchQuery, can't tell if it's a phone or email.
      initialQuery = initialQuery.replaceFirst( "^0+", "" );
      return participantDAO.getContactsAutocompletePhone( initialQuery, searchQuery );
    }
  }

  public String getHeroModuleAudienceTypeByUserId( Long userId )
  {
    return participantDAO.getHeroModuleAppAudienceTypeByUserId( userId );
  }

  public List<AlertsValueBean> getUnverifiedRecoveryAlerts( Participant participant )
  {
    List<AlertsValueBean> buildRecoveryContact = new ArrayList<AlertsValueBean>();
    boolean hasVerifiedRecoveryContact = false;
    boolean hasRecoveryContact = false;
    UserEmailAddress userEmailAddress = participant.getEmailAddressByType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
    if ( userEmailAddress != null )
    {
      hasRecoveryContact = true;
      if ( VerificationStatusType.VERIFIED.equals( userEmailAddress.getVerificationStatus().getCode() ) )
      {
        hasVerifiedRecoveryContact = true;
      }
    }
    if ( !hasVerifiedRecoveryContact )
    {
      UserPhone userPhone = participant.getPhoneByType( PhoneType.lookup( PhoneType.RECOVERY ) );
      if ( userPhone != null )
      {
        hasRecoveryContact = true;
        if ( VerificationStatusType.VERIFIED.equals( userPhone.getVerificationStatus().getCode() ) )
        {
          hasVerifiedRecoveryContact = true;
        }
      }
    }

    if ( !hasVerifiedRecoveryContact && hasRecoveryContact )
    {
      AlertsValueBean alertBean = new AlertsValueBean();
      alertBean.setActivityType( ActivityType.UNVERIFIED_RECOVERY_CONTACT_ALERT );
      buildRecoveryContact.add( alertBean );
    }

    return buildRecoveryContact;
  }

  @Override
  public void sendRecoveryChangeNotification( Long userId, String previousPhoneCountryCode, String previousPhoneNbr, String newPhoneNbr, String previousEmailAddr, String newEmailAddr )
  {
    // Figure out if this notification even needs to be sent. Send if changed existing, added
    // second, or
    // removed existing
    boolean sendNotification = false;
    // Changed existing phone number
    if ( !StringUtils.isEmpty( previousPhoneNbr ) && !StringUtils.isEmpty( newPhoneNbr ) && !previousPhoneNbr.equals( newPhoneNbr ) )
    {
      sendNotification = true;
    }
    // Changed existing email address
    else if ( !StringUtils.isEmpty( previousEmailAddr ) && !StringUtils.isEmpty( newEmailAddr ) && !previousEmailAddr.equals( newEmailAddr ) )
    {
      sendNotification = true;
    }
    // Added second (added phone)
    else if ( !StringUtils.isEmpty( previousEmailAddr ) && StringUtils.isEmpty( previousPhoneNbr ) && !StringUtils.isEmpty( newPhoneNbr ) )
    {
      sendNotification = true;
    }
    // Added second (added email)
    else if ( !StringUtils.isEmpty( previousPhoneNbr ) && StringUtils.isEmpty( previousEmailAddr ) && !StringUtils.isEmpty( newEmailAddr ) )
    {
      sendNotification = true;
    }
    // Removed phone
    else if ( !StringUtils.isEmpty( previousPhoneNbr ) && StringUtils.isEmpty( newPhoneNbr ) )
    {
      sendNotification = true;
    }
    // Removed email
    else if ( !StringUtils.isEmpty( previousEmailAddr ) && StringUtils.isEmpty( newEmailAddr ) )
    {
      sendNotification = true;
    }

    if ( !sendNotification )
    {
      return;
    }

    // The notification is sent to the primary email, previous recovery contact, and unchanged
    // recovery contacts
    // Not to the new recovery contact
    Participant participant = getParticipantById( userId );

    UserEmailAddress primaryEmailAddress = participant.getPrimaryEmailAddress();
    if ( primaryEmailAddress != null )
    {
      PaxContactType primaryContact = buildRecoveryNotificationContactType( ContactType.EMAIL, primaryEmailAddress.getEmailAddr() );
      Mailing primaryEmailMailing = getMailingService().buildRecoveryNotificationMailing( userId, primaryContact );
      getMailingService().submitMailing( primaryEmailMailing, null, userId );
    }

    if ( !StringUtils.isEmpty( previousEmailAddr ) )
    {
      PaxContactType previousEmailContact = buildRecoveryNotificationContactType( ContactType.EMAIL, previousEmailAddr );
      Mailing previousEmailMailing = getMailingService().buildRecoveryNotificationMailing( userId, previousEmailContact );
      getMailingService().submitMailing( previousEmailMailing, null, userId );
    }

    if ( !StringUtils.isEmpty( previousPhoneNbr ) )
    {
      String txtMessage = getMailingService().buildRecoveryNotificationText( participant );
      getMailingService().sendSmsMessage( userId, previousPhoneCountryCode, previousPhoneNbr, txtMessage );
    }
  }

  private PaxContactType buildRecoveryNotificationContactType( ContactType contactType, String value )
  {
    // Recovery notification method only needs the type, value, and unique flag
    PaxContactType paxContactType = new PaxContactType();
    paxContactType.setContactType( contactType );
    paxContactType.setValue( value );
    boolean unique = contactType == ContactType.EMAIL ? userService.isUniqueEmail( value ) : userService.isUniquePhoneNumber( value );
    paxContactType.setUnique( unique );
    return paxContactType;
  }
  /* coke customization start */
  public boolean isOptedOut( Long userId )
  {
    return participantDAO.isOptedOut( userId );
  }
  /* coke customization end */
  public List<Long> getAllEligibleApproversForCustomApprovalWithOpenClaims( Long promotionId )
  {
    return participantDAO.getAllEligibleApproversForCustomApprovalWithOpenClaims( promotionId );
  }
//Client customization for wip #26532 starts
 public boolean isAllowePurlOutsideInvites( Long participantId )
 {
   return participantDAO.isAllowePurlOutsideInvites( participantId );
 }
 // Client customization for wip #26532 ends
  @Override
  public List<UserNode> getAllManagerAndOwner()
  {
    return participantDAO.getAllManagerAndOwner();
  }

  @Override
  public Map inActivateBiwUsers( Long runByuserId )
  {
    return participantDAO.inActivateBiwUsers( runByuserId );
  }
  
  //client customization start - wip 52159
  public List<Participant> getUsersByCharacteristicIdAndValue(Long charId, String charValue )
  {
	  return participantDAO.getUsersByCharacteristicIdAndValue(charId, charValue);
  }
  //client customization end - wip 52159
  /* Customization for WIP 39735 starts here */
  @Override
  public List<Participant> getNodeMemberForPurlMgrRecipient( Long purlRecipientId )
  {
    return participantDAO.getNodeMemberForPurlMgrRecipient( purlRecipientId );
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  protected static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  /* Customization for WIP 39735 ends here */
  
  //Client customization tuning start
  public List<String> getByPositionTypeForAutoComplete( String startsWith )
  {
    return participantDAO.getByPositionTypeForAutoComplete( startsWith );
  }
  
  public List<String> getByDepartmentTypeForAutoComplete( String startsWith )
  {
    return participantDAO.getByDepartmentTypeForAutoComplete( startsWith );
  }
  //Client customization end
  
  public List<ClientPublicRecognitionDeptBean> getAllActiveDepartmentsForPublicRecognition()
  {
    return participantDAO.getAllActiveDepartmentsForPublicRecognition();
  }
}

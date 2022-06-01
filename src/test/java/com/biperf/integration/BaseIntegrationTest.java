
package com.biperf.integration;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.indexing.BIElasticSearchIndexerService;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.ui.profile.ProfileRPMController;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.ResourceManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.cms.ContentReaderUtils;
import com.biperf.core.utils.hibernate.SessionWrapper;
import com.biperf.core.value.promo.PromotionBasics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.BeanLocator;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.ContentReaderFactory;
import com.objectpartners.cms.util.ContentReaderManager;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = { "classpath:datasourceContext-test.xml",
                                     "classpath:dataAccessContext-hibernate.xml",
                                     // --------------------------
                                     "classpath:dataAccessContext-hibernate-properties-test.xml",
                                     "classpath:applicationContext-processes.xml",
                                     "classpath:applicationContext-jms-test.xml",
                                     "classpath:webservicesContext.xml",
                                     "classpath:applicationContext-fileload-template.xml",
                                     // ---------------
                                     "classpath:cmsConfigBeaconApplicationContext-test.xml",
                                     "classpath:applicationContext.xml",
                                     "classpath:springWeb-servlet.xml"

} )
public class BaseIntegrationTest
{

  public static Long PAX_ID = 5583L;
  static final String HIBERNATE_SESSION_INTERCEPTOR_BEAN = "hibernateSessionInterceptor";

  private boolean containerFullyLoaded = false;

  ContentReader reader = null;

  @Resource
  public ApplicationContext applicationContext;

  @Resource
  ParticipantService participantService;

  @Resource
  ContentReaderFactory readerFactory;

  @Resource
  CmsConfiguration cmsConfiguration;

  @Resource( name = "userService" )
  UserService userService;

  @Resource
  ClaimDAO dao;

  @Resource
  ImageCropStrategy strategy;

  public Transaction tx;

  @Autowired
  ProfileRPMController profileRPMAction;

  @Autowired
  AutoCompleteService autoCompleteService;

  @Autowired
  BIElasticSearchIndexerService search;

  @BeforeClass
  public static void beforeClass()
  {
    System.setProperty( Environment.ENV_PROPERTY_NAME, Environment.ENV_DEV );
    UserManager.setUser( getDefaultAuthenticatedUser() );
  }

  @AfterClass
  public static void afterClass() throws Exception
  {
    UserManager.removeUser();
  }

  /*
   * @Test public void verifyContextFullyLoaded() throws ServiceErrorException { User user =
   * userService.getUserById( PAX_ID ); user.getPrimaryAddress().getAddress().getAddr1(); }
   */

  @Before
  public void beforeEachTest() throws Exception
  {
    Session session = null;

    if ( !containerFullyLoaded )
    {
      ResourceManager.initResources();
      PickListItem.setPickListFactory( new MockPickListFactory() );
      ApplicationContextFactory.setApplicationContext( applicationContext );
      ApplicationContextFactory.setContentManagerApplicationContext( applicationContext );
      BeanLocator.setApplicationContext( applicationContext );
      // -----------------------

      ContentReaderUtils.prepareContentReader( cmsConfiguration );
      reader = ContentReaderManager.getContentReader();
      containerFullyLoaded = true;

    }

    if ( !HibernateSessionManager.isSessionDefined() )
    {
      if ( !ResourceManager.hasResource( "HibernateSession" ) )
      {
        session = ( (SessionFactory)ApplicationContextFactory.getApplicationContext().getBean( "sessionFactory" ) ).openSession();
        ResourceManager.bindResource( "HibernateSession", new SessionWrapper( session ) );
      }
    }

    if ( session != null )
    {
      session.clear();
      tx = session.beginTransaction();
    }

  }

  @After
  public void afterEachTest() throws Exception
  {
    if ( HibernateSessionManager.isSessionDefined() )
    {
      HibernateSessionManager.getSession().clear();
      HibernateSessionManager.getSession().flush();

      if ( tx != null )
      {
        tx.rollback();
      }
    }
  }

  public boolean validJSON( String jsonString )
  {
    try
    {
      final ObjectMapper mapper = new ObjectMapper();
      mapper.readTree( jsonString );
      return true;
    }
    catch( IOException e )
    {
      return false;
    }

  }

  public static AuthenticatedUser getDefaultAuthenticatedUser()
  {
    AuthenticatedUser user = new AuthenticatedUser();
    user.setUserId( new Long( PAX_ID ) );
    user.setTimeZoneId( TimeZone.getDefault().getID() );
    UserManager.setUser( user );
    ClientStatePasswordManager.setPassword( "password" );
    return user;
  }

  public void addAuthenticatedUserToResourceManager( Participant sender )
  {
    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( sender.getId() );
    authUser.setTimeZoneId( TimeZone.getDefault().getID() );
    UserManager.setUser( authUser );
    ClientStatePasswordManager.setPassword( "password" );
  }

  public static NominationPromotion buildNominationPromotion( String suffix, PromotionStatusType promoStatus, ClaimFormStatusType claimFormStatusType, ClaimFormModuleType claimFormModuleType )
  {
    NominationPromotion promotion = PromotionDAOImplTest.buildNominationPromotion( suffix );
    ClaimForm claimForm = promotion.getClaimForm();
    claimForm.setClaimFormStatusType( claimFormStatusType );
    claimForm.setClaimFormModuleType( claimFormModuleType );
    promotion.setPromotionStatus( promoStatus );
    return promotion;

  }

  public static NominationPromotion populateNomPromoBasics( NominationPromotion promotion, PromotionBasics basics )
  {
    return promotion;

  }

  public static void flushAndClearSession()
  {
    Session session = HibernateSessionManager.getSession();
    session.flush();
    session.clear();
  }

  @Test
  public void verifyContextLoadedSucccessfully()
  {
    assertNotNull( autoCompleteService );
    assertNotNull( search );

  }

}

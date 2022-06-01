
package com.biperf.core.service.goalquest.impl;

import java.util.Locale;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;

import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.GoalPayoutStrategyFactory;
import com.biperf.core.service.promotion.engine.ManagerOverrideGoalStrategyFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biw.hc.core.service.HCServices;

public class BaseGQTest extends BaseServiceTest
{
  protected GoalQuestServiceImpl testInstance = null;
  protected IMocksControl mockControl = null;

  // Mock all external services
  protected PaxGoalService mockPaxGoalService = null;
  protected UserService mockUserService = null;
  protected ParticipantService mockParticipantService = null;
  protected AudienceService mockAudienceService = null;
  protected SystemVariableService mockSystemVariableService = null;
  protected MessageService mockMessageService = null;
  protected MailingService mockMailingService = null;
  protected PromotionService mockPromotionService = null;
  protected GoalQuestPaxActivityService mockGoalQuestPaxActivityService = null;
  protected ListBuilderService mockListBuilderService = null;
  protected HierarchyService mockHierarchyService = null;
  protected HCServices mockHCServices = null;

  protected GoalPayoutStrategyFactory mockGoalPayoutStrategyFactory = null;
  protected ManagerOverrideGoalStrategyFactory mockManagerOverrideGoalStrategyFactory = null;

  /**
   * {@inheritDoc}
   * 
   * <p>This particular implementation serves as a base setup for 
   * {@link GoalQuestServiceImpl} tests. It creates a test instance
   *  of {@link GoalQuestServiceImpl} and mocks out all of its 
   *  external services.</p>
   */
  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    mockControl = EasyMock.createControl();

    mockPaxGoalService = mockControl.createMock( PaxGoalService.class );
    mockUserService = mockControl.createMock( UserService.class );
    mockParticipantService = mockControl.createMock( ParticipantService.class );
    mockAudienceService = mockControl.createMock( AudienceService.class );
    mockSystemVariableService = mockControl.createMock( SystemVariableService.class );
    mockMessageService = mockControl.createMock( MessageService.class );
    mockMailingService = mockControl.createMock( MailingService.class );
    mockPromotionService = mockControl.createMock( PromotionService.class );
    mockGoalQuestPaxActivityService = mockControl.createMock( GoalQuestPaxActivityService.class );
    mockListBuilderService = mockControl.createMock( ListBuilderService.class );
    mockHierarchyService = mockControl.createMock( HierarchyService.class );
    mockHCServices = mockControl.createMock( HCServices.class );

    mockGoalPayoutStrategyFactory = mockControl.createMock( GoalPayoutStrategyFactory.class );
    mockManagerOverrideGoalStrategyFactory = mockControl.createMock( ManagerOverrideGoalStrategyFactory.class );

    testInstance = new GoalQuestServiceImpl()
    {
      public HCServices getHCServices()
      {
        return mockHCServices;
      }
      
      Locale getUserLocale()
      {
        return Locale.US;
      }
    };
    testInstance.setPaxGoalService( mockPaxGoalService );
    testInstance.setUserService( mockUserService );
    testInstance.setParticipantService( mockParticipantService );
    testInstance.setAudienceService( mockAudienceService );
    testInstance.setSystemVariableService( mockSystemVariableService );
    testInstance.setMessageService( mockMessageService );
    testInstance.setMailingService( mockMailingService );
    testInstance.setPromotionService( mockPromotionService );
    testInstance.setGoalQuestPaxActivityService( mockGoalQuestPaxActivityService );
    testInstance.setListBuilderService( mockListBuilderService );
    testInstance.setHierarchyService( mockHierarchyService );
    testInstance.setGoalStrategyFactory( mockGoalPayoutStrategyFactory );
    testInstance.setManagerOverrideGoalStrategyFactory( mockManagerOverrideGoalStrategyFactory );

  }

  @Override
  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  @Test
  public void testFoo()
  {
    // Avoiding warning when there are no tests in a class
    assertTrue( true );
  }
}

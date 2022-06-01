
package com.biperf.core.process;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.easymock.Capture;
import org.easymock.EasyMock;

import com.biperf.core.dao.nomination.NominationDAO;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.process.ProcessInvocationService;
import com.biperf.core.service.system.SystemVariableService;

public class RefreshPendingNominationApproverProcessTest extends BaseServiceTest
{

  public RefreshPendingNominationApproverProcess classUnderTest = new RefreshPendingNominationApproverProcess();

  NominationDAO nominationDAO = createNiceMock( NominationDAO.class );
  MessageService messageService = createNiceMock( MessageService.class );
  MailingService mailingService = createNiceMock( MailingService.class );
  ProcessInvocationService processInvocationService = createNiceMock( ProcessInvocationService.class );
  SystemVariableService systemVariableService = createNiceMock( SystemVariableService.class );

  public void setUp() throws Exception
  {
    super.setUp();

    classUnderTest.setNominationDAO( nominationDAO );
    classUnderTest.setMessageService( messageService );
    classUnderTest.setSystemVariableService( systemVariableService );
    classUnderTest.setMailingService( mailingService );
    classUnderTest.setProcessInvocationService( processInvocationService );

  }

  public void testProcessSuccessCase()
  {
    Map<String, Object> prcResult = getPrcResult( 1 );

    PropertySetItem propertySet = new PropertySetItem();
    propertySet.setStringVal( "strvalue" );

    Message message = new Message();

    User user = getUser();

    ProcessInvocation processInvocation = getProcessInvocation( user );

    expect( nominationDAO.refreshPendingNominationApprover( anyObject() ) ).andReturn( prcResult ).times( 1 );
    expect( systemVariableService.getPropertyByName( anyObject() ) ).andReturn( propertySet ).times( 2 );
    expect( messageService.getMessageByCMAssetCode( anyObject() ) ).andReturn( message ).times( 1 );
    expect( processInvocationService.getProcessInvocationById( anyObject(), anyObject() ) ).andReturn( processInvocation );

    Capture<Mailing> mailcap = new Capture<Mailing>();
    Capture<Map<String, String>> paramCap = new Capture<Map<String, String>>();

    expect( mailingService.submitMailing( capture( mailcap ), capture( paramCap ) ) ).andReturn( null ).times( 1 );
    replay( nominationDAO, messageService, mailingService, processInvocationService, systemVariableService );

    classUnderTest.onExecute();

    EasyMock.verify( nominationDAO, messageService, mailingService, processInvocationService, systemVariableService );

    Map<String, String> map = paramCap.getValue();
    assertTrue( map.size() > 0 );

    String string = map.get( "status" );
    assertTrue( string.equalsIgnoreCase( "Success" ) );

  }

  public void testProcessFailureCase()
  {
    Map<String, Object> prcResult = getPrcResult( 99 );

    PropertySetItem propertySet = new PropertySetItem();
    propertySet.setStringVal( "strvalue" );

    Message message = new Message();

    User user = getUser();

    ProcessInvocation processInvocation = getProcessInvocation( user );

    expect( nominationDAO.refreshPendingNominationApprover( anyObject() ) ).andReturn( prcResult ).times( 1 );
    expect( systemVariableService.getPropertyByName( anyObject() ) ).andReturn( propertySet ).times( 2 );
    expect( messageService.getMessageByCMAssetCode( anyObject() ) ).andReturn( message ).times( 1 );
    expect( processInvocationService.getProcessInvocationById( anyObject(), anyObject() ) ).andReturn( processInvocation );

    Capture<Mailing> mailcap = new Capture<Mailing>();
    Capture<Map<String, String>> paramCap = new Capture<Map<String, String>>();

    expect( mailingService.submitMailing( capture( mailcap ), capture( paramCap ) ) ).andReturn( null ).times( 1 );
    replay( nominationDAO, messageService, mailingService, processInvocationService, systemVariableService );

    classUnderTest.onExecute();

    EasyMock.verify( nominationDAO, messageService, mailingService, processInvocationService, systemVariableService );

    Map<String, String> map = paramCap.getValue();
    assertTrue( map.size() > 0 );

    String string = map.get( "status" );
    assertTrue( string.equalsIgnoreCase( "Failed" ) );

  }

  private ProcessInvocation getProcessInvocation( User user )
  {
    ProcessInvocation processInvocation = new ProcessInvocation();
    processInvocation.setRunAsUser( user );
    return processInvocation;
  }

  private User getUser()
  {
    User user = new User();
    user.setFirstName( "firstName" );
    user.setLastName( "lastName" );
    return user;
  }

  private Map<String, Object> getPrcResult( Integer p_out_returncode )
  {
    Map<String, Object> prcResult = new HashMap<String, Object>();
    prcResult.put( "p_out_returncode", new BigDecimal( p_out_returncode.intValue() ) );
    return prcResult;
  }

}

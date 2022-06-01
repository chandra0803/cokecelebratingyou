
package com.biperf.core.process;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;

public class CashCurrencyProcessTest extends BaseProcessTest
{

  private CashCurrencyProcess cashCurrencyProcessTarget;

  private AwardBanQServiceFactory awardBanQServiceFactoryMock;
  private AwardBanQService awardBanQServiceMock;

  protected void setUp()
  {
    // The factory would be pretty lame to mock, so we're going to nest it and always work off the
    // service mock
    awardBanQServiceFactoryMock = createMock( AwardBanQServiceFactory.class );
    awardBanQServiceMock = createMock( AwardBanQService.class );
    expect( awardBanQServiceFactoryMock.getAwardBanQService() ).andReturn( awardBanQServiceMock ).anyTimes();
    replay( awardBanQServiceFactoryMock );

    cashCurrencyProcessTarget = new CashCurrencyProcess();
    cashCurrencyProcessTarget.setAwardBanQServiceFactory( awardBanQServiceFactoryMock );
  }

  @Test
  public void testCashCurrencyProcess() throws Exception
  {
    Map<String, Object> parameters = new HashMap<>();
    Long invocationId = getTestProcessInvocationId();

    awardBanQServiceMock.updateCashCurrencies();
    expectLastCall();
    replay( awardBanQServiceMock );

    // Execute the process
    cashCurrencyProcessTarget.setSkipMessageFlag( true );
    cashCurrencyProcessTarget.execute( parameters, invocationId );
  }

}


package com.biperf.integration.ui.claim.nom;

import static com.biperf.core.domain.enums.nomination.NominationsInProgressConstants.CLAIM_ID;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.biperf.core.domain.enums.nomination.NominationClaimsInProgressSortColumn;
import com.biperf.core.ui.nomination.NomInProgressForm;
import com.biperf.core.ui.nomination.NominationInProgressAction;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.integration.BaseIntegrationTest;

public class NominationInProgressActionIntegTest extends BaseIntegrationTest
{

  HttpServletRequest httpRequest = createMock( HttpServletRequest.class );
  HttpServletResponse httpResponse = createNiceMock( HttpServletResponse.class );

  NominationInProgressAction testClass = new NominationInProgressAction();

  public void beforeEach() throws Exception
  {
    super.beforeEachTest();
  }

  @Test
  public void getInProgressNomsWithOutAnyFilters() throws Exception
  {
    NomInProgressForm form = new NomInProgressForm();
    StringWriter strWriter = new StringWriter();
    PrintWriter writer = new PrintWriter( strWriter );

    expect( httpResponse.getWriter() ).andReturn( writer ).times( 1 );
    replay( httpResponse );

    httpRequest = createMock( HttpServletRequest.class );
    expect( httpRequest.getParameter( "sortedOn" ) ).andReturn( NominationClaimsInProgressSortColumn.DATE_STARTED.getDisplayValue() );
    expect( httpRequest.getParameter( "currentPage" ) ).andReturn( "1" ).times( 2 );
    expect( httpRequest.getParameter( "sortedBy" ) ).andReturn( null );
    replay( httpRequest );

    testClass.getInProgressNoms( null, form, httpRequest, httpResponse );
    assertTrue( validJSON( strWriter.toString() ) );
    System.out.println( strWriter.toString() );
    verify( httpRequest );
    verify( httpResponse );

  }

  @Test
  public void getInProgressNomsWithFilters() throws Exception
  {
    StringWriter strWriter = new StringWriter();
    PrintWriter writer = new PrintWriter( strWriter );

    httpRequest = createMock( HttpServletRequest.class );
    expect( httpRequest.getParameter( "sortedOn" ) ).andReturn( NominationClaimsInProgressSortColumn.DATE_STARTED.getDisplayValue() );
    expect( httpRequest.getParameter( "currentPage" ) ).andReturn( "1" ).times( 2 );
    expect( httpRequest.getParameter( "sortedBy" ) ).andReturn( null );
    replay( httpRequest );

    expect( httpResponse.getWriter() ).andReturn( writer ).times( 1 );
    replay( httpResponse );
    NomInProgressForm form = new NomInProgressForm();
    form.setStartIndex( 50 );

    testClass.getInProgressNoms( null, form, httpRequest, httpResponse );
    assertTrue( validJSON( strWriter.toString() ) );
    System.out.println( strWriter.toString() );
    verify( httpResponse );

  }

  @Test
  public void removeInProgressClaim() throws Exception
  {
    Map<String, Object> clientState = new HashMap<String, Object>();
    clientState.put( CLAIM_ID, 10010 );
    String serialize = ClientStateSerializer.serialize( clientState, ClientStatePasswordManager.getPassword() );
    StringWriter strWriter = new StringWriter();
    PrintWriter writer = new PrintWriter( strWriter );

    expect( httpRequest.getParameter( "removeParams" ) ).andReturn( serialize );
    expect( httpResponse.getWriter() ).andReturn( writer ).anyTimes();
    replay( httpResponse );
    replay( httpRequest );

    testClass.remove( null, null, httpRequest, httpResponse );

    assertTrue( validJSON( strWriter.toString() ) );
    System.out.println( strWriter.toString() );
    verify( httpResponse );
    verify( httpRequest );
  }

}

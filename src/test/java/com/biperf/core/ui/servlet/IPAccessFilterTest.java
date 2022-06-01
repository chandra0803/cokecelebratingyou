
package com.biperf.core.ui.servlet;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA. User: tennant Date: Aug 5, 2005 Time: 4:17:25 PM To change this
 * template use File | Settings | File Templates.
 */
public class IPAccessFilterTest extends TestCase
{

  // TODO uncomment this- there is a dependency between javatest and javaui here, which is not
  // TODO properly resolved by the build.xml file
  // TODO Also, add this to some suite when its ready
  //
  //
  // //Setup our test object. We have to subclass the IPAccessFilter
  // //so we can modify how it looks up the SystemVariables
  // protected IPAccessFilter filterWithBlocking = new IPAccessFilter () {
  // protected String getCSVListOfAllowedIPs() {
  // return "10.*,11.2.3.1";
  // }
  // protected boolean shouldBlockAccess() {
  // return true;
  // }
  // };
  //
  // protected IPAccessFilter filterNoBlocking = new IPAccessFilter () {
  // protected String getCSVListOfAllowedIPs() {
  // return "10.*,11.2.3.1";
  // }
  // protected boolean shouldBlockAccess() {
  // return false;
  // }
  // };
  //
  //
  // private MockControl filterControl;
  // private FilterChain mockFilterChain;
  // private HttpServletRequest mockRequest;
  // private MockControl requestControl;
  // private MockControl responseControl;
  // private HttpServletResponse mockResponse;
  //
  // public void setUp() {
  // filterControl = MockControl.createControl( FilterChain.class);
  // mockFilterChain = (FilterChain)filterControl.getMock();
  // requestControl = MockControl.createControl( HttpServletRequest.class );
  // mockRequest = (HttpServletRequest)requestControl.getMock();
  // responseControl = MockControl.createControl( HttpServletResponse.class );
  // mockResponse = (HttpServletResponse)responseControl.getMock();
  // }
  //
  // public void testIPAccessFilterAllowed () throws Exception {
  //
  // mockFilterChain.doFilter(mockRequest,mockResponse );
  // filterControl.replay();
  //
  // mockRequest.getRequestURL();
  // requestControl.setReturnValue( new StringBuffer("/path.do") );
  // mockRequest.getRemoteAddr();
  // requestControl.setReturnValue( "10.2.1.1");
  // requestControl.replay();
  //
  // responseControl.replay();
  // filterWithBlocking.doFilter(mockRequest,mockResponse, mockFilterChain );
  // }
  //
  // public void testIPAccessFilterNotAllowed () throws Exception {
  //
  // filterControl.replay();
  //
  // mockRequest.getRequestURL();
  // requestControl.setReturnValue( new StringBuffer("/path.do") );
  // mockRequest.getRemoteAddr();
  // requestControl.setReturnValue( "11.2.0.1");
  // requestControl.replay();
  //
  // mockResponse.sendError( HttpServletResponse.SC_SERVICE_UNAVAILABLE );
  // responseControl.replay();
  //
  // filterWithBlocking.doFilter(mockRequest,mockResponse, mockFilterChain );
  // }
  //
  // public void testIPAccessFilterNotAllowedButNotChecked () throws Exception {
  //
  // mockFilterChain.doFilter(mockRequest,mockResponse );
  // filterControl.replay();
  //
  // mockRequest.getRequestURL();
  // requestControl.setReturnValue( new StringBuffer("/path.do") );
  // mockRequest.getRemoteAddr();
  // requestControl.setReturnValue( "11.2.0.1");
  // requestControl.replay();
  //
  // responseControl.replay();
  //
  // filterNoBlocking.doFilter(mockRequest,mockResponse, mockFilterChain );
  // }
  //

  public void testSample()
  {
    assertTrue( true );
  }

}

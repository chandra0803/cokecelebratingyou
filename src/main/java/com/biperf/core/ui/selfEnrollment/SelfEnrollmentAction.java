
package com.biperf.core.ui.selfEnrollment;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;

/**
 * @author dudam
 * @since Jun 7, 2013
 * @version 1.0
 */
public class SelfEnrollmentAction extends BaseDispatchAction
{

  private static final Log log = LogFactory.getLog( SelfEnrollmentAction.class );
  private static final int DEFAULT_PAGE_NUM = 1;

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    log.info( "Unspecified Method" );
    return this.display( mapping, form, request, response );
  }

  // admin method
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    log.info( "Display Method" );
    request.setAttribute( "nodesToGenerateCodes", getNodeService().isAnyNodeWithOutEnrollmentCode() );
    Integer count = getNodeService().getNodesHavingEnrollmentCodesCount();
    request.setAttribute( "nodesToViewCodes", count > 0 ? true : false );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  // admin method
  public ActionForward generateCodes( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    log.info( "Generate Codes Method" );
    List<Node> nodes = getNodeService().generateEnrollmentCodes();
    response.setHeader( "content-type", "text/csv" );
    response.setHeader( "Content-Disposition", "attachment; filename=selfenrollmentCodes.csv" );
    StringBuffer stringbuilder = new StringBuffer();
    stringbuilder.append( "\"Code\"" );
    stringbuilder.append( ',' );
    stringbuilder.append( "\"OrgName\"" );
    stringbuilder.append( '\n' );
    for ( Node node : nodes )
    {
      stringbuilder.append( "\"" );
      stringbuilder.append( node.getSelfEnrollmentCode() );
      stringbuilder.append( "\"" );
      stringbuilder.append( ',' );
      stringbuilder.append( "\"" );
      stringbuilder.append( node.getName() );
      stringbuilder.append( "\"" );
      stringbuilder.append( '\n' );
    }
    response.getWriter().write( stringbuilder.toString() );
    response.getWriter().flush();
    response.getWriter().close();
    return null;
  }

  // admin method
  public ActionForward viewCodes( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    log.info( "View Codes Method" );
    ParamEncoder paramEncoder = new ParamEncoder( "node" );
    String pageParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_PAGE );
    String pageNumString = request.getParameter( pageParameterName );
    Integer nodesCount = getNodeService().getNodesHavingEnrollmentCodesCount();
    request.setAttribute( "count", nodesCount );
    List<Node> nodes = null;
    if ( request.getParameter( TableTagParameters.PARAMETER_EXPORTING ) != null )
    {
      nodes = getNodeService().getNodesHavingEnrollmentCodes( 1, nodesCount );
      request.setAttribute( "itemsPerPage", nodesCount );
    }
    else
    {
      nodes = getNodeService().getNodesHavingEnrollmentCodes( getPageNumber( pageNumString ), 20 );
      request.setAttribute( "itemsPerPage", 20 );
    }
    request.setAttribute( "nodes", nodes );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  private int getPageNumber( String pageNumString )
  {
    return pageNumString != null ? Integer.parseInt( pageNumString ) : DEFAULT_PAGE_NUM;
  }

}

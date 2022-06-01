
package com.biperf.core.ui.cms.debug;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.taglib.CMSContentLocator;
import com.biperf.core.ui.taglib.ContentElement;

public class CMSContentLocatorDisplayAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( CMSContentLocatorDisplayAction.class );

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    try
    {
      return display( mapping, actionForm, request, response );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
    return null;
  }

  public ActionForward refresh( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return display( mapping, form, request, response );
  }

  public ActionForward clear( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    CMSContentLocator contentLocator = getCMSContentLocator( request );
    if ( null != contentLocator )
    {
      contentLocator.clear();
    }
    return display( mapping, form, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      CMSContentLocator contentLocator = getCMSContentLocator( request );
      if ( null != contentLocator )
      {
        CMSContentData cmsContentData = buildCMSContentData( request );
        request.setAttribute( "cmsContentData", toJson( cmsContentData ) );
      }
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
    return mapping.findForward( "success" );
  }

  private CMSContentData buildCMSContentData( HttpServletRequest request )
  {
    CMSContentData cmsContentData = new CMSContentData( getCMSContentLocator( request ) );
    if ( request.getParameter( "sortBy" ) != null )
    {
      cmsContentData.setSortBy( request.getParameter( "sortBy" ) );
    }
    if ( request.getParameter( "filterBy" ) != null )
    {
      cmsContentData.setSortBy( request.getParameter( "filterBy" ) );
    }
    return cmsContentData;
  }

  private class CMSContentData
  {
    private int elementCount = 0;
    private int pageCount = 0;
    private String sortBy = "";
    private String filterBy = "";

    private Collection<ContentElement> elements = null;

    public CMSContentData( CMSContentLocator contentLocator )
    {
      this.elementCount = contentLocator.getElementCount();
      this.pageCount = contentLocator.getPageCount();
      this.elements = contentLocator.getContentElements();
    }

    @SuppressWarnings( "unused" )
    public Collection<ContentElement> getElements()
    {
      return this.elements;
    }

    @SuppressWarnings( "unused" )
    public int getElementCount()
    {
      return this.elementCount;
    }

    @SuppressWarnings( "unused" )
    public int getPageCount()
    {
      return this.pageCount;
    }

    public String getSortBy()
    {
      return sortBy;
    }

    public void setSortBy( String sortBy )
    {
      this.sortBy = sortBy;
    }

    public String getFilterBy()
    {
      return filterBy;
    }

    public void setFilterBy( String filterBy )
    {
      this.filterBy = filterBy;
    }

  }

  private CMSContentLocator getCMSContentLocator( HttpServletRequest request )
  {

    CMSContentLocator contentLocator = (CMSContentLocator)request.getSession().getAttribute( CMSContentLocator.CMS_DEBUG_LOCATOR );
    return contentLocator;
  }
}

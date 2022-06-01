
package com.biperf.core.ui.taglib;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

@SuppressWarnings( "serial" )
public class CMSContentLocator implements java.io.Serializable
{
  public static final String CMS_DEBUG_LOCATOR = "CMS_DEBUG_LOCATOR";

  private String sessionId;
  private String url;
  private Map<String, ContentElement> elements = new ConcurrentHashMap<String, ContentElement>();

  public CMSContentLocator( HttpSession session, String url )
  {
    this.sessionId = session.getId();
    this.url = url;
  }

  public CMSContentLocator()
  {
  }

  public void clear()
  {
    elements.clear();
  }

  public void addContentElement( ContentElement element )
  {
    String key = element.getKey();
    element.setUrlPrefix( url );
    if ( elements.containsKey( key ) )
    {
      elements.get( key ).addPage( element.getPages().iterator().next() );
    }
    else
    {
      elements.put( key, element );
    }
  }

  public Collection<ContentElement> getContentElements()
  {
    return elements.values();
  }

  public int getElementCount()
  {
    return elements.size();
  }

  public int getPageCount()
  {
    int count = 0;
    for ( ContentElement element : elements.values() )
    {
      count = count + element.getPages().size();
    }
    return count;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "\nTotal elements: " + getElementCount() );
    sb.append( "\nTotal pages: " + getPageCount() );
    sb.append( "\nSession: " + sessionId );
    for ( ContentElement element : elements.values() )
    {
      sb.append( "\n" + element );
    }
    return sb.toString();
  }
}

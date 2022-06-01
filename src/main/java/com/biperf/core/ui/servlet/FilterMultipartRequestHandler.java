
package com.biperf.core.ui.servlet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.struts.upload.CommonsMultipartRequestHandler;

import com.biperf.core.utils.StringUtil;

public class FilterMultipartRequestHandler extends CommonsMultipartRequestHandler
{

  @SuppressWarnings( "unchecked" )
  protected void addTextParameter( HttpServletRequest request, FileItem item )
  {
    // This takes care of getting the text value and adding it to the element mappings
    super.addTextParameter( request, item );

    String parameter = item.getFieldName();

    // Some request URIs are excluded - matches list in HttpServletRequestFilter
    String requestUri = request.getRequestURI();
    if ( ( requestUri.contains( "/recognitionWizard/validate.do" ) || requestUri.contains( "/nomination/submitNomination.do" ) )
        && ( parameter.endsWith( "drawingData" ) || parameter.endsWith( "cardData" ) ) || requestUri.contains( "/admin/systemVariableEdit.do" ) || requestUri.contains( "/reports/exportToPDF.do" ) )
    {
      return;
    }

    // This is now an array of all parameter values, including recently added one
    String[] values = (String[])super.getTextElements().get( parameter );

    // Sanitize input values
    if ( values != null )
    {
      for ( int i = 0; i < values.length; ++i )
      {
        String value = values[i];

        if ( StringUtil.isNullOrEmpty( value ) )
        {
          continue;
        }

        value = StripXSSUtil.stripXSS( value );

        if ( value.startsWith( "$" ) )
        {
          value = " ";
        }

        values[i] = value;
      }

      // Replace old values with new sanitized ones
      super.getTextElements().put( parameter, values );
    }
  }

}

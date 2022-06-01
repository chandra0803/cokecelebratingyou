
package com.biperf.core.ui.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class TileSetupForm extends BaseActionForm
{
  /**
   * The key to the HTTP request attribute that refers to this form.
   */
  public static final String FORM_NAME = "tileSetupForm";

  private String tileName;

  private String moduleappId;

  private String primaryAudienceType;

  private List primaryAudienceList = new ArrayList();

  private String primaryAudienceId;

  private String method;

  public static String getFormName()
  {
    return FORM_NAME;
  }

  public String getTileName()
  {
    return tileName;
  }

  public void setTileName( String tileName )
  {
    this.tileName = tileName;
  }

  public String getPrimaryAudienceType()
  {
    return primaryAudienceType;
  }

  public void setPrimaryAudienceType( String primaryAudienceType )
  {
    this.primaryAudienceType = primaryAudienceType;
  }

  public List getPrimaryAudienceListAsList()
  {
    return primaryAudienceList;
  }

  public void setPrimaryAudienceListAsList( List primaryAudienceList )
  {
    this.primaryAudienceList = primaryAudienceList;
  }

  public int getPrimaryAudienceCount()
  {
    return primaryAudienceList != null ? primaryAudienceList.size() : 0;
  }

  public PromotionAudienceFormBean getPrimaryAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)primaryAudienceList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public int getPrimaryAudienceListCount()
  {
    if ( primaryAudienceList == null )
    {
      return 0;
    }

    return primaryAudienceList.size();
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    primaryAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "primaryAudienceListCount" ) );
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( getMethod() != null && getMethod().equals( "save" ) )
    {
      if ( getPrimaryAudienceType().equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE ) )
      {
        if ( getPrimaryAudienceListAsList() == null || getPrimaryAudienceListAsList().size() == 0 )
        {
          errors.add( "audienceList", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "tilesetup.setup.AUDIENCE_LIST_LABEL" ) ) );
          request.setAttribute( "showAudience", new Boolean( true ) );
        }
      }
    }
    return errors;
  }

  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionAudienceFormBean
      PromotionAudienceFormBean promoAudienceBean = new PromotionAudienceFormBean();
      valueList.add( promoAudienceBean );
    }

    return valueList;
  }

  public String getPrimaryAudienceId()
  {
    return primaryAudienceId;
  }

  public void setPrimaryAudienceId( String primaryAudienceId )
  {
    this.primaryAudienceId = primaryAudienceId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getModuleappId()
  {
    return moduleappId;
  }

  public void setModuleappId( String moduleappId )
  {
    this.moduleappId = moduleappId;
  }

}


package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * AudienceCopyForm.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Balamurugan Shanmugam</td>
 * <td>Aug 6, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AudienceCopyForm extends BaseActionForm
{

  private String audienceId;
  private String newAudienceName;
  private List audienceCopies = new ArrayList();

  /**
   * @return audienceCopies
   */
  public List getAudienceCopies()
  {
    return audienceCopies;
  }

  /**
   * @param audienceCopies
   */
  public void setAudienceCopies( List audienceCopies )
  {
    this.audienceCopies = audienceCopies;
  }

  /**
   * @return audienceId
   */
  public String getAudienceId()
  {
    return audienceId;
  }

  /**
   * @param audienceId
   */
  public void setAudienceId( String audienceId )
  {
    this.audienceId = audienceId;
  }

  /**
   * @return newAudienceName
   */
  public String getNewAudienceName()
  {
    return newAudienceName;
  }

  /**
   * @param newAudienceName
   */
  public void setNewAudienceName( String newAudienceName )
  {
    this.newAudienceName = newAudienceName;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( newAudienceName == null || newAudienceName.length() == 0 )
    {
      errors.add( "newAudienceName",
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.list.builder.details.NEW_AUDIENCE_NAME" ) ) );
    }

    return errors;
  }

}

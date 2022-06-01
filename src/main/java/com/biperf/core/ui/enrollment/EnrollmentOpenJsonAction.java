
package com.biperf.core.ui.enrollment;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.Fields;
import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.managertoolkit.StatesView;
import com.biperf.core.utils.FormFieldConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

public class EnrollmentOpenJsonAction extends BaseDispatchAction
{
  public ActionForward fetchStatesByCountry( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String countryCode = request.getParameter( "addressFormBean.countryCode" );
    StatesView beans = new StatesView();
    if ( !StringUtils.isEmpty( countryCode ) )
    {
      List states = StateType.getList( countryCode );
      NameIdBean selectOne = new NameIdBean();
      selectOne.setCode( "" );
      selectOne.setName( CmsResourceBundle.getCmsBundle().getString( "system.general.SELECT_ONE" ) );
      beans.getLocations().add( selectOne );
      for ( Object object : states )
      {
        StateType stateType = (StateType)object;
        NameIdBean bean = new NameIdBean();
        bean.setCode( stateType.getCode() );
        bean.setName( stateType.getName() );
        beans.getLocations().add( bean );
      }
    }
    super.writeAsJsonToResponse( beans, response );
    return null;
  }

  /**
   * Generates the user Id as per the use-case. First letter of first name + complete last name + random 4 digits 
   */
  public ActionForward generateUserName( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String firstName = request.getParameter( "firstName" );
    String lastName = request.getParameter( "lastName" );

    super.writeAsJsonToResponse( getUserService().generateUniqueUserIdBean( firstName, lastName ), response );
    return null;
  }

  public ActionForward validateUserName( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String userName = request.getParameter( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );

    super.writeAsJsonToResponse( getMessageValidatingUserName( userName ), response );
    return null;
  }

  private WebErrorMessageList getMessageValidatingUserName( String userName )
  {
    boolean isError = getUserService().isUserNameInUse( userName );

    WebErrorMessageList msgs = new WebErrorMessageList();
    WebErrorMessage msg = new WebErrorMessage( isError ? "error" : "success", isError ? "validationError" : null, null, null, null, null );
    Fields field = new Fields();
    field.setName( FormFieldConstants.FORGOT_PWD_FORM_USERNAME );
    field.setText( isError ? CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.JSON_PARTICIPANT_DUPLICATE_USER_NAME ) : null );
    msg.getFields().add( field );
    msgs.getMessages().add( msg );
    return msgs;
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}

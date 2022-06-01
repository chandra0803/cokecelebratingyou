
package com.biperf.core.ui.login;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.ujac.util.StringUtils;

import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.PhoneNumberValidator;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AboutMeValueBean;

/**
 * Used for the First time login screen.
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
 * <td>doodam</td>
 * <td>Oct 29, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LoginPageFirstTimeForm extends BaseForm
{

  private static final long serialVersionUID = 1L;
  private String termsAndConditions;
  private String newPassword = "";
  private FormFile profileImage;
  private ArrayList<AboutMeValueBean> aboutMeQuestions = new ArrayList<AboutMeValueBean>();
  private String phoneTypeDesc;
  private String phoneNumber;
  private String phoneExtension;
  private String countryPhoneCode;
  private String displayCountryPhoneCode;
  private String textPhoneNbr;
  private String[] activeSMSGroupTypes;
  private String termsAndConditionsForTxtMsgs;
  private boolean forceChangePassword;
  private boolean showTermsAndConditions;
  private boolean showUpdateEmail;
  // This avatarUrl is only for reference.
  private String avatarUrl;
  private String email;
  private boolean fromSSO;

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
    int count = AboutMeQuestionType.getList().size();
    aboutMeQuestions = (ArrayList<AboutMeValueBean>)buildEmptyAboutmMeValueBean( count );
  }

  public boolean isForceChangePassword()
  {
    return forceChangePassword;
  }

  public void setForceChangePassword( boolean forceChangePassword )
  {
    this.forceChangePassword = forceChangePassword;
  }

  public String getNewPassword()
  {
    return newPassword;
  }

  public void setNewPassword( String newPassword )
  {
    this.newPassword = newPassword;
  }

  public FormFile getProfileImage()
  {
    return profileImage;
  }

  public void setProfileImage( FormFile profileImage )
  {
    this.profileImage = profileImage;
  }

  public List<AboutMeValueBean> getAboutMeQuestions()
  {
    return this.aboutMeQuestions;
  }

  public void setAboutMeQuestions( ArrayList<AboutMeValueBean> aboutMeQuestion )
  {
    this.aboutMeQuestions = aboutMeQuestion;
  }

  public String getPhoneTypeDesc()
  {
    return phoneTypeDesc;
  }

  public void setPhoneTypeDesc( String phoneTypeDesc )
  {
    this.phoneTypeDesc = phoneTypeDesc;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneExtension()
  {
    return phoneExtension;
  }

  public void setPhoneExtension( String phoneExtension )
  {
    this.phoneExtension = phoneExtension;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

  public String getDisplayCountryPhoneCode()
  {
    return displayCountryPhoneCode;
  }

  public void setDisplayCountryPhoneCode( String displayCountryPhoneCode )
  {
    this.displayCountryPhoneCode = displayCountryPhoneCode;
  }

  public String getTextPhoneNbr()
  {
    return textPhoneNbr;
  }

  public void setTextPhoneNbr( String textPhoneNbr )
  {
    this.textPhoneNbr = textPhoneNbr;
  }

  public String[] getActiveSMSGroupTypes()
  {
    return activeSMSGroupTypes;
  }

  public void setActiveSMSGroupTypes( String[] activeSMSGroupTypes )
  {
    this.activeSMSGroupTypes = activeSMSGroupTypes;
  }

  private List<AboutMeValueBean> buildEmptyAboutmMeValueBean( int count )
  {
    List<AboutMeValueBean> aboutMeValueBeans = new ArrayList<AboutMeValueBean>();
    for ( int index1 = 0; index1 < count; index1++ )
    {
      AboutMeValueBean aboutMeValueBean = new AboutMeValueBean();
      aboutMeValueBeans.add( aboutMeValueBean );
    }
    return aboutMeValueBeans;
  }

  public void load( List<AboutMe> listaboutme )
  {
    List<AboutMeValueBean> aboutMevaluebean = new ArrayList<AboutMeValueBean>();
    if ( listaboutme != null && listaboutme.size() > 0 )
    {
      for ( Iterator<AboutMe> iterator = listaboutme.iterator(); iterator.hasNext(); )
      {
        AboutMe aboutMe = iterator.next();
        AboutMeValueBean aValueBean = new AboutMeValueBean();
        aValueBean.setAboutmeAnswer( aboutMe.getAnswer() );
        aValueBean.setAboutmeQuestioncode( aboutMe.getAboutMeQuestionType().getCode() );
        aValueBean.setAboutmeQuestion( aboutMe.getAboutMeQuestionType().getName() );
        aboutMevaluebean.add( aValueBean );
      }
    }

    loadValueBean( aboutMevaluebean );
  }

  public void loadValueBean( List<AboutMeValueBean> listaboutMeValueBean )
  {
    List<AboutMeValueBean> returnAboutmeQuestions = new ArrayList<AboutMeValueBean>();
    List<AboutMeQuestionType> aboutMeQuestionTypes = AboutMeQuestionType.getList();
    boolean checked = false;
    if ( aboutMeQuestionTypes != null && aboutMeQuestionTypes.size() > 0 )
    {
      for ( Iterator<AboutMeQuestionType> iterateor = aboutMeQuestionTypes.iterator(); iterateor.hasNext(); )
      {
        AboutMeQuestionType meQuestionType = iterateor.next();
        if ( listaboutMeValueBean != null && listaboutMeValueBean.size() > 0 )
        {
          for ( Iterator<AboutMeValueBean> aboutmevaluebean = listaboutMeValueBean.iterator(); aboutmevaluebean.hasNext(); )
          {
            AboutMeValueBean abvaluebean = aboutmevaluebean.next();
            if ( abvaluebean.getAboutmeQuestioncode().equals( meQuestionType.getCode() ) )
            {
              checked = true;
              returnAboutmeQuestions.add( abvaluebean );
              break;
            }
          }
        }
        if ( !checked )
        {
          AboutMeValueBean abvaluebean = new AboutMeValueBean();
          abvaluebean.setAboutmeQuestioncode( meQuestionType.getCode() );
          abvaluebean.setAboutmeAnswer( "" );
          abvaluebean.setAboutmeQuestion( meQuestionType.getName() );
          returnAboutmeQuestions.add( abvaluebean );
        }
        checked = false;
      }
    }
    this.aboutMeQuestions = (ArrayList<AboutMeValueBean>)returnAboutmeQuestions;
  }

  public int getaboutMeQuestionsListSize()
  {
    if ( this.aboutMeQuestions != null )
    {
      return this.aboutMeQuestions.size();
    }

    return 0;
  }

  public String getTermsAndConditions()
  {
    return termsAndConditions;
  }

  public void setTermsAndConditions( String termsAndConditions )
  {
    this.termsAndConditions = termsAndConditions;
  }

  public String getTermsAndConditionsForTxtMsgs()
  {
    return termsAndConditionsForTxtMsgs;
  }

  public void setTermsAndConditionsForTxtMsgs( String termsAndConditionsForTxtMsgs )
  {
    this.termsAndConditionsForTxtMsgs = termsAndConditionsForTxtMsgs;
  }

  public boolean isShowTermsAndConditions()
  {
    return showTermsAndConditions;
  }

  public void setShowTermsAndConditions( boolean showTermsAndConditions )
  {
    this.showTermsAndConditions = showTermsAndConditions;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail( String email )
  {
    this.email = email;
  }

  public boolean isShowUpdateEmail()
  {
    return showUpdateEmail;
  }

  public void setShowUpdateEmail( boolean showUpdateEmail )
  {
    this.showUpdateEmail = showUpdateEmail;
  }

  public boolean isFromSSO()
  {
    return fromSSO;
  }

  public void setFromSSO( boolean fromSSO )
  {
    this.fromSSO = fromSSO;
  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }
    if ( !request.getParameter( "method" ).equals( ActionConstants.DECLINE ) )
    {
      if ( !StringUtil.isEmpty( textPhoneNbr ) )
      {
        boolean valid = PhoneNumberValidator.checkPhoneNumberFormat( textPhoneNbr );
        if ( !valid )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "profile.preference.tab.PHN_INV" ) );
        }
      }

      if ( activeSMSGroupTypes != null && activeSMSGroupTypes.length > 0 && StringUtil.isEmpty( textPhoneNbr ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "profile.preference.tab.PHN_REQ" ) );
      }

      if ( !StringUtil.isEmpty( textPhoneNbr ) && ( activeSMSGroupTypes == null || activeSMSGroupTypes.length == 0 ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "profile.personal.info.NO_ALERTS_PICKED" ) );
      }

      if ( activeSMSGroupTypes != null && activeSMSGroupTypes.length > 0 && !StringUtil.isEmpty( textPhoneNbr ) && StringUtil.isEmpty( termsAndConditionsForTxtMsgs ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "profile.personal.info.ACCEPT_TERMS_TXT_MSG" ) );
      }

      // setting the avatar again to form when form validation fails.
      if ( actionErrors.size() > 0 )
      {
        Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
        String thumbImage = participant.getAvatarSmall();
        if ( !StringUtils.isEmpty( thumbImage ) )
        {
          avatarUrl = ImageUtils.getImageUploadPath() + thumbImage;
        }
        else
        {
          avatarUrl = participant.getAvatarSmallFullPath();
        }
        request.setAttribute( "serverReturnedErrored", true );
      }
    }

    return actionErrors;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)ServiceLocator.getService( ParticipantService.BEAN_NAME );
  }
}

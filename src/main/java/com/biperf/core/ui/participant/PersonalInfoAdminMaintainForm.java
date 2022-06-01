
package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.value.AboutMeValueBean;

public class PersonalInfoAdminMaintainForm extends BaseForm
{
  private String method;
  private String userId;
  private String avatarUrl;
  private FormFile profileImage;
  private boolean defaultImage;
  private List<AboutMeValueBean> aboutMeQuestions = new ArrayList<AboutMeValueBean>();

  public void load( List<AboutMe> aboutMeList )
  {
    List<AboutMeValueBean> aboutMeValueBean = new ArrayList<AboutMeValueBean>();
    if ( aboutMeList != null && aboutMeList.size() > 0 )
    {
      for ( Iterator<AboutMe> iterator = aboutMeList.iterator(); iterator.hasNext(); )
      {
        AboutMe aboutMe = iterator.next();
        if ( aboutMe.getAboutMeQuestionType() != null )
        {
          AboutMeValueBean aValueBean = new AboutMeValueBean();
          aValueBean.setAboutmeAnswer( aboutMe.getAnswer() );
          aValueBean.setAboutmeQuestioncode( aboutMe.getAboutMeQuestionType().getCode() );
          aValueBean.setAboutmeQuestion( aboutMe.getAboutMeQuestionType().getName() );
          aboutMeValueBean.add( aValueBean );
        }
      }
    }
    loadValueBean( aboutMeValueBean );
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
    this.aboutMeQuestions = returnAboutmeQuestions;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
    int count = RequestUtils.getOptionalParamInt( request, "aboutMeQuestionsListSize" );
    aboutMeQuestions = buildEmptyAboutmMeValueBean( count );
  }

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    return actionErrors;
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

  public int getAboutMeQuestionsListSize()
  {
    if ( this.aboutMeQuestions != null )
    {
      return this.aboutMeQuestions.size();
    }

    return 0;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMethod()
  {
    return method;
  }

  public void setProfileImage( FormFile profileImage )
  {
    this.profileImage = profileImage;
  }

  public FormFile getProfileImage()
  {
    return profileImage;
  }

  public void setAboutMeQuestions( List<AboutMeValueBean> aboutMeQuestions )
  {
    this.aboutMeQuestions = aboutMeQuestions;
  }

  public List<AboutMeValueBean> getAboutMeQuestions()
  {
    return aboutMeQuestions;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setDefaultImage( boolean defaultImage )
  {
    this.defaultImage = defaultImage;
  }

  public boolean isDefaultImage()
  {
    return defaultImage;
  }
}

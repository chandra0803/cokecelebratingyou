
package com.biperf.core.ui.profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.enums.AboutMeQuestionType;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.value.AboutMeValueBean;

public class AboutMeView
{

  private String method;
  private String userId;
  private List<AboutMeValueBean> aboutMeQuestions = new ArrayList<AboutMeValueBean>();
  private String siteUrlPrefix;

  public void load( List<AboutMe> listaboutme )
  {
    List<AboutMeValueBean> aboutMevaluebean = new ArrayList<AboutMeValueBean>();
    if ( listaboutme != null && listaboutme.size() > 0 )
    {
      for ( Iterator<AboutMe> iterator = listaboutme.iterator(); iterator.hasNext(); )
      {
        AboutMe aboutMe = iterator.next();
        if ( aboutMe.getAboutMeQuestionType() != null )
        {
          AboutMeValueBean aValueBean = new AboutMeValueBean();
          aValueBean.setAboutmeAnswer( aboutMe.getAnswer() );
          aValueBean.setAboutmeQuestioncode( aboutMe.getAboutMeQuestionType().getCode() );
          aValueBean.setAboutmeQuestion( aboutMe.getAboutMeQuestionType().getName() );
          aboutMevaluebean.add( aValueBean );
        }

      }
    }
    loadValueBean( aboutMevaluebean );
  }

  @SuppressWarnings( "unchecked" )
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

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public List<AboutMeValueBean> getAboutMeQuestions()
  {
    return aboutMeQuestions;
  }

  public void setAboutMeQuestions( List<AboutMeValueBean> aboutMeQuestions )
  {
    this.aboutMeQuestions = aboutMeQuestions;
  }

  public String getSiteUrlPrefix()
  {
    return siteUrlPrefix;
  }

  public void setSiteUrlPrefix( String siteUrlPrefix )
  {
    this.siteUrlPrefix = siteUrlPrefix;
  }

}


package com.biperf.core.ui.recognitionadvisor;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public class RARecognitionFlowBean implements Serializable
{
  public static final String KEY = "raRecognitionFlowBean";
  public String isRARecognitionFlow;

  private RARecognitionFlowBean()
  {
  }

  public static RARecognitionFlowBean addToSession( HttpServletRequest request, String isRARecognitionFlow )
  {
    RARecognitionFlowBean bean = create( isRARecognitionFlow );
    request.getSession().setAttribute( KEY, bean );
    return bean;

  }

  public static RARecognitionFlowBean create( String isRARecognitionFlow )
  {
    RARecognitionFlowBean bean = new RARecognitionFlowBean();
    bean.setIsRARecognitionFlow( isRARecognitionFlow );

    return bean;
  }

  public static void moveToRequest( HttpServletRequest request )
  {
    // get it from session....
    RARecognitionFlowBean bean = (RARecognitionFlowBean)request.getSession().getAttribute( KEY );

    // add it to the request object...
    request.setAttribute( KEY, bean );

    // remove it from session....
    request.getSession().removeAttribute( KEY );
  }

  public String getIsRARecognitionFlow()
  {
    return isRARecognitionFlow;
  }

  public void setIsRARecognitionFlow( String isRARecognitionFlow )
  {
    this.isRARecognitionFlow = isRARecognitionFlow;
  }

}

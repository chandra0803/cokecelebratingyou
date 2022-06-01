
package com.biperf.core.ui.recognition.easy;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitEasyRecognitionBean
{
  private List<ResponseBean> messages = new ArrayList<ResponseBean>();

  private SubmitEasyRecognitionBean()
  {
  }

  public static SubmitEasyRecognitionBean success()
  {
    SubmitEasyRecognitionBean bean = new SubmitEasyRecognitionBean();
    bean.succeed();
    return bean;
  }

  public static SubmitEasyRecognitionBean failure( String text )
  {
    SubmitEasyRecognitionBean bean = new SubmitEasyRecognitionBean();
    bean.fail( text );
    return bean;
  }

  public List<ResponseBean> getMessages()
  {
    return messages;
  }

  private void succeed()
  {
    messages.add( new ResponseBean( true, "" ) );
  }

  private void fail( String message )
  {
    messages.add( new ResponseBean( false, message ) );
  }

  public static class ResponseBean
  {
    private boolean success;
    private String text;

    public ResponseBean( boolean success, String text )
    {
      this.success = success;
      this.text = text;
    }

    @JsonProperty( value = "isSuccess" )
    public boolean isSuccess()
    {
      return success;
    }

    public String getText()
    {
      return text;
    }
  }

}

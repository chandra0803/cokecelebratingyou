
package com.biperf.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.utils.WebResponseConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

//This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_EMPTY )
public class WebErrorMessage
{
  private String type;
  private String code;
  private String name;
  private String text;
  private String url;
  private String command;
  private Boolean success;
  private String docURL;
  private String description;
  private List<Fields> fields = new ArrayList<Fields>();

  public WebErrorMessage()
  {

  }

  public WebErrorMessage( String command, String type )
  {
    this.command = command;
    this.type = type;
  }

  public WebErrorMessage( String type, boolean success, String text )
  {
    this.type = type;
    this.success = success;
    this.text = text;
  }

  public WebErrorMessage( String type, boolean success, String text, String command )
  {
    this.type = type;
    this.success = success;
    this.text = text;
    this.command = command;
  }

  public WebErrorMessage( String type, boolean success, String text, String command, String name )
  {
    this.type = type;
    this.success = success;
    this.text = text;
    this.command = command;
    this.name = name;
  }

  public WebErrorMessage( String type, String code, String name, String text, String url, String command )
  {
    this.type = type;
    this.code = code;
    this.name = name;
    this.text = text;
    this.url = url;
    this.command = command;
  }

  public WebErrorMessage( String type, String name, boolean success, String docUrl, String description )
  {
    this.type = type;
    this.name = name;
    this.success = success;
    this.docURL = docUrl;
    this.description = description;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getCommand()
  {
    return command;
  }

  public void setCommand( String command )
  {
    this.command = command;
  }

  public List<Fields> getFields()
  {
    return fields;
  }

  public void setFields( List<Fields> fields )
  {
    this.fields = fields;
  }

  @JsonProperty( "isSuccess" )
  public Boolean getSuccess()
  {
    return success;
  }

  public void setSuccess( Boolean success )
  {
    this.success = success;
  }

  public String getDocURL()
  {
    return docURL;
  }

  public void setDocURL( String docURL )
  {
    this.docURL = docURL;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public static WebErrorMessage addErrorMessage( WebErrorMessage message )
  {
    message.setName( CmsResourceBundle.getCmsBundle().getString( "system.generalerror.FOLLOWING_ERRORS" ) );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    message.setCode( WebResponseConstants.RESPONSE_CODE_VALIDATION_ERROR );
    return message;
  }

  public static WebErrorMessage addErrorMessageNoName( WebErrorMessage message )
  {
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    message.setCode( WebResponseConstants.RESPONSE_CODE_VALIDATION_ERROR );
    return message;
  }

  public static WebErrorMessage addServerCmd( WebErrorMessage message )
  {
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
    return message;
  }
}

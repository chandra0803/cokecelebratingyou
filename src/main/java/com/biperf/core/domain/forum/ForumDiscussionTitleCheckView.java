/**
 * 
 */

package com.biperf.core.domain.forum;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class ForumDiscussionTitleCheckView
{
  private List<Messages> messages = new ArrayList<Messages>();

  @JsonProperty( "messages" )
  public List<Messages> getMessages()
  {
    return messages;
  }

  public void setMessages( List<Messages> messages )
  {
    this.messages = messages;
  }

  public static class Messages
  {
    @JsonProperty( "type" )
    private String type;
    @JsonProperty( "code" )
    private String code;
    private List<Fields> fields = new ArrayList<Fields>();

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

    @JsonProperty( "fields" )
    public List<Fields> getFields()
    {
      return fields;
    }

    public void setFields( List<Fields> fields )
    {
      this.fields = fields;
    }

  }

  public static class Fields
  {
    @JsonProperty( "name" )
    private String name;
    @JsonProperty( "text" )
    private String text;

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
  }

}

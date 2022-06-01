package com.biperf.core.ui.ws.rest.sea.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailMessageBody
{

  private String plainText;
  private String htmlText;

  public String getPlainText()
  {
    return plainText;
  }

  public void setPlainText( String plainText )
  {
    this.plainText = plainText;
  }

  public String getHtmlText()
  {
    return htmlText;
  }

  public void setHtmlText( String htmlText )
  {
    this.htmlText = htmlText;
  }

}

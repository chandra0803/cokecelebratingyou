
package com.biperf.core.domain.news;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class object is used to generate news json object for new G5 layout.
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
 * <td>sharafud</td>
 * <td>dec 06, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class NewsView
{
  // This is for future use to keep additional messages
  private String[] messages = {};
  @JsonProperty( "news" )
  private List<NewsDetailsView> news;

  public NewsView()
  {

  }

  public NewsView( List<NewsDetailsView> news )
  {
    this.setNews( news );
  }

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<NewsDetailsView> getNews()
  {
    return news;
  }

  public void setNews( List<NewsDetailsView> news )
  {
    this.news = news;
  }
}

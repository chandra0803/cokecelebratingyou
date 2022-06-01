/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/security/credentials/QuestionAnswerCredentials.java,v $
 */

package com.biperf.core.security.credentials;

import java.io.Serializable;

/**
 * Holder of QuestionAnswer Credential.
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
 * <td>zahler</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuestionAnswerCredentials implements Serializable
{
  private String secretQuestion;
  private String secretAnswer;

  /**
   * Constructor with question and answer.
   * 
   * @param question
   * @param answer
   */
  public QuestionAnswerCredentials( String question, String answer )
  {
    this.secretQuestion = question;
    this.secretAnswer = answer;
  }

  /**
   * @return value of secretQuestion property
   */
  public String getSecretQuestion()
  {
    return secretQuestion;
  }

  /**
   * @param secretQuestion value for secretQuestion property
   */
  public void setSecretQuestion( String secretQuestion )
  {
    this.secretQuestion = secretQuestion;
  }

  /**
   * @return value of secretAnswer property
   */
  public String getSecretAnswer()
  {
    return secretAnswer;
  }

  /**
   * @param secretAnswer value for secretAnswer property
   */
  public void setSecretAnswer( String secretAnswer )
  {
    this.secretAnswer = secretAnswer;
  }

}

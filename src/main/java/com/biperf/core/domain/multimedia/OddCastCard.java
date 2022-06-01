/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/multimedia/OddCastCard.java,v $
 */

package com.biperf.core.domain.multimedia;

/**
 * OddCastCard.
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
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class OddCastCard extends Card
{
  private OddCastCategory category;

  private Long characterId;
  private String characterName;
  private String characterSmallImageName;
  private String characterLargeImageName;
  private Long outfitId;
  private String outfitName;
  private String outfitSmallImageName;
  private String outfitLargeImageName;
  private Long accessoryId;
  private String accessoryName;
  private String accessorySmallImageName;
  private String accessoryLargeImageName;
  private String sceneHeaderScript;
  private String sceneBodyScript;
  private Long voiceId;
  private Long languageId;
  private Long voiceFamilyId;

  public Long getLanguageId()
  {
    return languageId;
  }

  public void setLanguageId( Long languageId )
  {
    this.languageId = languageId;
  }

  public Long getVoiceFamilyId()
  {
    return voiceFamilyId;
  }

  public void setVoiceFamilyId( Long voiceFamilyId )
  {
    this.voiceFamilyId = voiceFamilyId;
  }

  public Long getVoiceId()
  {
    return voiceId;
  }

  public void setVoiceId( Long voiceId )
  {
    this.voiceId = voiceId;
  }

  public Long getAccessoryId()
  {
    return accessoryId;
  }

  public void setAccessoryId( Long accessoryId )
  {
    this.accessoryId = accessoryId;
  }

  public String getAccessoryLargeImageName()
  {
    return accessoryLargeImageName;
  }

  public void setAccessoryLargeImageName( String accessoryLargeImageName )
  {
    this.accessoryLargeImageName = accessoryLargeImageName;
  }

  public String getAccessoryName()
  {
    return accessoryName;
  }

  public void setAccessoryName( String accessoryName )
  {
    this.accessoryName = accessoryName;
  }

  public String getAccessorySmallImageName()
  {
    return accessorySmallImageName;
  }

  public void setAccessorySmallImageName( String accessorySmallImageName )
  {
    this.accessorySmallImageName = accessorySmallImageName;
  }

  public Long getCharacterId()
  {
    return characterId;
  }

  public void setCharacterId( Long characterId )
  {
    this.characterId = characterId;
  }

  public String getCharacterLargeImageName()
  {
    return characterLargeImageName;
  }

  public void setCharacterLargeImageName( String characterLargeImageName )
  {
    this.characterLargeImageName = characterLargeImageName;
  }

  public String getCharacterName()
  {
    return characterName;
  }

  public void setCharacterName( String characterName )
  {
    this.characterName = characterName;
  }

  public String getCharacterSmallImageName()
  {
    return characterSmallImageName;
  }

  public void setCharacterSmallImageName( String characterSmallImageName )
  {
    this.characterSmallImageName = characterSmallImageName;
  }

  public Long getOutfitId()
  {
    return outfitId;
  }

  public void setOutfitId( Long outfitId )
  {
    this.outfitId = outfitId;
  }

  public String getOutfitLargeImageName()
  {
    return outfitLargeImageName;
  }

  public void setOutfitLargeImageName( String outfitLargeImageName )
  {
    this.outfitLargeImageName = outfitLargeImageName;
  }

  public String getOutfitName()
  {
    return outfitName;
  }

  public void setOutfitName( String outfitName )
  {
    this.outfitName = outfitName;
  }

  public String getOutfitSmallImageName()
  {
    return outfitSmallImageName;
  }

  public void setOutfitSmallImageName( String outfitSmallImageName )
  {
    this.outfitSmallImageName = outfitSmallImageName;
  }

  public String getSceneBodyScript()
  {
    return sceneBodyScript;
  }

  public void setSceneBodyScript( String sceneBodyScript )
  {
    this.sceneBodyScript = sceneBodyScript;
  }

  public String getSceneHeaderScript()
  {
    return sceneHeaderScript;
  }

  public void setSceneHeaderScript( String sceneHeaderScript )
  {
    this.sceneHeaderScript = sceneHeaderScript;
  }

  public OddCastCategory getCategory()
  {
    return category;
  }

  public void setCategory( OddCastCategory category )
  {
    this.category = category;
  }

}

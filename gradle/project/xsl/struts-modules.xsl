<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns:j="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">

  <!-- create a web.xml file based on what is in the codebase-->
  <xsl:output method="xml" indent="yes" />

  <!-- declare the params that will be passed via ANT and insert after the last init-param we find -->
  <xsl:param name="quiz" />
  <xsl:param name="rec" />
  <xsl:param name="pc" />
  <xsl:param name="nom" />
  <xsl:param name="svy" />
  <xsl:param name="gq" />
  <xsl:param name="cp" />
  <xsl:param name="ref" />
  <xsl:param name="wel" />
  <xsl:param name="td" />
  <xsl:param name="eng" />
  <xsl:param name="ssi" />
  <!-- add the optional struts configurations to the Struts servlet definition -->
  <xsl:template match="//j:servlet/j:servlet-class[text()='org.apache.struts.action.ActionServlet']">
    <xsl:copy-of select="." />
    <!-- Submission Configs -->
    <xsl:if test="$quiz='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/quiz</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-quiz.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$rec='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/recognitionWizard</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-submit-recognition.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
     <xsl:if test="$nom='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/nomination</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-nomination.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
     <xsl:if test="$pc='true' or $ref='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/product</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-product.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$gq='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/goalquest</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-goalquest.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$td='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/throwdown</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-throwdown.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>        
    <xsl:if test="$cp='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/challengepoint</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-challenge-point.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$rec='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/purl</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-purl.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$eng='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/engagement</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-engagement.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <!-- Promo Setup Configs -->
    <xsl:if test="$quiz='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionQuiz</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-quiz.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$svy='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionSurvey</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-survey.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$rec='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionRecognition</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-recognition.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$nom='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionNomination</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-nomination.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$pc='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionProductClaim</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-product-claim.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$gq='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionGoalQuest</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-goal-quest.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$td='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionThrowDown</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-throwdown.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>      
    <xsl:if test="$cp='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionChallengepoint</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-challenge-point.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$wel='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionWellness</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-wellness.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$eng='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionEngagement</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-engagement.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$rec='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/celebration</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-celebration.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
    <xsl:if test="$ssi='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/promotionSSI</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-promotion-ssi.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>
     <xsl:if test="$ssi='true'">
      <xsl:element name="init-param">
        <xsl:element name="param-name">
          <xsl:text>config/ssi</xsl:text>
        </xsl:element>
        <xsl:element name="param-value">
          <xsl:text>/WEB-INF/struts-config-ssi.xml</xsl:text>
        </xsl:element>
      </xsl:element>
    </xsl:if>    
  </xsl:template>
  
  <!-- general rule to copy everything else -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
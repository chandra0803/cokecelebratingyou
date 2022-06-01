<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
	##cp_header_css_image##
	
	<div>Dear <strong>##firstName## ##lastName##</strong>,<br /><br /><strong>##programName##</strong> POINTS PARTIAL DISTRIBUTION!<br /><br />
	
	Congratulations! You have earned Points for your program results from <strong>##registrationStartDate##</strong> to <strong>##progressSubmissionDate##</strong>.<br />
	
	<strong>##interimAmount##</strong> Points have been deposited in your personal account at 
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/siteLink"/>
		</xsl:attribute>
			website
	</xsl:element>.<br /><br />
	
	You will continue to earn additional Points at the rate of <strong>##primaryAward##</strong>
	
	<xsl:if test="/StrongMail/points!=''">
		Points
	</xsl:if>	
	for each <strong>##primaryAwardIncrement##</strong>
	
	<xsl:if test="/StrongMail/isThreshold!='true'"> 
	above <strong>##primaryAwardThreshold##</strong>
	</xsl:if>
	
	until the end of the program period on <strong>##promotionEndDate##</strong>. 
	In addition you are eligible to earn an additional awards if you achieve your selected Challengepoint.<br /><br />
	At the start of the <strong>##programName##</strong> program you selected a personal Challengepoint at the program website.<br />You selected 
	<strong>##challengepointLevel##</strong> that had an achievement requirement of <strong>##challengepointLevelAmount##</strong> which equals 
	<strong>##totalChallengepointValue##</strong>.<br /><br />
	
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/siteLink"/>
		</xsl:attribute>
		Click here to access the website				
	</xsl:element><br /><br />
	
	<xsl:element name="a">
		<xsl:attribute name="href">
			<xsl:value-of select="/StrongMail/contactUsUrl"/>
		</xsl:attribute>If you have any questions, please click here to contact Program Administration
	</xsl:element><br /><br />
	
	Again, congratulations and good luck during the remainder of the program!<br /><br />
	
	Sincerely, <br /><br />The <strong>##programName##</strong> Challengepoint Team!</div>		

    ##cp_footer1## 
  </xsl:template>
</xsl:stylesheet>



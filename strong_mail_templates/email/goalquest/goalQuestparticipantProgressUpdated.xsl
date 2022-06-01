<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
   
		##header_css_image## 
		
						Dear ##first_name## ##last_name##,<br/><br/>
						
						PROGRESS UPDATE!<br/><br/>
						
						At the start of the ##program_name## incentive program you selected a personal goal at the program website.  
						<xsl:if test="/StrongMail/GOAL_LEVEL != ''"> 
							You selected ##goal_level## that had an achievement requirement of ##goal_level_amount## which equals ##total_goal_value##.
						</xsl:if><br/><br/>
						
						As of ##date_1## results reported to us show that you have achieved ##actual_results## 
						<xsl:if test="/StrongMail/GOAL_LEVEL != ''">  
							which is ##percent_to_goal## % of your personal goal
						</xsl:if>
						. For more information visit the ##program_name## 
									<xsl:element name="a">
										<xsl:attribute name="href">
											<xsl:value-of select="/StrongMail/SITE_LINK"/>
										</xsl:attribute>
										website				
									</xsl:element>
						, and click on Review Progress from the GoalQuest menu.<br/><br/>
						
						Good luck during the remainder of the program!<br/><br/>
						
						Sincerely,<br/><br/>
						
						The ##program_name## GoalQuest Team<br/><br/>
						<br/><br/>
      ##footer1## 
  </xsl:template>
</xsl:stylesheet>
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">

		##header_css_image## 
		
						 Dear 
						 ##partner_first_name##  ##partner_last_name##<br/><br/>
						 PROGRESS UPDATE! <br/><br/>
						 Recently, you were selected as a partner of  ##first_name## ##last_name##  in the  ##program_name##.<br/><br/>
						<xsl:if test="/StrongMail/GOAL_LEVEL != ''"> 
							 They selected ##goal_level##
							 that had an achievement requirement of ##goal_level_amount##
							 which equals ##total_goal_value##.
						</xsl:if>
						
						 As of ##date_1## 
						 results reported to us show that they have achieved   ##actual_results##.
						
						 Although the results do not entitle you to receive the reward 
						
						<xsl:if test="/StrongMail/GOAL_LEVEL != ''"> 
							 which is  ##percent_to_goal##   % of their personal goal 
						</xsl:if>
						  For more information visit the ##program_name## 
						 			<xsl:element name="a">
										<xsl:attribute name="href">
											<xsl:value-of select="/StrongMail/SITE_LINK"/>
										</xsl:attribute>
										website				
									</xsl:element>
			             
						 , and click on Review Progress from the GoalQuest menu. 
			             <br/><br/>
						
						 Good luck during the remainder of the program! <br/><br/>
						
						 Sincerely,<br/><br/>
						
						 The ##program_name##  GoalQuest Team<br/><br/>
			
						<br/><br/>
						
      ##footer1## 
  </xsl:template>
</xsl:stylesheet>

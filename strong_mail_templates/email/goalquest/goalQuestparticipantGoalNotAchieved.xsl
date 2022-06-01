<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">
		##header_css_image## 
		
						Dear ##first_name## ##last_name##,<br/><br/>
						Thank you for participating in the ##program_name## incentive program.<br/><br/>
						<xsl:if test="/StrongMail/GOAL_LEVEL != ''"> 
						At the start of the program you selected ##goal_level## that had an achievement requirement of ##goal_level_amount## which equals ##total_goal_value##.
						</xsl:if>
						Your final results as reported were ##actual_results##.<br/><br/>
						Although your results do not entitle you to receive the reward 
						<xsl:if test="/StrongMail/GOAL_LEVEL != ''">  
							offered at your selected goal level 
						</xsl:if> 
						, we appreciate your participation and the effort you put forth.<br/><br/>
						Sincerely,<br/><br/>
						The ##program_name## GoalQuest Team <br/><br/>
						<br/><br/>
      ##footer1## 
  </xsl:template>
</xsl:stylesheet>

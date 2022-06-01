<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output method="xml"/>
   <xsl:template match="/">

		##header_css_image## 
		
			        Dear  ##partner_first_name##  ##partner_last_name##<br/><br/>
					Thank you for participating in the ##program_name##  incentive program <br/><br/>
					You were selected as a partner of ##first_name## ##last_name##  in the  ##program_name##.<br/><br/>
					<xsl:if test="/StrongMail/GOAL_LEVEL != ''"> 
						 They selected ##goal_level##
						 that had an achievement requirement of ##goal_level_amount##
						 which equals ##total_goal_value##.
					</xsl:if>
					 Their final results as reported were ##actual_results##.
					
					 Although the results do not entitle you to receive the reward 
					
					<xsl:if test="/StrongMail/GOAL_LEVEL != ''"> 
						 offered at the selected goal level 
					</xsl:if>
					 , we appreciate your participation and the effort you put forth.<br/><br/>
					
					 Sincerely,<br/><br/>
					
					 The ##program_name##  GoalQuest Team<br/><br/>
		
				
					<br/><br/>
         	
      ##footer1## 
  </xsl:template>
</xsl:stylesheet>

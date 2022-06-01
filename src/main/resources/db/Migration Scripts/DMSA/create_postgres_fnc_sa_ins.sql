CREATE OR REPLACE FUNCTION fnc_sa_ins() RETURNS VOID AS $$
BEGIN
  INSERT INTO persons select * from "DMSADBNP".stg_sa_persons;
  
  INSERT INTO programs
        SELECT nextval('programs_id_seq'), companyid, program_uuid, programname, programstartdate, 
                programstate, awardtype, isbillprofileactive, billprofiles, giftcodeawardid, 
                '' awardintervaltype, awardintervalfield, awardintervalnumber, awardintervalpart, 
                recipientemail, giftcodereminderemail, gcrememailsenddaysafter, 
                giftcodeexpirationemail, gcexpemailsenddaysprior, contributionemail, 
                createdat, updatedat, programsetup, gcrememailfrequency, 
                gcrememailsendtime, gcrememailsendstartdate, 
                gcrememailsendenddate, celebrationsettings, 
                manageremail, '{}' branding, hierarchyid, emaildisplayname, 
                signatureimageurl, emailheaderimageurl
           FROM "DMSADBNP".stg_sa_programs;
  
  INSERT INTO milestones 
        SELECT nextval('milestones_id_seq'), companyid, programid, milestoneid, milestonecode, milestonename, milestoneaward, 
                milestonestate, ecard, '' celebrationtype, '' contributiontype, createdat, updatedat, maxaward, 
                messagelevellabel, webmessageimageurl, emailmessageimageurl,countrycode
	      FROM "DMSADBNP".stg_sa_milestones;
    
  INSERT INTO recipients
        SELECT nextval('recipients_id_seq'), companyid, recipientid, recipientstate, recipientstatus, programid, 
                milestonecode, awarddate, awardexpirationdate, giftcodeid, giftcodereference, 
				createdby, createdreference, createdat, updatedat, awarddatetimezone, celebrationid, 
                celebrationsettings, participantpersonid,celebrationyear, 
				emailsettings, awardeventsentat
		  FROM "DMSADBNP".stg_sa_recipients;
  
 INSERT INTO celebrationspersons select * from "DMSADBNP".stg_sa_celebrationspersons;  
 
END
$$ LANGUAGE 'plpgsql';
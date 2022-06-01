CREATE OR REPLACE FUNCTION fnc_chan_ins() RETURNS VOID AS $$
BEGIN
    INSERT INTO "Channels" select *from "DMSADBNP".stg_chan_channels; 
    INSERT INTO "Messages" 
      SELECT id, 
              "channelId", 
              "contributorName", 
              "contributorReference", 
              text, 
              '' effects, 
              "replyToId", 
              "threadId", 
              "isDeleted", 
              "createdAt", 
              "updatedAt",
              "companyId", 
              "isFlagged", 
              attachments, 
              "isPrivate", 
              "contributorStatus"
	    FROM "DMSADBNP".stg_chan_messages;
END
$$ LANGUAGE 'plpgsql';
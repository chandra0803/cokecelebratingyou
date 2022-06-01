INSERT INTO cms_audience
   SELECT CMS_GENERIC_PK_SQ.NEXTVAL,
          0,
          'beaconAudience.' || audience_id,
          name,
          name,
          (SELECT id FROM cms_application WHERE code = 'beacon'),
          5662,
          SYSDATE,
          NULL,
          NULL
     FROM audience
/

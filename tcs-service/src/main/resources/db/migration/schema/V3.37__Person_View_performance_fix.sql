CREATE OR REPLACE
  algorithm=TEMPTABLE
VIEW currprogsview
  AS
    SELECT  pm.personid           AS personid,
            pm.programmestartdate AS programmeStartDate,
            pm.programmeenddate   AS programmeEndDate,
            pm.programmeid        AS programmeId,
            prg.programmename     AS programmeName,
            prg.programmenumber   AS programmeNumber,
            tn.number             AS TrainingNumber,
            prg.managingdeanery   AS lo
    FROM   tcs.ProgrammeMembership pm
      JOIN   tcs.Programme prg
        ON    (prg.id = pm.programmeid)
      LEFT JOIN   tcs.TrainingNumber tn
        ON    (tn.programmeid = pm.programmeid)
    WHERE Curdate() BETWEEN pm.programmestartdate AND pm.programmeenddate;

CREATE OR REPLACE
  algorithm=TEMPTABLE
VIEW currplcsview
  AS
    SELECT  pl.traineeid            AS traineeId,
            pl.dateto               AS dateTo,
            pl.datefrom             AS dateFrom,
            pl.gradeabbreviation    AS gradeAbbreviation,
            pl.sitecode             AS siteCode,
            pl.placementtype        AS placementType,
            pst.managinglocaloffice AS lo
    FROM   tcs.Placement pl
      LEFT JOIN   tcs.Post pst
        ON    (pst.id = pl.postid)
    WHERE Curdate() BETWEEN pl.datefrom AND       pl.dateto;


CREATE OR REPLACE
  algorithm=undefined
VIEW `PersonView`
  AS
    SELECT    p.id                 AS id,
              p.intrepidid         AS intrepidId,
              cd.surname           AS surname,
              cd.forenames         AS forenames,
              gmc.gmcnumber        AS gmcNumber,
              gdc.gdcnumber        AS gdcNumber,
              p.publichealthnumber AS publicHealthNumber,
              pm.programmeid       AS programmeId,
              pm.programmename     AS programmeName,
              pm.programmenumber   AS programmeNumber,
              pm.trainingnumber    AS trainingNumber,
              pl.gradeabbreviation AS gradeAbbreviation,
              pl.sitecode          AS siteCode,
              pl.placementtype     AS placementType,
              p.role               AS role,
              p.status             AS status,
              lo.owner             AS currentOwner,
              lo.RULE              AS currentOwnerRule
    FROM      tcs.Person p
      LEFT JOIN tcs.ContactDetails cd
        ON       (cd.id = p.id)
      LEFT JOIN tcs.GmcDetails gmc
        ON       (gmc.id = p.id)
      LEFT JOIN tcs.GdcDetails gdc
        ON       (gdc.id = p.id)
      LEFT JOIN    currprogsview pm
        ON       (pm.personid = p.id )
      LEFT JOIN  currplcsview pl
        ON       (pl.traineeid = p.id)
      LEFT JOIN tcs.PersonOwner lo
        ON       (lo.id = p.id)
    ORDER BY p.id desc;
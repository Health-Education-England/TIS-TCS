insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960113', '1627392502000', 'DE_NTH_APP_20210729_00003782.DAT', '32615392', '7405826', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960113'))
and (exists (select id from Placement where id = '1960113'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960113', '1627392502000', 'DE_NTH_APP_20210729_00003782.DAT', '33065866', '7437127', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960113'))
and (exists (select id from Placement where id = '1960113'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960115', '1620809811000', 'DE_NTH_APP_20210729_00003782.DAT', '32616452', '7407727', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960115'))
and (exists (select id from Placement where id = '1960115'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975930', '1620124230000', 'DE_NTH_APP_20210729_00003782.DAT', '34154027', '7572825', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975930'))
and (exists (select id from Placement where id = '1975930'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975930', '1620124230000', 'DE_NTH_APP_20210729_00003782.DAT', '27650253', '7097969', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975930'))
and (exists (select id from Placement where id = '1975930'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975930', '1620124230000', 'DE_NTH_APP_20210729_00003782.DAT', '9575080', '5359790', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975930'))
and (exists (select id from Placement where id = '1975930'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1987370', '1622122971531', 'DE_NTH_APP_20210729_00003782.DAT', '2573898', '1305143', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1987370'))
and (exists (select id from Placement where id = '1987370'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1987370', '1620639245000', 'DE_NTH_APP_20210729_00003782.DAT', '23586901', '6832378', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1987370'))
and (exists (select id from Placement where id = '1987370'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960115', '1625840886394', 'DE_NTH_APP_20210729_00003782.DAT', '33065551', '7438789', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960115'))
and (exists (select id from Placement where id = '1960115'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975930', '1626877672129', 'DE_NTH_APP_20210729_00003782.DAT', '37934581', '9538779', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975930'))
and (exists (select id from Placement where id = '1975930'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026432', '1627483374000', 'DE_NTH_APP_20210729_00003782.DAT', '38114802', '9567912', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026432'))
and (exists (select id from Placement where id = '2026432'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996594', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114884', '9567937', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996594'))
and (exists (select id from Placement where id = '1996594'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026421', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114891', '9567941', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026421'))
and (exists (select id from Placement where id = '2026421'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026432', '1627483374000', 'DE_NTH_APP_20210729_00003782.DAT', '38114806', '9567915', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026432'))
and (exists (select id from Placement where id = '2026432'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026434', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114712', '9559887', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026434'))
and (exists (select id from Placement where id = '2026434'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026428', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38110747', '9566956', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026428'))
and (exists (select id from Placement where id = '2026428'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026422', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38112189', '9566965', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026422'))
and (exists (select id from Placement where id = '2026422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974853', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38074996', '9556793', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974853'))
and (exists (select id from Placement where id = '1974853'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996595', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114850', '9567928', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996595'))
and (exists (select id from Placement where id = '1996595'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976015', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38110316', '9566942', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976015'))
and (exists (select id from Placement where id = '1976015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026428', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38111344', '9566957', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026428'))
and (exists (select id from Placement where id = '2026428'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026433', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38110730', '9566953', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026433'))
and (exists (select id from Placement where id = '2026433'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996602', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114767', '9567904', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996602'))
and (exists (select id from Placement where id = '1996602'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026432', '1627483374000', 'DE_NTH_APP_20210729_00003782.DAT', '38114810', '9567916', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026432'))
and (exists (select id from Placement where id = '2026432'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996596', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114835', '9567924', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996596'))
and (exists (select id from Placement where id = '1996596'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026421', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114894', '9567942', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026421'))
and (exists (select id from Placement where id = '2026421'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026426', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38112208', '9566970', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026426'))
and (exists (select id from Placement where id = '2026426'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026431', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114400', '9559865', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026431'))
and (exists (select id from Placement where id = '2026431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996596', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114817', '9567917', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996596'))
and (exists (select id from Placement where id = '1996596'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976015', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38110317', '9566946', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976015'))
and (exists (select id from Placement where id = '1976015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026428', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38111654', '9566959', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026428'))
and (exists (select id from Placement where id = '2026428'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974853', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38075005', '9556795', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974853'))
and (exists (select id from Placement where id = '1974853'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026423', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114649', '9559872', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026423'))
and (exists (select id from Placement where id = '2026423'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026420', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114725', '9559892', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026420'))
and (exists (select id from Placement where id = '2026420'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996594', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114864', '9567933', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996594'))
and (exists (select id from Placement where id = '1996594'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026431', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114460', '9559869', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026431'))
and (exists (select id from Placement where id = '2026431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996595', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114857', '9567930', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996595'))
and (exists (select id from Placement where id = '1996595'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974853', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38075009', '9556797', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974853'))
and (exists (select id from Placement where id = '1974853'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026422', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38112193', '9566967', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026422'))
and (exists (select id from Placement where id = '2026422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996594', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114871', '9567936', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996594'))
and (exists (select id from Placement where id = '1996594'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996602', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114763', '9567902', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996602'))
and (exists (select id from Placement where id = '1996602'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2028717', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114795', '9567911', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2028717'))
and (exists (select id from Placement where id = '2028717'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026426', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38112460', '9566972', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026426'))
and (exists (select id from Placement where id = '2026426'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026420', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114744', '9567898', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026420'))
and (exists (select id from Placement where id = '2026420'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026421', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114898', '9567943', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026421'))
and (exists (select id from Placement where id = '2026421'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976015', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38110692', '9566950', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976015'))
and (exists (select id from Placement where id = '1976015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2028717', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114783', '9567908', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2028717'))
and (exists (select id from Placement where id = '2028717'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2028717', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114779', '9567907', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2028717'))
and (exists (select id from Placement where id = '2028717'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026433', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38110738', '9566955', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026433'))
and (exists (select id from Placement where id = '2026433'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026426', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38112683', '9566974', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026426'))
and (exists (select id from Placement where id = '2026426'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026431', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114413', '9559866', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026431'))
and (exists (select id from Placement where id = '2026431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026420', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114733', '9567895', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026420'))
and (exists (select id from Placement where id = '2026420'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026433', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38110725', '9566952', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026433'))
and (exists (select id from Placement where id = '2026433'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996595', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114840', '9567926', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996595'))
and (exists (select id from Placement where id = '1996595'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026434', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114701', '9559884', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026434'))
and (exists (select id from Placement where id = '2026434'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026434', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114689', '9559880', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026434'))
and (exists (select id from Placement where id = '2026434'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996602', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114759', '9567901', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996602'))
and (exists (select id from Placement where id = '1996602'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996596', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114824', '9567921', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996596'))
and (exists (select id from Placement where id = '1996596'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026422', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38112196', '9566968', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026422'))
and (exists (select id from Placement where id = '2026422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026423', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114676', '9559874', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026423'))
and (exists (select id from Placement where id = '2026423'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026423', '1627483371401', 'DE_NTH_APP_20210729_00003782.DAT', '38114682', '9559878', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026423'))
and (exists (select id from Placement where id = '2026423'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048222', '1627486479000', 'DE_NTH_APP_20210729_00003782.DAT', '20492866', '6605070', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048222'))
and (exists (select id from Placement where id = '2048222'));

insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1708036', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25713154', '6957985', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1708036'))
and (exists (select id from Placement where id = '1708036'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1850178', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '27578868', '7087711', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1850178'))
and (exists (select id from Placement where id = '1850178'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1896101', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '23468190', '6818413', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1896101'))
and (exists (select id from Placement where id = '1896101'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1695352', '2021-07-27 13:00:10', 'DE_MER_APP_20210727_00003765.DAT', '32744621', '7414411', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1695352'))
and (exists (select id from Placement where id = '1695352'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1901774', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '31889144', '7360049', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1901774'))
and (exists (select id from Placement where id = '1901774'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1898431', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '28325801', '7147009', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1898431'))
and (exists (select id from Placement where id = '1898431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1940337', '2021-07-27 13:00:05', 'DE_WES_APP_20210727_00003763.DAT', '33130069', '7445092', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1940337'))
and (exists (select id from Placement where id = '1940337'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1940363', '2021-07-27 13:00:05', 'DE_WES_APP_20210727_00003763.DAT', '33130095', '7445100', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1940363'))
and (exists (select id from Placement where id = '1940363'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1883475', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '20893961', '6627718', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1883475'))
and (exists (select id from Placement where id = '1883475'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1894710', '2021-07-27 13:00:03', 'DE_WMD_APP_20210727_00003757.DAT', '21066975', '6633670', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1894710'))
and (exists (select id from Placement where id = '1894710'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1942414', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '31736454', '7353034', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1942414'))
and (exists (select id from Placement where id = '1942414'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1898431', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '28325645', '7145853', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1898431'))
and (exists (select id from Placement where id = '1898431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1954009', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '11778911', '5777459', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1954009'))
and (exists (select id from Placement where id = '1954009'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1850178', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '27578867', '7087710', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1850178'))
and (exists (select id from Placement where id = '1850178'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1954932', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '30903142', '7299029', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1954932'))
and (exists (select id from Placement where id = '1954932'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1956159', '2021-07-29 13:10:00', 'DE_NWN_APP_20210729_00003785.DAT', '28325936', '7147144', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1956159'))
and (exists (select id from Placement where id = '1956159'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1956676', '2021-07-30 13:10:01', 'DE_OXF_APP_20210730_00003795.DAT', '31545087', '7338966', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1956676'))
and (exists (select id from Placement where id = '1956676'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960113', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '32615392', '7405826', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960113'))
and (exists (select id from Placement where id = '1960113'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960113', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '33065866', '7437127', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960113'))
and (exists (select id from Placement where id = '1960113'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960115', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '32616452', '7407727', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960115'))
and (exists (select id from Placement where id = '1960115'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960142', '2021-07-28 13:10:00', 'DE_WES_APP_20210728_00003776.DAT', '17634682', '6396076', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960142'))
and (exists (select id from Placement where id = '1960142'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1965234', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '8441896', '5054501', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1965234'))
and (exists (select id from Placement where id = '1965234'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1967044', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '33063633', '7439618', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1967044'))
and (exists (select id from Placement where id = '1967044'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1967826', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '23583301', '6833917', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1967826'))
and (exists (select id from Placement where id = '1967826'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974820', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '36767908', '9021029', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974820'))
and (exists (select id from Placement where id = '1974820'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975773', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '31717385', '7350667', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975773'))
and (exists (select id from Placement where id = '1975773'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975773', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '18155686', '6440079', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975773'))
and (exists (select id from Placement where id = '1975773'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975930', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '34154027', '7572825', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975930'))
and (exists (select id from Placement where id = '1975930'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975930', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '27650253', '7097969', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975930'))
and (exists (select id from Placement where id = '1975930'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975930', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '9575080', '5359790', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975930'))
and (exists (select id from Placement where id = '1975930'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976069', '2021-07-29 13:10:00', 'DE_WES_APP_20210729_00003789.DAT', '34602964', '7606731', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976069'))
and (exists (select id from Placement where id = '1976069'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976461', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32959209', '7427405', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976461'))
and (exists (select id from Placement where id = '1976461'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1977549', '2021-07-27 13:00:03', 'DE_EMD_APP_20210727_00003754.DAT', '15112707', '6181575', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1977549'))
and (exists (select id from Placement where id = '1977549'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1977963', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '13240954', '5988097', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1977963'))
and (exists (select id from Placement where id = '1977963'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1980996', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '11417293', '5736863', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1980996'))
and (exists (select id from Placement where id = '1980996'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1986943', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25837948', '6976284', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1986943'))
and (exists (select id from Placement where id = '1986943'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1987193', '2021-07-27 13:00:05', 'DE_WES_APP_20210727_00003763.DAT', '36035453', '8001874', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1987193'))
and (exists (select id from Placement where id = '1987193'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1987370', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '2573898', '1305143', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1987370'))
and (exists (select id from Placement where id = '1987370'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1987370', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '23586901', '6832378', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1987370'))
and (exists (select id from Placement where id = '1987370'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1987662', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '5871988', '2134114', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1987662'))
and (exists (select id from Placement where id = '1987662'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990054', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '33102596', '7441326', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990054'))
and (exists (select id from Placement where id = '1990054'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993894', '2021-07-27 13:00:05', 'DE_OXF_APP_20210727_00003762.DAT', '34422860', '7588762', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993894'))
and (exists (select id from Placement where id = '1993894'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1994204', '2021-07-30 13:10:01', 'DE_OXF_APP_20210730_00003795.DAT', '31487483', '7337972', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1994204'))
and (exists (select id from Placement where id = '1994204'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996529', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '32657584', '7410319', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996529'))
and (exists (select id from Placement where id = '1996529'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2001485', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '36698732', '9000089', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2001485'))
and (exists (select id from Placement where id = '2001485'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2007658', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '17221265', '6357879', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2007658'))
and (exists (select id from Placement where id = '2007658'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009042', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25790187', '6967369', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009042'))
and (exists (select id from Placement where id = '2009042'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009042', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25512593', '6950161', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009042'))
and (exists (select id from Placement where id = '2009042'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009055', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '4469431', '1829859', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009055'))
and (exists (select id from Placement where id = '2009055'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009055', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25832279', '6974015', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009055'))
and (exists (select id from Placement where id = '2009055'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009057', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25177543', '6924770', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009057'))
and (exists (select id from Placement where id = '2009057'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009057', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33142853', '7448755', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009057'))
and (exists (select id from Placement where id = '2009057'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009058', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25787174', '6964399', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009058'))
and (exists (select id from Placement where id = '2009058'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009058', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33091760', '7440257', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009058'))
and (exists (select id from Placement where id = '2009058'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009076', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25790076', '6967364', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009076'))
and (exists (select id from Placement where id = '2009076'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009076', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25512374', '6950147', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009076'))
and (exists (select id from Placement where id = '2009076'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009077', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '34858763', '7656265', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009077'))
and (exists (select id from Placement where id = '2009077'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009077', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '12891954', '5930659', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009077'))
and (exists (select id from Placement where id = '2009077'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009079', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25789414', '6967320', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009079'))
and (exists (select id from Placement where id = '2009079'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009079', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25509038', '6950144', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009079'))
and (exists (select id from Placement where id = '2009079'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009081', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '25832224', '6974011', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009081'))
and (exists (select id from Placement where id = '2009081'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009081', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '16108129', '6262738', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009081'))
and (exists (select id from Placement where id = '2009081'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009084', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '33191276', '7452097', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009084'))
and (exists (select id from Placement where id = '2009084'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009084', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '12892106', '5930735', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009084'))
and (exists (select id from Placement where id = '2009084'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009086', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33189698', '7451892', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009086'))
and (exists (select id from Placement where id = '2009086'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009086', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25639078', '6956373', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009086'))
and (exists (select id from Placement where id = '2009086'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009087', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25819190', '6967710', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009087'))
and (exists (select id from Placement where id = '2009087'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009088', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25637660', '6954312', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009088'))
and (exists (select id from Placement where id = '2009088'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009088', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33142855', '7448756', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009088'))
and (exists (select id from Placement where id = '2009088'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009090', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25787336', '6964407', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009090'))
and (exists (select id from Placement where id = '2009090'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009090', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '31164620', '7313686', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009090'))
and (exists (select id from Placement where id = '2009090'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009096', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '31164616', '7313682', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009096'))
and (exists (select id from Placement where id = '2009096'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009098', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25788743', '6967180', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009098'))
and (exists (select id from Placement where id = '2009098'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009098', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '31163272', '7311431', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009098'))
and (exists (select id from Placement where id = '2009098'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009099', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '31164618', '7313684', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009099'))
and (exists (select id from Placement where id = '2009099'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009100', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '12891965', '5930671', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009100'))
and (exists (select id from Placement where id = '2009100'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009100', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33319097', '7467633', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009100'))
and (exists (select id from Placement where id = '2009100'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009111', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25206794', '6926875', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009111'))
and (exists (select id from Placement where id = '2009111'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009120', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25786587', '6966010', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009120'))
and (exists (select id from Placement where id = '2009120'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009120', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33362848', '7475755', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009120'))
and (exists (select id from Placement where id = '2009120'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009125', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33091812', '7440883', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009125'))
and (exists (select id from Placement where id = '2009125'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009125', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '12891813', '5930590', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009125'))
and (exists (select id from Placement where id = '2009125'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009126', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '24178378', '6862205', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009126'))
and (exists (select id from Placement where id = '2009126'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009126', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '36769138', '9025964', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009126'))
and (exists (select id from Placement where id = '2009126'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009126', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25778221', '6965985', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009126'))
and (exists (select id from Placement where id = '2009126'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009127', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25778354', '6965992', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009127'))
and (exists (select id from Placement where id = '2009127'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009127', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33363091', '7475826', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009127'))
and (exists (select id from Placement where id = '2009127'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009129', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33363092', '7475827', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009129'))
and (exists (select id from Placement where id = '2009129'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009129', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25786557', '6966001', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009129'))
and (exists (select id from Placement where id = '2009129'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009130', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '23486623', '6820030', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009130'))
and (exists (select id from Placement where id = '2009130'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009130', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25786548', '6966000', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009130'))
and (exists (select id from Placement where id = '2009130'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009132', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25786697', '6966038', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009132'))
and (exists (select id from Placement where id = '2009132'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009132', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33240656', '7458691', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009132'))
and (exists (select id from Placement where id = '2009132'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009134', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33240861', '7458716', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009134'))
and (exists (select id from Placement where id = '2009134'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009136', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25787050', '6964369', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009136'))
and (exists (select id from Placement where id = '2009136'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009136', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '33240657', '7458692', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009136'))
and (exists (select id from Placement where id = '2009136'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009138', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33363094', '7475829', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009138'))
and (exists (select id from Placement where id = '2009138'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009138', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25786687', '6966037', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009138'))
and (exists (select id from Placement where id = '2009138'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009151', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25630154', '6951867', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009151'))
and (exists (select id from Placement where id = '2009151'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009749', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '25631513', '6955009', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009749'))
and (exists (select id from Placement where id = '2009749'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009753', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '25836580', '6972505', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009753'))
and (exists (select id from Placement where id = '2009753'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009764', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '25841673', '6976482', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009764'))
and (exists (select id from Placement where id = '2009764'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009766', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25836585', '6972506', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009766'))
and (exists (select id from Placement where id = '2009766'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009767', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25841716', '6976490', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009767'))
and (exists (select id from Placement where id = '2009767'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009768', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '22581841', '6758012', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009768'))
and (exists (select id from Placement where id = '2009768'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009769', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25836815', '6974318', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009769'))
and (exists (select id from Placement where id = '2009769'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009770', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25841662', '6976470', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009770'))
and (exists (select id from Placement where id = '2009770'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009851', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '12696113', '5899966', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009851'))
and (exists (select id from Placement where id = '2009851'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2014248', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25830410', '6966861', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2014248'))
and (exists (select id from Placement where id = '2014248'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024842', '2021-07-27 13:00:03', 'DE_WMD_APP_20210727_00003757.DAT', '32179340', '7381918', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024842'))
and (exists (select id from Placement where id = '2024842'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024849', '2021-07-27 13:00:03', 'DE_WMD_APP_20210727_00003757.DAT', '37325703', '9271870', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024849'))
and (exists (select id from Placement where id = '2024849'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024869', '2021-07-27 13:00:03', 'DE_WMD_APP_20210727_00003757.DAT', '36036409', '8006885', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024869'))
and (exists (select id from Placement where id = '2024869'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024869', '2021-07-27 13:00:03', 'DE_WMD_APP_20210727_00003757.DAT', '8856728', '5144497', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024869'))
and (exists (select id from Placement where id = '2024869'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024873', '2021-07-27 13:00:03', 'DE_WMD_APP_20210727_00003757.DAT', '36036409', '8006885', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024873'))
and (exists (select id from Placement where id = '2024873'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024873', '2021-07-27 13:00:03', 'DE_WMD_APP_20210727_00003757.DAT', '8856728', '5144497', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024873'))
and (exists (select id from Placement where id = '2024873'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2005733', '2021-07-27 13:00:10', 'DE_MER_APP_20210727_00003765.DAT', '13249865', '5989214', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2005733'))
and (exists (select id from Placement where id = '2005733'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2027042', '2021-07-27 13:00:10', 'DE_MER_APP_20210727_00003765.DAT', '32509507', '7401008', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2027042'))
and (exists (select id from Placement where id = '2027042'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2027755', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '33319099', '7467631', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2027755'))
and (exists (select id from Placement where id = '2027755'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2027755', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '12891985', '5930680', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2027755'))
and (exists (select id from Placement where id = '2027755'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2027758', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '31164617', '7313683', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2027758'))
and (exists (select id from Placement where id = '2027758'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1885132', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25832730', '6974081', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1885132'))
and (exists (select id from Placement where id = '1885132'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2028989', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '28326577', '7147785', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2028989'))
and (exists (select id from Placement where id = '2028989'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030119', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '5871988', '2134114', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030119'))
and (exists (select id from Placement where id = '2030119'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975773', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '31717388', '7350665', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975773'))
and (exists (select id from Placement where id = '1975773'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975773', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '18155690', '6440084', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975773'))
and (exists (select id from Placement where id = '1975773'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033001', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25713161', '6957988', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033001'))
and (exists (select id from Placement where id = '2033001'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033778', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25654228', '6956484', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033778'))
and (exists (select id from Placement where id = '2033778'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033855', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25786630', '6966027', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033855'))
and (exists (select id from Placement where id = '2033855'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033855', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '33240788', '7458702', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033855'))
and (exists (select id from Placement where id = '2033855'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035721', '2021-07-27 13:00:03', 'DE_KSS_APP_20210727_00003755.DAT', '10498885', '5626701', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035721'))
and (exists (select id from Placement where id = '2035721'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1980996', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '8973261', '5173421', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1980996'))
and (exists (select id from Placement where id = '1980996'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035992', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '34955507', '7661977', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035992'))
and (exists (select id from Placement where id = '2035992'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037303', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '32275993', '7382355', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037303'))
and (exists (select id from Placement where id = '2037303'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037521', '2021-07-29 13:10:00', 'DE_EMD_APP_20210729_00003780.DAT', '30716688', '7280820', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037521'))
and (exists (select id from Placement where id = '2037521'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037553', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '31269557', '7317318', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037553'))
and (exists (select id from Placement where id = '2037553'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960115', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '33065551', '7438789', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960115'))
and (exists (select id from Placement where id = '1960115'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030637', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '1289649', '919734', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030637'))
and (exists (select id from Placement where id = '2030637'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2041158', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '33140712', '7447434', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2041158'))
and (exists (select id from Placement where id = '2041158'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037504', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '37873741', '9524864', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037504'))
and (exists (select id from Placement where id = '2037504'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975930', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '37934581', '9538779', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975930'))
and (exists (select id from Placement where id = '1975930'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993733', '2021-07-27 13:00:05', 'DE_OXF_APP_20210727_00003762.DAT', '37443533', '9338220', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993733'))
and (exists (select id from Placement where id = '1993733'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993817', '2021-07-27 13:00:05', 'DE_OXF_APP_20210727_00003762.DAT', '37444419', '9338234', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993817'))
and (exists (select id from Placement where id = '1993817'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993817', '2021-07-27 13:00:05', 'DE_OXF_APP_20210727_00003762.DAT', '37444425', '9338236', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993817'))
and (exists (select id from Placement where id = '1993817'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993740', '2021-07-27 13:00:05', 'DE_OXF_APP_20210727_00003762.DAT', '37444493', '9338246', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993740'))
and (exists (select id from Placement where id = '1993740'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009128', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '23218056', '6801251', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009128'))
and (exists (select id from Placement where id = '2009128'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2045735', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '11738134', '5773229', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2045735'))
and (exists (select id from Placement where id = '2045735'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047145', '2021-07-27 13:00:03', 'DE_EMD_APP_20210727_00003754.DAT', '23583376', '6833941', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047145'))
and (exists (select id from Placement where id = '2047145'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047146', '2021-07-27 13:00:04', 'DE_PEN_APP_20210727_00003758.DAT', '13276946', '5994627', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047146'))
and (exists (select id from Placement where id = '2047146'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1959771', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '31541476', '7339759', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1959771'))
and (exists (select id from Placement where id = '1959771'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042535', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '32658384', '7410514', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042535'))
and (exists (select id from Placement where id = '2042535'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047153', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '32642099', '7410180', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047153'))
and (exists (select id from Placement where id = '2047153'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047204', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '21772476', '6683168', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047204'))
and (exists (select id from Placement where id = '2047204'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047221', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '32682864', '7410723', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047221'))
and (exists (select id from Placement where id = '2047221'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047228', '2021-07-27 13:00:04', 'DE_PEN_APP_20210727_00003758.DAT', '33143291', '7445783', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047228'))
and (exists (select id from Placement where id = '2047228'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047242', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '3842023', '1692229', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047242'))
and (exists (select id from Placement where id = '2047242'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047244', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '1291034', '921119', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047244'))
and (exists (select id from Placement where id = '2047244'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047256', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '12557539', '5885745', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047256'))
and (exists (select id from Placement where id = '2047256'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047260', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '29982546', '7234522', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047260'))
and (exists (select id from Placement where id = '2047260'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047284', '2021-07-27 13:00:10', 'DE_MER_APP_20210727_00003765.DAT', '32750623', '7415296', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047284'))
and (exists (select id from Placement where id = '2047284'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047285', '2021-07-27 13:00:10', 'DE_MER_APP_20210727_00003765.DAT', '32750686', '7415307', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047285'))
and (exists (select id from Placement where id = '2047285'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017579', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '37613896', '9417907', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017579'))
and (exists (select id from Placement where id = '2017579'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017511', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '37613897', '9417908', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017511'))
and (exists (select id from Placement where id = '2017511'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047308', '2021-07-27 13:00:10', 'DE_MER_APP_20210727_00003765.DAT', '16949260', '6333968', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047308'))
and (exists (select id from Placement where id = '2047308'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047310', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '32748386', '7414968', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047310'))
and (exists (select id from Placement where id = '2047310'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047375', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '9407224', '5229085', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047375'))
and (exists (select id from Placement where id = '2047375'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047375', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '23587057', '6832398', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047375'))
and (exists (select id from Placement where id = '2047375'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974991', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '34121217', '7562933', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974991'))
and (exists (select id from Placement where id = '1974991'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974991', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '32489832', '7398307', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974991'))
and (exists (select id from Placement where id = '1974991'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047387', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '25830623', '6967805', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047387'))
and (exists (select id from Placement where id = '2047387'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047387', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '31486820', '7337923', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047387'))
and (exists (select id from Placement where id = '2047387'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047388', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '32166048', '7381197', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047388'))
and (exists (select id from Placement where id = '2047388'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047389', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '10508390', '5630173', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047389'))
and (exists (select id from Placement where id = '2047389'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047392', '2021-07-27 13:00:04', 'DE_PEN_APP_20210727_00003758.DAT', '30783537', '7283729', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047392'))
and (exists (select id from Placement where id = '2047392'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047396', '2021-07-27 13:00:04', 'DE_PEN_APP_20210727_00003758.DAT', '30783537', '7283729', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047396'))
and (exists (select id from Placement where id = '2047396'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047400', '2021-07-27 13:00:03', 'DE_EMD_APP_20210727_00003754.DAT', '34629966', '7611495', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047400'))
and (exists (select id from Placement where id = '2047400'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047401', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '28326485', '7147693', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047401'))
and (exists (select id from Placement where id = '2047401'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047402', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '28326485', '7147693', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047402'))
and (exists (select id from Placement where id = '2047402'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047408', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '28326485', '7147693', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047408'))
and (exists (select id from Placement where id = '2047408'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047413', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '10774055', '5641596', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047413'))
and (exists (select id from Placement where id = '2047413'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009102', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '31164501', '7310664', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009102'))
and (exists (select id from Placement where id = '2009102'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009102', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '12892054', '5930720', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009102'))
and (exists (select id from Placement where id = '2009102'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047415', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '13319656', '5999741', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047415'))
and (exists (select id from Placement where id = '2047415'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047415', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '30906185', '7298318', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047415'))
and (exists (select id from Placement where id = '2047415'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047417', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '28422817', '7154131', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047417'))
and (exists (select id from Placement where id = '2047417'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047419', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '35255347', '7741230', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047419'))
and (exists (select id from Placement where id = '2047419'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047422', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '30823503', '7290827', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047422'))
and (exists (select id from Placement where id = '2047422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047422', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '31443522', '7334852', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047422'))
and (exists (select id from Placement where id = '2047422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047423', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '22989573', '6780913', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047423'))
and (exists (select id from Placement where id = '2047423'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047424', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '32119966', '7373811', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047424'))
and (exists (select id from Placement where id = '2047424'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047425', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '13299905', '5996650', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047425'))
and (exists (select id from Placement where id = '2047425'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047429', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '29414719', '7199288', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047429'))
and (exists (select id from Placement where id = '2047429'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047435', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '28109955', '7122160', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047435'))
and (exists (select id from Placement where id = '2047435'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047436', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '27619015', '7093383', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047436'))
and (exists (select id from Placement where id = '2047436'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047440', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '27619018', '7093386', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047440'))
and (exists (select id from Placement where id = '2047440'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047441', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '9296520', '5213530', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047441'))
and (exists (select id from Placement where id = '2047441'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047441', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '23587269', '6832479', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047441'))
and (exists (select id from Placement where id = '2047441'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047442', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '27578867', '7087710', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047442'))
and (exists (select id from Placement where id = '2047442'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1997989', '2021-07-27 13:00:03', 'DE_KSS_APP_20210727_00003755.DAT', '35675505', '7796892', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1997989'))
and (exists (select id from Placement where id = '1997989'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047448', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '25170758', '6921472', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047448'))
and (exists (select id from Placement where id = '2047448'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047449', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '24059793', '6859675', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047449'))
and (exists (select id from Placement where id = '2047449'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047451', '2021-07-27 13:00:03', 'DE_KSS_APP_20210727_00003755.DAT', '37338353', '9257818', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047451'))
and (exists (select id from Placement where id = '2047451'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047452', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '13298373', '5996460', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047452'))
and (exists (select id from Placement where id = '2047452'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2002354', '2021-07-27 13:00:03', 'DE_KSS_APP_20210727_00003755.DAT', '37338379', '9257825', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2002354'))
and (exists (select id from Placement where id = '2002354'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047462', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '23561210', '6830628', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047462'))
and (exists (select id from Placement where id = '2047462'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047462', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '7661086', '4919007', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047462'))
and (exists (select id from Placement where id = '2047462'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047472', '2021-07-27 13:00:03', 'DE_EMD_APP_20210727_00003754.DAT', '13939129', '6068391', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047472'))
and (exists (select id from Placement where id = '2047472'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047482', '2021-07-27 13:00:05', 'DE_OXF_APP_20210727_00003762.DAT', '22693837', '6761944', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047482'))
and (exists (select id from Placement where id = '2047482'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047486', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '24052423', '6857538', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047486'))
and (exists (select id from Placement where id = '2047486'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047488', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '24398199', '6872794', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047488'))
and (exists (select id from Placement where id = '2047488'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047491', '2021-07-27 13:00:04', 'DE_YHD_APP_20210727_00003760.DAT', '27713764', '7100436', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047491'))
and (exists (select id from Placement where id = '2047491'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047493', '2021-07-27 13:00:10', 'DE_MER_APP_20210727_00003765.DAT', '32509820', '7401046', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047493'))
and (exists (select id from Placement where id = '2047493'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047496', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '15395189', '6204639', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047496'))
and (exists (select id from Placement where id = '2047496'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047497', '2021-07-27 13:00:05', 'DE_OXF_APP_20210727_00003762.DAT', '33352307', '7469147', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047497'))
and (exists (select id from Placement where id = '2047497'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047498', '2021-07-27 13:00:05', 'DE_OXF_APP_20210727_00003762.DAT', '35084167', '7683155', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047498'))
and (exists (select id from Placement where id = '2047498'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047499', '2021-07-27 13:00:03', 'DE_NTH_APP_20210727_00003756.DAT', '9529783', '5354930', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047499'))
and (exists (select id from Placement where id = '2047499'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047502', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '10914950', '5654499', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047502'))
and (exists (select id from Placement where id = '2047502'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047504', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '7857181', '4956017', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047504'))
and (exists (select id from Placement where id = '2047504'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037260', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '34039734', '7547815', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037260'))
and (exists (select id from Placement where id = '2037260'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037260', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '37698363', '9504711', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037260'))
and (exists (select id from Placement where id = '2037260'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042899', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '31643392', '7343258', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042899'))
and (exists (select id from Placement where id = '2042899'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042894', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23492350', '6821095', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042894'))
and (exists (select id from Placement where id = '2042894'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042895', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23486429', '6820835', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042895'))
and (exists (select id from Placement where id = '2042895'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042890', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23475912', '6819759', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042890'))
and (exists (select id from Placement where id = '2042890'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044031', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23472462', '6820457', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044031'))
and (exists (select id from Placement where id = '2044031'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2046415', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '35395113', '7755534', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2046415'))
and (exists (select id from Placement where id = '2046415'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2046415', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '32275995', '7382356', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2046415'))
and (exists (select id from Placement where id = '2046415'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044030', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23472996', '6819552', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044030'))
and (exists (select id from Placement where id = '2044030'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2006239', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '29494889', '7204656', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2006239'))
and (exists (select id from Placement where id = '2006239'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044047', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23473495', '6819573', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044047'))
and (exists (select id from Placement where id = '2044047'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2006241', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '32293418', '7382783', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2006241'))
and (exists (select id from Placement where id = '2006241'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2006239', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '34547902', '7599910', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2006239'))
and (exists (select id from Placement where id = '2006239'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2006241', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '13346439', '6000756', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2006241'))
and (exists (select id from Placement where id = '2006241'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047248', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23516801', '6822423', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047248'))
and (exists (select id from Placement where id = '2047248'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044045', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23473802', '6819594', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044045'))
and (exists (select id from Placement where id = '2044045'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044036', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23484594', '6819877', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044036'))
and (exists (select id from Placement where id = '2044036'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047258', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23492250', '6821082', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047258'))
and (exists (select id from Placement where id = '2047258'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2025849', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23492309', '6821089', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2025849'))
and (exists (select id from Placement where id = '2025849'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019074', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '15650938', '6227612', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019074'))
and (exists (select id from Placement where id = '2019074'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019064', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23492309', '6821089', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019064'))
and (exists (select id from Placement where id = '2019064'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019074', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '22542103', '6755455', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019074'))
and (exists (select id from Placement where id = '2019074'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1837715', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '2646704', '1352353', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1837715'))
and (exists (select id from Placement where id = '1837715'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2032741', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '27860481', '7110345', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2032741'))
and (exists (select id from Placement where id = '2032741'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1837715', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '32276071', '7382388', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1837715'))
and (exists (select id from Placement where id = '1837715'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044788', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '28971539', '7186535', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044788'))
and (exists (select id from Placement where id = '2044788'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044143', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23473779', '6819585', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044143'))
and (exists (select id from Placement where id = '2044143'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044056', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23516858', '6822434', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044056'))
and (exists (select id from Placement where id = '2044056'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2046575', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '29304779', '7200637', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2046575'))
and (exists (select id from Placement where id = '2046575'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1728175', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '36727483', '9006766', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1728175'))
and (exists (select id from Placement where id = '1728175'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1728175', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23495558', '6821227', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1728175'))
and (exists (select id from Placement where id = '1728175'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2046700', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23499052', '6823331', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2046700'))
and (exists (select id from Placement where id = '2046700'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2045539', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '25026643', '6911989', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2045539'))
and (exists (select id from Placement where id = '2045539'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2043004', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23490123', '6820879', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2043004'))
and (exists (select id from Placement where id = '2043004'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042558', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23486376', '6820826', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042558'))
and (exists (select id from Placement where id = '2042558'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042563', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23486376', '6820826', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042563'))
and (exists (select id from Placement where id = '2042563'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2046403', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23500899', '6823348', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2046403'))
and (exists (select id from Placement where id = '2046403'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042350', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23500965', '6823352', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042350'))
and (exists (select id from Placement where id = '2042350'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042603', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23475842', '6819732', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042603'))
and (exists (select id from Placement where id = '2042603'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042693', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '29845136', '7227980', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042693'))
and (exists (select id from Placement where id = '2042693'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042688', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '29845136', '7227980', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042688'))
and (exists (select id from Placement where id = '2042688'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042586', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23475842', '6819732', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042586'))
and (exists (select id from Placement where id = '2042586'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042374', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23506481', '6823355', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042374'))
and (exists (select id from Placement where id = '2042374'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2046404', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '33825286', '7525726', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2046404'))
and (exists (select id from Placement where id = '2046404'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2043030', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '23506481', '6823355', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2043030'))
and (exists (select id from Placement where id = '2043030'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042870', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '31682459', '7348748', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042870'))
and (exists (select id from Placement where id = '2042870'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2031174', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '22541041', '6755375', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2031174'))
and (exists (select id from Placement where id = '2031174'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2031184', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '34154087', '7572036', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2031184'))
and (exists (select id from Placement where id = '2031184'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2031171', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '32224292', '7382136', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2031171'))
and (exists (select id from Placement where id = '2031171'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047508', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '32735689', '7413968', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047508'))
and (exists (select id from Placement where id = '2047508'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047509', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '11731790', '5771741', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047509'))
and (exists (select id from Placement where id = '2047509'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047519', '2021-07-27 13:00:03', 'DE_EMD_APP_20210727_00003754.DAT', '27652235', '7096137', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047519'))
and (exists (select id from Placement where id = '2047519'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047523', '2021-07-27 13:00:03', 'DE_EMD_APP_20210727_00003754.DAT', '24431755', '6876862', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047523'))
and (exists (select id from Placement where id = '2047523'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044574', '2021-07-27 13:00:03', 'DE_WMD_APP_20210727_00003757.DAT', '32116101', '7372808', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044574'))
and (exists (select id from Placement where id = '2044574'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2025405', '2021-07-27 13:00:04', 'DE_PEN_APP_20210727_00003758.DAT', '29108966', '7189989', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2025405'))
and (exists (select id from Placement where id = '2025405'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024295', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '34715000', '7622078', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024295'))
and (exists (select id from Placement where id = '2024295'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024325', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '34714999', '7622077', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024325'))
and (exists (select id from Placement where id = '2024325'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017469', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '37542201', '9349887', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017469'))
and (exists (select id from Placement where id = '2017469'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017465', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '37542217', '9374888', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017465'))
and (exists (select id from Placement where id = '2017465'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015410', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '3527457', '1568438', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015410'))
and (exists (select id from Placement where id = '2015410'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047534', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '15629084', '6223853', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047534'))
and (exists (select id from Placement where id = '2047534'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047534', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '37055944', '9145863', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047534'))
and (exists (select id from Placement where id = '2047534'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047534', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '22540954', '6755373', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047534'))
and (exists (select id from Placement where id = '2047534'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047534', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '37055943', '9145862', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047534'))
and (exists (select id from Placement where id = '2047534'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047536', '2021-07-27 13:00:10', 'DE_LDN_APP_20210727_00003764.DAT', '21673127', '6677832', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047536'))
and (exists (select id from Placement where id = '2047536'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047539', '2021-07-27 13:00:04', 'DE_NWN_APP_20210727_00003759.DAT', '34406140', '7587775', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047539'))
and (exists (select id from Placement where id = '2047539'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015414', '2021-07-27 13:00:05', 'DE_EOE_APP_20210727_00003761.DAT', '36691268', '8991183', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015414'))
and (exists (select id from Placement where id = '2015414'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047551', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '34763328', '7628077', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047551'))
and (exists (select id from Placement where id = '2047551'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047551', '2021-07-27 13:00:15', 'DE_SEV_APP_20210727_00003766.DAT', '25789966', '6967361', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047551'))
and (exists (select id from Placement where id = '2047551'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1895902', '2021-07-27 13:00:04', 'DE_PEN_APP_20210727_00003758.DAT', '30932750', '7300108', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1895902'))
and (exists (select id from Placement where id = '1895902'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047586', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '23475912', '6819759', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047586'))
and (exists (select id from Placement where id = '2047586'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047608', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '32641973', '7410138', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047608'))
and (exists (select id from Placement where id = '2047608'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047625', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '33432825', '7486783', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047625'))
and (exists (select id from Placement where id = '2047625'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047625', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '33506879', '7495612', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047625'))
and (exists (select id from Placement where id = '2047625'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047627', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '23468196', '6818417', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047627'))
and (exists (select id from Placement where id = '2047627'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047630', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '15807964', '6234842', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047630'))
and (exists (select id from Placement where id = '2047630'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047630', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '7661271', '4919084', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047630'))
and (exists (select id from Placement where id = '2047630'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047633', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '23498909', '6823313', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047633'))
and (exists (select id from Placement where id = '2047633'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047634', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '37696211', '9507964', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047634'))
and (exists (select id from Placement where id = '2047634'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047635', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '37697192', '9501599', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047635'))
and (exists (select id from Placement where id = '2047635'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047635', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '34039775', '7547817', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047635'))
and (exists (select id from Placement where id = '2047635'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047637', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '23517525', '6823510', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047637'))
and (exists (select id from Placement where id = '2047637'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1939143', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '28373136', '7151573', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1939143'))
and (exists (select id from Placement where id = '1939143'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047649', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '23475908', '6819756', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047649'))
and (exists (select id from Placement where id = '2047649'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047651', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '22249300', '6727353', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047651'))
and (exists (select id from Placement where id = '2047651'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047651', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '22543955', '6753591', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047651'))
and (exists (select id from Placement where id = '2047651'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047654', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '22290975', '6730080', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047654'))
and (exists (select id from Placement where id = '2047654'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047654', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '32275915', '7382346', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047654'))
and (exists (select id from Placement where id = '2047654'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047657', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '22543956', '6753592', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047657'))
and (exists (select id from Placement where id = '2047657'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1991860', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '21105560', '6640483', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1991860'))
and (exists (select id from Placement where id = '1991860'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1966938', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '21795166', '6687341', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1966938'))
and (exists (select id from Placement where id = '1966938'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1827210', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '37743503', '9519949', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1827210'))
and (exists (select id from Placement where id = '1827210'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1829779', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '37743504', '9519950', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1829779'))
and (exists (select id from Placement where id = '1829779'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1826807', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '38074553', '9565073', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1826807'))
and (exists (select id from Placement where id = '1826807'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019618', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '37743499', '9519945', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019618'))
and (exists (select id from Placement where id = '2019618'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1827177', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '37743501', '9519947', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1827177'))
and (exists (select id from Placement where id = '1827177'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1829625', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '36280019', '8356684', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1829625'))
and (exists (select id from Placement where id = '1829625'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1829686', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '37743502', '9519948', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1829686'))
and (exists (select id from Placement where id = '1829686'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2025873', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '38070881', '9556642', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2025873'))
and (exists (select id from Placement where id = '2025873'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1982827', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '38070811', '9556635', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1982827'))
and (exists (select id from Placement where id = '1982827'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012030', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '38070870', '9556637', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012030'))
and (exists (select id from Placement where id = '2012030'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1982886', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '38070908', '9556650', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1982886'))
and (exists (select id from Placement where id = '1982886'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996082', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '38071209', '9560563', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996082'))
and (exists (select id from Placement where id = '1996082'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012230', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '38070897', '9556647', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012230'))
and (exists (select id from Placement where id = '2012230'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1951598', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '34134142', '7561801', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1951598'))
and (exists (select id from Placement where id = '1951598'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1950858', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '31660657', '7346653', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1950858'))
and (exists (select id from Placement where id = '1950858'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1950797', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '35979540', '7883861', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1950797'))
and (exists (select id from Placement where id = '1950797'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1951380', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '31443309', '7334511', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1951380'))
and (exists (select id from Placement where id = '1951380'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1949497', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '26692485', '7034241', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1949497'))
and (exists (select id from Placement where id = '1949497'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2029553', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '37898142', '9538241', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2029553'))
and (exists (select id from Placement where id = '2029553'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047660', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '37148394', '9240001', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047660'))
and (exists (select id from Placement where id = '2047660'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2018101', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '11474414', '5744442', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2018101'))
and (exists (select id from Placement where id = '2018101'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2018105', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '11474414', '5744442', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2018105'))
and (exists (select id from Placement where id = '2018105'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030004', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '36724013', '9002614', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030004'))
and (exists (select id from Placement where id = '2030004'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2029755', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '38070718', '9564938', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2029755'))
and (exists (select id from Placement where id = '2029755'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2029924', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '36724024', '9002620', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2029924'))
and (exists (select id from Placement where id = '2029924'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2029949', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '36724033', '9002623', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2029949'))
and (exists (select id from Placement where id = '2029949'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2023295', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '38069958', '9564933', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2023295'))
and (exists (select id from Placement where id = '2023295'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2029959', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '36723942', '9002589', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2029959'))
and (exists (select id from Placement where id = '2029959'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030012', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '36723997', '9002607', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030012'))
and (exists (select id from Placement where id = '2030012'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2029759', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '38070615', '9564935', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2029759'))
and (exists (select id from Placement where id = '2029759'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2018509', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '11474421', '5744445', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2018509'))
and (exists (select id from Placement where id = '2018509'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2022272', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '38070714', '9564937', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2022272'))
and (exists (select id from Placement where id = '2022272'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1988928', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38068866', '9564901', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1988928'))
and (exists (select id from Placement where id = '1988928'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1988963', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38068868', '9564903', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1988963'))
and (exists (select id from Placement where id = '1988963'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1988752', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38068862', '9560485', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1988752'))
and (exists (select id from Placement where id = '1988752'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1947874', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38071073', '9564968', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1947874'))
and (exists (select id from Placement where id = '1947874'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1988943', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38068867', '9564902', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1988943'))
and (exists (select id from Placement where id = '1988943'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1989620', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38068828', '9560481', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1989620'))
and (exists (select id from Placement where id = '1989620'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1939205', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38068834', '9560482', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1939205'))
and (exists (select id from Placement where id = '1939205'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1989088', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38071101', '9564974', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1989088'))
and (exists (select id from Placement where id = '1989088'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2006474', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '38071108', '9564977', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2006474'))
and (exists (select id from Placement where id = '2006474'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1991030', '2021-07-28 13:10:00', 'DE_WES_APP_20210728_00003776.DAT', '38068925', '9560497', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1991030'))
and (exists (select id from Placement where id = '1991030'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1983761', '2021-07-28 13:10:00', 'DE_WES_APP_20210728_00003776.DAT', '38068689', '9556555', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1983761'))
and (exists (select id from Placement where id = '1983761'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1971480', '2021-07-28 13:10:00', 'DE_WES_APP_20210728_00003776.DAT', '38068668', '9564896', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1971480'))
and (exists (select id from Placement where id = '1971480'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024203', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38073978', '9560593', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024203'))
and (exists (select id from Placement where id = '2024203'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2005599', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38073922', '9560589', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2005599'))
and (exists (select id from Placement where id = '2005599'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012459', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '8600780', '5093501', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012459'))
and (exists (select id from Placement where id = '2012459'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2006787', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38074389', '9560616', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2006787'))
and (exists (select id from Placement where id = '2006787'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024561', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38074346', '9560606', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024561'))
and (exists (select id from Placement where id = '2024561'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024566', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38074371', '9560609', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024566'))
and (exists (select id from Placement where id = '2024566'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024556', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38074289', '9560603', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024556'))
and (exists (select id from Placement where id = '2024556'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2004024', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38074428', '9560618', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2004024'))
and (exists (select id from Placement where id = '2004024'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2014899', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38074451', '9560624', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2014899'))
and (exists (select id from Placement where id = '2014899'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024556', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '38074275', '9560596', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024556'))
and (exists (select id from Placement where id = '2024556'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074412', '9565051', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074414', '9565053', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074415', '9565054', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074417', '9565056', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074418', '9565057', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035740', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074405', '9565064', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035740'))
and (exists (select id from Placement where id = '2035740'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074416', '9565055', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1980999', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074402', '9565061', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1980999'))
and (exists (select id from Placement where id = '1980999'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2007342', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074404', '9565063', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2007342'))
and (exists (select id from Placement where id = '2007342'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981001', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '27840869', '7109863', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981001'))
and (exists (select id from Placement where id = '1981001'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074411', '9565050', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030534', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '22011361', '6707080', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030534'))
and (exists (select id from Placement where id = '2030534'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074408', '9565047', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074409', '9565048', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074410', '9565049', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2007340', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074403', '9565062', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2007340'))
and (exists (select id from Placement where id = '2007340'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981015', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074413', '9565052', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981015'))
and (exists (select id from Placement where id = '1981015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2007304', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074399', '9565058', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2007304'))
and (exists (select id from Placement where id = '2007304'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035788', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074399', '9565058', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035788'))
and (exists (select id from Placement where id = '2035788'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2007336', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '38074401', '9565060', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2007336'))
and (exists (select id from Placement where id = '2007336'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047662', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '23492273', '6821086', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047662'))
and (exists (select id from Placement where id = '2047662'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2031664', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '29746403', '7221945', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2031664'))
and (exists (select id from Placement where id = '2031664'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047665', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '22541930', '6755428', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047665'))
and (exists (select id from Placement where id = '2047665'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2031664', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '32733847', '7411389', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2031664'))
and (exists (select id from Placement where id = '2031664'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047672', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '8512767', '5070325', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047672'))
and (exists (select id from Placement where id = '2047672'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047672', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '23583579', '6832171', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047672'))
and (exists (select id from Placement where id = '2047672'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047744', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '8597498', '5090771', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047744'))
and (exists (select id from Placement where id = '2047744'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047750', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '26711332', '7034748', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047750'))
and (exists (select id from Placement where id = '2047750'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047754', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '20893646', '6627923', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047754'))
and (exists (select id from Placement where id = '2047754'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047762', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '24407091', '6875587', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047762'))
and (exists (select id from Placement where id = '2047762'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047801', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '32779049', '7417033', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047801'))
and (exists (select id from Placement where id = '2047801'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047854', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '35077664', '7681651', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047854'))
and (exists (select id from Placement where id = '2047854'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047854', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '13586329', '6025935', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047854'))
and (exists (select id from Placement where id = '2047854'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2029206', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '20893608', '6627885', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2029206'))
and (exists (select id from Placement where id = '2029206'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047863', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '31854338', '7358809', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047863'))
and (exists (select id from Placement where id = '2047863'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1736566', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '26682970', '7035109', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1736566'))
and (exists (select id from Placement where id = '1736566'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047867', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '13940272', '6070745', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047867'))
and (exists (select id from Placement where id = '2047867'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047868', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25789950', '6967355', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047868'))
and (exists (select id from Placement where id = '2047868'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047868', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25630162', '6951871', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047868'))
and (exists (select id from Placement where id = '2047868'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047869', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25790197', '6967371', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047869'))
and (exists (select id from Placement where id = '2047869'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047869', '2021-07-28 13:10:00', 'DE_SEV_APP_20210728_00003779.DAT', '25630148', '6951865', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047869'))
and (exists (select id from Placement where id = '2047869'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047871', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '34940102', '7659494', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047871'))
and (exists (select id from Placement where id = '2047871'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047874', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '28325077', '7145285', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047874'))
and (exists (select id from Placement where id = '2047874'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047876', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '36235121', '8088438', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047876'))
and (exists (select id from Placement where id = '2047876'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047876', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '32746616', '7414636', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047876'))
and (exists (select id from Placement where id = '2047876'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047878', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '33452117', '7490909', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047878'))
and (exists (select id from Placement where id = '2047878'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047879', '2021-07-28 13:10:00', 'DE_MER_APP_20210728_00003778.DAT', '32137443', '7377849', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047879'))
and (exists (select id from Placement where id = '2047879'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047881', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '32746463', '7414577', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047881'))
and (exists (select id from Placement where id = '2047881'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047889', '2021-07-28 13:10:00', 'DE_WES_APP_20210728_00003776.DAT', '37744125', '9520017', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047889'))
and (exists (select id from Placement where id = '2047889'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047889', '2021-07-28 13:10:00', 'DE_WES_APP_20210728_00003776.DAT', '15374161', '6206004', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047889'))
and (exists (select id from Placement where id = '2047889'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047896', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '24016103', '6854692', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047896'))
and (exists (select id from Placement where id = '2047896'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047896', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '9436336', '5326086', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047896'))
and (exists (select id from Placement where id = '2047896'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047897', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '31854747', '7361975', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047897'))
and (exists (select id from Placement where id = '2047897'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047898', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '32532645', '7404311', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047898'))
and (exists (select id from Placement where id = '2047898'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047898', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '11656475', '5759677', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047898'))
and (exists (select id from Placement where id = '2047898'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047900', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '20893936', '6627693', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047900'))
and (exists (select id from Placement where id = '2047900'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047903', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '20893611', '6627888', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047903'))
and (exists (select id from Placement where id = '2047903'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990935', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '31489373', '7335470', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990935'))
and (exists (select id from Placement where id = '1990935'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1991153', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '33442421', '7488676', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1991153'))
and (exists (select id from Placement where id = '1991153'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1998144', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '32959141', '7427811', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1998144'))
and (exists (select id from Placement where id = '1998144'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1998144', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '32615724', '7405852', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1998144'))
and (exists (select id from Placement where id = '1998144'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047912', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '10571433', '5633823', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047912'))
and (exists (select id from Placement where id = '2047912'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1936125', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '25245594', '6935081', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1936125'))
and (exists (select id from Placement where id = '1936125'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033846', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '25245594', '6935081', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033846'))
and (exists (select id from Placement where id = '2033846'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047913', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '37847174', '9524599', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047913'))
and (exists (select id from Placement where id = '2047913'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2021219', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '19881458', '6577688', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2021219'))
and (exists (select id from Placement where id = '2021219'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1899556', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '20948680', '6629562', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1899556'))
and (exists (select id from Placement where id = '1899556'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2008598', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '33348982', '7469965', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2008598'))
and (exists (select id from Placement where id = '2008598'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1941751', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '35724968', '7798906', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1941751'))
and (exists (select id from Placement where id = '1941751'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1941751', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '34990072', '7675931', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1941751'))
and (exists (select id from Placement where id = '1941751'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1998164', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '17334014', '6370146', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1998164'))
and (exists (select id from Placement where id = '1998164'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1987543', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '22199328', '6717103', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1987543'))
and (exists (select id from Placement where id = '1987543'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033661', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '22199328', '6717103', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033661'))
and (exists (select id from Placement where id = '2033661'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047964', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '27015356', '7049761', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047964'))
and (exists (select id from Placement where id = '2047964'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1825095', '2021-07-28 13:00:00', 'DE_EMD_APP_20210728_00003767.DAT', '22199346', '6717106', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1825095'))
and (exists (select id from Placement where id = '1825095'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047971', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '32748382', '7414967', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047971'))
and (exists (select id from Placement where id = '2047971'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1998709', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '12259625', '5846162', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1998709'))
and (exists (select id from Placement where id = '1998709'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2047982', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '27015356', '7049761', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2047982'))
and (exists (select id from Placement where id = '2047982'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048000', '2021-07-28 13:10:00', 'DE_OXF_APP_20210728_00003775.DAT', '11462101', '5740257', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048000'))
and (exists (select id from Placement where id = '2048000'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048004', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '31372680', '7328937', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048004'))
and (exists (select id from Placement where id = '2048004'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048004', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '28975898', '7188674', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048004'))
and (exists (select id from Placement where id = '2048004'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048008', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '28327111', '7148319', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048008'))
and (exists (select id from Placement where id = '2048008'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048010', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '22580278', '6757884', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048010'))
and (exists (select id from Placement where id = '2048010'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048011', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '35608152', '7778717', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048011'))
and (exists (select id from Placement where id = '2048011'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048020', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '34955349', '7661953', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048020'))
and (exists (select id from Placement where id = '2048020'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048021', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '28325294', '7145502', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048021'))
and (exists (select id from Placement where id = '2048021'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048022', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '15009268', '6174450', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048022'))
and (exists (select id from Placement where id = '2048022'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035818', '2021-07-28 13:00:01', 'DE_NWN_APP_20210728_00003772.DAT', '34773645', '7629133', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035818'))
and (exists (select id from Placement where id = '2035818'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048025', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '25378210', '6942549', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048025'))
and (exists (select id from Placement where id = '2048025'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048029', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '4367994', '1809776', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048029'))
and (exists (select id from Placement where id = '2048029'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1811218', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '33155002', '7449033', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1811218'))
and (exists (select id from Placement where id = '1811218'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026424', '2021-07-28 13:00:00', 'DE_NTH_APP_20210728_00003769.DAT', '37899367', '9544140', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026424'))
and (exists (select id from Placement where id = '2026424'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048036', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '37305277', '9247764', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048036'))
and (exists (select id from Placement where id = '2048036'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048037', '2021-07-28 13:00:01', 'DE_YHD_APP_20210728_00003773.DAT', '37305277', '9247764', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048037'))
and (exists (select id from Placement where id = '2048037'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048046', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '31163535', '7311495', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048046'))
and (exists (select id from Placement where id = '2048046'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048056', '2021-07-28 13:00:00', 'DE_KSS_APP_20210728_00003768.DAT', '23486620', '6820869', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048056'))
and (exists (select id from Placement where id = '2048056'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048061', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '2532325', '1271140', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048061'))
and (exists (select id from Placement where id = '2048061'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1733558', '2021-07-28 13:00:00', 'DE_WMD_APP_20210728_00003770.DAT', '20890839', '6627351', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1733558'))
and (exists (select id from Placement where id = '1733558'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957460', '2021-07-28 13:10:00', 'DE_LDN_APP_20210728_00003777.DAT', '2517849', '1256928', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957460'))
and (exists (select id from Placement where id = '1957460'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044178', '2021-07-28 13:00:05', 'DE_EOE_APP_20210728_00003774.DAT', '35251761', '7742091', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044178'))
and (exists (select id from Placement where id = '2044178'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048082', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '24179797', '6863198', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048082'))
and (exists (select id from Placement where id = '2048082'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2014057', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '11767556', '5774811', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2014057'))
and (exists (select id from Placement where id = '2014057'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048095', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '34583313', '7601554', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048095'))
and (exists (select id from Placement where id = '2048095'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012479', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '11767564', '5774814', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012479'))
and (exists (select id from Placement where id = '2012479'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048098', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '30815826', '7289287', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048098'))
and (exists (select id from Placement where id = '2048098'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048099', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '15678897', '6227026', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048099'))
and (exists (select id from Placement where id = '2048099'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048100', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '23496610', '6823261', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048100'))
and (exists (select id from Placement where id = '2048100'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048101', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '23496610', '6823261', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048101'))
and (exists (select id from Placement where id = '2048101'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048102', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '30830703', '7290979', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048102'))
and (exists (select id from Placement where id = '2048102'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048103', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '23077486', '6789829', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048103'))
and (exists (select id from Placement where id = '2048103'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048104', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32166237', '7381218', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048104'))
and (exists (select id from Placement where id = '2048104'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048105', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32166223', '7381217', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048105'))
and (exists (select id from Placement where id = '2048105'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048107', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32207001', '7379157', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048107'))
and (exists (select id from Placement where id = '2048107'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048108', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32207025', '7379160', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048108'))
and (exists (select id from Placement where id = '2048108'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048109', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32207036', '7379161', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048109'))
and (exists (select id from Placement where id = '2048109'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048111', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32207064', '7379163', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048111'))
and (exists (select id from Placement where id = '2048111'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048112', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32207072', '7379164', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048112'))
and (exists (select id from Placement where id = '2048112'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048113', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32207076', '7379166', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048113'))
and (exists (select id from Placement where id = '2048113'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048114', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '32207080', '7379169', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048114'))
and (exists (select id from Placement where id = '2048114'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048115', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '13706902', '6039439', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048115'))
and (exists (select id from Placement where id = '2048115'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048116', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '2668501', '1359672', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048116'))
and (exists (select id from Placement where id = '2048116'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048117', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '3670274', '1613690', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048117'))
and (exists (select id from Placement where id = '2048117'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048118', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '3670274', '1613690', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048118'))
and (exists (select id from Placement where id = '2048118'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048119', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '27093259', '7057680', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048119'))
and (exists (select id from Placement where id = '2048119'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048119', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37698108', '9501711', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048119'))
and (exists (select id from Placement where id = '2048119'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048120', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '27093259', '7057680', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048120'))
and (exists (select id from Placement where id = '2048120'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048120', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37698108', '9501711', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048120'))
and (exists (select id from Placement where id = '2048120'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012472', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '11772352', '5776918', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012472'))
and (exists (select id from Placement where id = '2012472'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048122', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '37946363', '9555969', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048122'))
and (exists (select id from Placement where id = '2048122'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048123', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '35281674', '7739532', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048123'))
and (exists (select id from Placement where id = '2048123'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048127', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '15409127', '6207899', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048127'))
and (exists (select id from Placement where id = '2048127'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048130', '2021-07-29 13:10:00', 'DE_NWN_APP_20210729_00003785.DAT', '34557633', '7600379', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048130'))
and (exists (select id from Placement where id = '2048130'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048131', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '24486517', '6880521', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048131'))
and (exists (select id from Placement where id = '2048131'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048137', '2021-07-29 13:10:00', 'DE_EOE_APP_20210729_00003787.DAT', '13362404', '6005320', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048137'))
and (exists (select id from Placement where id = '2048137'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048138', '2021-07-29 13:10:00', 'DE_EOE_APP_20210729_00003787.DAT', '22664880', '6761033', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048138'))
and (exists (select id from Placement where id = '2048138'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2036959', '2021-07-29 13:10:00', 'DE_EMD_APP_20210729_00003780.DAT', '34716558', '7616453', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2036959'))
and (exists (select id from Placement where id = '2036959'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048149', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '31437777', '7332425', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048149'))
and (exists (select id from Placement where id = '2048149'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048151', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '33312574', '7460369', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048151'))
and (exists (select id from Placement where id = '2048151'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048152', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '11465809', '5741722', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048152'))
and (exists (select id from Placement where id = '2048152'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048153', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '31437782', '7332426', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048153'))
and (exists (select id from Placement where id = '2048153'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048155', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '11465800', '5741715', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048155'))
and (exists (select id from Placement where id = '2048155'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048156', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '11465749', '5741702', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048156'))
and (exists (select id from Placement where id = '2048156'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033382', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '25291074', '6936827', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033382'))
and (exists (select id from Placement where id = '2033382'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033382', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '19000754', '6514039', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033382'))
and (exists (select id from Placement where id = '2033382'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033382', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '35484105', '7769283', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033382'))
and (exists (select id from Placement where id = '2033382'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048160', '2021-07-29 13:10:00', 'DE_NWN_APP_20210729_00003785.DAT', '28422672', '7154060', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048160'))
and (exists (select id from Placement where id = '2048160'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048169', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '31338905', '7328859', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048169'))
and (exists (select id from Placement where id = '2048169'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2041213', '2021-07-29 13:10:00', 'DE_EMD_APP_20210729_00003780.DAT', '28288989', '7137032', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2041213'))
and (exists (select id from Placement where id = '2041213'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2041220', '2021-07-29 13:10:00', 'DE_EMD_APP_20210729_00003780.DAT', '38113411', '9566065', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2041220'))
and (exists (select id from Placement where id = '2041220'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2027876', '2021-07-29 13:10:00', 'DE_EMD_APP_20210729_00003780.DAT', '38113910', '9566079', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2027876'))
and (exists (select id from Placement where id = '2027876'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2041215', '2021-07-29 13:10:00', 'DE_EMD_APP_20210729_00003780.DAT', '38113856', '9566075', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2041215'))
and (exists (select id from Placement where id = '2041215'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1898971', '2021-07-29 13:10:00', 'DE_EOE_APP_20210729_00003787.DAT', '38075131', '9565978', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1898971'))
and (exists (select id from Placement where id = '1898971'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012684', '2021-07-29 13:10:00', 'DE_EOE_APP_20210729_00003787.DAT', '34157448', '7569925', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012684'))
and (exists (select id from Placement where id = '2012684'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2023857', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114780', '9567092', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2023857'))
and (exists (select id from Placement where id = '2023857'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1992961', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114135', '9567023', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1992961'))
and (exists (select id from Placement where id = '1992961'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990838', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114761', '9567086', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990838'))
and (exists (select id from Placement where id = '1990838'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2010169', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113352', '9567001', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2010169'))
and (exists (select id from Placement where id = '2010169'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1985232', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114152', '9567026', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1985232'))
and (exists (select id from Placement where id = '1985232'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1823811', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114323', '9567041', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1823811'))
and (exists (select id from Placement where id = '1823811'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990823', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114412', '9567055', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990823'))
and (exists (select id from Placement where id = '1990823'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1823800', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114175', '9567031', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1823800'))
and (exists (select id from Placement where id = '1823800'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019321', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113389', '9567002', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019321'))
and (exists (select id from Placement where id = '2019321'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019338', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113389', '9567002', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019338'))
and (exists (select id from Placement where id = '2019338'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1823811', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114238', '9567039', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1823811'))
and (exists (select id from Placement where id = '1823811'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1823839', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114681', '9567063', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1823839'))
and (exists (select id from Placement where id = '1823839'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1800360', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113413', '9567006', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1800360'))
and (exists (select id from Placement where id = '1800360'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990831', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114693', '9567071', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990831'))
and (exists (select id from Placement where id = '1990831'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1824020', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114688', '9567067', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1824020'))
and (exists (select id from Placement where id = '1824020'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2046128', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '32826292', '7419433', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2046128'))
and (exists (select id from Placement where id = '2046128'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975714', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114057', '9567022', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975714'))
and (exists (select id from Placement where id = '1975714'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1975845', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113405', '9567004', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1975845'))
and (exists (select id from Placement where id = '1975845'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1684288', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114028', '9567020', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1684288'))
and (exists (select id from Placement where id = '1684288'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1823789', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114751', '9567083', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1823789'))
and (exists (select id from Placement where id = '1823789'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1823922', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114396', '9567049', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1823922'))
and (exists (select id from Placement where id = '1823922'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990817', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114373', '9567047', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990817'))
and (exists (select id from Placement where id = '1990817'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990829', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114731', '9567080', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990829'))
and (exists (select id from Placement where id = '1990829'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1823855', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114739', '9567082', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1823855'))
and (exists (select id from Placement where id = '1823855'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2023856', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114766', '9567089', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2023856'))
and (exists (select id from Placement where id = '2023856'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1899863', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1899863'))
and (exists (select id from Placement where id = '1899863'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1922863', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1922863'))
and (exists (select id from Placement where id = '1922863'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1934058', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1934058'))
and (exists (select id from Placement where id = '1934058'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1934062', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1934062'))
and (exists (select id from Placement where id = '1934062'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1934068', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1934068'))
and (exists (select id from Placement where id = '1934068'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1939192', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1939192'))
and (exists (select id from Placement where id = '1939192'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1942175', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1942175'))
and (exists (select id from Placement where id = '1942175'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1942176', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1942176'))
and (exists (select id from Placement where id = '1942176'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1942177', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1942177'))
and (exists (select id from Placement where id = '1942177'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1942178', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1942178'))
and (exists (select id from Placement where id = '1942178'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1942179', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1942179'))
and (exists (select id from Placement where id = '1942179'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1942180', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1942180'))
and (exists (select id from Placement where id = '1942180'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1943411', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1943411'))
and (exists (select id from Placement where id = '1943411'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1951516', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1951516'))
and (exists (select id from Placement where id = '1951516'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976085', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976085'))
and (exists (select id from Placement where id = '1976085'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996953', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996953'))
and (exists (select id from Placement where id = '1996953'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996958', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996958'))
and (exists (select id from Placement where id = '1996958'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1997280', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1997280'))
and (exists (select id from Placement where id = '1997280'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1997288', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1997288'))
and (exists (select id from Placement where id = '1997288'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1997294', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1997294'))
and (exists (select id from Placement where id = '1997294'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1997366', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1997366'))
and (exists (select id from Placement where id = '1997366'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1997372', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1997372'))
and (exists (select id from Placement where id = '1997372'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1997377', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1997377'))
and (exists (select id from Placement where id = '1997377'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2021397', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2021397'))
and (exists (select id from Placement where id = '2021397'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2021404', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2021404'))
and (exists (select id from Placement where id = '2021404'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2021408', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2021408'))
and (exists (select id from Placement where id = '2021408'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033176', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113145', '9566997', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033176'))
and (exists (select id from Placement where id = '2033176'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1823894', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114684', '9567065', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1823894'))
and (exists (select id from Placement where id = '1823894'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1814281', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113605', '9567018', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1814281'))
and (exists (select id from Placement where id = '1814281'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990828', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114724', '9567078', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990828'))
and (exists (select id from Placement where id = '1990828'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1997795', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38113317', '9566998', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1997795'))
and (exists (select id from Placement where id = '1997795'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990812', '2021-07-29 13:10:00', 'DE_KSS_APP_20210729_00003781.DAT', '38114188', '9567036', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990812'))
and (exists (select id from Placement where id = '1990812'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1991163', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38073896', '9565013', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1991163'))
and (exists (select id from Placement where id = '1991163'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1998413', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38074642', '9565905', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1998413'))
and (exists (select id from Placement where id = '1998413'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1971201', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38110240', '9559800', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1971201'))
and (exists (select id from Placement where id = '1971201'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1998407', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '32969093', '7429487', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1998407'))
and (exists (select id from Placement where id = '1998407'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2005802', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38071252', '9565001', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2005802'))
and (exists (select id from Placement where id = '2005802'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1967383', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38075307', '9559776', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1967383'))
and (exists (select id from Placement where id = '1967383'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1971175', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '33232729', '7451611', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1971175'))
and (exists (select id from Placement where id = '1971175'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1998416', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38074643', '9565906', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1998416'))
and (exists (select id from Placement where id = '1998416'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1971152', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38091917', '9559794', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1971152'))
and (exists (select id from Placement where id = '1971152'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2031066', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38073939', '9565022', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2031066'))
and (exists (select id from Placement where id = '2031066'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993900', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38071164', '9564987', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993900'))
and (exists (select id from Placement where id = '1993900'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2011162', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '19387911', '6541621', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2011162'))
and (exists (select id from Placement where id = '2011162'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1998367', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38074638', '9565903', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1998367'))
and (exists (select id from Placement where id = '1998367'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1937932', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38074305', '9565040', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1937932'))
and (exists (select id from Placement where id = '1937932'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2036294', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38074305', '9565040', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2036294'))
and (exists (select id from Placement where id = '2036294'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012497', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38072164', '9565011', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012497'))
and (exists (select id from Placement where id = '2012497'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048174', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '31338910', '7328864', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048174'))
and (exists (select id from Placement where id = '2048174'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1863338', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38074608', '9565893', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1863338'))
and (exists (select id from Placement where id = '1863338'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035522', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38071263', '9565004', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035522'))
and (exists (select id from Placement where id = '2035522'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2027745', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38073947', '9565024', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2027745'))
and (exists (select id from Placement where id = '2027745'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1966956', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38071253', '9565002', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1966956'))
and (exists (select id from Placement where id = '1966956'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2005800', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38071253', '9565002', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2005800'))
and (exists (select id from Placement where id = '2005800'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1959638', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38075145', '9559770', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1959638'))
and (exists (select id from Placement where id = '1959638'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2025973', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38114414', '9567056', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2025973'))
and (exists (select id from Placement where id = '2025973'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1960637', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38071155', '9564986', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1960637'))
and (exists (select id from Placement where id = '1960637'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2025996', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38114650', '9567059', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2025996'))
and (exists (select id from Placement where id = '2025996'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1931624', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38074728', '9565944', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1931624'))
and (exists (select id from Placement where id = '1931624'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1986132', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114157', '9566109', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1986132'))
and (exists (select id from Placement where id = '1986132'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2014290', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '38071683', '9565006', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2014290'))
and (exists (select id from Placement where id = '2014290'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1986129', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38113996', '9566096', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1986129'))
and (exists (select id from Placement where id = '1986129'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1965427', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114842', '9566156', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1965427'))
and (exists (select id from Placement where id = '1965427'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024437', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114855', '9566157', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024437'))
and (exists (select id from Placement where id = '2024437'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1986136', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114172', '9566112', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1986136'))
and (exists (select id from Placement where id = '1986136'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1965435', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114875', '9566159', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1965435'))
and (exists (select id from Placement where id = '1965435'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1965436', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114875', '9566159', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1965436'))
and (exists (select id from Placement where id = '1965436'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1986319', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114180', '9566114', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1986319'))
and (exists (select id from Placement where id = '1986319'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1965428', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114797', '9566151', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1965428'))
and (exists (select id from Placement where id = '1965428'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1986134', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114165', '9566111', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1986134'))
and (exists (select id from Placement where id = '1986134'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1953531', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114402', '9566126', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1953531'))
and (exists (select id from Placement where id = '1953531'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1948244', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114409', '9566127', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1948244'))
and (exists (select id from Placement where id = '1948244'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1965433', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114887', '9566163', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1965433'))
and (exists (select id from Placement where id = '1965433'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015543', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114418', '9566128', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015543'))
and (exists (select id from Placement where id = '2015543'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1965434', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '38114892', '9566164', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1965434'))
and (exists (select id from Placement where id = '1965434'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026432', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114802', '9567912', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026432'))
and (exists (select id from Placement where id = '2026432'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996594', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114884', '9567937', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996594'))
and (exists (select id from Placement where id = '1996594'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026421', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114891', '9567941', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026421'))
and (exists (select id from Placement where id = '2026421'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026432', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114806', '9567915', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026432'))
and (exists (select id from Placement where id = '2026432'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026434', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114712', '9559887', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026434'))
and (exists (select id from Placement where id = '2026434'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026428', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38110747', '9566956', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026428'))
and (exists (select id from Placement where id = '2026428'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026422', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38112189', '9566965', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026422'))
and (exists (select id from Placement where id = '2026422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974853', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38074996', '9556793', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974853'))
and (exists (select id from Placement where id = '1974853'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996595', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114850', '9567928', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996595'))
and (exists (select id from Placement where id = '1996595'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976015', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38110316', '9566942', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976015'))
and (exists (select id from Placement where id = '1976015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026428', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38111344', '9566957', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026428'))
and (exists (select id from Placement where id = '2026428'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026433', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38110730', '9566953', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026433'))
and (exists (select id from Placement where id = '2026433'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996602', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114767', '9567904', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996602'))
and (exists (select id from Placement where id = '1996602'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026432', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114810', '9567916', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026432'))
and (exists (select id from Placement where id = '2026432'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996596', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114835', '9567924', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996596'))
and (exists (select id from Placement where id = '1996596'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026421', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114894', '9567942', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026421'))
and (exists (select id from Placement where id = '2026421'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026426', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38112208', '9566970', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026426'))
and (exists (select id from Placement where id = '2026426'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026431', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114400', '9559865', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026431'))
and (exists (select id from Placement where id = '2026431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996596', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114817', '9567917', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996596'))
and (exists (select id from Placement where id = '1996596'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976015', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38110317', '9566946', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976015'))
and (exists (select id from Placement where id = '1976015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026428', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38111654', '9566959', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026428'))
and (exists (select id from Placement where id = '2026428'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974853', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38075005', '9556795', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974853'))
and (exists (select id from Placement where id = '1974853'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026423', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114649', '9559872', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026423'))
and (exists (select id from Placement where id = '2026423'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026420', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114725', '9559892', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026420'))
and (exists (select id from Placement where id = '2026420'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996594', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114864', '9567933', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996594'))
and (exists (select id from Placement where id = '1996594'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026431', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114460', '9559869', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026431'))
and (exists (select id from Placement where id = '2026431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996595', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114857', '9567930', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996595'))
and (exists (select id from Placement where id = '1996595'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1974853', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38075009', '9556797', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1974853'))
and (exists (select id from Placement where id = '1974853'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026422', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38112193', '9566967', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026422'))
and (exists (select id from Placement where id = '2026422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996594', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114871', '9567936', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996594'))
and (exists (select id from Placement where id = '1996594'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996602', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114763', '9567902', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996602'))
and (exists (select id from Placement where id = '1996602'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2028717', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114795', '9567911', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2028717'))
and (exists (select id from Placement where id = '2028717'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026426', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38112460', '9566972', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026426'))
and (exists (select id from Placement where id = '2026426'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026420', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114744', '9567898', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026420'))
and (exists (select id from Placement where id = '2026420'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026421', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114898', '9567943', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026421'))
and (exists (select id from Placement where id = '2026421'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1976015', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38110692', '9566950', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1976015'))
and (exists (select id from Placement where id = '1976015'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2028717', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114783', '9567908', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2028717'))
and (exists (select id from Placement where id = '2028717'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2028717', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114779', '9567907', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2028717'))
and (exists (select id from Placement where id = '2028717'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026433', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38110738', '9566955', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026433'))
and (exists (select id from Placement where id = '2026433'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026426', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38112683', '9566974', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026426'))
and (exists (select id from Placement where id = '2026426'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026431', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114413', '9559866', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026431'))
and (exists (select id from Placement where id = '2026431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026420', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114733', '9567895', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026420'))
and (exists (select id from Placement where id = '2026420'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026433', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38110725', '9566952', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026433'))
and (exists (select id from Placement where id = '2026433'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996595', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114840', '9567926', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996595'))
and (exists (select id from Placement where id = '1996595'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026434', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114701', '9559884', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026434'))
and (exists (select id from Placement where id = '2026434'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026434', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114689', '9559880', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026434'))
and (exists (select id from Placement where id = '2026434'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996602', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114759', '9567901', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996602'))
and (exists (select id from Placement where id = '1996602'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1996596', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114824', '9567921', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1996596'))
and (exists (select id from Placement where id = '1996596'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026422', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38112196', '9566968', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026422'))
and (exists (select id from Placement where id = '2026422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026423', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114676', '9559874', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026423'))
and (exists (select id from Placement where id = '2026423'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026423', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '38114682', '9559878', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026423'))
and (exists (select id from Placement where id = '2026423'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1938034', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075143', '9556845', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1938034'))
and (exists (select id from Placement where id = '1938034'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048096', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075143', '9556845', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048096'))
and (exists (select id from Placement where id = '2048096'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1958293', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075326', '9556878', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1958293'))
and (exists (select id from Placement where id = '1958293'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993825', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38113840', '9567010', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993825'))
and (exists (select id from Placement where id = '1993825'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993218', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075108', '9556828', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993218'))
and (exists (select id from Placement where id = '1993218'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2008577', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075196', '9556858', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2008577'))
and (exists (select id from Placement where id = '2008577'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019277', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075196', '9556858', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019277'))
and (exists (select id from Placement where id = '2019277'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957955', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075337', '9556880', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957955'))
and (exists (select id from Placement where id = '1957955'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012506', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075023', '9556800', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012506'))
and (exists (select id from Placement where id = '2012506'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012518', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075029', '9556804', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012518'))
and (exists (select id from Placement where id = '2012518'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015394', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075149', '9556848', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015394'))
and (exists (select id from Placement where id = '2015394'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2002455', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075139', '9556844', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2002455'))
and (exists (select id from Placement where id = '2002455'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1984134', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075102', '9556825', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1984134'))
and (exists (select id from Placement where id = '1984134'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1958340', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075155', '9556851', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1958340'))
and (exists (select id from Placement where id = '1958340'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035681', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075126', '9556840', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035681'))
and (exists (select id from Placement where id = '2035681'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015961', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075147', '9556846', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015961'))
and (exists (select id from Placement where id = '2015961'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012580', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075058', '9556815', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012580'))
and (exists (select id from Placement where id = '2012580'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993886', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075277', '9556866', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993886'))
and (exists (select id from Placement where id = '1993886'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1987991', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075266', '9556863', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1987991'))
and (exists (select id from Placement where id = '1987991'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993911', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075289', '9556869', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993911'))
and (exists (select id from Placement where id = '1993911'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1956686', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075160', '9556852', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1956686'))
and (exists (select id from Placement where id = '1956686'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1994067', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075301', '9556872', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1994067'))
and (exists (select id from Placement where id = '1994067'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993909', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075279', '9556867', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993909'))
and (exists (select id from Placement where id = '1993909'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1978964', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075258', '9556862', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1978964'))
and (exists (select id from Placement where id = '1978964'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2008702', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075253', '9556861', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2008702'))
and (exists (select id from Placement where id = '2008702'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1956688', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075162', '9556855', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1956688'))
and (exists (select id from Placement where id = '1956688'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990642', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075308', '9556873', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990642'))
and (exists (select id from Placement where id = '1990642'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2032299', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075308', '9556873', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2032299'))
and (exists (select id from Placement where id = '2032299'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1958287', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075322', '9556877', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1958287'))
and (exists (select id from Placement where id = '1958287'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012578', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075053', '9556814', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012578'))
and (exists (select id from Placement where id = '2012578'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044198', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075081', '9556820', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044198'))
and (exists (select id from Placement where id = '2044198'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2025586', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075343', '9556884', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2025586'))
and (exists (select id from Placement where id = '2025586'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1994505', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075294', '9556870', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1994505'))
and (exists (select id from Placement where id = '1994505'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012583', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075074', '9556819', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012583'))
and (exists (select id from Placement where id = '2012583'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012519', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075033', '9556805', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012519'))
and (exists (select id from Placement where id = '2012519'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1956691', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075167', '9556856', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1956691'))
and (exists (select id from Placement where id = '1956691'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993221', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075118', '9556836', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993221'))
and (exists (select id from Placement where id = '1993221'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993224', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075118', '9556836', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993224'))
and (exists (select id from Placement where id = '1993224'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1993225', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075118', '9556836', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1993225'))
and (exists (select id from Placement where id = '1993225'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1984136', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075101', '9556824', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1984136'))
and (exists (select id from Placement where id = '1984136'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1958197', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075315', '9556875', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1958197'))
and (exists (select id from Placement where id = '1958197'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1988013', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075269', '9556865', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1988013'))
and (exists (select id from Placement where id = '1988013'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012560', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075050', '9556813', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012560'))
and (exists (select id from Placement where id = '2012560'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019294', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075050', '9556813', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019294'))
and (exists (select id from Placement where id = '2019294'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012523', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075048', '9556812', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012523'))
and (exists (select id from Placement where id = '2012523'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012525', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075048', '9556812', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012525'))
and (exists (select id from Placement where id = '2012525'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1983145', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075328', '9556879', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1983145'))
and (exists (select id from Placement where id = '1983145'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012521', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075039', '9556810', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012521'))
and (exists (select id from Placement where id = '2012521'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2014153', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075039', '9556810', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2014153'))
and (exists (select id from Placement where id = '2014153'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035796', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '38075134', '9556842', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035796'))
and (exists (select id from Placement where id = '2035796'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042315', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '38074613', '9565895', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042315'))
and (exists (select id from Placement where id = '2042315'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2042326', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '38074619', '9565897', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2042326'))
and (exists (select id from Placement where id = '2042326'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2014851', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '38112700', '9566045', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2014851'))
and (exists (select id from Placement where id = '2014851'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2014827', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '38074984', '9565971', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2014827'))
and (exists (select id from Placement where id = '2014827'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2040246', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '38112715', '9566047', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2040246'))
and (exists (select id from Placement where id = '2040246'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2040259', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '38112715', '9566047', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2040259'))
and (exists (select id from Placement where id = '2040259'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2040242', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '38112720', '9566048', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2040242'))
and (exists (select id from Placement where id = '2040242'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2040402', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '38112720', '9566048', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2040402'))
and (exists (select id from Placement where id = '2040402'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048177', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '32153395', '7378653', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048177'))
and (exists (select id from Placement where id = '2048177'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048178', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '34549343', '7600173', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048178'))
and (exists (select id from Placement where id = '2048178'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2020513', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '38075291', '9559775', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2020513'))
and (exists (select id from Placement where id = '2020513'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024557', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '38075383', '9566896', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024557'))
and (exists (select id from Placement where id = '2024557'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024563', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '38075574', '9566904', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024563'))
and (exists (select id from Placement where id = '2024563'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024565', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '38075283', '9556888', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024565'))
and (exists (select id from Placement where id = '2024565'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024564', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '38080929', '9566918', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024564'))
and (exists (select id from Placement where id = '2024564'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2036168', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '38112191', '9566971', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2036168'))
and (exists (select id from Placement where id = '2036168'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024562', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '38110729', '9566963', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024562'))
and (exists (select id from Placement where id = '2024562'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2044157', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '38114177', '9560777', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2044157'))
and (exists (select id from Placement where id = '2044157'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035760', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074416', '9565055', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035760'))
and (exists (select id from Placement where id = '2035760'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035757', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074415', '9565054', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035757'))
and (exists (select id from Placement where id = '2035757'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957623', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074990', '9556790', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957623'))
and (exists (select id from Placement where id = '1957623'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957620', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38075016', '9556799', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957620'))
and (exists (select id from Placement where id = '1957620'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2045360', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38114374', '9567046', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2045360'))
and (exists (select id from Placement where id = '2045360'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030416', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074878', '9556775', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030416'))
and (exists (select id from Placement where id = '2030416'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015590', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37659278', '9475427', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015590'))
and (exists (select id from Placement where id = '2015590'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2021778', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38114277', '9567038', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2021778'))
and (exists (select id from Placement where id = '2021778'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037528', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37659275', '9475426', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037528'))
and (exists (select id from Placement where id = '2037528'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035755', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074414', '9565053', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035755'))
and (exists (select id from Placement where id = '2035755'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957620', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38075028', '9556807', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957620'))
and (exists (select id from Placement where id = '1957620'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030415', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074778', '9556773', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030415'))
and (exists (select id from Placement where id = '2030415'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2026198', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '35460021', '7759673', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2026198'))
and (exists (select id from Placement where id = '2026198'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035764', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074418', '9565057', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035764'))
and (exists (select id from Placement where id = '2035764'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037529', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37659266', '9475425', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037529'))
and (exists (select id from Placement where id = '2037529'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2037530', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37659266', '9475425', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2037530'))
and (exists (select id from Placement where id = '2037530'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030416', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074922', '9556781', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030416'))
and (exists (select id from Placement where id = '2030416'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2021716', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38114183', '9567033', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2021716'))
and (exists (select id from Placement where id = '2021716'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035743', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074409', '9565048', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035743'))
and (exists (select id from Placement where id = '2035743'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035752', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074412', '9565051', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035752'))
and (exists (select id from Placement where id = '2035752'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035748', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074411', '9565050', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035748'))
and (exists (select id from Placement where id = '2035748'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1982199', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38114161', '9567028', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1982199'))
and (exists (select id from Placement where id = '1982199'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035742', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074408', '9565047', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035742'))
and (exists (select id from Placement where id = '2035742'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015600', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37659296', '9475431', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015600'))
and (exists (select id from Placement where id = '2015600'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957623', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074970', '9556787', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957623'))
and (exists (select id from Placement where id = '1957623'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957623', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074925', '9556784', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957623'))
and (exists (select id from Placement where id = '1957623'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957613', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074689', '9556768', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957613'))
and (exists (select id from Placement where id = '1957613'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030422', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074689', '9556768', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030422'))
and (exists (select id from Placement where id = '2030422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957620', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38075019', '9556801', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957620'))
and (exists (select id from Placement where id = '1957620'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1957613', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074765', '9556770', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1957613'))
and (exists (select id from Placement where id = '1957613'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030422', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074765', '9556770', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030422'))
and (exists (select id from Placement where id = '2030422'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2030416', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074920', '9556780', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2030416'))
and (exists (select id from Placement where id = '2030416'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015792', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37659289', '9475429', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015792'))
and (exists (select id from Placement where id = '2015792'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015793', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '37659289', '9475429', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015793'))
and (exists (select id from Placement where id = '2015793'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035746', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074410', '9565049', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035746'))
and (exists (select id from Placement where id = '2035746'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2021780', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38114324', '9567044', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2021780'))
and (exists (select id from Placement where id = '2021780'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2035754', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38074413', '9565052', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2035754'))
and (exists (select id from Placement where id = '2035754'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2021691', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '38114173', '9567030', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2021691'))
and (exists (select id from Placement where id = '2021691'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048185', '2021-07-29 13:10:00', 'DE_EMD_APP_20210729_00003780.DAT', '22199328', '6717103', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048185'))
and (exists (select id from Placement where id = '2048185'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1920828', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '21067028', '6633708', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1920828'))
and (exists (select id from Placement where id = '1920828'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1920828', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '15364206', '6203759', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1920828'))
and (exists (select id from Placement where id = '1920828'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048194', '2021-07-29 13:10:00', 'DE_YHD_APP_20210729_00003786.DAT', '3461094', '1538836', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048194'))
and (exists (select id from Placement where id = '2048194'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048196', '2021-07-29 13:10:00', 'DE_WES_APP_20210729_00003789.DAT', '36181541', '8070908', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048196'))
and (exists (select id from Placement where id = '2048196'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048196', '2021-07-29 13:10:00', 'DE_WES_APP_20210729_00003789.DAT', '36181543', '8070910', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048196'))
and (exists (select id from Placement where id = '2048196'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048196', '2021-07-29 13:10:00', 'DE_WES_APP_20210729_00003789.DAT', '36181545', '8070912', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048196'))
and (exists (select id from Placement where id = '2048196'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048198', '2021-07-29 13:10:00', 'DE_EOE_APP_20210729_00003787.DAT', '15005606', '6174175', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048198'))
and (exists (select id from Placement where id = '2048198'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048198', '2021-07-29 13:10:00', 'DE_EOE_APP_20210729_00003787.DAT', '24179759', '6860869', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048198'))
and (exists (select id from Placement where id = '2048198'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048205', '2021-07-29 13:10:00', 'DE_NWN_APP_20210729_00003785.DAT', '32734457', '7412756', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048205'))
and (exists (select id from Placement where id = '2048205'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048206', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '14026155', '6080285', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048206'))
and (exists (select id from Placement where id = '2048206'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048213', '2021-07-29 13:10:00', 'DE_NWN_APP_20210729_00003785.DAT', '28325081', '7145289', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048213'))
and (exists (select id from Placement where id = '2048213'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048220', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '24732268', '6910670', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048220'))
and (exists (select id from Placement where id = '2048220'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048220', '2021-07-29 13:10:00', 'DE_OXF_APP_20210729_00003788.DAT', '28264207', '7132629', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048220'))
and (exists (select id from Placement where id = '2048220'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048221', '2021-07-29 13:10:00', 'DE_NWN_APP_20210729_00003785.DAT', '33451879', '7490825', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048221'))
and (exists (select id from Placement where id = '2048221'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048222', '2021-07-29 13:10:00', 'DE_NTH_APP_20210729_00003782.DAT', '20492866', '6605070', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048222'))
and (exists (select id from Placement where id = '2048222'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048223', '2021-07-29 13:10:00', 'DE_WMD_APP_20210729_00003783.DAT', '22079408', '6709538', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048223'))
and (exists (select id from Placement where id = '2048223'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048226', '2021-07-29 13:10:00', 'DE_NWN_APP_20210729_00003785.DAT', '31324558', '7323454', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048226'))
and (exists (select id from Placement where id = '2048226'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048228', '2021-07-29 13:10:00', 'DE_MER_APP_20210729_00003791.DAT', '13267127', '5993481', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048228'))
and (exists (select id from Placement where id = '2048228'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048302', '2021-07-29 13:10:01', 'DE_LDN_APP_20210729_00003790.DAT', '37007304', '9086246', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048302'))
and (exists (select id from Placement where id = '2048302'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048305', '2021-07-29 13:10:01', 'DE_SEV_APP_20210729_00003792.DAT', '25639102', '6956378', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048305'))
and (exists (select id from Placement where id = '2048305'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1983676', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '27775945', '7103788', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1983676'))
and (exists (select id from Placement where id = '1983676'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048308', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '35222444', '7737745', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048308'))
and (exists (select id from Placement where id = '2048308'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048311', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '15644489', '6224649', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048311'))
and (exists (select id from Placement where id = '2048311'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048330', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '23498819', '6823310', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048330'))
and (exists (select id from Placement where id = '2048330'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048375', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25637239', '6954280', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048375'))
and (exists (select id from Placement where id = '2048375'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048377', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '33366574', '7467593', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048377'))
and (exists (select id from Placement where id = '2048377'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2015340', '2021-07-30 13:10:01', 'DE_OXF_APP_20210730_00003795.DAT', '15679012', '6227031', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2015340'))
and (exists (select id from Placement where id = '2015340'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048416', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '9472885', '5329880', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048416'))
and (exists (select id from Placement where id = '2048416'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048424', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '33092292', '7441543', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048424'))
and (exists (select id from Placement where id = '2048424'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048431', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '5293847', '2027900', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048431'))
and (exists (select id from Placement where id = '2048431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048434', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '18235533', '6447192', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048434'))
and (exists (select id from Placement where id = '2048434'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016356', '2021-07-30 13:10:00', 'DE_MER_APP_20210730_00003798.DAT', '31506105', '7340454', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016356'))
and (exists (select id from Placement where id = '2016356'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048447', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '32779806', '7417915', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048447'))
and (exists (select id from Placement where id = '2048447'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048449', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25633492', '6955252', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048449'))
and (exists (select id from Placement where id = '2048449'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017184', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '20874140', '6625357', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017184'))
and (exists (select id from Placement where id = '2017184'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017179', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '20874140', '6625357', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017179'))
and (exists (select id from Placement where id = '2017179'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017527', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461932', '7079106', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017527'))
and (exists (select id from Placement where id = '2017527'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017527', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '33183591', '7452739', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017527'))
and (exists (select id from Placement where id = '2017527'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012369', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324017', '6193711', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012369'))
and (exists (select id from Placement where id = '2012369'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017533', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461965', '7079114', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017533'))
and (exists (select id from Placement where id = '2017533'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016970', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13957422', '6073010', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016970'))
and (exists (select id from Placement where id = '2016970'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017528', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461932', '7079106', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017528'))
and (exists (select id from Placement where id = '2017528'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017528', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '33183591', '7452739', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017528'))
and (exists (select id from Placement where id = '2017528'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012413', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15628487', '6223478', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012413'))
and (exists (select id from Placement where id = '2012413'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017267', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14552599', '6126480', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017267'))
and (exists (select id from Placement where id = '2017267'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012503', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '29660779', '7206387', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012503'))
and (exists (select id from Placement where id = '2012503'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016637', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15628025', '6223393', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016637'))
and (exists (select id from Placement where id = '2016637'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017126', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324380', '6198858', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017126'))
and (exists (select id from Placement where id = '2017126'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016273', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15349617', '6201743', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016273'))
and (exists (select id from Placement where id = '2016273'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1861181', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14965393', '6173329', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1861181'))
and (exists (select id from Placement where id = '1861181'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017004', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324011', '6193705', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017004'))
and (exists (select id from Placement where id = '2017004'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017341', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34154113', '7570828', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017341'))
and (exists (select id from Placement where id = '2017341'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017438', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '30305828', '7248613', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017438'))
and (exists (select id from Placement where id = '2017438'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017113', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324379', '6198857', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017113'))
and (exists (select id from Placement where id = '2017113'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017107', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324014', '6193708', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017107'))
and (exists (select id from Placement where id = '2017107'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016047', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14552620', '6126488', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016047'))
and (exists (select id from Placement where id = '2016047'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017238', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '35251461', '7741019', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017238'))
and (exists (select id from Placement where id = '2017238'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017238', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '24608673', '6896823', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017238'))
and (exists (select id from Placement where id = '2017238'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012505', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '22669742', '6759327', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012505'))
and (exists (select id from Placement where id = '2012505'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012505', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '24615297', '6896872', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012505'))
and (exists (select id from Placement where id = '2012505'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017372', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34773411', '7629063', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017372'))
and (exists (select id from Placement where id = '2017372'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017117', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324379', '6198857', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017117'))
and (exists (select id from Placement where id = '2017117'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017252', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14552611', '6126483', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017252'))
and (exists (select id from Placement where id = '2017252'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012485', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34157496', '7569931', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012485'))
and (exists (select id from Placement where id = '2012485'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017432', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '6972144', '4740147', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017432'))
and (exists (select id from Placement where id = '2017432'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017405', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '23006611', '6783356', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017405'))
and (exists (select id from Placement where id = '2017405'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017405', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '37212500', '9233712', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017405'))
and (exists (select id from Placement where id = '2017405'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017402', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '23006611', '6783356', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017402'))
and (exists (select id from Placement where id = '2017402'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017402', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '37212500', '9233712', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017402'))
and (exists (select id from Placement where id = '2017402'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012531', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '2571676', '1304062', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012531'))
and (exists (select id from Placement where id = '2012531'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017448', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15597242', '6222475', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017448'))
and (exists (select id from Placement where id = '2017448'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012520', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '25900735', '6983970', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012520'))
and (exists (select id from Placement where id = '2012520'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016042', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14965346', '6173322', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016042'))
and (exists (select id from Placement where id = '2016042'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017236', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34982863', '7672958', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017236'))
and (exists (select id from Placement where id = '2017236'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017370', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34773411', '7629063', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017370'))
and (exists (select id from Placement where id = '2017370'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017233', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34982863', '7672958', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017233'))
and (exists (select id from Placement where id = '2017233'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017001', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324010', '6193704', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017001'))
and (exists (select id from Placement where id = '2017001'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017526', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461931', '7079105', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017526'))
and (exists (select id from Placement where id = '2017526'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017419', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14552606', '6126481', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017419'))
and (exists (select id from Placement where id = '2017419'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016153', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '25833819', '6974171', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016153'))
and (exists (select id from Placement where id = '2016153'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017219', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34982855', '7672957', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017219'))
and (exists (select id from Placement where id = '2017219'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017426', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15597229', '6222473', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017426'))
and (exists (select id from Placement where id = '2017426'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012539', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '23285154', '6809569', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012539'))
and (exists (select id from Placement where id = '2012539'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012570', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13317215', '6000021', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012570'))
and (exists (select id from Placement where id = '2012570'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012478', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '2646792', '1352405', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012478'))
and (exists (select id from Placement where id = '2012478'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017430', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15597229', '6222473', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017430'))
and (exists (select id from Placement where id = '2017430'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017241', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '25833918', '6974184', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017241'))
and (exists (select id from Placement where id = '2017241'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017128', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324381', '6198862', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017128'))
and (exists (select id from Placement where id = '2017128'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017531', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461945', '7079113', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017531'))
and (exists (select id from Placement where id = '2017531'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017531', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461908', '7079102', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017531'))
and (exists (select id from Placement where id = '2017531'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017283', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '25833802', '6974168', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017283'))
and (exists (select id from Placement where id = '2017283'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017283', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '36041162', '8031013', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017283'))
and (exists (select id from Placement where id = '2017283'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012431', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '5239088', '2019947', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012431'))
and (exists (select id from Placement where id = '2012431'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012433', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461943', '7079111', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012433'))
and (exists (select id from Placement where id = '2012433'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012493', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '2646801', '1352409', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012493'))
and (exists (select id from Placement where id = '2012493'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048471', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '31026928', '7306349', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048471'))
and (exists (select id from Placement where id = '2048471'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017466', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13319403', '6000073', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017466'))
and (exists (select id from Placement where id = '2017466'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012529', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15349623', '6201745', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012529'))
and (exists (select id from Placement where id = '2012529'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017468', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13319404', '6000072', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017468'))
and (exists (select id from Placement where id = '2017468'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017324', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34154083', '7570825', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017324'))
and (exists (select id from Placement where id = '2017324'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017462', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13319402', '6000074', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017462'))
and (exists (select id from Placement where id = '2017462'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016368', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '28149331', '7128972', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016368'))
and (exists (select id from Placement where id = '2016368'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017545', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '6225953', '4612431', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017545'))
and (exists (select id from Placement where id = '2017545'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017470', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13319405', '6000075', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017470'))
and (exists (select id from Placement where id = '2017470'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017442', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '7118067', '4785232', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017442'))
and (exists (select id from Placement where id = '2017442'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017347', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34154175', '7570840', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017347'))
and (exists (select id from Placement where id = '2017347'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017514', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15349634', '6201748', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017514'))
and (exists (select id from Placement where id = '2017514'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016305', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15456757', '6212392', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016305'))
and (exists (select id from Placement where id = '2016305'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016991', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324009', '6193703', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016991'))
and (exists (select id from Placement where id = '2016991'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012584', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '35006686', '7679926', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012584'))
and (exists (select id from Placement where id = '2012584'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012524', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '24615289', '6896871', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012524'))
and (exists (select id from Placement where id = '2012524'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017367', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '33290778', '7463638', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017367'))
and (exists (select id from Placement where id = '2017367'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017544', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '37642083', '9435597', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017544'))
and (exists (select id from Placement where id = '2017544'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017264', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34982756', '7664568', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017264'))
and (exists (select id from Placement where id = '2017264'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016941', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13957400', '6073008', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016941'))
and (exists (select id from Placement where id = '2016941'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016615', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461944', '7079112', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016615'))
and (exists (select id from Placement where id = '2016615'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012417', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15628482', '6223474', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012417'))
and (exists (select id from Placement where id = '2012417'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012532', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15349605', '6201739', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012532'))
and (exists (select id from Placement where id = '2012532'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012561', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '23285144', '6809567', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012561'))
and (exists (select id from Placement where id = '2012561'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016332', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13317216', '6000020', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016332'))
and (exists (select id from Placement where id = '2016332'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017334', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '2646793', '1352406', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017334'))
and (exists (select id from Placement where id = '2017334'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012538', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '23087999', '6793180', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012538'))
and (exists (select id from Placement where id = '2012538'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016972', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13957471', '6073021', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016972'))
and (exists (select id from Placement where id = '2016972'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012423', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15572746', '6222078', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012423'))
and (exists (select id from Placement where id = '2012423'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012375', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324022', '6193709', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012375'))
and (exists (select id from Placement where id = '2012375'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017329', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34154028', '7570823', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017329'))
and (exists (select id from Placement where id = '2017329'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012471', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14965377', '6173327', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012471'))
and (exists (select id from Placement where id = '2012471'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016063', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461941', '7079110', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016063'))
and (exists (select id from Placement where id = '2016063'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1866021', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14965324', '6173320', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1866021'))
and (exists (select id from Placement where id = '1866021'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012534', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15349610', '6201740', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012534'))
and (exists (select id from Placement where id = '2012534'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012534', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '36438436', '8451476', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012534'))
and (exists (select id from Placement where id = '2012534'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017250', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34982756', '7664568', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017250'))
and (exists (select id from Placement where id = '2017250'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016982', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '2476904', '1246945', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016982'))
and (exists (select id from Placement where id = '2016982'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016083', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34157469', '7569928', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016083'))
and (exists (select id from Placement where id = '2016083'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012480', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '267666', '388302', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012480'))
and (exists (select id from Placement where id = '2012480'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017524', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461930', '7079104', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017524'))
and (exists (select id from Placement where id = '2017524'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017259', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34982756', '7664568', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017259'))
and (exists (select id from Placement where id = '2017259'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016197', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '22833405', '6772193', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016197'))
and (exists (select id from Placement where id = '2016197'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017245', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13362367', '6005299', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017245'))
and (exists (select id from Placement where id = '2017245'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012522', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '22839281', '6772241', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012522'))
and (exists (select id from Placement where id = '2012522'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017223', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '9655118', '5385460', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017223'))
and (exists (select id from Placement where id = '2017223'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017019', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324015', '6193716', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017019'))
and (exists (select id from Placement where id = '2017019'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012420', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15628277', '6222790', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012420'))
and (exists (select id from Placement where id = '2012420'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017008', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324012', '6193706', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017008'))
and (exists (select id from Placement where id = '2017008'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017089', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '20874108', '6626921', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017089'))
and (exists (select id from Placement where id = '2017089'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016044', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14552623', '6126492', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016044'))
and (exists (select id from Placement where id = '2016044'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017006', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324012', '6193706', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017006'))
and (exists (select id from Placement where id = '2017006'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012581', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '35006684', '7679924', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012581'))
and (exists (select id from Placement where id = '2012581'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1753948', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14965359', '6173324', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1753948'))
and (exists (select id from Placement where id = '1753948'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012563', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13310569', '6000823', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012563'))
and (exists (select id from Placement where id = '2012563'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017209', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324027', '6199189', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017209'))
and (exists (select id from Placement where id = '2017209'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1861201', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '14965368', '6173326', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1861201'))
and (exists (select id from Placement where id = '1861201'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017270', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '24797142', '6910887', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017270'))
and (exists (select id from Placement where id = '2017270'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017254', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13362384', '6005315', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017254'))
and (exists (select id from Placement where id = '2017254'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017254', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '25900097', '6981196', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017254'))
and (exists (select id from Placement where id = '2017254'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017011', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15324013', '6193707', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017011'))
and (exists (select id from Placement where id = '2017011'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017398', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '35186765', '7689284', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017398'))
and (exists (select id from Placement where id = '2017398'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016081', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '7987603', '4989783', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016081'))
and (exists (select id from Placement where id = '2016081'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017436', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '5240615', '2021310', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017436'))
and (exists (select id from Placement where id = '2017436'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012602', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13302782', '5996767', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012602'))
and (exists (select id from Placement where id = '2012602'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012442', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '30207888', '7237751', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012442'))
and (exists (select id from Placement where id = '2012442'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016298', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15456756', '6212388', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016298'))
and (exists (select id from Placement where id = '2016298'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017421', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '36041209', '8031014', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017421'))
and (exists (select id from Placement where id = '2017421'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017374', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34773413', '7629065', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017374'))
and (exists (select id from Placement where id = '2017374'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017316', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '34152800', '7570792', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017316'))
and (exists (select id from Placement where id = '2017316'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017522', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '18536834', '6474039', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017522'))
and (exists (select id from Placement where id = '2017522'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016062', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '5239732', '2020996', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016062'))
and (exists (select id from Placement where id = '2016062'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2016610', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '27461939', '7079109', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2016610'))
and (exists (select id from Placement where id = '2016610'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012526', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '25833862', '6974173', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012526'))
and (exists (select id from Placement where id = '2012526'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2012500', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '22669758', '6759329', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2012500'))
and (exists (select id from Placement where id = '2012500'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017218', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13362382', '6005318', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017218'))
and (exists (select id from Placement where id = '2017218'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048601', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '15820116', '6235248', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048601'))
and (exists (select id from Placement where id = '2048601'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1800410', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25787075', '6964376', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1800410'))
and (exists (select id from Placement where id = '1800410'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1887796', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25637391', '6954289', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1887796'))
and (exists (select id from Placement where id = '1887796'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1729762', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '23491416', '6820117', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1729762'))
and (exists (select id from Placement where id = '1729762'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1729762', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25787075', '6964376', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1729762'))
and (exists (select id from Placement where id = '1729762'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048627', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '28324187', '7143966', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048627'))
and (exists (select id from Placement where id = '2048627'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1800410', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '23491416', '6820117', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1800410'))
and (exists (select id from Placement where id = '1800410'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1887779', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '13577248', '6024077', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1887779'))
and (exists (select id from Placement where id = '1887779'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1889517', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25713209', '6957998', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1889517'))
and (exists (select id from Placement where id = '1889517'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1887926', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '13577248', '6024077', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1887926'))
and (exists (select id from Placement where id = '1887926'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1990057', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '24252928', '6870901', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1990057'))
and (exists (select id from Placement where id = '1990057'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2017725', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38127415', '9569019', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2017725'))
and (exists (select id from Placement where id = '2017725'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2004396', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38124102', '9567235', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2004396'))
and (exists (select id from Placement where id = '2004396'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2004467', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38124102', '9567235', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2004467'))
and (exists (select id from Placement where id = '2004467'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2004167', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13957140', '6072962', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2004167'))
and (exists (select id from Placement where id = '2004167'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2019064', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38125607', '9567240', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2019064'))
and (exists (select id from Placement where id = '2019064'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2022478', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38125607', '9567240', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2022478'))
and (exists (select id from Placement where id = '2022478'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2024774', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38125607', '9567240', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2024774'))
and (exists (select id from Placement where id = '2024774'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2025849', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38125607', '9567240', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2025849'))
and (exists (select id from Placement where id = '2025849'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2036953', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38125607', '9567240', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2036953'))
and (exists (select id from Placement where id = '2036953'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2001697', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13956969', '6072904', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2001697'))
and (exists (select id from Placement where id = '2001697'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1944112', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38125599', '9567238', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1944112'))
and (exists (select id from Placement where id = '1944112'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1944131', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38125599', '9567238', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1944131'))
and (exists (select id from Placement where id = '1944131'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1944134', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38125599', '9567238', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1944134'))
and (exists (select id from Placement where id = '1944134'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1943676', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38124884', '9567237', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1943676'))
and (exists (select id from Placement where id = '1943676'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1944102', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38124884', '9567237', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1944102'))
and (exists (select id from Placement where id = '1944102'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1955920', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38124884', '9567237', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1955920'))
and (exists (select id from Placement where id = '1955920'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981145', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38124884', '9567237', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981145'))
and (exists (select id from Placement where id = '1981145'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2004367', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38124884', '9567237', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2004367'))
and (exists (select id from Placement where id = '2004367'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2001712', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '13956966', '6072899', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2001712'))
and (exists (select id from Placement where id = '2001712'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1977656', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '38131736', '9570069', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1977656'))
and (exists (select id from Placement where id = '1977656'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1971198', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '38130421', '9570054', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1971198'))
and (exists (select id from Placement where id = '1971198'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1953725', '2021-07-30 13:10:00', 'DE_MER_APP_20210730_00003798.DAT', '32223827', '7382081', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1953725'))
and (exists (select id from Placement where id = '1953725'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981460', '2021-07-30 13:10:00', 'DE_MER_APP_20210730_00003798.DAT', '32121851', '7372442', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981460'))
and (exists (select id from Placement where id = '1981460'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1981487', '2021-07-30 13:10:00', 'DE_MER_APP_20210730_00003798.DAT', '32121851', '7372442', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1981487'))
and (exists (select id from Placement where id = '1981487'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048633', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '11656938', '5760724', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048633'))
and (exists (select id from Placement where id = '2048633'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048634', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '23278058', '6809447', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048634'))
and (exists (select id from Placement where id = '2048634'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009042', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '38125765', '9568976', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009042'))
and (exists (select id from Placement where id = '2009042'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2009076', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '38125965', '9568979', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2009076'))
and (exists (select id from Placement where id = '2009076'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2018088', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '38116895', '9567168', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2018088'))
and (exists (select id from Placement where id = '2018088'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048660', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '35251216', '7741978', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048660'))
and (exists (select id from Placement where id = '2048660'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048709', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25713046', '6957955', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048709'))
and (exists (select id from Placement where id = '2048709'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048714', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '33361816', '7469402', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048714'))
and (exists (select id from Placement where id = '2048714'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048720', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '2520623', '1259438', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048720'))
and (exists (select id from Placement where id = '2048720'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048723', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '26802357', '7038327', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048723'))
and (exists (select id from Placement where id = '2048723'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048725', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '24821680', '6909139', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048725'))
and (exists (select id from Placement where id = '2048725'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048727', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '7061777', '4767270', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048727'))
and (exists (select id from Placement where id = '2048727'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048729', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '7061799', '4767277', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048729'))
and (exists (select id from Placement where id = '2048729'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048731', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '25026003', '6914618', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048731'))
and (exists (select id from Placement where id = '2048731'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048739', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '12589061', '5887152', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048739'))
and (exists (select id from Placement where id = '2048739'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048744', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '11978632', '5815237', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048744'))
and (exists (select id from Placement where id = '2048744'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048753', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '37892955', '9541946', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048753'))
and (exists (select id from Placement where id = '2048753'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048755', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '37892914', '9540968', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048755'))
and (exists (select id from Placement where id = '2048755'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048760', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '9127933', '5194480', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048760'))
and (exists (select id from Placement where id = '2048760'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048761', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '11784250', '5778429', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048761'))
and (exists (select id from Placement where id = '2048761'))
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048715', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '36698524', '9000003', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048715'))
and (exists (select id from Placement where id = '2048715'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048730', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '2638167', '1347735', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048730'))
and (exists (select id from Placement where id = '2048730'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048735', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '26261187', '7014818', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048735'))
and (exists (select id from Placement where id = '2048735'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048737', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '7061792', '4767273', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048737'))
and (exists (select id from Placement where id = '2048737'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048741', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '2615837', '1347054', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048741'))
and (exists (select id from Placement where id = '2048741'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048750', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '9341878', '5217430', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048750'))
and (exists (select id from Placement where id = '2048750'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048752', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '25242137', '6935905', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048752'))
and (exists (select id from Placement where id = '2048752'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048759', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '23037430', '6784579', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048759'))
and (exists (select id from Placement where id = '2048759'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048763', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '33179756', '7446968', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048763'))
and (exists (select id from Placement where id = '2048763'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048764', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '37769846', '9516874', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048764'))
and (exists (select id from Placement where id = '2048764'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048822', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '6153106', '4595862', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048822'))
and (exists (select id from Placement where id = '2048822'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048733', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '37382666', '9335891', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048733'))
and (exists (select id from Placement where id = '2048733'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048764', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '9711913', '5400493', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048764'))
and (exists (select id from Placement where id = '2048764'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048765', '2021-07-30 13:10:00', 'DE_EOE_APP_20210730_00003793.DAT', '37698101', '9503679', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048765'))
and (exists (select id from Placement where id = '2048765'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048766', '2021-07-30 13:10:00', 'DE_MER_APP_20210730_00003798.DAT', '36046276', '8040993', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048766'))
and (exists (select id from Placement where id = '2048766'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048820', '2021-07-30 13:10:00', 'DE_MER_APP_20210730_00003798.DAT', '27416760', '7074275', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048820'))
and (exists (select id from Placement where id = '2048820'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048822', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '36892089', '9033100', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048822'))
and (exists (select id from Placement where id = '2048822'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2033211', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '36037788', '8020937', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2033211'))
and (exists (select id from Placement where id = '2033211'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '1793640', '2021-07-30 13:10:00', 'DE_LDN_APP_20210730_00003794.DAT', '22171014', '6714909', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '1793640'))
and (exists (select id from Placement where id = '1793640'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048927', '2021-07-30 13:10:00', 'DE_MER_APP_20210730_00003798.DAT', '18345859', '6461055', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048927'))
and (exists (select id from Placement where id = '2048927'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2048991', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25829767', '6972012', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2048991'))
and (exists (select id from Placement where id = '2048991'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2049018', '2021-07-30 13:10:01', 'DE_WES_APP_20210730_00003797.DAT', '33140679', '7448426', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2049018'))
and (exists (select id from Placement where id = '2049018'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2049029', '2021-07-30 13:10:00', 'DE_SEV_APP_20210730_00003796.DAT', '25654652', '6956519', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2049029'))
and (exists (select id from Placement where id = '2049029'));
insert into PlacementEsrEvent (placementId, eventDateTime, filename, positionNumber, positionId, status)
select '2049045', '2021-07-30 13:10:01', 'DE_OXF_APP_20210730_00003795.DAT', '33178312', '7446930', 'EXPORTED'
where not (exists (select placementId from PlacementEsrEvent where placementId = '2049045'))
and (exists (select id from Placement where id = '2049045'));

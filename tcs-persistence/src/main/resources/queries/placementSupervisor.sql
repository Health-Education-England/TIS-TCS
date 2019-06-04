select p.id, trim(concat(cd.forenames, ' ', cd.surname)) as name, p.publicHealthNumber, gmc.gmcNumber, gdc.gdcNumber, ps.type
from Person p 
left join ContactDetails cd 
  on cd.id = p.id 
left join GmcDetails gmc 
  on gmc.id = p.id 
left join GdcDetails gdc 
  on gdc.id = p.id
inner join PlacementSupervisor ps
  on ps.personId = p.id
where ps.placementId in (:ids)
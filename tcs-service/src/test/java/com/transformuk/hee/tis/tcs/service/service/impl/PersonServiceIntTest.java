package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.service.Application;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.RightToWorkRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PersonServiceIntTest {

  @Autowired
  private PersonService personService;
  @Autowired
  private PersonMapper personMapper;
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private GdcDetailsRepository gdcDetailsRepository;
  @Autowired
  private GmcDetailsRepository gmcDetailsRepository;
  @Autowired
  private ContactDetailsRepository contactDetailsRepository;
  @Autowired
  private PersonalDetailsRepository personalDetailsRepository;
  @Autowired
  private RightToWorkRepository rightToWorkRepository;
  @MockBean
  private PermissionService permissionServiceMock;

  private PersonDTO personDTO;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    personDTO = buildPerson();
    personDTO.setGmcDetails(buildGmcDetails());
    personDTO.setGdcDetails(buildGdcDetails());
    personDTO.setContactDetails(buildContactDetails());
    personDTO.setPersonalDetails(buildPersonalDetails());

    when(permissionServiceMock.canEditSensitiveData()).thenReturn(true);
    when(permissionServiceMock.canEditSensitiveData()).thenReturn(true);
  }

  private PersonalDetailsDTO buildPersonalDetails() {
    PersonalDetailsDTO personalDetailsDTO = new PersonalDetailsDTO();
    personalDetailsDTO.setGender("M");
    personalDetailsDTO.setEthnicOrigin("origin");
    personalDetailsDTO.setMaritalStatus("single");
    return personalDetailsDTO;
  }

  private ContactDetailsDTO buildContactDetails() {
    ContactDetailsDTO contactDetailsDTO = new ContactDetailsDTO();
    contactDetailsDTO.setAddress1("add line 1");
    contactDetailsDTO.setEmail("email address");
    contactDetailsDTO.setForenames("forename");
    contactDetailsDTO.setSurname("surname");
    return contactDetailsDTO;
  }

  private PersonDTO buildPerson() {
    PersonDTO personDTO = new PersonDTO();
    personDTO.setComments("comments");
    personDTO.setRegulator("regulator");
    personDTO.setRole("role");
    return personDTO;
  }

  private GmcDetailsDTO buildGmcDetails() {
    GmcDetailsDTO gmcDetails = new GmcDetailsDTO();
    gmcDetails.setGmcNumber("gmc number");
    gmcDetails.setGmcStatus("gmc status");
    return gmcDetails;
  }

  private GdcDetailsDTO buildGdcDetails() {
    GdcDetailsDTO gdcDetails = new GdcDetailsDTO();
    gdcDetails.setGdcNumber("gdc number");
    gdcDetails.setGdcStatus("gdc status");
    return gdcDetails;
  }

  @Test
  @Transactional
  public void createShouldCreateNewPersonWithLinkedEntities() {
    int beforeGdcSize = gdcDetailsRepository.findAll().size();
    int beforeGmcSize = gmcDetailsRepository.findAll().size();
    int beforeContactSize = contactDetailsRepository.findAll().size();
    int beforePersonalDetailsSize = personalDetailsRepository.findAll().size();
    int beforeRightToWorkSize = rightToWorkRepository.findAll().size();

    PersonDTO result = personService.create(this.personDTO);

    Assert.assertNotNull(result.getId());
    Assert.assertEquals(result.getId(), result.getGdcDetails().getId());
    Assert.assertEquals(result.getId(), result.getGmcDetails().getId());
    Assert.assertEquals(result.getId(), result.getContactDetails().getId());
    Assert.assertEquals(result.getId(), result.getPersonalDetails().getId());
    Assert.assertEquals(result.getId(), result.getRightToWork().getId());

    Assert.assertEquals(beforeGdcSize + 1, gdcDetailsRepository.findAll().size());
    Assert.assertEquals(beforeGmcSize + 1, gmcDetailsRepository.findAll().size());
    Assert.assertEquals(beforeContactSize + 1, contactDetailsRepository.findAll().size());
    Assert.assertEquals(beforePersonalDetailsSize + 1, personalDetailsRepository.findAll().size());
    Assert.assertEquals(beforeRightToWorkSize + 1, rightToWorkRepository.findAll().size());
  }


  @Ignore
  @Test(expected = Exception.class)
  @Transactional()
  public void createShouldRollbackWhenCreationOfRelatedEntitiesFail() {
    RightToWorkRepository rightToWorkRepositoryMock = Mockito.mock(RightToWorkRepository.class);
    personService.setRightToWorkRepository(rightToWorkRepositoryMock);

    when(rightToWorkRepository.save(any(RightToWork.class))).thenThrow(new RuntimeException("Random exception because of id"));

    int beforeGdcSize = gdcDetailsRepository.findAll().size();
    int beforeGmcSize = gmcDetailsRepository.findAll().size();
    int beforeContactSize = contactDetailsRepository.findAll().size();
    int beforePersonalDetailsSize = personalDetailsRepository.findAll().size();
    int beforeRightToWorkSize = rightToWorkRepository.findAll().size();

    PersonDTO result = null;
    try {
      result = personService.create(this.personDTO);
    } catch (Exception e) {
      Assert.assertNull(result);
      Assert.assertEquals(beforeGdcSize, gdcDetailsRepository.findAll().size());
      Assert.assertEquals(beforeGmcSize, gmcDetailsRepository.findAll().size());
      Assert.assertEquals(beforeContactSize, contactDetailsRepository.findAll().size());
      Assert.assertEquals(beforePersonalDetailsSize, personalDetailsRepository.findAll().size());
      Assert.assertEquals(beforeRightToWorkSize, rightToWorkRepository.findAll().size());
      throw e;
    }
  }

}
package com.transformuk.hee.tis.tcs.service.api.validation

import com.transformuk.hee.tis.reference.client.ReferenceService
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository
import com.transformuk.hee.tis.tcs.service.repository.RotationRepository
import com.transformuk.hee.tis.tcs.service.service.RotationService
import com.transformuk.hee.tis.tcs.service.service.impl.RotationServiceImpl
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationMapper
import org.springframework.web.bind.MethodArgumentNotValidException
import spock.lang.Specification

class ProgrammeMembershipValidatorSpec extends Specification {
    def "should reject rotation if it does not exist" () {
        given:

            PersonRepository personRepository = Mock()
            personRepository.exists(_) >> true
            RotationService rotationService = Mock()
            rotationService.rotationExists(_) >> false

            ProgrammeMembershipValidator pmv = new ProgrammeMembershipValidator(
                    personRepository, Stub(ProgrammeRepository), Stub(CurriculumRepository), Stub(ReferenceService), rotationService)

            PersonDTO person = new PersonDTO(id: 1L)
            def dto = new ProgrammeMembershipDTO(person: person, rotation: "rotation")
            dto.setCurriculumMemberships(new ArrayList<>())


        when:
            pmv.validate(dto)

        then:
            def x = thrown MethodArgumentNotValidException
            x.bindingResult.hasFieldErrors("rotation")
    }

    def "should accept rotation if it exists" () {
        given:
            PersonRepository personRepository = Mock()
            personRepository.exists(_) >> true
            RotationService rotationService = Mock()
            rotationService.rotationExists(_, 1) >> true
            ProgrammeRepository programmeRepository = Mock()
            programmeRepository.exists(_) >> true

            ProgrammeMembershipValidator pmv = new ProgrammeMembershipValidator(
                    personRepository, programmeRepository, Stub(CurriculumRepository), Stub(ReferenceService), rotationService)

            PersonDTO person = new PersonDTO(id: 1L)
            def dto = new ProgrammeMembershipDTO(person: person, rotation: "rotation", programmeId: 1, curriculumMemberships: new ArrayList<>())

        when:
            pmv.validate(dto)

        then:
            notThrown MethodArgumentNotValidException
    }

    def "should check programme associated with rotation when checking if it exists" () {
        given:
            PersonRepository personRepository = Mock()
            personRepository.exists(_) >> true

            RotationRepository rotationRepository = Mock()
            rotationRepository.findByNameAndProgrammeId(_, _) >> Optional.of(Boolean.TRUE)
            ProgrammeRepository programmeRepository = Mock()
            programmeRepository.exists(_) >> true

            RotationService rotationService = new RotationServiceImpl(rotationRepository, Stub(RotationMapper), programmeRepository)

            ProgrammeMembershipValidator pmv = new ProgrammeMembershipValidator(
                personRepository, programmeRepository, Stub(CurriculumRepository), Stub(ReferenceService), rotationService)

            PersonDTO person = new PersonDTO(id: 1L)
            def dto = new ProgrammeMembershipDTO(person: person, rotation: "rotation", programmeId: 1, curriculumMemberships: new ArrayList<>())


        when:
            pmv.validate(dto)

        then:
            notThrown MethodArgumentNotValidException
    }


    def "should check if rotation exists if name is empty" () {
        given:
            PersonRepository personRepository = Mock()
            personRepository.exists(_) >> true

            RotationService rotationService = Mock()

            ProgrammeMembershipValidator pmv = new ProgrammeMembershipValidator(
                personRepository, Stub(ProgrammeRepository), Stub(CurriculumRepository), Stub(ReferenceService), rotationService)

            PersonDTO person = new PersonDTO(id: 1L)
            def dto = new ProgrammeMembershipDTO(person: person, rotation: name)
            dto.setCurriculumMemberships(new ArrayList<>())

        when:
            pmv.validate(dto)

        then:
            notThrown MethodArgumentNotValidException
            0 * rotationService.rotationExists(_)

        where:
            name | _
            null | _
            ""   | _
    }
}

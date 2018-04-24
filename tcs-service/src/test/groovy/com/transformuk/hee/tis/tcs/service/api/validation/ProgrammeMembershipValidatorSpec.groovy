package com.transformuk.hee.tis.tcs.service.api.validation

import com.transformuk.hee.tis.reference.client.ReferenceService
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository
import com.transformuk.hee.tis.tcs.service.service.RotationService
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
            rotationService.rotationExists(_) >> true

            ProgrammeMembershipValidator pmv = new ProgrammeMembershipValidator(
                    personRepository, Stub(ProgrammeRepository), Stub(CurriculumRepository), Stub(ReferenceService), rotationService)

            PersonDTO person = new PersonDTO(id: 1L)
            def dto = new ProgrammeMembershipDTO(person: person, rotation: "rotation")

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

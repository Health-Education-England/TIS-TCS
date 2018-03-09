package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.tcs.service.model.PersonBasicDetails;
import com.transformuk.hee.tis.tcs.service.repository.PersonBasicDetailsRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class PersonBasicDetailsRepositoryAccessor {
    private PersonBasicDetailsRepository personBasicDetailsRepository;

    @Autowired
    public PersonBasicDetailsRepositoryAccessor(PersonBasicDetailsRepository personBasicDetailsRepository) {
        this.personBasicDetailsRepository = personBasicDetailsRepository;
    }

    public void doWithPersonBasicDetails(Long personId, Consumer<PersonBasicDetails> consumer) {
        if (personId != null && personId != 0) {
            PersonBasicDetails bdt = personBasicDetailsRepository.findOne(personId);
            if (bdt != null) {
                consumer.accept(bdt);
            }
        }
    }

    public void doWithPersonBasicDetailsSet(Set<Long> personIds, Consumer<Map<Long, PersonBasicDetails>> consumer) {
        if (CollectionUtils.isNotEmpty(personIds)) {
            List<PersonBasicDetails> details = personBasicDetailsRepository.findAll(personIds);
            if (CollectionUtils.isNotEmpty(details)) {
                Map<Long, PersonBasicDetails> detailsMap = details.stream().collect(Collectors.toMap(PersonBasicDetails::getId, d -> d));
                consumer.accept(detailsMap);
            }
        }
    }
}
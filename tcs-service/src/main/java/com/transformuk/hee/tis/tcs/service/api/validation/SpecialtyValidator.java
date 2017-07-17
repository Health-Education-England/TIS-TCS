package com.transformuk.hee.tis.tcs.service.api.validation;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyGroupRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecialtyValidator {

	private final SpecialtyGroupRepository specialtyGroupRepository;

	@Autowired
	public SpecialtyValidator(SpecialtyGroupRepository specialtyGroupRepository) {
		this.specialtyGroupRepository = specialtyGroupRepository;
	}

	public void validate(SpecialtyDTO specialtyDTO) throws MethodArgumentNotValidException {
		List<FieldError> fieldErrors = new ArrayList<>();
		fieldErrors.addAll(checkSpecialtyGroup(specialtyDTO.getSpecialtyGroup()));
		fieldErrors.addAll(checkNonEmptyField(specialtyDTO));

		if (!fieldErrors.isEmpty()) {
			BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(specialtyDTO, "SpecialtyDTO");
			fieldErrors.forEach(bindingResult::addError);
			throw new MethodArgumentNotValidException(null, bindingResult);
		}
	}

	private List<FieldError> checkSpecialtyGroup(SpecialtyGroupDTO specialtyGroup) {
		List<FieldError> fieldErrors = Lists.newArrayList();
		if (specialtyGroup.getId() == null || specialtyGroup.getId() < 0) {
			fieldErrors.add(new FieldError("SpecialtyDTO", "specialtyGroup",
					String.format("SpecialtyGroup with id %d cannot be null or negative", specialtyGroup.getId())));
		} else if (!specialtyGroupRepository.exists(specialtyGroup.getId())) {
			fieldErrors.add(new FieldError("SpecialtyDTO", "specialtyGroup",
					String.format("SpecialtyGroup with id %d does not exist", specialtyGroup.getId())));
		}
		return fieldErrors;
	}

	//The @NotNull annotations don't check for empty strings
	private List<FieldError> checkNonEmptyField(SpecialtyDTO specialtyDTO) {
		List<FieldError> fieldErrors = Lists.newArrayList();
		if (StringUtils.isBlank(specialtyDTO.getNhsSpecialtyCode())) {
			fieldErrors.add(new FieldError("SpecialtyDTO", "nhsSpecialtyCode",
					String.format("SpecialtyGroup with nhsSpecialtyCode [%s] cannot be null or empty", specialtyDTO.getNhsSpecialtyCode())));
		}
		if (StringUtils.isBlank(specialtyDTO.getName())) {
			fieldErrors.add(new FieldError("SpecialtyDTO", "name",
					String.format("SpecialtyGroup with name [%s] cannot be null or empty", specialtyDTO.getName())));
		}
		return fieldErrors;
	}
}

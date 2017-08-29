package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.TrainingNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
/**
 * Holds more complex custom validation for a {@link TrainingNumberDTO} that
 * cannot be easily done via annotations
 */
@Component
public class TrainingNumberValidator {

    private TrainingNumberRepository trainingNumberRepository;

    @Autowired
    public TrainingNumberValidator(TrainingNumberRepository trainingNumberRepository) {
        this.trainingNumberRepository = trainingNumberRepository;
    }

    /**
     * Custom validation on the trainingNumber DTO, this is meant to supplement the annotation based validation
     * already in place. It checks if the number field in trainingNumber is unique.
     *
     * @param trainingNumberDTO the number to check
     * @throws MethodArgumentNotValidException if there are validation errors
     */
    public void validate(TrainingNumberDTO trainingNumberDTO) throws MethodArgumentNotValidException {

        List<FieldError> fieldErrors = new ArrayList<>();;
        fieldErrors.addAll(checkNumber(trainingNumberDTO));

        if (!fieldErrors.isEmpty()) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(trainingNumberDTO, "TrainingNumberDTO");
            fieldErrors.forEach(bindingResult::addError);
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
    }

    private List<FieldError> checkNumber(TrainingNumberDTO trainingNumberDTO) {
        List<FieldError> fieldErrors = new ArrayList<>();
        //check if the  number is unique
        //if we update a number
        if (trainingNumberDTO.getId() != null) {
            List<TrainingNumber> trainingNumberList = trainingNumberRepository.findByNumber(trainingNumberDTO.getNumber());
            if (trainingNumberList.size() > 1) {
                fieldErrors.add(new FieldError("TrainingNumberDTO", "number",
                        String.format("Number %s is not unique, there are currently %d training numbers with this number: %s",
                                trainingNumberDTO.getNumber(), trainingNumberList.size(),
                                trainingNumberList)));
            } else if (trainingNumberList.size() == 1) {
                if (!trainingNumberDTO.getId().equals(trainingNumberList.get(0).getId())) {
                    fieldErrors.add(new FieldError("TrainingNumberDTO", "number",
                            String.format("Number %s is not unique, there is currently one training number with this number: %s",
                                    trainingNumberDTO.getNumber(), trainingNumberList.get(0))));
                }
            }
        } else {
            //if we create a number
            List<TrainingNumber> trainingNumberList = trainingNumberRepository.findByNumber(trainingNumberDTO.getNumber());
            if (!trainingNumberList.isEmpty()) {
                fieldErrors.add(new FieldError("TrainingNumberDTO", "number",
                        String.format("Number %s is not unique, there is currently one programme with this number: %s",
                                trainingNumberDTO.getNumber(), trainingNumberList.get(0))));
            }
        }
        return fieldErrors;
    }
}

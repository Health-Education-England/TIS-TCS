package com.transformuk.hee.tis.tcs.service.service.mapper;


import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.TrainingNumberType;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TrainingNumberMapperTest {

  @Test
  public void mapsTrainingNumberToDTOSuccessfully() {

    TrainingNumberMapper trainingNumberMapper = new TrainingNumberMapper();
    TrainingNumber trainingNumber = aValidTrainingNumber();

    TrainingNumberDTO trainingNumberDTO = trainingNumberMapper.trainingNumberToTrainingNumberDTO(trainingNumber);

    assertThat(trainingNumberDTO.getId()).isEqualTo(trainingNumber.getId());
    assertThat(trainingNumberDTO.getNumber()).isEqualTo(10);
    assertThat(trainingNumberDTO.getTrainingNumberType().toString()).isEqualTo(trainingNumber.getTrainingNumberType().toString());
    assertThat(trainingNumberDTO.getTrainingNumber()).isEqualTo(trainingNumber.getTrainingNumber());
    assertThat(trainingNumberDTO.getProgramme()).isEqualTo(100);

  }

  @Test
  public void mapsDTOToTrainingNumberSuccessfully() {
    TrainingNumberMapper trainingNumberMapper = new TrainingNumberMapper();
    TrainingNumberDTO trainingNumberDTO = aValidTrainingNumberDTO();

    TrainingNumber trainingNumber = trainingNumberMapper.trainingNumberDTOToTrainingNumber(trainingNumberDTO);

    assertThat(trainingNumber.getId()).isEqualTo(trainingNumberDTO.getId());
    assertThat(trainingNumber.getTrainingNumberType()).isEqualTo(trainingNumberDTO.getTrainingNumberType());
    assertThat(trainingNumber.getTrainingNumber()).isEqualTo(trainingNumberDTO.getTrainingNumber());
    assertThat(trainingNumber.getProgramme().getId()).isEqualTo(trainingNumberDTO.getProgramme());
    assertThat(trainingNumber.getNumber()).isEqualTo(trainingNumberDTO.getNumber());

  }

  private TrainingNumberDTO aValidTrainingNumberDTO() {

    TrainingNumberDTO trainingNumberDTO = new TrainingNumberDTO();
    trainingNumberDTO.setId(1L);
    trainingNumberDTO.setTrainingNumber("NWN/025/040/C");
    trainingNumberDTO.setNumber(100);
    trainingNumberDTO.setProgramme(10L);
    trainingNumberDTO.setTrainingNumberType(TrainingNumberType.NTN);
    return trainingNumberDTO;
  }

  private TrainingNumber aValidTrainingNumber() {
    TrainingNumber trainingNumber = new TrainingNumber();
    trainingNumber.setId(1L);
    trainingNumber.setNumber(10);
    trainingNumber.setTrainingNumberType(TrainingNumberType.NTN);
    trainingNumber.setTrainingNumber("NWN/025/040/C");
    Programme programme = new Programme();
    programme.setId(100L);
    trainingNumber.setProgramme(programme);
    return trainingNumber;
  }
}
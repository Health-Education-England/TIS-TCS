package com.transformuk.hee.tis.tcs.service.service.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeCurriculum;
import java.util.Collections;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgrammeMapperTest {

  private ProgrammeMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ProgrammeMapperImpl();
  }

  @Test
  void shouldAddProgrammeOnCurriculumWhenHasCurriculum() {
    Programme target = new Programme();

    ProgrammeCurriculum curriculum1 = new ProgrammeCurriculum(null, null, "1");
    ProgrammeCurriculum curriculum2 = new ProgrammeCurriculum(null, null, "2");
    target.setCurricula(Sets.newHashSet(curriculum1, curriculum2));

    mapper.addProgrammeToCurricula(null, target);

    assertThat("Unexpected number of curricula.", target.getCurricula().size(), is(2));
    Iterator<ProgrammeCurriculum> curriculumIterator = target.getCurricula().iterator();

    ProgrammeCurriculum curriculum = curriculumIterator.next();
    assertThat("Unexpected curriculum programme.", curriculum.getProgramme(), is(target));

    curriculum = curriculumIterator.next();
    assertThat("Unexpected curriculum programme.", curriculum.getProgramme(), is(target));
  }

  @Test
  void shouldNotAddProgrammeOnCurriculumWhenEmptyCurriculum() {
    Programme target = new Programme();
    target.setCurricula(Collections.emptySet());

    mapper.addProgrammeToCurricula(null, target);

    assertThat("Unexpected number of curricula.", target.getCurricula().size(), is(0));
  }

  @Test
  void shouldNotAddProgrammeOnCurriculumWhenNullCurriculum() {
    Programme target = new Programme();
    target.setCurricula(null);

    mapper.addProgrammeToCurricula(null, target);

    assertThat("Unexpected curricula value.", target.getCurricula(), nullValue());
  }
}

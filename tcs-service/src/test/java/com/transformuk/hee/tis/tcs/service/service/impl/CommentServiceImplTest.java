package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.service.model.Comment;
import com.transformuk.hee.tis.tcs.service.repository.CommentRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementCommentMapper;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The unit tests for {@link CommentServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

  @Mock
  CommentRepository commentRepository;

  private CommentServiceImpl service;

  @BeforeEach
  void setUp() {
    PlacementCommentMapper mapper = Mappers.getMapper(PlacementCommentMapper.class);
    service = new CommentServiceImpl(mapper, commentRepository);
  }

  /**
   * Test that an exception is thrown when the DTO's ID is not found.
   */
  @Test
  void findByIdShouldThrowExceptionWhenIdIsNotFound() {
    // Given
    when(commentRepository.findById(1L)).thenReturn(Optional.empty());

    // Then
    String message = Assertions.assertThrows(IllegalArgumentException.class, () ->
        // When
        service.findById(1L)
    ).getMessage();

    assertThat(message, containsString("1"));
  }

  /**
   * Test that a PlacementCommentDto is returned when the ID is found.
   */
  @Test
  void findByIdShouldReturnDtoWhenIdIsFound() {
    // Given
    Comment comment = new Comment();
    comment.setId(1L);

    when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

    // When
    PlacementCommentDTO commentDto = service.findById(1L);

    // Then
    assertThat(commentDto.getId(), is(1L));
  }

  /**
   * Test that a PlacementCommentDto is returned when the placement ID is found.
   */
  @Test
  void findByPlacementIdShouldReturnDtoWhenPlacementIdIsFound() {
    // Given
    Comment comment = new Comment();
    comment.setId(1L);

    when(commentRepository.findFirstByPlacementIdOrderByAmendedDateDesc(1L))
        .thenReturn(Optional.of(comment));

    // When
    PlacementCommentDTO commentDto = service.findByPlacementId(1L);

    // Then
    assertThat(commentDto.getId(), is(1L));
  }

  /**
   * Test that an empty PlacementCommentDto is returned when the placement ID is not found.
   */
  @Test
  void findByPlacementIdShouldReturnEmptyDtoWhenPlacementIdIsNotFound() {
    // Given
    when(commentRepository.findFirstByPlacementIdOrderByAmendedDateDesc(1L))
        .thenReturn(Optional.empty());

    // When
    PlacementCommentDTO commentDto = service.findByPlacementId(1L);

    // Then
    assertThat(commentDto, is(new PlacementCommentDTO()));
  }

  /**
   * Test that a new entity is created when the DTO's ID is null.
   */
  @Test
  void saveShouldCreateNewEntityWhenDtoIdIsNull() {
    // Given
    PlacementCommentDTO originalCommentDto = new PlacementCommentDTO();

    Comment newComment = new Comment();
    newComment.setId(2L);

    when(commentRepository.saveAndFlush(argThat(c -> c.getId() == null))).thenReturn(newComment);

    // When
    PlacementCommentDTO savedCommentDto = service.save(originalCommentDto);

    // Then
    assertThat(savedCommentDto.getId(), is(2L));
  }

  /**
   * Test that the existing entity is overwritten when the DTO's ID is not null.
   */
  @Test
  void saveShouldOverwriteExistingEntityWhenDtoIdIsNotNull() {
    // Given
    PlacementCommentDTO originalCommentDto = new PlacementCommentDTO();
    originalCommentDto.setId(1L);

    Comment originalComment = new Comment();
    originalComment.setId(2L);

    Comment newComment = new Comment();
    newComment.setId(3L);

    when(commentRepository.findById(1L)).thenReturn(Optional.of(originalComment));
    when(commentRepository.saveAndFlush(argThat(c -> c.getId() == 2L))).thenReturn(newComment);

    // When
    PlacementCommentDTO savedCommentDto = service.save(originalCommentDto);

    // Then
    assertThat(savedCommentDto.getId(), is(3L));
  }

  /**
   * Test that an exception is thrown when the DTO's ID is not found.
   */
  @Test
  void saveShouldThrowExceptionWhenDtoIdIsNotFound() {
    // Given
    PlacementCommentDTO originalCommentDto = new PlacementCommentDTO();
    originalCommentDto.setId(1L);

    when(commentRepository.findById(1L)).thenReturn(Optional.empty());

    // Then
    String message = Assertions.assertThrows(IllegalArgumentException.class, () ->
        // When
        service.save(originalCommentDto)
    ).getMessage();

    assertThat(message, containsString("1"));
  }
}

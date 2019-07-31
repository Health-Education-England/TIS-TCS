package com.transformuk.hee.tis.tcs.service.api.util;

import java.util.List;
import org.springframework.data.domain.Pageable;

public class BasicPage<T> {

  private List<T> content;
  private Pageable pageable;
  private boolean hasNext;

  public BasicPage(List<T> content, Pageable pageable, boolean hasNext) {
    this.content = content;
    this.pageable = pageable;
    this.hasNext = hasNext;
  }

  public BasicPage(List<T> content, Pageable pageable) {
    this.content = content;
    this.pageable = pageable;
    this.hasNext = false;
  }

  public int getNumber() {
    return pageable == null ? 0 : pageable.getPageNumber();
  }

  public int getSize() {
    return pageable == null ? 0 : pageable.getPageSize();
  }

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }

  public Pageable getPageable() {
    return pageable;
  }

  public void setPageable(Pageable pageable) {
    this.pageable = pageable;
  }

  public boolean isHasNext() {
    return hasNext;
  }

  public void setHasNext(boolean hasNext) {
    this.hasNext = hasNext;
  }
}

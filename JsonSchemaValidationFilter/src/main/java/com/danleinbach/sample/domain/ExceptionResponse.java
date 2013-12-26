package com.danleinbach.sample.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created: 12/17/13
 *
 * @author Daniel
 */
public class ExceptionResponse {

  private String subCode;
  private String errorMessage;
  private String errorCausedBy;

  @JsonProperty
  public String getSubCode() {
    return subCode;
  }

  public void setSubCode(String subCode) {
    this.subCode = subCode;
  }

  @JsonProperty
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @JsonProperty
  public String getErrorCausedBy() {
    return errorCausedBy;
  }

  public void setErrorCausedBy(String errorCausedBy) {
    this.errorCausedBy = errorCausedBy;
  }
}

package com.danleinbach.sample.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.report.ProcessingMessage;
import com.github.fge.jsonschema.report.ProcessingReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 12/18/13
 *
 * @author Daniel
 */
public class ValidationResponse {

	private ProcessingReport headerReport;
	private ProcessingReport bodyReport;
	private ProcessingReport paramReport;

	public boolean isSuccess() {
		boolean headerSuccess = checkSuccess(headerReport);
		boolean bodySuccess = checkSuccess(bodyReport);
		boolean paramSuccess = checkSuccess(paramReport);

		return headerSuccess && bodySuccess && paramSuccess;
	}

	private boolean checkSuccess(ProcessingReport report) {
		return (report == null) || report.isSuccess();
	}

	public ProcessingReport getHeaderReport() {
		return headerReport;
	}

	public void setHeaderReport(ProcessingReport headerReport) {
		this.headerReport = headerReport;
	}

	public ProcessingReport getBodyReport() {
		return bodyReport;
	}

	public void setBodyReport(ProcessingReport bodyReport) {
		this.bodyReport = bodyReport;
	}

	public ProcessingReport getParamReport() {
		return paramReport;
	}

	public void setParamReport(ProcessingReport paramReport) {
		this.paramReport = paramReport;
	}

	@JsonProperty("headerReport")
	public List<JsonNode> getSerializeHeaderReport() {
		return getJsonNodeList(headerReport);
	}

	@JsonProperty("bodyReport")
	public List<JsonNode> getSerializeBodyReport() {
		return getJsonNodeList(bodyReport);
	}

	@JsonProperty("paramReport")
	public List<JsonNode> getSerializeParamReport() {
		return getJsonNodeList(paramReport);
	}

	private List<JsonNode> getJsonNodeList(ProcessingReport report) {
		List<JsonNode> list = new ArrayList<JsonNode>();
		if(report != null) {
			for(ProcessingMessage message : report) {
				list.add(message.asJson());
			}
		}
		return list;
	}
}

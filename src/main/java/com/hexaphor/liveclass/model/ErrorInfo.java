package com.hexaphor.liveclass.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo {

	private String time;
	private String status;
	private int code;
	private String message;
	private String module;
}

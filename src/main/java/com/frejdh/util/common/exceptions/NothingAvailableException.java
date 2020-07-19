package com.frejdh.util.common.exceptions;

public class NothingAvailableException extends RuntimeException {
	public NothingAvailableException(String msg, Throwable e) {
		super(msg, e);
	}

	public NothingAvailableException(String msg) {
		super(msg);
	}

	public NothingAvailableException() {
		super();
	}
}

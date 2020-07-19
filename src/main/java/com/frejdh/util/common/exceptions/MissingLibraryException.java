package com.frejdh.util.common.exceptions;

public class MissingLibraryException extends Exception {
	public MissingLibraryException(String msg, Throwable e) {
		super(msg, e);
	}

	public MissingLibraryException(String msg) {
		super(msg);
	}

	public MissingLibraryException() {
		super();
	}
}

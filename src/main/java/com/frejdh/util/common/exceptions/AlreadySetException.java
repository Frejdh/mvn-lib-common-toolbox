package com.frejdh.util.common.exceptions;

/**
 * Exception, thrown when a value is already set.
 * Can be used to avoid concurrency problems.
 *
 * @author Kevin Frejdh
 */
public class AlreadySetException extends RuntimeException {
	public AlreadySetException(String msg, Throwable e) {
		super(msg, e);
	}

	public AlreadySetException(String msg) {
		super(msg);
	}

	public AlreadySetException() {
		super();
	}
}

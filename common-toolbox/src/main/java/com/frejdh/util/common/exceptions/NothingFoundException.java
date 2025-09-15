package com.frejdh.util.common.exceptions;

/**
 * Exception, thrown when there is no result available to fetch.
 * Intended to be used similarly to the HTTP 404 (not found) error.
 *
 * @author Kevin Frejdh
 */
public class NothingFoundException extends RuntimeException {
	public NothingFoundException(String msg, Throwable e) {
		super(msg, e);
	}

	public NothingFoundException(String msg) {
		super(msg);
	}

	public NothingFoundException() {
		super();
	}
}

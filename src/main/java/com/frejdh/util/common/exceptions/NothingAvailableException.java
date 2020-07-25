package com.frejdh.util.common.exceptions;

/**
 * Exception, thrown when there is no result available to fetch.
 * Intended to be used similarly to the HTTP 404 (not found) error.
 *
 * @author Kevin Frejdh
 */
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

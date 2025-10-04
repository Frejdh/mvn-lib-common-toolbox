package com.frejdh.util.common.exceptions;

/**
 * Exception, thrown when there is no result available to fetch.
 * Intended to be used similarly to the HTTP 404 (not found) error.
 *
 * @author Kevin Frejdh
 */
public class NothingFoundException extends RuntimeException {

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public NothingFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public NothingFoundException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public NothingFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public NothingFoundException() {
		super();
	}

}

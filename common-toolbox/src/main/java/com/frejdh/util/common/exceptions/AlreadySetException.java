package com.frejdh.util.common.exceptions;

/**
 * Exception, thrown when a value is already set.
 * Can be used to avoid concurrency problems.
 *
 * @author Kevin Frejdh
 */
public class AlreadySetException extends RuntimeException {

	/**
	 * @see RuntimeException#RuntimeException(String, Throwable)
	 */
	public AlreadySetException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @see RuntimeException#RuntimeException(String)
	 */
	public AlreadySetException(String message) {
		super(message);
	}

	/**
	 * @see RuntimeException#RuntimeException(Throwable)
	 */
	public AlreadySetException(Throwable cause) {
		super(cause);
	}

	/**
	 * @see RuntimeException#RuntimeException()
	 */
	public AlreadySetException() {
		super();
	}

}

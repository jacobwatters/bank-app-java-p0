package com.revature.exceptions;

/**
 * Thrown if user does not have permission to perform the operation
 */
public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String msg){
        super(msg);
    }
}

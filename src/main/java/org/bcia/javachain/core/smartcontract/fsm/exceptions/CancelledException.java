/*
Copyright IBM Corp., DTCC All Rights Reserved.

SPDX-License-Identifier: Apache-2.0
*/

package org.bcia.javachain.core.smartcontract.fsm.exceptions;

public class CancelledException extends Exception {

	public final Exception error;

	public CancelledException() {
		this(null);
	}

	public CancelledException(Exception error) {
		super("The transition was cancelled" + error == null ?
				"" : " with error " + error.toString());
		this.error = error;
	}

}

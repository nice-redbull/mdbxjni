/**
 * Copyright (C) 2013, RedHat, Inc.
 *
 *    http://www.redhat.com/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castortech.mdbxjni;

/**
 * General exception thrown when error codes are reported by LMDB.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class MDBXException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("nls")
	public enum Status {
		OK(0, "OK"),
		EINVAL(JNI.MDBX_EINVAL, "Invalid Parameter"),
		EACCES(JNI.MDBX_EACCESS, "Access Denied"),
		ENODATA(JNI.MDBX_ENODATA, "Handle EOF"),
		ENOMEM(JNI.MDBX_ENOMEM, "Out of Memory"),
		EROFS(JNI.MDBX_EROFS, "File Read Only"),
		ENOSYS(JNI.MDBX_ENOSYS, "Not Supported"),
		EIO(JNI.MDBX_EIO, "Write Fault"),
		EPERM(JNI.MDBX_EPERM, "Invalid Function"),
		EINTR(JNI.MDBX_EINTR, "Cancelled"),
		KEYEXIST(JNI.MDBX_KEYEXIST, "key/data pair already exists"),
		NOTFOUND(JNI.MDBX_NOTFOUND, "key/data pair not found (EOF)"),
		PAGE_NOTFOUND(JNI.MDBX_PAGE_NOTFOUND, "Requested page not found - this usually indicates corruption"),
		CORRUPTED(JNI.MDBX_CORRUPTED, "Located page was wrong type"),
		PANIC(JNI.MDBX_PANIC, "Update of meta page failed or environment had fatal error"),
		VERSION_MISMATCH(JNI.MDBX_VERSION_MISMATCH, "DB file version mismatch with libmdbx"),
		INVALID(JNI.MDBX_INVALID, "File is not a valid MDBX file"),
		MAP_FULL(JNI.MDBX_MAP_FULL, "Environment mapsize reached"),
		DBS_FULL(JNI.MDBX_DBS_FULL, "Environment maxdbs reached"),
		READERS_FULL(JNI.MDBX_READERS_FULL, "Environment maxreaders reached"),
		TXN_FULL(JNI.MDBX_TXN_FULL, "Transaction has too many dirty pages"),
		CURSOR_FULL(JNI.MDBX_CURSOR_FULL, "Cursor stack too deep - internal error"),
		PAGE_FULL(JNI.MDBX_PAGE_FULL, "Page has not enough space - internal error"),
		MAP_RESIZED(JNI.MDBX_MAP_RESIZED, "Database contents grew beyond environment mapsize"),
		INCOMPATIBLE(JNI.MDBX_INCOMPATIBLE, "Operation and DB incompatible, or DB type changed."),
		BAD_RSLOT(JNI.MDBX_BAD_RSLOT, "Invalid reuse of reader locktable slot"),
		BAD_TXN(JNI.MDBX_BAD_TXN, "Transaction must abort, has a child, or is invalid"),
		BAD_VALSIZE(JNI.MDBX_BAD_VALSIZE, "Unsupported size of key/DB name/data, or wrong DUPFIXED size"),
		BAD_DBI(JNI.MDBX_BAD_DBI, "The specified DBI was changed unexpectedly"),
		PROBLEM(JNI.MDBX_PROBLEM, "Unexpected problem - Transaction should abort"),
		BUSY(JNI.MDBX_BUSY, "Another write transaction is running"),
		EMULTIVAL(JNI.MDBX_EMULTIVAL, "The mdbx_put() or mdbx_replace() was called for key, that has more that one associated value."),
		BAD_SIGNATGURE(JNI.MDBX_EBADSIGN, "Bad signature of a runtime object(s), this can mean: - memory corruption or double-free; - ABI version mismatch (rare case)"),
		WANNA_RECOVERY(JNI.MDBX_WANNA_RECOVERY, "Database should be recovered, but this could NOT be done automatically right now (e.g. in readonly mode and so forth)."),
		KE_YMISMATCH(JNI.MDBX_EKEYMISMATCH, "The given key value is mismatched to the current cursor position, when mdbx_cursor_put() called with MDBX_CURRENT option."),
		TOO_LARGE(JNI.MDBX_TOO_LARGE, "Database is too large for current system, e.g. could NOT be mapped into RAM."),
		THREAD_MISMATCH(JNI.MDBX_THREAD_MISMATCH, "A thread has attempted to use a not owned object, e.g. a transaction that started by another thread."),
		;

		private final int code;
		private final String reason;

		Status(final int statusCode, final String reasonPhrase) {
			this.code = statusCode;
			this.reason = reasonPhrase;
		}

		/**
		 * Get the associated status code
		 * 
		 * @return the status code
		 */
		public int getStatusCode() {
			return code;
		}

		/**
		 * Get the reason phrase
		 * 
		 * @return the reason phrase
		 */
		public String getReasonPhrase() {
			return toString();
		}

		/**
		 * Get the reason phrase
		 * 
		 * @return the reason phrase
		 */
		@Override
		public String toString() {
			return reason;
		}

		/**
		 * Convert a numerical status code into the corresponding Status
		 * 
		 * @param statusCode
		 *          the numerical status code
		 * @return the matching Status or null is no matching Status is defined
		 */
		public static Status fromStatusCode(final int statusCode) {
			for (Status s : Status.values()) {
				if (s.code == statusCode) {
					return s;
				}
			}
			return null;
		}
	}

	int errorCode;

	public MDBXException() {
	}

	public MDBXException(String message) {
		super(message);
	}

	public MDBXException(String message, int errorCode) {
		super(message + ",rc:" + (Status.fromStatusCode(errorCode) != null ? 
				Status.fromStatusCode(errorCode).name() : errorCode));
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		Status status = Status.fromStatusCode(getErrorCode());
		return getMessage() + ",rc:" + (status != null ? status.name() : getErrorCode());
	}
}
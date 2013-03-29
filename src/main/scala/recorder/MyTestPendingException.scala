package recorder

class CustomTestPendingException extends Exception(new NotImplementedError)

class MyException(
  val message: String,
  val context: Option[String],
  val cause: Throwable,
  val fileNameAndLineNumber: Option[String])
extends Exception(message, cause) {}

class MyTestPendingException(
  message: String,
  context: Option[String],
  cause: Throwable,
  fileNameAndLineNumber: Option[String])
extends MyException(message, context, cause, fileNameAndLineNumber) {}

class MyNotImplException(
  message: String,
  context: Option[String],
  cause: Throwable,
  fileNameAndLineNumber: Option[String])
extends MyException(message, context, cause, fileNameAndLineNumber) {}

class MyTestFailedException(
  message: String,
  context: Option[String],
  cause: Throwable,
  fileNameAndLineNumber: Option[String])
extends MyException(message, context, cause, fileNameAndLineNumber) {}


package com.transformuk.hee.tis.tcs.service.exception;

/**
 * Custom, parameterized exception, which can be translated on the client side. For example:
 * <p>
 * <pre>
 * throw new CustomParameterizedException(&quot;myCustomError&quot;, &quot;hello&quot;, &quot;world&quot;);
 * </pre>
 * <p>
 * Can be translated with:
 * <p>
 * <pre>
 * "error.myCustomError" :  "The server says {{params[0]}} to {{params[1]}}"
 * </pre>
 */
public class CustomParameterizedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String message;
  private final String[] params;

  public CustomParameterizedException(String message, String... params) {
    super(message);
    this.message = message;
    this.params = params;
  }

  public ParameterizedErrorVM getErrorVM() {
    return new ParameterizedErrorVM(message, params);
  }

}

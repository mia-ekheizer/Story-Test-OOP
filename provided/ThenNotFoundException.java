package provided;

/**
 * You should throw this exception when there isn't any method
 * annotated by the Then annotation (and has the same sentence),
 * which you can invoke.
 */
public class ThenNotFoundException extends WordNotFoundException {
	private static final long serialVersionUID = 6238508041463898707L;
}

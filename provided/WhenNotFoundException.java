package provided;

/**
 * You should throw this exception when there isn't any method
 * annotated by the When annotation (and has the same sentence),
 * which you can invoke.
 */
public class WhenNotFoundException extends WordNotFoundException {
	private static final long serialVersionUID = -6414207426858456606L;
}

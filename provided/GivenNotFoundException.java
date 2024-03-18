package provided;

/**
 * You should throw this exception when there isn't any method
 * annotated by the Given annotation (and has the same sentence),
 * which you can invoke.
 */
public class GivenNotFoundException extends WordNotFoundException {
	private static final long serialVersionUID = 8972097939354941818L;
}

package os.util.parser;

public interface Parser<T extends Object, S extends Object> {

	public T parse(S source);

	public S compose(T source);
}

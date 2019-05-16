package os.util.parser;


public interface Parser<T extends Object, S extends Object> {

	public void parse(T target, S source);
	
	public void compose(S target, T source);
}

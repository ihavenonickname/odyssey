package odyssey.generator.interpreter;

public interface Converter<T> {
	public T convert(String input);
}
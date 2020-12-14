// Generated from /data/Studie/Research project/sanitizer-specifications/src/main/java/io/danielhuisman/sanitizers/grammar/Regex.g4 by ANTLR 4.9
package io.danielhuisman.sanitizers.grammar;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link RegexVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class RegexBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements RegexVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitRange(RegexParser.RangeContext ctx) { return visitChildren(ctx); }
}
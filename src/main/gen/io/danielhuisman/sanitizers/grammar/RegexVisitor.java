// Generated from /data/Studie/Research project/sanitizer-specifications/src/main/java/io/danielhuisman/sanitizers/grammar/Regex.g4 by ANTLR 4.9
package io.danielhuisman.sanitizers.grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RegexParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RegexVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RegexParser#range}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRange(RegexParser.RangeContext ctx);
}
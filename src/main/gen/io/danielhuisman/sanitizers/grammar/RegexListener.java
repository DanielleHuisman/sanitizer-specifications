// Generated from /data/Studie/Research project/sanitizer-specifications/src/main/java/io/danielhuisman/sanitizers/grammar/Regex.g4 by ANTLR 4.9
package io.danielhuisman.sanitizers.grammar;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RegexParser}.
 */
public interface RegexListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RegexParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(RegexParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegexParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(RegexParser.RangeContext ctx);
}
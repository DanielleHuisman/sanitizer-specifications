# Language

This project includes a simple domain-specific language (DSL) for generating and testing sanitizer specifications.

## Identifiers
Identifiers are used to name variables and generators.
An identifier consists of alphanumeric characters, dashes or underscores, but cannot start or end with a dash or underscore.
For example: `var1`, `ABC`, `test_name`, `cheese-factory`.

## Data types
### Boolean
Either `true` of `false`.

### Integer
Any positive whole number, for example: `0` or `123`.

### Character
A single unicode character. A character is surrounded by single quotes. A backslash can be used to escape characters. For example:
```
'a'
'6'
'\''
'\\'
' '
```

### String
A sequence of zero of more unicode characters. A string is surrounded by double quotes. A backslash can be used to escape charactesr. For example:
```
""
"abc"
"\"quoted\""
"\\\"escaped\\\""
```
### Tuple
A finite ordered list with elements of different or equal data types. This language supports 2-tuples (pairs) and 3-tuples (triples).
A tuple can be defined by placing parentheses around multiple comma separated elements. For example:
```
(4, 7)
(3, 18, 9)
(false, false)
('q', 3, "cheese")
```

### List
An ordered list of zero or more elements of equal data types. A list can be defined by placing brackets around multiple comma separated elements. For example:
```
[]
[24]
[false, true, false]
['a', 's', 'd', 'f']
["bread", "cheese", "ham"]
```

### Regex
A regular expression. This language supports a subset of common regular expression features.

Supported features:
- Character classes
    - Character set (`[abc]`)
    - Negated set (`[^abc]`)
    - Range (`[a-z]`)
    - Dot (`.`)
    - Word, not word (`\w` and `\W`)
    - Digit, not digit (`\d` and `\D`)
    - Whitespace, not whitespace (`\s` and `\S`)
- Groups and references
    - Capturing group `(abc)`
- Quantifiers and alternation
    - Plus (`+`)
    - Star (`*`)
    - Optional (`?`)
    - Quantifier (`{1}`, `{1,}`, `{1,3}`)
    - Alternation (`|`)

Unsupported features:
- Character classes
    - Unicode category, not unicode category (`\p{L}` and `\P{L}`)
    - Unicode script, not unicode script (`\p{Han}` and `\P{Han}`)
- Anchors
    - Beginning, end (`^` and `$`)
    - Word boundary, not word boundary (`\b` and `\B`)
- Groups and references
    - Named group (`(?<name>abc)`)
    - Numeric reference (`\1`)
    - Non-capturing group `(?:abc)`
- Lookaround
    - Positive and negative lookahead (`(?=abc)` and `(?!abc)`)
    - Positive and negative lookbehind (`(?<abc)` and `(?<!abc)`)
- Quantifiers and alternation
    - Lazy (`a+?`)
- Substitution
    - Match (`$&`)
    - Capture group (`$1`)
    - Before match, after match (`` $` `` and `$'`)
    - Escaped $ (`$$`)
- Flags
    - Ignore case (`i`)
    - Global search (`g`)
    - Multiline (`m`)
    - Unicode (`u`)
    - Sticky (`y`)

A regex can be defined surrounded by `r/` and `/r`. For example:
```
r/[a-z0-9_]+/r
r/apple|pear/r
r/\s*(a\d?|q{4,5})e{2,}\s*/r
```

### Automaton
An automaton is either a Symbolic Finite Automaton (SFA) or a Symbolic Finite Transducer (SFT).
An automaton can not be defined directly, instead it is created by calling a generator, see generator expressions for more information.

## Expressions
An expression consists of one or more primitive values, variables or operators and always produces a primitive value. 

### Variable
Returns the value of a previously defined variable.

**Syntax:** `<identifier>`

**Examples:**
```
var1
ABC
test_name
cheese-factory
```

### Generator
Uses a generator to generate an automaton from the given input. The input is a one or more primitive values, but most generators only take one input.
A list of generators can be found [here](generators.md).

**Syntax:** `<identifier> <primitve>+`

**Examples:**
```
length (">", 3)
regex r/[a-z]+0?/r
word-list [("not_contains", "forbidden"), ("not_contains", "dangerous")]
```

### Operator
Operator expressions are used to modify the result of one or more other expressions.

**Syntax, parentheses:** `(<expression>)`

**Syntax, one operand:** `<operator> <expression>`

**Syntax, two operands:** `<expression> <operator> <expression>`

## Operators
Operators only work on the automaton data type. Not all operators work on both SFAs and SFTs. The behaviour of an operator also differs between SFAs and SFTs.

### Not operator
**Symbol:** `not`

**Operands:** 1

**For SFAs:** Returns the complement of the SFA, i.e. all previously accepted output is now rejected and vice versa.

**For SFTs:** Not supported.

**Examples:**
```
not var1
not length ("=", 3)
```

### And operator
**Symbol:** `and`

**Operands:** 2

**For SFAs:** Returns the intersection of two SFAs, i.e. all input accepted by both SFAs is accepted and all other input is rejected.

**For SFTs:** Returns the composition of two SFTs, i.e. all operations applied to the input by both SFAs is also sequentially applied to the combined input.

**Examples:**
```
var1 and var2
length (">", 3) and length ("<", 5)
trim 3 and replace-char [('a', "e")] 
```

### Or operator
**Symbol:** `plus`

**Operands:** 2

**For SFAs:** Returns the union of two SFAs, i.e. all input accepted by one or both SFAs is accepted and all other input is rejected.

**For SFTs:** Not supported.

**Examples:**
```
var1 or var2
length ("=", 3) or length (">", 5)
```

### Plus operator
**Symbol:** `plus`

**Operands:** 2

**For SFAs:** Returns the concatenation of two SFAs, i.e. all input that is sequentially accepted by the SFAs is accepted and all other input is rejected.

**For SFTs:** Not supported.

**Examples:**
```
var1 plus var2
word ("equals", "ap") plus word ("equals", "ple)
```

## Statements
A statement represents an action that should be carried out. Unlike an expression, a statement has no return value.
Therefore, a statement can’t be used in place of an expression. A program consists of zero or more statements. These statements are executed sequentially.

### Assignment
Assign the value of an expression to a variable. In this language, an expression always returns an automaton, so variables are only of the automaton data type.

**Syntax:** `<identifier> = <expression>`

**Examples:**
```
gt3 = length (">", 3) 
lte10 = length ("<=", 10)
combined = gt3 and lte10
negative = not combined 
```

### Import
Import an automaton from a DOT file to a variable. DOT is a graph description language.

**Syntax:** `import <identifier>`

**Examples:**
```
import var1
import abc_to_def
```

### Export
Export an automaton from a variable to a DOT file. DOT is a graph description language.

**Syntax:** `export <identifier>`

**Examples:**
```
export var1
export abc_to_def
```

### Assert
Check whether an automaton accepts or rejects certain input. The input is a string. This works for both SFAs and SFTs.

**Syntax:** `accepts|rejects <identifier> <input>`

**Examples:**
```
accepts length3 "abc"
rejects length3 "abcde"
```

### Test
Check whether an automaton produces a certain output for an input.The input and output are both strings. This only works for SFTs.

**Syntax:** `outputs <identifier> <input> <output>`

**Examples:**
```
outputs abc_to_def "abc" "def"
outputs no_script "te<script>st" "test"
```

## References
- [Language grammar](../src/main/antlr/io/danielhuisman/sanitizers/language/grammar/Language.g4)
- [Regex grammar](../src/main/antlr/io/danielhuisman/sanitizers/language/grammar/Regex.g4)

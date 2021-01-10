# Generators
Generators are used to automatically generate an automaton from a certain input.
Generators can be used in the [domain-specific language](language.md) and in Java itself.

This project can generate two types of automata:
- [Symbolic Finite Automata (SFAs)](#symbolic-finite-automata), which only accept or reject input
- [Symbolic Finite Transducers (SFTs)](#symbolic-finite-transducers), which also produce output for the input

More information about SFAs and SFTs, can be found [here](overview.md).

## Symbolic Finite Automata
### Trivial
- **Name:** `trivial`
- **Input type:** `boolean`
- **Java name:** [`GeneratorTrivial`](../src/main/java/io/danielhuisman/sanitizers/generators/sfa/GeneratorTrivial.java)
- **Java input type:** `Boolean`

TODO

### Length
TODO

### Length list
TODO

### Range
TODO

### Range list
TODO

### Word
TODO

### Word list
TODO

### Regex
TODO

## Symbolic Finite Transducers
### Constant
- **Name:** `constant`
- **Input type:** `string`
- **Java name:** [`GeneratorTrivial`](../src/main/java/io/danielhuisman/sanitizers/generators/sft/GeneratorConstant.java)
- **Java input type:** `String`

TODO

### Trim
TODO

### Pad
TODO

### Replace char
TODO

### Replace word
TODO

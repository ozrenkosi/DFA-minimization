DFA minimization
==========

Solution for the second lab at "Introduction to Theoretical Computer Science" course - Minimization of Deterministic final automata.

Example
------

Input file consists of rows in this specific order:

1. line - lexicographically sorted states separated with `,`
2. line - lexicographically sorted symbols of the alphabet separated with `,`
3. line - lexicographically sorted acceptable states separated with `,`
4. line - starting state
5. line and all other lines - transition function in format `currentState,alphabetSymbol->nextState`.

Input

    p1,p2,p3,p4,p5,p6,p7
    c,d
    p5,p6,p7
    p1
    p1,c->p6
    p1,d->p3
    p2,c->p7
    p2,d->p3
    p3,c->p1
    p3,d->p5
    p4,c->p4
    p4,d->p6
    p5,c->p7
    p5,d->p3
    p6,c->p4
    p6,d->p1

will give output

    p1,p3,p4,p5,p6
    c,d
    p5,p6
    p1
    p1,c->p6
    p1,d->p3
    p3,c->p1
    p3,d->p5
    p4,c->p4
    p4,d->p6
    p5,c->p6
    p5,d->p3
    p6,c->p4
    p6,d->p1

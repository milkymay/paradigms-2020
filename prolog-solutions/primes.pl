%delay

divides(N, D) :- divides_(N, D), !.

divides(N, D) :-
	D < N,
	0 is mod(N, D),
	assert (divides_ (N, D)), !.

divides(N, D) :-
	(D * D) =< N,
	D1 is D + 1,
	divides(N, D1).

prime(N) :- prime_(N), !.

prime(N) :-
	N > 1,
	not(divides(N, 2)),
	assert (prime_ (N)).

composite(N) :- composite_(N), !.

composite(N) :- not(prime(N)), assert(composite_(N)), !.

ordered([H1 | []]) :- !.

ordered([H1 | [H2 | T]]) :-
	H1 =< H2,
	ordered([H2 | T]).

listToNum([H1 | []], Cur, N) :-
	N is Cur * H1.

listToNum([H1 | [H2 | T]], Cur, N) :-
	prime(H1),
	Cur1 is Cur * H1,
	listToNum([H2 | T], Cur1, N).

prime_divisors(1, []) :- !.

prime_divisors(N, [N]) :- prime(N), !.

prime_divisors(N, Divisors) :-
	is_list(Divisors),
	ordered(Divisors),
	listToNum(Divisors, 1, N), !.

prime_divisors(N, Divisors) :-
	not(is_list(Divisors)),
	prime_divs(N, Divisors), !.

prime_divs(N, L) :- prime_divs_(N, L), !.

prime_divs(N, L) :- findall(D, prime_factor(N, D), L), assert(prime_divs_(N, L)), !.

prime_factor(N, D) :- find_prime_factor(N, 2, D).

find_prime_factor(N, D, D) :- 0 is N mod D.

find_prime_factor(N, D, R) :-
    D < N,
    (0 is N mod D ->
        (N1 is N/D, find_prime_factor(N1, D, R)) ;
        (D1 is D + 1, find_prime_factor(N, D1, R))).
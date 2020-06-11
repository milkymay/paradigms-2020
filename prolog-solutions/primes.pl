%review
% :NOTE: too many code
composite(1).

init(N) :- erat(2, N).

erat(A, N) :-
	A * A < N,
	B is A * A,
	not erat2(A, B, N),
	A1 is A + 1,
	erat1(A1, R),
	erat(R, N).

erat1(A, R) :-
	not composite(A), R is A, !;
	A1 is A + 1, erat1(A1, R).

erat2(A, B, N) :-
	B < N,
	assert(composite(B)),
	B1 is A + B,
	erat2(A, B1, N).

prime(N) :- not composite(N), !.

ordered([H1 | []]) :- !.

ordered([H1 | [H2 | T]]) :-
	H1 =< H2, 
	ordered([H2 | T]).

listToInt([H1 | []], Cur, N) :- N is Cur * H1.

listToInt([H1 | [H2 | T]], Cur, N) :-
	prime(H1),
	Cur1 is Cur * H1,
	listToInt([H2 | T], Cur1, N).
	
prime_divisors(1, []) :- !.
prime_divisors(N, [N]) :- prime(N), !.
	
prime_divisors(N, Divisors) :- 
	var(N), 
	ordered(Divisors),
	listToInt(Divisors, 1, N), !.

prime_divisors(N, [H | T]) :-
	number(N), 
	prime_div(N, H),
	R is N / H,
	prime_divisors(R, T),
	ordered([H | T]),	!.

prime_div(N, D) :- search(N, 2, D).

search(N, D, R) :-
    D * D =< N,
    (0 is N mod D -> 
    		(prime(D), R is D) ;  
    		(D1 is D + 1, search(N, D1, R))
    ).

next(K, R) :-
	prime(K), 
	R is K, !.

next(K, R) :-
	K1 is K + 1,
	next(K1, R).

nth_prime(1, 2).
nth_prime(N, P) :- count(3, 2, N, P).
	
count(Cur, Num, N, P) :- P is Cur, Num is N, !. 

count(Cur, Num, N, P) :-
	Num < N, 
	T is Cur + 1, 
	next(T, Cur1),
	Num1 is Num + 1,
	count(Cur1, Num1, N, P).

compare([], []) :- !.
compare([], [H]) :- fail, !.
compare([H], []) :- fail, !.
compare([H | T1], [H | T2]) :- compare(T1, T2).

trans(N, K, []) :- N = 0, !.

trans(N, K, [H | T]) :-
  H is mod(N, K),
  N1 is div(N, K), 
  trans(N1, K, T).
  
prime_palindrome(N, K) :-
  prime(N), 
  trans(N, K, Res),
  reverse(Res, RevRes),
  compare(Res, RevRes), !.
  

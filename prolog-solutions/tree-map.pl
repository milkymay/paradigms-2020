%delay

new_node(P, t(P, Y, nil, nil)) :- rand_int(1000, Y).

map_split(nil, K, nil, nil).
map_split(t((Key, Val), Y, L, R), K, SpL, SpR) :-
		(K > Key -> 
			map_split(R, K, T1, SpR), 
				SpL = t((Key, Val), Y, L, T1);
			map_split(L, K, SpL, T2), 
				SpR = t((Key, Val), Y, T2, R)).

map_merge(A, nil, A).
map_merge(nil, A, A) :- A \= nil.
map_merge(t((Key1, Val1), Y1, L1, R1), t((Key2, Val2), Y2, L2, R2), Res) :-
		(Y1 > Y2 -> 
			map_merge(R1, t((Key2, Val2), Y2, L2, R2), T), 
				Res = t((Key1, Val1), Y1, L1, T);
			map_merge(t((Key1, Val1), Y1, L1, R1), L2, T), 
				Res = t((Key2, Val2), Y2, T, R2)).

map_insert((X, Y), nil, R) :- new_node((X, Y), R).
map_insert((X, Y), t((Key, Val), TY, TL, TR), R) :-
		map_split(t((Key, Val), TY, TL, TR), X, LeftT, RightT),
		new_node((X, Y), Node),
		map_merge(LeftT, Node, Res),
		map_merge(Res, RightT, R).

map_remove(T, Key, R) :-
		map_split(T, Key, TL, TR),
		map_split(TR, Key + 1, _, Res),
		map_merge(TL, Res, R).

map_put(T, Key, Val, Res) :-
		map_remove(T, Key, R),
		map_insert((Key, Val), R, Res).

map_get(nil, not Key, _).
map_get(t((X, Val), _, _, _), X, Val).
map_get(t((Key, Val), Y, L, R), X, P) :- X \= Key,
		(X > Key ->
			map_get(R, X, P);
			map_get(L, X, P)).

build([], T, T).
build([(X, Y) | Tail], T, R) :-
    map_put(T, X, Y, New),
    build(Tail, New, R).

map_build(List, T) :- 
		build(List, nil, T).
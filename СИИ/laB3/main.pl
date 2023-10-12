
% Различные герои
hero(anti_mage).
hero(invoker).
hero(pudge).
hero(lina).
hero(juggernaut).
hero(sven).

% различные предметы
item(battlefury).
item(eul_scepter).
item(heart_of_tarrasque).
item(boots_of_travel).
item(yasha).
item(sange).
item(kaya).
item(mango).
item(beer).
item(wine).



% роли героев
role(anti_mage, carry).
role(invoker, nuker).
role(pudge, disabler).
role(lina, support).
role(juggernaut, carry).
role(sven, initiator).


% способности героев
has_ability(anti_mage, blink).
has_ability(anti_mage, mana_void).
has_ability(anti_mage, feed).
has_ability(anti_mage, dead).
has_ability(anti_mage, counter_spell).
has_ability(invoker, tornado).
has_ability(invoker, emp).
has_ability(invoker, wall).
has_ability(invoker, meteor).
has_ability(invoker, invoke).
has_ability(pudge, dismember).
has_ability(lina, laguna_blade).
has_ability(juggernaut, omnislash).
has_ability(sven, storm_bolt).
has_ability(sven, gds).


% Сильный героой - георй с 4+ абилками
powerful_hero(Hero) :-
    hero(Hero),
    findall(A, has_ability(Hero, A), Abilities),
    length(Abilities, Count),
    Count > 4.

%один и тот же герой
same_hero(Hero1, Hero2) :-
    Hero1 == Hero2.

% Сильное дуо - саппорт + керри
strong
_duo(Hero1, Hero2) :-
    (role(Hero1, carry),
    role(Hero2, support));
    (role(Hero2, carry),
    role(Hero1, support)).


weak_duo(Hero1, Hero2) :-
    role(Hero1, X),
    role(Hero2, Y),
    X==Y,
    Hero1 \= Hero2.


can_kill(Hero1, Hero2) :-
    hero(Hero1),
    hero(Hero2),
    findall(A1, has_ability(Hero1, A1), Abilities1),
    findall(A2, has_ability(Hero2, A2), Abilities2),
    length(Abilities1, Count1),
    length(Abilities2, Count2),
    Count1>Count2.



% hero(anti_mage). 
% item(yasha).     

% role(pudge, disabler), has_ability(pudge, dismember). 
% role(anti_mage, support), has_ability(anti_mage, blink). 
% powerful_hero(sven).                           

% role(Hero, carry). 
% strong_duo(Hero1, Hero2).

% powerful_hero(lina).  
% strong_duo(juggernaut, lina).

% week_duo(anti_mage, lina).

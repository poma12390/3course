import re
from pyswip import Prolog

prolog = Prolog()
prolog.consult("main.pl")

def query(str):
    return list(prolog.query(str))

def hero_explain(hero):
    results = query(f"has_ability({hero}, Ability)")

    if not results:
        print("I have no abilities")
    else:
        for i in range(len(results)):
            print(f"I have: ", end="")
            print(f"{results[i]['Ability']}")

    duos = ["strong_duo", "weak_duo"]

    for duo in duos:
        results = query(f"{duo}({hero}, Hero)")

        if not results:
            print(f"I haven't {duo}")
        else:
            for i in range(len(results)):
                print(f"My {duo}:", end=" ")
                print(f"{results[i]['Hero']}")

    results = query(f"can_kill({hero}, Hero)")
    if not results:
        print(f"I can kill nobody")
    else:
        for i in range(len(results)):
            print(f"I can kill: ", end="")
            print(f"{results[i]['Hero']}")
    results = query(f"powerful_hero({hero})")
    if not results:
        print(f"I am weak hero")
    else:
        print("I am strong hero")


while (True):
    user_input = input("Yes, how can I help u? : ")

    pattern = r"I am (\w+)"

    match_hero = re.match(pattern, user_input)

    if match_hero:
        results = query(f"hero({match_hero.group(1)})")
        if not results:
            print("No such hero")
        else:
            hero = match_hero.group(1)
            hero_explain(hero)



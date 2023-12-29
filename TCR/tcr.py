#! /usr/bin/env python

import json

def fit(x, y) -> float:
    return int(x/y*100)/100

def load() -> dict:
    with open("tcr.json", "r") as r:
        return json.load(r)

def main():
    for file, values in load().items():
        c, t = values["code"], values["test"]
        print(f"|{file:<35}|{c:>4}|{t:>4}|~{fit(t, c):<4}|")
if __name__ == "__main__":
    main()

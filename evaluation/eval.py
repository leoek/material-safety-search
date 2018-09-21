import json
import os
from statistics import mean
from pprint import pprint
from prettytable import PrettyTable

def evaltable(fuzzy=False, wholeDoc=False):
    avgprecs = []

    pathadaption = ''
    if fuzzy:
        pathadaption += 'f'
    if wholeDoc:
        pathadaption += 'w'

    t = PrettyTable(['querystring', 'average precision'])

    for subdir, dirs, files in os.walk('./reljudge' + pathadaption):
        files.sort()
        for file in files:
            with open('./reljudge' + pathadaption + '/' + file) as f:
                data = json.load(f)

            querystring = data["querystring"]
            results = data["results"]
            count = 0
            relevants = []
            precs = []
            for j in range(1, len(results)+1):
                
                if results[j-1]["relevant"]:
                    count += 1
                    current = (float)(count)/(float)(j)
                    relevants.append(current)
                else:
                    current = (float)(count)/(float)(j)

                precs.append(current)

            if relevants:
                avgprecs.append(mean(relevants))
            t.add_row([querystring, avgprecs[-1]])

    print(t)

    if avgprecs:
        meanavgprec = mean(avgprecs)
    print("mean average precision: \t" + str(meanavgprec))

fuzzy = input('enable fuzzy search y/n: ')
if fuzzy == "y":
    fuzzy = True
    print('yes saved')
elif fuzzy == "n":
    fuzzy = False
    print('no saved')

wholeDoc = input('enable wholeDoc search y/n: ')
if wholeDoc == "y":
    wholeDoc = True
    print('yes saved')
elif wholeDoc == "n":
    wholeDoc = False
    print('no saved')

evaltable(fuzzy, wholeDoc)
import json
import os
from statistics import mean
from pprint import pprint

def stats():
    meanavgprecs = []
    totalcounts = []
    with open('./eval.json') as f:
        data = json.load(f)

    for i in data:
        c = 0
        meanavgprecs.append(data[i]["mean average precision"])
        for j in data[i]['queries']:
            c += j["totalcount"]
        totalcounts.append(c)
        
    baselinemap = meanavgprecs[0]
    baselinetc = totalcounts[0]

    i = 0

    print('\nbaseline map: ' + str(baselinemap) + ' baseline totalcount: ' +  str(baselinetc) + '\n')
    for string in data:
        if i != 0: 
            print('for ' + string + ' is mean average precision is '  +  str(meanavgprecs[i]) + ', thats ' + str(round(-100+meanavgprecs[i]/(baselinemap/100), 2)) + "% different to baseline")
            print('and totalcounts are ' +  str(totalcounts[i]) + ', thats ' + str(round((totalcounts[i]/baselinetc), 2)) + ' times baseline\n')

        i = i + 1
    

 

stats()

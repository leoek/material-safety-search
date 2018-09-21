import json
from statistics import mean
 
#read in judgments
with open('reljudge.json') as f:
    data = json.load(f)

avgprecs = []

for i in range(0, len(data)):
    avgprecs.append(0)
    querystring = data[i]["querystring"]
    results = data[i]["results"]
    count = 0
    relevants = []
    precisisions = []
    for j in range(1, len(results)+1):
        
        if results[j-1]["relevant"]:
            count += 1
            current = (float)(count)/(float)(j)
            relevants.append(current)
        else:
            current = (float)(count)/(float)(j)

        precisisions.append(current)
    if relevants:
        avgprecs[i] = mean(relevants)
    print(avgprecs)

if avgprecs:
    meanavgprec = mean(avgprecs)
print(meanavgprec)

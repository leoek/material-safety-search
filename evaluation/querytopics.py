import json
import requests
import yaml
from pprint import pprint

#read in topics
with open('topics.json') as f:
    data = json.load(f)

reljudges = []

#iterate through topics and send queries and print the results, adapt numsearchresults as wanted
numsearchresults = 10
queryadapt = ""
#judge every result at once
def querytopics(numsearchresults, queryadapt):
    judgement = False
    input("q to quit\n enter start")
    for i in range(0, len(data)):
        query = yaml.safe_load(data[i]["query"])
        params = {'s': query + queryadapt}
        r = requests.get("http://localhost:8080/search", params=params)
        print('query: ' + query + '\n')
        reljudges.append({
                        'querystring': query,
                        'results': []
                        })
        pprint(reljudges)
        for j in range(0, numsearchresults):
            retrieved = r.json()['items'][j]['rawIdentification']
            print(retrieved + '\n')
            line = input('y/n: ')
            if line == "y":
                judgement = True
            elif line == "n":
                judgement = False
            elif line == "q":
                return
                
            reljudges[i]["results"].append({
                'doc': retrieved, 
                'relevant': judgement
                })
        print('\n')


querytopics(numsearchresults, queryadapt)

#with open('reljudge.json', 'r') as fp: 
    #reljudges += json.load(fp)
#file is overwritten everytime so judgements need to done all at once
with open('reljudge.json', 'w') as fp:   
    json.dump(reljudges, fp, indent=4)

    
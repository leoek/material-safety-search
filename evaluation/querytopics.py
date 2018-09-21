import json
import requests
import yaml
from pprint import pprint

#how many searchresults for one query to be judged
numsearchresults = 10
#which keys to be displayed on console when judging document
keylist = [
    'docType',
    'productId',
    'niin',
    'companyName',
    'fsc',
    'fscString',
    'fsgString'
]



#iterate through topics and send queries and print the results, adapt numsearchresults as wanted
def querytopics(filename, requesttype, numsearchresults, fuzzy=None, wholeDoc=None):

    #read in topics
    with open(filename) as f:
        data = json.load(f)

    judgement = None

    #start dialog
    topicnumber = (int)(input("\nq to quit and save progress\nenter topicnumber to start at (null-based): "))
    for i in range(topicnumber, len(data)):
        query = yaml.safe_load(data[i]["query"])

        #for post with json
        if requesttype == 'post':
            payload = {
                        'searchTerm': query,
                        'fsgFacet': None,
                        'fscFacet': None,
                        'fuzzy': fuzzy,
                        'wholeDoc': wholeDoc
                        }
            r = requests.post("http://localhost:8080/search", json=payload, headers={'content-type': 'application/json'})            
        
        #for get request with params (temporary)
        elif requesttype == 'get':
            payload = {'s': query}
            r = requests.get("http://localhost:8080/search", params=payload)
        else:
            print('invalid requesttype')
            return

        #check if request successfull
        if r.status_code == requests.codes.ok:
            print('query: ' + query + '\n')
            reljudges = {
                        'querystring': query,
                        'results': []
                        }
            for j in range(0, numsearchresults):
                retrieved = {key : r.json()['items'][j][key] for key in keylist }
                pprint(retrieved)
                line = input('y/n: ')
                
                if line == "y":
                    judgement = True
                    print('yes saved')
                elif line == "n":
                    judgement = False
                    print('no saved')
                elif line == "q":
                    break
                else:
                    print('nothing saved')

                reljudges["results"].append({
                    'doc': retrieved, 
                    'relevant': judgement
                    })
                print('\n')

            #write to json file with topicnumber
            with open('./reljudge/topic' + str(data[i]["number"]) + '.json', 'w') as fp:   
                json.dump(reljudges, fp, indent=4)

            if line == 'q':
                break

        #request to solr failed
        else:
            print(r.status_code + " request failed")
 

querytopics('topics.json', 'get', numsearchresults, fuzzy=None, wholeDoc=None)
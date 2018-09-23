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
    #'fscString',
    #'fsgString',
    #'ingredients',
    #'rawDisposal',
    #'rawChemicalProperties',
    #'rawComposition',
    #'rawAccidentalRelease',
    #'rawEco',
    #'rawFireFighting',
    #'rawFirstAid',
    #'rawHandlingStorage',
    #'rawHazards',
    #'rawIdentification',
    #'rawOther',
    #'rawProtection',
    #'rawRegulatory',
    #'rawStabilityReactivity',
    #'rawToxic',
    #'rawTransport'
]


#iterate through topics and send queries and print the results, adapt numsearchresults as wanted
def querytopics(filename, numsearchresults, fuzzy=False, wholeDoc=False, requesttype='post'):

    #read in topics
    with open(filename) as f:
        data = json.load(f)

    judgement = None
    
    pathadaption = ''
    if fuzzy:
        pathadaption += 'f'
    if wholeDoc:
        pathadaption += 'w'

    #start dialog
    topicnumber = (int)(input("\nq to quit and save progress\nenter topicnumber to start at (null-based): "))
    print()
    for i in range(topicnumber, len(data)):
        query = yaml.safe_load(data[i]["query"])

        #for post with json
        if requesttype == 'post':
            payload = {
                            'searchTerm': query,
                            'fsgFacet': '',
                            'fscFacet': '',
                            'fuzzy': fuzzy,
                            'wholeDoc': wholeDoc
                    }
            r = requests.post("http://localhost:8080/search", json=payload)            
        
        #for get request with params (temporary)
        elif requesttype == 'get':
            payload = {'s': query}
            r = requests.get("http://localhost:8080/search", params=payload)
        else:
            print('invalid requesttype')
            return

        #check if request successfull
        if r.status_code == requests.codes.ok:
            print('query#' + str(topicnumber) + ': ' + query + '\n')
            print('total count of results: ' + str(r.json()['meta']['totalCount']))
            reljudges = {
                        'querystring': query,
                        'totalcount': r.json()['meta']['totalCount'],
                        'results': []
                        }
            for j in range(0, numsearchresults):
                retrieved = {key : r.json()['items'][j][key] for key in keylist }
                
                print('query#' + str(topicnumber) + ': ' + query + '\n')
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

            

            print('./reljudge' + pathadaption + '/topic' + str(data[i]["number"]) + '.json')
            #write to json file with topicnumber
            with open('./reljudge' + pathadaption + '/topic' + str(data[i]["number"]) + '.json', 'w') as fp:   
                json.dump(reljudges, fp, indent=4)

            line = input('query finished, enter to start next query or q to quit ')
            if line == 'q':
                break

        #request to solr failed
        else:
            print(str(r.status_code) + " request failed")
            break
 
#requestmethod = input('enable requestmethod search get/post: ')

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

querytopics('topics.json', numsearchresults, fuzzy, wholeDoc)
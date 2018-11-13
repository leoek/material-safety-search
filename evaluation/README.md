# Evaluation

## Requirements

**NOTE**: install python 3 and prettytable possibly (pip3 install prettytable)

* the solr server must run to receive the requests

## Usage

### query topics
**NOTE**: all topicjudgements were made **randomly** so far
* in topics.json specify topics ("query" parameter will be execute as in json)

* in `querytopics.py` you can change how many results should be judged `numsearchresults` (default 10),
and you can say which fields should be displayed when you judge so you can make a better choice (simply uncomment disired fields in `keylist`)

* python command may vary
* in ./evaluation call
`python3 querytopics.py`
to query the specified topics and judge them regarding to relevance

* at the start you are asked which requestmethod you want to use, if you want to use fuzzy search or wholeDoc search

**NOTE**: if you dont quit with q but stop the programm, your judgements wont be saved.
**NOTE**: judging a topic again will override the old file completly in /reljudge, so its recommended to at least finish the topic.
* you can at the start specify with which topic you wish to begin and you are asked after every topic if you want to quit, so you are able to judge every topic seperatly

### evaluation calculation
* call
`python3 eval.py` 
to display a table with the average precisions of the topics and the mean average precision of all topics

* at the start you are asked if you want to use fuzzy search or wholeDoc search, which will determine from which folder and so with which searchsettings, the calculation will be done 

* (/reljudge) = normal
(/reljudgef) = fuzzy on
(/reljudgew) = wholeDoc on
(/reljudgefw) = both on

### statistics

* call 
`python3 stats.py` 
to display the % difference of mean average precision of with fuzzy, wholeDoc, and both enabled to normal search,
aswell as what the ratio of total search results (over all queries) the configurations had compared to normal search
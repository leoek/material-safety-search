# MSDS Search
Search engine for Material Safety Datasheets based on Solr.

## Installation
NOTE: You will need the dataset that is NOT in this repository because of liscensing issues. Contact the developers.

This project uses docker-compose for comfortable installation and distribution.
### Backend
Clone the repository and navigate to the base folder with a shell, then clone the hazard-ds repository here
then:
to start backend and frontend locally:
```docker-compose -f docker-compose.local.yml up -d```
it might fail if ur port 80 or 8080 are used by something else
To shut down the local docker container use:
```docker-compose -f docker-compose.local.yml down ``` 
or with -v to kill the image aswell

On startup the application will check if documents are already imported to Solr and wont try to import anything if that is the case.  
To erase the docker volume that is used to store these documents use the `-v` flag:

### Frontend
Todo

## Usage
Use the frontend application running at `<insert url>` to communicate with the engine.  
Alternatively you can use the backend API directly:  
`http://localhost:8080/search?s=<searchTerm>&page=<pageNumber>&size=<pageSize>`  
The Solr Admin UI runs on `http://localhost:8983/solr/#/` as per default.  
See `swagger.yml` for more detailed API information.

the webui is locally available at http://localhost:80/

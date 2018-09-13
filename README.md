# MSDS Search
Search engine for Material Safety Datasheets based on Solr.

## Installation
NOTE: You will need the dataset that is NOT in this repository because of liscensing issues.
This project uses docker-compose for comfortable installation and distribution.
### Backend
Clone the repository and navigate to the base folder with a shell, then type:
```docker-compose up
./gradlew bootRun
```
To shut down the docker container use:
```docker-compose down```
On startup the application will check if documents are already imported to Solr and wont try to import anything if that is the case.
To erase the docker volume that is used to store these documents use the `-v` flag:
```docker-compose down -v```
### Frontend
Todo

## Usage
Use the frontend application running at `<insert url>` to communicate with the engine.
Alternatively you can use the backend API directly:
`http://localhost:8080/search?s=<searchTerm>&page=<pageNumber>&size=<pageSize>`
The Solr Admin UI runs on `http://localhost:8983/solr/#/` as per default.
See `swagger.yml` for more detailed API information.

# MSDS Search
Search engine for Material Safety Datasheets based on Solr

## [Live Demo](https://mss.leoek.tech)

## Status

| Backend | Frontend | Deployment |
|---|---|---|
|   |   |   |
| [![Build Status](https://ci.net1.leoek.eu/buildStatus/icon?job=material-safety-search/material-safety-search_server-build)](https://ci.net1.leoek.eu/job/material-safety-search/job/material-safety-search_server-build/) | [![Build Status](https://ci.net1.leoek.eu/buildStatus/icon?job=material-safety-search/material-safety-search-frontend-build)](https://ci.net1.leoek.eu/job/material-safety-search/job/material-safety-search-frontend-build/) | [![Build Status](https://ci.net1.leoek.eu/buildStatus/icon?job=material-safety-search/material-safety-search-staging-deploy)](https://ci.net1.leoek.eu/job/material-safety-search/job/material-safety-search-staging-deploy/) |

## How to Run
**NOTE**: Read the following section carefully! 

* You will need the dataset that is NOT in this repository because of liscensing issues. 

* The dataset is linked as git submodule and can be installed by running: 
`git submodule update --init --recursive`

* All of the following setups will expect the dataset to be in `<repo-root>/hazard-ds/hazard-dataset`. If your dataset is in a different location please override the path in the docker-compose files (the dataset is just mounted into the container) or override the application.property `dataSetPath`.

* If you are **not** running the backend in a docker container, there are 2 additional files needed.
Their default paths are `<repo-root>/hazard-ds/fscMap.txt` and `<repo-root>/hazard-ds/fsgMap.txt`. These paths can be adjusted through the environment variables `fscMapPath` and `fsgMapPath`.

**NOTE**: If you have questions about the following docker commands, you can find their documentation [here](https://docs.docker.com/compose/reference/overview/). These are the basic commands which you will probably need:

1. `docker-compose up -d` Start/create containers, volumes and networks. Setup and detach your console.
2. `docker-compose logs -f` View the logs and attach your console to them.
3. `docker-compose down` Stop and delete all containers.
4. `docker-compose down -v` Stop, delete all containers and remove all data volumes.
5. The `-f` flag is used to specify a certain compose file. There are different compose files with different purposes in this repository.

#### There are two options available to run this search engine:

1. Use our prebuilt docker images: `sudo docker-compose -f docker-compose.staging.yml up -d`
2. Build and run the images on your local machine (which might take a while): `sudo docker-compose -f docker-compose.local.yml up -d`

**ATTENTION**: Please note that the solr container does not require any login information. Therefore it should not be exposed to the public. Our docker-compose.local.yml configuration **will expose all ports** for debugging purposes. You should never use that configuration in any production environment.

#### Production Setup / Deployment

* We recommend to run all containers behind some kind of reverse proxy. You can get an example on how to do that in our docker-compose.staging.yml configuration.
* Our backend and frontend containers do not have any SSL support. We recommend to setup SSL within the reverse proxy.
* You can find an example Ansible setup inside `/ansible` which we used to deploy the search engine to the demo server. The Ansible setup will work with most debian based systems.
    * `ansible-role-common`: Basic server setup, you probably want to replace / adjust this.
    * `ansible-role-docker`: Install / update docker and docker-compose. Set it up to work with the Ansible `docker_service` module (which has some flaws).
    * `ansible-role-docker-nginx-proxy`: Setup jwilder as a reverse proxy, with letsencrypt-companion for SSL certificates
    * `docker-mss-deployment-local`: This is the role which will deploy the search engine.
    * `docker-mss-deployment-local-reset-volumes`: This role can be used to clear the docker volumes. Use this if you want to re-index the dataset.

----
## Development / Building from Source

Check for these files to be present:
1. The dataset
2. fscMap.txt
3. fsgMap.txt

### Development Workflow

* We are using git flow.
    * The `master` branch only contains released versions.
    * Every release has its own tag, we are using semantic versioning.
    * The `develop` branch contains the current state of development. The code within `develop` should always be in such a shape, that we could make a release without any patches needed.
    * Actual development happens in `feature` branches. `feature` branches should be commited to the remote repository and deleted once they are merged into `develop`.
    * Try to avoid merge commits with the usage of `git pull --rebase`.

* Every commit to develop will be built and published to [docker hub](https://hub.docker.com/r/materialsafetysearch/private/) by [Jenkins](ci.leoek.eu). Builds from develop are tagged with `next`, builds from master with `stable` and their `<full version>-<buildnumber>`. Until the first release, builds from develop will be pushed with their version to develop as well.

* Every build from develop is deployed to [mss.leoek.tech](mss.leoek.tech) and [api.mss.leoek.tech](api.mss.leoek.tech). The frontend can develop against that staging api.


#### Building inside a Docker Container:

Requirements:
1. Docker-ce 15+
2. docker-compose 1.16+

Procedure:
1. `sudo docker-compose -f docker-compose.local.yml up -d`
2. Solr will be available at [http://localhost:8983](http://localhost:8983)
3. The Backend will be available at [http://localhost:8080](http://localhost:8080)
4. The Frontend will be available at [http://localhost:80](http://localhost:80)

#### Backend

Requirements:
1. Java JDK 10
2. Docker-ce
3. docker-compose 1.16+

Procedure:
1. You will need a Solr instance for development. The easiest way to get one up and running which is configured for local development is by running `docker-compose up -d`.
2. Our Backend is a Spring Boot application, which uses Gradle for package management. You can build and run it with: `./gradlew bootRun` (theoretically it should work with the .bat file on windows, however until now nobody had the nerves to mess with windows).
3. Solr will be available at [http://localhost:8983](http://localhost:8983)
4. The Backend will be available at [http://localhost:8080](http://localhost:8080)

#### Frontend

The frontend can be found within the /frontend folder.

Requirements:
1. Yarn 1.8+
2. Node 8+

Procedure:
1. Run `yarn` to obtain the dependencies.
2. Run `yarn start` to start the development server.
3. The Frontend will be available at [http://localhost:3000](http://localhost:3000)


**NOTES**:

1. The Backend will check whether documents are already imported to Solr. If the solr core is not empty it will skip the import of the documents.
2. Solr data is in a named docker volume, which will prevent losing the Solr core with the indexed documents in between container restarts.
3. The Solr data volume can be deleted with the `-v` flag: `docker-compose down -v`

#### Postman

Postman can be used to talk to the backend directly. [Import](https://medium.com/@codebyjeff/using-postman-environment-variables-auth-tokens-ea9c4fe9d3d7) the supplied collection and environments and you should be good to go.

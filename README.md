Run DB in Docker container:
```
docker run --name building-limits-processor-db -e POSTGRES_PASSWORD=buildinglimitspassword -e POSTGRES_USER=buildinglimits -e POSTGRES_DB=buildinglimits -d -p 5011:5432 postgres 
```
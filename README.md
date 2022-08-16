To be able to run the app locally, run DB in Docker container:
```
docker run --name building-limits-processor-db -e POSTGRES_PASSWORD=buildinglimitspassword -e POSTGRES_USER=buildinglimits -e POSTGRES_DB=buildinglimits -d -p 5011:5432 postgres 
```


So I didn't see this task as something particularly difficult - to me it was basically a simple CRUD with a bit of logic and a careful validation on top.

I started off confidently, did my research, read about GeoJson, looked at a few libraries, chose one lightweight, that would help me parse and operate on provided geometries. I was working in TDD style, which I usually advocate for. TDD slows things down, but, again, I was confident that I could do the task in time, provided that I had plenty of it and the task was typical.

It was going fine until I ran into problems with parsing - the library that I chose turned out to be very clumsy. I switched to another one - it wasn't better. What's exactly the input? Is it FeatureCollection, GeometryCollection, Feature? Those libraries treated them all differently. In the end I decided to just parse the input manually. The domain turned out to be with a good amount of hidden things, that needed additional research, so it all cost me a few hours.

Then I got a problem with implementing the logic itself - it turned out that the library I was hoping for (Spatial4j) could determine if two polygons intersect, but couldn't give the actual intersection points. So I had to switch again, checking all the available libraries whether they could do it. I even thougth briefly about implementing the logic myself, but after some reading decided to not go into that particular rabbit hole. After a couple of hours I discovered that JTS can solve my problem. At that point things were moving too slow, I was feeling the pressure, but I still tried to stick to TDD and best practices.

In short - the unknown domain considerably hindered my progress, as I had to pivot a few times, and every time it required rewriting some portion of the existing code. The last thing that I changed was making the models store point in a different collection, which just made some tests failing. I was running out of time, so I just commented them. What a shameful display. Things started failing, everything went out of hand and I ran out of time writing a simple piece of software. Nothing can crush your soul like software development :)

The service is still able to do it's job, provided that the input is validated by the user. I tried to give it meaningful logging, some basic exception handling and nice rest responces in case of errors.

The scenario of usage is as follows:
1. *POST localhost:8080/geodata*
```
{"building_limits":{"type":"GeometryCollection","geometries":[{"type":"Polygon","coordinates":[[[10.0,10.0],[10.0,20.0],[20.0,20.0],[20.0,10.0]]]},{"type":"Polygon","coordinates":[[[1.0,1.0],[5.0,1.0],[5.0,5.0],[1.0,5.0]]]}]},"height_plateaus":{"type":"GeometryCollection","geometries":[{"type":"Polygon","coordinates":[[[0.0,0.0,10.0],[3.0,0.0,10.0],[3.0,12.0,10.0],[0.0,12.0,10.0]]]},{"type":"Polygon","coordinates":[[[0.0,12.0,1.0],[22.0,12.0,1.0],[22.0,18.0,1.0],[0.0,18.0,1.0]]]},{"type":"Polygon","coordinates":[[[3.0,0.0,15.0],[21.0,0.0,15.0],[21.0,12.0,15.0],[3.0,12.0,15.0]]]}]}}
```
this will create and store building limits and height plateaus

2. *POST localhost:8080/buildinglimitsplits/calculate*
```
[{"id":14,"points":[[10,12],[10,18],[20,18],[20,12]],"height":1.00},{"id":15,"points":[[20,10],[10,10],[10,12],[20,12]],"height":15.00},{"id":16,"points":[[3,1],[1,1],[1,5],[3,5]],"height":10.00},{"id":17,"points":[[3,5],[5,5],[5,1],[3,1]],"height":15.00}]
```
this will calculate, store and return building limit splits, based on all the building limits and height plateaus stored in the DB.
so the response should look like this:

3. 	*GET localhost:8080/buildinglimitsplits*

this just returns all the building limit splits


This is in no way a complete and stable API, but I hope that it might still be interesting to you and can start the conversation about it. My biggest gripe about this task is that I've done all of that before numerous times without big problems but this time everything just collapsed onto me. I still think that I did an ok job here, considering that I worked in between my actual job tasks and some French bureaucratic things. At least it looks ok, gives some information about what is failing, saves all the data (duplicates it though hehehe) and returns it. If it wasn't for the unknown domain I'm sure I'd do better.

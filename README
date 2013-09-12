TWiS = This Week In Scala
==========================

Objective
---------

An app to gather content for Cake's #ThisWeekInScala blog series.  


Method
------

The initial idea is to use a scheduled Actor to grab content
from Twitter which is tagged with #Scala, #Akka, #Play and #Scalaz.

Of course, some other content sources could be introduced over time, i.e. Prismatic.

Content (actually to be named a resource) will be persisted in MongoDB and will
be indexed by URL.  It is anticpated that content referring to a particular URL will be grouped
together (i.e. in the same MongoDb document).  Shortended URLs will be normalized, i.e. visited to get
the true URL.

A Play web app will be created to search for content within a given timeframe.

It is anticipated that the Twitter tags used for searching will be configurable via this app.
Naturally, any configuration needed for future content sources should be made in a similar way.


Components
----------

### Curators or Harvesters

Responsible for gathering content relevant to TWiS on a scheduled basis.  Interacts with services to store
incoming content.


### Core or Services

Responsible for fulfilling CRUD operations for persistent data.


### Portal or Viewer

Responsible for exposing content via a pretty bootstrap web UI.  Interacts with services to read content.
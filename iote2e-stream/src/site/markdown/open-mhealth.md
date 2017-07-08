Initial targets

High Level Goals
+ Avro schema against a set of open mhealth schemas
+ Generate test data via java - one reading per second, configurable patient id sets
+ speed and batch layers just for open mhealth
+ speed layer - 
	rules above a certain level, 
	stream a single user/single measure (or maybe just fixed to BP)
+ batch layer
	write all values to db -single table or multiple?
+ real time UI
	initially for a single user/single measure
	add selectable patient
+ tableau or qlikview
	subjects, measures on composite graph over time
	
Open Questions
+ need a WS or some other passthru for Docker?


Weekend
+ review the examples
+ create avro schema encapsulating multiple open mhealth schemas
+ ?wrap in Iote2eRequest?  That would be the fastest, but maybe not the most accurate

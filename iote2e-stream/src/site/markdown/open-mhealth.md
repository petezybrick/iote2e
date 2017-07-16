Initial targets

High Level Goals
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



Next Steps

+ Batch layer
	+ create tables
	+ write to separate tables based on OMH Schema
		>>> 2016-07-16 need to integrate and test DAO/VO's with populated schemas, then integrate into spark code
+ Speed Layer
	1. for a specific user and BP, write the systolic and diastolic to WS #2
	2. Create WS page to get the systolic and diastolic, plot in real time
	3. rule: When exceeded, send email to doctor_email - create drzybrick@gmail.com
	

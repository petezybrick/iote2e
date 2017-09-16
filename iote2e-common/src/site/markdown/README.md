# iote2e - Internet of Things End to End

iote2e is a set of projects that demonstrates four use cases of IoT End to End interactions via a Lambda architecture.  The four use cases are highlighted below. Please view this [presentation](slides/iote2e-1.0.0.pptx) for a high level understanding.  To install locally, please review the [Installation Guide](INSTALL.md).  This project encompasses the usage and integration of various open source technologies, including Docker, Avro, Kafka, Spark Streaming, Cassandra, Ignite, Zeppelin and Rickshaw running on Ubuntu and integrating Raspberry Pi's.  The application consists of 24 virtual servers running under Docker on a single workstation/laptop as well as optional Tableau dashboards running on Windows.


## Demonstrated Use Cases

### MyHomeGreenhouse.com
- A greenhouse in your backyard that is remotely managed
- Sensor data is streamed from all greenhouses, actuators are instructed to perform tasks (i.e. turn on water, turn off fan)
- Realtime Display of Temperature Data

### Dynamic Clinical Trial
- Clinical trial for a blood pressure medication
- Pill Dispenser is installed in the home of each Subject
- Each pill is 40mg, and the number of pills dispensed is based on criteria determined by the team managing the trial
- This criteria can change change the number of pills dispensed/subject over the course of the study and must be tracked
- When pills are dispensed, the number of pills dispensed must be verified programmatically and receipt confirmed by the subject

### YourPersonalizedMedicine.com
- Patient Device submits various measures (blood pressure, blood glucose, etc.) 
- Scalably support ever increasing number of patients
- Support industry standards - Open mHealth and Apple HealthKit
- Dashboard for doctor and patient, can view history and real time values
- If a criteria is exceeded (i.e. diastolic > 100) then send an email to doctor, with dashboard link embedded in the email.

### Big Data Black Box
- Stream Flight Status data for Airframe and Engines
- Scalably support ever increasing number of flights
- Dashboard for airline chief mechanic, view history and real time values
- If a criteria is exceeded (i.e. oil pressure > 90) then send an email to that airlines chief mechanic, with dashboard link embedded in the email.



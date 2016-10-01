package com.pzybrick.iote2e.ws.igniteavro;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pzybrick.iote2e.avro.schema.ActuatorResponse;

public class CacheEntryUpdatedListenerActuatorResponse implements CacheEntryUpdatedListener<Integer, byte[]> {
    private Gson gson;
	private ConcurrentLinkedQueue<String> messagesToSend = null;
	private BinaryDecoder binaryDecoder = null;
	private ActuatorResponse actuatorResponse = null;
	private DatumReader<ActuatorResponse> datumReaderActuatorResponse = new SpecificDatumReader<ActuatorResponse>(ActuatorResponse.getClassSchema());


	public CacheEntryUpdatedListenerActuatorResponse() {
		this.gson = new GsonBuilder().create();
	}

	public CacheEntryUpdatedListenerActuatorResponse(ConcurrentLinkedQueue<String> messagesToSend) {
		this();
		this.messagesToSend = messagesToSend;
	}
	
	@Override
	public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends byte[]>> events) {
		for (CacheEntryEvent<? extends Integer, ? extends byte[]> event : events) {
			try {
				binaryDecoder = DecoderFactory.get().binaryDecoder(event.getValue(), binaryDecoder);
				actuatorResponse = datumReaderActuatorResponse.read(actuatorResponse, binaryDecoder);
				//System.out.println(">>> Sub: " + actuatorResponse.toString());
				if( messagesToSend != null ) {
					String sourceUuid = actuatorResponse.getSourceUuid().toString();
					int sensorId = Integer.parseInt( sourceUuid.substring(sourceUuid.lastIndexOf('-')+1 ));
					ActuatorResponseItem actuatorResponseItem = new ActuatorResponseItem( sensorId, sourceUuid, actuatorResponse.getTsEpoch(), actuatorResponse.getActuatorResponseValue());
					messagesToSend.add( gson.toJson(actuatorResponseItem) );
				}
				
			} catch( Exception e ) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}

	
	public ConcurrentLinkedQueue<String> getMessagesToSend() {
		return messagesToSend;
	}

	public void setMessagesToSend(ConcurrentLinkedQueue<String> messagesToSend) {
		this.messagesToSend = messagesToSend;
	}


}

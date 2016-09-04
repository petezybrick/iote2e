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
import com.pzybrick.avro.schema.Wave;
import com.pzybrick.learnjetty.jsr.WaveItem;

public class CacheEntryUpdatedListenerAvroWave implements CacheEntryUpdatedListener<Integer, byte[]> {
    private Gson gson;
	private ConcurrentLinkedQueue<String> messagesToSend = null;
	private BinaryDecoder binaryDecoder = null;
	private Wave wave = null;
	private DatumReader<Wave> datumReaderWave = new SpecificDatumReader<Wave>(Wave.getClassSchema());


	public CacheEntryUpdatedListenerAvroWave() {
		this.gson = new GsonBuilder().create();
	}

	public CacheEntryUpdatedListenerAvroWave(ConcurrentLinkedQueue<String> messagesToSend) {
		this();
		this.messagesToSend = messagesToSend;
	}
	
	@Override
	public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends byte[]>> events) {
		for (CacheEntryEvent<? extends Integer, ? extends byte[]> event : events) {
			try {
				binaryDecoder = DecoderFactory.get().binaryDecoder(event.getValue(), binaryDecoder);
				wave = datumReaderWave.read(wave, binaryDecoder);
				//System.out.println(">>> Sub: " + wave.toString());
				if( messagesToSend != null ) {
					String sourceUuid = wave.getSourceUuid().toString();
					int sensorId = Integer.parseInt( sourceUuid.substring(sourceUuid.lastIndexOf('-')+1 ));
					WaveItem waveItem = new WaveItem( sensorId, sourceUuid, wave.getTsEpoch(), wave.getWaveValue());
					messagesToSend.add( gson.toJson(waveItem) );
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

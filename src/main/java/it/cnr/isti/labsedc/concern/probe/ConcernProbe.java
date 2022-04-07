package it.cnr.isti.labsedc.concern.probe;

import it.cnr.isti.labsedc.concern.event.ConcernBaseEvent;

/**
 * 
 * The interface {@link ConcernProbe} contains the event that a generic probe<br />
 * must implement to generate events.
 * 
 *  The behaviour of a Probe is defined in {@link GlimpseProbe}
 * 
 * @author acalabro
 *
 */
public interface ConcernProbe {

	public void sendMessage(ConcernBaseEvent<?> event, boolean debug);
}

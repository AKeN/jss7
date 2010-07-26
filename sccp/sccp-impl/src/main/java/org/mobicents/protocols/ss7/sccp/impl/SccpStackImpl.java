package org.mobicents.protocols.ss7.sccp.impl;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;

/**
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class SccpStackImpl implements SccpStack {
	
	private static final Logger logger = Logger.getLogger(SccpStackImpl.class);

	private State state = State.IDLE;

	private AbstractSccpProviderImpl sccpProvider;

	private final static SccpProviderFactory factory = new SccpProviderFactory();
	
	public SccpStackImpl(){
		
	}
	
	public SccpStackImpl(SccpProvider sccpProvider){
		this.sccpProvider = (AbstractSccpProviderImpl)sccpProvider;
		this.state = State.CONFIGURED;
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.sccp.SccpStack#configure(java.util.Properties
	 * )
	 */
	public void configure(Properties properties) throws ConfigurationException {
		if (state != State.IDLE) {
			throw new IllegalStateException("Stack already been configured or is already running!");
		}
		this.sccpProvider = (AbstractSccpProviderImpl) factory.getProvider(properties);
		this.state = State.CONFIGURED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#getSccpProvider()
	 */
	public SccpProvider getSccpProvider() {
		// TODO Auto-generated method stub
		return sccpProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#start()
	 */
	public void start() throws IllegalStateException, StartFailedException {
		logger.info("Starting ...");
		if (state != State.CONFIGURED) {
			throw new IllegalStateException("Stack has not been configured or is already running!");
		}
		this.sccpProvider.start();
		this.state = State.RUNNING;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#stop()
	 */
	public void stop() {
		if (state != State.RUNNING) {
			throw new IllegalStateException("Stack is not running!");
		}
		this.sccpProvider.stop();
		this.state = State.CONFIGURED;
	}

	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

}

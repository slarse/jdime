/*******************************************************************************
 * Copyright (c) 2013 Olaf Lessenich.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Olaf Lessenich - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package de.fosd.jdime.common;

import java.io.IOException;

import de.fosd.jdime.engine.EngineNotFoundException;

/**
 * @author Olaf Lessenich
 *
 */
public abstract class Operation {
	public MergeReport apply()  throws EngineNotFoundException, IOException, InterruptedException, NotYetImplementedException {
		throw new NotYetImplementedException();
	}
	
	public String description() throws NotYetImplementedException {
		throw new NotYetImplementedException();
	}
}
package org.apel.gaia.persist.config.hibernate;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;

public class ExtendDialectResolver implements DialectResolver {

	private static final long serialVersionUID = -4900577685304036692L;

	@Override
	public Dialect resolveDialect(DialectResolutionInfo info) {
//		final String databaseName = info.getDatabaseName();
//		if ( "XX DBMS".equalsIgnoreCase( databaseName ) ) {
//			return new XXDialect();
//		}
		return null;
	}

}

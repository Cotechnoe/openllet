package openllet.core.datatypes.types.datetime;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import openllet.core.datatypes.RestrictedDatatype;
import openllet.core.utils.ATermUtils;
import openllet.core.utils.Namespaces;

/**
 * <p>
 * Title: <code>xsd:gDay</code>
 * </p>
 * <p>
 * Description: Singleton implementation of <code>xsd:gDay</code> datatype. This implementation diverges from the XML Schema specification because
 * <ol>
 * <li>the value space is disjoint from the value space of other timeline based datatypes (e.g., xsd:dateTime)</li>
 * <li>values are treated as points, not as intervals</li>
 * </ol>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 *
 * @author Mike Smith
 */
public class XSDGDay extends AbstractTimelineDatatype
{

	private static final XSDGDay instance;

	static
	{
		instance = new XSDGDay();
	}

	public static XSDGDay getInstance()
	{
		return instance;
	}

	private final RestrictedTimelineDatatype dataRange;

	private XSDGDay()
	{
		super(ATermUtils.makeTermAppl(Namespaces.XSD + "gDay"), DatatypeConstants.GDAY);

		dataRange = new RestrictedTimelineDatatype(this, DatatypeConstants.GDAY, false);
	}

	@Override
	public RestrictedDatatype<XMLGregorianCalendar> asDataRange()
	{
		return dataRange;
	}
}

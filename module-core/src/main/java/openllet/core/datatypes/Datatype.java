package openllet.core.datatypes;

import openllet.aterm.ATermAppl;
import openllet.core.datatypes.exceptions.InvalidLiteralException;

/**
 * <p>
 * Title: Datatype
 * </p>
 * <p>
 * Description: A datatype as described in the OWL 2 specificationf
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 *
 * @author Mike Smith
 * @param <T> type of literal
 */
public interface Datatype<T>
{

	/**
	 * Get the canonical _data range for a datatype
	 *
	 * @return a {@link DataRange} representation of the datatype value space
	 */
	RestrictedDatatype<T> asDataRange();

	/**
	 * Get the canonical representation of a lexical form
	 *
	 * @param input a lexical form of the literal
	 * @return the canonical representation of the lexical form
	 * @throws InvalidLiteralException if input is invalid
	 */
	ATermAppl getCanonicalRepresentation(ATermAppl input) throws InvalidLiteralException;

	/**
	 * Get the canonical {@link ATermAppl} literal representation for a value space object. This may be unsupported by datatypes for which
	 * {@link #isPrimitive()} returns <code>false</code>.
	 *
	 * @param value Element of the value space for some datatype
	 * @return The {@link ATermAppl} representation of <code>value</code>
	 * @throws IllegalArgumentException if <code>value</code> is not contained in the value space of the datatype
	 */
	ATermAppl getLiteral(Object value);

	/**
	 * Get the datatype identifier
	 *
	 * @return <code>ATermAppl</code> of URI for datatype
	 */
	ATermAppl getName();

	/**
	 * Get the primitive datatype associated with this datatype.
	 *
	 * @return <code>this</code> if <code>isPrimitive() == true</code>, else a primitive datatype that is a superset of the value space of this datatype.
	 */
	Datatype<?> getPrimitiveDatatype();

	/**
	 * Get the Java object representation of a _data value
	 *
	 * @param literal the literal
	 * @return the Java object representation of the lexical form
	 * @throws InvalidLiteralException if literal is invalid
	 */
	T getValue(ATermAppl literal) throws InvalidLiteralException;

	/**
	 * Check if a datatype is primitive. All datatypes are either primitive or derived. Derived datatypes are names for subsets of the value spaces of primitive
	 * datatypes, defined using specific constraining facet values.
	 *
	 * @return <code>true</code> if the datatype is primitive, <code>false</code> else
	 */
	boolean isPrimitive();
}

// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package openllet.core.rules.model;

/**
 * <p>
 * Title: Rule Atom Visitor
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 *
 * @author Ron Alford
 */
public interface RuleAtomVisitor
{

	default void visit(@SuppressWarnings("unused") final BuiltInAtom atom)
	{//
	}

	default void visit(@SuppressWarnings("unused") final ClassAtom atom)
	{//
	}

	default void visit(@SuppressWarnings("unused") final DataRangeAtom atom)
	{//
	}

	default void visit(@SuppressWarnings("unused") final DatavaluedPropertyAtom atom)
	{//
	}

	default void visit(@SuppressWarnings("unused") final DifferentIndividualsAtom atom)
	{//
	}

	default void visit(@SuppressWarnings("unused") final IndividualPropertyAtom atom)
	{//
	}

	default void visit(@SuppressWarnings("unused") final SameIndividualAtom atom)
	{//
	}
}

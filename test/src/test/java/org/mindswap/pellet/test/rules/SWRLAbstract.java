// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package org.mindswap.pellet.test.rules;

import static org.junit.Assert.assertTrue;

import com.clarkparsia.owlapi.OntologyUtils;
import com.clarkparsia.pellet.owlapi.PelletReasoner;
import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.junit.After;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * <p>
 * Title: SWRLAbstract
 * </p>
 * <p>
 * Description: Abstract class that is extended by SWRL test suites
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 *
 * @author Markus Stocker
 */
public class SWRLAbstract
{

	protected static String _base;

	protected void test(final String test)
	{
		testJena(url(test + "-premise.rdf"), url(test + "-conclusion.rdf"));
		testOWLAPI(url(test + "-premise.rdf"), url(test + "-conclusion.rdf"));
	}

	private void testJena(final String premiseURI, final String conclusionURI)
	{
		final OntModel premise = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		premise.read(premiseURI);
		premise.prepare();

		final Model conclusion = ModelFactory.createDefaultModel();
		conclusion.read(conclusionURI);

		final StmtIterator stmtIter = conclusion.listStatements();

		while (stmtIter.hasNext())
		{
			final Statement s = stmtIter.nextStatement();
			assertTrue(premise.contains(s));
		}
	}

	private void testOWLAPI(final String premiseURI, final String conclusionURI)
	{
		org.semanticweb.owlapi.model.OWLOntologyManager manager = null;

		try
		{
			manager = OWLManager.createOWLOntologyManager();
			final OWLOntology premise = manager.loadOntology(IRI.create(premiseURI));
			manager = OWLManager.createOWLOntologyManager();
			final OWLOntology conclusion = manager.loadOntology(IRI.create(conclusionURI));

			final PelletReasoner reasoner = new com.clarkparsia.pellet.owlapi.PelletReasonerFactory().createReasoner(premise);
			assertTrue(reasoner.isEntailed(conclusion.axioms()));
		}
		catch (final OWLOntologyCreationException e)
		{
			throw new RuntimeException(e);
		}

	}

	private String url(final String filename)
	{
		return _base + filename;
	}

	@After
	public void after()
	{
		OntologyUtils.clearOWLOntologyManager();
	}
}

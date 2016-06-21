package com.clarkparsia.owlwg.runner.pellet;

import com.clarkparsia.owlwg.owlapi.runner.impl.OwlApiAbstractRunner;
import com.clarkparsia.owlwg.testrun.TestRunResult;
import openllet.owlapi.PelletReasoner;
import openllet.owlapi.PelletReasonerFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * <p>
 * Title: Openllet OWLAPI Test Runner
 * </p>
 * <p>
 * Description: Openllet 2.0 based test case runner using alpha OWLAPI support.
 * </p>
 * <p>
 * Copyright: Copyright &copy; 2009
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC.
 * </p>
 * 
 * @author Mike Smith &lt;msmith@clarkparsia.com&gt;
 */
public class PelletOA3TestRunner extends OwlApiAbstractRunner
{

	private static final PelletReasonerFactory _reasonerFactory;

	private static final IRI _iri;

	static
	{
		_iri = IRI.create("http://clarkparsia.com/pellet");
		_reasonerFactory = new PelletReasonerFactory();
	}

	@Override
	public String getName()
	{
		return "Openllet";
	}

	@Override
	public IRI getIRI()
	{
		return _iri;
	}

	@Override
	protected boolean isConsistent(final OWLOntology o)
	{
		final PelletReasoner reasoner = _reasonerFactory.createReasoner(o);
		reasoner.getKB().setTimeout(_timeout);
		return reasoner.isConsistent();
	}

	@Override
	protected boolean isEntailed(final OWLOntology premise, final OWLOntology conclusion)
	{
		final PelletReasoner reasoner = _reasonerFactory.createReasoner(premise);
		reasoner.getKB().setTimeout(_timeout);
		return reasoner.isEntailed(conclusion.logicalAxioms());
	}

	@Override
	protected TestRunResult run(final TestAsRunnable runnable)
	{
		runnable.run();

		try
		{
			return runnable.getResult();
		}
		catch (final Throwable th)
		{
			System.gc();
			return runnable.getErrorResult(th);
		}

	}
}

package openllet.pellet.owlapi.test;

import static openllet.owlwg.Constants.OWLWG_TEST_CASES_IRI;
import static openllet.owlwg.Constants.RESULTS_ONTOLOGY_PHYSICAL_IRI;
import static openllet.owlwg.Constants.TEST_ONTOLOGY_PHYSICAL_IRI;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import openllet.owlwg.TestCollection;
import openllet.owlwg.cli.FilterConditionParser;
import openllet.owlwg.owlapi.testcase.impl.OwlApiTestCaseFactory;
import openllet.owlwg.runner.pellet.PelletOA3TestRunner;
import openllet.owlwg.testcase.TestCase;
import openllet.owlwg.testcase.filter.FilterCondition;
import openllet.owlwg.testrun.RunResultType;
import openllet.owlwg.testrun.TestRunResult;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.FreshEntitiesException;

@Ignore("Failing tests")
@RunWith(Parameterized.class)
public class OWLWGTestCase
{

	/**
	 * Ensure that test cases timeout after 30 seconds. This is in slightly broader than the 20 second timeout for each PelletOA3TestRunner.
	 */
	@Rule
	public Timeout timeout = new Timeout(30, TimeUnit.SECONDS);

	@Parameters
	public static List<Object[]> data() throws OWLOntologyCreationException, OWLOntologyChangeException
	{
		final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		final OwlApiTestCaseFactory factory = new OwlApiTestCaseFactory();

		final FilterCondition filter = FilterConditionParser.parse("approved proposed extracredit or direct dl and");

		/*
		 * Load the _test and results ontology from local files before
		 * reading the _test cases, otherwise import of them is likely to
		 * fail.
		 */
		manager.loadOntologyFromOntologyDocument(OWLWGTestCase.class.getResourceAsStream(TEST_ONTOLOGY_PHYSICAL_IRI));
		manager.loadOntologyFromOntologyDocument(OWLWGTestCase.class.getResourceAsStream(RESULTS_ONTOLOGY_PHYSICAL_IRI));

		final OWLOntology casesOntology = manager.loadOntologyFromOntologyDocument(OWLWGTestCase.class.getResourceAsStream(OWLWG_TEST_CASES_IRI));

		try
		{
			final TestCollection<OWLOntology> cases = new TestCollection<>(factory, casesOntology, filter);

			final List<Object[]> testParams = new ArrayList<>(cases.size());
			for (final Object test : cases.asList())
			{
				testParams.add(new Object[] { test });
			}

			return testParams;
		}
		finally
		{
			manager.removeOntology(casesOntology);
		}
	}

	private TestCase<OWLOntology> _test;

	public OWLWGTestCase(TestCase<OWLOntology> test)
	{
		_test = test;
	}

	@Test
	public void runTestCase()
	{
		try
		{
			final PelletOA3TestRunner runner = new PelletOA3TestRunner();
			for (final TestRunResult result : runner.run(_test, 10 * 1000))
			{
				final RunResultType resultType = result.getResultType();
				if (!RunResultType.PASSING.equals(resultType))
				{
					if (result.getCause() != null)
					{
						// FIXME Can get rid of conditional once #295 is fixed.
						if (!(result.getCause() instanceof FreshEntitiesException))
							throw new RuntimeException(_test.getIdentifier(), result.getCause());
					}
					else
					{
						fail(result.toString());
					}
				}
			}
		}
		finally
		{
			_test.dispose();
			_test = null;
			System.gc();
		}
	}

}

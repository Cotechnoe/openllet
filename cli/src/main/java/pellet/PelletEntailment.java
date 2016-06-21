// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public
// License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of
// proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package pellet;

import static pellet.PelletCmdOptionArg.NONE;
import static pellet.PelletCmdOptionArg.REQUIRED;

import com.clarkparsia.owlapi.explanation.io.manchester.ManchesterSyntaxObjectRenderer;
import com.clarkparsia.owlapi.explanation.io.manchester.TextBlockWriter;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;
import openllet.core.utils.FileUtils;
import openllet.owlapi.EntailmentChecker;
import openllet.owlapi.OWLAPILoader;
import openllet.owlapi.PelletReasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * <p>
 * Title: PelletEntailment
 * </p>
 * <p>
 * Description: Given an input ontology check if the axioms in the output ontology are all entailed. If not, report either the first non-entailment or all
 * non-entailments.
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
public class PelletEntailment extends PelletCmdApp
{

	private String entailmentFile;
	private boolean findAll;

	public PelletEntailment()
	{
	}

	@Override
	public String getAppId()
	{
		return "PelletEntailment: Check if all axioms are entailed by the ontology";
	}

	@Override
	public String getAppCmd()
	{
		return "pellet entail " + getMandatoryOptions() + "[_options] <file URI>...";
	}

	@Override
	public PelletCmdOptions getOptions()
	{
		final PelletCmdOptions options = getGlobalOptions();

		options.add(getIgnoreImportsOption());

		PelletCmdOption option = new PelletCmdOption("entailment-file");
		option.setShortOption("e");
		option.setType("<file URI>");
		option.setDescription("Entailment ontology URI");
		option.setIsMandatory(true);
		option.setArg(REQUIRED);
		options.add(option);

		option = new PelletCmdOption("all");
		option.setShortOption("a");
		option.setDefaultValue(false);
		option.setDescription("Show all non-entailments");
		option.setDefaultValue(findAll);
		option.setIsMandatory(false);
		option.setArg(NONE);
		options.add(option);

		return options;
	}

	@Override
	public void run()
	{
		entailmentFile = _options.getOption("entailment-file").getValueAsString();
		findAll = _options.getOption("all").getValueAsBoolean();

		final OWLAPILoader loader = (OWLAPILoader) getLoader("OWLAPI");

		getKB();

		final PelletReasoner reasoner = loader.getReasoner();

		OWLOntology entailmentOntology = null;
		try
		{
			verbose("Loading entailment file: ");
			verbose(entailmentFile);
			final IRI entailmentFileURI = IRI.create(FileUtils.toURI(entailmentFile));
			entailmentOntology = loader.getManager().loadOntology(entailmentFileURI);
		}
		catch (final Exception e)
		{
			throw new PelletCmdException(e);
		}

		final EntailmentChecker checker = new EntailmentChecker(reasoner);
		final Set<OWLLogicalAxiom> axioms = entailmentOntology.logicalAxioms().collect(Collectors.toSet());

		verbose("Check entailments for (" + axioms.size() + ") axioms");
		startTask("Checking");
		final Set<OWLAxiom> nonEntailments = checker.findNonEntailments(axioms, findAll);
		finishTask("Checking");

		if (nonEntailments.isEmpty())
			output("All axioms are entailed.");
		else
		{
			output("Non-entailments (" + nonEntailments.size() + "): ");

			int index = 1;
			final TextBlockWriter writer = new TextBlockWriter(new PrintWriter(System.out));
			final ManchesterSyntaxObjectRenderer renderer = new ManchesterSyntaxObjectRenderer(writer);
			writer.println();
			for (final OWLAxiom axiom : nonEntailments)
			{
				writer.print(index++);
				writer.print(")");
				writer.printSpace();

				writer.startBlock();
				axiom.accept(renderer);
				writer.endBlock();
				writer.println();
			}
			writer.flush();
		}
	}

}

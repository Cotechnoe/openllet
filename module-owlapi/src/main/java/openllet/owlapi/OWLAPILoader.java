// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public
// License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of
// proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package openllet.owlapi;

import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asSet;

import java.util.Set;
import java.util.stream.Stream;
import openllet.core.KBLoader;
import openllet.core.KnowledgeBase;
import openllet.core.OpenlletOptions;
import openllet.owlapi.facet.FacetManagerOWL;
import openllet.owlapi.facet.FacetOntologyOWL;
import openllet.owlapi.facet.FacetReasonerOWL;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportEvent;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.MissingImportListener;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 *
 * @author Evren Sirin
 */
public class OWLAPILoader extends KBLoader implements FacetReasonerOWL, FacetManagerOWL, FacetOntologyOWL
{
	private final OWLOntologyManager _manager;

	private final LimitedMapIRIMapper _iriMapper;

	private OpenlletReasoner _reasoner;

	private OWLOntology _baseOntology;

	private boolean _ignoreImports;

	/**
	 * A workaround for OWLAPI bug that does not let us import a loaded ontology so that we can minimize the warnings printed when
	 * OWLOntologyManager.makeLoadImportRequest is called
	 */
	private boolean _loadSingleFile;

	@Override
	public OWLOntologyManager getManager()
	{
		return _manager;
	}

	/**
	 * Returns the reasoner created by this loader. A <code>null</code> value is returned until {@link #load()} function is called (explicitly or implicitly).
	 *
	 * @return the reasoner created by this loader
	 */
	@Override
	public OpenlletReasoner getReasoner()
	{
		return _reasoner;
	}

	@Override
	public OWLOntology getOntology()
	{
		return _baseOntology;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public KnowledgeBase getKB()
	{
		return _reasoner.getKB();
	}

	public OWLAPILoader()
	{
		_iriMapper = new LimitedMapIRIMapper();
		_manager = OWLManager.createOWLOntologyManager();

		_manager.setOntologyLoaderConfiguration(_manager.getOntologyLoaderConfiguration().setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
		_manager.addMissingImportListener(new MissingImportListener()
		{
			/**
			 * TODO
			 *
			 * @since
			 */
			private static final long serialVersionUID = -1580704502184270618L;

			@Override
			public void importMissing(final MissingImportEvent event)
			{
				if (!_ignoreImports)
				{
					final IRI importURI = event.getImportedOntologyURI();
					System.err.println("WARNING: Cannot import " + importURI);
					event.getCreationException().printStackTrace();
				}
			}
		});

		clear();
	}

	/**
	 * @Deprecated 2.5.1 use the stream version
	 */
	@Deprecated
	public Set<OWLOntology> getAllOntologies()
	{
		return asSet(_manager.ontologies());
	}

	public Stream<OWLOntology> allOntologies()
	{
		return _manager.ontologies();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load()
	{
		_reasoner = new OpenlletReasonerFactory().createReasoner(_baseOntology);
		_reasoner.getKB().setTaxonomyBuilderProgressMonitor(OpenlletOptions.USE_CLASSIFICATION_MONITOR.create());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void parse(final String... fileNames)
	{
		// note if we will load a single file
		_loadSingleFile = fileNames.length == 1;

		super.parse(fileNames);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void parseFile(final String file)
	{
		try
		{
			final IRI fileIRI = IRI.create(file);
			_iriMapper.addAllowedIRI(fileIRI);

			if (_loadSingleFile)
				// we are loading a single file so we can load it directly
				_baseOntology = _manager.loadOntologyFromOntologyDocument(fileIRI);
			else
			{
				// loading multiple files so each input file should be added as
				// an import to the base ontology we created
				final OWLOntology importOnt = _manager.loadOntologyFromOntologyDocument(fileIRI);
				final OWLImportsDeclaration declaration = _manager.getOWLDataFactory().getOWLImportsDeclaration(importOnt.getOntologyID().getOntologyIRI().get());
				_manager.applyChange(new AddImport(_baseOntology, declaration));
			}
		}
		catch (final IllegalArgumentException e)
		{
			throw new RuntimeException(file, e);
		}
		catch (final OWLOntologyCreationException e)
		{
			throw new RuntimeException(file, e);
		}
		catch (final OWLOntologyChangeException e)
		{
			throw new RuntimeException(file, e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIgnoreImports(final boolean ignoreImports)
	{
		_ignoreImports = ignoreImports;
		_manager.getIRIMappers().clear();
		if (ignoreImports)
			_manager.getIRIMappers().add(_iriMapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear()
	{

		_iriMapper.clear();
		_manager.ontologies().forEach(_manager::removeOntology);

		try
		{
			_baseOntology = _manager.createOntology();
		}
		catch (final OWLOntologyCreationException e)
		{
			throw new RuntimeException(e);
		}
	}

}

// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package openllet.core.el;

import openllet.aterm.ATermAppl;
import openllet.core.taxonomy.SubsumptionComparator;
import openllet.core.utils.ATermUtils;
import openllet.core.utils.MultiValueMap;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Clark & Parsia, LLC. <http://www.clarkparsia.com>
 * </p>
 *
 * @author Evren Sirin
 */
public class CachedSubsumptionComparator extends SubsumptionComparator
{
	private final MultiValueMap<ATermAppl, ATermAppl> _subsumers;

	public CachedSubsumptionComparator(final MultiValueMap<ATermAppl, ATermAppl> subsumers)
	{
		super(null);
		this._subsumers = subsumers;
	}

	@Override
	public boolean isSubsumedBy(final ATermAppl a, final ATermAppl b)
	{
		return a == ATermUtils.BOTTOM || b == ATermUtils.TOP || a.equals(b) || _subsumers.contains(a, b);
	}

}

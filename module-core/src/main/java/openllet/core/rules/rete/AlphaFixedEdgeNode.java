// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package openllet.core.rules.rete;

import openllet.aterm.ATermAppl;
import openllet.core.boxes.abox.ABox;
import openllet.core.boxes.abox.Node;
import openllet.core.boxes.rbox.Role;

public class AlphaFixedEdgeNode extends AlphaEdgeNode
{
	protected final ATermAppl name;
	private Node node;

	public AlphaFixedEdgeNode(final ABox abox, final Role role, final ATermAppl subjectName)
	{
		super(abox, role);
		name = subjectName;
	}

	@SuppressWarnings("unchecked")
	protected <N extends Node> N initNode()
	{
		if (node == null)
			node = initNode(name);
		assert node != null;
		return (N) node;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		result = prime * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		final AlphaFixedEdgeNode other = (AlphaFixedEdgeNode) obj;
		if (getClass() != other.getClass())
			return false;
		return _role.equals(other._role);
	}
}

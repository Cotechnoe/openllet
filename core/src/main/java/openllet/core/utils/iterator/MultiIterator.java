// Portions Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// Clark & Parsia, LLC parts of this source code are available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package openllet.core.utils.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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
public class MultiIterator<T> implements Iterator<T>
{
	private final List<Iterator<? extends T>> list = new ArrayList<>(2);

	private int index = 0;

	private Iterator<? extends T> curr;

	public MultiIterator(final Iterator<? extends T> first)
	{
		curr = first;
	}

	public MultiIterator(final Iterator<? extends T> first, final Iterator<? extends T> second)
	{
		curr = first;
		list.add(second);
	}

	@Override
	public boolean hasNext()
	{
		while (!curr.hasNext() && index < list.size())
			curr = list.get(index++);

		return curr.hasNext();
	}

	@Override
	public T next()
	{
		if (!hasNext())
			throw new NoSuchElementException("multi iterator");

		return curr.next();
	}

	public void append(final Iterator<? extends T> other)
	{
		if (other.hasNext())
			if (other instanceof MultiIterator)
				list.addAll(((MultiIterator<? extends T>) other).list);
			else
				list.add(other);
	}

	@Override
	public void remove()
	{
		curr.remove();
	}
}

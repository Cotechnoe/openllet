/*
 * Copyright (c) 2002-2007, CWI and INRIA
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the University of California, Berkeley nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package openllet.aterm.pure;

import java.util.List;

import openllet.aterm.AFun;
import openllet.aterm.ATerm;
import openllet.aterm.ATermAppl;
import openllet.aterm.ATermInt;
import openllet.aterm.ATermPlaceholder;
import openllet.aterm.Visitor;
import openllet.shared.hash.HashFunctions;
import openllet.shared.hash.SharedObject;

public class ATermIntImpl extends ATermImpl implements ATermInt
{
	private int _value;

	/**
	 * depricated Use the new constructor instead.
	 *
	 * @param factory x
	 */
	@Deprecated
	protected ATermIntImpl(final PureFactory factory)
	{
		super(factory);
	}

	protected ATermIntImpl(final PureFactory factory, final int value)
	{
		super(factory);

		_value = value;

		setHashCode(hashFunction());
	}

	@Override
	public int getType()
	{
		return ATerm.INT;
	}

	/**
	 * depricated Use the new constructor instead.
	 *
	 * @param hashCode x
	 * @param annos x
	 * @param _value x
	 */
	@Deprecated
	protected void init(final int hashCode, final int value)
	{
		super.init(hashCode);
		_value = value;
	}

	/**
	 * depricated Use the new constructor instead.
	 *
	 * @param annos x
	 * @param _value x
	 */
	protected void initHashCode(final int value)
	{
		_value = value;
		setHashCode(hashFunction());
		//super.init(hashCode, annos);
	}

	@Override
	public SharedObject duplicate()
	{
		return this;
	}

	@Override
	public boolean equivalent(final SharedObject obj)
	{
		if (obj instanceof ATermInt)
		{
			final ATermInt peer = (ATermInt) obj;
			if (peer.getType() != getType())
				return false;

			return peer.getInt() == _value;
		}

		return false;
	}

	@Override
	protected boolean match(final ATerm pattern, final List<Object> list)
	{
		if (equals(pattern))
			return true;

		if (pattern.getType() == ATerm.PLACEHOLDER)
		{
			final ATerm type = ((ATermPlaceholder) pattern).getPlaceholder();
			if (type.getType() == ATerm.APPL)
			{
				final ATermAppl appl = (ATermAppl) type;
				final AFun afun = appl.getAFun();
				if ("int".equals(afun.getName()) && afun.getArity() == 0 && !afun.isQuoted())
				{
					list.add(_value);
					return true;
				}
			}
		}

		return super.match(pattern, list);
	}

	@Override
	public int getInt()
	{
		return _value;
	}

	@Override
	public ATerm accept(final Visitor<ATerm> v)
	{
		return v.visitInt(this);
	}

	private int hashFunction()
	{
		final int alpha = GOLDEN_RATIO + _value;
		return HashFunctions.mix(alpha, GOLDEN_RATIO, 2);
	}
}

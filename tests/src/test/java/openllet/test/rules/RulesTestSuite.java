// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package openllet.test.rules;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;

@RunWith(Suite.class)
@SuiteClasses({ BindingGeneratorsTest.class, BuiltInTests.class, MiscRuleTests.class, SWRLTestSuite.class, SWRLBuiltIns.class })
public class RulesTestSuite
{
	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(RulesTestSuite.class);
	}
}

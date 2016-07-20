package openllet.core.rules.rete;

public class RetePrinter
{
	public void print(final AlphaNetwork network)
	{
		for (final AlphaNode node : network)
			System.out.println(node);
	}
}

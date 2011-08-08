package seamshop.web.tag;

/**
 * Panel at the bottom of form to contain form controls.
 *
 * @author Alex Siman 2009-10-21
 */
// TODO: Low: Rename tag to: 'formControls'? (mb)
// TODO: Low: Impl: 'position' attribute w/ values 'top' & 'bottom'.
public class FormControlPanelTag extends AbstractSimpleTag
{
	// Tag Attributes

	@Override
	public void print() throws Exception
	{
		HtmlBuilder htmlBuilder = new HtmlBuilder(getJspWriter())
		.startTag("div", classAttribute("wwgrp FormControlPanel"))
			.startTag("table", classAttribute("HiddenTable"))
				.startTag("tr")
					.startAndEndTag("td", classAttribute("wwlbl"))
					.startTag("td", classAttribute("wwctrl"))
					.flush();

						printJspBodyIfExists();

					htmlBuilder
					.endTag("td")
				.endTag("tr")
			.endTag("table")
		.endTag("div")
		.flush();
	}
}

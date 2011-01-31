package cmfaur.client.crud.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RichTextArea;

public class FormattedRichTextArea extends RichTextArea {
	private static final String CSS = "body { font-family: helvetica,arial,sans-serif; font-size: 12px } .video-embed { background-color: #e0e0e0; padding: 93px 5px; border: 1px solid black; width: 363px; height: 23px; display: block; user-select: none; text-align: center; font-weight: bold } .video-embed:before { content: \"VIDEO\" }";

	@Override
	protected void onAttach() {
		super.onAttach();

		Timer cssTimer = new Timer() {
			@Override
			public void run() {
				Document doc = IFrameElement.as(getElement()).getContentDocument();
				StyleElement style = doc.createStyleElement();
				style.setInnerText(CSS);
				HeadElement.as(Element.as(doc.getBody().getPreviousSibling())).appendChild(style);
			}
		};

		cssTimer.schedule(100);
	}
}

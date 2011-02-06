package cmfaur.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvcaur.Controller;
import mvcaur.Renderer;

public class MessagesRenderer implements Renderer {

	@Override
	public void render(Object obj, Controller<?> ctrl,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/javascript");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.println("var CmfaurMessages = {");
		
		@SuppressWarnings("unchecked")
		Map<String, String> res = (Map<String, String>) obj;
		for (Entry<String, String> entry : res.entrySet()) {
			writer.println("\"" + entry.getKey() + "\" : \"" + entry.getValue() +  "\",");
		}
		writer.println("\"_dummy\" : \"dummy\""); //prevents syntax error
		writer.println("};");
	}

}

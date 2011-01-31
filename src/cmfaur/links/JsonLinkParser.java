package cmfaur.links;

import java.util.ArrayList;
import java.util.List;

import siena.Model;
import cmfaur.util.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Parser that knows how to deal with json {@link Link} objects
 * 
 * @author henper
 * 
 */
public class JsonLinkParser {

	/**
	 * This method will parse a json string to a {@link Link}. If the json is
	 * malformed or invalid, a {@link JsonParseException} will be thrown. If you
	 * need to be failsafe, use parseFailSafe(json) instead.
	 * 
	 * @param json
	 * @return
	 * @throws JsonParseException
	 */
	public Link parse(String json) throws JsonParseException {
		Gson gson = new Gson();
		LinkJsonOverlay overlay = gson.fromJson(json, LinkJsonOverlay.class);
		Link link = new Link();
		link.setText(overlay.linkText);
		if (overlay.absoluteUrl != null) {
			link.setUrl(overlay.absoluteUrl);
			return link;
		}
		Class<?> clazz;
		try {
			clazz = Class.forName(overlay.typeClassName);
		} catch (Exception e) {
			throw new RuntimeException("No such class: "
					+ overlay.typeClassName, e);
		}
		Linkable linkable = (Linkable) Model.all(clazz).filter("id",
				overlay.typeId).get();
		link.setUrl(linkable.getUrl());
		return link;
	}

	/**
	 * Same as parse() but will not throw an exception if the json is invalid.
	 * In such cases, an error message is logged and <code>null</code> is
	 * returned.
	 * 
	 * @param json
	 * @return
	 */
	public Link parseFailSafe(String json) {		
		try {
			return parse(json);
		} catch (JsonParseException e) {
			Logger.error("Json could not be parsed: " + json);
			return null;
		}
	}

	/**
	 * Parses a list of strings to {@link Link}s. This method is not fail safe
	 * and will throw an exception if the json can't be parsed. Use
	 * parseFailSafe() if you need to be fail safe.
	 * 
	 * @param jsons
	 * @return
	 */
	public List<Link> parse(List<String> jsons) throws JsonParseException {
		List<Link> result = new ArrayList<Link>(jsons.size());
		for (String json : jsons) {
			result.add(parse(json));
		}
		return result;
	}

	/**
	 * Parses a list of strings to {@link Link}s. This method is fail safe and
	 * will only add those links that can be parsed to the resulting list.
	 * 
	 * @param jsons
	 * @return
	 */
	public List<Link> parseFailSafe(List<String> jsons) {
		List<Link> result = new ArrayList<Link>(jsons.size());
		for (String json : jsons) {
			Link failSafe = parseFailSafe(json);
			if (failSafe != null) {
				result.add(failSafe);
			}
		}
		return result;
	}

}

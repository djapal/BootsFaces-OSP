/**
 *  Copyright 2014-15 by Riccardo Massera (TheCoder4.Eu) and Stephan Rauh (http://www.beyondjava.net).
 *  
 *  This file is part of BootsFaces.
 *  
 *  BootsFaces is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BootsFaces is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with BootsFaces. If not, see <http://www.gnu.org/licenses/>.
 */

package net.bootsfaces.component.slider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import net.bootsfaces.C;
import net.bootsfaces.component.badge.BadgeRenderer;
import net.bootsfaces.render.A;
import net.bootsfaces.render.JQ;
import net.bootsfaces.render.R;
import net.bootsfaces.render.Tooltip;

/** This class generates the HTML code of &lt;b:slider /&gt;. */
@FacesRenderer(componentFamily = "net.bootsfaces.component", rendererType = "net.bootsfaces.component.slider.Slider")
public class SliderRenderer extends BadgeRenderer {
	/**
	 * This methods receives and processes input made by the user. More
	 * specifically, it ckecks whether the user has interacted with the current
	 * b:slider. The default implementation simply stores the input value in the
	 * list of submitted values. If the validation checks are passed, the values
	 * in the <code>submittedValues</code> list are store in the backend bean.
	 * 
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:slider.
	 */
	@Override
	public void decode(FacesContext context, UIComponent component) {
		Slider slider = (Slider) component;

		if (slider.isDisabled() || slider.isReadonly()) {
			return;
		}

		decodeBehaviors(context, slider);

		String clientId = slider.getClientId(context);
		String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(clientId);

		if (submittedValue != null) {
			slider.setSubmittedValue(submittedValue);
			slider.setValid(true);
		}
	}

	/**
	 * This methods generates the HTML code of the current b:slider.
	 * 
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:slider.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		Slider slider = (Slider) component;
		ResponseWriter rw = context.getResponseWriter();
		encodeHTML(slider, context, rw);

		Tooltip.activateTooltips(context, slider);

	}

	private void encodeHTML(Slider slider, FacesContext context, ResponseWriter rw) throws IOException {
		String clientId = slider.getClientId(context);

		String mode = slider.getMode();
		String label = slider.getLabel();

		int min = slider.getMin();
		int max = slider.getMax();
		Object v = slider.getSubmittedValue();
		if (v == null) {
			v = slider.getValue();
		}
		if (v == null) {
			v = slider.getValue();
			slider.setValue(v);
		}
		if (v == null) {
			v = max / 2;
			slider.setValue(v);
		}
		int val = A.toInt(v);
		if (val > max) {
			val = max;
		}
		if (val < min) {
			val = min;
		}
		String o;
		if (slider.getOrientation() != null) {
			o = slider.getOrientation();
		} else {
			o = C.H;
		}
		boolean isVertical = o.startsWith("vertical");
		boolean bottom = o.endsWith("bottom");

		rw.startElement("div", null);// form-group
		rw.writeAttribute("id", clientId, "id");
		Tooltip.generateTooltip(context, slider, rw);
		
		rw.writeAttribute("class", "form-group", "class");
		rw.startElement("div", null);
		String s = "row " + (isVertical ? "slider-vertical" : "slider");
		rw.writeAttribute("class", s, "class");
		// -------------------------------------------------------------->
		// <<-- Vertical -->>
		if (isVertical) {
                    if (label != null && !bottom) {
                            rw.startElement("div", null);
                            rw.writeAttribute("class", "row " + getErrorAndRequiredClass(slider, clientId), "class");
                            encodeVLabel(slider, rw, label);
                            rw.endElement("div");/* Row */
                    }
                    rw.startElement("div", null);
                    rw.writeAttribute("class", "row", "class");
                    if (bottom) {
                            encodeSliderDiv(rw, isVertical, clientId);
                            rw.endElement("div");/* Row */
                            rw.startElement("div", null);
                            rw.writeAttribute("class", "row", "class");
                    }
                    encodeInput(slider, rw, mode, context, val, clientId, isVertical, min, max);
                    if (!bottom) {
                            rw.endElement("div"); /* Row */

                            rw.startElement("div", null);
                            rw.writeAttribute("class", "row "+ getErrorAndRequiredClass(slider, clientId), "class");
                            encodeSliderDiv(rw, isVertical, clientId);
                    }
                    rw.endElement("div"); /* Row */
                    if (label != null && bottom) {
                            rw.startElement("div", null);
                            rw.writeAttribute("class", "row " + getErrorAndRequiredClass(slider, clientId), "class");
                            encodeVLabel(slider, rw, label);
                            rw.endElement("div"); /* Row */
                    }

		} else {
                // <<-- Horizontal -->>

                    if (label != null) {
                            rw.startElement("div", null);
                            rw.writeAttribute("class", "row "+ getErrorAndRequiredClass(slider, clientId), "class");

                            R.encodeColumn(rw, null, 6, 6, 6, 6, 0, 0, 0, 0, null, null);
                            rw.startElement("label", slider);
                            rw.writeAttribute("for", clientId, null);
                            rw.write(label);
                            rw.endElement("label"); // Label

                            rw.endElement("div");// Column

                            rw.endElement("div");/* Row */
                    }
                    rw.startElement("div", null);
                    rw.writeAttribute("class", "row", "class");

                    encodeInput(slider, rw, mode, context, val, clientId, isVertical, min, max);

                    encodeSliderDiv(rw, isVertical, clientId);
                    rw.endElement("div");/* Row */

		}

		// <<---------------------------------------
		rw.endElement("div"); // rw.write("<!-- Slider Widget Row
								// -->\n");//Slider Widget Row

		rw.endElement("div"); // rw.write("<!-- form-group -->\n");//form-group

		encodeJS(slider, rw, clientId);
		Tooltip.activateTooltips(context, slider);
	}

	private void encodeVLabel(Slider slider, ResponseWriter rw, String label) throws IOException {
		R.encodeColumn(rw, null, 12, 12, 12, 12, 0, 0, 0, 0, null, null);
		rw.startElement("p", slider);
		rw.write(label);
		rw.endElement("p"); // Label
		rw.endElement("div"); // Column
	}

	private void encodeInput(Slider slider, ResponseWriter rw, String mode, FacesContext context, int val,
			String clientId, boolean vo, int min, int max) throws IOException {
		int cols = (vo ? 12 : 1);
		if (!mode.equals("basic")) {
			/*
			 * int span, int offset, int cxs, int csm, int clg, int oxs, int
			 * osm, int olg
			 */
			R.encodeColumn(rw, null, cols, cols, cols, cols, 0, 0, 0, 0, null, null);
			if (mode.equals("badge")) {
				generateBadge(context, slider, rw, clientId, slider.getStyleClass(), slider.getStyle(),
						Integer.toString(val), "_badge");
			}
		}
		removeMisleadingType(slider);
		// Input
		rw.startElement("input", slider);
//		rw.writeAttribute("id", clientId, null);
		rw.writeAttribute("name", clientId, null);
		rw.writeAttribute("type", (mode.equals("edit") ? "text" : "hidden"), null);
		rw.writeAttribute("size", String.valueOf(max).length() - 1, null);
		rw.writeAttribute("min", min, null);
		rw.writeAttribute("max", max, null);
		rw.writeAttribute("maxlength", String.valueOf(max).length(), null);
		if (slider.isDisabled()) {
			rw.writeAttribute("disabled", "disabled", null);
		}
		if (slider.isReadonly()) {
			rw.writeAttribute("readonly", "readonly", null);
		}

		rw.writeAttribute("class", "form-control input-sm" + (vo ? " text-center" : ""), "class");

		rw.writeAttribute("value", val, null);

		rw.endElement("input");

		if (!mode.equals("basic")) {
			rw.endElement("div");
		} // Column
	}

	/**
	 * remove wrong type information that may have been added by AngularFaces
	 */
	@SuppressWarnings("rawtypes")
	private void removeMisleadingType(Slider slider) {
		try {
			Method method = getClass().getMethod("getPassThroughAttributes", (Class[]) null);
			if (null != method) {
				Object map = method.invoke(this, (Object[]) null);
				if (null != map) {
					Map attributes = (Map) map;
					if (attributes.containsKey("type"))
						attributes.remove("type");
				}
			}
		} catch (ReflectiveOperationException ignoreMe) {
			// we don't really have to care about this error
		}

	}

	private void encodeSliderDiv(ResponseWriter rw, boolean vo, String clientId) throws IOException {
		/*
		 * int span, int offset, int cxs, int csm, int clg, int oxs, int osm,
		 * int olg
		 */
		R.encodeColumn(rw, null, (vo ? 12 : 4), (vo ? 12 : 4), (vo ? 12 : 4), (vo ? 12 : 4), 0, 0, 0, 0, null, null);
		// Slider <div>
		rw.startElement("div", null);
		rw.writeAttribute("id", clientId + "_slider", null);// concat
															// controproducente
		rw.endElement("div");
		rw.endElement("div"); // Column
	}

	private void encodeJS(Slider slider, ResponseWriter rw, String cId) throws IOException {
		StringBuilder sb = new StringBuilder(100);
		sb.append("value").append(":").append(slider.getValue()).append(",");
		if (slider.isDisabled() || slider.isReadonly()) {
			sb.append("disabled: true,");
		}
		if (slider.getMax() > 0) {
			sb.append("max").append(":").append(slider.getMax()).append(",");
		}
		sb.append("min").append(":").append(slider.getMin()).append(",");
		if (slider.getOrientation() != null) {
			String o = slider.getOrientation();
			if (o.endsWith("bottom")) {
				o = "vertical";
			}
			sb.append("orientation").append(":").append("'".concat(o).concat("'")).append(",");
		}
		if (slider.getStep() > 0) {
			sb.append("step").append(":").append(slider.getStep()).append(",");
		}
		sb.append("range").append(":").append("\"min\"").append(",");

		Map<String, Object> attributes = slider.getAttributes();
		String hsize = A.asString(attributes.get("handle-size"));
		String hshape = A.asString(attributes.get("handle-shape"));
		boolean hround = ((hshape != null) && (hshape.equals("round")));

		JQ.simpleSlider(rw, cId, sb.toString(), slider.getMode().equals("badge"), hsize, hround);
	}
}

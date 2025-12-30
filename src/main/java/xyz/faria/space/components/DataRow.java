package xyz.faria.space.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a row of data with a label and content.
 */
public class DataRow extends Div {

    public DataRow(String label, String content) {
        var labelDiv = new Div(label);
        labelDiv.getStyle().set("font-weight", "bold").set("display", "inline-block");
        var contentDiv = new Div(content);
        contentDiv.getStyle().set("display", "inline-block");
        add(labelDiv, new Text(": "), contentDiv);
    }

    public DataRow(String label, OffsetDateTime content) {
        var formattedContent = content.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this(label, formattedContent);
    }

    public DataRow(String label, Object content) {
        this(label, content.toString());
    }
}

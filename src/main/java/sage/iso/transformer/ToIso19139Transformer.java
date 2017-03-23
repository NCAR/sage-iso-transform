package sage.iso.transformer;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ToIso19139Transformer {

    private Transformer transformer;

    public ToIso19139Transformer(Transformer transformer) {

        this.transformer = transformer;
    }

    public void transform(StreamSource sourceXml, StreamResult resultXml) throws Exception {

        this.transformer.transform(sourceXml, resultXml);
    }
}


package ch.iec._61400.ews._1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tSource.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tSource">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="process"/>
 *     &lt;enumeration value="substituted"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tSource")
@XmlEnum
public enum TSource {

    @XmlEnumValue("process")
    PROCESS("process"),
    @XmlEnumValue("substituted")
    SUBSTITUTED("substituted");
    private final String value;

    TSource(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TSource fromValue(String v) {
        for (TSource c: TSource.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}

package absl.domain.enumeration;

/**
 * The Gender enumeration.
 */
public enum Gender {
    HOMBRE("H"),
    MUJER("M");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

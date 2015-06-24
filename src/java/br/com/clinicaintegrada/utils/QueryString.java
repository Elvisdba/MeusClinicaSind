package br.com.clinicaintegrada.utils;

public class QueryString {

    /**
     *
     * @param field
     * @param type - 0 = Total; 1 = Inicial; 2 = Parcial; 3 Final;
     * @return
     */
    public static String typeSearch(String field, int type) {
        switch (type) {
            case 0:
                field = " = UPPER(FUNC_TRANSLATE('" + field + "'))";
                break;
            case 1:
                field = " LIKE UPPER(FUNC_TRANSLATE('" + field + "%'))";
                break;
            case 2:
                field = " LIKE UPPER(FUNC_TRANSLATE('%" + field + "%'))";
                break;
            case 3:
                field = " LIKE UPPER(FUNC_TRANSLATE('%" + field + "'))";
                break;
        }
        return field;
    }

}

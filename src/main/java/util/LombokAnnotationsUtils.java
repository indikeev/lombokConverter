package util;

import com.intellij.psi.util.PsiUtil;
import lombok.experimental.UtilityClass;

/**
 * Created by 1 on 12.05.2018.
 */
@UtilityClass
public class LombokAnnotationsUtils {
    private static final String LOMBOK_PROTECTED_ACCESS = "lombok.AccessLevel.PROTECTED";
    private static final String LOMBOK_PUBLIC_ACCESS = "";
    private static final String LOMBOK_PRIVATE_ACCESS = "lombok.AccessLevel.PRIVATE";
    private static final String LOMBOK_PACKAGE_ACCESS = "lombok.AccessLevel.PACKAGE";

    public static final String LOMBOK_GETTER = "lombok.Getter";
    public static final String LOMBOK_NO_ARGS_CONSTRUCTOR = "lombok.NoArgsConstructor";
    private static final String LOMBOK_SETTER = "lombok.Setter";


    private static String createSingleAttribute(String attribute) {
        return "(" + attribute + ")";
    }

    private static String getSimpleAccessAttribute(int accessLevel) {
        switch (accessLevel) {
            case PsiUtil.ACCESS_LEVEL_PRIVATE:
                return createSingleAttribute(LOMBOK_PRIVATE_ACCESS);
            case PsiUtil.ACCESS_LEVEL_PACKAGE_LOCAL:
                return createSingleAttribute(LOMBOK_PACKAGE_ACCESS);
            case PsiUtil.ACCESS_LEVEL_PROTECTED:
                return createSingleAttribute(LOMBOK_PROTECTED_ACCESS);
            default:
                return LOMBOK_PUBLIC_ACCESS;
        }
    }

    public static String getGetterAnnotation(int accessLevel) {
        return LOMBOK_GETTER + getSimpleAccessAttribute(accessLevel);
    }

    public static String getSetterAnnotation(int accessLevel) {
        return LOMBOK_SETTER + getSimpleAccessAttribute(accessLevel);
    }

    public static String getNoArgConstructorAnnotation(int accessLevel) {
        return LOMBOK_NO_ARGS_CONSTRUCTOR + getAccessAttribute(accessLevel);
    }

    private static String getAccessAttribute(int accessLevel) {

        switch (accessLevel) {
            case PsiUtil.ACCESS_LEVEL_PRIVATE:
                return "(access = " + LOMBOK_PRIVATE_ACCESS + ")";
            case PsiUtil.ACCESS_LEVEL_PACKAGE_LOCAL:
                return "(access = " + LOMBOK_PACKAGE_ACCESS + ")";
            case PsiUtil.ACCESS_LEVEL_PROTECTED:
                return "(access = " + LOMBOK_PROTECTED_ACCESS + ")";
            default:
                return LOMBOK_PUBLIC_ACCESS;
        }
    }
}

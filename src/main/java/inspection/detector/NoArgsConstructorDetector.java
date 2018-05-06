package inspection.detector;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import util.LombokAnnotationsUtils;

/**
 * Created by 1 on 12.05.2018.
 */
public class NoArgsConstructorDetector {
    final PsiMethod psiMethod;

    private boolean isSimpleConstructor;

    public boolean isNoArgsConstructor() {
        return isSimpleConstructor;
    }

    public NoArgsConstructorDetector(PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
        init();
    }

    private void init() {
        if (!psiMethod.isConstructor())
            return;

        if (psiMethod.getParameterList().getParameters().length != 0)
            return;

        if (psiMethod.getModifierList().getAnnotations().length != 0) {
            return;
        }

        final PsiCodeBlock body = psiMethod.getBody();
        if (body == null)
            return;

        final PsiClass psiClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass.class);
        if (psiClass == null)
            return;

        if (containsNotInitializedFinalFields(psiClass))
            return;

        final PsiStatement[] statements = body.getStatements();
        if (statements.length != 0)
            return;

        if (isClassHasNoArgsConstructorAnnotation(psiClass))
            return;

        isSimpleConstructor = true;
    }

    private boolean isClassHasNoArgsConstructorAnnotation(PsiClass psiClass) {
        final PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList != null) {
            final PsiAnnotation annotation =
                    modifierList.findAnnotation(LombokAnnotationsUtils.LOMBOK_NO_ARGS_CONSTRUCTOR);
            return annotation != null;
        }
        return false;
    }

    private boolean containsNotInitializedFinalFields(@NotNull PsiClass psiClass) {
        for (PsiField psiField : psiClass.getFields()) {
            final PsiModifierList modifierList = psiField.getModifierList();
            if (modifierList == null)
                continue;
            if (!modifierList.hasModifierProperty(PsiModifier.FINAL))
                continue;

            final PsiExpression initializer = psiField.getInitializer();
            if (initializer != null)
                continue;

            return true;
        }
        return false;
    }
}

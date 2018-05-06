package inspection.detector.logger;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class Log4jLoggerDetector {
    private static final Logger log = Logger.getLogger(PsiClass.class.getName());
    private boolean isLogger;

    private final String canonicalLoggerClassName = "org.apache.log4j.Logger";

    public Log4jLoggerDetector(@NotNull PsiField field) {
        final PsiType type = field.getType();
        if (!type.getCanonicalText().equals(canonicalLoggerClassName))
            return;
        final PsiModifierList modifierList = field.getModifierList();
        if (modifierList == null)
            return;

        if (modifierList.getAnnotations().length != 0)
            return;

        if (!modifierList.hasModifierProperty(PsiModifier.FINAL))
            return;

        if (!modifierList.hasModifierProperty(PsiModifier.STATIC))
            return;

        if (!field.getNameIdentifier().getText().equals("log"))
            return;

        final PsiExpression initializer = field.getInitializer();
        if (!(initializer instanceof PsiMethodCallExpression))
            return;

        final PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) initializer;

        final PsiExpressionList argumentList = methodCallExpression.getArgumentList();
        if (argumentList.getExpressionCount() != 1)
            return;
        final PsiExpression psiExpression = argumentList.getExpressions()[0];

        final PsiReferenceExpression methodExpression = methodCallExpression.getMethodExpression();


        final PsiElement resolve = methodExpression.resolve();
        if (!(resolve instanceof PsiMethod))
            return;
        final PsiMethod method = (PsiMethod) resolve;
        if (!method.getName().equals("getLogger"))
            return;
        final PsiClass psiClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        if (psiClass == null)
            return;
        if (psiClass.getQualifiedName() == null)
            return;

        if (!psiClass.getQualifiedName().equals(canonicalLoggerClassName))
            return;

        isLogger = true;
    }

    public boolean isLogger() {
        return isLogger;
    }
}

package inspection.detector;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.LombokAnnotationsUtils;

/**
 * Created by 1 on 11.05.2018.
 */
public class GetterDetector {
    private boolean isGetterMethod = false;
    protected boolean isRenameEnabled = true; // TODO move out to settings

    PsiMethod psiMethod;
    PsiField resolvedField;


    public GetterDetector(@NotNull PsiMethod psiMethod) {
        this.psiMethod = psiMethod;
        init();
    }

    private void init() {
        PsiCodeBlock body = psiMethod.getBody();
        if (body == null)
            return;

        if (psiMethod.getParameterList().getParametersCount() != 0)
            return;

        PsiStatement[] statements = body.getStatements();
        if (statements.length != 1)
            return;

        PsiStatement statement = statements[0];
        if (!(statement instanceof PsiReturnStatement))
            return;

        PsiReturnStatement psiReturnStatement = (PsiReturnStatement) statement;
        PsiExpression psiExpression = psiReturnStatement.getReturnValue();
        if (psiExpression == null)
            return;

        PsiReferenceExpression psiReferenceExpression
                = tryGetPsiReferenceExpression(psiExpression);
        if (psiReferenceExpression == null)
            return;

        PsiElement resolveElement = psiReferenceExpression.resolve();
        if (!(resolveElement instanceof PsiField))
            return;

        resolvedField = (PsiField) resolveElement;
        if (!resolvedField.getType().equals(psiMethod.getReturnType()))
            return;

        PsiClass parentOfResolvedField = PsiTreeUtil.getParentOfType(resolvedField, PsiClass.class);
        PsiClass parentOfReferenceElement = PsiTreeUtil.getParentOfType(psiReferenceExpression, PsiClass.class);

        if (parentOfReferenceElement != parentOfResolvedField)
            return;

        final String getterName = getGetterName(resolvedField);
        final String realGetterName = psiMethod.getName();
        if (!getterName.equals(realGetterName) && !isRenameEnabled)
            return;

        if (isFieldAlreadyHasGetterAnnotation(resolvedField))
            return;

        isGetterMethod = true;
    }

    private boolean isFieldAlreadyHasGetterAnnotation(PsiField psiField) {
        final PsiModifierList modifierList = psiField.getModifierList();
        if (modifierList != null) {
            final PsiAnnotation annotation = modifierList.findAnnotation(LombokAnnotationsUtils.LOMBOK_GETTER);
            return annotation != null;
        }
        return false;
    }

    @Nullable
    private PsiReferenceExpression tryGetPsiReferenceExpression(@NotNull PsiExpression psiExpression) {
        while (true) {
            if (psiExpression instanceof PsiTypeCastExpression) {
                final PsiTypeCastExpression psiTypeCastExpression = (PsiTypeCastExpression) psiExpression;
                psiExpression = psiTypeCastExpression.getOperand();
            } else if (psiExpression instanceof PsiParenthesizedExpression) {
                final PsiParenthesizedExpression psiParenthesizedExpression = (PsiParenthesizedExpression) psiExpression;
                psiExpression = psiParenthesizedExpression.getExpression();
            } else if (psiExpression instanceof PsiReferenceExpression) {
                return ((PsiReferenceExpression) psiExpression);
            } else {
                return null;
            }
        }
    }

    @NotNull
    String getGetterName(PsiField psiField) {
        if (PsiType.BOOLEAN.equals(psiField.getType())) {
            return getBooleanGetterName(psiField.getName());
        } else {
            return getDefaultGetterName(psiField.getName());
        }
    }

    @NotNull
    private String getDefaultGetterName(String fieldName) {
        return "get" + StringUtils.capitalize(fieldName);
    }

    @NotNull
    private String getBooleanGetterName(String fieldName) {
        return "is" + StringUtils.capitalize(fieldName);
    }

    public boolean isGetter() {
        return isGetterMethod;
    }

}

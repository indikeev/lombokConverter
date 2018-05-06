package inspection.detector.setter;

import com.intellij.psi.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class SetterDetector {
    @Getter
    private boolean isSetter;
    protected final PsiMethod method;
    protected PsiField leftResolveField;

    public SetterDetector(@NotNull PsiMethod method) {
        this.method = method;
        init();
    }

    private void init() {
        if (!PsiType.VOID.equals(method.getReturnType()))
            return;
        final PsiModifierList modifierList = method.getModifierList();
        if (modifierList.getAnnotations().length != 0)
            return;

        if (method.getParameterList().getParametersCount() != 1)
            return;
        final PsiParameter parameter = method.getParameterList().getParameters()[0];
        if (parameter.getAnnotations().length != 0) {
            return;
        }
        final PsiCodeBlock methodBody = method.getBody();
        if (methodBody == null)
            return;
        if (methodBody.getStatementCount() != 1)
            return;

        final PsiStatement statement = methodBody.getStatements()[0];
        if (!(statement instanceof PsiExpressionStatement))
            return;

        final PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;
        final PsiExpression expression = expressionStatement.getExpression();

        if (!(expression instanceof PsiAssignmentExpression))
            return;
        final PsiAssignmentExpression assignmentExpression = (PsiAssignmentExpression) expression;
        final PsiExpression leftExpression = assignmentExpression.getLExpression();
        if (!(leftExpression instanceof PsiReferenceExpression))
            return;
        final PsiExpression rightExpression = assignmentExpression.getRExpression();
        if (!(rightExpression instanceof PsiReferenceExpression))
            return;
        final PsiReferenceExpression leftReferenceExpression = (PsiReferenceExpression) leftExpression;
        final PsiReferenceExpression rightReferenceExpression = (PsiReferenceExpression) rightExpression;

        final PsiElement rightResolveElement = rightReferenceExpression.resolve();
        if (!(rightResolveElement instanceof PsiParameter))
            return;
        final PsiParameter rightResolveParameter = (PsiParameter) rightResolveElement;

        //i think it always true??
        if (!rightResolveParameter.equals(parameter))
            return;
        final PsiElement leftResolveElement = leftReferenceExpression.resolve();
        if (!(leftResolveElement instanceof PsiField))
            return;

        leftResolveField = (PsiField) leftResolveElement;

        if (!leftResolveField.getType().equals(rightResolveParameter.getType()))
            return;

        isSetter = true;
    }

    public void setSetter(boolean setter) {
        isSetter = setter;
    }
}

package inspection.quickFixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import inspection.detector.setter.SetterConverter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class ConvertSetterToAnnotationFix implements LocalQuickFix {
    @Nls
    @NotNull
    @Override
    public String getName() {
        return "Convert setter to annotation";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Lombok converter";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
        PsiElement psiElement = descriptor.getPsiElement();
        assert psiElement instanceof PsiMethod; // TODO do we need it?
        PsiMethod psiMethod = (PsiMethod) psiElement;
        SetterConverter detector = new SetterConverter(project, psiMethod);
        if (detector.isSetter()) {
            detector.convert();
        }
    }
}
